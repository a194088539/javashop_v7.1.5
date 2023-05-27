package com.enation.app.javashop.core.member.service;

import com.enation.app.javashop.core.member.model.dos.MemberZpzzDO;
import com.enation.app.javashop.core.member.model.dto.ZpzzQueryParam;
import com.enation.app.javashop.framework.database.Page;

/**
 * 会员增票资质业务层
 *
 * @author duanmingyu
 * @version v7.1.4
 * @since v7.0.0
 * 2019-06-18
 */
public interface MemberZpzzManager {

    /**
     * 获取会员增票资质申请信息集合
     * @param pageNo 页数
     * @param pageSize 每页记录数
     * @param zpzzQueryParam 搜索参数
     * @return
     */
    Page list(Integer pageNo, Integer pageSize, ZpzzQueryParam zpzzQueryParam);

    /**
     * 新增会员增票资质信息
     * @param memberZpzzDO
     */
    MemberZpzzDO add(MemberZpzzDO memberZpzzDO);

    /**
     * 修改会员增票资质信息
     * @param memberZpzzDO
     */
    MemberZpzzDO edit(MemberZpzzDO memberZpzzDO, Integer id);

    /**
     * 根据ID获取会员增票资质信息
     * @param id 主键ID
     * @return
     */
    MemberZpzzDO get(Integer id);

    /**
     * 根据获取当前登录会员增票资质信息
     * @return
     */
    MemberZpzzDO get();

    /**
     * 删除会员增票资质信息
     * @param id 主键ID
     * @return
     */
    void delete(Integer id);

    /**
     * 审核会员增票资质申请
     * @param id 主键ID
     * @param status 审核状态 AUDIT_PASS：审核通过，AUDIT_REFUSE：审核未通过
     * @param remark 审核备注
     */
    void audit(Integer id, String status, String remark);
}
