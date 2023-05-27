<template>
  <div>
    <en-table-layout
      pagination
      :tableData="tableData.data">
      <div slot="toolbar" class="inner-toolbar">
        <div class="toolbar-btns"></div>
        <div class="toolbar-search">
          <en-table-search
            @search="searchEvent"
            @advancedSearch="advancedSearchEvent"
            advanced
            advancedWidth="465"
            placeholder="请输入关键词">
            <template slot="advanced-content">
              <el-form ref="advancedForm" :model="advancedForm" label-width="80px">
                <el-form-item label="订单编号">
                  <el-input v-model="advancedForm.order_sn" clearable></el-input>
                </el-form-item>
                <el-form-item label="投诉状态">
                  <el-select v-model="advancedForm.tag" placeholder="请选择" clearable>
                    <el-option label="所有" value="ALL"/>
                    <el-option label="进行中" value="COMPLAINING"/>
                    <el-option label="已取消" value="CANCELED"/>
                    <el-option label="已完成" value="COMPLETE"/>
                  </el-select>
                </el-form-item>
              </el-form>
            </template>
          </en-table-search>
        </div>
      </div>
      <template slot="table-columns">
        <el-table-column type="index" :index="tableIndex" label="投诉编号" width="80px"/>
        <el-table-column label="投诉商品">
          <template slot-scope="{ row }">
            <div class="goods-info">
              <a :href="`${MixinBuyerDomain}/goods/${row.goods_id}`" target="_blank">
                <img :src="row.goods_image">
              </a>
              <a :href="`${MixinBuyerDomain}/goods/${row.goods_id}`" target="_blank" style="color: #00a2d4;" v-html="row.goods_name">
                {{row.goods_name}}
              </a>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="complain_topic" label="投诉主题"/>
        <el-table-column label="投诉时间">
          <template slot-scope="scope">{{ scope.row.create_time | unixToDate('yyyy-MM-dd hh:mm:ss') }}</template>
        </el-table-column>
        <el-table-column prop="status" label="投诉状态" width="80">
          <template slot-scope="{ row }">
            {{row.status | invoiceStatus}}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="操作" width="150">
          <template slot-scope="scope">
            <el-button
              type="primary"
              v-if="scope.row.status === 'WAIT_APPEAL'"
              @click="handleComplaint(scope.row)">处理
            </el-button>
            <el-button
              type="primary"
              v-if="scope.row.status !== 'WAIT_APPEAL'"
              @click="handleComplaint(scope.row)">查看
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
  import * as API_Complaint from '@/api/complain'
  export default {
    name: 'complaintList',
    data() {
      return {
        /** 列表数据 */
        tableData: '',
        /** 列表分页数据 */
        pageData: [],
        params: {
          page_no: 1,
          page_size: 10
        },
        /** 高级搜索数据 */
        advancedForm: {}
      }
    },
    filters: {
      /* 申诉状态 过滤 */
      invoiceStatus(status) {
        switch (status) {
          case 'NEW': return '新投诉'
          case 'WAIT_APPEAL': return '待申诉'
          case 'CANCEL': return '已撤销'
          case 'COMMUNICATION': return '对话中'
          case 'WAIT_ARBITRATION': return '等待仲裁'
          case 'COMPLETE': return '已完成'
        }
      }
    },
    mounted() {
      this.GET_ComplaintList()
    },
    activated() {
      this.GET_ComplaintList()
    },
    methods: {
      /** 搜索事件触发 */
      searchEvent(data) {
        this.params = {
          ...this.params,
          page_no: 1,
          keyword: data
        }
        this.GET_ComplaintList()
      },
      /** 高级搜索事件触发 */
      advancedSearchEvent() {
        this.params = {
          ...this.params,
          page_no: 1,
          ...this.advancedForm
        }
        delete this.params.keyword
        this.GET_ComplaintList()
      },
      /* 查看详情，处理 */
      handleComplaint(row) {
        this.$router.push({ name: 'complaintDetail', params: { id: row.complain_id }})
      },
      /* 投诉编号递减 */
      tableIndex(index) {
        // 总数
        const total = this.tableData.data_total || 0
        // 当前是第几页，说明 从那个地方开始 减  0, 10, 20, 30 ....
        const num = (this.params.page_no - 1) * this.params.page_size
        return total - num - index
      },
      /** 分页大小发生改变 */
      handlePageSizeChange(size) {
        this.params.page_size = size
        this.GET_ComplaintList()
      },

      /** 分页页数发生改变 */
      handlePageCurrentChange(page) {
        this.params.page_no = page
        this.GET_ComplaintList()
      },
      /** 获取交易投诉列表 */
      GET_ComplaintList() {
        API_Complaint.getComplainList(this.params).then(response => {
          this.pageData = {
            page_no: response.page_no,
            page_size: response.page_size,
            data_total: response.data_total
          }
          this.tableData = response
        })
      }
    }
  }
</script>

<style lang="scss" scoped>
  /deep/ div.toolbar {
    height: 70px;
    padding: 20px 0;
  }

  .inner-toolbar {
    display: flex;
    flex-direction: row;
    flex-wrap: nowrap;
    width: 100%;
    justify-content: space-between;
    align-items: center;
  }
  .goods-info {
    display: flex;
    flex-direction: row;
    flex-wrap: nowrap;
    justify-content: center;
    align-items: center;
    padding: 10px 0;
    img {
      display: block;
      margin-right: 10px;
      width: 50px;
      height: 50px;
    }
    a {
      display: block;
      color: #409EFF;
    }
  }
</style>
