<template>
  <div id="service-detail">
    <div class="member-nav">
      <ul class="member-nav-list">
        <li class="active">
          <a href="javascript:;" style="color: #f42424;">售后服务详情</a>
        </li>
      </ul>
    </div>
    <div class="mod-main mod-comm">
      <div class="mc">
        <div class="remind-box">
          <div class="after-sale-info">
            <div class="r-col">
              <div class="after-sale-item">
                <div class="after-sale-msg">
                  <div>售后服务单号：<span class="ftx-01">{{ serviceDetail.sn }}</span></div>
                  <div>订单编号：<span class="ftx-01">{{ serviceDetail.order_sn }}</span></div>
                </div>
              </div>
            </div>
            <div class="l-col">
              本次售后服务由<span class="ftx-01">{{ serviceDetail.seller_name }}</span>为您提供<br>
              <div>售后服务类型：<span class="ftx-01">{{ serviceDetail.service_type_text }}</span></div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="mod-main mod-comm">
      <div class="mc">
        <table class="tb-void mb10">
          <thead>
          <tr>
            <th width="280">商品名称</th>
            <th width="100">商品价格</th>
            <th width="70">购买数量</th>
            <th width="70">申请售后数量</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="goods in serviceDetail.goods_list" :key="goods.id">
            <td>
              <ul class="list-proinfo">
                <li class="fore1">
                  <a :href="'/goods/' + goods.goods_id" target="_blank">
                    <img height="50" width="50" :src="goods.goods_image" :title="goods.goods_name" :alt="goods.goods_name">
                  </a>
                  <div class="p-info">
                    <a :href="'/goods/' + goods.goods_id" target="_blank">{{ goods.goods_name }}</a>
                  </div>
                </li>
              </ul>
            </td>
            <td>
              ￥{{ (goods.price || 0) | unitPrice }}
            </td>
            <td>{{ goods.ship_num }}</td>
            <td>{{ goods.return_num }}</td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
    <div class="mod-main mod-comm">
      <div class="qww-promise-w">
        <div class="qww-promise">
          <em class="top">{{ serviceDetail.service_status_text }}<b>|</b></em>
          <span class="tips">{{ serviceDetail.service_status | statusFilter }}</span>
        </div>
      </div>
      <div class="mt">
        <h3>日志信息</h3>
      </div>
      <div class="mc">
        <div class="deal-cont">
          <div class="deal-items">
            <div class="deal-item ">
              <div class="deal-txt">
                审核环节<b></b>
              </div>
              <ul>
                <li class="fore1" v-for="log in logList" :key="log.id">
                  <div class="deal-msg">
                    <strong>处理信息：</strong>
                    <div>{{ log.log_detail }}
                    </div>
                  </div>
                  <div class="deal-opers">
                    <strong>操作人：</strong>{{ log.operator }}
                    ({{ log.log_time | unixToDate }})
                  </div>
                </li>
              </ul>
            </div>
            <div class="deal-more" v-if="logMoreShow">
              <a href="javascript:void(0);" class="ftx-05 deal-more-btn" v-if="!logShow" @click="handleOpenLogDetail">查看更多</a>
              <a href="javascript:void(0);" class="ftx-05 deal-more-btn" v-else @click="handleCloseLogDetail">收起</a>
            </div>
          </div>
        </div>
        <div style="color: red;">
          <strong>重要提醒：</strong>
          Javashop平台及销售商不会以
          <span color="red" style="font-weight:bold; font-size: 13px;">任何理由</span>
          ，要求您点击
          <span color="red" style="font-weight:bold; font-size: 13px;">任何网址链接</span>
          进行退款操作。
        </div>
      </div>
    </div>
    <div class="mod-main mod-comm">
      <div class="mt">
        <h3>服务单信息</h3>
      </div>
      <div class="mc" v-if="allowable.allow_ship">
        <div class="repair-box">
          <div class="form" style="margin: 0 10px;">
            <div style="margin: 20px 0px;">
              <el-alert v-if="serviceDetail.service_type === 'RETURN_GOODS'" title="物流信息填写完成，商家才会进行退款操作，请您及时填写，避免出现纠纷" type="warning" :closable="false" show-icon></el-alert>
              <el-alert v-else title="物流信息填写完成，商家才会对新订单进行发货操作，请您及时填写，避免出现纠纷" type="warning" :closable="false" show-icon></el-alert>
            </div>
            <el-form :model="expressForm" :rules="expressRules" ref="expressForm" label-width="100px" class="demo-ruleForm">
              <el-form-item label="快递公司：" prop="courier_company_id">
                <el-select v-model="expressForm.courier_company_id" filterable placeholder="请选择" size="small">
                  <el-option v-for="logi in logiList" :key="logi.id" :label="logi.name" :value="logi.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="快递单号：" prop="courier_number">
                <el-input v-model="expressForm.courier_number" :maxlength="180" placeholder="请输入快递单号" style="width: 50%;" size="small"/>
              </el-form-item>
              <el-form-item label="发货时间：" prop="ship_time">
                <el-date-picker
                  v-model="expressForm.ship_time"
                  type="date"
                  placeholder="选择日期"
                  :editable="false"
                  :picker-options="{disabledDate(time) { return (time.getTime() > Date.now() || time.getTime() < -2208988800000) }}"
                  size="small"
                  clearable
                  value-format="timestamp">
                </el-date-picker>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="submitExpressForm" size="small">提 交</el-button>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </div>
      <div class="mc">
        <table class="tb-void tb-left">
          <colgroup>
            <col width="110px">
            <col>
          </colgroup>
          <tbody>
          <tr v-if="serviceDetail.service_type === 'RETURN_GOODS' || serviceDetail.service_type === 'CHANGE_GOODS'">
            <td>商品返回方式</td>
            <td>客户期望处理方式为“快递至第三方卖家“</td>
          </tr>
          <tr>
            <td>商品处理方式</td>
            <td>客户期望处理方式为“{{ serviceDetail.service_type_text }}”</td>
          </tr>
          <tr v-if="serviceDetail.apply_vouchers">
            <td>申请凭证</td>
            <td>{{ serviceDetail.apply_vouchers }}</td>
          </tr>
          <tr>
            <td>申请原因</td>
            <td>{{ serviceDetail.reason }}</td>
          </tr>
          <tr v-if="serviceDetail.problem_desc">
            <td>问题描述</td>
            <td>
              <div class="row6col2">{{ serviceDetail.problem_desc }}
              </div>
              <div class="img-list" v-if="serviceDetail.images">
                <viewer :images="serviceDetail.images">
                  <a
                    :key="index"
                    class="service-img-item"
                    v-for="(image, index) in serviceDetail.images"
                  >
                    <img :src="image.img" alt="">
                  </a>
                </viewer>
              </div>
            </td>
          </tr>
          <tr>
            <td>收货地址</td>
            <td>
              <div class="row7col2">
                {{ `${change_info.province || '' } ${change_info.city || ''} ${change_info.county || ''} ${change_info.town || ''} ${change_info.ship_addr || ''} `}}
              </div>
            </td>
          </tr>
          <tr>
            <td>联系信息</td>
            <td>联系人：{{ change_info.ship_name }} &nbsp;&nbsp;手机：{{ change_info.ship_mobile | secrecyMobile }}</td>
          </tr>
          <tr v-if="allowable.allow_show_return_addr">
            <td>
              退货地址信息
              <el-tooltip placement="right">
                <div slot="content">请将申请售后的商品邮寄至此地址</div>
                <i class="el-icon-info"></i>
              </el-tooltip>
            </td>
            <td>{{ serviceDetail.return_addr }}</td>
          </tr>
          <tr v-if="allowable.allow_show_ship_info">
            <td>
              发货单信息
              <div class="jd-date">{{ express_info.ship_time | unixToDate('yyyy-MM-dd') }}</div>
            </td>
            <td class="clsExpressCode">
              快递公司：{{ express_info.courier_company }} &nbsp;&nbsp;运单号：{{ express_info.courier_number }}
            </td>
          </tr>
          <tr v-if="serviceDetail.new_order_sn">
            <td>新订单编号</td>
            <td>{{ serviceDetail.new_order_sn }}</td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
    <div class="mod-main mod-comm" v-if="refundShow">
      <div class="mt">
        <h3>退款信息</h3>
      </div>
      <div class="mc">
        <table class="tb-void tb-left">
          <colgroup>
            <col width="110px">
            <col>
          </colgroup>
          <tbody>
            <tr>
              <td>申请退款金额</td>
              <td>{{ refund_info.refund_price | unitPrice('￥') }}</td>
            </tr>
            <tr v-if="refund_info.agree_price">
              <td>同意退款金额</td>
              <td>{{ refund_info.agree_price | unitPrice('￥') }}</td>
            </tr>
            <tr v-if="refund_info.actual_price">
              <td>实际退款金额</td>
              <td style="color: red;">{{ refund_info.actual_price | unitPrice('￥') }}</td>
            </tr>
            <tr v-if="refund_info.actual_price">
              <td>退款时间</td>
              <td>{{ refund_info.refund_time | unixToDate }}</td>
            </tr>
            <tr>
              <td>退款方式</td>
              <td>{{ refund_info.refund_way | refundWayFilter }}</td>
            </tr>
            <tr v-if="accountShow">
              <td>账户类型</td>
              <td>{{ refund_info.account_type | accountTypeFilter }}</td>
            </tr>
            <tr v-if="accountShow && !bankShow">
              <td>退款账号</td>
              <td>{{ refund_info.return_account }}</td>
            </tr>
            <tr v-if="bankShow">
              <td>银行名称</td>
              <td>{{ refund_info.bank_name }}</td>
            </tr>
            <tr v-if="bankShow">
              <td>银行账号</td>
              <td>{{ refund_info.bank_account_number }}</td>
            </tr>
            <tr v-if="bankShow">
              <td>银行开户名</td>
              <td>{{ refund_info.bank_account_name }}</td>
            </tr>
            <tr v-if="bankShow">
              <td>银行开户行</td>
              <td>{{ refund_info.bank_deposit_name }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
  import Vue from 'vue'
  import * as API_AfterSale from '@/api/after-sale'
  import { RegExp } from '@/ui-utils'
  import 'viewerjs/dist/viewer.css'
  import Viewer from 'v-viewer'
  import { Select, Option, DatePicker, Tooltip } from 'element-ui'
  Vue.use(Viewer).use(Select).use(Option).use(DatePicker).use(Tooltip)
  export default {
    name: 'service-detail',
    head() {
      return {
        title: `售后服务详情-${this.site.title}`
      }
    },
    data() {
      return {
        // 售后服务单号
        service_sn: this.$route.query.service_sn,
        // 初始化页面展示信息
        serviceDetail: '',
        // 售后服务单允许操作信息
        allowable: '',
        // 日志信息集合
        logList: [],
        // 是否展示查看更多日志按钮
        logMoreShow: true,
        // 是否展示全部日志
        logShow: false,
        // 收货地址信息
        change_info: '',
        // 物流信息
        express_info: '',
        // 退款相关信息
        refund_info: '',
        // 是否展示退款相关信息
        refundShow: false,
        // 是否展示退款账号相关信息
        accountShow: false,
        // 是否展示银行卡相关信息
        bankShow: false,
        // 物流公司集合
        logiList: [],
        // 物流信息表单
        expressForm: {
          courier_company_id: '',
          courier_number: '',
          ship_time: ''
        },
        // 物流信息表单校验
        expressRules: {
          // 快递公司名称
          courier_company_id: [{ required: true, message: '请选择快递公司！', trigger: 'change' }],
          // 快递单号
          courier_number: [{ required: true, message: '请填写快递单号！', trigger: 'blur' }],
          // 发货时间
          ship_time: [{ required: true, message: '请填写发货时间！', trigger: 'blur' }]
        }
      }
    },
    mounted() {
      this.GET_AfterSaleServiceDetail()
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

      /** 展开全部日志信息 */
      handleOpenLogDetail() {
        this.logList = this.serviceDetail.logs
        this.logShow = true
      },

      /** 关闭全部日志信息 */
      handleCloseLogDetail() {
        this.logList = []
        this.logList.push(this.serviceDetail.logs[0])
        this.logShow = false
      },

      /** 填充物流信息 */
      submitExpressForm() {

        this.$refs['expressForm'].validate((valid) => {
          if (valid) {
            this.$confirm('请确认物流信息是否填写正确！', () => {
              const params = JSON.parse(JSON.stringify(this.expressForm));
              params.ship_time /= 1000;

              API_AfterSale.fillShipInfo(this.service_sn, params).then(() => {
                this.$message.success('提交成功')
                this.GET_AfterSaleServiceDetail()
              })
            })
          } else {
            this.$message.error('表单填写有误，请核对！')
            return false
          }
        })
      },

      /** 获取售后服务详情页面数据 */
      GET_AfterSaleServiceDetail() {
        API_AfterSale.getServiceDetail(this.service_sn).then(response => {
          this.serviceDetail = response
          this.allowable = this.serviceDetail.allowable
          this.change_info = this.serviceDetail.change_info
          this.express_info = this.serviceDetail.express_info
          this.logiList = this.serviceDetail.logi_list
          this.refund_info = this.serviceDetail.refund_info
          this.refundShow = this.serviceDetail.service_type === 'RETURN_GOODS' || this.serviceDetail.service_type === 'ORDER_CANCEL'
          this.accountShow = (this.serviceDetail.service_type === 'RETURN_GOODS' || this.serviceDetail.service_type === 'ORDER_CANCEL') && this.refund_info.refund_way === 'ACCOUNT'
          this.bankShow = (this.serviceDetail.service_type === 'RETURN_GOODS' || this.serviceDetail.service_type === 'ORDER_CANCEL') && this.refund_info.refund_way === 'ACCOUNT' && this.refund_info.account_type === 'BANKTRANSFER'

          this.logList = []
          if (this.serviceDetail.logs.length === 1) {
            this.logList = this.serviceDetail.logs
            this.logMoreShow = false
          } else {
            this.logList.push(this.serviceDetail.logs[0])
            this.logMoreShow = true
          }
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  table {
    border-collapse: collapse;
  }
  .mod-main {
    padding: 10px;
    background-color: #fff;
    margin-bottom: 20px;
  }
  .mod-main .mc {
    overflow: visible;
  }
  .mod-comm {
    padding: 10px 20px 20px;
  }
  .mod-comm .mc {
    line-height: 20px;
  }
  .mod-comm .mt {
    padding: 10px 0;
    display: block;
  }
  .mod-main .mt:after {
    content: ".";
    display: block;
    height: 0;
    clear: both;
    visibility: hidden;
  }
  .mod-comm .mt h3 {
    font-size: 12px;
    color: #666;
    font-family: '\5b8b\4f53';
    line-height: 18px;
    float: left;
  }
  .remind-box {
    background: #fffdee;
    border: 1px solid #edd28b;
    padding: 10px;
  }
  .after-sale-info {
    display: block;
  }
  .after-sale-info:after {
    content: ".";
    display: block;
    height: 0;
    clear: both;
    visibility: hidden;
  }
  .after-sale-info .r-col {
    float: left;
    border-right: 1px solid #edd28b;
  }
  .after-sale-info .l-col {
    float: left;
    font-size: 14px;
    font-weight: 700;
    padding: 10px 0 0 30px;
    line-height: 25px;
  }
  .ftx-01, .ftx01 {
    color: #e4393c;
  }
  .after-sale-info .after-sale-item {
    display: block;
  }
  .after-sale-info .after-sale-item:after {
    content: ".";
    display: block;
    height: 0;
    clear: both;
    visibility: hidden;
  }
  .after-sale-info .after-sale-item .after-sale-msg {
    float: left;
    margin-left: 20px;
    padding: 10px 0 0;
    line-height: 25px;
    width: 420px;
    white-space: nowrap;
    overflow: hidden;
    font-size: 14px;
    font-weight: 700;
  }
  .tb-void {
    line-height: 18px;
    text-align: center;
    border: 1px solid #f2f2f2;
    border-top: 0;
    color: #333;
    width: 100%;
  }
  .tb-void th {
    background: #f5f5f5;
    height: 32px;
    line-height: 32px;
    padding: 0 5px;
    text-align: center;
    font-weight: 400;
  }
  .tb-void td {
    border: 1px solid #f2f2f2;
    padding: 10px 5px;
  }
  .mod-main .tb-void td {
    padding: 10px;
  }
  .tb-left {
    text-align: left;
  }
  .list-proinfo {
    text-align: left;
    padding-left: 18px;
    padding-right: 18px;
  }
  .list-proinfo li {
    margin-bottom: 15px;
  }
  .list-proinfo .fore1 {
    overflow: hidden;
  }
  .mod-main .tb-void a {
    color: #333;
    &:hover {
      color: #e4393c;
    }
  }
  .list-proinfo .p-info {
    display: inline-block;
    height: 36px;
    line-height: 18px;
    vertical-align: middle;
    overflow: hidden;
    margin-left: 5px;
  }
  .qww-promise-w {
    height: 35px;
    position: relative;
    margin-left: 20px;
    border-bottom: 1px solid #f5f5f5;
  }
  .qww-promise-w .qww-promise {
    border-left: 2px solid #7abd53;
    background: #fff;
    height: 34px;
    line-height: 34px;
    padding: 0 20px 0 10px;
    color: #7abd53;
    overflow: hidden;
    position: absolute;
    width: 828px;
    left: -20px;
    top: -10px;
  }
  .qww-promise-w em {
    float: left;
    font-weight: 700;
    padding-right: 5px;
    font-size: 14px;
  }
  .qww-promise-w b {
    font-weight: 400;
    padding-left: 5px;
  }
  .qww-promise-w span {
    float: left;
  }
  .deal-cont {
    border: 1px solid #e6e6e6;
    padding: 20px 0;
  }
  .deal-cont .deal-items {
    position: relative;
  }
  .deal-cont .deal-item {
    position: relative;
    line-height: 20px;
    width: 445px;
    margin: 0 auto;
    padding: 5px 0 0 65px;
    border-left: 1px solid #7abd54;
  }
  .deal-cont .deal-item .deal-txt {
    position: absolute;
    top: -1px;
    left: -145px;
    width: 150px;
    text-align: right;
    color: #7abd54;
    font-weight: 700;
  }
  .deal-cont .deal-item .deal-txt b {
    display: inline-block;
    vertical-align: middle;
    width: 10px;
    height: 20px;
    overflow: hidden;
    margin-left: 10px;
    background: url(../../assets/images/icon-repair.png) #fff center no-repeat;
  }
  .deal-cont .deal-item li {
    border-bottom: 1px dashed #dfdfdf;
    padding: 15px 0;
  }
  .deal-cont .deal-item li.fore1 {
    padding: 0 0 15px;
  }
  .deal-cont .deal-item .deal-msg {
    margin: 0 0 10px;
  }
  .deal-cont .deal-more {
    width: 445px;
    margin: 20px auto 0;
    padding: 0 0 0 66px;
  }
  .ftx-05, .ftx05 {
    color: #005ea7;
    &:hover {
      color: red;
    }
  }
  .row6col2, .row7col2 {
    text-align: left;
    word-break: break-all;
    word-wrap: break-word;
  }
  .row6col2 {
    position: relative;
    padding-right: 55px;
    width: 620px;
  }
  .tb-void .img-list {
    padding: 10px 0;
  }
  .tb-void .service-img-item {
    float: left;
    display: block;
    width: 50px;
    height: 50px;
    margin-right: 10px;
    cursor: pointer;
    border: 1px solid #e2e2e2;
    padding: 1px;
    img {
      width: 100%;
      height: 100%;
    }
  }
  .repair-box {
    border: 1px solid #ff6c00;
  }
  .repair-box {
    border-color: #999;
  }
  .tip {
    margin-bottom: 20px;
  }
  .deal-more-btn {
    cursor: pointer;
  }
</style>
