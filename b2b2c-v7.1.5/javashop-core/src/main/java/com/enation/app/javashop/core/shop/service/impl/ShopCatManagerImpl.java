package com.enation.app.javashop.core.shop.service.impl;

import com.enation.app.javashop.core.shop.ShopCatShowTypeEnum;
import com.enation.app.javashop.core.shop.ShopErrorCode;
import com.enation.app.javashop.core.shop.model.dos.ShopCatDO;
import com.enation.app.javashop.core.shop.service.ShopCatManager;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.javashop.framework.database.DaoSupport;

import java.util.List;

/**
 * 店铺分组业务类
 * @author zjp
 * @version v7.0.0
 * @since v7.0.0
 * 2018-04-24 11:18:37
 */
@Service
public class ShopCatManagerImpl implements ShopCatManager{

	@Autowired
	@Qualifier("memberDaoSupport")
	private	DaoSupport	daoSupport;
	
	@Override
	public List list(Integer shopId,String display){
		StringBuffer sql = new StringBuffer("select * from es_shop_cat where shop_id = ? and shop_cat_pid = 0  ");

		this.appendSQL(display,sql);
		List<ShopCatDO> list = this.daoSupport.queryForList(sql.toString(), ShopCatDO.class,shopId);

		StringBuffer sqlB = new StringBuffer("select * from es_shop_cat where shop_cat_pid = ? ") ;
		this.appendSQL(display, sqlB);

		for (ShopCatDO shopCatDO : list) {
			List<ShopCatDO> shopCatDOS = this.daoSupport.queryForList(sqlB.toString(), ShopCatDO.class, shopCatDO.getShopCatId());
			shopCatDO.setChildren(shopCatDOS);
		}
		return list;
	}



	@Override
	@Transactional(value = "memberTransactionManager",propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public ShopCatDO add(ShopCatDO	shopCat)	{
		shopCat.setShopId(UserContext.getSeller().getSellerId());
		ShopCatDO parent = null;
		//如果为顶级分组，父分类为0
		if(shopCat.getShopCatPid()==null||shopCat.getShopCatPid()==0){
			shopCat.setShopCatPid(0);
		}else {
			parent = this.getModel(shopCat.getShopCatPid());
			if(parent == null){
				throw new ServiceException(ShopErrorCode.E223.name(),"父分组不存在");
			}
		}
		this.daoSupport.insert(shopCat);
		shopCat.setShopCatId(this.daoSupport.getLastId("es_shop_cat"));
		if(shopCat.getShopCatPid()==0 ){
			//填充分类路径
			shopCat.setCatPath("0|" + shopCat.getShopCatId() + "|");
		}

		if(shopCat.getShopCatPid()!=0){
			//填充分类路径
			shopCat.setCatPath(this.getModel(shopCat.getShopCatPid()).getCatPath() + shopCat.getShopCatId() + "|");
			//如果是子分类且是开启状态，则父分类也要是开启状态
			if(shopCat.getDisable().equals(1)){
				String sql = "update es_shop_cat set disable=1 where shop_cat_id=?";
				this.daoSupport.execute(sql, shopCat.getShopCatPid());
			}
		}
		String sql = "update es_shop_cat set  cat_path=? where  shop_cat_id = ? ";
		this.daoSupport.execute(sql,shopCat.getCatPath(),shopCat.getShopCatId());
		return shopCat;
	}
	
	@Override
	@Transactional(value = "memberTransactionManager",propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public	ShopCatDO  edit(ShopCatDO	shopCat,Integer id){

		shopCat.setShopId(UserContext.getSeller().getSellerId());
		shopCat.setShopCatId(id);
		if(shopCat.getShopCatPid()==null||shopCat.getShopCatPid()==0){
			shopCat.setShopCatPid(0);
		}

		ShopCatDO shopCatDO = this.getModel(id);
		if (shopCatDO==null||!shopCatDO.getShopId().equals(UserContext.getSeller().getSellerId())){
			throw new ServiceException(ShopErrorCode.E218.name(),"店铺分组不存在");
		}

		if(shopCatDO.getShopCatPid() == 0 && shopCat.getShopCatPid()!=0){
			throw new ServiceException(ShopErrorCode.E219.name(),"顶级分类不可修改上级分类");
		}

		if(shopCat.getShopCatPid()!=0){
			ShopCatDO parent = this.getModel(shopCat.getShopCatPid());
			if(parent == null){
				throw new ServiceException(ShopErrorCode.E223.name(),"父分组不存在");
			}
		}

		if(shopCat.getShopCatPid()==0 ){
			//填充分类路径
			shopCat.setCatPath("0|" + shopCat.getShopCatId() + "|");
			//如果是父分类并且是关闭状态，则子分类也要是关闭状态
			if(shopCat.getDisable().equals(0)){
				String sql = "update es_shop_cat set disable=0 where shop_cat_pid=?";
				this.daoSupport.execute(sql, shopCat.getShopCatId());
			}
		}

		if(shopCat.getShopCatPid()!=0){
			//填充分类路径
			shopCat.setCatPath(this.getModel(shopCat.getShopCatPid()).getCatPath() + shopCat.getShopCatId() + "|");
			//如果是子分类且是开启状态，则父分类也要是开启状态
			if(shopCat.getDisable().equals(1)){
				String sql = "update es_shop_cat set disable=1 where shop_cat_id=?";
				this.daoSupport.execute(sql, shopCat.getShopCatPid());
			}
		}
		this.daoSupport.update(shopCat,id);
		return shopCat;
	}
	
	@Override
	@Transactional(value = "memberTransactionManager",propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public	void delete( Integer id)	{
		Integer sellerId = UserContext.getSeller().getSellerId();

		ShopCatDO shopCatDO = this.getModel(id);
		if(shopCatDO==null){
			throw new ServiceException(ShopErrorCode.E218.name(),"店铺分组不存在");
		}

		//查询当前分组下是否有子分组
		Integer num = this.daoSupport.queryForInt("select count(0) from es_shop_cat s where s.shop_cat_pid=? and shop_id=?",id,sellerId);
		if(num>=1){
			throw new ServiceException(ShopErrorCode.E220.name(),"当前分组存在子分组，不能删除");
		}
//TODO
//		int goodsNum = this.daoSupport.queryForInt("select count(0) from es_goods where shop_cat_id=? and seller_id=?", id,sellerId);
//		if(goodsNum>=1){
//			throw new ServiceException(ShopErrorCode.E221.name(),"当前分组存在商品，请删除此分类下所有商品(包括商品回收站)！");
//		}

		this.daoSupport.delete(ShopCatDO.class,	id);
	}
	
	@Override
	public	ShopCatDO getModel(Integer id)	{
		ShopCatDO shopCatDO = this.daoSupport.queryForObject(ShopCatDO.class, id);
		return shopCatDO;
	}

	@Override
	public List getChildren(String catPath) {
		String sql = "select shop_cat_id from es_shop_cat where cat_path like ? ";
		return this.daoSupport.queryForList(sql, catPath + "%");
	}



	private void appendSQL(String display, StringBuffer sql) {

		if(ShopCatShowTypeEnum.SHOW.name().equals(display)){
			sql.append(" and disable = 1 ");
		}else if(ShopCatShowTypeEnum.HIDE.name().equals(display)){
			sql.append(" and disable = 0 ");
		}
		sql.append(" order by sort asc ");
	}

}
