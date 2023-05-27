package com.enation.app.javashop.core.trade.complain.model.enums;

/**
 * sku的投诉状态
 *
 * @author fk
 * @version v2.0
 * @since v2.0
 * 2019-12-02 16:48:27
 */
public enum ComplainSkuStatusEnum {

    /**
     * 未申请，此时可申请
     */
    NO_APPLY,

    /**
     * 申请中，此时不可申请，可链接查看
     */
    APPLYING,

    /**
     * 已完成，此时可申请
     */
    COMPLETE,

    /**
     * 已失效，不可申请
     */
    EXPIRED;

}
