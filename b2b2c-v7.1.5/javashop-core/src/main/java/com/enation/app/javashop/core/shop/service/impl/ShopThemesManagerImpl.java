package com.enation.app.javashop.core.shop.service.impl;


import java.util.List;

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
import com.enation.app.javashop.core.shop.model.dos.ShopThemesDO;
import com.enation.app.javashop.core.shop.model.enums.ShopThemesEnum;
import com.enation.app.javashop.core.shop.model.vo.ShopThemesVO;
import com.enation.app.javashop.core.shop.service.ShopThemesManager;

/**
 * 店铺模版业务类
 *
 * @author zjp
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-27 14:17:32
 */
@Service
public class ShopThemesManagerImpl implements ShopThemesManager{

	@Autowired
	@Qualifier("memberDaoSupport")
	private	DaoSupport	daoSupport;

	@Override
	public Page list(int page,int pageSize,String type){
		if(!type.equals(ShopThemesEnum.PC.name())&&!type.equals(ShopThemesEnum.WAP.name())) {
			throw new ServiceException(ShopErrorCode.E201.name(),"模版类型不匹配");
		}
		String sql = "select * from es_shop_themes where type = ? ";
		Page  webPage = this.daoSupport.queryForPage(sql,page, pageSize ,ShopThemesDO.class,type);
		
		return webPage;
	}
	
	@Override
	@Transactional(value = "memberTransactionManager",propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public	ShopThemesDO  add(ShopThemesDO	shopThemes)	{

		String sql = "select count(*) from es_shop_themes where mark = ? ";
		Integer integer = this.daoSupport.queryForInt(sql, shopThemes.getMark());
		if(integer>0){
			throw  new ServiceException(ShopErrorCode.E225.name(),"店铺模版标识重复");
		}

		//如果不存在模版则设置为默认模版
		sql="select count(id) from es_shop_themes";
		if(daoSupport.queryForInt(sql)==0){
			shopThemes.setIsDefault(1);
		}

		//只能有一个默认模板
		if(shopThemes.getIsDefault().equals(1)){
			sql = "UPDATE es_shop_themes SET is_default=0 where type = ? ";
			this.daoSupport.execute(sql,shopThemes.getType());
		}

		this.daoSupport.insert(shopThemes);
		
		int lastId = this.daoSupport.getLastId("es_shop_themes");
		shopThemes.setId(lastId);
		return shopThemes;
	}
	
	@Override
	@Transactional(value = "memberTransactionManager",propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public	ShopThemesDO  edit(ShopThemesDO	shopThemes,Integer id){

		ShopThemesDO model = this.getModel(id);
		if(model==null) {
			throw new ServiceException(ShopErrorCode.E202.name(), "模版不存在");
		}

		String sql = "select count(*) from es_shop_themes where mark = ? and id != ? ";
		Integer integer = this.daoSupport.queryForInt(sql, shopThemes.getMark(),id);
		if(integer>0){
			throw  new ServiceException(ShopErrorCode.E225.name(),"店铺模版标识重复");
		}

		//不允许将默认模板改成非默认
		if (model.getIsDefault().equals(1) && shopThemes.getIsDefault().equals(0)) {
			throw new ServiceException(ShopErrorCode.E225.name(), "至少要有一个默认模板");
		}

		//只能有一个默认模板
		if(shopThemes.getIsDefault().equals(1)){
			sql = "UPDATE es_shop_themes SET is_default=0 where type = ? ";
			this.daoSupport.execute(sql,shopThemes.getType());
		}
		
		this.daoSupport.update(shopThemes, id);
		return shopThemes;
	}
	
	@Override
	@Transactional(value = "memberTransactionManager",propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public	void delete( Integer id)	{
		ShopThemesDO shopThemes = this.getModel(id);
		if(shopThemes==null) {
			throw new ServiceException(ShopErrorCode.E202.name(), "模版不存在");
		}
		if(shopThemes.getIsDefault()==1) {
			throw new ServiceException(ShopErrorCode.E205.name(), "默认模版不能删除");
		}
		//查询到当前默认模版
		String sql="select * from es_shop_themes where is_default = 1 and type = ? ";
		ShopThemesDO shopThemesDO = this.daoSupport.queryForObject(sql, ShopThemesDO.class,shopThemes.getType());
		//将用到此模版的店铺修改成默认模版
		this.daoSupport.execute("update es_shop_detail set shop_themeid = ?,shop_theme_path = ? where shop_themeid = ?",shopThemesDO.getId(),shopThemesDO.getMark(),id);
		//删除模版
		this.daoSupport.delete(ShopThemesDO.class,	id);
	}
	
	@Override
	public	ShopThemesDO getModel(Integer id)	{
		return this.daoSupport.queryForObject(ShopThemesDO.class, id);
	}

	@Override
	@Transactional(value = "memberTransactionManager",propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public void changeShopThemes(Integer themesId) {
		Seller seller = UserContext.getSeller();
		ShopThemesDO model = this.getModel(themesId);
		if(model==null) {
			throw new ServiceException(ShopErrorCode.E202.name(), "模版不存在");
		}
		if(model.getType().equals(ShopThemesEnum.PC.name())) {
			this.daoSupport.execute("update es_shop_detail set shop_themeid=?,shop_theme_path=? where shop_id=?", themesId,model.getMark(),seller.getSellerId());
		}else {
			this.daoSupport.execute("update es_shop_detail set wap_themeid=?,wap_theme_path=? where shop_id=?", themesId,model.getMark(),seller.getSellerId());
		}
	}

	@Override
	public ShopThemesDO getDefaultShopThemes(String type) {
		if(!type.equals(ShopThemesEnum.PC.name())&&!type.equals(ShopThemesEnum.WAP.name())) {
			throw new ServiceException(ShopErrorCode.E201.name(),"模版类型不匹配");
		}
		String sql = "select * from es_shop_themes where is_default = ? and type = ? ";
		return this.daoSupport.queryForObject(sql, ShopThemesDO.class, 1,type);
	}

	@Override
	public List<ShopThemesVO> list(String type) {
		if(!type.equals(ShopThemesEnum.PC.name())&&!type.equals(ShopThemesEnum.WAP.name())) {
			throw new ServiceException(ShopErrorCode.E201.name(),"模版类型不匹配");
		}
		String sql = "select * from es_shop_themes where type = ? ";
		List<ShopThemesVO> list = this.daoSupport.queryForList(sql, ShopThemesVO.class, type);
		return list;
	}

	@Override
	public ShopThemesDO get(String type) {
		Seller seller = UserContext.getSeller();
		String sql = "";
		if(type.equals(ShopThemesEnum.PC.name())) {
			sql = "select shop_themeid from es_shop_detail where shop_id = ? ";
		}else {
			sql = "select wap_themeid from es_shop_detail where shop_id = ? ";
		}
		
		Integer id = this.daoSupport.queryForInt(sql, seller.getSellerId());
		return this.getModel(id);
	}
}
