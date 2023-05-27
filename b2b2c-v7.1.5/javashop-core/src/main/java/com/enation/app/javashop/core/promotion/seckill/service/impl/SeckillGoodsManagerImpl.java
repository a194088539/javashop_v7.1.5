package com.enation.app.javashop.core.promotion.seckill.service.impl;

import com.enation.app.javashop.core.client.goods.GoodsClient;
import com.enation.app.javashop.core.goods.model.vo.CacheGoods;
import com.enation.app.javashop.core.promotion.PromotionErrorCode;
import com.enation.app.javashop.core.promotion.seckill.model.dos.SeckillApplyDO;
import com.enation.app.javashop.core.promotion.seckill.model.dto.SeckillQueryParam;
import com.enation.app.javashop.core.promotion.seckill.model.enums.SeckillGoodsApplyStatusEnum;
import com.enation.app.javashop.core.promotion.seckill.model.vo.SeckillApplyVO;
import com.enation.app.javashop.core.promotion.seckill.model.vo.SeckillGoodsVO;
import com.enation.app.javashop.core.promotion.seckill.model.vo.SeckillVO;
import com.enation.app.javashop.core.promotion.seckill.service.SeckillGoodsManager;
import com.enation.app.javashop.core.promotion.seckill.service.SeckillManager;
import com.enation.app.javashop.core.promotion.tool.model.dto.PromotionDTO;
import com.enation.app.javashop.core.promotion.tool.service.impl.AbstractPromotionRuleManagerImpl;
import com.enation.app.javashop.core.promotion.tool.support.PromotionCacheKeys;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.locks.Lock;

/**
 * 限时抢购申请业务类
 *
 * @author Snow
 * @version v7.0.0
 * @since v7.0.0
 * 2018-04-02 17:30:09
 */
@Service
public class SeckillGoodsManagerImpl extends AbstractPromotionRuleManagerImpl implements SeckillGoodsManager {

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SeckillManager seckillManager;

    @Autowired
    private Cache cache;

    @Autowired
    private RedissonClient redisson;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Page list(SeckillQueryParam queryParam) {
        List param = new ArrayList();

        StringBuffer sql = new StringBuffer();
        sql.append("select * from es_seckill_apply where seckill_id = ? ");
        param.add(queryParam.getSeckillId());

        //按商家查询
        if (queryParam.getSellerId() != null && queryParam.getSellerId() != 0) {
            sql.append(" and seller_id = ? ");
            param.add(queryParam.getSellerId());
        }

        //按商品名称查询
        if (StringUtil.notEmpty(queryParam.getGoodsName())) {
            sql.append(" and goods_name like ? ");
            param.add("%" + queryParam.getGoodsName() + "%");
        }

        //按审核状态查询
        if (StringUtil.notEmpty(queryParam.getStatus())) {
            sql.append(" and status = ? ");
            param.add(queryParam.getStatus());
        }

        sql.append(" order by apply_id desc");
        Page webPage = this.daoSupport.queryForPage(sql.toString(), queryParam.getPageNo(), queryParam.getPageSize(), SeckillApplyVO.class, param.toArray());

        return webPage;
    }


    @Override
    public void delete(Integer id) {

        this.daoSupport.delete(SeckillApplyDO.class, id);
    }

    @Override
    public SeckillApplyDO getModel(Integer id) {

        return this.daoSupport.queryForObject(SeckillApplyDO.class, id);
    }


