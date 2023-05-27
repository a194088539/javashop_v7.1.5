<template>
  <div id="refund-record">
    <div class="member-nav">
      <ul class="member-nav-list">
        <li>
          <nuxt-link to="./after-sale">申请售后服务</nuxt-link>
        </li>
        <li>
          <nuxt-link to="./service-record">售后服务记录</nuxt-link>
        </li>
        <li class="active">
          <nuxt-link to="./refund-record">退款明细</nuxt-link>
        </li>
      </ul>
    </div>
    <empty-member v-if="refundList && !refundList.data.length">暂无售后退款记录</empty-member>
    <template v-else>
      <div class="mod-main mod-comm lefta-box mod-main-fxthh">
        <div class="mc">
          <table class="tb-void tb-top">
            <colgroup>
              <col width="130">
              <col width="130">
              <col width="130">
              <col width="100">
              <col width="100">
              <col width="100">
              <col width="100">
              <col width="80">
            </colgroup>
            <thead>
            <tr>
              <th>售后单号</th>
              <th>订单编号</th>
              <th>创建时间</th>
              <th>申请退款金额</th>
              <th>同意退款金额</th>
              <th>实际退款金额</th>
              <th>状态</th>
              <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="refund in refundList.data" :key="refund.service_sn">
              <td align="center">
                <nuxt-link :to="'./service-detail?service_sn=' + refund.service_sn" target="_blank">{{ refund.service_sn }}</nuxt-link>
              </td>
              <td>
                <nuxt-link :to="'./my-order/detail?order_sn=' + refund.order_sn" target="_blank">{{ refund.order_sn }}</nuxt-link>
              </td>
              <td>{{ refund.create_time | unixToDate }}</td>
              <td>￥{{ (refund.refund_price || 0) | unitPrice }}</td>
              <td>￥{{ (refund.agree_price || 0) | unitPrice }}</td>
              <td>￥{{ (refund.actual_price || 0) | unitPrice }}</td>
              <td>
                {{ refund.refund_status_text }}
              </td>
              <td>
                <nuxt-link :to="'./service-detail?service_sn=' + refund.service_sn" target="_blank">查看</nuxt-link>
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div class="member-pagination" v-if="refundList">
        <el-pagination
          @current-change="handleCurrentPageChange"
          :current-page.sync="params.page_no"
          :page-size="params.page_size"
          layout="total, prev, pager, next"
          :total="refundList.data_total">
        </el-pagination>
      </div>
    </template>
  </div>
</template>

<script>
  import Vue from 'vue'
  import * as API_AfterSale from '@/api/after-sale'
  import { Tooltip } from 'element-ui'
  Vue.use(Tooltip)
  export default {
    name: 'refund-record',
    head() {
      return {
        title: `退款单记录-${this.site.title}`
      }
    },
    data() {
      return {
        refundList: '',
        params: {
          page_no: 1,
          page_size: 10
        }
      }
    },
    mounted() {
      this.GET_RefundRecords()
    },
    methods: {
      /** 当前页数发生改变 */
      handleCurrentPageChange(page) {
        this.params.page_no = page;
        this.GET_RefundRecords()
      },
      /** 获取我的售后服务记录数据 */
      GET_RefundRecords() {
        API_AfterSale.getRefundList(this.params).then(response => {
          this.refundList = response;
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
    color: #333;
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
  .mod-main-fxthh .tb-void a {
    color: #005ea7;
    &:hover {
      color: #e4393c;
    }
  }
</style>
