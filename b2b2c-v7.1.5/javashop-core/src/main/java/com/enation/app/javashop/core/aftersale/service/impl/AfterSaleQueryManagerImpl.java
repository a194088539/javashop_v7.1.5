package com.enation.app.javashop.core.aftersale.service.impl;

import com.enation.app.javashop.core.aftersale.AftersaleErrorCode;
import com.enation.app.javashop.core.aftersale.model.dos.*;
import com.enation.app.javashop.core.aftersale.model.dto.AfterSaleOrderDTO;
import com.enation.app.javashop.core.aftersale.model.dto.AfterSaleQueryParam;
import com.enation.app.javashop.core.aftersale.model.dto.ServiceOperateAllowable;
import com.enation.app.javashop.core.aftersale.model.enums.AccountTypeEnum;
import com.enation.app.javashop.core.aftersale.model.enums.RefundWayEnum;
import com.enation.app.javashop.core.aftersale.model.enums.ServiceStatusEnum;
import com.enation.app.javashop.core.aftersale.model.enums.ServiceTypeEnum;
import com.enation.app.javashop.core.aftersale.model.vo.AfterSaleExportVO;
import com.enation.app.javashop.core.aftersale.model.vo.AfterSaleRecordVO;
import com.enation.app.javashop.core.aftersale.model.vo.ApplyAfterSaleVO;
import com.enation.app.javashop.core.aftersale.service.*;
import com.enation.app.javashop.core.member.MemberErrorCode;
import com.enation.app.javashop.core.payment.model.dos.PaymentMethodDO;
import com.enation.app.javashop.core.payment.service.PaymentMethodManager;
import com.enation.app.javashop.core.promotion.fulldiscount.model.dos.FullDiscountGiftDO;
import com.enation.app.javashop.core.shop.model.dos.ShopDetailDO;
import com.enation.app.javashop.core.shop.service.ShopManager;
import com.enation.app.javashop.core.system.enums.DeleteStatusEnum;
import com.enation.app.javashop.core.system.model.dos.LogisticsCompanyDO;
import com.enation.app.javashop.core.system.service.LogisticsCompanyManager;
import com.enation.app.javashop.core.trade.TradeErrorCode;
import com.enation.app.javashop.core.trade.order.model.dos.OrderDO;
import com.enation.app.javashop.core.trade.order.model.dos.OrderItemsDO;
import com.enation.app.javashop.core.trade.order.model.dos.OrderMetaDO;
import com.enation.app.javashop.core.trade.order.model.enums.OrderMetaKeyEnum;
import com.enation.app.javashop.core.trade.order.model.enums.OrderServiceStatusEnum;
import com.enation.app.javashop.core.trade.order.model.enums.OrderTypeEnum;
import com.enation.app.javashop.core.trade.order.service.OrderMetaManager;
import com.enation.app.javashop.core.trade.order.service.OrderQueryManager;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.security.model.Buyer;
import com.enation.app.javashop.framework.util.BeanUtil;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.JsonUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 售后服务查询管理接口实现
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-12-04
 */
@Service
public class AfterSaleQueryManagerImpl implements AfterSaleQueryManager {

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    private AfterSaleRefundManager afterSaleRefundManager;

    @Autowired
    private AfterSaleGoodsManager afterSaleGoodsManager;

    @Autowired
    private AfterSaleChangeManager afterSaleChangeManager;

    @Autowired
    private AfterSaleGalleryManager afterSaleGalleryManager;

    @Autowired
    private AfterSaleLogManager afterSaleLogManager;

    @Autowired
    private OrderQueryManager orderQueryManager;

    @Autowired
    private PaymentMethodManager paymentMethodManager;

    @Autowired
    private OrderMetaManager orderMetaManager;

    @Autowired
    private LogisticsCompanyManager logisticsCompanyManager;

    @Autowired
    private ShopManager shopManager;

