package com.enation.app.javashop.core.client.system;

import java.util.List;
import java.util.Map;

/**
 * @author fk
 * @version v2.0
 * @Description: 管理员角色
 * @date 2018/9/26 14:10
 * @since v7.0.0
 */
public interface RoleClient {


    /**
     * 获取所有角色的权限对照表
     *
     * @return 权限对照表
     */
    Map<String, List<String>> getRoleMap();
}
