package com.enation.app.javashop.core.member.service.impl;


import com.enation.app.javashop.core.client.goods.GoodsClient;
import com.enation.app.javashop.core.client.member.ShopClient;
import com.enation.app.javashop.core.base.rabbitmq.AmqpExchange;
import com.enation.app.javashop.core.goods.model.dto.GoodsQueryParam;
import com.enation.app.javashop.core.goods.model.vo.GoodsVO;
import com.enation.app.javashop.core.member.MemberErrorCode;
import com.enation.app.javashop.core.member.model.dos.Member;
import com.enation.app.javashop.core.member.model.dos.MemberCollectionShop;
import com.enation.app.javashop.core.member.model.vo.MemberCollectionShopVO;
import com.enation.app.javashop.core.member.service.MemberCollectionShopManager;
import com.enation.app.javashop.core.member.service.MemberManager;
import com.enation.app.javashop.core.shop.model.vo.ShopVO;
import com.enation.app.javashop.core.statistics.model.dto.ShopData;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.exception.NoPermissionException;
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

import java.util.ArrayList;
import java.util.List;


/**
 * 会员收藏店铺表业务类
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-30 20:34:23
 */
@Service
public class MemberCollectionShopManagerImpl implements MemberCollectionShopManager {

    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport memberDaoSupport;
    @Autowired
    private MemberManager memberManager;
    @Autowired
    private ShopClient shopClient;
    @Autowired
    private GoodsClient goodsClient;


    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public Page list(int page, int pageSize) {
        Buyer buyer = UserContext.getBuyer();
        String sql = "select * from es_member_collection_shop where member_id = ? order by create_time desc";
        Page webPage = this.memberDaoSupport.queryForPage(sql, page, pageSize, MemberCollectionShopVO.class, buyer.getUid());
        //存储查询到的商品数据
        List<MemberCollectionShopVO> memberCollectionShopVOS = new ArrayList<>();
        for (int i = 0; i < webPage.getData().size(); i++) {
            //获取当前的查询到的收藏信息
            MemberCollectionShopVO memberCollectionShopVO = (MemberCollectionShopVO) webPage.getData().get(i);
            //组织查询条件查询商品数据，将查询到的商品数据组织好
            GoodsQueryParam goodsQueryParam = new GoodsQueryParam();
            goodsQueryParam.setSellerId(memberCollectionShopVO.getShopId());
            goodsQueryParam.setPageNo(1);
            goodsQueryParam.setPageSize(10);
            Page goodsPage = goodsClient.list(goodsQueryParam);
            memberCollectionShopVO.setGoodsList((List<GoodsVO>) goodsPage.getData());
            memberCollectionShopVOS.add(memberCollectionShopVO);
        }
        //将收藏店铺信息重新组织返回前端
        webPage.setData(memberCollectionShopVOS);
        return webPage;
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public MemberCollectionShop add(MemberCollectionShop memberCollectionShop) {
        Integer shopId = memberCollectionShop.getShopId();
        //获取店铺信息
        ShopVO shop = shopClient.getShop(shopId);
        if (shop == null) {
            throw new ResourceNotFoundException("当前店铺不存在");
        }
        Buyer buyer = UserContext.getBuyer();
        //查询当前会员是否存在
        Member member = memberManager.getModel(buyer.getUid());
        if (member == null) {
            throw new ResourceNotFoundException("当前会员不存在");
        }
        //判断是否收藏的店铺是自己的
        if (!member.getHaveShop().equals(0)) {
            if (member.getShopId().equals(shopId)) {
                throw new ServiceException(MemberErrorCode.E102.code(), "无法将自己的店铺添加为收藏");
            }
        }
        String sql = "select * from es_member_collection_shop where member_id = ? and shop_id = ?";
        List<MemberCollectionShop> list = this.memberDaoSupport.queryForList(sql, MemberCollectionShop.class, buyer.getUid(), shopId);
        if (list.size() > 0) {
            throw new ServiceException(MemberErrorCode.E103.code(), "此店铺已经添加为收藏");
        }
        //查看店铺信息
        memberCollectionShop.setCreateTime(DateUtil.getDateline());
        memberCollectionShop.setMemberId(buyer.getUid());
        memberCollectionShop.setShopId(shop.getShopId());
        memberCollectionShop.setShopName(shop.getShopName());
        memberCollectionShop.setShopProvince(shop.getShopProvince());
        memberCollectionShop.setShopCity(shop.getShopCity());
        memberCollectionShop.setShopRegion(shop.getShopCounty());
        memberCollectionShop.setShopTown(shop.getShopTown());
        memberCollectionShop.setLogo(shop.getShopLogo());
        memberCollectionShop.setShopPraiseRate(shop.getShopPraiseRate());
        memberCollectionShop.setShopDescriptionCredit(shop.getShopDescriptionCredit());
        memberCollectionShop.setShopServiceCredit(shop.getShopServiceCredit());
        memberCollectionShop.setShopDeliveryCredit(shop.getShopDeliveryCredit());
        this.memberDaoSupport.insert(memberCollectionShop);
        Integer id = this.memberDaoSupport.getLastId("es_member_collection_shop");
        memberCollectionShop.setId(id);
        //添加为收藏后需要更新店铺收藏数

        shopClient.addCollectNum(shop.getShopId());

        //发送消息
        ShopData shopData = new ShopData();
        shopData.setSellerId(shopId);
        shopData.setFavoriteNum(this.getCollectionBySeller(shopId));
        amqpTemplate.convertAndSend(AmqpExchange.SELLER_COLLECTION_CHANGE, AmqpExchange.SELLER_COLLECTION_CHANGE + "_ROUTING",
                shopData);

        return memberCollectionShop;
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delete(Integer shopId) {
        Buyer buyer = UserContext.getBuyer();
        MemberCollectionShop memberCollectionShop = this.memberDaoSupport.queryForObject("select * from es_member_collection_shop where member_id = ? and shop_id = ?", MemberCollectionShop.class, buyer.getUid(), shopId);
        if (memberCollectionShop == null) {
            throw new NoPermissionException("无权限操作此收藏");
        }
        this.memberDaoSupport.delete(MemberCollectionShop.class, memberCollectionShop.getId());
        //取消收藏后更新收藏店铺数
        shopClient.reduceCollectNum(shopId);
        //发送消息
        ShopData shopData = new ShopData();
        shopData.setSellerId(shopId);
        shopData.setFavoriteNum(this.getCollectionBySeller(shopId));
        amqpTemplate.convertAndSend(AmqpExchange.SELLER_COLLECTION_CHANGE, AmqpExchange.SELLER_COLLECTION_CHANGE + "_ROUTING",
                shopData);
    }

    @Override
    public MemberCollectionShop getModel(Integer id) {
        return this.memberDaoSupport.queryForObject(MemberCollectionShop.class, id);
    }

    /**
     * 获取会员收藏店铺
     *
     * @param id 会员id
     * @return
     */
    @Override
    public boolean isCollection(Integer id) {

        Buyer buyer = UserContext.getBuyer();
        //查询当前会员是否存在
        Member member = memberManager.getModel(buyer.getUid());
        int count = this.memberDaoSupport.queryForInt("select count(0) from es_member_collection_shop where member_id = ? and shop_id = ?", member.getMemberId(), id);
        return count > 0;
    }

    @Override
    public Integer getMemberCollectCount() {
        return this.memberDaoSupport.queryForInt("select count(*) from es_member_collection_shop where member_id = ?", UserContext.getBuyer().getUid());
    }

    /**
     * 获取店铺有多少收藏量
     *
     * @param sellerId
     * @return 收藏量
     */
    @Override
    public Integer getCollectionBySeller(Integer sellerId) {
        return this.memberDaoSupport.queryForInt("select count(0) from es_member_collection_shop where shop_id = ?", sellerId);
    }
}
