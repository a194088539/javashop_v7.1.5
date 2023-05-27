package com.enation.app.javashop.core.orderbill.service.impl;

import com.enation.app.javashop.core.client.distribution.DistributionSellerBillClient;
import com.enation.app.javashop.core.client.member.ShopClient;
import com.enation.app.javashop.core.distribution.model.dto.DistributionSellerBillDTO;
import com.enation.app.javashop.core.goods.model.enums.Permission;
import com.enation.app.javashop.core.orderbill.OrderBillErrorCode;
import com.enation.app.javashop.core.orderbill.model.dos.BillItem;
import com.enation.app.javashop.core.orderbill.model.enums.BillStatusEnum;
import com.enation.app.javashop.core.orderbill.model.enums.BillType;
import com.enation.app.javashop.core.orderbill.model.vo.*;
import com.enation.app.javashop.core.orderbill.service.BillItemManager;
import com.enation.app.javashop.core.shop.model.dto.ShopBankDTO;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.security.model.Seller;
import com.enation.app.javashop.framework.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.core.orderbill.model.dos.Bill;
import com.enation.app.javashop.core.orderbill.service.BillManager;

import java.util.*;

/**
 * 结算单业务类
 *
 * @author fk
 * @version v1.0
 * @since v7.0.0
 * 2018-04-26 16:21:26
 */
@Service
public class BillManagerImpl implements BillManager {

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private BillItemManager billItemManager;

    @Autowired
    private DistributionSellerBillClient distributionSellerBillClient;


    @Autowired
    private ShopClient shopClient;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 结算的前缀
     */
    private static final String BILL_SN_CACHE_PREFIX = "BILL_SN";

    /**
     * 结算的周期账单前缀
     */
    private static final String SN_CACHE_PREFIX = "BILL_SN_TOTAL";

    @Override
    public Page list(int page, int pageSize) {

        String sql = "select * from es_bill  ";
        Page webPage = this.daoSupport.queryForPage(sql, page, pageSize, Bill.class);

        return webPage;
    }

    @Override
    public Page getAllBill(Integer pageNo, Integer pageSize, String sn) {
        StringBuffer sqlBuffer = new StringBuffer("select sn,start_time,end_time,sum(price) price,sum(commi_price)commi_price," +
                "sum(discount_price)discount_price,sum(bill_price)bill_price,sum(refund_price)refund_price,sum(refund_commi_price)refund_commi_price from es_bill");

        List<Object> term = new ArrayList<>();

        if (StringUtil.notEmpty(sn)) {
            sqlBuffer.append(" where sn = ? ");
            term.add(sn);
        }

        sqlBuffer.append(" group by sn,start_time,end_time");
        return this.daoSupport.queryForPage(sqlBuffer.toString(), pageNo, pageSize, term.toArray());
    }


    @Override
    public Page queryBills(BillQueryParam param) {

        StringBuffer sqlBuffer = new StringBuffer("select * from es_bill ");
        List<String> list = new ArrayList<>();
        List<Object> term = new ArrayList<>();

        if (StringUtil.notEmpty(param.getBillSn())) {
            list.add("bill_sn = ?");
            term.add(param.getBillSn());
        }

        if (param.getSellerId() != null && param.getSellerId() != 0) {
            list.add("seller_id = ?");
            term.add(param.getSellerId());
        }

        if (!StringUtil.isEmpty(param.getSn())) {
            list.add("sn = ?");
            term.add(param.getSn());
        }
        sqlBuffer.append(SqlUtil.sqlSplicing(list))
                .append(" order by create_time desc");
        return this.daoSupport.queryForPage(sqlBuffer.toString(), param.getPageNo(), param.getPageSize(), BillDetail.class, term.toArray());
    }

    @Override
    public BillDetail getBillDetail(Integer billId, Permission permission) {

        String sql = "select * from es_bill where bill_id = ? ";
        BillDetail bill = this.daoSupport.queryForObject(sql, BillDetail.class, billId);
        if (bill == null) {
            throw new ServiceException(OrderBillErrorCode.E700.code(), "没有权限");
        }

        //卖家所属权限校验
        if (Permission.SELLER.equals(permission)) {
            Seller seller = UserContext.getSeller();
            if (!bill.getSellerId().equals(seller.getSellerId())) {
                throw new ServiceException(OrderBillErrorCode.E700.code(), "没有权限");
            }
        }

        OperateAllowable allowable = new OperateAllowable(BillStatusEnum.valueOf(bill.getStatus()), permission);
        bill.setOperateAllowable(allowable);

        return bill;
    }

