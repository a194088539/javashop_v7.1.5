package com.enation.app.javashop.consumer.shop.goods;

import com.enation.app.javashop.core.base.message.GoodsCommentMsg;
import com.enation.app.javashop.core.goods.model.dos.GoodsDO;
import com.enation.app.javashop.core.member.model.dos.MemberComment;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.test.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Map;

/**
 * @author fk
 * @version v1.0
 * @Description: 商品评论数量的变化消费者单元测试
 * @date 2018/6/26 10:37
 * @since v7.0.0
 */
public class GoodsCommentNumConsumerTest extends BaseTest {

    @Autowired
    private GoodsCommentNumConsumer goodsCommentNumConsumer;

    @Autowired
    @Qualifier("goodsDaoSupport")
    private DaoSupport daoSupport;

    @Test
    public void testEditCommentNum() {

        //添加一个商品
        GoodsDO goods = new GoodsDO();
        goods.setCommentNum(0);
        this.daoSupport.insert(goods);
        Integer goodsId = this.daoSupport.getLastId("");

        GoodsCommentMsg goodsCommentMsg = new GoodsCommentMsg();
        MemberComment comment = new MemberComment();
        comment.setGoodsId(goodsId);
        goodsCommentMsg.setComment(null);

        goodsCommentNumConsumer.goodsComment(goodsCommentMsg);

        //查询这个商品的评论数量，是1
        Map map = this.daoSupport.queryForMap("select comment_num from es_goods where goods_id = ?", goodsId);

        Assert.assertTrue(map.get("comment_num").toString().equals("1"));


    }



}
