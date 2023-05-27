package com.enation.app.javashop.core.shop.service;

import java.util.List;
import java.util.Map;

import com.enation.app.javashop.core.shop.model.dos.ShopDO;
import com.enation.app.javashop.core.shop.model.dos.ShopDetailDO;
import com.enation.app.javashop.core.shop.model.dto.*;
import com.enation.app.javashop.core.shop.model.enums.ShopStatusEnum;
import com.enation.app.javashop.core.shop.model.vo.*;
import com.enation.app.javashop.framework.database.Page;

/**
 * 店铺业务类
 *
 * @author zhangjiping
 * @version v7.0
 * @since v7.0
 * 2018年3月20日 上午10:06:33
 */
public interface ShopManager {

    /**
     * 获取店铺详细
     *
     * @param shopId 店铺id
     * @return ShopVO  店铺详细
     */
    ShopInfoVO getShopInfo(Integer shopId);


    /**
     * 初始化店铺信息
     */
    void saveInit();

    /**
     * 申请开店第一步
     *
     * @param applyStep1 申请开店第一步VO
     * @return 申请开店第一步VO
     */
    ApplyStep1VO step1(ApplyStep1VO applyStep1);

    /**
     * 申请开店第二步
     *
     * @param applyStep2 申请开店第二步VO
     * @return 申请开店第二步VO
     */
    ApplyStep2VO step2(ApplyStep2VO applyStep2);

    /**
     * 申请开店第三步
     *
     * @param applyStep3 申请开店第三步VO
     * @return 申请开店第三步VO
     */
    ApplyStep3VO step3(ApplyStep3VO applyStep3);

    /**
     * 申请开店第四步
     *
     * @param applyStep4 申请开店第四步VO
     * @return 申请开店第四步VO
     */
    ApplyStep4VO step4(ApplyStep4VO applyStep4);

    /**
     * 店铺分页列表
     *
     * @param shopParams 店铺搜索参数VO
     * @return
     */
    Page list(ShopParamsVO shopParams);


    /**
     *
     * @return
     */
    List<ShopVO> list();

    /**
     * 禁用店铺
     *
     * @param shopId 店铺id
     */
    void disShop(Integer shopId);

    /**
     * 恢复店铺使用
     *
     * @param shopId 店铺id
     */
    void useShop(Integer shopId);

    /**
     * 添加店铺详细
     *
     * @param shopVO 店铺详细
     * @return shopVO 店铺详细
     */
    ShopVO add(ShopVO shopVO);

    /**
     * 修改店铺详细
     *
     * @param shopVO 店铺详细
     * @param id     店铺详细主键
     * @return shopVO 店铺详细
     */
    ShopVO edit(ShopVO shopVO, Integer id);

    /**
     * 删除店铺详细
     *
     * @param id 店铺详细主键
     */
    void delete(Integer id);

    /**
     * 获取店铺详细
     *
     * @param shopId 店铺id
     * @return ShopVO  店铺详细
     */
    ShopVO getShop(Integer shopId);

    /**
     * 根据会员Id获取店铺
     *
     * @param memberId
     * @return 店铺vo
     */
    ShopVO getShopByMemberId(Integer memberId);

    /**
     * 店铺审核
     *
     * @param memberId 会员ID
     * @param shopId   店铺ID
     * @param pass     是否通过：1，通过 0未通过
     */
    void auditpass(Integer memberId, Integer shopId, Integer pass);

    /**
     * 检查店铺名称是否重复
     *
     * @param shopName 店铺名
     * @param shopId 店铺id
     * @return boolean 布尔值 重复：是 不重复：否
     */
    boolean checkShopName(String shopName,Integer shopId);

    /**
     * 后台注册店铺
     *
     * @param shopVO 店铺详细
     */
    void registStore(ShopVO shopVO);

    /**
     * 填充店铺信息，包括商品标签及店铺幻灯片
     *
     * @param shopId
     */
    void fillShopInformation(Integer shopId);

    /**
     * 修改店铺信息-后台使用
     *
     * @param shopVO 店铺详细信息
     */
    void editShopInfo(ShopVO shopVO);

    /**
     * 根据店铺名称查询店铺信息
     *
     * @param shopName 店铺名称
     * @return 店铺信息
     */
    ShopDO getShopByName(String shopName);

    /**
     * 修改店铺信息-商家中心使用
     *
     * @param shopSettingDTO 店铺详细信息
     */
    void editShopSetting(ShopSettingDTO shopSettingDTO);

    /**
     * 获取店铺详细信息
     *
     * @param shopId 店铺id
     * @return ShopDetailDO 店铺详细信息
     */
    ShopDetailDO getShopDetail(Integer shopId);

    /**
     * 修改店铺信息
     *
     * @param shop
     */
    void editShop(Map shop);

    /**
     * 检查会员是否已经申请了店铺
     *
     * @return 布尔值 已经申请 true 未申请 false
     */
    boolean checkShop();

    /**
     * 检查身份证是否使用
     *
     * @param idNumber 身份证id
     * @return 布尔值 已经使用 true 未使用 false
     */
    boolean checkIdNumber(String idNumber);

    /**
     * 增加收藏数量
     *
     * @param shopId 店铺id
     */
    void addcollectNum(Integer shopId);

    /**
     * 减少收藏数量
     *
     * @param shopId
     */
    void reduceCollectNum(Integer shopId);

    /**
     * 修改店铺中的某一个值
     *
     * @param key
     * @param value
     */
    void editShopOnekey(String key, String value);

    /**
     * 获取所有店铺的银行信息佣金比例
     *
     * @return
     */
    List<ShopBankDTO> listShopBankInfo();

    /**
     * 获取店铺基本信息
     *
     * @param shopId 店铺ID
     * @return
     */
    ShopBasicInfoDTO getShopBasicInfo(Integer shopId);

    /**
     * 前台获取店铺列表
     *
     * @param shopParams
     * @return
     */
    Page listShopBasicInfo(ShopParamsVO shopParams);

    /**
     * 修改店铺状态
     *
     * @param shopStatusEnum
     * @param shopId
     */
    void editStatus(ShopStatusEnum shopStatusEnum, Integer shopId);

    /**
     * 修改店铺评分
     *
     * @param shopScore 评分对象
     */
    void editShopScore(ShopScoreDTO shopScore);

    /**
     * 计算店铺评分
     */
    void calculateShopScore();

    /**
     * 商家发票设置
     * @param shopReceiptDTO
     */
    void receiptSetting(ShopReceiptDTO shopReceiptDTO);

    /**
     * 批量检测商家发票功能是否可用
     * @param ids 商家ID集合
     * @return
     */
    ShopReceiptDTO checkSellerReceipt(Integer[] ids);
}
