package com.enation.app.javashop.buyer.api.trade;

import com.enation.app.javashop.core.trade.complain.model.dos.OrderComplain;
import com.enation.app.javashop.core.trade.complain.model.dos.OrderComplainCommunication;
import com.enation.app.javashop.core.trade.complain.model.dto.ComplainDTO;
import com.enation.app.javashop.core.trade.complain.model.dto.ComplainQueryParam;
import com.enation.app.javashop.core.trade.complain.model.vo.OrderComplainVO;
import com.enation.app.javashop.core.trade.complain.service.OrderComplainCommunicationManager;
import com.enation.app.javashop.core.trade.complain.service.OrderComplainManager;
import com.enation.app.javashop.core.trade.order.model.vo.OrderFlowNode;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.security.model.Buyer;
import com.enation.app.javashop.framework.security.model.Seller;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.enation.app.javashop.framework.database.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 交易投诉表控制器
 *
 * @author fk
 * @version v2.0
 * @since v2.0
 * 2019-11-27 16:48:27
 */
@RestController
@RequestMapping("/trade/order-complains")
@Api(description = "交易投诉表相关API")
public class OrderComplainBuyerController {

    @Autowired
    private OrderComplainManager orderComplainManager;

    @Autowired
    private OrderComplainCommunicationManager orderComplainCommunicationManager;


    @ApiOperation(value = "查询交易投诉表列表", response = OrderComplain.class)
    @GetMapping
    public Page list(@ApiIgnore Integer pageNo, @ApiIgnore Integer pageSize, ComplainQueryParam param) {

        param.setPageNo(pageNo);
        param.setPageSize(pageSize);
        Buyer buyer = UserContext.getBuyer();

        param.setMemberId(buyer.getUid());

        return this.orderComplainManager.list(param);
    }


    @ApiOperation(value = "添加交易投诉", response = OrderComplain.class)
    @PostMapping
    public OrderComplain add(@Valid ComplainDTO complain) {

        return this.orderComplainManager.add(complain);

    }

    @PutMapping(value = "/{id}")
    @ApiOperation(value = "撤销交易投诉", response = OrderComplain.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "int", paramType = "path")
    })
    public OrderComplain cancel(@PathVariable Integer id) {

        return this.orderComplainManager.cancel(id);
    }

    @PutMapping(value = "/{id}/communication")
    @ApiOperation(value = "提交对话", response = OrderComplain.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "content", value = "对话内容", required = true, dataType = "string", paramType = "query"),
    })
    public OrderComplainCommunication communicate(@PathVariable Integer id, String content) {

        Buyer buyer = UserContext.getBuyer();
        OrderComplainCommunication communication = new OrderComplainCommunication(id, content, "买家", buyer.getUsername(), buyer.getUid());

        return this.orderComplainCommunicationManager.add(communication);
    }


    @GetMapping(value = "/{id}")
    @ApiOperation(value = "查询一个交易投诉")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "要查询的交易投诉表主键", required = true, dataType = "int", paramType = "path")
    })
    public OrderComplainVO get(@PathVariable Integer id) {

        OrderComplainVO orderComplain = this.orderComplainManager.getModelAndCommunication(id);

        return orderComplain;
    }

    @GetMapping(value = "/{id}/flow")
    @ApiOperation(value = "查询一个交易投诉的流程图")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "要查询的交易投诉表主键", required = true, dataType = "int", paramType = "path")
    })
    public List<OrderFlowNode> getFlow(@PathVariable Integer id) {

        return this.orderComplainManager.getComplainFlow(id);
    }

    @PutMapping(value = "/{id}/to-arbitration")
    @ApiOperation(value = "对话中提交仲裁，随时可以提交仲裁", response = OrderComplain.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "int", paramType = "path"),
    })
    public OrderComplain complete(@PathVariable Integer id) {

        return this.orderComplainManager.arbitrate(id);
    }

}