package com.enation.app.javashop.core.promotion.groupbuy.service.impl;

import com.enation.app.javashop.core.promotion.PromotionErrorCode;
import com.enation.app.javashop.core.promotion.groupbuy.model.dos.GroupbuyCatDO;
import com.enation.app.javashop.core.promotion.groupbuy.service.GroupbuyCatManager;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enation.app.javashop.framework.database.Page;

import java.util.List;

/**
 * 团购分类业务类
 *
 * @author Snow
 * @version v7.0.0
 * @since v7.0.0
 * 2018-04-02 16:08:03
 */
@Service
public class GroupbuyCatManagerImpl implements GroupbuyCatManager {

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    @Override
    public Page list(Integer pageNo, Integer pageSize) {
        String sql = "select * from es_groupbuy_cat  ";
        Page webPage = this.daoSupport.queryForPage(sql, pageNo, pageSize, GroupbuyCatDO.class);
        return webPage;
    }

    @Override
    public List<GroupbuyCatDO> getList(Integer parentId) {
        String sql = "select * from es_groupbuy_cat where parent_id = ? order by cat_order ";
        List<GroupbuyCatDO> list = this.daoSupport.queryForList(sql, GroupbuyCatDO.class, parentId);
        return list;
    }

    @Override
    public GroupbuyCatDO add(GroupbuyCatDO groupbuyCat) {

        //验证团购分类名称是否重复
        String sql = "select * from es_groupbuy_cat where cat_name = ?";
        List list = this.daoSupport.queryForList(sql, groupbuyCat.getCatName());
        if (list.size() > 0) {
            throw new ServiceException(PromotionErrorCode.E408.code(), "团购分类名称重复");
        }
        if (groupbuyCat.getParentId() == null) {
            groupbuyCat.setParentId(0);
        }
        this.daoSupport.insert(groupbuyCat);
        int id = this.daoSupport.getLastId("es_groupbuy_cat");
        groupbuyCat.setCatId(id);
        return groupbuyCat;
    }

    @Override
    public GroupbuyCatDO edit(GroupbuyCatDO groupbuyCat, Integer id) {

        //验证团购分类名称是否重复
        String sql = "select * from es_groupbuy_cat where cat_name = ? and cat_id != ?";
        List list = this.daoSupport.queryForList(sql, groupbuyCat.getCatName(), id);
        if (list.size() > 0) {
            throw new ServiceException(PromotionErrorCode.E408.code(), "团购分类名称重复");
        }

        this.daoSupport.update(groupbuyCat, id);
        return groupbuyCat;
    }

    @Override
    public void delete(Integer id) {
        if (!this.checkCat(id)) {
            throw new ServiceException(PromotionErrorCode.E408.code(), "当前有正在进行或还未开始的团购活动商品关联了此分类，不可删除");
        }

        this.daoSupport.delete(GroupbuyCatDO.class, id);
    }

    @Override
    public GroupbuyCatDO getModel(Integer id) {
        return this.daoSupport.queryForObject(GroupbuyCatDO.class, id);
    }

    /**
     * 检查团购分类是否可以被删除
     * @param catId 分类id
     * @return
     */
    private boolean checkCat(Integer catId) {
        String sql = "select count(0) from es_groupbuy_cat gc left join es_groupbuy_goods gg on gc.cat_id = gg.cat_id " +
                "left join es_groupbuy_active ga on gg.act_id = ga.act_id where gc.cat_id = ? and ga.end_time >= ?";
        int count = this.daoSupport.queryForInt(sql, catId, DateUtil.getDateline());
        boolean flag = count == 0 ? true : false;
        return  flag;
    }
}
