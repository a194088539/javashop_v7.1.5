package com.enation.app.javashop.core.promotion.pintuan;

import com.enation.app.javashop.core.promotion.pintuan.model.Pintuan;
import com.enation.app.javashop.core.promotion.pintuan.service.PintuanManager;
import com.enation.app.javashop.core.promotion.pintuan.service.PintuanOrderManager;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

/**
 * PintuanOrderMockTest
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2019-03-08 下午2:53
 */

public class PintuanOrderMockTest extends BaseTest {


    @MockBean
    private PintuanManager pintuanManager;


    @Autowired
    private PintuanOrderManager pintuanOrderManager;


    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport tradeDaoSupport;


    //提前声明订单id
    int orderId = 1;


    //初始化数据
    public void initOrder() {
        tradeDaoSupport.execute(" TRUNCATE TABLE es_pintuan_order ");
        tradeDaoSupport.execute(" TRUNCATE TABLE es_pintuan_child_order ");
        tradeDaoSupport.execute(" TRUNCATE TABLE es_order ");
        tradeDaoSupport.execute(" TRUNCATE TABLE es_order_items ");



        tradeDaoSupport.execute(" INSERT INTO `es_pintuan_order` VALUES ('1', null, '168', '1552031600', '537', '2', '1', '[{\"id\":2,\"name\":\"javashop\",\"face\":\"http://javashop-statics.oss-cn-beijing.aliyuncs.com/v70/normal/912BDD3146AE4BE19831DB9F357A34D8.jpeg\",\"isMaster\":1}]', 'wait', '334', 'http://javashop-statics.oss-cn-beijing.aliyuncs.com/v70/goods/402E7E6F6BD24287AB32A8DCF214F848.jpg_300x300', 'LANCOME 兰蔻清滢保湿柔肤水大粉水400ml爽肤水女补水保湿补水滋润营养正品大瓶装 法国进口【最新版本】');");
        tradeDaoSupport.execute(" INSERT INTO `es_pintuan_child_order` VALUES ('1', '20190308000016', '2', '537', '168', 'pay_off', '1', 'javashop', '309.00', '111.00');");
        tradeDaoSupport.execute(" INSERT INTO `es_order` VALUES ('1', '20190308000016', '1', '平台自营24', '2', 'javashop', 'PAID_OFF', 'PAY_YES', 'SHIP_NO', '0', 'UNFINISHED', null, null, 'weixinPayPlugin', '微信', 'ONLINE', '1552031468', '116.00', '胜多负少的', '啥打法是否', null, '13695958585', '', '任意时间', '1', '72', '2799', '53119', '北京', '朝阳区', '三环以内', '唉唉唉啊啊', '116.00', '111.00', '5.00', '0.00', '0', '1.00', null, '', null, null, '[{\"seller_id\":1,\"seller_name\":\"平台自营24\",\"goods_id\":334,\"sku_id\":537,\"sku_sn\":\"000000000683626274\",\"cat_id\":383,\"num\":1,\"goods_weight\":1.0,\"original_price\":309.0,\"purchase_price\":111.0,\"subtotal\":111.0,\"name\":\"LANCOME 兰蔻清滢保湿柔肤水大粉水400ml爽肤水女补水保湿补水滋润营养正品大瓶装 法国进口【最新版本】\",\"goods_image\":\"http://javashop-statics.oss-cn-beijing.aliyuncs.com/v70/goods/402E7E6F6BD24287AB32A8DCF214F848.jpg_300x300\",\"spec_list\":null,\"point\":null,\"snapshot_id\":2607,\"service_status\":\"NOT_APPLY\",\"single_list\":null,\"group_list\":[],\"goods_operate_allowable_vo\":null,\"promotion_tags\":[],\"purchase_num\":0}]', null, '116.00', null, '89', null, null, null, null, '1552031431', null, null, null, 'EXPIRED', null, null, 'WAP', '20190308000016', '1', 'pintuan', '{\\\"pintuan\\\":\\\"{\\\\\\\"owesPersonNums\\\\\\\":1}\\\"}');");
        tradeDaoSupport.execute(" INSERT INTO `es_order_items` VALUES ('1', '334', '537', '1', null, '20190308000016', '20190308000016', 'http://javashop-statics.oss-cn-beijing.aliyuncs.com/v70/goods/402E7E6F6BD24287AB32A8DCF214F848.jpg_300x300', 'LANCOME 兰蔻清滢保湿柔肤水大粉水400ml爽肤水女补水保湿补水滋润营养正品大瓶装 法国进口【最新版本】', '111.00', '383', null, '2607', 'null', null, null);");
    }

    /**
     * 虚拟成团关闭测试
     */
    @Test
    public void testMockPintuan() {
        initOrder();
        disableMockPintuan();

        pintuanOrderManager.handle(orderId);
    }

    /**
     * 禁用虚拟成团拼团
     */
    public void disableMockPintuan() {

        Pintuan pintuan = new Pintuan();

        pintuan.setPromotionId(1);
        pintuan.setRequiredNum(2);
        pintuan.setLimitNum(2);
        pintuan.setEnableMocker(0);


        when(pintuanManager.getModel(anyInt())).thenReturn(pintuan);

    }


}
