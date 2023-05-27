<template>
  <div id="ask-detail">
    <nav-bar title="问题详情" fixed/>
    <div class="faq_goods type_slim type_arrow">
      <a :href="'/goods/' + askDetail.goods_id">
        <div class="faq_goods_cover">
          <img :src="askDetail.goods_img">
        </div>
        <div class="faq_goods_info">
          <div class="faq_goods_info_name">{{ askDetail.goods_name }}</div>
        </div>
      </a>
    </div>
    <div class="faq_question">
      <div class="faq_question_wrap">
        <div class="faq_question_head">
          <span v-if="askDetail.anonymous === 'NO'">用户{{ askDetail.member_name }}的提问：</span>
          <span v-else>匿名用户的提问：</span>
          <span class="faq_question_head_time">{{ askDetail.create_time | unixToDate }}<i class="faq_question_head_more"></i></span>
        </div>
        <div class="faq_question_desc">{{askDetail.content}}</div>
      </div>
    </div>
    <div class="faq_answers">
      <div class="faq_answers_num">共{{ askDetail.reply_num }}个回答</div>
      <div class="faq_answers_list">
        <div v-if="finished && !replyList.length" class="no-data">暂无回复</div>
        <van-list
          v-else
          v-model="loading"
          :finished="finished"
          @load="onLoad"
        >
        <ul>
          <li v-if="askDetail.reply_status === 'YES'">
            <div class="faq_answers_list_head seller-reply">
              <span class="faq_answers_list_status">{{ askDetail.reply_time | unixToDate }}</span>
              <span>商家回复：</span>
            </div>
            <div class="faq_answers_list_desc" style="overflow: hidden;">{{ askDetail.reply }}</div>
          </li>
          <li v-for="(reply, index) in replyList" :key="index">
            <div class="faq_answers_list_head">
              <span class="faq_answers_list_status">{{ reply.reply_time | unixToDate }}</span>
              <span v-if="reply.anonymous === 'NO'">用户{{ reply.member_name }}说：</span>
              <span v-else>匿名用户说：</span>
            </div>
            <div class="faq_answers_list_desc" style="overflow: hidden;">{{ reply.content }}</div>
          </li>
        </ul>
        </van-list>
      </div>
    </div>
  </div>
</template>
<script>
  import * as API_Members from '@/api/members'
  export default {
    name: 'ask-detail',
    data() {
      return {
        loading: false,
        finished: false,
        ask_id: this.$route.query.ask_id,
        goods_id: this.$route.query.goods_id,
        /** 咨询详情信息 */
        askDetail:'',

        params: {
          page_no: 0,
          page_size: 10
        },

        replyList: []
      }
    },
    mounted() {
      this.GET_AskDetail()
    },
    methods: {
      /** 当页数发生改变时 */
      onLoad() {
        this.params.page_no += 1
        this.GET_AskReplyList()
      },

      /** 获取问题回复列表 */
      GET_AskReplyList() {
        this.loading = true
        API_Members.getGoodsAskReplys(this.ask_id, this.params).then(response => {
          this.loading = false
          const { data } = response
          if (!data || !data.length) {
            this.finished = true
          } else {
            this.replyList.push(...data)
          }
        }).catch(() => { this.loading = false })
      },

      /** 获取问题详情信息 */
      GET_AskDetail() {
        API_Members.getAskDetail(this.ask_id).then(response => {
          this.askDetail = response
        })
      }
    }
  }
