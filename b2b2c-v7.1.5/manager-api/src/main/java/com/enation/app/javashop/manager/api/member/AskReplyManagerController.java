package com.enation.app.javashop.manager.api.member;

import com.enation.app.javashop.core.member.model.dos.AskReplyDO;
import com.enation.app.javashop.core.member.model.dos.MemberAsk;
import com.enation.app.javashop.core.member.model.dto.ReplyQueryParam;
import com.enation.app.javashop.core.member.model.enums.CommonStatusEnum;
import com.enation.app.javashop.core.member.model.vo.BatchAuditVO;
import com.enation.app.javashop.core.member.service.AskReplyManager;
import com.enation.app.javashop.framework.database.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 会员商品咨询回复API
 *
 * @author duanmingyu
 * @version v2.0
 * @since v7.1.5
 * 2019-09-17
 */
@RestController
@RequestMapping("/admin/members/reply")
@Api(description = "会员商品咨询回复API")
@Validated
public class AskReplyManagerController {

    @Autowired
    private AskReplyManager askReplyManager;

    @ApiOperation(value = "查询会员商品咨询回复列表", response = AskReplyDO.class)
    @GetMapping
    public Page list(@Valid ReplyQueryParam param) {

        param.setReplyStatus(CommonStatusEnum.YES.value());

        return this.askReplyManager.list(param);
    }

    @ApiOperation(value = "批量审核会员商品咨询回复")
    @PostMapping("/batch/audit")
    public String batchAuditReply(@Valid @RequestBody BatchAuditVO batchAuditVO) {

        this.askReplyManager.batchAudit(batchAuditVO);

        return "";
    }

    @ApiOperation(value = "删除会员商品咨询回复", response = MemberAsk.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "会员商品咨询回复主键id", dataType = "int", paramType = "path"),
    })
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Integer id) {

        this.askReplyManager.delete(id);

        return "";
    }
}
