package com.enation.app.javashop.core.trade.order.service.impl;

import com.enation.app.javashop.core.base.CachePrefix;
import com.enation.app.javashop.core.client.goods.GoodsClient;
import com.enation.app.javashop.core.client.member.MemberAddressClient;
import com.enation.app.javashop.core.client.member.ShipTemplateClient;
import com.enation.app.javashop.core.goods.model.vo.CacheGoods;
import com.enation.app.javashop.core.goods.model.vo.GoodsSkuVO;
import com.enation.app.javashop.core.member.model.dos.MemberAddress;
import com.enation.app.javashop.core.shop.model.dto.ShipTemplateChildDTO;
import com.enation.app.javashop.core.shop.model.vo.ShipTemplateChildBuyerVO;
import com.enation.app.javashop.core.shop.model.vo.ShipTemplateVO;
import com.enation.app.javashop.core.trade.cart.model.vo.CartSkuVO;
import com.enation.app.javashop.core.trade.cart.model.vo.CartVO;
import com.enation.app.javashop.core.trade.cart.model.vo.PromotionRule;
import com.enation.app.javashop.core.trade.order.service.CheckoutParamManager;
import com.enation.app.javashop.core.trade.order.service.ShippingManager;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.security.model.Buyer;
import com.enation.app.javashop.framework.util.CurrencyUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.*;

/**
 * 运费计算业务层实现类
 *
 * @author Snow create in 2018/4/8
 * @version v2.0
 * @since v7.0.0
 */
@Service
public class ShippingManagerImpl implements ShippingManager {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private ShipTemplateClient shipTemplateClient;

    @Autowired
    private MemberAddressClient memberAddressClient;

    @Autowired
    private CheckoutParamManager checkoutParamManager;

    @Autowired
    private Cache cache;

    @Override
    public void setShippingPrice(List<CartVO> cartList) {
        //获取会员选中的地址信息
        MemberAddress address = memberAddressClient.getModel(checkoutParamManager.getParam().getAddressId());
        Buyer buyer = UserContext.getBuyer();
        if (address == null || !address.getMemberId().equals(buyer.getUid())) {
            return;
        }
        Integer areaId = address.actualAddress();

        // 检测不在配送区域的货品
        this.checkArea(cartList, areaId);
        //存储各个商家的运费价格
        Map<Integer, Double> shipPrice = new HashMap<>();
        //获取各个商家的运费价格
        shipPrice = this.getShippingPrice(cartList, areaId);

        for (CartVO cartVo : cartList) {

            List<PromotionRule> ruleList = cartVo.getRuleList();
            //如果满减慢增免邮则不计算邮费
            if (StringUtil.isNotEmpty(ruleList)) {
                boolean flag = false;
                for (PromotionRule rule : ruleList) {
                    if (rule != null && rule.getFreeShipping()) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    continue;
                }
            }

            // 获取购物车商品运费总计
            double finalShip = shipPrice.get(cartVo.getSellerId());
            if (logger.isDebugEnabled()) {
                logger.debug(cartVo.getSellerName() + " 最终运费金额计算：" + finalShip);

            }
            cartVo.getPrice().setFreightPrice(finalShip);
            if (finalShip > 0) {
                cartVo.getPrice().setIsFreeFreight(0);
            }
            cartVo.setShippingTypeName("运费");
        }


    }

    /**
     * 校验地区
     *
     * @param cartList 购物车
     * @param areaId   地区
     * @return
     */
    @Override
    public List<CacheGoods> checkArea(List<CartVO> cartList, Integer areaId) {
        List<CacheGoods> errorGoods = new ArrayList<>();
        for (CartVO cartVo : cartList) {
            //运费模版映射
            Map<Integer, ShipTemplateChildDTO> shipMap = new HashMap<>(16);
            List<CartSkuVO> cartSkuVOS = cartVo.getSkuList();
            for (CartSkuVO skuVO : cartSkuVOS) {
                // 未选中则先不处理
                if (skuVO.getChecked() == 0) {
                    continue;
                }
                // 不免运费
                if (skuVO.getIsFreeFreight() != 1) {
                    skuVO.setIsShip(1);
                    // 获取运费模板信息 没有运费模版的话 记录错误的商品，禁止下单
                    ShipTemplateVO temp = this.shipTemplateClient.get(skuVO.getTemplateId());
                    //如果模版空
                    if (temp == null) {
                        errorGoods.add(goodsClient.getFromCache(skuVO.getGoodsId()));
                        skuVO.setIsShip(0);
                    } else {

                        for (ShipTemplateChildBuyerVO child : temp.getItems()) {
                            if (child.getAreaId() != null) {
                                /** 校验地区 */
                                if (child.getAreaId().indexOf("," + areaId + ",") >= 0) {
                                    ShipTemplateChildDTO dto = new ShipTemplateChildDTO(child);
                                    dto.setType(temp.getType());
                                    shipMap.put(skuVO.getSkuId(), dto);
                                }
                            }
                        }
                        // 如果没有匹配 则当
                        if (!shipMap.containsKey(skuVO.getSkuId())) {
                            errorGoods.add(goodsClient.getFromCache(skuVO.getGoodsId()));
                            skuVO.setIsShip(0);
                        }
                    }
                } else {
                    //如果没有设置运费模版 则默认地区有货
                    skuVO.setIsShip(1);
                }
            }
            cartVo.setShipTemplateChildMap(shipMap);
        }
        return errorGoods;
    }


