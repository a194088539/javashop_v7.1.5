<template>
  <div>
    <en-table-layout
      toolbar
      pagination
      :tableData="tableData"
      :loading="loading">
      <div slot="toolbar" class="inner-toolbar">
        <div class="toolbar-btns">
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
                <el-form-item label="退款单状态">
                  <el-select v-model="advancedForm.refund_status" placeholder="请选择" clearable>
                    <el-option v-for="status in refundStatusList" :key="status.value" :label="status.label" :value="status.value"/>
                  </el-select>
                </el-form-item>
                <el-form-item label="创建时间">
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
        <el-table-column label="创建时间">
          <template slot-scope="scope">{{ scope.row.create_time | unixToDate }}</template>
        </el-table-column>
        <!--售后服务单号-->
        <el-table-column prop="service_sn" label="售后服务单号"/>
        <!--订单号-->
        <el-table-column prop="order_sn" label="订单编号"/>
        <!--申请退款金额-->
        <el-table-column prop="refund_price" label="申请退款金额"/>
        <!--商家同意的退款金额-->
        <el-table-column prop="agree_price" label="同意退款金额"/>
        <!--实际退款金额-->
        <el-table-column prop="actual_price" label="实际退款金额"/>
        <!--状态-->
        <el-table-column prop="refund_status_text" label="状态"/>
        <!--操作-->
        <el-table-column label="操作" width="150">
          <template slot-scope="scope">
            <el-button type="primary" @click="handleServiceDetail(scope.row)">详情</el-button>
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

  export default {
    name: 'refundList',
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
          refund_status: '',
          apply_time_range: []
        },
        /** 售后服务单状态列表 */
        refundStatusList: [
          { label: '新创建', value: 'APPLY' },
          { label: '退款中', value: 'REFUNDING' },
          { label: '退款失败', value: 'REFUNDFAIL' },
          { label: '已完成', value: 'COMPLETED' }
        ]
      }
    },
    mounted() {
      this.GET_AfterSaleRefund()
    },
    methods: {
      /** 分页大小发生改变 */
      handlePageSizeChange(size) {
        this.params.page_size = size
        this.GET_AfterSaleRefund()
      },

      /** 分页页数发生改变 */
      handlePageCurrentChange(page) {
        this.params.page_no = page
        this.GET_AfterSaleRefund()
      },

      /** 搜索事件触发 */
      searchEvent(data) {
        Object.keys(this.advancedForm).forEach(key => delete this.params[key])
        this.params = {
          ...this.params,
          page_no: 1,
          keyword: data
        }
        this.GET_AfterSaleRefund()
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
        this.GET_AfterSaleRefund()
      },

      /** 跳转至售后服务详情页面 */
      handleServiceDetail(row) {
        this.$router.push({ path: `/order/service-detail/${row.service_sn}` })
      },

      /** 获取售后服务列表数据 */
      GET_AfterSaleRefund() {
        this.loading = true
        API_AfterSale.getRefundList(this.params).then(response => {
          this.loading = false
          this.tableData = response.data
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
