package com.enation.app.javashop.core.shop.service.impl;


import java.text.SimpleDateFormat;
import java.util.*;

import com.enation.app.javashop.core.base.message.ShopStatusChangeMsg;
import com.enation.app.javashop.core.base.rabbitmq.AmqpExchange;
import com.enation.app.javashop.core.client.goods.GoodsClient;
import com.enation.app.javashop.core.client.goods.TagClient;
import com.enation.app.javashop.core.client.statistics.ShopDataClient;
import com.enation.app.javashop.core.goods.model.dos.GoodsDO;
import com.enation.app.javashop.core.member.MemberErrorCode;
import com.enation.app.javashop.core.member.model.dos.Member;
import com.enation.app.javashop.core.member.model.dos.MemberZpzzDO;
import com.enation.app.javashop.core.member.model.dto.MemberShopScoreDTO;
import com.enation.app.javashop.core.member.model.enums.ZpzzStatusEnum;
import com.enation.app.javashop.core.member.service.MemberManager;
import com.enation.app.javashop.core.member.service.MemberShopScoreManager;
import com.enation.app.javashop.core.member.service.MemberZpzzManager;
import com.enation.app.javashop.core.shop.model.dos.Clerk;
import com.enation.app.javashop.core.shop.model.dto.*;
import com.enation.app.javashop.core.shop.model.vo.*;
import com.enation.app.javashop.core.shop.service.ClerkManager;
import com.enation.app.javashop.core.statistics.model.dto.ShopData;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.util.*;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.javashop.core.shop.ShopErrorCode;
import com.enation.app.javashop.core.shop.model.dos.ShopDO;
import com.enation.app.javashop.core.shop.model.dos.ShopDetailDO;
import com.enation.app.javashop.core.shop.model.dos.ShopThemesDO;
import com.enation.app.javashop.core.shop.model.enums.ShopStatusEnum;
import com.enation.app.javashop.core.shop.model.enums.ShopThemesEnum;
import com.enation.app.javashop.core.shop.service.ShopManager;
import com.enation.app.javashop.core.shop.service.ShopThemesManager;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.security.model.Buyer;
import com.enation.app.javashop.framework.security.model.Seller;

/**
 * 店铺业务类
 *
 * @author zhangjiping
 * @version v7.0
 * @since v7.0
 * 2018年3月20日 上午10:06:33
 */
@Service
public class ShopManagerImpl implements ShopManager {

    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport shopDaoSupport;

    @Autowired
    private ShopThemesManager shopThemesManager;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private TagClient tagClient;

    @Autowired
    private MemberManager memberManager;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private ClerkManager clerkManager;

    @Autowired
    private ShopDataClient shopDataClient;

    @Autowired
    private MemberZpzzManager memberZpzzManager;


    private String all = "ALL";