    @Override
    public Bill editStatus(Integer billId, Permission permission) {

        Bill bill = this.getModel(billId);
        if (bill == null) {
            throw new ServiceException(OrderBillErrorCode.E700.code(), "没有权限");
        }

        Map<BillStatusEnum, BillStatusEnum> map = new HashMap(4);
        map.put(BillStatusEnum.OUT, BillStatusEnum.RECON);
        map.put(BillStatusEnum.RECON, BillStatusEnum.PASS);
        map.put(BillStatusEnum.PASS, BillStatusEnum.PAY);
        map.put(BillStatusEnum.PAY, BillStatusEnum.COMPLETE);

        BillStatusEnum status = BillStatusEnum.valueOf(bill.getStatus());
        //通过当前状态得到下一状态
        OperateAllowable allowable = new OperateAllowable(status, permission);
        if (!allowable.getAllowNextStep()) {
            throw new ServiceException(OrderBillErrorCode.E700.code(), status.description() + "状态，您没有权限进项下一步操作");
        }

        //是不是我的账单
        if (Permission.SELLER.equals(permission)) {
            Seller seller = UserContext.getSeller();
            if (!seller.getSellerId().equals(bill.getSellerId())) {
                throw new ServiceException(OrderBillErrorCode.E700.code(), "没有权限");
            }

        }
        BillStatusEnum newStatus = map.get(status);
        bill.setStatus(newStatus.name());
        this.daoSupport.update(bill, billId);

        return bill;

    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Bill add(Bill bill) {

        this.daoSupport.insert(bill);
        bill.setBillId(this.daoSupport.getLastId(""));

        return bill;
    }

    @Override
    public Bill getModel(Integer id) {
        return this.daoSupport.queryForObject(Bill.class, id);
    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void createBills(Long startTime, Long endTime) {

        // 查询所有的卖家
        List<ShopBankDTO> shops = this.shopClient.listShopBankInfo();


        //获取分销商品返现支出
        List<DistributionSellerBillDTO> dsbs = distributionSellerBillClient.countSeller(startTime.intValue(), endTime.intValue());


        // 结束时间
        String lastTime = String.valueOf(endTime);
        // 统计各卖家的结算结果
        Map<Integer, BillResult> billMap = this.billItemManager.countBillResultMap(String.valueOf(startTime), lastTime);

        String sn = "B" + DateUtil.toString(DateUtil.getDateline(), "yyyyMMdd") + new Random().nextInt(5);

        if (shops != null) {
            for (ShopBankDTO shop : shops) {
                Integer sellerId = shop.getShopId();
                //佣金比例
                Double commissionRate = shop.getShopCommission() / 100;

                BillResult billRes = billMap.get(sellerId);
                //为空说明上个结算周期没有响应没有订单记录
                if (billRes == null) {
                    billRes = new BillResult(0.00, 0.00, 0.00, 0.00, sellerId,0.00);
                }
                //创建结算单号
                String billSn = this.createBillSn();
                //在线支付的总收入金额
                Double onlinePrice = billRes.getOnlinePrice();
                //在线支付退款的金额
                Double onlineRefundPrice = billRes.getOnlineRefundPrice();
                // 货到付款的总收入金额
                Double codPrice = billRes.getCodPrice();
                // 货到付款的退款金额
                Double codRefundPrice = billRes.getCodRefundPrice();
                //优惠券佣金
                Double couponCommissionPrice = billRes.getSiteCouponCommi();
                //佣金 在线支付的总收入金额 * 佣金比例
                Double commissionPrice = CurrencyUtil.mul(onlinePrice, commissionRate);
                //退还佣金 在线支付退款的金额 * 佣金比例
                Double refundCommissionPrice = CurrencyUtil.mul(onlineRefundPrice, commissionRate);

                //分销商品返现
                Double distributionGoodsRebate = 0d;
                //分销商品退单返现返还
                Double distributionReturnRebate = 0d;
                if (StringUtil.isNotEmpty(dsbs)) {
                    for (DistributionSellerBillDTO dsb : dsbs) {
                        if (dsb.getSellerId().equals(sellerId)) {
                            if (dsb.getCountExpenditure() != null) {
                                distributionGoodsRebate = dsb.getCountExpenditure();
                            }
                            if (dsb.getReturnExpenditure() != null) {
                                distributionReturnRebate = dsb.getReturnExpenditure();
                            }
                        }
                    }
                }

                //结算周期内订单总收入金额 = 在线支付的总收入金额 + 货到付款的总收入金额
                Double orderTotal = CurrencyUtil.add(onlinePrice, codPrice);

                //结算周期内订单总退款金额 = 在线支付总退款的金额 + 货到付款的总退款金额
                Double refundTotal = CurrencyUtil.add(onlineRefundPrice, codRefundPrice);

                //结算金额 = 结算周期内在线支付订单总收入金额 - 结算周期内在线支付订单总退款金额 - 平台收取的佣金总额 + 平台退还的佣金金额 + 用户使用的平台优惠券的金额
                Double billPrice = CurrencyUtil.sub(CurrencyUtil.sub(onlinePrice, onlineRefundPrice), commissionPrice);
                billPrice = CurrencyUtil.add(billPrice, CurrencyUtil.add(refundCommissionPrice, couponCommissionPrice));

                //分销最终收入 = 分销返现佣金支付金额 - 分销返现佣金退还金额
                Double distributionBillPrice = CurrencyUtil.sub(distributionGoodsRebate, distributionReturnRebate);

                //最终结算金额 = 商家结算 - 分销结算
                billPrice = CurrencyUtil.sub(billPrice, distributionBillPrice);

                Bill bill = new Bill();
                bill.setStartTime(startTime);
                bill.setEndTime(endTime);
                bill.setBankAccountName(shop.getBankAccountName());
                bill.setBankAccountNumber(shop.getBankNumber());
                String bankAddress = createBankAddress(shop);
                bill.setBankAddress(bankAddress);
                bill.setBankCode("");
                bill.setBankName(shop.getBankName());
                bill.setBillSn(billSn);
                bill.setBillType(0);
                bill.setCommiPrice(commissionPrice);
                // 优惠金额目前没有用，没有计算
                bill.setDiscountPrice(0.00);
                bill.setCreateTime(DateUtil.getDateline());
                bill.setStatus(BillStatusEnum.OUT.name());
                bill.setSellerId(sellerId);
                bill.setShopName(shop.getShopName());
                // 总收入 = 在线支付总收入
                bill.setPrice(billRes.getOnlinePrice());
                bill.setRefundCommiPrice(refundCommissionPrice);
                //在线退款金额
                bill.setRefundPrice(onlineRefundPrice);
                bill.setBillPrice(billPrice);
                bill.setCodPrice(codPrice);
                bill.setCodRefundPrice(codRefundPrice);
                bill.setSiteCouponCommi(couponCommissionPrice);
                bill.setSn(sn);
                bill.setDistributionRebate(distributionGoodsRebate);
                bill.setDistributionReturnRebate(distributionReturnRebate);
                bill.setOrderTotalPrice(orderTotal);
                bill.setRefundTotalPrice(refundTotal);
                this.add(bill);
                //更新结算项
                this.billItemManager.updateBillItem(sellerId, bill.getBillId(), String.valueOf(startTime), lastTime);
            }
        }


    }

    /**
     * 创建店铺的银行地址
     *
     * @param shop
     * @return
     */
    private String createBankAddress(ShopBankDTO shop) {

        StringBuffer stringBuffer = new StringBuffer();
        //省
        stringBuffer.append(shop.getBankProvince() == null ? "" : shop.getBankProvince());
        //市
        stringBuffer.append(shop.getBankCity() == null ? "" : shop.getBankCity());
        //区
        stringBuffer.append(shop.getBankCounty() == null ? "" : shop.getBankCounty());
        //镇
        stringBuffer.append(shop.getBankTown() == null ? "" : shop.getBankTown());

        return stringBuffer.toString();
    }

    @Override
    public BillExcel exportBill(Integer billId) {

        BillExcel billExcel = new BillExcel();


        String sql = "select * from es_bill where bill_id = ? ";
        Bill bill = this.daoSupport.queryForObject(sql, Bill.class, billId);
        if (bill == null) {
            throw new ServiceException(OrderBillErrorCode.E700.code(), "没有权限");
        }

        bill.setStatus(BillStatusEnum.valueOf(bill.getStatus()).description());
        billExcel.setBill(bill);

        //订单列表
        sql = "select * from es_bill_item where bill_id = ? and  item_type = ?";
        List<BillItem> orderList = this.daoSupport.queryForList(sql, BillItem.class, billId, BillType.PAYMENT.name());
        billExcel.setOrderList(orderList);
        //退单列表
        List<BillItem> refundList = this.daoSupport.queryForList(sql, BillItem.class, billId, BillType.REFUND.name());
        billExcel.setRefundList(refundList);

        return billExcel;
    }

    /**
     * 新建一个结算编号
     *
     * @return
     */
    private String createBillSn() {

        // 当天的日期
        String timeStr = DateUtil.toString(DateUtil.getDateline(), "yyyyMMdd");

        //组合出当天的Key
        String redisKey = BILL_SN_CACHE_PREFIX + "_" + timeStr;

        //用当天的时间进行自增
        Long sncount = stringRedisTemplate.opsForValue().increment(redisKey, 1);

        String sn;

        if (sncount < 1000000) {
            sn = "000000" + sncount;
            sn = sn.substring(sn.length() - 6, sn.length());
        } else {
            sn = String.valueOf(sncount);
        }

        sn = "B" + timeStr + sn;

        return sn;
    }
}
