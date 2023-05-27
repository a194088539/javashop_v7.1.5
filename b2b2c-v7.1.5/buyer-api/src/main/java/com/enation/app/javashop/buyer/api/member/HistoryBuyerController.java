package com.enation.app.javashop.buyer.api.member;

import com.enation.app.javashop.core.member.model.dos.HistoryDO;
import com.enation.app.javashop.core.member.model.dto.HistoryDelDTO;
import com.enation.app.javashop.core.member.service.HistoryManager;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.security.model.Buyer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 会员足迹控制器
 *
 * @author zh
 * @version v7.1.4
 * @since vv7.1
 * 2019-06-18 15:18:56
 */
@RestController
@RequestMapping("/members/history")
@Api(description = "会员足迹相关API")
public class HistoryBuyerController {

    @Autowired
    private HistoryManager historyManager;


    @ApiOperation(value = "查询会员足迹列表", response = HistoryDO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page_no", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "page_size", value = "每页显示数量", required = true, dataType = "int", paramType = "query")
    })
    @GetMapping("list")
    public Page list(@ApiIgnore Integer pageNo, @ApiIgnore Integer pageSize) {
        return this.historyManager.list(pageNo, pageSize);
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "根据足迹id删除会员足迹")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "要删除的会员足迹主键", required = true, dataType = "int", paramType = "path")
    })
    public String delById(@PathVariable Integer id) {
        HistoryDelDTO historyDelDTO = new HistoryDelDTO();
        historyDelDTO.setId(id);
        Buyer buyer = UserContext.getBuyer();
        historyDelDTO.setMemberId(buyer.getUid());
        historyManager.delete(historyDelDTO);

        return "";
    }


    @DeleteMapping(value = "/day/{day}")
    @ApiOperation(value = "根据日期清空会员足迹")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "day", value = "当天的时间戳", required = true, dataType = "String", paramType = "path")
    })
    public String delByDate(@PathVariable String day) {
        HistoryDelDTO historyDelDTO = new HistoryDelDTO();
        historyDelDTO.setDate(day);
        Buyer buyer = UserContext.getBuyer();
        historyDelDTO.setMemberId(buyer.getUid());

        historyManager.delete(historyDelDTO);
        return "";
    }

    @DeleteMapping
    @ApiOperation(value = "清空所有会员足迹")
    public String delAll() {
        HistoryDelDTO historyDelDTO = new HistoryDelDTO();
        Buyer buyer = UserContext.getBuyer();
        historyDelDTO.setMemberId(buyer.getUid());
        this.historyManager.delete(historyDelDTO);
        return "";
    }


}