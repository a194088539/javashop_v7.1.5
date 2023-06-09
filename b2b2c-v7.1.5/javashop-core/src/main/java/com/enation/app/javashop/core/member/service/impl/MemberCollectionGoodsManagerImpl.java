package com.enation.app.javashop.core.member.service.impl;

import com.enation.app.javashop.core.base.rabbitmq.AmqpExchange;
import com.enation.app.javashop.core.client.goods.GoodsClient;
import com.enation.app.javashop.core.goods.model.vo.CacheGoods;
import com.enation.app.javashop.core.member.MemberErrorCode;
import com.enation.app.javashop.core.member.model.dos.Member;
import com.enation.app.javashop.core.member.model.dos.MemberCollectionGoods;
import com.enation.app.javashop.core.member.service.MemberCollectionGoodsManager;
import com.enation.app.javashop.core.member.service.MemberManager;
import com.enation.app.javashop.core.statistics.model.dto.GoodsData;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.exception.ResourceNotFoundException;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.security.model.Buyer;
import com.enation.app.javashop.framework.util.DateUtil;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 会员商品收藏业务类
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-04-02 10:13:41
 */
@Service
public class MemberCollectionGoodsManagerImpl implements MemberCollectionGoodsManager {

    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport memberDaoSupport;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private MemberManager memberManager;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public Page list(int page, int pageSize) {
        Buyer buyer = UserContext.getBuyer();
        String sql = "select * from es_member_collection_goods where member_id = ? ";
        Page webPage = this.memberDaoSupport.queryForPage(sql, page, pageSize, MemberCollectionGoods.class, buyer.getUid());
        return webPage;
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public MemberCollectionGoods add(MemberCollectionGoods memberCollectionGoods) {
        Buyer buyer = UserContext.getBuyer();
        //查询当前会员是否存在
        Member member = memberManager.getModel(buyer.getUid());
        if (member == null) {
            throw new ResourceNotFoundException("当前会员不存在");
        }
        //获取商品id
        Integer goodsId = memberCollectionGoods.getGoodsId();
        //查询此商品信息
        CacheGoods goods = goodsClient.getFromCache(memberCollectionGoods.getGoodsId());
        //判断商品是否存在
        if (goods == null) {
            throw new ResourceNotFoundException("此商品不存在");
        }
        //判断当前商品是否已经添加为收藏
        String sql = "select * from es_member_collection_goods where member_id = ? and goods_id = ?";
        List<MemberCollectionGoods> list = this.memberDaoSupport.queryForList(sql, MemberCollectionGoods.class, buyer.getUid(), goodsId);
        if (list.size() > 0) {
            throw new ServiceException(MemberErrorCode.E105.code(), "当前商品已经添加为收藏");
        }
        //如果当前会员拥有店铺，则查看是否是收藏的自己店铺的商品
        if (!member.getHaveShop().equals(0)) {
            if (member.getShopId().equals(goods.getSellerId())) {
                throw new ServiceException(MemberErrorCode.E104.code(), "无法收藏自己店铺的商品");
            }
        }

        memberCollectionGoods.setMemberId(buyer.getUid());
        memberCollectionGoods.setGoodsName(goods.getGoodsName());
        memberCollectionGoods.setGoodsImg(goods.getThumbnail());
        memberCollectionGoods.setShopId(goods.getSellerId());
        memberCollectionGoods.setCreateTime(DateUtil.getDateline());
        memberCollectionGoods.setGoodsSn(goods.getSn());
        memberCollectionGoods.setGoodsPrice(goods.getPrice());
        this.memberDaoSupport.insert("es_member_collection_goods", memberCollectionGoods);
        memberCollectionGoods.setId(memberDaoSupport.getLastId("es_member_collection_goods"));

        //发送消息
        GoodsData goodsData = new GoodsData();
        goodsData.setGoodsId(goodsId);
        goodsData.setFavoriteNum(this.getGoodsCollectCount(goodsId));
        amqpTemplate.convertAndSend(AmqpExchange.GOODS_COLLECTION_CHANGE, AmqpExchange.GOODS_COLLECTION_CHANGE + "_ROUTING",
                goodsData);
        return memberCollectionGoods;
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delete(Integer goodsId) {
        Buyer buyer = UserContext.getBuyer();
        MemberCollectionGoods memberCollectionGoods = this.memberDaoSupport.queryForObject("select * from es_member_collection_goods where goods_id = ? and member_id = ?", MemberCollectionGoods.class, goodsId, buyer.getUid());
        if (memberCollectionGoods != null) {
            this.memberDaoSupport.delete(MemberCollectionGoods.class, memberCollectionGoods.getId());
            //发送消息
            GoodsData goodsData = new GoodsData();
            goodsData.setGoodsId(goodsId);
            goodsData.setFavoriteNum(this.getGoodsCollectCount(goodsId));
            amqpTemplate.convertAndSend(AmqpExchange.GOODS_COLLECTION_CHANGE, AmqpExchange.GOODS_COLLECTION_CHANGE + "_ROUTING",
                    goodsData);
        }
    }


    @Override
    public boolean isCollection(Integer id) {
        Buyer buyer = UserContext.getBuyer();
        int count = this.memberDaoSupport.queryForInt("select count(0) from es_member_collection_goods where goods_id = ? and member_id = ?", id, buyer.getUid());
        return count > 0;
    }

    @Override
    public MemberCollectionGoods getModel(Integer id) {
        return this.memberDaoSupport.queryForObject(MemberCollectionGoods.class, id);
    }

    @Override
    public Integer getMemberCollectCount() {
        return this.memberDaoSupport.queryForInt("select count(*) from es_member_collection_goods where member_id = ?", UserContext.getBuyer().getUid());
    }

    /**
     * 获取会员收藏商品数
     *
     * @return 收藏商品数
     */
    @Override
    public Integer getGoodsCollectCount(Integer goodsId) {
        try {
            return this.memberDaoSupport.queryForInt("select count(0) from es_member_collection_goods where goods_id = ?", goodsId);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }
}
