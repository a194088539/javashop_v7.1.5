package com.enation.app.javashop.buyer.api.payment;

import com.enation.app.javashop.core.payment.model.enums.ClientType;
import com.enation.app.javashop.core.payment.model.enums.PayMode;
import com.enation.app.javashop.core.payment.model.enums.TradeType;
import com.enation.app.javashop.core.payment.model.vo.PayBill;
import com.enation.app.javashop.core.payment.plugin.weixin.WeixinPayPlugin;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
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

import java.io.ByteArrayOutputStream;

/**
 * @author fk
 * @version v2.0
 * @Description: 订单支付
 * @date 2018/4/1616:44
 * @since v7.0.0
 */
@Api(description = "订单支付API")
@RestController
@RequestMapping("/order/pay")
@Validated
public class WeixinPayBuyerController {

    @Autowired
    private WeixinPayPlugin weixinPayPlugin;

    @ApiIgnore
    @ApiOperation(value = "显示一个微信二维码")
    @GetMapping(value = "/weixin/qr/{pr}")
    public byte[] qr(@PathVariable(name = "pr") String pr) throws Exception {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //图片的宽度
        int width = 200;
        //高度
        int height = 200;
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix m = writer.encode("weixin://wxpay/bizpayurl?pr=" + pr, BarcodeFormat.QR_CODE, height, width);
        MatrixToImageWriter.writeToStream(m, "png", stream);

        return stream.toByteArray();
    }



    @ApiOperation(value = "获取微信扫描支付的状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "weixin_trade_sn", value = "微信预付订单号", required = true, dataType = "String", paramType = "path"),
    })
    @GetMapping("/weixin/status/{weixin_trade_sn}")
    public String payStatus(@PathVariable(name = "weixin_trade_sn") String weixinTradeSn) {


        PayBill bill = new PayBill();
        bill.setBillSn(weixinTradeSn);
        bill.setClientType(ClientType.PC);
        bill.setTradeType(TradeType.trade);
        bill.setPayMode(PayMode.qr.name());
         String result  = weixinPayPlugin.onQuery(bill);
        return result;

    }

}
