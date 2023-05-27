package com.enation.app.javashop.core.promotion.pintuan.service.impl;

import com.enation.app.javashop.core.aftersale.service.AfterSaleManager;
import com.enation.app.javashop.core.base.rabbitmq.AmqpExchange;
import com.enation.app.javashop.core.client.member.MemberClient;
import com.enation.app.javashop.core.member.model.dos.Member;
import com.enation.app.javashop.core.promotion.pintuan.model.*;
import com.enation.app.javashop.core.promotion.pintuan.service.PinTuanSearchManager;
import com.enation.app.javashop.core.promotion.pintuan.service.PintuanGoodsManager;
import com.enation.app.javashop.core.promotion.pintuan.service.PintuanManager;
import com.enation.app.javashop.core.promotion.pintuan.service.PintuanOrderManager;
import com.enation.app.javashop.core.trade.order.model.dos.OrderDO;
import com.enation.app.javashop.core.trade.order.model.dto.OrderDTO;
import com.enation.app.javashop.core.trade.order.model.dto.PersonalizedData;
import com.enation.app.javashop.core.trade.order.model.enums.OrderDataKey;
import com.enation.app.javashop.core.trade.order.model.enums.OrderStatusEnum;
import com.enation.app.javashop.core.trade.order.model.enums.PayStatusEnum;
import com.enation.app.javashop.core.trade.order.model.vo.OrderSkuVO;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ResourceNotFoundException;
import com.enation.app.javashop.framework.trigger.Interface.TimeTrigger;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by kingapex on 2019-01-24.
 * 拼团订单业务实现类<br/>
 * 实现了拼团订单开团和参团 *
 *
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2019-01-24
 */

@Service
public class PintuanOrderManagerImpl implements PintuanOrderManager {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport tradeDaoSupport;


    @Autowired
    private PintuanGoodsManager pintuanGoodsManager;

    @Autowired
    private MemberClient memberClient;

    @Autowired
    private PintuanManager pintuanManager;

    @Autowired
    private TimeTrigger timeTrigger;

    @Autowired
    private PinTuanSearchManager pinTuanSearchManager;

    @Autowired
    private AfterSaleManager afterSaleManager;

