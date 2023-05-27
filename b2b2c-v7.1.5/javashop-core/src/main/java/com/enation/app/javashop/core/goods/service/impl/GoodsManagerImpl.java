package com.enation.app.javashop.core.goods.service.impl;

import com.enation.app.javashop.core.base.rabbitmq.AmqpExchange;
import com.enation.app.javashop.core.base.CachePrefix;
import com.enation.app.javashop.core.base.SettingGroup;
import com.enation.app.javashop.core.base.message.GoodsChangeMsg;
import com.enation.app.javashop.core.client.member.MemberCommentClient;
import com.enation.app.javashop.core.client.member.ShopClient;
import com.enation.app.javashop.core.client.system.SettingClient;
import com.enation.app.javashop.core.client.trade.ExchangeGoodsClient;
import com.enation.app.javashop.core.goods.GoodsErrorCode;
import com.enation.app.javashop.core.goods.model.dos.GoodsDO;
import com.enation.app.javashop.core.goods.model.dos.GoodsGalleryDO;
import com.enation.app.javashop.core.goods.model.dos.GoodsSkuDO;
import com.enation.app.javashop.core.goods.model.dto.ExchangeClientDTO;
import com.enation.app.javashop.core.goods.model.dto.GoodsAuditParam;
import com.enation.app.javashop.core.goods.model.dto.GoodsDTO;
import com.enation.app.javashop.core.goods.model.dto.GoodsSettingVO;
import com.enation.app.javashop.core.goods.model.enums.GoodsOperate;
import com.enation.app.javashop.core.goods.model.enums.GoodsType;
import com.enation.app.javashop.core.goods.model.enums.Permission;
import com.enation.app.javashop.core.goods.model.vo.BackendGoodsVO;
import com.enation.app.javashop.core.goods.model.vo.GoodsSkuVO;
import com.enation.app.javashop.core.goods.model.vo.OperateAllowable;
import com.enation.app.javashop.core.goods.service.*;
import com.enation.app.javashop.core.member.model.vo.GoodsGrade;
import com.enation.app.javashop.core.promotion.PromotionErrorCode;
import com.enation.app.javashop.core.promotion.exchange.model.dos.ExchangeDO;
import com.enation.app.javashop.core.promotion.tool.model.dto.PromotionGoodsDTO;
import com.enation.app.javashop.core.shop.model.vo.ShopVO;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.context.ThreadContextHolder;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.redis.transactional.RedisTransactional;
import com.enation.app.javashop.framework.security.model.Seller;
import com.enation.app.javashop.framework.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品业务类
 *
 * @author fk
 * @version v2.0
 * @since v7.0.0 2018-03-21 11:23:10
 */
@Service
public class GoodsManagerImpl implements GoodsManager {
    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    @Qualifier("goodsDaoSupport")
    private DaoSupport daoSupport;
    @Autowired
    private GoodsGalleryManager goodsGalleryManager;
    @Autowired
    private GoodsParamsManager goodsParamsManager;
    @Autowired
    private GoodsSkuManager goodsSkuManager;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private Cache cache;

    @Autowired
    private GoodsQueryManager goodsQueryManager;
    @Autowired
    private SettingClient settingClient;
    @Autowired
    private ExchangeGoodsClient exchangeGoodsClient;
    @Autowired
    private MemberCommentClient memberCommentClient;
    @Autowired
    private ShopClient shopClient;


