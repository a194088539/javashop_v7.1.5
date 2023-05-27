package com.enation.app.javashop.seller.api.promotion;

import com.enation.app.javashop.core.promotion.seckill.model.dos.SeckillApplyDO;
import com.enation.app.javashop.core.promotion.seckill.model.dos.SeckillDO;
import com.enation.app.javashop.core.promotion.seckill.model.dos.SeckillRangeDO;
import com.enation.app.javashop.core.promotion.seckill.model.dto.SeckillQueryParam;
import com.enation.app.javashop.core.promotion.seckill.model.enums.SeckillStatusEnum;
import com.enation.app.javashop.core.promotion.seckill.model.vo.SeckillVO;
import com.enation.app.javashop.core.promotion.seckill.service.SeckillGoodsManager;
import com.enation.app.javashop.core.promotion.seckill.service.SeckillManager;
import com.enation.app.javashop.core.promotion.seckill.service.SeckillRangeManager;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.exception.NoPermissionException;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.exception.SystemErrorCodeV1;
import com.enation.app.javashop.framework.security.model.Seller;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * 限时抢购申请控制器
 *
 * @author Snow
 * @version v7.0.0
 * @since v7.0.0
 * 2018-04-02 17:30:09
 */
@RestController
@RequestMapping("/seller/promotion/seckill-applys")
@Api(description = "限时抢购申请相关API")
@Validated
public class SeckillApplySellerController {

    @Autowired
    private SeckillGoodsManager seckillApplyManager;

    @Autowired
    private SeckillRangeManager seckillRangeManager;

    @Autowired
    private SeckillManager seckillManager;


