package com.enation.app.javashop.core.member.service.impl;

import com.enation.app.javashop.core.base.SettingGroup;
import com.enation.app.javashop.core.base.message.AskReplyMessage;
import com.enation.app.javashop.core.base.rabbitmq.AmqpExchange;
import com.enation.app.javashop.core.client.system.SettingClient;
import com.enation.app.javashop.core.goods.model.dto.GoodsSettingVO;
import com.enation.app.javashop.core.member.MemberErrorCode;
import com.enation.app.javashop.core.member.model.dos.AskReplyDO;
import com.enation.app.javashop.core.member.model.dos.MemberAsk;
import com.enation.app.javashop.core.member.model.dto.ReplyQueryParam;
import com.enation.app.javashop.core.member.model.enums.AskMsgTypeEnum;
import com.enation.app.javashop.core.member.model.enums.AuditEnum;
import com.enation.app.javashop.core.member.model.enums.CommonStatusEnum;
import com.enation.app.javashop.core.member.model.vo.AskReplyVO;
import com.enation.app.javashop.core.member.model.vo.BatchAuditVO;
import com.enation.app.javashop.core.member.service.AskMessageManager;
import com.enation.app.javashop.core.member.service.AskReplyManager;
import com.enation.app.javashop.core.member.service.MemberAskManager;
import com.enation.app.javashop.core.system.enums.DeleteStatusEnum;
import com.enation.app.javashop.framework.context.AdminUserContext;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.exception.NoPermissionException;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.security.model.Admin;
import com.enation.app.javashop.framework.security.model.Buyer;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.JsonUtil;
import com.enation.app.javashop.framework.util.SqlUtil;
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
 * 会员回复商品咨询业务接口实现
 *
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-09-16
 */
@Service
public class AskReplyManagerImpl implements AskReplyManager {

    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    private SettingClient settingClient;

    @Autowired
    private MemberAskManager memberAskManager;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private AskMessageManager askMessageManager;

    @Override
    public Page list(ReplyQueryParam param) {
        StringBuffer sqlBuffer = new StringBuffer("select * from es_ask_reply where is_del = ? ");
        List<Object> term = new ArrayList<Object>();
        term.add(DeleteStatusEnum.NORMAL.value());

        //按会员商品咨询ID查询
        if (param.getAskId() != null) {
            sqlBuffer.append(" and ask_id = ? ");
            term.add(param.getAskId());
        }

        //按关键字查询
        if (!StringUtil.isEmpty(param.getKeyword())) {
            sqlBuffer.append(" and (content like ? or member_name like ?)");
            term.add("%" + param.getKeyword() + "%");
            term.add("%" + param.getKeyword() + "%");
        }

        //按会员名称查询
        if (!StringUtil.isEmpty(param.getMemberName())) {
            sqlBuffer.append(" and member_name like ? ");
            term.add("%" + param.getMemberName() + "%");
        }

        //按回复内容查询
        if (!StringUtil.isEmpty(param.getContent())) {
            sqlBuffer.append(" and content like ? ");
            term.add("%" + param.getContent() + "%");
        }

        //按审核状态查询
        if (StringUtil.notEmpty(param.getAuthStatus())) {
            sqlBuffer.append(" and auth_status = ? ");
            term.add(param.getAuthStatus());
        }

        //按回复时间-起始时间查询
        if (param.getStartTime() != null && param.getStartTime() != 0) {
            sqlBuffer.append(" and reply_time >= ?");
            term.add(param.getStartTime());
        }
        //按回复时间-结束时间查询
        if (param.getEndTime() != null && param.getEndTime() != 0) {
            sqlBuffer.append(" and reply_time <= ?");
            term.add(param.getEndTime());
        }

        //按匿名状态查询
        if (StringUtil.notEmpty(param.getAnonymous())) {
            sqlBuffer.append(" and anonymous = ?");
            term.add(param.getAnonymous());
        }

        //按回复状态查询
        if (StringUtil.notEmpty(param.getReplyStatus())) {
            sqlBuffer.append(" and reply_status = ?");
            term.add(param.getReplyStatus());
        }

        //按会员id查询
        if (param.getMemberId() != null) {
            sqlBuffer.append(" and member_id = ?");
            term.add(param.getMemberId());
        }

        //排除某条回复（一般用于商品详情页面获取咨询回复）
        if (param.getReplyId() != null) {
            sqlBuffer.append(" and id != ?");
            term.add(param.getReplyId());
        }

        sqlBuffer.append(" order by reply_time desc ");
        Page webPage = this.daoSupport.queryForPage(sqlBuffer.toString(), param.getPageNo(), param.getPageSize(), AskReplyDO.class, term.toArray());

        return webPage;
    }

