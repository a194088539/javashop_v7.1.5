<template>
  <div id="after-sale">
    <van-nav-bar left-arrow title="操作日志" @click-left="MixinRouterBack"></van-nav-bar>
    <div class="after-sale-container">
      <div class="log-item" v-for="(log, index) in logList" :key="index">
        <div class="item-line" v-if="index === 0">
          <div class="line-top"></div>
          <div class="mark"></div>
          <div class="line-down"></div>
        </div>
        <div class="item-line" v-else>
          <div class="line-top-more"></div>
          <div class="mark-more"></div>
          <div class="line-down"></div>
        </div>
        <div class="log-detail">
          <div style="height: 16px;"></div>
          <div class="detail-box">
            <span class="box-title">处理信息</span>
            <span class="box-content">{{ log.log_detail }}</span>
            <span class="box-time">{{ log.operator }}（{{ log.log_time | unixToDate }}）</span>
            <div style="height: 16px;"></div>
            <div class="bottom-line"></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
  import * as API_AfterSale from '@/api/after-sale'
  export default {
    name: 'service-logs',
    head() {
      return {
        title: `售后详情-${this.site.title}`
      }
    },
    data() {
      return {
        /** 售后服务单号 */
        service_sn: this.$route.query.service_sn,
        /** 申请售后商品信息集合 */
        logList: []
      }
    },
    mounted() {
      this.GET_AfterSaleLogs()
    },
    methods: {
      GET_AfterSaleLogs() {
        API_AfterSale.getServiceLogs(this.service_sn).then(response => {
          this.logList = response
        })
      }
    }
  }
</script>
<style type="text/scss" lang="scss" scoped>
  @import "../../../assets/styles/color";
  div {
    position: relative;
    box-sizing: border-box;
    display: flex;
    -webkit-box-orient: vertical;
    -webkit-box-direction: normal;
    flex-direction: column;
    flex-shrink: 0;
  }
  .after-sale-container {
    padding-top: 46px;
  }
  .log-item {
    flex-direction: row;
    justify-content: flex-start;
    .item-line {
      flex-direction: column;
      width: 55px;
      align-items: center;
      .line-top {
        height: 16px;
        width: 1px;
        background-color: rgb(255, 255, 255);
      }
      .mark {
        width: 12px;
        height: 12px;
        border-radius: 6px;
        background-color: rgb(240, 37, 15);
        border-style: solid;
        border-width: 2px;
        border-color: rgba(240, 37, 15, 0.2);
      }
      .line-down {
        flex: 1 1 0%;
        width: 1px;
        background-color: rgb(227, 229, 233);
      }
      .line-top-more {
        height: 16px;
        width: 1px;
        background-color: rgb(227, 229, 233);
      }
      .mark-more {
        width: 8px;
        height: 8px;
        border-radius: 5px;
        background-color: rgb(191, 191, 191);
      }
    }
    .log-detail {
      flex-direction: column;
      flex: 1 1 0%;
      .detail-box {
        flex-direction: column;
        span {
          display: inline-block;
          padding: 0px;
          font-family: PingFangSC-Semibold;
        }
        .box-title {
          margin: 0px 0px 13px;
          font-size: 15px;
          color: rgb(46, 45, 45);
          font-weight: bold;
          line-height: 18px;
        }
        .box-content {
          margin: 0px 0px 8px;
          font-size: 13px;
          color: rgb(46, 45, 45);
          line-height: 16px;
        }
        .box-time {
          margin: 0px;
          font-size: 11px;
          color: rgb(132, 132, 132);
          line-height: 15px;
        }
        .bottom-line {
          height: 1px;
          background-color: rgb(238, 238, 238);
          position: absolute;
          bottom: 0px;
          left: 0px;
          right: 0px;
          width: 90%;
        }
      }
    }
  }
</style>
