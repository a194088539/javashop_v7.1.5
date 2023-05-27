package com.enation.app.javashop.core.member.service;

import com.enation.app.javashop.core.member.model.vo.SalesVO;
import com.enation.app.javashop.framework.database.Page;

/**
 * 会员销售记录
 * @author chopper
 * @version v1.0
 * @since v7.0
 * 2018/6/29 上午9:31
 * @Description:
 *
 */
public interface MemberSalesManager {


    /**
     * 商品销售记录
     * @param pageSize
     * @param pageNo
     * @param goodsId
     * @return
     */
    Page<SalesVO> list(Integer pageSize, Integer pageNo, Integer goodsId);


}
