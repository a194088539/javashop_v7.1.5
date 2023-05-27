package com.enation.app.javashop.core.aftersale.service.impl;

import com.enation.app.javashop.core.aftersale.model.dos.AfterSaleLogDO;
import com.enation.app.javashop.core.aftersale.service.AfterSaleLogManager;
import com.enation.app.javashop.core.statistics.util.DateUtil;
import com.enation.app.javashop.framework.database.DaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 售后日志业务接口实现类
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-10-16
 */
@Service
public class AfterSaleLogManagerImpl implements AfterSaleLogManager {

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    @Override
    public void add(String serviceSn, String logDetail, String operator) {
        AfterSaleLogDO afterSaleLogDO = new AfterSaleLogDO();
        afterSaleLogDO.setSn(serviceSn);
        afterSaleLogDO.setLogDetail(logDetail);
        afterSaleLogDO.setOperator(operator);
        afterSaleLogDO.setLogTime(DateUtil.getDateline());
        this.daoSupport.insert(afterSaleLogDO);
    }

    @Override
    public List<AfterSaleLogDO> list(String serviceSn) {
        String sql = "select * from es_as_log where sn = ? order by log_time desc";
        return this.daoSupport.queryForList(sql, AfterSaleLogDO.class, serviceSn);
    }
}
