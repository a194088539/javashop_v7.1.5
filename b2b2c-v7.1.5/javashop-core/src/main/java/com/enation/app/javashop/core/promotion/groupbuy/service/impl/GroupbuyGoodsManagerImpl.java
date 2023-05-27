package com.enation.app.javashop.core.promotion.groupbuy.service.impl;

import com.enation.app.javashop.core.client.goods.GoodsClient;
import com.enation.app.javashop.core.goods.model.vo.CacheGoods;
import com.enation.app.javashop.core.goods.model.vo.GoodsSkuVO;
import com.enation.app.javashop.core.promotion.PromotionErrorCode;
import com.enation.app.javashop.core.promotion.groupbuy.model.dos.GroupbuyActiveDO;
import com.enation.app.javashop.core.promotion.groupbuy.model.dos.GroupbuyGoodsDO;
import com.enation.app.javashop.core.promotion.groupbuy.model.dos.GroupbuyQuantityLog;
import com.enation.app.javashop.core.promotion.groupbuy.model.enums.GroupBuyGoodsStatusEnum;
import com.enation.app.javashop.core.promotion.groupbuy.model.enums.GroupbuyQuantityLogEnum;
import com.enation.app.javashop.core.promotion.groupbuy.model.vo.GroupbuyGoodsVO;
import com.enation.app.javashop.core.promotion.groupbuy.model.vo.GroupbuyQueryParam;
import com.enation.app.javashop.core.promotion.groupbuy.service.GroupbuyActiveManager;
import com.enation.app.javashop.core.promotion.groupbuy.service.GroupbuyGoodsManager;
import com.enation.app.javashop.core.promotion.groupbuy.service.GroupbuyQuantityLogManager;
import com.enation.app.javashop.core.promotion.tool.model.dto.PromotionDTO;
import com.enation.app.javashop.core.promotion.tool.model.dto.PromotionGoodsDTO;
import com.enation.app.javashop.core.promotion.tool.model.enums.PromotionTypeEnum;
import com.enation.app.javashop.core.promotion.tool.service.PromotionGoodsManager;
import com.enation.app.javashop.core.promotion.tool.service.impl.AbstractPromotionRuleManagerImpl;
import com.enation.app.javashop.core.system.enums.DeleteStatusEnum;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.exception.NoPermissionException;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.security.model.Seller;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.SqlUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.locks.Lock;

/**
 * 团购商品业务类
 *
 * @author Snow
 * @version v7.0.0
 * @since v7.0.0
 * 2018-04-02 16:57:26
 */
@Service
public class GroupbuyGoodsManagerImpl extends AbstractPromotionRuleManagerImpl implements GroupbuyGoodsManager {

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    private GroupbuyActiveManager groupbuyActiveManager;


    @Autowired
    private GroupbuyQuantityLogManager groupbuyQuantityLogManager;

    @Autowired
    private RedissonClient redisson;

    @Autowired
    private GoodsClient goodsClient;


