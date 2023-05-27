package com.enation.app.javashop.seller.api.promotion;

import com.enation.app.javashop.core.promotion.groupbuy.model.vo.GroupbuyActiveVO;
import com.enation.app.javashop.core.promotion.pintuan.model.PinTuanGoodsVO;
import com.enation.app.javashop.core.promotion.pintuan.model.Pintuan;
import com.enation.app.javashop.core.promotion.pintuan.model.PintuanGoodsDO;
import com.enation.app.javashop.core.promotion.pintuan.model.PintuanQueryParam;
import com.enation.app.javashop.core.promotion.pintuan.service.PintuanGoodsManager;
import com.enation.app.javashop.core.promotion.pintuan.service.PintuanManager;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;


/**
 * 拼团卖家控制器
 *
 * @author liushuai
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2019/1/28 下午2:41
 */
@RestController
@RequestMapping("/seller/promotion/pintuan")
@Api(description = "拼团")
@Validated
public class PintuanSellerController {

    @Autowired
    private PintuanManager pintuanManager;

    @Autowired
    private PintuanGoodsManager pintuanGoodsManager;

    @ApiOperation(value = "查询活动表列")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page_no", value = "页码", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "page_size", value = "每页显示数量", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "拼团活动名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "拼团活动名称", dataType = "String", paramType = "query",
                    allowableValues = "WAIT,UNDERWAY,END",example = "WAIT:待开始，UNDERWAY：进行中，END：已结束"),
            @ApiImplicitParam(name	= "start_time",	value =	"拼团日期-开始日期", dataType = "int",	paramType =	"query"),
            @ApiImplicitParam(name	= "end_time",	value =	"拼团日期-结束日期", dataType = "int",	paramType =	"query")
    })
    @GetMapping
    public Page list(@ApiIgnore Integer pageNo, @ApiIgnore Integer pageSize, @ApiIgnore String name,
                     @ApiIgnore String status, @ApiIgnore Long startTime, @ApiIgnore Long endTime) {
        PintuanQueryParam param = new PintuanQueryParam();
        param.setPageNo(pageNo);
        param.setPageSize(pageSize);
        param.setName(name);
        param.setSellerId(UserContext.getSeller().getSellerId());
        param.setStatus(status);
        param.setStartTime(startTime);
        param.setEndTime(endTime);
        Page<GroupbuyActiveVO> page = this.pintuanManager.list(param);
        return page;
    }


    @ApiOperation(value = "添加活动")
    @PostMapping
    public Pintuan add(@Valid Pintuan pintuan) {
        return this.pintuanManager.add(pintuan);
    }


    @ApiOperation(value = "修改活动参与的商品", response = PintuanGoodsDO.class)
    @PostMapping("/goods/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "活动ID", required = true, dataType = "int", paramType = "path")
    })
    public void savePromotionGoods(@RequestBody @Valid List<PintuanGoodsDO> pintuanGoodsDOS, @ApiIgnore @PathVariable Integer id) {
        this.pintuanGoodsManager.save(id, pintuanGoodsDOS);
    }


    @ApiOperation(value = "获取活动参与的商品", response = PintuanGoodsDO.class)
    @GetMapping("/goods/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "活动ID", required = true, dataType = "int", paramType = "path")
    })
    public List<PinTuanGoodsVO> promotionGoods(@ApiIgnore @PathVariable Integer id) {
        return this.pintuanGoodsManager.all(id);
    }


    @ApiOperation(value = "修改活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "int", paramType = "path")
    })
    @PutMapping(value = "/{id}")
    public Pintuan edit(@Valid Pintuan activeDO, @PathVariable Integer id) {
        this.pintuanManager.edit(activeDO, id);
        return activeDO;
    }


    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "要删除的活动表主键", required = true, dataType = "int", paramType = "path")
    })
    public void delete(@PathVariable Integer id) {
        this.pintuanManager.delete(id);

    }


    @GetMapping(value = "/{id}")
    @ApiOperation(value = "查询一个活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "要查询的活动表主键", required = true, dataType = "int", paramType = "path")
    })
    public Pintuan get(@PathVariable Integer id) {
        return this.pintuanManager.getModel(id);
    }


}
