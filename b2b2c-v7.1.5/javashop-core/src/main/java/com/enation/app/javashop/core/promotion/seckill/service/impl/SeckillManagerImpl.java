package com.enation.app.javashop.core.promotion.seckill.service.impl;

import com.enation.app.javashop.core.base.rabbitmq.TimeExecute;
import com.enation.app.javashop.core.client.goods.GoodsClient;
import com.enation.app.javashop.core.goods.GoodsErrorCode;
import com.enation.app.javashop.core.goods.model.vo.CacheGoods;
import com.enation.app.javashop.core.promotion.PromotionErrorCode;
import com.enation.app.javashop.core.promotion.seckill.model.dos.SeckillApplyDO;
import com.enation.app.javashop.core.promotion.seckill.model.dos.SeckillDO;
import com.enation.app.javashop.core.promotion.seckill.model.dos.SeckillRangeDO;
import com.enation.app.javashop.core.promotion.seckill.model.dto.SeckillAuditParam;
import com.enation.app.javashop.core.promotion.seckill.model.dto.SeckillQueryParam;
import com.enation.app.javashop.core.promotion.seckill.model.enums.SeckillGoodsApplyStatusEnum;
import com.enation.app.javashop.core.promotion.seckill.model.enums.SeckillStatusEnum;
import com.enation.app.javashop.core.promotion.seckill.model.vo.SeckillGoodsVO;
import com.enation.app.javashop.core.promotion.seckill.model.vo.SeckillVO;
import com.enation.app.javashop.core.promotion.seckill.service.SeckillGoodsManager;
import com.enation.app.javashop.core.promotion.seckill.service.SeckillManager;
import com.enation.app.javashop.core.promotion.seckill.service.SeckillRangeManager;
import com.enation.app.javashop.core.promotion.tool.model.dos.PromotionGoodsDO;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 限时抢购入库业务类
 *
 * @author Snow
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-21 10:32:36
 */
@Service
public class SeckillManagerImpl extends AbstractPromotionRuleManagerImpl implements SeckillManager {

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    private SeckillRangeManager seckillRangeManager;

    @Autowired
    private SeckillGoodsManager seckillGoodsManager;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private TimeTrigger timeTrigger;

    @Autowired
    private Cache cache;

    @Autowired
    private PromotionGoodsManager promotionGoodsManager;