    @Override
    @Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public GoodsDO add(GoodsDTO goodsVo) {

        Seller seller = UserContext.getSeller();
        // 没有规格给这个字段塞0
        goodsVo.setHaveSpec(StringUtil.isNotEmpty(goodsVo.getSkuList()) ? 1 : 0);

        GoodsDO goods = new GoodsDO(goodsVo);
        // 判断是否添加的是积分商品
        if (goodsVo.getExchange() != null && goodsVo.getExchange().getEnableExchange() == 1) {
            if (seller.getSelfOperated() != 1) {
                throw new ServiceException(GoodsErrorCode.E301.code(), "非自营店铺不能添加积分商品");
            }
            goods.setGoodsType(GoodsType.POINT.name());
        } else {
            goods.setGoodsType(GoodsType.NORMAL.name());
        }
        // 判断是否是自营店铺
        goods.setSelfOperated(seller.getSelfOperated() == 1 ? 1 : 0);
        String goodsSettingJson = settingClient.get(SettingGroup.GOODS);

        GoodsSettingVO goodsSetting = JsonUtil.jsonToObject(goodsSettingJson, GoodsSettingVO.class);
        // 判断商品是否需要审核
        goods.setIsAuth(goodsSetting.getMarcketAuth() == 1 ? 0 : 1);
        // 商品状态 是否可用
        goods.setDisabled(1);
        // 商品创建时间
        goods.setCreateTime(DateUtil.getDateline());
        // 商品浏览次数
        goods.setViewCount(0);
        // 商品购买数量
        goods.setBuyCount(0);
        // 评论次数
        goods.setCommentNum(0);
        // 商品评分
        goods.setGrade(100.0);
        // 商品最后更新时间
        goods.setLastModify(DateUtil.getDateline());
        // 商品库存
        goods.setQuantity(goodsVo.getQuantity() == null ? 0 : goodsVo.getQuantity());
        // 可用库存
        goods.setEnableQuantity(goods.getQuantity());
        // 向goods加入图片
        GoodsGalleryDO goodsGalley = goodsGalleryManager
                .getGoodsGallery(goodsVo.getGoodsGalleryList().get(0).getOriginal());
        goods.setOriginal(goodsGalley.getOriginal());
        goods.setBig(goodsGalley.getBig());
        goods.setSmall(goodsGalley.getSmall());
        goods.setThumbnail(goodsGalley.getThumbnail());
        goods.setSellerId(seller.getSellerId());
        goods.setSellerName(seller.getSellerName());
        //如果有规格，则将规格中最低的价格赋值到商品价格中 update by liuyulei  2019-05-21
        if (goods.getHaveSpec() == 1) {
            pushGoodsPrice(goodsVo, goods);


        }
        // 添加商品
        this.daoSupport.insert(goods);
        // 获取添加商品的商品ID
        Integer goodsId = this.daoSupport.getLastId("es_goods");
        goods.setGoodsId(goodsId);
        // 添加商品参数
        this.goodsParamsManager.addParams(goodsVo.getGoodsParamsList(), goodsId);
        // 添加商品sku信息
        this.goodsSkuManager.add(goodsVo.getSkuList(), goods);
        // 添加相册
        this.goodsGalleryManager.add(goodsVo.getGoodsGalleryList(), goodsId);
        // 添加积分换购商品
        if (goods.getGoodsType().equals(GoodsType.POINT.name())) {
            PromotionGoodsDTO goodsDTO = new PromotionGoodsDTO();
            BeanUtils.copyProperties(goods, goodsDTO);
            ExchangeDO exchange = new ExchangeDO();
            BeanUtils.copyProperties(goodsVo.getExchange(), exchange);
            //校验积分兑换的价格不能高于商品销售价
            if (exchange.getExchangeMoney() > goods.getPrice()) {
                throw new ServiceException(GoodsErrorCode.E301.code(), "积分商品价格不能高于商品原价");
            }
            exchangeGoodsClient.add(new ExchangeClientDTO(exchange, goodsDTO));
        }
        // 发送增加商品消息，店铺增加自身商品数量，静态页使用
        GoodsChangeMsg goodsChangeMsg = new GoodsChangeMsg(new Integer[]{goods.getGoodsId()},
                GoodsChangeMsg.ADD_OPERATION);
        this.amqpTemplate.convertAndSend(AmqpExchange.GOODS_CHANGE, AmqpExchange.GOODS_CHANGE + "_ROUTING", goodsChangeMsg);

        return goods;
    }

