package com.enation.app.javashop.core.member.service.impl;

import com.enation.app.javashop.core.base.CachePrefix;
import com.enation.app.javashop.core.member.MemberErrorCode;
import com.enation.app.javashop.core.member.model.dos.Member;
import com.enation.app.javashop.core.member.model.dos.MemberAddress;
import com.enation.app.javashop.core.member.service.MemberAddressManager;
import com.enation.app.javashop.core.member.service.MemberManager;
import com.enation.app.javashop.core.trade.order.support.CheckoutParamName;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.exception.NoPermissionException;
import com.enation.app.javashop.framework.exception.ResourceNotFoundException;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.security.model.Buyer;
import com.enation.app.javashop.framework.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 会员地址业务类
 *
 * @author zh
 * @version v2.0
 * @since v7.0.0
 * 2018-03-18 15:37:00
 */
@Service
public class MemberAddressManagerImpl implements MemberAddressManager {

    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport memberDaoSupport;
    @Autowired
    private MemberManager memberManager;
    @Autowired
    private Cache cache;

    @Override
    public List<MemberAddress> list() {
        Buyer buer = UserContext.getBuyer();
        String sql = "select * from es_member_address where member_id = ? order by addr_id desc";
        return this.memberDaoSupport.queryForList(sql, MemberAddress.class, buer.getUid());
    }


    @Override
    public Page list(int page, int pageSize, Integer memberId) {
        String sql = "select * from es_member_address  where member_id = ? ";
        Page webPage = this.memberDaoSupport.queryForPage(sql, page, pageSize, MemberAddress.class, memberId);
        return webPage;
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public MemberAddress add(MemberAddress memberAddress) {
        BeanUtil.copyProperties(memberAddress.getRegion(), memberAddress);
        Buyer buyer = UserContext.getBuyer();
        //对会员是否存在进行校验
        Member member = memberManager.getModel(buyer.getUid());
        if (member == null) {
            throw new ResourceNotFoundException("当前会员不存在");
        }
        //对会员最大地址进行校验
        String sql = "select count(*) from es_member_address where member_id = ?";
        Integer count = this.memberDaoSupport.queryForInt(sql, buyer.getUid());
        if (count == 20) {
            throw new ServiceException(MemberErrorCode.E100.code(), "会员地址已达20个上限，无法添加");
        }
        memberAddress.setMemberId(buyer.getUid());
        memberAddress.setCountry("中国");
        MemberAddress defAddr = this.getDefaultAddress(buyer.getUid());
        //默认地址的处理
        if (memberAddress.getDefAddr() > 1 || memberAddress.getDefAddr() < 0) {
            memberAddress.setDefAddr(0);
        }
        if (defAddr == null) {
            memberAddress.setDefAddr(1);
        } else {
            //不是第一个，且设置为默认地址了，则更新其它地址为非默认地址
            if (memberAddress.getDefAddr() == 1) {
                this.memberDaoSupport.execute("update es_member_address set def_addr = 0 where member_id = ?", buyer.getUid());
            }
        }
        this.memberDaoSupport.insert(memberAddress);
        int memberAddressId = this.memberDaoSupport.getLastId("es_member_address");
        memberAddress.setAddrId(memberAddressId);
        return memberAddress;
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public MemberAddress edit(MemberAddress memberAddress, Integer id) {
        Buyer buyer = UserContext.getBuyer();
        Member member = memberManager.getModel(buyer.getUid());
        if (member == null) {
            throw new ResourceNotFoundException("当前会员不存在");
        }
        //权限判断
        MemberAddress address = this.getModel(id);
        if (address == null || !Objects.equals(address.getMemberId(), buyer.getUid())) {
            throw new NoPermissionException("无权限操作此地址");
        }
        //如果要将默认地址修改为非默认地址
        if (address.getDefAddr() == 1 && memberAddress.getDefAddr() == 0) {
            throw new ServiceException(MemberErrorCode.E101.code(), "无法更改当前默认地址为非默认地址");
        }
        //如果要将非默认地址修改为默认
        if (address.getDefAddr() == 0 && memberAddress.getDefAddr() == 1) {
            this.memberDaoSupport.execute("update es_member_address set def_addr = 0 where member_id = ?", buyer.getUid());
        }
        BeanUtil.copyProperties(memberAddress.getRegion(), address);
        address.setDefAddr(memberAddress.getDefAddr());
        address.setName(memberAddress.getName());
        address.setAddr(memberAddress.getAddr());
        address.setMobile(memberAddress.getMobile());
        address.setTel(memberAddress.getTel());
        address.setShipAddressName(memberAddress.getShipAddressName());
        this.memberDaoSupport.update(address, id);
        return address;
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delete(Integer id) {
        Buyer buyer = UserContext.getBuyer();
        MemberAddress address = this.getModel(id);
        if (address == null || !address.getMemberId().equals(buyer.getUid())) {
            throw new NoPermissionException("无权限操作此地址");
        }
        //默认地址不能删除
        if(address.getDefAddr().equals(1)){
            throw new ServiceException(MemberErrorCode.E101.code(), "默认地址不能删除");
        }
        this.memberDaoSupport.delete(MemberAddress.class, id);
        //会员地址删除后，检测结算参数中的收货地址是否为该地址
        String key = CachePrefix.CHECKOUT_PARAM_ID_PREFIX.getPrefix() + buyer.getUid();
        Map<String, Object> map = cache.getHash(key);

        if(map == null){
            return;
        }
        //如果取到了，则取出来生成param
        Integer addressId = (Integer) map.get(CheckoutParamName.ADDRESS_ID);
        if(id.equals(addressId)){
            //查询默认的收货地址
            MemberAddress defaultAddress = this.getDefaultAddress(buyer.getUid());
            this.cache.putHash(key, CheckoutParamName.ADDRESS_ID, defaultAddress.getAddrId());
        }
    }

    @Override
    public MemberAddress getModel(Integer id) {
        return this.memberDaoSupport.queryForObject(MemberAddress.class, id);
    }

    @Override
    public MemberAddress getDefaultAddress(Integer memberId) {
        String sql = "select * from es_member_address where member_id=? and def_addr=1";
        List<MemberAddress> addressList = this.memberDaoSupport.queryForList(sql, MemberAddress.class, memberId);
        MemberAddress address = null;
        if (!addressList.isEmpty()) {
            address = addressList.get(0);
        }
        return address;
    }

    @Override
    public void editDefault(Integer id) {
        MemberAddress memberAddress = this.getModel(id);
        if (memberAddress == null) {
            throw new NoPermissionException("权限不足");
        }
        Buyer buyer = UserContext.getBuyer();
        if (!buyer.getUid().equals(memberAddress.getMemberId())) {
            throw new NoPermissionException("权限不足");
        }
        this.memberDaoSupport.execute("update es_member_address set def_addr = 0");
        this.memberDaoSupport.execute("update es_member_address set def_addr = 1 where addr_id = ?", id);
    }
}
