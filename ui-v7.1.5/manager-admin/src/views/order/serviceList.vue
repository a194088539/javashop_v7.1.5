<template>
  <div>
    <en-table-layout
      toolbar
      pagination
      :tableData="tableData"
      :loading="loading"
      :tips="true">
      <div slot="tips">
        <h4>操作提示</h4>
        <ul>
          <li>导出时，会按右侧高级搜索中的筛选条件导出全部数据。如果不需要，请先清空筛选条件！</li>
        </ul>
      </div>
      <div slot="toolbar" class="inner-toolbar">
        <div class="toolbar-btns">
          <el-button size="mini" type="primary" icon="el-icon-download" @click="handleExportService" style="margin-left: 5px">导出Excel</el-button>
        </div>
        <div class="toolbar-search">
          <en-table-search
            @search="searchEvent"
            @advancedSearch="advancedSearchEvent"
            advanced
            advancedWidth="465"
            placeholder="请输入售后服务单号、订单编号">
            <template slot="advanced-content">
              <el-form ref="advancedForm" :model="advancedForm" label-width="100px">
                <el-form-item label="售后服务单号">
                  <el-input v-model="advancedForm.service_sn" clearable></el-input>
                </el-form-item>
                <el-form-item label="订单编号">
                  <el-input v-model="advancedForm.order_sn" clearable></el-input>
                </el-form-item>
                <el-form-item label="售后服务类型">
                  <el-select v-model="advancedForm.service_type" placeholder="请选择" clearable>
                    <el-option label="退货" value="RETURN_GOODS"/>
                    <el-option label="换货" value="CHANGE_GOODS"/>
                    <el-option label="补发商品" value="SUPPLY_AGAIN_GOODS"/>
                    <el-option label="取消订单" value="ORDER_CANCEL"/>
                  </el-select>
                </el-form-item>
                <el-form-item label="售后状态">
                  <el-select v-model="advancedForm.service_status" placeholder="请选择" clearable>
                    <el-option v-for="status in serviceStatusList" :key="status.value" :label="status.label" :value="status.value"/>
                  </el-select>
                </el-form-item>
                <el-form-item label="申请时间">
                  <el-date-picker
                    v-model="advancedForm.apply_time_range"
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
              </el-form>
            </template>
          </en-table-search>
        </div>
      </div>
      <template slot="table-columns">
        <!--创建时间-->
        <el-table-column label="申请时间">
          <template slot-scope="scope">{{ scope.row.create_time | unixToDate }}</template>
        </el-table-column>
        <!--售后服务单号-->
        <el-table-column prop="service_sn" label="售后服务单号"/>
        <!--订单号-->
        <el-table-column prop="order_sn" label="订单编号"/>
        <!--售后服务类型-->
        <el-table-column prop="service_type_text" label="售后服务类型"/>
        <!--状态-->
        <el-table-column prop="service_status_text" label="状态"/>
        <!--操作-->
        <el-table-column label="操作" width="150">
          <template slot-scope="scope">
            <el-button type="primary" @click="handleServiceDetail(scope.row)" v-if="scope.row.allowable.allow_admin_refund">退款</el-button>
            <el-button type="primary" @click="handleServiceDetail(scope.row)" v-else>详情</el-button>
          </template>
        </el-table-column>
      </template>
      <!--分页-->
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
    </en-table-layout>
  </div>
</template>

