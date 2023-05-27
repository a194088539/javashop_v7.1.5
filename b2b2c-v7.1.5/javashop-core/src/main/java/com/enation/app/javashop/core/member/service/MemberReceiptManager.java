package com.enation.app.javashop.core.member.service;

import com.enation.app.javashop.core.member.model.dos.MemberReceipt;

import java.util.List;

/**
 * 会员发票信息缓存业务层
 * @author duanmingyu
 * @version v7.1.4
 * @since v7.0.0
 * 2019-06-19
 */
public interface MemberReceiptManager {

    /**
     * 根据发票类型查询当前会员发票列表
     * @param receiptType 发票类型
     * @return
     */
    List<MemberReceipt> list(String receiptType);

    /**
     * 添加会员发票信息缓存
     * @param memberReceipt
     * @return
     */
    MemberReceipt add(MemberReceipt memberReceipt);

    /**
     * 修改会员发票信息缓存
     * @param memberReceipt
     * @param id
     * @return
     */
    MemberReceipt edit(MemberReceipt memberReceipt, Integer id);

    /**
     * 删除会员发票
     *
     * @param id 会员发票主键
     */
    void delete(Integer id);

    /**
     * 获取会员发票
     *
     * @param id 会员发票主键
     * @return MemberReceipt  会员发票
     */
    MemberReceipt getModel(Integer id);

    /**
     * 设置会员发票信息默认选项
     * @param receiptType 发票类型 ELECTRO：电子普通发票，VATORDINARY：增值税普通发票
     * @param id 主键ID 发票抬头为个人时则设置此参数为0
     */
    void setDefaultReceipt(String receiptType, Integer id);

}