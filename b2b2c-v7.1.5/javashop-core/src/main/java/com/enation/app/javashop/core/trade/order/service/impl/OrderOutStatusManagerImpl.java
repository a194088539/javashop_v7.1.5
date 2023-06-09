package com.enation.app.javashop.core.trade.order.service.impl;

import com.enation.app.javashop.core.trade.order.model.dos.OrderOutStatus;
import com.enation.app.javashop.core.trade.order.model.enums.OrderOutStatusEnum;
import com.enation.app.javashop.core.trade.order.model.enums.OrderOutTypeEnum;
import com.enation.app.javashop.core.trade.order.service.OrderOutStatusManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;

/**
 * 订单出库状态业务类
 * @author xlp
 * @version v2.0
 * @since v7.0.0
 * 2018-07-10 14:06:38
 */
@Service
public class OrderOutStatusManagerImpl implements OrderOutStatusManager {

	@Autowired
	@Qualifier("tradeDaoSupport")
	private	DaoSupport	daoSupport;

	@Override
	public Page list(int page,int pageSize){

		String sql = "select * from es_order_out_status  ";
		Page  webPage = this.daoSupport.queryForPage(sql,page, pageSize ,OrderOutStatus.class );

		return webPage;
	}

	@Override
	@Transactional(value = "",propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public	OrderOutStatus  add(OrderOutStatus	orderOutStatus)	{
		this.daoSupport.insert(orderOutStatus);
		return orderOutStatus;
	}

	@Override
	@Transactional(value = "",propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public	void   edit(String orderSn, OrderOutTypeEnum typeEnum, OrderOutStatusEnum statusEnum){
		String sql = "update es_order_out_status set out_status =? where order_sn=? and out_type=?";
		this.daoSupport.execute(sql,statusEnum.name(),orderSn,typeEnum.name());
	}

	@Override
	@Transactional(value = "",propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public	void delete( Integer id)	{

		this.daoSupport.delete(OrderOutStatus.class,	id);
	}


	@Override
	public	OrderOutStatus getModel(String orderSn, OrderOutTypeEnum typeEnum)	{
		String sql = "select * from es_order_out_status where order_sn=? and out_type=?";
		return this.daoSupport.queryForObject(sql,OrderOutStatus.class, orderSn,typeEnum);
	}

}
