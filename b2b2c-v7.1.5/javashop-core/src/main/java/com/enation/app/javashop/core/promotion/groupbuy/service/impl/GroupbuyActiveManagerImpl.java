package com.enation.app.javashop.core.promotion.groupbuy.service.impl;

import com.enation.app.javashop.core.base.rabbitmq.TimeExecute;
import com.enation.app.javashop.core.goods.GoodsErrorCode;
import com.enation.app.javashop.core.promotion.PromotionErrorCode;
import com.enation.app.javashop.core.promotion.groupbuy.model.dos.GroupbuyActiveDO;
import com.enation.app.javashop.core.promotion.groupbuy.model.dos.GroupbuyGoodsDO;
import com.enation.app.javashop.core.promotion.groupbuy.model.enums.GroupBuyGoodsStatusEnum;
import com.enation.app.javashop.core.promotion.groupbuy.model.vo.GroupbuyActiveVO;
import com.enation.app.javashop.core.promotion.groupbuy.model.vo.GroupbuyAuditParam;
import com.enation.app.javashop.core.promotion.groupbuy.model.vo.GroupbuyQueryParam;
import com.enation.app.javashop.core.promotion.groupbuy.service.GroupbuyActiveManager;
import com.enation.app.javashop.core.promotion.groupbuy.service.GroupbuyGoodsManager;
import com.enation.app.javashop.core.promotion.tool.model.dto.PromotionDetailDTO;
import com.enation.app.javashop.core.promotion.tool.model.dto.PromotionGoodsDTO;
import com.enation.app.javashop.core.promotion.tool.model.dto.PromotionPriceDTO;
import com.enation.app.javashop.core.promotion.tool.model.enums.PromotionTypeEnum;
import com.enation.app.javashop.core.promotion.tool.service.PromotionGoodsManager;
import com.enation.app.javashop.core.promotion.tool.service.impl.AbstractPromotionRuleManagerImpl;
import com.enation.app.javashop.core.promotion.tool.support.PromotionCacheKeys;
import com.enation.app.javashop.core.system.enums.DeleteStatusEnum;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.trigger.Interface.TimeTrigger;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 团购活动表业务类
 *
 * @author Snow
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-21 11:52:14
 */
@Service
public class GroupbuyActiveManagerImpl extends AbstractPromotionRuleManagerImpl implements GroupbuyActiveManager {

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    private GroupbuyGoodsManager groupbuyGoodsManager;

    @Autowired
    private PromotionGoodsManager promotionGoodsManager;

    @Autowired
    private TimeTrigger timeTrigger;

    @Autowired
    private Cache cache;

    @Override
    public Page list(GroupbuyQueryParam param) {
        StringBuffer sqlBuffer = new StringBuffer("select * from es_groupbuy_active where delete_status = ? ");
        List<Object> term = new ArrayList<>();

        term.add(DeleteStatusEnum.NORMAL.value());

        long nowTime = DateUtil.getDateline();

        //活动名称查询
        if (StringUtil.notEmpty(param.getActName())) {
            sqlBuffer.append(" and act_name like ?");
            term.add("%" + param.getActName() + "%");
        }

        //活动状态查询
        if (StringUtil.notEmpty(param.getActStatus()) && !"ALL".equals(param.getActStatus())) {
            //未开始的活动
            if ("NOT_STARTED".equals(param.getActStatus())) {
                sqlBuffer.append(" and start_time > ?");
                term.add(nowTime);

            //进行中的活动
            } else if ("STARTED".equals(param.getActStatus())) {
                sqlBuffer.append(" and start_time <= ? and end_time >= ?");
                term.add(nowTime);
                term.add(nowTime);

            //已结束的活动
            } else if ("OVER".equals(param.getActStatus())) {
                sqlBuffer.append(" and end_time < ?");
                term.add(nowTime);
            }
        }

        //活动开始时间查询
        if (param.getStartTime() != null && param.getStartTime() != 0) {
            sqlBuffer.append(" and start_time >= ?");
            term.add(param.getStartTime());
        }

        //活动结束时间查询
        if (param.getEndTime() != null && param.getEndTime() != 0) {
            sqlBuffer.append(" and end_time <= ?");
            term.add(param.getEndTime());
        }

        sqlBuffer.append(" order by add_time desc");

        Page webPage = this.daoSupport.queryForPage(sqlBuffer.toString(), param.getPage(), param.getPageSize(), GroupbuyActiveVO.class, term.toArray());
        return webPage;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {RuntimeException.class})
    public GroupbuyActiveDO add(GroupbuyActiveDO groupbuyActive) {
        this.verifyTime(groupbuyActive.getStartTime(), groupbuyActive.getEndTime(), PromotionTypeEnum.GROUPBUY, null);
        this.verifyName(groupbuyActive.getActName(), false, 0);
        groupbuyActive.setAddTime(DateUtil.getDateline());
        groupbuyActive.setDeleteStatus(DeleteStatusEnum.NORMAL.value());
        this.daoSupport.insert(groupbuyActive);
        int id = this.daoSupport.getLastId("es_groupbuy_active");
        groupbuyActive.setActId(id);
        return groupbuyActive;
    }

    @Override
    public GroupbuyActiveDO edit(GroupbuyActiveDO groupbuyActive, Integer id) {
        this.verifyTime(groupbuyActive.getStartTime(), groupbuyActive.getEndTime(), PromotionTypeEnum.GROUPBUY, id);

        this.verifyName(groupbuyActive.getActName(), true, id);
        this.verifyAuth(id);
        this.daoSupport.update(groupbuyActive, id);
        return groupbuyActive;
    }

