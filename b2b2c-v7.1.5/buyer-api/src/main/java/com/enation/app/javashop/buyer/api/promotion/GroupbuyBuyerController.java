package com.enation.app.javashop.buyer.api.promotion;

import com.enation.app.javashop.core.promotion.groupbuy.model.dos.GroupbuyActiveDO;
import com.enation.app.javashop.core.promotion.groupbuy.model.dos.GroupbuyCatDO;
import com.enation.app.javashop.core.promotion.groupbuy.model.vo.GroupbuyGoodsVO;
import com.enation.app.javashop.core.promotion.groupbuy.model.vo.GroupbuyQueryParam;
import com.enation.app.javashop.core.promotion.groupbuy.service.GroupbuyActiveManager;
import com.enation.app.javashop.core.promotion.groupbuy.service.GroupbuyCatManager;
import com.enation.app.javashop.core.promotion.groupbuy.service.GroupbuyGoodsManager;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.util.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;


/**
 * 团购相关API
 * @author Snow create in 2018/5/28
 * @version v2.0
 * @since v7.0.0
 */
@RestController
@RequestMapping("/promotions/group-buy")
@Api(description = "团购相关API")
public class GroupbuyBuyerController {

    @Autowired
    private GroupbuyGoodsManager groupbuyGoodsManager;

    @Autowired
    private GroupbuyCatManager groupbuyCatManager;

    @Autowired
    private GroupbuyActiveManager groupbuyActiveManager;


    @ApiOperation(value = "查询团购分类的所有标签")
    @GetMapping("/cats")
    public List<GroupbuyCatDO> getCat(){
        List<GroupbuyCatDO> groupbuyCatDOList = this.groupbuyCatManager.getList(0);
        return groupbuyCatDOList;
    }


    @ApiOperation(value = "查询团购活动的信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "active_id", value = "团购活动主键", required = false, dataType = "int", paramType = "query")
    })
    @GetMapping("/active")
    public GroupbuyActiveDO getActive(@ApiIgnore Integer activeId){
        GroupbuyActiveDO activeDO = this.groupbuyActiveManager.getModel(activeId);
        return activeDO;
    }


    @ApiOperation(value = "查询团购商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name	= "cat_id", value = "团购分类id",dataType = "int",paramType =	"query"),
            @ApiImplicitParam(name	= "page_no", value = "页码", dataType = "int",	paramType =	"query"),
            @ApiImplicitParam(name	= "page_size", value = "条数", dataType = "int",	paramType =	"query")
    })
    @GetMapping("/goods")
    public Page<GroupbuyGoodsVO> list(@ApiIgnore Integer catId, @ApiIgnore Integer pageNo, @ApiIgnore Integer pageSize) {
        GroupbuyQueryParam param = new GroupbuyQueryParam();
        param.setCatId(catId);
        param.setPage(pageNo);
        param.setPageSize(pageSize);
        param.setMemberId(-1);

        param.setStartTime(DateUtil.getDateline());
        param.setEndTime(DateUtil.getDateline());

        Page webPage = this.groupbuyGoodsManager.listPageByBuyer(param);
        return webPage;
    }


}
