package com.enation.app.javashop.core.distribution.service.impl;

import com.enation.app.javashop.core.distribution.model.dos.BillTotalDO;
import com.enation.app.javashop.core.distribution.service.BillTotalManager;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 总结算单处理
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018/6/14 上午7:13
 * @Description:
 *
 */

@Service
public class BillTotalManagerImpl implements BillTotalManager {

    @Autowired
    @Qualifier("distributionDaoSupport")
    private DaoSupport daoSupport;

    @Override
    public Page page(int page, int pageSize) {
        return this.daoSupport.queryForPage("select * from es_bill_total", page, pageSize);
    }


    @Override
    public BillTotalDO add(BillTotalDO billTotal) {
        daoSupport.insert("es_bill_total", billTotal);
        return billTotal;

    }


    @Override
    public BillTotalDO getTotalByStart(Long startTime){
        return this.daoSupport.queryForObject("select * from es_bill_total where start_time = ? ", BillTotalDO.class,startTime);
    }


}
