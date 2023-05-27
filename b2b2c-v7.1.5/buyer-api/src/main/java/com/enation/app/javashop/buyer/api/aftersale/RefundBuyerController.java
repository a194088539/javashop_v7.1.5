package com.enation.app.javashop.buyer.api.aftersale;

import com.enation.app.javashop.core.aftersale.model.dto.RefundQueryParam;
import com.enation.app.javashop.core.aftersale.model.enums.CreateChannelEnum;
import com.enation.app.javashop.core.aftersale.model.vo.RefundRecordVO;
import com.enation.app.javashop.core.aftersale.service.AfterSaleRefundManager;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 售后退款相关API
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-10-24
 */
@Api(description="售后退款相关API")
@RestController
@RequestMapping("/buyer/after-sales/refund")
@Validated
public class RefundBuyerController {

    @Autowired
    private AfterSaleRefundManager afterSaleRefundManager;

    @ApiOperation(value = "获取售后退款单列表", response = RefundRecordVO.class)
    @GetMapping()
    public Page list(@Valid RefundQueryParam param){
        param.setMemberId(UserContext.getBuyer().getUid());
        param.setCreateChannel(CreateChannelEnum.NORMAL.value());
        return afterSaleRefundManager.list(param);
    }

}
