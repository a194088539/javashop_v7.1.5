<template>
  <div id="after-sale" style="background-color: #f7f7f7">
    <van-nav-bar left-arrow title="售后管理" @click-left="MixinRouterBack"></van-nav-bar>
    <van-tabs v-model="tabActive" :swipe-threshold="2" @change="handleTabChange">
      <van-tab title="申请售后"/>
      <van-tab title="申请记录"/>
    </van-tabs>
    <div class="after-sale-container" v-if="tabActive === 0">
      <empty-member v-if="finished && !orderList.length">暂无可申请售后的订单</empty-member>
      <van-list v-else v-model="loading" :finished="finished" @load="onLoad">
        <div class="react-view main-page" v-for="(order, index) in orderList" :key="index">
          <div class="react-view seller-box">
            <div class="react-view seller-box-content">
              <div class="react-view">
                <img src="../../../assets/images/icon-shop.png">
              </div>
              <a :href="'/shop/' + order.seller_id">
                <span>{{ order.seller_name }}</span>
              </a>
            </div>
          </div>
          <div class="react-view goods-box" v-for="goods in order.sku_list" :key="goods.sku_id">
            <div class="react-view goods-box-content">
              <div class="react-view goods-box-img">
                <a :href="'/goods/' + goods.goods_id">
                  <img :src="goods.goods_image">
                </a>
              </div>
              <div class="react-view goods-box-name">
                <a :href="'/goods/' + goods.goods_id">
                  <span class="name-span">{{ goods.name }}</span>
                </a>
                <div class="react-view">
                  <span class="num-span">数量：{{ goods.num }}</span>
                </div>
              </div>
            </div>
            <div class="react-view btn-box">
              <div class="react-view btn-box-left">
                <div @click="handleTitleShow" v-if="!goods.goods_operate_allowable_vo.allow_apply_service">
                  <span>该商品无法申请售后</span>
                  <img src="../../../assets/images/icon-warn.png">
                </div>
              </div>
              <div class="react-view btn-box-right" @click="handleApplyService(order.sn, goods.sku_id)" v-if="goods.goods_operate_allowable_vo.allow_apply_service">
                <span>申请售后</span>
              </div>
              <div class="react-view btn-box-right" v-else style="border-color: rgb(250, 190, 183);">
                <span style="color: rgb(250, 190, 183);">申请售后</span>
              </div>
            </div>
          </div>
        </div>
      </van-list>
    </div>
    <div class="after-sale-container" v-if="tabActive === 1">
      <empty-member v-if="finished && !afterSaleList.length">暂无售后申请记录</empty-member>
      <van-list v-else v-model="loading" :finished="finished" @load="onLoad">
        <div class="react-view main-page" v-for="(afterSale, index) in afterSaleList" :key="index">
          <nuxt-link :to="'./after-sale/detail?service_sn=' + afterSale.service_sn">
            <div class="react-view seller-box-content">
              <div class="react-view service-num-box">
                <span>服务单号：{{ afterSale.service_sn }}</span>
                <div class="react-view" style="flex-direction: row; align-items: center;">
                  <img src="../../../assets/images/icon-huanhuo.png" v-if="afterSale.service_type === 'CHANGE_GOODS'">
                  <img src="../../../assets/images/icon-bufa.png" v-if="afterSale.service_type === 'SUPPLY_AGAIN_GOODS'">
                  <img src="../../../assets/images/icon-tuihuo.png" v-if="afterSale.service_type === 'RETURN_GOODS' || afterSale.service_type === 'ORDER_CANCEL'">
                  <span>{{ afterSale.service_type_text }}</span>
                </div>
              </div>
            </div>
            <div class="react-view box-line"></div>
            <div class="react-view goods-box">
              <div class="react-view goods-box-content" v-for="goods in afterSale.goods_list" :key="goods.sku_id" style="padding: 15px 0px;">
                <div class="react-view goods-box-img">
                  <img :src="goods.goods_image">
                </div>
                <div class="react-view goods-box-name">
                  <span class="name-span">{{ goods.goods_name }}</span>
                  <div class="react-view">
                    <span class="num-span">申请数量：{{ goods.return_num }}</span>
                  </div>
                </div>
              </div>
              <div class="react-view" style="width: 100%;">
                <div class="react-view service-status-box">
                  <span>{{ afterSale.service_status_text }}</span>
                  <div class="react-view" style="flex: 1 1 0%;">
                    <div class="react-view" style="background-color: transparent; justify-content: center; align-items: flex-start; flex: 1 1 0%;">
                      <span v-if="afterSale.allowable.allow_ship">请您尽快将申请售后的商品退还给卖家</span>
                      <span v-else>{{ afterSale.service_status | statusFilter }}</span>
                    </div>
                  </div>
                  <van-icon name="arrow" style="margin-right: 5px;"/>
                </div>
              </div>
            </div>
          </nuxt-link>
        </div>
      </van-list>
    </div>
  </div>
