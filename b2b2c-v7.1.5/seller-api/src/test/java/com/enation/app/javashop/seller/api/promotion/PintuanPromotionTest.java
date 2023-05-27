//package com.enation.app.javashop.seller.api.promotion;
//
//import com.enation.app.javashop.core.promotion.minus.model.vo.MinusVO;
//import com.enation.app.javashop.core.promotion.pintuan.model.Pintuan;
//import com.enation.app.javashop.core.promotion.pintuan.service.PintuanManager;
//import com.enation.app.javashop.core.statistics.util.DateUtil;
//import com.enation.app.javashop.framework.context.UserContext;
//import com.enation.app.javashop.framework.security.model.Seller;
//import com.enation.app.javashop.framework.test.BaseTest;
//import com.enation.app.javashop.framework.util.JsonUtil;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
///**
// * 拼团活动单元测试
// *
// * @author Chopper
// * @version v1.0
// * @since v7.0
// * 2019-02-26 下午2:47
// */
//public class PintuanPromotionTest extends BaseTest {
//
//
//    @Autowired
//    private PintuanManager pintuanManager;
//
//
//
//    public void testAdd(){
//        Pintuan pintuan = new Pintuan();
//        pintuan.setPromotionName("测试活动");
//        pintuan.setPromotionId(9527);
//        pintuan.setEnableMocker(1);
//        pintuan.setEndTime(DateUtil.getDateline()+10);
//        pintuan.setStartTime(DateUtil.getDateline()+5);
//        pintuan.setLimitNum(2);
//        pintuan.setPromotionTitle("测试标题");
//        pintuan.setRequiredNum(2);
//
//        //场景1: 正确添加
//        String resultJson = mockMvc.perform(post("/seller/promotion/minus")
//                .header("Authorization", seller1)
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .content(JsonUtil.objectToJson(map)))
//                .andExpect(status().is(200))
//                .andReturn().getResponse().getContentAsString();
//
//        MinusVO minusVO = JsonUtil.jsonToObject(resultJson, MinusVO.class);
//    }
//
//
//    /**
//     * 测试 开启/关闭 活动
//     * 思路，创建活动，然后调用开启方法，查看活动状态
//     * 关闭活动，查看活动状态
//     */
//    @Test
//    public void testOpenClosePromotion() {
//
//
//
//        Pintuan addpintuan = pintuanManager.add(pintuan);
//        pintuanManager.manualOpenPromotion(addpintuan.getPromotionId());
//
//        pintuanManager.add(pintuan);
//
//        pintuanManager.manualOpenPromotion(addpintuan.getPromotionId());
//
//    }
//
//
//}
