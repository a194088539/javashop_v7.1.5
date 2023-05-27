package com.enation.app.javashop.core.client.system.impl;

import com.enation.app.javashop.core.client.system.RoleClient;
import com.enation.app.javashop.core.system.service.RoleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author fk
 * @version v2.0
 * @Description:
 * @date 2018/9/26 14:12
 * @since v7.0.0
 */
@Service
@ConditionalOnProperty(value="javashop.product", havingValue="stand")
public class RoleClientDefaultImpl implements RoleClient {

    @Autowired
    private RoleManager roleManager;

    @Override
    public Map<String, List<String>> getRoleMap() {

        return roleManager.getRoleMap();
    }
}
