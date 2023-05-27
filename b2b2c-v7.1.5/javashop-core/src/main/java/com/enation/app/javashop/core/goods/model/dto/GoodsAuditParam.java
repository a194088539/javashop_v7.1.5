package com.enation.app.javashop.core.goods.model.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.Arrays;
import java.util.Objects;

/**
 * 管理端审核商品参数实体
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 */
public class GoodsAuditParam {
    @ApiModelProperty(name = "goods_ids", value = "要审核的商品ID组", required = true)
    private Integer[] goodsIds;

    @ApiModelProperty(name = "pass", value = "是否通过审核 1：通过，0：未通过", required = true, allowableValues = "0,1")
    private Integer pass;

    @ApiModelProperty(name = "message",	value = "审核备注(未通过必须需要填写)")
    private String message;

    public Integer[] getGoodsIds() {
        return goodsIds;
    }

    public void setGoodsIds(Integer[] goodsIds) {
        this.goodsIds = goodsIds;
    }

    public Integer getPass() {
        return pass;
    }

    public void setPass(Integer pass) {
        this.pass = pass;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GoodsAuditParam that = (GoodsAuditParam) o;
        return Arrays.equals(goodsIds, that.goodsIds) &&
                Objects.equals(pass, that.pass) &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(pass, message);
        result = 31 * result + Arrays.hashCode(goodsIds);
        return result;
    }

    @Override
    public String toString() {
        return "GoodsAuditParam{" +
                "goodsIds=" + Arrays.toString(goodsIds) +
                ", pass=" + pass +
                ", message='" + message + '\'' +
                '}';
    }
}
