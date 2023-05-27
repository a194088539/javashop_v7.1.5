package com.enation.app.javashop.manager.api.system;

import com.enation.app.javashop.core.base.CachePrefix;
import com.enation.app.javashop.core.system.model.dos.AdminUser;
import com.enation.app.javashop.core.system.model.dos.RoleDO;
import com.enation.app.javashop.core.system.model.vo.AdminLoginVO;
import com.enation.app.javashop.core.system.model.vo.AdminUserVO;
import com.enation.app.javashop.core.system.model.vo.Menus;
import com.enation.app.javashop.core.system.model.vo.RoleVO;
import com.enation.app.javashop.core.system.service.AdminUserManager;
import com.enation.app.javashop.core.system.service.RoleManager;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 权限测试用例
 *
 * @author zh
 * @version v7.0
 * @date 18/7/4 上午10:07
 * @since v7.0
 */
@Transactional(value = "systemTransactionManager", rollbackFor = Exception.class)
public class AuthTest extends BaseTest {

    @Autowired
    private RoleManager roleManager;
    @Autowired
    private AdminUserManager adminUserManager;
    @Autowired
    @Qualifier("systemDaoSupport")
    private DaoSupport systemDaoSupport;
    @Autowired
    private Cache cache;


    @Before
    public void dataPreparation() throws Exception {
        //清除权限
        cache.remove(CachePrefix.ADMIN_URL_ROLE.name());
        //添加商品管理权限
        String menu = "[{\"title\":\"商品管理\",\"identifier\":\"goods\",\"checked\":true,\"authRegular\":\"/goods.*\",\"children\":[{\"title\":\"商品列表\",\"identifier\":\"goodsList\",\"checked\":true,\"authRegular\":\"/admin/goods.*\",\"children\":[]},{\"title\":\"商品设置\",\"identifier\":\"goodsSetting\",\"checked\":true,\"authRegular\":\"/goodsSetting.*\",\"children\":[{\"title\":\"分类列表\",\"identifier\":\"categoryList\",\"checked\":true,\"authRegular\":\"(/admin/goods/categories.*)|(/admin/goods/parameters.*)|(/admin/goods/parameter-groups.*)\",\"children\":[]},{\"title\":\"品牌列表\",\"identifier\":\"brandList\",\"checked\":true,\"authRegular\":\"/admin/goods/brands.*\",\"children\":[]},{\"title\":\"规格列表\",\"identifier\":\"specList\",\"checked\":true,\"authRegular\":\"/admin/goods/specs.*\",\"children\":[]}]},{\"title\":\"商品审核\",\"identifier\":\"goodsAudit\",\"checked\":true,\"authRegular\":\"/admin/goods.*\",\"children\":[]}]},{\"title\":\"订单管理\",\"identifier\":\"order\",\"checked\":false,\"authRegular\":\"/order.*\",\"children\":[{\"title\":\"订单列表\",\"identifier\":\"orderList\",\"checked\":false,\"authRegular\":\"(/admin/trade/orders.*)|(/admin/shops.*)\",\"children\":[]},{\"title\":\"退款单\",\"identifier\":\"refundList\",\"checked\":false,\"authRegular\":\"(/admin/after-sales/refund.*)|(/admin/after-sales/exports.*)\",\"children\":[]},{\"title\":\"收款单\",\"identifier\":\"orderpay\",\"checked\":false,\"authRegular\":\"/admin/trade/orders/pay-log.*\",\"children\":[]},{\"title\":\"开票历史\",\"identifier\":\"receipthistory\",\"checked\":false,\"authRegular\":\"/admin/members/receipts.*\",\"children\":[]}]},{\"title\":\"会员管理\",\"identifier\":\"member\",\"checked\":true,\"authRegular\":\"/member.*\",\"children\":[{\"title\":\"管理会员\",\"identifier\":\"memberManage\",\"checked\":true,\"authRegular\":\"/member.*\",\"children\":[{\"title\":\"会员列表\",\"identifier\":\"memberList\",\"checked\":true,\"authRegular\":\"(/admin/members.*)|(/admin/trade/orders.*)\",\"children\":[]},{\"title\":\"会员回收站\",\"identifier\":\"memberRecycle\",\"checked\":true,\"authRegular\":\"/admin/members.*\",\"children\":[]}]},{\"title\":\"商品评论\",\"identifier\":\"goodsComment\",\"checked\":true,\"authRegular\":\"/member.*\",\"children\":[{\"title\":\"商品评论列表\",\"identifier\":\"mgoodsCommentList\",\"checked\":true,\"authRegular\":\"/admin/members/comments.*\",\"children\":[]},{\"title\":\"商品咨询列表\",\"identifier\":\"goodsAskList\",\"checked\":true,\"authRegular\":\"/admin/members/asks.*\",\"children\":[]}]},{\"title\":\"站内消息\",\"identifier\":\"memberNotification\",\"checked\":true,\"authRegular\":\"/admin/systems/messages.*\",\"children\":[]}]},{\"title\":\"店铺管理\",\"identifier\":\"shop\",\"checked\":false,\"authRegular\":\"/shop.*\",\"children\":[{\"title\":\"管理店铺\",\"identifier\":\"shopManage\",\"checked\":false,\"authRegular\":\"/shop,*\",\"children\":[{\"title\":\"店铺列表\",\"identifier\":\"shopList\",\"checked\":false,\"authRegular\":\"(/admin/shops.*)|(/admin/goods/categories.*)\",\"children\":[]},{\"title\":\"店铺审核\",\"identifier\":\"shopAudit\",\"checked\":false,\"authRegular\":\"/admin/shops.*\",\"children\":[]}]},{\"title\":\"店铺模板管理\",\"identifier\":\"shopThemeManage\",\"checked\":false,\"authRegular\":\"/shop.*\",\"children\":[{\"title\":\"模板列表\",\"identifier\":\"themeList\",\"checked\":false,\"authRegular\":\"/admin/shops/themes.*\",\"children\":[]},{\"title\":\"WAP模板列表\",\"identifier\":\"themeListWap\",\"checked\":false,\"authRegular\":\"/admin/shops/themes.*\",\"children\":[]}]},{\"title\":\"结算列表\",\"identifier\":\"settlementList\",\"checked\":false,\"authRegular\":\"/admin/order/bills.*\",\"children\":[]}]},{\"title\":\"促销管理\",\"identifier\":\"promotions\",\"checked\":false,\"authRegular\":\"/admin/promotions.*\",\"children\":[{\"title\":\"团购管理\",\"identifier\":\"groupBuyManage\",\"checked\":false,\"authRegular\":\"/promotion.*\",\"children\":[{\"title\":\"团购列表\",\"identifier\":\"groupBuyList\",\"checked\":false,\"authRegular\":\"/admin/promotion.*\",\"children\":[]},{\"title\":\"团购分类\",\"identifier\":\"groupBuyCategory\",\"checked\":false,\"authRegular\":\"/admin/promotion/group-buy-cats.*\",\"children\":[]}]},{\"title\":\"积分商城\",\"identifier\":\"pointsMallManage\",\"checked\":false,\"authRegular\":\"/promotion.*\",\"children\":[{\"title\":\"积分分类\",\"identifier\":\"pointsClassify\",\"checked\":false,\"authRegular\":\"/admin/promotion/exchange-cats.*\",\"children\":[]},{\"title\":\"积分商品\",\"identifier\":\"pointsGoods\",\"checked\":false,\"authRegular\":\"/admin/goods.*\",\"children\":[]}]},{\"title\":\"限时抢购\",\"identifier\":\"seckillList\",\"checked\":false,\"authRegular\":\"/admin/promotion.*\",\"children\":[]}]},{\"title\":\"页面设置\",\"identifier\":\"page\",\"checked\":false,\"authRegular\":\"/page.*\",\"children\":[{\"title\":\"PC装修\",\"identifier\":\"pcDecoration\",\"checked\":false,\"authRegular\":\"/page.*\",\"children\":[{\"title\":\"楼层装修\",\"identifier\":\"pcFloorManage\",\"checked\":false,\"authRegular\":\"/admin/pages.*\",\"children\":[]},{\"title\":\"焦点图管理\",\"identifier\":\"pcFocusManage\",\"checked\":false,\"authRegular\":\"/admin/focus-pictures.*\",\"children\":[]}]},{\"title\":\"移动端装修\",\"identifier\":\"mobileDecoration\",\"checked\":false,\"authRegular\":\"/page.*\",\"children\":[{\"title\":\"楼层装修\",\"identifier\":\"mobileFloorManage\",\"checked\":false,\"authRegular\":\"/admin/pages.*\",\"children\":[]},{\"title\":\"焦点图管理\",\"identifier\":\"mobileFocusManage\",\"checked\":false,\"authRegular\":\"/admin/focus-pictures.*\",\"children\":[]}]},{\"title\":\"页面设置\",\"identifier\":\"pageSetting\",\"checked\":false,\"authRegular\":\"/page.*\",\"children\":[{\"title\":\"PC导航菜单\",\"identifier\":\"pcSiteMenu\",\"checked\":false,\"authRegular\":\"/admin/pages/site-navigations.*\",\"children\":[]},{\"title\":\"移动端导航菜单\",\"identifier\":\"mobileSiteMenu\",\"checked\":false,\"authRegular\":\"/admin/pages/site-navigations.*\",\"children\":[]},{\"title\":\"热门关键字\",\"identifier\":\"hotKeyword\",\"checked\":false,\"authRegular\":\"/admin/pages/hot-keywords.*\",\"children\":[]}]}]},{\"title\":\"统计分析\",\"identifier\":\"statistics\",\"checked\":false,\"authRegular\":\"/statistics.*\",\"children\":[{\"title\":\"会员分析\",\"identifier\":\"memberAnalysis\",\"checked\":false,\"authRegular\":\"/statistics.*\",\"children\":[{\"title\":\"会员下单量\",\"identifier\":\"orderAmount\",\"checked\":false,\"authRegular\":\"/admin/statistics.*\",\"children\":[]},{\"title\":\"新增会员统计\",\"identifier\":\"addedMmeber\",\"checked\":false,\"authRegular\":\"/admin/statistics.*\",\"children\":[]}]},{\"title\":\"商品统计\",\"identifier\":\"goodsStatistics\",\"checked\":false,\"authRegular\":\"/statistic.*\",\"children\":[{\"title\":\"价格销量\",\"identifier\":\"priceSales\",\"checked\":false,\"authRegular\":\"(/admin/shops.*)|(/admin/goods.*)|(/admin/statistics.*)\",\"children\":[]},{\"title\":\"热卖商品统计\",\"identifier\":\"hotGoods\",\"checked\":false,\"authRegular\":\"(/admin/shops.*)|(/admin/goods.*)|(/admin/statistics.*)\",\"children\":[]},{\"title\":\"商品销售明细\",\"identifier\":\"goodsSalesDetails\",\"checked\":false,\"authRegular\":\"(/admin/shops.*)|(/admin/goods.*)|(/admin/statistics.*)\",\"children\":[]},{\"title\":\"商品收藏统计\",\"identifier\":\"goodsCollect\",\"checked\":false,\"authRegular\":\"(/admin/shops.*)|(/admin/goods.*)|(/admin/statistics.*)\",\"children\":[]}]},{\"title\":\"行业分析\",\"identifier\":\"industryAnalysis\",\"checked\":false,\"authRegular\":\"/statistic.*\",\"children\":[{\"title\":\"行业规模\",\"identifier\":\"industryScale\",\"checked\":false,\"authRegular\":\"(/admin/shops.*)|(/admin/statistics.*)\",\"children\":[]},{\"title\":\"概括总览\",\"identifier\":\"generalityOverview\",\"checked\":false,\"authRegular\":\"(/admin/shops.*)|(/admin/statistics.*)\",\"children\":[]}]},{\"title\":\"流量分析\",\"identifier\":\"trafficAnalysis\",\"checked\":false,\"authRegular\":\"/admin/statistics.*\",\"children\":[{\"title\":\"店铺流量分析\",\"identifier\":\"indexTrafficAnalysis\",\"checked\":false,\"authRegular\":\"(/admin/shops.*)|(/admin/statistics.*)\",\"children\":[]},{\"title\":\"商品流量分析\",\"identifier\":\"goodsTrafficAnalysis\",\"checked\":false,\"authRegular\":\"(/admin/shops.*)|(/admin/statistics.*)\",\"children\":[]}]},{\"title\":\"其它统计\",\"identifier\":\"otherStatistics\",\"checked\":false,\"authRegular\":\"/statistics.*\",\"children\":[{\"title\":\"订单统计\",\"identifier\":\"orderStatistics\",\"checked\":false,\"authRegular\":\"(/admin/shops.*)|(/admin/statistics.*)\",\"children\":[]},{\"title\":\"销售收入统计\",\"identifier\":\"salesRevenueStatistics\",\"checked\":false,\"authRegular\":\"(/admin/shops.*)|(/admin/statistics.*)\",\"children\":[]},{\"title\":\"区域分析\",\"identifier\":\"regionalAnalysis\",\"checked\":false,\"authRegular\":\"(/admin/shops.*)|(/admin/statistics.*)\",\"children\":[]},{\"title\":\"客单价分布图\",\"identifier\":\"customerPriceDistribution\",\"checked\":false,\"authRegular\":\"(/admin/shops.*)|(/admin/goods/categories.*)|(/admin/statistics.*)\",\"children\":[]},{\"title\":\"退款统计\",\"identifier\":\"refundStatistics\",\"checked\":false,\"authRegular\":\"(/admin/shops.*)|(/admin/statistics/order/return.*)\",\"children\":[]}]}]},{\"title\":\"设置管理\",\"identifier\":\"setting\",\"checked\":false,\"authRegular\":\"/system.*\",\"children\":[{\"title\":\"网店设置\",\"identifier\":\"shopSettings\",\"checked\":false,\"authRegular\":\"/system.*\",\"children\":[{\"title\":\"系统设置\",\"identifier\":\"systemSettings\",\"checked\":false,\"authRegular\":\"(/admin/settings.*)|(/admin/goods.*)|(/admin/trade.*)\",\"children\":[]},{\"title\":\"SMTP设置\",\"identifier\":\"smtpSettings\",\"checked\":false,\"authRegular\":\"/admin/systems/smtps.*\",\"children\":[]},{\"title\":\"短信网关设置\",\"identifier\":\"smsGatewaySettings\",\"checked\":false,\"authRegular\":\"/admin/systems/platforms.*\",\"children\":[]},{\"title\":\"快递平台设置\",\"identifier\":\"expressPlatformSettings\",\"checked\":false,\"authRegular\":\"/admin/systems/express-platforms.*\",\"children\":[]},{\"title\":\"电子面单\",\"identifier\":\"electronicrEceiptSettings\",\"checked\":false,\"authRegular\":\"/admin/systems/waybills.*\",\"children\":[]},{\"title\":\"储存方案\",\"identifier\":\"storageSolutionSettings\",\"checked\":false,\"authRegular\":\"/admin/systems/uploaders.*\",\"children\":[]},{\"title\":\"静态页\",\"identifier\":\"staticPageSettings\",\"checked\":false,\"authRegular\":\"(/admin/task.*)|(/admin/page-create.*)\",\"children\":[]},{\"title\":\"商品索引\",\"identifier\":\"goodsIndexSettings\",\"checked\":false,\"authRegular\":\"(/admin/task.*)|(/admin/goods/search.*)\",\"children\":[]},{\"title\":\"信任登录\",\"identifier\":\"trustLoginSettings\",\"checked\":false,\"authRegular\":\"/admin/members/connect.*\",\"children\":[]}]},{\"title\":\"消息设置\",\"identifier\":\"messageSettings\",\"checked\":false,\"authRegular\":\"/system.*\",\"children\":[{\"title\":\"店铺消息\",\"identifier\":\"shopMessageSettings\",\"checked\":false,\"authRegular\":\"/admin/systems/message-templates.*\",\"children\":[]},{\"title\":\"会员消息\",\"identifier\":\"memberMessageSettings\",\"checked\":false,\"authRegular\":\"/admin/systems/message-templates.*\",\"children\":[]}]},{\"title\":\"推送设置\",\"identifier\":\"pushSettings\",\"checked\":false,\"authRegular\":\"/admin/systems.*\",\"children\":[{\"title\":\"商品推送\",\"identifier\":\"goodsPush\",\"checked\":false,\"authRegular\":\"/admin/systems.*\",\"children\":[]},{\"title\":\"APP推送\",\"identifier\":\"appPush\",\"checked\":false,\"authRegular\":\"/admin/systems.*\",\"children\":[]}]},{\"title\":\"支付和配送\",\"identifier\":\"paymentAndDelivery\",\"checked\":false,\"authRegular\":\"/system.*\",\"children\":[{\"title\":\"支付方式\",\"identifier\":\"paymentSettings\",\"checked\":false,\"authRegular\":\"(/base-api/uploaders.*)|(/admin/payment/payment-methods.*)\",\"children\":[]},{\"title\":\"物流公司\",\"identifier\":\"expressSettings\",\"checked\":false,\"authRegular\":\"/admin/systems/logi-companies.*\",\"children\":[]},{\"title\":\"区域管理\",\"identifier\":\"regionalManagementSettings\",\"checked\":false,\"authRegular\":\"/base-api/regions.*\",\"children\":[]}]},{\"title\":\"权限管理\",\"identifier\":\"authSettings\",\"checked\":false,\"authRegular\":\"/system.*\",\"children\":[{\"title\":\"管理员管理\",\"identifier\":\"administratorManage\",\"checked\":false,\"authRegular\":\"(/base-api/uploaders.*)|(/admin/systems/manager/admin-users.*)\",\"children\":[]},{\"title\":\"角色管理\",\"identifier\":\"roleManage\",\"checked\":false,\"authRegular\":\"(/admin/systems/roles.*)|(/admin/systems/menus.*)\",\"children\":[]}]}]},{\"title\":\"开发管理\",\"identifier\":\"development\",\"checked\":false,\"authRegular\":\"/development.*\",\"children\":[{\"title\":\"菜单管理\",\"identifier\":\"menuManage\",\"checked\":false,\"authRegular\":\"/admin/systems/menus.*\",\"children\":[]},{\"title\":\"文章管理\",\"identifier\":\"articleManage\",\"checked\":false,\"authRegular\":\"/system.*\",\"children\":[{\"title\":\"文章列表\",\"identifier\":\"articleList\",\"checked\":false,\"authRegular\":\"(/admin/systems/admin-users.*)|(/admin/pages.*)\",\"children\":[]},{\"title\":\"文章分类\",\"identifier\":\"articleCategory\",\"checked\":false,\"authRegular\":\"/admin/pages/article-categories.*\",\"children\":[]}]}]},{\"title\":\"分销管理\",\"identifier\":\"distribution\",\"checked\":false,\"authRegular\":\"/distribution.*\",\"children\":[{\"title\":\"提成模板\",\"identifier\":\"extractTpl\",\"checked\":false,\"authRegular\":\"/distribution.*\",\"children\":[{\"title\":\"个人提成模板\",\"identifier\":\"perAccomplishmentTpl\",\"checked\":false,\"authRegular\":\"/admin/distribution.*\",\"children\":[]},{\"title\":\"升级日志\",\"identifier\":\"upgradeLogs\",\"checked\":false,\"authRegular\":\"/distribution.*\",\"children\":[]}]},{\"title\":\"分销商管理\",\"identifier\":\"distributor\",\"checked\":false,\"authRegular\":\"/distribution.*\",\"children\":[{\"title\":\"个人分销商\",\"identifier\":\"distributorList\",\"checked\":false,\"authRegular\":\"/distribution.*\",\"children\":[]},{\"title\":\"分销商统计\",\"identifier\":\"distributorStatistics\",\"checked\":false,\"authRegular\":\"/distribution.*\",\"children\":[]}]},{\"title\":\"业绩管理\",\"identifier\":\"achievement\",\"checked\":false,\"authRegular\":\"/distribution.*\",\"children\":[{\"title\":\"业绩列表\",\"identifier\":\"achievementList\",\"checked\":false,\"authRegular\":\"/distribution.*\",\"children\":[]},{\"title\":\"账单列表\",\"identifier\":\"billList\",\"checked\":false,\"authRegular\":\"/distribution.*\",\"children\":[]},{\"title\":\"结算单详情\",\"identifier\":\"billDetails\",\"checked\":false,\"authRegular\":\"/distribution.*\",\"children\":[]}]},{\"title\":\"提现管理\",\"identifier\":\"putforwardManage\",\"checked\":false,\"authRegular\":\"/distribution.*\",\"children\":[{\"title\":\"提现设置\",\"identifier\":\"putforwardSettings\",\"checked\":false,\"authRegular\":\"/distribution.*\",\"children\":[]},{\"title\":\"提现申请\",\"identifier\":\"putforwardApply\",\"checked\":false,\"authRegular\":\"/distribution.*\",\"children\":[]}]}]}]";
        List<Menus> menus = JsonUtil.jsonToList(menu, Menus.class);
        RoleVO roleVO = new RoleVO();
        roleVO.setRoleName("测试商品管理员");
        roleVO.setRoleDescribe("测试商品管理员描述");
        roleVO.setMenus(menus);
        RoleVO role = roleManager.add(roleVO);
        //新建商品管理员
        systemDaoSupport.execute("delete from es_admin_user where username='测试张三'");
        AdminUserVO adminUserVO = new AdminUserVO();
        adminUserVO.setUsername("测试张三");
        adminUserVO.setFounder(0);
        adminUserVO.setDepartment("铲平部门");
        adminUserVO.setRealName("张三");
        adminUserVO.setPassword(StringUtil.md5("111111"));
        adminUserVO.setRoleId(role.getRoleId());
        adminUserVO.setFace("http://a.jpg");
        adminUserManager.add(adminUserVO);
        //新建超级管理员
        systemDaoSupport.execute("delete from es_admin_user where username='测试'");
        adminUserVO = new AdminUserVO();
        adminUserVO.setUsername("测试");
        adminUserVO.setFounder(1);
        adminUserVO.setDepartment("铲平部门");
        adminUserVO.setRealName("测试");
        adminUserVO.setPassword(StringUtil.md5("111111"));
        adminUserVO.setRoleId(0);
        adminUserVO.setFace("http://a.jpg");
        adminUserManager.add(adminUserVO);
    }


