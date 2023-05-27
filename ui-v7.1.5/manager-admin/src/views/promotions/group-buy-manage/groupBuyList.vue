<template>
  <div>
    <en-table-layout
      :tableData="tableData.data"
      :loading="loading"
    >
      <div slot="toolbar" class="inner-toolbar">
        <div class="toolbar-btns">
          <el-button size="mini" type="primary" icon="el-icon-circle-plus-outline" @click="handleAddGroupBuy">添加</el-button>
        </div>
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
                  <el-input size="medium" v-model="advancedForm.act_name" clearable></el-input>
                </el-form-item>
                <el-form-item label="活动状态">
                  <el-select v-model="advancedForm.act_status" placeholder="请选择">
                    <el-option label="全部" value="ALL"/>
                    <el-option label="未开始" value="NOT_STARTED"/>
                    <el-option label="进行中" value="STARTED"/>
                    <el-option label="已结束" value="OVER"/>
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
        <el-table-column label="创建时间">
          <template slot-scope="scope">{{ scope.row.add_time | unixToDate }}</template>
        </el-table-column>
        <el-table-column prop="act_name" label="活动名称"/>
        <el-table-column label="开始时间">
          <template slot-scope="scope">{{ scope.row.start_time | unixToDate }}</template>
        </el-table-column>
        <el-table-column label="结束时间">
          <template slot-scope="scope">{{ scope.row.end_time | unixToDate }}</template>
        </el-table-column>
        <el-table-column prop="status_text" label="活动状态"/>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="primary"
              @click="handleOperateGroupBuy(scope.$index, scope.row)">管理</el-button>
            <el-button
              v-if="scope.row.status_text === '未开始'"
              size="mini"
              type="primary"
              @click="handleEditGroupBuy(scope.$index, scope.row)">修改</el-button>
            <el-button
              v-if="scope.row.status_text === '未开始' || scope.row.status_text === '已结束'"
              size="mini"
              type="danger"
              @click="handleDeleteGroupBuy(scope.$index, scope.row)">删除</el-button>
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

    <!--添加团购 dialog-->
    <el-dialog
      :title="groupBuyForm.act_id ? '编辑团购' : '添加团购'"
      :visible.sync="dialogGroupBuyVisible"
      width="650px"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <el-form :model="groupBuyForm" :rules="groupBuyRules" ref="groupBuyForm" label-width="120px">
        <el-form-item label="活动名称" prop="act_name">
          <el-input v-model="groupBuyForm.act_name" :maxlength="20"></el-input>
        </el-form-item>
        <el-form-item label="报名截止时间" prop="join_end_time">
          <el-date-picker
            v-model="groupBuyForm.join_end_time"
            type="datetime"
            :editable="false"
            value-format="timestamp"
            placeholder="报名截止时间"
            :default-time="MixinDefaultTime"
            :picker-options="{disabledDate(time) { return time.getTime() < Date.now() - 8.64E7 }}">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="活动时间段" prop="time_range">
          <el-date-picker
            v-model="groupBuyForm.time_range"
            type="datetimerange"
            range-separator="-"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            :editable="false"
            value-format="timestamp"
            :default-time="[MixinDefaultTime, MixinDefaultTime]"
            :picker-options="{disabledDate(time) { return time.getTime() < Date.now() - 8.64E7 }}">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogGroupBuyVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitAddGroupBuyForm('groupBuyForm')">确 定</el-button>
      </div>
    </el-dialog>

    <!--删除团购活动 dialog-->
    <el-dialog
      :title="dialogDeleteActiveTitle"
      :visible.sync="dialogDeleteActiveVisible"
      width="400px"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <el-form :model="deleteActiveForm" ref="deleteActiveForm" label-width="100px">
        <el-form-item label="删除原因" prop="delete_reason">
          <el-input
            type="textarea"
            :autosize="{ minRows: 2, maxRows: 4}"
            placeholder="请输入删除原因(200字以内)"
            :maxlength="200"
            v-model="deleteActiveForm.delete_reason">
          </el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogDeleteActiveVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitDeleteActiveForm">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import * as API_Promotion from '@/api/promotion'

  export default {
    name: 'groupBuyList',
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
        tableData: '',

        /** 添加团购 表单 */
        groupBuyForm: {},

        /** 高级搜索数据 */
        advancedForm: {},

        /** 删除团购活动 表单 */
        deleteActiveForm: {
          delete_reason: ''
        },
        /** 删除团购活动 dialog */
        dialogDeleteActiveVisible: false,
        // 删除团购活动 dialogTitle
        dialogDeleteActiveTitle: '删除团购活动',

        /** 添加团购 表单规则 */
        groupBuyRules: {
          // act_name: [this.MixinRequired('请输入活动名称！')],
          act_name: [
            { required: true, message: '请输入活动名称！' },
            {
              validator: (rule, value, callback) => {
                if (value.trim()) {
                  callback()
                } else {
                  callback('请输入正确的活动名称！')
                }
              }
            }
          ],
          time_range: [
            this.MixinRequired('请选择活动时间段！'),
            { validator: (rule, value, callback) => {
              const { join_end_time } = this.groupBuyForm
              if (!join_end_time) {
                callback()
              } else {
                value[0] > join_end_time ? callback() : callback(new Error('活动开始时间必须大于报名截止时间！'))
              }
            } }
          ],
          join_end_time: [
            this.MixinRequired('请选择截止报名时间！'),
            { validator: (rule, value, callback) => {
              const { time_range } = this.groupBuyForm
              if (!time_range) {
                callback()
              } else {
                value < time_range[0] ? callback() : callback(new Error('报名截止时间必须小于活动开始时间！'))
              }
            } }
          ]
        },

        /** 添加团购 dialog */
        dialogGroupBuyVisible: false
      }
    },
    mounted() {
      this.GET_GroupBuyList()
    },
    methods: {
      /** 分页大小发生改变 */
      handlePageSizeChange(size) {
        this.params.page_size = size
        this.GET_GroupBuyList()
      },

      /** 分页页数发生改变 */
      handlePageCurrentChange(page) {
        this.params.page_no = page
        this.GET_GroupBuyList()
      },

      /** 添加团购 */
      handleAddGroupBuy() {
        this.groupBuyForm = {}
        this.dialogGroupBuyVisible = true
      },

      /** 修改团购 */
      handleEditGroupBuy(index, row) {
        const params = this.MixinClone(row)
        params.join_end_time *= 1000
        params.time_range = [
          params.start_time *= 1000,
          params.end_time *= 1000
        ]
        this.groupBuyForm = params
        this.dialogGroupBuyVisible = true
      },

      /** 管理团购 */
      handleOperateGroupBuy(index, row) {
        this.$router.push({ path: `/promotions/group-buy-manage/group-buy-goods/${row.act_id}` })
      },

      /** 删除团购 */
      handleDeleteGroupBuy(index, row) {
        this.dialogDeleteActiveTitle = '删除ID为' + row.act_id + '的团购活动'
        this.deleteActiveForm = {
          act_id: row.act_id,
          delete_reason: ''
        }
        this.dialogDeleteActiveVisible = true
      },

      /** 审核商品 表单提交 */
      submitDeleteActiveForm() {
        const val = this.deleteActiveForm.delete_reason.trim()
        if (val) {
          API_Promotion.deleteGroupBuyActivity(this.deleteActiveForm.act_id, val).then(response => {
            this.dialogDeleteActiveVisible = false
            this.$message.success('删除成功！')
            this.GET_GroupBuyList()
          })
        } else {
          this.$message.error('请输入删除原因!')
          return false
        }
      },

      /** 添加团购 提交表单 */
      submitAddGroupBuyForm(formName) {
        const _time_range = this.groupBuyForm.time_range
        this.$refs[formName].validate((valid) => {
          const params = this.MixinClone(this.groupBuyForm)
          if (valid) {
            const { act_id } = params
            params.join_end_time /= 1000
            params.start_time = _time_range[0] / 1000
            params.end_time = _time_range[1] / 1000
            if (act_id) {
              API_Promotion.editGroupBuyActivity(act_id, params).then(response => {
                this.dialogGroupBuyVisible = false
                this.$message.success('修改成功！')
                this.GET_GroupBuyList()
              })
            } else {
              API_Promotion.addGrouBuyActivity(params).then(response => {
                this.dialogGroupBuyVisible = false
                this.GET_GroupBuyList()
                this.$message.success('添加成功！')
              })
            }
          } else {
            this.$message.error('表单填写有误，请检查！')
            return false
          }
        })
      },

      /** 搜索事件触发 */
      searchEvent(data) {
        this.params.page_no = 1
        this.params.act_name = data
        if (!data) delete this.params.act_name
        this.GET_GroupBuyList()
      },

      /** 高级搜索事件触发 */
      advancedSearchEvent() {
        this.params = {
          ...this.params,
          ...this.advancedForm
        }
        if (!this.params.act_status) this.params.act_status = 'ALL'
        delete this.params.start_time
        delete this.params.end_time
        const { act_time_range } = this.advancedForm
        if (act_time_range) {
          this.params.start_time = parseInt(act_time_range[0] /= 1000)
          this.params.end_time = parseInt(act_time_range[1] /= 1000)
        }
        delete this.params.act_time_range
        this.params.page_no = 1
        this.GET_GroupBuyList()
      },

      /** 获取团购活动列表 */
      GET_GroupBuyList() {
        this.loading = true
        API_Promotion.getGroupBuyActives(this.params).then(response => {
          this.loading = false
          this.tableData = response
        }).catch(() => { this.loading = false })
      }
    }
  }
</script>
