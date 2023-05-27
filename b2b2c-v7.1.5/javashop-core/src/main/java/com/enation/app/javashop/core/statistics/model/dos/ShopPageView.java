package com.enation.app.javashop.core.statistics.model.dos;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Id;
import com.enation.app.javashop.framework.database.annotation.Table;

import java.io.Serializable;

/**
 * ShopPV
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018-08-07 上午11:19
 */
@Table(name = "es_sss_shop_pv")
public class ShopPageView  implements Serializable {

    @Id
    private Integer id;

    @Column(name = "seller_id")
    private Integer sellerId;

    @Column(name = "vs_year")
    private Integer year;

    @Column(name = "vs_month")
    private Integer month;

    @Column(name = "vs_day")
    private Integer day;

    @Column(name = "vs_num")
    private Integer num;

    @Column(name="create_time")
    private Long createTime;

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (sellerId != null ? sellerId.hashCode() : 0);
        result = 31 * result + (year != null ? year.hashCode() : 0);
        result = 31 * result + (month != null ? month.hashCode() : 0);
        result = 31 * result + (day != null ? day.hashCode() : 0);
        result = 31 * result + (num != null ? num.hashCode() : 0);
        return result;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
