package com.enation.app.javashop.core.trade.cart.service;

import com.enation.app.javashop.core.trade.cart.model.enums.CheckedWay;
import com.enation.app.javashop.core.trade.cart.model.vo.CartSkuOriginVo;

import java.util.List;

/**
 * 购物车原始数据业务类<br/>
 * 负责对购物车原始数据{@link CartSkuOriginVo}在缓存中的读写
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/12/11
 */
public interface CartOriginDataManager {


    /**
     * 由缓存中读取数据
     * @param checkedWay 表明调用该方法是CART,BUY_NOW还是TRADE,确定调用不同的购物缓存数据
     * @return 列表
     */
    List<CartSkuOriginVo> read(CheckedWay checkedWay);

    /**
     * 向缓存中写入数据 
     * @param skuId 要写入的skuid
     * @param num 要加入购物车的数量
     * @param activityId 要参加的活动
     * @return
     */
    CartSkuOriginVo add(int skuId, int num,Integer activityId);

    /**
     * 向缓存中写入数据
     * @param skuId 要写入的skuid
     * @param num 要加入购物车的数量
     * @param activityId 要参加的活动
     * @return
     */
    CartSkuOriginVo addBuyNow(int skuId, int num,Integer activityId);

    /**
     * 立即购买
     * @param skuId
     * @param num
     * @param activityId
     */
    void buy(Integer skuId, Integer num, Integer activityId);

    /**
     * 更新数量
     * @param skuId 要更新的sku id
     * @param num 要更新的数量
     */
    CartSkuOriginVo updateNum(int skuId,int num);


    /**
     * 更新选中状态
     * @param skuId
     * @param checked
     * @return
     */
    CartSkuOriginVo checked(int skuId,int checked);


    /**
     * 更新某个店铺的所有商品的选中状态
     * @param sellerId
     * @param checked
     */
    void checkedSeller(int sellerId,int checked);


    /**
     * 更新全部的选中状态
     * @param way 更新源，BUY_NOW,立即购买,CART,购物车
     *
     */
    void checkedAll(int checked, CheckedWay way);


    /**
     * 批量删除
     * @param skuIds
     * @param way 删除源，BUY_NOW,立即购买,CART,购物车
     */
    void delete(Integer[] skuIds, CheckedWay way);

    /**
     * 清空购物车
     */
    void clean();

    /**
     * 清除掉已经选中的商品
     * @param way 清除缓存源，BUY_BOW,立即购买，CART,购物车
     */
    void cleanChecked(CheckedWay way);


}
