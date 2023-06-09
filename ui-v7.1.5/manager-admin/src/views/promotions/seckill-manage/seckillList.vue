<template>
  <div>
    <en-table-layout
      :tableData="tableData.data"
      :loading="loading"
    >

      <div slot="toolbar" class="inner-toolbar">
        <div class="toolbar-btns">
          <el-button size="mini" type="primary" icon="el-icon-circle-plus-outline" @click="handleAddSeckill">添加</el-button>
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
                  <el-input size="medium" v-model="advancedForm.seckill_name" clearable></el-input>
                </el-form-item>
                <el-form-item label="活动状态">
                  <el-select v-model="advancedForm.status" placeholder="请选择">
                    <el-option label="全部" value="ALL"/>
                    <el-option label="编辑中" value="EDITING"/>
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
        <el-table-column prop="start_day" :formatter="MixinUnixToDate" label="活动开始时间">
          <template slot-scope="scope">
            {{ formatterDate(scope.row.start_day) }}
          </template>
        </el-table-column>
        <el-table-column prop="apply_end_time" :formatter="MixinUnixToDate" label="报名截止时间"/>
        <el-table-column prop="seckill_status_text" label="状态"/>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button
              v-if="scope.row.seckill_status === 'RELEASE'"
              size="mini"
              type="primary"
              @click="handleAuditSeckill(scope.$index, scope.row)"
            >审核商品</el-button>
            <el-button
              v-if="scope.row.seckill_status !== 'EDITING'"
              size="mini"
              type="primary"
              @click="handleViewSeckillGoods(scope.$index, scope.row)"
            >查看商品</el-button>
            <el-button
              v-if="scope.row.seckill_status === 'EDITING'"
              size="mini"
              type="primary"
              @click="handleEditSeckill(scope.$index, scope.row)"
            >编辑</el-button>
            <el-button
              v-if="scope.row.seckill_status === 'EDITING' || scope.row.seckill_status_text === '已关闭'"
              size="mini"
              type="danger"
              @click="handleDeleteSeckill(scope.$index, scope.row)"
            >删除</el-button>
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
    <el-dialog
      title="添加限时抢购"
      :visible.sync="dialogSeckillVisible"
      width="500px"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <el-form :model="seckillForm" :rules="seckillRules" ref="seckillForm" label-width="120px">
        <el-form-item label="活动名称" prop="seckill_name">
          <el-input v-model="seckillForm.seckill_name" :maxlength="20" clearable></el-input>
        </el-form-item>
        <el-form-item label="报名截止时间" prop="apply_end_time">
          <el-date-picker
            v-model="seckillForm.apply_end_time"
            type="datetime"
            placeholder="选择报名截止时间"
            :editable="false"
            :default-time="MixinDefaultTime"
            value-format="timestamp"
            :picker-options="{disabledDate(time) { return time.getTime() < Date.now() - 8.64E7 }}">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="活动时间" prop="start_day">
          <el-date-picker
            v-model="seckillForm.start_day"
            type="date"
            :editable="false"
            :default-time="MixinDefaultTime"
            placeholder="选择活动日期"
            value-format="timestamp"
            :picker-options="{disabledDate(time) { return time.getTime() < Date.now() - 8.64E7 }}">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="抢购阶段" prop="range_list">
          <el-tag
            :key="tag"
            v-for="tag in seckillForm.range_list"
            closable
            :disable-transitions="false"
            class="time-tag"
            @close="() => { seckillForm.range_list.splice(seckillForm.range_list.indexOf(tag), 1) }">
            {{ tag < 10 ? '0' + tag : tag }} : 00
          </el-tag>
          <el-input
            class="input-new-tag"
            v-if="inputVisible"
            v-model.number="inputValue"
            type="number"
            autofocus
            ref="saveTagInput"
            size="small"
            @keyup.enter.native="handleTagInputConfirm"
            @blur="handleTagInputConfirm"
            :min="0" :max="23"
          />
          <el-button v-else class="button-new-tag" size="small" @click="showTagInput">+ 添加时间段</el-button>
        </el-form-item>
        <el-form-item label="申请规则" prop="seckill_rule">
          <el-input
            type="textarea"
            :autosize="{ minRows: 2, maxRows: 4}"
            placeholder="请输入申请规则"
            v-model="seckillForm.seckill_rule"
            :maxlength="200"
            clearable
          ></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogSeckillVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitSeckillForm('release')">立即发布</el-button>
        <el-button type="primary" @click="submitSeckillForm('save')">保 存</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import * as API_Promotion from '@/api/promotion'
  import { Foundation } from '~/ui-utils'

  export default {
    name: 'seckillList',
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
        tableData: '',
        /** 添加、编辑限时抢购 dialog */
        dialogSeckillVisible: false,

        /** 添加、编辑限时抢购 表单 */
        seckillForm: {},

        /** 添加、编辑限时抢购 表单规则 */
        seckillRules: {
          seckill_name: [this.MixinRequired('请输入活动名称！')],
          apply_end_time: [this.MixinRequired('请选择截止报名时间！')],
          start_day: [this.MixinRequired('请选择活动日期！')],
          range_list: [{ type: 'array', required: true, message: '请至少添加一个时间段', trigger: 'change' }],
          seckill_rule: [{ pattern: /^\s\S|.*$/gi, required: true, message: '申请规则不能为空oooo！', trigger: 'blur' }]
        },
        inputVisible: false,
        inputValue: '',

        /** 高级搜索数据 */
        advancedForm: {}
      }
    },
    mounted() {
      this.GET_SeckillList()
    },
    methods: {
      /** 分页大小发生改变 */
      handlePageSizeChange(size) {
        this.params.page_size = size
        this.GET_SeckillList()
      },

      /** 分页页数发生改变 */
      handlePageCurrentChange(page) {
        this.params.page_no = page
        this.GET_SeckillList()
      },

      /** 添加限时抢购 */
      handleAddSeckill() {
        this.seckillForm = { range_list: [] }
        this.dialogSeckillVisible = true
      },

      /** 编辑限时抢购 */
      handleEditSeckill(index, row) {
        this.seckillForm = this.MixinClone(row)
        this.seckillForm.apply_end_time *= 1000
        this.seckillForm.start_day *= 1000
        this.dialogSeckillVisible = true
      },

      /** 审核 */
      handleAuditSeckill(index, row) {
        this.$router.push({ path: `/promotions/seckill-manage/seckill-list/audit/${row.seckill_id}` })
      },

      /** 查看限时抢购商品 */
      handleViewSeckillGoods(index, row) {
        this.$router.push({ path: `/promotions/seckill-manage/seckill-list/pass/${row.seckill_id}` })
      },

      // 显示时间段Tag输入框
      showTagInput() {
        this.inputVisible = true
        this.$nextTick(_ => {
          // 定时器 为了解决火狐浏览器对focus的兼容性问题
          setTimeout(() => this.$refs.saveTagInput.$refs.input.focus(), 50)
        })
      },

      /** 删除限时抢购活动 */
      handleDeleteSeckill(index, row) {
        this.$confirm('确定要删除这个限时抢购活动吗？', '提示', { type: 'warning' }).then(() => {
          API_Promotion.deleteSeckill(row.seckill_id).then(response => {
            this.$message.success('删除成功！')
            this.GET_SeckillList()
          })
        }).catch(() => {})
      },

      // 时间段Tag输入确认事件
      handleTagInputConfirm() {
        let inputValue = this.inputValue
        const { range_list } = this.seckillForm
        if (inputValue !== undefined && inputValue !== '') {
          if (inputValue < 0 || inputValue > 23) {
            this.$message.error('时间段应在0-23之间！')
            this.$refs.saveTagInput.$refs.input.focus()
            return false
          }
          if (inputValue <= range_list[range_list.length - 1]) {
            this.$message.error('不能小于或等于上一时间段！')
            this.$refs.saveTagInput.$refs.input.focus()
            return false
          }
          range_list.push(inputValue)
        }
        this.inputVisible = false
        this.inputValue = ''
      },

      /** 提交限时抢购表单 */
      submitSeckillForm(type) {
        this.$refs['seckillForm'].validate((valid) => {
          if (valid) {
            let { seckill_id } = this.seckillForm
            const params = this.MixinClone(this.seckillForm)
            params.apply_end_time /= 1000
            params.start_day /= 1000
            if (params.apply_end_time >= params.start_day) {
              this.$message.error('报名截止时间不能大于活动开始时间！')
              return
            }
            if (type === 'save') {
              if (!seckill_id) {
                API_Promotion.addSeckill(params).then(response => {
                  this.dialogSeckillVisible = false
                  this.$message.success('添加成功！')
                  this.GET_SeckillList()
                })
              } else {
                API_Promotion.editSeckill(seckill_id, params).then(response => {
                  this.dialogSeckillVisible = false
                  this.$message.success('编辑成功！')
                  this.GET_SeckillList()
                })
              }
            } else {
              if (!seckill_id) seckill_id = 0
              API_Promotion.releaseSeckill(seckill_id, params).then(() => {
                this.dialogSeckillVisible = false
                this.$message.success('发布成功！')
                this.GET_SeckillList()
              })
            }
          } else {
            this.$message.error('表单填写有误，请检查！')
            return false
          }
        })
      },

      /** 格式化时间 */
      formatterDate(date) {
        return Foundation.unixToDate(date, 'yyyy-MM-dd')
      },

      /** 搜索事件触发 */
      searchEvent(data) {
        this.params.page_no = 1
        this.params.seckill_name = data
        if (!data) delete this.params.seckill_name
        this.GET_SeckillList()
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
        this.GET_SeckillList()
      },

      /** 获取限时抢购列表 */
      GET_SeckillList() {
        this.loading = true
        API_Promotion.getSeckillList(this.params).then(response => {
          this.loading = false
          this.tableData = response
        }).catch(() => { this.loading = false })
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
