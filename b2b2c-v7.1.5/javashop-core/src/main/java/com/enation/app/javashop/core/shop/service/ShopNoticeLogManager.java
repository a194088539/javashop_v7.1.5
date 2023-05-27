package com.enation.app.javashop.core.shop.service;

import com.enation.app.javashop.core.shop.model.dos.ShopNoticeLogDO;
import com.enation.app.javashop.framework.database.Page;

/**
 * 店铺站内消息业务层
 *
 * @author zjp
 * @version v7.0.0
 * @since v7.0.0
 * 2018-07-10 10:21:45
 */
public interface ShopNoticeLogManager {

    /**
     * 查询店铺站内消息列表
     *
     * @param page     页码
     * @param pageSize 每页数量
     * @param type     类型
     * @param isRead   1 已读，0 未读
     * @return Page
     */
    Page list(int page, int pageSize, String type, Integer isRead);

    /**
     * 添加店铺站内消息
     *
     * @param shopNoticeLog 店铺站内消息
     * @return ShopNoticeLog 店铺站内消息
     */
    ShopNoticeLogDO add(ShopNoticeLogDO shopNoticeLog);

    /**
     * 删除历史消息
     *
     * @param ids
     */
    void delete(Integer[] ids);

    /**
     * 设置已读
     *
     * @param ids 消息id
     */
    void read(Integer[] ids);

}