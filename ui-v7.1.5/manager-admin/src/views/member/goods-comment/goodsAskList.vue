<template>
  <div>
    <en-table-layout
      :tableData="tableData.data"
      :row-key="setRowKey"
      @selection-change="handleSelectionChange"
      :loading="loading"
      ref="tableContainer"
    >
      <div slot="toolbar" class="inner-toolbar">
        <div class="toolbar-btns">
          <el-button
            size="mini"
            type="primary"
            @click="handleAskBatchAudit">批量审核</el-button>
        </div>
        <div class="toolbar-search">
          <en-table-search
            @search="searchEvent"
            @advancedSearch="advancedSearchEvent"
            advanced
            advancedWidth="465"
          >
            <template slot="advanced-content">
              <el-form ref="advancedForm" :model="advancedForm" label-width="80px">
                <el-form-item label="商品名称">
                  <el-input size="medium" v-model="advancedForm.goods_name" clearable></el-input>
                </el-form-item>
                <el-form-item label="咨询会员">
                  <el-input size="medium" v-model="advancedForm.member_name" clearable></el-input>
                </el-form-item>
                <el-form-item label="咨询内容">
                  <el-input v-model="advancedForm.content" clearable></el-input>
                </el-form-item>
                <el-form-item label="审核状态">
                  <el-select v-model="advancedForm.auth_status" placeholder="请选择" clearable>
                    <el-option label="待审核" value="WAIT_AUDIT"/>
                    <el-option label="审核通过" value="PASS_AUDIT"/>
                    <el-option label="审核未通过" value="REFUSE_AUDIT"/>
                  </el-select>
                </el-form-item>
                <el-form-item label="咨询日期">
                  <el-date-picker
                    v-model="advancedForm.create_time_range"
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
        <el-table-column type="selection" width="55" :selectable="isSelectRow" :reserve-selection="true"></el-table-column>
        <el-table-column prop="create_time" :formatter="MixinUnixToDate" label="咨询日期"/>
        <el-table-column label="商品名称">
          <template slot-scope="scope">
            <a :href="MixinBuyerDomain + '/goods/' + scope.row.goods_id" class="goods-name" target="_blank">{{ scope.row.goods_name }}</a>
          </template>
        </el-table-column>
        <el-table-column prop="member_name" label="咨询会员"/>
        <el-table-column prop="content" label="咨询内容" width="350"/>
        <el-table-column label="审核状态">
          <template slot-scope="scope">{{ scope.row.auth_status | statusFilter }}</template>
        </el-table-column>
        <el-table-column prop="reply_num" label="回复数量"/>
        <el-table-column label="操作" width="240">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="primary"
              @click="handleAskAudit(scope.$index, scope.row)"
            v-if="scope.row.auth_status === 'WAIT_AUDIT'">
              审核
            </el-button>
            <el-button
              size="mini"
              type="primary"
              @click="handleDetailAsk(scope.$index, scope.row)">查看</el-button>
            <el-button
              size="mini"
              type="danger"
              @click="handleDeleteAsk(scope.$index, scope.row)">删除</el-button>
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
    <!--审核会员商品咨询 dialog-->
    <el-dialog
      :title="dialogConsultationAuditTitle"
      :visible.sync="dialogConsultationAuditVisible"
      width="400px"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <el-form :model="auditInformation" ref="auditInformation" label-width="100px">
        <!--是否通过=-->
        <el-form-item label="是否通过" prop="auth_status">
          <el-radio-group v-model="auditInformation.auth_status">
            <el-radio v-model="auditInformation.auth_status" label="PASS_AUDIT">通过</el-radio>
            <el-radio v-model="auditInformation.auth_status" label="REFUSE_AUDIT">不通过</el-radio>
          </el-radio-group>
        </el-form-item>

      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogConsultationAuditVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitExamine">确 定</el-button>
      </span>
    </el-dialog>
    <!--批量审核会员商品咨询 dialog-->
    <el-dialog
      title="批量审核"
      :visible.sync="dialogConsultationBatchAuditVisible"
      width="400px"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <el-form :model="batchAuditForm" ref="batchAuditForm" label-width="100px">
        <!--是否通过=-->
        <el-form-item label="是否通过" prop="auth_status">
          <el-radio-group v-model="batchAuditForm.auth_status">
            <el-radio v-model="batchAuditForm.auth_status" label="PASS_AUDIT">通过</el-radio>
            <el-radio v-model="batchAuditForm.auth_status" label="REFUSE_AUDIT">不通过</el-radio>
          </el-radio-group>
        </el-form-item>

      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogConsultationBatchAuditVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitBatchExamine">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import * as API_Member from '@/api/member'
  import { Foundation } from '~/ui-utils'

  export default {
    name: 'goodsAskList',
    data() {
      return {
        // 列表loading状态
        loading: false,
        // 列表参数
        params: {
          page_no: 1,
          page_size: 10
        },
        // 列表数据
        tableData: '',
        // 审核商品 dialog
        dialogConsultationAuditVisible: false,
        // 标题
        dialogConsultationAuditTitle: '',
        // 审核信息
        auditInformation: {
          ids: [],
          auth_status: 'PASS_AUDIT'
        },
        // 批量审核商品 dialog
        dialogConsultationBatchAuditVisible: false,
        // 批量审核信息
        batchAuditForm: {
          ids: [],
          auth_status: 'PASS_AUDIT'
        },
        // 关键字
        keyword: '',
        // 高级搜索
        advancedForm: {},
        // 当前已选择的行
        multipleSelection: []
      }
    },
    mounted() {
      this.GET_AskList()
    },
    filters: {
      statusFilter(val) {
        switch (val) {
          case 'WAIT_AUDIT': return '待审核'
          case 'PASS_AUDIT': return '审核通过'
          case 'REFUSE_AUDIT': return '审核未通过'
        }
      }
    },
    methods: {
      // 单选或者全选
      handleSelectionChange(val) {
        this.multipleSelection = val
      },
      // 判断 当前行是都可以选中
      isSelectRow(row, index) {
        return row.auth_status === 'WAIT_AUDIT' ? 1 : 0
      },
      setRowKey(row) {
        return row.create_time
      },
      /** 分页大小发生改变 */
      handlePageSizeChange(size) {
        this.params.page_size = size
        this.GET_AskList()
      },

      /** 分页页数发生改变 */
      handlePageCurrentChange(page) {
        this.params.page_no = page
        this.GET_AskList()
      },

      /** 单个审核 */
      handleAskAudit(index, row) {
        let content = row.content
        if (row.content.length > 10) {
          content = content.slice(0, 10) + '...'
        }
        this.dialogConsultationAuditTitle = '咨询审核 - ' + content
        this.auditInformation = {
          ids: [row.ask_id],
          auth_status: 'PASS_AUDIT'
        }
        this.dialogConsultationAuditVisible = true
      },

      /** 批量审核 */
      handleAskBatchAudit() {
        if (!this.multipleSelection.length) {
          return this.$message.error('请选择要审核的商品咨询！')
        }
        this.dialogConsultationBatchAuditVisible = true
      },

      /** 提交咨询审核 */
      submitExamine() {
        API_Member.batchAuditAsk(this.auditInformation).then(response => {
          this.$message.success('操作成功！')
          this.$refs.tableContainer && this.$refs.tableContainer.$refs && this.$refs.tableContainer.$refs.table && this.$refs.tableContainer.$refs.table.clearSelection()
          this.dialogConsultationAuditVisible = false
          this.GET_AskList()
        }).catch(() => {})
      },

      /** 批量审核商品咨询 表单提交 */
      async submitBatchExamine() {
        let params = JSON.parse(JSON.stringify(this.batchAuditForm))
        try {
          params.ids = this.multipleSelection.map(item => item.ask_id)
          await API_Member.batchAuditAsk(params)
          await this.GET_AskList()
          this.params.page_no = 1
          this.$refs.tableContainer && this.$refs.tableContainer.$refs && this.$refs.tableContainer.$refs.table && this.$refs.tableContainer.$refs.table.clearSelection()
          this.dialogConsultationBatchAuditVisible = false
          this.$message.success('操作成功！')
        } catch (e) {
          this.$message.error('操作失败，请重试！')
        }
      },

      /** 查看咨询详情和回复内容 */
      handleDetailAsk(index, row) {
        this.$router.push({
          name: 'askDetail',
          params: {
            askId: row.ask_id,
            callback: this.GET_AskList
          }
        })
      },

      /** 删除咨询 */
      handleDeleteAsk(index, row) {
        this.$confirm('确定要删除这条咨询吗？', '提示', { type: 'warning' }).then(() => {
          API_Member.deleteMemberAsk(row.ask_id).then(() => {
            this.$message.success('删除成功！')
            this.GET_AskList()
          })
        }).catch(() => {})
      },

      /** 搜索事件触发 */
      searchEvent(keyword) {
        this.params.keyword = keyword
        Object.keys(this.advancedForm).forEach(key => delete this.params[key])
        this.params.page_no = 1
        this.GET_AskList()
      },

      /** 高级搜索事件触发 */
      advancedSearchEvent() {
        const { advancedForm } = this
        Object.keys(advancedForm).forEach(key => {
          if (advancedForm[key] !== undefined) {
            this.params[key] = advancedForm[key]
          }
        })
        delete this.params.keyword

        delete this.params.start_time
        delete this.params.end_time
        if (this.advancedForm.create_time_range) {
          this.params.start_time = this.advancedForm.create_time_range[0]
          this.params.end_time = this.advancedForm.create_time_range[1]
        }
        delete this.params.create_time_range
        this.params.page_no = 1
        this.GET_AskList()
      },

      /** 获取咨询列表 */
      GET_AskList() {
        this.loading = true
        const params = this.MixinClone(this.params)
        if (params.start_time && params.end_time) {
          params.start_time = parseInt(params.start_time / 1000)
          params.end_time = parseInt(params.end_time / 1000)
        }
        API_Member.getMemberAsks(params).then(response => {
          this.loading = false
          this.tableData = response
        }).catch(() => { this.loading = false })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .goods-name {
    color: #4183c4;
    &:hover { color: #f42424 }
  }
	.content-text {
		display:inline-block ;
	}
</style>