    @Override
    public Page list(AfterSaleQueryParam param) {
        StringBuffer sqlBuffer = new StringBuffer("select * from es_as_order where disabled = ? ");
        List<Object> term = new ArrayList<Object>();
        term.add(DeleteStatusEnum.NORMAL.value());

        //按会员ID查询
        if (param.getMemberId() != null && param.getMemberId() != 0) {
            sqlBuffer.append(" and member_id = ?");
            term.add(param.getMemberId());
        }

        //按商家ID查询
        if (param.getSellerId() != null && param.getSellerId() != 0) {
            sqlBuffer.append(" and seller_id = ?");
            term.add(param.getSellerId());
        }

        //按关键字查询
        if (StringUtil.notEmpty(param.getKeyword())) {
            sqlBuffer.append(" and (sn like ? or order_sn like ? or goods_json like ?)");
            term.add("%" + param.getKeyword() + "%");
            term.add("%" + param.getKeyword() + "%");
            term.add("%" + param.getKeyword() + "%");
        }

        //按售后服务单号查询
        if (StringUtil.notEmpty(param.getServiceSn())) {
            sqlBuffer.append(" and sn like ?");
            term.add("%" + param.getServiceSn() + "%");
        }

        //按订单编号查询
        if (StringUtil.notEmpty(param.getOrderSn())) {
            sqlBuffer.append(" and order_sn like ?");
            term.add("%" + param.getOrderSn() + "%");
        }

        //按商品名称查询
        if (StringUtil.notEmpty(param.getGoodsName())) {
            sqlBuffer.append(" and goods_json like ?");
            term.add("%" + param.getGoodsName() + "%");
        }

        //按售后类型查询
        if (StringUtil.notEmpty(param.getServiceType())) {
            sqlBuffer.append(" and service_type = ?");
            term.add(param.getServiceType());
        }

        //按售后状态查询
        if (StringUtil.notEmpty(param.getServiceStatus())) {
            sqlBuffer.append(" and service_status = ?");
            term.add(param.getServiceStatus());
        }

        //按申请时间-起始时间查询
        if (param.getStartTime() != null && param.getStartTime() != 0) {
            sqlBuffer.append(" and create_time >= ?");
            term.add(param.getStartTime());
        }
        //按申请时间-结束时间查询
        if (param.getEndTime() != null && param.getEndTime() != 0) {
            sqlBuffer.append(" and create_time <= ?");
            term.add(param.getEndTime());
        }

        //按创建渠道查询
        if (StringUtil.notEmpty(param.getCreateChannel())) {
            sqlBuffer.append(" and create_channel = ?");
            term.add(param.getCreateChannel());
        }

        sqlBuffer.append(" order by create_time desc");

        Page page = this.daoSupport.queryForPage(sqlBuffer.toString(), param.getPageNo(), param.getPageSize(), AfterSaleServiceDO.class, term.toArray());

        //转换商品数据
        List<AfterSaleServiceDO> serviceDOList = page.getData();
        List<AfterSaleRecordVO> recordVOList = new ArrayList<>();

        for (AfterSaleServiceDO serviceDO : serviceDOList) {
            AfterSaleRecordVO recordVO = new AfterSaleRecordVO(serviceDO);

            //获取售后服务允许操作信息
            ServiceOperateAllowable allowable = new ServiceOperateAllowable(serviceDO);
            recordVO.setAllowable(allowable);

            recordVOList.add(recordVO);
        }

        Page<AfterSaleRecordVO> webPage = new Page(param.getPageNo(), page.getDataTotal(), param.getPageSize(), recordVOList);

        return webPage;
    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public AfterSaleOrderDTO applyOrderInfo(String orderSn, Integer skuId) {
        //获取当前登录的会员信息
        Buyer buyer = UserContext.getBuyer();
        if (buyer == null) {
            throw new ServiceException(MemberErrorCode.E110.code(), "当前会员已经退出登录");
        }

        //订单编号和商品skuID不能为空
        if (StringUtil.isEmpty(orderSn) || skuId == null) {
            throw new ServiceException(AftersaleErrorCode.E601.name(), "参数上传有误，不允许操作");
        }

        //获取要申请售后的订单信息
        OrderDO order = this.orderQueryManager.getOrder(orderSn);

        //不存在的订单或者不属于当前会员的订单进行校验
        if (order == null || !buyer.getUid().equals(order.getMemberId())) {
            throw new ServiceException(AftersaleErrorCode.E604.name(), "申请售后服务的订单信息不存在");
        }

        //获取订单的支付方式
        PaymentMethodDO paymentMethodDO = this.paymentMethodManager.getByPluginId(order.getPaymentPluginId());

        //根据订单编号和商品skuID获取订单项信息
        OrderItemsDO itemsDO = this.getOrderItems(orderSn, skuId);
        if (itemsDO == null) {
            throw new ServiceException(AftersaleErrorCode.E604.name(), "申请售后服务的订单信息不存在");
        }

        //获取订单赠品信息json
        OrderMetaDO orderMetaDO = this.orderMetaManager.getModel(orderSn, OrderMetaKeyEnum.GIFT);
        List<FullDiscountGiftDO> giftList = JsonUtil.jsonToList(orderMetaDO.getMetaValue(), FullDiscountGiftDO.class);
        List<FullDiscountGiftDO> resultList = new ArrayList<>();
        if (giftList != null && giftList.size() != 0) {
            for (FullDiscountGiftDO giftDO : giftList) {
                if (OrderServiceStatusEnum.NOT_APPLY.value().equals(orderMetaDO.getStatus())) {
                    resultList.add(giftDO);
                }
            }
        }

        AfterSaleOrderDTO afterSaleOrderDTO = new AfterSaleOrderDTO();
        afterSaleOrderDTO.setOrderSn(orderSn);
        afterSaleOrderDTO.setGoodId(itemsDO.getGoodsId());
        afterSaleOrderDTO.setSkuId(skuId);
        afterSaleOrderDTO.setGoodsName(itemsDO.getName());
        afterSaleOrderDTO.setGoodsImg(itemsDO.getImage());
        afterSaleOrderDTO.setGoodsPrice(itemsDO.getPrice());
        afterSaleOrderDTO.setBuyNum(itemsDO.getNum());
        afterSaleOrderDTO.setProvinceId(order.getShipProvinceId());
        afterSaleOrderDTO.setCityId(order.getShipCityId());
        afterSaleOrderDTO.setCountyId(order.getShipCountyId());
        afterSaleOrderDTO.setTownId(order.getShipTownId());
        afterSaleOrderDTO.setProvince(order.getShipProvince());
        afterSaleOrderDTO.setCity(order.getShipCity());
        afterSaleOrderDTO.setCounty(order.getShipCounty());
        afterSaleOrderDTO.setTown(order.getShipTown());
        afterSaleOrderDTO.setShipAddr(order.getShipAddr());
        afterSaleOrderDTO.setShipName(order.getShipName());
        afterSaleOrderDTO.setShipMobile(order.getShipMobile());
        afterSaleOrderDTO.setGiftList(giftList);
        afterSaleOrderDTO.setSellerName(order.getSellerName());

        //如果订单类型是换货或补发商品售后服务重新生成的订单，或者订单金额为0的订单，那么不允许申请退货
        if (OrderTypeEnum.CHANGE.name().equals(order.getOrderType()) || OrderTypeEnum.SUPPLY_AGAIN.name().equals(order.getOrderType()) || order.getOrderPrice().doubleValue() == 0) {
            afterSaleOrderDTO.setAllowReturnGoods(false);
        } else {
            afterSaleOrderDTO.setAllowReturnGoods(true);
        }

        //如果订单没有支付方式信息或者支付方式不支持原路退款
        if (paymentMethodDO == null || paymentMethodDO.getIsRetrace() == 0) {
            afterSaleOrderDTO.setIsRetrace(false);
        } else {
            afterSaleOrderDTO.setIsRetrace(true);
        }

        //订单是否含有发票
        afterSaleOrderDTO.setIsReceipt(order.getNeedReceipt() == 1 ? true : false);

        return afterSaleOrderDTO;
    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ApplyAfterSaleVO detail(String serviceSn) {

        //售后服务单编号不能为空
        if (StringUtil.isEmpty(serviceSn)) {
            throw new ServiceException(AftersaleErrorCode.E614.name(), "售后服务单编号不能为空");
        }

        //根据售后服务单号获取服务单信息
        AfterSaleServiceDO afterSaleServiceDO = this.getService(serviceSn);

        if (afterSaleServiceDO == null) {
            throw new ServiceException(AftersaleErrorCode.E614.name(), "售后服务单信息不存在");
        }

        //获取申请售后的订单信息
        OrderDO orderDO = this.orderQueryManager.getOrder(afterSaleServiceDO.getOrderSn());

        if (orderDO == null) {
            throw new ServiceException(TradeErrorCode.E453.name(), "订单信息不存在");
        }

        ApplyAfterSaleVO applyAfterSaleVO = new ApplyAfterSaleVO();
        BeanUtil.copyProperties(afterSaleServiceDO, applyAfterSaleVO);

        //如果售后服务类型为退货或取消订单，则需要获取退款账户相关信息
        if (ServiceTypeEnum.RETURN_GOODS.value().equals(applyAfterSaleVO.getServiceType()) || ServiceTypeEnum.ORDER_CANCEL.value().equals(applyAfterSaleVO.getServiceType())) {
            AfterSaleRefundDO afterSaleRefundDO = this.afterSaleRefundManager.getAfterSaleRefund(serviceSn);
            applyAfterSaleVO.setRefundInfo(afterSaleRefundDO);
        }

        //获取售后服务单允许操作情况
        ServiceOperateAllowable allowable = new ServiceOperateAllowable(afterSaleServiceDO);
        applyAfterSaleVO.setAllowable(allowable);

        //获取申请售后的商品信息集合
        List<AfterSaleGoodsDO> goodsList = this.afterSaleGoodsManager.listGoods(serviceSn);
        applyAfterSaleVO.setGoodsList(goodsList);

        //获取售后服务收货地址相关信息
        AfterSaleChangeDO afterSaleChangeDO = this.afterSaleChangeManager.getModel(serviceSn);
        applyAfterSaleVO.setChangeInfo(afterSaleChangeDO);

        //获取售后服务物流相关信息
        AfterSaleExpressDO afterSaleExpressDO = this.getExpress(serviceSn);
        applyAfterSaleVO.setExpressInfo(afterSaleExpressDO);

        //获取售后服务用户上传的图片信息
        List<AfterSaleGalleryDO> images = this.afterSaleGalleryManager.listImages(serviceSn);
        applyAfterSaleVO.setImages(images);

        //获取售后服务日志相关信息
        List<AfterSaleLogDO> logs = this.afterSaleLogManager.list(serviceSn);
        applyAfterSaleVO.setLogs(logs);

        //获取平台所有的正常开启使用的物流公司信息集合
        List<LogisticsCompanyDO> logiList = this.logisticsCompanyManager.listAllNormal();
        applyAfterSaleVO.setLogiList(logiList);

        //获取订单的发货状态
        applyAfterSaleVO.setOrderShipStatus(orderDO.getShipStatus());
        //获取订单的付款类型
        applyAfterSaleVO.setOrderPaymentType(orderDO.getPaymentType());

        //如果退货地址为空，那么需要获取商家店铺的默认地址作为退货地址
        if (StringUtil.isEmpty(applyAfterSaleVO.getReturnAddr())) {
            ShopDetailDO shopDetailDO = this.shopManager.getShopDetail(applyAfterSaleVO.getSellerId());
            String returnAddr = "收货人：" + shopDetailDO.getLinkName() +  "，联系方式：" + shopDetailDO.getLinkPhone() + "，地址："
                    + shopDetailDO.getShopProvince() + shopDetailDO.getShopCity() + shopDetailDO.getShopCounty() + shopDetailDO.getShopTown() + "  " + shopDetailDO.getShopAdd();
            applyAfterSaleVO.setReturnAddr(returnAddr);
        }

        return applyAfterSaleVO;
    }

    @Override
    public List<AfterSaleExportVO> exportAfterSale(AfterSaleQueryParam param) {
        StringBuffer sqlBuffer = new StringBuffer("select ao.sn as service_sn,ao.order_sn,ao.create_time,ao.member_name,ao.seller_name,ao.mobile,ao.service_type,ao.service_status," +
                "ao.reason,ao.problem_desc,ao.apply_vouchers,ao.audit_remark,ao.stock_remark,ao.refund_remark,ar.refund_price,ar.agree_price,ar.actual_price,ar.refund_time," +
                "ar.refund_way,ar.account_type,ar.return_account,ar.bank_name,ar.bank_account_number,ar.bank_account_name,ar.bank_deposit_name from es_as_order ao left join " +
                "es_as_refund ar on ao.sn = ar.service_sn where ao.disabled = ? ");

        List<Object> term = new ArrayList<Object>();
        term.add(DeleteStatusEnum.NORMAL.value());

        //按商家ID查询
        if (param.getSellerId() != null && param.getSellerId() != 0) {
            sqlBuffer.append(" and ao.seller_id = ?");
            term.add(param.getSellerId());
        }

        //按售后服务单号查询
        if (StringUtil.notEmpty(param.getServiceSn())) {
            sqlBuffer.append(" and ao.sn like ?");
            term.add("%" + param.getServiceSn() + "%");
        }

        //按订单编号查询
        if (StringUtil.notEmpty(param.getOrderSn())) {
            sqlBuffer.append(" and ao.order_sn like ?");
            term.add("%" + param.getOrderSn() + "%");
        }

        //按商品名称查询
        if (StringUtil.notEmpty(param.getGoodsName())) {
            sqlBuffer.append(" and ao.goods_json like ?");
            term.add("%" + param.getGoodsName() + "%");
        }

        //按售后类型查询
        if (StringUtil.notEmpty(param.getServiceType())) {
            sqlBuffer.append(" and ao.service_type = ?");
            term.add(param.getServiceType());
        }

        //按售后状态查询
        if (StringUtil.notEmpty(param.getServiceStatus())) {
            sqlBuffer.append(" and ao.service_status = ?");
            term.add(param.getServiceStatus());
        }

        //按申请时间-起始时间查询
        if (param.getStartTime() != null && param.getStartTime() != 0) {
            sqlBuffer.append(" and ao.create_time >= ?");
            term.add(param.getStartTime());
        }
        //按申请时间-结束时间查询
        if (param.getEndTime() != null && param.getEndTime() != 0) {
            sqlBuffer.append(" and ao.create_time <= ?");
            term.add(param.getEndTime());
        }

        sqlBuffer.append(" order by ao.create_time desc");

        //转换商品数据
        List<AfterSaleExportVO> exportList = this.daoSupport.queryForList(sqlBuffer.toString(), AfterSaleExportVO.class, term.toArray());

        for (AfterSaleExportVO exportVO : exportList) {
            //转换售后服务类型和状态
            exportVO.setServiceTypeText(ServiceTypeEnum.valueOf(exportVO.getServiceType()).description());
            exportVO.setServiceStatusText(ServiceStatusEnum.valueOf(exportVO.getServiceStatus()).description());

            //转换退款方式
            if (StringUtil.notEmpty(exportVO.getRefundWay())) {
                exportVO.setRefundWayText(RefundWayEnum.valueOf(exportVO.getRefundWay()).description());
            }

            //转换账户类型
            if (StringUtil.notEmpty(exportVO.getAccountType())) {
                exportVO.setAccountTypeText(AccountTypeEnum.valueOf(exportVO.getAccountType()).description());
            }

            //组合商品信息
            List<AfterSaleGoodsDO> goodsList = this.afterSaleGoodsManager.listGoods(exportVO.getServiceSn());
            String goodsInfo = "";
            for (AfterSaleGoodsDO goodsDO : goodsList) {
                String storageNum = goodsDO.getStorageNum() == null ? "未入库" : goodsDO.getStorageNum()+"";
                goodsInfo += "【商品名称：" + goodsDO.getGoodsName() + "，商品价格：" + goodsDO.getPrice() + "，购买数量：" + goodsDO.getShipNum() + "，申请售后数量：" + goodsDO.getReturnNum() + "，入库数量：" + storageNum + "】";
            }
            exportVO.setGoodsInfo(goodsInfo);

            //组合收货地址信息
            AfterSaleChangeDO changeDO = this.afterSaleChangeManager.getModel(exportVO.getServiceSn());
            String rogInfo = "收货地址：" + changeDO.getProvince() + changeDO.getCity() + changeDO.getCounty() + changeDO.getTown() + changeDO.getShipAddr() + "，收货人：" + changeDO.getShipName() + "，联系方式：" + changeDO.getShipMobile();
            exportVO.setRogInfo(rogInfo);

            //组合用户退还商品的物流信息
            AfterSaleExpressDO expressDO = this.getExpress(exportVO.getServiceSn());
            if (expressDO != null) {
                String expressInfo = "物流公司：" + expressDO.getCourierCompany() + "，快递单号：" + expressDO.getCourierNumber() + "，发货时间：" + DateUtil.toString(expressDO.getShipTime(), "yyyy-MM-dd");
                exportVO.setExpressInfo(expressInfo);
            }

        }

        return exportList;
    }

    @Override
    public Integer getAfterSaleCount(Integer memberId, Integer sellerId) {
        StringBuffer sql = new StringBuffer("select count(*) from es_as_order where service_status != ? and service_status != ? ");
        List<Object> term = new ArrayList<>();
        term.add(ServiceStatusEnum.COMPLETED.value());
        term.add(ServiceStatusEnum.REFUSE.value());

        if (memberId != null) {
            sql.append("and member_id = ? ");
            term.add(memberId);
        }

        if (sellerId != null) {
            sql.append("and seller_id = ? ");
            term.add(sellerId);
        }
        return this.daoSupport.queryForInt(sql.toString(), term.toArray());

    }

    @Override
    public AfterSaleServiceDO getService(String serviceSn) {
        String sql = "select * from es_as_order where sn = ?";
        return this.daoSupport.queryForObject(sql, AfterSaleServiceDO.class, serviceSn);
    }

    @Override
    public AfterSaleServiceDO getCancelService(String orderSn) {
        String sql = "select * from es_as_order where order_sn = ? and service_type = ? and service_status != ? and service_status != ?";
        return this.daoSupport.queryForObject(sql, AfterSaleServiceDO.class, orderSn, ServiceTypeEnum.ORDER_CANCEL.value(), ServiceStatusEnum.REFUSE.value(), ServiceStatusEnum.CLOSED.value());
    }

    @Override
    public AfterSaleExpressDO getExpress(String serviceSn) {
        String sql = "select * from es_as_express where service_sn = ?";
        return this.daoSupport.queryForObject(sql, AfterSaleExpressDO.class, serviceSn);
    }

    /**
     * 获取订单项信息
     * @param orderSn 订单编号
     * @param skuId 商品skuID
     * @return
     */
    private OrderItemsDO getOrderItems(String orderSn, Integer skuId) {
        String sql = "select * from es_order_items where order_sn = ? and product_id = ?";
        OrderItemsDO orderItemsDO = this.daoSupport.queryForObject(sql, OrderItemsDO.class, orderSn, skuId);
        return orderItemsDO;
    }
}
