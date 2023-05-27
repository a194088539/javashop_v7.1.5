package com.enation.app.javashop.core.member.service;

import com.enation.app.javashop.core.member.model.dto.HistoryQueryParam;
import com.enation.app.javashop.core.member.model.vo.ReceiptFileVO;
import com.enation.app.javashop.core.member.model.vo.ReceiptHistoryVO;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.core.member.model.dos.ReceiptHistory;

/**
 * 会员开票历史记录业务层
 *
 * @author duanmingyu
 * @version v7.1.4
 * @since v7.0.0
 * 2019-06-20
 */
public interface ReceiptHistoryManager {

    /**
     * 查询会员开票历史记录列表
     *
     * @param page     页码
     * @param pageSize 每页数量
     * @param params 搜索参数
     * @return Page
     */
    Page list(int page, int pageSize, HistoryQueryParam params);

    /**
     * 添加会员开票历史记录
     * @param receiptHistory
     * @return
     */
    ReceiptHistory add(ReceiptHistory receiptHistory);

    /**
     * 修改会员开票历史记录
     * @param receiptHistory
     * @param historyId 开票记录ID
     * @return
     */
    ReceiptHistory edit(ReceiptHistory receiptHistory, Integer historyId);

    /**
     * 根据订单编号查询会员开票历史记录
     * @param orderSn 订单编号
     * @return
     */
    ReceiptHistoryVO getReceiptHistory(String orderSn);


    /**
     * 获取会员开票历史记录详细信息
     *
     * @param historyId 会员开票历史记录id
     * @return 发票详细VO
     */
    ReceiptHistoryVO get(Integer historyId);

    /**
     * 商家开票--增值税普通发票和增值税专用发票
     * @param historyId 开票历史记录id
     * @param logiId 物流公司id
     * @param logiName 物流公司名称
     * @param logiCode 快递单号
     */
    void updateLogi(Integer historyId, Integer logiId, String logiName, String logiCode);

    /**
     * 商家开具发票-上传电子普通发票附件
     * @param receiptFileVO
     * @return
     */
    void uploadFiles(ReceiptFileVO receiptFileVO);
}