    @Autowired
    private PromotionGoodsManager promotionGoodsManager;

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public Page listPage(GroupbuyQueryParam param) {

        List whereParam = new ArrayList();
        StringBuffer sql = new StringBuffer("select gg.*,ga.act_name as title,ga.start_time,ga.end_time,ga.delete_status,ga.delete_reason,ga.delete_name,ga.delete_time " +
                "from es_groupbuy_goods as gg left join es_groupbuy_active as ga on gg.act_id=ga.act_id ");

        List<String> sqlList = new ArrayList();
        long nowTime = DateUtil.getDateline();

        //如果是平台管理端
        if (StringUtil.notEmpty(param.getClientType()) && "ADMIN".equals(param.getClientType())) {
            sqlList.add(" gg.act_id=? ");
            whereParam.add(param.getActId());
        }

        //按商家查询
        if (param.getSellerId() != null && param.getSellerId() != 0) {
            sqlList.add(" gg.seller_id=? ");
            whereParam.add(param.getSellerId());
        }

        //按商品名称查询
        if (StringUtil.notEmpty(param.getGoodsName())) {
            sqlList.add(" gg.goods_name like ? ");
            whereParam.add("%" + param.getGoodsName() + "%");
        }

        //按团购名称查询
        if (StringUtil.notEmpty(param.getGbName())) {
            sqlList.add(" gg.gb_name like ? ");
            whereParam.add("%" + param.getGbName() + "%");
        }

        //按商品审核状态查询
        if (param.getGbStatus() != null) {
            sqlList.add(" gg.gb_status=? ");
            whereParam.add(param.getGbStatus());
        }

        //按团购活动名称查询
        if (StringUtil.notEmpty(param.getActName())) {
            sqlList.add(" ga.act_name like ? ");
            whereParam.add("%" + param.getActName() + "%");
        }

        //按团购活动状态查询
        if (StringUtil.notEmpty(param.getActStatus()) && !"ALL".equals(param.getActStatus())) {
            //未开始的活动
            if ("NOT_STARTED".equals(param.getActStatus())) {
                sqlList.add(" ga.start_time > ?");
                whereParam.add(nowTime);

                //进行中的活动
            } else if ("STARTED".equals(param.getActStatus())) {
                sqlList.add(" ga.start_time <= ? and ga.end_time >= ?");
                whereParam.add(nowTime);
                whereParam.add(nowTime);

                //已结束的活动
            } else if ("OVER".equals(param.getActStatus())) {
                sqlList.add(" ga.end_time < ?");
                whereParam.add(nowTime);
            }
        }

        sql.append(SqlUtil.sqlSplicing(sqlList));

        sql.append(" order by gg.add_time desc ");

        Page webPage = this.daoSupport.queryForPage(sql.toString(), param.getPage(), param.getPageSize(), GroupbuyGoodsVO.class, whereParam.toArray());

        List<GroupbuyGoodsVO> groupbuyGoodsVOList = webPage.getData();
        Long currTime = DateUtil.getDateline();
        for (GroupbuyGoodsVO goodsVO : groupbuyGoodsVOList) {
            switch (goodsVO.getGbStatus()) {
                case 0:
                    goodsVO.setGbStatusText("待审核");
                    break;
                case 1:
                    goodsVO.setGbStatusText("通过审核");
                    break;
                case 2:
                    goodsVO.setGbStatusText("未通过审核");
                    break;
                default:
                    break;
            }
            if (currTime <= goodsVO.getEndTime() && DeleteStatusEnum.NORMAL.value().equals(goodsVO.getDeleteStatus())) {
                goodsVO.setIsEnable(1);//活动没有开始或者活动正在进行中，可以对商品进行修改
            } else {
                goodsVO.setIsEnable(0);//活动失效
            }

        }
        webPage.setData(groupbuyGoodsVOList);
        return webPage;
    }