    @Override
    @Transactional(value = "tradeTransactionManager",propagation = Propagation.REQUIRED, rollbackFor = {RuntimeException.class, ServiceException.class})
    public void addApply(List<SeckillApplyDO> list) {

        Integer sellerId = list.get(0).getSellerId();
        String sellerName = list.get(0).getShopName();

        SeckillVO seckillVO = this.seckillManager.getModel(list.get(0).getSeckillId());
        String sellerIds = seckillVO.getSellerIds();

        //如果是空的则不需要进行判断重复参与
        if (!StringUtil.isEmpty(sellerIds)) {
            String[] sellerIdSplit = sellerIds.split(",");
            if (Arrays.asList(sellerIdSplit).contains(sellerId + "")) {
                throw new ServiceException(PromotionErrorCode.E402.code(), "您已参与此活动，无法重复参与");
            }
        }
        for (SeckillApplyDO seckillApplyDO : list) {
            Integer goodsId = seckillApplyDO.getGoodsId();
            //查询商品
            CacheGoods goods = goodsClient.getFromCache(goodsId);
            //判断参加活动的数量和库存数量
            if (seckillApplyDO.getSoldQuantity() > goods.getEnableQuantity()) {
                throw new ServiceException(PromotionErrorCode.E402.code(), seckillApplyDO.getGoodsName() + ",此商品库存不足");
            }

            /**
             * *************两种情况：******************
             * 团购时间段：      |________________|
             * 秒杀时间段：  |_____|           |_______|
             *
             * ************第三种情况：******************
             * 团购时间段：        |______|
             * 秒杀时间段：   |________________|
             *
             * ************第四种情况：******************
             * 团购时间段：   |________________|
             * 秒杀时间段：        |______|
             *
             */
            //这个商品的开始时间计算要用他参与的时间段来计算，结束时间是当天晚上23：59：59
            String date = DateUtil.toString(seckillVO.getStartDay(), "yyyy-MM-dd");
            long startTime = DateUtil.getDateline(date + " " + seckillApplyDO.getTimeLine() + ":00:00", "yyyy-MM-dd HH:mm:ss");
            long endTime = DateUtil.getDateline(date + " 23:59:59", "yyyy-MM-dd HH:mm:ss");

            String sql = "select count(0) from es_promotion_goods where promotion_type='GROUPBUY' and goods_id=? and (" +
                    " ( start_time<?  && end_time>? )" +
                    " || ( start_time<?  && end_time>? )"+
                    " || ( start_time<?  && end_time>? )"+
                    " || ( start_time>?  && end_time<? ))";
            int count = daoSupport.queryForInt(sql, goodsId,
                    startTime,startTime,
                    endTime, endTime,
                    startTime, endTime,
                    startTime, endTime
            );
            if (count > 0) {
                throw new ServiceException(PromotionErrorCode.E400.code(), "商品[" + goods.getGoodsName() + "]已经在重叠的时间段参加了团购活动，不能参加限时抢购活动");
            }


            //商品的原始价格
            seckillApplyDO.setOriginalPrice(goods.getPrice());
            seckillApplyDO.setSellerId(sellerId);
            seckillApplyDO.setShopName(sellerName);
            seckillApplyDO.setStatus(SeckillGoodsApplyStatusEnum.APPLY.name());
            seckillApplyDO.setSalesNum(0);
            this.daoSupport.insert(seckillApplyDO);
            int applyId = this.daoSupport.getLastId("es_seckill_apply");
            seckillApplyDO.setApplyId(applyId);
        }

        this.seckillManager.sellerApply(sellerId, seckillVO.getSeckillId());

    }



