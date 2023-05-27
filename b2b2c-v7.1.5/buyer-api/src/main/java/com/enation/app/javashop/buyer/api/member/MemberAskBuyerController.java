package com.enation.app.javashop.buyer.api.member;

import com.enation.app.javashop.core.base.CharacterConstant;
import com.enation.app.javashop.core.member.model.dos.MemberAsk;
import com.enation.app.javashop.core.member.model.dto.AskQueryParam;
import com.enation.app.javashop.core.member.model.vo.MemberAskVO;
import com.enation.app.javashop.core.system.sensitiveutil.SensitiveFilter;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.exception.NoPermissionException;
import com.enation.app.javashop.framework.security.model.Buyer;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.enation.app.javashop.framework.database.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enation.app.javashop.core.member.service.MemberAskManager;

import java.util.List;

/**
 * 会员商品咨询API
 *
 * @author duanmingyu
 * @version v2.0
 * @since v7.1.5
 * 2019-09-17
 */
@RestController
@RequestMapping("/members/asks")
@Api(description = "会员商品咨询API")
@Validated
public class MemberAskBuyerController {

    @Autowired
    private MemberAskManager memberAskManager;


    @ApiOperation(value = "查询我的咨询列表", response = MemberAsk.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page_no", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "page_size", value = "每页显示数量", required = true, dataType = "int", paramType = "query")
    })
    @GetMapping
    public Page list(@ApiIgnore Integer pageNo, @ApiIgnore Integer pageSize) {

        AskQueryParam param = new AskQueryParam();
        param.setPageNo(pageNo);
        param.setPageSize(pageSize);
        Buyer member = UserContext.getBuyer();
        param.setMemberId(member.getUid());

        return this.memberAskManager.list(param);
    }


    @ApiOperation(value = "添加咨询", response = MemberAsk.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ask_content", value = "咨询内容", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "goods_id", value = "咨询商品id", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "anonymous", value = "是否匿名 YES:是，NO:否", required = true, dataType = "string", paramType = "query", allowableValues = "YES,NO")
    })
    @PostMapping
    public MemberAsk add(@NotEmpty(message = "请输入咨询内容")@ApiIgnore String askContent,
                         @NotNull(message = "咨询商品不能为空") @ApiIgnore Integer goodsId,
                         @NotNull(message = "请选择是否匿名提问") @ApiIgnore String anonymous) {

        //咨询内容敏感词过滤
        askContent = SensitiveFilter.filter(askContent, CharacterConstant.WILDCARD_STAR);

        MemberAsk memberAsk = this.memberAskManager.add(askContent, goodsId, anonymous);

        return memberAsk;
    }

    @ApiOperation(value = "查看会员商品咨询详情", response = MemberAskVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ask_id", value = "会员商品咨询id", required = true, dataType = "int", paramType = "path")
    })
    @GetMapping("/detail/{ask_id}")
    public MemberAskVO detail(@PathVariable("ask_id") Integer askId) {

        MemberAskVO memberAskVO = this.memberAskManager.getModelVO(askId);

        return memberAskVO;
    }

    @ApiOperation(value	= "查询某商品的咨询", response = MemberAskVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page_no", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "page_size", value = "每页显示数量", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name="goods_id", value="商品ID", required=true, dataType="int", paramType="path")
    })
    @GetMapping("/goods/{goods_id}")
    public Page listGoodsAsks(@ApiIgnore Integer pageNo, @ApiIgnore Integer pageSize, @PathVariable("goods_id") Integer goodsId)	{

        return this.memberAskManager.listGoodsAsks(pageNo, pageSize, goodsId);
    }

    @ApiOperation(value	= "查询与会员商品咨询相关的其它咨询", response = MemberAskVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name="ask_id", value="会员商品咨询ID", required=true, dataType="int", paramType="path"),
            @ApiImplicitParam(name="goods_id", value="商品ID", required=true, dataType="int", paramType="path")
    })
    @GetMapping("/relation/{ask_id}/{goods_id}")
    public List<MemberAsk> listRelationAsks(@PathVariable("ask_id") Integer askId, @PathVariable("goods_id") Integer goodsId)	{

        return this.memberAskManager.listRelationAsks(askId, goodsId);
    }

    @ApiOperation(value = "删除咨询", response = MemberAsk.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ask_id", value = "会员商品咨询id", dataType = "int", paramType = "path"),
    })
    @DeleteMapping("/{ask_id}")
    public String delete(@PathVariable("ask_id") Integer askId) {

        Buyer buyer = UserContext.getBuyer();

        MemberAsk memberAsk = this.memberAskManager.getModel(askId);

        if (memberAsk == null || memberAsk.getMemberId().intValue() != buyer.getUid().intValue()) {
            throw new NoPermissionException("您没有权限删除此条咨询信息!");
        }

        this.memberAskManager.delete(askId);

        return "";
    }

}