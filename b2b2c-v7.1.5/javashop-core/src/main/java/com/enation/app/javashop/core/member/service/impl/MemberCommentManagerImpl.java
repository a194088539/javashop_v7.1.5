package com.enation.app.javashop.core.member.service.impl;

import com.enation.app.javashop.core.base.CharacterConstant;
import com.enation.app.javashop.core.base.SettingGroup;
import com.enation.app.javashop.core.base.message.GoodsCommentMsg;
import com.enation.app.javashop.core.base.rabbitmq.AmqpExchange;
import com.enation.app.javashop.core.client.system.SettingClient;
import com.enation.app.javashop.core.client.trade.OrderClient;
import com.enation.app.javashop.core.goods.model.dto.GoodsSettingVO;
import com.enation.app.javashop.core.goods.model.enums.Permission;
import com.enation.app.javashop.core.goods.service.GoodsGalleryManager;
import com.enation.app.javashop.core.member.MemberErrorCode;
import com.enation.app.javashop.core.member.model.dos.*;
import com.enation.app.javashop.core.member.model.dto.AdditionalCommentDTO;
import com.enation.app.javashop.core.member.model.dto.CommentDTO;
import com.enation.app.javashop.core.member.model.dto.CommentQueryParam;
import com.enation.app.javashop.core.member.model.dto.CommentScoreDTO;
import com.enation.app.javashop.core.member.model.enums.AuditEnum;
import com.enation.app.javashop.core.member.model.enums.CommentGrade;
import com.enation.app.javashop.core.member.model.enums.CommentTypeEnum;
import com.enation.app.javashop.core.member.model.vo.BatchAuditVO;
import com.enation.app.javashop.core.member.model.vo.CommentVO;
import com.enation.app.javashop.core.member.model.vo.GoodsGrade;
import com.enation.app.javashop.core.member.model.vo.MemberCommentCount;
import com.enation.app.javashop.core.member.service.*;
import com.enation.app.javashop.core.system.sensitiveutil.SensitiveFilter;
import com.enation.app.javashop.core.trade.order.model.enums.CommentStatusEnum;
import com.enation.app.javashop.core.trade.sdk.model.OrderDetailDTO;
import com.enation.app.javashop.core.trade.sdk.model.OrderSkuDTO;
import com.enation.app.javashop.framework.context.AdminUserContext;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.exception.NoPermissionException;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.security.model.Admin;
import com.enation.app.javashop.framework.security.model.Buyer;
import com.enation.app.javashop.framework.util.*;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
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
 * 评论业务类
 *
 * @author fk
 * @version v1.0
 * @since v7.0.0
 * 2018-05-03 10:19:14
 */
@Service
public class MemberCommentManagerImpl implements MemberCommentManager {

    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport daoSupport;
    @Autowired
    private MemberShopScoreManager memberShopScoreManager;
    @Autowired
    private CommentGalleryManager commentGalleryManager;
    @Autowired
    private CommentReplyManager commentReplyManager;
    @Autowired
    private GoodsGalleryManager goodsGalleryManager;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private OrderClient orderClient;
    @Autowired
    private MemberManager memberManager;
    @Autowired
    private SettingClient settingClient;

