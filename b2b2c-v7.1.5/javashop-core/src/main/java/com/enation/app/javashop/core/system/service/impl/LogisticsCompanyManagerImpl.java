package com.enation.app.javashop.core.system.service.impl;

import javax.validation.Valid;

import com.enation.app.javashop.core.client.member.ShopLogisticsCompanyClient;
import com.enation.app.javashop.core.system.enums.DeleteStatusEnum;
import com.enation.app.javashop.core.system.enums.LogiCompanyStatusEnum;
import org.apache.tools.ant.taskdefs.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.util.StringUtil;
import com.enation.app.javashop.core.shop.ShopErrorCode;
import com.enation.app.javashop.core.system.model.dos.LogisticsCompanyDO;
import com.enation.app.javashop.core.system.service.LogisticsCompanyManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 物流公司业务类
 * @author zjp
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-29 15:10:38
 */
@Service
public class LogisticsCompanyManagerImpl implements LogisticsCompanyManager {

	@Autowired
	@Qualifier("systemDaoSupport")
	private	DaoSupport daoSupport;

	@Autowired
	private ShopLogisticsCompanyClient shopLogisticsCompanyClient;

	@Override
	public Page list(int page, int pageSize, String name){

		StringBuffer sqlBuffer = new StringBuffer("select * from es_logistics_company where delete_status = ? ");
		List<Object> term = new ArrayList<>();
		term.add(DeleteStatusEnum.NORMAL.value());

		//按物流公司查询
		if (StringUtil.notEmpty(name)) {
			sqlBuffer.append(" and name like ? ");
			term.add("%" + name + "%");
		}

		sqlBuffer.append(" order by id desc");

		Page  webPage = this.daoSupport.queryForPage(sqlBuffer.toString(), page, pageSize ,LogisticsCompanyDO.class, term.toArray());

		return webPage;
	}

