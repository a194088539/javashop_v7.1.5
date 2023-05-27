package com.enation.app.javashop.core.goods.service.impl;

import com.enation.app.javashop.core.base.CachePrefix;
import com.enation.app.javashop.core.base.message.CategoryChangeMsg;
import com.enation.app.javashop.core.base.rabbitmq.AmqpExchange;
import com.enation.app.javashop.core.client.member.ShopClient;
import com.enation.app.javashop.core.goods.GoodsErrorCode;
import com.enation.app.javashop.core.goods.model.dos.CategoryBrandDO;
import com.enation.app.javashop.core.goods.model.dos.CategoryDO;
import com.enation.app.javashop.core.goods.model.dos.CategorySpecDO;
import com.enation.app.javashop.core.goods.model.vo.CategoryPluginVO;
import com.enation.app.javashop.core.goods.model.vo.CategoryVO;
import com.enation.app.javashop.core.goods.service.CategoryManager;
import com.enation.app.javashop.core.shop.model.vo.ShopVO;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.security.model.Seller;
import com.enation.app.javashop.framework.util.SqlUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品分类业务类
 *
 * @author fk
 * @version v1.0
 * @since v7.0.0 2018-03-15 17:22:06
 */
@Service
public class CategoryManagerImpl implements CategoryManager {

    @Autowired
    @Qualifier("goodsDaoSupport")
    protected DaoSupport goodsDaoSupport;


    @Autowired
    protected Cache cache;

    @Autowired
    protected AmqpTemplate amqpTemplate;

    @Autowired
    protected ShopClient shopClient;

    /**
     * 获取缓存key
     * @return
     */
    protected String getCacheKey() {
        return  CachePrefix.GOODS_CAT.getPrefix() + "ALL";

    }

    @Override
    @Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public CategoryDO add(CategoryDO category) {

        CategoryDO parent = null;
        //不能添加重复的分类名称
        String sqlQuery = "select * from es_category where name = ? ";
        List list = this.goodsDaoSupport.queryForList(sqlQuery, category.getName());
        if(StringUtil.isNotEmpty(list)){
            throw new ServiceException(GoodsErrorCode.E300.code(), "该分类名称已存在");
        }

        // 非顶级分类
        if (category.getParentId() != null && category.getParentId() != 0) {
            parent = this.getModel(category.getParentId());
            if (parent == null) {
                throw new ServiceException(GoodsErrorCode.E300.code(), "父分类不存在");
            }
            // 替换catPath 根据catPath规则来匹配级别
            String catPath = parent.getCategoryPath().replace("|", ",");
            String[] str = catPath.split(",");
            // 如果当前的catPath length 大于4 证明当前分类级别大于五级 提示
            if (str.length >= 4) {
                throw new ServiceException(GoodsErrorCode.E300.code(), "最多为三级分类,添加失败");
            }
        }

        this.goodsDaoSupport.insert(category);
        int categoryId = this.goodsDaoSupport.getLastId("");
        category.setCategoryId(categoryId);

        String sql = "";
        // 判断是否是顶级类似别，如果parentid为空或为0则为顶级类似别
        // 注意末尾都要加|，以防止查询子孙时出错
        // 不是顶级类别，有父
        if (parent != null) {
            category.setCategoryPath(parent.getCategoryPath() + categoryId + "|");
        } else {// 是顶级类别
            category.setCategoryPath("0|" + categoryId + "|");
        }

        sql = "update es_category set  category_path=? where  category_id=?";
        this.goodsDaoSupport.execute(sql, category.getCategoryPath(), categoryId);

        cache.remove(getCacheKey());

        CategoryChangeMsg categoryChangeMsg = new CategoryChangeMsg(categoryId, CategoryChangeMsg.ADD_OPERATION);
        this.amqpTemplate.convertAndSend(AmqpExchange.GOODS_CATEGORY_CHANGE, AmqpExchange.GOODS_CATEGORY_CHANGE + "_ROUTING", categoryChangeMsg);

        return category;
    }

