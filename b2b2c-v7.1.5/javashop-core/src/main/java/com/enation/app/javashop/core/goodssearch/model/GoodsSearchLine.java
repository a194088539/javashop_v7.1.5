package com.enation.app.javashop.core.goodssearch.model;

/**
 * 商品搜索
 *
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018-03-16 16:32:45
 */
public class GoodsSearchLine {

    /**
     * 商品id
     */
    private int goodsId;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 缩略图
     */
    private String thumbnail;

    /**
     * 小图
     */
    private String small;

    /**
     * 商品优惠价格
     */
    private Double discountPrice;

    /**
     * 商品价格
     */
    private Double price;

    /**
     * 购买数
     */
    private Integer buyCount;

    /**
     * 评论数
     */
    private Integer commentNum;

//	/**库存数*/
//	private Integer quantity;

    /**
     * 商品好評率
     */
    private Double grade;

    /**
     * 卖家id
     */
    private Integer sellerId;
    /**
     * 店铺名字
     */
    private String sellerName;

    /**
     * 是否自营商品 0否 1是
     */
    private Integer selfOperated;

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(Integer buyCount) {
        this.buyCount = buyCount;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public Integer getSelfOperated() {
        return selfOperated;
    }

    public void setSelfOperated(Integer selfOperated) {
        this.selfOperated = selfOperated;
    }

    public Double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Double discountPrice) {
        this.discountPrice = discountPrice;
    }
}
