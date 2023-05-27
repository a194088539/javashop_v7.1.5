package com.enation.app.javashop.core.distribution.service;

import com.enation.app.javashop.core.distribution.model.dos.CommissionTpl;
import com.enation.app.javashop.framework.database.Page;

/**
 * 提成模板manager接口
 *
 * @author Chopper
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2018/5/22 上午10:52
 */

public interface CommissionTplManager {


    /**
     * 获取某个会员使用的模版
     *
     * @param memberId 会员id
     * @return CommissionTplDO
     */
    CommissionTpl getCommissionTplByMember(int memberId);

    /**
     * page
     *
     * @param page     页码
     * @param pageSize 分页大小
     * @return page
     */
    Page page(int page, int pageSize);

    /**
     * 通过id获得CommissionTpl
     *
     * @param id
     * @return CommissionTpl
     */
    CommissionTpl getModel(int id);


    /**
     * 添加一个commissionTpl
     *
     * @param commissionTpl 模版
     * @return CommissionTplDO
     */
    CommissionTpl add(CommissionTpl commissionTpl);


    /**
     * 修改一个CommissionTpl
     *
     * @param commissionTpl
     * @return CommissionTplDO
     */
    CommissionTpl edit(CommissionTpl commissionTpl);

    /**
     * 删除一个CommissionTpl
     * @param tplId
     */
    void delete(Integer tplId);

    /**
     * 得到一个默认的模版
     *
     * @return DO
     */
    CommissionTpl getDefaultCommission();
}
