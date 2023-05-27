package com.enation.app.javashop.core.client.distribution;

import com.enation.app.javashop.core.distribution.model.dos.CommissionTpl;

/**
 * 模版client
 * @author liushuai
 * @version v1.0
 * @since v7.0
 * 2018/8/14 下午2:17
 * @Description:
 *
 */ 

public interface CommissionTplClient {

    /**
     * 获取默认模版
     * @return
     */
    CommissionTpl getDefaultCommission();

}