    @Override
    @Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public GoodsDO edit(GoodsDTO goodsVO, Integer id) {

        Seller seller = UserContext.getSeller();
        GoodsDO goodsDO = goodsQueryManager.getModel(id);
        if (goodsDO == null || !goodsDO.getSellerId().equals(seller.getSellerId())) {
            throw new ServiceException(GoodsErrorCode.E301.code(), "没有操作权限");
        }

        goodsVO.setGoodsId(id);
        GoodsDO goods = new GoodsDO(goodsVO);
        // 判断是否把商品修改成积分商品,自营店
        if (seller.getSelfOperated() == 1) {
            goods.setGoodsType(goodsVO.getExchange() != null && goodsVO.getExchange().getEnableExchange() == 1 ? GoodsType.POINT.name() : GoodsType.NORMAL.name());
        } else {
            goods.setGoodsType(GoodsType.NORMAL.name());
        }

        String goodsSettingJson = settingClient.get(SettingGroup.GOODS);

        GoodsSettingVO goodsSetting = JsonUtil.jsonToObject(goodsSettingJson, GoodsSettingVO.class);
        // 判断商品修改是否需要审核
        goods.setIsAuth(goodsSetting.getUpdateAuth() == 1 ? 0 : 1);

        // 添加商品更新时间
        goods.setLastModify(DateUtil.getDateline());
        // 修改相册信息
        List<GoodsGalleryDO> goodsGalleys = goodsVO.getGoodsGalleryList();
        this.goodsGalleryManager.edit(goodsGalleys, goodsVO.getGoodsId());
        // 向goods加入图片
        goods.setOriginal(goodsGalleys.get(0).getOriginal());
        goods.setBig(goodsGalleys.get(0).getBig());
        goods.setSmall(goodsGalleys.get(0).getSmall());
        goods.setThumbnail(goodsGalleys.get(0).getThumbnail());
        goods.setSellerId(seller.getSellerId());
        goods.setSellerName(seller.getSellerName());

        //如果有规格，则将规格中最低的价格赋值到商品价格中 update by liuyulei  2019-05-21
        if (StringUtil.isNotEmpty(goodsVO.getSkuList())) {
            pushGoodsPrice(goodsVO, goods);
        }

        // 更新商品
        this.daoSupport.update(goods, id);
        // 如果商品品牌为空，更新商品品牌为空
        if (goods.getBrandId() == null){
            this.daoSupport.execute("update es_goods set brand_id = null where goods_id = ?",id);
        }
        // 处理参数信息
        this.goodsParamsManager.addParams(goodsVO.getGoodsParamsList(), id);
        // 处理规格信息
        this.goodsSkuManager.edit(goodsVO.getSkuList(), goods);
        // 添加商品的积分换购信息
        PromotionGoodsDTO goodsDTO = new PromotionGoodsDTO();
        BeanUtils.copyProperties(goods, goodsDTO);
        if (goodsVO.getExchange() == null || GoodsType.NORMAL.name().equals(goods.getGoodsType())) {
            exchangeGoodsClient.edit(new ExchangeClientDTO(null, goodsDTO));
        } else {
            ExchangeDO exchange = new ExchangeDO();
            BeanUtils.copyProperties(goodsVO.getExchange(), exchange);
            if (exchange.getExchangeMoney() > goods.getPrice()) {
                throw new ServiceException(GoodsErrorCode.E301.code(), "积分商品价格不能高于商品原价");
            }
            exchangeGoodsClient.edit(new ExchangeClientDTO(exchange, goodsDTO));
        }

        //清除该商品关联的东西
        this.cleanGoodsAssociated(id, goodsVO.getMarketEnable());


        // 发送增加商品消息，店铺增加自身商品数量，静态页使用
        GoodsChangeMsg goodsChangeMsg = new GoodsChangeMsg(new Integer[]{id}, GoodsChangeMsg.UPDATE_OPERATION);
        //修改商品时需要删除商品参与的促销活动
        goodsChangeMsg.setDelPromotion(true);
        this.amqpTemplate.convertAndSend(AmqpExchange.GOODS_CHANGE, AmqpExchange.GOODS_CHANGE + "_ROUTING", goodsChangeMsg);

        return goods;
    }