    @Override
    public Page list(CommentQueryParam param) {

        StringBuffer sqlBuffer = new StringBuffer("select * from es_member_comment where status = 1");
        List<Object> term = new ArrayList<Object>();

        // 按会员ID查询
        if (param.getMemberId() != null && param.getMemberId().intValue() != 0) {
            sqlBuffer.append(" and member_id = ?");
            term.add(param.getMemberId());
        }

        // 按商家ID查询
        if (param.getSellerId() != null && param.getSellerId().intValue() != 0) {
            sqlBuffer.append(" and seller_id = ?");
            term.add(param.getSellerId());
        }

        // 按商品ID查询
        if (param.getGoodsId() != null && param.getGoodsId().intValue() != 0) {
            sqlBuffer.append(" and goods_id = ?");
            term.add(param.getGoodsId());
        }

        // 按关键字查询
        if (StringUtil.notEmpty(param.getKeyword())) {
            sqlBuffer.append(" and (content like ? or goods_name like ? or member_name like ?)");
            term.add("%" + param.getKeyword() + "%");
            term.add("%" + param.getKeyword() + "%");
            term.add("%" + param.getKeyword() + "%");
        }

        // 按商品名称查询
        if (StringUtil.notEmpty(param.getGoodsName())) {
            sqlBuffer.append(" and goods_name like ?");
            term.add("%" + param.getGoodsName() + "%");
        }

        // 按会员名称查询
        if (StringUtil.notEmpty(param.getMemberName())) {
            sqlBuffer.append(" and member_name like ?");
            term.add("%" + param.getMemberName() + "%");
        }

        // 按评价等级查询
        if (StringUtil.notEmpty(param.getGrade())) {
            sqlBuffer.append(" and grade = ?");
            term.add(param.getGrade());
        }

        // 按审核状态查询
        if (StringUtil.notEmpty(param.getAuditStatus())) {
            sqlBuffer.append(" and audit_status = ?");
            term.add(param.getAuditStatus());
        }

        // 按评价类型查询
        if (StringUtil.notEmpty(param.getCommentsType())) {
            sqlBuffer.append(" and comments_type = ?");
            term.add(param.getCommentsType());
        }

        // 按是否有图查询
        if (param.getHaveImage() != null) {
            sqlBuffer.append(" and have_image = ?");
            term.add(param.getHaveImage());
        }

        // 按回复状态查询
        if (param.getReplyStatus() != null) {
            sqlBuffer.append(" and reply_status = ?");
            term.add(param.getReplyStatus());
        }

        // 按评论日期查询
        if (param.getStartTime() != null) {
            sqlBuffer.append(" and create_time >= ?");
            term.add(param.getStartTime());
        }

        // 按评论日期查询
        if (param.getEndTime() != null) {
            sqlBuffer.append(" and create_time <= ?");
            term.add(param.getEndTime());
        }

        // 按评论状态查询(主要应用于会员中心--评论管理)
        if(StringUtil.notEmpty(param.getCommentStatus())){
            if (CommentStatusEnum.WAIT_CHASE.name().equals(param.getCommentStatus())) {
                //待追评:评论为初评且审核通过且未追评过
                sqlBuffer.append(" and comment_id not in (select parent_id from es_member_comment where comments_type = ? and status = 1)");
                term.add(CommentTypeEnum.ADDITIONAL.name());
            } else if (CommentStatusEnum.FINISHED.name().equals(param.getCommentStatus())) {
                sqlBuffer.append(" and parent_id = 0  and comments_type = ?");
                term.add(CommentTypeEnum.INITIAL.name());
            }

        }

        // 按是否有追评查询(主要应用于商品详情页面评论列表数据展示)
        if (param.getHaveAdditional() != null && param.getHaveAdditional().intValue() == 1) {
            sqlBuffer.append(" and comment_id in (select parent_id from es_member_comment where comments_type = ? and goods_id = ? and audit_status = ? and status = 1)");
            term.add(CommentTypeEnum.ADDITIONAL.name());
            term.add(param.getGoodsId());
            term.add(param.getAuditStatus());
        }

        sqlBuffer.append(" order by create_time desc ");

        Page<CommentVO> webPage = this.daoSupport.queryForPage(sqlBuffer.toString(), param.getPageNo(), param.getPageSize(), CommentVO.class, term.toArray());

        List<CommentVO> list = webPage.getData();
        if (StringUtil.isNotEmpty(list)) {
            // 找出有图片和回复过的评论id
            for (CommentVO comment : list) {
                /*
                 * 获取评论图片 ， 回复 ，追评信息
                 */
                this.getCommentVO(comment);
            }

        }

        return new Page(param.getPageNo(), webPage.getDataTotal(), param.getPageSize(), list);

    }


    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public MemberComment add(CommentScoreDTO comment, Permission permission) {

        OrderDetailDTO orderDetail = orderClient.getModel(comment.getOrderSn());
        // 不存在的订单/不是我的订单
        if (Permission.BUYER.equals(permission)) {
            Buyer member = UserContext.getBuyer();
            if (orderDetail == null || !member.getUid().equals(orderDetail.getMemberId())) {
                throw new NoPermissionException( "没有操作权限");
            }
        }

        if (!orderDetail.getOrderOperateAllowableVO().getAllowComment()) {
            throw new NoPermissionException( "没有操作权限");
        }

        // 添加店铺评分
        MemberShopScore shopScore = new MemberShopScore();
        BeanUtils.copyProperties(comment, shopScore);
        shopScore.setMemberId(orderDetail.getMemberId());
        shopScore.setSellerId(orderDetail.getSellerId());
        this.memberShopScoreManager.add(shopScore);

        // 添加评论
        this.add(comment.getComments(), orderDetail,false);

        return null;
    }

