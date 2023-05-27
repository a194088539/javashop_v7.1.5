<template>
  <div id="website-message">
    <div class="member-nav">
      <ul class="member-nav-list">
        <li>
          <nuxt-link to="./website-message?read=0">未读消息</nuxt-link>
        </li>
        <li>
          <nuxt-link to="./website-message?read=1">已读消息</nuxt-link>
        </li>
      </ul>
    </div>
    <div class="message-container">
      <empty-member v-if="tableData && !tableData.data.length && (params.read === 0 || params.read === '0')">暂无未读消息</empty-member>
      <empty-member v-if="tableData && !tableData.data.length && (params.read === 1 || params.read === '1')">暂无已读消息</empty-member>
      <ul v-else>
        <li v-for="message in tableData.data" :key="message.id" class="message-item">
          <div class="msg-time">{{ message.send_time | unixToDate }}</div>
          <div class="msg-box">
            <div class="msg-title">
              <h4>{{ message.title || '系统消息' }}</h4>
              <div class="message-tools">
                <i v-if="params.read === 1 || params.read === '1'" class="el-icon-close" title="删除消息" @click="handleDeleteMessage(message)"></i>
              </div>
            </div>
            <div class="msg-content clearfix">
              <div>{{ message.content }}</div>
              <!--<nuxt-link to="#">查看详情 > </nuxt-link>-->
            </div>
          </div>
        </li>
      </ul>
    </div>
    <div class="member-pagination" v-if="tableData && tableData.data.length">
      <a v-if="params.read === 1 || params.read === '1'" href="javascript:;" class="read-all" @click="handleDeletePageMessages">删除当前页面消息</a>
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
    name: 'website-message',
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
          read: this.$route.query.read
        },
        tableData: ''
      }
    },
    mounted() {
      this.GET_MessageList()
    },
    watch: {
      $route: function({ query }) {
        this.params.read = query.read
        this.params.page_no = 1
        this.GET_MessageList()
      },
    },
    methods: {
      /** 当前页数发生改变 */
      handleCurrentPageChange(page) {
        this.params.page_no = page
        this.GET_MessageList()
      },

      /** 删除消息 */
      handleDeleteMessage(message) {
        this.$confirm('确定要删除这条消息吗？', () => {
          API_Message.deleteMessage(message.id).then(async () => {
            await this.getUnreadMessageNumAction()
            this.$message.success('删除成功！')
            this.GET_MessageList()
          })
        })
      },

      /** 删除当前页消息 */
      handleDeletePageMessages() {
        const ids = this.tableData.data.map(item => item.id).join(',')
        API_Message.deleteMessage(ids).then(async () => {
          await this.getUnreadMessageNumAction()
          this.GET_MessageList()
        })
      },

      /** 标记当前页消息为已读 */
      handleReadPageMessages() {
        if (this.params.read === '0' || this.params.read === 0) {
          const ids = this.tableData.data.map(item => item.id).join(',')
          API_Message.messageMarkAsRead(ids).then(async () => {
            await this.getUnreadMessageNumAction()
          })
        }
      },

      /** 获取站内消息 */
      GET_MessageList(){
        API_Message.getMessages(this.params).then(response => {
          this.tableData = response
          if (response && response.data.length) {
            this.handleReadPageMessages()
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
