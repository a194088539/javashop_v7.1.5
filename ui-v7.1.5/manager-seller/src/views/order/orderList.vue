<template>
  <div>
    <en-table-layout
      toolbar
      pagination
      :loading="loading"
      :tableData="tableData"
      :tips="true">
      <div slot="tips">
        <h4>操作提示</h4>
        <ul>
          <li>导出时，会按右侧高级搜索中的筛选条件导出全部数据。如果不需要，请先清空筛选条件！</li>
        </ul>
      </div>
      <div slot="toolbar" class="inner-toolbar">
        <div class="toolbar-btns">
          <el-button size="mini" type="primary" icon="el-icon-download" @click="handleExportOrder" style="margin-left: 5px">导出Excel</el-button>
          <el-button size="mini" type="primary" style="margin-left: 5px" @click="handleBatchDeliverGoods">批量发货</el-button>
        </div>
        <div class="toolbar-search">
          <en-table-search
            @search="searchEvent"
            @advancedSearch="advancedSearchEvent"
            advanced
            advancedWidth="465"
            placeholder="请输入订单编号或者商品名称">
            <template slot="advanced-content">
              <el-form ref="advancedForm" :model="advancedForm" label-width="80px">
                <el-form-item label="订单编号">
                  <el-input v-model="advancedForm.order_sn" clearable></el-input>
                </el-form-item>
                <el-form-item label="商品名称">
                  <el-input v-model="advancedForm.goods_name" clearable></el-input>
                </el-form-item>
                <el-form-item label="买家账号">
                  <el-input v-model="advancedForm.buyer_name" clearable></el-input>
                </el-form-item>
                <el-form-item label="下单日期">
                  <el-date-picker
                    v-model="advancedForm.order_time_range"
                    type="daterange"
                    align="center"
                    :editable="false"
                    unlink-panels
                    :default-time="['00:00:00', '23:59:59']"
                    range-separator="-"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    :picker-options="{ disabledDate (time) { return time.getTime() - 1 >= new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate()).getTime() + 86400000 - 1 }, shortcuts: MixinPickerShortcuts }">
                  </el-date-picker>
                </el-form-item>
                <el-form-item label="订单来源">
                  <el-select v-model="advancedForm.client_type" placeholder="请选择" clearable>
                    <el-option label="PC" value="PC"/>
                    <el-option label="WAP" value="WAP"/>
                    <el-option label="APP" value="APP"/>
                    <el-option label="小程序" value="MINI"/>
                  </el-select>
                </el-form-item>
                <el-form-item label="订单状态">
                  <el-select v-model="advancedForm.order_status" placeholder="请选择订单状态">
                    <el-option
                      v-for="item in orderStatusList"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-form>
            </template>
          </en-table-search>
        </div>
      </div>
    </en-table-layout>
    <div class="my-table-out" :style="{maxHeight: tableMaxHeight + 'px'}">
      <table class="my-table">
        <thead>
        <tr class="bg-order">
          <th class="shoplist-header">
            <el-checkbox v-model="checkAll">全选</el-checkbox>
            <span style="width: 100%;">商品名称</span>
            <span>单价/数量</span>
          </th>
          <th>买家</th>
          <!--<th>下单时间</th>-->
          <th>订单状态</th>
          <!--<th>来源</th>-->
          <th>实付金额</th>
        </tr>
        </thead>
        <tbody v-for="item in tableData">
        <tr style="width: 100%;height: 10px;"></tr>
        <tr class="bg-order">
          <td class="shoplist-content-out" colspan="3">
            <el-checkbox
              v-model="item.isSelected"
              :disabled="!item.order_operate_allowable_vo.allow_ship"
              @change="checkOne(item.isSelected, item)"/>
            订单编号：{{item.sn}}
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            下单时间: {{ item.create_time | unixToDate }}
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            来源: {{ item.client_type === 'MINI' ? '小程序' : item.client_type }}
            <svg-icon v-if="item.client_type === 'PC'" iconClass="pc" style="width: 15px;height: 15px;" />
            <svg-icon v-if="item.client_type === 'WAP' || item.client_type === 'APP' || item.client_type === 'MINI'" iconClass="mobile" style="width: 15px;height: 15px;"/>
          </td>
          <td>
            <el-button type="text" @click="handleOperateOrder(item)">查看详情</el-button>
          </td>
        </tr>
        <tr>
          <!--商品-->
          <td>
            <p v-for="shop in item.sku_list" class="shoplist-content">
              <span class="goods-info">
                <img :src="shop.goods_image" alt="" class="goods-image"/>
                <a :href="`${MixinBuyerDomain}/goods/${shop.goods_id}`" target="_blank" style="color: #00a2d4;">{{ shop.name }}</a>
              </span>
              <span>
                <a class="trade-record" :href="`${MixinBuyerDomain}/goods/snapshot?id=${shop.snapshot_id}&sku_id=${shop.sku_id}&orderData=${item.order_amount}&owner=SELLER&token=${tokenStorage}`" target="_blank">
                  <el-button type="text">交易快照</el-button>
                </a>
                <span>{{shop.original_price | unitPrice('￥')}}</span> × <span>{{ shop.num }}</span>
              </span>
            </p>
          </td>
          <!--买家-->
          <td> {{ item.member_name }}</td>
          <!--订单状态-->
          <td>
            {{ item.order_status_text }}
            <el-tooltip placement="top" v-if="item.order_operate_allowable_vo.allow_check_cancel && item.order_status != 'CANCELLED'">
              <div slot="content">当前订单已申请售后服务，请到"售后<br/>管理-售后服务列表"中查看相关信息</div>
              <i class="el-icon-info"></i>
            </el-tooltip>
          </td>
          <!--实付金额-->
          <td>
            <div class="order-money">
              <!--订单总金额-->
              <span class="order-amount">{{ item.order_amount | unitPrice('￥')}}</span>
              <!--运费/邮费-->
              <span>运费({{ item.shipping_amount | unitPrice('￥') }})</span>
              <!--支付方式-->
              <span>{{ item.payment_name }}</span>
            </div>
          </td>
        </tr>
        </tbody>
        <div v-if="tableData.length === 0 " class="empty-block">
          暂无数据
        </div>
      </table>
    </div>
    <el-pagination
      slot="pagination"
      v-if="pageData"
      @size-change="handlePageSizeChange"
      @current-change="handlePageCurrentChange"
      :current-page="pageData.page_no"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="pageData.page_size"
      layout="total, sizes, prev, pager, next, jumper"
      :total="pageData.data_total">
    </el-pagination>
    <el-dialog :visible.sync="logisticsShow" width="50%" align="center">
        <en-table-layout
          :tableData="logisticsData"
          :loading="loading">
          <div slot="toolbar" class="inner-toolbar">
            <span style="line-height: 35px;">物流信息</span>
          </div>
          <template slot="table-columns">
            <!--公司名称-->
            <el-table-column prop="logistics_company_do.name" label="公司名称"/>
            <!--是否支持电子面单-->
            <el-table-column label="是否支持电子面单">
              <template slot-scope="scope">
                <span v-if="scope.row.logistics_company_do.is_waybill === 0">不支持电子面单</span>
                <el-button
                  type="text"
                  v-if="scope.row.logistics_company_do.is_waybill === 1"
                  @click="produceElectronicSurface(scope.row)">生成电子面单</el-button>
              </template>
            </el-table-column>
          </template>
        </en-table-layout>
    </el-dialog>
    <!--电子面单-->
    <el-dialog title="电子面单" :visible.sync="electronicSurfaceShow" width="30%" align="center">
      <!--主体-->
      <div>
        <div  v-for="(item, key) in electronicSurface" :key="key">
          <div class="electronic-surface" :ref="`${key}item`" v-html="item" :id="`${key}item`"></div>
          <div class="el-dialog__footer">
            <el-button type="primary" @click="handlePrint(item, `${key}item`)">打印</el-button>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import * as API_order from '@/api/order'
  import * as API_logistics from '@/api/expressCompany'
  import { CategoryPicker } from '@/components'
  import { Foundation } from '~/ui-utils'
  import Storage from '@/utils/storage'
  import Print from 'print-js'

  export default {
    name: 'orderList',
    components: {
      [CategoryPicker.name]: CategoryPicker
    },
    data() {
      return {
        /** 列表loading状态 */
        loading: false,

        /** 列表参数 */
        params: {
          page_no: 1,
          page_size: 10,
          ...this.$route.query
        },

        /** 列表数据 */
        tableData: [],

        /** 列表分页数据 */
        pageData: [],

        /** 高级搜索数据 */
        advancedForm: {},
        tokenStorage: Storage.getItem('seller_access_token'),

        /** 订单状态 列表*/
        orderStatusList: [
          { value: 'ALL', label: '全部' },
          { value: 'WAIT_PAY', label: '待付款' },
          { value: 'WAIT_SHIP', label: '待发货' },
          { value: 'WAIT_ROG', label: '待收货' },
          { value: 'COMPLETE', label: '已完成' },
          { value: 'CANCELLED', label: '已取消' }
        ],
        isIndeterminate: true,
        /** 是否全选*/
        // checkAll: false,
        /** 物流信息弹框是否显示 */
        logisticsShow: false,
        /** 物流信息 */
        logisticsData: [],

        /** 是否显示电子面单 */
        electronicSurfaceShow: false,
        electronicSurface: '',

        /** 表格最大高度 */
        tableMaxHeight: (document.body.clientHeight - 54 - 34 - 50 - 15)
      }
    },
    computed: {
      /** 全选 */
      checkAll: {
        get() {
          if (!this.tableData.length) { return false }
          const filterTableData = this.tableData.filter(item => item.order_status_text === '待发货')
          if (!filterTableData.length) { return false }
          return filterTableData.every(item => item.isSelected)
        },
        set() {
          const filterArr = this.tableData.filter(item => item.order_status_text === '待发货')
          if (filterArr.every(item => item.isSelected)) {
            filterArr.forEach(item => { item.isSelected = false })
            return false
          } else {
            filterArr.forEach(item => { item.isSelected = true })
            return true
          }
        }
      }
    },
    mounted() {
      this.GET_OrderList()
    },
    activated() {
      delete this.params.market_enable
      this.params = {
        ...this.params,
        ...this.$route.query
      }
      this.GET_OrderList()
      window.onresize = this.countTableHeight
    },
    beforeRouteUpdate(to, from, next) {
      delete this.params.market_enable
      this.params = {
        ...this.params,
        ...this.$route.query
      }
      this.GET_OrderList()
      next()
    },
    methods: {
      /** 计算高度 */
      countTableHeight() {
        this.tableHeight = (document.body.clientHeight - 54 - 35 - 50)
      },

      /** 分页大小发生改变 */
      handlePageSizeChange(size) {
        this.params.page_size = size
        this.GET_OrderList()
      },

      /** 分页页数发生改变 */
      handlePageCurrentChange(page) {
        this.params.page_no = page
        this.GET_OrderList()
      },

      /** 订单状态改变 */
      changeOrderStatus(data) {
        delete this.params.keywords
        delete this.params.order_status
        if (data) {
          this.params = {
            ...this.params,
            order_status: data
          }
          this.params.page_no = 1
          this.params.page_size = 10
        }
        Object.keys(this.advancedForm).forEach(key => delete this.params[key])
        this.GET_OrderList()
      },

      /** 搜索事件触发 */
      searchEvent(data) {
        this.params = {
          ...this.params,
          page_no: 1,
          keywords: data
        }
        delete this.params.order_status
        Object.keys(this.advancedForm).forEach(key => delete this.params[key])
        this.GET_OrderList()
      },

      /** 高级搜索事件触发 */
      advancedSearchEvent() {
        this.params = {
          ...this.params,
          page_no: 1,
          ...this.advancedForm
        }
        delete this.params.start_time
        delete this.params.end_time
        if (this.advancedForm.order_time_range) {
          this.params.start_time = this.advancedForm.order_time_range[0].getTime() / 1000
          this.params.end_time = this.advancedForm.order_time_range[1].getTime() / 1000
        }
        delete this.params.keywords
        delete this.params.order_time_range
        this.GET_OrderList()
      },

      /** 查看、操作订单 */
      handleOperateOrder(item) {
        this.$router.push({ path: `/order/detail/${item.sn}` })
      },
      checkOne(value, currentItem) {
        currentItem.isSelected = value
      },

      /** 批量发货 */
      handleBatchDeliverGoods() {
        if (this.tableData.filter(item => item.isSelected).length > 0) {
          this.logisticsShow = true
          this.getLogisticsCompanies()
        } else {
          this.$message.error('请选择发货订单！')
        }
      },

      /** 获取物流公司信息列表 */
      getLogisticsCompanies() {
        API_logistics.getExpressCompanyList({}).then(response => {
          this.logisticsData = response
          this.logisticsData = this.logisticsData.filter(key => {
            return key.open
          })
          this.logisticsData.forEach(key => {
            this.$set(key, 'ship_no', '')
          })
        })
      },

      /** 生成电子面单 */
      produceElectronicSurface(row) {
        const _params = {
          order_sns: this.tableData.map(item => {
            if (item.isSelected) {
              return item.sn
            }
          }),
          logistics_id: row.logistics_company_do.id
        }
        this.$confirm('确认生成电子面单?', '提示', { type: 'warning' }).then(() => {
          API_order.generateWaybill(_params).then((response) => {
            this.electronicSurfaceShow = true
            this.logisticsData.forEach(key => {
              if (row.id === key.id) {
                key.ship_no = response.code
              }
            })
            setTimeout(() => {
              this.electronicSurface = response.split('<!DOCTYPE html>').slice(1).map(item => `<!DOCTYPE html>${item}`)
            }, 200)
            this.GET_OrderList()
          })
        })
      },

      /** 打印电子面单 */
      handlePrint(item, ref) {
        Print({
          // printable: 'item',
          printable: ref,
          type: 'html',
          // 继承原来的所有样式
          targetStyles: ['*']
        })
      },

      /** 导出订单列表 */
      handleExportOrder() {
        API_order.exportOrders(this.params).then(response => {
          const json = {
            sheet_name: '订单列表',
            sheet_values: response.map(item => ({
              '订单编号': item.sn,
              '下单时间': Foundation.unixToDate(item.create_time),
              '订单来源': item.client_type,
              '商品信息': item.sku_list.map(sku_item => {
                return `「商品名称：${sku_item.name}, 商品价格：${sku_item.purchase_price}, 商品数量：${sku_item.num}」`
              }).join(' \r\n'),
              '买家': item.member_name,
              '订单状态': item.order_status_text,
              '实付金额': item.order_amount
            }))
          }
          this.MixinExportJosnToExcel(json, '订单列表')
        })
      },

      GET_OrderList() {
        this.loading = true
        API_order.getOrderList(this.params).then(response => {
          this.loading = false
          this.tableData = response.data.map(item => {
            item.isSelected = false
            return item
          })
          this.pageData = {
            page_no: response.page_no,
            page_size: response.page_size,
            data_total: response.data_total
          }
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  /deep/ div.toolbar {
    height: 70px;
    padding: 20px 0;
  }
  /* 工具条*/
  .inner-toolbar {
    display: flex;
    width: 100%;
    justify-content: space-between;
    padding: 0 20px;
  }

  /*暂无数据时的样式*/
  /deep/ .el-table__empty-block {
    display: none;
  }

  .empty-block {
    position: relative;
    min-height: 60px;
    line-height: 60px;
    text-align: center;
    width: 190%;
    height: 100%;
    font-size: 14px;
    color: #606266;
  }

  /*表格信息*/
  .my-table-out{
    white-space: nowrap;
    overflow-y: scroll;
    text-overflow: ellipsis;
    width: 100%;
    max-height: 800px;
  }
  .my-table {
    width: 100%;
    margin-bottom: 40px;
    background: #fff;
    border-collapse: collapse;
    font-family: Helvetica Neue, Helvetica, PingFang SC, Hiragino Sans GB, Microsoft YaHei, Arial, sans-serif;
    font-size: 14px;
    font-bold: 700;
    .bg-order {
      background: #FAFAFA;
    }
    thead {
      th {
        padding: 10px 0;
        border: 1px solid #ebeef5;
        border-collapse: collapse;
        color: #909399;
      }
      th.shoplist-header {
        padding: 10px 20px;
        display: flex;
        flex-direction: row;
        flex-wrap: nowrap;
        justify-content: space-between;
        align-items: center;
      }
    }
    tbody {
      margin-top: 10px;
      td {
        border: 1px solid #ebeef5;
        border-collapse: collapse;
        text-align: center;
        padding: 0;
      }
      td:first-child {
        text-align: left;
      }
      td:not(:first-child) {
        padding: 3px;
      }
      td.shoplist-content-out {
        padding-left: 20px;
      }

      /*商品信息*/
      p.shoplist-content:not(:last-child) {
        border-bottom: 1px solid #ebeef5;
        border-collapse: collapse;
      }
      p.shoplist-content {
        margin: 0;
        padding: 0 20px;
        box-sizing: padding-box;
        display: flex;
        flex-direction: row;
        flex-wrap: nowrap;
        justify-content: space-between;
        align-items: center;
        .goods-info {
          display: flex;
          flex-direction: row;
          flex-wrap: nowrap;
          justify-content: flex-start;
          align-items: center;
          padding: 10px 0;
          img {
            display: block;
            margin-right: 10px;
          }
          a {
            display: block;
            color: #409EFF;
          }
        }
      }
      div.order-money {
        display: flex;
        flex-direction: column;
        flex-wrap: nowrap;
        justify-content: center;
        align-items: center;
        span {
          display: inline-block;
          padding: 5px;
        }
        span.order-amount {
          color: red;
        }
      }
    }
    /* 商品图片 */
    .goods-image {
      width: 50px;
      height: 50px;
    }
    /** 交易快照 */
    .trade-record {
      display: inline-block;
      margin-right: 50px;
    }
  }

  /*分页信息*/
  section>div {
    position: relative;
  }
  .el-pagination {
    text-align: right;
    width: 100%;
    background: #ffffff;
    height: 40px;
    position: absolute;
    bottom: 0;
    right: 0;
    border-top: 1px solid #e5e5e5;
    padding: 5px 20px;
  }
</style>