    @ApiOperation(value = "查询限时抢购申请商品列表", response = SeckillApplyDO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name	= "seckill_id", value = "限时抢购活动id", dataType = "int",	paramType =	"query"),
            @ApiImplicitParam(name	= "status", value = "审核状态", dataType = "String",	paramType =	"query",
                    allowableValues = "APPLY,PASS,FAIL",example = "APPLY:待审核，PASS：通过审核，FAIL：未通过审核"),
            @ApiImplicitParam(name	= "goods_name", value = "商品名称", dataType = "String",	paramType =	"query"),
            @ApiImplicitParam(name	= "page_no", value = "取消原因", dataType = "int",	paramType =	"query"),
            @ApiImplicitParam(name	= "page_size", value = "取消原因", dataType = "int",	paramType =	"query")
    })
    @GetMapping
    public Page<SeckillVO> list(@ApiIgnore @NotNull(message = "限时抢购活动参数为空") Integer seckillId, @ApiIgnore String status,
                                @ApiIgnore String goodsName, @ApiIgnore Integer pageNo, @ApiIgnore Integer pageSize) {

        SeckillQueryParam queryParam = new SeckillQueryParam();
        queryParam.setPageNo(pageNo);
        queryParam.setPageSize(pageSize);
        queryParam.setSeckillId(seckillId);
        queryParam.setStatus(status);
        queryParam.setGoodsName(goodsName);
        queryParam.setSellerId(UserContext.getSeller().getSellerId());
        return this.seckillApplyManager.list(queryParam);
    }


    @ApiOperation(value = "添加限时抢购申请", response = SeckillApplyDO.class)
    @PostMapping
    public List<SeckillApplyDO> add(@Valid @RequestBody @NotEmpty(message = "申请参数为空") List<SeckillApplyDO> seckillApplyList) {

        SeckillApplyDO applyDO = seckillApplyList.get(0);
        this.verifyParam(seckillApplyList, applyDO.getSeckillId());
        this.seckillApplyManager.addApply(seckillApplyList);

        return seckillApplyList;
    }


    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除限时抢购申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "要删除的限时抢购申请主键", required = true, dataType = "int", paramType = "path")
    })
    public String delete(@PathVariable Integer id) {

        this.seckillApplyManager.delete(id);

        return "";
    }


    @GetMapping(value = "/{id}")
    @ApiOperation(value = "查询一个限时抢购申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "要查询的限时抢购申请主键", required = true, dataType = "int", paramType = "path")
    })
    public SeckillApplyDO get(@PathVariable Integer id) {
        SeckillApplyDO seckillApply = this.seckillApplyManager.getModel(id);

        Seller seller = UserContext.getSeller();
        //验证越权操作
        if (seckillApply == null || seller.getSellerId().intValue() != seckillApply.getSellerId().intValue()) {
            throw new NoPermissionException("无权操作");
        }

        return seckillApply;
    }


    @ApiOperation(value = "查询所有的限时抢购活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page_no", value = "页码", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "page_size", value = "每页显示数量", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "seckill_name", value = "活动名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name	= "status",	value =	"状态", dataType = "String",	paramType =	"query"),
            @ApiImplicitParam(name	= "start_time",	value =	"限时抢购日期-开始日期", dataType = "int",	paramType =	"query"),
            @ApiImplicitParam(name	= "end_time",	value =	"限时抢购日期-结束日期", dataType = "int",	paramType =	"query")
    })
    @GetMapping(value = "/seckill")
    public Page list(@ApiIgnore Integer pageNo, @ApiIgnore Integer pageSize, @ApiIgnore String seckillName,
                     @ApiIgnore String status, @ApiIgnore Long startTime, @ApiIgnore Long endTime) {

        SeckillQueryParam param = new SeckillQueryParam();
        param.setPageNo(pageNo);
        param.setPageSize(pageSize);
        param.setSeckillName(seckillName);
        param.setStatus(status);
        param.setStartTime(startTime);
        param.setEndTime(endTime);
        Page page = this.seckillManager.list(param);

        List<SeckillDO> list = page.getData();
        Seller seller = UserContext.getSeller();

        List<SeckillVO> seckillVOList = new ArrayList<>();
        for (SeckillDO seckillDO : list) {

            //处于编辑中的显示抢购活动，不在商家显示  update -by liuyulei 2019-05-23
            if(SeckillStatusEnum.EDITING.name().equals(seckillDO.getSeckillStatus())){
                continue;
            }

            String shopIds = seckillDO.getSellerIds();

            String[] shopId = new String[0];

            if (!StringUtil.isEmpty(shopIds)) {
                shopId = shopIds.split(",");
            }

            SeckillVO seckillVO = new SeckillVO();
            BeanUtils.copyProperties(seckillDO, seckillVO);

            //如果已超过报名时间，已截止
            long nowTime = DateUtil.getDateline();
            long applyEndTime = seckillDO.getApplyEndTime();
            if (nowTime > applyEndTime) {
                seckillVO.setIsApply(2);

                //如果申请过，已报名
            } else if (Arrays.asList(shopId).contains(seller.getSellerId() + "")) {
                seckillVO.setIsApply(1);

                //未报名
            } else {
                seckillVO.setIsApply(0);
            }
            seckillVOList.add(seckillVO);
        }
        page.setData(seckillVOList);
        return page;
    }


    @GetMapping(value = "/{seckill_id}/seckill")
    @ApiOperation(value = "查询一个限时抢购活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "seckill_id", value = "要查询的限时抢购活动主键", required = true, dataType = "int", paramType = "path")
    })
    public SeckillVO getSeckill(@ApiIgnore @PathVariable("seckill_id") Integer seckillId) {
        SeckillVO seckill = this.seckillManager.getModel(seckillId);
        return seckill;
    }


    /**
     * 验证参数的正确性
     *
     * @param applyDOList
     * @param seckillId   限时抢购活动id
     */
    private void verifyParam(List<SeckillApplyDO> applyDOList, Integer seckillId) {


        //根据限时抢购活动id 读取所有的时刻集合
        List<SeckillRangeDO> list = this.seckillRangeManager.getList(seckillId);
        List<Integer> rangIdList = new ArrayList<>();

        for (SeckillRangeDO seckillRangeDO : list) {
            rangIdList.add(seckillRangeDO.getRangeTime());
        }

        /**
         * 存储参加活动的商品id，用来判断同一个商品不能重复参加某个活动
         */
        Map<Integer, Integer> map = new HashMap<>();

        for (SeckillApplyDO applyDO : applyDOList) {

            Seller seller = UserContext.getSeller();
            applyDO.setSellerId(seller.getSellerId());
            applyDO.setShopName(seller.getSellerName());

            if (applyDO.getSeckillId() == null || applyDO.getSeckillId().intValue() == 0) {
                throw new ServiceException(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER, "限时抢购活动ID参数异常");
            } else {
                SeckillVO seckillVO = this.seckillManager.getModel(seckillId);

                //活动申请最后时间
                long applyEndTime = seckillVO.getApplyEndTime();

                //服务器当前时间
                long nowTime = DateUtil.getDateline();

                //当前时间大于活动最后申请时间，不能申请
                if (nowTime > applyEndTime) {
                    throw new ServiceException(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER, "已超过活动最后申请时间");
                }

            }

            if (applyDO.getTimeLine() == null) {
                throw new ServiceException(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER, "时刻参数异常");
            } else {

                //判断此活动的时刻集合是否包含正要添加的时刻,如果不包含说明时刻参数有异常
                if (!rangIdList.contains(applyDO.getTimeLine())) {
                    throw new ServiceException(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER, "时刻参数异常");
                }
            }

            if (applyDO.getStartDay() == null || applyDO.getStartDay().intValue() == 0) {
                throw new ServiceException(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER, "活动开始时间参数异常");
            }

            if (applyDO.getGoodsId() == null || applyDO.getGoodsId().intValue() == 0) {
                throw new ServiceException(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER, "商品ID参数异常");
            }

            if (StringUtil.isEmpty(applyDO.getGoodsName())) {
                throw new ServiceException(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER, "商品名称参数异常");
            }

            if (applyDO.getPrice() == null || applyDO.getPrice() < 0) {
                throw new ServiceException(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER, "抢购价参数不能小于0");
            }

            if (applyDO.getSoldQuantity() == null || applyDO.getSoldQuantity().intValue() <= 0) {
                throw new ServiceException(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER, "售空数量参数不能小于0");
            }

            if (applyDO.getPrice() > applyDO.getOriginalPrice()) {
                throw new ServiceException(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER, "活动价格不能大于商品原价");
            }

            if (map.get(applyDO.getGoodsId()) != null) {
                throw new ServiceException(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER, applyDO.getGoodsName() + ",该商品不能同时参加多个时间段的活动");
            }

            map.put(applyDO.getGoodsId(), applyDO.getGoodsId());

        }
    }


}