    @Override
    public Page listMemberReply(ReplyQueryParam param) {
        StringBuffer sqlBuffer = new StringBuffer("select ar.*, ma.content as ask_content, ma.goods_id, ma.goods_name, ma.goods_img from es_ask_reply ar " +
                "left join es_member_ask ma on ar.ask_id = ma.ask_id where ar.is_del = ? ");
        List<Object> term = new ArrayList<Object>();
        term.add(DeleteStatusEnum.NORMAL.value());

        //按会员id查询
        if (param.getMemberId() != null) {
            sqlBuffer.append(" and ar.member_id = ?");
            term.add(param.getMemberId());
        }

        //按回复状态查询
        if (StringUtil.notEmpty(param.getReplyStatus())) {
            sqlBuffer.append(" and ar.reply_status = ?");
            term.add(param.getReplyStatus());
        }

        sqlBuffer.append(" order by create_time desc ");
        Page webPage = this.daoSupport.queryForPage(sqlBuffer.toString(), param.getPageNo(), param.getPageSize(), AskReplyVO.class, term.toArray());

        return webPage;
    }

    @Override
    public void add(AskReplyDO askReplyDO) {
        this.daoSupport.insert(askReplyDO);
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public AskReplyDO updateReply(Integer askId, String replyContent, String anonymous) {
        //获取当前登录的会员信息
        Buyer buyer = UserContext.getBuyer();

        if (buyer == null) {
            throw new ServiceException(MemberErrorCode.E110.code(), "当前会员已经退出登录");
        }

        if (askId == null || askId == 0) {
            throw new ServiceException(MemberErrorCode.E107.code(), "问题咨询ID参数值不正确");
        }

        if (StringUtil.isEmpty(replyContent)) {
            throw new ServiceException(MemberErrorCode.E202.code(), "回复内容不能为空");
        }

        if (replyContent.length() < 3 && replyContent.length() > 120) {
            throw new ServiceException(MemberErrorCode.E202.code(), "回复内容应在3到120个字符之间");
        }

        if (!CommonStatusEnum.YES.value().equals(anonymous) && !CommonStatusEnum.NO.value().equals(anonymous)) {
            throw new ServiceException(MemberErrorCode.E107.code(), "是否匿名参数值不正确");
        }

        MemberAsk memberAsk = this.memberAskManager.getModel(askId);
        if (memberAsk == null) {
            throw new ServiceException(MemberErrorCode.E202.code(), "会员商品咨询信息不存在");
        }

        //获取商品咨询设置
        String json = this.settingClient.get(SettingGroup.GOODS);
        GoodsSettingVO goodsSettingVO = JsonUtil.jsonToObject(json,GoodsSettingVO.class);

        AskReplyDO askReplyDO = this.getModel(askId, buyer.getUid());

        if (askReplyDO == null || buyer.getUid().intValue() != askReplyDO.getMemberId().intValue()) {
            throw new ServiceException(MemberErrorCode.E200.code(), "无权回复");
        }

        if (CommonStatusEnum.YES.value().equals(askReplyDO.getReplyStatus())) {
            throw new ServiceException(MemberErrorCode.E202.code(), "您已回复过问题，不可重复回复");
        }

        askReplyDO.setMemberName(CommonStatusEnum.YES.value().equals(anonymous) ? "匿名" : buyer.getUsername());
        askReplyDO.setContent(replyContent);
        askReplyDO.setReplyTime(DateUtil.getDateline());
        askReplyDO.setAnonymous(anonymous);
        askReplyDO.setAuthStatus(goodsSettingVO.getAskAuth().intValue() == 0 ? AuditEnum.PASS_AUDIT.name() : AuditEnum.WAIT_AUDIT.name());
        askReplyDO.setReplyStatus(CommonStatusEnum.YES.value());

        this.daoSupport.update(askReplyDO, askReplyDO.getId());

        //如果平台没有开启会员咨询审核，那么就直接发送消息
        if (goodsSettingVO.getAskAuth().intValue() == 0) {
            List<AskReplyDO> list = new ArrayList<>();
            list.add(askReplyDO);

            AskReplyMessage askReplyMessage = new AskReplyMessage(list, memberAsk, DateUtil.getDateline());
            this.amqpTemplate.convertAndSend(AmqpExchange.MEMBER_GOODS_ASK_REPLY, AmqpExchange.MEMBER_GOODS_ASK_REPLY + "_ROUTING", askReplyMessage);

            //修改会员商品咨询回复数量
            this.memberAskManager.updateReplyNum(askId, 1);
        }
        return askReplyDO;
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delete(Integer id) {
        AskReplyDO askReplyDO = this.getModel(id);
        if (askReplyDO == null) {
            throw new ServiceException(MemberErrorCode.E202.code(), "当前会员商品咨询回复已经删除");
        }

        String sql = "update es_ask_reply set is_del = ? where id = ?";
        this.daoSupport.execute(sql, DeleteStatusEnum.DELETED.value(), id);

        //修改会员商品咨询回复数量
        this.memberAskManager.updateReplyNum(askReplyDO.getAskId(), -1);

        //删除消息数据
        this.askMessageManager.delete(askReplyDO.getAskId(), askReplyDO.getMemberId(), AskMsgTypeEnum.ASK.value());
    }

    @Override
    public void deleteByAskId(Integer askId) {
        String sql = "update es_ask_reply set is_del = ? where ask_id = ?";
        this.daoSupport.execute(sql, DeleteStatusEnum.DELETED.value(), askId);
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void batchAudit(BatchAuditVO batchAuditVO) {
        // 校验是否有管理端权限
        Admin admin = AdminUserContext.getAdmin();

        if(admin == null ){
            throw new NoPermissionException("没有权限审核会员咨询回复信息!");
        }

        if (batchAuditVO.getIds() == null || batchAuditVO.getIds().length == 0) {
            throw new ServiceException(MemberErrorCode.E107.code(), "请选择要进行审核的会员商品咨询回复");
        }

        if (!AuditEnum.PASS_AUDIT.value().equals(batchAuditVO.getAuthStatus()) && !AuditEnum.REFUSE_AUDIT.value().equals(batchAuditVO.getAuthStatus())) {
            throw new ServiceException(MemberErrorCode.E107.code(), "审核状态参数值不正确");
        }

        List<Object> term = new ArrayList<>();
        String idStr = SqlUtil.getInSql(batchAuditVO.getIds(), term);

        String sql = "select * from es_ask_reply where id in (" + idStr + ")";
        List<AskReplyDO> askReplyDOList = this.daoSupport.queryForList(sql, AskReplyDO.class, term.toArray());

        for (AskReplyDO askReplyDO : askReplyDOList) {
            if (!AuditEnum.WAIT_AUDIT.value().equals(askReplyDO.getAuthStatus())) {
                throw new ServiceException(MemberErrorCode.E107.code(), "内容为【"+askReplyDO.getContent()+"】的回复不是可以进行审核的状态");
            }

            this.daoSupport.execute("update es_ask_reply set auth_status = ? where id = ? ", batchAuditVO.getAuthStatus(), askReplyDO.getId());
        }

        //获取会员商品咨询ID
        Integer askId = askReplyDOList.get(0).getAskId();

        //发送消息
        AskReplyMessage askReplyMessage = new AskReplyMessage(askReplyDOList, this.memberAskManager.getModel(askId), DateUtil.getDateline());
        this.amqpTemplate.convertAndSend(AmqpExchange.MEMBER_GOODS_ASK_REPLY, AmqpExchange.MEMBER_GOODS_ASK_REPLY + "_ROUTING", askReplyMessage);

        //修改会员商品咨询回复数量
        this.memberAskManager.updateReplyNum(askId, askReplyDOList.size());
    }

    @Override
    public AskReplyDO getModel(Integer id) {
        String sql = "select * from es_ask_reply where id = ?";
        return this.daoSupport.queryForObject(sql, AskReplyDO.class, id);
    }

    @Override
    public AskReplyDO getModel(Integer askId, Integer memberId) {
        String sql = "select * from es_ask_reply where ask_id = ? and member_id = ?";
        return this.daoSupport.queryForObject(sql, AskReplyDO.class, askId, memberId);
    }

    @Override
    public AskReplyDO getNewestModel(Integer askId) {
        String sql = "select * from es_ask_reply where ask_id = ? and reply_status = ? and is_del = ? and auth_status = ? order by reply_time desc LIMIT 1";
        return this.daoSupport.queryForObject(sql, AskReplyDO.class, askId, CommonStatusEnum.YES.value(), DeleteStatusEnum.NORMAL.value(), AuditEnum.PASS_AUDIT.value());
    }
}