    @Override
    @Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public CategoryDO edit(CategoryDO category, Integer id) {

        CategoryDO parent = null;

        CategoryDO catTemp = this.getModel(id);

        if (catTemp == null) {
            throw new ServiceException(GoodsErrorCode.E300.code(), "该分类不存在");
        }

        //不能添加重复的分类名称
        String sqlQuery = "select * from es_category where name = ? and category_id != ? ";
        List listQuery = this.goodsDaoSupport.queryForList(sqlQuery, category.getName(),id);
        if(StringUtil.isNotEmpty(listQuery)){
            throw new ServiceException(GoodsErrorCode.E300.code(), "该分类名称已存在");
        }

        // 如果有子分类则不能更换上级分类
        // 更换上级分类
        if (!category.getParentId().equals(catTemp.getParentId())) {
            // 查看是否有子分类
            List list = this.list(id, null);
            if (list != null && list.size() > 0) {
                throw new ServiceException(GoodsErrorCode.E300.code(), "当前分类有子分类，不能更换上级分类");
            } else {
                parent = this.getModel(category.getParentId());
                if (parent == null) {
                    throw new ServiceException(GoodsErrorCode.E300.code(), "父分类不存在");
                }
                // 替换catPath 根据catPath规则来匹配级别
                String catPath = parent.getCategoryPath().replace("|", ",");
                String[] str = catPath.split(",");
                // 如果当前的catPath length 大于4 证明当前分类级别大于五级 提示
                if (str.length >= 4) {
                    throw new ServiceException(GoodsErrorCode.E300.code(), "最多为三级分类,添加失败");
                }
                category.setCategoryPath(parent.getCategoryPath() + category.getCategoryId() + "|");
            }
        }

        this.goodsDaoSupport.update(category, id);

        cache.remove(getCacheKey());

        CategoryChangeMsg categoryChangeMsg = new CategoryChangeMsg(id, CategoryChangeMsg.UPDATE_OPERATION);
        this.amqpTemplate.convertAndSend(AmqpExchange.GOODS_CATEGORY_CHANGE, AmqpExchange.GOODS_CATEGORY_CHANGE + "_ROUTING", categoryChangeMsg);

        return category;
    }

    @Override
    public void delete(Integer id) {

        List<CategoryVO> list = this.listAllChildren(id);
        if (list != null && list.size() > 0) {
            throw new ServiceException(GoodsErrorCode.E300.code(), "此类别下存在子类别不能删除");
        }
        // 查询某商品分类的商品
        String goodsSql = "select count(0) from es_goods where category_id = ? and disabled != -1";
        Integer count = this.goodsDaoSupport.queryForInt(goodsSql, id);

        if (count > 0) {
            throw new ServiceException(GoodsErrorCode.E300.code(), "此类别下存在商品不能删除");
        }

        // 缓存
        cache.remove(CachePrefix.GOODS_CAT.getPrefix() + id);
        cache.remove(getCacheKey());

        this.goodsDaoSupport.delete(CategoryDO.class, id);
        this.goodsDaoSupport.execute("delete from es_category_brand where category_id = ?",id);
        this.goodsDaoSupport.execute("delete from es_category_spec where category_id = ?",id);
    }

    @Override
    public CategoryDO getModel(Integer id) {

        return this.goodsDaoSupport.queryForObject(CategoryDO.class, id);
    }

    @Override
    public List list(Integer parentId, String format) {
        if (parentId == null) {
            return null;
        }
        String sql = "select c.* from es_category c  where c.parent_id = ? order by c.category_order asc";
        if (format != null) {
            List<CategoryPluginVO> catlist = this.goodsDaoSupport.queryForList(sql, CategoryPluginVO.class, parentId);
            return catlist;
        } else {
            List<CategoryVO> catlist = this.goodsDaoSupport.queryForList(sql, CategoryVO.class, parentId);

            for (CategoryVO categoryVo : catlist) {
                List<CategoryVO> listAllChildren = this.listChildren(categoryVo.getCategoryId());
                if (listAllChildren.size() > 0) {
                    categoryVo.setChildren(listAllChildren);
                }
            }
            return catlist;
        }
    }

