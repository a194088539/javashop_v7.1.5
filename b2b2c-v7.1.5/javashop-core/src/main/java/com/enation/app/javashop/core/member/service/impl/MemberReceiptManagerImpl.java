package com.enation.app.javashop.core.member.service.impl;

import com.enation.app.javashop.core.member.MemberErrorCode;
import com.enation.app.javashop.core.member.model.dos.MemberReceipt;
import com.enation.app.javashop.core.member.model.enums.ReceiptTypeEnum;
import com.enation.app.javashop.core.member.service.MemberReceiptManager;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.NoPermissionException;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.security.model.Buyer;
import com.enation.app.javashop.framework.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 会员发票信息缓存业务实现
 * @author duanmingyu
 * @version v7.1.4
 * @since v7.0.0
 * 2019-06-19
 */
@Service
public class MemberReceiptManagerImpl implements MemberReceiptManager {

    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport memberDaoSupport;

    @Override
    public List<MemberReceipt> list(String receiptType) {
        Buyer buyer = UserContext.getBuyer();
        if (buyer == null) {
            throw new ServiceException(MemberErrorCode.E137.code(), "当前会员登录信息已经失效");
        }

        if (!ReceiptTypeEnum.ELECTRO.value().equals(receiptType) && !ReceiptTypeEnum.VATORDINARY.value().equals(receiptType)) {
            throw new ServiceException(MemberErrorCode.E147.code(), "参数错误");
        }

        String sql = "select * from es_member_receipt where member_id = ? and receipt_type = ? ORDER BY receipt_id desc ";
        return this.memberDaoSupport.queryForList(sql, MemberReceipt.class, buyer.getUid(), receiptType);
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public MemberReceipt add(MemberReceipt memberReceipt) {
        //校验当前会员是否存在
        Buyer buyer = UserContext.getBuyer();
        if (buyer == null) {
            throw new ServiceException(MemberErrorCode.E137.code(), "当前会员登录信息已经失效");
        }

        if (this.checkTitle(memberReceipt.getReceiptTitle(), null, buyer.getUid(), memberReceipt.getReceiptType())) {
            throw new ServiceException(MemberErrorCode.E121.code(), "发票抬头不能重复");
        }

        if(StringUtil.isEmpty(memberReceipt.getTaxNo())){
            throw new ServiceException(MemberErrorCode.E121.code(), "纳税人识别号不能为空");
        }

        List<MemberReceipt> list = this.list(memberReceipt.getReceiptType());
        if (list.size() >= 10) {
            throw new ServiceException(MemberErrorCode.E121.code(), "会员发票信息缓存数不能超过10条");
        }

        memberReceipt.setMemberId(buyer.getUid());
        memberReceipt.setIsDefault(1);
        memberDaoSupport.insert(memberReceipt);
        Integer receiptId = memberDaoSupport.getLastId("es_member_receipt");
        memberReceipt.setReceiptId(receiptId);

        //将此会员发票信息设置为默认选项
        this.setDefaultReceipt(memberReceipt.getReceiptType(), receiptId);

        return memberReceipt;
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public MemberReceipt edit(MemberReceipt memberReceipt, Integer id) {
        //校验当前会员是否存在
        Buyer buyer = UserContext.getBuyer();
        if (buyer == null) {
            throw new ServiceException(MemberErrorCode.E137.code(), "当前会员登录信息已经失效");
        }

        MemberReceipt receipt = this.getModel(id);
        if (receipt == null || buyer.getUid().intValue() != receipt.getMemberId().intValue()) {
            throw new ServiceException(MemberErrorCode.E136.code(), "没有操作权限");
        }

        if (this.checkTitle(memberReceipt.getReceiptTitle(), id, buyer.getUid(), memberReceipt.getReceiptType())) {
            throw new ServiceException(MemberErrorCode.E121.code(), "发票抬头不能重复");
        }

        if(StringUtil.isEmpty(memberReceipt.getTaxNo())){
            throw new ServiceException(MemberErrorCode.E121.code(), "纳税人识别号不能为空");
        }

        memberReceipt.setReceiptId(id);
        memberReceipt.setMemberId(buyer.getUid());
        memberReceipt.setIsDefault(receipt.getIsDefault());
        this.memberDaoSupport.update(memberReceipt, id);
        return memberReceipt;
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delete(Integer id) {
        //校验当前会员是否存在
        Buyer buyer = UserContext.getBuyer();
        if (buyer == null) {
            throw new ServiceException(MemberErrorCode.E137.code(), "当前会员登录信息已经失效");
        }

        MemberReceipt memberReceipt = this.getModel(id);
        if (memberReceipt == null || !memberReceipt.getMemberId().equals(buyer.getUid())) {
            throw new NoPermissionException("无权操作");
        }
        this.memberDaoSupport.delete(MemberReceipt.class, id);
    }

    @Override
    public MemberReceipt getModel(Integer id) {
        String sql = "select * from es_member_receipt where receipt_id = ?";
        return this.memberDaoSupport.queryForObject(sql, MemberReceipt.class, id);
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void setDefaultReceipt(String receiptType, Integer id) {
        //校验当前会员是否存在
        Buyer buyer = UserContext.getBuyer();
        if (buyer == null) {
            throw new ServiceException(MemberErrorCode.E137.code(), "当前会员登录信息已经失效");
        }

        //先将所有发票信息默认选项去除
        this.memberDaoSupport.execute("update es_member_receipt set is_default = 0 where member_id = ? and receipt_type = ?", buyer.getUid(), receiptType);

        //如果发票抬头不为个人，则将此发票信息设置为默认选项
        if (id != 0) {
            MemberReceipt memberReceipt = this.getModel(id);
            if (memberReceipt == null || buyer.getUid().intValue() != memberReceipt.getMemberId().intValue()) {
                throw new NoPermissionException("无权操作");
            }
            this.memberDaoSupport.execute("update es_member_receipt set is_default = 1 where member_id = ? and receipt_type = ? and receipt_id = ?", buyer.getUid(), receiptType, id);
        }
    }

    /**
     * 检测发票抬头是否重复
     * @param title 发票抬头
     * @param id 主键id
     * @param memberId 会员id
     * @param receiptType 发票类型
     * @return
     */
    protected boolean checkTitle(String title, Integer id, Integer memberId, String receiptType) {
        if ("个人".equals(title)) {
            return true;
        }

        String sql = "select count(0) from es_member_receipt where member_id = ? and receipt_title = ? and receipt_type = ?";

        List<Object> params = new ArrayList<>();
        params.add(memberId);
        params.add(title);
        params.add(receiptType);

        if (id != null) {
            sql += " and receipt_id != ?";
            params.add(id);
        }

        int count = this.memberDaoSupport.queryForInt(sql, params.toArray());
        boolean flag = count != 0 ? true : false;
        return flag;
    }
}
