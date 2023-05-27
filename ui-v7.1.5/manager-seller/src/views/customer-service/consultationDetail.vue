<template>
  <div>
    <div class="detail-info">
      <el-collapse style="margin: 0px 0px 20px 10px;">
        <el-collapse-item>
          <template slot="title">
            商品咨询详情<small style="color: #b5b5b5;">--点击查看</small>
          </template>
          <div class="item clearfix">
            <div style="width: 20%;float: left">
              <a :href="MixinBuyerDomain + '/goods/' + goodsAsk.goods_id" target="_blank" class="item-img">
                <img :src="goodsAsk.goods_img" title="goodsAsk.goods_name" width="50" height="50"/>
              </a>
              <div class="cont">
                <div class="tit clearfix">
                  <a :href="MixinBuyerDomain + '/goods/' + goodsAsk.goods_id" class="goods-name" target="_blank">{{ goodsAsk.goods_name }}</a>
                </div>
              </div>
            </div>
            <div class="cont ask-content">
              <div class="tit clearfix" style="padding-left: 20px;padding-bottom: 15px;">
                <strong>回复状态：</strong>
                <span style="color:red;">{{ goodsAsk.reply_status | statusFilter }}</span>
              </div>
            </div>
          </div>
          <div class="item clearfix">
            <div class="cont ask-base">
              <div class="tit clearfix">
                <strong>咨询日期：</strong>{{goodsAsk['create_time'] | unixToDate}}
              </div>
              <div class="tit clearfix">
                <strong>咨询人：</strong>{{goodsAsk.member_name}}
              </div>
            </div>
            <div class="cont ask-content">
              <div class="tit clearfix" style="padding-left: 20px;">
                <strong>咨询内容：</strong>
              </div>
              <div class="tit clearfix" style="padding-left: 20px;">
                {{goodsAsk.content}}
              </div>
            </div>
          </div>
          <div class="item clearfix" v-if="goodsAsk.reply_status === 'YES'">
            <div class="cont ask-base">
              <div class="tit clearfix">
                <strong>商家回复日期：</strong>{{goodsAsk['reply_time'] | unixToDate}}
              </div>
            </div>
            <div class="cont ask-content">
              <div class="tit clearfix" style="padding-left: 20px;">
                <strong>商家回复内容：</strong>
              </div>
              <div class="tit clearfix" style="padding-left: 20px;">
                {{goodsAsk.reply}}
              </div>
            </div>
          </div>
        </el-collapse-item>
      </el-collapse>
    </div>

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
                <el-form-item label="回复人">
                  <el-input size="medium" v-model="advancedForm.member_name" clearable></el-input>
                </el-form-item>
                <el-form-item label="回复内容">
                  <el-input v-model="advancedForm.content" clearable></el-input>
                </el-form-item>
                <el-form-item label="回复日期">
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
                    :picker-options="{ disabledDate (time) { return time.getTime() - 86400000 - 1 >= new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDay()).getTime() + 86400000 - 1 }, shortcuts: MixinPickerShortcuts }">
                  </el-date-picker>
                </el-form-item>
              </el-form>
            </template>
          </en-table-search>
        </div>
      </div>
      <template slot="table-columns">
        <el-table-column prop="reply_time" :formatter="MixinUnixToDate" label="回复日期"/>
        <el-table-column prop="content" label="回复内容" width="350"/>
        <el-table-column prop="member_name" label="回复人"/>
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
  </div>
</template>

<script>
  import * as API_consultation from '@/api/consultation'
  import { Foundation } from '~/ui-utils'

  export default {
    name: 'consultationDetail',
    data() {
      return {
        askId: this.$route.params.askId,
        // 列表loading状态
        loading: false,
        // 列表参数
        params: {
          page_no: 1,
          page_size: 10,
          ask_id: this.$route.params.askId
        },
        // 列表数据
        tableData: '',
        // 关键字
        keyword: '',
        // 高级搜索
        advancedForm: {},
        // 咨询详情
        goodsAsk: ''
      }
    },
    mounted() {
      this.GET_AskReplyList()
      this.GET_GoodsAskDetail()
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
        this.GET_AskReplyList()
      },

      /** 分页页数发生改变 */
      handlePageCurrentChange(page) {
        this.params.page_no = page
        this.GET_AskReplyList()
      },

      /** 搜索事件触发 */
      searchEvent(keyword) {
        this.params.keyword = keyword
        Object.keys(this.advancedForm).forEach(key => delete this.params[key])
        this.params.page_no = 1
        this.GET_AskReplyList()
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
        this.GET_AskReplyList()
      },

      /** 获取咨询回复列表 */
      GET_AskReplyList() {
        this.loading = true
        const params = this.MixinClone(this.params)
        if (params.start_time && params.end_time) {
          params.start_time = parseInt(params.start_time / 1000)
          params.end_time = parseInt(params.end_time / 1000)
        }
        API_consultation.getMemberAsksReply(params).then(response => {
          this.loading = false
          this.tableData = response
        }).catch(() => { this.loading = false })
      },

      /** 获取商品咨询详情 */
      GET_GoodsAskDetail() {
        API_consultation.getMemberAsksDetail(this.askId).then(response => {
          this.goodsAsk = response
        })
      }
    }
  }
</script>
<style type="text/scss" lang="scss" scoped>
  .detail-info {
    background-color: white;
  }
  .detail-info .item {
    padding: 10px 0;
    border-bottom: 1px solid #e5e5e5;
  }
  .detail-info .item .item-img {
    display: block;
    float: left;
    width: 50px;
    height: 50px;
    margin: 0 15px 0 0;
  }
  .detail-info .item .cont {
    overflow: hidden;
    zoom: 1;
  }
  .detail-info .item .ask-base {
    width: 20%;
    float: left;
  }
  .detail-info .item .ask-content {
    width: 80%;
    float: left;
    border-left: 1px solid #e8e8e8;
  }
  .detail-info .item .cont .tit {
    padding-top: 10px;
  }
  .detail-info .item .cont .tit .goods-name {
    color: #4183c4;
    &:hover { color: #f42424 }
    max-width: 746px;
    display: inline-block;
    *display: inline;
    *zoom: 1;
    text-overflow: ellipsis;
    -o-text-overflow: ellipsis;
    white-space: nowrap;
    overflow: hidden;
    font-weight: 700;
    position: relative;
    top: 5px;
  }
</style>
