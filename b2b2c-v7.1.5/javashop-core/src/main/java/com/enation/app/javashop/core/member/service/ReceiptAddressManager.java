package com.enation.app.javashop.core.member.service;

import com.enation.app.javashop.core.member.model.dos.ReceiptAddressDO;

/**
 * 会员收票地址业务层
 *
 * @author duanmingyu
 * @version v7.1.4
 * @since v7.0.0
 * 2019-06-19
 */
public interface ReceiptAddressManager {

    /**
     * 新增会员收票地址
     * @param receiptAddressDO
     * @return
     */
    ReceiptAddressDO add(ReceiptAddressDO receiptAddressDO);

    /**
     * 修改会员收票地址
     * @param receiptAddressDO
     * @param id 主键id
     * @return
     */
    ReceiptAddressDO edit(ReceiptAddressDO receiptAddressDO, Integer id);

    /**
     * 获取会员收票地址
     * @return
     */
    ReceiptAddressDO get();
}
