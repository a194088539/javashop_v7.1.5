package com.enation.app.javashop.core.trade.complain.service.impl;

import com.enation.app.javashop.core.trade.TradeErrorCode;
import com.enation.app.javashop.core.trade.complain.model.dos.OrderComplainCommunication;
import com.enation.app.javashop.core.trade.complain.model.dto.ComplainDTO;
import com.enation.app.javashop.core.trade.complain.model.dto.ComplainQueryParam;
import com.enation.app.javashop.core.trade.complain.model.enums.ComplainQueryTagEnum;
import com.enation.app.javashop.core.trade.complain.model.enums.ComplainSkuStatusEnum;
import com.enation.app.javashop.core.trade.complain.model.enums.ComplainStatusEnum;
import com.enation.app.javashop.core.trade.complain.model.vo.ComplainFlow;
import com.enation.app.javashop.core.trade.complain.model.vo.OrderComplainVO;
import com.enation.app.javashop.core.trade.complain.service.OrderComplainCommunicationManager;
import com.enation.app.javashop.core.trade.order.model.vo.OrderFlowNode;
import com.enation.app.javashop.core.trade.order.service.OrderOperateManager;
import com.enation.app.javashop.core.trade.order.service.OrderQueryManager;
import com.enation.app.javashop.core.trade.sdk.model.OrderDetailDTO;
import com.enation.app.javashop.core.trade.sdk.model.OrderSkuDTO;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.security.model.Buyer;
import com.enation.app.javashop.framework.util.BeanUtil;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.SqlUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.core.trade.complain.model.dos.OrderComplain;
import com.enation.app.javashop.core.trade.complain.service.OrderComplainManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 交易投诉表业务类
 *
 * @author fk
 * @version v2.0
 * @since v2.0
 * 2019-11-27 16:48:27
 */
@Service
public class OrderComplainManagerImpl implements OrderComplainManager {

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;
    @Autowired
    private OrderQueryManager orderQueryManager;

    @Autowired
    private OrderComplainCommunicationManager orderComplainCommunicationManager;

    @Autowired
    private OrderOperateManager orderOperateManager;