    /**
     * 管理员权限
     *
     * @throws Exception
     */
    @Test
    public void adminAuthTest() throws Exception {
        ErrorMessage error = new ErrorMessage("001", "无权访问");
        //携带错误token校验
        mockMvc.perform(delete("/admin/systems/manager/admin-users/1")
                .header("Authorization", "123").header("uuid", uuid))
                .andExpect(status().is(401))
                .andExpect(objectEquals(error));
        //不携带token校验
        mockMvc.perform(delete("/admin/systems/manager/admin-users/1"))
                .andExpect(status().is(401))
                .andExpect(objectEquals(error));

        //正确校验
        cache.put(CachePrefix.CAPTCHA.getPrefix() + uuid + "_LOGIN", "1111", 1000);
        String json = mockMvc.perform(get("/admin/systems/admin-users/login")
                .param("username", "测试张三")
                .param("password", StringUtil.md5("111111"))
                .param("uuid", uuid)
                .param("captcha", "1111"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        Map tokenMap = JsonUtil.toMap(json);


        mockMvc.perform(delete("/admin/systems/manager/admin-users/1")
                .header("Authorization", tokenMap.get("access_token"))
                .header("uuid", uuid))
                .andExpect(status().is(401))
                .andExpect(objectEquals(error));
        cache.remove(CachePrefix.ADMIN_URL_ROLE.name());
        //商品管理员访问商品
        mockMvc.perform(get("/admin/goods?page_no=1&page_size=10")
                .header("Authorization", tokenMap.get("access_token")))
                .andExpect(status().is(200));
        //超级管理员访问系统配置
        //正确校验
        cache.put(CachePrefix.CAPTCHA.getPrefix() + uuid + "_LOGIN", "1111", 1000);
        json = mockMvc.perform(get("/admin/systems/admin-users/login")
                .param("username", "测试")
                .param("password", StringUtil.md5("111111"))
                .param("uuid", uuid)
                .param("captcha", "1111"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        tokenMap = JsonUtil.toMap(json);
        mockMvc.perform(get("/admin/systems/manager/admin-users/" + tokenMap.get("uid"))
                .header("Authorization", tokenMap.get("access_token"))
                .header("uuid", uuid))
                .andExpect(status().is(200));
        cache.remove(CachePrefix.ADMIN_URL_ROLE.name());

    }
}
