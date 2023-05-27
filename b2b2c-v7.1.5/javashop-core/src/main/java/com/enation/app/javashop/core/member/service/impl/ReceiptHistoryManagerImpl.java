package com.enation.app.javashop.core.member.service.impl;

import com.enation.app.javashop.core.member.MemberErrorCode;
import com.enation.app.javashop.core.member.model.dos.ReceiptFileDO;
import com.enation.app.javashop.core.member.model.dto.HistoryQueryParam;
import com.enation.app.javashop.core.member.model.enums.ReceiptTypeEnum;
import com.enation.app.javashop.core.member.model.vo.ReceiptFileVO;
import com.enation.app.javashop.core.member.model.vo.ReceiptHistoryVO;
import com.enation.app.javashop.core.trade.order.model.enums.OrderStatusEnum;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.security.model.Seller;
import com.enation.app.javashop.framework.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.core.member.model.dos.ReceiptHistory;
import com.enation.app.javashop.core.member.service.ReceiptHistoryManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 会员开票历史记录业务实现
 *
 * @author duanmingyu
 * @version v7.1.4
 * @since v7.0.0
 * 2019-06-20
 */
@Service
public class ReceiptHistoryManagerImpl implements ReceiptHistoryManager {

    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport memberDaoSupport;

    @Override
    public Page list(int page, int pageSize, HistoryQueryParam params) {
        StringBuffer sqlBuffer = new StringBuffer("select * from es_receipt_history where order_status = ? ");
        List<Object> term = new ArrayList<>();
        term.add(OrderStatusEnum.CONFIRM.value());

        if (params.getSellerId() != null) {
            sqlBuffer.append(" and seller_id = ? ");
            term.add(params.getSellerId());
        }

        if (params.getMemberId() != null) {
            sqlBuffer.append(" and member_id = ? ");
            term.add(params.getMemberId());
        }

        if (StringUtil.notEmpty(params.getKeyword())) {
            sqlBuffer.append(" and (order_sn like ? or seller_name like ?) ");
            term.add("%" + params.getKeyword() + "%");
            term.add("%" + params.getKeyword() + "%");
        }

        if (StringUtil.notEmpty(params.getOrderSn())) {
            sqlBuffer.append(" and order_sn like ? ");
            term.add("%" + params.getOrderSn() + "%");
        }

        if (StringUtil.notEmpty(params.getSellerName())) {
            sqlBuffer.append(" and seller_name like ? ");
            term.add("%" + params.getSellerName() + "%");
        }

        if (StringUtil.notEmpty(params.getStatus())) {
            sqlBuffer.append(" and status = ? ");
            term.add(params.getStatus());
        }

        if (StringUtil.notEmpty(params.getReceiptType())) {
            sqlBuffer.append(" and receipt_type = ? ");
            term.add(params.getReceiptType());
        }

        if (StringUtil.notEmpty(params.getStartTime())) {
            sqlBuffer.append(" and add_time >= ? ");
            term.add(params.getStartTime());
        }

        if (StringUtil.notEmpty(params.getEndTime())) {
            sqlBuffer.append(" and add_time <= ? ");
            term.add(params.getEndTime());
        }

        if (StringUtil.notEmpty(params.getUname())) {
            sqlBuffer.append(" and uname like ? ");
            term.add("%" + params.getUname() + "%");
        }

        sqlBuffer.append(" order by add_time desc");
        Page<ReceiptHistoryVO> webPage = this.memberDaoSupport.queryForPage(sqlBuffer.toString(), page, pageSize, ReceiptHistoryVO.class, term.toArray());

        return webPage;
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ReceiptHistory add(ReceiptHistory receiptHistory) {
        this.memberDaoSupport.insert(receiptHistory);
        receiptHistory.setHistoryId(memberDaoSupport.getLastId("es_history_receipt"));
        return receiptHistory;
    }

    @Override
    public ReceiptHistory edit(ReceiptHistory receiptHistory, Integer historyId) {
        this.memberDaoSupport.update(receiptHistory, historyId);
        return receiptHistory;
    }

    @Override
    public ReceiptHistoryVO getReceiptHistory(String orderSn) {
        ReceiptHistoryVO receiptHistoryVO = this.memberDaoSupport.queryForObject("select * from es_receipt_history where order_sn = ?", ReceiptHistoryVO.class, orderSn);

        if (receiptHistoryVO == null) {
            throw new ServiceException(MemberErrorCode.E150.code(), "会员开票历史记录不存在");
        }

        //获取电子发票附件
        receiptHistoryVO.setElecFileList(this.listElecFile(receiptHistoryVO.getHistoryId()));

        return receiptHistoryVO;
    }

    @Override
    public ReceiptHistoryVO get(Integer historyId) {
        //获取发票详细信息
        StringBuffer sqlBuffer = new StringBuffer("select * from es_receipt_history where history_id = ?");

        ReceiptHistoryVO receiptHistoryVO = this.memberDaoSupport.queryForObject(sqlBuffer.toString(), ReceiptHistoryVO.class, historyId);

        if (receiptHistoryVO == null) {
            throw new ServiceException(MemberErrorCode.E150.code(), "会员开票历史记录不存在");
        }

        //获取电子发票附件
        receiptHistoryVO.setElecFileList(this.listElecFile(historyId));

        return receiptHistoryVO;
    }

    @Override
    public void updateLogi(Integer historyId, Integer logiId, String logiName, String logiCode) {
        Seller seller = UserContext.getSeller();
        if (seller == null) {
            throw new ServiceException(MemberErrorCode.E137.code(), "当前商家登录信息已经失效");
        }

        ReceiptHistoryVO receiptHistoryVO = this.get(historyId);

        if (receiptHistoryVO.getSellerId().intValue() != seller.getSellerId().intValue()) {
            throw new ServiceException(MemberErrorCode.E136.code(), "没有操作权限");
        }

        if (!ReceiptTypeEnum.VATOSPECIAL.value().equals(receiptHistoryVO.getReceiptType())
                &&!ReceiptTypeEnum.VATORDINARY.value().equals(receiptHistoryVO.getReceiptType())) {
            throw new ServiceException(MemberErrorCode.E150.code(), "发票类型错误，不可操作");
        }

        if (logiId == null || StringUtil.isEmpty(logiName)) {
            throw new ServiceException(MemberErrorCode.E150.code(), "物流公司信息不能为空");
        }

        if (StringUtil.isEmpty(logiCode)) {
            throw new ServiceException(MemberErrorCode.E150.code(), "快递单号不能为空");
        }

        List<Object> term = new ArrayList<>();
        term.add(logiId);
        term.add(logiName);
        term.add(logiCode);
        term.add(historyId);

        String sql = "update es_receipt_history set status=1,logi_id=?,logi_name=?,logi_code=? where history_id = ?";
        this.memberDaoSupport.execute(sql, term.toArray());
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void uploadFiles(ReceiptFileVO receiptFileVO) {
        Seller seller = UserContext.getSeller();
        if (seller == null) {
            throw new ServiceException(MemberErrorCode.E137.code(), "当前商家登录信息已经失效");
        }

        Integer historyId = receiptFileVO.getHistoryId();
        if (historyId == null) {
            throw new ServiceException(MemberErrorCode.E147.code(), "参数错误");
        }

        ReceiptHistoryVO receiptHistoryVO = this.get(historyId);

        if (receiptHistoryVO.getSellerId().intValue() != seller.getSellerId().intValue()) {
            throw new ServiceException(MemberErrorCode.E136.code(), "没有操作权限");
        }

        if (!ReceiptTypeEnum.ELECTRO.value().equals(receiptHistoryVO.getReceiptType())) {
            throw new ServiceException(MemberErrorCode.E150.code(), "发票类型错误，不可操作");
        }

        if (receiptFileVO.getFiles() == null || receiptFileVO.getFiles().size() == 0) {
            throw new ServiceException(MemberErrorCode.E150.code(), "电子发票附件不能为空");
        }

        for (String file : receiptFileVO.getFiles()) {
            ReceiptFileDO receiptFileDO = new ReceiptFileDO();
            receiptFileDO.setHistoryId(historyId);
            receiptFileDO.setElecFile(file);
            this.memberDaoSupport.insert(receiptFileDO);
        }

        //将会员开票历史记录信息标记为已开票状态
        this.memberDaoSupport.execute("update es_receipt_history set status=1 where history_id = ?", historyId);
    }

    /**
     * 获取电子发票附件集合
     *
     * @param historyId 会员开票历史记录ID
     * @return
     */
    protected List<String> listElecFile(Integer historyId) {
        String sql = "select elec_file from es_receipt_file where history_id = ?";
        List<Map> fileList = this.memberDaoSupport.queryForList(sql, historyId);
        List<String> res = new ArrayList<>();
        if(StringUtil.isNotEmpty(fileList)){
            for(Map map : fileList){
                res.add(map.get("elec_file").toString());
            }
        }
        return res;
    }
}
