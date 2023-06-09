package com.enation.app.javashop.core.promotion.minus.service.impl;

import com.enation.app.javashop.core.client.goods.GoodsClient;
import com.enation.app.javashop.core.goods.model.vo.GoodsSelectLine;
import com.enation.app.javashop.core.promotion.PromotionErrorCode;
import com.enation.app.javashop.core.promotion.minus.model.dos.MinusDO;
import com.enation.app.javashop.core.promotion.minus.model.vo.MinusVO;
import com.enation.app.javashop.core.promotion.minus.service.MinusManager;
import com.enation.app.javashop.core.promotion.tool.model.dos.PromotionGoodsDO;
import com.enation.app.javashop.core.promotion.tool.model.dto.PromotionDetailDTO;
import com.enation.app.javashop.core.promotion.tool.model.enums.PromotionStatusEnum;
import com.enation.app.javashop.core.promotion.tool.model.enums.PromotionTypeEnum;
import com.enation.app.javashop.core.promotion.tool.model.dto.PromotionGoodsDTO;
import com.enation.app.javashop.core.promotion.tool.service.PromotionGoodsManager;
import com.enation.app.javashop.core.promotion.tool.service.impl.AbstractPromotionRuleManagerImpl;
import com.enation.app.javashop.core.promotion.tool.support.PromotionCacheKeys;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.exception.NoPermissionException;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.security.model.Seller;
import com.enation.app.javashop.framework.util.BeanUtil;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * 单品立减实现类
 * @author Snow create in 2018/3/22
 * @version v2.0
 * @since v7.0.0
 */
@Service
public class MinusManagerImpl extends AbstractPromotionRuleManagerImpl implements MinusManager {

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    private PromotionGoodsManager promotionGoodsManager;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private Cache cache;


    @Override
    public MinusVO getFromDB(Integer minusId) {

        MinusDO minusDO = (MinusDO) this.cache.get(PromotionCacheKeys.getMinusKey(minusId));
        if(minusDO == null ){
            minusDO = this.daoSupport.queryForObject(MinusDO.class,minusId);
        }

        if(minusDO == null){
            return null;
        }
        MinusVO minusVO = new MinusVO();
        BeanUtils.copyProperties(minusDO,minusVO);

        List<PromotionGoodsDO> goodsDOList = this.promotionGoodsManager.getPromotionGoods(minusId,PromotionTypeEnum.MINUS.name());
        Integer[] goodsIds = new Integer[goodsDOList.size()];
        for(int i =0;i<goodsDOList.size(); i++){
            goodsIds[i] = goodsDOList.get(i).getGoodsId();
        }

        List<GoodsSelectLine> goodsSelectLineList = this.goodsClient.query(goodsIds);
        List<PromotionGoodsDTO> goodsList = new ArrayList<>();

        for(GoodsSelectLine goodsSelectLine:goodsSelectLineList){
            PromotionGoodsDTO goodsDTO = new PromotionGoodsDTO();
            BeanUtil.copyProperties(goodsSelectLine,goodsDTO);
            goodsList.add(goodsDTO);
        }

        minusVO.setGoodsList(goodsList);
        return minusVO;
    }

