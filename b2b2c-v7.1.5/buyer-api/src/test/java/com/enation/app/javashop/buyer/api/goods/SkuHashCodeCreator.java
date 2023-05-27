package com.enation.app.javashop.buyer.api.goods;

import com.enation.app.javashop.core.goods.model.dos.GoodsSkuDO;
import com.enation.app.javashop.core.goods.model.vo.SpecValueVO;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.Rollback;

import java.util.List;

/**
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2019-03-11
 */
@Rollback(false)
public class SkuHashCodeCreator extends BaseTest {


    @Autowired
    @Qualifier("goodsDaoSupport")
    private DaoSupport daoSupport;


    @Test
    public void create() {
        daoSupport.execute("update es_goods_sku set hash_code=-1 where  specs is  null");
        List<GoodsSkuDO> skuList  = daoSupport.queryForList("select * from es_goods_sku where specs is not null", GoodsSkuDO.class);

        for (GoodsSkuDO sku : skuList) {
            String specs = sku.getSpecs();
            if (specs != null) {
                List<SpecValueVO> specValueVOS =JsonUtil.jsonToList( specs , SpecValueVO.class);

                int hashCode= buildHashCode(specValueVOS);
                daoSupport.execute("update es_goods_sku set hash_code=? where sku_id=?", hashCode, sku.getSkuId());
            }
        }
    }


    private int buildHashCode(List<SpecValueVO> specValueVOList) {
        HashCodeBuilder codeBuilder = new HashCodeBuilder(17, 37);
        specValueVOList.forEach(specValueVO -> {
            String specValue = specValueVO.getSpecValue();
            codeBuilder.append(specValue);

        });
        int hashCode = codeBuilder.toHashCode();

        return hashCode;
    }
}
