package com.enation.app.javashop.seller.api.member;

import com.enation.app.javashop.core.member.model.dos.MemberAsk;
import com.enation.app.javashop.core.member.model.dto.AskQueryParam;
import com.enation.app.javashop.core.member.model.enums.AuditEnum;
import com.enation.app.javashop.core.member.service.MemberAskManager;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

/**
 * 咨询控制器
 *
 * @author fk
 * @version v1.0
 * @since v7.0.0
 * 2018-05-04 17:41:18
 */
@RestController
@RequestMapping("/seller/members/asks")
@Api(description = "咨询相关API")
@Validated
public class MemberAskSellerController {

    @Autowired
    private MemberAskManager memberAskManager;


    @ApiOperation(value = "查询咨询列表", response = MemberAsk.class)
    @GetMapping
    public Page list(@Valid AskQueryParam param) {

        param.setSellerId(UserContext.getSeller().getSellerId());
        param.setAuthStatus(AuditEnum.PASS_AUDIT.value());

        return this.memberAskManager.list(param);
    }


    @ApiOperation(value = "商家回复会员商品咨询", response = MemberAsk.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "reply_content", value = "回复内容", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "ask_id", value = "咨询id", required = true, dataType = "int", paramType = "path")
    })
    @PutMapping("/{ask_id}/reply")
    public MemberAsk reply(@ApiIgnore @NotEmpty(message = "请输入回复内容") String replyContent, @ApiIgnore @PathVariable("ask_id") Integer askId) {

        MemberAsk memberAsk = this.memberAskManager.reply(replyContent, askId);

        return memberAsk;
    }

    @ApiOperation(value = "查询会员商品咨询详请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ask_id", value = "主键ID", required = true, dataType = "int", paramType = "path")
    })
    @GetMapping("/{ask_id}")
    public MemberAsk get(@PathVariable("ask_id") Integer askId) {
        return this.memberAskManager.getModel(askId);
    }
}