	@Override
	@Transactional(value = "systemTransactionManager",propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public LogisticsCompanyDO add(LogisticsCompanyDO logi)	{
		LogisticsCompanyDO logicode = this.getLogiByCode(logi.getCode());
		LogisticsCompanyDO logikdcode = this.getLogiBykdCode(logi.getKdcode());
		LogisticsCompanyDO loginame = this.getLogiByName(logi.getName());
		if(loginame != null){
			throw new ServiceException(ShopErrorCode.E211.name(), "物流公司名称重复");
		}
		if(logicode != null){
			throw new ServiceException(ShopErrorCode.E213.name(), "物流公司代码重复");
		}
		if(logikdcode != null){
			throw new ServiceException(ShopErrorCode.E212.name(), "快递鸟公司代码重复");
		}

		logi.setDeleteStatus(DeleteStatusEnum.NORMAL.value());
		logi.setDisabled(LogiCompanyStatusEnum.OPEN.value());

		this.daoSupport.insert(logi);
		int lastId = this.daoSupport.getLastId("es_logistics_company");
		logi.setId(lastId);

		return logi;
	}

	@Override
	@Transactional(value = "systemTransactionManager",propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public LogisticsCompanyDO edit(@Valid LogisticsCompanyDO logi, Integer id){
		LogisticsCompanyDO model = this.getModel(id);
		if(model==null) {
			throw new ServiceException(ShopErrorCode.E214.name(), "物流公司不存在");
		}
		//当支持电子面单时，需要填写快递鸟物流公司code
		if(logi.getIsWaybill() == 1 && StringUtil.isEmpty(logi.getKdcode())){
			throw new ServiceException(ShopErrorCode.E212.name(), "快递鸟公司代码必填");
		}

		LogisticsCompanyDO logicode = this.getLogiByCode(logi.getCode());
		LogisticsCompanyDO logikdcode = this.getLogiBykdCode(logi.getKdcode());
		LogisticsCompanyDO loginame = this.getLogiByName(logi.getName());
		if(logikdcode != null && !logikdcode.getId().equals(logi.getId())){
			throw new ServiceException(ShopErrorCode.E212.name(), "快递鸟公司代码重复");
		}
		if(loginame != null && !loginame.getId() .equals(logi.getId())){
			throw new ServiceException(ShopErrorCode.E211.name(), "物流公司名称重复");
		}
		if(logicode != null && !logicode.getId().equals(logi.getId())){
			throw new ServiceException(ShopErrorCode.E213.name(), "物流公司代码重复");
		}
		this.daoSupport.update(logi, id);
		return logi;
	}

	@Override
	@Transactional(value = "systemTransactionManager",propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public	void delete(Integer logiId)	{
		LogisticsCompanyDO model = this.getModel(logiId);
		if (model == null ) {
			throw new ServiceException(ShopErrorCode.E214.name(), "物流公司不存在");
		}

		List list = shopLogisticsCompanyClient.queryListByLogisticsId(logiId);
		if (StringUtil.isNotEmpty(list)) {
			throw new ServiceException(ShopErrorCode.E214.name(), "当前物流公司已经被商家使用，不能删除");
		}

		String sql = "delete from es_logistics_company where id = ?";
		this.daoSupport.execute(sql, logiId);
	}

	@Override
	public LogisticsCompanyDO getModel(Integer id)	{
		return this.daoSupport.queryForObject(LogisticsCompanyDO.class, id);
	}

	@Override
	public LogisticsCompanyDO getLogiByCode(String code) {
		String sql  = "select * from es_logistics_company where code=?";
		LogisticsCompanyDO logiCompany =  this.daoSupport.queryForObject(sql, LogisticsCompanyDO.class, code);
		return logiCompany;
	}

	@Override
	public LogisticsCompanyDO getLogiBykdCode(String kdcode) {
		String sql  = "select * from es_logistics_company where kdcode=?";
		LogisticsCompanyDO logiCompany =  this.daoSupport.queryForObject(sql, LogisticsCompanyDO.class, kdcode);
		return logiCompany;
	}

	@Override
	public LogisticsCompanyDO getLogiByName(String name) {
		String sql  = "select * from es_logistics_company where name=?";
		LogisticsCompanyDO logiCompany =  this.daoSupport.queryForObject(sql, LogisticsCompanyDO.class, name);
		return logiCompany;
	}

	@Override
	public List<LogisticsCompanyDO> list() {
		String sql = "select * from es_logistics_company order by id desc";
		return this.daoSupport.queryForList(sql,LogisticsCompanyDO.class);
	}

	@Override
	@Transactional(value = "systemTransactionManager",propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public void openCloseLogi(Integer id, String disabled) {
		if (StringUtil.isEmpty(disabled) || (!LogiCompanyStatusEnum.OPEN.name().equals(disabled) && !LogiCompanyStatusEnum.CLOSE.name().equals(disabled))) {
			throw new ServiceException(ShopErrorCode.E227.name(), "参数传递不正确");
		}

		LogisticsCompanyDO model = this.getModel(id);
		if (model == null ) {
			throw new ServiceException(ShopErrorCode.E214.name(), "物流公司不存在");
		}

		String sql = "update es_logistics_company set disabled = ? where id = ?";
		this.daoSupport.execute(sql, disabled, id);

		//如果是禁用操作，需要将商家关联的物流公司信息删除掉
		if (disabled.equals(LogiCompanyStatusEnum.CLOSE.name())) {
			shopLogisticsCompanyClient.deleteByLogisticsId(id);
		}
	}

	@Override
	public List<LogisticsCompanyDO> listAllNormal() {
		String sql = "select * from es_logistics_company where delete_status = ? and disabled = ? order by id desc";
		return this.daoSupport.queryForList(sql, LogisticsCompanyDO.class, DeleteStatusEnum.NORMAL.value(), LogiCompanyStatusEnum.OPEN.value());
	}
}