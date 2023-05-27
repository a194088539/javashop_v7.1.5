package com.enation.app.javashop.core.distribution.service.impl;

import com.enation.app.javashop.core.client.member.ConnectClient;
import com.enation.app.javashop.core.client.trade.WechatSmallchangeClient;
import com.enation.app.javashop.core.distribution.exception.DistributionErrorCode;
import com.enation.app.javashop.core.distribution.exception.DistributionException;
import com.enation.app.javashop.core.distribution.model.dos.DistributionDO;
import com.enation.app.javashop.core.distribution.model.dos.WithdrawApplyDO;
import com.enation.app.javashop.core.distribution.model.dos.WithdrawSettingDO;
import com.enation.app.javashop.core.distribution.model.enums.WithdrawStatusEnum;
import com.enation.app.javashop.core.distribution.model.vo.BankParamsVO;
import com.enation.app.javashop.core.distribution.model.vo.WithdrawApplyVO;
import com.enation.app.javashop.core.distribution.model.vo.WithdrawAuditPaidVO;
import com.enation.app.javashop.core.distribution.service.DistributionManager;
import com.enation.app.javashop.core.distribution.service.WithdrawManager;
import com.enation.app.javashop.core.member.model.dos.ConnectDO;
import com.enation.app.javashop.core.member.model.enums.ConnectTypeEnum;
import com.enation.app.javashop.core.member.service.ConnectManager;
import com.enation.app.javashop.core.payment.service.WechatSmallchangeManager;
import com.enation.app.javashop.core.trade.order.service.TradeSnCreator;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.util.*;
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
 * 提现设置实现
 *
 * @author Chopper
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2018/5/22 下午12:57
 */

@Service
public class WithdrawManagerImpl implements WithdrawManager {


