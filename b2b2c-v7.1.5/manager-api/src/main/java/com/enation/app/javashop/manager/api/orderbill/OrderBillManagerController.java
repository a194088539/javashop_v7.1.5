package com.enation.app.javashop.manager.api.orderbill;

import com.enation.app.javashop.core.goods.model.enums.Permission;
import com.enation.app.javashop.core.orderbill.constraint.annotation.BillItemType;
import com.enation.app.javashop.core.orderbill.model.dos.Bill;
import com.enation.app.javashop.core.orderbill.model.vo.BillDetail;
import com.enation.app.javashop.core.orderbill.model.vo.BillExcel;
import com.enation.app.javashop.core.orderbill.model.vo.BillQueryParam;
import com.enation.app.javashop.core.orderbill.service.BillItemManager;
import com.enation.app.javashop.core.orderbill.service.BillManager;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.util.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author fk
 * @version v1.0
 * @Description: 结算单账单控制器
 * @date 2018/4/27 11:49
 * @since v7.0.0
 */
@RestController
@RequestMapping("/admin/order/bills")
@Api(description = "结算相关API")
@Validated
public class OrderBillManagerController {

    @Autowired
    private BillManager billManager;

    @Autowired
    private BillItemManager billItemManager;

    @ApiOperation(value = "管理员查询所有周期结算单列表统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page_no", value = "页码", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "page_size", value = "每页数量", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "sn", value = "账单号", required = false, dataType = "String", paramType = "query")
    })
    @GetMapping("/statistics")
    public Page getAllBill(@ApiIgnore Integer pageNo, @ApiIgnore Integer pageSize, @ApiIgnore String sn){

        Page page = this.billManager.getAllBill(pageNo, pageSize, sn);

        return page;
    }


    @ApiOperation(value = "管理员查询某周期结算列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page_no", value = "页码", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "page_size", value = "每页数量", required = false, dataType = "int", paramType = "query")
    })
    @GetMapping
    public Page getBillDetail(@ApiIgnore Integer pageNo, @ApiIgnore Integer pageSize, BillQueryParam param){

        param.setPageNo(pageNo);
        param.setPageSize(pageSize);

        return  this.billManager.queryBills(param);
    }


    @ApiOperation(value = "查看某账单详细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bill_id", value = "结算单id", required = true, dataType = "int", paramType = "path"),
    })
    @GetMapping("/{bill_id}")
    public BillDetail queryBill(@PathVariable("bill_id") Integer billId) {

        return  this.billManager.getBillDetail(billId, Permission.ADMIN);
    }

    @ApiOperation(value = "导出某账单详细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bill_id", value = "结算单id", required = true, dataType = "int", paramType = "path"),
    })
    @GetMapping("/{bill_id}/export")
    public BillExcel exportBill(@PathVariable("bill_id") Integer billId) {

        return  this.billManager.exportBill(billId);
    }


    @ApiOperation(value = "对账单进行下一步操作")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bill_id", value = "账单id", required = true, dataType = "int", paramType = "path"),
    })
    @PutMapping(value = "/{bill_id}/next")
    public Bill nextBill(@PathVariable("bill_id")Integer billId) {

        Bill bill = this.billManager.editStatus(billId, Permission.ADMIN);

        return bill;
    }

    @ApiOperation(value = "查看账单中的订单列表或者退款单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page_no", value = "页码", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "page_size", value = "每页数量", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "bill_id", value = "账单id", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "bill_type", value = "账单类型", required = true, dataType = "string", paramType = "path", allowableValues = "REFUND,PAYMENT"),
    })
    @GetMapping("/{bill_id}/{bill_type}")
    public Page queryBillItems(@ApiIgnore Integer pageNo, @ApiIgnore Integer pageSize, @PathVariable("bill_id") Integer billId,@BillItemType @PathVariable("bill_type") String billType) {

        return this.billItemManager.list(pageNo, pageSize, billId, billType);
    }

    @GetMapping("/init")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "start_time", value = "开始时间 2018-08-21 19:52:21", required = true, dataType = "string",paramType = "query"),
            @ApiImplicitParam(name = "end_time", value = "结束时间 2018-08-21 20:04:50", required = true, dataType = "string",paramType = "query"),
    })
    public String createBill(@ApiIgnore String startTime,@ApiIgnore String endTime){

        this.billManager.createBills(DateUtil.getDateline(startTime,"yyyy-MM-dd HH:mm:ss"),DateUtil.getDateline(endTime,"yyyy-MM-dd HH:mm:ss"));

        return "";
    }
}
