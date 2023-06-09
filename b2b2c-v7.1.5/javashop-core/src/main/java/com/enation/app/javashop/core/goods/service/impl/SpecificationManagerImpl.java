package com.enation.app.javashop.core.goods.service.impl;

import com.enation.app.javashop.core.goods.GoodsErrorCode;
import com.enation.app.javashop.core.goods.model.dos.CategoryDO;
import com.enation.app.javashop.core.goods.model.dos.CategorySpecDO;
import com.enation.app.javashop.core.goods.model.dos.SpecValuesDO;
import com.enation.app.javashop.core.goods.model.dos.SpecificationDO;
import com.enation.app.javashop.core.goods.model.vo.SelectVO;
import com.enation.app.javashop.core.goods.model.vo.SpecificationVO;
import com.enation.app.javashop.core.goods.service.CategoryManager;
import com.enation.app.javashop.core.goods.service.SpecificationManager;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.security.model.Seller;
import com.enation.app.javashop.framework.util.SqlUtil;
import com.enation.app.javashop.framework.util.StringUtil;
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
 * 规格项业务类
 *
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018-03-20 09:31:27
 */
@Service
public class SpecificationManagerImpl implements SpecificationManager {

    @Autowired
    @Qualifier("goodsDaoSupport")
    private DaoSupport daoSupport;
    @Autowired
    private CategoryManager categoryManager;

    @Override
    public Page list(int page, int pageSize, String keyword) {

        StringBuffer sqlBuffer = new StringBuffer("select * from es_specification  where disabled = 1 and seller_id = 0 ");

        List<Object> term = new ArrayList<>();
        if (StringUtil.notEmpty(keyword)) {
            sqlBuffer.append(" and spec_name like ? ");
            term.add("%" + keyword + "%");
        }

        sqlBuffer.append(" order by spec_id desc");

        Page webPage = this.daoSupport.queryForPage(sqlBuffer.toString(), page, pageSize, SpecificationDO.class, term.toArray());

        return webPage;
    }

    @Override
    @Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public SpecificationDO add(SpecificationDO specification) {


        //如果是管理端添加的规格，则验证管理端的对个名称是否重复
        if(specification.getSellerId() == 0){
            String sql = "select * from es_specification  where disabled = 1 and seller_id = 0 and spec_name = ? ";
            List list = this.daoSupport.queryForList(sql, specification.getSpecName());

            if (list.size() > 0) {
                throw new ServiceException(GoodsErrorCode.E305.code(), "规格名称重复");
            }
        }


        specification.setDisabled(1);
        this.daoSupport.insert(specification);
        specification.setSpecId(this.daoSupport.getLastId(""));

        return specification;
    }

    @Override
    @Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public SpecificationDO edit(SpecificationDO specification, Integer id) {

        SpecificationDO model = this.getModel(id);
        if (model == null) {
            throw new ServiceException(GoodsErrorCode.E305.code(), "规格不存在");
        }

        String sql = "select * from es_specification  where disabled = 1 and seller_id = 0 and spec_name = ? and spec_id!=? ";
        List list = this.daoSupport.queryForList(sql, specification.getSpecName(),id);

        if (list.size() > 0) {
            throw new ServiceException(GoodsErrorCode.E305.code(), "规格名称重复");
        }

        this.daoSupport.update(specification, id);
        return specification;
    }

    @Override
    @Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delete(Integer[] ids) {

        List<Object> term = new ArrayList<>();
        String idsStr = SqlUtil.getInSql(ids, term);
        //查看是否已经有分类绑定了该规格
        String sql = "select * from es_category_spec where spec_id in (" + idsStr + ")";
        List<CategorySpecDO> list = this.daoSupport.queryForList(sql, CategorySpecDO.class, term.toArray());
        if (list.size() > 0) {

            throw new ServiceException(GoodsErrorCode.E305.code(), "有分类已经绑定要删除的规格，请先解绑分类规格");
        }

        sql = " update es_specification set disabled = 0 where spec_id in (" + idsStr + ")";

        this.daoSupport.execute(sql, term.toArray());
    }

    @Override
    public SpecificationDO getModel(Integer id) {

        return this.daoSupport.queryForObject(SpecificationDO.class, id);
    }

    @Override
    public List<SelectVO> getCatSpecification(Integer categoryId) {

        String sql = "select s.spec_id id,s.spec_name text,  "
                + "case category_id when ? then true else false end selected  "
                + "from es_specification s left join  es_category_spec cs "
                + "on s.spec_id=cs.spec_id and category_id=? where s.seller_id=0 and s.disabled=1";

        return this.daoSupport.queryForList(sql, SelectVO.class, categoryId, categoryId);
    }

    @Override
    @Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public SpecificationDO addSellerSpec(Integer categoryId, String specName) {

        CategoryDO category = categoryManager.getModel(categoryId);
        if (category == null) {
            throw new ServiceException(GoodsErrorCode.E305.code(), "分类不存在");
        }

        //商家添加规格，则验证当前这个分类下是否有重复的规格
        Seller seller = UserContext.getSeller();

        String sql = "select * from es_specification s inner join es_category_spec cs on s.spec_id = cs.spec_id  where disabled = 1 and category_id = ? and seller_id = ? and spec_name = ? ";
        List list = this.daoSupport.queryForList(sql, categoryId, seller.getSellerId(), specName);
        if(list.size()>0){
            throw new ServiceException(GoodsErrorCode.E305.code(), "规格名称重复");
        }

        SpecificationDO specification = new SpecificationDO(specName, 1, "商家自定义", seller.getSellerId());
        specification = this.add(specification);

        //保存分类规格的关系
        CategorySpecDO categorySpec = new CategorySpecDO(categoryId, specification.getSpecId());
        this.daoSupport.insert(categorySpec);

        return specification;
    }

    @Override
    public List<SpecificationVO> querySellerSpec(Integer categoryId) {
        Seller seller = UserContext.getSeller();
        //查询规格
        String sql = "select s.spec_id,s.spec_name "
                + "from es_specification s inner join es_category_spec cs on s.spec_id=cs.spec_id "
                + "where cs.category_id = ? and (s.seller_id=0 or s.seller_id=?)";
        List<SpecificationVO> specList = this.daoSupport.queryForList(sql, SpecificationVO.class, categoryId, seller.getSellerId());

        //没有规格
        if (specList == null || specList.size() == 0) {
            return new ArrayList<>();
        }
        //封装规格id的集合
        String[] temp = new String[specList.size()];
        List<Object> specIdList = new ArrayList<>();

        for (int i = 0; i < specList.size(); i++) {
            specIdList.add(specList.get(i).getSpecId());
            temp[i] = "?";
        }
        String str = StringUtil.arrayToString(temp, ",");
        specIdList.add(seller.getSellerId());

        String sqlValue = "select * from es_spec_values where spec_id in (" + str + ") and (seller_id=0 or seller_id=?)";
        //查询到的是所有规格的规格值
        List<SpecValuesDO> valueList = this.daoSupport.queryForList(sqlValue, SpecValuesDO.class, specIdList.toArray());

        Map<Integer, List<SpecValuesDO>> map = new HashMap<>(valueList.size());
        for (SpecValuesDO specValue : valueList) {

            List<SpecValuesDO> list = map.get(specValue.getSpecId());
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(specValue);
            map.put(specValue.getSpecId(), list);
        }
        //赋值规格值
        for (SpecificationVO vo : specList) {
            vo.setValueList(map.get(vo.getSpecId()));
        }

        return specList;

    }
}
