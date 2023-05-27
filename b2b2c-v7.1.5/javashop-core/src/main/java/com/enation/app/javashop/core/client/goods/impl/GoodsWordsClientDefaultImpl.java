package com.enation.app.javashop.core.client.goods.impl;

import com.enation.app.javashop.core.base.rabbitmq.AmqpExchange;
import com.enation.app.javashop.core.client.goods.GoodsWordsClient;
import com.enation.app.javashop.core.goods.GoodsErrorCode;
import com.enation.app.javashop.core.goodssearch.enums.GoodsWordsType;
import com.enation.app.javashop.core.goodssearch.service.GoodsWordsManager;
import com.enation.app.javashop.core.goodssearch.util.PinYinUtil;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.database.StringMapper;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.util.StringUtil;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fk
 * @version v2.0
 * @Description:
 * @date 2018/8/21 16:11
 * @since v7.0.0
 */
@Service
@ConditionalOnProperty(value = "javashop.product", havingValue = "stand")
public class GoodsWordsClientDefaultImpl implements GoodsWordsClient {

    @Autowired
    @Qualifier("goodsDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    protected AmqpTemplate amqpTemplate;

    @Autowired
    private GoodsWordsManager goodsWordsManager;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delete(String words) {
        this.daoSupport.execute("update es_goods_words set goods_num  = (case goods_num-1<0 when true  then 0 else goods_num-1 end ) where words=?", words);
    }

    @Override
    public void addWords(String words) {
        String sql = "select * from es_goods_words where words=?";
        List list = this.daoSupport.queryForList(sql, words);
        if (list == null || list.size() == 0) {
            Map map = new HashMap(16);
            map.put("words", words);
            map.put("quanpin", PinYinUtil.getPingYin(words));
            map.put("szm", PinYinUtil.getPinYinHeadChar(words));
            map.put("goods_num", 1);
            map.put("type", GoodsWordsType.SYSTEM.name());
            map.put("sort", 0);
            this.daoSupport.insert("es_goods_words", map);
        } else {
            this.daoSupport.execute("update es_goods_words set goods_num=goods_num+1 where words=?", words);
        }
    }

    @Override
    public void delete(GoodsWordsType goodsWordsType, Integer id) {
        goodsWordsManager.delete(goodsWordsType,id);
    }

    @Override
    public void updateGoodsNum(String words) {
        goodsWordsManager.updateGoodsNum(words);
    }

    @Override
    public void batchUpdateGoodsNum() {
        goodsWordsManager.batchUpdateGoodsNum();
    }


}
