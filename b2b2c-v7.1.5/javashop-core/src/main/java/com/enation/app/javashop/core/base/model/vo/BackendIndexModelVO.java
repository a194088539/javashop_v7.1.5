package com.enation.app.javashop.core.base.model.vo;

import com.enation.app.javashop.core.goods.model.vo.BackendGoodsVO;
import com.enation.app.javashop.core.member.model.vo.BackendMemberVO;
import com.enation.app.javashop.core.statistics.model.vo.SalesTotal;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;

import java.util.List;

/**
 * 后台首页模型
 *
 * @author chopper
 * @version v1.0
 * @since v7.0
 * 2018-06-29 上午9:03
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BackendIndexModelVO {

    private SalesTotal salesTotal;

    private List<BackendGoodsVO> goodsVos;

    private List<BackendMemberVO> memberVos;

    public SalesTotal getSalesTotal() {
        return salesTotal;
    }

    public void setSalesTotal(SalesTotal salesTotal) {
        this.salesTotal = salesTotal;
    }


    public List<BackendGoodsVO> getGoodsVos() {
        return goodsVos;
    }

    public void setGoodsVos(List<BackendGoodsVO> goodsVos) {
        this.goodsVos = goodsVos;
    }

    public List<BackendMemberVO> getMemberVos() {
        return memberVos;
    }

    public void setMemberVos(List<BackendMemberVO> memberVos) {
        this.memberVos = memberVos;
    }

    @Override
    public String toString() {
        return "BackendIndexModelVO{" +
                "salesTotal=" + salesTotal +
                ", goodsVos=" + goodsVos +
                ", memberVos=" + memberVos +
                '}';
    }
}
