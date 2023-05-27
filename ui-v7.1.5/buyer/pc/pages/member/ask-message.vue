<template>
  <div id="website-message">
    <div class="member-nav">
      <ul class="member-nav-list">
        <li>
          <nuxt-link to="./ask-message?is_read=NO">未读消息</nuxt-link>
        </li>
        <li>
          <nuxt-link to="./ask-message?is_read=YES">已读消息</nuxt-link>
        </li>
      </ul>
    </div>
    <div class="message-container">
      <empty-member v-if="tableData && !tableData.data.length && params.is_read === 'NO'">暂无未读消息</empty-member>
      <empty-member v-if="tableData && !tableData.data.length && params.is_read === 'YES'">暂无已读消息</empty-member>
      <ul v-else>
        <li v-for="message in tableData.data" :key="message.id" class="message-item">
          <div class="msg-time">{{ message.receive_time | unixToDate }}</div>
          <div class="msg-box">
            <div class="msg-title">
              <h4 v-if="message.msg_type === 'ASK'">问答消息--提问</h4>
              <h4 v-if="message.msg_type === 'REPLY'">问答消息--回复</h4>
              <div class="message-tools">
                <i class="el-icon-close" v-if="params.is_read === 'YES'" title="删除消息" @click="handleDeleteMessage(message)"></i>
              </div>
            </div>
            <div class="msg-content clearfix" v-if="message.msg_type === 'ASK'">
              <div v-if="message.ask_anonymous === 'YES'"><strong>匿名用户：</strong>{{ message.ask }}</div>
              <div v-if="message.ask_anonymous === 'NO'"><strong>{{ message.ask_member }}：</strong>{{ message.ask }}</div>
              <a :href="`/member/my-answer`">去回复 > </a>
            </div>
            <div class="msg-content clearfix" v-if="message.msg_type === 'REPLY'">
              <div><strong>我的问题：</strong>{{ message.ask }}</div>
              <div v-if="message.reply_anonymous === 'YES'"><strong>匿名用户：</strong>{{ message.reply }}</div>
              <div v-if="message.reply_anonymous === 'NO'"><strong>{{ message.reply_member }}：</strong>{{ message.reply }}</div>
              <a target="_blank" :href="`/ask-detail?ask_id=${message.ask_id}&goods_id=${message.goods_id}`">去查看 > </a>
            </div>
          </div>
        </li>
      </ul>
    </div>
    <div class="member-pagination" v-if="tableData && tableData.data.length">
      <a v-if="params.is_read === 'YES'" href="javascript:;" class="read-all" @click="handleDeletePageMessages">删除当前页面消息</a>
      <el-pagination
        @current-change="handleCurrentPageChange"
        :current-page.sync="params.page_no"
        :page-size="params.page_size"
        layout="total, prev, pager, next"
        :total="tableData.data_total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
  import { mapActions, mapGetters } from 'vuex'
  import * as API_Message from '@/api/message'
  export default {
    name: 'ask-message',
    head() {
      return {
        title: `站内消息-${this.site.title}`
      }
    },
    data() {
      return {
        params: {
          page_no: 1,
          page_size: 10,
          is_read: this.$route.query.is_read
        },
        tableData: ''
      }
    },
    mounted() {
      this.GET_AskMessageList()
    },
    watch: {
      $route: function({ query }) {
        this.params.is_read = query.is_read
        this.params.page_no = 1
        this.GET_AskMessageList()
      }
    },
    methods: {
      /** 当前页数发生改变 */
      handleCurrentPageChange(page) {
        this.params.page_no = page
        this.GET_AskMessageList()
      },

      /** 删除单条消息 */
      handleDeleteMessage(message) {
        this.$confirm('确定要删除这条消息吗？', () => {
          API_Message.deleteAskMessage(message.id).then(async () => {
            await this.getUnreadMessageNumAction()
            this.$message.success('删除成功！')
            this.GET_AskMessageList()
          })
        })
      },

      /** 删除当前页消息 */
      handleDeletePageMessages() {
        const ids = this.tableData.data.map(item => item.id).join(',')
        API_Message.deleteAskMessage(ids).then(async () => {
          await this.getUnreadMessageNumAction()
          this.GET_AskMessageList()
        })
      },

      /** 标记当前页消息为已读 */
      handleCurrPageMessageRead() {
        if (this.params.is_read === 'NO') {
          const ids = this.tableData.data.map(item => item.id).join(',')
          API_Message.setAskMessageRead(ids).then(async () => {
            await this.getUnreadMessageNumAction()
          })
        }
      },

      /** 获取站内消息 */
      GET_AskMessageList(){
        API_Message.getAskMessages(this.params).then(response => {
          this.tableData = response
          if (response && response.data.length) {
            this.handleCurrPageMessageRead()
          }
          this.MixinScrollToTop()
        })
      },
      ...mapActions(['getUnreadMessageNumAction'])
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../assets/styles/color";
  .message-container {
    padding-top: 10px;
  }
  .message-item {
    position: relative;
    text-align: center;
    margin-bottom: 15px;
    .msg-time {
      display: inline-block;
      background-color: #f7f7f7;
      height: 20px;
      padding: 0 10px;
      margin-bottom: 10px;
      border-radius: 1px;
      text-align: center;
    }
    .msg-box {
      position: relative;
      padding: 10px 20px;
      background-color: #f3f3f3;
      border: 1px solid #FFF;
      text-align: left;
      &:hover { border: 1px solid #dbdbdb }
      .msg-title {
        position: relative;
        height: 32px;
        line-height: 32px;
        border-bottom: 1px solid #d3d3d3;
        .message-tools {
          position: absolute;
          top: 0;
          right: 0;
        }
        .el-icon-check {
          margin-right: 10px;
        }
        .el-icon-close, .el-icon-check {
          width: 20px;
          height: 20px;
          text-align: center;
          line-height: 20px;
          cursor: pointer;
        }
      }
      .msg-content {
        width: 100%;
        padding-top: 10px;
        a { float: right }
      }
    }
  }
  .member-pagination {
    position: relative;
    .read-all {
      position: absolute;
      left: 10px;
      color: $color-href;
      &:hover {
        color: $color-main;
        text-decoration: underline;
      }
    }
  }
</style>
