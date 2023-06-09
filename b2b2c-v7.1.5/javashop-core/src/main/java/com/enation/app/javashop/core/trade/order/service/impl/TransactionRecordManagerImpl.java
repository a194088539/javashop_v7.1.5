package com.enation.app.javashop.core.trade.order.service.impl;

import com.enation.app.javashop.core.trade.order.model.dos.TransactionRecord;
import com.enation.app.javashop.core.trade.order.service.TransactionRecordManager;
import com.enation.app.javashop.framework.database.DaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 交易记录表业务类
 * @author Snow
 * @version v7.0.0
 * @since v7.0.0
 * 2018-05-25 15:37:56
 */
@Service
public class TransactionRecordManagerImpl implements TransactionRecordManager {

	@Autowired
	@Qualifier("tradeDaoSupport")
	private	DaoSupport	daoSupport;

	@Override
	public List listAll(String orderSn){

		String sql = "select * from es_transaction_record  where order_sn = ?";
		List list  = this.daoSupport.queryForList(sql,TransactionRecord.class,orderSn);

		return list;
	}

	@Override
	@Transactional(value = "tradeTransactionManager",propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public	TransactionRecord  add(TransactionRecord	transactionRecord)	{
		this.daoSupport.insert(transactionRecord);
		return transactionRecord;
	}

	@Override
	@Transactional(value = "tradeTransactionManager",propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public	TransactionRecord  edit(TransactionRecord	transactionRecord,Integer id){
		this.daoSupport.update(transactionRecord, id);
		return transactionRecord;
	}

	@Override
	@Transactional(value = "tradeTransactionManager",propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public	void delete( Integer id)	{
		this.daoSupport.delete(TransactionRecord.class,	id);
	}

	@Override
	public	TransactionRecord getModel(Integer id)	{
		return this.daoSupport.queryForObject(TransactionRecord.class, id);
	}
}
