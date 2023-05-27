package com.enation.app.javashop.core.promotion.exchange.service.impl;

import com.enation.app.javashop.core.goods.GoodsErrorCode;
import com.enation.app.javashop.core.promotion.PromotionErrorCode;
import com.enation.app.javashop.core.promotion.exchange.model.dos.ExchangeCat;
import com.enation.app.javashop.core.promotion.exchange.service.ExchangeCatManager;
import com.enation.app.javashop.core.promotion.exchange.service.ExchangeGoodsManager;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 积分兑换分类业务类
 * @author Snow
 * @version v7.0.0
 * @since v7.0.0
 * 2018-05-29 16:56:22
 */
@Service
public class ExchangeCatManagerImpl implements ExchangeCatManager {

	@Autowired
	@Qualifier("tradeDaoSupport")
	private	DaoSupport	daoSupport;

	@Autowired
	private ExchangeGoodsManager exchangeGoodsManager;

	@Override
	public List<ExchangeCat> list(Integer parentId){

		String sql = "select * from es_exchange_cat where parent_id = ? order by category_order asc ";
		List<ExchangeCat> list = this.daoSupport.queryForList(sql,ExchangeCat.class,parentId);
		return list;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public	ExchangeCat  add(ExchangeCat exchangeCat) {

		String sql = "select * from es_exchange_cat where name = ?";
		List list = this.daoSupport.queryForList(sql, exchangeCat.getName());
		if(list.size() > 0){
			throw new ServiceException(PromotionErrorCode.E407.code(),"积分分类名称重复");
		}

		if(exchangeCat.getParentId()==null){
			exchangeCat.setParentId(0);
		}
		if(exchangeCat.getListShow()==null){
			exchangeCat.setListShow(1);
		}
		this.daoSupport.insert(exchangeCat);

		return exchangeCat;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public	ExchangeCat  edit(ExchangeCat	exchangeCat,Integer id){

		String sql = "select * from es_exchange_cat where name = ? and category_id != ? ";
		List list = this.daoSupport.queryForList(sql, exchangeCat.getName(),id);
		if(list.size() > 0){
			throw new ServiceException(PromotionErrorCode.E407.code(),"积分分类名称重复");
		}


		if(exchangeCat.getParentId()==null){
			exchangeCat.setParentId(0);
		}
		if(exchangeCat.getListShow()==null){
			exchangeCat.setListShow(1);
		}
		this.daoSupport.update(exchangeCat, id);
		return exchangeCat;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public void delete( Integer id) {
		if (exchangeGoodsManager.getModelByCategoryId(id) != null){
			throw new ServiceException(GoodsErrorCode.E300.code(), "此类别下存在商品不能删除");
		}
		this.daoSupport.delete(ExchangeCat.class,	id);
	}

	@Override
	public	ExchangeCat getModel(Integer id)	{

		return this.daoSupport.queryForObject(ExchangeCat.class, id);

	}
}
