package com.enation.app.javashop.core.base.content;

import com.enation.app.javashop.core.base.context.Region;
import com.enation.app.javashop.core.base.context.RegionFormatter;
import com.enation.app.javashop.core.client.system.RegionsClient;
import com.enation.app.javashop.framework.test.BaseTest;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Locale;


/**
 * 地区格式化单元测试
 *
 * @author zh
 * @version v7.0
 * @date 18/5/8 下午5:27
 * @since v7.0
 */

public class RegionFormatterTest extends BaseTest {

    @Autowired
    private RegionsClient regionsClient;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * region地区格式化校验
     *
     * @throws Exception
     */
    @Test
    public void parseTest() throws Exception {
        RegionFormatter regionFormatter = new RegionFormatter(regionsClient);
        //地区不存在校验
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("地区不合法");
        regionFormatter.parse("88888", Locale.US);

        //校验如果传值地区为两级(只允许传第三级或者第四级)
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("地区不合法");
        regionFormatter.parse("142", Locale.US);
        //地区为空校验
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("地区不合法");
        regionFormatter.parse("", Locale.US);
        //正确性校验
        Region regionFormatterData = regionFormatter.parse("5008", Locale.US);
        Region region = new Region();
        region.setCity("石家庄市");
        region.setProvince("河北");
        region.setCounty("辛集市");
        region.setTown("辛集镇");
        region.setProvinceId(5);
        region.setCityId(142);
        region.setCountyId(143);
        region.setTownId(5008);
        Assert.assertEquals(region, regionFormatterData);


    }
}
