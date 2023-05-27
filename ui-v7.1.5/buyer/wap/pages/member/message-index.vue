<template>
  <div id="message-index" style="background-color: rgb(241, 241, 241)">
    <van-nav-bar left-arrow title="站内消息" @click-left="onClickLeft"></van-nav-bar>
    <div class="message-container">
      <ul>
        <li class="mg-types">
          <a :href="'/member/website-message?read=0'">
            <div class="msg-box">
              <span class="mg-timg actimg system-icon"></span>
              <div class="mg-tcont">
                <div class="mg-ttitle">
                  <div style="float:left;">
                    <a href="javascript:void(0);" title="系统消息">
                      系统消息
                    </a>
                  </div>
                </div>
                <span class="mg-illus">
                  <div style="float:left">
                    <span color="grey" v-if="no_read.system_num === 0">暂无未读消息</span>
                    <span style="color: #b50005;" v-if="no_read.system_num != 0">有{{ no_read.system_num }}条未读消息</span>
                  </div>
                </span>
              </div>
            </div>
          </a>
        </li>
        <li class="mg-types">
          <a :href="'/member/ask-message?read=NO'">
            <div class="msg-box">
              <span class="mg-timg actimg ask-icon"></span>
              <div class="mg-tcont">
                <div class="mg-ttitle">
                  <div style="float:left;">
                    <a href="javascript:void(0);" title="问答消息">
                      问答消息
                    </a>
                  </div>
                </div>
                <span class="mg-illus">
                  <div style="float:left">
                    <span color="grey" v-if="no_read.ask_num === 0">暂无未读消息</span>
                    <span style="color: #b50005;" v-if="no_read.ask_num != 0">有{{ no_read.ask_num }}条未读消息</span>
                  </div>
                </span>
              </div>
            </div>
          </a>
        </li>
      </ul>
    </div>
  </div>
</template>
<script>
  import * as API_Message from '@/api/message'
  export default {
    name: 'message-index',
    data() {
      return {
        no_read: ''
      }
    },
    mounted() {
      this.GET_NoReadMessageNum()
    },
    methods: {
      onClickLeft() {
        this.$router.push({path: '/member', query: {}})
      },

      /** 获取未读消息数量信息 */
      GET_NoReadMessageNum() {
        API_Message.getNoReadMessageNum().then(response => {
          this.no_read = response
        })
      }
    }
  }
</script>
<style type="text/scss" lang="scss" scoped>
  @import "../../assets/styles/color";
  .message-container {
    padding-top: 50px;
  }
  .mg-types {
    height: 52px;
    padding: 10px;
    margin-bottom: 20px;
  }
  .msg-box {
    position: relative;
    padding: 10px;
    background-color: #FFF;
    border: 1px solid #FFF;
    text-align: left;
    height: 52px;
  }
  .mg-types .mg-timg {
    width: 34px;
    height: 34px;
    border-radius: 4.5px;
    float: left;
    position: relative;
    top: 5px;
  }
  .mg-types .system-icon {
    background-image: url(../../assets/images/icon-msg-system.png);
    background-color: #eade87;
  }
  .mg-types .ask-icon {
    background-image: url(../../assets/images/icon-msg-ask.png);
    background-color: #87eaea;
  }
  .mg-types .mg-tcont {
    float: left;
    padding-left: 18px;
  }
  .mg-types .mg-ttitle {
    height: 24px;
    line-height: 24px;
  }
  .mg-types .mg-ttitle a {
    color: #000;
    font-weight: 700;
  }
  .mg-types .mg-ttime {
    float: right;
    color: #676d70;
  }
  .mg-types .mg-illus {
    color: #676d70;
  }
</style>
