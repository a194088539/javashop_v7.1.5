package com.enation.app.javashop.core.promotion.fulldiscount.service.impl;

import com.enation.app.javashop.core.goods.model.enums.QuantityType;
import com.enation.app.javashop.core.promotion.PromotionErrorCode;
import com.enation.app.javashop.core.promotion.fulldiscount.model.dos.FullDiscountGiftDO;
import com.enation.app.javashop.core.promotion.fulldiscount.service.FullDiscountGiftManager;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.exception.NoPermissionException;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.security.model.Seller;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 满优惠赠品业务类
 *
 * @author Snow
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-30 17:34:46
 */
@Service
public class FullDiscountGiftManagerImpl implements FullDiscountGiftManager {

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    @Override
    public Page list(int page, int pageSize, String keyWord) {

        Seller seller = UserContext.getSeller();
        String sql = "select * from es_full_discount_gift  where seller_id=? ";

        List<Object> params = new ArrayList<>();

        params.add(seller.getSellerId());


        //如果字段非空
        if (!StringUtil.isEmpty(keyWord)) {
            sql += " and gift_name like ? ";
            params.add("%" + keyWord + "%");

        }
        Page webPage = this.daoSupport.queryForPage(sql, page, pageSize, FullDiscountGiftDO.class, params.toArray());

        return webPage;
    }

    @Override
    public List<FullDiscountGiftDO> listAll() {
        Seller seller = UserContext.getSeller();
        String sql = "select * from es_full_discount_gift  where seller_id=? ";
        return this.daoSupport.queryForList(sql, FullDiscountGiftDO.class, seller.getSellerId());
    }

    @Override
    public FullDiscountGiftDO add(FullDiscountGiftDO fullDiscountGift) {
        this.daoSupport.insert(fullDiscountGift);
        int id = this.daoSupport.getLastId("es_full_discount_gift");
        fullDiscountGift.setGiftId(id);
        return fullDiscountGift;
    }

    @Override
    public FullDiscountGiftDO edit(FullDiscountGiftDO giftDO, Integer id) {
        this.daoSupport.update(giftDO, id);
        return giftDO;
    }

    @Override
    public void delete(Integer id) {
        Seller seller = UserContext.getSeller();
        this.daoSupport.execute("delete from es_full_discount_gift where gift_id=? and seller_id=?", id, seller.getSellerId());
    }

    @Override
    public FullDiscountGiftDO getModel(Integer id) {
        FullDiscountGiftDO giftDO = this.daoSupport.queryForObject(FullDiscountGiftDO.class, id);
        return giftDO;
    }

    @Override
    public void verifyAuth(Integer id) {

        FullDiscountGiftDO fullDiscountGift = this.getModel(id);

        Seller seller = UserContext.getSeller();
        //验证越权操作
        if (fullDiscountGift == null || seller.getSellerId().intValue() != fullDiscountGift.getSellerId().intValue()) {
            throw new NoPermissionException("无权操作");
        }

    }


    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public boolean addGiftQuantity(List<FullDiscountGiftDO> giftDOList) {
        try {
            for (FullDiscountGiftDO giftDO : giftDOList) {
                //当前取消的订单有赠品
                String giftSql = "update es_full_discount_gift set enable_store=enable_store+1 ,actual_store=actual_store+1 where gift_id=?";
                daoSupport.execute(giftSql, giftDO.getGiftId());
            }
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public boolean addGiftEnableQuantity(List<FullDiscountGiftDO> giftDOList) {
        try {
            for (FullDiscountGiftDO giftDO : giftDOList) {
                //当前取消的订单有赠品,增加赠品可用库存
                String giftSql = "update es_full_discount_gift set enable_store=enable_store+1 where gift_id=?";
                daoSupport.execute(giftSql, giftDO.getGiftId());
            }
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public boolean reduceGiftQuantity(List<FullDiscountGiftDO> giftDOList, QuantityType type) {
        try {

            String giftSql = "";
            if (QuantityType.enable.equals(type)) {
                giftSql = "update es_full_discount_gift set enable_store=enable_store-1 where gift_id=? and enable_store>0";
            } else if (QuantityType.actual.equals(type)) {
                giftSql = "update es_full_discount_gift set actual_store=actual_store-1 where gift_id=? and actual_store>0";
            }

            for (FullDiscountGiftDO giftDO : giftDOList) {
                //当前取消的订单有赠品
                daoSupport.execute(giftSql, giftDO.getGiftId());
            }
            return true;

        } catch (Exception e) {
            return false;
        }
    }


    @Override
    public void verifyGift(Integer giftId) {
        Long currentDate = DateUtil.getDateline();
        Integer count = this.daoSupport.queryForInt("select count(1) from es_full_discount where gift_id = ? and start_time <= ? and end_time >= ?  ",
                giftId,currentDate,currentDate);
        if(count > 0){
            throw new ServiceException(PromotionErrorCode.E401.code(),"赠品参与促销活动正在进行中，无法删除！");
        }
    }
}
