package com.enation.app.javashop.core.shop.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.enation.app.javashop.core.client.system.LogiCompanyClient;
import com.enation.app.javashop.core.shop.model.dos.ShopLogisticsSetting;
import com.enation.app.javashop.core.shop.model.vo.ShopLogisticsSettingVO;
import com.enation.app.javashop.core.system.model.dto.KDNParams;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.javashop.core.shop.ShopErrorCode;
import com.enation.app.javashop.core.system.model.dos.LogisticsCompanyDO;
import com.enation.app.javashop.core.shop.service.ShopLogisticsCompanyManager;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.security.model.Seller;

/**
 * 店铺物流公司管理类
 *
 * @author zhangjiping
 * @version v7.0.0
 * @since v7.0.0
 * 2018年3月29日 下午5:25:33
 */
@Service()
public class ShopLogisticsCompanyManagerImpl implements ShopLogisticsCompanyManager {

    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport daoSupport;
    @Autowired
    private LogiCompanyClient logiCompanyClient;


    @Override
    public List list() {

        List<ShopLogisticsSettingVO> shopLogisticsSettingVOS = new ArrayList<>();
        List<LogisticsCompanyDO> list = logiCompanyClient.list();

        Seller seller = UserContext.getSeller();
        String sql = "SELECT * from es_shop_logistics_setting where shop_id = ? ";

        List<ShopLogisticsSetting> shopLogisticsSettings = this.daoSupport.queryForList(sql, ShopLogisticsSetting.class, seller.getSellerId());
        for (LogisticsCompanyDO logisticsCompanyDO : list) {
            ShopLogisticsSettingVO shopLogisticsSettingVO = new ShopLogisticsSettingVO();
            shopLogisticsSettingVO.setLogisticsCompanyDO(logisticsCompanyDO);
            if (shopLogisticsSettings != null && shopLogisticsSettings.size() > 0) {
                for (ShopLogisticsSetting shopLogisticsSetting :
                        shopLogisticsSettings) {
                    if (shopLogisticsSetting.getLogisticsId().equals(logisticsCompanyDO.getId())) {
                        shopLogisticsSettingVO.setShopLogisticsSetting(shopLogisticsSetting);
                    }
                }
            }
            shopLogisticsSettingVOS.add(shopLogisticsSettingVO);
        }
        return shopLogisticsSettingVOS;
    }

    /**
     * 开启某个物流
     *
     * @param logisticsId
     */
    @Override
    public void open(Integer logisticsId) {

        ShopLogisticsSetting shopLogisticsSetting = new ShopLogisticsSetting();
        shopLogisticsSetting.setLogisticsId(logisticsId);
        int count = this.count(shopLogisticsSetting.getLogisticsId(), UserContext.getSeller().getSellerId());
        if (count > 0) {
            throw new ServiceException(ShopErrorCode.E215.name(), "物流公司已开启");
        }
        shopLogisticsSetting.setShopId(UserContext.getSeller().getSellerId());
        this.daoSupport.insert(shopLogisticsSetting);
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void add(ShopLogisticsSetting shopLogisticsSetting) {
        Seller seller = UserContext.getSeller();
        check(shopLogisticsSetting);
        int count = this.count(shopLogisticsSetting.getLogisticsId(), seller.getSellerId());
        if (count > 0) {
            throw new ServiceException(ShopErrorCode.E215.name(), "物流公司已配置，请刷新页面");
        }

        this.daoSupport.insert(shopLogisticsSetting);
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void edit(ShopLogisticsSetting shopLogisticsSetting, int id) {
        Seller seller = UserContext.getSeller();
        check(shopLogisticsSetting);

        int count = this.count(shopLogisticsSetting.getLogisticsId(), seller.getSellerId());
        if (count == 0) {
            this.add(shopLogisticsSetting);
        } else {
            Map<String, Integer> where = new HashMap(1);
            where.put("id", id);
            this.daoSupport.update(shopLogisticsSetting, id);
        }
    }

    private void check(ShopLogisticsSetting shopLogisticsSetting) throws ServiceException {
        LogisticsCompanyDO model = logiCompanyClient.getModel(shopLogisticsSetting.getLogisticsId());
        if (model == null) {
            throw new ServiceException(ShopErrorCode.E214.name(), "物流公司不存在");
        }
    }

    /**
     * 设置某店铺物流参数
     *
     * @param kdnParams 快递鸟参数
     */
    @Override
    public void setting(KDNParams kdnParams, Integer logisticsId) {
        String params = JsonUtil.objectToJson(kdnParams);
        Seller seller = UserContext.getSeller();
        if (this.count(logisticsId, seller.getSellerId()) > 0) {
            this.daoSupport.execute("update es_shop_logistics_setting set config = ? where logistics_id = ? and shop_id = ?"
                    , params, logisticsId, seller.getSellerId());
        }
    }


    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delete(Integer logiId) {
        Seller seller = UserContext.getSeller();
        int count = this.count(logiId, seller.getSellerId());
        if (count == 0) {
            throw new ServiceException(ShopErrorCode.E216.name(), "物流公司已关闭");
        }
        String sql = "delete from es_shop_logistics_setting where shop_id=? and logistics_id=?";
        this.daoSupport.execute(sql, seller.getSellerId(), logiId);
    }

    /**
     * 查询物流公司
     *
     * @param logisticsId
     * @param sellerId
     * @return
     */
    @Override
    public ShopLogisticsSetting query(Integer logisticsId, Integer sellerId) {

        ShopLogisticsSetting shopLogisticsSetting = this.daoSupport.queryForObject("select * from es_shop_logistics_setting where shop_id = ? and logistics_id = ? ", ShopLogisticsSetting.class, sellerId, logisticsId);
        return shopLogisticsSetting;
    }

    @Override
    public List queryListByLogisticsId(Integer logisticsId) {

        String sql = "select * from es_shop_logistics_setting where logistics_id = ? ";

        return this.daoSupport.queryForList(sql, logisticsId);
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteByLogisticsId(Integer logisticsId) {

        String sql = "delete from es_shop_logistics_setting where logistics_id = ? ";

        this.daoSupport.execute(sql, logisticsId);
    }

    /**
     * 查询指定物流公司
     *
     * @param logiId
     * @param shopId
     * @return
     */
    private Integer count(Integer logiId, Integer shopId) {
        String sql = "select count(*) from es_shop_logistics_setting where shop_id = ? and logistics_id = ? ";
        Integer count = this.daoSupport.queryForInt(sql, shopId, logiId);
        return count;
    }

}
