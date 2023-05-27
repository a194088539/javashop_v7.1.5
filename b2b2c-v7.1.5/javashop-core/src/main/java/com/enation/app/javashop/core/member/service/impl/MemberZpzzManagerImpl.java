package com.enation.app.javashop.core.member.service.impl;

import com.enation.app.javashop.core.member.MemberErrorCode;
import com.enation.app.javashop.core.member.model.dos.MemberZpzzDO;
import com.enation.app.javashop.core.member.model.dto.ZpzzQueryParam;
import com.enation.app.javashop.core.member.model.enums.ZpzzStatusEnum;
import com.enation.app.javashop.core.member.service.MemberZpzzManager;
import com.enation.app.javashop.core.trade.order.service.CheckoutParamManager;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.security.model.Buyer;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 会员增票资质业务实现
 *
 * @author duanmingyu
 * @version v7.1.4
 * @since v7.0.0
 * 2019-06-18
 */
@Service
public class MemberZpzzManagerImpl implements MemberZpzzManager {

    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    private CheckoutParamManager checkoutParamManager;

    @Override
    public Page list(Integer pageNo, Integer pageSize, ZpzzQueryParam zpzzQueryParam) {
        StringBuffer sqlBuffer = new StringBuffer("select * from es_member_zpzz where id != 0 ");
        List<Object> term = new ArrayList<>();

        if (StringUtil.notEmpty(zpzzQueryParam.getUname())) {
            sqlBuffer.append(" and uname like ?");
            term.add("%" + zpzzQueryParam.getUname() + "%");
        }

        if (StringUtil.notEmpty(zpzzQueryParam.getStatus())) {
            sqlBuffer.append(" and status = ?");
            term.add(zpzzQueryParam.getStatus());
        }

        if (StringUtil.notEmpty(zpzzQueryParam.getStartTime())) {
            sqlBuffer.append(" and apply_time >= ?");
            term.add(zpzzQueryParam.getStartTime());
        }

        if (StringUtil.notEmpty(zpzzQueryParam.getEndTime())) {
            sqlBuffer.append(" and apply_time <= ?");
            term.add(zpzzQueryParam.getEndTime());
        }

        sqlBuffer.append(" order by apply_time desc");
        Page page = this.daoSupport.queryForPage(sqlBuffer.toString(), pageNo, pageSize, MemberZpzzDO.class, term.toArray());
        return page;
    }

    @Override
    public MemberZpzzDO add(MemberZpzzDO memberZpzzDO) {
        Buyer buyer = UserContext.getBuyer();
        if (buyer == null) {
            throw new ServiceException(MemberErrorCode.E137.code(), "当前会员登录信息已经失效");
        }

        MemberZpzzDO zpzzDO = this.get();
        if (zpzzDO != null) {
            throw new ServiceException(MemberErrorCode.E148.code(), "增票资质信息已存在，不可重复添加");
        }

        //参数验证
        this.verify(memberZpzzDO);

        memberZpzzDO.setApplyTime(DateUtil.getDateline());
        memberZpzzDO.setMemberId(buyer.getUid());
        memberZpzzDO.setUname(buyer.getUsername());
        memberZpzzDO.setStatus(ZpzzStatusEnum.NEW_APPLY.value());

        this.daoSupport.insert(memberZpzzDO);
        Integer id = this.daoSupport.getLastId("es_member_zpzz");
        memberZpzzDO.setId(id);
        return memberZpzzDO;
    }

    @Override
    public MemberZpzzDO edit(MemberZpzzDO memberZpzzDO, Integer id) {
        Buyer buyer = UserContext.getBuyer();
        if (buyer == null) {
            throw new ServiceException(MemberErrorCode.E137.code(), "当前会员登录信息已经失效");
        }

        MemberZpzzDO zpzzDO = this.get(id);

        if (zpzzDO == null || buyer.getUid().intValue() != zpzzDO.getMemberId().intValue()) {
            throw new ServiceException(MemberErrorCode.E136.code(), "没有操作权限");
        }

        //参数验证
        this.verify(memberZpzzDO);

        memberZpzzDO.setId(id);
        memberZpzzDO.setMemberId(buyer.getUid());
        memberZpzzDO.setApplyTime(DateUtil.getDateline());
        memberZpzzDO.setUname(buyer.getUsername());
        memberZpzzDO.setStatus(ZpzzStatusEnum.NEW_APPLY.value());

        this.daoSupport.update(memberZpzzDO, memberZpzzDO.getId());

        //如果会员修改了增票资质信息，那么需要将会员缓存的发票信息清除掉
        this.checkoutParamManager.deleteReceipt();

        return memberZpzzDO;
    }

