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
            @click="handleCommentBatchAudit">批量审核</el-button>
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
                <el-form-item label="评论内容">
                  <el-input size="medium" v-model="advancedForm.content" clearable></el-input>
                </el-form-item>
                <el-form-item label="商品名称">
                  <el-input size="medium" v-model="advancedForm.goods_name" clearable></el-input>
                </el-form-item>
                <el-form-item label="评论人">
                  <el-input size="medium" v-model="advancedForm.member_name" clearable></el-input>
                </el-form-item>
                <el-form-item label="评价等级">
                  <el-select v-model="advancedForm.grade" placeholder="请选择" clearable>
                    <el-option label="好评" value="good"/>
                    <el-option label="中评" value="neutral"/>
                    <el-option label="差评" value="bad"/>
                  </el-select>
                </el-form-item>
                <el-form-item label="评论类型">
                  <el-select v-model="advancedForm.comments_type" placeholder="请选择" clearable>
                    <el-option label="初评" value="INITIAL"/>
                    <el-option label="追评" value="ADDITIONAL"/>
                  </el-select>
                </el-form-item>
                <el-form-item label="是否有图">
                  <el-select v-model="advancedForm.have_image" placeholder="请选择" clearable>
                    <el-option label="无图" value="0"/>
                    <el-option label="有图" value="1"/>
                  </el-select>
                </el-form-item>
                <el-form-item label="审核状态">
                  <el-select v-model="advancedForm.audit_status" placeholder="请选择" clearable>
                    <el-option label="待审核" value="WAIT_AUDIT"/>
                    <el-option label="审核通过" value="PASS_AUDIT"/>
                    <el-option label="审核拒绝" value="REFUSE_AUDIT"/>
                  </el-select>
                </el-form-item>
                <el-form-item label="评论日期">
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
        <el-table-column prop="create_time" :formatter="MixinUnixToDate" label="评论日期" width="150"/>
        <el-table-column label="商品名称" width="350">
          <template slot-scope="scope">
            <a :href="MixinBuyerDomain + '/goods/' + scope.row.goods_id" class="goods-name" target="_blank">{{ scope.row.goods_name }}</a>
          </template>
        </el-table-column>
        <el-table-column prop="member_name" label="评论人" width="200"/>
        <el-table-column prop="content" label="评论内容">
          <template slot-scope="scope">
            <span>{{scope.row.content}}</span>
            <div class="comment-list-img" v-if="scope.row.have_image === 1">
              <div class="div-list-img" v-for="(img, ind) in scope.row.images" :key="ind+scope.row.comment_id+'A'">
                <a :href="img" target="_blank">
                  <img :src="img" width="50" height="50">
                </a>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="评价等级" width="80">
          <template slot-scope="scope">{{ scope.row.grade | gradeFilter }}</template>
        </el-table-column>
        <el-table-column label="评论类型" width="80">
          <template slot-scope="scope">{{ scope.row.comments_type | typeFilter }}</template>
        </el-table-column>
        <el-table-column label="审核状态" width="80">
           <template slot-scope="scope">
             <span>{{scope.row.audit_status | auditStatusFilter}}</span>
           </template>
        </el-table-column>
        <el-table-column label="操作" width="240">
          <template slot-scope="scope">
            <el-button
              v-if="scope.row.audit_status === 'WAIT_AUDIT'"
              size="mini"
              type="primary"
              @click="primaryCommentExamineBtn(scope.$index, scope.row)">审核
            </el-button>
            <el-button
              size="mini"
              type="primary"
              @click="handleViewComments(scope.$index, scope.row)">查看
            </el-button>
            <el-button
              size="mini"
              type="danger"
              @click="handleDeleteComments(scope.$index, scope.row)">删除
            </el-button>
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
      :title="auditTitle"
      :visible.sync="auditVisible"
      width="400px"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <el-form :model="auditForm" ref="auditForm" label-width="100px">
        <!--是否通过=-->
        <el-form-item label="是否通过" prop="auth_status">
          <el-radio-group v-model="auditForm.auth_status">
            <el-radio v-model="auditForm.auth_status" label="PASS_AUDIT">通过</el-radio>
            <el-radio v-model="auditForm.auth_status" label="REFUSE_AUDIT">不通过</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="auditVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitBatchExamine" v-if="auditType">确 定</el-button>
        <el-button type="primary" @click="submitExamine" v-if="!auditType">确 定</el-button>
      </span>
    </el-dialog>
    <!--单查看详情dialog-->
    <el-dialog
      :title="commentDetailTitle"
      :visible.sync="commentDetailVisible"
      width="35%">
      <el-form :model="commentDetailForm" label-position="right">
        <template>
          <div class="goods-info">
            <div class="goods-img">
              <a :href="MixinBuyerDomain + '/goods/' + commentDetailForm.goods_id" target="_blank">
                <img :src="commentDetailForm.goods_img" width="60" height="60">
              </a>
            </div>
            <div class="goods-name">
              <a :href="MixinBuyerDomain + '/goods/' + commentDetailForm.goods_id" class="goods-name" target="_blank" style="margin-left: 10px;">{{commentDetailForm.goods_name}}</a>
            </div>
          </div>
          <div class="shop-grade" v-if="commentDetailForm.member_shop_score">
            <ul style="list-style: none;padding: 0px;">
              <li class="shop-grade-item">描述相符：<el-rate v-model="commentDetailForm.member_shop_score.description_score" score-template="{value}分" text-color="#666" show-score disabled></el-rate></li>
              <li class="shop-grade-item">服务态度：<el-rate v-model="commentDetailForm.member_shop_score.service_score" score-template="{value}分" text-color="#666" show-score disabled></el-rate></li>
              <li class="shop-grade-item">物流服务：<el-rate v-model="commentDetailForm.member_shop_score.delivery_score" score-template="{value}分" text-color="#666" show-score disabled></el-rate></li>
            </ul>
          </div>
          <div class="comment-info">
            <div style="padding: 10px 0px;">
              <span><strong>评论人：</strong>{{commentDetailForm.member_name}}</span>
              <span style="float: right"><strong>评论日期：</strong>{{commentDetailForm.create_time | unixToDate}}</span>
            </div>
            <div class="comment-content"><strong>评论内容：</strong>{{ commentDetailForm.content }}</div>
            <div class="comment-img" v-if="commentDetailForm.have_image === 1">
              <div class="div-img" v-for="(fImage, fIndex) in commentDetailForm.images" :key="fIndex+commentDetailForm.comment_id+'B'">
                <a :href="fImage" target="_blank">
                  <img :src="fImage" width="60" height="60">
                </a>
              </div>
            </div>
            <div class="comment-content" v-if="commentDetailForm.reply_status === 1">
              <div style="font-weight: bolder;">商家于{{commentDetailForm.reply.create_time | unixToDate}}对评论进行了回复：</div>
              <div style="margin-top: 5px;">{{ commentDetailForm.reply.content }}</div>
            </div>
          </div>
        </template>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
  import * as API_Member from '@/api/member'

  export default {
    name: 'goodsCommentList',
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
        // 审核dialog标题
        auditTitle: '',
        // 审核dialog是否显示
        auditVisible: false,
        // 审核dialog信息
        auditForm: {
          ids: [],
          auth_status: 'PASS_AUDIT'
        },
        // 是否为批量审核
        auditType: true,
        // 查看评论dialog标题
        commentDetailTitle: '',
        // 查看评论dialog是否显示
        commentDetailVisible: false,
        // 查看评论dialog显示的信息
        commentDetailForm: {},
        // 关键字
        keyword: '',
        // 高级搜索
        advancedForm: {},
        // 当前已选择的行
        multipleSelection: []
      }
    },
    mounted() {
      this.GET_CommentList()
    },
    filters: {
      gradeFilter(val) {
        switch (val) {
          case 'bad': return '差评'
          case 'neutral': return '中评'
          default: return '好评'
        }
      },
      auditStatusFilter(val) {
        switch (val) {
          case 'WAIT_AUDIT': return '待审核'
          case 'PASS_AUDIT': return '审核通过'
          case 'REFUSE_AUDIT': return '审核未通过'
        }
      },
      typeFilter(val) {
        switch (val) {
          case 'INITIAL': return '初评'
          case 'ADDITIONAL': return '追评'
        }
      }
    },
    methods: {
      /** 分页大小发生改变 */
      handlePageSizeChange(size) {
        this.params.page_size = size
        this.GET_CommentList()
      },

      /** 分页页数发生改变 */
      handlePageCurrentChange(page) {
        this.params.page_no = page
        this.GET_CommentList()
      },

      /** 搜索事件触发 */
      searchEvent(keyword) {
        this.params.keyword = keyword
        Object.keys(this.advancedForm).forEach(key => delete this.params[key])
        this.params.page_no = 1
        this.GET_CommentList()
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
        this.GET_CommentList()
      },

      // 单选或者全选
      handleSelectionChange(val) {
        this.multipleSelection = val
      },
      // 判断 当前行是都可以选中
      isSelectRow(row, index) {
        return row.audit_status === 'WAIT_AUDIT' ? 1 : 0
      },
      setRowKey(row) {
        return row.create_time
      },

      /** 获取评论审核详情 */
      primaryCommentExamineBtn(index, row) {
        this.auditType = false
        let content = row.content
        if (row.content.length > 10) {
          content = content.slice(0, 10) + '...'
        }
        this.auditTitle = '评论审核 - ' + content
        this.auditForm = {
          ids: [row.comment_id],
          auth_status: 'PASS_AUDIT'
        }
        this.auditVisible = true
      },

      /** 提交评论审核 */
      submitExamine() {
        API_Member.batchAuditComment(this.auditForm).then(response => {
          this.$message.success('审核成功！')
          this.$refs.tableContainer && this.$refs.tableContainer.$refs && this.$refs.tableContainer.$refs.table && this.$refs.tableContainer.$refs.table.clearSelection()
          this.auditVisible = false
          this.GET_CommentList()
        }).catch(() => {})
      },

      /** 批量审核 */
      handleCommentBatchAudit() {
        this.auditType = true
        if (!this.multipleSelection.length) {
          return this.$message.error('请选择要审核的商品评论！')
        }
        this.auditTitle = '批量审核'
        this.auditVisible = true
      },

      /** 批量审核商品评论 表单提交 */
      async submitBatchExamine() {
        let params = JSON.parse(JSON.stringify(this.auditForm))
        try {
          params.ids = this.multipleSelection.map(item => item.comment_id)
          await API_Member.batchAuditComment(params)
          await this.GET_CommentList()
          this.params.page_no = 1
          this.$refs.tableContainer && this.$refs.tableContainer.$refs && this.$refs.tableContainer.$refs.table && this.$refs.tableContainer.$refs.table.clearSelection()
          this.auditVisible = false
          this.$message.success('操作成功！')
        } catch (e) {
          this.$message.error('操作失败，请重试！')
        }
      },

      /** 查看评论详情 */
      handleViewComments(index, row) {
        API_Member.getMemberCommentDetail(row.comment_id).then(response => {
          let content = row.content
          if (row.content.length > 15) {
            content = content.slice(0, 15) + '...'
          }
          this.commentDetailForm = response
          this.commentDetailTitle = '评论详情 - ' + content
          this.commentDetailVisible = true
        })
      },

      /** 删除评论 */
      handleDeleteComments(index, row) {
        this.$confirm('确定要删除这条评论吗？', '提示', { type: 'warning' }).then(() => {
          API_Member.deleteMemberComments(row.comment_id).then(() => {
            this.$message.success('删除成功！')
            this.GET_CommentList()
          })
        }).catch(() => {})
      },

      /** 获取评论列表 */
      GET_CommentList() {
        this.loading = true
        const params = this.MixinClone(this.params)
        if (params.start_time && params.end_time) {
          params.start_time = parseInt(params.start_time / 1000)
          params.end_time = parseInt(params.end_time / 1000)
        }
        API_Member.getMemberComments(params).then(response => {
          this.loading = false
          this.tableData = response
        }).catch(() => { this.loading = false })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .comment-list-img .div-list-img {
    float: left;
    width: 52px;
    height: 52px;
    border: 1px solid #efefef;
    margin: 0 5px;
  }
  .goods-info {
    height: 70px;
    border-bottom: 1px solid #efebeb;
  }
  .goods-info .goods-img {
    float: left;
  }
  .goods-name {
    color: #4183c4;
    &:hover { color: #f42424 }
  }
  .comment-info {
    margin-top: 10px;
    border-bottom: 1px solid #efebeb;
  }
  .comment-info .comment-content {
    margin: 10px 0px;
    word-wrap: break-word;
  }
  .comment-info .comment-img {
    margin-top: 10px;
    height: 70px;
  }
  .comment-info .comment-img .div-img {
    float: left;
    width: 62px;
    height: 62px;
    border: 1px solid #efefef;
    margin: 0 5px;
  }
  .comment-audit {
    margin-top: 10px;
  }
  .shop-grade {
    overflow: hidden;
    border-bottom: 1px solid #efefef;
    .shop-grade-item {
      .el-rate {
        position: relative;
        top: -2px;
        display: inline-block;
        .el-rate__text {
          font-size: 12px;
        }
      }
    }
  }
  span{
    word-wrap:break-word;
  }
</style>
