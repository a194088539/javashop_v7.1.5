<template>
  <div>
    <en-table-layout
      :tableData="tableData.data"
      :loading="loading"
    >

      <div slot="toolbar" class="inner-toolbar">
        <div class="toolbar-btns"></div>
        <div class="toolbar-search">
          <en-table-search
            @search="searchEvent"
            @advancedSearch="advancedSearchEvent"
            advanced
            advancedWidth="465"
            placeholder="请输入活动名称"
          >
            <template slot="advanced-content">
              <el-form ref="advancedForm" :model="advancedForm" label-width="80px">
                <el-form-item label="活动名称">
                  <el-input size="medium" v-model="advancedForm.name" clearable></el-input>
                </el-form-item>
                <el-form-item label="店铺名称">
                  <en-shop-picker @changed="(shop) => { advancedForm.seller_id = shop.shop_id }"/>
                </el-form-item>
                <el-form-item label="活动状态">
                  <el-select v-model="advancedForm.status" placeholder="请选择">
                    <el-option label="全部" value=""/>
                    <el-option label="待开始" value="WAIT"/>
                    <el-option label="进行中" value="UNDERWAY"/>
                    <el-option label="已结束" value="END"/>
                  </el-select>
                </el-form-item>
                <el-form-item label="活动时间">
                  <el-date-picker
                    v-model="advancedForm.act_time_range"
                    type="daterange"
                    align="center"
                    :editable="false"
                    unlink-panels
                    :default-time="['00:00:00', '23:59:59']"
                    range-separator="-"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    value-format="timestamp"
                    :picker-options="{ disabledDate (time) { return time.getTime() - 1 >= new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate()).getTime() + 86400000 - 1 }, shortcuts: MixinPickerShortcuts }">
                  </el-date-picker>
                </el-form-item>
              </el-form>
            </template>
          </en-table-search>
        </div>
      </div>

      <template slot="table-columns">
        <el-table-column prop="promotion_name" label="活动名称"/>
        <el-table-column prop="seller_name" label="店铺名称"/>
        <el-table-column prop="start_time" width="150" :formatter="MixinUnixToDate" label="开始时间" />
        <el-table-column prop="end_time" width="150" :formatter="MixinUnixToDate" label="结束时间"/>
        <el-table-column prop="status" :formatter="AssembleStatusText" width="120" label="活动状态"/>
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="primary"
              @click="handleViewAssembleDetail(scope.row)"
            >查看</el-button>
            <assembleOpenOrClose
              v-if="scope.row.option_status !== 'NOTHING'"
              :row="scope.row"
              size="mini"
              type="danger"
              @clickEmit="handleCloseAssemble"
            >{{ scope.row.option_status | operaStatus }}</assembleOpenOrClose>
          </template>
        </el-table-column>
      </template>
      <el-pagination
        v-if="tableData"
        slot="pagination"
        @size-change="handlePageSizeChange"
        @current-change="handlePageCurrentChange"
        :current-page="tableData.page_no"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="tableData.page_size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="tableData.data_total">
      </el-pagination>
    </en-table-layout>
  </div>
</template>

<script>
  import * as API_Promotion from '@/api/promotion'
  import { Foundation } from '~/ui-utils'
  import assembleOpenOrClose from './assembleOpenOrClose.vue'

  export default {
    name: 'assembleList',
    data() {
      return {
        // 列表loading状态
        loading: false,
        /** 列表参数 */
        params: {
          page_no: 1,
          page_size: 10
        },
        /** 列表数据 */
        tableData: {
          data: []
        },

        /** 开始/关闭文本提示 */
        tip: '关闭',

        /** 高级搜索数据 */
        advancedForm: {}
      }
    },
    mounted() {
      this.GET_AssembleList()
    },
    components: {
      assembleOpenOrClose
    },
    filters: {
      operaStatus(val) {
        switch (val) {
          case 'CAN_OPEN': return '开启'
          case 'CAN_CLOSE': return '关闭'
        }
      }
    },
    methods: {

      /** 状态文本显示 */
      AssembleStatusText({ status }) {
        switch (status) {
          case 'WAIT': return '待开始'
          case 'UNDERWAY': return '进行中'
          case 'END': return '已结束'
        }
      },

      /** 分页大小发生改变 */
      handlePageSizeChange(size) {
        this.params.page_size = size
        this.GET_AssembleList()
      },

      /** 分页页数发生改变 */
      handlePageCurrentChange(page) {
        this.params.page_no = page
        this.GET_AssembleList()
      },

      /** 搜索事件触发 */
      searchEvent(data) {
        this.params.page_no = 1
        this.params.name = data
        if (!data) delete this.params.name
        this.GET_AssembleList()
      },

      /** 高级搜索事件触发 */
      advancedSearchEvent() {
        this.params = {
          ...this.params,
          ...this.advancedForm
        }
        delete this.params.start_time
        delete this.params.end_time
        const { act_time_range } = this.advancedForm
        if (act_time_range) {
          this.params.start_time = parseInt(act_time_range[0] /= 1000)
          this.params.end_time = parseInt(act_time_range[1] /= 1000)
        }
        delete this.params.act_time_range
        this.params.page_no = 1
        this.GET_AssembleList()
      },

      /** 获取拼团活动列表 */
      GET_AssembleList() {
        this.loading = true
        API_Promotion.getAssembleList(this.params).then(response => {
          this.loading = false
          this.tableData = { ...response }
        })
      },

      /** 关闭/开启某个拼团活动 */
      handleCloseAssemble(row) {
        const tip = row.option_status === 'CAN_OPEN' ? '开启' : '关闭'
        this.$confirm(`确定要${tip}此拼团活动？`, '提示', { type: 'warning' }).then(() => {
          Promise.all([
            row.option_status === 'CAN_OPEN'
              ? API_Promotion.openAssemble(row.promotion_id)
              : API_Promotion.deleteAssemble(row.promotion_id)
          ]).then(response => {
            this.$message.success(`${tip}成功`)
            this.GET_AssembleList()
          }).catch(_ => {})
        }).catch(() => {})
      },

      /** 查看某个拼团活动详情 */
      handleViewAssembleDetail(row) {
        this.$router.push({ name: 'assembleDetail', params: { promotion_id: row.promotion_id }})
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .time-tag {
    display: block;
    width: 80px;
    margin: 5px 0;
  }
  .input-new-tag {
    width: 112px;
  }

  /deep/ input::-webkit-outer-spin-button,
  /deep/ input::-webkit-inner-spin-button {
    -webkit-appearance: none !important;
    margin: 0;
  }
</style>
