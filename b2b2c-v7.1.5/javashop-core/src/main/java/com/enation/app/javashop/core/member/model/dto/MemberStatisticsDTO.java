package com.enation.app.javashop.core.member.model.dto;

/**
 * 会员数据附属数据统计
 *
 * @author zh
 * @version v7.0
 * @date 18/6/13 下午3:07
 * @since v7.0
 */
public class MemberStatisticsDTO {
    /**
     * 订单数
     */
    private Integer orderCount;
    /**
     * 商品收藏数
     */
    private Integer goodsCollectCount;
    /**
     * 店铺收藏数
     */
    private Integer shopCollectCount;
    /**
     * 待评论数
     */
    private Integer pendingCommentCount;

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public Integer getGoodsCollectCount() {
        return goodsCollectCount;
    }

    public void setGoodsCollectCount(Integer goodsCollectCount) {
        this.goodsCollectCount = goodsCollectCount;
    }

    public Integer getShopCollectCount() {
        return shopCollectCount;
    }

    public void setShopCollectCount(Integer shopCollectCount) {
        this.shopCollectCount = shopCollectCount;
    }

    public Integer getPendingCommentCount() {
        return pendingCommentCount;
    }

    public void setPendingCommentCount(Integer pendingCommentCount) {
        this.pendingCommentCount = pendingCommentCount;
    }

    @Override
    public String toString() {
        return "MemberStatisticsDTO{" +
                "orderCount=" + orderCount +
                ", goodsCollectCount=" + goodsCollectCount +
                ", shopCollectCount=" + shopCollectCount +
                ", pendingCommentCount=" + pendingCommentCount +
                '}';
    }
}
