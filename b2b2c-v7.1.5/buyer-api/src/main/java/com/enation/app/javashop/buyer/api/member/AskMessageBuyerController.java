package com.enation.app.javashop.buyer.api.member;

import com.enation.app.javashop.core.member.model.dos.AskMessageDO;
import com.enation.app.javashop.core.member.service.AskMessageManager;
import com.enation.app.javashop.framework.database.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 会员商品咨询消息API
 *
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-09-18
 */
@RestController
@RequestMapping("/members/asks/message")
@Api(description = "会员商品咨询消息API")
@Validated
public class AskMessageBuyerController {

    @Autowired
    private AskMessageManager askMessageManager;

    @ApiOperation(value = "查询会员商品咨询回复消息列表", response = AskMessageDO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page_no", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "page_size", value = "每页显示数量", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "is_read", value = "是否已读 YES：是 NO：否", dataType = "String", paramType = "query", allowableValues = "YES,NO")
    })
    @GetMapping
    public Page list(@ApiIgnore Integer pageNo, @ApiIgnore Integer pageSize, @ApiIgnore String isRead) {
        return this.askMessageManager.list(pageNo, pageSize, isRead);
    }

    @PutMapping(value = "/{ids}/read")
    @ApiOperation(value = "将会员商品咨询消息设置为已读", response = AskMessageDO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "要设置为已读消息的id", required = true, dataType = "int", paramType = "path", allowMultiple = true)
    })
    public String setRead(@PathVariable Integer[] ids) {
        this.askMessageManager.read(ids);
        return null;
    }

    @ApiOperation(value = "删除会员商品咨询消息", response = AskMessageDO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "会员商品咨询消息id", required = true, dataType = "int", paramType = "path", allowMultiple = true),
    })
    @DeleteMapping("/{ids}")
    public String delete(@PathVariable("ids") Integer[] ids) {

        this.askMessageManager.delete(ids);

        return "";
    }
}
