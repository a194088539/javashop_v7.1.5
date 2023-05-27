<template>
  <div id="goods-ask">
    <div class="detail_faqbox" @click="handleAskList">
      <div class="detail_faqbox_head">
        <div class="detail_faqbox_head_tit">问答专区</div>
        <div class="detail_faqbox_head_empty" v-if="finished && !asks.length">暂无提问信息，<span>去提问</span></div>
        <div class="detail_faqbox_head_num" v-else>查看全部问答</div>
      </div>
      <ul class="detail_faqbox_list" style="">
        <template v-for="(ask, index) in asks">
          <li v-if="index < 4" :key="index">
            <p class="detail_faqbox_list_desc">{{ ask.content | filterContent}}</p><span class="detail_faqbox_list_num">共{{ ask.reply_num }}个回答</span>
          </li>
        </template>
      </ul>
    </div>
  </div>
</template>

<script>
  import * as API_Members from '@/api/members'
  import Storage from '@/utils/storage'
  export default {
    name: 'goods-ask',
    props: ['goodsId', 'selectedSku'],
    data() {
      return {
        loading: false,
        finished: false,
        params: {
          page_no: 1,
          page_size: 10
        },
        asks: []
      }
    },
    mounted() {
      this.GET_Asks()
    },
    filters: {
      filterContent(val) {
        if (val.length > 20) {
          return val.substr(0,20)+"...";
        }
        return val;
      }
    },
    methods: {
      /** 跳转至商品咨询列表 */
      handleAskList() {
        this.$router.push({path: '/goods/goods-ask-list', query: { goods_id: this.goodsId}})
      },
      /** 获取商品咨询 */
      GET_Asks() {
        this.loading = true
        API_Members.getGoodsConsultations(this.goodsId, this.params).then(response => {
          this.loading = false
          const { data } = response
          if (!data || !data.length) {
            this.finished = true
          } else {
            this.asks.push(...data)
          }
        }).catch(() => { this.loading = false })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../assets/styles/color";
  .detail_faqbox_head {
    position: relative;
    padding: 0 10px;
    height: 45px;
    line-height: 45px;
    font-size: 14px;
    color: #333;
  }
  detail_faqbox_head::after {
    content: "";
    height: 0;
    display: block;
    border-bottom: 1px solid #ddd;
    position: absolute;
    left: 0;
    right: 0;
    bottom: 0;
  }
  .detail_faqbox_head_num {
    position: absolute;
    top: 0;
    right: 14px;
    font-size: 12px;
    color: #666;
  }
  .detail_faqbox_head_num::after {
    position: relative;
    width: 8px;
    height: 8px;
    margin-top: -2px;
    margin-left: 7px;
    display: inline-block;
    vertical-align: middle;
    border-color: #666;
    -webkit-transform: rotate(135deg);
    top: 50%;
    right: 4px;
    border-top: 1px solid #b3aeae;
    border-left: 1px solid #b3aeae;
    background: none;
    content: "\20";
  }
  .detail_faqbox_head_empty {
    position: absolute;
    top: 0;
    right: 14px;
    font-size: 14px;
    color: #999;
  }
  .detail_faqbox_head_empty span {
    color: #3985ff;
  }
  .detail_faqbox_list {
    padding: 0 10px;
  }
  .detail_faqbox_list li {
    position: relative;
    height: 34px;
    line-height: 34px;
  }
  .detail_faqbox_list_desc {
    padding-right: 80px;
    color: #333;
    font-size: 13px;
  }
  .detail_faqbox_list_desc::before {
    content: "Q";
    display: inline-block;
    margin: -2px 5px 0 0;
    width: 15px;
    height: 15px;
    line-height: 15px;
    border-radius: 2px;
    background-color: #ff9600;
    color: #fff;
    font-size: 12px;
    vertical-align: middle;
    text-align: center;
  }
  .detail_faqbox_list_num {
    position: absolute;
    top: 0;
    right: 0;
    color: #999;
    font-size: 12px;
  }
</style>
