package com.enation.app.javashop.core.goods.service.impl;

import com.enation.app.javashop.core.goods.model.dos.GoodsParamsDO;
import com.enation.app.javashop.core.goods.model.dos.ParameterGroupDO;
import com.enation.app.javashop.core.goods.model.vo.GoodsParamsGroupVO;
import com.enation.app.javashop.core.goods.model.vo.GoodsParamsVO;
import com.enation.app.javashop.core.goods.service.GoodsParamsManager;
import com.enation.app.javashop.framework.database.DaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 商品参数
 *
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018年3月21日 下午5:30:11
 */
@Service
public class GoodsParamsManagerImpl implements GoodsParamsManager {

    @Autowired
    @Qualifier("goodsDaoSupport")
    private DaoSupport daoSupport;

    @Override
    @Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addParams(List<GoodsParamsDO> paramList, Integer goodsId) {
        String sql = "delete from es_goods_params where goods_id = ?";
        this.daoSupport.execute(sql, goodsId);

        if (paramList != null) {
            for (GoodsParamsDO param : paramList) {
                param.setGoodsId(goodsId);
                this.daoSupport.insert(param);
            }
        }
    }

    @Override
    public List<GoodsParamsGroupVO> queryGoodsParams(Integer categoryId, Integer goodsId) {
        String sql = "select * from es_parameter_group where category_id = ?";
        //查询参数组
        List<ParameterGroupDO> groupList = this.daoSupport.queryForList(sql, ParameterGroupDO.class, categoryId);
        sql = "select p.*,gp.param_value,p.group_id "
                + "from es_parameters p "
                + "left join es_goods_params gp on p.param_id=gp.param_id and gp.goods_id = ?  where p.category_id = ?"
                + " order by sort ";

        List<GoodsParamsVO> paramList = this.daoSupport.queryForList(sql, GoodsParamsVO.class, goodsId,categoryId);

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


    @Override
    public List<GoodsParamsGroupVO> queryGoodsParams(Integer categoryId) {
        String sql = "select * from es_parameter_group where category_id = ?";
        //查询参数组
        List<ParameterGroupDO> groupList = this.daoSupport.queryForList(sql, ParameterGroupDO.class, categoryId);
        sql = "select * from es_parameters where category_id = ? order by sort ";

        List<GoodsParamsVO> paramList = this.daoSupport.queryForList(sql, GoodsParamsVO.class, categoryId);

        List<GoodsParamsGroupVO> resList = this.convertParamList(groupList, paramList);

        return resList;
    }
}
