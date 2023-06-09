package com.enation.app.javashop.core.goods.service.impl;

import com.enation.app.javashop.core.goods.GoodsErrorCode;
import com.enation.app.javashop.core.goods.model.dos.SpecValuesDO;
import com.enation.app.javashop.core.goods.model.dos.SpecificationDO;
import com.enation.app.javashop.core.goods.model.enums.Permission;
import com.enation.app.javashop.core.goods.service.SpecValuesManager;
import com.enation.app.javashop.core.goods.service.SpecificationManager;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 规格值业务类
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018-03-20 10:23:53
 */
@Service
public class SpecValuesManagerImpl implements SpecValuesManager{

	@Autowired
	@Qualifier("goodsDaoSupport")
	private	DaoSupport	daoSupport;
	@Autowired
	private SpecificationManager specificationManager;

	@Override
	@Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public	SpecValuesDO  add(SpecValuesDO	specValues)	{
		
		this.daoSupport.insert(specValues);
		
		specValues.setSpecValueId(this.daoSupport.getLastId(""));
		
		return specValues;
	}
	
	@Override
	@Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public	SpecValuesDO  edit(SpecValuesDO	specValues,Integer id){
		this.daoSupport.update(specValues, id);
		return specValues;
	}
	
	@Override
	public	SpecValuesDO getModel(Integer id)	{
		return this.daoSupport.queryForObject(SpecValuesDO.class, id);
	}

	@Override
	public List<SpecValuesDO> listBySpecId(Integer specId,Permission permission) {
		
		StringBuffer sql = new StringBuffer(" select * from es_spec_values where spec_id = ? ");
		//商家或者后台调用
		if(Permission.ADMIN.equals(permission)){
			sql.append(" and seller_id = 0");
		}
		
		List<SpecValuesDO> list = this.daoSupport.queryForList(sql.toString(), SpecValuesDO.class, specId);
		
		return list;
	}

	@Override
	@Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public List<SpecValuesDO> saveSpecValue(Integer specId, String[] valueList) {

		//查询规格是否存在
		SpecificationDO spec = specificationManager.getModel(specId);
		if(spec == null){
			throw new ServiceException(GoodsErrorCode.E306.code(),"所属规格不存在");
		}
		String sql = "delete from es_spec_values where spec_id=? and seller_id=0";

		this.daoSupport.execute(sql, specId);
		List<SpecValuesDO> res = new ArrayList<>();
		for (String value : valueList) {
			if(value.length()>50){
				throw new ServiceException(GoodsErrorCode.E305.code(),"规格值为1到50个字符之间");
			}
			SpecValuesDO specValue = new SpecValuesDO( specId, value, 0);
			specValue.setSpecName(spec.getSpecName());
			this.daoSupport.insert(specValue);
			specValue.setSpecId(this.daoSupport.getLastId(""));
			res.add(specValue);
		}
		return res;
		
	}
}
