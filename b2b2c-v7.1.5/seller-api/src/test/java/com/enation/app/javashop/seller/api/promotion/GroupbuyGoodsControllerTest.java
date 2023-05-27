package com.enation.app.javashop.seller.api.promotion;

import com.enation.app.javashop.core.goods.model.dos.GoodsDO;
import com.enation.app.javashop.core.promotion.groupbuy.model.dos.GroupbuyActiveDO;
import com.enation.app.javashop.core.promotion.groupbuy.model.dos.GroupbuyGoodsDO;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.exception.SystemErrorCodeV1;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.JsonUtil;
import net.sf.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 团购商品测试类
 *
 * @author Snow create in 2018/4/24
 * @version v2.0
 * @since v7.0.0
 */
@Transactional(value = "tradeTransactionManager",rollbackFor = Exception.class)
public class GroupbuyGoodsControllerTest extends BaseTest {

    /** 正确数据  */
    private Map successData = new HashMap();

    /** 错误数据—参数验证  */
    private List<MultiValueMap<String, String>> errorDataList = new ArrayList();

    /** 错误数据—逻辑错误  */
    private List<MultiValueMap<String, String>> errorDataTwoList = new ArrayList();

    private GroupbuyActiveDO activeDO;

    @Autowired
    @Qualifier("goodsDaoSupport")
    private DaoSupport goodsDaoSupport;

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    private List<Integer> goodsIds = new ArrayList<>();


    @Before
    public void testData() {

        GoodsDO goodsDO = new GoodsDO();
        goodsDO.setGoodsName("团购测试商品名称");
        goodsDO.setSn(DateUtil.getDateline()+"");
        goodsDO.setSellerId(3);
        goodsDO.setThumbnail("http://测试图片");
        this.goodsDaoSupport.insert(goodsDO);
        int goodsId = this.goodsDaoSupport.getLastId("es_goods");
        goodsDO.setGoodsId(goodsId);
        goodsIds.add(goodsId);

        //模拟团购活动信息
        activeDO = new GroupbuyActiveDO();
        activeDO.setActName("测试团购活动");
        activeDO.setAddTime(1527609600l);
        activeDO.setEndTime(4683283200l);
        activeDO.setJoinEndTime(4651747200l);
        this.daoSupport.insert(activeDO);
        int  id = this.daoSupport.getLastId("es_groupbuy_active");
        activeDO.setActId(id);

        String[] names = new String[]{"act_id", "cat_id", "gb_name", "gb_title", "goods_name", "goods_id","sku_id","original_price","price","img_url",
                "goods_num", "visual_num","limit_num","remark","message"};

        String[] values0 = new String[]{""+activeDO.getActId(),"1","团购活动主标题","副标题",goodsDO.getGoodsName(),goodsDO.getGoodsId()+"","2","100","50","img_path","100", "10","1","描述","正确添加",};

        for(int i  = 0; i<names.length-1; i++){
            successData.put(names[i],values0[i]);
        }

        String[] values1 = new String[]{"","1","团购活动主标题","副标题","商品名称1","1","2","100","50","img_path", "100", "10","1","描述","请选择要参与的团购活动"};
        String[] values2 = new String[]{""+activeDO.getActId(),"","团购活动主标题","副标题","商品名称1","1","2","100","50","img_path", "100", "10","1","描述","请选择团购分类"};
        String[] values3 = new String[]{""+activeDO.getActId(),"1","","副标题","商品名称1","1","2","100","50","img_path", "100", "10","1","描述","请填写团购名称"};
        String[] values5 = new String[]{""+activeDO.getActId(),"1","团购活动主标题","副标题","","1","2","100","50","img_path", "100", "10","1","描述","请选择商品"};
        String[] values6 = new String[]{""+activeDO.getActId(),"1","团购活动主标题","副标题","商品名称1","","2","100","50","img_path", "100", "10","1","描述","请选择商品"};
        String[] values8 = new String[]{""+activeDO.getActId(),"1","团购活动主标题","副标题","商品名称1","1","2","","50","img_path", "100", "10","1","描述","请选择商品"};
        String[] values9 = new String[]{""+activeDO.getActId(),"1","团购活动主标题","副标题","商品名称1","1","2","100","","img_path", "100", "10","1","描述","请输入团购价格"};
        String[] values10 = new String[]{""+activeDO.getActId(),"1","团购活动主标题","副标题","商品名称1","1","2","100","50","", "100", "10","1","描述","请上传团购图片"};
        String[] values11 = new String[]{""+activeDO.getActId(),"1","团购活动主标题","副标题","商品名称1","1","2","100","50","img_path", "", "10","1","描述","请输入商品总数"};
        String[] values12 = new String[]{""+activeDO.getActId(),"1","团购活动主标题","副标题","商品名称1","1","2","100","50","img_path", "100", "","1","描述","请输入虚拟数量"};
        String[] values13 = new String[]{""+activeDO.getActId(),"1","团购活动主标题","副标题","商品名称1","1","2","100","50","img_path", "100", "10","","描述","请输入限购数量"};

        errorDataList = toMultiValueMaps(names,values1,values2,values3,values5,values6,values8,values9,values10,values11,values12,values13);

        String[] values14 = new String[]{""+activeDO.getActId(),"1","团购活动主标题阿萨德发斯蒂芬骄傲和福建水电费哈市将的看法和萨芬1231231","副标题","商品名称1","1","2","100","50","img_path", "100", "10","1","描述","团购名称字数超限"};
        errorDataTwoList = toMultiValueMaps(names,values14);

    }


