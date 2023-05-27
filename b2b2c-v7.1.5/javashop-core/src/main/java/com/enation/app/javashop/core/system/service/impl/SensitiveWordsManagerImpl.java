package com.enation.app.javashop.core.system.service.impl;

import com.enation.app.javashop.core.base.message.SensitiveWordsMsg;
import com.enation.app.javashop.core.base.model.dos.SensitiveWords;
import com.enation.app.javashop.core.base.redismq.RedisChannel;
import com.enation.app.javashop.core.statistics.util.DateUtil;
import com.enation.app.javashop.core.system.SystemErrorCode;
import com.enation.app.javashop.core.system.sensitiveutil.SensitiveFilter;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.util.JsonUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.core.system.service.SensitiveWordsManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 敏感词业务类
 *
 * @author fk
 * @version v1.0
 * @since v7.0.0
 * 2018-08-02 11:30:59
 */
@Service
public class SensitiveWordsManagerImpl implements SensitiveWordsManager {

    @Autowired
    @Qualifier("systemDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Override
    public Page list(int page, int pageSize, String keyword) {

        StringBuffer sqlBuffer = new StringBuffer("select * from es_sensitive_words where is_delete = 1 ");

        List<Object> term = new ArrayList<>();
        if (keyword != null) {
            sqlBuffer.append(" and  word_name like ? ");
            term.add("%" + keyword + "%");
        }

        Page webPage = this.daoSupport.queryForPage(sqlBuffer.toString(), page, pageSize, SensitiveWords.class, term.toArray());

        return webPage;
    }

    @Override
    @Transactional(value = "systemTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public SensitiveWords add(SensitiveWords sensitiveWords) {

        //不能重复添加
        String sql = "select * from es_sensitive_words where word_name = ? and is_delete = 1";
        List list = this.daoSupport.queryForList(sql, sensitiveWords.getWordName());
        if (StringUtil.isNotEmpty(list)) {
            throw new ServiceException(SystemErrorCode.E928.code(), "敏感词语不能重复");
        }

        sensitiveWords.setIsDelete(1);
        sensitiveWords.setCreateTime(DateUtil.getDateline());

        this.daoSupport.insert(sensitiveWords);

        //将敏感词发送消息
        SensitiveWordsMsg msg = new SensitiveWordsMsg(sensitiveWords.getWordName(), SensitiveWordsMsg.ADD);
        redisTemplate.convertAndSend(RedisChannel.SENSITIVE_WORDS, JsonUtil.objectToJson(msg));

        return sensitiveWords;
    }

    @Override
    @Transactional(value = "systemTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public SensitiveWords edit(SensitiveWords sensitiveWords, Integer id) {
        this.daoSupport.update(sensitiveWords, id);
        return sensitiveWords;
    }

    @Override
    @Transactional(value = "systemTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delete(Integer id) {

        SensitiveWords model = this.getModel(id);

        String sql = "update es_sensitive_words set is_delete = 0 where id = ?";
        this.daoSupport.execute(sql, id);

        //将删除敏感词发送消息
        SensitiveWordsMsg msg = new SensitiveWordsMsg(model.getWordName(), SensitiveWordsMsg.DELETE);
        redisTemplate.convertAndSend(RedisChannel.SENSITIVE_WORDS, JsonUtil.objectToJson(msg));

    }

    @Override
    public SensitiveWords getModel(Integer id) {
        return this.daoSupport.queryForObject(SensitiveWords.class, id);
    }

    @Override
    public List<String> listWords() {

        String sql = "select * from es_sensitive_words where is_delete = 1";

        List<SensitiveWords> list = this.daoSupport.queryForList(sql, SensitiveWords.class);

        List<String> words = new ArrayList<>();
        if (list != null) {

            for (SensitiveWords word : list) {
                words.add(word.getWordName());
            }

        }
        return words;
    }

}
