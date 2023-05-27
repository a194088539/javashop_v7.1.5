package com.enation.app.javashop.seller.api.promotion;

import com.enation.app.javashop.core.promotion.exchange.model.dos.ExchangeDO;
import com.enation.app.javashop.core.promotion.exchange.service.ExchangeGoodsManager;
import com.enation.app.javashop.core.promotion.tool.model.dto.PromotionGoodsDTO;
import com.enation.app.javashop.core.promotion.tool.service.PromotionGoodsManager;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.DateUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

/**
 * 积分商品测试
 * @author Snow create in 2018/7/5
 * @version v2.0
 * @since v7.0.0
 */
@Transactional(value = "tradeTransactionManager",rollbackFor = Exception.class)
public class ExchangeGoodsManagerTest extends BaseTest {

    @Autowired
    private ExchangeGoodsManager exchangeGoodsManager;

    private ExchangeDO exchangeDO;

    private PromotionGoodsDTO goodsDTO;

    @MockBean
    private PromotionGoodsManager promotionGoodsManager;


    @Before
    public void testData() {

        int goodsId = 1;

        exchangeDO = new ExchangeDO();
        exchangeDO.setGoodsId(goodsId);
        exchangeDO.setCategoryId(33);
        exchangeDO.setEnableExchange(1);
        exchangeDO.setExchangeMoney(0.0);
        exchangeDO.setExchangePoint(100);

        goodsDTO = new PromotionGoodsDTO();
        goodsDTO.setGoodsId(goodsId);
        goodsDTO.setGoodsName("商品名称");
        goodsDTO.setPrice(55.0);
        goodsDTO.setThumbnail("path");
        goodsDTO.setQuantity(100);
        goodsDTO.setSn(DateUtil.getDateline()+"");
    }

    @Test
    public void testAdd() throws Exception {
        this.exchangeGoodsManager.add(exchangeDO,goodsDTO);
        ExchangeDO exchangeD2 = this.exchangeGoodsManager.getModel(exchangeDO.getExchangeId());
        Assert.assertEquals(exchangeDO.getExchangePoint(),exchangeD2.getExchangePoint());
    }


    @Test
    public void testEdit() throws Exception {
        this.exchangeGoodsManager.add(exchangeDO,goodsDTO);
        exchangeDO.setExchangePoint(50);
        this.exchangeGoodsManager.edit(exchangeDO,goodsDTO);
        ExchangeDO exchangeD2 = this.exchangeGoodsManager.getModel(exchangeDO.getExchangeId());
        Assert.assertEquals(exchangeDO.getExchangePoint(),exchangeD2.getExchangePoint());
    }

}
