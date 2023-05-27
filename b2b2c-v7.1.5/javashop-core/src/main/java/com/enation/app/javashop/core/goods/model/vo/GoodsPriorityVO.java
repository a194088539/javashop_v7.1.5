package com.enation.app.javashop.core.goods.model.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;

/**
* @author liuyulei
 * @version 1.0
 * @Description: 商品排序VO
 * @date 2019/6/10 14:30
 * @since v7.0
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GoodsPriorityVO implements Serializable {

    private static final long serialVersionUID = 8328687336811396475L;

    @ApiModelProperty(value = "id")
    private Integer goodsId;

    @ApiModelProperty(name = "goods_name", value = "商品名称")
    private String goodsName;

    @ApiModelProperty(name = "thumbnail", value = "缩略图路径", required = false)
    private String thumbnail;

    @ApiModelProperty(name = "is_auth", value = "0 待审核，1 审核通过 2 未通过", required = false)
    private Integer isAuth;

    @ApiModelProperty(name = "market_enable", value = "上架状态 1上架  0下架", required = false)
    private Integer marketEnable;

    @ApiModelProperty(name = "priority", value = "优先级", required = false)
    private Integer priority;

    @ApiModelProperty(name = "priority_text", value = "优先级文本", required = false)
    private String priorityText;


    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Integer getIsAuth() {
        return isAuth;
    }

    public void setIsAuth(Integer isAuth) {
        this.isAuth = isAuth;
    }

    public Integer getMarketEnable() {
        return marketEnable;
    }

    public void setMarketEnable(Integer marketEnable) {
        this.marketEnable = marketEnable;
    }

    public String getPriorityText() {
        return priorityText;
    }

    public void setPriorityText(String priorityText) {
        this.priorityText = priorityText;
    }


    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "GoodsPriorityVO{" +
                "goodsId=" + goodsId +
                ", goodsName='" + goodsName + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", isAuth=" + isAuth +
                ", marketEnable=" + marketEnable +
                ", priority='" + priority + '\'' +
                ", priorityText='" + priorityText + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GoodsPriorityVO that = (GoodsPriorityVO) o;

        return new EqualsBuilder()
                .append(goodsId, that.goodsId)
                .append(goodsName, that.goodsName)
                .append(thumbnail, that.thumbnail)
                .append(isAuth, that.isAuth)
                .append(marketEnable, that.marketEnable)
                .append(priority, that.priority)
                .append(priorityText, that.priorityText)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(goodsId)
                .append(goodsName)
                .append(thumbnail)
                .append(isAuth)
                .append(marketEnable)
                .append(priority)
                .append(priorityText)
                .toHashCode();
    }

}
