package com.enation.app.javashop.core.client.member.impl;

import com.enation.app.javashop.core.client.member.MemberCollectionGoodsClient;
import com.enation.app.javashop.core.member.service.MemberCollectionGoodsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * 会员收藏商品默认实现
 *
 * @author zh
 * @version v7.0
 * @date 18/7/27 下午4:47
 * @since v7.0
 */
@Service
@ConditionalOnProperty(value="javashop.product", havingValue="stand")
public class MemberCollectionGoodsDefaultImpl implements MemberCollectionGoodsClient {

    @Autowired
    private MemberCollectionGoodsManager memberCollectionGoodsManager;

    @Override
    public Integer getGoodsCollectCount(Integer goodsId) {
        return memberCollectionGoodsManager.getGoodsCollectCount(goodsId);
    }
}
