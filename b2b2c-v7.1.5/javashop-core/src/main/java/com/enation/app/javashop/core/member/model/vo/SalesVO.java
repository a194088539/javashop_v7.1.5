package com.enation.app.javashop.core.member.model.vo;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.util.StringUtil;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 销售记录VO
 *
 * @author chopper
 * @version v1.0
 * @since v7.0
 * 2018-06-29 上午9:32
 */
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SalesVO implements Serializable {
    @ApiModelProperty(name="buyer_name",value="买家",required=false)
    @Column(name = "buyer_name")
    private String buyerName;

    @ApiModelProperty(name="price",value="价格",required=false)
    @Column(name = "price")
    private Double price;

    @ApiModelProperty(name="num",value="数量",required=false)
    @Column(name = "num")
    private Integer num;

    @ApiModelProperty(name="create_time",value="购买日期",required=false)
    @Column(name = "create_time")
    private Integer createTime;


    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        if(!StringUtil.isEmpty(buyerName)){
            if(buyerName.length()==1){
                this.buyerName="***";
            }else {
                this.buyerName = buyerName.substring(0, 1) + "***" + buyerName.substring(buyerName.length() - 1, buyerName.length());
            }
        }
    }
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "SalesVO{" +
                "buyerName='" + buyerName + '\'' +
                ", price=" + price +
                ", num=" + num +
                ", createTime=" + createTime +
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

        SalesVO salesVO = (SalesVO) o;

        if (buyerName != null ? !buyerName.equals(salesVO.buyerName) : salesVO.buyerName != null) {
            return false;
        }
        if (price != null ? !price.equals(salesVO.price) : salesVO.price != null) {
            return false;
        }
        if (num != null ? !num.equals(salesVO.num) : salesVO.num != null) {
            return false;
        }
        return createTime != null ? createTime.equals(salesVO.createTime) : salesVO.createTime == null;
    }

    @Override
    public int hashCode() {
        int result = buyerName != null ? buyerName.hashCode() : 0;
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (num != null ? num.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        return result;
    }
}