    private void pushGoodsPrice(GoodsDTO goodsVO, GoodsDO goods) {
        GoodsSkuVO sku = goodsVO.getSkuList().get(0);
        goods.setPrice(sku.getPrice());
        goods.setCost(sku.getCost());
        goods.setWeight(sku.getWeight());

        goodsVO.getSkuList().forEach(skuVo -> {
            if (skuVo.getPrice() < goods.getPrice()) {
                goods.setPrice(skuVo.getPrice());
                goods.setCost(skuVo.getCost());
                goods.setWeight(skuVo.getWeight());
            }
        });
    }


    /**
     * 清除商品的关联<br/>
     * 在商品删除、下架要进行调用
     *
     * @param goodsId
     */
    private void cleanGoodsAssociated(int goodsId, Integer markenable) {

        if (logger.isDebugEnabled()) {
            logger.debug("清除goodsid[" + goodsId + "]相关的缓存，包括促销的缓存");
        }

        this.cache.remove(CachePrefix.GOODS.getPrefix() + goodsId);

        // 删除这个商品的sku缓存(必须要在删除库中sku前先删缓存),首先查出商品对应的sku_id
        String sql = "select sku_id from es_goods_sku where goods_id = ?";
        List<Map> skuIds = this.daoSupport.queryForList(sql, goodsId);
        for (Map map : skuIds) {
            cache.remove(CachePrefix.SKU.getPrefix() + map.get("sku_id"));
        }

        //不再读一次缓存竟然清不掉？？所以在这里又读了一下
        this.cache.get(CachePrefix.GOODS.getPrefix() + goodsId);

        //删除该商品关联的活动缓存
        long currTime = DateUtil.getDateline();
        String currDate = DateUtil.toString(currTime, "yyyyMMdd");

        //清除此商品的缓存
        this.cache.remove(CachePrefix.PROMOTION_KEY + currDate + goodsId);

        if (markenable == 0) {
            this.deleteExchange(goodsId);
        }

    }


    /**
     * 删除积分商品
     *
     * @param goodsId
     */
    private void deleteExchange(Integer goodsId) {

        //删除积分商品
        exchangeGoodsClient.del(goodsId);
    }

    @Override
    @RedisTransactional
    @Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void under(Integer[] goodsIds, String reason, Permission permission) {

        if (reason.length() > 500){
            throw new ServiceException(PromotionErrorCode.E400.code(), "下架原因长度不能超过500个字符");
        }
        List<Object> term = new ArrayList<>();
        String idStr = SqlUtil.getInSql(goodsIds, term);

        if (Permission.SELLER.equals(permission)) {
            this.checkPermission(goodsIds, GoodsOperate.UNDER);
            Seller seller = UserContext.getSeller();
            reason = "店员" + seller.getUsername() + "下架，原因为：" + reason;
        } else {
            //查看是否是不能下架的状态
            String sql = "select disabled,market_enable from es_goods where goods_id in (" + idStr + ")";
            List<Map> list = this.daoSupport.queryForList(sql, term.toArray());
            for (Map map : list) {
                Integer disabled = (Integer) map.get("disabled");
                Integer marketEnable = (Integer) map.get("market_enable");
                OperateAllowable operateAllowable = new OperateAllowable(marketEnable, disabled);
                if (!operateAllowable.getAllowUnder()) {
                    throw new ServiceException(GoodsErrorCode.E301.code(), "存在不能下架的商品，不能操作");
                }

            }
            reason = "平台下架，原因为：" + reason;
        }

        term.add(0, reason);
        term.add(1, DateUtil.getDateline());
        String sql = "update es_goods set market_enable = 0,under_message = ?, last_modify=?  where goods_id in (" + idStr + ")";
        this.daoSupport.execute(sql, term.toArray());

        //清除相关的关联
        for (int goodsId : goodsIds) {
            this.cleanGoodsAssociated(goodsId, 0);
        }

        GoodsChangeMsg goodsChangeMsg = new GoodsChangeMsg(goodsIds, GoodsChangeMsg.UNDER_OPERATION, reason);
        this.amqpTemplate.convertAndSend(AmqpExchange.GOODS_CHANGE, AmqpExchange.GOODS_CHANGE + "_ROUTING", goodsChangeMsg);

    }

