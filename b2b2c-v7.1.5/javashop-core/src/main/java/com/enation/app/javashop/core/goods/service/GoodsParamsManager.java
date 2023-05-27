package com.enation.app.javashop.core.goods.service;

import java.util.List;

import com.enation.app.javashop.core.goods.model.dos.GoodsParamsDO;
import com.enation.app.javashop.core.goods.model.vo.GoodsParamsGroupVO;

/**
 * 商品参数关联接口
 *
 * @author fk
 * @version v2.0
 * @since v7.0.0 2018年3月21日 下午5:29:09
 */
public interface GoodsParamsManager {

    /**
     * 添加商品关联的参数
     *
     * @param goodsId   商品id
     * @param paramList 参数集合
     */
    void addParams(List<GoodsParamsDO> paramList, Integer goodsId);

    /**
     * 修改商品查询分类和商品关联的参数
     *
     * @param categoryId
     * @param goodsId
     * @return
     */
    List<GoodsParamsGroupVO> queryGoodsParams(Integer categoryId, Integer goodsId);

    /**
     * 添加商品查询分类和商品关联的参数
     *
     * @param categoryId
     * @return
     */
    List<GoodsParamsGroupVO> queryGoodsParams(Integer categoryId);

}
