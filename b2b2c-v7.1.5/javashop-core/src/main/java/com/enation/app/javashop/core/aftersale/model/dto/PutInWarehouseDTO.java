package com.enation.app.javashop.core.aftersale.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 售后商品入库参数实体
 * 主要用于商家对售后商品进行入库操作
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-11-13
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PutInWarehouseDTO implements Serializable {

    private static final long serialVersionUID = -7065459381149938873L;

    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品ID", name = "goods_id", required = true)
    private Integer goodsId;
    /**
     * 商品skuID
     */
    @ApiModelProperty(value = "商品skuID" ,name = "sku_id", required = true)
    private Integer skuId;
    /**
     * 用户退还数量
     */
    @ApiModelProperty(value = "用户退还数量" ,name = "return_num", required = true)
    private Integer returnNum;
    /**
     * 实际入库数量
     */
    @ApiModelProperty(value = "实际入库数量" ,name = "storage_num", required = true)
    private Integer storageNum;

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

    public Integer getReturnNum() {
        return returnNum;
    }

    public void setReturnNum(Integer returnNum) {
        this.returnNum = returnNum;
    }

    public Integer getStorageNum() {
        return storageNum;
    }

    public void setStorageNum(Integer storageNum) {
        this.storageNum = storageNum;
    }

    @Override
    public String toString() {
        return "PutInWarehouseDTO{" +
                "goodsId=" + goodsId +
                ", skuId=" + skuId +
                ", returnNum=" + returnNum +
                ", storageNum=" + storageNum +
                '}';
    }
}
