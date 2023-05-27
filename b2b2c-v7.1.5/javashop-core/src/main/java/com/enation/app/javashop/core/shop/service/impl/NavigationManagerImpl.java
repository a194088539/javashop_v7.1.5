package com.enation.app.javashop.core.shop.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.security.model.Seller;
import com.enation.app.javashop.core.shop.ShopErrorCode;
import com.enation.app.javashop.core.shop.model.dos.NavigationDO;
import com.enation.app.javashop.core.shop.service.NavigationManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 店铺导航管理业务类
 * @author zjp
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-28 20:44:54
 */
@Service
public class NavigationManagerImpl implements NavigationManager{

	@Autowired
	@Qualifier("memberDaoSupport")
	private	DaoSupport	daoSupport;
	
	@Override
	public Page list(int page,int pageSize,Integer shopId){
		String sql = "select * from es_navigation where shop_id = ? ";
		Page  webPage = this.daoSupport.queryForPage(sql,page, pageSize ,NavigationDO.class, shopId);
		return webPage;
	}
	
	@Override
	@Transactional(value = "memberTransactionManager",propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public	NavigationDO  add(NavigationDO	navigation)	{
		int sum = this.getNavSum(navigation.getShopId());
		if (sum >= 10) {
			throw new ServiceException(ShopErrorCode.E231.name(), "店铺导航最多允许添加10个");
		}
		this.daoSupport.insert(navigation);
		int lastId = this.daoSupport.getLastId("es_navigation");
		navigation.setId(lastId);
		return navigation;
	}
	
	@Override
	@Transactional(value = "memberTransactionManager",propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public	NavigationDO  edit(NavigationDO	navigation,Integer id){
		Seller seller = UserContext.getSeller();
		NavigationDO model = this.getModel(id);
		if(model==null||!seller.getSellerId().equals(model.getShopId())) {
			throw new ServiceException(ShopErrorCode.E209.name(), "导航不存在，不能进行编辑操作");
		}
		navigation.setShopId(seller.getSellerId());
		this.daoSupport.update(navigation, id);
		return navigation;
	}
	
	@Override
	@Transactional(value = "memberTransactionManager",propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public	void delete( Integer id)	{
		Seller seller = UserContext.getSeller();
		NavigationDO model = this.getModel(id);
		if(model==null||!seller.getSellerId().equals(model.getShopId())) {
			throw new ServiceException(ShopErrorCode.E209.name(), "导航不存在，不能进行删除操作");
		}
		this.daoSupport.delete(NavigationDO.class,	id);
	}
	
	@Override
	public	NavigationDO getModel(Integer id)	{
		return this.daoSupport.queryForObject(NavigationDO.class, id);
	}

	@Override
	public List<NavigationDO> list(Integer shopId,Boolean isShow) {
		StringBuffer sql = new StringBuffer("select * from es_navigation where shop_id = ? ");
		List<Object> terms = new ArrayList<>();

		terms.add(shopId);
		if(isShow != null && isShow){
			sql.append(" and disable = ? ");
			terms.add(1);
		}



		sql.append("order by sort desc ");
		return this.daoSupport.queryForList(sql.toString(), NavigationDO.class, terms.toArray());
	}

	/**
	 * 获取店铺导航数量
	 * @param shopId 商家店铺ID
	 * @return
	 */
	protected int getNavSum(Integer shopId) {
		String sql = "select count(0) from es_navigation where shop_id = ?";
		return this.daoSupport.queryForInt(sql, shopId);
	}
}
