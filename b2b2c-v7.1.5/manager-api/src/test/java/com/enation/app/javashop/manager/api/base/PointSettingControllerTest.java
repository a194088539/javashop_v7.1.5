package com.enation.app.javashop.manager.api.base;

import com.enation.app.javashop.core.base.SettingGroup;
import com.enation.app.javashop.core.base.service.SettingManager;
import com.enation.app.javashop.core.system.model.vo.PointSetting;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 积分设置测试
 *
 * @author zh
 * @version v7.0
 * @date 18/6/5 下午1:44
 * @since v7.0
 */
@Transactional(value = "systemTransactionManager", rollbackFor = Exception.class)
public class PointSettingControllerTest extends BaseTest {

    List<MultiValueMap<String, String>> list = null;

    @Autowired
    private SettingManager settingManager;

    @Test
    public void editPointSettingTest() throws Exception {
        //参数校验
        String[] names = new String[]{"register", "register_consumer_point", "register_grade_point", "login", "login_consumer_point", "login_grade_point", "online_pay", "online_pay_consumer_point", "online_pay_grade_point", "comment_img", "comment_img_consumer_point", "comment_img_grade_point", "comment", "comment_consumer_point", "comment_grade_point", "first_comment", "first_comment_consumer_point", "first_comment_grade_point", "buy_goods", "buy_goods_consumer_point", "buy_goods_grade_point", "parities", "parities_point", "message"};
        String[] values1 = new String[]{"", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "是否开启注册获取积分不能为空"};
        String[] values2 = new String[]{"-1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "是否开启注册获取积分必须为数字且,1为开启,0为关闭"};
        String[] values3 = new String[]{"22", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "是否开启注册获取积分必须为数字且,1为开启,0为关闭"};
        String[] values4 = new String[]{"1", "", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "注册获取消费积分数不能为空"};
        String[] values5 = new String[]{"1", "-1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "注册获取消费积分数不能为负数"};
        String[] values6 = new String[]{"1", "1", "", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "注册获取等级积分数不能为空"};
        String[] values7 = new String[]{"1", "1", "-1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "注册获取等级积分数不能为负数"};
        String[] values8 = new String[]{"1", "1", "1", "", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "是否开启登录获取积分不能为空"};
        String[] values9 = new String[]{"1", "1", "1", "-1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "是否开启登录获取积分必须为数字且,1为开启,0为关闭"};
        String[] values10 = new String[]{"1", "1", "1", "22", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "是否开启登录获取积分必须为数字且,1为开启,0为关闭"};
        String[] values11 = new String[]{"1", "1", "1", "1", "", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "登录获取消费积分数不能为空"};
        String[] values12 = new String[]{"1", "1", "1", "1", "-1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "登录获取消费积分数不能为负数"};
        String[] values13 = new String[]{"1", "1", "1", "1", "1", "", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "登录获取等级积分数不能为空"};
        String[] values14 = new String[]{"1", "1", "1", "1", "1", "-1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "登录获取等级积分数不能为负数"};
        String[] values15 = new String[]{"1", "1", "1", "1", "1", "1", "", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "是否开启在线支付获取积分不能为空"};
        String[] values16 = new String[]{"1", "1", "1", "1", "1", "1", "-1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "是否开启在线支付获取积分必须为数字且,1为开启,0为关闭"};
        String[] values17 = new String[]{"1", "1", "1", "1", "1", "1", "22", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "是否开启在线支付获取积分必须为数字且,1为开启,0为关闭"};
        String[] values18 = new String[]{"1", "1", "1", "1", "1", "1", "1", "", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "在线支付获取消费积分数不能为空"};
        String[] values19 = new String[]{"1", "1", "1", "1", "1", "1", "1", "-1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "在线支付获取消费积分数不能为负数"};
        String[] values20 = new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "在线支付获取等级积分数不能为空"};
        String[] values21 = new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "-1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "在线支付获取等级积分数不能为负数"};
        String[] values29 = new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "是否开启图片评论获取积分不能为空"};
        String[] values30 = new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "-1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "是否开启图片评论获取积分必须为数字且,1为开启,0为关闭"};
        String[] values31 = new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "22", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "是否开启图片评论获取积分必须为数字且,1为开启,0为关闭"};
        String[] values32 = new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "图片评论获取消费积分不能为空"};
        String[] values33 = new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "-1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "图片评论获取消费积分数不能为负数"};
        String[] values34 = new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "图片评论获取等级积分数不能为空"};
        String[] values35 = new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "-1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "图片评论获取等级积分数不能为负数"};
        String[] values36 = new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "是否开启文字评论获取积分不能为空"};
        String[] values37 = new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "-1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "是否开启文字评论获取积分必须为数字且,1为开启,0为关闭"};
        String[] values38 = new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "22", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "是否开启文字评论获取积分必须为数字且,1为开启,0为关闭"};
        String[] values39 = new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "", "1", "1", "1", "1", "1", "1", "1", "1", "1", "文字评论获取消费积分数不能为空"};
        String[] values40 = new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "-1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "文字评论获取消费积分数不能为负数"};
        String[] values41 = new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "", "1", "1", "1", "1", "1", "1", "1", "1", "文字评论获取等级积分数不能为空"};
        String[] values42 = new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "-1", "1", "1", "1", "1", "1", "1", "1", "1", "文字评论获取等级积分数不能为负数"};
        String[] values43 = new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "", "1", "1", "1", "1", "1", "1", "1", "是否开启首次评论获取积分不能为空"};
        String[] values44 = new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "-1", "1", "1", "1", "1", "1", "1", "1", "是否开启首次评论获取积分必须为数字且,1为开启,0为关闭"};
        String[] values45 = new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "22", "1", "1", "1", "1", "1", "1", "1", "是否开启首次评论获取积分必须为数字且,1为开启,0为关闭"};
        String[] values46 = new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "", "1", "1", "1", "1", "1", "1", "首次评论获取消费积分数不能为空"};
        String[] values47 = new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "-1", "1", "1", "1", "1", "1", "1", "首次评论获取消费积分数不能为负数"};
        String[] values48 = new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "", "1", "1", "1", "1", "1", "首次评论获取等级积分数不能为空"};
        String[] values49 = new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "-1", "1", "1", "1", "1", "1", "首次评论获取等级积分数不能为负数"};
        String[] values50 = new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "", "1", "1", "1", "1", "是否开启购买商品获取积分不能为空"};
        String[] values51 = new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "-1", "1", "1", "1", "1", "是否开启购买商品获取积分必须为数字且,1为开启,0为关闭"};
        String[] values52 = new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "22", "1", "1", "1", "1", "是否开启购买商品获取积分必须为数字且,1为开启,0为关闭"};
        String[] values53 = new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "", "1", "1", "1", "购买商品获取消费积分数不能为空"};
        String[] values54 = new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "-1", "1", "1", "1", "购买商品获取消费积分数不能为负数"};
        String[] values55 = new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "", "1", "1", "购买商品获取等级积分数不能为空"};
        String[] values56 = new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "-1", "1", "1", "购买商品获取等级积分数不能为负数"};
        String[] values57 = new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "", "1", "是否开启人民币与积分兑换比例不能为空"};
        String[] values58 = new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "-1", "1", "是否开启人民币与积分兑换比例必须为数字且,1为开启,0为关闭"};
        String[] values59 = new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "22", "1", "是否开启人民币与积分兑换比例必须为数字且,1为开启,0为关闭"};
        String[] values60 = new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "", "人民币与积分兑换积分数不能为空"};
        String[] values61 = new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "-1", "人民币与积分兑换积分数不能为负数"};

        list = toMultiValueMaps(names, values1, values2, values3, values4, values5, values6, values7, values8, values9, values10, values11, values12, values13, values14, values15, values16, values17, values18, values19, values20, values21,
                values29, values30, values31, values32, values33, values34, values35, values36, values37, values38, values39, values40, values41, values42, values43, values44, values45, values46, values47, values48, values49, values50,
                values51, values52, values53, values54, values55, values56, values57, values58, values59, values60, values61);
        for (MultiValueMap<String, String> params : list) {
            String message = params.get("message").get(0);
            ErrorMessage error = new ErrorMessage("004", message);
            mockMvc.perform(put("/admin/settings/point")
                    .header("Authorization",superAdmin)
                    .params(params))
                    .andExpect(status().is(400))
                    .andExpect(objectEquals(error));
        }
        //正确测试
        PointSetting pointSetting = new PointSetting();
        pointSetting.setRegister(1);
        pointSetting.setRegisterConsumerPoint(1);
        pointSetting.setRegisterGradePoint(1);
        pointSetting.setLogin(1);
        pointSetting.setLoginConsumerPoint(1);
        pointSetting.setLoginGradePoint(1);
        pointSetting.setOnlinePay(1);
        pointSetting.setOnlinePayConsumerPoint(1);
        pointSetting.setOnlinePayGradePoint(1);
        pointSetting.setCommentImg(1);
        pointSetting.setCommentImgConsumerPoint(1);
        pointSetting.setCommentImgGradePoint(1);
        pointSetting.setComment(1);
        pointSetting.setCommentGradePoint(1);
        pointSetting.setCommentConsumerPoint(1);
        pointSetting.setFirstComment(1);
        pointSetting.setFirstCommentConsumerPoint(1);
        pointSetting.setFirstCommentGradePoint(1);
        pointSetting.setBuyGoods(1);
        pointSetting.setBuyGoodsConsumerPoint(1);
        pointSetting.setBuyGoodsGradePoint(1);
        pointSetting.setParities(1);
        pointSetting.setParitiesPoint(1);

        MultiValueMap<String, String> siteSettingMap = objectToMap(pointSetting);
        mockMvc.perform(put("/admin/settings/point/")
                .header("Authorization",superAdmin)
                .params(siteSettingMap))
                .andExpect(status().is(200));
        String pointSettingJson = settingManager.get( SettingGroup.POINT);
        PointSetting setting = JsonUtil.jsonToObject(pointSettingJson,PointSetting.class);
        MultiValueMap<String, String> dbSiteSettingMap = objectToMap(setting);
        Assert.assertEquals(siteSettingMap, dbSiteSettingMap);

    }
}