    /**
     * 获取某个类别的所有子类
     *
     * @param parentId
     * @return
     */
    protected List<CategoryVO> listChildren(Integer parentId) {
        // 从缓存取所有的分类
        List<CategoryDO> list = (List<CategoryDO>) cache.get(getCacheKey());
        if (list == null) {
            // 调用初始化分类缓存方法
            list = initCategory();
        }
        List<CategoryVO> topCatList = new ArrayList<CategoryVO>();

        for (CategoryDO cat : list) {
            CategoryVO categoryVo = new CategoryVO(cat);
            if (cat.getParentId().compareTo(parentId) == 0) {
                topCatList.add(categoryVo);
            }
        }
        return topCatList;
    }

    /**
     * 初始化分类缓存
     *
     * @return
     */
    protected List<CategoryDO> initCategory() {
        List<CategoryDO> list = this.getCategoryList();
        if (list != null && list.size() > 0) {
            for (CategoryDO cat : list) {
                cache.put(CachePrefix.GOODS_CAT.getPrefix() + cat.getCategoryId(), cat);
            }
            cache.put(getCacheKey(), list);
        }
        return list;
    }

    /**
     * 查询分类列表
     *
     * @return
     */
    protected List<CategoryDO> getCategoryList() {
        // 不能修改为缓存读取
        String sql = "select * from es_category order by category_order asc";
        List<CategoryDO> list = this.goodsDaoSupport.queryForList(sql, CategoryDO.class);
        return list;
    }

    @Override
    public List<CategoryVO> listAllChildren(Integer parentid) {
        // 从缓存取所有的分类
        List<CategoryDO> list = (List<CategoryDO>) cache.get(getCacheKey());
        if (list == null) {

            // 调用初始化分类缓存方法
            list = initCategory();
        }
        List<CategoryVO> topCatList = new ArrayList<CategoryVO>();

        for (CategoryDO cat : list) {
            CategoryVO categoryVo = new CategoryVO(cat);
            if (cat.getParentId().compareTo(parentid) == 0) {
                List<CategoryVO> children = this.getChildren(list, cat.getCategoryId());
                categoryVo.setChildren(children);
                topCatList.add(categoryVo);
            }
        }
        return topCatList;
    }


    @Override
    public List<CategoryVO> listAllSellerChildren() {

        Seller seller = UserContext.getSeller();

        ShopVO shop = shopClient.getShop(seller.getSellerId());

        String goodsManagementCategory = shop.getGoodsManagementCategory();

        StringBuffer likeSql = new StringBuffer();
        String[] categoryId = goodsManagementCategory.split(",");
        if (categoryId.length > 0) {
            likeSql.append("where ");
        }
        for (String category : categoryId) {
            likeSql.append(" category_path like ('0|" + category + "%') or ");
        }
        String like = "";
        if (likeSql.length() > 2) {
            like = likeSql.substring(0, likeSql.length() - 3);
        }

        String sql = "select * from es_category c " +
                like +
                "order by category_order";
        List<CategoryDO> list = this.goodsDaoSupport.queryForList(sql, CategoryDO.class);

        List<CategoryVO> vos = new ArrayList<>();
        for (CategoryDO categoryDO : list) {
            if (categoryDO.getParentId().equals(0)) {
                vos.add(new CategoryVO(categoryDO));
            }
        }
        vos = reSet(list, vos);

        return vos;
    }

    protected List<CategoryVO> reSet(List<CategoryDO> list, List<CategoryVO> vos) {
        for (CategoryVO vo : vos) {
            List<CategoryVO> child = new ArrayList<>();
            for (CategoryDO cat : list) {
                if (cat.getParentId().equals(vo.getCategoryId())) {
                    CategoryVO categoryVo = new CategoryVO(cat);
                    child.add(categoryVo);
                }
            }
            if (child.size() > 0) {
                vo.setChildren(child);
                this.reSet(list, child);
            }
        }
        return vos;
    }