</script>
<style type="text/scss" lang="scss" scoped>
  @import "../assets/styles/color";
  .faq_goods {
    position: relative;
    padding: 10px;
    height: 75px;
    background-color: #fff;
  }
  .faq_goods.type_arrow {
    padding: 10px 40px 10px 10px;
  }
  .faq_goods.type_slim {
    height: 50px;
    margin-top: 45px;
  }
  .faq_goods.type_arrow::after {
    position: absolute;
    top: 45%;
    right: 13px;
    transform: translateY(-50%);
    content: "";
    display: block;
    width: 10px;
    height: 10px;
    border-top: 1px solid #666;
    border-left: 1px solid #666;
    transform-origin: 50%;
    transform: rotate(135deg);
  }
  .faq_goods_cover {
    position: absolute;
    top: 10px;
    left: 10px;
    width: 50px;
    height: 50px;
    overflow: hidden;
  }
  .faq_goods_cover img {
    position: absolute;
    top: 50%;
    left: 50%;
    width: 100%;
    height: auto;
    transform: translate(-50%,-50%);
  }
  .faq_goods.type_slim .faq_goods_info {
    margin-left: 60px;
    padding: 7px 0;
  }
  .faq_goods_info_name {
    color: #333;
    font-size: 12px;
    height: 36px;
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
  }
  .faq_question {
    padding: 0 10px 10px;
    background-color: #fff;
  }
  .faq_question_wrap {
    position: relative;
    padding: 0 10px 6px;
    background-color: #f2f2f7;
    border-radius: 2px;
  }
  .faq_question_wrap::before {
    content: "";
    position: absolute;
    top: -10px;
    left: 32px;
    width: 0;
    height: 0;
    border-width: 5px;
    border-color: transparent transparent #f2f2f7;
    border-style: dashed dashed solid;
  }
  .faq_question_head {
    position: relative;
    height: 34px;
    line-height: 34px;
    color: #666;
    font-size: 12px;
  }
  .faq_question_head_time {
    position: absolute;
    top: 0;
    right: 0;
    color: #999;
    font-size: 10px;
  }
  .faq_question_desc {
    position: relative;
    padding-left: 20px;
    color: #333;
    font-size: 14px;
    font-weight: 700;
    word-wrap: break-word;
  }
  .faq_question_desc::before {
    content: "Q";
    display: inline-block;
    margin: -2px 5px 0 -20px;
    width: 15px;
    height: 15px;
    line-height: 15px;
    color: #fff;
    font-size: 14px;
    font-weight: 400;
    background-color: #ff9600;
    text-align: center;
    border-radius: 2px;
    vertical-align: middle;
  }
  .faq_answers_num {
    padding: 0 10px;
    height: 32px;
    line-height: 32px;
    color: #666;
    font-size: 12px;
    background-color: #e8e8ed;
  }
  .faq_answers_list {
    padding: 0 10px;
    background-color: #fff;
  }
  .faq_answers_list li:not(:last-child)::after {
    content: "";
    position: absolute;
    z-index: 1;
    pointer-events: none;
    background-color: #ddd;
    height: 1px;
    left: 0;
    right: 0;
    bottom: 0;
  }
  .faq_answers_list li {
    position: relative;
    padding-bottom: 12px;
  }
  .faq_answers_list_head {
    position: relative;
    padding: 15px 0;
    color: #666;
    font-size: 12px;
  }
  .faq_answers_list_status {
    display: inline-block;
    padding: 0 5px;
    height: 15px;
    line-height: 15px;
    font-size: 10px;
    vertical-align: middle;
    border-radius: 1px;
    float: right;
    color: #999;
  }
  .faq_answers_list_status::before {
    content: "";
    display: inline-block;
    vertical-align: middle;
    width: 0;
    height: 100%;
    margin-top: 1px;
  }
  .faq_answers_list_desc {
    position: relative;
    margin-bottom: 5px;
    padding-left: 20px;
    color: #333;
    font-size: 14px;
  }
  .seller-reply::before {
    content: "S";
    display: inline-block;
    margin-right: 6px;
    width: 15px;
    height: 15px;
    line-height: 15px;
    color: #fff;
    font-size: 14px;
    background-color: #c41818;
    text-align: center;
    border-radius: 2px;
    vertical-align: middle;
  }
  .faq_answers_list_desc::before {
    content: "A";
    display: inline-block;
    margin: -2px 5px 0 -20px;
    width: 15px;
    height: 15px;
    line-height: 15px;
    color: #fff;
    font-size: 14px;
    background-color: #18c461;
    text-align: center;
    border-radius: 2px;
    vertical-align: middle;
  }
  .faq_answers_list_time {
    margin-left: 20px;
    color: #999;
    font-size: 10px;
  }
  .no-data {
    height: 100px;
    line-height: 100px;
    text-align: center;
    font-size: 14px;
  }
</style>

