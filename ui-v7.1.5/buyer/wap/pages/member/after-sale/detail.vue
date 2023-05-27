<template>
  <div id="after-sale-detail" style="background-color: #f7f7f7;">
    <van-nav-bar left-arrow title="服务单详情" @click-left="MixinRouterBack"></van-nav-bar>
    <div class="after-sale-container">
      <div class="seller-info">
        <span>本次售后服务将由<span>{{ serviceDetail.seller_name }}</span>提供</span>
      </div>
      <div class="log-info">
        <div class="status-info">
          <div class="status-info-box">
            <span class="status-val">{{ serviceDetail.service_status_text }}</span>
            <span class="status-tip" v-if="allowable.allow_ship">请您尽快将申请售后的商品退还给卖家</span>
            <span class="status-tip" v-else>{{ serviceDetail.service_status | statusFilter }}</span>
          </div>
        </div>
        <div class="log-box-bottom"></div>
        <nuxt-link :to="'./service-logs?service_sn=' + serviceDetail.sn">
          <div class="log-box-top">
            <div class="top01">
              <span>操作日志</span>
              <div class="log-first-show">
                <span>{{ log.log_detail }}</span>
              </div>
            </div>
            <van-icon name="arrow" style="margin-right: 5px;"/>
          </div>
        </nuxt-link>
      </div>
      <div class="goods-info">
        <div class="info-box">
          <span class="title">商品信息</span>
          <div class="goods-item" v-for="goods in goodsList" :key="goods.sku_id">
            <nuxt-link :to="'/goods/' + goods.goods_id">
              <div class="item-info">
                <div style="width: 30%;">
                  <img :src="goods.goods_image">
                </div>
                <div class="goods-name-info">
                  <span>{{ goods.goods_name }}</span>
                  <span>单价：{{ goods.price | unitPrice('￥') }}</span>
                </div>
              </div>
            </nuxt-link>
          </div>
        </div>
      </div>
      <div class="service-info" v-if="allowable.allow_ship">
        <div class="service-info-box">
          <div class="box-items" style="padding-bottom: 30px;">
            <span class="item-key" style="font-size: 15px;color: #2e2d2d;font-weight: bold;width: 100%;">填写物流信息</span>
          </div>
          <div class="box-items">
            <span class="item-key">快递公司</span>
            <div class="item-val" @click="handelExpressShow">
              <div style="flex-direction: row; align-items: center;">
                <span style="font-weight: bold;font-size: 16px;">{{ courier_company }}</span>
                <van-icon name="arrow" style="margin: 0px 5px;"/>
              </div>
            </div>
          </div>
          <div class="box-items">
            <span class="item-key">快递单号</span>
            <div class="item-val">
              <div style="flex-direction: row; align-items: center;">
                <van-field v-model="expressForm.courier_number" clearable right-icon="question-o" placeholder="请输入快递单号" maxlength="50"/>
              </div>
            </div>
          </div>
          <div class="box-items">
            <span class="item-key">发货时间</span>
            <div class="item-val" @click="shipTimeShow = true">
              <div style="flex-direction: row; align-items: center;">
                <span style="font-weight: bold;font-size: 16px;">{{ shipTimeText }}</span>
                <van-icon name="arrow" style="margin: 0px 5px;"/>
              </div>
            </div>
          </div>
          <div class="box-items" style="padding-top: 15px;">
            <span class="item-key"></span>
            <div class="item-val">
              <van-button type="danger" size="small" round style="width: 100px;" @click="submitShipInfo">提交</van-button>
            </div>
          </div>
        </div>
      </div>
      <div class="service-info">
        <div class="service-info-box">
          <div class="box-items">
            <span class="item-key">服务单号</span>
            <span class="item-val">{{ serviceDetail.sn }}</span>
          </div>
          <div class="box-items">
            <span class="item-key">订单编号</span>
            <span class="item-val">{{ serviceDetail.order_sn }}</span>
          </div>
          <div class="box-items" v-if="serviceDetail.new_order_sn">
            <span class="item-key">新订单编号</span>
            <span class="item-val">{{ serviceDetail.new_order_sn }}</span>
          </div>
          <div class="box-items">
            <span class="item-key">服务类型</span>
            <span class="item-val">{{ serviceDetail.service_type_text }}</span>
          </div>
          <div class="box-items">
            <span class="item-key">申请原因</span>
            <span class="item-val">{{ serviceDetail.reason }}</span>
          </div>
          <div class="box-items" v-if="serviceDetail.apply_vouchers">
            <span class="item-key">申请凭证</span>
            <span class="item-val">{{ serviceDetail.apply_vouchers }}</span>
          </div>
          <div class="box-items" v-if="serviceDetail.problem_desc">
            <span class="item-key">问题描述</span>
            <span class="item-val">{{ serviceDetail.problem_desc }}</span>
          </div>
          <div class="box-items" v-if="serviceDetail.images && serviceDetail.images.length != 0">
            <p>
              <img :src="img.img" v-for="(img, index) in serviceDetail.images" :key="img.id" @click="showImage = true;indexes = index" style="width: 50px;height: 50px;padding: 0px 5px;">
            </p>
          </div>
          <div class="box-items">
            <span class="item-key">收货地址</span>
            <span class="item-val">{{ `${change_info.province || '' } ${change_info.city || ''} ${change_info.county || ''} ${change_info.town || ''} ${change_info.ship_addr || ''} `}}</span>
          </div>
          <div class="box-items">
            <span class="item-key">联系人</span>
            <span class="item-val">{{ change_info.ship_name }}</span>
          </div>
          <div class="box-items">
            <span class="item-key">联系方式</span>
            <span class="item-val">{{ change_info.ship_mobile | secrecyMobile }}</span>
          </div>
          <div class="box-items" v-if="allowable.allow_show_return_addr">
            <span class="item-key">退货地址信息</span>
            <span class="item-val" style="color:#ff0000;" @click="returnAddrShow = true">点击查看</span>
          </div>
          <div class="box-items" v-if="allowable.allow_show_ship_info">
            <span class="item-key">发货单信息</span>
            <span class="item-val" style="color:#ff0000;" @click="shipInfoShow = true">点击查看</span>
          </div>
          <div class="react-view" v-if="refundShow" style="border-style: solid; border-width: 1px 0px 0px; border-color: rgb(238, 238, 238); margin-top: 24px; margin-bottom: 16px;"></div>
          <div v-if="refundShow">
            <div class="box-items">
              <span class="item-key">申请退款金额</span>
              <span class="item-val">{{ refund_info.refund_price | unitPrice('￥') }}</span>
            </div>
            <div class="box-items" v-if="refund_info.agree_price">
              <span class="item-key">同意退款金额</span>
              <span class="item-val">{{ refund_info.agree_price | unitPrice('￥') }}</span>
            </div>
            <div class="box-items" v-if="refund_info.actual_price">
              <span class="item-key">实际退款金额</span>
              <span class="item-val" style="color: red;">{{ refund_info.actual_price | unitPrice('￥') }}</span>
            </div>
            <div class="box-items" v-if="refund_info.actual_price">
              <span class="item-key">退款时间</span>
              <span class="item-val">{{ refund_info.refund_time | unixToDate }}</span>
            </div>
            <div class="box-items">
              <span class="item-key">退款方式</span>
              <span class="item-val">{{ refund_info.refund_way | refundWayFilter }}</span>
            </div>
            <div class="box-items" v-if="accountShow">
              <span class="item-key">账户类型</span>
              <span class="item-val">{{ refund_info.account_type | accountTypeFilter }}</span>
            </div>
            <div class="box-items" v-if="accountShow && !bankShow">
              <span class="item-key">退款账号</span>
              <span class="item-val">{{ refund_info.return_account }}</span>
            </div>
            <div class="box-items" v-if="bankShow">
              <span class="item-key">银行名称</span>
              <span class="item-val">{{ refund_info.bank_name }}</span>
            </div>
            <div class="box-items" v-if="bankShow">
              <span class="item-key">银行账号</span>
              <span class="item-val">{{ refund_info.bank_account_number }}</span>
            </div>
            <div class="box-items" v-if="bankShow">
              <span class="item-key">银行开户名</span>
              <span class="item-val">{{ refund_info.bank_account_name }}</span>
            </div>
            <div class="box-items" v-if="bankShow">
              <span class="item-key">银行开户行</span>
              <span class="item-val">{{ refund_info.bank_deposit_name }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- 图片预览 -->
    <van-image-preview v-model="showImage" :startPosition="indexes" :images="imagesList" @change="(index) => { indexes = index }" style="position: fixed;"></van-image-preview>
    <!-- 退货地址 -->
    <van-popup v-model="returnAddrShow" position="bottom" :overlay="true" style="position: fixed;">
      <div>
        <span class="addr-title">退货地址信息</span>
      </div>
      <div class="addr-info">{{ serviceDetail.return_addr }}</div>
    </van-popup>
    <!-- 退货地址 -->
    <van-popup v-model="shipInfoShow" position="bottom" :overlay="true" style="position: fixed;" v-if="allowable.allow_show_ship_info">
      <div>
        <span class="addr-title">发货单信息</span>
      </div>
      <div class="addr-info">快递公司：{{ express_info.courier_company }}，运单号:{{ express_info.courier_number }}，发货时间：{{ express_info.ship_time | unixToDate('yyyy-MM-dd') }}</div>
    </van-popup>
    <!-- 物流公司下拉框 -->
    <van-actionsheet v-model="expressShow" :actions="expressSelectActions" @select="onSelectExpress" style="position: fixed;"/>
    <!-- 发货日期 -->
    <van-actionsheet v-model="shipTimeShow" style="position: fixed;">
      <van-datetime-picker
        v-model="shipTime"
        type="date"
        :min-date="new Date(1900,1,1)"
        :max-date="new Date()"
        @cancel="shipTimeShow = false"
        @confirm="handleConfirmShipTime"
      />
    </van-actionsheet>
  </div>
</template>

<script>
  import Vue from 'vue'
  import { ImagePreview } from 'vant'
  Vue.use(ImagePreview)
  import { Foundation, RegExp } from '@/ui-utils'
  import * as API_AfterSale from '@/api/after-sale'
  export default {
    name: 'after-sale-detail',
    head() {
      return {
        title: `售后详情-${this.site.title}`
      }
    },
    data() {
      return {
        /** 售后服务单号 */
        service_sn: this.$route.query.service_sn,
        /** 售后服务详情 */
        serviceDetail: '',
        /** 最新一条日志信息 */
        log: '',
        /** 申请售后商品信息集合 */
        goodsList: [],
        /** 售后服务单允许操作信息 */
        allowable: '',
        /** 收货地址相关信息 */
        change_info: '',
        /** 退款相关信息 */
        refund_info: '',
        /** 发货单信息 */
        express_info: '',
        /** 是否展示退款相关信息 */
        refundShow: false,
        /** 是否展示退款账号相关信息 */
        accountShow: false,
        /** 是否展示银行卡相关信息 */
        bankShow: false,
        /** 是否展示图片预览 */
        showImage: false,
        /** 展示的图片下标 */
        indexes: 0,
        /** 预览图片信息集合 */
        imagesList: [],
        /** 物流公司集合 */
        logiList: [],
        /** 是否展示退货地址弹出框 */
        returnAddrShow: false,
        /** 物流信息表单 */
        expressForm: {
          courier_company_id: '',
          courier_number: '',
          ship_time: 0
        },
        /** 是否显示快递公司下拉框 */
        expressShow: false,
        /** 快递公司名称 */
        courier_company: '请选择快递公司',
        /** 快递公司下拉框数据 */
        expressSelectActions: [],
        /** 是否显示发货日期时间选择器 */
        shipTimeShow: false,
        /** 发货日期（默认当前时间）*/
        shipTime: new Date(),
        shipTimeText: '其选择发货时间',
        /** 是否显示发货单信息 */
        shipInfoShow: false
      }
    },
    mounted() {
      this.GET_AfterSaleDetail()
    },
    filters: {
      statusFilter(val) {
        switch (val) {
          case 'APPLY':
            return '售后服务申请成功，等待商家审核'
          case 'PASS':
            return '售后服务申请审核通过'
          case 'REFUSE':
            return '售后服务申请已被商家拒绝，如有疑问请及时联系商家'
          case 'FULL_COURIER':
            return '申请售后的商品已经寄出，等待商家收货'
          case 'STOCK_IN':
            return '商家已将售后商品入库'
          case 'WAIT_FOR_MANUAL':
            return '等待平台进行人工退款'
          case 'REFUNDING':
            return '商家退款中，请您耐心等待'
          case 'COMPLETED':
            return '售后服务已完成，感谢您对Javashop的支持'
          case 'ERROR_EXCEPTION':
            return '系统生成新订单异常，等待商家手动创建新订单'
          case 'CLOSED':
            return '售后服务已关闭'
          default:
            return ''
        }
      },
      refundWayFilter(val) {
        switch (val) {
          case 'ACCOUNT':
            return '账户退款'
          case 'OFFLINE':
            return '线下退款'
          case 'ORIGINAL':
            return '原路退回'
          default:
            return ''
        }
      },
      accountTypeFilter(val) {
        switch (val) {
          case 'WEIXINPAY':
            return '微信'
          case 'ALIPAY':
            return '支付宝'
          case 'BANKTRANSFER':
            return '银行卡'
          default:
            return ''
        }
      }
    },
    methods: {
      /** 展示快递公司下拉框 */
      handelExpressShow() {
        this.expressSelectActions = []
        this.logiList.forEach(item => {
          let param = {}
          param.name = item.name
          param.value = item.id
          this.expressSelectActions.push(param)
        })
        this.expressShow = true
      },

      /** 快递公司下拉框选中事件绑定 */
      onSelectExpress(item) {
        this.expressShow = false
        this.courier_company = item.name
        this.expressForm.courier_company_id = item.value
      },

      /** 确认发货时间 */
      handleConfirmShipTime() {
        this.expressForm.ship_time = parseInt(new Date(this.shipTime).getTime() / 1000)
        this.shipTimeText = Foundation.unixToDate(this.expressForm.ship_time, 'yyyy-MM-dd')
        this.shipTimeShow = false
      },

      /** 提交物流信息 */
      submitShipInfo() {
        // 物流公司校验
        if (!this.expressForm.courier_company_id) {
          this.$message.error('请选择快递公司！')
          return false
        }
        // 快递单号校验
        if (!this.expressForm.courier_number) {
          this.$message.error('请填写快递单号！')
          return false
        }
        // 申请原因校验
        if (!this.expressForm.ship_time || this.expressForm.ship_time === 0) {
          this.$message.error('请选择发货时间！')
          return false
        }

        API_AfterSale.fillShipInfo(this.service_sn, this.expressForm).then(() => {
          this.$message.success('提交成功')
          this.GET_AfterSaleDetail()
        })
      },

      GET_AfterSaleDetail() {
        API_AfterSale.getServiceDetail(this.service_sn).then(response => {
          this.serviceDetail = response
          this.log = this.serviceDetail.logs[0]
          this.allowable = this.serviceDetail.allowable
          this.goodsList = this.serviceDetail.goods_list
          this.change_info = this.serviceDetail.change_info
          this.refund_info = this.serviceDetail.refund_info
          this.express_info = this.serviceDetail.express_info
          this.logiList = this.serviceDetail.logi_list
          this.refundShow = this.serviceDetail.service_type === 'RETURN_GOODS' || this.serviceDetail.service_type === 'ORDER_CANCEL'
          this.accountShow = (this.serviceDetail.service_type === 'RETURN_GOODS' || this.serviceDetail.service_type === 'ORDER_CANCEL') && this.refund_info.refund_way === 'ACCOUNT'
          this.bankShow = (this.serviceDetail.service_type === 'RETURN_GOODS' || this.serviceDetail.service_type === 'ORDER_CANCEL') && this.refund_info.refund_way === 'ACCOUNT' && this.refund_info.account_type === 'BANKTRANSFER'
          var imgList = []
          this.serviceDetail.images.forEach(item => {
            imgList.push(item.img)
          })
          this.imagesList = imgList
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../../assets/styles/color";
  /deep/ .van-cell {
    padding: 10px 0px;
    font-size: 12px;
  }
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
  .seller-info {
    background-color: rgb(255, 255, 255);
    align-items: center;
    height: 27px;
    flex-direction: row;
    justify-content: center;

    span {
      display: inline-block;
      margin: 0px;
      padding: 0px;
      font-family: PingFangSC-Regular;
      font-size: 12px;
      color: rgb(140, 140, 140);
      line-height: 14.4px;

      span {
        display: inline;
        color: rgb(242, 48, 48);
      }
    }
  }
  .log-info {
    flex-direction: column;
    background-color: rgb(247, 247, 247);

    .status-info {
      flex-direction: row;
      background-image: linear-gradient(14deg, #FF614D 0%, #FE7552 100%);

      .status-info-box {
        height: 110px;
        flex-direction: column;
        padding-left: 27px;
        padding-right: 27px;

        span {
          display: inline-block;
          margin: 13px 0px 0px;
          padding: 0px;
          font-family: PingFangSC-Regular;
          color: rgb(255, 255, 255);
          background-color: rgba(0, 0, 0, 0);
          line-height: 21.6px;
          font-weight: bold;
        }
        .status-val {
          font-size: 18px;
        }
        .status-tip {
          font-size: 12px;
        }
      }
    }
  }
  .log-box-bottom {
    height: 73px;
    flex-direction: column;
    background-color: rgb(247, 247, 247);
  }
  .log-box-top {
    height: 94px;
    flex-direction: row;
    background-color: rgb(255, 255, 255);
    position: absolute;
    top: 88px;
    left: 0px;
    right: 0px;
    bottom: 0px;
    margin-left: 11px;
    margin-right: 11px;
    border-radius: 11px;
    justify-content: space-between;
    align-items: center;
    padding-bottom: 26px;
    padding-top: 26px;
    padding-left: 16px;

    .top01 {
      width: 90%;
      span {
        display: -webkit-box;
        margin: 0px 5px 0px 0px;
        padding: 0px;
        font-family: PingFangSC-Regular;
        font-size: 12px;
        line-height: 15px;
        color: rgb(46, 45, 45);
        overflow: hidden;
        text-overflow: ellipsis;
        overflow-wrap: break-word;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
        max-height: 30px;
        white-space: normal;
      }

      .log-first-show {
        flex-direction: row;
        margin-top: 8px;
        margin-right: 22px;
        span {
          display: inline-block;
          margin: 0px 3px 0px 0px;
          padding: 0px;
          font-family: PingFangSC-Regular;
          font-size: 11px;
          color: rgb(140, 140, 140);
          line-height: 15px;
        }
      }
    }
  }
  .goods-info {
    margin-top: 16px;
    background-color: rgb(247, 247, 247);
    flex-direction: column;
    .info-box {
      margin-left: 11px;
      margin-right: 11px;
      flex-direction: column;
      background-color: rgb(255, 255, 255);
      border-radius: 13px;
      justify-content: center;
      border-style: solid;
      border-width: 1px;
      border-color: rgb(255, 255, 255);
      padding-left: 16px;
      padding-right: 22px;
      .title {
        display: inline-block;
        margin: 24px 0px 0px;
        padding: 0px;
        font-family: PingFangSC-Regular;
        font-size: 15px;
        color: rgb(46, 45, 45);
        font-weight: bold;
        line-height: 18px;
      }
      .goods-item {
        padding-bottom: 16px;
        padding-top: 16px;
        .item-info {
          flex-direction: row;
          align-items: center;
          img {
            justify-content: flex-end;
            align-items: flex-end;
            height: 66px;
            width: 66px;
            margin-right: 22px;
          }
          .goods-name-info {
            height: 66px;
            width: 70%;
            justify-content: space-between;
            span {
              display: -webkit-box;
              margin: 0px;
              padding: 0px;
              color: rgb(132, 132, 132);
              font-size: 11px;
              line-height: 17px;
              text-align: left;
              overflow: hidden;
              text-overflow: ellipsis;
              overflow-wrap: break-word;
              -webkit-line-clamp: 2;
              -webkit-box-orient: vertical;
              max-height: 34px;
              white-space: normal;
            }
          }
        }
      }
    }
  }
  .service-info {
    margin-top: 16px;
    background-color: rgb(247, 247, 247);
    flex-direction: column;
    margin-bottom: 16px;
    .service-info-box {
      margin-left: 11px;
      margin-right: 11px;
      flex-direction: column;
      background-color: rgb(255, 255, 255);
      justify-content: center;
      border-style: solid;
      border-width: 1px;
      border-color: rgb(255, 255, 255);
      border-radius: 13px;
      padding:16px;
      .box-items {
        flex-direction: row;
        justify-content: flex-start;
        margin-right: 5px;
        align-items: center;
        padding-bottom: 13px;
        span {
          display: inline-block;
          padding: 0px;
          font-family: PingFangSC-Regular;
          font-size: 12px;
        }
        .item-key {
          margin: 0px 2px 0px 0px;
          color: rgb(132, 132, 132);
          width: 85px;
          line-height: 14.4px;
        }
        .item-val {
          margin: 0px 5px 0px 0px;
          line-height: 20px;
          color: rgb(46, 45, 45);
        }
      }
    }
  }
  .addr-title {
    height: 44px;
    line-height: 44px;
    text-align: center;
    font-size: 16px;
    font-weight: bold;
    border-bottom: 1px solid #d6d6d6;
  }
  .addr-info {
    padding: 25px;
    font-size: 13px;
    font-weight: bold;
  }
</style>
