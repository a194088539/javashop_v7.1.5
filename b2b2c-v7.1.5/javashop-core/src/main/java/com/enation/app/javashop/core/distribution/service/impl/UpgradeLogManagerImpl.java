package com.enation.app.javashop.core.distribution.service.impl;

import com.enation.app.javashop.core.client.member.MemberClient;
import com.enation.app.javashop.core.distribution.model.dos.CommissionTpl;
import com.enation.app.javashop.core.distribution.model.dos.UpgradeLogDO;
import com.enation.app.javashop.core.distribution.model.enums.UpgradeTypeEnum;
import com.enation.app.javashop.core.distribution.service.CommissionTplManager;
import com.enation.app.javashop.core.distribution.service.DistributionManager;
import com.enation.app.javashop.core.distribution.service.UpgradeLogManager;
import com.enation.app.javashop.core.member.model.dos.Member;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 升级日志 实现
 *
 * @author Chopper
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2018/5/22 下午12:58
 */

@Component
public class UpgradeLogManagerImpl implements UpgradeLogManager {

    @Autowired
    @Qualifier("distributionDaoSupport")
    private DaoSupport daoSupport;
    @Autowired
    private DistributionManager distributionManager;
    @Autowired
    private CommissionTplManager commissionTplManager;

    @Autowired
    private MemberClient memberClient;

    @Override
    public Page page(int page, int pageSize, String memberName) {
        String sql = "SELECT * FROM es_upgrade_log";

        List<String> params = new ArrayList<>();
        // 只传入了搜索的名字
        if (!StringUtil.isEmpty(memberName)) {
            sql += " WHERE member_name LIKE ?";
            params.add("%" + memberName + "%");
        }
        sql += " ORDER BY create_time DESC";
        Page webpage = this.daoSupport.queryForPage(sql, page, pageSize,params.toArray());
        return webpage;
    }

    @Override
    public UpgradeLogDO add(UpgradeLogDO upgradeLog) {

        // 非空
        if (upgradeLog != null) {
            this.daoSupport.insert("es_upgrade_log", upgradeLog);
        }
        return upgradeLog;
    }

    @Override
    public void addUpgradeLog(int memberId, int newTplId, UpgradeTypeEnum upgradeType) {
        UpgradeLogDO upgradelog = new UpgradeLogDO();
        Member member = this.memberClient.getModel(memberId);
        int oldTplId = this.distributionManager.getDistributorByMemberId(memberId).getCurrentTplId();
        CommissionTpl oldTpl = this.commissionTplManager.getModel(oldTplId);
        CommissionTpl newTpl = this.commissionTplManager.getModel(newTplId);

        //set数据
        upgradelog.setMemberId(memberId);
        if(member!=null) {
            upgradelog.setMemberName(member.getUname());
        }else{
            upgradelog.setMemberName("无名");
        }

        // 如果有 就记录
        if (oldTpl != null) {
            upgradelog.setOldTplId(oldTplId);
            upgradelog.setOldTplName(oldTpl.getTplName());
        }

        upgradelog.setNewTplId(newTplId);
        upgradelog.setNewTplName(newTpl.getTplName());
        upgradelog.setType(upgradeType.getName());
        upgradelog.setCreateTime(DateUtil.getDateline());
        this.add(upgradelog);
    }
}
