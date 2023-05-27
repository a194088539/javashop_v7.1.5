package com.enation.app.javashop.core.client.member;

import com.enation.app.javashop.core.shop.model.vo.ShipTemplateVO;

import java.util.List;

/**
 * @version v7.0
 * @Description: 店铺运费模版Client默认实现
 * @Author: zjp
 * @Date: 2018/7/25 16:20
 */
public interface ShipTemplateClient {
    /**
     * 获取运费模版
     * @param id 运费模版主键
     * @return ShipTemplate  运费模版
     */
    ShipTemplateVO get(Integer id);

    /**
     * 获取运费模板的脚本
     * @param id
     * @return
     */
    List<String> getScripts(Integer id);

    /**
     * 新增运费模板的时候，生成script缓存到redis
     *
     * @param shipTemplateVO 运费模板
     */
    void cacheShipTemplateScript(ShipTemplateVO shipTemplateVO);
}
