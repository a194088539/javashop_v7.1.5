package com.enation.app.javashop.manager.api.promotion;

import com.enation.app.javashop.core.promotion.pintuan.model.PinTuanGoodsVO;
import com.enation.app.javashop.core.promotion.pintuan.model.Pintuan;
import com.enation.app.javashop.core.promotion.pintuan.model.PintuanQueryParam;
import com.enation.app.javashop.core.promotion.pintuan.service.PintuanGoodsManager;
import com.enation.app.javashop.core.promotion.pintuan.service.PintuanManager;
import com.enation.app.javashop.framework.database.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 拼团活动控制器
 *
 * @author liushuai
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2019/2/12 下午5:34
 */

@RestController
@RequestMapping("/admin/promotion/pintuan")
@Api(description = "拼团API")
@Validated
public class PintuanManagerController {

    @Autowired
    private PintuanManager pintuanManager;

    @Autowired
    private PintuanGoodsManager pintuanGoodsManager;

    @ApiOperation(value = "查询拼团列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page_no", value = "页码", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "page_size", value = "每页显示数量", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "拼团活动名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "seller_id", value = "商家ID", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "拼团活动名称", dataType = "String", paramType = "query",
                    allowableValues = "WAIT,UNDERWAY,END",example = "WAIT:待开始，UNDERWAY：进行中，END：已结束"),
            @ApiImplicitParam(name	= "start_time",	value =	"拼团日期-开始日期", dataType = "int",	paramType =	"query"),
            @ApiImplicitParam(name	= "end_time",	value =	"拼团日期-结束日期", dataType = "int",	paramType =	"query")
    })
    @GetMapping
    public Page<Pintuan> list(@ApiIgnore Integer pageNo, @ApiIgnore Integer pageSize, @ApiIgnore String name,
                              @ApiIgnore Integer sellerId, @ApiIgnore String status, @ApiIgnore Long startTime, @ApiIgnore Long endTime) {
        PintuanQueryParam param = new PintuanQueryParam();
        param.setPageNo(pageNo);
        param.setPageSize(pageSize);
        param.setName(name);
        param.setSellerId(sellerId);
        param.setStatus(status);
        param.setStartTime(startTime);
        param.setEndTime(endTime);

        return this.pintuanManager.list(param);
    }



    @ApiOperation(value = "获取活动参与的商品", response = PinTuanGoodsVO.class)
    @GetMapping("/goods/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "活动ID", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "page_no", value = "页码", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "page_size", value = "每页显示数量", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "名称", dataType = "String", paramType = "query")

    })
    public Page promotionGoods(@ApiIgnore @PathVariable Integer id,@ApiIgnore Integer pageNo, @ApiIgnore Integer pageSize, @ApiIgnore String name) {
        return this.pintuanGoodsManager.page(pageNo,pageSize,id,name);
    }


    @PutMapping(value = "/{id}/close")
    @ApiOperation(value = "关闭拼团")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "要关闭的拼团入库主键", required = true, dataType = "int", paramType = "path")
    })
    public void close(@PathVariable Integer id) {
        this.pintuanManager.manualClosePromotion(id);

    }

    @PutMapping(value = "/{id}/open")
    @ApiOperation(value = "开启拼团")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "要关闭的拼团入库主键", required = true, dataType = "int", paramType = "path")
    })
    public void open(@PathVariable Integer id) {

        this.pintuanManager.manualOpenPromotion(id);

    }


    @GetMapping(value = "/{id}")
    @ApiOperation(value = "查询一个拼团入库")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "要查询的拼团主键", required = true, dataType = "int", paramType = "path")
    })
    public Pintuan get(@PathVariable Integer id) {
        return this.pintuanManager.getModel(id);
    }


}
