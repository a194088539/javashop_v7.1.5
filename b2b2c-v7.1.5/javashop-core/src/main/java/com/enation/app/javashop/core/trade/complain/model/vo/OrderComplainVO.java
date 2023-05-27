package com.enation.app.javashop.core.trade.complain.model.vo;

import com.enation.app.javashop.core.trade.complain.model.dos.OrderComplain;
import com.enation.app.javashop.core.trade.complain.model.dos.OrderComplainCommunication;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 交易投诉表实体
 *
 * @author fk
 * @version v2.0
 * @since v2.0
 * 2019-11-27 16:48:27
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OrderComplainVO extends OrderComplain {

    /**
     * 对话信息
     */
    @ApiModelProperty(name = "communication_list", value = "对话信息")
    private List<OrderComplainCommunication> communicationList;


    public List<OrderComplainCommunication> getCommunicationList() {
        return communicationList;
    }

    public void setCommunicationList(List<OrderComplainCommunication> communicationList) {
        this.communicationList = communicationList;
    }
}
