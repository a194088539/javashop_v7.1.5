package com.enation.app.javashop.core.promotion.coupon.service.impl;

import com.enation.app.javashop.core.client.goods.GoodsClient;
import com.enation.app.javashop.core.goods.model.vo.CacheGoods;
import com.enation.app.javashop.core.promotion.PromotionErrorCode;
import com.enation.app.javashop.core.promotion.coupon.model.dos.CouponDO;
import com.enation.app.javashop.core.promotion.coupon.model.dto.CouponParams;
import com.enation.app.javashop.core.promotion.coupon.model.enums.CouponType;
import com.enation.app.javashop.core.promotion.coupon.model.enums.CouponUseScope;
import com.enation.app.javashop.core.promotion.coupon.service.CouponManager;
import com.enation.app.javashop.core.promotion.fulldiscount.model.dos.FullDiscountDO;
import com.enation.app.javashop.core.shop.model.vo.ShopChangeMsg;
import com.enation.app.javashop.framework.context.AdminUserContext;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.exception.NoPermissionException;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.exception.SystemErrorCodeV1;
import com.enation.app.javashop.framework.security.model.Seller;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.SqlUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * 优惠券业务类
 *
 * @author Snow
 * @version v2.0
 * @since v7.0.0
 * 2018-04-17 23:19:39
 */
@Service
public class CouponManagerImpl implements CouponManager {

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    private GoodsClient goodsClient;

    @Override
    public Page list(CouponParams couponParams) {

        List params = new ArrayList();

        StringBuffer sql = new StringBuffer("select * from es_coupon where seller_id = ? ");
        params.add(couponParams.getSellerId());

        if (couponParams.getStartTime() != null) {
            sql.append(" and start_time>=? ");
            params.add(couponParams.getStartTime());
        }

        if (couponParams.getEndTime() != null) {
            sql.append(" and end_time<=? ");
            params.add(couponParams.getEndTime());
        }

        if (StringUtil.notEmpty(couponParams.getKeyword())) {
            sql.append(" and title like ? ");
            params.add("%" + couponParams.getKeyword() + "%");
        }

        sql.append(" order by coupon_id desc");
        Page webPage = this.daoSupport.queryForPage(sql.toString(), couponParams.getPageNo(),
                couponParams.getPageSize(), CouponDO.class, params.toArray());

        return webPage;
    }

    @Override
    public List<CouponDO> getListByGoods(Integer goodsId) {

        //查询商品的分类
        CacheGoods goods = goodsClient.getFromCache(goodsId);
        Integer sellerId = goods.getSellerId();
        Integer categoryId = goods.getCategoryId();
        List<Object> term = new ArrayList<>();

        //全品
        String allSql = "use_scope = '" + CouponUseScope.ALL + "'";
        //部分商品
        String someSql = "use_scope = '" + CouponUseScope.SOME_GOODS + "' and concat(',',scope_id,',') like '%," + goodsId + ",%'";
        //分类
        String catSql = "use_scope = '" + CouponUseScope.CATEGORY + "' and concat(',',scope_id,',') like '%," + categoryId + ",%'";


        String sql = "select * from es_coupon where start_time < ? and end_time > ? " +
                " and type = ? and ( seller_id = ?  or (seller_id = 0 and (" + allSql + " or (" + catSql + ") or (" + someSql + "))))";

        long time = DateUtil.getDateline();
        term.add(time);
        term.add(time);
        term.add(CouponType.FREE_GET.name());
        term.add(sellerId);

        return this.daoSupport.queryForList(sql, CouponDO.class, term.toArray());
    }

