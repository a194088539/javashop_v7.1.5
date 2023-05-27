package com.enation.app.javashop.core.shop.model.vo;

import com.enation.app.javashop.core.shop.model.dos.ShopLogisticsSetting;
import com.enation.app.javashop.core.system.enums.KDNParamsEnum;
import com.enation.app.javashop.core.system.model.dos.LogisticsCompanyDO;
import com.enation.app.javashop.core.system.model.dto.FormItem;
import com.enation.app.javashop.core.system.model.dto.KDNParams;
import com.enation.app.javashop.core.system.model.dto.TextField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

/**
 * 店铺物流配置
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2019-05-23 下午2:25
 */
public class ShopLogisticsSettingVO {


    /**
     * 店铺配置，可能为空
     */
    private ShopLogisticsSetting shopLogisticsSetting;

    /**
     * 平台配置物流公司细节对象
     */
    private LogisticsCompanyDO logisticsCompanyDO;

    /**
     * 是否开启
     */
    private boolean isOpen;

    /**
     * 前端展示字段
     */
    private List<TextField> textFields;

    @JsonIgnore
    public ShopLogisticsSetting getShopLogisticsSetting() {
        return shopLogisticsSetting;
    }

    public void setShopLogisticsSetting(ShopLogisticsSetting shopLogisticsSetting) {
        this.isOpen = shopLogisticsSetting != null ? true : false;
        this.shopLogisticsSetting = shopLogisticsSetting;
        initTextfield();
    }


    private void initTextfield() {
        List<TextField> textFields = new ArrayList<>();
        //是否有表单内容判定
        if (logisticsCompanyDO.getForm() != null && logisticsCompanyDO.getForm().size() > 0) {

            for (FormItem formItem : logisticsCompanyDO.getForm()) {
                TextField textField = new TextField();
                textField.setLabel(formItem.getName());
                textField.setName(formItem.getCode());
                //判断是否需要填写value值
                if (shopLogisticsSetting != null) {
                    KDNParams kdnParams = shopLogisticsSetting.getParams();
                    if (kdnParams != null) {

                        if (textField.getName().equals(KDNParamsEnum.customer_name.name())) {
                            textField.setValue(kdnParams.getCustomerName());
                        }

                        if (textField.getName().equals(KDNParamsEnum.customer_pwd.name())) {
                            textField.setValue(kdnParams.getCustomerPwd());
                        }

                        if (textField.getName().equals(KDNParamsEnum.month_code.name())) {
                            textField.setValue(kdnParams.getMonthCode());
                        }

                        if (textField.getName().equals(KDNParamsEnum.send_site.name())) {
                            textField.setValue(kdnParams.getSendSite());
                        }

                        if (textField.getName().equals(KDNParamsEnum.send_staff.name())) {
                            textField.setValue(kdnParams.getSendStaff());
                        }
                    }
                }

                textFields.add(textField);
            }
        }
        this.setTextFields(textFields);

    }

    public LogisticsCompanyDO getLogisticsCompanyDO() {
        return logisticsCompanyDO;
    }

    public void setLogisticsCompanyDO(LogisticsCompanyDO logisticsCompanyDO) {
        this.logisticsCompanyDO = logisticsCompanyDO;
    }

    public List<TextField> getTextFields() {
        return textFields;
    }

    public void setTextFields(List<TextField> textFields) {
        this.textFields = textFields;
    }


    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }
}
