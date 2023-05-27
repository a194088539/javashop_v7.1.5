package com.enation.app.javashop.core.promotion.pintuan;

import com.enation.app.javashop.core.promotion.pintuan.service.PintuanGoodsManager;
import com.enation.app.javashop.core.promotion.pintuan.service.PintuanManager;
import com.enation.app.javashop.framework.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2019-03-04
 */

public class PinTuanTest  extends BaseTest {

    @Autowired
    private PintuanManager pintuanManager;

    @Autowired
    private PintuanGoodsManager pintuanGoodsManager;

    @Test
    public void test() {
        pintuanGoodsManager.addIndex(108);

     }
}
