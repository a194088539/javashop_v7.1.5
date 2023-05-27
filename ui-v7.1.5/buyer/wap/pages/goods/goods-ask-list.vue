<template>
  <div id="goods-ask-list" style="background-color: #eaeaea;padding-bottom:50px;">
    <nav-bar title="问答专区" fixed/>
    <div class="faq_goods type_slim type_arrow">
      <a :href="'/goods/' + goods.goods_id">
        <div class="faq_goods_cover">
          <img :src="goods.thumbnail">
        </div>
        <div class="faq_goods_info">
          <div class="faq_goods_info_name">{{ goods.goods_name }}</div>
        </div>
      </a>
    </div>
    <div id="ask-list" style="margin-top: 10px;">
      <div v-if="finished && !asks.length" class="no-data">暂无咨询信息</div>
      <van-list
        v-else
        v-model="loading"
        :finished="finished"
        @load="onLoad"
      >
        <ul class="faq_area">
          <li v-for="(ask, index) in asks" :key="index" @click="handleAskDetail(ask.ask_id)">
            <div class="faq_area_head">
              <em v-if="ask.anonymous === 'NO'">用户{{ ask.member_name }}的提问：</em>
              <em v-if="ask.anonymous === 'YES'">匿名用户的提问：</em>
              <span class="faq_area_head_time">
              {{ ask.create_time | unixToDate('yyyy-MM-dd') }}
              <i class="faq_area_head_more"></i>
            </span>
            </div>
            <div class="faq_area_tit">{{ ask.content }}</div>
            <div class="faq_area_desc" v-if="ask.reply_status === 'YES'">{{ ask.reply }}</div>
            <div class="faq_area_desc" v-if="ask.reply_status === 'NO'">{{ ask.first_reply.content }}</div>
            <div class="faq_area_num">查看全部{{ ask.reply_num }}个回答</div>
          </li>
        </ul>
      </van-list>
    </div>
    <div class="faq_publish hide" id="submit-ask">
      <div class="faq_publish_text">
        <textarea placeholder="说出您想要问的问题，字数请控制在3-120个。我们将邀请已购用户帮您解答~" v-model="ask_content" maxlength="120"></textarea>
      </div>
      <van-checkbox v-model="checked" style="height: 40px;font-size: 13px;">匿名提问</van-checkbox>
      <div>
        <div class="faq_publish_btn" style="background-color: #b50005;" @click="handleCancelSubmitAsk">取消</div>
        <div id="submit-form-btn" class="disabled faq_publish_btn" style="float: right;" @click="handleSubmitQuestion">发布</div>
      </div>
    </div>
    <div class="faq_btn fixed" id="submit-btn" @click="handleSubmitAsk">向已购买的用户提问</div>
  </div>
