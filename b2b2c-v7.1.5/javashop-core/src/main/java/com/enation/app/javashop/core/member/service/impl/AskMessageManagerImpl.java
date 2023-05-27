package com.enation.app.javashop.core.member.service.impl;

import com.enation.app.javashop.core.member.MemberErrorCode;
import com.enation.app.javashop.core.member.model.dos.AskMessageDO;
import com.enation.app.javashop.core.member.model.dos.AskReplyDO;
import com.enation.app.javashop.core.member.model.enums.AskMsgTypeEnum;
import com.enation.app.javashop.core.member.model.enums.CommonStatusEnum;
import com.enation.app.javashop.core.member.service.AskMessageManager;
import com.enation.app.javashop.core.member.service.AskReplyManager;
import com.enation.app.javashop.core.system.enums.DeleteStatusEnum;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.security.model.Buyer;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.SqlUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 会员商品咨询消息业务接口实现
 *
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-09-16
 */
@Service
public class AskMessageManagerImpl implements AskMessageManager {

    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    private AskReplyManager askReplyManager;

    @Override
    public Page list(Integer pageNo, Integer pageSize, String isRead) {
        Buyer buyer = UserContext.getBuyer();
        if (buyer == null) {
            throw new ServiceException(MemberErrorCode.E110.code(), "当前会员已经退出登录");
        }

        StringBuffer sqlBuffer = new StringBuffer("select * from es_ask_message where is_del = ? and member_id = ? ");
        List<Object> term = new ArrayList<Object>();
        term.add(DeleteStatusEnum.NORMAL.value());
        term.add(buyer.getUid());

        if (StringUtil.notEmpty(isRead)) {
            sqlBuffer.append(" and is_read = ? ");
            term.add(isRead);
        }

        sqlBuffer.append(" order by receive_time desc");
        Page page = this.daoSupport.queryForPage(sqlBuffer.toString(), pageNo, pageSize, AskMessageDO.class, term.toArray());
        return page;
    }

    @Override
    public void read(Integer[] ids) {
        Buyer buyer = UserContext.getBuyer();
        if (buyer == null) {
            throw new ServiceException(MemberErrorCode.E110.code(), "当前会员已经退出登录");
        }

        List<Object> term = new ArrayList<>();
        term.add(CommonStatusEnum.YES.value());
        String str = SqlUtil.getInSql(ids, term);
        term.add(buyer.getUid());
        String sql = "update es_ask_message set is_read = ? where id in (" + str + ") and member_id = ?";
        this.daoSupport.execute(sql, term.toArray());
    }

    @Override
    public void delete(Integer[] ids) {
        Buyer buyer = UserContext.getBuyer();
        if (buyer == null) {
            throw new ServiceException(MemberErrorCode.E110.code(), "当前会员已经退出登录");
        }

        List<Object> term = new ArrayList<>();
        term.add(DeleteStatusEnum.DELETED.value());
        String str = SqlUtil.getInSql(ids, term);
        term.add(buyer.getUid());
        String sql = "update es_ask_message set is_del = ? where id in (" + str + ") and member_id = ?";
        this.daoSupport.execute(sql, term.toArray());
    }

    @Override
    public void delete(Integer askId, Integer memberId, String msgType) {
        List<Object> term = new ArrayList<>();
        term.add(DeleteStatusEnum.DELETED.value());
        term.add(askId);
        term.add(memberId);
        term.add(AskMsgTypeEnum.ASK.value());
        String sql = "update es_ask_message set is_del = ? where ask_id = ? and member_id = ? and msg_type = ?";
        this.daoSupport.execute(sql, term.toArray());
    }

    @Override
    public void deleteByAskId(Integer askId) {
        String sql = "update es_ask_message set is_del = ? where ask_id = ?";
        this.daoSupport.execute(sql, DeleteStatusEnum.DELETED.value(), askId);
    }

    @Override
    public AskMessageDO getModel(Integer id) {
        String sql = "select * from es_ask_message where id = ?";
        return this.daoSupport.queryForObject(sql, AskMessageDO.class, id);
    }

    @Override
    public void addAskMessage(AskMessageDO askMessageDO) {

        this.daoSupport.insert(askMessageDO);

        //如果是消息类型为提问消息，那么需要给接收消息的会员在回复表中添加一条未回复的信息
        if (AskMsgTypeEnum.ASK.value().equals(askMessageDO.getMsgType())) {
            AskReplyDO askReplyDO = new AskReplyDO();
            askReplyDO.setAskId(askMessageDO.getAskId());
            askReplyDO.setMemberId(askMessageDO.getMemberId());
            askReplyDO.setIsDel(DeleteStatusEnum.NORMAL.value());
            askReplyDO.setReplyStatus(CommonStatusEnum.NO.value());
            askReplyDO.setCreateTime(DateUtil.getDateline());
            this.askReplyManager.add(askReplyDO);
        }

    }
}
