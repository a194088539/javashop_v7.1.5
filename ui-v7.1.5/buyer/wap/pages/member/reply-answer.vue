<template>
  <div id="goods-ask-list" style="background-color: #eaeaea;">
    <nav-bar title="问答专区" fixed/>
    <div class="faq_goods type_slim">
      <div class="faq_goods_cover">
        <img :src="ask.goods_img">
      </div>
      <div class="faq_goods_info">
        <div class="faq_goods_info_name">{{ ask.goods_name }}</div>
      </div>
    </div>
    <div class="faq_question">
      <div class="faq_question_wrap">
        <div class="faq_question_head">{{ ask.content }}</div>
      </div>
    </div>
    <div class="faq_publish" id="submit-ask">
      <div class="faq_publish_text">
        <textarea placeholder="请输入您的回答，字数请控制在3-120个。" v-model="reply_content" maxlength="120"></textarea>
      </div>
      <van-checkbox v-model="checked" style="height: 40px;font-size: 13px;">匿名回答</van-checkbox>
      <div>
        <div class="faq_publish_btn" style="background-color: #b50005;" @click="handleCancelSubmit">取消</div>
        <div id="submit-form-btn" class="disabled faq_publish_btn" style="float: right;" @click="handleSubmit">发布</div>
      </div>
    </div>
  </div>
</template>
<script>
  import * as API_Members from '@/api/members'
  import Vue from 'vue';
  import { Checkbox} from 'vant';
  Vue.use(Checkbox);
  export default {
    name: 'reply-answer',
    data() {
      return {
        loading: false,
        finished: false,
        ask_id: this.$route.query.ask_id,

        params: {
          page_no: 0,
          page_size: 10
        },

        ask: '',

        reply_content: '',
        checked: true,
        anonymous: 'YES'
      }
    },
    mounted() {
      this.GET_AskDetail()
    },
    watch: {
      reply_content(){
        this.handleDisabledBtn()
      }
    },
    methods: {

      handleCancelSubmit() {
        this.$router.push({path: '/member/my-invite-ask', query: {}})
      },

      handleDisabledBtn() {
        if (this.reply_content.length > 2) {
          $('#submit-form-btn').removeClass('disabled')
        } else {
          $('#submit-form-btn').addClass('disabled')
        }
      },

      handleSubmit() {
        if(this.MixinEmojiCharacter(this.reply_content)){
          this.$message.error('不能输入表情符号！')
          return
        }
        if (!$('#submit-form-btn').hasClass('disabled')) {
          this.anonymous = this.checked ? 'YES' : 'NO'
          API_Members.replyAsk(this.ask_id, this.reply_content, this.anonymous).then(() => {
            this.$message.success('发布成功')
            this.$router.push({path: '/member/my-answer', query: {}})
          })
        }
      },

      /** 获取会员咨询信息 */
      GET_AskDetail() {
        API_Members.getAskDetail(this.ask_id).then(response => {
          this.ask = response
        })
      }
    }
  }
</script>
<style type="text/scss" lang="scss" scoped>
  @import "../../assets/styles/color";
  .faq_goods {
    position: relative;
    padding: 10px;
    height: 50px;
    background-color: #fff;
  }
  .faq_goods.type_slim {
    height: 50px;
    margin-top: 45px;
  }
  .faq_goods_cover {
    position: absolute;
    top: 10px;
    left: 10px;
    width: 75px;
    height: 75px;
    overflow: hidden;
  }
  .faq_goods.type_slim .faq_goods_cover {
    width: 50px;
    height: 50px;
  }
  .faq_goods_cover img {
    position: absolute;
    top: 50%;
    left: 50%;
    width: 100%;
    height: auto;
    transform: translate(-50%,-50%);
  }
  .faq_goods_info {
    margin-left: 85px;
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
  .faq_publish {
    padding: 20px 10px 15px;
    background-color: #fff;
  }
  .faq_publish_text {
    position: relative;
    margin-bottom: 10px;
  }
  .faq_publish_text::before {
    content: "";
    position: absolute;
    z-index: 1;
    pointer-events: none;
    background-color: #ddd;
    border: 1px solid #ddd;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: none;
    border-color: #ddd;
  }
  .faq_publish_text textarea {
    border: none;
    background: none;
    -webkit-appearance: none;
    appearance: none;
    border-radius: 0;
  }
  .faq_publish_text textarea {
    display: block;
    padding: 10px;
    width: 100%;
    height: 100px;
    color: #333;
    font-size: 14px;
    box-sizing: border-box;
  }
  .faq_publish_btn {
    height: 40px;
    line-height: 40px;
    color: #fff;
    font-size: 16px;
    background-color: #3985ff;
    text-align: center;
    width: 48%;
    display: inline-block;
  }
  .faq_publish_btn.disabled {
    color: #999;
    background-color: #ccc;
  }
  .faq_question {
    padding: 10px;
    background-color: #fff;
  }
  .faq_question_wrap{
    position: relative;
    border-radius: 2px;
  }
  .faq_question_head{
    position: relative;
    color: #333;
    font-size: 14px;
    font-weight: 700;
    line-height: 1.5;
    word-wrap: break-word;
  }
</style>
