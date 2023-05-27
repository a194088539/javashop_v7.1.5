<template>
  <div id="ask-detail" style="background-color: #f9f9f9;">
    <div class="w">
      <div class="breadcrumb">
        <a href="/member">会员中心</a>
        &nbsp;&gt;&nbsp;
        <a href="/member/my-consultation">购买咨询</a>
        &nbsp;&gt;&nbsp;
        <a href="javascript:">
          {{askDetail.content}}
        </a>
      </div>
      <div class="g-a1">
        <div class="m" id="pinfo">
          <div class="mt">
            <h2>商品信息</h2>
          </div>
          <b></b>
          <div class="mc">
            <div class="p-img">
              <a :href="'/goods/' + askDetail.goods_id" target="_blank">
                <img alt="" :src="askDetail.goods_img" :title="askDetail.goods_name" width="130" height="130">
              </a>
            </div>
            <div class="p-name">
              商品名称：<a :href="'/goods/' + askDetail.goods_id" target="_blank">{{ askDetail.goods_name }}</a>
            </div>
            <div class="p-price">
              销售价：<strong>￥{{ askDetail.goods_price | unitPrice }}</strong>
            </div>
            <div class="p-price">
              好评率：<span>{{ askDetail.praise_rate }}%</span>
            </div>
            <div class="p-price">
              评论数：<span>{{ askDetail.comment_num }}条</span>
            </div>
          </div>
        </div>
      </div>
      <div class="main">
        <div class="details">
          <div class="ask details-ask">
            <div class="clearfix"  style="display: block;">
              <div class="def">
                <span class="name">{{ askDetail.member_name }}</span>
                <span class="time">{{ askDetail.create_time | unixToDate }}</span>
              </div>
              <span class="icon icon_ask">问</span>
              <span class="tt">{{askDetail.content}}</span>
            </div>
            <div class="tic clearfix">
              <el-form :model="replyForm" ref="replyForm">
                <div class="reply-textarea J-reply-con">
                  <div class="inner">
                    <div class="wrap-textarea">
                      <textarea class="reply-input f-textarea" v-model="replyForm.reply" maxlength="120" placeholder="热心肠的你帮TA解答下吧~"></textarea>
                    </div>
                    <span class="cursor"></span>
                  </div>
                  <div class="operate-box">
                  <span class="checkbox">
                    <input class="check" type="checkbox" v-model="replyForm.checked">匿名回答
                  </span>
                    <a class="btn_aq" href="javascript:;" target="_self" @click="submitReplyForm">发布</a>
                  </div>
                </div>
              </el-form>
            </div>
          </div>

          <div class="answer" v-if="askDetail.reply_num != 0">
            <div class="title">
              <span class="icon icon_answer">答</span>已有{{ askDetail.reply_num }}人回答
            </div>
            <div class="item" v-if="askDetail.reply_status === 'YES'">
              <div class="def">
                <span class="name">商家回复</span>
                <span class="time">{{ askDetail.reply_time | unixToDate }}</span>
              </div>
              <div class="txt"><span class="seller-answer">商</span>{{ askDetail.reply }}</div>
            </div>
            <div class="item" v-for="reply in replyList.data" :key="reply.id">
              <div class="def">
                <span class="name">{{ reply.member_name }}</span>
                <span class="time">{{ reply.reply_time | unixToDate }}</span>
              </div>
              <div class="txt">{{ reply.content }}</div>
            </div>
          </div>
          <div class="answer" v-if="askDetail.reply_num === 0">
            <div class="title">
              <span class="icon icon_answer">答</span>已有0人回答
            </div>
            <div class="item">
              <div class="tic">
                <div class="blank">
                  还没有用户回答过此问题
                </div>
              </div>
            </div>
          </div>
          <div class="member-pagination" v-if="replyList.data_total > 10">
            <el-pagination
              @current-change="handleCurrentPageChange"
              :current-page.sync="params.page_no"
              :page-size="params.page_size"
              layout="total, prev, pager, next"
              :total="replyList.data_total">
            </el-pagination>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import * as API_Members from '@/api/members'
  import Vue from 'vue'
  import { Form } from 'element-ui'
  Vue.use(Form);
  export default {
    name: "reply-answer",
    data() {
      return {
        /** 咨询详情信息 */
        askDetail:'',
        ask_id: this.$route.query.ask_id,
        goods_id: this.$route.query.goods_id,

        params: {
          page_no: 1,
          page_size: 10
        },

        replyList: '',

        replyForm: {
          reply: '',
          checked: true,
          anonymous: 'YES'
        }
      }
    },
    mounted() {
      this.GET_AskDetail()
      this.GET_AskReplyList()
    },
    methods: {
      /** 分页大小发生改变 */
      handlePageSizeChange(size) {
        this.params.page_size = size;
        this.GET_AskReplyList()
      },

      /** 当页数发生改变时 */
      handleCurrentPageChange(page_no) {
        this.params.page_no = page_no;
        this.GET_AskReplyList()
      },

      submitReplyForm() {
        this.replyForm.anonymous = this.replyForm.checked ? 'YES' : 'NO'
        API_Members.replyAsk(this.ask_id, this.replyForm.reply, this.replyForm.anonymous).then(() => {
          this.$message.success('发布成功')
          this.$router.replace({path: '/ask-detail', query: { ask_id: this.ask_id, goods_id: this.goods_id}})
        })
      },

      /** 获取咨询详情 */
      GET_AskDetail() {
        API_Members.getAskDetail(this.ask_id).then(response => {
          this.askDetail = response
        })
      },

      /** 获取咨询回复列表 */
      GET_AskReplyList() {
        API_Members.getGoodsAskReplys(this.ask_id, this.params).then(response => {
          this.replyList = response
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../assets/styles/color";
  .m, .mb, .mc, .mt, .sm, .smb, .smc, .smt {
    overflow: hidden;
    zoom: 1;
  }
  .p-detail, .p-img, .p-market, .p-name, .p-price {
    overflow: hidden;
  }
  #pinfo .btn, #pinfo .num-comment, #pinfo .p-grade, #pinfo .p-price {
    margin-top: 8px;
  }
  .fl {
    float: left;
  }
  .w {
    width: 1210px;
  }
  .breadcrumb {
    line-height: 48px;
    font-family: "宋体";
  }
  .g-a1 {
    width: 210px;
    margin-left: 10px;
    float: right;
  }
  #pinfo {
    margin-bottom: 20px;
    background-color: #fff;
  }
  #pinfo .mt {
    height: 30px;
    border: 1px solid #f2f2f2;
    background-color: #fff;
    border-bottom: 1px solid #f1f1f1;
  }
  #pinfo h2 {
    height: 30px;
    line-height: 30px;
    color: #333;
    padding-left: 10px;
    font-family: \5fae\8f6f\96c5\9ed1;
    font-weight: 400;
    font-size: 14px;
  }
  #pinfo .mc {
    padding: 10px;
    border: solid #f2f2f2;
    border-width: 0 1px 1px;
  }
  #pinfo .p-img {
    padding: 10px 0 20px;
    text-align: center;
  }
  #pinfo .p-name {
    height: auto;
  }
  #pinfo .p-name a {
    color: #005aa0;
  }
  #pinfo .p-price {
    color: #333;
  }
  #pinfo .star {
    float: left;
    margin-top: 2px;
  }
  .main {
    overflow: hidden;
    zoom: 1;
  }
  .details .ask {
    padding: 11px 20px 10px;
    font-weight: 700;
    background-color: #fff;
    line-height: 42px;
    margin-bottom: 10px;
  }
  .details .ask .def {
    float: right;
    color: #999;
    font-family: Verdana;
    font-weight: 400;
  }
  .details .ask .def .name {
    margin-right: 10px;
    display: inline-block;
  }
  .details .ask .icon_ask {
    width: 18px;
    height: 18px;
    color: #fff;
    float: left;
    line-height: 18px;
    margin-right: 5px;
    margin-top: 12px;
    display: inline-block;
    background-color: #f91;
    border-radius: 9px;
    text-align: center;
    font-weight: 400;
  }
  .details .ask .tt {
    width: 680px;
    margin: 10px 0;
    max-height: 44px;
    overflow: hidden;
    line-height: 22px;
    display: block;
    float: left;
    font-weight: 700;
    color: #666;
  }
  .details .answer {
    padding: 0 20px 20px;
    background-color: #fff;
  }
  .details .answer .title {
    line-height: 60px;
    font-weight: 700;
  }
  .details .answer .title .icon_answer {
    width: 18px;
    height: 18px;
    margin-right: 5px;
    color: #fff;
    line-height: 18px;
    display: inline-block;
    background-color: #9b1;
    border-radius: 9px;
    text-align: center;
  }
  .details .answer .item {
    border-top: 1px solid #f5f5f5;
    padding: 16px 0;
    line-height: 24px;
  }
  .details .answer .item .def {
    float: right;
    color: #999;
    font-family: Verdana;
    font-weight: 400;
  }
  .details .answer .item .def .name {
    margin-right: 10px;
    display: inline-block;
  }
  .details .answer .item .txt {
    overflow: hidden;
    zoom: 1;
    padding-right: 50px;
  }
  .details .answer .blank {
    margin-top: 30px;
    padding-bottom: 35px;
    text-align: center;
  }
  .details .details-ask .tic {
    clear: both;
    line-height: 27px;
    font-weight: 400;
    width: 100%;
  }
  .details .details-ask .tic .reply-textarea {
    padding-top: 13px;
    overflow: hidden;
  }
  .details .details-ask .tic .reply-textarea .inner {
    border: 1px solid #ececec;
    background-color: #fff;
    position: relative;
  }
  .details .details-ask .tic .reply-textarea .inner .wrap-textarea {
    margin: 10px 10px 2px;
  }
  .details .details-ask .tic .reply-textarea .reply-input {
    display: block;
    width: 100%;
    height: 80px;
    border: none;
    line-height: 20px;
    font-size: 12px;
    resize: none;
  }
  .details .details-ask .tic .reply-textarea .cursor {
    width: 15px;
    height: 8px;
    display: block;
    position: absolute;
    top: -8px;
    left: 42px;
    background: url(../assets/images/cursor.gif) no-repeat 0 0;
  }
  .details .details-ask .tic .reply-textarea .operate-box {
    padding: 20px 0 0;
    text-align: right;
  }
  .details .details-ask .tic .reply-textarea .operate-box .check {
    margin-top: 0;
    position: relative;
    top: 0px;
    display: inline-block;
  }
  .details .details-ask .tic .reply-textarea .operate-box .btn_aq {
    height: 34px;
    margin-left: 10px;
    line-height: 34px;
    padding: 0 30px;
    margin-bottom: 10px;
    display: inline-block;
    border-radius: 3px;
    color: #fff;
    background-color: #e4393c;
    border: 1px solid #e4393c;
    font-size: 14px;
    transition: .3s;
    font-weight: 700;
  }
  .seller-answer {
    width: 18px;
    height: 18px;
    border-radius: 9px;
    font-size: 12px;
    color: #fff;
    text-align: center;
    float: left;
    background: rgb(181, 0, 5);
    margin-right: 3px;
    margin-top: 4px;
    line-height: 18px;
  }
</style>
