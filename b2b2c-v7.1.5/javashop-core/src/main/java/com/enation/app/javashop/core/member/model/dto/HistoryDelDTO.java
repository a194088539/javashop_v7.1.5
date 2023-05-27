package com.enation.app.javashop.core.member.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 会员足迹删除param
 *
 * @author zh
 * @version v7.1.4
 * @since vv7.1
 * 2019-06-18 15:18:56
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class HistoryDelDTO {

    /**
     * 商品足迹id
     */
    @ApiModelProperty(name = "id", value = "商品足迹id")
    private Integer id;
    /**
     * 日期时间戳
     */
    @ApiModelProperty(name = "date", value = "日期时间戳", required = true)
    private String date;

    @ApiModelProperty(name = "member_id", value = "会员id", required = true)
    private Integer memberId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    @Override
    public String toString() {
        return "HistoryDelDTO{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", memberId=" + memberId +
                '}';
    }
}