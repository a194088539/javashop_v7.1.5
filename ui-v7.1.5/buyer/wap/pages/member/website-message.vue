<template>
  <div id="website-message" style="background-color: #f7f7f7">
    <van-nav-bar left-arrow title="系统消息" @click-left="onClickLeft"></van-nav-bar>
    <van-tabs v-model="params.read" :line-width="100" :swipe-threshold="2" @click="handleTabChange">
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
            <div class="msg-date">{{ message.send_time | unixToDate }}</div>
            <div class="msg-content">
              <van-swipe-cell :right-width="65">
                <p v-if="message.title" class="msg-title">{{ message.title }}</p>
                <p v-else class="msg-title">系统消息</p>
                <div class="msg-detail">{{ message.content }}</div>
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
    name: 'website-message',
    data() {
      return {
        loading: false,
        finished: false,
        params: {
          page_no: 0,
          page_size: 10,
          read: this.$route.query.read
        },
        messageList: []
      }
    },
    methods: {
      onClickLeft() {
        this.$router.push({path: '/member/message-index', query: {}})
      },

      handleTabChange(index) {
        this.$router.push({path: '/member/website-message', query: {read : index}})
        this.params.read = index
        this.GET_MessageList(true)
      },

      onLoad() {
        this.params.page_no += 1
        this.GET_MessageList(false)
      },

      /** 删除消息 */
      handleDeleteMessage(message) {
        this.$confirm('确定要删除这条消息吗？', () => {
          API_Message.deleteMessage(message.id).then(() => {
            this.$message.success('删除成功！')
            this.GET_MessageList(true)
          })
        })
      },


      /** 获取站内消息 */
      GET_MessageList(reset){
        this.loading = true
        if (reset) {
          this.params.page_no = 1
          this.finished = false
          this.messageList = []
        }
        API_Message.getMessages(this.params).then(async response => {
          this.loading = false
          const { data } = response
          if (!data || !data.length) {
            this.finished = true
          } else {
            if (this.params.read === 0) {
              const ids = data.map(item => item.id).join(',')
              await API_Message.messageMarkAsRead(ids)
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
    line-height: 1.5;
  }
</style>
