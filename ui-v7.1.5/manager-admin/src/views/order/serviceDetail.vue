<template>
  <div id="serviceDetail" class="serviceDetail">
    <el-row :gutter="0" style="padding:10px 20px 20px;">
      <el-col :span="24">
        <div class="after-sale-info">
          <div class="after-sale-left">
            <ul>
              <li>售后服务单号：<span>{{ serviceDetail.sn }}</span></li>
              <li>订单编号：<span>{{ serviceDetail.order_sn }}</span></li>
            </ul>
          </div>
          <div class="after-sale-right">
            <ul>
              <li>本次售后服务由商家<span>{{ serviceDetail.seller_name }}</span>提供</li>
              <li>售后服务类型：<span>{{ serviceDetail.service_type_text }}</span></li>
            </ul>
          </div>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="0">
      <el-col :span="24">
        <div class="d-header">商品列表</div>
        <el-table :data="serviceDetail.goods_list" :header-cell-style="{textAlign: 'center'}">
          <el-table-column label="商品图片" width="180">
            <template slot-scope="scope">
              <a :href="`${MixinBuyerDomain}/goods/${scope.row.goods_id}`" target="_blank">
                <img :src="scope.row.goods_image" class="goods-image"/>
              </a>
            </template>
          </el-table-column>
          <el-table-column label="商品名称" align="center">
            <template slot-scope="scope">
              <a :href="`${MixinBuyerDomain}/goods/${scope.row.goods_id}`" class="goods-name" target="_blank">{{ scope.row.goods_name }}</a>
            </template>
          </el-table-column>
          <el-table-column prop="price" label="商品价格"/>
          <el-table-column prop="ship_num" label="购买数量"/>
          <el-table-column prop="return_num" label="申请售后数量"/>
          <el-table-column prop="storage_num" label="入库数量" v-if="allowable.allow_show_storage_num"/>
        </el-table>
      </el-col>
    </el-row>
    <el-row :gutter="0">
      <el-col :span="24">
        <div class="d-header" style="margin-bottom: 18px;">服务单信息</div>
        <div class="d-content submit-from" v-if="allowable.allow_admin_refund">
          <el-form :model="refundForm" :rules="refundRules" ref="refundForm" label-width="100px" class="demo-ruleForm">
            <el-form-item label="退款金额：" prop="refund_price" v-if="refundShow">
              <el-input v-model="refundForm.refund_price" :maxlength="20" placeholder="请输入退款金额" style="width: 20%;" size="small"/>
            </el-form-item>
            <el-form-item label="退款备注：" prop="remark">
              <el-input type="textarea" placeholder="请输入退款备注" v-model="refundForm.remark" maxlength="150" show-word-limit rows="5" resize="none" style="width: 40%;"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="small" @click="submitRefundForm">退 款</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div class="d-content">
          <div class="item">
            <span class="item-label">售后服务单号：</span>
            <span class="item-value">{{ serviceDetail.sn }}</span>
          </div>
          <div class="item">
            <span class="item-label">订单编号：</span>
            <span class="item-value">{{ serviceDetail.order_sn }}</span>
          </div>
          <div class="item" v-if="serviceDetail.new_order_sn">
            <span class="item-label">新订单编号：</span>
            <span class="item-value">{{ serviceDetail.new_order_sn }}</span>
          </div>
          <div class="item">
            <span class="item-label">售后服务类型：</span>
            <span class="item-value">{{ serviceDetail.service_type_text }}</span>
          </div>
          <div class="item">
            <span class="item-label">售后服务状态：</span>
            <span class="item-value">{{ serviceDetail.service_status_text }}</span>
          </div>
          <div class="item" v-if="serviceDetail.service_type === 'RETURN_GOODS' || serviceDetail.service_type === 'CHANGE_GOODS'">
            <span class="item-label">商品返回方式：</span>
            <span class="item-value">客户期望处理方式为“快递至第三方卖家“</span>
          </div>
          <div class="item" v-if="serviceDetail.apply_vouchers">
            <span class="item-label">申请凭证：</span>
            <span class="item-value">{{ serviceDetail.apply_vouchers }}</span>
          </div>
          <div class="item">
            <span class="item-label">申请原因：</span>
            <span class="item-value">{{ serviceDetail.reason }}</span>
          </div>
          <div class="item" v-if="serviceDetail.problem_desc">
            <span class="item-label">问题描述：</span>
            <span class="item-value">
              {{ serviceDetail.problem_desc }}
              <div class="img-list" v-if="serviceDetail.images">
                <a :key="index" class="service-img-item" v-for="(image, index) in serviceDetail.images" :href="image.img" target="_blank">
                  <img :src="image.img" alt="">
                </a>
              </div>
            </span>
          </div>
          <div class="item">
            <span class="item-label">收货地址：</span>
            <span class="item-value">{{ `${change_info.province || '' } ${change_info.city || ''} ${change_info.county || ''} ${change_info.town || ''} ${change_info.ship_addr || ''} `}}</span>
          </div>
          <div class="item">
            <span class="item-label">联系信息：</span>
            <span class="item-value">联系人：{{ change_info.ship_name }}， 联系人电话：{{ change_info.ship_mobile }}</span>
          </div>
          <div class="item" v-if="allowable.allow_show_return_addr">
            <span class="item-label">退货地址信息：</span>
            <span class="item-value">{{ serviceDetail.return_addr }}</span>
          </div>
          <div class="item" v-if="allowable.allow_show_ship_info">
            <span class="item-label">发货单信息：</span>
            <span class="item-value">{{ express_info.ship_time | unixToDate('yyyy-MM-dd') }}， 快递公司：{{ express_info.courier_company }}， 运单号：{{ express_info.courier_number }}</span>
          </div>
          <div class="item" v-if="serviceDetail.audit_remark">
            <span class="item-label">审核备注：</span>
            <span class="item-value">{{ serviceDetail.audit_remark }}</span>
          </div>
          <div class="item" v-if="serviceDetail.stock_remark">
            <span class="item-label">入库备注：</span>
            <span class="item-value">{{ serviceDetail.stock_remark }}</span>
          </div>
          <div class="item" v-if="serviceDetail.stock_remark">
            <span class="item-label">退款备注：</span>
            <span class="item-value">{{ serviceDetail.refund_remark }}</span>
          </div>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="0"  v-if="refundShow">
      <el-col :span="24">
        <div class="d-header">退款信息</div>
        <div class="d-content">
          <div class="item">
            <span class="item-label">申请退款金额：</span>
            <span class="item-value">{{ refund_info.refund_price | unitPrice('￥') }}</span>
          </div>
          <div class="item" v-if="refund_info.agree_price">
            <span class="item-label">同意退款金额：</span>
            <span class="item-value">{{ refund_info.agree_price | unitPrice('￥') }}</span>
          </div>
          <div class="item" v-if="refund_info.actual_price">
            <span class="item-label">实际退款金额：</span>
            <span class="item-value">{{ refund_info.actual_price | unitPrice('￥') }}</span>
          </div>
          <div class="item" v-if="refund_info.actual_price">
            <span class="item-label">退款时间：</span>
            <span class="item-value">{{ refund_info.refund_time | unixToDate }}</span>
          </div>
          <div class="item">
            <span class="item-label">退款方式：</span>
            <span class="item-value">
              {{ refund_info.refund_way | refundWayFilter }}
              <el-tooltip placement="right" v-if="refund_info.refund_way === 'OFFLINE' && serviceDetail.service_status != 'COMPLETED'">
                <div slot="content">请联系买家获取退款账号后<br/>进行线下人工退款</div>
                <i class="el-icon-info"></i>
              </el-tooltip>
            </span>
          </div>
          <div class="item" v-if="accountShow">
            <span class="item-label">账户类型：</span>
            <span class="item-value">{{ refund_info.account_type | accountTypeFilter }}</span>
          </div>
          <div class="item" v-if="accountShow && !bankShow">
            <span class="item-label">退款账号：</span>
            <span class="item-value">{{ refund_info.return_account }}</span>
          </div>
          <div class="item" v-if="bankShow">
            <span class="item-label">银行名称：</span>
            <span class="item-value">{{ refund_info.bank_name }}</span>
          </div>
          <div class="item" v-if="bankShow">
            <span class="item-label">银行账号：</span>
            <span class="item-value">{{ refund_info.bank_account_number }}</span>
          </div>
          <div class="item" v-if="bankShow">
            <span class="item-label">银行开户名：</span>
            <span class="item-value">{{ refund_info.bank_account_name }}</span>
          </div>
          <div class="item" v-if="bankShow">
            <span class="item-label">银行开户行：</span>
            <span class="item-value">{{ refund_info.bank_deposit_name }}</span>
          </div>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="0">
      <el-col :span="24">
        <div class="d-header">日志信息</div>
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
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import * as API_AfterSale from '@/api/afterSale'
  import { RegExp } from '~/ui-utils'

  export default {
    name: 'serviceDetail',
    data() {
      return {
        // 售后服务单号
        service_sn: this.$route.params.service_sn,
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
        // 退款表单提交数据
        refundForm: {
          refund_price: '',
          remark: ''
        },
        // 退款表单验证
        refundRules: {
          // 退款金额
          refund_price: [{ required: true, message: '请填写退款金额！', trigger: 'blur' }]
        }
      }
    },
    beforeRouteUpdate(to, from, next) {
      this.service_sn = to.params.service_sn
      this.GET_AfterSaleServiceDetail()
      next()
    },
    activated() {
      this.service_sn = this.$route.params.service_sn
      this.GET_AfterSaleServiceDetail()
    },
    mounted() {
      this.GET_AfterSaleServiceDetail()
    },
    filters: {
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

      /** 提交退款数据表单 */
      submitRefundForm() {
        this.$refs['refundForm'].validate((valid) => {
          if (valid) {
            this.$confirm('请确定已经人工退款成功', '提示', { type: 'warning' }).then(() => {
              API_AfterSale.adminRefund(this.service_sn, this.refundForm).then(() => {
                this.$message.success('操作成功')
                this.GET_AfterSaleServiceDetail()
              })
            }).catch(() => {})
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
          this.refund_info = this.serviceDetail.refund_info
          this.refundForm.refund_price = this.refund_info === null ? 0.00 : this.refund_info.agree_price
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
  /deep/ .el-dialog__body {
    padding: 20px 25px;
    .logistics-info {
      max-height: 500px;
      overflow-y: scroll;
    }
  }
  /deep/ .el-form-item__label {
    color: #999;
  }
  .serviceDetail {
    background: #fff;
  }
  .d-header {
    padding: 10px 0 10px 20px;
    font-size: 14px;
    color: #333;
    background-color: #f5f7fa;
    border-bottom: 1px solid #e1e8ed;
    &h2 {
      font-size: 14px;
      font-weight: 400;
    }
  }
  .item {
    width: 100%;
    position: relative;
    display: inline-block;
    vertical-align: top;
    box-sizing: border-box;
    padding: 16px 0 16px 20px;
    line-height: 24px;
    white-space: nowrap;
    & .item-label {
      float: left;
      margin-right: 20px;
      font-size: 14px;
      color: #999;
    }
    & .item-value {
      overflow: hidden;
      white-space: normal;
      word-break: break-all;
      font-size: 14px;
      color: #666;
    }
  }
  /deep/ .el-table td:not(.is-left) {
    text-align: center;
  }
  .goods-image {
    width: 50px;
    height: 50px;
  }
  .goods-name {
    color: #4183c4;
    &:hover { color: #f42424 }
  }
  .after-sale-info {
    background: #fffdee;
    border: 1px solid #edd28b;
    padding: 10px;
    overflow: hidden;
    .after-sale-left{
      float: left;
      border-right: 1px solid #edd28b;
      width: 420px;
    }
    .after-sale-right{
      float: left;
    }
    ul{
      list-style: none;
      font-size: 14px;
      color: #333;
      li{
        line-height: 25px;
        span{
          color: #e4393c;
          font-weight: 500;
        }
      }
    }
  }
  .submit-from{
    padding-top: 20px;
    border: 1px solid #dcdfe6;
    margin: 20px;
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
    width: 86%;
    margin: 0 auto;
    padding: 5px 0 0 5px;
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
    background: url(../../assets/icon-repair.png) #fff center no-repeat;
  }
  .deal-cont .deal-item li {
    border-bottom: 1px dashed #dfdfdf;
    padding: 15px 0;
  }
  .deal-cont .deal-item li.fore1 {
    padding: 0 0 15px;
    margin-bottom: 10px;
    font-size: 16px;
    color: #666;
  }
  .deal-cont .deal-item .deal-msg {
    margin: 0 0 10px;
    strong{
      padding-bottom: 5px;
      display: inline-block;
    }
  }
  .deal-cont .deal-more {
    width: 86%;
    margin: 20px auto 0;
    padding: 0 0 0 42px;
  }
</style>
