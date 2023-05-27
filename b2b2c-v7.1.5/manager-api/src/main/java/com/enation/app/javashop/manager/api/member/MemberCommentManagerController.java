package com.enation.app.javashop.manager.api.member;

import com.enation.app.javashop.core.member.model.dto.CommentQueryParam;
import com.enation.app.javashop.core.member.model.vo.BatchAuditVO;
import com.enation.app.javashop.core.member.model.vo.CommentVO;
import com.enation.app.javashop.core.member.service.MemberCommentManager;
import com.enation.app.javashop.framework.database.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 评论控制器
 *
 * @author fk
 * @version v1.0
 * @since v7.0.0
 * 2018-05-03 10:19:14
 */
@RestController
@RequestMapping("/admin/members/comments")
@Api(description = "评论相关API")
public class MemberCommentManagerController {

    @Autowired
    private MemberCommentManager memberCommentManager;

    @ApiOperation(value = "查询评论列表", response = CommentVO.class)
    @GetMapping
    public Page list(@Valid CommentQueryParam param) {

        return this.memberCommentManager.list(param);
    }

    @ApiOperation(value = "批量审核商品评论")
    @PostMapping("/batch/audit")
    public String batchAuditComment(@Valid @RequestBody BatchAuditVO batchAuditVO) {

        this.memberCommentManager.batchAudit(batchAuditVO);

        return "";
    }

    @ApiOperation(value = "删除评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "comment_id", value = "评论id", required = true, dataType = "int", paramType = "path"),
    })
    @DeleteMapping(value = "/{comment_id}")
    public String deleteComment(@PathVariable(name = "comment_id") Integer commentId) {

        this.memberCommentManager.delete(commentId);

        return "";
    }

    @ApiOperation(value = "查询会员商品评论详请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "comment_id", value = "主键ID", required = true, dataType = "int", paramType = "path")
    })
    @GetMapping("/{comment_id}")
    public CommentVO get(@PathVariable("comment_id") Integer commentId) {
        return this.memberCommentManager.get(commentId);
    }

}
