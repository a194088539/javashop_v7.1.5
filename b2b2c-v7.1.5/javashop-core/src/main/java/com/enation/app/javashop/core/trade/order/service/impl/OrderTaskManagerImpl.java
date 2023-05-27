package com.enation.app.javashop.core.trade.order.service.impl;

import com.enation.app.javashop.core.base.SettingGroup;
import com.enation.app.javashop.core.client.system.SettingClient;
import com.enation.app.javashop.core.client.trade.OrderClient;
import com.enation.app.javashop.core.member.service.MemberCommentManager;
import com.enation.app.javashop.core.system.model.vo.SiteSetting;
import com.enation.app.javashop.core.trade.cart.model.dos.OrderPermission;
import com.enation.app.javashop.core.trade.complain.model.enums.ComplainSkuStatusEnum;
import com.enation.app.javashop.core.trade.order.model.dos.OrderDO;
import com.enation.app.javashop.core.trade.order.model.enums.*;
import com.enation.app.javashop.core.trade.order.model.vo.*;
import com.enation.app.javashop.core.trade.order.service.OrderOperateManager;
import com.enation.app.javashop.core.trade.order.service.OrderTaskManager;
import com.enation.app.javashop.core.trade.sdk.model.OrderDetailDTO;
import com.enation.app.javashop.core.trade.sdk.model.OrderSkuDTO;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.JsonUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 订单任务
 *
 * @author Snow create in 2018/7/13
 * @version v2.0
 * @since v7.0.0
 */
@Service
public class OrderTaskManagerImpl implements OrderTaskManager {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    private SettingClient settingClient;

    @Autowired
    private OrderOperateManager orderOperateManager;

    @Autowired
    private MemberCommentManager memberCommentManager;