    @Override
    public Page list(SeckillQueryParam param) {

        StringBuffer sqlBuffer = new StringBuffer("select * from es_seckill where delete_status = ? ");
        List<Object> term = new ArrayList<>();
        term.add(DeleteStatusEnum.NORMAL.value());

        long toDayStartTime = DateUtil.startOfTodDay();
        long toDayEndTime = DateUtil.endOfTodDay();

        //按活动名称查询
        if (StringUtil.notEmpty(param.getSeckillName())) {
            sqlBuffer.append(" and seckill_name like ?");
            term.add("%" + param.getSeckillName() + "%");
        }

        //按活动状态查询
        if (StringUtil.notEmpty(param.getStatus()) && !"ALL".equals(param.getStatus())) {
            //编辑中
            if (SeckillStatusEnum.EDITING.value().equals(param.getStatus())) {
                sqlBuffer.append(" and seckill_status = ?");
                term.add(param.getStatus());
            } else if (SeckillStatusEnum.RELEASE.value().equals(param.getStatus())) {
                sqlBuffer.append(" and seckill_status = ? and start_day > ?");
                term.add(param.getStatus());
                term.add(toDayEndTime);
            } else if (SeckillStatusEnum.OPEN.value().equals(param.getStatus())) {
                sqlBuffer.append(" and seckill_status = ? and start_day >= ? and start_day <= ?");
                term.add(SeckillStatusEnum.RELEASE.value());
                term.add(toDayStartTime);
                term.add(toDayEndTime);
            } else if (SeckillStatusEnum.CLOSED.value().equals(param.getStatus())) {
                term.add(SeckillStatusEnum.RELEASE.value());
                sqlBuffer.append(" and seckill_status = ? and start_day < ?");
                term.add(toDayStartTime);
            }

        }

        //按活动日期查询
        if (param.getStartTime() != null && param.getStartTime() != 0) {
            sqlBuffer.append(" and start_day >= ?");
            term.add(param.getStartTime());
        }

        if (param.getEndTime() != null && param.getEndTime() != 0) {
            sqlBuffer.append(" and start_day <= ?");
            term.add(param.getEndTime());
        }

        sqlBuffer.append(" order by start_day desc");

        Page webPage = this.daoSupport.queryForPage(sqlBuffer.toString(), param.getPageNo(), param.getPageSize(), SeckillDO.class, term.toArray());

        List<SeckillVO> seckillVOList = new ArrayList<>();
        List<SeckillDO> seckillDOList = webPage.getData();
        for (SeckillDO seckillDO : seckillDOList) {

            List<Integer> rangeList = new ArrayList<>();
            List<SeckillRangeDO> rangeDOList = this.seckillRangeManager.getList(seckillDO.getSeckillId());
            for (SeckillRangeDO rangeDO : rangeDOList) {
                rangeList.add(rangeDO.getRangeTime());
            }

            SeckillVO seckillVO = new SeckillVO();
            BeanUtils.copyProperties(seckillDO, seckillVO);

            if (seckillVO.getSeckillStatus() != null) {
                SeckillStatusEnum statusEnum = SeckillStatusEnum.valueOf(seckillVO.getSeckillStatus());
                //如果状态是已发布状态，则判断该活动是否已开始或者已结束
                seckillVO.setSeckillStatusText(statusEnum.description());
                if (SeckillStatusEnum.RELEASE.equals(statusEnum)) {

                    //活动开始时间
                    long startDay = seckillDO.getStartDay();

                    if (DateUtil.startOfTodDay() <= startDay && DateUtil.endOfTodDay() > startDay) {
                        seckillVO.setSeckillStatusText("已开启");
                    } else if (startDay < DateUtil.endOfTodDay()) {
                        seckillVO.setSeckillStatusText("已关闭");
                    }

                }
            }

            seckillVO.setRangeList(rangeList);
            seckillVOList.add(seckillVO);
        }
        webPage.setData(seckillVOList);
        return webPage;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ServiceException.class, RuntimeException.class})
    public SeckillVO add(SeckillVO seckill) {

        String sql = "select * from es_seckill where seckill_name = ? ";
        List list = this.daoSupport.queryForList(sql, seckill.getSeckillName());
        if (list.size() > 0) {
            throw new ServiceException(PromotionErrorCode.E400.code(), "活动名称重复");
        }

        String date = DateUtil.toString(seckill.getStartDay(), "yyyy-MM-dd");
        long startTime = DateUtil.getDateline(date + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        long endTime = DateUtil.getDateline(date + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        this.verifyTime(startTime, endTime, PromotionTypeEnum.SECKILL, null);

        SeckillDO seckillDO = new SeckillDO();
        seckillDO.setDeleteStatus(DeleteStatusEnum.NORMAL.value());
        BeanUtils.copyProperties(seckill, seckillDO);
        this.daoSupport.insert(seckillDO);

        Integer id = this.daoSupport.getLastId("es_seckill");
        seckill.setSeckillId(id);

        this.seckillRangeManager.addList(seckill.getRangeList(), id);
        return seckill;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ServiceException.class})
    public SeckillVO edit(SeckillVO seckill, Integer id) {

        String sql = "select * from es_seckill where seckill_name = ? and seckill_id != ? ";
        List list = this.daoSupport.queryForList(sql, seckill.getSeckillName(), id);
        if (list.size() > 0) {
            throw new ServiceException(PromotionErrorCode.E400.code(), "活动名称重复");
        }

        String date = DateUtil.toString(seckill.getStartDay(), "yyyy-MM-dd");
        long startTime = DateUtil.getDateline(date + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        long endTime = DateUtil.getDateline(date + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        this.verifyTime(startTime, endTime, PromotionTypeEnum.SECKILL, id);

        SeckillDO seckillDO = new SeckillDO();
        BeanUtils.copyProperties(seckill, seckillDO);

        this.daoSupport.update(seckillDO, id);

        this.seckillRangeManager.addList(seckill.getRangeList(), id);
        return seckill;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ServiceException.class})
    public void delete(Integer id) {

        SeckillVO seckill = this.getModel(id);
        if (seckill == null) {
            throw new ServiceException(PromotionErrorCode.E400.code(), "活动不存在");
        }

        //编辑中和已经结束的活动均可以删除
        if (SeckillStatusEnum.EDITING.name().equals(seckill.getSeckillStatus()) || seckill.getStartDay().longValue() < DateUtil.startOfTodDay()) {
            String sql = "delete from es_seckill where seckill_id = ? ";
            this.daoSupport.execute(sql, id);
            //删除限时抢购商品
            this.seckillGoodsManager.deleteBySeckillId(id);
            this.promotionGoodsManager.delete(id, PromotionTypeEnum.SECKILL.name());
        } else {
            throw new ServiceException(PromotionErrorCode.E400.code(), "该活动不是能删除的状态");
        }

    }

    @Override
    public SeckillVO getModel(Integer id) {

        SeckillDO seckillDO = this.daoSupport.queryForObject(SeckillDO.class, id);
        if (seckillDO == null) {
            throw new ServiceException(PromotionErrorCode.E400.code(), "活动不存在");
        }
        SeckillVO seckillVO = new SeckillVO();
        BeanUtils.copyProperties(seckillDO, seckillVO);

        List<Integer> rangeList = new ArrayList<>();
        List<SeckillRangeDO> rangeDOList = this.seckillRangeManager.getList(id);
        for (SeckillRangeDO rangeDO : rangeDOList) {
            rangeList.add(rangeDO.getRangeTime());
        }
        seckillVO.setRangeList(rangeList);
        return seckillVO;
    }


    @Override
    public SeckillGoodsVO getSeckillGoods(Integer goodsId) {

        Map<Integer, List<SeckillGoodsVO>> map = this.seckillGoodsManager.getSeckillGoodsList();
        SeckillGoodsVO goodsVO = null;
        for (Map.Entry<Integer, List<SeckillGoodsVO>> entry : map.entrySet()) {
            List<SeckillGoodsVO> list = entry.getValue();

            for (SeckillGoodsVO seckillGoods : list) {
                if (seckillGoods.getGoodsId().equals(goodsId)) {
                    goodsVO = new SeckillGoodsVO(seckillGoods, entry.getKey());
                }
            }
        }
        return goodsVO;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ServiceException.class, RuntimeException.class})
    public void batchAuditGoods(SeckillAuditParam param) {
        if (param.getApplyIds() == null || param.getApplyIds().length == 0) {
            throw new ServiceException(GoodsErrorCode.E301.code(), "请选择要审核的商品");
        }

        if (StringUtil.isEmpty(param.getStatus())) {
            throw new ServiceException(PromotionErrorCode.E400.code(), "审核状态值不正确");
        }

        //状态值不正确
        SeckillGoodsApplyStatusEnum applyStatusEnum = SeckillGoodsApplyStatusEnum.valueOf(param.getStatus());

        //驳回，原因必填
        if (applyStatusEnum.equals(SeckillGoodsApplyStatusEnum.FAIL)) {
            if (StringUtil.isEmpty(param.getFailReason())) {
                throw new ServiceException(PromotionErrorCode.E400.code(), "驳回原因必填");
            }

            if (param.getFailReason().length() > 500){
                throw new ServiceException(PromotionErrorCode.E400.code(), "驳回原因长度不能超过500个字符");
            }
        }

        //批量审核
        for (Integer applyId : param.getApplyIds()) {
            String sql = "select *  from es_seckill_apply where apply_id = ? and status = ? ";
            SeckillApplyDO apply = this.daoSupport.queryForObject(sql, SeckillApplyDO.class, applyId, SeckillGoodsApplyStatusEnum.APPLY.name());
            //申请不存在
            if (apply == null) {
                throw new ServiceException(PromotionErrorCode.E400.code(), "商品【"+apply.getGoodsName()+"】不是可以审核的状态");
            }

            apply.setStatus(applyStatusEnum.name());
            apply.setFailReason(param.getFailReason());

            Map where = new HashMap(16);
            where.put("apply_id", applyId);
            this.daoSupport.update("es_seckill_apply", apply, where);
            //查询商品
            CacheGoods goods = goodsClient.getFromCache(apply.getGoodsId());
            //将审核通过的商品，存储到活动商品表和缓存中
            if (applyStatusEnum.equals(SeckillGoodsApplyStatusEnum.PASS)) {
                //促销商品表
                PromotionGoodsDO promotion = new PromotionGoodsDO();
                promotion.setTitle("限时抢购");
                promotion.setGoodsId(apply.getGoodsId());
                promotion.setPromotionType(PromotionTypeEnum.SECKILL.name());
                promotion.setActivityId(apply.getApplyId());
                promotion.setNum(apply.getSoldQuantity());
                promotion.setPrice(apply.getPrice());
                promotion.setSellerId(goods.getSellerId());
                //商品活动的开始时间为当前商品的参加时间段
                int timeLine = apply.getTimeLine();
                String date = DateUtil.toString(apply.getStartDay(), "yyyy-MM-dd");
                long startTime = DateUtil.getDateline(date + " " + timeLine + ":00:00", "yyyy-MM-dd HH:mm:ss");
                long endTime = DateUtil.getDateline(date + " 23:59:59", "yyyy-MM-dd HH:mm:ss");

                promotion.setStartTime(startTime);
                promotion.setEndTime(endTime);
                this.daoSupport.insert("es_promotion_goods", promotion);

                //从缓存读取限时抢购的活动的商品
                String redisKey = getRedisKey(apply.getStartDay());
                Map<Integer, List<SeckillGoodsVO>> map = this.cache.getHash(redisKey);
                //如果redis中有当前审核商品参与的限时抢购活动商品信息，就删除掉
                if (map != null && !map.isEmpty()) {
                    this.cache.remove(redisKey);
                }

                //设置延迟加载任务，到活动开始时间后将搜索引擎中的优惠价格设置为0
                PromotionPriceDTO promotionPriceDTO = new PromotionPriceDTO();
                promotionPriceDTO.setGoodsId(apply.getGoodsId());
                promotionPriceDTO.setPrice(apply.getPrice());
                timeTrigger.add(TimeExecute.PROMOTION_EXECUTER, promotionPriceDTO, startTime, null);
                //此活动结束后将索引的优惠价格重置为0
                promotionPriceDTO.setPrice(0.0);
                timeTrigger.add(TimeExecute.PROMOTION_EXECUTER, promotionPriceDTO, endTime, null);
            }
        }
    }

    @Override
    public void sellerApply(Integer sellerId, Integer seckillId) {
        SeckillDO seckillDO = this.getModel(seckillId);
        String sellerIds;
        if (!StringUtil.isEmpty(seckillDO.getSellerIds())) {
            sellerIds = seckillDO.getSellerIds() + sellerId + ",";
        } else {
            sellerIds = sellerId + ",";
        }
        String sql = "update es_seckill set seller_ids = ? where seckill_id = ? ";
        this.daoSupport.execute(sql, sellerIds, seckillId);
    }

    @Override
    public void close(Integer id) {

        SeckillVO seckill = this.getModel(id);
        if (seckill == null) {
            throw new ServiceException(PromotionErrorCode.E400.code(), "活动不存在");
        }

        String statusEnum = seckill.getSeckillStatus();
        if (SeckillStatusEnum.RELEASE.name().equals(statusEnum)) {

            //活动开始时间
            long startDay = seckill.getStartDay();

            //已开启状态
            if (DateUtil.startOfTodDay() < startDay && DateUtil.endOfTodDay() > startDay) {
                //此时可以暂停

            }
        }

    }

    /**
     * 获取限时抢购key
     * @param dateline
     * @return
     */
    private String getRedisKey(long dateline) {
        return PromotionCacheKeys.getSeckillKey(DateUtil.toString(dateline, "yyyyMMdd"));
    }

}
