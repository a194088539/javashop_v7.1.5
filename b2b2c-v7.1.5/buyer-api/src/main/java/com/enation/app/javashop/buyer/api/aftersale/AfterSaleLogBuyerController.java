package com.enation.app.javashop.buyer.api.aftersale;

import com.enation.app.javashop.core.aftersale.model.dos.AfterSaleLogDO;
import com.enation.app.javashop.core.aftersale.service.AfterSaleLogManager;
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

import java.util.List;

/**
 * 售后服务日志相关API
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-11-27
 */
@Api(description="售后服务相关API")
@RestController
@RequestMapping("/after-sales/log")
@Validated
public class AfterSaleLogBuyerController {

    @Autowired
    private AfterSaleLogManager afterSaleLogManager;

    @ApiOperation(value = "获取售后服务操作日志信息", response = AfterSaleLogDO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "service_sn", value = "售后服务单号", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping(value = "/{service_sn}")
    public List<AfterSaleLogDO> list(@PathVariable("service_sn") String serviceSn){

        return afterSaleLogManager.list(serviceSn);
    }
}