    @Override
    public void inRecycle(Integer[] goodsIds) {

        this.checkPermission(goodsIds, GoodsOperate.RECYCLE);

        List<Object> term = new ArrayList<>();
        //修改最后修改时间
        term.add(DateUtil.getDateline());
        String idStr = getIdStr(goodsIds, term);
        String sql = "update  es_goods set disabled = 0 ,market_enable=0 , last_modify=?  where goods_id in (" + idStr + ")";
        this.daoSupport.execute(sql, term.toArray());

        //清除相关的关联
        for (int goodsId : goodsIds) {
            this.cleanGoodsAssociated(goodsId, 0);
        }

        GoodsChangeMsg goodsChangeMsg = new GoodsChangeMsg(goodsIds, GoodsChangeMsg.INRECYCLE_OPERATION);
        this.amqpTemplate.convertAndSend(AmqpExchange.GOODS_CHANGE, AmqpExchange.GOODS_CHANGE + "_ROUTING", goodsChangeMsg);

    }

    @Override
    @RedisTransactional
    @Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delete(Integer[] goodsIds) {

        this.checkPermission(goodsIds, GoodsOperate.DELETE);

        List<Object> term = new ArrayList<>();
        String idStr = getIdStr(goodsIds, term);
//        String sql = "delete  from es_goods  where goods_id in (" + idStr + ")";
        //修改删除商品的逻辑不做物理删除 add by liuyulei 2019-06-03
        String sql = "update es_goods set disabled = -1  where goods_id in (" + idStr + ")";
        this.daoSupport.execute(sql, term.toArray());
//        // 删除商品sku信息
//        this.goodsSkuManager.delete(goodsIds);
//        // 删除相册信息
//        this.goodsGalleryManager.delete(goodsIds);
        //删除商品发送商品删除消息DEL_OPERATION
        GoodsChangeMsg goodsChangeMsg = new GoodsChangeMsg(goodsIds, GoodsChangeMsg.DEL_OPERATION);
        this.amqpTemplate.convertAndSend(AmqpExchange.GOODS_CHANGE, AmqpExchange.GOODS_CHANGE + "_ROUTING", goodsChangeMsg);
    }

    /**
     * 查看商品是否属于当前登录用户
     *
     * @param goodsIds
     */
    private void checkPermission(Integer[] goodsIds, GoodsOperate goodsOperate) {

        Seller seller = UserContext.getSeller();
        List<Object> term = new ArrayList<>();
        String idStr = SqlUtil.getInSql(goodsIds, term);

        String sql = "select disabled,market_enable from es_goods where goods_id in (" + idStr + ") and seller_id = ?";
        term.add(seller.getSellerId());
        List<Map> list = this.daoSupport.queryForList(sql, term.toArray());
        if (list.size() != goodsIds.length) {
            throw new ServiceException(GoodsErrorCode.E301.code(), "存在不属于您的商品，不能操作");
        }

        for (Map map : list) {
            Integer disabled = (Integer) map.get("disabled");
            Integer marketEnable = (Integer) map.get("market_enable");
            OperateAllowable operateAllowable = new OperateAllowable(marketEnable, disabled);
            switch (goodsOperate) {
                case DELETE:
                    if (!operateAllowable.getAllowDelete()) {
                        throw new ServiceException(GoodsErrorCode.E301.code(), "存在不能删除的商品，不能操作");
                    }
                    break;
                case RECYCLE:
                    if (!operateAllowable.getAllowRecycle()) {
                        throw new ServiceException(GoodsErrorCode.E301.code(), "存在不能放入回收站的商品，不能操作");
                    }
                    break;
                case REVRET:
                    if (!operateAllowable.getAllowRevert()) {
                        throw new ServiceException(GoodsErrorCode.E301.code(), "存在不能还原的商品，不能操作");
                    }
                    break;
                case UNDER:
                    if (!operateAllowable.getAllowUnder()) {
                        throw new ServiceException(GoodsErrorCode.E301.code(), "存在不能下架的商品，不能操作");
                    }
                    break;
                default:
                    break;
            }
        }

    }

