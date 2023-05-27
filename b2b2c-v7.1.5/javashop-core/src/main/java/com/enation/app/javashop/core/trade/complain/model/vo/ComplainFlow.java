package com.enation.app.javashop.core.trade.complain.model.vo;

import com.enation.app.javashop.core.trade.complain.model.enums.ComplainStatusEnum;
import com.enation.app.javashop.core.trade.order.model.vo.MyList;
import com.enation.app.javashop.core.trade.order.model.vo.OrderFlowNode;

import java.util.List;

/**
 * 订单流程对象，用于定义订单流程图
 * @author fk
 * @version 1.0
 * @since 7.1.5
 * 2019-03-11
 */
public class ComplainFlow {

    /**
     * 取消流程
     */
    private static List<OrderFlowNode> CANCEL_FLOW;

    /**
     * 正常流程
     */
    private static List<OrderFlowNode> NORMAL_FLOW;

    static{
        initFlow();
    }

    /**
     * 初始化
     */
    private static void initFlow() {

        //取消流程
        CANCEL_FLOW = new MyList()
                .addComplain(ComplainStatusEnum.NEW)
                .addComplain(ComplainStatusEnum.CANCEL)
                .getList();

        /**
         * 正常流程
         */
        NORMAL_FLOW = new MyList()
                .addComplain(ComplainStatusEnum.NEW)
                .addComplain(ComplainStatusEnum.WAIT_APPEAL)
                .addComplain(ComplainStatusEnum.COMMUNICATION)
                .addComplain(ComplainStatusEnum.WAIT_ARBITRATION)
                .addComplain(ComplainStatusEnum.COMPLETE)
                .getList();
    }

    /**
     * 获取取消流程
     * @return
     */
    public static List<OrderFlowNode> getCancelFlow(){

        return CANCEL_FLOW;
    }


    /**
     * 获取正常流程
     * @return
     */
    public static List<OrderFlowNode> getNormalFlow(){

        return NORMAL_FLOW;
    }


}
