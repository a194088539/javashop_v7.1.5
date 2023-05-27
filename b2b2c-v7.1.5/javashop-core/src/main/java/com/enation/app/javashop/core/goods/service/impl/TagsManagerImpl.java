package com.enation.app.javashop.core.goods.service.impl;

import com.enation.app.javashop.core.goods.GoodsErrorCode;
import com.enation.app.javashop.core.goods.model.dos.TagGoodsDO;
import com.enation.app.javashop.core.goods.model.dos.TagsDO;
import com.enation.app.javashop.core.goods.model.enums.TagType;
import com.enation.app.javashop.core.goods.model.vo.GoodsSelectLine;
import com.enation.app.javashop.core.goods.model.vo.TagGoodsNum;
import com.enation.app.javashop.core.goods.service.TagsManager;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.security.model.Seller;
import com.enation.app.javashop.framework.util.SqlUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品标签业务类
 *
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018-03-28 14:49:36
 */
@Service
public class TagsManagerImpl implements TagsManager {

    @Autowired
    @Qualifier("goodsDaoSupport")
    private DaoSupport daoSupport;

    @Override
    public Page list(int page, int pageSize) {

        Seller seller = UserContext.getSeller();

        String sql = "select * from es_tags  where seller_id = ?";
        Page webPage = this.daoSupport.queryForPage(sql, page, pageSize, TagsDO.class, seller.getSellerId());

        return webPage;
    }


    @Override
    @Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addShopTags(Integer sellerId) {

        TagType[] tags = TagType.values();
        for (TagType tag : tags) {
            TagsDO tagDO = new TagsDO(tag.tagName(), sellerId, tag.mark());
            this.daoSupport.insert(tagDO);

        }

    }


    @Override
    public Page queryTagGoods(Integer tagId, Integer pageNo, Integer pageSize) {

        Seller seller = UserContext.getSeller();
        TagsDO tag = this.getModel(tagId);
        if (tag == null || !tag.getSellerId().equals(seller.getSellerId())) {
            throw new ServiceException(GoodsErrorCode.E309.code(), "无权操作");
        }

        String sql = "select g.goods_id,g.goods_name,g.price,g.buy_count,g.enable_quantity,g.thumbnail from es_tag_goods r LEFT JOIN es_goods g ON g.goods_id=r.goods_id  "
                + "where g.disabled=1 and g.market_enable=1 and r.tag_id=? ";

        return this.daoSupport.queryForPage(sql, pageNo, pageSize, tagId);
    }


    @Override
    public void saveTagGoods(Integer tagId, Integer[] goodsIds) {

        Seller seller = UserContext.getSeller();
        TagsDO tag = this.getModel(tagId);
        if (tag == null || !tag.getSellerId().equals(seller.getSellerId())) {
            throw new ServiceException(GoodsErrorCode.E309.code(), "无权操作");
        }

        if(goodsIds[0] != -1){
            List<Object> term = new ArrayList<>();
            String idStr = SqlUtil.getInSql(goodsIds, term);
            term.add(seller.getSellerId());
            Integer count = this.daoSupport.queryForInt("select count(1) from es_goods where goods_id in (" + idStr + ") and seller_id = ?", term.toArray());
            if (goodsIds.length != count) {
                throw new ServiceException(GoodsErrorCode.E309.code(), "无权操作");
            }
        }

        //删除
        String sql = "delete from es_tag_goods where tag_id = ?";
        this.daoSupport.execute(sql,tagId);

        if(goodsIds[0] == -1){
            //表示这个标签下不保存商品
            return;
        }
        //添加
        for (Integer goodsId : goodsIds) {
            TagGoodsDO tagGoods = new TagGoodsDO(tagId, goodsId);
            this.daoSupport.insert(tagGoods);
        }
    }


    @Override
    public List<GoodsSelectLine> queryTagGoods(Integer sellerId, Integer num, String mark) {

        String sql = "select g.goods_id,g.goods_name,g.price,g.sn,g.thumbnail,g.big,g.quantity,g.buy_count from es_tag_goods r "
                + " inner join es_goods g on g.goods_id=r.goods_id "
                + " inner join es_tags t on t.tag_id = r.tag_id"
                + " where g.disabled=1 and g.market_enable=1 and t.mark = ? and t.seller_id = ?   limit 0,? ";

        return this.daoSupport.queryForList(sql, GoodsSelectLine.class, mark, sellerId, num);
    }

    @Override
    public TagsDO getModel(Integer id) {

        return this.daoSupport.queryForObject(TagsDO.class, id);
    }


    @Override
    public TagGoodsNum queryGoodsNumByShopId(Integer shopId) {

        String sql = "select count(1) count,t.mark from es_tags t left join es_tag_goods tg on t.tag_id=tg.tag_id where t.seller_id = ?  group by t.tag_id";

        List<Map> list = this.daoSupport.queryForList(sql,shopId);

        Integer hotNum = 0;
        Integer newNum = 0;
        Integer recommendNum = 0;

        if(StringUtil.isNotEmpty(list)){
            for(Map map : list){
                String mark = map.get("mark").toString();
                Integer count = Integer.valueOf(map.get("count").toString());

                switch (mark) {
                    case "hot":
                        hotNum = count;
                        break;
                    case "new":
                        newNum = count;
                        break;
                    case "recommend":
                        recommendNum = count;
                        break;
                    default:
                        break;
                }

            }
        }

        TagGoodsNum tagGoodsNum = new TagGoodsNum(hotNum,newNum,recommendNum);

        return tagGoodsNum;
    }
}
