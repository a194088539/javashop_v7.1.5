package com.enation.app.javashop.core.base.content;

import com.enation.app.javashop.core.base.DomainSettings;
import com.enation.app.javashop.framework.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Created by kingapex on 2018/6/20.
 *
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/6/20
 */
public class DomainSettingsTest extends BaseTest {

    @Autowired
    private DomainSettings domainSettings;

    @Test
    public void test() {
        Assert.notNull(domainSettings, "domainSettings 对象未能正确初始化");
    }

}
