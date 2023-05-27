<template>
  <div id="after-sale">
    <div class="member-nav">
      <ul class="member-nav-list">
        <li class="active">
          <nuxt-link to="./after-sale">申请售后服务</nuxt-link>
        </li>
        <li>
          <nuxt-link to="./service-record">售后服务记录</nuxt-link>
        </li>
        <li>
          <nuxt-link to="./refund-record">退款明细</nuxt-link>
        </li>
      </ul>
    </div>
    <empty-member v-if="orderList && !orderList.data.length">暂无可申请售后的订单</empty-member>
    <template v-else>
      <div class="mod-main mod-comm">
        <div class="mc">
          <table class="tb-void tb-top">
            <colgroup>
              <col width="130">
              <col width="">
              <col width="260">
            </colgroup>
            <thead>
              <tr>
                <th>订单编号</th>
                <th>订单商品</th>
                <th>下单时间</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="order in orderList.data" :key="order.sn">
                <td>
                  <nuxt-link :to="'./my-order/detail?order_sn=' + order.sn" target="_blank" class="order-sn">{{ order.sn }}</nuxt-link>
                </td>
                <td>
                  <div class="list-h">
                    <ul>
                      <li style="width:auto;" v-for="sku in order.sku_list" :key="sku.sku_id">
                        <div class="p-img">
                          <a :href="'/goods/' + sku.goods_id" target="_blank">
                            <img width="50" height="50" :src="sku.goods_image" :title="sku.name">
                          </a>
                        </div>
                        <div class="btns" v-if="sku.goods_operate_allowable_vo.allow_apply_service">
                          <nuxt-link :to="'./apply-service?order_sn=' + order.sn + '&sku_id=' + sku.sku_id" class="btn btn-5">申请</nuxt-link>
                        </div>
                      </li>
                    </ul>
                  </div>
                </td>
                <td>
                  <div class="ftx03">{{ order.create_time | unixToDate }}</div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div class="member-pagination" v-if="orderList">
        <el-pagination
          @current-change="handleCurrentPageChange"
          :current-page.sync="params.page_no"
          :page-size="params.page_size"
          layout="total, prev, pager, next"
          :total="orderList.data_total">
        </el-pagination>
      </div>
    </template>
  </div>
</template>

<script>
  import * as API_Order from '@/api/order'
  export default {
    name: 'after-sale',
    head() {
      return {
        title: `申请售后服务-${this.site.title}`
      }
    },
    data() {
      return {
        orderList: '',
        params: {
          page_no: 1,
          page_size: 10
        }
      }
    },
    mounted() {
      this.GET_Orders()
    },
    methods: {
      /** 当前页数发生改变 */
      handleCurrentPageChange(page) {
        this.params.page_no = page;
        this.GET_Orders()
      },
      /** 获取我的订单数据 */
      GET_Orders() {
        API_Order.getOrderList(this.params).then(response => {
          this.orderList = response;
          this.MixinScrollToTop()
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .p-detail, .p-img, .p-market, .p-name, .p-price {
    overflow: hidden;
  }
  .ftx-03, .ftx03 {
    color: #999;
  }
  table {
    border-collapse: collapse;
  }
  .mod-main {
    padding: 10px;
    background-color: #fff;
    margin-bottom: 20px;
  }
  .mod-comm {
    padding: 10px 20px 20px;
  }
  .mc {
    zoom: 1;
    overflow: visible;
    line-height: 20px;
  }
  .mod-main .order-sn {
    &:hover {
      color: #e4393c;
    }
  }
  .tb-void {
    line-height: 18px;
    text-align: center;
    border: 1px solid #f2f2f2;
    border-top: 0;
    color: #333;
    width: 100%;
  }
  .tb-void th {
    background: #f5f5f5;
    height: 32px;
    line-height: 32px;
    padding: 0 5px;
    text-align: center;
    font-weight: 400;
  }
  .mod-main .tb-void td {
    border: 1px solid #f2f2f2;
    padding: 10px;
  }
  .mod-main .tb-void a {
    color: #005ea7;
  }
  .mod-main .list-h {
    overflow: hidden;
    zoom: 1;
  }
  .mod-main .list-h li {
    float: left;
    padding: 0 10px;
    width: 60px;
  }
  .mod-main .btns a {
    display: block;
    width: 50px;
    height: 25px;
    line-height: 25px;
    border: 1px solid #bfd6af;
    text-align: center;
    background: #f5fbef;
    color: #666;
    cursor: pointer;
    margin-bottom: 5px;
    &:first-child {
      margin-top: 5px;
    };
  }

</style>
