package com.enation.app.javashop.core.trade.complain.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.core.trade.complain.model.dos.OrderComplainCommunication;
import com.enation.app.javashop.core.trade.complain.service.OrderComplainCommunicationManager;

import java.util.List;

/**
 * 交易投诉对话表业务类
 * @author fk
 * @version v2.0
 * @since v2.0
 * 2019-11-29 10:46:34
 */
@Service
public class OrderComplainCommunicationManagerImpl implements OrderComplainCommunicationManager{

	@Autowired
	@Qualifier("tradeDaoSupport")
	private	DaoSupport	daoSupport;


	@Override
	public List<OrderComplainCommunication> list(int complainId) {

		String sql = "select * from es_order_complain_communication where complain_id = ? order by create_time asc";

		return this.daoSupport.queryForList(sql,OrderComplainCommunication.class,complainId);
	}

	@Override
	@Transactional(value = "tradeTransactionManager",propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public	OrderComplainCommunication  add(OrderComplainCommunication	orderComplainCommunication)	{
		this.daoSupport.insert(orderComplainCommunication);

		orderComplainCommunication.setCommunicationId(this.daoSupport.getLastId(""));
		return orderComplainCommunication;
	}
	
}