    @Override
    public List<CouponDO> getList(Integer sellerId) {

        //查询免费领取的优惠券
        String sql = "select * from es_coupon where seller_id = ?  and start_time < ? and end_time > ? and type = ? ";
        List<CouponDO> couponDOList = this.daoSupport.queryForList(sql, CouponDO.class, sellerId,
                DateUtil.getDateline(), DateUtil.getDateline(), CouponType.FREE_GET.name());

        return couponDOList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ServiceException.class, RuntimeException.class, Exception.class})
    public CouponDO add(CouponDO coupon) {

        if (coupon.getLimitNum() < 0) {
            throw new ServiceException(PromotionErrorCode.E406.code(), "限领数量不能为负数");
        }
        //校验每人限领数是都大于发行量
        if (coupon.getLimitNum() > coupon.getCreateNum()) {
            throw new ServiceException(PromotionErrorCode.E405.code(), "限领数量超出发行量");
        }
        //校验优惠券面额是否小于门槛价格
        if (coupon.getCouponPrice() >= coupon.getCouponThresholdPrice()) {
            throw new ServiceException(PromotionErrorCode.E409.code(), "优惠券面额必须小于优惠券门槛价格");
        }

        //开始时间取前段+00:00:00 结束时间取前段+23:59:59
        String startStr = DateUtil.toString(coupon.getStartTime(), "yyyy-MM-dd");
        String endStr = DateUtil.toString(coupon.getEndTime(), "yyyy-MM-dd");

        coupon.setStartTime(DateUtil.getDateline(startStr + " 00:00:00"));
        coupon.setEndTime(DateUtil.getDateline(endStr + " 23:59:59", "yyyy-MM-dd hh:mm:ss"));

        this.paramValid(coupon.getStartTime(), coupon.getEndTime());

        coupon.setReceivedNum(0);
        coupon.setUsedNum(0);
        //部分商品和分类的id存储增加,,
        if(CouponUseScope.SOME_GOODS.name().equals(coupon.getUseScope())||CouponUseScope.CATEGORY.name().equals(coupon.getUseScope())){
            coupon.setScopeId(","+coupon.getScopeId()+",");
        }

        this.daoSupport.insert(coupon);
        int id = this.daoSupport.getLastId("es_coupon");
        coupon.setCouponId(id);

        return coupon;
    }

    @Override
    public CouponDO edit(CouponDO coupon, Integer id) {

        //校验每人限领数是都大于发行量
        if (coupon.getLimitNum() > coupon.getCreateNum()) {
            throw new ServiceException(PromotionErrorCode.E405.code(), "限领数量超出发行量");
        }

        //验证开始结束时间
        this.paramValid(coupon.getStartTime(), coupon.getEndTime());
        //验证优惠券活动是不是不是可更改的状态
        this.verifyStatus(id);
        this.daoSupport.update(coupon, id);
        return coupon;
    }

    @Override
    public void delete(Integer id) {

        this.verifyStatus(id);
        this.daoSupport.delete(CouponDO.class, id);
    }

    @Override
    public CouponDO getModel(Integer id) {

        CouponDO couponDO = this.daoSupport.queryForObject(CouponDO.class, id);

        return couponDO;
    }

    @Override
    public void verifyAuth(Integer id) {
        CouponDO couponDO = this.getModel(id);

        if (couponDO == null) {
            throw new NoPermissionException("无权操作或者数据不存在");
        }

        if (!couponDO.getSellerId().equals(0) && !couponDO.getSellerId().equals(UserContext.getSeller().getSellerId())) {
            throw new NoPermissionException("无权操作或者数据不存在");
        }

        if (couponDO.getSellerId().equals(0) && AdminUserContext.getAdmin() == null) {
            throw new NoPermissionException("无权操作或者数据不存在");
        }
    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void addUsedNum(Integer couponId) {
        String sql = "update es_coupon set used_num = used_num+1 where coupon_id=?";
        this.daoSupport.execute(sql, couponId);
    }


    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void addReceivedNum(Integer couponId) {
        String sql = "update es_coupon set received_num = received_num+1 where coupon_id=?";
        this.daoSupport.execute(sql, couponId);
    }

    @Override
    public Page all(int page, int pageSize, Integer sellerId) {
        Long nowTime = DateUtil.getDateline();
        List params = new ArrayList();

        StringBuffer sql = new StringBuffer("select * from es_coupon where ? >= start_time and ? < end_time and type = ? ");
        params.add(nowTime);
        params.add(nowTime);
        params.add(CouponType.FREE_GET.name());

        if (sellerId != null) {
            sql.append(" and seller_id = ? ");
            params.add(sellerId);
        }

        sql.append(" order by coupon_id desc");
        Page webPage = this.daoSupport.queryForPage(sql.toString(), page, pageSize, CouponDO.class, params.toArray());

        return webPage;
    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void editCouponShopName(Integer shopId, String shopName) {
        //修改优惠券的店铺名称
        String sql = "UPDATE es_coupon SET seller_name = ? where seller_id = ?";
        daoSupport.execute(sql, shopName, shopId);
    }

    @Override
    public List<CouponDO> getByStatus(Integer status) {
        Seller seller = UserContext.getSeller();
        List params = new ArrayList();

        StringBuffer sql = new StringBuffer("select * from es_coupon where seller_id = ? ");
        params.add(seller.getSellerId());

        //获取当前时间
        Long currentTime = DateUtil.getDateline();

        if (status == 1) {
            sql.append(" and end_time >= ? and type = ? and create_num-received_num>0 ");
            params.add(currentTime);
            //活动赠送
            params.add(CouponType.ACTIVITY_GIVE.name());
        } else if (status == 2) {
            sql.append(" and end_time < ?");
            params.add(currentTime);
        }

        return this.daoSupport.queryForList(sql.toString(), CouponDO.class, params.toArray());
    }

    /**
     * 验证是否可修改和删除
     *
     * @param id
     */
    private void verifyStatus(Integer id) {
        CouponDO couponDO = this.getModel(id);
        long nowTime = DateUtil.getDateline();

        //如果当前时间大于起始时间，小于终止时间，标识活动已经开始了，不可修改和删除。
        if (couponDO.getStartTime().longValue() < nowTime && couponDO.getEndTime().longValue() > nowTime) {
            throw new ServiceException(PromotionErrorCode.E400.code(), "活动已经开始，不能进行编辑删除操作");
        }

        String sql = "select * from es_full_discount where is_send_bonus = 1 and bonus_id = ? and seller_id = ?";
        List<FullDiscountDO> list = this.daoSupport.queryForList(sql, FullDiscountDO.class, id, couponDO.getSellerId());
        if (list != null && list.size() != 0) {
            String msg = "";
            for (FullDiscountDO full : list) {
                msg += "【" + full.getTitle() + "】";
            }

            throw new ServiceException(PromotionErrorCode.E400.code(), "当前优惠券参与了促销活动" + msg + "，不能进行编辑删除操作");
        }
    }

    /**
     * 参数验证
     *
     * @param startTime
     * @param endTime
     */
    private void paramValid(Long startTime, Long endTime) {

        long nowTime = DateUtil.getDateline();

        //如果活动起始时间小于现在时间
//        if (startTime.longValue() < nowTime) {
//            throw new ServiceException(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER, "活动起始时间必须大于当前时间");
//        }

        // 开始时间不能大于结束时间
        if (startTime.longValue() > endTime.longValue()) {
            throw new ServiceException(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER, "活动起始时间不能大于活动结束时间");
        }
    }

}