    @Override
    public void delete(Integer id, String deleteReason, String operaName) {
        this.verifyAuth(id);
        //删除团购活动
        String sql = "update es_groupbuy_active set delete_status = ?, delete_reason = ?, delete_time = ?, delete_name = ? where act_id = ? ";
        this.daoSupport.execute(sql, DeleteStatusEnum.DELETED.value(), deleteReason, DateUtil.getDateline(), operaName, id);

        //删除团购商品（这里清空的是es_promotion_goods表中的团购商品，对es_groupbuy_active表的团购商品没影响）
        this.promotionGoodsManager.delete(id, PromotionTypeEnum.GROUPBUY.name());

    }

    @Override
    public GroupbuyActiveDO getModel(Integer id) {
        return this.daoSupport.queryForObject(GroupbuyActiveDO.class, id);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {RuntimeException.class, Exception.class})
    public void batchAuditGoods(GroupbuyAuditParam param) {
        // 校验活动id
        if (param.getActId() == null || param.getActId() == 0) {
            throw new ServiceException(PromotionErrorCode.E400.code(), "参数活动ID不正确");
        }

        // 校验活动商品
        if (param.getGbIds() == null || param.getGbIds().length == 0) {
            throw new ServiceException(GoodsErrorCode.E301.code(), "请选择要审核的商品");
        }

        // 校验审核状态
        if (param.getStatus() == null) {
            throw new ServiceException(PromotionErrorCode.E400.code(), "参数审核状态值不能为空");
        }

        if (param.getStatus().intValue() != 1 && param.getStatus().intValue() != 2) {
            throw new ServiceException(PromotionErrorCode.E400.code(), "参数审核状态值不正确");
        }

        for (Integer gbId : param.getGbIds()) {

            //读取团购商品
            GroupbuyGoodsDO goodsDO = this.groupbuyGoodsManager.getModel(gbId);
            if (goodsDO == null) {
                throw new ServiceException(PromotionErrorCode.E400.code(), "商品【"+goodsDO.getGoodsName()+"】不存在");
            }

            if (goodsDO.getGbStatus().intValue() != 0) {
                throw new ServiceException(PromotionErrorCode.E400.code(), "商品【"+goodsDO.getGoodsName()+"】不是可以审核的状态");
            }

            //修改状态
            this.groupbuyGoodsManager.updateStatus(gbId, param.getStatus());

            //如果通过审核
            if (param.getStatus().intValue() == GroupBuyGoodsStatusEnum.APPROVED.status()) {

                //修改已参与团购活动的商品数量
                this.daoSupport.execute("update es_groupbuy_active set goods_num=goods_num+1 where act_id=?", param.getActId());

                //活动信息DO
                GroupbuyActiveDO activeDO = this.getModel(param.getActId());

                //商品集合
                List<PromotionGoodsDTO> goodsDTOList = new ArrayList<>();
                PromotionGoodsDTO goodsDTO = new PromotionGoodsDTO();
                goodsDTO.setGoodsName(goodsDO.getGoodsName());
                goodsDTO.setThumbnail(goodsDO.getThumbnail());
                goodsDTO.setGoodsId(goodsDO.getGoodsId());
                goodsDTO.setPrice(goodsDO.getPrice());
                goodsDTO.setQuantity(goodsDO.getGoodsNum());
                goodsDTO.setSellerId(goodsDO.getSellerId());
                goodsDTOList.add(goodsDTO);

                //活动信息DTO
                PromotionDetailDTO detailDTO = new PromotionDetailDTO();
                detailDTO.setActivityId(param.getActId());
                detailDTO.setStartTime(activeDO.getStartTime());
                detailDTO.setEndTime(activeDO.getEndTime());
                detailDTO.setPromotionType(PromotionTypeEnum.GROUPBUY.name());
                detailDTO.setTitle(activeDO.getActName());
                //入库到活动商品对照表
                this.promotionGoodsManager.add(goodsDTOList, detailDTO);
                this.cache.put(PromotionCacheKeys.getGroupbuyKey(param.getActId()), goodsDO);
                //将此商品加入延迟加载队列，到指定的时间将索引价格变成最新的优惠价格
                PromotionPriceDTO promotionPriceDTO = new PromotionPriceDTO();
                promotionPriceDTO.setGoodsId(goodsDO.getGoodsId());
                promotionPriceDTO.setPrice(goodsDO.getPrice());
                timeTrigger.add(TimeExecute.PROMOTION_EXECUTER, promotionPriceDTO, activeDO.getStartTime(), null);
                //此活动结束后将索引的优惠价格重置为0
                promotionPriceDTO.setPrice(0.0);
                timeTrigger.add(TimeExecute.PROMOTION_EXECUTER, promotionPriceDTO, activeDO.getEndTime(), null);

            }
        }
    }

    @Override
    public List<GroupbuyActiveDO> getActiveList() {

        long nowTime = DateUtil.getDateline();
        String sql = "select * from es_groupbuy_active where join_end_time>=? and delete_status = ? order by add_time desc";
        return this.daoSupport.queryForList(sql, GroupbuyActiveDO.class, nowTime, DeleteStatusEnum.NORMAL.value());
    }


    @Override
    public void verifyAuth(Integer id) {
        GroupbuyActiveDO activeDO = this.getModel(id);
        long nowTime = DateUtil.getDateline();

        //如果活动起始时间小于现在时间，活动已经开始了。
        if (activeDO.getStartTime().longValue() < nowTime && activeDO.getEndTime().longValue() > nowTime) {
            throw new ServiceException(PromotionErrorCode.E400.code(), "活动已经开始，不能进行编辑删除操作");
        }
    }

}
