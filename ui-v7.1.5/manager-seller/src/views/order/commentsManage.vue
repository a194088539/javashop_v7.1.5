<template>
  <div>
    <el-tabs v-model="activeName" @tab-click="handleScoreClick">
      <el-tab-pane label="评价列表" name="commentlist">
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
                    <el-form-item label="回复状态">
                      <el-select v-model="advancedForm.reply_status" placeholder="请选择" clearable>
                        <el-option label="未回复" value="0"/>
                        <el-option label="已回复" value="1"/>
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
            <el-table-column prop="create_time" :formatter="MixinUnixToDate" label="评论日期" width="150"/>
            <el-table-column label="商品名称" width="350">
              <template slot-scope="scope">
                <a :href="MixinBuyerDomain + '/goods/' + scope.row.goods_id" class="goods-name" target="_blank" v-html="scope.row.goods_name">{{ scope.row.goods_name }}</a>
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
            <el-table-column label="回复状态" width="80">
              <template slot-scope="scope">
                <span>{{scope.row.reply_status | replyStatusFilter}}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="240">
              <template slot-scope="scope">
                <el-button
                  v-if="scope.row.reply_status === 0"
                  size="mini"
                  type="primary"
                  @click="handleReplyComment(scope.$index, scope.row)">回复
                </el-button>
                <el-button
                  size="mini"
                  type="primary"
                  @click="handleViewComments(scope.$index, scope.row)">查看
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
        <!--回复商品评论 dialog-->
        <el-dialog
          :title="replyTitle"
          :visible.sync="replyVisible"
          width="30%"
          :close-on-click-modal="false"
          :close-on-press-escape="false"
        >
          <el-form :model="replyForm" :rules="replyRules" ref="replyForm">
            <el-form-item label="回复内容" label-width="120px" prop="reply_content">
              <el-input type="textarea" rows="5" :maxlength="120" v-model="replyForm.reply_content" auto-complete="off"></el-input>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="replyVisible = false">取 消</el-button>
            <el-button type="primary" @click="saveCommentReply('replyForm')">确 定</el-button>
          </div>
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
      </el-tab-pane>
      <el-tab-pane label="店铺评分" name="score" class="shop-score-container">
        <div class="shop-score">
          <div class="score-title">宝贝与描述相符</div>
          <div class="score-pic">
            <dl class="score-pic-content">
              <dt>
                <em :style="socreDescription">{{ shopInfo.shop_description_credit | socre }}</em>
              </dt>
              <dd>非常不满</dd>
              <dd>不满意</dd>
              <dd>一般</dd>
              <dd>满意</dd>
              <dd>非常满意</dd>
            </dl>
          </div>
        </div>
        <div class="shop-score">
          <div class="score-title">卖家的服务态度</div>
          <div class="score-pic">
            <dl class="score-pic-content">
              <dt>
                <em :style="socreService">{{ shopInfo.shop_service_credit | socre }}</em>
              </dt>
              <dd>非常不满</dd>
              <dd>不满意</dd>
              <dd>一般</dd>
              <dd>满意</dd>
              <dd>非常满意</dd>
            </dl>
          </div>
        </div>
        <div class="shop-score">
          <div class="score-title">卖家的发货速度</div>
          <div class="score-pic">
            <dl class="score-pic-content">
              <dt>
                <em :style="socreDelivery">{{ shopInfo.shop_delivery_credit | socre  }}</em>
              </dt>
              <dd>非常不满</dd>
              <dd>不满意</dd>
              <dd>一般</dd>
              <dd>满意</dd>
              <dd>非常满意</dd>
            </dl>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
  import * as API_comment from '@/api/comment'
  import { CategoryPicker } from '@/components'

  export default {
    name: 'commentsManage',
    data() {
      const checkReply = (rule, value, callback) => {
        if (!value) {
          callback(new Error('回复内容不可为空'))
        } else if (this.MixinEmojiCharacter(value)) {
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
        // 审核dialog标题
        replyTitle: '',
        // 审核dialog是否显示
        replyVisible: false,
        // 审核dialog信息
        replyForm: {
          comment_id: '',
          reply_content: ''
        },
        // 检验工具
        replyRules: {
          reply_content: [
            { required: true, message: '请输入回复内容', trigger: 'blur' },
            { max: 120, message: '回复内容最多120个字', trigger: 'blur' },
            { validator: checkReply, trigger: 'blur' }
          ]
        },
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
        // 当前面板
        activeName: 'commentlist',
        // 店铺信息
        shopInfo: this.$store.getters.shopInfo
      }
    },
    mounted() {
      this.GET_CommentList()
    },
    computed: {
      socreDescription() {
        return {
          'left': this.shopInfo.shop_description_credit
            ? parseFloat((this.shopInfo.shop_description_credit / 5).toFixed(2) * 100 - 7.5) + '%' : 0
        }
      },
      socreService() {
        return {
          'left': this.shopInfo.shop_service_credit
            ? parseFloat((this.shopInfo.shop_service_credit / 5).toFixed(2) * 100 - 7.5) + '%' : 0
        }
      },
      socreDelivery() {
        return {
          'left': this.shopInfo.shop_delivery_credit
            ? parseFloat((this.shopInfo.shop_delivery_credit / 5).toFixed(2) * 100 - 7.5) + '%' : 0
        }
      }
    },
    filters: {
      socre(val) {
        return val.toFixed(2)
      },
      gradeFilter(val) {
        switch (val) {
          case 'bad': return '差评'
          case 'neutral': return '中评'
          default: return '好评'
        }
      },
      replyStatusFilter(val) {
        switch (val) {
          case 0: return '未回复'
          case 1: return '已回复'
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

      /** 切换面板 */
      handleScoreClick(tab) {
        this.activeName = tab.name
        if (this.activeName === 'commentlist') {
          this.GET_CommmentsList()
        }
      },

      /** 回复评论 */
      handleReplyComment(index, row) {
        let content = row.content
        if (row.content.length > 10) {
          content = content.slice(0, 10) + '...'
        }
        this.replyTitle = '评论回复 - ' + content
        this.replyForm = {
          comment_id: row.comment_id,
          reply_content: ''
        }
        this.replyVisible = true
      },

      /** 保存评论回复*/
      saveCommentReply(FormName) {
        this.$refs[FormName].validate((valid) => {
          if (valid) {
            API_comment.replyComment(this.replyForm.comment_id, { reply: this.replyForm.reply_content }).then(() => {
              this.replyVisible = false
              this.GET_CommentList()
              this.$message.success('保存成功')
            })
          }
        })
      },

      /** 查看评论详情 */
      handleViewComments(index, row) {
        API_comment.getCommentDetail(row.comment_id).then(response => {
          let content = row.content
          if (row.content.length > 15) {
            content = content.slice(0, 15) + '...'
          }
          this.commentDetailForm = response
          this.commentDetailTitle = '评论详情 - ' + content
          this.commentDetailVisible = true
        })
      },

      /** 获取评论列表 */
      GET_CommentList() {
        this.loading = true
        const params = this.MixinClone(this.params)
        if (params.start_time && params.end_time) {
          params.start_time = parseInt(params.start_time / 1000)
          params.end_time = parseInt(params.end_time / 1000)
        }
        API_comment.getCommentList(params).then(response => {
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
  /** 店铺评分 */
  .shop-score-container {
    background: #fff;
    border: 1px solid #ddd;
    padding: 10px;
  }
  .shop-score {
    width: 60%;
    display: flex;
    flex-direction: row;
    flex-wrap: nowrap;
    justify-content: space-around;
    align-items: center;
    margin: 50px 0;
    padding: 15px;
    div.score-title {

    }
    /** 评分图标标识 */
    div.score-pic {
      .score-pic-content {
        display: block;
        margin: 0 auto;
        padding: 0;
        width: 352px;
        height: 20px;
        dt {
          height: 20px;
          position: relative;
          background: url(./images/rate_column.gif) no-repeat 0px -88px;
        }
        em {
          position: absolute;
          background: url(./images/rate_column.gif) no-repeat 0 0;
          top: -26px;
          color: #fff;
          display: block;
          font-weight: 600;
          height: 30px;
          line-height: 16px;
          padding: 0 0 7px;
          text-align: center;
          width: 37px;
          font-size: 14px;
        }
      }
      dd {
        width: 65px;
        display: inline-block;
        margin-left: 0px;
        font-size: 14px;
      }
    }
  }
</style>