    @Autowired
    private AmqpTemplate amqpTemplate;


    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public PintuanOrder createOrder(OrderDTO order, Integer skuId, Integer pinTuanOrderId) {

        PintuanOrder pintuanOrder;
        PinTuanGoodsVO pinTuanGoodsVO = pintuanGoodsManager.getDetail(skuId,null);

        //拼团订单不为空，表示要参团
        if (pinTuanOrderId != null) {
            pintuanOrder = getMainOrderById(pinTuanOrderId);
            if (pintuanOrder == null) {
                if (logger.isErrorEnabled()) {
                    logger.error("试图参加拼团，但拼团订单[" + pinTuanOrderId + "]不存在");
                }
                throw new ResourceNotFoundException("拼团订单[" + pinTuanOrderId + "]不存在");
            }


            if (logger.isDebugEnabled()) {
                logger.debug("参加拼团订单：");
                logger.debug(pintuanOrder);
            }
        } else {

            //创建拼团
            pintuanOrder = new PintuanOrder();
            pintuanOrder.setEndTime(pinTuanGoodsVO.getEndTime());
            pintuanOrder.setOfferedNum(0);
            pintuanOrder.setPintuanId(pinTuanGoodsVO.getPintuanId());
            pintuanOrder.setRequiredNum(pinTuanGoodsVO.getRequiredNum());
            pintuanOrder.setSkuId(skuId);
            pintuanOrder.setGoodsId(pinTuanGoodsVO.getGoodsId());
            pintuanOrder.setThumbnail(pinTuanGoodsVO.getThumbnail());
            pintuanOrder.setOrderStatus(PintuanOrderStatus.new_order.name());
            pintuanOrder.setGoodsName(pinTuanGoodsVO.getGoodsName());

            //新增一个拼团订单
            this.tradeDaoSupport.insert(pintuanOrder);
            pinTuanOrderId = this.tradeDaoSupport.getLastId("es_pintuan_order");
            pintuanOrder.setOrderId(pinTuanOrderId);

            if (logger.isDebugEnabled()) {
                logger.debug("创建一个新的拼团订单：");
                logger.debug(pintuanOrder);
            }
        }

        //创建子订单
        PintuanChildOrder childOrder = new PintuanChildOrder();
        childOrder.setSkuId(skuId);
        childOrder.setOrderStatus(PintuanOrderStatus.wait.name());

        //拼团活动id
        childOrder.setPintuanId(pinTuanGoodsVO.getPintuanId());
        childOrder.setOrderSn(order.getSn());

        //拼团订单id
        childOrder.setOrderId(pintuanOrder.getOrderId());
        childOrder.setMemberId(order.getMemberId());
        childOrder.setMemberName(order.getMemberName());
        childOrder.setOriginPrice(pinTuanGoodsVO.getOriginPrice());
        childOrder.setSalesPrice(pinTuanGoodsVO.getSalesPrice());

        tradeDaoSupport.insert(childOrder);

        if (logger.isDebugEnabled()) {
            logger.debug("创建一个新的子订单：");
            logger.debug(childOrder);
        }
        return pintuanOrder;
    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void payOrder(OrderDO order) {

        String orderSn = order.getSn();
        //查找子订单
        PintuanChildOrder childOrder = this.getChildByOrderSn(orderSn);

        if (logger.isDebugEnabled()) {
            logger.debug("订单【" + order.getSn() + "】支付成功，获得其对应拼团子订单为：");
            logger.debug(childOrder);
        }

        //查找主订单
        PintuanOrder pintuanOrder = this.getMainOrderById(childOrder.getOrderId());

        //加入一个参团者
        Member member = memberClient.getModel(order.getMemberId());
        Participant participant = new Participant();
        participant.setId(member.getMemberId());
        participant.setName(member.getUname());
        participant.setFace(member.getFace());
        pintuanOrder.appendParticipant(participant);


        //成团人数
        Integer requiredNum = pintuanOrder.getRequiredNum();

        //已参团人数
        Integer offeredNum = pintuanOrder.getOfferedNum();

        //新增一人
        offeredNum++;
        pintuanOrder.setOfferedNum(offeredNum);

        //如果已经成团 如果系统设置两人成团，实际三人付款，那么也成团
        if (offeredNum >= requiredNum) {

            if (logger.isDebugEnabled()) {
                logger.debug("拼团订单：" + pintuanOrder + "已经成团");
            }

            pintuanOrder.setOrderStatus(PintuanOrderStatus.formed.name());

            //更新拼团订单
            tradeDaoSupport.update(pintuanOrder, pintuanOrder.getOrderId());

            formed(pintuanOrder.getOrderId());

            if (logger.isDebugEnabled()) {
                logger.debug("更新所有子订单及普通订单为已成团");
            }
        } else {

            if (logger.isDebugEnabled()) {
                logger.debug("offeredNum[" + offeredNum + "],requiredNum[" + requiredNum + "]");
                logger.debug("拼团订单：" + pintuanOrder + "尚未成团");
            }

            //更新拼团订单为待成团
            pintuanOrder.setOrderStatus(PintuanOrderStatus.wait.name());

            //更新子订单为已支付状态
            tradeDaoSupport.execute("update es_pintuan_child_order set order_status=? where child_order_id=?", PintuanOrderStatus.pay_off.name(), childOrder.getChildOrderId());

            //更新拼团订单
            tradeDaoSupport.update(pintuanOrder, pintuanOrder.getOrderId());
        }

        //第一个人，即为创建拼团的人，那么配置定时任务进行处理
        if (pintuanOrder.getOfferedNum() == 1) {
            //标记延时任务处理这个订单，活动结束时   延时取消/自动成团
            timeTrigger.add("pintuanOrderHandlerTriggerExecuter", pintuanOrder.getOrderId(), pintuanOrder.getEndTime(), "pintuan_order_handler_" + pintuanOrder.getOrderId());
        }

        Integer count = requiredNum - offeredNum;
        if(count < 0 ){
            count = 0;
        }
        //更新拼团成团人数，更新本订单，也要更新整个团的订单
        updatePintuanPerson(pintuanOrder, count);

    }

    /**
     * 根据id获取模型
     *
     * @param id
     * @return
     */
    @Override
    public PintuanOrder getModel(Integer id) {
        return this.tradeDaoSupport.queryForObject(PintuanOrder.class,id);
    }

    /**
     * 更新拼团成团人数
     * @param pintuanOrder 拼团主订单
     * @param num   数量
     */
    private void updatePintuanPerson(PintuanOrder pintuanOrder,Integer num){

        //根据主订单查询所有的子订单
        String sql = "select o.order_data,o.sn from  es_pintuan_child_order pc inner join es_order o on pc.order_sn = o.sn  and pc.order_id = ?";
        List<Map> list = this.tradeDaoSupport.queryForList(sql, pintuanOrder.getOrderId());

        for(Map orderMap : list){

            String orderData = orderMap.get("order_data") == null ? "" :orderMap.get("order_data").toString();
            String sn = orderMap.get("sn").toString();

            PersonalizedData personalizedData = new PersonalizedData(orderData);
            Map map = new HashMap();
            //还差几人成团
            map.put("owesPersonNums", num);
            personalizedData.setPersonalizedData(OrderDataKey.pintuan, map);

            if(logger.isDebugEnabled()){
                logger.debug("拼团订单"+sn+"还差成团人数:"+num);
            }

            //更新订单的个性化数据
            tradeDaoSupport.execute("update es_order set order_data=? where sn=?", personalizedData.getData(), sn);


        }


    }

    /**
     * 某个拼团订单成团操作
     *
     * @param pinTuanOrderId
     */
    private void formed(Integer pinTuanOrderId) {

        //订单
        PintuanOrder pintuanOrder = this.getMainOrderById(pinTuanOrderId);

        String sql = "select order_sn from es_pintuan_child_order where order_id =?";

        List<Map> list = tradeDaoSupport.queryForList(sql, pinTuanOrderId);
        list.forEach(map -> {
            String orderSn = map.get("order_sn").toString();
            //更新订单状态为已成团
            tradeDaoSupport.execute("update es_order set order_status=? where sn=? and pay_status = ? ", OrderStatusEnum.FORMED.name(), orderSn,PayStatusEnum.PAY_YES.name());

        });

        //更新所有子订单为已经成团
        tradeDaoSupport.execute("update es_pintuan_child_order set order_status=? where order_id=?", PintuanOrderStatus.formed.name(), pinTuanOrderId);

        PintuanGoodsDO goodsDO = pintuanGoodsManager.getModel(pintuanOrder.getPintuanId(), pintuanOrder.getSkuId());
        pintuanGoodsManager.addQuantity(goodsDO.getId(), pintuanOrder.getOfferedNum());

        //延时任务执行-拼团活动结束时，获取拼团商品，
        PinTuanGoodsVO goodsVO = pintuanGoodsManager.getDetail(goodsDO.getSkuId(),pintuanOrder.getEndTime() - 1);
        pinTuanSearchManager.addIndex(goodsVO);

        this.amqpTemplate.convertAndSend(AmqpExchange.PINTUAN_SUCCESS, AmqpExchange.PINTUAN_SUCCESS + "_ROUTING", pinTuanOrderId);


    }


    @Override
    public PintuanOrderDetailVo getMainOrderBySn(String orderSn) {

        String sql = "select o.*,co.origin_price,co.sales_price from es_pintuan_order o ,es_pintuan_child_order co where o.order_id =co.order_id  and co.order_sn=?";

        return tradeDaoSupport.queryForObject(sql, PintuanOrderDetailVo.class, orderSn);

    }

    @Override
    public List<PintuanOrder> getWaitOrder(Integer goodsId,Integer skuId) {

        String sql = "select * from es_pintuan_order where sku_id=? and order_status=? " +
                " and pintuan_id = (select pintuan_id from es_pintuan p inner join es_pintuan_goods pg on p.promotion_id = pg.pintuan_id " +
                "  where start_time < ? and end_time >? and pg.sku_id = ? GROUP BY pintuan_id) " ;

        return tradeDaoSupport.queryForList(sql, PintuanOrder.class, skuId, PintuanOrderStatus.wait.name(),DateUtil.getDateline(),DateUtil.getDateline(),skuId);
    }

    /**
     * 读取某订单的所有子订单
     *
     * @param orderId
     * @return
     */
    @Override
    public List<PintuanChildOrder> getPintuanChild(Integer orderId) {
        String sql = "select * from es_pintuan_child_order where order_id=?";
        return tradeDaoSupport.queryForList(sql, PintuanChildOrder.class, orderId);
    }

    /**
     * 处理拼团订单
     *
     * @param orderId 订单id
     */
    @Override
    public void handle(Integer orderId) {

        //处理订单
        PintuanOrder pintuanOrder = this.getMainOrderById(orderId);

        Pintuan pintuan = pintuanManager.getModel(pintuanOrder.getPintuanId());

        List<PintuanChildOrder> pintuanChildOrders = this.getPintuanChild(pintuanOrder.getOrderId());

        //如果未开启虚拟成团
        if (pintuan.getEnableMocker().equals(0)) {
            //成团人数<参团人数
            if (pintuanOrder.getOfferedNum() < pintuanOrder.getRequiredNum()) {
                pintuanChildOrders.forEach(child -> {
                    afterSaleManager.cancelPintuanOrder(child.getOrderSn(), "拼团活动结束未成团，系统自动取消订单");
                });
            }
        }//虚拟成团&&未成团
        else if(pintuanOrder.getRequiredNum()-pintuanOrder.getOfferedNum()>0){

            //修改订单信息
            int num = pintuanOrder.getRequiredNum()-pintuanOrder.getOfferedNum();
            pintuanOrder.setOfferedNum(pintuanOrder.getRequiredNum());
            //匿名参团人
            for (int i = 0; i < num; i++) {
                Participant participant = new Participant();
                participant.setId(-1);
                participant.setName("小强");
                participant.setFace("http://javashop-statics.oss-cn-beijing.aliyuncs.com/v70/normal/912BDD3146AE4BE19831DB9F357A34D8.jpeg");
                pintuanOrder.appendParticipant(participant);
            }
            //更新订单的个性化数据
            Map map = new HashMap(1);
            map.put("order_id", pintuanOrder.getOrderId());
            //该商品已经成团
            pintuanOrder.setOrderStatus(PintuanOrderStatus.formed.name());
            this.tradeDaoSupport.update("es_pintuan_order", pintuanOrder, map);
            //虚拟成团更显订单中待成团人数
            this.updatePintuanPerson(pintuanOrder,pintuanOrder.getRequiredNum()-pintuanOrder.getParticipants().size());
            //更新这个拼团订单成团了
            this.formed(orderId);
        }

    }

    @Override
    public void cancelOrder(String orderSn) {
        PintuanChildOrder childByOrderSn = this.getChildByOrderSn(orderSn);
        if (childByOrderSn == null || this.getMainOrderById(childByOrderSn.getOrderId()) == null){
            throw new ResourceNotFoundException("拼团订单不存在");
        }

        //取消拼团订单,修改主订单中参团人列表和参团人数  add by liuyulei 2019-07-25
        PintuanOrder mainPinTuanOrder = this.getMainOrderById(childByOrderSn.getOrderId());

        List<Participant> list = mainPinTuanOrder.getParticipants();

        mainPinTuanOrder.getParticipants().forEach(participant -> {
            if(participant.getId().equals(childByOrderSn.getMemberId())){
                list.remove(participant);
            }
        });


        //拼团订单取消,修改子订单状态为取消
        this.tradeDaoSupport.execute("update es_pintuan_child_order set order_status = ? where order_sn = ?", PintuanOrderStatus.cancel.name(), orderSn);


        List<Object> term = new ArrayList<>();
        StringBuffer sql = new StringBuffer("update es_pintuan_order set ");

        //如果所有子订单都被取消了  那么主订单也需要取消  add by liuyulei 2019-07-31
        if(this.checkCancelStatus(childByOrderSn.getOrderId())){
            sql.append(" order_status = ? ,");
            term.add(PintuanOrderStatus.cancel.name());
        }


        sql.append(" offered_num = ? , offered_persons = ?  where order_id = ?");
        term.add(list.size());
        term.add(JsonUtil.objectToJson(list));
        term.add(childByOrderSn.getOrderId());

        //修改主订单参团人数信息
        this.tradeDaoSupport.execute(sql.toString(),term.toArray());

    }

    /**
     * 根据订单编号找到拼团子订单
     *
     * @param orderSn
     * @return
     */
    private PintuanChildOrder getChildByOrderSn(String orderSn) {
        String sql = "select * from es_pintuan_child_order where order_sn=?";
        return tradeDaoSupport.queryForObject(sql, PintuanChildOrder.class, orderSn);
    }

    /**
     * 通过订单获取skuid
     *
     * @param order 普通订单do
     * @return 这个订单对应的skuid
     */
    private Integer getSkuId(OrderDO order) {
        String itemsJson = order.getItemsJson();
        List<OrderSkuVO> list = JsonUtil.jsonToList(itemsJson, OrderSkuVO.class);
        OrderSkuVO skuVO = list.get(0);
        return skuVO.getSkuId();

    }

    private PintuanOrder getMainOrderById(Integer orderId) {
        String sql = "select * from es_pintuan_order where order_id=?";
        return tradeDaoSupport.queryForObject(sql, PintuanOrder.class, orderId);
    }

    /**
     * 查找此sku待成团的订单<br/>
     *
     * @param skuId
     * @return 如果存在这个sku的拼团订单则返回，否则返回null
     */
    private PintuanOrder getBySkuId(Integer skuId) {
        String sql = "select * from es_pintuan_order where sku_id=?";
        return tradeDaoSupport.queryForObject(sql, PintuanOrder.class, skuId);
    }


    /**
     * 监测所有子订单是否全部取消
     *
     * add by liuyulei 2019-07-31
     */
    private boolean checkCancelStatus(Integer orderId){
        List<PintuanChildOrder> list = this.getPintuanChild(orderId);

        AtomicInteger count = new AtomicInteger();

        //计算取消状态的子订单数量
        list.forEach(pintuanChildOrder -> {

            if(PintuanOrderStatus.cancel.name().equals(pintuanChildOrder.getOrderStatus())){
                count.getAndIncrement();

            }
        });

        //如果所有子订单都是取消状态,那么取消主订单
        return count.intValue() == list.size();
    }

}