    @Autowired
    private OrderClient orderClient;


    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void cancelTask() {
        OrderSettingVO settingVO = this.getOrderSetting();
        int time = this.dayConversionSecond(settingVO.getCancelOrderDay());

        String sql = "select sn from es_order  where payment_type!=? and create_time+?<? and (order_status=? or order_status=? )";
        List<Map> list = daoSupport.queryForList(sql, PaymentTypeEnum.COD.value(), time,
                DateUtil.getDateline(), OrderStatusEnum.NEW.value(), OrderStatusEnum.CONFIRM.value());

        for (Map map : list) {
            CancelVO cancel = new CancelVO();
            cancel.setOrderSn(map.get("sn").toString());
            cancel.setReason("超时未付款");
            cancel.setOperator("系统检测");
            this.orderOperateManager.cancel(cancel, OrderPermission.client);
        }

    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void rogTask() {
        OrderSettingVO settingVO = this.getOrderSetting();
        //系统时间
        long unixTime = DateUtil.getDateline();
        int time = this.dayConversionSecond(settingVO.getRogOrderDay());

        String sql = "select sn from es_order where order_status = ? and ship_time+?<? ";
        List<Map> list = this.daoSupport.queryForList(sql, OrderStatusEnum.SHIPPED.value(), time, unixTime);
        for (Map map : list) {
            RogVO rog = new RogVO();
            rog.setOrderSn(map.get("sn").toString());
            rog.setOperator("系统检测");
            this.orderOperateManager.rog(rog, OrderPermission.client);
        }

    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void completeTask() {
        OrderSettingVO settingVO = this.getOrderSetting();
        //系统时间
        long unixTime = DateUtil.getDateline();
        int time = this.dayConversionSecond(settingVO.getCompleteOrderDay());

        //获取要取消的订单编号集合：订单状态不等于已完成 并且 ((收货时间加上自动完成设置的时间小于当前时间 并且 订单付款类型为在线支付 并且 订单收货状态为已收货)
        // 或者 (付款时间加上自动完成设置的时间小于当前时间 并且 订单付款类型为货到付款 并且 订单付款状态为已付款))
        StringBuffer sqlBuffer = new StringBuffer("select sn from es_order where order_status != ? and ((signing_time + ? < ? and payment_type = ? and ship_status = ?) " +
                "or (payment_time + ? < ? and payment_type = ? and pay_status = ?))");

        List<Object> term = new ArrayList<Object>();
        term.add(OrderStatusEnum.COMPLETE.value());
        term.add(time);
        term.add(unixTime);
        term.add(PaymentTypeEnum.ONLINE.value());
        term.add(ShipStatusEnum.SHIP_ROG.value());
        term.add(time);
        term.add(unixTime);
        term.add(PaymentTypeEnum.COD.value());
        term.add(PayStatusEnum.PAY_YES.value());

        List<Map> list = this.daoSupport.queryForList(sqlBuffer.toString(), term.toArray());

        for (Map map : list) {
            CompleteVO complete = new CompleteVO();
            complete.setOrderSn(map.get("sn").toString());
            complete.setOperator("系统检测");
            try {
                this.orderOperateManager.complete(complete, OrderPermission.client);
            } catch (Exception e) {
                logger.error("订单自动标记为完成出错：订单编号"+complete.getOrderSn(), e);
            }
        }
    }


    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void payTask() {
        OrderSettingVO settingVO = this.getOrderSetting();
        int time = this.dayConversionSecond(settingVO.getCompleteOrderPay());

        String sql = "select sn,order_price from es_order where signing_time+?<? and payment_type=? and  order_status=?";
        List<Map> list = daoSupport.queryForList(sql, time, DateUtil.getDateline(), PaymentTypeEnum.COD.value(), OrderStatusEnum.ROG.value());
        for (Map map : list) {
            this.orderOperateManager.payOrder(map.get("sn").toString(), StringUtil.toDouble(map.get("order_price"), false), "", OrderPermission.client);
        }

    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void serviceTask() {
        OrderSettingVO settingVO = this.getOrderSetting();
        //系统时间
        long unixTime = DateUtil.getDateline();
        int time = this.dayConversionSecond(settingVO.getServiceExpiredDay());

        //检测订单状态售后状态的同时也需要检测订单中ItemJson售后状态
        String sql = "select sn from es_order where (complete_time+?<? and order_status=? and service_status = ?) or ( service_status = ? and items_json like ? )";


        // 查询所有订单状态为已完成的订单并且是未申请售后的订单
        List<Map> list = this.daoSupport.queryForList(sql, time, unixTime, OrderStatusEnum.COMPLETE.value(), OrderServiceStatusEnum.NOT_APPLY.value(),OrderServiceStatusEnum.EXPIRED.value(),"%"+OrderServiceStatusEnum.NOT_APPLY.value()+"%");
        String sn = "";
        for (Map map : list) {
            sn = map.get("sn").toString();
            this.orderOperateManager.updateServiceStatus(sn, OrderServiceStatusEnum.EXPIRED);

            //修改订单项(itemJson)中售后状态 为已过期
            OrderDetailDTO order = this.orderClient.getModel(sn);
            //获取订单SKU信息
            List<OrderSkuDTO> orderSkuList = order.getOrderSkuList();

            orderSkuList.forEach(orderSkuDTO -> {
                orderSkuDTO.setServiceStatus(OrderServiceStatusEnum.EXPIRED.value());
            });

            this.orderOperateManager.updateItemJson(JsonUtil.objectToJson(orderSkuList), sn);
        }

    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void commentTask() {
        OrderSettingVO settingVO = this.getOrderSetting();
        int time = this.dayConversionSecond(settingVO.getCommentOrderDay());

        String sql = "select * from es_order o where o.ship_status = ? and o.comment_status =?  and ship_time +?<?";
        List<OrderDetailVO> detailList = this.daoSupport.queryForList(sql, OrderDetailVO.class,
                ShipStatusEnum.SHIP_ROG.value(), CommentStatusEnum.UNFINISHED.value(), time, DateUtil.getDateline());
        List<OrderDetailDTO> detailDTOList = new ArrayList<>();
        for (OrderDetailVO orderDetail : detailList) {
            this.orderOperateManager.updateCommentStatus(orderDetail.getSn(), CommentStatusEnum.FINISHED);

            OrderDetailDTO detailDTO = new OrderDetailDTO();
            BeanUtils.copyProperties(orderDetail, detailDTO);
            detailDTOList.add(detailDTO);
        }
        this.memberCommentManager.autoGoodComments(detailDTOList);

    }

    @Override
    public void complainTask() {
        OrderSettingVO settingVO = this.getOrderSetting();
        int time = this.dayConversionSecond(settingVO.getComplainExpiredDay());

        String sql = "select * from es_order o where complete_time+?<? and order_status=? ";
        long unixTime = DateUtil.getDateline();
        List<OrderDO> orderList = this.daoSupport.queryForList(sql,OrderDO.class,time, unixTime, OrderStatusEnum.COMPLETE.value());

        for(OrderDO order : orderList){

            List<OrderSkuVO> skuList = JsonUtil.jsonToList(order.getItemsJson(), OrderSkuVO.class);

            for(OrderSkuVO sku : skuList){
                this.orderOperateManager.updateOrderItemsComplainStatus(order.getSn(),sku.getSkuId(), null, ComplainSkuStatusEnum.EXPIRED);
            }

        }

    }


    /**
     * 读取订单设置
     *
     * @return
     */
    private OrderSettingVO getOrderSetting() {
        String settingVOJson = this.settingClient.get(SettingGroup.TRADE);

        OrderSettingVO settingVO = JsonUtil.jsonToObject(settingVOJson, OrderSettingVO.class);
        return settingVO;
    }

    /**
     * 将天数转换为相应的秒数
     * 如果是测试模式，默认为1秒
     *
     * @param day
     * @return
     */
    private Integer dayConversionSecond(int day) {
        Integer time = day * 24 * 60 * 60;
        String siteSettingJson = settingClient.get(SettingGroup.SITE);

        SiteSetting siteSetting = JsonUtil.jsonToObject(siteSettingJson, SiteSetting.class);
        if (siteSetting.getTestMode().intValue() == 1) {
            time = 1;
        }

        return time;
    }


}
