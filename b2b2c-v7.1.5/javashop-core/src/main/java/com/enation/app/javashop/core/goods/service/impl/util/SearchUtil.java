package com.enation.app.javashop.core.goods.service.impl.util;

import com.enation.app.javashop.core.client.member.ShopCatClient;
import com.enation.app.javashop.core.goods.GoodsErrorCode;
import com.enation.app.javashop.core.goods.model.dto.GoodsQueryParam;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.util.StringUtil;

import java.util.List;
import java.util.Map;

/**
 * SearchUtil
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2019-01-25 下午4:17
 */
public class SearchUtil {


    /**
     * 店铺分类
     *
     * @param goodsQueryParam
     * @param term
     * @param sqlBuffer
     */
    public static void shopCatQuery(GoodsQueryParam goodsQueryParam, List<Object> term, StringBuffer sqlBuffer, ShopCatClient shopCatClient) {
        if (goodsQueryParam.getShopCatPath() != null) {
            List<Map> catList = shopCatClient.getChildren(goodsQueryParam.getShopCatPath());

            if (!StringUtil.isNotEmpty(catList)) {
                throw new ServiceException(GoodsErrorCode.E301.code(), "店铺分组不存在");
            }

            String[] temp = new String[catList.size()];
            for (int i = 0; i < catList.size(); i++) {
                temp[i] = "?";
                term.add(catList.get(i).get("shop_cat_id"));
            }
            String str = StringUtil.arrayToString(temp, ",");
            sqlBuffer.append(" and g.shop_cat_id in (" + str + ")");
        }
    }

    /**
     * 分类查询
     *
     * @param goodsQueryParam
     * @param term
     * @param sqlBuffer
     * @param daoSupport
     */
    public static void categoryQuery(GoodsQueryParam goodsQueryParam, List<Object> term, StringBuffer sqlBuffer, DaoSupport daoSupport) {
        // 商城分类，同时需要查询出子分类的商品
        if (!StringUtil.isEmpty(goodsQueryParam.getCategoryPath())) {
            List<Map> list = daoSupport.queryForList(
                    "select category_id from es_category where category_path like ? ",
                    goodsQueryParam.getCategoryPath() + "%");

            if (!StringUtil.isNotEmpty(list)) {
                throw new ServiceException(GoodsErrorCode.E301.code(), "分类不存在");
            }

            String[] temp = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                temp[i] = "?";
                term.add(list.get(i).get("category_id"));
            }
            String str = StringUtil.arrayToString(temp, ",");
            sqlBuffer.append(" and g.category_id in (" + str + ")");

        }
    }

    /**
     * 基础查询
     *
     * @param goodsQueryParam
     * @param term
     * @param sqlBuffer
     */
    public static void baseQuery(GoodsQueryParam goodsQueryParam, List<Object> term, StringBuffer sqlBuffer) {
        if (goodsQueryParam.getDisabled() == null) {
            goodsQueryParam.setDisabled(1);
        }
        sqlBuffer.append(" where  g.disabled = ? ");
        term.add(goodsQueryParam.getDisabled());

        // 上下架
        if (goodsQueryParam.getMarketEnable() != null) {
            sqlBuffer.append(" and g.market_enable = ? ");
            term.add(goodsQueryParam.getMarketEnable());
        }
        // 模糊关键字
        if (!StringUtil.isEmpty(goodsQueryParam.getKeyword())) {
            sqlBuffer.append(" and (g.goods_name like ? or g.sn like ? ) ");
            term.add("%" + goodsQueryParam.getKeyword() + "%");
            term.add("%" + goodsQueryParam.getKeyword() + "%");
        }
        // 名称
        if (!StringUtil.isEmpty(goodsQueryParam.getGoodsName())) {
            sqlBuffer.append(" and g.goods_name like ?");
            term.add("%" + goodsQueryParam.getGoodsName() + "%");
        }
        // 店铺名称
        if (!StringUtil.isEmpty(goodsQueryParam.getSellerName())) {
            sqlBuffer.append(" and g.seller_name like ?");
            term.add("%" + goodsQueryParam.getSellerName() + "%");
        }
        // 商品编号
        if (!StringUtil.isEmpty(goodsQueryParam.getGoodsSn())) {
            sqlBuffer.append(" and g.sn like ?");
            term.add("%" + goodsQueryParam.getGoodsSn() + "%");
        }
        // 店铺id
        if (goodsQueryParam.getSellerId() != null && goodsQueryParam.getSellerId().intValue() != 0) {
            sqlBuffer.append(" and g.seller_id = ?");
            term.add(goodsQueryParam.getSellerId());
        }
        //商品类型
        if (!StringUtil.isEmpty(goodsQueryParam.getGoodsType())) {
            sqlBuffer.append(" and g.goods_type = ?");
            term.add(goodsQueryParam.getGoodsType());
        }
        // 审核状态
        if (goodsQueryParam.getIsAuth() != null) {
            sqlBuffer.append(" and g.is_auth = ?");
            term.add(goodsQueryParam.getIsAuth());
        }
        // 商品品牌
        if (goodsQueryParam.getBrandId() != null && goodsQueryParam.getBrandId().intValue() != 0) {
            sqlBuffer.append(" and g.brand_id = ?");
            term.add(goodsQueryParam.getBrandId());
        }
        //商品价格
        if (goodsQueryParam.getStart_price() != null) {
            sqlBuffer.append(" and g.price >= ?");
            term.add(goodsQueryParam.getStart_price());
        }
        if (goodsQueryParam.getEnd_price() != null) {
            sqlBuffer.append(" and g.price <= ?");
            term.add(goodsQueryParam.getEnd_price());
        }
    }

}