    @Test
    public void testAdd() throws Exception {

        Map map;
        //场景1: 正确添加
        map = successData;
        String resultJson = mockMvc.perform(post("/seller/promotion/group-buy-goods")
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.objectToJson(map)))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        JSONObject groupbuyGoods = JSONObject.fromObject(resultJson);
        //GroupbuyGoodsDO goodsDO = JsonUtil.jsonToObject(resultJson,GroupbuyGoodsDO.class);

        //验证是否正确添加
        mockMvc.perform(get("/seller/promotion/group-buy-goods/"+groupbuyGoods.get("gb_id"))
                .header("Authorization",seller1))
                .andExpect(status().is(200))
                .andExpect(jsonPath("gb_name").value("团购活动主标题"));


        //场景2：参数为空的验证
        for (MultiValueMap<String,String> params  : errorDataList){
            String message =  params.get("message").get(0);
            ErrorMessage error  = new ErrorMessage(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER,message);

            map = new HashMap<>();
            for (String key : params.keySet()){
                map.put(key,params.get(key).get(0));
            }

            mockMvc.perform(post("/seller/promotion/group-buy-goods")
                    .header("Authorization",seller1)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(JsonUtil.objectToJson(map)))
                    .andExpect(status().is(400))
                    .andExpect(  objectEquals( error ));
        }


        //场景3：参数异常验证
        for (MultiValueMap<String,String> params  : errorDataTwoList){
            String message =  params.get("message").get(0);
            ErrorMessage error  = new ErrorMessage("004",message);

            map = new HashMap<>();
            for (String key : params.keySet()){
                map.put(key,params.get(key).get(0));
            }

            mockMvc.perform(post("/seller/promotion/group-buy-goods")
                    .header("Authorization",seller1)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(JsonUtil.objectToJson(map)))
                    .andExpect(status().is(500))
                    .andExpect(  objectEquals( error ));
        }
    }


    @Test
    public void testEdit() throws Exception {
        GroupbuyGoodsDO goodsDO = this.add();
        goodsDO.setGbName("修改过的团购名称");

        //越权操作
        ErrorMessage error  = new ErrorMessage(SystemErrorCodeV1.NO_PERMISSION,"无权操作");
        mockMvc.perform(put("/seller/promotion/group-buy-goods/"+goodsDO.getGbId())
                .header("Authorization",seller2)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.objectToJson(goodsDO)))
                .andExpect(status().is(401))
                .andExpect(  objectEquals( error ));


        //场景1：正确修改
        String resultJson = mockMvc.perform(put("/seller/promotion/group-buy-goods/"+goodsDO.getGbId())
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.objectToJson(goodsDO)))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        //GroupbuyGoodsDO resultGoodsDO = JsonUtil.jsonToObject(resultJson,GroupbuyGoodsDO.class);
        //验证是否正确添加
        mockMvc.perform(get("/seller/promotion/group-buy-goods/"+goodsDO.getGbId())
                .header("Authorization",seller1))
                .andExpect(status().is(200))
                .andExpect(jsonPath("gb_name").value(goodsDO.getGbName()));
    }


    @Test
    public void testDelete() throws Exception {

        GroupbuyGoodsDO goodsDO = this.add();

        //越权操作
        ErrorMessage error  = new ErrorMessage(SystemErrorCodeV1.NO_PERMISSION,"无权操作");
        mockMvc.perform(delete("/seller/promotion/group-buy-goods/"+goodsDO.getGbId())
                .header("Authorization",seller2)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(401))
                .andExpect(  objectEquals( error ));


        //正确删除
        mockMvc.perform(delete("/seller/promotion/group-buy-goods/"+goodsDO.getGbId())
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

    }


    @Test
    public void testGetOne() throws Exception {

        GroupbuyGoodsDO goodsDO = this.add();

        //场景一
        String resultJson = mockMvc.perform(get("/seller/promotion/group-buy-goods/"+goodsDO.getGbId())
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        JSONObject groupbuyGoods = JSONObject.fromObject(resultJson);

//        GroupbuyGoodsVO resultGoodsVO= JsonUtil.jsonToObject(resultJson,GroupbuyGoodsVO.class);
        Assert.assertEquals(goodsDO.getGbName(),groupbuyGoods.getString("gb_name"));

        //场景2
        ErrorMessage error  = new ErrorMessage(SystemErrorCodeV1.NO_PERMISSION,"无权操作");
        mockMvc.perform(get("/seller/promotion/group-buy-goods/"+goodsDO.getGbId())
                .header("Authorization",seller2)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(401))
                .andExpect(  objectEquals( error ));
    }


    @Test
    public void testGetPage() throws Exception {
        this.add();
        //场景一
        String resultJson = mockMvc.perform(get("/seller/promotion/group-buy-goods")
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("page_no","1").param("page_size","10")
                .param("keywords","团购").param("status","0"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

    }


    @After
    public void cleanDate(){
        for (Integer goodsId:goodsIds){
            this.goodsDaoSupport.execute("delete from es_goods where goods_id = ?",goodsId);
        }
    }


    /**
     * 公共添加方法
     * @return
     * @throws Exception
     */
    private GroupbuyGoodsDO add() throws Exception {
        GroupbuyGoodsDO goodsDO ;
        //场景1: 正确添加
        String resultJson = mockMvc.perform(post("/seller/promotion/group-buy-goods")
                .header("Authorization",seller1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.objectToJson(successData)))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        JSONObject groupbuyGoods = JSONObject.fromObject(resultJson);
        groupbuyGoods.remove("show_buy_num");

        goodsDO = JsonUtil.jsonToObject(groupbuyGoods.toString(),GroupbuyGoodsDO.class);

        return goodsDO;
    }



}
