package com.enation.app.javashop.core.payment.plugin.weixin.exception;

import com.enation.app.javashop.core.statistics.StatisticsErrorCode;
import com.enation.app.javashop.framework.exception.ServiceException;
import org.springframework.http.HttpStatus;

public class WeixinSignatrueExceprion extends ServiceException {

    public WeixinSignatrueExceprion(String message){

        super(StatisticsErrorCode.E801.code(),message);
        this.statusCode= HttpStatus.INTERNAL_SERVER_ERROR;
        statusCode=HttpStatus.BAD_REQUEST;

    }
}
