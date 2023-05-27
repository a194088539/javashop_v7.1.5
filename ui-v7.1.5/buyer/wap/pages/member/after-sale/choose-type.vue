<template>
  <div id="choose-type" style="background-color: #f7f7f7">
    <van-nav-bar left-arrow title="选择售后类型" @click-left="MixinRouterBack"></van-nav-bar>
    <div class="after-sale-container">
      <div class="goods-info-box">
        <div class="seller-tip">
          <span class="tip01">
            本次售后服务将由<span class="tip02">{{ applyInfo.seller_name }}</span>为您提供
          </span>
        </div>
        <div class="goods-info-item">
          <div class="goods-img">
            <img :src="applyInfo.goods_img">
          </div>
          <div class="item-name">
            <span>{{ applyInfo.goods_name }}</span>
            <div class="price-num">
              <span class="price-show">单价：{{ applyInfo.goods_price | unitPrice('￥') }}</span>
              <span class="price-show">购买数量：{{ applyInfo.buy_num }}</span>
            </div>
          </div>
        </div>
      </div>
      <div class="service-type-box">
        <div class="box-info">
          <div class="info-return" @click="handleApplyService('RETURN_GOODS')" v-if="applyInfo.allow_return_goods">
            <div class="icon-con">
              <div>
                <img src="../../../assets/images/icon-tuihuo.png">
              </div>
              <span>退货</span>
            </div>
            <div class="title-tips">
              <span>退回收到的商品</span>
            </div>
            <van-icon name="arrow" style="margin-right: 5px;"/>
          </div>
          <div class="divider-line"></div>
          <div class="info-return" @click="handleApplyService('CHANGE_GOODS')">
            <div class="icon-con">
              <div>
                <img src="../../../assets/images/icon-huanhuo.png">
              </div>
              <span>换货</span>
            </div>
            <div class="title-tips">
              <span>更换收到的商品</span>
            </div>
            <van-icon name="arrow" style="margin-right: 5px;"/>
          </div>
          <div class="divider-line"></div>
          <div class="info-return" @click="handleApplyService('SUPPLY_AGAIN_GOODS')">
            <div class="icon-con">
              <div>
                <img src="../../../assets/images/icon-bufa.png">
              </div>
              <span>补发商品</span>
            </div>
            <div class="title-tips">
              <span>商家少发商品</span>
            </div>
            <van-icon name="arrow" style="margin-right: 5px;"/>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
  import * as API_AfterSale from '@/api/after-sale'
  export default {
    name: 'choose-type',
    head() {
      return {
        title: `申请售后-${this.site.title}`
      }
    },
    data() {
      return {
        /** 售后服务单号 */
        order_sn: this.$route.query.orderSn,
        /** 商品skuID */
        sku_id: this.$route.query.skuId,
        /** 页面展示的信息 */
        applyInfo: ''
      }
    },
    mounted() {
      this.GET_AfterSaleApplyInfo()
    },
    methods: {
      /** 申请售后 */
      handleApplyService(type) {
        this.$router.replace({ path: '/member/after-sale/apply', query: {'orderSn':this.order_sn, 'skuId':this.sku_id, 'serviceType':type}})
      },

      GET_AfterSaleApplyInfo() {
        API_AfterSale.getApplyInfo(this.order_sn, this.sku_id).then(response => {
          this.applyInfo = response;
        })
      }
    }
  }
</script>
<style type="text/scss" lang="scss" scoped>
  @import "../../../assets/styles/color";
  div {
    position: relative;
    box-sizing: border-box;
    display: flex;
    -webkit-box-orient: vertical;
    -webkit-box-direction: normal;
    flex-direction: column;
    flex-shrink: 0;
  }
  .after-sale-container {
    padding-top: 46px;
  }
  .goods-info-box {
    padding-top: 6px;
    padding-bottom: 19px;
    align-items: center;
    background-color: rgb(255, 255, 255);
    border-bottom-left-radius: 13px;
    border-bottom-right-radius: 13px;
    .seller-tip {
      justify-content: center;
      align-items: center;
      padding: 4px 13px;
      margin-bottom: 22px;
      background-color: rgb(246, 246, 246);
      border-style: solid;
      border-width: 1px;
      border-color: rgb(246, 246, 246);
      border-radius: 55px;
      span {
        display: inline-block;
        margin: 0px;
        padding: 0px;
      }
      .tip01 {
        color: rgb(132, 132, 132);
        font-size: 12px;
        line-height: 14.4px;
      }
      .tip02 {
        color: rgb(255, 0, 0);
        line-height: 19.2px;
      }
    }
    .goods-info-item {
      flex-direction: row;
      align-items: center;
      width: 100%;
      .goods-img {
        width: 30%;
      }
      img {
        justify-content: flex-end;
        align-items: flex-end;
        height: 66px;
        width: 66px;
        margin: 0px 22px;
        border-radius: 4px;
      }
      .item-name {
        height: 72px;
        width: 70%;
        justify-content: space-between;
        span {
          display: -webkit-box;
          padding: 0px;
          color: rgb(132, 132, 132);
          font-size: 11px;
          line-height: 18px;
          overflow: hidden;
          text-overflow: ellipsis;
          overflow-wrap: break-word;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
          max-height: 36px;
          white-space: normal;
          margin-right: 5px;
        }
        .price-num {
          flex: 1 1 0%;
          flex-direction: row;
          margin-top: 14px;
          .price-show {
            display: inline-block;
            margin: 0px 11px 0px 0px;
            padding: 0px;
            font-size: 12px;
            font-family: JDZhengHT-Regular;
            line-height: 14.4px;
          }
        }
      }
    }
  }
  .service-type-box {
    flex: 1 1 0%;
    padding: 16px 11px 33px;
    .box-info {
      background-color: rgb(255, 255, 255);
      border-radius: 13px;
      .info-return {
        height: 82px;
        flex-direction: row;
        justify-content: space-between;
        align-items: center;
        padding-left: 16px;
        padding-right: 22px;
        .icon-con {
          flex-direction: row;
          align-items: center;
          img {
            width: 27px;
            height: 27px;
            margin-right: 11px;
          }
          span {
            display: inline-block;
            margin: 0px;
            padding: 0px;
            font-size: 16px;
            font-weight: bold;
            line-height: 19.2px;
          }
        }
        .title-tips {
          flex-direction: row;
          align-items: center;
          justify-content: flex-end;
          flex: 1 1 0%;
          padding-left: 11px;
          span {
            display: inline-block;
            margin: 0px 11px 0px 0px;
            padding: 0px;
            text-align: right;
            color: rgb(132, 132, 132);
            font-size: 11px;
            justify-content: flex-end;
            line-height: 13.2px;
          }
        }
      }
      .divider-line {
        margin-left: 16px;
        margin-right: 16px;
        border-style: solid;
        border-width: 0px 0px 1px;
        border-color: black black rgb(238, 238, 238);
      }
    }
  }
</style>

