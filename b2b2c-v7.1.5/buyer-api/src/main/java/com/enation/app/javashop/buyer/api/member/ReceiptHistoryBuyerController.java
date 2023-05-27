package com.enation.app.javashop.buyer.api.member;

import com.enation.app.javashop.core.member.MemberErrorCode;
import com.enation.app.javashop.core.member.model.dos.ReceiptHistory;
import com.enation.app.javashop.core.member.model.dto.HistoryQueryParam;
import com.enation.app.javashop.core.member.model.vo.ReceiptHistoryVO;
import com.enation.app.javashop.core.member.service.ReceiptHistoryManager;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.exception.NoPermissionException;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.security.model.Buyer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

/**
 * 会员开票历史记录API
 *
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-09-16
 */
@Api(description = "会员开票历史记录API")
@RestController
@RequestMapping("/members/receipt/history")
@Validated
public class ReceiptHistoryBuyerController {

    @Autowired
    private ReceiptHistoryManager receiptHistoryManager;

    @ApiOperation(value = "查询会员开票历史记录信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page_no", value = "页数", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "page_size", value = "条数", dataType = "int", paramType = "query"),
    })
    @GetMapping()
    public Page list(@Valid HistoryQueryParam params, @ApiIgnore Integer pageNo, @ApiIgnore Integer pageSize) {
        Buyer buyer = UserContext.getBuyer();
        if (buyer == null) {
            throw new ServiceException(MemberErrorCode.E137.code(), "当前会员登录信息已经失效");
        }

        params.setMemberId(buyer.getUid());

        Page page = this.receiptHistoryManager.list(pageNo, pageSize, params);
        return page;
    }

    @ApiOperation(value = "查询会员开票历史记录详细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "history_id", value = "主键ID", required = true, dataType = "int", paramType = "path")
    })
    @GetMapping("/{history_id}")
    public ReceiptHistoryVO get(@PathVariable("history_id") Integer historyId) {
        Buyer buyer = UserContext.getBuyer();
        if (buyer == null) {
            throw new ServiceException(MemberErrorCode.E137.code(), "当前会员登录信息已经失效");
        }

        ReceiptHistoryVO receiptHistoryVO = this.receiptHistoryManager.get(historyId);

        if (receiptHistoryVO.getMemberId().intValue() != buyer.getUid().intValue()) {
            throw new ServiceException(MemberErrorCode.E136.code(), "没有操作权限");
        }

        return receiptHistoryVO;
    }

    @GetMapping(value = "/order/{order_sn}")
    @ApiOperation(value = "根据订单sn查询订单发票信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order_sn", value = "订单sn", required = true, dataType = "String", paramType = "path")
    })
    public ReceiptHistory getReceiptByOrderSn(@PathVariable("order_sn") String orderSn) {
        ReceiptHistory receiptHistory = this.receiptHistoryManager.getReceiptHistory(orderSn);
        if (receiptHistory != null && receiptHistory.getMemberId().equals(UserContext.getBuyer().getUid())) {
            return receiptHistory;
        }
        throw new NoPermissionException("无权限");

    }
}
