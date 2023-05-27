package com.enation.app.javashop.core.member.service.impl;

import com.enation.app.javashop.core.client.goods.GoodsClient;
import com.enation.app.javashop.core.client.member.MemberClient;
import com.enation.app.javashop.core.goods.model.vo.CacheGoods;
import com.enation.app.javashop.core.member.model.dos.HistoryDO;
import com.enation.app.javashop.core.member.model.dos.Member;
import com.enation.app.javashop.core.member.model.dto.HistoryDTO;
import com.enation.app.javashop.core.member.model.dto.HistoryDelDTO;
import com.enation.app.javashop.core.member.model.vo.HistoryVO;
import com.enation.app.javashop.core.member.service.HistoryManager;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.IntegerMapper;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 会员足迹业务类
 *
 * @author zh
 * @version v7.1.4
 * @since vv7.1
 * 2019-06-18 15:18:56
 */
@Service
public class HistoryManagerImpl implements HistoryManager {

    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private MemberClient memberClient;

    @Override
    public Page list(int page, int pageSize) {
        //根据天的日期分组，分页查询出日期
        String sql = "select update_time from es_history where member_id = ? group by update_time order by create_time desc";
        Page webPage = this.daoSupport.queryForPage(sql, page, pageSize, UserContext.getBuyer().getUid());
        List<Map<String, Object>> data = webPage.getData();
        if (data != null && data.size() > 0) {
            List<HistoryVO> historyVOS = new ArrayList<>();
            String time = "";
            //从结果中提取天的时间并且组织成以下sql查询的条件
            for (Map map : data) {
                HistoryVO historyVO = new HistoryVO();
                historyVO.setTime((long) map.get("update_time"));
                historyVOS.add(historyVO);
                time += map.get("update_time") + ",";
            }
            time = time.substring(0, time.length() - 1);
            //根据日期查询出此会员在以上结果的天里面的浏览足迹
            sql = "select * from es_history where member_id = ? and update_time in (" + time + ") order by create_time desc";
            List<HistoryDO> historyDOS = this.daoSupport.queryForList(sql, HistoryDO.class, UserContext.getBuyer().getUid());
            //将查询出的商品组织成要输出的格式，格式是
            //data[{
            //  time:00000
            //  history:{historyDo,historyDO}
            // }]
            for (HistoryVO historyVO : historyVOS) {
                List<HistoryDO> list = new ArrayList<>();
                for (HistoryDO history : historyDOS) {
                    if (history.getUpdateTime().equals(historyVO.getTime())) {
                        list.add(history);
                    }
                    historyVO.setHistory(list);
                }
            }
            //将组织好的数据放入返回对象里面
            webPage.setData(historyVOS);
        }
        return webPage;
    }


    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public HistoryDO edit(HistoryDO historyDO, Integer id) {
        this.daoSupport.update(historyDO, id);
        return historyDO;
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delete(HistoryDelDTO historyDelDTO) {
        StringBuffer sql = new StringBuffer(" delete from es_history where member_id = ? ");
        List<Object> term = new ArrayList<>();
        term.add(historyDelDTO.getMemberId());
        if (historyDelDTO.getDate() != null) {
            sql.append(" and update_time = ?");
            term.add(historyDelDTO.getDate());
        }
        if (historyDelDTO.getId() != null) {
            sql.append(" and id = ? ");
            term.add(historyDelDTO.getId());
        }
        this.daoSupport.execute(sql.toString(),term.toArray());
    }

    @Override
    public void delete(Integer memberId) {
         //根据时间最新查询100条数据
        String  sql = " select id from es_history where member_id = ?  order by create_time desc limit 99";

        List<Integer> ids = this.daoSupport.queryForList(sql,new IntegerMapper(),memberId);


        //设置参数
        List<Integer> term = new ArrayList<>();

        term.add(memberId);
        term.addAll(ids);

        List<String> idList = new ArrayList<>();
        for (Integer id: ids) {
            idList.add("?");
        }

        String idStr = StringUtil.listToString(idList,",");

        sql = "delete from es_history where member_id = ?  and id not in ("+ idStr+")";

        //删除多余数据
        this.daoSupport.execute(sql,term.toArray());


    }

    @Override
    public HistoryDO getModel(Integer id) {
        return this.daoSupport.queryForObject(HistoryDO.class, id);
    }

    @Override
    public HistoryDO getHistoryByGoods(Integer goodsId, Integer memberId) {
        return this.daoSupport.queryForObject("select * from es_history where goods_id = ? and member_id = ?", HistoryDO.class, goodsId, memberId);
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addMemberHistory(HistoryDTO historyDTO) {
        //会员id
        Integer memberId = historyDTO.getMemberId();
        //校验此商品是否存在并且上架状态
        CacheGoods cacheGoods = goodsClient.getFromCache(historyDTO.getGoodsId());
        //如果商品为下架状态则不记录足迹
        if (!cacheGoods.getMarketEnable().equals(1)) {
            return;
        }
        //检测此商品是否已经存在浏览记录
        HistoryDO historyDO = this.getHistoryByGoods(historyDTO.getGoodsId(), historyDTO.getMemberId());
        //如果为空则是添加反之为修改
        if (historyDO != null) {
            historyDO.setCreateTime(DateUtil.getDateline());
            historyDO.setUpdateTime(getDateDay());
            historyDO.setGoodsName(cacheGoods.getGoodsName());
            historyDO.setGoodsPrice(cacheGoods.getPrice());
            historyDO.setGoodsImg(cacheGoods.getThumbnail());
            this.edit(historyDO, historyDO.getId());
            return;
        }
        //校验如果从会员足迹已经超过一百个，需要删除历史最早的
        String sql = "select count(1) from es_history where member_id = ?";
        Integer count = this.daoSupport.queryForInt(sql, memberId);
        if (count >= 100) {
            this.delete(historyDTO.getMemberId());
        }
        //获取当前会员
        Member member = memberClient.getModel(historyDTO.getMemberId());
        //如果当前会员不存在，则不记录信息
        if (member == null) {
            return;
        }
        historyDO = new HistoryDO();
        historyDO.setGoodsImg(cacheGoods.getThumbnail());
        historyDO.setGoodsName(cacheGoods.getGoodsName());
        historyDO.setGoodsId(historyDTO.getGoodsId());
        historyDO.setGoodsPrice(cacheGoods.getPrice());
        historyDO.setMemberId(memberId);
        historyDO.setMemberName(member.getUname());
        historyDO.setCreateTime(DateUtil.getDateline());
        historyDO.setUpdateTime(getDateDay());
        this.daoSupport.insert(historyDO);
    }

    /**
     * 获取当前天的时间戳
     *
     * @return 当前天的时间戳
     */
    private static long getDateDay() {
        String res = DateUtil.toString(new Date(), "yyyy-MM-dd");
        return DateUtil.getDateline(res);
    }
}