    /**
     * 获取商品id的拼接，且删除缓存中的商品信息
     *
     * @param goodsIds
     * @param term
     * @return
     */
    private String getIdStr(Integer[] goodsIds, List<Object> term) {

        String[] goods = new String[goodsIds.length];
        for (int i = 0; i < goodsIds.length; i++) {
            goods[i] = "?";
            term.add(goodsIds[i]);

        }

        return StringUtil.arrayToString(goods, ",");
    }

    @Override
    public void revert(Integer[] goodsIds) {

        this.checkPermission(goodsIds, GoodsOperate.REVRET);
        List<Object> term = new ArrayList<>();
        String sql = "update  es_goods set disabled = 1  where goods_id in (" + getIdStr(goodsIds, term) + ")";
        this.daoSupport.execute(sql, term.toArray());

        GoodsChangeMsg goodsChangeMsg = new GoodsChangeMsg(goodsIds, GoodsChangeMsg.REVERT_OPERATION);
        this.amqpTemplate.convertAndSend(AmqpExchange.GOODS_CHANGE, AmqpExchange.GOODS_CHANGE + "_ROUTING", goodsChangeMsg);

    }

    @Override
    public void up(Integer goodsId) {

        //查看是否允许上架
        String sql = "select disabled,market_enable,seller_id from es_goods where goods_id = ?";
        Map map = this.daoSupport.queryForMap(sql, goodsId);

        Integer disabled = (Integer) map.get("disabled");
        Integer marketEnable = (Integer) map.get("market_enable");
        //商品所属店铺id
        Integer sellerId = (Integer) map.get("seller_id");
        //查询店铺是否是关闭中，若未开启，则不能上架
        ShopVO shop = shopClient.getShop(sellerId);
        if (shop == null || !"OPEN".equals(shop.getShopDisable())) {
            throw new ServiceException(GoodsErrorCode.E301.code(), "店铺关闭中,商品不能上架操作");
        }

        OperateAllowable operateAllowable = new OperateAllowable(marketEnable, disabled);
        if (!operateAllowable.getAllowMarket()) {
            throw new ServiceException(GoodsErrorCode.E301.code(), "商品不能上架操作");
        }

        sql = "update es_goods set market_enable = 1 and disabled = 1 where goods_id  = ?";
        this.daoSupport.execute(sql, goodsId);

        cache.remove(CachePrefix.GOODS.getPrefix() + goodsId);

        GoodsChangeMsg goodsChangeMsg = new GoodsChangeMsg(new Integer[]{goodsId}, GoodsChangeMsg.UPDATE_OPERATION);
        this.amqpTemplate.convertAndSend(AmqpExchange.GOODS_CHANGE, AmqpExchange.GOODS_CHANGE + "_ROUTING", goodsChangeMsg);
    }

