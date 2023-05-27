package com.enation.app.javashop.buyer.api.pagedata;

import com.enation.app.javashop.core.goods.service.GoodsQueryManager;
import com.enation.app.javashop.core.pagedata.model.PageData;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.test.BaseTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author fk
 * @version v1.0
 * @Description: 楼层数据单元测试
 * @date 2018/6/15 14:30
 * @since v7.0.0
 */
@Transactional(value = "systemTransactionManager", rollbackFor = Exception.class)
public class PageDataControllerTest extends BaseTest {

    @Autowired
    @Qualifier("systemDaoSupport")
    private DaoSupport daoSupport;

    @MockBean
    private GoodsQueryManager goodsQueryManager;

    String pageData = "[     {         \"tpl_id\":23,         \"blockList\":[             {                 \"block_type\":\"IMAGE\",                 \"block_value\":\"http://javashop-statics.oss-cn-beijing.aliyuncs.com/demo/56256727.jpg\"             }         ]     },     {         \"tpl_id\":24,         \"blockList\":[             {                 \"block_type\":\"GOODS\",                 \"block_value\": {                     \"goods_id\":75,                     \"goods_name\":\"促销商品K\",                     \"sn\":\"0074\",                     \"goods_image\":\"http://javashop-statics.oss-cn-beijing.aliyuncs.com/demo/E1EE42BA506F4A2895B449F661D1644B.jpg_300x300\",                     \"enable_quantity\":100,                     \"quantity\":100,                     \"price\":100.0,                     \"create_time\":1517815032,                     \"market_enable\":1,                     \"brand_name\":\"蜜饯\",                     \"category_name\":\"蜜饯\"                 }             }         ]     },     {         \"tpl_id\":27,         \"blockList\":[             {                 \"block_type\":\"IMAGE\",                 \"block_value\":\"http://javashop-statics.oss-cn-beijing.aliyuncs.com/test/null/4D928A6DABE74EF5A78C2A2E2F5764F7.GIF\",                 \"block_opt\":{                     \"opt_type\":\"keyword\",                     \"opt_value\":\"关键字\",                     \"opt_detail\":\"这是个关键字类型\"                 }             },             {                 \"block_type\":\"IMAGE\",                 \"block_value\":\"\"             }         ]     } ]";


    @Before
    public void insertTestData() {

        String sql = "delete from es_page";
        this.daoSupport.execute(sql);

        PageData data = new PageData();
        data.setClientType("PC");
        data.setPageType("INDEX");
        data.setPageName("首页");
        data.setPageData(pageData);
        this.daoSupport.insert(data);
    }

    /**
     * 添加或修改楼层数据
     *
     * @throws Exception
     */
    @Test
    public void testAdd() throws Exception {

//        CacheGoods goods = new CacheGoods();
//        goods.setGoodsId(75);
//        goods.setName("好商品");
//        goods.setDisabled(1);
//
//        when (goodsQueryManager.getFromCache(75)).thenReturn(goods);
//
//        String res = mockMvc.perform(get("/pages/PC/INDEX"))
//                .andExpect(status().is(200))
//                .andReturn().getResponse().getContentAsString();
//
//        PageData page = JsonUtil.jsonToObject(res,PageData.class);
//
//        Assert.assertThat(page.getPageData(),containsString("\"goods_name\":\"好商品\""));


    }


}
