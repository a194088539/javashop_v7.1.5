package com.enation.app.javashop.core.goodssearch.service.impl;

import com.enation.app.javashop.core.base.rabbitmq.AmqpExchange;
import com.enation.app.javashop.core.goodssearch.service.GoodsWordsManager;
import com.enation.app.javashop.core.goods.GoodsErrorCode;
import com.enation.app.javashop.core.goodssearch.enums.GoodsWordsType;
import com.enation.app.javashop.core.goodssearch.util.PinYinUtil;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.database.StringMapper;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.util.StringUtil;
import org.springframework.amqp.core.AmqpTemplate;
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
 * @author fk
 * @version v2.0
 * @Description: 商品分词Manager
 * @date 2019/12/6 11:04
 * @since v7.0.0
 */
@Service
public class GoodsWordsManagerImpl implements GoodsWordsManager {

    @Autowired
    @Qualifier("goodsDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    protected AmqpTemplate amqpTemplate;


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addWord(String word) {
        String sql = "select count(1) from es_goods_words where words=?";
        Integer count = this.daoSupport.queryForInt(sql, word);
        if (count > 0) {
            throw new ServiceException(GoodsErrorCode.E310.code(), "提示词【" + word + "】已存在");
        }
        Map map = new HashMap(16);
        map.put("words", word);
        map.put("quanpin", PinYinUtil.getPingYin(word));
        map.put("szm", PinYinUtil.getPinYinHeadChar(word));
        map.put("goods_num", 0);
        map.put("type", GoodsWordsType.PLATFORM.name());
        map.put("sort", 0);
        this.daoSupport.insert("es_goods_words", map);

        this.amqpTemplate.convertAndSend(AmqpExchange.GOODS_WORDS_CHANGE, AmqpExchange.GOODS_WORDS_CHANGE + "_ROUTING",word);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateWords(String word, Integer id) {
        String sql = "select count(1) from es_goods_words where words=? and id != ? ";
        Integer count = this.daoSupport.queryForInt(sql, word,id);
        if (count > 0) {
            throw new ServiceException(GoodsErrorCode.E310.code(), "提示词【" + word + "】已存在");
        }

        Map map = new HashMap(16);
        map.put("words", word);
        map.put("quanpin", PinYinUtil.getPingYin(word));
        map.put("szm", PinYinUtil.getPinYinHeadChar(word));

        Map where = new HashMap();
        where.put("id",id);

        this.daoSupport.update("es_goods_words", map,where);
        this.amqpTemplate.convertAndSend(AmqpExchange.GOODS_WORDS_CHANGE, AmqpExchange.GOODS_WORDS_CHANGE + "_ROUTING",word);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateSort(Integer id, Integer sort) {
        if(sort == null || sort < 0 || sort > 999999){
            throw new ServiceException(GoodsErrorCode.E310.code(),"优先级范围值[0-999999],请重新输入");
        }
        this.daoSupport.execute("update es_goods_words set sort = ? where id = ? ",sort,id);
    }


    @Override
    public Page listPage(Integer pageNo, Integer pageSize, String keyword) {

        StringBuffer sql = new StringBuffer("select id,words,quanpin,sort,type,goods_num from es_goods_words ");

        List<Object> term = new ArrayList<>();

        if (!StringUtil.isEmpty(keyword)) {
            sql.append(" where words like ? or quanpin like ? ");
            term.add("%" + keyword + "%");
            term.add("%" + keyword + "%");
        }

        sql.append(" order by sort , id desc ");

        Page page = this.daoSupport.queryForPage(sql.toString(),pageNo,pageSize,term.toArray());


        return page;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delete(GoodsWordsType goodsWordsType,Integer id) {
        if(GoodsWordsType.SYSTEM.equals(goodsWordsType)){
            this.daoSupport.execute("update es_goods_words set goods_num = 0 where type = ? ",goodsWordsType.name());
        }else {
            this.daoSupport.execute("delete from es_goods_words where id = ? ",id);
        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateGoodsNum(String words) {
        //匹配已上架且审核通过的商品数量
        Integer goodsNum = this.daoSupport.queryForInt("select count(1) from es_goods where goods_name like ? and market_enable = 1 and is_auth = 1 ","%"+words+"%");
        this.daoSupport.execute("update es_goods_words set goods_num = ? where words = ? ",goodsNum,words);
    }

    @Override
    public void batchUpdateGoodsNum() {
        //变更所有管理员添加的提示词相关商品数量   商品添加 或者修改时使用
        List<String> list = this.daoSupport.queryForList("select words from es_goods_words where type = ? ",new StringMapper(),GoodsWordsType.PLATFORM.name());
        list.forEach( str -> {
            this.updateGoodsNum(str);
        });
    }
}
