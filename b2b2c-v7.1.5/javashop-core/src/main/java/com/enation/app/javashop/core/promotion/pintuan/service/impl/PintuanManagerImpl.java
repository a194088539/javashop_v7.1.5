package com.enation.app.javashop.core.promotion.pintuan.service.impl;

import com.enation.app.javashop.core.base.message.PintuanChangeMsg;
import com.enation.app.javashop.core.base.rabbitmq.TimeExecute;
import com.enation.app.javashop.core.promotion.pintuan.exception.PintuanErrorCode;
import com.enation.app.javashop.core.promotion.pintuan.model.*;
import com.enation.app.javashop.core.promotion.pintuan.service.PintuanGoodsManager;
import com.enation.app.javashop.core.promotion.pintuan.service.PintuanOrderManager;
import com.enation.app.javashop.core.promotion.tool.model.enums.PromotionStatusEnum;
import com.enation.app.javashop.core.statistics.util.DateUtil;
import com.enation.app.javashop.framework.context.AdminUserContext;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.exception.SystemErrorCodeV1;
import com.enation.app.javashop.framework.trigger.Interface.TimeTrigger;
import com.enation.app.javashop.framework.util.SqlUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.core.promotion.pintuan.service.PintuanManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 拼团业务类
 *
 * @author admin
 * @version vv1.0.0
 * @since vv7.1.0
 * 2019-01-21 15:17:57
 */
@Service
public class PintuanManagerImpl implements PintuanManager {

    /**
     * 拼团促销前缀
     */
    private static final String TRIGGER_PREFIX = "{pintuan_promotion}_";

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport tradeDaoSupport;

    @Autowired
    private PintuanGoodsManager pintuanGoodsManager;

    @Autowired
    private TimeTrigger timeTrigger;

    @Autowired
    private PintuanOrderManager pintuanOrderManager;

    @Override
    public Page list(PintuanQueryParam param) {

        String sql = "select * from es_pintuan ";

        List<String> where = new ArrayList<>();
        List term = new ArrayList<>();

        //按商家查询
        if (param.getSellerId() != null && param.getSellerId() != 0) {
            where.add(" seller_id = ? ");
            term.add(param.getSellerId());
        }

        //按活动名称查询
        if (StringUtil.notEmpty(param.getName())) {
            where.add(" promotion_name like ? ");
            term.add("%" + param.getName() + "%");
        }

        //按活动状态查询
        if (StringUtil.notEmpty(param.getStatus())) {
            where.add(" status = ? ");
            term.add(param.getStatus());
        }

        //按活动时间查询
        if (param.getStartTime() != null && param.getStartTime() != 0) {
            where.add(" start_time >= ? ");
            term.add(param.getStartTime());
        }
        if (param.getEndTime() != null && param.getEndTime() != 0) {
            where.add(" end_time <= ? ");
            term.add(param.getEndTime());
        }

        sql += SqlUtil.sqlSplicing(where);
        sql += " order by create_time desc";
        Page webPage = this.tradeDaoSupport.queryForPage(sql, param.getPageNo(), param.getPageSize(), Pintuan.class, term.toArray());

        return webPage;
    }

