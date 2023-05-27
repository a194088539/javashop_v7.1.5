package com.enation.app.javashop.core.system.model.vo;

import com.enation.app.javashop.framework.util.StringUtil;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fk
 * @version v7.1.4
 * @Description 微信服务消息data数据
 * @ClassName WechatMsgTemplateTypeEnum
 * @since v7.1
 */
public class WechatMsgData {

    /**
     * 抬头文字
     */
    private String firstMessage;

    /**
     * 抬头文字颜色
     */
    private String firstColor;

    /**
     * 备注文字
     */
    private String remarkMessage;

    /**
     * 备注文字颜色
     */
    private String remarkColor;

    /**
     * 关键字
     */
    private List<String> keywords = new ArrayList<>();

    /**
     * 关键字文字颜色
     */
    private List<String> colors = new ArrayList<>();


    /**
     * 添加抬头，不带颜色
     * @param firstMessage
     * @return
     */
    public WechatMsgData first(String firstMessage) {

        return first(firstMessage, "");
    }

    /**
     * 添加抬头，带颜色
     * @param firstMessage
     * @param firstColor
     * @return
     */
    public WechatMsgData first(String firstMessage, String firstColor) {

        this.firstMessage = firstMessage;
        this.firstColor = firstColor;

        return this;
    }

    /**
     * 添加备注，不带颜色
     * @param remarkMessage
     * @return
     */
    public WechatMsgData remark(String remarkMessage) {

        return remark(remarkMessage, "");
    }

    /**
     * 添加备注，带颜色
     * @param remarkMessage
     * @param remarkColor
     * @return
     */
    public WechatMsgData remark(String remarkMessage, String remarkColor) {

        this.remarkMessage = remarkMessage;
        this.remarkColor = remarkColor;

        return this;
    }

    /**
     * 添加关键字，不带颜色
     * @param keyword
     * @return
     */
    public WechatMsgData keywords(String keyword) {

        return keywords(keyword, "");
    }

    /**
     * 添加关键字，带颜色
     * @param keyword
     * @param color
     * @return
     */
    public WechatMsgData keywords(String keyword, String color) {

        this.keywords.add(keyword);
        this.colors.add(color);

        return this;
    }

    /**
     * 创建data数据
     * @return
     */
    public String createData() {

        String data = "";

        Map<String, Map<String, String>> dataMap = new HashMap<>();

        //拼接开头
        dataMap.put("first", createValue(this.firstMessage, this.firstColor));

        //拼接关键字
        for (int i = 0; i < keywords.size(); i++) {

            dataMap.put("keyword"+(i+1),createValue(this.keywords.get(i), this.colors.get(i)));
        }

        //拼接备注
        dataMap.put("remark", createValue(this.remarkMessage, this.remarkColor));


        return JSONObject.fromObject(dataMap).toString();
    }

    /**
     * 创建统一格式的map
     * @param msg
     * @param color
     * @return
     */
    private Map<String, String> createValue(String msg, String color) {

        Map<String, String> map = new HashMap<>();

        map.put("value", msg);
        if (!StringUtil.isEmpty(color)) {
            map.put("color", color);
        }
        return map;
    }

}