    @Override
    public Page listPageByBuyer(GroupbuyQueryParam param) {

        List whereParam = new ArrayList();
        StringBuffer sql = new StringBuffer("select gg.*,pg.start_time,pg.end_time,pg.title from es_promotion_goods pg " +
                " left join es_groupbuy_goods gg on pg.goods_id =gg.goods_id and pg.activity_id = gg.act_id left join es_groupbuy_active ga on gg.act_id = ga.act_id " +
                " where pg.promotion_type = ? and ga.delete_status = ? ");
        whereParam.add(PromotionTypeEnum.GROUPBUY.name());
        whereParam.add(DeleteStatusEnum.NORMAL.value());

        if (param.getStartTime() != null && param.getEndTime() != null) {
            sql.append(" and ? > pg.start_time and ? < pg.end_time ");
            whereParam.add(param.getStartTime());
            whereParam.add(param.getEndTime());
        }
        if (param.getCatId() != null) {
            sql.append(" and gg.cat_id = ?");
            whereParam.add(param.getCatId());
        }
        Page webPage = this.daoSupport.queryForPage(sql.toString(), param.getPage(), param.getPageSize(), GroupbuyGoodsVO.class, whereParam.toArray());
        return webPage;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public GroupbuyGoodsDO add(GroupbuyGoodsDO goodsDO) {

        GroupbuyActiveDO activeDO = groupbuyActiveManager.getModel(goodsDO.getActId());
        if (activeDO == null) {
            throw new ServiceException(PromotionErrorCode.E400.code(), "参与的活动不存在");
        }
        //如果团购价格 大于等于商品原价，则抛出异常
        if (goodsDO.getPrice() >= goodsDO.getOriginalPrice()) {
            throw new ServiceException(PromotionErrorCode.E400.code(), "参与活动的商品促销价格不得大于或等于商品原价");
        }
        //校验限购数量是否超过商品总数
        if (goodsDO.getLimitNum() > goodsDO.getGoodsNum()) {
            throw new ServiceException(PromotionErrorCode.E400.code(), "商品限购数量不能大于商品总数");
        }

        /**
         * *************两种情况：******************
         * 秒杀时间段：      |________________|
         * 团购时间段：  |_____|           |_______|
         *
         * ************第三种情况：******************
         * 秒杀时间段：        |______|
         * 团购时间段：   |________________|
         *
         * ************第四种情况：******************
         * 秒杀时间段：   |________________|
         * 团购时间段：        |______|
         */


        String sql = "select count(0) from es_promotion_goods where promotion_type='SECKILL' and goods_id=? and (" +
                " ( start_time<?  && end_time>? )" +
                " || ( start_time<?  && end_time>? )" +
                " || ( start_time<?  && end_time>? )" +
                " || ( start_time>?  && end_time<? ))";
        int count = daoSupport.queryForInt(sql, goodsDO.getGoodsId(),
                activeDO.getStartTime(), activeDO.getStartTime(),
                activeDO.getEndTime(), activeDO.getEndTime(),
                activeDO.getStartTime(), activeDO.getEndTime(),
                activeDO.getStartTime(), activeDO.getEndTime()
        );

        if (count > 0) {
            throw new ServiceException(PromotionErrorCode.E400.code(), "该商品已经在重叠的时间段参加了限时抢购活动，不能参加团购活动");
        }
        List<PromotionGoodsDTO> goodsDTOList = new ArrayList<>();
        PromotionGoodsDTO goodsDTO = new PromotionGoodsDTO();
        goodsDTO.setGoodsId(goodsDO.getGoodsId());
        goodsDTO.setThumbnail(goodsDO.getThumbnail());
        goodsDTO.setGoodsName(goodsDO.getGoodsName());
        goodsDTOList.add(goodsDTO);

        //检测活动商品规则
        this.verifyRule(goodsDTOList);
        goodsDO.setGbStatus(GroupBuyGoodsStatusEnum.PENDING.status());

        this.daoSupport.insert(goodsDO);
        int id = this.daoSupport.getLastId("es_groupbuy_goods");
        goodsDO.setGbId(id);
        return goodsDO;
    }

    @Override
    public GroupbuyGoodsDO edit(GroupbuyGoodsDO goodsDO, Integer id) {

        GroupbuyActiveDO activeDO = groupbuyActiveManager.getModel(goodsDO.getActId());

        List<PromotionGoodsDTO> goodsDTOList = new ArrayList<>();
        PromotionGoodsDTO goodsDTO = new PromotionGoodsDTO();
        goodsDTO.setGoodsId(goodsDO.getGoodsId());
        goodsDTO.setThumbnail(goodsDO.getThumbnail());
        goodsDTO.setGoodsName(goodsDO.getGoodsName());
        goodsDTOList.add(goodsDTO);

        //检测活动商品规则
        this.verifyRule(goodsDTOList);

        //如果团购价格 大于等于商品原价，则抛出异常
        if (goodsDO.getPrice() >= goodsDO.getOriginalPrice()) {
            throw new ServiceException(PromotionErrorCode.E400.code(), "参与活动的商品促销价格不得大于或等于商品原价");
        }
        //校验限购数量是否超过商品总数
        if (goodsDO.getLimitNum() > goodsDO.getGoodsNum()) {
            throw new ServiceException(PromotionErrorCode.E400.code(), "商品限购数量不能大于商品总数");
        }

        Seller seller = UserContext.getSeller();
        goodsDO.setSellerId(seller.getSellerId());

        // 将团购商品状态重置为待审核
        goodsDO.setGbStatus(GroupBuyGoodsStatusEnum.PENDING.status());
        this.daoSupport.update(goodsDO, id);
        this.daoSupport.execute("delete from es_promotion_goods where goods_id = ? and promotion_type = ? and activity_id = ?", goodsDTO.getGoodsId(), PromotionTypeEnum.GROUPBUY.name(), goodsDO.getActId());
        return goodsDO;
    }

    @Override
    public void delete(Integer id) {

        //删除活动商品表中的团购信息
        GroupbuyGoodsVO groupbuyGoods = this.getModel(id);
        Integer goodsId = groupbuyGoods.getGoodsId();
        this.promotionGoodsManager.delete(goodsId, groupbuyGoods.getActId(), PromotionTypeEnum.GROUPBUY.name());

        this.daoSupport.delete(GroupbuyGoodsDO.class, id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public void deleteGoods(Integer goodsId) {
        this.daoSupport.execute("delete from es_groupbuy_goods where goods_id = ? ", goodsId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public GroupbuyGoodsVO getModel(Integer gbId) {

        String sql = "select gg.*,ga.act_name as title,ga.start_time,ga.end_time from es_groupbuy_goods as gg " +
                "left join es_groupbuy_active as ga on gg.act_id=ga.act_id where gb_id = ?";

        GroupbuyGoodsVO groupbuyGoodsVO = this.daoSupport.queryForObject(sql, GroupbuyGoodsVO.class, gbId);

        if (groupbuyGoodsVO != null && groupbuyGoodsVO.getSkuId() != null) {
            GoodsSkuVO skuVO = goodsClient.getSku(groupbuyGoodsVO.getSkuId());
            groupbuyGoodsVO.setEnableQuantity(skuVO.getEnableQuantity());
            groupbuyGoodsVO.setQuantity(skuVO.getQuantity());
        } else {
            CacheGoods cacheGoods = goodsClient.getFromCache(groupbuyGoodsVO.getGoodsId());
            groupbuyGoodsVO.setEnableQuantity(cacheGoods.getEnableQuantity());
            groupbuyGoodsVO.setQuantity(cacheGoods.getQuantity());
        }


        return groupbuyGoodsVO;
    }

    @Override
    public GroupbuyGoodsDO getModel(Integer actId, Integer goodsId) {
        String sql = "select * from es_groupbuy_goods where act_id = ? and goods_id=?";
        GroupbuyGoodsDO groupbuyGoodsDO = this.daoSupport.queryForObject(sql, GroupbuyGoodsDO.class, actId, goodsId);
        return groupbuyGoodsDO;
    }


    @Override
    public void verifyAuth(Integer id) {
        GroupbuyGoodsDO groupbuyGoodsDO = this.getModel(id);

        Seller seller = UserContext.getSeller();
        if (groupbuyGoodsDO == null || groupbuyGoodsDO.getSellerId().intValue() != seller.getSellerId()) {
            throw new NoPermissionException("无权操作");
        }

    }


    @Override
    public void updateStatus(Integer gbId, Integer status) {
        String sql = "update es_groupbuy_goods set gb_status=? where gb_id=?";
        this.daoSupport.execute(sql, status, gbId);
    }


    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public boolean cutQuantity(String orderSn, List<PromotionDTO> promotionDTOList) {

        for (PromotionDTO promotionDTO : promotionDTOList) {

            int num = promotionDTO.getNum();
            int goodsId = promotionDTO.getGoodsId();
            int actId = promotionDTO.getActId();
            try {

                String sql = "update es_groupbuy_goods set buy_num=buy_num+?,goods_num=goods_num-? where goods_id=? and act_id=? and goods_num >=?";
                int rowNum = this.daoSupport.execute(sql, num, num, goodsId, actId, num);

                if (rowNum <= 0) {
                    return false;
                } else {
                    logAndCleanCache(promotionDTO, orderSn);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;

            }
        }


        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public void addQuantity(String orderSn) {

        List<GroupbuyQuantityLog> logs = groupbuyQuantityLogManager.rollbackReduce(orderSn);
        for (GroupbuyQuantityLog log : logs) {
            int num = log.getQuantity();
            int goodsId = log.getGoodsId();
            int gbId = log.getGbId();
            String sql = "update es_groupbuy_goods set buy_num=buy_num-?,goods_num=goods_num+? where goods_id=? and act_id=?";
            this.daoSupport.execute(sql, num, num, goodsId, gbId);
        }
    }

    @Override
    public GroupbuyGoodsVO getModelAndQuantity(Integer id) {

        GroupbuyGoodsDO groupbuyGoods = this.getModel(id);
        if (groupbuyGoods != null) {
            GroupbuyGoodsVO res = new GroupbuyGoodsVO();
            BeanUtils.copyProperties(groupbuyGoods, res);
            CacheGoods goods = goodsClient.getFromCache(groupbuyGoods.getGoodsId());
            res.setEnableQuantity(goods.getEnableQuantity());
            res.setQuantity(goods.getQuantity());
            return res;
        }

        return null;
    }

    @Override
    public void updateGoodsInfo(Integer[] goodsIds) {
        if (goodsIds == null) {
            return;
        }
        List<Map<String, Object>> result = goodsClient.getGoods(goodsIds);
        if (result == null || result.isEmpty()) {
            return;
        }
        for (Map<String, Object> map : result) {
            Map<String, Object> whereMap = new HashMap<>();
            whereMap.put("goods_id", map.get("goods_id"));
            this.daoSupport.update("es_groupbuy_goods", map, whereMap);
        }


    }

    @Override
    public void rollbackStock(List<PromotionDTO> promotionDTOList, String orderSn) {
        for (PromotionDTO promotionDTO : promotionDTOList) {

            int num = promotionDTO.getNum();
            int goodsId = promotionDTO.getGoodsId();
            int actId = promotionDTO.getActId();

            String sql = "update es_groupbuy_goods set buy_num=buy_num-?,goods_num=goods_num+? where goods_id=? and act_id=? ";
            this.daoSupport.execute(sql, num, num, goodsId, actId);
            logAndCleanCache(promotionDTO, orderSn);

        }
    }


    /**
     * 获取商品id的拼接
     *
     * @param goodsIds
     * @param term
     * @return
     */
    private String getIdStr(Integer[] goodsIds, List<Integer> term) {

        String[] goods = new String[goodsIds.length];
        for (int i = 0; i < goodsIds.length; i++) {
            goods[i] = "?";
            term.add(goodsIds[i]);

        }

        return StringUtil.arrayToString(goods, ",");
    }


    private Lock getGoodsQuantityLock(Integer gbId) {
        RLock lock = redisson.getLock("groupbuy_goods_quantity_lock_" + gbId);
        return lock;
    }


    /**
     * 记录日志并清空缓存
     *
     * @param promotionDTO
     * @param orderSn
     */
    private void logAndCleanCache(PromotionDTO promotionDTO, String orderSn) {
        GroupbuyQuantityLog groupbuyQuantityLog = new GroupbuyQuantityLog();
        groupbuyQuantityLog.setOpTime(DateUtil.getDateline());
        groupbuyQuantityLog.setQuantity(promotionDTO.getNum());
        groupbuyQuantityLog.setReason("团购销售");
        groupbuyQuantityLog.setGbId(promotionDTO.getActId());
        groupbuyQuantityLog.setLogType(GroupbuyQuantityLogEnum.BUY.name());
        groupbuyQuantityLog.setOrderSn(orderSn);
        groupbuyQuantityLog.setGoodsId(promotionDTO.getGoodsId());
        groupbuyQuantityLogManager.add(groupbuyQuantityLog);
        this.promotionGoodsManager.reputCache(promotionDTO.getGoodsId());
    }


}