    @Override
    public Page list(int page,int pageSize,String keywords){

        // 获取当前登录的店铺ID
        Seller seller = UserContext.getSeller();
        Integer sellerId = seller.getSellerId();
        List params = new ArrayList();

        StringBuffer sql = new StringBuffer("select * from es_minus where seller_id = ? ");
        params.add(sellerId);

        if(!StringUtil.isEmpty(keywords)){
            sql.append(" and title like ? ");
            params.add("%"+keywords+"%");
        }

        sql.append(" order by minus_id desc");
        Page  webPage = this.daoSupport.queryForPage(sql.toString(),page, pageSize ,MinusVO.class,params.toArray());

        List<MinusVO> minusVOList = webPage.getData();
        for (MinusVO minusVO :minusVOList){
            long nowTime = DateUtil.getDateline();
            //当前时间小于活动的开始时间 则为活动未开始
            if(nowTime < minusVO.getStartTime().longValue() ){
                minusVO.setStatusText("活动未开始");
                minusVO.setStatus(PromotionStatusEnum.WAIT.toString());
                //大于活动的开始时间，小于活动的结束时间
            }else if(minusVO.getStartTime().longValue() < nowTime && nowTime < minusVO.getEndTime() ){
                minusVO.setStatusText("正在进行中");
                minusVO.setStatus(PromotionStatusEnum.UNDERWAY.toString());

            }else{
                minusVO.setStatusText("活动已失效");
                minusVO.setStatus(PromotionStatusEnum.END.toString());
            }
        }

        return webPage;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = {RuntimeException.class,Exception.class, ServiceException.class})
    public	MinusVO  add(MinusVO minusVO)	{

        this.verifyTime(minusVO.getStartTime(),minusVO.getEndTime(),PromotionTypeEnum.MINUS,null);

        //初步形成商品的DTO列表
        List<PromotionGoodsDTO> goodsDTOList = new ArrayList<>();
        //是否是全部商品参与
        if(minusVO.getRangeType() == 1){
            PromotionGoodsDTO goodsDTO = new PromotionGoodsDTO();
            goodsDTO.setGoodsId(-1);
            goodsDTO.setGoodsName("全部商品");
            goodsDTO.setThumbnail("path");
            goodsDTOList.add(goodsDTO);
            minusVO.setGoodsList(goodsDTOList);
        }
        //检测活动规则
        this.verifyRule(minusVO.getGoodsList());

        MinusDO minusDO = new MinusDO();
        BeanUtils.copyProperties(minusVO,minusDO);
        this.daoSupport.insert(minusDO);

        // 获取活动Id
        Integer minusId = this.daoSupport.getLastId("es_minus");
        minusDO.setMinusId(minusId);
        minusVO.setMinusId(minusId);

        PromotionDetailDTO detailDTO = new PromotionDetailDTO();
        detailDTO.setStartTime(minusVO.getStartTime());
        detailDTO.setEndTime(minusVO.getEndTime());
        detailDTO.setActivityId(minusVO.getMinusId());
        detailDTO.setPromotionType(PromotionTypeEnum.MINUS.name());
        detailDTO.setTitle(minusVO.getTitle());

        //将活动商品入库
        this.promotionGoodsManager.add(minusVO.getGoodsList(),detailDTO);

        String minusKey = PromotionCacheKeys.getMinusKey(minusId);
        cache.put(minusKey, minusDO);

        return minusVO;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = {RuntimeException.class,Exception.class,ServiceException.class,NoPermissionException.class})
    public	MinusVO  edit(MinusVO	minusVO,Integer id){

        //检查此活动是否可操作
        this.verifyStatus(id);

        this.verifyTime(minusVO.getStartTime(),minusVO.getEndTime(),PromotionTypeEnum.MINUS,id);

        //初步形成商品的DTO列表
        List<PromotionGoodsDTO> goodsDTOList = new ArrayList<>();
        //是否是全部商品参与
        if(minusVO.getRangeType() == 1){
            PromotionGoodsDTO goodsDTO = new PromotionGoodsDTO();
            goodsDTO.setGoodsId(-1);
            goodsDTO.setGoodsName("全部商品");
            goodsDTO.setThumbnail("");
            goodsDTOList.add(goodsDTO);
            minusVO.setGoodsList(goodsDTOList);
        }
        //检测活动规则
        this.verifyRule(minusVO.getGoodsList());

        // 获取当前登录的店铺ID
        Seller seller = UserContext.getSeller();
        Integer sellerId = seller.getSellerId();
        minusVO.setSellerId(sellerId);

        MinusDO minusDO = new MinusDO();
        BeanUtils.copyProperties(minusVO,minusDO);
        this.daoSupport.update(minusDO, id);

        //删除之前的活动与商品的对照关系
        PromotionDetailDTO detailDTO = new PromotionDetailDTO();
        detailDTO.setStartTime(minusVO.getStartTime());
        detailDTO.setEndTime(minusVO.getEndTime());
        detailDTO.setActivityId(minusVO.getMinusId());
        detailDTO.setPromotionType(PromotionTypeEnum.MINUS.name());
        detailDTO.setTitle(minusVO.getTitle());

        //将活动商品入库
        this.promotionGoodsManager.edit(minusVO.getGoodsList(),detailDTO);

        String minusKey = PromotionCacheKeys.getMinusKey(id);
        cache.put(minusKey, minusDO);

        return minusVO;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = {RuntimeException.class,Exception.class,ServiceException.class,NoPermissionException.class})
    public	void delete( Integer id)	{

        this.verifyStatus(id);
        this.daoSupport.delete(MinusDO.class,id);
        //删除单品立减商品活动对照表
        this.promotionGoodsManager.delete(id,PromotionTypeEnum.MINUS.name());
        this.cache.remove(PromotionCacheKeys.getMinusKey(id));
    }


    @Override
    public void verifyAuth(Integer minusId) {
        Seller seller = UserContext.getSeller();
        MinusDO minusDO = this.getFromDB(minusId);

        //验证越权操作
        if (minusDO == null || seller.getSellerId().intValue() != minusDO.getSellerId().intValue() ){
            throw new NoPermissionException("无权操作");
        }
    }


    /**
     * 验证此活动是否可进行编辑删除操作<br/>
     * 如有问题则抛出异常
     * @param minusId   活动id
     */
    private void verifyStatus(Integer minusId) {
        MinusVO minusVO = this.getFromDB(minusId);
        long nowTime = DateUtil.getDateline();

        //如果活动起始时间小于现在时间，活动已经开始了。
        if(minusVO.getStartTime().longValue() < nowTime && minusVO.getEndTime().longValue() > nowTime){
            throw new ServiceException(PromotionErrorCode.E400.code(),"活动已经开始，不能进行编辑删除操作");
        }
    }

}
