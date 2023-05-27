package com.enation.app.javashop.core.shop.service.impl;

import com.enation.app.javashop.core.base.CachePrefix;
import com.enation.app.javashop.core.base.rabbitmq.AmqpExchange;
import com.enation.app.javashop.core.client.goods.GoodsClient;
import com.enation.app.javashop.core.client.system.RegionsClient;
import com.enation.app.javashop.core.goods.model.dos.GoodsDO;
import com.enation.app.javashop.core.goods.model.vo.ShipTemplateMsg;
import com.enation.app.javashop.core.member.model.vo.RegionVO;
import com.enation.app.javashop.core.shop.ShopErrorCode;
import com.enation.app.javashop.core.shop.model.dos.ShipTemplateChild;
import com.enation.app.javashop.core.shop.model.dos.ShipTemplateDO;
import com.enation.app.javashop.core.shop.model.vo.ShipTemplateChildBuyerVO;
import com.enation.app.javashop.core.shop.model.vo.ShipTemplateChildSellerVO;
import com.enation.app.javashop.core.shop.model.vo.ShipTemplateSellerVO;
import com.enation.app.javashop.core.shop.model.vo.ShipTemplateVO;
import com.enation.app.javashop.core.shop.service.ShipTemplateManager;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.util.StringUtil;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 运费模版业务类
 *
 * @author zjp
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-28 21:44:49
 */
@Service
public class ShipTemplateManagerImpl implements ShipTemplateManager {

    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    private Cache cache;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private RegionsClient regionsClient;

