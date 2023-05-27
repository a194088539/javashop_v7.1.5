package com.enation.app.javashop.core.member.service.impl;

import com.enation.app.javashop.core.member.MemberErrorCode;
import com.enation.app.javashop.core.member.model.dos.ReceiptAddressDO;
import com.enation.app.javashop.core.member.service.ReceiptAddressManager;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.security.model.Buyer;
import com.enation.app.javashop.framework.util.BeanUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import com.enation.app.javashop.framework.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 会员收票地址业务实现
 *
 * @author duanmingyu
 * @version v7.1.4
 * @since v7.0.0
 * 2019-06-19
 */
@Service
public class ReceiptAddressManagerImpl implements ReceiptAddressManager {

    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport daoSupport;

    @Override
    public ReceiptAddressDO add(ReceiptAddressDO receiptAddressDO) {
        BeanUtil.copyProperties(receiptAddressDO.getRegion(), receiptAddressDO);

        Buyer buyer = UserContext.getBuyer();
        if (buyer == null) {
            throw new ServiceException(MemberErrorCode.E137.code(), "当前会员登录信息已经失效");
        }

        ReceiptAddressDO addressDO = this.get();
        if (addressDO != null) {
            throw new ServiceException(MemberErrorCode.E149.code(), "收票地址已存在，不可重复添加");
        }

        //验证信息
        this.verify(receiptAddressDO);

        receiptAddressDO.setMemberId(buyer.getUid());

        this.daoSupport.insert(receiptAddressDO);
        Integer id = this.daoSupport.getLastId("es_receipt_address");
        receiptAddressDO.setId(id);
        return receiptAddressDO;
    }

    @Override
    public ReceiptAddressDO edit(ReceiptAddressDO receiptAddressDO, Integer id) {
        Buyer buyer = UserContext.getBuyer();
        if (buyer == null) {
            throw new ServiceException(MemberErrorCode.E137.code(), "当前会员登录信息已经失效");
        }

        ReceiptAddressDO addressDO = this.get(id);
        if (addressDO == null || buyer.getUid().intValue() != addressDO.getMemberId().intValue()) {
            throw new ServiceException(MemberErrorCode.E136.code(), "没有操作权限");
        }

        BeanUtil.copyProperties(receiptAddressDO.getRegion(), addressDO);
        addressDO.setDetailAddr(receiptAddressDO.getDetailAddr());
        addressDO.setMemberMobile(receiptAddressDO.getMemberMobile());
        addressDO.setMemberName(receiptAddressDO.getMemberName());

        this.daoSupport.update(addressDO, id);

        return addressDO;
    }

    @Override
    public ReceiptAddressDO get() {
        Buyer buyer = UserContext.getBuyer();
        if (buyer == null) {
            throw new ServiceException(MemberErrorCode.E137.code(), "当前会员登录信息已经失效");
        }
        return this.daoSupport.queryForObject("select * from es_receipt_address where member_id = ?", ReceiptAddressDO.class, buyer.getUid());
    }

    /**
     * 根据id获取收票地址信息
     * @param id
     * @return
     */
    private ReceiptAddressDO get(Integer id) {
        return this.daoSupport.queryForObject("select * from es_receipt_address where id = ?", ReceiptAddressDO.class, id);
    }

    /**
     * 会员收票地址参数验证
     * @param receiptAddressDO
     */
    protected void verify(ReceiptAddressDO receiptAddressDO) {
        if (StringUtil.isEmpty(receiptAddressDO.getMemberName())) {
            throw new ServiceException(MemberErrorCode.E148.code(), "收票人姓名不能为空");
        }

        if (StringUtil.isEmpty(receiptAddressDO.getMemberMobile())) {
            throw new ServiceException(MemberErrorCode.E148.code(), "收票人手机号不能为空");
        }

        if (!Validator.isMobile(receiptAddressDO.getMemberMobile())) {
            throw new ServiceException(MemberErrorCode.E148.code(), "收票人手机号格式不正确");
        }

        if (StringUtil.isEmpty(receiptAddressDO.getDetailAddr())) {
            throw new ServiceException(MemberErrorCode.E148.code(), "详细地址不能为空");
        }
    }
}
