package com.enation.app.javashop.core.aftersale.service.impl;

import com.enation.app.javashop.core.aftersale.model.dos.AfterSaleChangeDO;
import com.enation.app.javashop.core.aftersale.service.AfterSaleChangeManager;
import com.enation.app.javashop.framework.database.DaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 售后服务退货地址业务接口实现
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-12-03
 */
@Service
public class AfterSaleChangeManagerImpl implements AfterSaleChangeManager {

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    @Override
    public void add(AfterSaleChangeDO afterSaleChangeDO) {
        this.daoSupport.insert(afterSaleChangeDO);
    }

    @Override
    public AfterSaleChangeDO fillChange(String serviceSn, AfterSaleChangeDO afterSaleChangeDO) {
        afterSaleChangeDO.setServiceSn(serviceSn);
        this.add(afterSaleChangeDO);
        return afterSaleChangeDO;
    }

    @Override
    public AfterSaleChangeDO getModel(String serviceSn) {
        String sql = "select * from es_as_change where service_sn = ?";
        return this.daoSupport.queryForObject(sql, AfterSaleChangeDO.class, serviceSn);
    }
}
