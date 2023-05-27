package com.enation.app.javashop.core.shop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.security.model.Seller;

import com.enation.app.javashop.core.shop.ShopErrorCode;
import com.enation.app.javashop.core.shop.model.dos.ShopSildeDO;
import com.enation.app.javashop.core.shop.service.ShopSildeManager;

/**
 * 店铺幻灯片业务类
 * @author zjp
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-28 18:50:58
 */
@Service
public class ShopSildeManagerImpl implements ShopSildeManager{

	@Autowired
	@Qualifier("memberDaoSupport")
	private	DaoSupport	daoSupport;

	@Override
	public List<ShopSildeDO> list(Integer shopId){
		Seller seller = UserContext.getSeller();
		String sql = "select * from es_shop_silde where shop_id = ? ";
		return this.daoSupport.queryForList(sql,ShopSildeDO.class,shopId);
	}

	@Override
	@Transactional(value = "memberTransactionManager",propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public	ShopSildeDO  add(ShopSildeDO	shopSilde)	{
		this.daoSupport.insert(shopSilde);

		return shopSilde;
	}

	@Override
	@Transactional(value = "memberTransactionManager",propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public	void edit(List<ShopSildeDO> list){
		Seller seller = UserContext.getSeller();
		if(list.size()>0){
			for (ShopSildeDO shopSildeDO:list) {
				shopSildeDO.setShopId(seller.getSellerId());
				if(shopSildeDO.getSildeId()==0){
					// 如果前端传入id不为0 则为修改 否则为新增
					this.add(shopSildeDO);
				}else {
					//对不存在的或不属于本店铺的幻灯片进行校验
					ShopSildeDO model = this.getModel(shopSildeDO.getSildeId());
					if(model==null||!model.getShopId().equals(seller.getSellerId())) {
						throw new ServiceException(ShopErrorCode.E208.name(), "存在无效幻灯片，无法进行编辑操作");
					}
					this.edit(shopSildeDO, shopSildeDO.getSildeId());

				}
			}
		}
	}

	@Override
	@Transactional(value = "memberTransactionManager",propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public	void delete( Integer id)	{
		//对于不存在的或者不属于本店铺的幻灯片同时校验
		Seller seller = UserContext.getSeller();
		ShopSildeDO model = this.getModel(id);
		if(model==null||!model.getShopId().equals(seller.getSellerId())) {
			throw new ServiceException(ShopErrorCode.E208.name(), "不存在此幻灯片，无法删除");
		}

		this.daoSupport.delete(ShopSildeDO.class,id);
	}

	@Override
	public	ShopSildeDO getModel(Integer id)	{
		return this.daoSupport.queryForObject(ShopSildeDO.class, id);
	}

	@Override
	@Transactional(value = "memberTransactionManager",propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public	ShopSildeDO  edit(ShopSildeDO	shopSilde,Integer id){
		this.daoSupport.update(shopSilde, id);
		return shopSilde;
	}
}