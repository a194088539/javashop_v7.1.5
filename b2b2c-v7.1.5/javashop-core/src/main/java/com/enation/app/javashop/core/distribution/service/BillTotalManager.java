package com.enation.app.javashop.core.distribution.service;

import com.enation.app.javashop.core.distribution.model.dos.BillTotalDO;
import com.enation.app.javashop.framework.database.Page;

/**
 * 用户结算单
 *
 * @author Chopper
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2018/5/22 上午9:30
 */

public interface BillTotalManager {
    /**
     * 获取结算page
     *
     * @param page     页码
     * @param pageSize 分页大小
     * @return
     */
    Page page(int page, int pageSize);

    /**
     *  新增一个总结算单
     * @param billTotal
     * @return
     */
    BillTotalDO add(BillTotalDO billTotal);

    /**
     * 获取
     * @param startTime
     * @return
     */
    BillTotalDO getTotalByStart(Long startTime);
}
