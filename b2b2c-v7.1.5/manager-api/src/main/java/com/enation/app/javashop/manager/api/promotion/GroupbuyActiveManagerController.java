package com.enation.app.javashop.manager.api.promotion;

import com.enation.app.javashop.core.promotion.groupbuy.model.dos.GroupbuyActiveDO;
import com.enation.app.javashop.core.promotion.groupbuy.model.vo.GroupbuyActiveVO;
import com.enation.app.javashop.core.promotion.groupbuy.model.vo.GroupbuyAuditParam;
import com.enation.app.javashop.core.promotion.groupbuy.model.vo.GroupbuyQueryParam;
import com.enation.app.javashop.core.promotion.groupbuy.service.GroupbuyActiveManager;
import com.enation.app.javashop.framework.context.AdminUserContext;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.exception.SystemErrorCodeV1;
import com.enation.app.javashop.framework.util.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 团购活动表控制器
 * @author Snow
 * @version v7.0.0
 * @since v7.0.0
 * 2018-04-02 15:46:51
 */
@RestController
@RequestMapping("/admin/promotion/group-buy-actives")
@Api(description = "团购活动表相关API")
@Validated
public class GroupbuyActiveManagerController	{

	@Autowired
	private	GroupbuyActiveManager groupbuyActiveManager;

	@ApiOperation(value	= "查询团购活动表列表")
	@ApiImplicitParams({
		 	@ApiImplicitParam(name	= "page_no",	value =	"页码",	dataType = "int",	paramType =	"query"),
		 	@ApiImplicitParam(name	= "page_size",	value =	"每页显示数量", dataType = "int",	paramType =	"query"),
			@ApiImplicitParam(name	= "act_name",	value =	"活动名称", dataType = "String",	paramType =	"query"),
			@ApiImplicitParam(name	= "act_status",	value =	"活动状态 NOT_STARTED:未开始,STARTED:进行中,OVER:已结束", dataType = "String",	paramType =	"query",allowableValues = "NOT_STARTED,STARTED,OVER"),
			@ApiImplicitParam(name	= "start_time", value = "开始时间", dataType = "long",paramType =	"query"),
			@ApiImplicitParam(name	= "end_time", value = "结束时间", dataType = "long",paramType =	"query")
	})
	@GetMapping
	public Page<GroupbuyActiveVO> list(@ApiIgnore  Integer pageNo, @ApiIgnore Integer pageSize, @ApiIgnore String actName,
									   @ApiIgnore String actStatus,  @ApiIgnore Long startTime, @ApiIgnore Long endTime)	{
		GroupbuyQueryParam param = new GroupbuyQueryParam();
		param.setPage(pageNo);
		param.setPageSize(pageSize);
		param.setActName(actName);
		param.setActStatus(actStatus);
		param.setStartTime(startTime);
		param.setEndTime(endTime);

		Page<GroupbuyActiveVO> page = this.groupbuyActiveManager.list(param);
		return page;

	}


	@ApiOperation(value	= "添加团购活动表", response = GroupbuyActiveDO.class)
	@ApiImplicitParam(name = "activeDO", value = "团购信息", required = true, dataType = "GroupbuyActiveDO", paramType = "body")
	@PostMapping
	public GroupbuyActiveDO add(@ApiIgnore @Valid  @RequestBody GroupbuyActiveDO activeDO)	{

		this.verifyParam(activeDO.getStartTime(),activeDO.getEndTime(),activeDO.getJoinEndTime());
		this.groupbuyActiveManager.add(activeDO);
		return	activeDO;
	}


	@ApiOperation(value	= "修改团购活动表", response = GroupbuyActiveDO.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name	= "id",	value =	"主键",	required = true, dataType = "int",	paramType =	"path")
	})
	@PutMapping(value = "/{id}")
	public	GroupbuyActiveDO edit(@Valid @RequestBody GroupbuyActiveDO activeDO, @PathVariable Integer id) {
		this.verifyParam(activeDO.getStartTime(),activeDO.getEndTime(),activeDO.getJoinEndTime());
		this.groupbuyActiveManager.edit(activeDO,id);
		return	activeDO;
	}


	@PostMapping(value = "/{id}/delete")
	@ApiOperation(value	= "删除团购活动")
	@ApiImplicitParams({
			@ApiImplicitParam(name	= "id",	value =	"要删除的团购活动主键",	required = true, dataType = "int",	paramType =	"path"),
			@ApiImplicitParam(name	= "delete_reason",	value =	"删除原因",	required = true, dataType = "String",	paramType =	"query")
	})
	public String delete(@PathVariable Integer id, @ApiIgnore String deleteReason) {
		this.groupbuyActiveManager.delete(id, deleteReason, AdminUserContext.getAdmin().getUsername());
		return "";
	}


	@GetMapping(value =	"/{id}")
	@ApiOperation(value	= "查询一个团购活动表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id",	value = "要查询的团购活动表主键",	required = true, dataType = "int",	paramType = "path")
	})
	public	GroupbuyActiveDO get(@PathVariable	Integer	id)	{

		GroupbuyActiveDO groupbuyActive = this.groupbuyActiveManager.getModel(id);

		return	groupbuyActive;
	}


	@ApiOperation(value	= "批量审核商品")
	@PostMapping(value = "/batch/audit")
	public void batchAudit(@Valid @RequestBody GroupbuyAuditParam param){

		this.groupbuyActiveManager.batchAuditGoods(param);
	}

	/**
	 * 验证参数
	 * @param startTime	活动开始时间
	 * @param endTime	活动结束时间
	 * @param joinEndTime	报名截止时间
	 */
	private void verifyParam(long startTime,long endTime,long joinEndTime){

		long nowTime  = DateUtil.getDateline();

		//如果活动起始时间小于现在时间
		if(joinEndTime < nowTime){
			throw new ServiceException(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER,"报名截止时间必须大于当前时间");
		}

		//如果活动开始时间小于 报名截止时间
		if (startTime < joinEndTime) {
			throw new ServiceException(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER, "活动开始时间必须大于报名截止时间");
		}

		// 开始时间不能大于结束时间
		if (startTime > endTime ) {
			throw new ServiceException(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER,"活动起始时间不能大于活动结束时间");
		}

	}

}