    @Override
    public Page list(ComplainQueryParam param) {

        StringBuffer sqlBuffer = new StringBuffer("select * from es_order_complain ");

        List<Object> term = new ArrayList<>();
        List<String> condition = new ArrayList<>();

        //会员id
        if (param.getMemberId() != null) {
            condition.add("member_id = ?");
            term.add(param.getMemberId());
        }
        //商家id
        if (param.getSellerId() != null) {
            condition.add("seller_id = ?");
            term.add(param.getSellerId());
        }
        //订单编号
        if (!StringUtil.isEmpty(param.getOrderSn())) {
            condition.add("order_sn = ?");
            term.add(param.getOrderSn());
        }
        //关键字
        if (!StringUtil.isEmpty(param.getKeywords())) {
            condition.add("(order_sn = ? or goods_name = ?)");
            term.add(param.getKeywords());
            term.add(param.getKeywords());
        }

        // 按标签查询
        String tag = param.getTag();
        if (!StringUtil.isEmpty(tag)) {
            ComplainQueryTagEnum tagEnum = ComplainQueryTagEnum.valueOf(tag);
            switch (tagEnum) {

                case ALL:
                    break;
                //进行中
                case COMPLAINING:
                    condition.add("status != ? and status != ?");
                    term.add(ComplainStatusEnum.COMPLETE.name());
                    term.add(ComplainStatusEnum.CANCEL.name());
                    break;
                //已完成
                case COMPLETE:
                    condition.add("status = ?");
                    term.add(ComplainStatusEnum.COMPLETE.name());
                    break;
                //已撤销
                case CANCELED:
                    condition.add("status = ?");
                    term.add(ComplainStatusEnum.CANCEL.name());
                    break;
                default: {
                    break;
                }
            }
        }

        sqlBuffer.append(SqlUtil.sqlSplicing(condition));
        sqlBuffer.append(" order by create_time desc ");

        Page webPage = this.daoSupport.queryForPage(sqlBuffer.toString(), param.getPageNo(), param.getPageSize(), OrderComplain.class, term.toArray());

        return webPage;
    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public OrderComplain add(ComplainDTO complain) {

        Buyer buyer = UserContext.getBuyer();
        String orderSn = complain.getOrderSn();
        OrderDetailDTO order = orderQueryManager.getModel(orderSn);
        List<OrderSkuDTO> skuList = order.getOrderSkuList();
        OrderSkuDTO skuGoods = null;
        for (OrderSkuDTO sku : skuList) {
            if (sku.getSkuId().equals(complain.getSkuId())) {
                skuGoods = sku;
                break;
            }
        }
        if (skuGoods == null) {
            throw new ServiceException(TradeErrorCode.E472.code(), "您要投诉的商品不存在");
        }
        OrderComplain orderComplain = new OrderComplain();
        BeanUtil.copyProperties(complain, orderComplain);
        orderComplain.setCreateTime(DateUtil.getDateline());
        orderComplain.setStatus(ComplainStatusEnum.NEW.name());
        //商品信息
        orderComplain.setGoodsId(skuGoods.getGoodsId());
        orderComplain.setGoodsName(skuGoods.getName());
        orderComplain.setGoodsPrice(skuGoods.getPurchasePrice());
        orderComplain.setNum(skuGoods.getNum());
        orderComplain.setGoodsImage(skuGoods.getGoodsImage());
        //订单信息
        orderComplain.setOrderPrice(order.getOrderPrice());
        orderComplain.setOrderTime(order.getCreateTime());
        orderComplain.setShippingPrice(order.getShippingPrice());
        orderComplain.setShipNo(order.getShipNo());
        orderComplain.setShipMobile(order.getShipMobile());
        String addr = order.getShipProvince() + order.getShipCity() + order.getShipCounty();
        if (order.getShipTown() != null) {
            addr += order.getShipTown();
        }
        orderComplain.setShipAddr(addr + order.getShipAddr());
        orderComplain.setShipName(order.getShipName());
        //会员信息
        orderComplain.setMemberId(buyer.getUid());
        orderComplain.setMemberName(buyer.getUsername());
        //商家信息
        orderComplain.setSellerId(order.getSellerId());
        orderComplain.setSellerName(order.getSellerName());

        this.daoSupport.insert(orderComplain);
        int id = this.daoSupport.getLastId("");
        orderComplain.setComplainId(id);

        //更新订单商品的可投诉状态
        orderOperateManager.updateOrderItemsComplainStatus(order.getSn(), skuGoods.getSkuId(), id, ComplainSkuStatusEnum.APPLYING);

        return orderComplain;
    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public OrderComplain edit(OrderComplain orderComplain, Integer id) {
        this.daoSupport.update(orderComplain, id);
        return orderComplain;
    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delete(Integer id) {
        this.daoSupport.delete(OrderComplain.class, id);
    }

    @Override
    public OrderComplain getModel(Integer id) {
        return this.daoSupport.queryForObject(OrderComplain.class, id);
    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public OrderComplain cancel(Integer id) {

        Buyer buyer = UserContext.getBuyer();
        OrderComplain model = this.getModel(id);
        if (model == null || !model.getMemberId().equals(buyer.getUid())) {
            throw new ServiceException(TradeErrorCode.E472.code(), "您无法操作该交易");
        }
        model.setStatus(ComplainStatusEnum.CANCEL.name());
        this.daoSupport.update(model, id);

        //更新订单商品的可投诉状态
        orderOperateManager.updateOrderItemsComplainStatus(model.getOrderSn(), model.getSkuId(), null, ComplainSkuStatusEnum.COMPLETE);

        return model;
    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public OrderComplain auth(Integer id) {

        OrderComplain model = this.checkModel(id);
        model.setStatus(ComplainStatusEnum.WAIT_APPEAL.name());
        this.daoSupport.update(model, id);

        return model;
    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public OrderComplain complete(Integer id, String arbitrationResult) {

        OrderComplain model = this.checkModel(id);
        model.setArbitrationResult(arbitrationResult);
        model.setStatus(ComplainStatusEnum.COMPLETE.name());
        this.daoSupport.update(model, id);

        //更新订单商品的可投诉状态
        orderOperateManager.updateOrderItemsComplainStatus(model.getOrderSn(), model.getSkuId(), null, ComplainSkuStatusEnum.COMPLETE);

        return model;
    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public OrderComplain appeal(Integer id, String appealContent, String[] images) {

        OrderComplain model = this.checkModel(id);
        model.setAppealContent(appealContent);
        if (images != null) {
            model.setAppealImages(StringUtil.arrayToString(images,","));
        }
        model.setAppealTime(DateUtil.getDateline());
        model.setStatus(ComplainStatusEnum.COMMUNICATION.name());
        this.daoSupport.update(model, id);

        return model;
    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public OrderComplain arbitrate(Integer id) {

        OrderComplain model = this.checkModel(id);
        model.setStatus(ComplainStatusEnum.WAIT_ARBITRATION.name());
        this.daoSupport.update(model, id);
        return model;
    }

    @Override
    public OrderComplainVO getModelAndCommunication(Integer id) {

        List<OrderComplainCommunication> list = orderComplainCommunicationManager.list(id);

        OrderComplain orderComplain = this.getModel(id);

        OrderComplainVO res = new OrderComplainVO();
        BeanUtil.copyProperties(orderComplain, res);

        res.setCommunicationList(list);

        return res;
    }

    @Override
    public List<OrderFlowNode> getComplainFlow(Integer id) {

        OrderComplain model = this.getModel(id);
        // 交易投诉状态
        String status = model.getStatus();
        // 获取已撤销流程
        if (ComplainStatusEnum.CANCEL.name().equals(status)) {
            return ComplainFlow.getCancelFlow();
        }

        List<OrderFlowNode> flowList = ComplainFlow.getNormalFlow();

        boolean isEnd = false;
        for (OrderFlowNode flow : flowList) {

            Integer showStatus = isEnd ? 0 : 1;
            flow.setShowStatus(showStatus);

            if (flow.getOrderStatus().equals(status)) {
                //当前状态往后的流程都是没走的，所以是灰色
                isEnd = true;
            }
        }

        return flowList;
    }

    /**
     * 查询，并查看是否真实存在该交易
     *
     * @param id
     * @return
     */
    private OrderComplain checkModel(Integer id) {

        OrderComplain model = this.getModel(id);
        if (model == null) {
            throw new ServiceException(TradeErrorCode.E472.code(), "您无法操作该交易");
        }

        return model;
    }
}
