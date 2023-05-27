package com.enation.app.javashop.manager.api.system;

import com.enation.app.javashop.core.goods.model.dos.GoodsDO;
import com.enation.app.javashop.core.goods.service.GoodsQueryManager;
import com.enation.app.javashop.core.system.model.vo.AppPushSetting;
import com.enation.app.javashop.core.system.service.PushManager;
import com.enation.app.javashop.framework.exception.ResourceNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotEmpty;

/**
 * 商品推送api
 *
 * @author zh
 * @version v7.0
 * @date 18/6/6 下午7:57
 * @since v7.0
 */
@RestController
@RequestMapping("/admin/systems/push")
@Api(description = "商品推送api")
@Validated
public class PushGoodsManagerController {

    @Autowired
    private PushManager pushManager;
    @Autowired
    private GoodsQueryManager goodsQueryManager;

    @GetMapping(value = "/{goods_id}")
    @ApiOperation(value = "商品推送", response = AppPushSetting.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "标题", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "goods_id", value = "商品id", required = true, dataType = "int", paramType = "path")
    })
    public String getPushSetting(@NotEmpty(message = "标题不能为空") String title, @PathVariable("goods_id") @ApiIgnore Integer goodsId) {
        GoodsDO goodsDO = goodsQueryManager.getModel(goodsId);
        if (goodsDO == null) {
            throw new ResourceNotFoundException("此商品不存在");
        }
        //推送Android端
        pushManager.pushGoodsForAndroid(title, goodsId);
        //推送ios端
        pushManager.pushGoodsForIOS(title, goodsId);
        return null;

    }

}
