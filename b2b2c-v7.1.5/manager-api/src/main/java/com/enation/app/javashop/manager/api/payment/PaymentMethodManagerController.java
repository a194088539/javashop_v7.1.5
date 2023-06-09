package com.enation.app.javashop.manager.api.payment;

import com.enation.app.javashop.core.payment.model.dos.PaymentMethodDO;
import com.enation.app.javashop.core.payment.model.vo.PaymentPluginVO;
import com.enation.app.javashop.core.payment.service.PaymentMethodManager;
import com.enation.app.javashop.framework.database.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

/**
 * 支付方式表控制器
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018-04-11 16:06:57
 */
@RestController
@RequestMapping("/admin/payment/payment-methods")
@Api(description = "支付方式表相关API")
public class PaymentMethodManagerController	{
	
	@Autowired
	private	PaymentMethodManager paymentMethodManager;
				

	@ApiOperation(value	= "查询支付方式列表", response = PaymentMethodDO.class)
	@ApiImplicitParams({
		 @ApiImplicitParam(name	= "page_no",	value =	"页码",	required = true, dataType = "int",	paramType =	"query"),
		 @ApiImplicitParam(name	= "page_size",	value =	"每页显示数量",	required = true, dataType = "int",	paramType =	"query")
	})
	@GetMapping
	public Page list(@ApiIgnore Integer pageNo,@ApiIgnore Integer pageSize)	{
		
		return	this.paymentMethodManager.list(pageNo,pageSize);
	}
	
	
	@ApiOperation(value	= "修改支付方式", response = PaymentMethodDO.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "payment_plugin_id",	value = "支付插件id", required = true, dataType = "string",	paramType = "path"),
			@ApiImplicitParam(name = "payment_method",	value = "支付方式对象", required = true, dataType = "PaymentPluginVO",	paramType = "body")
	})
	@PutMapping("/{payment_plugin_id}")
	public PaymentMethodDO add(@Valid @RequestBody @ApiIgnore  PaymentPluginVO paymentMethod,@PathVariable("payment_plugin_id") String paymentPluginId)	{

		return	this.paymentMethodManager.add(paymentMethod,paymentPluginId);
	}
	
	@GetMapping(value =	"/{plugin_id}")
	@ApiOperation(value	= "查询一个支付方式")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "plugin_id",	value = "要查询的支付插件id", required = true, dataType = "string",	paramType = "path")
	})
	public	PaymentPluginVO get(@PathVariable("plugin_id")	String	pluginId)	{

		return this.paymentMethodManager.getByPlugin(pluginId);
	}
				
}
