package com.enation.app.javashop.core.trade.order.service.impl;

import com.enation.app.javashop.core.client.goods.GoodsClient;
import com.enation.app.javashop.core.client.member.MemberAddressClient;
import com.enation.app.javashop.core.client.member.MemberClient;
import com.enation.app.javashop.core.member.model.dos.MemberAddress;
import com.enation.app.javashop.core.payment.model.enums.ClientType;
import com.enation.app.javashop.core.trade.cart.model.enums.CheckedWay;
import com.enation.app.javashop.core.trade.cart.model.vo.CartView;
import com.enation.app.javashop.core.trade.cart.service.CartReadManager;
import com.enation.app.javashop.core.trade.order.model.vo.CheckoutParamVO;
import com.enation.app.javashop.core.trade.order.model.vo.TradeVO;
import com.enation.app.javashop.core.trade.order.service.*;
import com.enation.app.javashop.framework.util.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * 交易业务类
 *
 * @author Snow create in 2018/4/8
 * @version v2.0
 * @since v7.0.0
 *
 * @version 2.1
 * 使用TradeCreator来创建交易
 */
@Service
@Primary
public class TradeManagerImpl implements TradeManager {
    protected final Log logger = LogFactory.getLog(this.getClass());
 

    @Autowired
    protected CheckoutParamManager checkoutParamManager;

    @Autowired
    protected CartReadManager cartReadManager;

    @Autowired
    protected ShippingManager shippingManager;

    @Autowired
    protected GoodsClient goodsClient;


    @Autowired
    protected TradeSnCreator tradeSnCreator;

    @Autowired
    protected MemberAddressClient memberAddressClient;

    @Autowired
    protected MemberClient memberClient;

    @Autowired
    protected TradeIntodbManager tradeIntodbManager;


    @Override
    public TradeVO createTrade(String client, CheckedWay way) {

        this.setClientType(client);
        CheckoutParamVO param = checkoutParamManager.getParam();
        CartView cartView = this.cartReadManager.getCheckedItems(way);
        MemberAddress memberAddress = this.memberAddressClient.getModel(param.getAddressId());

        TradeCreator tradeCreator = new DefaultTradeCreator(param,cartView,memberAddress).setTradeSnCreator( tradeSnCreator).setGoodsClient(goodsClient).setMemberClient(memberClient).setShippingManager(shippingManager);

        //检测配置范围-> 检测商品合法性 -> 检测促销活动合法性 -> 创建交易
        TradeVO tradeVO = tradeCreator.checkShipRange().checkGoods().checkPromotion().createTrade();

        //订单入库
        this.tradeIntodbManager.intoDB(tradeVO,way);

        return tradeVO;
    }


    /**
     * 设置client type
     * @param client
     */
    protected void setClientType(String client) {

        //如果客户端类型为空，那么需要判断是否是wap访问
        if (StringUtil.isEmpty(client) && StringUtil.isWap()) {
            client = "WAP";
        }

        ClientType type = ClientType.valueOf(client);

        this.checkoutParamManager.setClientType(type.getClient());
    }

}
