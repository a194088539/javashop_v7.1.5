package com.enation.app.javashop.core.system.model.vo;

import java.util.List;
import java.util.Map;


/**
 * 快递平台实体
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-07-11 14:42:50
 */

public class ExpressDetailVO {

    /**
     * 快递名称
     */
    private String name;
    /**
     * 快递单号
     */
    private String courierNum;
    /**
     * 物流详细信息
     */
    private List<Map> data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourierNum() {
        return courierNum;
    }

    public void setCourierNum(String courierNum) {
        this.courierNum = courierNum;
    }

    public List<Map> getData() {
        return data;
    }

    public void setData(List<Map> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ExpressDetailVO{" +
                "name='" + name + '\'' +
                ", courierNum='" + courierNum + '\'' +
                ", data=" + data +
                '}';
    }
}