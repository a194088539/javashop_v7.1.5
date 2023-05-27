package com.enation.app.javashop.core.system.service.impl;

import com.enation.app.javashop.core.system.SystemErrorCode;
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
import com.enation.app.javashop.core.system.model.ComplainTopic;
import com.enation.app.javashop.core.system.service.ComplainTopicManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 投诉主题业务类
 *
 * @author fk
 * @version v2.0
 * @since v2.0
 * 2019-11-26 16:06:44
 */
@Service
public class ComplainTopicManagerImpl implements ComplainTopicManager {

    @Autowired
    @Qualifier("systemDaoSupport")
    private DaoSupport daoSupport;

    @Override
    public Page list(int page, int pageSize) {

        String sql = "select * from es_complain_topic  ";
        Page webPage = this.daoSupport.queryForPage(sql, page, pageSize, ComplainTopic.class);

        return webPage;
    }

    @Override
    @Transactional(value = "systemTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ComplainTopic add(ComplainTopic complainTopic) {

        this.checkName(complainTopic.getTopicName(), null);
        complainTopic.setCreateTime(DateUtil.getDateline());
        this.daoSupport.insert(complainTopic);
        complainTopic.setTopicId(this.daoSupport.getLastId("es_complain_topic"));

        return complainTopic;
    }

    /**
     * 查看主体名称是否重复
     *
     * @param topicName
     * @param id
     */
    private void checkName(String topicName, Integer id) {

        String sql = "select * from es_complain_topic where topic_name = ?";
        List<Object> term = new ArrayList<>();
        term.add(topicName);
        if (id != null) {
            sql += " and topic_id !=? ";
            term.add(id);
        }
        List list = this.daoSupport.queryForList(sql, term.toArray());
        if (StringUtil.isNotEmpty(list)) {
            throw new ServiceException(SystemErrorCode.E929.code(), "主题重复");
        }
    }

    @Override
    @Transactional(value = "systemTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ComplainTopic edit(ComplainTopic complainTopic, Integer id) {
        this.checkName(complainTopic.getTopicName(), id);
        this.daoSupport.update(complainTopic, id);
        return complainTopic;
    }

    @Override
    @Transactional(value = "systemTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delete(Integer id) {
        this.daoSupport.delete(ComplainTopic.class, id);
    }

    @Override
    public ComplainTopic getModel(Integer id) {
        return this.daoSupport.queryForObject(ComplainTopic.class, id);
    }

    @Override
    public List<ComplainTopic> list() {

        String sql = "select * from es_complain_topic order by  create_time asc";

        return this.daoSupport.queryForList(sql, ComplainTopic.class);
    }
}
