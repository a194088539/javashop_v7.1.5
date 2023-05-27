package com.enation.app.javashop.manager.api.distribution;

import com.enation.app.javashop.core.distribution.exception.DistributionErrorCode;
import com.enation.app.javashop.core.distribution.exception.DistributionException;
import com.enation.app.javashop.core.distribution.model.dos.WithdrawApplyDO;
import com.enation.app.javashop.core.distribution.model.vo.WithdrawApplyVO;
import com.enation.app.javashop.core.distribution.model.vo.WithdrawAuditPaidVO;
import com.enation.app.javashop.core.distribution.service.WithdrawManager;
import com.enation.app.javashop.framework.database.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分销提现控制器
 *
 * @author Chopper
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2018/5/24 下午5:11
 */
@Api(description = "分销提现控制器")
@RestController
@RequestMapping("/admin/distribution/withdraw")
public class WithdrawManagerController {
    @Autowired
    private WithdrawManager withdrawManager;

    protected final Log logger = LogFactory.getLog(this.getClass());

    @ApiOperation("提现申请审核列表")
    @GetMapping(value = "/apply")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page_size", value = "页码大小", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "page_no", value = "页码", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "uname", value = "会员名", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "end_time", value = "结束时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "start_time", value = "开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "status", value = "状态 全部的话，不要传递参数即可 APPLY:申请中/VIA_AUDITING:审核通过/FAIL_AUDITING:审核未通过/RANSFER_ACCOUNTS:已转账 ", paramType = "query", dataType = "String")
    })
    public Page<WithdrawApplyVO> pageApply(@ApiIgnore String endTime, @ApiIgnore String startTime, @ApiIgnore String uname, @ApiIgnore Integer pageNo, @ApiIgnore Integer pageSize, @ApiIgnore String status) {

        Map<String,String> map = new HashMap(4);
        map.put("start_time", startTime);
        map.put("end_time", endTime);
        map.put("uname", uname);
        map.put("status",status);
        return withdrawManager.pageApply(pageNo, pageSize, map);
    }

    @ApiOperation("导出提现申请")
    @GetMapping(value = "/export")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uname", value = "会员名", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "end_time", value = "结束时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "start_time", value = "开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "status", value = "状态 全部的话，不要传递参数即可 APPLY:申请中/VIA_AUDITING:审核通过/FAIL_AUDITING:审核未通过/RANSFER_ACCOUNTS:已转账 ", paramType = "query", dataType = "String")
    })
    public List<WithdrawApplyDO> exportApply(@ApiIgnore String endTime, @ApiIgnore String startTime, @ApiIgnore String uname, @ApiIgnore String status) {

        Map<String,String> map = new HashMap(4);
        map.put("start_time", startTime);
        map.put("end_time", endTime);
        map.put("uname", uname);
        map.put("status",status);
        return withdrawManager.exportApply(map);
    }

    @PostMapping(value = "/batch/auditing")
    @ApiOperation("批量审核提现申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "audit_result", value = "申请结果 VIA_AUDITING：审核通过/FAIL_AUDITING：审核拒绝", required = true, paramType = "query", dataType = "String", allowableValues = "VIA_AUDITING,FAIL_AUDITING")
    })
    public void batchAuditing(@Valid @RequestBody WithdrawAuditPaidVO withdrawAuditPaidVO, @ApiIgnore @NotEmpty(message = "请选择审核通过还是拒绝") String auditResult) {
        try {
            this.withdrawManager.batchAuditing(withdrawAuditPaidVO, auditResult);
        } catch (Exception e) {
            logger.error("提现审核异常：", e);
            throw new DistributionException(DistributionErrorCode.E1000.code(), DistributionErrorCode.E1000.des());
        }
    }

    @ApiOperation("批量设为已转账")
    @PostMapping(value = "/batch/account/paid")
    public void batchAccountPaid(@Valid @RequestBody WithdrawAuditPaidVO withdrawAuditPaidVO) {
        try {
            this.withdrawManager.batchAccountPaid(withdrawAuditPaidVO);
        } catch (Exception e) {
            logger.error("提现申请设置已转账出错：", e);
            throw new DistributionException(DistributionErrorCode.E1000.code(), DistributionErrorCode.E1000.des());
        }
    }
}
