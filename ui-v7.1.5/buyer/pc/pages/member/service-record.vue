<template>
  <div id="service-record">
    <div class="member-nav">
      <ul class="member-nav-list">
        <li>
          <nuxt-link to="./after-sale">申请售后服务</nuxt-link>
        </li>
        <li class="active">
          <nuxt-link to="./service-record">售后服务记录</nuxt-link>
        </li>
        <li>
          <nuxt-link to="./refund-record">退款明细</nuxt-link>
        </li>
      </ul>
    </div>
    <empty-member v-if="afterSaleList && !afterSaleList.data.length">暂无售后服务记录</empty-member>
    <template v-else>
      <div class="mod-main mod-comm lefta-box mod-main-fxthh">
        <div class="mc">
          <table class="tb-void tb-top">
            <colgroup>
              <col width="130">
              <col width="130">
              <col width="130">
              <col width="120">
              <col width="120">
              <col width="110">
            </colgroup>
            <thead>
              <tr>
                <th>售后单号</th>
                <th>订单编号</th>
                <th>申请时间</th>
                <th>服务类型</th>
                <th>状态</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="service in afterSaleList.data" :key="service.service_sn">
                <td align="center">
                  <nuxt-link :to="'./service-detail?service_sn=' + service.service_sn" target="_blank">{{ service.service_sn }}</nuxt-link>
                </td>
                <td>
                  <nuxt-link :to="'./my-order/detail?order_sn=' + service.order_sn" target="_blank">{{ service.order_sn }}</nuxt-link>
                </td>
                <td>{{ service.create_time | unixToDate }}</td>
                <td>{{ service.service_type_text }}</td>
                <td>
                  {{ service.service_status_text }}
                  <el-tooltip placement="right" v-if="service.allowable.allow_ship">
                    <div slot="content">请您及时将商品寄还给卖家，退货地址<br/>可以进入售后详情中查看，寄出后<br/>请进入售后详情页面填写物流信息</div>
                    <i class="el-icon-info"></i>
                  </el-tooltip>
                  <el-tooltip placement="right" v-if="service.service_status === 'ERROR_EXCEPTION'">
                    <div slot="content">系统生成新订单异常，等待商家手动创建新订单</div>
                    <i class="el-icon-info"></i>
                  </el-tooltip>
                </td>
                <td>
                  <nuxt-link :to="'./service-detail?service_sn=' + service.service_sn" target="_blank">查看</nuxt-link>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div class="member-pagination" v-if="afterSaleList">
        <el-pagination
          @current-change="handleCurrentPageChange"
          :current-page.sync="params.page_no"
          :page-size="params.page_size"
          layout="total, prev, pager, next"
          :total="afterSaleList.data_total">
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
    name: 'service-record',
    head() {
      return {
        title: `售后服务记录-${this.site.title}`
      }
    },
    data() {
      return {
        afterSaleList: '',
        params: {
          page_no: 1,
          page_size: 10
        }
      }
    },
    mounted() {
      this.GET_AfterSaleRecords()
    },
    methods: {
      /** 当前页数发生改变 */
      handleCurrentPageChange(page) {
        this.params.page_no = page;
        this.GET_AfterSaleRecords()
      },
      /** 获取我的售后服务记录数据 */
      GET_AfterSaleRecords() {
        API_AfterSale.getAfterSaleList(this.params).then(response => {
          this.afterSaleList = response;
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