</template>
<script>
  import * as API_Members from '@/api/members'
  import * as API_Goods from '@/api/goods'
  import Storage from '@/utils/storage'
  import Vue from 'vue';
  import { Checkbox} from 'vant';
  Vue.use(Checkbox);
  export default {
    name: 'goods-ask-list',
    data() {
      return {
        loading: false,
        finished: false,
        goods_id: this.$route.query.goods_id,

        params: {
          page_no: 0,
          page_size: 10
        },

        asks: [],

        goods: '',

        ask_content: '',
        checked: true,
        anonymous: 'YES'
      }
    },
    mounted() {
      this.GET_GoodsDetail()
    },
    watch: {
      ask_content(){
        this.handleDisabledBtn()
      }
    },
    methods: {
      /** 当页数发生改变时 */
      onLoad() {
        this.params.page_no += 1
        this.GET_Asks()
      },

      /** 跳转至问题详情页面 */
      handleAskDetail(ask_id) {
        this.$router.push({path: '/ask-detail', query: { ask_id: ask_id, goods_id: this.goods_id}})
      },

      /** 展示咨询框 */
      handleSubmitAsk() {
        if (Storage.getItem('refresh_token')) {
          $('#ask-list').addClass('hide')
          $('#submit-btn').addClass('hide')
          $('#submit-ask').removeClass('hide')
        } else {
          this.$confirm('您还未登录，要现在去登录吗？', () => {
            let _forward = `${this.$route.path}?goods_id=`+this.goods_id
            if (this.$route.query.from_nav === 'assemble') {
              _forward = `${this.$route.path}?goods_id=`+this.goods_id+`&from_nav=assemble`
            }
            this.$router.push({ path: '/login', query: { forward:_forward }})
          })
        }
      },

      handleCancelSubmitAsk() {
        $('#ask-list').removeClass('hide')
        $('#submit-btn').removeClass('hide')
        $('#submit-ask').addClass('hide')
      },

      handleDisabledBtn() {
        if (this.ask_content.length > 2) {
          $('#submit-form-btn').removeClass('disabled')
        } else {
          $('#submit-form-btn').addClass('disabled')
        }
      },

      handleSubmitQuestion() {
        if (!$('#submit-form-btn').hasClass('disabled')) {
          this.anonymous = this.checked ? 'YES' : 'NO'
          API_Members.consultating(this.goods_id, this.ask_content, this.anonymous).then(() => {
            this.$message.success('发布成功')
            this.anonymous = 'YES'
            this.ask_content = ''
            this.handleCancelSubmitAsk()
            this.GET_Asks()
          })
        }
      },

      /** 获取商品咨询 */
      GET_Asks() {
        this.loading = true
        API_Members.getGoodsConsultations(this.goods_id, this.params).then(response => {
          this.loading = false
          const { data } = response
          if (!data || !data.length) {
            this.finished = true
          } else {
            this.asks.push(...data)
          }
        }).catch(() => { this.loading = false })
      },

      /** 获取商品详情信息 */
      GET_GoodsDetail() {
        API_Goods.getGoods(this.goods_id).then(response => {
          this.goods = response
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
  .faq_goods.type_arrow {
    padding: 10px 40px 10px 10px;
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
  .faq_area {
    position: relative;
  }
  .faq_area li {
    position: relative;
    margin-bottom: 10px;
    padding: 0 10px 12px;
    background-color: #fff;
  }
  .faq_area_head {
    position: relative;
    margin-bottom: 10px;
    height: 34px;
    line-height: 34px;
    color: #666;
    font-size: 12px;
  }
  .faq_area_head_time {
    position: absolute;
    top: 0;
    right: 0;
    color: #999;
    font-size: 10px;
  }
  .faq_area_tit {
    position: relative;
    margin-bottom: 5px;
    padding-left: 20px;
    color: #333;
    font-size: 16px;
    font-weight: 700;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  .faq_area_tit::before {
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
  .faq_area_desc {
    position: relative;
    margin-bottom: 5px;
    padding-left: 20px;
    color: #333;
    font-size: 16px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  .faq_area_desc::before {
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
  .faq_area_num {
    color: #3985ff;
    font-size: 12px;
    text-align: right;
  }
  .faq_area_num::after {
    content: "";
    width: 8px;
    height: 8px;
    border-top: 1px solid #3985ff;
    border-left: 1px solid #3985ff;
    transform-origin: 50%;
    transform: rotate(135deg);
    margin: -2px 0 0 5px;
    display: inline-block;
    vertical-align: middle;
  }
  .faq_btn {
    height: 50px;
    line-height: 50px;
    color: #fff;
    font-size: 16px;
    background-color: #3985ff;
    text-align: center;
  }
  .faq_btn.fixed {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    margin: 0 auto;
    min-width: 320px;
    max-width: 540px;
    z-index: 101;
  }
  .no-data {
    height: 100px;
    line-height: 100px;
    text-align: center;
    font-size: 14px;
  }
  .hide {
    display: none;
  }
  .faq_publish {
    padding: 0 10px 15px;
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
</style>
