package com.enation.app.javashop.core.promotion.coupon.service;

import com.enation.app.javashop.core.promotion.coupon.model.dos.CouponDO;
import com.enation.app.javashop.core.promotion.coupon.model.dto.CouponParams;
import com.enation.app.javashop.framework.database.Page;

import java.util.List;

/**
 * 优惠券业务层
 *
 * @author Snow
 * @version v2.0
 * @since v7.0.0
 * 2018-04-17 23:19:39
 */
public interface CouponManager {

    /**
     * 查询优惠券列表
     *
     * @param params   参数
     * @return Page
     */
    Page list(CouponParams params);

    /**
     * 读取商品可用优惠券，进行中的
     *
     * @param goodsId
     * @return
     */
    List<CouponDO> getListByGoods(Integer goodsId);

    /**
     * 读取商家优惠券，正在进行中的
     *
     * @param sellerId
     * @return
     */
    List<CouponDO> getList(Integer sellerId);

    /**
     * 添加优惠券
     *
     * @param coupon 优惠券
     * @return Coupon 优惠券
     */
    CouponDO add(CouponDO coupon);

    /**
     * 修改优惠券
     *
     * @param coupon 优惠券
     * @param id     优惠券主键
     * @return Coupon 优惠券
     */
    CouponDO edit(CouponDO coupon, Integer id);

    /**
     * 删除优惠券
     *
     * @param id 优惠券主键
     */
    void delete(Integer id);

    /**
     * 获取优惠券
     *
     * @param id 优惠券主键
     * @return Coupon  优惠券
     */
    CouponDO getModel(Integer id);

    /**
     * 验证操作权限<br/>
     * 如有问题直接抛出权限异常
     *
     * @param id
     */
    void verifyAuth(Integer id);

    /**
     * 增加优惠券使用数量
     *
     * @param id
     */
    void addUsedNum(Integer id);


    /**
     * 增加被领取数量
     *
     * @param couponId
     */
    void addReceivedNum(Integer couponId);


    /**
     * 查询所有商家优惠券列表
     *
     * @param page     页码
     * @param pageSize 每页数量
     * @param sellerId 商家id
     * @return Page
     */
    Page all(int page, int pageSize, Integer sellerId);

    /**
     * 修改店铺名称改变优惠券中的店铺名称
     *
     * @param shopId   店铺id
     * @param shopName 店铺名称
     */
    void editCouponShopName(Integer shopId, String shopName);

    /**
     * 根据失效状态获取优惠券数据集合
     * @param status 失效状态 0：全部，1：有效，2：失效
     * @return
     */
    List<CouponDO> getByStatus(Integer status);
}