</template>

<script>
  import Vue from 'vue'
  import { Dialog } from 'vant'
  Vue.use(Dialog)
  import * as API_Order from '@/api/order'
  import * as API_AfterSale from '@/api/after-sale'
  export default {
    name: 'after-sale-index',
    head() {
      return {
        title: `售后列表-${this.site.title}`
      }
    },
    data() {
      return {
        /** 是否加载 */
        loading: false,
        /** 是否终止加载 */
        finished: false,
        /** 当前tab的index */
        tabActive: 0,
        /** 订单数据集合 */
        orderList: [],
        /** 售后服务单数据集合 */
        afterSaleList: [],
        /** 参数 */
        params: {
          page_no: 0,
          page_size: 10
        }
      }
    },
    filters: {
      statusFilter(val) {
        switch (val) {
          case 'APPLY':
            return '售后服务申请成功，等待商家审核'
          case 'PASS':
            return '售后服务申请审核通过'
          case 'REFUSE':
            return '售后服务申请已被商家拒绝，如有疑问请及时联系商家'
          case 'FULL_COURIER':
            return '申请售后的商品已经寄出，等待商家收货'
          case 'STOCK_IN':
            return '商家已将售后商品入库'
          case 'WAIT_FOR_MANUAL':
            return '等待平台进行人工退款'
          case 'REFUNDING':
            return '商家退款中，请您耐心等待'
          case 'COMPLETED':
            return '售后服务已完成，感谢您对Javashop的支持'
          case 'ERROR_EXCEPTION':
            return '系统生成新订单异常，等待商家手动创建新订单'
          case 'CLOSED':
            return '售后服务已关闭'
          default:
            return ''
        }
      }
    },
    methods: {
      /** Tab切换事件绑定 */
      handleTabChange() {
        this.loading = false
        this.finished = false
        this.params.page_no = 0
        if (this.tabActive === 0) {
          this.orderList = []
        } else {
          this.afterSaleList = []
        }
        this.onLoad()
      },

      /** 弹出提示 */
      handleTitleShow() {
        Dialog.alert({
          closeOnClickOverlay: true,
          message: '当订单未确认收货|已过售后服务有效期|已申请售后服务时，不能申请售后'
        }).then(() => {})
      },

      /** 申请售后 */
      handleApplyService(orderSn, skuId) {
        this.$router.push({ path: '/member/after-sale/choose-type', query: {'orderSn':orderSn, 'skuId':skuId}})
      },

      /** 加载售后数据 */
      onLoad() {
        this.params.page_no += 1
        if (this.tabActive === 0) {
          this.GET_OrderList()
        } else {
          this.GET_AfterSaleServiceList()
        }
      },

      /** 获取订单数据 */
      GET_OrderList() {
        this.loading = true
        API_Order.getOrderList(this.params).then(response => {
          const { data } = response
          if(!data || !data.length) {
            this.finished = true
          } else {
            this.orderList.push(...data)
          }
          this.loading = false
        })
      },

      /** 获取已申请的售后服务数据 */
      GET_AfterSaleServiceList() {
        this.loading = true
        API_AfterSale.getAfterSaleList(this.params).then(response => {
          const { data } = response
          if(!data || !data.length) {
            this.finished = true
          } else {
            this.afterSaleList.push(...data)
          }
          this.loading = false
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../../assets/styles/color";
  .after-sale-container {
    padding-top: 92px;
  }
  .react-view {
    position: relative;
    box-sizing: border-box;
    display: flex;
    flex-direction: column;
    flex-shrink: 0;
  }
  .main-page {
    margin-top: 11px;
    background-color: rgb(255, 255, 255);
  }
  .seller-box {
    flex-direction: row;
    align-items: center;
    padding: 11px 16px;
    border-style: solid;
    border-width: 0px 0px 1px;
    border-color: black black rgb(240, 240, 240);
    background-color: rgb(255, 255, 255);
    margin-bottom: 11px;
  }
  .goods-box {
    background-color: rgb(255, 255, 255);
    flex-direction: column;
    align-items: flex-start;
    padding-right: 16px;
    padding-left: 16px;
  }
  .seller-box-content {
    flex: 1 1 0%;
    flex-direction: row;
    align-items: center;
    img {
      width: 16px;
      height: 15px;
    }
    span {
      display: inline-block;
      margin: 0px 0px 0px 5px;
      padding: 0px;
      color: rgb(102, 102, 102);
      font-size: 12px;
      line-height: 14.4px;
    }
  }
  .goods-box-content {
    flex-direction: row;
    align-items: stretch;
  }
  .goods-box-img {
    background-color: transparent;
    cursor: pointer;
    img {
      justify-content: flex-end;
      align-items: flex-end;
      height: 66px;
      width: 66px;
      margin-right: 16px;
      border-radius: 4px;
    }
  }
  .goods-box-name {
    flex: 1 1 0%;
    flex-direction: column;
    align-items: flex-start;
    justify-content: space-between;

    .name-span {
      display: -webkit-box;
      margin: 0px;
      padding: 0px;
      font-size: 13px;
      color: rgb(46, 45, 45);
      line-height: 18px;
      overflow: hidden;
      text-overflow: ellipsis;
      overflow-wrap: break-word;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      max-height: 36px;
      white-space: normal;
    }
    .num-span {
      display: inline-block;
      margin: 0px 0px 7px;
      padding: 0px;
      font-size: 11px;
      color: rgb(132, 132, 132);
      line-height: 19px;
    }
  }
  .btn-box {
    width: 100%;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 11px;
    margin-top: 11px;
    background-color: rgb(255, 255, 255);
  }
  .btn-box-left {
    width: 70%;
    flex-direction: row;
    justify-content: flex-start;
    align-items: center;
    span {
      display: inline-block;
      margin: 0px;
      padding: 0px;
      font-size: 11px;
      color: rgb(132, 132, 132);
      line-height: 13.2px;
      float: left;
    }
    img {
      width: 12px;
      height: 12px;
      margin-left: 5px;
      float: left;
    }
  }
  .btn-box-right {
    border-style: solid;
    border-width: 1px;
    border-radius: 22px;
    padding: 8px 17px;
    flex-direction: row;
    justify-content: center;
    align-items: center;
    border-color: rgb(240, 37, 15);
    span {
      display: inline-block;
      margin: 0px; padding: 0px;
      font-size: 13px;
      color: rgb(240, 37, 15);
      line-height: 15.6px;
    }
  }
  .service-num-box {
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    height: 38px;
    padding-right: 16px;
    padding-left: 16px;
    width: 100%;
    span {
      display: inline-block;
      margin: 0px;
      padding: 0px;
      font-size: 13px;
      color: rgb(102, 102, 102);
      line-height: 15.6px;
    }
    img {
      width: 16px;
      height: 16px;
      margin-right: 5px;
    }
  }
  .box-line {
    border-style: solid;
    border-width: 0px 0px 1px;
    border-color: black black rgb(240, 240, 240);
  }
  .service-status-box {
    flex-direction: row;
    align-items: center;
    height: 54px;
    margin: 16px 0px;
    background-color: rgb(246, 246, 246);
    border-style: solid;
    border-width: 1px;
    border-color: rgb(246, 246, 246);
    border-radius: 4px;
    span {
      display: -webkit-box;
      margin: 0px;
      padding: 0px 23px 0px 16px;
      font-size: 12px;
      color: rgb(46, 45, 45);
      line-height: 14.4px;
      overflow: hidden;
      text-overflow: ellipsis;
      overflow-wrap: break-word;
      -webkit-line-clamp: 1;
      -webkit-box-orient: vertical;
      max-height: 14.4px;
      white-space: normal;
    }
  }
</style>
