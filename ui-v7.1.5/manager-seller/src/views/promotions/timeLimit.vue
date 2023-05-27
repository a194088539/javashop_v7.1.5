<template>
  <div>
    <en-table-layout
      toolbar
      pagination
      :tableData="tableData"
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
                  <el-input size="medium" v-model="advancedForm.seckill_name" clearable></el-input>
                </el-form-item>
                <el-form-item label="活动状态">
                  <el-select v-model="advancedForm.status" placeholder="请选择">
                    <el-option label="全部" value="ALL"/>
                    <el-option label="已发布" value="RELEASE"/>
                    <el-option label="已开启" value="OPEN"/>
                    <el-option label="已关闭" value="CLOSED"/>
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
        <el-table-column prop="seckill_name" label="活动名称"/>
        <el-table-column label="活动时间" width="150">
          <template slot-scope="scope">
            <span>{{ scope.row.start_day | unixToDate }}</span>
          </template>
        </el-table-column>
        <el-table-column label="报名截止时间" width="150">
          <template slot-scope="scope">
            <span>{{ scope.row.apply_end_time | unixToDate }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="seckill_rule" label="报名条件"/>
        <el-table-column prop="seckill_status_text" label="活动状态"/>
        <el-table-column label="报名状态" :formatter="marketStatus" width="80"/>
        <el-table-column label="操作" width="180">
          <template slot-scope="scope">
            <el-button
              type="primary"
              v-if="scope.row.is_apply === 0"
              @click="handleSignUpTimeLimt(scope.row)">报名
            </el-button>
            <el-button
              type="primary"
              v-else
              @click="activityGoodsInfo(scope.row)">查看商品
            </el-button>
          </template>
        </el-table-column>
      </template>
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
  import * as API_limitTime from '@/api/limitTime'
  import { CategoryPicker } from '@/components'

  export default {
    name: 'timeLimit',
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
          page_size: 10
        },

        /** 列表数据 */
        tableData: [],

        /** 列表分页数据 */
        pageData: [],

        /** 高级搜索数据 */
        advancedForm: {}
      }
    },
    mounted() {
      this.GET_LimitActivityList()
    },
    methods: {
      /** 分页大小发生改变 */
      handlePageSizeChange(size) {
        this.params.page_size = size
        this.GET_LimitActivityList()
      },

      /** 分页页数发生改变 */
      handlePageCurrentChange(page) {
        this.params.page_no = page
        this.GET_LimitActivityList()
      },

      /** 报名状态格式化 */
      marketStatus(row, column, cellValue) {
        switch (row.is_apply) {
          case 0: return '未报名'
          case 1: return '已报名'
          case 2: return '已截止'
        }
      },

      /** 搜索事件触发 */
      searchEvent(data) {
        this.params.page_no = 1
        this.params.seckill_name = data
        if (!data) delete this.params.seckill_name
        this.GET_LimitActivityList()
      },

      /** 高级搜索事件触发 */
      advancedSearchEvent() {
        this.params = {
          ...this.params,
          ...this.advancedForm
        }
        if (!this.params.status) this.params.status = 'ALL'
        delete this.params.start_time
        delete this.params.end_time
        const { act_time_range } = this.advancedForm
        if (act_time_range) {
          this.params.start_time = parseInt(act_time_range[0] /= 1000)
          this.params.end_time = parseInt(act_time_range[1] /= 1000)
        }
        delete this.params.act_time_range
        this.params.page_no = 1
        this.GET_LimitActivityList()
      },

      /** 获取限时活动列表*/
      GET_LimitActivityList() {
        this.loading = true
        API_limitTime.getLimitTimeActivityList(this.params).then(response => {
          this.loading = false
          this.pageData = {
            page_no: response.page_no,
            page_size: response.page_size,
            data_total: response.data_total
          }
          this.tableData = response.data
        })
      },

      /** 报名 */
      handleSignUpTimeLimt(row) {
        this.$router.push({ name: 'addTimeLimit', params: { id: row.seckill_id, callback: this.GET_LimitActivityList }})
      },

      /** 活动商品信息 已报名*/
      activityGoodsInfo(row) {
        this.$router.push({ path: `/promotions/activity-goods-data/${row.seckill_id}` })
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
  }

  .toolbar-search {
    margin-right: 10px;
  }

  .goods-image {
    width: 50px;
    height: 50px;
  }

</style>
