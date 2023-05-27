package com.enation.app.javashop.buyer.api.promotion;

import com.enation.app.javashop.core.promotion.tool.model.vo.PromotionVO;
import com.enation.app.javashop.core.promotion.tool.service.PromotionGoodsManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 促销活动控制器
 *
 * @author Snow create in 2018/7/10
 * @version v2.0
 * @since v7.0.0
 */

@RestController
@RequestMapping("/promotions")
@Api(description = "促销活动相关API")
@Validated
public class PromotionBuyerController {

    @Autowired
    private PromotionGoodsManager promotionGoodsManager;

    @ApiOperation(value = "根据商品读取参与的所有活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "goods_id", value = "商品ID", required = true, dataType = "int", paramType = "path")
    })
    @GetMapping("/{goods_id}")
    public List<PromotionVO> getGoods(@ApiIgnore @PathVariable("goods_id") Integer goodsId){
        List<PromotionVO> promotionVOList = this.promotionGoodsManager.getPromotion(goodsId);
        return promotionVOList;
    }
}
