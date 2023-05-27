package com.enation.app.javashop.manager.api.promotion;

import com.enation.app.javashop.core.promotion.groupbuy.model.dos.GroupbuyActiveDO;
import com.enation.app.javashop.core.promotion.groupbuy.model.dos.GroupbuyGoodsDO;
import com.enation.app.javashop.core.promotion.groupbuy.model.enums.GroupBuyGoodsStatusEnum;
import com.enation.app.javashop.core.promotion.groupbuy.model.vo.GroupbuyGoodsVO;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.JsonUtil;
import net.sf.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 平台—团购商品测试脚本
 *
 * @author Snow create in 2018/6/14
 * @version v2.0
 * @since v7.0.0
 */
@Transactional(value = "tradeTransactionManager",rollbackFor = Exception.class)
public class GroupbuyGoodsControllerTest extends BaseTest {

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    private GroupbuyActiveDO activeDO;

    private GroupbuyGoodsDO goodsDO;

    @Before
    public void testData(){

        activeDO = new GroupbuyActiveDO();
        activeDO.setActName("团购活动详情");
        activeDO.setAddTime(DateUtil.getDateline());
        activeDO.setEndTime(2834947200l);
        activeDO.setStartTime(2334947200l);
        this.daoSupport.insert(activeDO);
        int actId = this.daoSupport.getLastId("es_groupbuy_active");
        activeDO.setActId(actId);

        goodsDO = new GroupbuyGoodsDO();
        goodsDO.setSellerId(3);
        goodsDO.setActId(actId);
        goodsDO.setGbTitle("团购商品11");
        goodsDO.setGbStatus(GroupBuyGoodsStatusEnum.PENDING.status());
        this.daoSupport.insert(goodsDO);
        int id =this.daoSupport.getLastId("es_groupbuy_goods");
        goodsDO.setGbId(id);
    }


    @Test
    public void testPage() throws Exception {

        mockMvc.perform(get("/admin/promotion/group-buy-goods")
                .header("Authorization",superAdmin)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("page_no","1").param("page_size","10")
                .param("act_id",activeDO.getActId()+""))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

    }


    @Test
    public void testOne() throws Exception {

        String json = mockMvc.perform(get("/admin/promotion/group-buy-goods/"+goodsDO.getGbId())
                .header("Authorization",superAdmin)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("gb_id",goodsDO.getGbId()+""))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

//        GroupbuyGoodsVO goodsDO2 = JsonUtil.jsonToObject(json,GroupbuyGoodsVO.class);

        JSONObject object = JSONObject.fromObject(json);
        Assert.assertEquals(goodsDO.getGbTitle(),object.getString("gb_title"));
    }


}
