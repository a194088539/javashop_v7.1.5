package com.enation.app.javashop.manager.api.base;

import com.enation.app.javashop.core.base.SearchCriteria;
import com.enation.app.javashop.core.base.model.vo.BackendIndexModelVO;
import com.enation.app.javashop.core.goods.service.GoodsManager;
import com.enation.app.javashop.core.member.service.MemberManager;
import com.enation.app.javashop.core.statistics.model.enums.QueryDateType;
import com.enation.app.javashop.core.statistics.model.vo.SalesTotal;
import com.enation.app.javashop.core.statistics.service.OrderStatisticManager;
import com.enation.app.javashop.framework.test.BaseTest;
import io.swagger.annotations.Api;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 首页api单元测试
 *
 * @author chopper
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2018/7/2 上午5:35
 */

public class IndexPageControllerTest extends BaseTest {

    @Test
    public void index() throws Exception {
        mockMvc.perform(get("/admin/index/page")
                .header("Authorization",superAdmin))
                .andExpect(status().is(200));
    }


}
