package com.enation.app.javashop.seller.api.member;

import com.enation.app.javashop.core.client.trade.OrderClient;
import com.enation.app.javashop.core.member.MemberErrorCode;
import com.enation.app.javashop.core.member.model.dto.HistoryQueryParam;
import com.enation.app.javashop.core.member.model.vo.ReceiptFileVO;
import com.enation.app.javashop.core.member.model.vo.ReceiptHistoryVO;
import com.enation.app.javashop.core.member.service.ReceiptHistoryManager;
import com.enation.app.javashop.core.trade.order.model.vo.OrderDetailVO;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.security.model.Seller;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
@RequestMapping("/seller/members/receipts")
@Validated
public class ReceiptSellerController {

    @Autowired
    private ReceiptHistoryManager receiptHistoryManager;

    @Autowired
    private OrderClient orderClient;

    @ApiOperation(value = "查询会员开票历史记录信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page_no", value = "页数", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "page_size", value = "条数", dataType = "int", paramType = "query"),
    })
    @GetMapping()
    public Page list(@Valid HistoryQueryParam params, @ApiIgnore Integer pageNo, @ApiIgnore Integer pageSize) {
        Seller seller = UserContext.getSeller();
        if (seller == null) {
            throw new ServiceException(MemberErrorCode.E137.code(), "当前商家登录信息已经失效");
        }

        params.setSellerId(seller.getSellerId());

        Page page = this.receiptHistoryManager.list(pageNo, pageSize, params);
        return page;
    }

    @ApiOperation(value = "查询会员开票历史记录详细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "history_id", value = "主键ID", required = true, dataType = "int", paramType = "path")
    })
    @GetMapping("/{history_id}")
    public ReceiptHistoryVO get(@PathVariable("history_id") Integer historyId) {
        Seller seller = UserContext.getSeller();
        if (seller == null) {
            throw new ServiceException(MemberErrorCode.E137.code(), "当前商家登录信息已经失效");
        }

        ReceiptHistoryVO receiptHistoryVO = this.receiptHistoryManager.get(historyId);

        if (receiptHistoryVO.getSellerId().intValue() != seller.getSellerId().intValue()) {
            throw new ServiceException(MemberErrorCode.E136.code(), "没有操作权限");
        }

        OrderDetailVO orderDetailVO = this.orderClient.getOrderVO(receiptHistoryVO.getOrderSn());
        receiptHistoryVO.setOrderStatus(orderDetailVO.getOrderStatus());

        return receiptHistoryVO;
    }

    @ApiOperation(value = "商家开具发票-增值税普通发票和增值税专用发票")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "history_id", value = "主键ID", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "logi_id", value = "物流公司ID", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "logi_name", value = "物流公司名称", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "logi_code", value = "快递单号", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/{history_id}/logi")
    public void updateLogi(@PathVariable("history_id") Integer historyId, Integer logiId, String logiName, String logiCode) {

        this.receiptHistoryManager.updateLogi(historyId, logiId, logiName, logiCode);
    }

    @ApiOperation(value = "商家开具发票-上传电子普通发票附件")
    @PostMapping(value = "/upload/files")
    public void uploadFiles(@Valid @RequestBody ReceiptFileVO receiptFileVO) {

        this.receiptHistoryManager.uploadFiles(receiptFileVO);
    }
}
