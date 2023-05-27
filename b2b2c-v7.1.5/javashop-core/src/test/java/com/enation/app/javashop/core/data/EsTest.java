package com.enation.app.javashop.core.data;

import com.enation.app.javashop.core.promotion.pintuan.model.PtGoodsDoc;
import com.enation.app.javashop.core.promotion.pintuan.service.PinTuanSearchManager;
import com.enation.app.javashop.framework.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import java.util.List;
import java.util.Map;

/**
 * Created by kingapex on 2019-01-21.
 *
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2019-01-21
 */
public class EsTest extends BaseTest {

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Autowired
    private PinTuanSearchManager pinTuanSearchManager;

    @Test
    public void test() {

        Map map  = elasticsearchOperations.getMapping(PtGoodsDoc.class);

        System.out.println(map);

        PtGoodsDoc ptGoodsDoc = new PtGoodsDoc();
        ptGoodsDoc.setCategoryId(496);
        ptGoodsDoc.setThumbnail("http://javashop-statics.oss-cn-beijing.aliyuncs.com/demo/FDE6EEE32ADC4411AC625882F070452D.jpg_300x300");
        ptGoodsDoc.setSalesPrice(10.00d);
        ptGoodsDoc.setOriginPrice(15.00D);
        ptGoodsDoc.setGoodsName("海信(Hisense)LED50EC660US 50英寸");
        ptGoodsDoc.setBuyCount(0);
        ptGoodsDoc.setGoodsId(202);
        ptGoodsDoc.setSkuId(231);
        ptGoodsDoc.setCategoryPath("1,2,3");
        pinTuanSearchManager.addIndex(ptGoodsDoc);

        List list  = pinTuanSearchManager.search(1, 1, 10);
        System.out.println(list);

    }

    @Test
    public void test1(){
        List<PtGoodsDoc> list  = pinTuanSearchManager.search(1, 1, 10);
        list.forEach(goodsDoc -> {
            elasticsearchOperations.delete("javashop","pintuan_goods",""+goodsDoc.getGoodsId());
            System.out.println( goodsDoc.getGoodsId());
        });

    }


}
