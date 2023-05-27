<template>
  <div>
    <en-table-layout
      pagination
      :tableData="tableData"
      @selection-change="handleSelectionChange"
      :loading="loading"
      ref="tableContainer"
      :tips="true">
      <div slot="tips">
        <h4>操作提示</h4>
        <ul>
          <li>导出时，会按右侧高级搜索中的筛选条件导出全部数据。如果不需要，请先清空筛选条件！</li>
        </ul>
      </div>
      <div slot="toolbar" class="inner-toolbar">
        <div class="toolbar-btns">
          <el-button size="mini" type="primary" @click="handlePutForward">批量审核</el-button>
          <el-button size="mini" type="primary" @click="handleBatchOpera">批量设为已转账</el-button>
          <el-button size="mini" type="primary" icon="el-icon-download" @click="handleExportApply" style="margin-left: 5px">导出Excel</el-button>
        </div>
        <div class="toolbar-search">
          <en-table-search
            @search="searchEvent"
            @advancedSearch="advancedSearchEvent"
            advanced
            advancedWidth="465"
            placeholder="请输入会员名称">
            <template slot="advanced-content">
              <el-form ref="advancedForm" :model="advancedForm" label-width="80px">
                <el-form-item label="申请时间">
                  <el-date-picker
                    v-model="advancedForm.putforward_time_range"
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
                <el-form-item label="状态">
                  <el-select v-model="advancedForm.status" placeholder="请选择" clearable>
                    <el-option label="审核未通过" value="FAIL_AUDITING"/>
                    <el-option label="审核通过" value="VIA_AUDITING"/>
                    <el-option label="已转账" value="TRANSFER_ACCOUNTS"/>
                    <el-option label="申请中" value="APPLY"/>
                  </el-select>
                </el-form-item>
              </el-form>
            </template>
          </en-table-search>
        </div>
      </div>
      <template slot="table-columns">
        <el-table-column type="selection" width="55" :selectable="isSelectRow" :reserve-selection="false"></el-table-column>
        <!--ID-->
        <el-table-column prop="id" label="ID"/>
        <!--申请时间-->
        <el-table-column prop="apply_time" :formatter="MixinUnixToDate" label="申请时间"/>
        <!--申请金额-->
        <el-table-column label="申请金额">
          <template slot-scope="scope">{{ scope.row.apply_money | unitPrice('￥') }}</template>
        </el-table-column>
        <!--会员-->
        <el-table-column prop="member_name" label="会员"/>
        <!--提现状态-->
        <el-table-column prop="status" label="提现状态" :formatter="withDrawStatus"/>
        <el-table-column label="操作" width="280">
          <template slot-scope="scope">
            <el-button v-if="scope.row.status === 'VIA_AUDITING'" type="success"  size="mini" @click="handleOpera('TRANSFER_ACCOUNTS', scope.row)">设为已转账</el-button>
            <el-button v-if="scope.row.status === 'APPLY'" type="success"  size="mini" @click="handleOpera('VIA_AUDITING', scope.row)">通过审核</el-button>
            <el-button v-if="scope.row.status === 'APPLY'" type="danger" size="mini" @click="handleOpera('FAIL_AUDITING', scope.row)">不能通过</el-button>
            <el-button size="mini" type="primary" @click="handleNext(scope.row)">查看</el-button>
          </template>
        </el-table-column>
      </template>
      <el-pagination
        v-if="tableData"
        slot="pagination"
        @size-change="handlePageSizeChange"
        @current-change="handlePageCurrentChange"
        :current-page="pageData.page_no"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pageData.page_size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pageData.data_total">
      </el-pagination>
    </en-table-layout>
    <!--查看提现记录-->
    <el-dialog title="查看提现记录" :visible.sync="isShowPutForwardRecoreds" width="55%" align="center">
      <div align="center">
        <div class="d-header"> 提现基本信息 </div>
        <table class="putforawrd-baseinfo">
          <tr>
            <td>申请金额</td>
            <td>{{ currentRow.apply_money  | unitPrice('￥') }}</td>
          </tr>
          <tr>
            <td>申请时间</td>
            <td>{{ currentRow.apply_time | unixToDate }}</td>
          </tr>
        </table>
        <div class="d-header"> 银行卡信息 </div>
        <table class="putforawrd-baseinfo">
          <tr>
            <td>户名</td>
            <td>{{bank.member_name}}</td>
          </tr>
          <tr>
            <td>所属银行</td>
            <td>{{bank.bank_name}}</td>
          </tr>
          <tr>
            <td>开户行号</td>
            <td>{{bank.opening_num}}</td>
          </tr>
          <tr>
            <td>银行卡号</td>
            <td>{{bank.bank_card}}</td>
          </tr>
        </table>
        <!--提现日志-->
        <div class="d-header"> 提现日志 </div>
        <en-table-layout :tableData="putforwardLogs" class="pop-table" border v-if="putforwardLogs[0]">
          <template slot="table-columns">
            <el-table-column v-if="putforwardLogs[0].apply_time"  prop="apply_time" :formatter="MixinUnixToDate" label="申请时间"/>
            <el-table-column v-if="putforwardLogs[0].apply_time"  prop="apply_remark" label="申请备注"/>
            <el-table-column v-if="putforwardLogs[0].inspect_time" prop="inspect_time" :formatter="MixinUnixToDate" label="审核时间"/>
            <el-table-column v-if="putforwardLogs[0].inspect_time"  prop="inspect_remark" label="审核备注"/>
            <el-table-column v-if="putforwardLogs[0].transfer_time"  prop="transfer_time" :formatter="MixinUnixToDate" label="转账时间"/>
            <el-table-column v-if="putforwardLogs[0].transfer_time" prop="transfer_remark" label="转账备注"/>
            <el-table-column  prop="status" label="状态" :formatter="withDrawStatus" />
          </template>
        </en-table-layout>
      </div>
    </el-dialog>
    <!--审核备注-->
    <el-dialog title="审核备注" :visible.sync="isShowAuthRemarks" width="23%" align="center">
      <el-form>
        <el-form-item label="审核备注" label-width="80px">
          <el-input type="textarea" v-model="authRemarks" clearable></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="isShowAuthRemarks = false">取 消</el-button>
        <el-button type="primary" @click="handleRefusedAudit()">确 定</el-button>
      </div>
    </el-dialog>
    <!--转账备注-->
    <el-dialog title="转账备注" :visible.sync="isShowTransRemarks" width="23%" align="center">
      <el-form>
        <el-form-item label="转账备注" label-width="80px">
          <el-input type="textarea" v-model="transRemarks" clearable></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="isShowTransRemarks = false">取 消</el-button>
        <el-button type="primary" @click="handleTrans()">确 定</el-button>
      </div>
    </el-dialog>
    <!--批量审核商品 dialog-->
    <el-dialog
      title="批量审核限时抢购商品"
      :visible.sync="dialogPutForward"
      width="400px"
    >
      <el-form :model="putForwardForm" :rules="putForwardFormRules" ref="putForwardForm" label-width="100px">
        <!--是否通过=-->
        <el-form-item label="是否通过" prop="status">
          <el-radio-group v-model="putForwardForm.audit_result">
            <el-radio label="VIA_AUDITING">通过</el-radio>
            <el-radio label="FAIL_AUDITING">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="审核备注" prop="remark" v-if="putForwardForm.audit_result === 'FAIL_AUDITING'">
          <el-input
            type="textarea"
            :autosize="{ minRows: 2, maxRows: 4}"
            placeholder="请输入审核备注(120字以内)"
            :maxlength="120"
            v-model="putForwardForm.remark">
          </el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogPutForward = false">取 消</el-button>
        <el-button type="primary" @click="submitPutForwardForm">确 定</el-button>
      </span>
    </el-dialog>
    <!--批量转账备注-->
    <el-dialog title="转账备注" :visible.sync="isShowBatchTransRemarks" width="23%" align="center">
      <el-form>
        <el-form-item label="转账备注" label-width="80px">
          <el-input type="textarea" v-model="batchTransRemarks" clearable></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="isShowBatchTransRemarks = false">取 消</el-button>
        <el-button type="primary" @click="handleBatchTrans">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import * as API_distribution from '@/api/distribution'
  import { Foundation } from '~/ui-utils'
  export default {
    name: 'put-forward-apply',
    data() {
      const checkRemark = (rule, value, callback) => {
        if (this.MixinEmojiCharacter(value)) {
          callback(new Error('不能输入表情符号！'))
        } else {
          callback()
        }
      }
      return {
        // 列表loading状态
        loading: false,

        // 列表参数
        params: {
          page_no: 1,
          page_size: 10
        },

        /** 高级搜索参数 */
        advancedForm: {},

        // 列表数据
        tableData: [],

        // 分页数据
        pageData: [],

        /** 是否显示提现记录 */
        isShowPutForwardRecoreds: false,

        /** 当前操作行 */
        currentRow: {},

        /** 银行信息 */
        bank: {},

        /** 提现日志 */
        putforwardLogs: [],

        /** 是否显示审核备注 */
        isShowAuthRemarks: false,

        /** 审核备注 */
        authRemarks: '',

        /** 是否显示转账备注 */
        isShowTransRemarks: false,

        /** 转账备注 */
        transRemarks: '',

        // 是否显示批量转账
        isShowBatchTransRemarks: false,

        // 批量转账备注
        batchTransRemarks: '',

        /** 当前操作名称 */
        operaName: '',

        // 当前已选择的行
        multipleSelection: [],

        // 批量审核表单
        putForwardForm: {
          apply_ids: [],
          remark: '',
          audit_result: 'VIA_AUDITING'
        },
        apply_ids: [],
        // 是否显示批量审核
        dialogPutForward: false,

        putForwardFormRules: {
          remark: [
            { required: true, message: '请输入审核备注！', trigger: 'blur' },
            { validator: checkRemark, trigger: 'blur' }
          ]
        },
        rules: {
          authRemarks: [{ validator: checkRemark, trigger: 'blur' }]
        }
      }
    },
    mounted() {
      this.GET_WithdrawApplyList()
    },
    methods: {
      /** 状态格式化 */
      withDrawStatus(row) {
        switch (row.status) {
          case 'FAIL_AUDITING': return '审核未通过'
          case 'VIA_AUDITING': return '审核通过'
          case 'TRANSFER_ACCOUNTS': return '已转账'
          case 'APPLY': return '申请中'
        }
      },
      // 单选或者全选
      handleSelectionChange(val) {
        this.multipleSelection = val
      },
      // 判断 当前行是否可以选中
      isSelectRow(row, index) {
        if (row.status === 'TRANSFER_ACCOUNTS' || row.status === 'FAIL_AUDITING') {
          return 0
        } else {
          return 1
        }
      },
      setRowKey(row) {
        return row.create_time
      },
      /** 分页大小发生改变 */
      handlePageSizeChange(size) {
        this.params.page_size = size
        this.GET_WithdrawApplyList()
      },

      /** 分页页数发生改变 */
      handlePageCurrentChange(page) {
        this.params.page_no = page
        this.GET_WithdrawApplyList()
      },

      /** 获取提现申请列表 */
      GET_WithdrawApplyList() {
        this.loading = true
        API_distribution.getWithdrawApplyList(this.params).then(response => {
          this.loading = false
          this.tableData = response.data
          this.pageData = {
            page_no: response.page_no,
            page_size: response.page_size,
            data_total: response.data_total
          }
        })
      },

      /** 改变状态 */
      changeStatus(val) {
        this.params = {
          ...this.params,
          status: val
        }
        this.GET_WithdrawApplyList()
      },

      /** 搜索事件触发 */
      searchEvent(data) {
        this.params = {
          ...this.params,
          page_no: 1,
          uname: data
        }
        Object.keys(this.advancedForm).forEach(key => delete this.params[key])
        this.GET_WithdrawApplyList()
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
        if (this.advancedForm.putforward_time_range) {
          this.params.start_time = this.advancedForm.putforward_time_range[0].getTime() / 1000
          this.params.end_time = this.advancedForm.putforward_time_range[1].getTime() / 1000
        }
        delete this.params.uname
        delete this.params.putforward_time_range
        this.GET_WithdrawApplyList()
      },

      /** 下一步操作 */
      handleNext(row) {
        this.isShowPutForwardRecoreds = true
        this.currentRow = row
        this.bank = this.currentRow.bank_params_vo
        this.putforwardLogs = [{
          'apply_time': row.apply_time,
          'apply_remark': row.apply_remark,
          'inspect_time': row.inspect_time,
          'inspect_remark': row.inspect_remark,
          'transfer_time': row.transfer_time,
          'transfer_remark': row.transfer_remark,
          'status': row.status
        }]
      },

      /** 下一步操作 */
      handleOpera(operaName, row) {
        if (operaName === 'TRANSFER_ACCOUNTS') { // 转账
          this.isShowTransRemarks = true
          this.apply_ids = row.id
        } else { // 审核
          this.isShowAuthRemarks = true
          this.operaName = operaName
          this.apply_ids = row.id
        }
      },

      /** 转账 */
      handleTrans() {
        if (this.MixinEmojiCharacter(this.transRemarks)) {
          this.$message.error('不能输入表情符号！')
          return
        }
        const apply_ids = [this.apply_ids]
        API_distribution.batchSetTransferAccounts({ apply_ids, remark: this.transRemarks }).then(() => {
          this.$message.success('转账成功')
          this.isShowTransRemarks = false
          this.GET_WithdrawApplyList()
        })
      },

      /** 审核 */
      handleRefusedAudit() {
        if (this.MixinEmojiCharacter(this.authRemarks)) {
          this.$message.error('不能输入表情符号！')
          return
        }
        const _params = {
          apply_ids: [this.apply_ids],
          remark: this.authRemarks
        }
        const audit_result = this.operaName
        API_distribution.authWithdrawBatchApply(audit_result, _params).then(() => {
          this.$message.success('已保存审核结果')
          this.GET_WithdrawApplyList()
          this.isShowAuthRemarks = false
        })
      },
      /** 批量审核*/
      handlePutForward() {
        if (!this.multipleSelection.length) {
          return this.$message.error('请选择要审核的提现申请！')
        }
        if (!this.multipleSelection.every(item => item.status === 'APPLY')) {
          return this.$message.error('请选择待审核的提现申请！')
        }
        this.dialogPutForward = true
      },
      /** 批量审核 表单提交*/
      async submitPutForwardForm() {
        let flag = false
        let params = JSON.parse(JSON.stringify(this.putForwardForm))
        params.apply_ids = this.multipleSelection.map(item => item.id)
        const apply_ids = params.apply_ids
        const remark = params.remark
        if (params.audit_result === 'VIA_AUDITING') {
          delete params.remark
          flag = true
        } else {
          await this.$refs.putForwardForm.validate(volid => {
            if (volid) {
              flag = true
            } else {
              return false
            }
          })
        }
        if (flag) {
          try {
            await API_distribution.authWithdrawBatchApply(params.audit_result, { apply_ids, remark })
            await this.GET_WithdrawApplyList()
            this.params.page_no = 1
            this.$refs.tableContainer && this.$refs.tableContainer.$refs && this.$refs.tableContainer.$refs.table && this.$refs.tableContainer.$refs.table.clearSelection()
            this.dialogPutForward = false
            this.$message.success('批量审核成功！')
          } catch (e) {
            this.$message.error('批量审核失败，请重试！')
          }
        }
      },
      /** 批量设为已转账 */
      handleBatchOpera() {
        if (!this.multipleSelection.length) {
          return this.$message.error('请选择要设为已转账的提现申请！')
        }
        if (!this.multipleSelection.every(item => item.status === 'VIA_AUDITING')) {
          return this.$message.error('请选择要设为已转账的提现申请！')
        }
        this.isShowBatchTransRemarks = true
      },
      /** 批量设为已转账 表单提交*/
      async handleBatchTrans() {
        let params = {
          remark: this.batchTransRemarks,
          apply_ids: this.multipleSelection.map(item => item.id)
        }
        try {
          await API_distribution.batchSetTransferAccounts(params)
          await this.GET_WithdrawApplyList()
          this.params.page_no = 1
          this.$refs.tableContainer && this.$refs.tableContainer.$refs && this.$refs.tableContainer.$refs.table && this.$refs.tableContainer.$refs.table.clearSelection()
          this.isShowBatchTransRemarks = false
          this.$message.success('批量设为已转账成功！')
        } catch (e) {
          this.$message.error('批量设为已转账失败，请重试！')
        }
      },
      handleExportApply() {
        API_distribution.exportWithdrawApplyList(this.params).then(response => {
          const json = {
            sheet_name: '提现申请列表',
            sheet_values: response.map(item => ({
              'ID': item.id,
              '提现金额': Foundation.formatPrice(item.apply_money),
              '提现申请时间': Foundation.unixToDate(item.apply_time),
              '提现状态': item.status === 'APPLY' ? '申请中' : item.status === 'VIA_AUDITING' ? '审核通过' : item.status === 'FAIL_AUDITING' ? '审核未通过' : item.status === 'TRANSFER_ACCOUNTS' ? '已转账' : '',
              '申请人': item.member_name,
              '申请备注': item.apply_remark,
              '审核时间': item.inspect_time ? Foundation.unixToDate(item.inspect_time) : '',
              '审核备注': item.inspect_remark,
              '转账时间': item.transfer_time ? Foundation.unixToDate(item.transfer_time) : '',
              '转账备注': item.transfer_remark
            }))
          }
          this.MixinExportJosnToExcel(json, '提现申请列表')
        })
      }
    }
  }
</script>
<style type="text/scss" lang="scss" scoped>
  /deep/ .el-table td:not(.is-left) {
    text-align: center;
  }
  .inner-toolbar {
    display: flex;
    width: 100%;
    justify-content: space-between;
    padding: 0 20px;
  }
  .conditions {
    span {
      font-size: 14px;
      color: #666;
    }
  }

  /*提现基本信息*/
  .putforawrd-baseinfo {
    width: 100%;
    border: 1px solid #ddd;
    border-collapse: collapse;
    tr, td {
      border: 1px solid #ddd;
      border-collapse: collapse;
      line-height: 40px;
      padding: 0 15px;
    }
    td:first-child {
      width: 200px;
    }
  }
  /*提现日志*/
  div.d-header {
    width: 100%;
    line-height: 40px;
    background-color: #eee;
    text-align: left;
    padding:0 15px;
    & + .container {
      padding: 0;
      /deep/ .toolbar {
        display: none;
      }
    }
  }

</style>