    /**
     * 添加评论
     *
     * @param commentList 发起的评论
     * @param orderDetail 订单
     */
    private void add(List<CommentDTO> commentList, OrderDetailDTO orderDetail,Boolean isAutoComment) {

        Map<Integer, Object> skuMap = new HashMap<Integer, Object>(orderDetail.getOrderSkuList().size());
        // 将product循环放入map
        for (OrderSkuDTO sku : orderDetail.getOrderSkuList()) {
            skuMap.put(sku.getSkuId(), sku);
        }
        List<MemberComment> comments = new ArrayList<>();

        //获取商品评论设置
        String json = this.settingClient.get(SettingGroup.GOODS);
        GoodsSettingVO goodsSettingVO = JsonUtil.jsonToObject(json,GoodsSettingVO.class);

        for (CommentDTO comment : commentList) {
            OrderSkuDTO product = (OrderSkuDTO) skuMap.get(comment.getSkuId());
            if (product == null) {
                throw new NoPermissionException("没有操作权限");
            }
            MemberComment memberComment = new MemberComment();
            BeanUtils.copyProperties(comment, memberComment);
            Member member = memberManager.getModel(orderDetail.getMemberId());
            //评论内容敏感词过滤
            memberComment.setContent(SensitiveFilter.filter(memberComment.getContent(), CharacterConstant.WILDCARD_STAR));
            memberComment.setMemberFace(member.getFace());
            memberComment.setGoodsId(product.getGoodsId());
            memberComment.setCreateTime(DateUtil.getDateline());
            memberComment.setMemberId(orderDetail.getMemberId());
            memberComment.setStatus(1);
            memberComment.setReplyStatus(0);
            memberComment.setSellerId(product.getSellerId());
            memberComment.setGoodsName(product.getName());
            memberComment.setGoodsImg(product.getGoodsImage());
            memberComment.setMemberName(member.getUname());
            memberComment.setOrderSn(orderDetail.getSn());
            memberComment.setCommentsType(CommentTypeEnum.INITIAL.name());

            //如果平台未开启商品评论审核功能，那么评论默认为审核通过状态
            if (goodsSettingVO.getCommentAuth().intValue() == 0) {
                memberComment.setAuditStatus(AuditEnum.PASS_AUDIT.name());
            } else {
                //如果平台开启了商品评论审核功能，那么评论（除系统自动好评之外，系统自动好评不用审核）默认为待审核状态
                memberComment.setAuditStatus(isAutoComment ? AuditEnum.PASS_AUDIT.name() : AuditEnum.WAIT_AUDIT.name());
            }

            memberComment.setParentId(0);

            // 是否有图片
            memberComment.setHaveImage(StringUtil.isNotEmpty(comment.getImages()) ? 1 : 0);

            if (CommentGrade.good.name().equals(comment.getGrade()) && StringUtil.isEmpty(memberComment.getContent())) {

                memberComment.setContent("此评论默认好评！！");
                //默认好评自动审核通过  update by liuyulei 2019-07-24
                memberComment.setAuditStatus(AuditEnum.PASS_AUDIT.name());
            }

            if (!CommentGrade.good.name().equals(comment.getGrade()) && StringUtil.isEmpty(memberComment.getContent())) {

                throw new ServiceException(MemberErrorCode.E201.code(), "非好评评论必填");
            }

            this.daoSupport.insert(memberComment);

            int commentId = this.daoSupport.getLastId("es_member_comment");
            memberComment.setCommentId(commentId);

            //添加图片
            this.commentGalleryManager.add(commentId, comment.getImages());
            comments.add(memberComment);
        }


        comments.forEach(comment -> {
            //如果评论不为空  且是初评
            if (comment != null && CommentTypeEnum.INITIAL.name().equals(comment.getCommentsType())) {
                // 更改订单的评论状态，同步更改 ，避免重复评论
                orderClient.updateItemsCommentStatus(comment.getOrderSn(),comment.getGoodsId(), CommentStatusEnum.WAIT_CHASE);
            }
        });

        //如果是系统自动评价，那么直接发送消息;如果不是系统自动评价，那么还需判断平台是否开启了商品评论审核功能
        if (isAutoComment) {
            sendCommentMsg(true, comments, GoodsCommentMsg.ADD);
        } else {
            //如果平台未开启商品评论审核功能，那么需要直接发送评论消息
            if (goodsSettingVO.getCommentAuth().intValue() == 0) {
                sendCommentMsg(false, comments, GoodsCommentMsg.ADD);
            }
        }
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public MemberComment edit(MemberComment memberComment, Integer id) {
        this.daoSupport.update(memberComment, id);
        return memberComment;
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delete(Integer id) {
        MemberComment memberComment = this.getModel(id);
        if (memberComment == null || memberComment.getStatus() == 0) {
            throw new NoPermissionException("没有操作权限");
        }
        String sql = "update es_member_comment set status  = 0  where comment_id = ? ";
        this.daoSupport.execute(sql, id);

        List<MemberComment> commentList = new ArrayList<>();
        commentList.add(memberComment);

        //发送删除评论消息
        this.sendCommentMsg(false, commentList, GoodsCommentMsg.DELETE);
    }

    @Override
    public MemberComment getModel(Integer id) {
        return this.daoSupport.queryForObject(MemberComment.class, id);
    }

    @Override
    public List<GoodsGrade> queryGoodsGrade() {

        String sql = " select goods_id, sum( CASE grade WHEN '" + CommentGrade.good.name() + "' THEN 1 ELSE 0  END ) /count(*) good_rate " +
                " from es_member_comment where status = 1 group by goods_id";

        List<GoodsGrade> goodsList = this.daoSupport.queryForList(sql, GoodsGrade.class);

        return goodsList;
    }

    @Override
    public Integer getGoodsCommentCount(Integer goodsId) {
        String sql = "SELECT COUNT(0) FROM es_member_comment where goods_id = ? and status = 1";
        return this.daoSupport.queryForInt(sql, goodsId);
    }

    @Override
    public void autoGoodComments(List<OrderDetailDTO> detailDTOList) {

        // 查询过期没有评论订单
        List<OrderDetailDTO> list = detailDTOList;

        // 循环订单的商品自动给好评
        if (StringUtil.isNotEmpty(list)) {
            for (OrderDetailDTO orderDetail : list) {
                // 添加店铺评分
                MemberShopScore shopScore = new MemberShopScore();
                shopScore.setDeliveryScore(5);
                shopScore.setDescriptionScore(5);
                shopScore.setServiceScore(5);
                shopScore.setOrderSn(orderDetail.getSn());
                shopScore.setMemberId(orderDetail.getMemberId());
                shopScore.setSellerId(orderDetail.getSellerId());
                this.memberShopScoreManager.add(shopScore);
                //  添加商品评分
                List<OrderSkuDTO> skuList = orderDetail.getOrderSkuList();
                List<CommentDTO> commentList = new ArrayList<>();

                for (OrderSkuDTO sku : skuList) {
                    CommentDTO comment = new CommentDTO();
                    comment.setSkuId(sku.getSkuId());
                    comment.setGrade(CommentGrade.good.name());
                    comment.setContent("此商品默认好评");
                    comment.setImages(null);
                    commentList.add(comment);
                }
                this.add(commentList, orderDetail,true);
            }
        }
    }

    @Override
    public MemberCommentCount count(Integer goodsId) {

        //商品的评论数量--评论必须是审核通过的状态
        String sql = "select count(1) count,grade,have_image from es_member_comment where goods_id = ? and audit_status = ?  group by grade,have_image";
        List<Map> list = this.daoSupport.queryForList(sql, goodsId, AuditEnum.PASS_AUDIT.name());

        Integer allCount = 0;
        Integer goodCount = 0;
        Integer neutralCount = 0;
        Integer badCount = 0;
        Integer imageCount = 0;

        if (StringUtil.isNotEmpty(list)) {
            for (Map map : list) {
                String grade = map.get("grade").toString();
                Integer count = Integer.valueOf(map.get("count").toString());
                allCount += count;
                switch (grade) {
                    case "good":
                        goodCount += count;
                        break;
                    case "neutral":
                        neutralCount += count;
                        break;
                    case "bad":
                        badCount += count;
                        break;
                    default:
                        break;
                }
                //图片评论的数量
                Integer haveImage = (Integer) map.get("have_image");
                if (haveImage == 1) {
                    imageCount += count;
                }
            }
        }

        return new MemberCommentCount(allCount, goodCount, neutralCount, badCount, imageCount);
    }


    @Override
    public void editComment(Integer memberId, String face) {
        this.daoSupport.execute("update es_member_comment set member_face = ? where member_id = ?", face, memberId);
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public List<AdditionalCommentDTO> additionalComments(List<AdditionalCommentDTO> comments, Permission permission) {
        List<MemberComment> commentsList = new ArrayList<>();

        if(comments == null || comments.isEmpty()){
            throw new ServiceException(MemberErrorCode.E200.code(), "请补充待追评数据后再提交！");
        }

        //获取商品评论设置
        String json = this.settingClient.get(SettingGroup.GOODS);
        GoodsSettingVO goodsSettingVO = JsonUtil.jsonToObject(json,GoodsSettingVO.class);

        for (AdditionalCommentDTO commentDTO : comments) {
            MemberComment memberComment = this.getModel(commentDTO.getCommentId());

            //如果会员评论为空或者评论已删除，则不允许添加追评
            if (memberComment == null || memberComment.getStatus().intValue() == 0) {
                throw new ServiceException(MemberErrorCode.E200.code(), "没有权限");
            }

            MemberComment additional = this.getAdditionalById(memberComment.getCommentId());
            if (additional != null) {
                throw new ServiceException(MemberErrorCode.E200.code(), "已追加过评论，无法再次追加评论！");
            }

            if (!AuditEnum.PASS_AUDIT.name().equals(memberComment.getAuditStatus())) {
                throw new ServiceException(MemberErrorCode.E200.code(), "初评未审核或审核拒绝，无法追评！");
            }

            // 验证权限
            if (Permission.BUYER.equals(permission)) {
                Buyer member = UserContext.getBuyer();
                if (!member.getUid().equals(memberComment.getMemberId())) {
                    throw new NoPermissionException("没有操作权限");
                }
            }

            if (StringUtil.isEmpty(commentDTO.getContent())) {
                throw new ServiceException(MemberErrorCode.E201.code(), "追加的评论内容不能为空");
            }

            Integer haveImage = 0;

            //判断追评是否包含评论信息
            if (commentDTO.getImages() != null && commentDTO.getImages().size() > 0) {
                haveImage = 1;
            }

            //如果平台没有开启商品评论审核功能，那么商品评论默认为审核通过
            if (goodsSettingVO.getCommentAuth().intValue() == 0) {
                memberComment.setAuditStatus(AuditEnum.PASS_AUDIT.name());
            } else {
                memberComment.setAuditStatus(AuditEnum.WAIT_AUDIT.name());
            }

            //追评内容敏感词过滤
            memberComment.setContent(SensitiveFilter.filter(commentDTO.getContent(),CharacterConstant.WILDCARD_STAR));
            memberComment.setParentId(commentDTO.getCommentId());
            memberComment.setCreateTime(DateUtil.getDateline());
            memberComment.setHaveImage(haveImage);
            memberComment.setCommentsType(CommentTypeEnum.ADDITIONAL.name());
            memberComment.setStatus(1);
            memberComment.setReplyStatus(0);

            this.daoSupport.insert(memberComment);
            memberComment.setCommentId(this.daoSupport.getLastId("es_member_comment"));

            if (haveImage == 1) {
                //添加图片
                this.commentGalleryManager.add(memberComment.getCommentId(), commentDTO.getImages());
            }
            commentsList.add(memberComment);
            //追评完成后，修改订单评论状态为已完成，即只允许一次追评  ，审核只是为了是否在前端展示
            this.orderClient.updateItemsCommentStatus(memberComment.getOrderSn(),memberComment.getGoodsId(),CommentStatusEnum.FINISHED);

        }

        //发送追评消息
        this.sendCommentMsg(false, commentsList, GoodsCommentMsg.ADD);

        return comments;
    }

    @Override
    public MemberComment getAdditionalById(Integer commentId) {
        int count = this.daoSupport.queryForInt("select count(1) from es_member_comment where parent_id = ? and status = 1", commentId);
        if (count > 0) {
            return this.daoSupport.queryForObject("select * from es_member_comment where parent_id = ? and comments_type = ? and status = 1 ",
                    MemberComment.class, commentId, CommentTypeEnum.ADDITIONAL.name());
        }
        return null;
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void batchAudit(BatchAuditVO batchAuditVO) {
        Admin admin = AdminUserContext.getAdmin();

        //判断是否为管理员操作
        if (admin == null) {
            throw new NoPermissionException("没有操作权限");
        }

        if (batchAuditVO.getIds() == null || batchAuditVO.getIds().length == 0) {
            throw new ServiceException(MemberErrorCode.E201.code(), "请选择要进行审核的会员商品评论");
        }

        if (!AuditEnum.PASS_AUDIT.value().equals(batchAuditVO.getAuthStatus()) && !AuditEnum.REFUSE_AUDIT.value().equals(batchAuditVO.getAuthStatus())) {
            throw new ServiceException(MemberErrorCode.E201.code(), "审核状态参数值不正确");
        }

        List<Object> term = new ArrayList<>();
        String idStr = SqlUtil.getInSql(batchAuditVO.getIds(), term);

        String sql = "select * from es_member_comment where comment_id in (" + idStr + ")";
        List<MemberComment> memberCommentList = this.daoSupport.queryForList(sql, MemberComment.class, term.toArray());

        for (MemberComment memberComment : memberCommentList) {
            if (!AuditEnum.WAIT_AUDIT.value().equals(memberComment.getAuditStatus())) {
                throw new ServiceException(MemberErrorCode.E200.code(), "内容为【"+memberComment.getContent()+"】的评论不是可以进行审核的状态");
            }

            this.daoSupport.execute("update es_member_comment set audit_status = ? where comment_id = ? ", batchAuditVO.getAuthStatus(), memberComment.getCommentId());
        }

        //发送评论消息
        this.sendCommentMsg(false, memberCommentList, GoodsCommentMsg.ADD);
    }

    @Override
    public CommentVO get(Integer commentId) {

        MemberComment memberComment = this.getModel(commentId);

        if(memberComment == null ){
            return null;
        }

        CommentVO commentVO = new CommentVO();

        BeanUtil.copyProperties(memberComment,commentVO);

        this.getCommentVO(commentVO);

        return commentVO;
    }

    @Override
    public List<CommentVO> get(String orderSn,Integer skuId) {
        StringBuffer sql = new StringBuffer("select * from es_member_comment where order_sn = ? ");

        List<Object>  term = new ArrayList<>();
        term.add(orderSn);

        if(skuId != null ){
            sql.append(" and sku_id = ? ");
            term.add(skuId);
        }

        sql.append(" and comments_type = ? ");
        term.add(CommentTypeEnum.INITIAL.name());

        List<CommentVO> comments = this.daoSupport.queryForList(sql.toString(),CommentVO.class,term.toArray());

        comments.forEach(comment -> {
            this.getCommentVO(comment);
        });

        return comments;
    }

    /**
     * 获取评论图片和回复信息下
     * @param comment
     * @return
     */
    private CommentVO getCommentVO(CommentVO comment) {

        if (comment.getHaveImage() == 1) {
            Map<Integer, List<String>> map = this.commentGalleryManager.getGalleryByCommentIds(comment.getCommentId());
            comment.setImages(map.get(comment.getCommentId()));
        }

        //找出评论回复信息
        if (comment.getReplyStatus() == 1) {
            CommentReply reply = this.commentReplyManager.getReply(comment.getCommentId());
            comment.setReply(reply);
        }

        //获取店铺评分信息
        comment.setMemberShopScore(this.memberShopScoreManager.getModel(comment.getMemberId(), comment.getOrderSn()));

        //如果评论为初评则获取评论追评信息
        if (CommentTypeEnum.INITIAL.value().equals(comment.getCommentsType())) {
            this.getAdditinalInfo(comment);
        }
        return comment;
    }

    /**
     * 获取初评所属的追评信息
     * @param comment
     */
    private void getAdditinalInfo(CommentVO comment) {
        MemberComment memberComment = this.getAdditionalById(comment.getCommentId());

        if (memberComment != null) {

            CommentVO commentVO = new CommentVO();

            BeanUtil.copyProperties(memberComment,commentVO);

            this.getCommentVO(commentVO);

            comment.setAdditionalComment(commentVO);
        }
    }

    /**
     * 发送商品评论完成消息
     * @param isAutoComment 是否为系统自动评论
     * @param comments 评论信息集合
     * @param operaType 操作类型
     */
    private void sendCommentMsg(Boolean isAutoComment, List<MemberComment> comments, int operaType) {
        GoodsCommentMsg goodsCommentMsg = new GoodsCommentMsg();
        goodsCommentMsg.setComment(comments);
        goodsCommentMsg.setAutoComment(isAutoComment);
        goodsCommentMsg.setOperaType(operaType);
        this.amqpTemplate.convertAndSend(AmqpExchange.GOODS_COMMENT_COMPLETE, AmqpExchange.GOODS_COMMENT_COMPLETE + "_ROUTING", goodsCommentMsg);
    }
}
