package com.enation.app.javashop.core.shop.service.impl;

import com.enation.app.javashop.core.base.CachePrefix;
import com.enation.app.javashop.core.shop.ShopErrorCode;
import com.enation.app.javashop.core.shop.model.dos.Clerk;
import com.enation.app.javashop.core.shop.model.dos.ShopRole;
import com.enation.app.javashop.core.shop.model.vo.ShopMenus;
import com.enation.app.javashop.core.shop.model.vo.ShopRoleVO;
import com.enation.app.javashop.core.shop.service.ShopRoleManager;
import com.enation.app.javashop.core.system.model.dos.RoleDO;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.exception.ResourceNotFoundException;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.util.JsonUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
 * 店铺角色业务类
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-08-02 15:22:20
 */
@Service
public class ShopRoleManagerImpl implements ShopRoleManager {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport memberDaoSupport;

    @Autowired
    private Cache cache;


    @Override
    public Page list(int page, int pageSize) {
        String sql = "select role_id,role_name,role_describe from es_shop_role where shop_id = ?";
        Page webPage = this.memberDaoSupport.queryForPage(sql, page, pageSize, RoleDO.class, UserContext.getSeller().getSellerId());
        return webPage;
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ShopRoleVO add(ShopRoleVO shopRoleVO) {


        //首先校验角色名称是否已经存在,如果存在抛出异常
        String roleName = shopRoleVO.getRoleName();
        int sellerId = StringUtil.toInt(UserContext.getSeller().getSellerId(), false);
        String sql = "select count(0) from es_shop_role where  shop_id = ? and  role_name=?";
        int count = memberDaoSupport.queryForInt(sql, sellerId, roleName);
        if (count > 0) {
            if (logger.isDebugEnabled()) {
                logger.debug("试图添加店铺角色时发现已经存在要添加的角色名称：【" + roleName + "】，店铺id为：【" + sellerId + "】");
            }
            throw new ServiceException(ShopErrorCode.E228.code(), ShopErrorCode.E228.getDescribe());
        }


        //角色信息入库
        ShopRole shopRole = new ShopRole();
        shopRole.setRoleName(shopRoleVO.getRoleName());
        shopRole.setAuthIds(JsonUtil.objectToJson(shopRoleVO.getMenus()));
        shopRole.setRoleDescribe(shopRoleVO.getRoleDescribe());
        shopRole.setShopId(sellerId);
        this.memberDaoSupport.insert(shopRole);
        shopRoleVO.setRoleId(memberDaoSupport.getLastId("es_shop_role"));
        cache.remove(CachePrefix.SHOP_URL_ROLE.getPrefix() + UserContext.getSeller().getSellerId());
        return shopRoleVO;
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ShopRoleVO edit(ShopRoleVO shopRoleVO, Integer id) {


        ShopRole shopRole = this.getModel(id);
        if (shopRole == null) {
            throw new ResourceNotFoundException("此角色不存在");
        }

        //首先校验角色名称是否已经存在,如果存在抛出异常，和添加校验方式不同的是：此处要将非本角色的过滤，即：role_id!=?
        String roleName = shopRoleVO.getRoleName();
        int sellerId = StringUtil.toInt(UserContext.getSeller().getSellerId(), false);
        String sql = "select count(0) from es_shop_role where  shop_id = ? and role_id!=? and  role_name=?";
        int count = memberDaoSupport.queryForInt(sql, sellerId, id, roleName);
        if (count > 0) {
            if (logger.isDebugEnabled()) {
                logger.debug("试图修改店铺角色时发现已经存在要添加的角色名称：【" + roleName + "】，店铺id为：【" + sellerId + "】");
            }
            throw new ServiceException(ShopErrorCode.E228.code(), ShopErrorCode.E228.getDescribe());
        }


        shopRole.setRoleName(shopRoleVO.getRoleName());
        shopRole.setAuthIds(JsonUtil.objectToJson(shopRoleVO.getMenus()));
        shopRole.setRoleDescribe(shopRoleVO.getRoleDescribe());
        shopRole.setShopId(UserContext.getSeller().getSellerId());
        this.memberDaoSupport.update(shopRole, id);
        //删除缓存中角色所拥有的菜单权限
        cache.remove(CachePrefix.SHOP_URL_ROLE.getPrefix() + UserContext.getSeller().getSellerId());
        return shopRoleVO;
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delete(Integer id) {
        ShopRole shopRole = this.getModel(id);
        if (shopRole == null) {
            throw new ResourceNotFoundException("此角色不存在");
        }
        //校验此觉得是否已经被使用
        String sql = "select * from es_clerk where shop_id = ? and role_id = ?";
        List<Clerk> clerks = this.memberDaoSupport.queryForList(sql, Clerk.class, UserContext.getSeller().getSellerId(), id);
        if (clerks.size() > 0) {
            throw new ServiceException(ShopErrorCode.E229.code(), "此角色已经被使用");
        }
        cache.remove(CachePrefix.SHOP_URL_ROLE.getPrefix() + UserContext.getSeller().getSellerId());
        this.memberDaoSupport.delete(ShopRole.class, id);
    }

    @Override
    public ShopRole getModel(Integer id) {
        return this.memberDaoSupport.queryForObject(ShopRole.class, id);
    }

    @Override
    public Map<String, List<String>> getRoleMap(Integer sellerId) {

        Map<String, List<String>> roleMap = new HashMap<>(16);
        String sql = "select * from es_shop_role where shop_id = ?";
        List<RoleDO> roles = this.memberDaoSupport.queryForList(sql, RoleDO.class, sellerId);
        for (int i = 0; i < roles.size(); i++) {
            List<ShopMenus> menusList = JsonUtil.jsonToList(roles.get(i).getAuthIds(), ShopMenus.class);
            if (menusList.size() > 0) {
                List<String> authList = new ArrayList<>();
                //递归查询角色所拥有的菜单权限
                this.getChildren(menusList, authList);
                roleMap.put(roles.get(i).getRoleName(), authList);
                cache.put(CachePrefix.SHOP_URL_ROLE.getPrefix()+ sellerId, roleMap);
            }
        }
        return roleMap;

    }

    /**
     * 递归将此角色锁拥有的菜单权限保存到list
     *
     * @param menuList 菜单集合
     * @param authList 权限组集合
     */
    private void getChildren(List<ShopMenus> menuList, List<String> authList) {
        for (ShopMenus menus : menuList) {
            //将此角色拥有的菜单权限放入list中
            if (menus.isChecked()) {
                authList.add(menus.getAuthRegular());
            }
            if (!menus.getChildren().isEmpty()) {
                getChildren(menus.getChildren(), authList);
            }
        }
    }

    @Override
    public List<String> getRoleMenu(Integer id) {
        ShopRole shopRole = this.getModel(id);
        if (shopRole == null || !shopRole.getShopId().equals(UserContext.getSeller().getSellerId())) {
            throw new ResourceNotFoundException("此角色不存在");
        }
        List<ShopMenus> menusList = JsonUtil.jsonToList(shopRole.getAuthIds(), ShopMenus.class);
        List<String> authList = new ArrayList<>();
        //筛选菜单
        this.reset(menusList, authList);
        return authList;
    }

    /**
     * 筛选checked为true的菜单
     *
     * @param menuList 菜单集合
     */
    private void reset(List<ShopMenus> menuList, List<String> authList) {
        for (ShopMenus menus : menuList) {
            //将此角色拥有的菜单权限放入list中
            if (menus.isChecked()) {
                authList.add(menus.getIdentifier());
            }
            if (!menus.getChildren().isEmpty()) {
                reset(menus.getChildren(), authList);
            }
        }
    }

    @Override
    public ShopRoleVO getRole(Integer id) {
        return new ShopRoleVO(this.getModel(id));
    }
}