    @Autowired
    private AmqpTemplate amqpTemplate;

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ShipTemplateDO save(ShipTemplateSellerVO template) {
        template.setSellerId(UserContext.getSeller().getSellerId());
        ShipTemplateDO t = new ShipTemplateDO();
        BeanUtils.copyProperties(template, t);

        this.daoSupport.insert(t);
        int id = this.daoSupport.getLastId("es_ship_template");
        t.setId(id);
        //保存运费模板子模板
        List<ShipTemplateChildSellerVO> items = template.getItems();

        this.addTemplateChildren(items, id);

        cache.remove(CachePrefix.SHIP_TEMPLATE.getPrefix() + template.getSellerId());

        this.amqpTemplate.convertAndSend(AmqpExchange.SHIP_TEMPLATE_CHANGE, AmqpExchange.SHIP_TEMPLATE_CHANGE + "_ROUTING", new ShipTemplateMsg(id, 1));
        return t;
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ShipTemplateDO edit(ShipTemplateSellerVO template) {
        template.setSellerId(UserContext.getSeller().getSellerId());
        ShipTemplateDO t = new ShipTemplateDO();
        BeanUtils.copyProperties(template, t);

        Integer id = template.getId();
        this.daoSupport.update(t, id);
        //删除子模板
        this.daoSupport.execute("delete from es_ship_template_child where template_id = ?", id);

        //保存运费模板子模板
        List<ShipTemplateChildSellerVO> items = template.getItems();
        this.addTemplateChildren(items, id);

        //移除缓存某个VO
        this.cache.remove(CachePrefix.SHIP_TEMPLATE_ONE.getPrefix() + id);

        this.cache.remove(CachePrefix.SHIP_TEMPLATE.getPrefix() + template.getSellerId());

        this.amqpTemplate.convertAndSend(AmqpExchange.SHIP_TEMPLATE_CHANGE, AmqpExchange.SHIP_TEMPLATE_CHANGE + "_ROUTING", new ShipTemplateMsg(id, 2));


        return t;
    }

    /**
     * 添加运费模板子模板
     */
    private void addTemplateChildren(List<ShipTemplateChildSellerVO> items, Integer templateId) {

        for (ShipTemplateChildSellerVO child : items) {

            ShipTemplateChild shipTemplateChild = new ShipTemplateChild();
            BeanUtils.copyProperties(child, shipTemplateChild);
            shipTemplateChild.setTemplateId(templateId);
            //获取地区id
            String area = child.getArea();

            Gson gson = new Gson();
            Map<String, Map> map = new HashMap();
            map = gson.fromJson(area, map.getClass());
            StringBuffer areaIdBuffer = new StringBuffer(",");
            // 获取所有的地区
            Object obj = this.cache.get(CachePrefix.REGIONALL.getPrefix() + 4);
            List<RegionVO> allRegions = null;
            if (obj == null) {
                allRegions = regionsClient.getRegionByDepth(4);
            }
            allRegions = (List<RegionVO>) obj;
            Map<String, RegionVO> regionsMap = new HashMap();
            //循环地区放到Map中，便于取出
            for (RegionVO region : allRegions) {
                regionsMap.put(region.getId() + "", region);
            }

            for (String key : map.keySet()) {
                //拼接地区id
                areaIdBuffer.append(key + ",");
                Map dto = map.get(key);
                //需要取出改地区下面所有的子地区
                RegionVO provinceRegion = regionsMap.get(key);
                List<RegionVO> cityRegionList = provinceRegion.getChildren();

                Map<String, RegionVO> cityRegionMap = new HashMap<>();
                for (RegionVO city : cityRegionList) {
                    cityRegionMap.put(city.getId() + "", city);
                }
                //判断下面的地区是否被全选
                if ((boolean) dto.get("selected_all")) {

                    //市
                    for (RegionVO cityRegion : cityRegionList) {

                        areaIdBuffer.append(cityRegion.getId() + ",");
                        List<RegionVO> regionList = cityRegion.getChildren();
                        //区
                        for (RegionVO region : regionList) {

                            areaIdBuffer.append(region.getId() + ",");
                            List<RegionVO> townList = region.getChildren();
                            //城镇
                            if (townList != null) {
                                for (RegionVO townRegion : townList) {

                                    areaIdBuffer.append(townRegion.getId() + ",");
                                }
                            }
                        }
                    }
                } else {
                    //没有全选，则看选中城市
                    Map<String, Map> citiesMap = (Map<String, Map>) dto.get("children");
                    for (String cityKey : citiesMap.keySet()) {

                        areaIdBuffer.append(cityKey + ",");

                        Map cityMap = citiesMap.get(cityKey);

                        RegionVO cityRegion = cityRegionMap.get(cityKey);
                        List<RegionVO> regionList = cityRegion.getChildren();
                        //某个城市如果全部选中，需要取出城市下面的子地区
                        if ((boolean) cityMap.get("selected_all")) {
                            //区
                            for (RegionVO region : regionList) {

                                areaIdBuffer.append(region.getId() + ",");
                                List<RegionVO> townList = region.getChildren();
                                //城镇
                                if (townList != null) {
                                    for (RegionVO townRegion : townList) {

                                        areaIdBuffer.append(townRegion.getId() + ",");
                                    }
                                }
                            }

                        } else {
                            //选中了某个城市下面的几个区
                            Map<String, Map> regionMap = (Map<String, Map>) cityMap.get("children");
                            for (String regionKey : regionMap.keySet()) {

                                areaIdBuffer.append(regionKey + ",");
                                for (RegionVO region : regionList) {
                                    if (("" + region.getId()).equals(regionKey)) {
                                        List<RegionVO> townList = region.getChildren();
                                        //城镇
                                        if (townList != null) {
                                            for (RegionVO townRegion : townList) {

                                                areaIdBuffer.append(townRegion.getId() + ",");
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            shipTemplateChild.setAreaId(areaIdBuffer.toString());
            this.daoSupport.insert(shipTemplateChild);
        }

    }

    @Override
    public List<ShipTemplateSellerVO> getStoreTemplate(Integer sellerId) {
        List<ShipTemplateSellerVO> list = (List<ShipTemplateSellerVO>) cache.get(CachePrefix.SHIP_TEMPLATE.getPrefix() + sellerId);
        if (list == null) {
            list = this.daoSupport.queryForList("select * from es_ship_template where seller_id = ? ", ShipTemplateSellerVO.class,
                    sellerId);

            if (list != null) {
                for (ShipTemplateSellerVO vo : list) {
                    String sql = "select first_company,first_price,continued_company,continued_price,area from es_ship_template_child where template_id = ?";
                    List<ShipTemplateChild> children = this.daoSupport.queryForList(sql, ShipTemplateChild.class, vo.getId());
                    List<ShipTemplateChildSellerVO> items = new ArrayList<>();
                    if (children != null) {
                        for (ShipTemplateChild child : children) {
                            ShipTemplateChildSellerVO childvo = new ShipTemplateChildSellerVO(child, true);
                            items.add(childvo);
                        }
                    }
                    vo.setItems(items);
                }
            }
            cache.put(CachePrefix.SHIP_TEMPLATE.getPrefix() + sellerId, list);
        }

        return list;
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delete(Integer templateId) {
        GoodsDO goodsDO = this.goodsClient.checkShipTemplate(templateId);
        if (goodsDO != null) {
            throw new ServiceException(ShopErrorCode.E226.code(), "模版被商品【" + goodsDO.getGoodsName() + "】使用，无法删除该模版");
        }

        ShipTemplateDO template = this.getOneDB(templateId);

        //删除运费模板
        this.daoSupport.execute("delete from es_ship_template where id=?", templateId);
        //删除运费模板关联地区
        this.daoSupport.execute("delete from es_ship_template_child where template_id = ?", templateId);

        Integer sellerId = template.getSellerId();

        //移除缓存某个VO
        this.cache.remove(CachePrefix.SHIP_TEMPLATE_ONE.getPrefix() + templateId);
        //移除缓存某商家的VO列表
        this.cache.remove(CachePrefix.SHIP_TEMPLATE.getPrefix() + sellerId);
    }

    @Override
    public ShipTemplateVO getFromCache(Integer templateId) {
        ShipTemplateVO tpl = (ShipTemplateVO) this.cache.get(CachePrefix.SHIP_TEMPLATE_ONE.getPrefix() + templateId);
        if (tpl == null) {
            //编辑运费模板的查询一个运费模板
            ShipTemplateDO template = this.getOneDB(templateId);
            tpl = new ShipTemplateVO();
            BeanUtils.copyProperties(template, tpl);

            //查询运费模板的子模板
            String sql = "select * from es_ship_template_child where template_id = ?";
            List<ShipTemplateChildBuyerVO> children = this.daoSupport.queryForList(sql, ShipTemplateChildBuyerVO.class, templateId);

            tpl.setItems(children);

            cache.put(CachePrefix.SHIP_TEMPLATE_ONE.getPrefix() + templateId, tpl);
        }
        return tpl;

    }

    @Override
    public ShipTemplateSellerVO getFromDB(Integer templateId) {

        ShipTemplateDO template = this.getOneDB(templateId);
        ShipTemplateSellerVO tpl = new ShipTemplateSellerVO();
        BeanUtils.copyProperties(template, tpl);

        //查询运费模板的子模板
        String sql = "select first_company,first_price,continued_company,continued_price,area from es_ship_template_child where template_id = ?";
        List<ShipTemplateChild> children = this.daoSupport.queryForList(sql, ShipTemplateChild.class, templateId);

        List<ShipTemplateChildSellerVO> items = new ArrayList<>();
        if (children != null) {
            for (ShipTemplateChild child : children) {
                ShipTemplateChildSellerVO childvo = new ShipTemplateChildSellerVO(child, false);
                items.add(childvo);
            }
        }

        tpl.setItems(items);

        return tpl;
    }

    @Override
    public List<String> getScripts(Integer id) {

        List<String> scripts = (List<String>) cache.get(CachePrefix.SHIP_SCRIPT.getPrefix() + id);
        if (scripts == null) {
            ShipTemplateVO shipTemplateVO = this.getFromCache(id);
            return this.cacheShipTemplateScript(shipTemplateVO);
        }
        return scripts;
    }

    @Override
    public List<String> cacheShipTemplateScript(ShipTemplateVO shipTemplateVO) {
        //获取运费模板子模板
        List<ShipTemplateChildBuyerVO> shipTemplateChildBuyerVOS = shipTemplateVO.getItems();
        List<String> scripts = new ArrayList<>();
        //循环子模板生成script
        for (ShipTemplateChildBuyerVO shipTemplateChildBuyerVO : shipTemplateChildBuyerVOS) {
            String script = getScript(shipTemplateChildBuyerVO, shipTemplateVO.getType());
            if (StringUtil.isEmpty(script)) {
                logger.error("运费模板id为" + shipTemplateVO.getId() + "的模板生成错误");
            }
            scripts.add(script);
        }

        //缓存模板script，格式为SHIP_SCRIPT_模板id
        cache.put(CachePrefix.SHIP_SCRIPT.getPrefix() + shipTemplateVO.getId(), scripts);

        return scripts;
    }

    /**
     * 生成脚本
     *
     * @param shipTemplateChildBuyerVO 子模板信息
     * @param shipTempalteType         模板类型
     * @return script脚本
     */
    private String getScript(ShipTemplateChildBuyerVO shipTemplateChildBuyerVO, Integer shipTempalteType) {
        String weightOrNum = "$goodsWeight";

        if (shipTempalteType.equals(2)) {
            weightOrNum = "$goodsNum";
        }
        String script = "function getShipPrice(){\n" +
                "if(" + "\"" + "" + shipTemplateChildBuyerVO.getAreaId() + "" + "\"" + ".indexOf(','+$address+',') < 0){\n" +
                "    return;\n" +
                "  }\n" +
                "  var shipPrice=" + shipTemplateChildBuyerVO.getFirstPrice() + ";\n" +
                "  if(" + shipTemplateChildBuyerVO.getFirstCompany() + " < " + weightOrNum + "){\n" +
                "     var count = (" + weightOrNum + " - " + shipTemplateChildBuyerVO.getFirstCompany() + ")/" + shipTemplateChildBuyerVO.getContinuedCompany() + ";\n" +
                "     count = Math.ceil(count)\n" +
                "     shipPrice = " + shipTemplateChildBuyerVO.getFirstPrice() + " + count*" + shipTemplateChildBuyerVO.getContinuedPrice() + ";\n" +
                "  }\n" +
                "  return shipPrice; \n" +
                "\n" +
                "}";
        return script;
    }

    /**
     * 数据库中查询运费模板
     *
     * @param templateId
     * @return
     */
    private ShipTemplateDO getOneDB(Integer templateId) {

        return this.daoSupport.queryForObject(ShipTemplateDO.class, templateId);
    }

}