    /**
     * 获取每个商家的运费
     *
     * @param cartList 购物车列表
     * @return 每一个商家的运费 key为商家id value为运费
     */
    @Override
    public Map<Integer, Double> getShippingPrice(List<CartVO> cartList, Integer areaId) {
        //用来存储每一个sku的数量
        Map<Integer, Integer> skuNum = new HashMap<>();
        //循环购物车获取所有sku中的key集合
        List<String> keyList = new ArrayList<>();
        //用来存储每一个商家的原始0.0运费价格，最后要和每一个商家的带运费模板的价格进行合并
        Map<Integer, Double> primaryShipPrice = new HashMap<>();
        for (CartVO cartVO : cartList) {
            List<CartSkuVO> CartSkuVOs = cartVO.getSkuList();
            primaryShipPrice.put(cartVO.getSellerId(), 0.0);
            for (CartSkuVO cartSkuVO : CartSkuVOs) {
                //只计算选中的商品
                if (cartSkuVO.getChecked() == 0) {
                    continue;
                }
                //如果没有绑定运费模板则返回
                if (cartSkuVO.getTemplateId().equals(0)) {
                    continue;
                }
                skuNum.put(cartSkuVO.getSkuId(), cartSkuVO.getNum());
                keyList.add(CachePrefix.SKU.getPrefix() + cartSkuVO.getSkuId());
            }
        }

        if (keyList.size() > 0) {
            //批量从缓存中读取脚本
            List<GoodsSkuVO> goodsSkuVOS = cache.multiGet(keyList);
            //根据模板id进行分组,查询出所有用到相同模板的sku
            Map<Integer, List<GoodsSkuVO>> shipTemplateGroup = this.getShipTemplateGroup(goodsSkuVOS);
            Iterator iter = shipTemplateGroup.values().iterator();
            while (iter.hasNext()) {
                //用来记录价格
                Double price = 0.0;
                List<GoodsSkuVO> sList = (List<GoodsSkuVO>) iter.next();
                //计算总重量
                Double goodsWeigth = getSkuWeight(sList, skuNum);
                //计算总数量
                Integer goodsNum = getSkuNum(sList, skuNum);
                //调用脚本引擎计算价格
                ScriptEngineManager manager = new ScriptEngineManager();
                ScriptEngine engine = manager.getEngineByName("javascript");
                //设置常量，重量
                engine.put("$goodsWeight", goodsWeigth);
                //设置常量，数量
                engine.put("$goodsNum", goodsNum);
                //设置常量，地址
                engine.put("$address", areaId);
                List<String> scripts = sList.get(0).getScripts();
                //获取运费价格
                try {
                    for (String script : scripts) {
                        String jsFunc = script;
                        engine.eval(jsFunc);
                        Invocable invocable = (Invocable) engine;
                        Object shipPrice = invocable.invokeFunction("getShipPrice");
                        if (shipPrice != null) {
                            price = CurrencyUtil.add(price, (double) shipPrice);
                        }
                    }
                } catch (Exception e) {
                    logger.error("运费计算发生错误=" + e.getMessage());
                }
                //获取商家id
                Integer sellerId = sList.get(0).getSellerId();
                Double shipPrice = primaryShipPrice.get(sellerId);
                //如果是同一个商家的运费让他们相加
                if (!shipPrice.equals(0.0)) {
                    shipPrice = CurrencyUtil.add(shipPrice, price);
                    primaryShipPrice.put(sellerId, shipPrice);
                } else {
                    primaryShipPrice.put(sellerId, price);
                }
            }
        }
        return primaryShipPrice;
    }

    /**
     * 根据模板id进行分组,查询出所有用到相同模板的sku
     *
     * @param goodsSkuVOS
     * @return
     */
    private Map<Integer, List<GoodsSkuVO>> getShipTemplateGroup(List<GoodsSkuVO> goodsSkuVOS) {
        Map<Integer, List<GoodsSkuVO>> group = new HashMap<>();
        for (GoodsSkuVO goodsSkuVO : goodsSkuVOS) {
            //重新组织数据，把相同模板的sku组合，方便计算运费
            List<GoodsSkuVO> newGoodsSku = group.get(goodsSkuVO.getTemplateId());
            if (newGoodsSku == null) {
                newGoodsSku = new ArrayList<>();
            }
            newGoodsSku.add(goodsSkuVO);
            group.put(goodsSkuVO.getTemplateId(), newGoodsSku);

        }
        return group;
    }

    /**
     * 获取相同模板下的sku总重
     *
     * @param sList  相同模板下的所有sku
     * @param skuNum 记录购买数
     * @return 总重
     */
    private Double getSkuWeight(List<GoodsSkuVO> sList, Map<Integer, Integer> skuNum) {
        double weight = 0.0;
        for (GoodsSkuVO goodsSkuVO : sList) {
            weight = CurrencyUtil.add(weight, CurrencyUtil.mul(goodsSkuVO.getWeight(), skuNum.get(goodsSkuVO.getSkuId())));
        }
        return weight;
    }

    /**
     * 获取相同模板下的sku总数量
     *
     * @param sList  相同模板下的所有sku
     * @param skuNum 记录购买数
     * @return 总数
     */
    private Integer getSkuNum(List<GoodsSkuVO> sList, Map<Integer, Integer> skuNum) {
        double num = 0.0;
        for (GoodsSkuVO goodsSkuVO : sList) {
            num = CurrencyUtil.add(num, skuNum.get(goodsSkuVO.getSkuId()));
        }
        return (int) num;
    }
}
