package com.enation.app.javashop.core.aftersale;

/**
 * 售后异常码
 * Created by kingapex on 2018/3/13.
 *
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/3/13
 */
public enum AftersaleErrorCode {

    /**
     * 某个异常
     */
    E600("退款金额不能大于支付金额"),
    E601("操作不允许"),
    E602("商品不存在"),
    E603("退款单不存在"),
    E604("订单不存在"),
    E605("退款方式必填"),
    E606("入库操作出错"),
    E607("申请售后货品数量不能大于购买数量"),
    E608("导出数据失败"),
    E609("可退款金额为0，无需申请退款/退货，请与平台联系解决"),
    E610("申请售后类型有误"),
    E611("申请售后退款账户信息有误"),
    E612("申请售后原因必填"),
    E613("申请售后描述必填"),
    E614("售后服务单不存在"),
    E615("物流信息有误"),
    E616("商家审核售后服务出错"),
    E617("申请售后服务的商品信息有误"),
    E618("参数错误")
    ;

    private String describe;

    AftersaleErrorCode(String des){
        this.describe =des;
    }

    /**
     * 获取异常码
     * @return
     */
    public String code(){
        return this.name().replaceAll("E","");
    }

    /**
     * 获取异常描述
     * @return
     */
    public String describe(){
        return this.describe;
    }


}