    @Override
    public List<Pintuan> get(String status) {
        String sql = "select * from es_pintuan where status = ?";
        if (PromotionStatusEnum.UNDERWAY.name().equals(status)) {
            long now = DateUtil.getDateline();
            sql += " and start_time > " + now + " and end_time < " + now;
        }
        return tradeDaoSupport.queryForList(sql, Pintuan.class, status);
    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Pintuan add(Pintuan pintuan) {

        this.verifyParam(pintuan.getStartTime(), pintuan.getEndTime());
        pintuan.setStatus(PromotionStatusEnum.WAIT.name());
        pintuan.setSellerName(UserContext.getSeller().getSellerName());
        pintuan.setCreateTime(DateUtil.getDateline());
        pintuan.setSellerId(UserContext.getSeller().getSellerId());
        //可操作状态为nothing，代表活动不可以执行任何操作
        pintuan.setOptionStatus(PintuanOptionEnum.NOTHING.name());
        this.tradeDaoSupport.insert(pintuan);
        Integer pintuanId = this.tradeDaoSupport.getLastId("es_pintuan");
        pintuan.setPromotionId(pintuanId);

        //创建活动 启用延时任务
        PintuanChangeMsg pintuanChangeMsg = new PintuanChangeMsg();
        pintuanChangeMsg.setPintuanId(pintuan.getPromotionId());
        pintuanChangeMsg.setOptionType(1);
        timeTrigger.add(TimeExecute.PINTUAN_EXECUTER, pintuanChangeMsg, pintuan.getStartTime(), TRIGGER_PREFIX + pintuan.getPromotionId());
        pintuan.setPromotionId(this.tradeDaoSupport.getLastId("es_pintuan"));
        return pintuan;
    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Pintuan edit(Pintuan pintuan, Integer id) {
        //获取拼团活动
        Pintuan oldPintaun = this.getModel(id);
        //校验拼团是否可以被操作
        if (pintuan.getStatus().equals(PromotionStatusEnum.UNDERWAY.name())) {
            throw new ServiceException(PintuanErrorCode.E5017.code(), PintuanErrorCode.E5017.describe());
        }

        this.verifyParam(pintuan.getStartTime(), pintuan.getEndTime());
        this.tradeDaoSupport.update(pintuan, id);
        //修改拼团活动,首先删除旧活动的关闭任务  add by liuyulei 2019-08-08
        timeTrigger.delete(TimeExecute.PINTUAN_EXECUTER,oldPintaun.getEndTime(),"{TIME_TRIGGER}_"  + id);

        PintuanChangeMsg pintuanChangeMsg = new PintuanChangeMsg();
        pintuanChangeMsg.setPintuanId(pintuan.getPromotionId());
        pintuanChangeMsg.setOptionType(1);
        timeTrigger.edit(TimeExecute.PINTUAN_EXECUTER, pintuanChangeMsg, pintuan.getStartTime(), pintuan.getStartTime(), TRIGGER_PREFIX + id);
        return pintuan;
    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delete(Integer id) {
        Pintuan pintuan = this.getModel(id);

        if (pintuan.getStatus().equals(PromotionStatusEnum.UNDERWAY.name())) {
            throw new ServiceException(PintuanErrorCode.E5017.code(), PintuanErrorCode.E5017.describe());
        }

        this.tradeDaoSupport.delete(Pintuan.class, id);


        timeTrigger.delete(TimeExecute.PINTUAN_EXECUTER, pintuan.getStartTime(), TRIGGER_PREFIX + id);
    }

    @Override
    public Pintuan getModel(Integer id) {
        return this.tradeDaoSupport.queryForObject(Pintuan.class, id);
    }


    /**
     * 开始一个活动
     *
     * @param promotionId
     */
    @Override
    public void openPromotion(Integer promotionId) {

        Pintuan pintuan = this.getModel(promotionId);

        //如果还在活动时间内
        //修改状态为进行中，活动可操作状态变成可以关闭
        if (pintuan.getEndTime() > DateUtil.getDateline()) {
            this.tradeDaoSupport.execute("update es_pintuan set status = ? ,option_status=? where promotion_id = ?", PromotionStatusEnum.UNDERWAY.name(), PintuanOptionEnum.CAN_CLOSE.name(), promotionId);
            pintuanGoodsManager.addIndex(promotionId);
        } else {
            //活动时间范围外，修改状态为已结束，活动可操作状态变成nothing
            this.tradeDaoSupport.execute("update es_pintuan set status = ? ,option_status=? where promotion_id = ?", PromotionStatusEnum.END.name(), PintuanOptionEnum.NOTHING.name(), promotionId);
        }

    }

    /**
     * 停止一个活动
     *
     * @param promotionId
     */
    @Override
    public void closePromotion(Integer promotionId) {

        Pintuan pintuan = this.getModel(promotionId);

        //如果结束时间大于当前时间
        // 可以操作为开启状态，活动状态为已结束
        if (pintuan.getEndTime() > DateUtil.getDateline()) {
            //表示可以再次开启，则不处理未成团订单，因为可以开启
            this.tradeDaoSupport.execute("update es_pintuan set status = ? ,option_status=? where promotion_id = ?", PromotionStatusEnum.END.name(), PintuanOptionEnum.CAN_OPEN.name(), promotionId);
        } else {
            this.tradeDaoSupport.execute("update es_pintuan set status = ? ,option_status=? where promotion_id = ?", PromotionStatusEnum.END.name(), PintuanOptionEnum.NOTHING.name(), promotionId);
            //查询所有该活动下的未成团订单（未付款，已付款未成团）
            String sql = "select * from es_pintuan_order where (order_status = ? or order_status = ?) and pintuan_id = ?";

            List<PintuanOrder> orderList = this.tradeDaoSupport.queryForList(sql,PintuanOrder.class,PintuanOrderStatus.new_order.name(),PintuanOrderStatus.wait.name(),promotionId);
            for(PintuanOrder order : orderList){
               pintuanOrderManager.handle(order.getOrderId());
            }


        }
        pintuanGoodsManager.delIndex(promotionId);
    }

    /**
     * 手动停止一个活动
     *
     * @param promotionId
     */
    @Override
    public void manualClosePromotion(Integer promotionId) {
        if (check(promotionId, 0)) {
            this.closePromotion(promotionId);
        } else {
            throw new ServiceException(PintuanErrorCode.E5012.code(), PintuanErrorCode.E5012.describe());
        }
    }

    /**
     * 手动开始一个活动
     *
     * @param promotionId
     */
    @Override
    public void manualOpenPromotion(Integer promotionId) {
        if (check(promotionId, 1)) {
            this.openPromotion(promotionId);
        } else {
            throw new ServiceException(PintuanErrorCode.E5012.code(), PintuanErrorCode.E5012.describe());
        }
    }

    /**
     * 校验 是否可以手动操作
     *
     * @param promotionId 拼团id
     * @param type        1开启检测 0结束检测
     * @return
     */
    private boolean check(Integer promotionId, Integer type) {


        Pintuan pintuan = this.getModel(promotionId);
        if (AdminUserContext.getAdmin() == null) {
            if (UserContext.getSeller().getSellerId().equals(pintuan.getSellerId())) {
                throw new ServiceException(PintuanErrorCode.E5013.code(), PintuanErrorCode.E5013.describe());
            }
        }

        //时间段不对，不许操作
        if (pintuan.getStartTime() > DateUtil.getDateline() || pintuan.getEndTime() < DateUtil.getDateline()) {
            return false;
        }
        //开启
        if (type == 1) {
            //如果活动已经结束 可以操作开始
            return pintuan.getStatus().equals(PromotionStatusEnum.END.name());
        } else {
            //如果活动进行中 可以操作停止
            return pintuan.getStatus().equals(PromotionStatusEnum.UNDERWAY.name());
        }
    }

    /**
     * 验证参数
     *
     * @param startTime 活动开始时间
     * @param endTime   活动结束时间
     */
    private void verifyParam(long startTime, long endTime) {

        // 开始时间不能大于结束时间
        if (startTime > endTime) {
            throw new ServiceException(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER, "活动起始时间不能大于活动结束时间");
        }

    }

}
