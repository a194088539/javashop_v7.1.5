package com.enation.app.javashop.core.goods.model.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author fk
 * @version v2.0
 * @Description: 标签商品的数量
 * @date 2018/9/11 15:00
 * @since v7.0.0
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TagGoodsNum {

    /**
     * 热销商品数量
     */
    @ApiModelProperty(name="hot_num",value="热销商品数量")
    private Integer hotNum;

    /**
     * 新品推荐数量
     */
    @ApiModelProperty(name="new_num",value="新品推荐数量")
    private Integer newNum;

    /**
     * 推荐商品数量
     */
    @ApiModelProperty(name="recommend_num",value="推荐商品数量")
    private Integer recommendNum;

    public TagGoodsNum() {
    }

    public TagGoodsNum(Integer hotNum, Integer newNum, Integer recommendNum) {
        this.hotNum = hotNum;
        this.newNum = newNum;
        this.recommendNum = recommendNum;
    }

    public Integer getHotNum() {
        return hotNum;
    }

    public void setHotNum(Integer hotNum) {
        this.hotNum = hotNum;
    }

    public Integer getNewNum() {
        return newNum;
    }

    public void setNewNum(Integer newNum) {
        this.newNum = newNum;
    }

    public Integer getRecommendNum() {
        return recommendNum;
    }

    public void setRecommendNum(Integer recommendNum) {
        this.recommendNum = recommendNum;
    }

    @Override
    public String toString() {
        return "TagGoodsNum{" +
                "hotNum=" + hotNum +
                ", newNum=" + newNum +
                ", recommendNum=" + recommendNum +
                '}';
    }
}
