package com.enation.app.javashop.core.goods.service.impl;

import com.enation.app.javashop.core.goods.GoodsErrorCode;
import com.enation.app.javashop.core.goods.model.dos.ParameterGroupDO;
import com.enation.app.javashop.core.goods.model.dos.ParametersDO;
import com.enation.app.javashop.core.goods.service.ParameterGroupManager;
import com.enation.app.javashop.core.goods.service.ParametersManager;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 参数业务类
 * 
 * @author fk
 * @version v2.0
 * @since v7.0.0 2018-03-20 16:14:31
 */
@Service
public class ParametersManagerImpl implements ParametersManager {

	@Autowired
	@Qualifier("goodsDaoSupport")
	private DaoSupport daoSupport;
	@Autowired
	private ParameterGroupManager parameterGroupManager;

	@Override
	public Page list(int page, int pageSize) {

		String sql = "select * from es_parameters  ";
		Page webPage = this.daoSupport.queryForPage(sql, page, pageSize, ParametersDO.class);

		return webPage;
	}

	@Override
	@Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ParametersDO add(ParametersDO parameters) {

		// 查询参数组
		ParameterGroupDO group = parameterGroupManager.getModel(parameters.getGroupId());
		if(group == null){
			throw new ServiceException(GoodsErrorCode.E303.code(), "所属参数组不存在");
		}
		parameters.setCategoryId(group.getCategoryId());
		// 选择项
		if(parameters.getParamType() == 2){
			if(!StringUtil.notEmpty(parameters.getOptions()) ){
				throw new ServiceException(GoodsErrorCode.E303.code(), "选择项类型必须填写选择内容");
			}
		}
		String sql = "select * from es_parameters where group_id = ? order by sort desc limit 0,1";
		ParametersDO paramtmp = this.daoSupport.queryForObject(sql, ParametersDO.class, parameters.getGroupId());
		if (paramtmp == null) {
			parameters.setSort(1);
		} else {
			parameters.setSort(paramtmp.getSort() + 1);
		}

		this.daoSupport.insert(parameters);
		int id = this.daoSupport.getLastId("es_parameters");
		parameters.setParamId(id);

		return parameters;
	}

	@Override
	@Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ParametersDO edit(ParametersDO parameters, Integer id) {

		ParametersDO temp = this.getModel(id);
		if(temp == null ){
			throw new ServiceException(GoodsErrorCode.E303.code(), "参数不存在");
		}
		parameters.setCategoryId(temp.getCategoryId());
		// 选择项
		if(parameters.getParamType() == 2){
			if(!StringUtil.notEmpty(parameters.getOptions()) ){
				throw new ServiceException(GoodsErrorCode.E303.code(), "选择项类型必须填写选择内容");
			}
		}

		BeanUtils.copyProperties(parameters,temp);

		this.daoSupport.update(temp, id);

		return parameters;
	}

	@Override
	@Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void delete(Integer id) {
		this.daoSupport.delete(ParametersDO.class, id);
	}

	@Override
	public ParametersDO getModel(Integer id) {
		return this.daoSupport.queryForObject(ParametersDO.class, id);
	}

	@Override
	public void paramSort(Integer paramId, String sortType) {

		String sql = "";

		ParametersDO curParam = this.getModel(paramId);

		if(curParam == null){
			throw new ServiceException(GoodsErrorCode.E303.code(), "要移动的参数不存在");
		}

		if ("up".equals(sortType)) {
			sql = "select * from es_parameters where sort<? and group_id=? order by sort desc limit 0,1";
		} else if ("down".equals(sortType)) {
			sql = "select * from es_parameters where sort>? and group_id=? order by sort asc limit 0,1";
		}

		ParametersDO changeParam = this.daoSupport.queryForObject(sql, ParametersDO.class, curParam.getSort(),
				curParam.getGroupId());
		if (changeParam != null) {
			sql = "update es_parameters set sort = ? where param_id = ?";
			this.daoSupport.execute(sql, changeParam.getSort(), curParam.getParamId());
			this.daoSupport.execute(sql, curParam.getSort(), changeParam.getParamId());
		}
	}

	@Override
	public void deleteByGroup(Integer groupId) {
		String sql = "delete from es_parameters where group_id = ?";
		this.daoSupport.execute(sql, groupId);
	}
}
