package com.enation.app.javashop.core.shop.service.impl;

import com.enation.app.javashop.core.shop.model.dos.ShopMenu;
import com.enation.app.javashop.core.shop.model.vo.ShopMenuVO;
import com.enation.app.javashop.core.shop.model.vo.ShopMenusVO;
import com.enation.app.javashop.core.shop.service.ShopMenuManager;
import com.enation.app.javashop.core.system.SystemErrorCode;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.exception.ResourceNotFoundException;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单管理店铺业务类
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-08-03 17:17:26
 */
@Service
public class ShopMenuManagerImpl implements ShopMenuManager {

    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport memberDaoSupport;

    @Override
    public Page list(int page, int pageSize) {

        String sql = "select * from es_shop_menu";
        Page webPage = this.memberDaoSupport.queryForPage(sql, page, pageSize, ShopMenu.class);

        return webPage;
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ShopMenu add(ShopMenuVO shopMenuVO) {
        //对菜单的唯一标识做校验
        ShopMenu valMenu = this.getMenuByIdentifier(shopMenuVO.getIdentifier());
        if (valMenu != null) {
            throw new ServiceException(SystemErrorCode.E913.code(), "菜单唯一标识重复");
        }
        //判断父级菜单是否有效
        ShopMenu parentMenu = this.getModel(shopMenuVO.getParentId());
        if (!shopMenuVO.getParentId().equals(0) && parentMenu == null) {
            throw new ResourceNotFoundException("父级菜单不存在");
        }
        //校验菜单级别是否超出限制
        if (!shopMenuVO.getParentId().equals(0) && parentMenu.getGrade() >= 3) {
            throw new ServiceException(SystemErrorCode.E914.code(), "菜单级别最多为3级");
        }
        //执行保存操作
        ShopMenu menu = new ShopMenu();
        BeanUtil.copyProperties(shopMenuVO, menu);
        menu.setDeleteFlag(0);
        this.memberDaoSupport.insert(menu);
        menu.setId(memberDaoSupport.getLastId("es_shop_menu"));
        return this.updateMenu(menu);
    }

    /**
     * 执行修改菜单操作
     *
     * @param menu 菜单对象
     * @return 菜单对象
     */
    private ShopMenu updateMenu(ShopMenu menu) {
        //判断父级菜单是否有效
        ShopMenu parentMenu = this.getModel(menu.getParentId());
        if (!menu.getParentId().equals(0) && parentMenu == null) {
            throw new ResourceNotFoundException("父级菜单不存在");
        }
        //校验菜单级别是否超出限制
        if (!menu.getParentId().equals(0) && parentMenu.getGrade() >= 3) {
            throw new ServiceException(SystemErrorCode.E914.code(), "菜单级别最多为3级");
        }
        String menuPath = null;
        if (menu.getParentId().equals(0)) {
            menuPath = "," + menu.getId() + ",";
        } else {
            menuPath = parentMenu.getPath() + menu.getId() + ",";
        }
        String subMenu = menuPath.substring(0, menuPath.length() - 1);
        String[] menus = subMenu.substring(1).split(",");
        menu.setGrade(menus.length);
        menu.setPath(menuPath);
        this.memberDaoSupport.update(menu, menu.getId());
        return menu;
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ShopMenu edit(ShopMenu shopMenu, Integer id) {
        //校验当前菜单是否存在
        ShopMenu valMenu = this.getModel(id);
        if (valMenu == null) {
            throw new ResourceNotFoundException("当前菜单不存在");
        }
        //校验菜单唯一标识重复
        valMenu = this.getMenuByIdentifier(shopMenu.getIdentifier());
        if (valMenu != null && !valMenu.getId().equals(id)) {
            throw new ServiceException(SystemErrorCode.E913.code(), "菜单唯一标识重复");
        }
        shopMenu.setId(id);
        //执行修改
        return this.updateMenu(shopMenu);
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delete(Integer id) {

        ShopMenu menu = this.getModel(id);
        if (menu == null) {
            throw new ResourceNotFoundException("当前菜单不存在");
        }
        this.memberDaoSupport.execute("update es_menu set delete_flag = -1 where id = ?", id);
        this.memberDaoSupport.execute("update es_menu set delete_flag = -1 where parent_id = ?", id);
    }

    @Override
    public ShopMenu getModel(Integer id) {
        return this.memberDaoSupport.queryForObject(ShopMenu.class, id);
    }


    @Override
    public List<ShopMenusVO> getMenuTree(Integer id) {
        List<ShopMenusVO> menuList = this.memberDaoSupport.queryForList("select * from es_shop_menu where delete_flag = '0' order by id asc", ShopMenusVO.class);
        List<ShopMenusVO> topMenuList = new ArrayList<ShopMenusVO>();
        for (ShopMenusVO menu : menuList) {
            if (menu.getParentId().compareTo(id) == 0) {
                List<ShopMenusVO> children = this.getChildren(menuList, menu.getId());
                menu.setChildren(children);
                topMenuList.add(menu);
            }
        }
        return topMenuList;
    }

    /**
     * 在一个集合中查找子
     *
     * @param menuList 所有菜单集合
     * @param parentid 父id
     * @return 找到的子集合
     */
    private List<ShopMenusVO> getChildren(List<ShopMenusVO> menuList, Integer parentid) {
        List<ShopMenusVO> children = new ArrayList<ShopMenusVO>();
        for (ShopMenusVO menu : menuList) {
            if (menu.getParentId().compareTo(parentid) == 0) {
                menu.setChildren(this.getChildren(menuList, menu.getId()));
                children.add(menu);
            }
        }
        return children;
    }

    @Override
    public ShopMenu getMenuByIdentifier(String identifier) {
        return this.memberDaoSupport.queryForObject("select * from es_shop_menu where delete_flag = '0' and identifier = ?", ShopMenu.class, identifier);

    }
}