    @Override
    public MemberZpzzDO get(Integer id) {
        StringBuffer sqlBuffer = new StringBuffer("select * from es_member_zpzz where id = ?");
        return this.daoSupport.queryForObject(sqlBuffer.toString(), MemberZpzzDO.class, id);
    }

    @Override
    public MemberZpzzDO get() {
        Buyer buyer = UserContext.getBuyer();
        if (buyer == null) {
            throw new ServiceException(MemberErrorCode.E137.code(), "当前会员登录信息已经失效");
        }

        StringBuffer sqlBuffer = new StringBuffer("select * from es_member_zpzz where member_id = ?");
        return this.daoSupport.queryForObject(sqlBuffer.toString(), MemberZpzzDO.class, buyer.getUid());
    }

    @Override
    public void delete(Integer id) {
        Buyer buyer = UserContext.getBuyer();
        if (buyer == null) {
            throw new ServiceException(MemberErrorCode.E137.code(), "当前会员登录信息已经失效");
        }

        MemberZpzzDO memberZpzzDO = this.get(id);
        if (memberZpzzDO.getMemberId().intValue() != buyer.getUid().intValue()) {
            throw new ServiceException(MemberErrorCode.E136.code(), "没有操作权限");
        }

        StringBuffer sqlBuffer = new StringBuffer("delete from es_member_zpzz where id = ?");
        this.daoSupport.execute(sqlBuffer.toString(), id);
    }

    @Override
    public void audit(Integer id, String status, String remark) {
        if (StringUtil.isEmpty(status)) {
            throw new ServiceException(MemberErrorCode.E147.code(), "参数错误");
        }

        if (!ZpzzStatusEnum.AUDIT_PASS.value().equals(status) && !ZpzzStatusEnum.AUDIT_REFUSE.value().equals(status)) {
            throw new ServiceException(MemberErrorCode.E147.code(), "参数错误");
        }

        if (ZpzzStatusEnum.AUDIT_REFUSE.value().equals(status)) {

            if (StringUtil.isEmpty(remark)) {
                throw new ServiceException(MemberErrorCode.E148.code(), "审核备注不能为空");
            }

            if (remark.length() > 200) {
                throw new ServiceException(MemberErrorCode.E148.code(), "审核备注不能超过200个字符");
            }
        }

        StringBuffer sqlBuffer = new StringBuffer("update es_member_zpzz set status = ?,audit_remark = ? where id = ?");
        this.daoSupport.execute(sqlBuffer.toString(), status, remark, id);
    }

    /**
     * 会员增票资质参数验证
     * @param memberZpzzDO
     */
    protected void verify(MemberZpzzDO memberZpzzDO) {
        if (StringUtil.isEmpty(memberZpzzDO.getCompanyName())) {
            throw new ServiceException(MemberErrorCode.E148.code(), "单位名称不能为空");
        }

        if (StringUtil.isEmpty(memberZpzzDO.getTaxpayerCode())) {
            throw new ServiceException(MemberErrorCode.E148.code(), "纳税人识别码不能为空");
        }

        if (StringUtil.isEmpty(memberZpzzDO.getRegisterAddress())) {
            throw new ServiceException(MemberErrorCode.E148.code(), "公司注册地址不能为空");
        }

        if (StringUtil.isEmpty(memberZpzzDO.getRegisterTel())) {
            throw new ServiceException(MemberErrorCode.E148.code(), "公司注册电话不能为空");
        }

        if (StringUtil.isEmpty(memberZpzzDO.getBankName())) {
            throw new ServiceException(MemberErrorCode.E148.code(), "开户银行不能为空");
        }

        if (StringUtil.isEmpty(memberZpzzDO.getBankAccount())) {
            throw new ServiceException(MemberErrorCode.E148.code(), "银行账户不能为空");
        }
    }
}