    /**
     * 回滚秒杀库存
     * @param goodsList
     */
    private void innerRollbackStock(List<SeckillGoodsVO> goodsList) {
        for (SeckillGoodsVO goodsVO : goodsList) {
            int num = goodsVO.getSoldNum();
            String sql = "update es_seckill_apply set sold_quantity = sold_quantity+?,sales_num = sales_num - ? where goods_id = ? and seckill_id=?";
            this.daoSupport.execute(sql, num, num, goodsVO.getGoodsId(), goodsVO.getSeckillId());
        }

    }



    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = {RuntimeException.class, ServiceException.class})
    public void rollbackStock(List<PromotionDTO> promotionDTOList) {

        List<SeckillGoodsVO> lockedList = new ArrayList<>();

        //遍历活动与商品关系的类
        for (PromotionDTO promotionDTO : promotionDTOList) {

            Map<Integer, List<SeckillGoodsVO>> map = this.getSeckillGoodsList();

            for (Map.Entry<Integer, List<SeckillGoodsVO>> entry : map.entrySet()) {

                List<SeckillGoodsVO> seckillGoodsDTOList = entry.getValue();
                for (SeckillGoodsVO goodsVO : seckillGoodsDTOList) {

                    if (goodsVO.getGoodsId().equals(promotionDTO.getGoodsId())) {
                        //用户购买的数量
                        int num = promotionDTO.getNum();
                        goodsVO.setSoldNum(num);
                        lockedList.add(goodsVO);

                    }
                }
            }
        }

        this.cache.remove(PromotionCacheKeys.getSeckillKey(DateUtil.toString(DateUtil.getDateline(), "yyyyMMdd")));

        innerRollbackStock(lockedList);

    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = {RuntimeException.class, ServiceException.class})
    public void deleteSeckillGoods(Integer goodsId) {

        //删除限时抢购已经开始和未开始的商品
        this.daoSupport.execute("delete from es_seckill_apply where goods_id = ? and start_day >= ? ",goodsId,DateUtil.startOfTodDay());

        //移除缓存中的数据
        String redisKey = getRedisKey(DateUtil.getDateline());

        this.cache.remove(redisKey);

    }

    @Override
    public void deleteBySeckillId(Integer seckillId) {
        this.daoSupport.execute("delete from es_seckill_apply where seckill_id = ?", seckillId);
    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = {RuntimeException.class, ServiceException.class})
    public boolean addSoldNum(List<PromotionDTO> promotionDTOList) {

        List<SeckillGoodsVO> lockedList = new ArrayList<>();

        boolean result = true;
        //遍历活动与商品关系的类
        for (PromotionDTO promotionDTO : promotionDTOList) {

            try {
                Map<Integer, List<SeckillGoodsVO>> map = this.getSeckillGoodsList();

                for (Map.Entry<Integer, List<SeckillGoodsVO>> entry : map.entrySet()) {

                    List<SeckillGoodsVO> seckillGoodsDTOList = entry.getValue();

                    for (SeckillGoodsVO goodsVO : seckillGoodsDTOList) {

                        if (goodsVO.getGoodsId().equals(promotionDTO.getGoodsId())) {
                            //用户购买的数量
                            int num = promotionDTO.getNum();

                            String sql = "update es_seckill_apply set sold_quantity = sold_quantity-?,sales_num = sales_num +? where goods_id = ? and seckill_id=? and sold_quantity>=?";
                            logger.debug("num is " + num + ";goodsid: " + goodsVO.getGoodsId() + " ; seckill: " + goodsVO.getSeckillId());
                            int rowNum = this.daoSupport.execute(sql, num, num, goodsVO.getGoodsId(), goodsVO.getSeckillId(), num);

                            //库存不足
                            if (rowNum <= 0) {
                                logger.debug("秒杀更新失败");
                                result = false;
                                break;

                            } else {
                                result = true;

                                goodsVO.setSoldNum(num);

                                //记录此商品已经锁成功,以便回滚
                                lockedList.add(goodsVO);
                                logger.debug("秒杀更新成功");

                            }
                        }

                    }

                    //发生锁库失败，则break;
                    if (!result) {
                        break;
                    }

                }

                this.cache.remove(PromotionCacheKeys.getSeckillKey(DateUtil.toString(DateUtil.getDateline(), "yyyyMMdd")));

            } catch (Exception e) {
                e.printStackTrace();
                result = false;
            }
        }

        //如果有锁库失败，则回滚已经更新成功的
        if (!result) {
            innerRollbackStock(lockedList);
        }
        return result;
    }



    @Override
    public Map<Integer, List<SeckillGoodsVO>> getSeckillGoodsList() {

        //读取今天的时间
        long today = DateUtil.startOfTodDay();
        //从缓存读取限时抢购的活动的商品
        String redisKey = getRedisKey(DateUtil.getDateline());
        Map<Integer, List<SeckillGoodsVO>> map = this.cache.getHash(redisKey);

        //如果redis中没有则从数据取
        if (map == null || map.isEmpty()) {

            //读取当天正在进行的活限时抢购活动的商品
            String sql = "select * from es_seckill_apply where start_day = ? and status = ?";
            List<SeckillApplyDO> list = this.daoSupport.queryForList(sql, SeckillApplyDO.class, today, SeckillGoodsApplyStatusEnum.PASS.name());

            //遍历所有的商品，并保存所有不同的时刻
            for (SeckillApplyDO applyDO : list) {
                map.put(applyDO.getTimeLine(), new ArrayList());
            }

            //遍历所有的时刻，并为每个时刻赋值商品
            for (SeckillApplyDO applyDO : list) {
                for (Map.Entry<Integer, List<SeckillGoodsVO>> entry : map.entrySet()) {
                    if (applyDO.getTimeLine().equals(entry.getKey())) {

                        //活动开始日期（天）的时间戳
                        long startDay = applyDO.getStartDay();
                        //形成 2018090910 这样的串
                        String timeStr = DateUtil.toString(startDay, "yyyyMMdd") + applyDO.getTimeLine();
                        //得到开始日期的时间戳
                        long startTime = DateUtil.getDateline(timeStr, "yyyyMMddHH");

                        //查询商品
                        CacheGoods goods = goodsClient.getFromCache(applyDO.getGoodsId());
                        SeckillGoodsVO seckillGoods = new SeckillGoodsVO();
                        seckillGoods.setGoodsId(goods.getGoodsId());
                        seckillGoods.setGoodsName(goods.getGoodsName());
                        seckillGoods.setOriginalPrice(goods.getPrice());
                        seckillGoods.setSeckillPrice(applyDO.getPrice());
                        seckillGoods.setSoldNum(applyDO.getSalesNum());
                        seckillGoods.setSoldQuantity(applyDO.getSoldQuantity());
                        seckillGoods.setGoodsImage(goods.getThumbnail());
                        seckillGoods.setStartTime(startTime);
                        seckillGoods.setSeckillId(applyDO.getSeckillId());
                        seckillGoods.setRemainQuantity(applyDO.getSoldQuantity() - applyDO.getSalesNum());

                        if (entry.getValue() == null) {
                            entry.setValue(new ArrayList<>());
                        }
                        entry.getValue().add(seckillGoods);
                    }
                }
            }

            //压入缓存
            for (Map.Entry<Integer, List<SeckillGoodsVO>> entry : map.entrySet()) {
                this.cache.putHash(redisKey, entry.getKey(), entry.getValue());
            }
        }

        return map;
    }

    public static void main(String[] args) {
        //活动开始日期（天）的时间戳
        long startDay = DateUtil.getDateline("20180101000000", "yyyyMMddHHMMss");
        //形成 2018090910 这样的串
        String timeStr = DateUtil.toString(startDay, "yyyyMMdd") + 10;
        //得到开始日期的时间戳
        long startTime = DateUtil.getDateline(timeStr, "yyyyMMddHH");

        String str = DateUtil.toString(startTime, "yyyyMMddHH");
        System.out.println(timeStr);
        System.out.println(str);

    }


    @Override
    public void addRedis(Long startTime, Integer rangeTime, SeckillGoodsVO goodsVO) {
        //得到活动缓存的key
        String redisKey = getRedisKey(startTime);
        //查询活动商品
        List<SeckillGoodsVO> list = (List<SeckillGoodsVO>) this.cache.getHash(redisKey, rangeTime);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(goodsVO);

        //压入缓存
        this.cache.putHash(redisKey, rangeTime, list);
    }


    @Override
    public List getSeckillGoodsList(Integer rangeTime, Integer pageNo, Integer pageSize) {

        //读取限时抢购活动商品
        Map<Integer, List<SeckillGoodsVO>> map = this.getSeckillGoodsList();
        List<SeckillGoodsVO> totalList = new ArrayList();

        //遍历活动商品
        for (Map.Entry<Integer, List<SeckillGoodsVO>> entry : map.entrySet()) {
            if (rangeTime.intValue() == entry.getKey().intValue()) {
                totalList = entry.getValue();
                break;
            }
        }

        //redis不能分页 手动根据分页读取数据
        List<SeckillGoodsVO> list = new ArrayList<SeckillGoodsVO>();
        int currIdx = (pageNo > 1 ? (pageNo - 1) * pageSize : 0);
        for (int i = 0; i < pageSize && i < totalList.size() - currIdx; i++) {
            SeckillGoodsVO goods = totalList.get(currIdx + i);
            list.add(goods);
        }

        return list;
    }


    private Lock getGoodsQuantityLock(Integer gbId) {
        RLock lock = redisson.getLock("seckill_goods_quantity_lock_" + gbId);
        return lock;
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
