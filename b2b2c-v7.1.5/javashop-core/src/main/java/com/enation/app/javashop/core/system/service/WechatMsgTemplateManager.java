package com.enation.app.javashop.core.system.service;

import com.enation.app.javashop.core.system.enums.WechatMsgTemplateTypeEnum;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.core.system.model.dos.WechatMsgTemplate;

/**
 * 微信服务消息模板业务层
 *
 * @author fk
 * @version v7.1.4
 * @since vv7.1.0
 * 2019-06-14 16:42:35
 */
public interface WechatMsgTemplateManager {

    /**
     * 查询微信服务消息模板列表
     *
     * @param page     页码
     * @param pageSize 每页数量
     * @return Page
     */
    Page list(int page, int pageSize);

    /**
     * 添加微信服务消息模板
     *
     * @param wechatMsgTemplate 微信服务消息模板
     * @return WechatMsgTemplate 微信服务消息模板
     */
    WechatMsgTemplate add(WechatMsgTemplate wechatMsgTemplate);

    /**
     * 修改微信服务消息模板
     *
     * @param wechatMsgTemplate 微信服务消息模板
     * @param id                微信服务消息模板主键
     * @return WechatMsgTemplate 微信服务消息模板
     */
    WechatMsgTemplate edit(WechatMsgTemplate wechatMsgTemplate, Integer id);

    /**
     * 删除微信服务消息模板
     *
     * @param id 微信服务消息模板主键
     */
    void delete(Integer id);

    /**
     * 获取微信服务消息模板
     *
     * @param id 微信服务消息模板主键
     * @return WechatMsgTemplate  微信服务消息模板
     */
    WechatMsgTemplate getModel(Integer id);

    /**
     * 同步微信模板消息
     * @return
     */
    boolean sycn();

    /**
     * 查看是否已经同步微信消息模板
     * @return
     */
    boolean isSycn();

    /**
     * 发送消息
     * @param openId
     * @param keyword
     * @param messageType
     */
    void send(String openId, WechatMsgTemplateTypeEnum messageType,Object... keyword);

}