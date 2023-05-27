<template>
  <div id="message-index">
    <div class="member-nav">
      <ul class="member-nav-list">
        <li class="active">
          <nuxt-link to="./message-index">站内消息</nuxt-link>
        </li>
      </ul>
    </div>
    <div class="message-container">
      <ul>
        <li class="mg-types">
          <div class="msg-box" @click="showSystemMessage(no_read.system_num)">
            <span class="mg-timg actimg"></span>
            <div class="mg-tcont">
              <div class="mg-ttitle">
                <div style="float:left;width:80px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;">
                  <a href="javascript:void(0);" title="系统消息">
                    系统消息
                  </a>
                </div>
                <span class="mg-ttime"></span>
              </div>
              <span class="mg-illus">
                <div style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;width:135px;float:left">
                  <span color="grey" v-if="no_read.system_num === 0">暂无未读消息</span>
                  <span style="color: #b50005;" v-if="no_read.system_num != 0">有{{ no_read.system_num }}条未读消息</span>
                </div>
              </span>
            </div>
          </div>
        </li>
        <li class="mg-types">
          <div class="msg-box" @click="showAskMessage(no_read.ask_num)">
            <span class="mg-timg simg"></span>
            <div class="mg-tcont">
              <div class="mg-ttitle">
                <div style="float:left;width:80px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;">
                  <a href="javascript:void(0);" title="问答消息">
                    问答消息
                  </a>
                </div>
                <span class="mg-ttime"></span>
              </div>
              <span class="mg-illus">
                <div style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;width:135px;float:left">
                  <span color="grey" v-if="no_read.ask_num === 0">暂无未读消息</span>
                  <span style="color: #b50005;" v-if="no_read.ask_num != 0">有{{ no_read.ask_num }}条未读消息</span>
                </div>
              </span>
            </div>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
  import * as API_Message from '@/api/message'
  export default {
    name: 'message-index',
    head() {
      return {
        title: `站内消息-${this.site.title}`
      }
    },
    data() {
      return {
        no_read: ''
      }
    },
    mounted() {
      this.GET_NoReadMessageNum()
    },
    methods: {
      showSystemMessage(num) {
        if (num === 0) {
          this.$router.push({path: '/member/website-message', query: { read: 1}})
        } else {
          this.$router.push({path: '/member/website-message', query: { read: 0}})
        }
      },

      showAskMessage(num) {
        if (num === 0) {
          this.$router.push({path: '/member/ask-message', query: { is_read: 'YES'}})
        } else {
          this.$router.push({path: '/member/ask-message', query: { is_read: 'NO'}})
        }
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
    padding-top: 10px;
  }
  .mg-types {
    height: 52px;
    padding: 30px 18px 12px 16px;
    cursor: pointer;
  }
  .msg-box {
    position: relative;
    padding: 10px 20px;
    background-color: #f3f3f3;
    border: 1px solid #FFF;
    text-align: left;
    height: 52px;
  }
  .msg-box:hover {
    border: 1px solid #e4df45;
  }
  .mg-types .mg-timg {
    width: 51px;
    height: 51px;
    border-radius: 25.5px;
    float: left;
    background-image: url(../../assets/images/icons-message.png);
    position: relative;
  }
  .mg-types .actimg {
    background-position: -70px -306px;
  }
  .mg-types .simg {
    background-position: -6px -189px;
  }
  .mg-types .mg-tcont {
    float: left;
    padding-left: 18px;
    width: 150px;
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
