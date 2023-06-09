package com.enation.app.javashop.core.goods.service.impl;

import com.enation.app.javashop.core.goods.GoodsErrorCode;
import com.enation.app.javashop.core.goods.model.dos.BrandDO;
import com.enation.app.javashop.core.goods.model.vo.SelectVO;
import com.enation.app.javashop.core.goods.service.BrandManager;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.util.SqlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 品牌业务类
 *
 * @author fk
 * @version v2.0
 * @since v7.0.0 2018-03-16 16:32:46
 */
@Service
public class BrandManagerImpl implements BrandManager {

    @Autowired
    @Qualifier("goodsDaoSupport")
    private DaoSupport daoSupport;

    @Override
    public Page list(int page, int pageSize, String name) {

        StringBuffer sqlBuffer = new StringBuffer("select * from es_brand ");
        List<Object> term = new ArrayList<>();
        if (name != null) {
            sqlBuffer.append(" where name like ? ");
            term.add("%" + name + "%");
        }

        sqlBuffer.append(" order by brand_id desc ");

        Page webPage = this.daoSupport.queryForPage(sqlBuffer.toString(), page, pageSize, BrandDO.class, term.toArray());

        return webPage;
    }

    @Override
    @Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public BrandDO add(BrandDO brand) {

        String sql = "select * from es_brand where name = ? ";
        List list = this.daoSupport.queryForList(sql, brand.getName());
        if (list.size() > 0) {
            throw new ServiceException(GoodsErrorCode.E302.code(), "品牌名称重复");
        }

        brand.setDisabled(1);
        this.daoSupport.insert(brand);
        brand.setBrandId(this.daoSupport.getLastId(""));

        return brand;
    }

    @Override
    public BrandDO edit(BrandDO brand, Integer id) {
        BrandDO brandDO = this.getModel(id);
        if (brandDO == null) {
            throw new ServiceException(GoodsErrorCode.E302.code(), "品牌不存在");
        }

        String sql = "select * from es_brand where name = ? and brand_id != ? ";
        List list = this.daoSupport.queryForList(sql, brand.getName(),id);
        if (list.size() > 0) {
            throw new ServiceException(GoodsErrorCode.E302.code(), "品牌名称重复");
        }

        this.daoSupport.update(brand, id);
        return brand;
    }

    @Override
    public void delete(Integer[] ids) {
        List term = new ArrayList<>();

        String idsStr = SqlUtil.getInSql(ids, term);

        //检测是否有分类关联
        String sql = "select count(0) from es_category_brand where brand_id in (" + idsStr + ")";
        Integer count = this.daoSupport.queryForInt(sql, term.toArray());
        if(count > 0){
            throw new ServiceException(GoodsErrorCode.E302.code(), "已有分类关联，不能删除");
        }
        // 检测是否有商品关联
        String checkSql = "select count(0) from es_goods where (disabled = 1 or disabled = 0) and brand_id in (" + idsStr + ")";
        int hasRel = this.daoSupport.queryForInt(checkSql, term.toArray());
        if (hasRel > 0) {
            throw new ServiceException(GoodsErrorCode.E302.code(), "已有商品关联，不能删除");
        }

        sql = "delete from es_brand where brand_id in (" + idsStr + ") ";

        this.daoSupport.execute(sql, term.toArray());
    }

    @Override
    public BrandDO getModel(Integer id) {
        return this.daoSupport.queryForObject(BrandDO.class, id);
    }

    @Override
    public List<BrandDO> getBrandsByCategory(Integer categoryId) {

        String sql = "select b.brand_id,b.`name`,b.logo "
                + "from es_category_brand cb inner join es_brand b on cb.brand_id=b.brand_id "
                + "where cb.category_id=? and b.disabled = 1 ";
        List<BrandDO> list = this.daoSupport.queryForList(sql, BrandDO.class, categoryId);
        return list;

    }

    @Override
    public List<SelectVO> getCatBrand(Integer categoryId) {

        String sql = "select b.brand_id id ,b.`name` text,case category_id when ?  then true else false end selected "
                + " from es_brand b left join  es_category_brand cb on b.brand_id=cb.brand_id and category_id = ?"
                + " where b.disabled=1 ";

        return this.daoSupport.queryForList(sql, SelectVO.class, categoryId, categoryId);

    }

    @Override
    public List<BrandDO> getAllBrands() {

        String sql = "select * from es_brand order by brand_id desc ";

        return this.daoSupport.queryForList(sql, BrandDO.class);
    }
}