    @Override
    public void batchAuditGoods(GoodsAuditParam param) {
        if (param.getGoodsIds() == null || param.getGoodsIds().length == 0) {
            throw new ServiceException(GoodsErrorCode.E301.code(), "请选择要审核的商品");
        }

        if (param.getPass() == null) {
            throw new ServiceException(GoodsErrorCode.E301.code(), "审核状态值不能为空");
        }

        if (param.getPass().intValue() != 0 && param.getPass().intValue() != 1) {
            throw new ServiceException(GoodsErrorCode.E301.code(), "审核状态值不正确");
        }

        if (param.getPass().intValue() == 0) {
            if (StringUtil.isEmpty(param.getMessage())) {
                throw new ServiceException(GoodsErrorCode.E301.code(), "拒绝原因不能为空");
            }

            if (param.getMessage().length() > 200) {
                throw new ServiceException(GoodsErrorCode.E301.code(), "拒绝原因不能超过200个字符");
            }
        }

        for (Integer goodsId : param.getGoodsIds()) {
            GoodsDO goods = this.goodsQueryManager.getModel(goodsId);
            if (goods == null) {
                throw new ServiceException(GoodsErrorCode.E301.code(), "要审核的商品【"+goods.getGoodsName()+"】不存在");
            }

            if (goods.getIsAuth() != 0) {
                throw new ServiceException(GoodsErrorCode.E301.code(), "商品【"+goods.getGoodsName()+"】已审核，请勿重复审核");
            }

            String sql = "update es_goods set is_auth=?,auth_message=?  where goods_id=? ";
            // 审核通过
            daoSupport.execute(sql, param.getPass() == 1 ? 1 : 2, param.getMessage(), goodsId);
            // 发送审核消息
            GoodsChangeMsg goodsChangeMsg = null;
            if (param.getPass() == 1) {
                goodsChangeMsg = new GoodsChangeMsg(new Integer[]{goodsId}, GoodsChangeMsg.GOODS_VERIFY_SUCCESS,
                        param.getMessage());
            } else {
                goodsChangeMsg = new GoodsChangeMsg(new Integer[]{goodsId}, GoodsChangeMsg.GOODS_VERIFY_FAIL,
                        param.getMessage());
            }
            this.amqpTemplate.convertAndSend(AmqpExchange.GOODS_CHANGE, AmqpExchange.GOODS_CHANGE + "_ROUTING", goodsChangeMsg);
            // 清楚商品的缓存
            cache.remove(CachePrefix.GOODS.getPrefix() + goodsId);
        }
    }

    @Override
    @Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Integer visitedGoodsNum(Integer goodsId) {
        /**
         * 逻辑：判断当前缓存中的viewedGoods变量是否包含了当前goods，如未，则 向变量中加入goods，如果已经存在则
         * 1、从redis中取出此商品的浏览数 2、如果为空则新增此商品浏览数为1，否则判断浏览数是否大于等于50，逻辑为每50次存入数据库 3、存入相关数据库
         */
        HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
        List<Integer> visitedGoods = (List<Integer>) cache.get(CachePrefix.VISIT_COUNT.getPrefix() + session.getId());
        // 标识此会话是否访问过
        boolean visited = false;
        if (visitedGoods == null) {
            visitedGoods = new ArrayList<Integer>();
        }

        if (visitedGoods.contains(goodsId)) {
            visited = true;
            visitedGoods.remove(goodsId);
        }
        visitedGoods.add(0, goodsId);

        String key = CachePrefix.VISIT_COUNT.getPrefix() + goodsId;
        /** 获取redis中此商品的浏览数 */
        Integer num = (Integer) this.cache.get(key);
        /** 如果redis中未记录此浏览数 则新增此商品浏览数为1 */
        if (num == null) {
            num = 0;
        }

        if (!visited) {

            num++;
            // 如果浏览数大于等于50则存入相关数据库
            if (num >= 50) {
                // num为历史访问量，如果满足条件需要将此次浏览数也要加进去 固+1
                this.daoSupport.execute("update es_goods set view_count = view_count + ? where goods_id = ?", num, goodsId);
                num = 0;
            }
            this.cache.put(key, num);
        }

        return num;
    }

    /**
     * 获取新赠商品
     *
     * @param length
     * @return
     */
    @Override
    public List<BackendGoodsVO> newGoods(Integer length) {
        return this.daoSupport.queryForList("select * from es_goods where market_enable = 1 and disabled = 1 order by create_time desc limit 0, ?", BackendGoodsVO.class, length);
    }


    @Override
    public void underShopGoods(Integer sellerId) {

        String sql = "update es_goods set market_enable = 0 where seller_id = ? ";
        this.daoSupport.execute(sql, sellerId);
        //发送商品下架消息
        sql = "select goods_id from es_goods where seller_id = ?";
        List<Map> list = this.daoSupport.queryForList(sql, sellerId);
        Integer[] goodsIds = new Integer[list.size()];
        int i = 0;
        if (StringUtil.isNotEmpty(list)) {
            for (Map map : list) {
                goodsIds[i] = StringUtil.toInt(map.get("goods_id").toString(), false);
                i++;
            }

            GoodsChangeMsg goodsChangeMsg = new GoodsChangeMsg(goodsIds, GoodsChangeMsg.UNDER_OPERATION, "店铺关闭");
            this.amqpTemplate.convertAndSend(AmqpExchange.GOODS_CHANGE, AmqpExchange.GOODS_CHANGE + "_ROUTING", goodsChangeMsg);

        }

    }

