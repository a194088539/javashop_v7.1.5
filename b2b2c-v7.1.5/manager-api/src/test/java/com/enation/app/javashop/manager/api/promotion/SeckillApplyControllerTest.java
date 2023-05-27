package com.enation.app.javashop.manager.api.promotion;

import com.enation.app.javashop.core.client.goods.GoodsClient;
import com.enation.app.javashop.core.goods.model.vo.CacheGoods;
import com.enation.app.javashop.core.promotion.seckill.model.dos.SeckillApplyDO;
import com.enation.app.javashop.core.promotion.seckill.model.vo.SeckillVO;
import com.enation.app.javashop.core.promotion.seckill.service.SeckillGoodsManager;
import com.enation.app.javashop.core.promotion.seckill.service.SeckillManager;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 平台管理—限时抢购申请测试
 *
 * @author Snow create in 2018/7/12
 * @version v2.0
 * @since v7.0.0
 */
@Transactional(value = "tradeTransactionManager",rollbackFor = Exception.class)
public class SeckillApplyControllerTest extends BaseTest {

    @Autowired
    private SeckillManager seckillManager;

    @Autowired
    private SeckillGoodsManager seckillApplyManager;

    private SeckillVO seckillVO;

    private List<SeckillApplyDO> applyDOList1 = new ArrayList<>();

    @MockBean
    private GoodsClient goodsClient;

    @Before
    @Transactional(value = "tradeTransactionManager",rollbackFor = Exception.class)
    public void testData() {

        //限时抢购活动的信息
        List<Integer> rangeList = new ArrayList<>();
        rangeList.add(1);
        rangeList.add(2);
        rangeList.add(3);

        seckillVO = new SeckillVO();
        seckillVO.setSeckillName("限时抢购活动名称");
        seckillVO.setStartDay(2534947200l);
        seckillVO.setApplyEndTime(2450566400l);
        seckillVO.setSeckillRule("活动规则");
        seckillVO.setRangeList(rangeList);
        seckillVO = this.seckillManager.add(seckillVO);


        //场景1：需要的参数
        SeckillApplyDO applyDO1 = new SeckillApplyDO();
        applyDO1.setSeckillId(seckillVO.getSeckillId());
        applyDO1.setTimeLine(1);
        applyDO1.setStartDay(seckillVO.getStartDay());
        applyDO1.setGoodsId(1);
        applyDO1.setGoodsName("商品名称");
        applyDO1.setPrice(11.0);
        applyDO1.setSoldQuantity(100);
        applyDOList1.add(applyDO1);

        CacheGoods goods = new CacheGoods();
        goods.setThumbnail("path");
        goods.setGoodsId(1);
        goods.setGoodsName("商品名称");
        goods.setPrice(11.0);
        goods.setEnableQuantity(101);
        when (goodsClient.getFromCache(1)).thenReturn(goods);

        this.seckillApplyManager.addApply(applyDOList1);
    }


    /**
     * 读取限时抢购活动申请列表
     * @throws Exception
     */
    @Test
    @Transactional(value = "tradeTransactionManager",rollbackFor = Exception.class)
    public void testGetPage() throws Exception {

        //场景一
        String json = mockMvc.perform(get("/admin/promotion/seckill-applys")
                .header("Authorization", superAdmin)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("page_no", "1").param("page_size", "10")
                .param("seckill_id", seckillVO.getSeckillId() + ""))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        Page page = JsonUtil.jsonToObject(json, Page.class);
        if (page.getDataTotal() == 0) {
            throw new RuntimeException("读取出错");
        }
    }
}
