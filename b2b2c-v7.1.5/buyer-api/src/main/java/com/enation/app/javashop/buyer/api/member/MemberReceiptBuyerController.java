package com.enation.app.javashop.buyer.api.member;

import com.enation.app.javashop.core.member.model.dos.MemberReceipt;
import com.enation.app.javashop.core.member.service.MemberReceiptManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 会员发票信息缓存相关API
 *
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-09-16
 */
@RestController
@RequestMapping("/members/receipt")
@Api(description = "会员发票信息缓存相关API")
public class MemberReceiptBuyerController {

    @Autowired
    private MemberReceiptManager memberReceiptManager;


    @ApiOperation(value = "查询当前会员发票缓存信息集合", response = MemberReceipt.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "发票类型", required = true, dataType = "String",
                    paramType = "path", allowableValues = "ELECTRO,VATORDINARY", example = "ELECTRO：电子普通发票，VATORDINARY：增值税普通发票")
    })
    @GetMapping("/{type}")
    public List<MemberReceipt> list(@PathVariable String type) {
        return this.memberReceiptManager.list(type);
    }


    @ApiOperation(value = "添加会员发票信息", response = MemberReceipt.class)
    @PostMapping
    public MemberReceipt add(@Valid MemberReceipt memberReceipt) {
        return this.memberReceiptManager.add(memberReceipt);
    }

    @ApiOperation(value = "修改会员发票信息", response = MemberReceipt.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "int", paramType = "path")
    })
    @PutMapping(value = "/{id}")
    public MemberReceipt edit(@Valid MemberReceipt memberReceipt, @PathVariable Integer id) {
        return this.memberReceiptManager.edit(memberReceipt, id);
    }

    @ApiOperation(value = "设置会员发票为默认", response = MemberReceipt.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "会员发票主键,发票抬头为个人时则设置此参数为0", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "type", value = "发票类型", required = true, dataType = "String",
                    paramType = "path", allowableValues = "ELECTRO,VATORDINARY", example = "ELECTRO：电子普通发票，VATORDINARY：增值税普通发票")
    })
    @PutMapping(value = "/{id}/{type}")
    public void setDefault(@PathVariable Integer id, @PathVariable String type) {
        this.memberReceiptManager.setDefaultReceipt(type, id);
    }


    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除会员发票信息缓存")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键ID", required = true, dataType = "int", paramType = "path")
    })
    public String delete(@PathVariable Integer id) {

        this.memberReceiptManager.delete(id);

        return "";
    }

}