    @Override
    public void updateGoodsGrade() {

        List<GoodsGrade> list = this.memberCommentClient.queryGoodsGrade();

        if (StringUtil.isNotEmpty(list)) {
            for (GoodsGrade goods : list) {
                String updateSql = "update es_goods set grade=? where goods_id=?";
                double grade = CurrencyUtil.mul(goods.getGoodRate(), 100);
                this.daoSupport.execute(updateSql, CurrencyUtil.round(grade, 1), goods.getGoodsId());
                cache.put(CachePrefix.GOODS_GRADE.getPrefix() + goods.getGoodsId(), CurrencyUtil.round(grade, 1));
                // 发送商品消息变化消息
                GoodsChangeMsg goodsChangeMsg = new GoodsChangeMsg(new Integer[]{goods.getGoodsId()},
                        GoodsChangeMsg.UPDATE_OPERATION);
                this.amqpTemplate.convertAndSend(AmqpExchange.GOODS_CHANGE, AmqpExchange.GOODS_CHANGE + "_ROUTING", goodsChangeMsg);
            }
        }


    }

    /**
     * 获取商品是否使用检测的模版
     *
     * @param templateId
     * @return 商品
     */
    @Override
    public GoodsDO checkShipTemplate(Integer templateId) {
        List<GoodsDO> goodsDOS = this.daoSupport.queryForList("select * from es_goods where disabled!= -1 and template_id = ?", GoodsDO.class, templateId);
        if (goodsDOS != null && goodsDOS.size() > 0) {
            return goodsDOS.get(0);
        }
        return null;
    }

    /**
     * 修改某店铺商品店铺名称
     *
     * @param sellerId
     * @param sellerName
     */
    @Override
    public void changeSellerName(Integer sellerId, String sellerName) {
        this.daoSupport.execute("update es_goods set seller_name = ? where seller_id = ?", sellerName, sellerId);
        this.daoSupport.execute("update es_goods_sku set seller_name = ? where seller_id = ?", sellerName, sellerId);

        this.getAll(sellerId).forEach(goodsDO -> {
            this.cache.remove(CachePrefix.GOODS.getPrefix() + goodsDO.getGoodsId());
        });
        this.getAllSku(sellerId).forEach(skuDO -> {
            this.cache.remove(CachePrefix.SKU.getPrefix() + skuDO.getSkuId());
        });
    }


    private List<GoodsDO> getAll(Integer sellerId) {
        return this.daoSupport.queryForList("select * from es_goods where seller_id = ?", GoodsDO.class, sellerId);
    }

    private List<GoodsSkuDO> getAllSku(Integer sellerId) {
        return this.daoSupport.queryForList("select * from es_goods_sku where seller_id = ?", GoodsSkuDO.class, sellerId);
    }

    @Override
    public void updateGoodsType(Integer sellerId, String type) {
        this.daoSupport.execute("update es_goods set goods_type=? where seller_id = ?", type, sellerId);

    }

    @Override
    @Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updatePriority(Integer goodsId, Integer priority) {
        this.daoSupport.execute("update es_goods set priority = ? where goods_id = ? ",priority,goodsId);
        // 发送修改商品优先级消息
        GoodsChangeMsg goodsChangeMsg = new GoodsChangeMsg(new Integer[]{goodsId}, GoodsChangeMsg.GOODS_PRIORITY_CHANGE);
        this.amqpTemplate.convertAndSend(AmqpExchange.GOODS_PRIORITY_CHANGE, AmqpExchange.GOODS_PRIORITY_CHANGE + "_ROUTING", goodsChangeMsg);
    }
}

