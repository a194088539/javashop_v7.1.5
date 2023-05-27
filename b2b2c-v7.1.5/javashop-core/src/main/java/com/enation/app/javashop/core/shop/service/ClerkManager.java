package com.enation.app.javashop.core.shop.service;

import com.enation.app.javashop.core.shop.model.dto.ClerkDTO;
import com.enation.app.javashop.core.shop.model.vo.ClerkVO;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.core.shop.model.dos.Clerk;

/**
 * 店员业务层
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-08-04 18:48:39
 */
public interface ClerkManager {

    /**
     * 查询店员列表
     *
     * @param page     页码
     * @param pageSize 每页数量
     * @param disabled 店员状态
     * @param keyword 关键字
     * @return Page
     */
    Page list(int page, int pageSize, int disabled, String keyword);

    /**
     * 添加店员
     *
     * @param clerkVO 店员
     * @return Clerk 店员
     */
    Clerk addNewMemberClerk(ClerkVO clerkVO);

    /**
     * 添加老会员为店员
     *
     * @param clerkDTO 店员dto
     * @return Clerk 店员
     */
    Clerk addOldMemberClerk(ClerkDTO clerkDTO);

    /**
     * 添加超级店员
     *
     * @param clerk 店员信息
     * @return 店员信息
     */
    Clerk addSuperClerk(Clerk clerk);

    /**
     * 修改店员
     *
     * @param clerk 店员
     * @param id    店员主键
     * @return Clerk 店员
     */
    Clerk edit(Clerk clerk, Integer id);

    /**
     * 删除店员
     *
     * @param id 店员主键
     */
    void delete(Integer id);

    /**
     * 获取店员
     *
     * @param id 店员主键
     * @return Clerk  店员
     */
    Clerk getModel(Integer id);

    /**
     * 恢复店员
     *
     * @param id 店员id
     */
    void recovery(Integer id);

    /**
     * 根据会员id查询店员
     *
     * @param memberId
     * @return
     */
    Clerk getClerkByMemberId(Integer memberId);

}