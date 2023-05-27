package com.enation.app.javashop.core.member.model.vo;

import com.enation.app.javashop.core.member.model.dos.ReceiptHistory;
import com.enation.app.javashop.core.trade.order.model.vo.OrderSkuVO;
import com.enation.app.javashop.framework.util.JsonUtil;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 会员开票历史记录对象vo
 *
 * @author duanmingyu
 * @version v7.1.4
 * @since v7.0.0
 * 2019-06-20
 */
public class ReceiptHistoryVO extends ReceiptHistory {

    /**
     * 订单购买的商品sku信息集合
     */
    @ApiModelProperty(value = "订单购买的商品sku信息集合")
    private List<OrderSkuVO> orderSkuList;

    /**
     * 电子发票附件集合
     */
    @ApiModelProperty(value = "电子发票附件集合")
    private List<String> elecFileList;

    public List<OrderSkuVO> getOrderSkuList() {

        if (this.getGoodsJson() != null) {
            return JsonUtil.jsonToList(this.getGoodsJson(), OrderSkuVO.class);
        }
        return null;
    }

    public void setOrderSkuList(List<OrderSkuVO> orderSkuList) {
        this.orderSkuList = orderSkuList;
    }

    public List<String> getElecFileList() {
        return elecFileList;
    }

    public void setElecFileList(List<String> elecFileList) {
        this.elecFileList = elecFileList;
    }

    @Override
    public String toString() {
        return "ReceiptHistoryVO{" +
                "orderSkuList=" + orderSkuList +
                ", elecFileList=" + elecFileList +
                '}';
    }
}
