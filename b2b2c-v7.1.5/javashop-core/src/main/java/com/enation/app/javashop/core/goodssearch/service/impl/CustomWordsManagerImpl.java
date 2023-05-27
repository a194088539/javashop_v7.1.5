package com.enation.app.javashop.core.goodssearch.service.impl;

import com.enation.app.javashop.core.goods.GoodsErrorCode;
import com.enation.app.javashop.framework.context.ThreadContextHolder;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.core.goodssearch.model.CustomWords;
import com.enation.app.javashop.core.goodssearch.service.CustomWordsManager;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义分词表业务类
 *
 * @author fk
 * @version v1.0
 * @since v7.0.0
 * 2018-06-20 16:08:07
 *
 * update by liuyulei 2019-05-27
 */
@Service
public class CustomWordsManagerImpl implements CustomWordsManager {

    @Autowired
    @Qualifier("goodsDaoSupport")
    private DaoSupport daoSupport;

    @Override
    public Page list(int page, int pageSize,String keywords) {

        StringBuffer sql = new StringBuffer("select * from es_custom_words where disabled = 1 ");

        List term = new ArrayList();

        if(!StringUtil.isEmpty(keywords)){
            sql.append(" and name like ? ");
            term.add("%" + keywords + "%");
        }

        sql.append(" order by modify_time desc");

        Page webPage = this.daoSupport.queryForPage(sql.toString(), page, pageSize, CustomWords.class,term.toArray());

        return webPage;
    }

    @Override
    @Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public CustomWords add(CustomWords customWords) {

        this.validKeywords(customWords.getName());

        customWords.setAddTime(DateUtil.getDateline());
        customWords.setModifyTime(DateUtil.getDateline());
        customWords.setDisabled(1);
        this.daoSupport.insert(customWords);
        customWords.setId(this.daoSupport.getLastId("es_custom_words"));
        return customWords;
    }

    @Override
    @Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public CustomWords edit(CustomWords customWords, Integer id) {
        validKeywords(customWords.getName());
        customWords.setDisabled(1);
        customWords.setModifyTime(DateUtil.getDateline());
        this.daoSupport.update(customWords, id);
        return customWords;
    }

    @Override
    @Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delete(Integer id) {

        CustomWords words = this.getModel(id);
        words.setModifyTime(DateUtil.getDateline());
        words.setDisabled(0);
        this.daoSupport.update(words, id);
    }

    @Override
    public CustomWords getModel(Integer id) {
        return this.daoSupport.queryForObject(CustomWords.class, id);
    }

    @Override
    public String deploy() {

        String sql = "select * from es_custom_words where disabled = 1 order by modify_time desc";
        List<CustomWords> list = this.daoSupport.queryForList(sql, CustomWords.class);

        HttpServletResponse response = ThreadContextHolder.getHttpResponse();

        StringBuffer buffer = new StringBuffer();

        if (StringUtil.isNotEmpty(list)) {
            int i = 0;
            for (CustomWords word : list) {

                if (i == 0) {


                    SimpleDateFormat format =   new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" );
                    try {
                        response.setHeader("Last-Modified", format.parse(DateUtil.toString(word.getAddTime(),"yyyy-MM-dd hh:mm:ss")) + "");
                        response.setHeader("ETag", format.parse(DateUtil.toString(word.getModifyTime(),"yyyy-MM-dd hh:mm:ss")) + "");
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    buffer.append(word.getName());
                } else {
                    buffer.append("\n" + word.getName());
                }

                i++;
            }
        }

        return buffer.toString();
    }

    @Override
    public boolean isExist(String keyword) {
        int count = this.daoSupport.queryForInt("select count(1) from es_custom_words where name = ? ",keyword);
        if(count > 0){
            return true;
        }
        return false;
    }

    private void validKeywords(String keyword){
        boolean isExist = this.isExist(keyword);
        if(isExist){
            throw new ServiceException(GoodsErrorCode.E310.code(),"【" + keyword + "】已存在");
        }
    }
}
