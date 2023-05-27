package com.enation.app.javashop.core.member.service.impl;

import com.enation.app.javashop.core.base.SettingGroup;
import com.enation.app.javashop.core.base.message.MemberAskMessage;
import com.enation.app.javashop.core.base.rabbitmq.AmqpExchange;
import com.enation.app.javashop.core.client.goods.GoodsClient;
import com.enation.app.javashop.core.client.system.SettingClient;
import com.enation.app.javashop.core.goods.model.dto.GoodsSettingVO;
import com.enation.app.javashop.core.goods.model.vo.CacheGoods;
import com.enation.app.javashop.core.member.MemberErrorCode;
import com.enation.app.javashop.core.member.model.dos.Member;
import com.enation.app.javashop.core.member.model.dos.MemberAsk;
import com.enation.app.javashop.core.member.model.dto.AskQueryParam;
import com.enation.app.javashop.core.member.model.enums.AuditEnum;
import com.enation.app.javashop.core.member.model.enums.CommonStatusEnum;
import com.enation.app.javashop.core.member.model.vo.BatchAuditVO;
import com.enation.app.javashop.core.member.model.vo.MemberAskVO;
import com.enation.app.javashop.core.member.service.AskMessageManager;
import com.enation.app.javashop.core.member.service.AskReplyManager;
import com.enation.app.javashop.core.member.service.MemberAskManager;
import com.enation.app.javashop.core.member.service.MemberManager;
import com.enation.app.javashop.core.system.enums.DeleteStatusEnum;
import com.enation.app.javashop.framework.context.AdminUserContext;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.exception.NoPermissionException;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.security.model.Admin;
import com.enation.app.javashop.framework.security.model.Buyer;
import com.enation.app.javashop.framework.security.model.Seller;
import com.enation.app.javashop.framework.util.*;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 咨询业务类
 *
 * @author fk
 * @version v1.0
 * @since v7.0.0
 * 2018-05-04 17:41:18
 */
@Service
public class MemberAskManagerImpl implements MemberAskManager {

    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport daoSupport;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private MemberManager memberManager;
    @Autowired
    private SettingClient settingClient;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private AskReplyManager askReplyManager;
    @Autowired
    private AskMessageManager askMessageManager;

