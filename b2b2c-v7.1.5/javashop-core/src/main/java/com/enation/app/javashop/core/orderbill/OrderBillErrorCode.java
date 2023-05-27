package com.enation.app.javashop.core.orderbill;

/**
 * 结算异常码
 * Created by kingapex on 2018/3/13.
 *
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/3/13
 */
public enum OrderBillErrorCode {

    /**
     * 没有操作权限
     */
    E700("没有操作权限"),
    /**
     * 参数不正确
     */
    E701("参数不正确");

    private String describe;

    OrderBillErrorCode(String des){
        this.describe =des;
    }

    /**
     * 获取异常码
     * @return
     */
    public String code(){
        return this.name().replaceAll("E","");
    }


}
