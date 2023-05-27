package com.enation.app.javashop.core.system.model.dto;

/**
 * 快递鸟参数
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2019-05-21 下午2:51
 */
public class KDNParams {

    /**
     * 都是快递鸟所需参数，具体意义见快递鸟api
     */
    private String CustomerName;
    private String CustomerPwd;
    private String MonthCode;
    private String SendSite;
    private String SendStaff;


    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getCustomerPwd() {
        return CustomerPwd;
    }

    public void setCustomerPwd(String customerPwd) {
        CustomerPwd = customerPwd;
    }

    public String getMonthCode() {
        return MonthCode;
    }

    public void setMonthCode(String monthCode) {
        MonthCode = monthCode;
    }

    public String getSendSite() {
        return SendSite;
    }

    public void setSendSite(String sendSite) {
        SendSite = sendSite;
    }

    public String getSendStaff() {
        return SendStaff;
    }

    public void setSendStaff(String sendStaff) {
        SendStaff = sendStaff;
    }
}
