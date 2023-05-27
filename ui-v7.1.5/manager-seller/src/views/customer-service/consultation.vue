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
                <el-form-item label="回复状态">
                  <el-select v-model="advancedForm.reply_status" placeholder="请选择" clearable>
                    <el-option label="已回复" value="YES"/>
                    <el-option label="未回复" value="NO"/>
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
        <el-table-column prop="create_time" :formatter="MixinUnixToDate" label="咨询日期"/>
        <el-table-column label="商品名称">
          <template slot-scope="scope">
            <a :href="MixinBuyerDomain + '/goods/' + scope.row.goods_id" class="goods-name" target="_blank">{{ scope.row.goods_name }}</a>
          </template>
        </el-table-column>
        <el-table-column prop="member_name" label="咨询会员"/>
        <el-table-column prop="content" label="咨询内容" width="350"/>
        <el-table-column label="回复状态">
          <template slot-scope="scope">{{ scope.row.reply_status | statusFilter }}</template>
        </el-table-column>
        <el-table-column prop="reply_num" label="回复数量"/>
        <el-table-column label="操作" width="240">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="primary"
              @click="handleAskReply(scope.$index, scope.row)"
              v-if="scope.row.reply_status === 'NO'">
              回复
            </el-button>
            <el-button
              size="mini"
              type="primary"
              @click="handleDetailAsk(scope.$index, scope.row)">查看</el-button>
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
    <!--问题咨询 dialog-->
    <el-dialog
      :title="dialogReplyTitle"
      :visible.sync="dialogReplyVisible"
      width="450px"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <el-form :model="goodsReplyForm" :rules="goodsReplyRules" ref="goodsReplyForm">
        <el-form-item prop="reply_content">
          <el-input
            type="textarea"
            :autosize="{ minRows: 3, maxRows: 4}"
            placeholder="请输入您的回复吧~(3-120字之间)"
            :maxlength="120"
            v-model="goodsReplyForm.reply_content">
          </el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogReplyVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitAskReplyForm">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import * as API_consultation from '@/api/consultation'

  export default {
    name: 'consultation',
    data() {
      const checkReply = (rule, value, callback) => {
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
        // 列表数据
        tableData: '',
        // 回复咨询 dialog
        dialogReplyVisible: false,
        // 回复咨询 dialog标题
        dialogReplyTitle: '',
        // 回复信息
        goodsReplyForm: {
          ask_id: '',
          reply_content: ''
        },
        // 问题咨询 表单校验
        goodsReplyRules: {
          reply_content: [
            { required: true, message: '请输入您的回复！', trigger: 'blur' },
            { validator: checkReply, trigger: 'blur' }
          ]
        },
        // 关键字
        keyword: '',
        // 高级搜索
        advancedForm: {}
      }
    },
    mounted() {
      this.GET_ConsultationList()
    },
    filters: {
      statusFilter(val) {
        switch (val) {
          case 'YES': return '已回复'
          case 'NO': return '未回复'
        }
      }
    },
    methods: {
      /** 分页大小发生改变 */
      handlePageSizeChange(size) {
        this.params.page_size = size
        this.GET_ConsultationList()
      },

      /** 分页页数发生改变 */
      handlePageCurrentChange(page) {
        this.params.page_no = page
        this.GET_ConsultationList()
      },

      /** 查看咨询详情和回复内容 */
      handleDetailAsk(index, row) {
        this.$router.push({
          name: 'consultationDetail',
          params: {
            askId: row.ask_id,
            callback: this.GET_ConsultationList
          }
        })
      },

      /** 显示咨询回复dialog */
      handleAskReply(index, row) {
        let content = row.content
        if (row.content.length > 10) {
          content = content.slice(0, 10) + '...'
        }
        this.dialogReplyTitle = '回复咨询 - ' + content
        this.goodsReplyForm = {
          ask_id: row.ask_id,
          reply_content: ''
        }
        this.dialogReplyVisible = true
      },

      /** 咨询回复 表单提交 */
      submitAskReplyForm() {
        this.$refs['goodsReplyForm'].validate((valid) => {
          if (valid) {
            API_consultation.replyConsultation(this.goodsReplyForm.ask_id, this.goodsReplyForm.reply_content).then(response => {
              this.dialogReplyVisible = false
              this.$message.success('操作成功！')
              this.GET_ConsultationList()
            })
          } else {
            this.$message.error('表单填写有误，请核对！')
            return false
          }
        })
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
        this.GET_ConsultationList()
      },

      /** 获取咨询列表*/
      GET_ConsultationList() {
        this.loading = true
        const params = this.MixinClone(this.params)
        if (params.start_time && params.end_time) {
          params.start_time = parseInt(params.start_time / 1000)
          params.end_time = parseInt(params.end_time / 1000)
        }
        API_consultation.getConsultationList(params).then(response => {
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