    @Override
    public ShopInfoVO getShopInfo(Integer shopId) {

        String sql = "select s.member_id,s.member_name,s.shop_name,s.shop_disable,s.shop_createtime,s.shop_endtime,d.* from es_shop s left join es_shop_detail d on  s.shop_id = d.shop_id where s.shop_id = ?";
        return new ShopInfoVO(this.shopDaoSupport.queryForObject(sql, ShopVO.class, shopId));
    }


    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveInit() {
        //获取会员系信息
        Buyer buyer = UserContext.getBuyer();
        ShopDO shop = new ShopDO();
        //查看会员时候拥有店铺
        if (buyer != null && this.getShopByMemberId(buyer.getUid()) == null) {
            //初始化店铺信息
            shop.setShopDisable(ShopStatusEnum.APPLYING.toString());
            //设置会员信息\
            shop.setMemberId(buyer.getUid());
            shop.setMemberName(buyer.getUsername());
            this.shopDaoSupport.insert(shop);

            //初始化店铺详细信息
            int lastId = this.shopDaoSupport.getLastId("es_shop");
            shop.setShopId(lastId);
            ShopDetailDO shopDetail = new ShopDetailDO();
            this.initShopDetail(shopDetail);
            //设置店铺id
            shopDetail.setShopId(lastId);
            this.shopDaoSupport.insert(shopDetail);

            //发送消息
            amqpTemplate.convertAndSend(AmqpExchange.SHOP_CHANGE_REGISTER, AmqpExchange.SHOP_CHANGE_REGISTER + "_ROUTING",
                    lastId);
        }
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ApplyStep1VO step1(ApplyStep1VO applyStep1) {

        if (applyStep1.getRegMoney() != null && applyStep1.getRegMoney() > 10000000000L) {
            throw new ServiceException(ShopErrorCode.E224.name(), "注册资金数值过大，请注意单位");
        }

        ShopVO shop = this.getShop();
        //判断是否拥有店铺
        this.whetheHaveShop(shop);
        //判断是否为店员
        this.validateClerk(shop);
        //设置申请开单第一步
        if (shop.getStep() == null) {
            applyStep1.setStep(1);
        }
        Map where = new HashMap(2);
        where.put("shop_id", shop.getShopId());
        this.shopDaoSupport.update("es_shop_detail", applyStep1, where);
        return applyStep1;
    }


    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ApplyStep2VO step2(ApplyStep2VO applyStep2) {
        ShopVO shop = this.getShop();
        //判断是否拥有店铺
        this.whetheHaveShop(shop);
        //判断是否为店员
        this.validateClerk(shop);
        //没有完成第一步不允许此步操作
        if (shop.getStep() == null) {
            throw new ServiceException(ShopErrorCode.E224.name(), "完成上一步才可进行此步操作");
        }
        //未完成第三步则设置为第二步
        if (shop.getStep() < 3) {
            applyStep2.setStep(2);
        }
        //校验营业执照有效期
        if (applyStep2.getLicenceStart() > applyStep2.getLicenceEnd()) {
            throw new ServiceException(ShopErrorCode.E217.name(), "营业执照开始时间不能大于结束时间");
        }
        //校验营业执照开始时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date()) + " 00:00:00";
        long startTime = DateUtil.getDateline(date);
        if (applyStep2.getLicenceEnd() <= startTime) {
            throw new ServiceException(ShopErrorCode.E217.name(), "营业执照结束时间不能小于当前时间");
        }
        Map where = new HashMap(2);
        where.put("shop_id", shop.getShopId());
        this.shopDaoSupport.update("es_shop_detail", applyStep2, where);
        return applyStep2;
    }


    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ApplyStep3VO step3(ApplyStep3VO applyStep3) {
        ShopVO shop = this.getShop();
        //判断是否拥有店铺
        this.whetheHaveShop(shop);
        //判断是否为店员
        this.validateClerk(shop);
        //没有完成第二步不允许此步操作
        if (shop.getStep() == null || shop.getStep() < 2) {
            throw new ServiceException(ShopErrorCode.E224.name(), "完成上一步才可进行此步操作");
        }
        applyStep3.setStep(3);

        Map where = new HashMap(2);
        where.put("shop_id", shop.getShopId());
        this.shopDaoSupport.update("es_shop_detail", applyStep3, where);
        return applyStep3;
    }


    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ApplyStep4VO step4(ApplyStep4VO applyStep4) {
        ShopVO shop = this.getShop();
        //判断是否拥有店铺
        this.whetheHaveShop(shop);
        //判断是否为店员
        this.validateClerk(shop);
        //没有完成第三步不允许此步操作
        if (shop.getStep() == null || shop.getStep() < 3) {
            throw new ServiceException(ShopErrorCode.E224.name(), "完成上一步才可进行此步操作");
        }
        applyStep4.setStep(4);

        boolean checkShopName = this.checkShopName(applyStep4.getShopName(), shop.getShopId());
        if (checkShopName) {
            throw new ServiceException(ShopErrorCode.E203.name(), "店铺名称重复");
        }

        //更新店铺基本信息
        String sql = "update es_shop set shop_name = ? , shop_disable = ?  where member_id = ? ";
        Map fields = new HashMap<>(2);
        fields.put("shop_name", applyStep4.getShopName());
        fields.put("shop_disable", ShopStatusEnum.APPLY.toString());

        Map where = new HashMap<>(2);
        where.put("member_id", shop.getMemberId());
        this.shopDaoSupport.update("es_shop", fields, where);
        //获取店铺详细信息

        ShopDetailDO shopDetail = new ShopDetailDO();
        shopDetail.setShopProvince(applyStep4.getShopProvince());
        shopDetail.setShopProvinceId(applyStep4.getShopProvinceId());
        shopDetail.setShopCity(applyStep4.getShopCity());
        shopDetail.setShopCityId(applyStep4.getShopCityId());
        shopDetail.setShopCounty(applyStep4.getShopCounty());
        shopDetail.setShopCountyId(applyStep4.getShopCountyId());
        shopDetail.setShopTown(applyStep4.getShopTown());
        shopDetail.setShopTownId(applyStep4.getShopTownId());
        shopDetail.setGoodsManagementCategory(applyStep4.getGoodsManagementCategory());
        shopDetail.setShopAdd(applyStep4.getShopAdd());
        where.clear();
        where.put("shop_id", shop.getShopId());
        this.shopDaoSupport.update("es_shop_detail", shopDetail, where);

        return applyStep4;
    }

    @Override
    public Page list(ShopParamsVO shopParams) {

        StringBuffer sql = new StringBuffer("");
        String disabled = shopParams.getShopDisable() == null ? "OPEN" : shopParams.getShopDisable();

        List<Object> params = new ArrayList<>();
        // 店铺状态
        if (disabled.equals(all)) {
            sql.append("select  s.member_id,s.member_name,s.shop_name,s.shop_disable,s.shop_createtime,s.shop_endtime,sd.* from es_shop s  left join es_shop_detail sd on s.shop_id = sd.shop_id  where  shop_disable != 'APPLYING' ");
        } else {
            sql.append("select  s.member_id,s.member_name,s.shop_name,s.shop_disable,s.shop_createtime,s.shop_endtime,sd.* from es_shop s  left join es_shop_detail sd on s.shop_id = sd.shop_id   where  shop_disable = '" + disabled + "'");
        }
        if (!StringUtil.isEmpty(shopParams.getKeyword())) {
            sql.append("  and (s.shop_name like ? or s.member_name like ?) ");
            params.add("%" + shopParams.getKeyword() + "%");
            params.add("%" + shopParams.getKeyword() + "%");
        }
        if (!StringUtil.isEmpty(shopParams.getShopName())) {
            sql.append("  and s.shop_name like ? ");
            params.add("%" + shopParams.getShopName() + "%");
        }
        if (!StringUtil.isEmpty(shopParams.getMemberName())) {
            sql.append("  and s.member_name like ? ");
            params.add("%" + shopParams.getMemberName() + "%");
        }
        if (!StringUtil.isEmpty(shopParams.getStartTime())) {
            sql.append(" and s.shop_createtime > ? ");
            params.add(shopParams.getStartTime());
        }
        if (!StringUtil.isEmpty(shopParams.getEndTime())) {
            sql.append(" and s.shop_createtime < ? ");
            params.add(shopParams.getEndTime());
        }
        sql.append(" order by s.shop_createtime desc");
        return this.shopDaoSupport.queryForPage(sql.toString(), shopParams.getPageNo(), shopParams.getPageSize(), params.toArray());
    }

    @Override
    public List<ShopVO> list() {
        StringBuffer sql = new StringBuffer();
        sql.append("select  s.member_id,s.member_name,s.shop_name,s.shop_disable,s.shop_createtime,s.shop_endtime,sd.* from es_shop s  left join es_shop_detail sd on s.shop_id = sd.shop_id   where  shop_disable = 'OPEN'");
        sql.append(" order by s.shop_createtime desc");
        return this.shopDaoSupport.queryForList(sql.toString(), ShopVO.class);
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void disShop(Integer shopId) {

        ShopVO shop = this.getShop(shopId);
        if (shop == null) {
            throw new ServiceException(ShopErrorCode.E206.name(), "不存在此店铺");
        }

        Map where = new HashMap<>(2);
        where.put("shop_id", shopId);
        Map fields = new HashMap<>(2);
        fields.put("shop_endtime", DateUtil.getDateline());
        fields.put("shop_disable", ShopStatusEnum.CLOSED.toString());

        this.shopDaoSupport.update("es_shop", fields, where);

        //更改统计中店铺状态
        ShopData shopData = new ShopData();
        shopData.setSellerId(shop.getShopId());
        shopData.setSellerName(shop.getShopName());
        shopData.setShopDisable(ShopStatusEnum.CLOSED.toString());
        shopDataClient.updateShopData(shopData);

        //下架店铺所有商品
        goodsClient.underShopGoods(shopId);
        amqpTemplate.convertAndSend(AmqpExchange.CLOSE_STORE, AmqpExchange.CLOSE_STORE + "_ROUTING", new ShopStatusChangeMsg(shopId, ShopStatusEnum.CLOSED));

    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void useShop(Integer shopId) {

        ShopVO shop = this.getShop(shopId);
        if (shop == null) {
            throw new ServiceException(ShopErrorCode.E206.name(), "不存在此店铺");
        }

        Map where = new HashMap<>(2);
        where.put("shop_id", shopId);
        Map fields = new HashMap<>(2);
        fields.put("shop_disable", ShopStatusEnum.OPEN.toString());

        this.shopDaoSupport.update("es_shop", fields, where);

        //更改统计中店铺状态
        ShopData shopData = new ShopData();
        shopData.setSellerId(shop.getShopId());
        shopData.setSellerName(shop.getShopName());
        shopData.setShopDisable(ShopStatusEnum.OPEN.toString());
        shopDataClient.updateShopData(shopData);
//		TODO
//		this.shopDaoSupport.execute(
//				"update es_member set is_store=" + 1 + " where member_id= ? " , this.getShop(shopId).getMember_id());
//		// 更高店铺商品状态
//		this.shopDaoSupport.execute("update es_goods set disabled=? where seller_id=?", 0, shopId);

    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ShopVO add(ShopVO shopVO) {
        this.shopDaoSupport.insert(shopVO);

        return shopVO;
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ShopVO edit(ShopVO shopVO, Integer id) {
        this.shopDaoSupport.update(shopVO, id);
        return shopVO;
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delete(Integer id) {
        this.shopDaoSupport.delete(ShopVO.class, id);
    }

    @Autowired
    private MemberShopScoreManager memberShopScoreManager;


    @Override
    public ShopVO getShop(Integer shopId) {

        String sql = "select s.member_id,s.member_name,s.shop_name,s.shop_disable,s.shop_createtime,s.shop_endtime,d.* from es_shop s left join es_shop_detail d on  s.shop_id = d.shop_id where s.shop_id = ?";

        ShopVO shopVO = this.shopDaoSupport.queryForObject(sql, ShopVO.class, shopId);

        return shopVO;
    }


    @Override
    public ShopVO getShopByMemberId(Integer memberId) {
        String sql = "select s.member_id,s.member_name,s.shop_name,s.shop_disable,s.shop_createtime,s.shop_endtime,d.* from es_shop s left join es_shop_detail d on  s.shop_id = d.shop_id where s.member_id = ?";
        return this.shopDaoSupport.queryForObject(sql, ShopVO.class, memberId);

    }


    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void auditpass(Integer memberId, Integer shopId, Integer pass) {
        if (pass == 1) {
            this.editShopdStatus(ShopStatusEnum.OPEN.toString(), shopId);
        } else {
            // 审核未通过
            this.editShopdStatus(ShopStatusEnum.REFUSED.toString(), shopId);
        }
    }


    @Override
    public boolean checkShopName(String shopName, Integer shopId) {

        String sql = "select  count(shop_id) from es_shop where shop_name= ? and shop_disable != ?";
        List<Object> term = new ArrayList<>();
        term.add(shopName);
        term.add(ShopStatusEnum.REFUSED.value());
        if (shopId != null) {
            sql = sql + " and shop_id != ?";
            term.add(shopId);
        }
        Integer count = this.shopDaoSupport.queryForInt(sql, term.toArray());
        return count != 0;
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void registStore(ShopVO shopVo) {

        ShopThemesDO pcThemes = shopThemesManager.getDefaultShopThemes(ShopThemesEnum.PC.name());
        ShopThemesDO wapThemes = shopThemesManager.getDefaultShopThemes(ShopThemesEnum.WAP.name());

        //TODO 验证会员有效性

        if (this.getShopByMemberId(shopVo.getMemberId()) != null) {
            throw new ServiceException(ShopErrorCode.E207.name(), "会员已存在店铺，不可重复添加");
        }

        if (pcThemes == null || wapThemes == null) {
            throw new ServiceException(ShopErrorCode.E202.name(), "店铺模版不存在,请设置店铺模版");
        }

        if (this.checkShopName(shopVo.getShopName(), shopVo.getShopId())) {
            throw new ServiceException(ShopErrorCode.E203.name(), "店铺名称重复");
        }


        //设置模版信息
        shopVo.setShopThemeid(pcThemes.getId());
        shopVo.setShopThemePath(pcThemes.getMark());
        shopVo.setWapThemeid(wapThemes.getId());
        shopVo.setWapThemePath(wapThemes.getMark());
        //设置店铺等级
        shopVo.setShopLevel(1);
        //后台无需审核直接开店
        shopVo.setShopDisable(ShopStatusEnum.OPEN.toString());
        //设置开店时间
        shopVo.setShopCreatetime(DateUtil.getDateline());

        //保存店铺信息
        shopVo.setMemberId(shopVo.getMemberId());

        //获取店铺店铺信息实体
        ShopDO shop = new ShopDO();
        ShopDetailDO shopDetail = new ShopDetailDO();
        BeanUtils.copyProperties(shopVo, shopDetail);
        BeanUtils.copyProperties(shopVo, shop);
        this.shopDaoSupport.insert("es_shop", shop);
        int lastId = this.shopDaoSupport.getLastId("es_shop");
        shopDetail.setShopId(lastId);
        shopVo.setShopId(lastId);

        this.initShopDetail(shopDetail);

        this.shopDaoSupport.insert("es_shop_detail", shopDetail);
        shop.setShopId(this.shopDaoSupport.getLastId("es_shop"));
        //增加店铺商品标签
        tagClient.addShopTags(lastId);

        //修改会员信息
        Member member = memberManager.getModel(shopVo.getMemberId());
        member.setShopId(lastId);
        member.setHaveShop(1);
        memberManager.edit(member, member.getMemberId());

    }

    @Override
    public void fillShopInformation(Integer shopId) {
//		//添加商品标签
//		StoreTag storeTag = new StoreTag();
//		storeTag.setStore_id(shop_id);
//		// 热卖排行
//		storeTag.setTag_name("热卖排行");
//		storeTag.setMark("hot");
//		storeGoodsTagManager.add(storeTag);
//		// 新品推荐
//		storeTag.setTag_name("新品推荐");
//		storeTag.setMark("new");
//		storeGoodsTagManager.add(storeTag);
//		// 推荐商品
//		storeTag.setTag_name("推荐商品");
//		storeTag.setMark("recommend");
//		storeGoodsTagManager.add(storeTag);
//
//		//添加店铺幻灯片
//		Map map = new HashMap();
//		for (int i = 0; i < 5; i++) {
//			map.put("store_id", shop_id);
//			map.put("img", "fs:/images/s_side.jpg");
//			this.shopDaoSupport.insert("es_store_silde", map);
//		}

    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void editShopInfo(ShopVO shopVO) {
        //获取店铺店铺信息实体
        ShopDO shop = new ShopDO();
        ShopDetailDO shopDetail = new ShopDetailDO();
        BeanUtils.copyProperties(shopVO, shopDetail);
        BeanUtils.copyProperties(shopVO, shop);
        //校验店铺名称是否重复
        boolean bool = false;
        String sql = "select * from es_shop where shop_name= ?";
        List<ShopDO> shops = this.shopDaoSupport.queryForList(sql, ShopDO.class, shop.getShopName());
        for (ShopDO shopDO : shops) {
            if (!shopDO.getShopId().equals(shopVO.getShopId())) {
                bool = true;
                continue;
            }
        }
        if (bool) {
            throw new ServiceException(ShopErrorCode.E203.code(), "店铺名称重复");
        }
        //查询原店铺信息
        ShopVO originalShop = this.getShop(shopVO.getShopId());
        Map where = new HashMap<>(2);
        where.put("shop_id", shopVO.getShopId());
        this.shopDaoSupport.update("es_shop", shop, where);
        this.shopDaoSupport.update("es_shop_detail", shopDetail, where);

        //更改统计中店铺数据
        ShopData shopData = new ShopData();
        shopData.setSellerId(shop.getShopId());
        shopData.setSellerName(shop.getShopName());
        shopData.setShopDisable(shop.getShopDisable());
        shopDataClient.updateShopData(shopData);
        //发送店铺信息改变消息
        amqpTemplate.convertAndSend(AmqpExchange.SHOP_CHANGE, AmqpExchange.SHOP_CHANGE + "_ROUTING", new ShopChangeMsg(originalShop, shopVO));
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void editShopSetting(ShopSettingDTO shopSetting) {
        Seller seller = UserContext.getSeller();
        Map where = new HashMap<>(2);
        where.put("shop_id", seller.getSellerId());

        this.shopDaoSupport.update("es_shop_detail", shopSetting, where);
    }

    @Override
    public ShopDetailDO getShopDetail(Integer shopId) {
        String sql = "select * from es_shop_detail where shop_id = ? ";
        return this.shopDaoSupport.queryForObject(sql, ShopDetailDO.class, shopId);
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void editShop(Map shop) {
        Map where = new HashMap<>(2);
        where.put("shop_id", shop.get("shop_id"));
        this.shopDaoSupport.update("es_shop_detail", shop, where);

    }

    @Override
    public boolean checkShop() {
        //获取当前会员
        Buyer buyer = UserContext.getBuyer();
        String sql = "select count(store_id) from es_shop where member_id=?";
        int isHas = this.shopDaoSupport.queryForInt(sql, buyer.getUid());
        return isHas > 0;
    }

    @Override
    public boolean checkIdNumber(String idNumber) {
        String sql = "select count(*) from es_shop_detail where legal_id=?";
        Integer count = this.shopDaoSupport.queryForInt(sql, idNumber);
        return count != 0;
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addcollectNum(Integer shopId) {
        String sql = "update es_shop_detail set shop_collect = shop_collect+1 where shop_id=?";
        this.shopDaoSupport.execute(sql, shopId);
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void reduceCollectNum(Integer shopId) {
        String sql = "update es_shop_detail set shop_collect = shop_collect-1 where shop_id=?";
        this.shopDaoSupport.execute(sql, shopId);
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void editShopOnekey(String key, String value) {
        Seller seller = UserContext.getSeller();
        Map where = new HashMap<>(2);
        where.put("shop_id", seller.getSellerId());
        Map map = new HashMap(2);
        map.put(key, value);
        this.shopDaoSupport.update("es_shop_detail", map, where);
    }

    @Override
    public List<ShopBankDTO> listShopBankInfo() {
        String sql = "select s.shop_id,s.shop_name,shop_commission,bank_account_name,bank_number,bank_name,bank_province_id,bank_city_id,bank_county_id,bank_town_id,bank_province,bank_city,bank_county,bank_town from es_shop s left join es_shop_detail d on s.shop_id = d.shop_id where shop_disable in ('" + ShopStatusEnum.OPEN.name() + "','" + ShopStatusEnum.CLOSED.name() + "')";
        List<ShopBankDTO> shopBankDTOS = this.shopDaoSupport.queryForList(sql, ShopBankDTO.class);
        return shopBankDTOS;
    }


    @Override
    public ShopBasicInfoDTO getShopBasicInfo(Integer shopId) {
        String sql = "select s.member_id,s.member_name,s.shop_name,s.shop_disable,s.shop_createtime,s.shop_endtime,d.* from es_shop s left join es_shop_detail d on  s.shop_id = d.shop_id where s.shop_id = ?";
        ShopBasicInfoDTO shopBasicInfoDTO = this.shopDaoSupport.queryForObject(sql, ShopBasicInfoDTO.class, shopId);
        if (shopBasicInfoDTO == null) {
            throw new ServiceException(ShopErrorCode.E206.name(), "店铺不存在");
        }
        return shopBasicInfoDTO;
    }

    @Override
    public Page listShopBasicInfo(ShopParamsVO shopParams) {

        StringBuffer sql = new StringBuffer("");
        String disabled = ShopStatusEnum.OPEN.value();
        List<Object> params = new ArrayList<>();

        sql.append("select  s.member_id,s.member_name,s.shop_name,s.shop_disable,s.shop_createtime,s.shop_endtime,sd.* from es_shop s  left join es_shop_detail sd on s.shop_id = sd.shop_id   where  shop_disable = '" + disabled + "'");

        if (!StringUtil.isEmpty(shopParams.getShopName())) {
            sql.append("  and s.shop_name like ? ");
            params.add("%" + shopParams.getShopName() + "%");

        }
        if (!StringUtil.isEmpty(shopParams.getOrder())) {
            sql.append("  order by shop_credit desc");
        }
        Page page = this.shopDaoSupport.queryForPage(sql.toString(), shopParams.getPageNo(), shopParams.getPageSize(), ShopListVO.class, params.toArray());

        List<ShopListVO> data = page.getData();
        for (ShopListVO shop : data) {
            List<GoodsDO> goodsDOS = this.goodsClient.listGoods(shop.getShopId());
            shop.setGoodsList(goodsDOS);
            Member model = memberManager.getModel(shop.getMemberId());
            shop.setMemberFace(model.getFace());
        }
        return page;
    }

    @Override
    public void editStatus(ShopStatusEnum shopStatusEnum, Integer shopId) {
        String sql = " update es_shop set shop_disable = ? where shop_id =  ? ";
        this.shopDaoSupport.execute(sql, shopStatusEnum.value(), shopId);
    }

    @Override
    public void editShopScore(ShopScoreDTO shopScore) {
        String updateSql = "update es_shop_detail set shop_description_credit = ?, shop_service_credit = ?,shop_delivery_credit = ?,shop_credit = ? where shop_id=? ";
        this.shopDaoSupport.execute(updateSql, CurrencyUtil.round(shopScore.getShopDescriptionCredit(), 2),
                CurrencyUtil.round(shopScore.getShopServiceCredit(), 2), CurrencyUtil.round(shopScore.getShopDeliveryCredit(), 2),
                CurrencyUtil.round(shopScore.getShopCredit(), 2), shopScore.getShopId());
    }

    @Override
    public void calculateShopScore() {
        //查询店铺评分的平均值
        List<MemberShopScoreDTO> shopScoreList = this.memberShopScoreManager.queryEveryShopScore();
        if (StringUtil.isNotEmpty(shopScoreList)) {
            for (MemberShopScoreDTO shopScore : shopScoreList) {
                Double descriptionScore = shopScore.getDescriptionScore();
                Double serviceScore = shopScore.getServiceScore();
                Double deliveryScore = shopScore.getDeliveryScore();
                Double shopCredit = CurrencyUtil.div(CurrencyUtil.add(CurrencyUtil.add(descriptionScore, serviceScore), deliveryScore), 3.00);
                ShopScoreDTO dto = new ShopScoreDTO(descriptionScore, serviceScore, deliveryScore, shopCredit, shopScore.getSellerId());

                this.editShopScore(dto);
            }
        }
    }

    @Override
    public void receiptSetting(ShopReceiptDTO shopReceiptDTO) {
        Seller seller = UserContext.getSeller();
        if (seller == null) {
            throw new ServiceException(MemberErrorCode.E137.code(), "当前商家登录信息已经失效");
        }

        String sql = "update es_shop_detail set ordin_receipt_status=?,elec_receipt_status=?,tax_receipt_status=? where shop_id = ?";
        this.shopDaoSupport.execute(sql, shopReceiptDTO.getOrdinReceiptStatus(), shopReceiptDTO.getElecReceiptStatus(), shopReceiptDTO.getTaxReceiptStatus(), seller.getSellerId());
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ShopReceiptDTO checkSellerReceipt(Integer[] ids) {

        //获取当前登录会员增票资质信息
        MemberZpzzDO memberZpzzDO = this.memberZpzzManager.get();

        List term = new ArrayList<>();

        String idsStr = SqlUtil.getInSql(ids, term);
        String sql = "select * from es_shop_detail where shop_id in (" + idsStr + ")";
        List<ShopDetailDO> detailDOS = this.shopDaoSupport.queryForList(sql, ShopDetailDO.class, term.toArray());

        int ordin = 1;
        int elec = 1;
        int tax = 1;

        for (ShopDetailDO shopDetailDO : detailDOS) {
            if (ordin == 1) {
                ordin = shopDetailDO.getOrdinReceiptStatus();
            }

            if (elec == 1) {
                elec = shopDetailDO.getElecReceiptStatus();
            }

            if (tax == 1) {
                tax = shopDetailDO.getTaxReceiptStatus();
            }
        }

        //如果当前登录会员还没有申请增票资质或者申请增票资质还未审核或审核不通过，那么不允许开具增值税专用发票
        if (memberZpzzDO == null || !ZpzzStatusEnum.AUDIT_PASS.value().equals(memberZpzzDO.getStatus())) {
            tax = 0;
        }

        ShopReceiptDTO shopReceiptDTO = new ShopReceiptDTO();
        shopReceiptDTO.setOrdinReceiptStatus(ordin);
        shopReceiptDTO.setElecReceiptStatus(elec);
        shopReceiptDTO.setTaxReceiptStatus(tax);
        return shopReceiptDTO;
    }

    /**
     * 初始化店铺信息
     *
     * @param shopDetail
     */
    private void initShopDetail(ShopDetailDO shopDetail) {
        shopDetail.setShopCredit(5.0);
        shopDetail.setShopPraiseRate(0.0);
        shopDetail.setShopDescriptionCredit(5.0);
        shopDetail.setShopServiceCredit(5.0);
        shopDetail.setShopDeliveryCredit(5.0);
        shopDetail.setShopCommission(5.0);
        shopDetail.setShopCollect(0);
        shopDetail.setShopLevel(1);
        shopDetail.setGoodsNum(0);
        shopDetail.setGoodsWarningCount(5);
        shopDetail.setShopLevelApply(0);
        shopDetail.setStoreSpaceCapacity(0.00);
        shopDetail.setSelfOperated(0);
    }

    /**
     * 根据当前会员获取店铺
     */
    private ShopVO getShop() {
        Buyer buyer = UserContext.getBuyer();
        ShopVO shop = this.getShopByMemberId(buyer.getUid());
        return shop;
    }

    /**
     * 判断当前会员时候拥有店铺
     *
     * @param shop
     */
    private void whetheHaveShop(ShopVO shop) {
        if (shop == null) {
            throw new ServiceException(ShopErrorCode.E200.name(), "您尚未拥有店铺，不能进行此操作");
        }

        if (!shop.getShopDisable().equals(ShopStatusEnum.APPLYING.name())) {
            throw new ServiceException(ShopErrorCode.E204.name(), "店铺在申请中，不允许此操作");
        }
    }

    /**
     * 检测是否店员
     *
     * @param shop
     */
    private void validateClerk(ShopVO shop) {
        Clerk clerk = this.clerkManager.getClerkByMemberId(shop.getMemberId());
        if (clerk != null) {
            throw new ServiceException(ShopErrorCode.E230.name(), "当前账号为店铺的管理员，不允许此操作！");
        }
    }

    /**
     * 更改店铺状态
     *
     * @param disabled 店铺状态
     * @param shopId   店铺id
     */
    private void editShopdStatus(String disabled, Integer shopId) {
        Map where = new HashMap<>(2);
        where.put("shop_id", shopId);
        Map fields = new HashMap<>(2);
        fields.put("shop_disable", disabled);
        this.shopDaoSupport.execute("update es_shop set shop_disable = ? where shop_id=? ", disabled, where);
    }

    @Override
    public ShopDO getShopByName(String shopName) {
        String sql = "select * from es_shop where shop_name = ?";
        return this.shopDaoSupport.queryForObject(sql, ShopDO.class, shopName);
    }
}