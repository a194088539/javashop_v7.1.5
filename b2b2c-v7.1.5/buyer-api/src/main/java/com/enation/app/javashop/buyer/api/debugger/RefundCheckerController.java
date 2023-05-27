package com.enation.app.javashop.buyer.api.debugger;


import com.enation.app.javashop.core.payment.model.dos.PaymentBillDO;
import com.enation.app.javashop.core.payment.service.RefundManager;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * 退款debugger程序
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2019-04-25
 */
@Controller
@RequestMapping("/debugger/refund")
@ConditionalOnProperty(value = "javashop.debugger", havingValue = "true")
public class RefundCheckerController {

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    private RefundManager refundManager;

    @GetMapping(value = "/list")
    public String list(Model model) {

        List<PaymentBillDO> list = daoSupport.queryForList("select * from es_payment_bill order by bill_id desc limit 0,5", PaymentBillDO.class);
        model.addAttribute("billList", list);

        return "refund_list";
    }

    @GetMapping(value = "/test")
    public String test(String returnTradeNo,Double refundPrice ) {
        String refundSn = System.currentTimeMillis() + StringUtil.getRandStr(6);
        Map hashMap  =  refundManager.originRefund(returnTradeNo, refundSn, refundPrice);
        return hashMap.get("result").toString();
    }
}
