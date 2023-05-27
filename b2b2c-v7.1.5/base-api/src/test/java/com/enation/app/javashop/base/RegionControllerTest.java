package com.enation.app.javashop.base;

import com.enation.app.javashop.core.system.model.dos.Regions;
import com.enation.app.javashop.core.system.model.vo.RegionsVO;
import com.enation.app.javashop.core.system.service.RegionsManager;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 地区api校验
 *
 * @author zh
 * @version v7.0
 * @date 18/5/29 下午9:04
 * @since v7.0
 */
@Transactional(value = "systemTransactionManager", rollbackFor = Exception.class)
public class RegionControllerTest extends BaseTest {

    @Autowired
    private RegionsManager regionsManager;
    /**
     * 一级地区id
     */
    private Integer regionId = 0;
    /**
     * 第一个二级地区id
     */
    private Integer regionTwo1 = 0;
    /**
     * 第二个二级地区id
     */
    private Integer regionTwo2 = 0;

    @Before
    public void dataPreparation() {
        //添加一级地区
        RegionsVO regionsVO = new RegionsVO();
        regionsVO.setLocalName("a");
        regionsVO.setParentId(0);
        regionsVO.setZipcode("123123");
        regionsVO.setCod(1);
        Regions regions = regionsManager.add(regionsVO);
        regionId = regions.getId();
        //添加二级地区
        regionsVO.setLocalName("a_1");
        regionsVO.setParentId(regions.getId());
        regionsVO.setZipcode("123123");
        regionsVO.setCod(1);
        Regions regions1 = regionsManager.add(regionsVO);
        regionTwo1 = regions1.getId();
        regionsVO.setLocalName("a_2");
        regionsVO.setParentId(regions.getId());
        regionsVO.setZipcode("123123");
        regionsVO.setCod(1);
        Regions regions2 = regionsManager.add(regionsVO);
        regionTwo2 = regions2.getId();

    }

    /**
     * 根据地区id获取子地区数据
     *
     * @throws Exception
     */
    @Test
    public void getChildrenByIdTest() throws Exception {
        ErrorMessage error = new ErrorMessage("003", "此地区不存在");
        //地区不存在校验
        mockMvc.perform(get("/regions/111111/children"))
                .andExpect(status().is(404))
                .andExpect(objectEquals(error));
        //正确性校验
        String json = mockMvc.perform(get("/regions/" + regionId+"/children"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        List<Regions> list = new ArrayList<>();
        Regions regions = new Regions();
        regions.setId(regionTwo1);
        regions.setRegionGrade(2);
        regions.setRegionPath("," + regionId + "," + regionTwo1 + ",");
        regions.setCod(1);
        regions.setLocalName("a_1");
        regions.setParentId(regionId);
        regions.setZipcode("123123");
        list.add(regions);
        regions = new Regions();
        regions.setId(regionTwo2);
        regions.setRegionGrade(2);
        regions.setRegionPath("," + regionId + "," + regionTwo2 + ",");
        regions.setCod(1);
        regions.setLocalName("a_2");
        regions.setZipcode("123123");
        regions.setParentId(regionId);
        list.add(regions);
        Assert.assertEquals(json, JsonUtil.objectToJson(list));
    }
}