<script>
  import * as API_AfterSale from '@/api/afterSale'
  import { Foundation } from '~/ui-utils'

  export default {
    name: 'serviceList',
    data() {
      return {
        /** 列表loading状态 */
        loading: false,
        /** 列表参数 */
        params: {
          page_no: 1,
          page_size: 10
        },
        /** 列表数据 */
        tableData: null,
        /** 列表分页数据 */
        pageData: null,
        /** 高级搜索数据 */
        advancedForm: {
          service_sn: '',
          order_sn: '',
          service_type: '',
          service_status: '',
          apply_time_range: []
        },
        /** 售后服务单状态列表 */
        serviceStatusList: [
          { label: '待审核', value: 'APPLY' },
          { label: '审核通过', value: 'PASS' },
          { label: '审核未通过', value: 'REFUSE' },
          { label: '已退还商品', value: 'FULL_COURIER' },
          { label: '待人工处理', value: 'WAIT_FOR_MANUAL' },
          { label: '退款中', value: 'REFUNDING' },
          { label: '退款失败', value: 'REFUNDFAIL' },
          { label: '已完成', value: 'COMPLETED' },
          { label: '异常状态', value: 'ERROR_EXCEPTION' },
          { label: '已关闭', value: 'CLOSED' }
        ]
      }
    },
    mounted() {
      this.GET_AfterSaleService()
    },
    methods: {
      /** 分页大小发生改变 */
      handlePageSizeChange(size) {
        this.params.page_size = size
        this.GET_AfterSaleService()
      },

      /** 分页页数发生改变 */
      handlePageCurrentChange(page) {
        this.params.page_no = page
        this.GET_AfterSaleService()
      },

      /** 搜索事件触发 */
      searchEvent(data) {
        Object.keys(this.advancedForm).forEach(key => delete this.params[key])
        this.params = {
          ...this.params,
          page_no: 1,
          keyword: data
        }
        this.GET_AfterSaleService()
      },

      /** 高级搜索事件触发 */
      advancedSearchEvent() {
        this.params.keyword = ''
        this.params = {
          ...this.params,
          ...this.advancedForm,
          page_no: 1
        }
        delete this.params.start_time
        delete this.params.end_time
        if (this.advancedForm.apply_time_range && this.advancedForm.apply_time_range.length > 0) {
          this.params.start_time = this.advancedForm.apply_time_range[0].getTime() / 1000
          this.params.end_time = this.advancedForm.apply_time_range[1].getTime() / 1000
        }
        delete this.params.apply_time_range
        this.GET_AfterSaleService()
      },

      /** 展示审核弹出框 */
      handleServiceDetail(row) {
        this.$router.push({ path: `/order/service-detail/${row.service_sn}` })
      },

      /** 获取售后服务列表数据 */
      GET_AfterSaleService() {
        this.loading = true
        API_AfterSale.getAfterSaleList(this.params).then(response => {
          this.loading = false
          this.tableData = response.data
          this.pageData = {
            page_no: response.page_no,
            page_size: response.page_size,
            data_total: response.data_total
          }
        })
      },

      /** 按条件导出售后服务数据 */
      handleExportService() {
        API_AfterSale.exportAfterSale(this.params).then(response => {
          const json = {
            sheet_name: '售后服务列表',
            sheet_values: response.map(item => ({
              '售后服务单号': item.service_sn,
              '订单编号': item.order_sn,
              '申请时间': Foundation.unixToDate(item.create_time),
              '申请会员': item.member_name,
              '所属店铺': item.seller_name,
              '会员联系方式': item.mobile,
              '售后服务类型': item.service_type_text,
              '售后服务状态': item.service_status_text,
              '申请原因': item.reason,
              '问题描述': item.problem_desc,
              '申请凭证': item.apply_vouchers,
              '商家审核备注': item.audit_remark,
              '商家入库备注': item.stock_remark,
              '平台退款备注': item.refund_remark,
              '售后商品信息': item.goods_info,
              '用户退还商品物流信息': item.express_info,
              '收货地址信息': item.rog_info,
              '申请退款金额': Foundation.formatPrice(item.refund_price),
              '商家同意退款金额': Foundation.formatPrice(item.agree_price),
              '实际退款金额': Foundation.formatPrice(item.actual_price),
              '退款时间': Foundation.unixToDate(item.refund_time),
              '退款方式': item.refund_way_text,
              '退款账号类型': item.account_type_text,
              '退款账号': item.return_account,
              '银行名称': item.bank_name,
              '银行账户': item.bank_account_number,
              '银行开户名': item.bank_account_name,
              '银行开户行': item.bank_deposit_name
            }))
          }
          this.MixinExportJosnToExcel(json, '售后服务列表')
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

  /deep/ .el-table td:not(.is-left) {
    text-align: center;
  }

  .inner-toolbar {
    display: flex;
    width: 100%;
    justify-content: space-between;
    padding: 0 20px;
  }
</style>
