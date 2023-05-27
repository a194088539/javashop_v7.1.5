<template>
  <div id="my-answer">
    <div class="member-nav">
      <ul class="member-nav-list">
        <li>
          <nuxt-link to="./my-consultation">我的咨询</nuxt-link>
        </li>
        <li class="active">
          <nuxt-link to="./my-answer">我的回答</nuxt-link>
        </li>
      </ul>
    </div>
    <empty-member v-if="answers && !answers.data.length">暂无回答</empty-member>
    <template v-else>
      <div class="mycomment-table">
        <div class="answers">
          <div class="items">
            <div class="item" v-for="answer in answers.data" :key="answer.id">
              <a :href="'/goods/' + answer.goods_id" target="_blank" class="item-img">
                <img class="" :src="answer.goods_img" :title="answer.goods_name" data-lazy-img="done" width="60" height="60">
              </a>
              <div class="cont">
                <div class="tit clearfix">
                  <div class="def">
                    <span class="time">{{ answer.create_time | unixToDate }}</span>
                  </div>
                  <span class="icon icon_ask">问</span>
                  <a target="_blank" :href="`/ask-detail?ask_id=${answer.ask_id}&goods_id=${answer.goods_id}`" class="tt">{{ answer.ask_content }}</a>
                </div>
                <div class="tic" v-if="answer.reply_status === 'NO'">
                  <a :href="`/reply-answer?ask_id=${answer.ask_id}&goods_id=${answer.goods_id}`" class="btn_aq">我要回答</a>
                </div>
                <div class="tic" v-if="answer.reply_status === 'YES'">
                  <a target="_blank" :href="`/ask-detail?ask_id=${answer.ask_id}&goods_id=${answer.goods_id}`">{{ answer.content }}</a>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="member-pagination" v-if="answers">
        <el-pagination
          @current-change="handleCurrentPageChange"
          :current-page.sync="params.page_no"
          :page-size="params.page_size"
          layout="total, prev, pager, next"
          :total="answers.data_total">
        </el-pagination>
      </div>
    </template>
  </div>
</template>

<script>
  import * as API_Members from '@/api/members'
  export default {
    name: 'my-answer',
    head() {
      return {
        title: `购买咨询-${this.site.title}`
      }
    },
    data() {
      return {
        answers: '',
        params: {
          page_no: 1,
          page_size: 10
        }
      }
    },
    mounted() {
      this.GET_Answer()
    },
    methods: {
      /** 当前页数发生改变 */
      handleCurrentPageChange(page) {
        this.params.page_no = page;
        this.GET_Answer()
      },
      /** 获取我的回答 */
      GET_Answer() {
        API_Members.getAnswers(this.params).then(response => {
          this.answers = response
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../assets/styles/color";
  .consultation-container {
    padding-top: 10px;
  }
  .comment-item {
    margin-bottom: 10px;
  }
  .comment-title {
    padding: 5px 0;
    border: 1px solid #e7e7e7;
    background: #fafafa;
    overflow: hidden;
    a {
      float: left;
      color: $color-href;
      margin-left: 10px;
      margin-right: 20px;
      display: -webkit-box;
      max-width: 500px;
      -webkit-box-orient: vertical;
      -webkit-line-clamp: 1;
      overflow: hidden;
      &:hover { color: $color-main }
    }
  }
  .comment-body {
    border: 1px solid #e7e7e7;
    overflow: hidden;
    padding: 10px;
    border-top: none;
  }
  .comment-content {
    strong { flex-shrink: 0 }
    display: flex;
    margin-bottom: 5px;
    &.seller-reply { color: #FF5722 }
  }

  .mycomment-table {
    padding: 0 20px 20px;
  }
  .answers .items .item {
    padding: 15px 0;
    border-bottom: 1px solid #e5e5e5;
    width: 100%;
  }
  .answers .items .item .item-img {
    display: block;
    float: left;
    width: 60px;
    height: 60px;
    border: 1px solid #e2e2e2;
    margin: 0 20px 0 0;
  }
  .answers .items .item .cont {
    overflow: hidden;
    zoom: 1;
  }
  .answers .items .item .cont .tit {
    padding-top: 3px;
    width: 100%;
  }
  .answers .items .item .cont .tit .def {
    padding-top: 3px;
    float: right;
    color: #999;
    font-family: Verdana;
  }
  .answers .items .item .cont .tit .icon_ask {
    width: 18px;
    height: 18px;
    color: #fff;
    line-height: 18px;
    float: left;
    margin-top: 5px;
    margin-right: 3px;
    display: inline-block;
    *display: inline;
    *zoom: 1;
    background-color: #f91;
    border-radius: 9px;
    text-align: center;
  }
  .answers .items .item .cont .tit .tt {
    max-width: 700px;
    display: inline-block;
    *display: inline;
    *zoom: 1;
    text-overflow: ellipsis;
    -o-text-overflow: ellipsis;
    white-space: nowrap;
    overflow: hidden;
    font-weight: 700;
    color: #666;
    position: relative;
    top: 5px;
  }
  .answers .items .item .cont .tic {
    max-height: 38px;
    _height: 38px;
    padding-top: 12px;
  }
  .answers .items .item .cont .tic .a_ask {
    color: #005ea7;
    font-family: "宋体";
  }
  .answers .items .item .cont .tic .btn_aq {
    height: 27px;
    line-height: 27px;
    padding: 0 10px;
    display: inline-block;
    border-radius: 2px;
    background-color: #fff;
    border: 1px solid #e4393c;
    color: #e4393c;
    font-size: 14px;
    transition: .3s;
  }
</style>
