package com.enation.app.javashop.buyer.api.promotion;

import com.enation.app.javashop.core.goods.model.vo.GoodsSkuVO;
import com.enation.app.javashop.core.goods.service.GoodsSkuManager;
import com.enation.app.javashop.core.promotion.pintuan.model.PintuanOrderDetailVo;
import com.enation.app.javashop.core.promotion.pintuan.model.PtGoodsDoc;
import com.enation.app.javashop.core.promotion.pintuan.service.PinTuanSearchManager;
import com.enation.app.javashop.core.promotion.pintuan.service.PintuanOrderManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * Created by kingapex on 2019-01-30.
 *
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2019-01-30
 */
@Api(description = "拼团订单API")
@RestController
@RequestMapping("/pintuan")
public class PinTuanOrderController {

    @Autowired
    private PintuanOrderManager pintuanOrderManager;

    @Autowired
    private PinTuanSearchManager pinTuanSearchManager;


    @Autowired
    private GoodsSkuManager goodsSkuManager;

    @GetMapping("/orders/{order_sn}")
    @ApiOperation(value = "获取某个拼团的详细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order_sn", value = "order_sn", required = true, dataType = "String", paramType = "path")

    })
    public PintuanOrderDetailVo detail(@ApiIgnore @PathVariable(name = "order_sn") String orderSn) {

        PintuanOrderDetailVo pintuanOrder = pintuanOrderManager.getMainOrderBySn(orderSn);

        return pintuanOrder;
    }

    @GetMapping("/orders/{order_sn}/guest")
    @ApiOperation(value = "猜你喜欢")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order_sn", value = "order_sn", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "num", value = "显示数量", required = true, dataType = "String", paramType = "path")

    })
    public List<PtGoodsDoc> guest(@ApiIgnore @PathVariable(name = "order_sn") String orderSn, Integer num) {

        try {
            PintuanOrderDetailVo pintuanOrder = pintuanOrderManager.getMainOrderBySn(orderSn);
            GoodsSkuVO skuVO = goodsSkuManager.getSkuFromCache(pintuanOrder.getSkuId());

            // 准备默认值
            if(num==null){
                num=10;
            }
            return pinTuanSearchManager.search(skuVO.getCategoryId(), 1, num);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
