package com.enation.app.javashop.core.client.member;

import java.util.List;
import java.util.Map;

/**
 * @author fk
 * @version v2.0
 * @Description:
 * @date 2018/8/17 14:42
 * @since v7.0.0
 */
public interface ShopRoleClient {

    /**
     * 获取所有角色的权限对照表
     *
     * @return 权限对照表
     */
    Map<String, List<String>> getRoleMap(Integer sellerId);

}
