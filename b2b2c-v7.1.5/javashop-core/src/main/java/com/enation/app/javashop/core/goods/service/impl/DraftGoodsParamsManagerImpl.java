package com.enation.app.javashop.core.goods.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enation.app.javashop.core.goods.model.dos.DraftGoodsParamsDO;
import com.enation.app.javashop.core.goods.model.dos.GoodsParamsDO;
import com.enation.app.javashop.core.goods.model.dos.ParameterGroupDO;
import com.enation.app.javashop.core.goods.model.vo.GoodsParamsGroupVO;
import com.enation.app.javashop.core.goods.model.vo.GoodsParamsVO;
import com.enation.app.javashop.core.goods.service.DraftGoodsParamsManager;
import com.enation.app.javashop.framework.database.DaoSupport;

/**
 * 草稿商品参数表业务类
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018-03-26 11:31:20
 */
@Service
public class DraftGoodsParamsManagerImpl implements DraftGoodsParamsManager{

	@Autowired
	@Qualifier("goodsDaoSupport")
	private	DaoSupport	daoSupport;

	@Override
	public void addParams(List<GoodsParamsDO> goodsParamsList, Integer draftGoodsId) {
		
		String sql = "delete from es_draft_goods_params where draft_goods_id = ?";
		this.daoSupport.execute(sql, draftGoodsId);
		for (GoodsParamsDO param : goodsParamsList){
			DraftGoodsParamsDO draftGoodsParams = new DraftGoodsParamsDO(param);
			draftGoodsParams.setDraftGoodsId(draftGoodsId);
			this.daoSupport.insert(draftGoodsParams);
		}
		
	}

	@Override
	public List<GoodsParamsGroupVO> getParamByCatAndDraft(Integer categoryId, Integer draftGoodsId) {
		
		String sql = "select * from es_parameter_group where category_id = ?";
		//查询参数组
		List<ParameterGroupDO> groupList = this.daoSupport.queryForList(sql, ParameterGroupDO.class, categoryId);
		
		sql = "select p.*,gp.param_value,p.group_id "
				+ "from es_parameters p "
				+ "left join es_draft_goods_params gp on p.param_id=gp.param_id where p.category_id = ?"
				+ " and (gp.draft_goods_id = ?  or gp.draft_goods_id is null)";

		List<GoodsParamsVO> paramList = this.daoSupport.queryForList(sql, GoodsParamsVO.class, categoryId, draftGoodsId);

		List<GoodsParamsGroupVO> resList = this.convertParamList(groupList, paramList);
		
		return resList;
	}

	/**
	 * 拼装返回值
	 * 
	 * @param paramList
	 * @return
	 */
	private List<GoodsParamsGroupVO> convertParamList(List<ParameterGroupDO> groupList, List<GoodsParamsVO> paramList) {
		Map<Integer, List<GoodsParamsVO>> map = new HashMap<>(16);
		for (GoodsParamsVO param : paramList) {
			if (map.get(param.getGroupId()) != null) {
				map.get(param.getGroupId()).add(param);
			} else {
				List<GoodsParamsVO> list = new ArrayList<>();
				list.add(param);
				map.put(param.getGroupId(), list);
			}
		}
		List<GoodsParamsGroupVO> resList = new ArrayList<>();
		for (ParameterGroupDO group : groupList) {
			GoodsParamsGroupVO list = new GoodsParamsGroupVO();
			list.setGroupName(group.getGroupName());
			list.setGroupId(group.getGroupId());
			list.setParams(map.get(group.getGroupId()));
			resList.add(list);
		}
		return resList;
	}
	
}