    /**
     * 得到当前分类的子孙
     *
     * @param catList  分类集合
     * @param parentid 父分类id
     * @return 带子分类的集合
     */
    protected List<CategoryVO> getChildren(List<CategoryDO> catList, Integer parentid) {
        List<CategoryVO> children = new ArrayList<CategoryVO>();
        for (CategoryDO cat : catList) {
            CategoryVO categoryVo = new CategoryVO(cat);
            if (cat.getParentId().compareTo(parentid) == 0) {
                categoryVo.setChildren(this.getChildren(catList, cat.getCategoryId()));
                children.add(categoryVo);
            }
        }
        return children;
    }

    @Override
    public List<CategoryDO> getSellerCategory(Integer categoryId) {
        String sql = "";
        Seller seller = UserContext.getSeller();

        ShopVO shop = shopClient.getShop(seller.getSellerId());

        String goodsManagementCategory = shop.getGoodsManagementCategory();
        if (categoryId == 0 && StringUtil.notEmpty(goodsManagementCategory)) {
            sql = "select * from es_category c where category_id in (" + goodsManagementCategory + ") and parent_id=? order by category_order";
            return this.goodsDaoSupport.queryForList(sql, CategoryDO.class, categoryId);
        } else {
            sql = "select * from es_category  where parent_id=?";
            return this.goodsDaoSupport.queryForList(sql, CategoryDO.class, categoryId);
        }
    }

    @Override
    @Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public List<CategoryBrandDO> saveBrand(Integer categoryId, Integer[] chooseBrands) {

        CategoryDO category = this.getModel(categoryId);
        if (category == null) {
            throw new ServiceException(GoodsErrorCode.E300.code(), "该分类不存在");
        }
        List<CategoryBrandDO> res = new ArrayList<>();
        if(chooseBrands==null){
            this.goodsDaoSupport.execute("delete from es_category_brand where category_id = ?", categoryId);
            return res;
        }


        //查看所选品牌是否存在
        List<Object> term = new ArrayList<>();
        String str = SqlUtil.getInSql(chooseBrands, term);
        Integer count = this.goodsDaoSupport.queryForInt("select count(0) from es_brand where brand_id in(" + str + ")", term.toArray());

        if (count < chooseBrands.length) {
            throw new ServiceException(GoodsErrorCode.E300.code(), "品牌传参错误");
        }
        String sql = "delete from es_category_brand where category_id = ?";
        this.goodsDaoSupport.execute(sql, categoryId);

        for (int i = 0; i < chooseBrands.length; i++) {
            CategoryBrandDO categoryBrand = new CategoryBrandDO(categoryId, chooseBrands[i]);
            this.goodsDaoSupport.insert(categoryBrand);

            res.add(categoryBrand);
        }

        return res;
    }

    @Override
    @Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public List<CategorySpecDO> saveSpec(Integer categoryId, Integer[] chooseSpecs) {

        CategoryDO category = this.getModel(categoryId);
        if (category == null) {
            throw new ServiceException(GoodsErrorCode.E300.code(), "该分类不存在");
        }
        List<CategorySpecDO> res = new ArrayList<>();
        if(chooseSpecs==null){
            this.goodsDaoSupport.execute("delete from es_category_spec where category_id = ?", categoryId);
            return res;
        }

        //查看所选规格是否存在
        List<Object> term = new ArrayList<>();
        String str = SqlUtil.getInSql(chooseSpecs, term);
        Integer count = this.goodsDaoSupport.queryForInt("select count(0) from es_specification where spec_id in(" + str + ")", term.toArray());

        if (count < chooseSpecs.length) {
            throw new ServiceException(GoodsErrorCode.E300.code(), "规格绑定传参错误:规格数量不一致");
        }

        String sql = "delete from es_category_spec where category_id = ?";
        this.goodsDaoSupport.execute(sql, categoryId);


        for (Integer specId : chooseSpecs) {
            CategorySpecDO categorySpec = new CategorySpecDO(categoryId, specId);
            this.goodsDaoSupport.insert(categorySpec);
            res.add(categorySpec);
        }
        return res;
    }

}