    @Override
    public Page list(AskQueryParam param) {

        StringBuffer sqlBuffer = new StringBuffer("select * from es_member_ask where status = ? ");
        List<Object> term = new ArrayList<Object>();
        term.add(DeleteStatusEnum.NORMAL.value());

        //按关键字查询
        if (!StringUtil.isEmpty(param.getKeyword())) {
            sqlBuffer.append(" and (content like ? or goods_name like ? or member_name like ?)");
            term.add("%" + param.getKeyword() + "%");
            term.add("%" + param.getKeyword() + "%");
            term.add("%" + param.getKeyword() + "%");
        }

        //按会员ID查询
        if (param.getMemberId() != null) {
            sqlBuffer.append(" and member_id = ? ");
            term.add(param.getMemberId());
        }

        //按商家回复状态查询
        if (param.getReplyStatus() != null) {
            sqlBuffer.append(" and reply_status = ? ");
            term.add(param.getReplyStatus());
        }

        //按商家ID查询
        if (param.getSellerId() != null && param.getSellerId() != 0) {
            sqlBuffer.append(" and seller_id = ? ");
            term.add(param.getSellerId());
        }

        //按商品名称查询
        if (!StringUtil.isEmpty(param.getGoodsName())) {
            sqlBuffer.append(" and goods_name like ? ");
            term.add("%" + param.getGoodsName() + "%");
        }

        //按会员名称查询
        if (!StringUtil.isEmpty(param.getMemberName())) {
            sqlBuffer.append(" and member_name like ? ");
            term.add("%" + param.getMemberName() + "%");
        }

        //按咨询内容查询
        if (!StringUtil.isEmpty(param.getContent())) {
            sqlBuffer.append(" and content like ? ");
            term.add("%" + param.getContent() + "%");
        }

        //按审核状态查询
        if (StringUtil.notEmpty(param.getAuthStatus())) {
            sqlBuffer.append(" and auth_status = ? ");
            term.add(param.getAuthStatus());
        }

        //按咨询时间-起始时间查询
        if (param.getStartTime() != null && param.getStartTime() != 0) {
            sqlBuffer.append(" and create_time >= ?");
            term.add(param.getStartTime());
        }
        //按咨询时间-结束时间查询
        if (param.getEndTime() != null && param.getEndTime() != 0) {
            sqlBuffer.append(" and create_time <= ?");
            term.add(param.getEndTime());
        }

        //按匿名状态查询
        if (StringUtil.notEmpty(param.getAnonymous())) {
            sqlBuffer.append(" and anonymous = ?");
            term.add(param.getAnonymous());
        }

        sqlBuffer.append(" order by create_time desc ");
        Page webPage = this.daoSupport.queryForPage(sqlBuffer.toString(), param.getPageNo(), param.getPageSize(), MemberAsk.class, term.toArray());

        return webPage;
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public MemberAsk add(String askContent, Integer goodsId, String anonymous) {
        if (askContent.length() < 3 || askContent.length() > 120) {
            throw new ServiceException(MemberErrorCode.E202.code(), "咨询内容应在3到120个字符之间");
        }

        //获取咨询的商品信息
        CacheGoods goods = goodsClient.getFromCache(goodsId);

        //获取当前登录的会员信息
        Buyer buyer = UserContext.getBuyer();

        if (buyer == null) {
            throw new ServiceException(MemberErrorCode.E110.code(), "当前会员已经退出登录");
        }

        //在数据库中取出最新的会员信息
        Member member = memberManager.getModel(buyer.getUid());

        MemberAsk memberAsk = new MemberAsk();
        memberAsk.setContent(askContent);
        memberAsk.setMemberId(member.getMemberId());
        memberAsk.setGoodsId(goodsId);
        memberAsk.setGoodsName(goods.getGoodsName());
        memberAsk.setGoodsImg(goods.getThumbnail());
        memberAsk.setCreateTime(DateUtil.getDateline());
        //咨询状态默认为正常状态
        memberAsk.setStatus(DeleteStatusEnum.NORMAL.value());
        memberAsk.setSellerId(goods.getSellerId());
        //商家回复状态默认为未回复
        memberAsk.setReplyStatus(CommonStatusEnum.NO.value());

        if (!CommonStatusEnum.YES.value().equals(anonymous) && !CommonStatusEnum.NO.value().equals(anonymous)) {
            throw new ServiceException(MemberErrorCode.E107.code(), "是否匿名参数值不正确");
        }
        memberAsk.setAnonymous(anonymous);
        memberAsk.setMemberName(CommonStatusEnum.YES.value().equals(anonymous) ? "匿名" : member.getUname());
        memberAsk.setMemberFace(member.getFace());

        //获取商品咨询设置
        String json = this.settingClient.get(SettingGroup.GOODS);
        GoodsSettingVO goodsSettingVO = JsonUtil.jsonToObject(json,GoodsSettingVO.class);
        memberAsk.setAuthStatus(goodsSettingVO.getAskAuth().intValue() == 0 ? AuditEnum.PASS_AUDIT.name() : AuditEnum.WAIT_AUDIT.name());

        this.daoSupport.insert(memberAsk);
        memberAsk.setAskId(this.daoSupport.getLastId(""));

        //如果平台没有开启会员咨询审核，那么就直接发送消息
        if (goodsSettingVO.getAskAuth().intValue() == 0) {
            List<MemberAsk> list = new ArrayList<>();
            list.add(memberAsk);
            this.sendMessage(list);
        }

        return memberAsk;
    }



    @Override
    public MemberAsk getModel(Integer askId) {
        return this.daoSupport.queryForObject(MemberAsk.class, askId);
    }

    @Override
    public MemberAskVO getModelVO(Integer askId) {
        MemberAsk memberAsk = this.getModel(askId);

        if (memberAsk == null) {
            throw new ServiceException(MemberErrorCode.E202.code(), "会员商品咨询信息不存在");
        }

        MemberAskVO memberAskVO = new MemberAskVO();
        BeanUtil.copyProperties(memberAsk, memberAskVO);

        //设置商品信息
        CacheGoods goods = goodsClient.getFromCache(memberAskVO.getGoodsId());
        memberAskVO.setGoodsPrice(goods.getPrice());
        memberAskVO.setCommentNum(goods.getCommentNum());
        memberAskVO.setPraiseRate(goods.getGrade());

        return memberAskVO;
    }

    @Override
    public Page listGoodsAsks(Integer pageNo, Integer pageSize, Integer goodsId) {
        StringBuffer sqlBuffer = new StringBuffer("select * from es_member_ask where status = ? and goods_id = ? and reply_num > ? order by create_time,reply_num desc");
        List<Object> term = new ArrayList<Object>();
        term.add(DeleteStatusEnum.NORMAL.value());
        term.add(goodsId);
        term.add(0);

        Page page = this.daoSupport.queryForPage(sqlBuffer.toString(), pageNo, pageSize, MemberAskVO.class, term.toArray());

        List<MemberAskVO> memberAskVOS = page.getData();
        for (MemberAskVO memberAskVO : memberAskVOS) {
            memberAskVO.setFirstReply(this.askReplyManager.getNewestModel(memberAskVO.getAskId()));
        }

        return page;
    }

    @Override
    public List<MemberAsk> listRelationAsks(Integer askId, Integer goodsId) {
        String sql = "select * from es_member_ask where auth_status = ? and ask_id != ? and goods_id = ? and reply_num > 0 order by rand() limit 10";
        return this.daoSupport.queryForList(sql, MemberAsk.class, AuditEnum.PASS_AUDIT.value(), askId, goodsId);
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void batchAudit(BatchAuditVO batchAuditVO) {
        // 校验是否有管理端权限
        Admin admin = AdminUserContext.getAdmin();

        if(admin == null ){
            throw new NoPermissionException("没有权限审核会员咨询信息!");
        }

        if (batchAuditVO.getIds() == null || batchAuditVO.getIds().length == 0) {
            throw new ServiceException(MemberErrorCode.E107.code(), "请选择要进行审核的会员商品咨询");
        }

        if (!AuditEnum.PASS_AUDIT.value().equals(batchAuditVO.getAuthStatus()) && !AuditEnum.REFUSE_AUDIT.value().equals(batchAuditVO.getAuthStatus())) {
            throw new ServiceException(MemberErrorCode.E107.code(), "审核状态参数值不正确");
        }

        List<Object> term = new ArrayList<>();
        String idStr = SqlUtil.getInSql(batchAuditVO.getIds(), term);

        String sql = "select * from es_member_ask where ask_id in (" + idStr + ")";
        List<MemberAsk> memberAskList = this.daoSupport.queryForList(sql, MemberAsk.class, term.toArray());

        for (MemberAsk memberAsk : memberAskList) {
            if (!AuditEnum.WAIT_AUDIT.value().equals(memberAsk.getAuthStatus())) {
                throw new ServiceException(MemberErrorCode.E107.code(), "内容为【"+memberAsk.getContent()+"】的咨询不是可以进行审核的状态");
            }

            this.daoSupport.execute("update es_member_ask set auth_status = ? where ask_id = ? ", batchAuditVO.getAuthStatus(), memberAsk.getAskId());
        }

        //发送消息
        this.sendMessage(memberAskList);
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delete(Integer askId) {
        //将状态变成已删除状态
        String sql = "update es_member_ask set status = ? where ask_id = ?";
        this.daoSupport.execute(sql, DeleteStatusEnum.DELETED.value(), askId);

        //同时删除咨询问题的回复和发送的站内消息
        this.askReplyManager.deleteByAskId(askId);
        this.askMessageManager.deleteByAskId(askId);
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public MemberAsk reply(String replyContent, Integer askId) {

        if (StringUtil.isEmpty(replyContent)) {
            throw new ServiceException(MemberErrorCode.E202.code(), "回复内容不能为空");
        }

        if (replyContent.length() < 3 || replyContent.length() > 120) {
            throw new ServiceException(MemberErrorCode.E202.code(), "回复内容应在3到120个字符之间");
        }

        Seller seller = UserContext.getSeller();

        MemberAsk ask = this.getModel(askId);
        if (ask == null || !seller.getSellerId().equals(ask.getSellerId())) {
            throw new ServiceException(MemberErrorCode.E200.code(), "无权回复");
        }

        if (CommonStatusEnum.YES.value().equals(ask.getReplyStatus())) {
            throw new ServiceException(MemberErrorCode.E202.code(), "不可重复回复");
        }

        ask.setReply(replyContent);
        ask.setReplyStatus(CommonStatusEnum.YES.value());
        ask.setReplyTime(DateUtil.getDateline());
        ask.setReplyNum(ask.getReplyNum() + 1);

        this.daoSupport.update(ask, askId);

        return ask;
    }

    @Override
    public void updateReplyNum(Integer askId, Integer num) {
        String sql = "update es_member_ask set reply_num = reply_num + ? where ask_id = ?";
        this.daoSupport.execute(sql, num, askId);
    }

    @Override
    public Integer getNoReplyCount(Integer sellerId) {

        StringBuffer sqlBuffer = new StringBuffer("select count(0) from es_member_ask c where c.status = ? ");
        sqlBuffer.append(" and  c.reply_status = ? and seller_id = ? and auth_status = ?");

        return this.daoSupport.queryForInt(sqlBuffer.toString(),DeleteStatusEnum.NORMAL.value(), CommonStatusEnum.NO.value(), sellerId, AuditEnum.PASS_AUDIT.name());
    }

    /**
     * 消息发送
     * @param memberAskList
     */
    private void sendMessage(List<MemberAsk> memberAskList) {
        MemberAskMessage memberAskMessage = new MemberAskMessage();
        memberAskMessage.setMemberAsks(memberAskList);
        memberAskMessage.setSendTime(DateUtil.getDateline());
        this.amqpTemplate.convertAndSend(AmqpExchange.MEMBER_GOODS_ASK, AmqpExchange.MEMBER_GOODS_ASK + "_ROUTING", memberAskMessage);
    }
}
