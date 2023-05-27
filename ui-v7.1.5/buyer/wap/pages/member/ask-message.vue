<template>
  <div id="website-message" style="background-color: #f7f7f7">
    <van-nav-bar left-arrow title="问答消息" @click-left="onClickLeft"></van-nav-bar>
    <van-tabs v-model="active" :line-width="100" :swipe-threshold="2" @click="handleTabChange">
      <van-tab title="未读消息"/>
      <van-tab title="已读消息"/>
    </van-tabs>
    <div class="message-container">
      <empty-member v-if="finished && !messageList.length">暂无消息</empty-member>
      <van-list
        v-else
        v-model="loading"
        :finished="finished"
        @load="onLoad"
      >
        <ul class="message-list">
          <li class="message-item" v-for="(message, index) in messageList" :key="index">
            <div class="msg-date">{{ message.receive_time | unixToDate }}</div>
            <div class="msg-content" v-if="message.msg_type === 'ASK'">
              <van-swipe-cell :right-width="65">
                <p class="msg-title">问答消息--有人向你提问</p>
                <div class="msg-detail" style="height: 50px;" @click="handleToReply">
                  <img :src="message.goods_img" width="50" height="50">
                  <div class="msg-text">{{ message.ask }}</div>
                </div>
                <span slot="right" @click="handleDeleteMessage(message)">删除</span>
              </van-swipe-cell>
            </div>
            <div class="msg-content" v-if="message.msg_type === 'REPLY'">
              <van-swipe-cell :right-width="65">
                <p class="msg-title">问答消息--有人回答了你</p>
                <div class="msg-detail" @click="handleAskDetail(message.ask_id, message.goods_id)">
                  <div class="msg-member ask-name">{{ message.ask_member }}</div>
                  <div class="ask-content">{{ message.ask }}</div>
                  <div class="msg-member reply-name">{{ message.reply_member }}</div>
                  <div class="ask-content">{{ message.reply }}</div>
                </div>
                <div class="msg-goods" @click="handleAskDetail(message.ask_id, message.goods_id)">
                  <img :src="message.goods_img" width="50" height="50">
                  <div class="msg-text">{{ message.goods_name }}</div>
                </div>
                <span slot="right" @click="handleDeleteMessage(message)">删除</span>
              </van-swipe-cell>
            </div>
          </li>
        </ul>
      </van-list>
    </div>
  </div>
</template>

<script>
  import * as API_Message from '@/api/message'
  export default {
    name: 'ask-message',
    data() {
      return {
        loading: false,
        finished: false,
        params: {
          page_no: 0,
          page_size: 10,
          is_read: this.$route.query.read
        },
        active: this.getActive(this.$route.query.read),
        messageList: []
      }
    },
    methods: {
      onClickLeft() {
        this.$router.push({path: '/member/message-index', query: {}})
      },

      getActive(read) {
        if (read === 'YES') {
          return 1
        } else {
          return 0
        }
      },

      handleTabChange(index) {
        this.active = index
        this.params.is_read = index === 0 ? 'NO' : 'YES'
        this.$router.push({path: '/member/ask-message', query: {read : this.params.is_read}})
        this.GET_MessageList(true)
      },

      onLoad() {
        this.params.page_no += 1
        this.GET_MessageList(false)
      },

      /** 删除消息 */
      handleDeleteMessage(message) {
        this.$confirm('确定要删除这条消息吗？', () => {
          API_Message.deleteAskMessage(message.id).then(() => {
            this.$message.success('删除成功！')
            this.GET_MessageList(true)
          })
        })
      },

      handleToReply() {
        this.$router.push({path: '/member/my-invite-ask', query: {}})
      },

      handleAskDetail(ask_id, goods_id){
        this.$router.push({path: '/ask-detail', query: {ask_id : ask_id, goods_id : goods_id}})
      },

      /** 获取站内消息 */
      GET_MessageList(reset){
        this.loading = true
        if (reset) {
          this.params.page_no = 1
          this.finished = false
          this.messageList = []
        }
        API_Message.getAskMessages(this.params).then(async response => {
          this.loading = false
          const { data } = response
          if (!data || !data.length) {
            this.finished = true
          } else {
            if (this.params.is_read === 'NO') {
              const ids = data.map(item => item.id).join(',')
              data.length && await API_Message.setAskMessageRead(ids)
            }
            this.messageList.push(...data)
          }
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../assets/styles/color";
  .message-container {
    background-color: #f7f7f7;
  }
  .message-list {
    margin: 0 10px;
  }
  .message-item {
    margin-bottom: 15px;
  }
  .msg-date {
    padding-top: 15px;
    padding-bottom: 10px;
    width: 100%;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    font-size: 10px;
    line-height: 14px;
    color: #848689;
    text-align: center;
    background-color: #f7f7f7;
  }
  .msg-content {
    overflow: hidden;
    font-size: 14px;
    color: #343434;
    line-height: 14px;
    background-color: #fff;
    border-radius: 8px;
  }
  .msg-title {
    padding: 10px 10px 0 10px;
    line-height: 21px;
    font-size: 15px;
    font-weight: 700;
    color: #343434;
  }
  .msg-detail {
    padding: 15px 10px;
  }
  .msg-goods img, .msg-detail img {
    float: left;
    margin-right: 10px;
  }
  .msg-goods .msg-text, .msg-detail .msg-text {
    float: left;
    width: 80%;
    line-height: 1.5;
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    margin-top: 5px;
    word-wrap: break-word;
    word-break: normal;
  }
  .msg-member.ask-name {
    margin-bottom: 10px;
  }
  .msg-member.reply-name {
    margin-top: 10px;
    margin-bottom: 10px;
  }
  .msg-member::before {
    display: inline-block;
    margin: -2px 5px 0 0px;
    width: 19px;
    height: 19px;
    line-height: 19px;
    color: #fff;
    font-size: 12px;
    font-weight: 400;
    text-align: center;
    border-radius: 10px;
    vertical-align: middle;
  }
  .ask-name::before {
    content: "问";
    background-color: #7f87f3;
  }
  .reply-name::before {
    content: "答";
    background-color: #f9850d;
  }
  .ask-content {
    line-height: 1.5;
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
  }
  .msg-goods {
    padding: 10px;
    border-top: 1px solid #f9f5f5;
    height: 50px;
  }
</style>
