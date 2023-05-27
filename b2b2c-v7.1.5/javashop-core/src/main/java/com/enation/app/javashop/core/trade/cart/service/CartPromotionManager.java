package com.enation.app.javashop.core.trade.cart.service;

import com.enation.app.javashop.core.promotion.fulldiscount.model.vo.FullDiscountVO;
import com.enation.app.javashop.core.promotion.tool.model.enums.PromotionTypeEnum;
import com.enation.app.javashop.core.trade.cart.model.vo.CartVO;
import com.enation.app.javashop.core.trade.cart.model.vo.SelectedPromotionVo;

import java.util.List;

/**
 * 购物车优惠信息处理接口<br/>
 * 负责促销的使用、取消、读取。
 * 文档请参考：<br>
 * <a href="http://doc.javamall.com.cn/current/achitecture/jia-gou/ding-dan/cart-and-checkout.html" >购物车架构</a>
 *
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/12/1
 */
public interface CartPromotionManager {


    SelectedPromotionVo getSelectedPromotion();

    /**
     * 获取所有有效的满优惠活动
     *
     * @return
     */
    List<FullDiscountVO> getFullDiscounPromotion(List<CartVO> cartList);

    /**
     * 使用一个促销活动
     *
     * @param sellerId
     * @param skuId
     * @param activityId
     * @param promotionType
     */
    void usePromotion(Integer sellerId, Integer skuId, Integer activityId, PromotionTypeEnum promotionType);


    /**
     * 使用一个优惠券
     *
     * @param sellerId
     * @param mcId
     * @param cartList
     */
    void useCoupon(Integer sellerId,Integer mcId, List<CartVO> cartList);


    /**
     * 删除一个店铺优惠券的使用
     *
     * @param sellerId
     */
    void deleteCoupon(Integer sellerId);

    /**
     * 清除一个所有的优惠券
     */
    void cleanCoupon();

    /**
     * 批量删除sku对应的优惠活动
     *
     * @param skuids
     */
    void delete(Integer skuids[]);

    /**
     * 根据sku检查并清除无效的优惠活动
     *
     * @param skuId
     * @return
     */
    boolean checkPromotionInvalid(Integer skuId);

    /**
     * 清空当前用户的所有优惠活动
     */
    void clean();
}
