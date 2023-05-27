<template>
  <div id="apply-cancel">
    <nav-bar title="取消订单" fixed/>
    <div class="main-page">
      <van-radio-group v-model="rog">
        <van-cell-group>
          <van-cell title="未收货" clickable @click="rog = 'NO'">
            <van-radio slot="right-icon" name="NO" />
          </van-cell>
          <van-cell title="已收货" clickable @click="rog = 'YES'" v-if="applyService">
            <van-radio slot="right-icon" name="YES" />
          </van-cell>
        </van-cell-group>
      </van-radio-group>
      <div class="cancel-main-page" v-if="rog === 'NO'">
        <div class="cancle_tip_bar">
          <div>温馨提示：</div>
          <div>1. 订单取消后无法恢复；</div>
          <div>2. 订单取消后，使用的优惠券和积分等将不再返还；</div>
          <div>3. 订单取消后，订单中的赠品要随商品一起返还给商家；</div>
        </div>
        <van-cell-group>
          <!-- 退款方式 -->
          <van-cell title="退款方式" value="原路退回" v-if="isRetrace"/>
          <van-cell title="退款方式" value="账号退款" v-else/>
          <!-- 退款金额 -->
          <van-cell title="退款金额" :value="refund_info.refund_price"/>
          <!-- 取消原因 -->
          <van-cell title="取消原因" is-link arrow-direction="down" :value="refund_info.reason" @click="handleShowReason" required></van-cell>
          <van-actionsheet v-model="reasonSelectShow" :actions="reasonSelectActions" @select="onSelectReason" />
          <div v-if="!isRetrace">
            <!-- 账户类型 -->
            <van-cell title="账户类型" is-link arrow-direction="down" :value="accountTypeText" @click="handleShowAccountType" required></van-cell>
            <van-actionsheet v-model="accountTypeSelectShow" :actions="accountTypeSelectActions" @select="onSelectAccountType" />
            <div v-if="refund_info.account_type === 'BANKTRANSFER'">
              <!-- 银行名称 -->
              <van-field v-model="refund_info.bank_name" required clearable label="银行名称" right-icon="question-o" placeholder="请输入银行名称"/>
              <!-- 银行开户行 -->
              <van-field v-model="refund_info.bank_deposit_name" required clearable label="银行开户行" right-icon="question-o" placeholder="请输入银行开户行"/>
              <!-- 银行开户名 -->
              <van-field v-model="refund_info.bank_account_name" required clearable label="银行开户名" right-icon="question-o" placeholder="请输入银行开户名"/>
              <!-- 银行账号 -->
              <van-field v-model="refund_info.bank_account_number" required clearable label="银行账号" right-icon="question-o" placeholder="请输入银行账号"/>
            </div>
            <!-- 退款账号 -->
            <van-field v-model="refund_info.return_account" required clearable label="退款账号" right-icon="question-o" placeholder="请输入退款账号" v-else/>
          </div>
          <!-- 联系方式 -->
          <van-field v-model="refund_info.mobile" required clearable label="联系方式" right-icon="question-o" placeholder="请输入手机号码"/>
        </van-cell-group>
        <div class="submit-btn">
          <van-button round type="default" style="margin-right: 30px;width: 40%;" @click="handleCancelSubmit">取消</van-button>
          <van-button round type="primary" style="width: 40%;" @click="handleSubmitApply">提交</van-button>
        </div>
      </div>
      <div class="service-main-page" v-else>
        <div class="cancle_tip_bar">
          <div>温馨提示：</div>
          <div>1. 当前订单还未确认收货，如果申请售后，则订单自动确认收货；</div>
          <div>2. 如申请售后，使用的优惠券和积分等将不再返还；</div>
          <div>3. 订单中的赠品要随申请售后的商品一起返还给商家；</div>
        </div>
        <div class="submit-btn">
          <van-button round type="primary" style="width: 40%;" @click="handleApplyService">申请售后</van-button>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
  import Vue from 'vue'
  import { Radio, RadioGroup, Actionsheet } from 'vant'
  Vue.use(Radio).use(RadioGroup).use(Actionsheet)
  import { Foundation, RegExp } from '@/ui-utils'
  import * as API_AfterSale from '@/api/after-sale'
  import * as API_Order from '@/api/order'
  export default {
    name: 'apply-cancel',
    data() {
      return {
        /** 订单编号 */
        order_sn: this.$route.query.order_sn,
        /** 订单详细 */
        order: '',
        /** 是否允许申请售后 */
        applyService: false,
        /** 是否已收货 */
        rog: 'NO',
        /** 是否支持原路退款 */
        isRetrace: false,
        /** 申请取消订单参数 */
        refund_info: {
          reason: '请选择取消原因',
          mobile: '',
          account_type: '',
          return_account: '',
          refund_price: 0.00
        },
        /** 是否展示取消原因下拉框 */
        reasonSelectShow: false,
        /** 取消原因下拉框数据 */
        reasonSelectActions: [
          { name: '商品无货' },
          { name: '配送时间问题' },
          { name: '不想要了' },
          { name: '商品信息填写错误' },
          { name: '地址信息填写错误' },
          { name: '商品降价' },
          { name: '货物破损已拒签' },
          { name: '订单无物流跟踪记录' },
          { name: '非本人签收' },
          { name: '其他' }
        ],
        /** 账户类型下拉框选中的值 */
        accountTypeText: '请选择账户类型',
        /** 是否展示账户类型下拉框 */
        accountTypeSelectShow: false,
        /** 账户类型下拉框数据 */
        accountTypeSelectActions: [
          { name: '支付宝', value: 'ALIPAY' },
          { name: '微信', value: 'WEIXINPAY' },
          { name: '银行卡', value: 'BANKTRANSFER' }
        ]
      }
    },
    mounted() {
      this.GET_OrderCancelDetail()
    },
    methods: {
      /** 展示申请原因上拉框事件绑定 */
      handleShowReason() {
        this.reasonSelectShow = true
      },

      /** 申请原因选中事件绑定 */
      onSelectReason(item) {
        this.reasonSelectShow = false
        this.refund_info.reason = item.name
      },

      /** 展示账户类型上拉框事件绑定 */
      handleShowAccountType() {
        this.accountTypeSelectShow = true
      },

      /** 账户类型选中事件绑定 */
      onSelectAccountType(item) {
        this.accountTypeSelectShow = false
        this.accountTypeText = item.name
        this.refund_info.account_type = item.value
      },

      /** 跳转至订单列表页面 */
      handleCancelSubmit() {
        this.$router.replace({path: '/member/my-order', query: {}})
      },

      /** 申请售后 */
      handleApplyService() {
        API_Order.confirmReceipt(this.order_sn).then(() => {
          this.$router.replace({ path: '/member/after-sale'})
        })
      },

      /** 校验参数 */
      handleCheckParams() {
        // 取消原因校验
        if (!this.refund_info.reason || this.refund_info.reason === '请选择取消原因') {
          this.$message.error('请选择取消原因！')
          return false
        }
        // 联系方式校验
        if (!this.refund_info.mobile || !RegExp.mobile.test(this.refund_info.mobile)) {
          this.$message.error('请输入正确格式的手机号码！')
          return false
        }

        // 如果不支持原路退款
        if (!this.isRetrace) {
          // 账户类型校验
          if (!this.refund_info.account_type) {
            this.$message.error('请选择账户类型！')
            return false
          }

          // 如果账户类型不为银行卡
          if (this.refund_info.account_type != 'BANKTRANSFER') {
            // 退款账号校验
            if (!this.refund_info.return_account) {
              this.$message.error('请输入退款账号！')
              return false
            }
          } else {
            // 银行名称校验
            if (!this.refund_info.bank_name) {
              this.$message.error('请输入银行名称！')
              return false
            }
            // 银行开户行校验
            if (!this.refund_info.bank_deposit_name) {
              this.$message.error('请输入银行开户行！')
              return false
            }
            // 银行开户名校验
            if (!this.refund_info.bank_account_name) {
              this.$message.error('请输入银行开户名！')
              return false
            }
            // 银行账号校验
            if (!this.refund_info.bank_account_number) {
              this.$message.error('请输入银行账号！')
              return false
            }
          }
        }

        return true
      },

      /** 提交取消订单申请 */
      handleSubmitApply() {

        // 校验参数
        if (!this.handleCheckParams()) {
          return false
        }

        this.refund_info.order_sn = this.order_sn

        API_AfterSale.applyCancelOrder(this.refund_info).then(() => {
          this.dialogCancelVisible = false
          this.$message.success('提交成功')
          this.handleCancelSubmit()
        })
      },

      /** 获取订单详情信息 */
      GET_OrderCancelDetail() {
        API_Order.getOrderDetail(this.order_sn).then(response => {
          this.order = response
          this.isRetrace = this.order.is_retrace
          this.refund_info.refund_price = Foundation.formatPrice(this.order.order_price)
          this.applyService = this.order.order_status === 'SHIPPED' && this.order.ship_status === 'SHIP_YES'
        })
      }
    }
  }
</script>
<style type="text/scss" lang="scss" scoped>
  @import "../../../assets/styles/color";
  /deep/ .van-field__control {
    text-align: right;
  }
  .main-page {
    margin-top: 46px;
  }
  .cancle_tip_bar {
    padding: 8px 10px;
    font-size: 12px;
    line-height: 18px;
    background: #fcf6ed;
    color: #de8c17;
  }
  .submit-btn {
    text-align: center;
    margin-top: 20px;
  }
</style>
