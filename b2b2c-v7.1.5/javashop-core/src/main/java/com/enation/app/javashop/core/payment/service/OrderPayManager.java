package com.enation.app.javashop.core.payment.service;

import com.enation.app.javashop.core.payment.model.dto.PayParam;
import com.enation.app.javashop.core.payment.model.vo.Form;

/**
 * @author fk
 * @version v2.0
 * @Description: 订单支付
 * @date 2018/4/1617:09
 * @since v7.0.0
 */
public interface OrderPayManager {

    /**
     * 支付
     *
     * @param param
     * @return
     */
    Form pay(PayParam param);



}
