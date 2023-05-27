package com.enation.app.javashop.core.system.service.impl;

import com.enation.app.javashop.core.base.rabbitmq.AmqpExchange;
import com.enation.app.javashop.core.member.MemberErrorCode;
import com.enation.app.javashop.core.system.model.dos.Message;
import com.enation.app.javashop.core.system.model.dto.MessageDTO;
import com.enation.app.javashop.core.system.model.dto.MessageQueryParam;
import com.enation.app.javashop.core.system.model.vo.MessageVO;
import com.enation.app.javashop.core.system.service.MessageManager;
import com.enation.app.javashop.framework.context.AdminUserContext;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.util.BeanUtil;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 站内消息业务类
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-07-04 21:50:52
 */
@Service
public class MessageManagerImpl implements MessageManager {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    @Qualifier("systemDaoSupport")
    private DaoSupport systemDaoSupport;

    @Override
    public Page list(MessageQueryParam param) {
        StringBuffer sqlBuffer = new StringBuffer("select * from es_message where disabled = 0 ");
        List<Object> term = new ArrayList<Object>();

        if (StringUtil.notEmpty(param.getKeyword())) {
            sqlBuffer.append(" and (title like ? or content like ?)");
            term.add("%" + param.getKeyword() + "%");
            term.add("%" + param.getKeyword() + "%");
        }

        if (StringUtil.notEmpty(param.getTitle())) {
            sqlBuffer.append(" and title like ?");
            term.add("%" + param.getTitle() + "%");
        }

        if (StringUtil.notEmpty(param.getContent())) {
            sqlBuffer.append(" and content like ?");
            term.add("%" + param.getContent() + "%");
        }

        if (param.getSendType() != null) {
            sqlBuffer.append(" and send_type = ?");
            term.add(param.getSendType());
        }

        if (param.getStartTime() != null && param.getStartTime() != 0) {
            sqlBuffer.append(" and send_time >= ?");
            term.add(param.getStartTime());
        }
        if (param.getEndTime() != null && param.getEndTime() != 0) {
            sqlBuffer.append(" and send_time <= ?");
            term.add(param.getEndTime());
        }

        sqlBuffer.append(" order by send_time desc");
        Page webPage = this.systemDaoSupport.queryForPage(sqlBuffer.toString(), param.getPageNo(), param.getPageSize(), MessageDTO.class, term.toArray());
        return webPage;
    }

    @Override
    @Transactional(value = "systemTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Message add(MessageVO messageVO) {
        if (messageVO.getSendType().equals(1)) {
            if (StringUtil.isEmpty(messageVO.getMemberIds())) {
                throw new ServiceException(MemberErrorCode.E122.code(), "请指定发送会员");
            }
        }
        Message message = new Message();
        BeanUtil.copyProperties(messageVO, message);
        message.setAdminId(AdminUserContext.getAdmin().getUid());
        message.setAdminName(AdminUserContext.getAdmin().getUsername());
        message.setSendTime(DateUtil.getDateline());
        message.setDisabled(0);
        this.systemDaoSupport.insert(message);
        Integer id = this.systemDaoSupport.getLastId("es_message");
        message.setId(id);
        this.amqpTemplate.convertAndSend(AmqpExchange.MEMBER_MESSAGE, "member-message-routingkey", id);
        return message;
    }

    @Override
    public Message get(Integer id) {
        String sql = "select * from es_message where id = ?";
        return this.systemDaoSupport.queryForObject(sql, Message.class, id);
    }
}