    @Autowired
    @Qualifier("distributionDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    private DistributionManager distributionManager;


    @Autowired
    private ConnectClient connectClient;

    @Autowired
    private WechatSmallchangeClient wechatSmallchangeClient;

    @Autowired
    private TradeSnCreator tradeSnCreator;


    @Override
    public WithdrawApplyDO getModel(Integer id) {
        String applysql = "select * from es_withdraw_apply where id=?";
        return this.daoSupport.queryForObject(applysql, WithdrawApplyDO.class, id);

    }


    @Override
    public Page<WithdrawApplyVO> pageWithdrawApply(Integer memberId, Integer pageNo, Integer pageSize) {
        String sql = "select * from es_withdraw_apply where member_id=? order by id desc";

        Page<WithdrawApplyDO> page = this.daoSupport.queryForPage(sql, pageNo, pageSize, WithdrawApplyDO.class, memberId);
        Page result = convertPage(page);
        return result;

    }




    @Override
    public void saveWithdrawWay(BankParamsVO bankParams) {
        String sql = "select * from es_withdraw_setting where member_id=?";
        Integer userId = UserContext.getBuyer().getUid();
        WithdrawSettingDO withdrawSetting = this.daoSupport.queryForObject(sql, WithdrawSettingDO.class,
                userId);
        if (withdrawSetting != null) {
            withdrawSetting.setMemberId(userId);
            withdrawSetting.setParam(JsonUtil.objectToJson(bankParams));
            Map where = new HashMap(16);
            where.put("id", withdrawSetting.getId());
            this.daoSupport.update("es_withdraw_setting", withdrawSetting, where);
        } else {
            withdrawSetting = new WithdrawSettingDO();
            withdrawSetting.setMemberId(userId);
            withdrawSetting.setParam(JsonUtil.objectToJson(bankParams));
            this.daoSupport.insert("es_withdraw_setting", withdrawSetting);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void applyWithdraw(Integer memberId, Double applyMoney, String applyRemark) {
        WithdrawApplyDO apply = new WithdrawApplyDO();
        apply.setApplyTime(DateUtil.getDateline());
        apply.setApplyMoney(applyMoney);
        apply.setApplyRemark(applyRemark);
        apply.setStatus(WithdrawStatusEnum.APPLY.name());
        apply.setMemberId(memberId);
        apply.setMemberName(UserContext.getBuyer().getUsername());
        apply.setSn(tradeSnCreator.generateSmallChangeLogSn());
        apply.setIp(IPUtil.getIpAdrress());
        this.daoSupport.insert("es_withdraw_apply", apply);
        // 修改可提现金额
        String sql = "update es_distribution set can_rebate=can_rebate-?,withdraw_frozen_price=withdraw_frozen_price+? where member_id =?";
        this.daoSupport.execute(sql, applyMoney, applyMoney, memberId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void batchAuditing(WithdrawAuditPaidVO withdrawAuditPaidVO, String auditResult) {
        //判断是否选择了提现申请
        if (withdrawAuditPaidVO.getApplyIds() == null || withdrawAuditPaidVO.getApplyIds().length == 0) {
            throw new DistributionException(DistributionErrorCode.E1000.code(), "请选择要审核的提现申请");
        }

        //判断审核状态值是否正确
        if (StringUtil.isEmpty(auditResult) || (!WithdrawStatusEnum.VIA_AUDITING.value().equals(auditResult) && !WithdrawStatusEnum.FAIL_AUDITING.value().equals(auditResult))) {
            throw new DistributionException(DistributionErrorCode.E1005.code(), "审核状态值不正确");
        }

        for (Integer applyId : withdrawAuditPaidVO.getApplyIds()) {
            WithdrawApplyDO wdo = this.getModel(applyId);
            //判断提现申请是否存在
            if (wdo == null) {
                throw new DistributionException(DistributionErrorCode.E1004.code(), "ID为"+wdo.getId()+"的提现申请不存在");
            }

            //除状态为申请中的提现申请，其它状态的提现申请都不允许审核
            if (!WithdrawStatusEnum.APPLY.value().equals(wdo.getStatus())) {
                throw new DistributionException(DistributionErrorCode.E1002.code(), "ID为"+wdo.getId()+"的提现申请已经审核，不能重复审核");
            }

            //判断审核状态值是否正确
            if (StringUtil.isEmpty(auditResult) || (!WithdrawStatusEnum.VIA_AUDITING.value().equals(auditResult) && !WithdrawStatusEnum.FAIL_AUDITING.value().equals(auditResult))) {
                throw new DistributionException(DistributionErrorCode.E1005.code(), "审核状态值不正确");
            }

            //更改提现申请审核状态数据
            String applySql = "update es_withdraw_apply set status=?,inspect_time=?,inspect_remark=? where id=?";
            this.daoSupport.execute(applySql, auditResult, DateUtil.getDateline(), withdrawAuditPaidVO.getRemark(), applyId);

            //如果审核未通过，要将提现的金额返还
            if (WithdrawStatusEnum.FAIL_AUDITING.name().equals(auditResult)) {
                // 获取分销商信息
                DistributionDO distributionDO = this.distributionManager.getDistributorByMemberId(wdo.getMemberId());

                double rebate = CurrencyUtil.add(distributionDO.getCanRebate(), wdo.getApplyMoney());
                double frozen = CurrencyUtil.sub(distributionDO.getWithdrawFrozenPrice(), wdo.getApplyMoney());

                String sql = "update es_distribution set can_rebate=?,withdraw_frozen_price=? where member_id =?";
                this.daoSupport.execute(sql, rebate, frozen, wdo.getMemberId());
            }else{
                autoSend(applyId);
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void batchAccountPaid(WithdrawAuditPaidVO withdrawAuditPaidVO) {
        //判断是否选择了提现申请
        if (withdrawAuditPaidVO.getApplyIds() == null || withdrawAuditPaidVO.getApplyIds().length == 0) {
            throw new DistributionException(DistributionErrorCode.E1000.code(), "请选择要设置为已转账的提现申请");
        }

        for (Integer applyId : withdrawAuditPaidVO.getApplyIds()) {
            WithdrawApplyDO wdo = this.getModel(applyId);
            //判断提现申请是否存在
            if (wdo == null) {
                throw new DistributionException(DistributionErrorCode.E1004.code(), "ID为"+wdo.getId()+"的提现申请不存在");
            }

            //除状态为审核通过的提现申请，其它状态的提现申请都不允许设置已转账
            if (!WithdrawStatusEnum.VIA_AUDITING.value().equals(wdo.getStatus())) {
                throw new DistributionException(DistributionErrorCode.E1002.code(), "ID为"+wdo.getId()+"的提现申请审核未通过，不能设置已转账");
            }

            String applySql = "update es_withdraw_apply set status=?,transfer_time=?,transfer_remark=? where id=?";
            this.daoSupport.execute(applySql, WithdrawStatusEnum.TRANSFER_ACCOUNTS.name(), DateUtil.getDateline(), withdrawAuditPaidVO.getRemark(), applyId);
        }
    }

    @Override
    public BankParamsVO getWithdrawSetting(int memberId) {
        String sql = "select * from es_withdraw_setting where member_id=?";
        WithdrawSettingDO withdrawSetting = this.daoSupport.queryForObject(sql, WithdrawSettingDO.class,
                memberId);
        if (withdrawSetting == null) {
            return new BankParamsVO();
        }
        return JsonUtil.jsonToObject(withdrawSetting.getParam(), BankParamsVO.class);
    }

    @Override
    public Page<WithdrawApplyVO> pageApply(Integer pageNo, Integer pageSize, Map<String, String> map) {
        Page<WithdrawApplyDO> page;
        List paramList = new ArrayList();
        List<String> sqlList = new ArrayList<>();
        StringBuffer sql = new StringBuffer("select * from es_withdraw_apply ");

        if (!StringUtil.isEmpty(map.get("uname"))) {
            sqlList.add(" member_name like ? ");
            paramList.add("%" + map.get("uname") + "%");
        }
        if (!StringUtil.isEmpty(map.get("start_time"))) {
            sqlList.add(" apply_time > ? ");
            paramList.add(map.get("start_time"));
        }
        if (!StringUtil.isEmpty(map.get("end_time"))) {
            sqlList.add(" apply_time < ? ");
            paramList.add(map.get("end_time"));
        }
        if (!StringUtil.isEmpty(map.get("status"))) {
            sqlList.add(" status = ? ");
            paramList.add(map.get("status"));

        }

        sql.append(SqlUtil.sqlSplicing(sqlList));
        sql.append(" order by id desc");

        page = this.daoSupport.queryForPage(sql.toString(), pageNo, pageSize, WithdrawApplyDO.class, paramList.toArray());
        Page<WithdrawApplyVO> result = convertPage(page);
        return result;
    }

    @Override
    public List<WithdrawApplyDO> exportApply(Map<String, String> map) {
        List term = new ArrayList();
        List<String> sqlList = new ArrayList<>();
        StringBuffer sql = new StringBuffer("select * from es_withdraw_apply ");

        if (!StringUtil.isEmpty(map.get("uname"))) {
            sqlList.add(" member_name like ? ");
            term.add("%" + map.get("uname") + "%");
        }
        if (!StringUtil.isEmpty(map.get("start_time"))) {
            sqlList.add(" apply_time > ? ");
            term.add(map.get("start_time"));
        }
        if (!StringUtil.isEmpty(map.get("end_time"))) {
            sqlList.add(" apply_time < ? ");
            term.add(map.get("end_time"));
        }
        if (!StringUtil.isEmpty(map.get("status"))) {
            sqlList.add(" status = ? ");
            term.add(map.get("status"));

        }

        sql.append(SqlUtil.sqlSplicing(sqlList));
        sql.append(" order by apply_time desc");

        List<WithdrawApplyDO> applyList = this.daoSupport.queryForList(sql.toString(), WithdrawApplyDO.class, term.toArray());
        return applyList;
    }

    @Override
    public Double getRebate(Integer memberId) {
        Double rebate= this.daoSupport.queryForDouble("select can_rebate from es_distribution where member_id = ? ", memberId);
        return rebate<=0?0:rebate;
    }

    /**
     * 转换page
     * @param page
     * @return
     */
    private Page convertPage(Page<WithdrawApplyDO> page) {

        List<WithdrawApplyVO> vos = new ArrayList<>();
        for (WithdrawApplyDO withdrawApplyDO : page.getData()) {

            WithdrawApplyVO applyVO = new WithdrawApplyVO(withdrawApplyDO);
            BankParamsVO paramsVO = this.getWithdrawSetting(withdrawApplyDO.getMemberId());
            applyVO.setBankParamsVO(paramsVO);
            vos.add(applyVO);
        }
        Page result = new Page(page.getPageNo(),page.getDataTotal(),page.getPageSize(),vos);
        return result;
    }

    /**
     * 自动发送红包
     *
     * @param applyId
     */
    private void autoSend(Integer applyId) {
        WithdrawApplyDO withdrawApplyDO = this.getModel(applyId);
        ConnectDO connectDO = connectClient.getConnect(withdrawApplyDO.getMemberId(), ConnectTypeEnum.WECHAT_OPENID.value());
        if (connectDO == null) {
            return;
        }
        boolean success = wechatSmallchangeClient.autoSend(connectDO.getUnionId(), withdrawApplyDO.getApplyMoney(), withdrawApplyDO.getIp(), withdrawApplyDO.getSn());
        if (success) {
            String applySql = "update es_withdraw_apply set status=?,transfer_time=?,transfer_remark=? where id=?";
            this.daoSupport.execute(applySql, WithdrawStatusEnum.TRANSFER_ACCOUNTS.name(), DateUtil.getDateline(), "零钱转账", applyId);
        }
    }
}
