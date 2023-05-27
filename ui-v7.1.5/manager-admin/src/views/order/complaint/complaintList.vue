<template>
  <en-table-layout
    :tableData="tableData.data"
    size="small"
    style="width: 100%">
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
      <el-table-column type="index" :index="tableIndex" label="投诉编号" width="80"></el-table-column>
      <el-table-column prop="member_name" label="投诉人" width="100"></el-table-column>
      <el-table-column prop="content" label="投诉内容"></el-table-column>
      <el-table-column prop="complain_topic" label="投诉主题"></el-table-column>
      <el-table-column prop="seller_name" label="被投诉商家" width="100"></el-table-column>
      <el-table-column prop="create_time" :formatter="MixinUnixToDate" label="投诉时间" width="180"></el-table-column>
      <el-table-column prop="status" label="投诉状态" width="80">
        <template slot-scope="{ row }">
          {{row.status | invoiceStatus}}
        </template>
      </el-table-column>
      <el-table-column label="管理操作" width="120">
        <template slot-scope="{ row }">
          <el-button type="primary" @click="handleComplaint(row)" v-if="row.status === 'COMPLETE' || row.status === 'CANCEL'">查看</el-button>
          <el-button type="primary" @click="handleComplaint(row)" v-else>处理</el-button>
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
</template>

<script>
    import * as API_Complaint from '@/api/complaint'
    export default {
      name: 'complaintList',
      data() {
        return {
          searchKey: '',
          tableData: {},
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
        this.GET_ComplainList()
      },
      activated() {
        this.GET_ComplainList()
      },
      methods: {
        /** 搜索事件触发 */
        searchEvent(data) {
          this.params = {
            ...this.params,
            page_no: 1,
            keyword: data
          }
          this.GET_ComplainList()
        },
        /** 高级搜索事件触发 */
        advancedSearchEvent() {
          this.params = {
            ...this.params,
            page_no: 1,
            ...this.advancedForm
          }
          delete this.params.keyword
          this.GET_ComplainList()
        },
        /* 投诉编号递减 */
        tableIndex(index) {
          // 总数
          const total = this.tableData.data_total || 0
          // 当前是第几页，说明 从那个地方开始 减  0, 10, 20, 30 ....
          const num = (this.params.page_no - 1) * this.params.page_size
          return total - num - index
        },
        /* 查看投诉详情 */
        handleComplaint(row) {
          this.$router.push({
            name: 'complaintDetail',
            params: {
              id: row.complain_id,
              callback: this.GET_ComplainList
            }
          })
        },
        /* 获取投诉列表 */
        GET_ComplainList() {
          API_Complaint.getComplainList(this.params).then(response => {
            this.tableData = response
          })
        },
        /* 分页大小发生改变 */
        handlePageSizeChange(size) {
          this.params.page_size = size
          this.GET_ComplainList()
        },
        /** 分页页数发生改变 */
        handlePageCurrentChange(page) {
          this.params.page_no = page
          this.GET_ComplainList()
        }
      }
    }
</script>

<style lang="scss" scoped>
  .inner-toolbar {
    display: flex;
    width: 100%;
    justify-content: space-between;
    padding: 0 20px;
  }
.complaint-toolbar {
  display: flex;
  justify-content: flex-end;
  width: 100%;
  .searchInput {
    width: 250px;
    border-radius: 50px;
    input {
      border-radius: 16px;
      padding: 0 20px;
    }
    .input-icon-suffix {
      cursor: pointer;
      &:hover {
        color: #409EFF;
      }
    }
  }
}
</style>
