package com.enation.app.javashop.seller.api.shop;

import com.enation.app.javashop.core.shop.model.dos.ShopNoticeLogDO;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author zjp
 * @version v7.0
 * @Description 店铺消息测试类
 * @ClassName ShopNoticeLogControllerTest
 * @since v7.0 上午11:42 2018/7/10
 */
@Transactional(value = "memberTransactionManager",rollbackFor=Exception.class)
public class ShopNoticeLogControllerTest extends BaseTest {
    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport daoSupport;

    Integer id1 = 0;
    Integer id2 = 0;
    Integer id3 = 0;


    @Before
    public void insertData() {

        this.daoSupport.execute("delete from es_shop_notice_log");

        Map map = new HashMap();
        map.put("shop_id",3);
        map.put("is_delete",0);
        map.put("is_read",0);
        this.daoSupport.insert("es_shop_notice_log",map);
        id1 = this.daoSupport.getLastId("es_shop_notice_log");

        map.put("shop_id",3);
        map.put("is_delete",0);
        map.put("is_read",0);
        this.daoSupport.insert("es_shop_notice_log",map);
        id2 = this.daoSupport.getLastId("es_shop_notice_log");

        map.put("shop_id",3);
        map.put("is_delete",0);
        map.put("is_read",0);
        this.daoSupport.insert("es_shop_notice_log",map);
        id3 = this.daoSupport.getLastId("es_shop_notice_log");

    }

    @Test
    public void testDelete() throws Exception {
        //正确性校验
        ErrorMessage error  = new ErrorMessage("E214","物流公司不存在");
        mockMvc.perform(delete("/seller/shops/shop-notice-logs/"+id1+","+id2)
                .header("Authorization", seller1))
                .andExpect(status().is(200));
        String sql = "select * from es_shop_notice_log where id = ? ";
        ShopNoticeLogDO shopNoticeLogDO = this.daoSupport.queryForObject(sql, ShopNoticeLogDO.class, id1);
        Assert.assertEquals(shopNoticeLogDO.getIsDelete().toString(),"1");

    }

    @Test
    public void testRead() throws Exception {
        //正确性校验
        ErrorMessage error  = new ErrorMessage("E214","物流公司不存在");
        mockMvc.perform(put("/seller/shops/shop-notice-logs/"+id3+"/read")
                .header("Authorization", seller1))
                .andExpect(status().is(200));
        String sql = "select * from es_shop_notice_log where id = ? ";
        ShopNoticeLogDO shopNoticeLogDO = this.daoSupport.queryForObject(sql, ShopNoticeLogDO.class, id3);
        Assert.assertEquals(shopNoticeLogDO.getIsRead().toString(),"1");

    }
}
