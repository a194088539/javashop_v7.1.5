<template>
  <div id="my-order">
    <div class="member-nav">
      <ul class="member-nav-list">
        <li v-for="item in navList" :key="item.status">
          <nuxt-link v-if="!item.status" to="./my-order">{{ item.title }}</nuxt-link>
          <nuxt-link v-else :to="'./my-order?order_status=' + item.status">{{ item.title }}</nuxt-link>
        </li>
      </ul>
    </div>
    <div class="order-search">
      <template>
        <el-date-picker
          v-model="order_time_range"
          type="daterange"
          align="center"
          :editable="false"
          unlink-panels
          :default-time="['00:00:00', '23:59:59']"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :picker-options="{ disabledDate (time) { return time.getTime() - 1 >= new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate()).getTime() + 86400000 - 1 }, shortcuts: MixinPickerShortcuts }">
        </el-date-picker>
      </template>
      <input type="text" v-model="params.key_words" placeholder="输入订单中商品关键词" @keyup.enter="Search_GET_OrderList">
      <button type="button" @click="Search_GET_OrderList">搜索</button>
      <span v-if="orderData">搜到：<em>{{ orderData.data_total }}</em> 笔订单</span>
      <span v-else>搜索中...</span>
    </div>
    <empty-member v-if="orderData && !orderData.data.length">暂无订单</empty-member>
    <template v-else>
      <div class="order-table">
        <div class="order-table-thead">
          <span style="width: 420px">商品名称</span>
          <span style="width: 80px">单价</span>
          <span style="width: 80px">数量</span>
          <span style="width: 60px"></span>
          <span style="width: 150px">订单金额</span>
          <span style="width: 100px">订单状态</span>
          <span style="width: 110px">订单操作</span>
        </div>
        <ul class="order-table-tbody">
          <template v-for="order in orderData.data">
            <li v-if="orderData" :key="order.order_sn">
              <div class="order-tbody-title">
                <span class="pay-type">
                  <el-tooltip placement="top" v-if="order.order_type === 'CHANGE'">
                    <div slot="content">当前订单为换货售后服务审核通过后，<br/>商家为您重新创建的订单</div>
                    <i class="el-icon-info"></i>
                  </el-tooltip>
                  <el-tooltip placement="top" v-if="order.order_type === 'SUPPLY_AGAIN'">
                    <div slot="content">当前订单为补发商品售后服务审核<br/>通过后，商家为您重新创建的订单</div>
                    <i class="el-icon-info"></i>
                  </el-tooltip>
                  {{ order.payment_type === 'ONLINE' ? '线上支付' : '货到付款' }}：
                </span>
                <span class="price"><em>￥</em>{{ order.order_amount | unitPrice }}</span>
              </div>
              <div class="order-tbody-ordersn">
                <span>订单编号：{{ order.sn }}</span>
                <span>下单时间：{{ order.create_time | unixToDate }}</span>
              </div>
              <div class="order-tbody-item">
                <div class="order-item-sku">
                  <div class="sku-item" v-for="sku in order.sku_list" :key="sku.sku_id">
                    <div class="goods-image">
                      <a :href="'/goods/' + sku.goods_id" target="_blank">
                        <img :src="sku.goods_image" :alt="sku.name">
                      </a>
                    </div>
                    <div class="goods-name-box">
                      <a :href="'/goods/' + sku.goods_id" class="goods-name" target="_blank">
                        <span v-if="order.order_type === 'PINTUAN'" class="assemble-color">多人拼团</span>
                        <span v-html="sku.name">{{ sku.name }}</span>
                      </a>
                      <p v-if="sku.spec_list" class="sku-spec">{{ sku | formatterSkuSpec }}</p>
                    </div>
                    <div class="sku-price">{{ sku.purchase_price | unitPrice('￥') }}</div>
                    <div class="sku-num">x {{ sku.num }}</div>
                    <div class="after-sale-btn">
                      <nuxt-link v-if="sku.goods_operate_allowable_vo.allow_apply_service" :to="'/member/after-sale'">申请售后</nuxt-link>
                      <nuxt-link v-if="order.order_status_text !== '待付款' && sku.goods_operate_allowable_vo.allow_order_complain && sku.complain_status === 'NO_APPLY' || sku.complain_status === 'COMPLETE'" :to="'/member/my-complaint/complaint-apply?order_sn=' + order.sn + '&sku_id=' + sku.sku_id">交易投诉</nuxt-link>
                      <span v-if="sku.complain_status === 'APPLYING'">投诉申请中</span>
                      <span v-if="sku.complain_status === 'EXPIRED'">投诉已失效</span>
                      <nuxt-link v-if="sku.snapshot_id" :to="{path:'/goods/snapshot?id=' + sku.snapshot_id + '&sku_id=' + sku.sku_id + '&owner=BUYER',query:{orderData:order.order_amount}}">交易快照</nuxt-link>
                    </div>
                  </div>
                </div>
                <div class="order-item-price">
                  <strong>{{ order.order_amount | unitPrice('￥') }}</strong>
                  <p>运费（{{ order.shipping_amount | unitPrice('￥') }}）</p>
                  <p>{{ order.payment_text }}</p>
                </div>
                <div class="order-item-status">
                  <span
                    :class="[
                    order.pay_status === 'PAY_NO' &&
                    order.payment_type === 'ONLINE' &&
                    order.order_status === 'CONFIRM' &&
                    'wait-pay'
                    ]"
                  >{{ order.order_status_text }}  </span>
                  <el-tooltip placement="right" v-if="order.order_operate_allowable_vo.allow_check_cancel && order.order_status != 'CANCELLED'">
                    <div slot="content">当前订单已申请取消，请进入<br/>"售后管理"来查看订单取消进度</div>
                    <i class="el-icon-info"></i>
                  </el-tooltip>
                  <el-tooltip placement="right" v-if="order.ship_status === 'SHIP_NO' && order.order_type === 'CHANGE'">
                    <div slot="content">请您及时将申请售后的商品寄还给卖家<br/>卖家收到商品后才会对新订单进行发货</div>
                    <i class="el-icon-info"></i>
                  </el-tooltip>
                  <div
                    class="time-down"
                    v-if="order.cancel_left_time && order.order_status !== 'INTODB_ERROR'"
                  >
                    <i class="iconfont ea-icon-time"></i>剩余{{ getPayTimeDown(order) }}
                  </div>
                </div>
                <div class="order-item-operate">
                  <a v-if="order.order_operate_allowable_vo.allow_cancel" href="javascript:;" @click="handleCancelOrder(order.sn)">取消订单</a>
                  <a v-if="order.order_operate_allowable_vo.allow_service_cancel" @click="handleCancelOrderDialog(order)">取消订单</a>
                  <a v-if="order.order_operate_allowable_vo.allow_rog" href="javascript:;" @click="handleRogOrder(order.sn)">确认收货</a>
                  <nuxt-link v-if="order.order_operate_allowable_vo.allow_pay" :to="'/checkout/cashier?order_sn=' + order.sn">订单付款</nuxt-link>
                  <nuxt-link v-if="order.order_operate_allowable_vo.allow_comment && order.comment_status === 'UNFINISHED'" :to="'/member/comments?order_sn=' + order.sn">去评论</nuxt-link>
                  <nuxt-link v-if="order.comment_status === 'WAIT_CHASE'" :to="`/member/comments?order_sn=${order.sn}&append_comment=review`">追加评论</nuxt-link>
                  <nuxt-link :to="'./my-order/detail?order_sn=' + order.sn">订单详情</nuxt-link>
                  <nuxt-link v-if="order.order_operate_allowable_vo.allow_check_cancel" :to="'/member/service-record'" class="cancel-order">取消详情</nuxt-link>
                </div>
              </div>
            </li>
          </template>
        </ul>
      </div>
      <div class="member-pagination">
        <el-pagination
          @current-change="handleCurrentPageChange"
          :current-page.sync="params.page_no"
          :page-size="params.page_size"
          layout="total, prev, pager, next"
          :total="orderData.data_total">
        </el-pagination>
      </div>
    </template>

    <!--取消订单 dialog-->
    <el-dialog
      title="取消订单"
      :visible.sync="dialogCancelVisible"
      width="480px"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <el-form :model="cancelOrderForm" :rules="cancelOrderRules" ref="cancelOrderForm">
        <el-form-item>
          <el-alert v-if="activeName === 'first'" title="订单取消后无法恢复，使用的优惠券和积分等将不再返还" type="warning" show-icon :closable="false"></el-alert>
          <el-alert v-else title="点击申请售后按钮后，订单会自动确认收货" type="warning" show-icon :closable="false"></el-alert>
        </el-form-item>
        <el-form-item>
          <el-tabs v-model="activeName" tab-position="top" stretch>
            <el-tab-pane label="未收到货" name="first">
              <el-form-item label="退款方式：">
                <div v-if="isRetrace">
                  <span>原路退回  </span>
                  <el-tooltip placement="right">
                    <div slot="content">您的取消订单申请通过后，退款将按您订单的<br/>支付方式原路退回</div>
                    <i class="el-icon-warning-outline"></i>
                  </el-tooltip>
                </div>
                <div v-else>
                  <span>账号退款  </span>
                  <el-tooltip placement="right">
                    <div slot="content">您的取消订单申请通过后，平台会将退款在线<br/>转入您下面所输入的账户中</div>
                    <i class="el-icon-warning-outline"></i>
                  </el-tooltip>
                </div>
              </el-form-item>
              <el-form-item label="退款金额：">
                <span style="font-size: 19px;font-weight: bolder;">{{ cancelOrderForm.refund_price | unitPrice('￥') }}</span>
                <span style="font-size: 13px;color: #ccc;" v-if="isRetrace">  {{cancelOrderForm.payment_method_name}}</span>
              </el-form-item>
              <el-form-item label="取消原因：" prop="reason">
                <el-select v-model="cancelOrderForm.reason" clearable placeholder="请选择">
                  <el-option label="商品无货" value="商品无货"></el-option>
                  <el-option label="配送时间问题" value="配送时间问题"></el-option>
                  <el-option label="不想要了" value="不想要了"></el-option>
                  <el-option label="商品信息填写错误" value="商品信息填写错误"></el-option>
                  <el-option label="地址信息填写错误" value="地址信息填写错误"></el-option>
                  <el-option label="商品降价" value="商品降价"></el-option>
                  <el-option label="货物破损已拒签" value="货物破损已拒签"></el-option>
                  <el-option label="订单无物流跟踪记录" value="订单无物流跟踪记录"></el-option>
                  <el-option label="非本人签收" value="非本人签收"></el-option>
                  <el-option label="其他" value="其他"></el-option>
                </el-select>
              </el-form-item>
              <div v-if="!isRetrace">
                <el-form-item label="账户类型：" prop="account_type">
                  <el-select v-model="cancelOrderForm.account_type" clearable placeholder="请选择账户类型">
                    <el-option label="支付宝" value="ALIPAY"></el-option>
                    <el-option label="微信" value="WEIXINPAY"></el-option>
                    <el-option label="银行转账" value="BANKTRANSFER"></el-option>
                  </el-select>
                </el-form-item>
                <div v-if="cancelOrderForm.account_type === 'BANKTRANSFER'">
                  <el-form-item label="银行名称：" prop="bank_name">
                    <el-input v-model="cancelOrderForm.bank_name" :maxlength="180" placeholder="请输入银行名称" style="width: 50%;"/>
                  </el-form-item>
                  <el-form-item label="银行开户行：" prop="bank_deposit_name">
                    <el-input v-model="cancelOrderForm.bank_deposit_name" :maxlength="180" placeholder="请输入银行开户行" style="width: 50%;"/>
                  </el-form-item>
                  <el-form-item label="银行开户名：" prop="bank_account_name">
                    <el-input v-model="cancelOrderForm.bank_account_name" :maxlength="180" placeholder="请输入银行开户名" style="width: 50%;"/>
                  </el-form-item>
                  <el-form-item label="银行账号：" prop="bank_account_number">
                    <el-input v-model="cancelOrderForm.bank_account_number" :maxlength="180" placeholder="请输入银行账号" style="width: 50%;"/>
                  </el-form-item>
                </div>
                <el-form-item v-else label="退款账号：" prop="return_account">
                  <el-input v-model="cancelOrderForm.return_account" :maxlength="180" placeholder="请输入退款账号" style="width: 50%;"/>
                </el-form-item>
              </div>
              <el-form-item label="取消结果短信通知：" prop="mobile">
                <el-input v-model="cancelOrderForm.mobile" :maxlength="11" placeholder="请输入手机号码" style="width: 50%;"/>
              </el-form-item>
            </el-tab-pane>
            <el-tab-pane label="已收到货" name="second" style="margin: 20px" v-if="tabDisabled">
              <a href="javascript:;" class="apply-service" @click="handleApplyService">申请售后</a>
            </el-tab-pane>
            <el-tab-pane label="已收到货" name="second" style="margin: 20px" disabled v-else></el-tab-pane>
          </el-tabs>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer" v-if="activeName === 'first'">
        <el-button @click="dialogCancelVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitCancelOrderForm">提 交</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import Vue from 'vue'
  import * as API_Order from '@/api/order'
  import * as API_AfterSale from '@/api/after-sale'
  import { RegExp } from '@/ui-utils'
  import { DatePicker, Dialog, Select, Option, Tabs, TabPane, Tag, Tooltip } from 'element-ui'
  Vue.use(DatePicker).use(Dialog).use(Select).use(Option).use(Tabs).use(TabPane).use(Tag).use(Tooltip)
  export default {
    name: 'my-order-index',
    mounted() {
      if (!this.orderData) {
        this.GET_OrderList()
      }

    },
    data() {
      return {
        params: {
          page_no: 1,
          page_size: 5,
          order_status: this.$route.query.order_status,
          key_words: '',
        },
        order_time_range: [],
        orderData: '',
        navList: [
          { title: '所有订单', status: '' },
          { title: '待付款', status: 'WAIT_PAY' },
          { title: '待发货', status: 'WAIT_SHIP' },
          { title: '待收货', status: 'WAIT_ROG' },
          { title: '已取消', status: 'CANCELLED' },
          { title: '已完成', status: 'COMPLETE' },
          { title: '待评价', status: 'WAIT_COMMENT' }
        ],
        // 日期选择器快捷选项
        MixinPickerShortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
            picker.$emit('pick', [start, end])
          }
        }],
        activeName: 'first',
        tabDisabled: false,
        isRetrace: false,
        cancelOrderForm: {
          order_sn: '',
          refund_way: '',
          payment_method_name: '',
          refund_price: 0.00
        },
        // 取消订单 表单校验
        cancelOrderRules: {
          // 取消原因
          reason: [{ required: true, message: '请选择取消原因！', trigger: 'change' }],
          // 账户类型
          account_type: [{ required: true, message: '请选择账户类型！', trigger: 'change' }],
          // 退款账号
          return_account: [{ required: true, message: '请输入退款账号！', trigger: 'blur' }],
          // 银行名称
          bank_name: [{ required: false, message: '请输入银行名称!', trigger: 'blur' }],
          // 银行开户行
          bank_deposit_name: [{ required: false, message: '请输入银行开户行!', trigger: 'blur' }],
          // 银行开户名
          bank_account_name: [{ required: false, message: '请输入银行开户名!', trigger: 'blur' }],
          // 银行账号
          bank_account_number: [{ required: false, message: '请输入银行账号!', trigger: 'blur' }],
          // 手机号
          mobile: [{ required: true, trigger: 'blur', validator: (rule, value, callback) => {
            if (!value) {
              return callback(new Error('请输入手机号码！'));
            } else {
              if (RegExp.mobile.test(value)) {
                callback();
              } else {
                return callback(new Error('输入的手机号码格式不正确！'));
              }
            }
          }}]
        },
        // 问题咨询 dialog
        dialogCancelVisible: false
      }
    },
    watch: {
      $route: function ({ query }) {
        this.params.page_no = 1
        this.params.order_status = query.order_status
        this.GET_OrderList()
      },
      'cancelOrderForm.account_type': function (newVal) {
        this.cancelOrderRules.return_account[0].required = newVal !== 'BANKTRANSFER'
        this.cancelOrderRules.bank_name[0].required = newVal === 'BANKTRANSFER'
        this.cancelOrderRules.bank_deposit_name[0].required = newVal === 'BANKTRANSFER'
        this.cancelOrderRules.bank_account_name[0].required = newVal === 'BANKTRANSFER'
        this.cancelOrderRules.bank_account_number[0].required = newVal === 'BANKTRANSFER'
      }
    },
    methods: {
      /** 当前页数发生改变 */
      handleCurrentPageChange(cur) {
        this.params.page_no = cur
        this.GET_OrderList()
      },

      /** 取消订单 */
      handleCancelOrder(order_sn) {
        this.$layer.prompt({
          formType: 2,
          value: ' ',
          title: '请输入取消原因',
          maxlength: 200
        }, (value, index) => {
          const val = value.trim()
          if (!val) {
            this.$message.error('请填写取消原因！')
          } else if (val.length > 200) {
            this.$message.error('最多输入200个字符！')
          } else if (this.MixinEmojiCharacter(value)) {
            this.$message.error('不能输入表情符号！');
            return false
          } else {
            API_Order.cancelOrder(order_sn, val).then(() => {
              this.$message.success('订单取消成功！')
              layer.close(index)
              this.GET_OrderList()
            })
          }
        })
      },

      /** 取消订单（售后）*/
      handleCancelOrderDialog(order) {
        this.cancelOrderForm.order_sn = order.sn
        this.cancelOrderForm.refund_price = order.pay_money
        this.isRetrace = order.is_retrace
        this.cancelOrderForm.refund_way = order.is_retrace ? 'ORIGINAL' : 'OFFLINE'
        this.cancelOrderForm.payment_method_name = order.payment_method_name
        this.tabDisabled = order.order_status === 'SHIPPED' && order.ship_status === 'SHIP_YES'
        this.dialogCancelVisible = true
      },

      /** 提交取消订单（售后）表单数据 */
      submitCancelOrderForm() {
        this.$refs['cancelOrderForm'].validate((valid) => {
          if (valid) {
            API_AfterSale.applyCancelOrder(this.cancelOrderForm).then(() => {
              this.dialogCancelVisible = false
              this.$message.success('提交成功')
              this.GET_OrderList()
            })
          } else {
            this.$message.error('表单填写有误，请核对！')
            return false
          }
        })
      },

      /** 申请售后 */
      handleApplyService() {
        API_Order.confirmReceipt(this.cancelOrderForm.order_sn).then(() => {
          this.$router.push({ path: '/member/after-sale'})
        })
      },

      /** 确认收货 */
      handleRogOrder(order_sn) {
        this.$confirm('请确认是否收到货物，否则可能会钱财两空！', () => {
          API_Order.confirmReceipt(order_sn).then(() => {
            this.$message.success('确认收货成功！')
            this.GET_OrderList()
          })
      })
      },

      /** 获取支付倒计时 */
      getPayTimeDown(order) {
        console.log(order)
        const { cancel_left_time: rema } = order
        if (rema <= 3600) {
          return `0时${parseInt(rema / 60)}分`
        } else {
          const h = parseInt(rema / 3600)
          const m = parseInt((rema - h * 3600) / 60)
          return `${h}时${m}分`
        }
      },

      /** 搜索获取数据 */
      Search_GET_OrderList(){
        this.params.page_no = 1
        if (this.order_time_range && this.order_time_range.length) {
          this.params.start_time = Math.floor(this.order_time_range[0].getTime() / 1000)
          this.params.end_time = Math.floor(this.order_time_range[1].getTime() / 1000)
        } else {
          this.params.start_time = this.params.end_time = ''
        }
        this.GET_OrderList()
      },

      /** 获取订单数据 */
      GET_OrderList() {
        // 去除参数中为空的选项
        let params = {}
        for (let key in this.params) {
          if (this.params[key]) {
            params[key] = this.params[key]
          }
        }
        API_Order.getOrderList(params).then(response => {
          this.orderData = response
          this.MixinScrollToTop()
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../../assets/styles/color";
  /deep/ .el-input__inner {
    height: 27px;
    line-height: 27px;
    width:250px;
    border: 1px solid #ccc;
  }
  /deep/ .el-date-editor {
    .el-range-separator{
      line-height: 19px;
    }
    .el-range__icon{
      line-height: 23px;
    }
  }
  /deep/ .el-range__close-icon{
    line-height:23px;
  }
  .order-search {
    display: flex;
    align-items: center;
    justify-content: flex-end;
    width: 100%;
    height: 44px;
    border-bottom: 1px solid #e7e7e7;
    em {
      color: $color-main;
    }
    input {
      border: 1px solid #ccc;
      padding: 5px 15px;
      height: 15px;
      width: 150px;
      color: #333;
      border-radius: 3px;
      transition: border .2s ease-out;
      margin-left: 10px;
      &:focus {
        border-color: darken($color-main, 75%);
      }
    }
    button {
      width: 80px;
      background-color: #e7e7e7;
      color: #333;
      cursor: pointer;
      line-height: 27px;
      margin-left: 10px;
      margin-right: 10px;
      transition: background-color .2s ease-out;
      border-radius: 3px;
      &:hover {
        background-color: #d3d3d3;
      }
    }
  }
  .order-table {
    .order-table-thead {
      display: flex;
      align-items: center;
      height: 20px;
      padding: 8px 0;
      text-align: center;
      color: #999;
    }
    .order-table-tbody {
      li {
        margin-bottom: 20px;
      }
    }
    .order-tbody-title {
      background-color: #faece0;
      border: 1px solid #f9dbcc;
      padding: 9px 0;
      text-align: left;
      .pay-type {
        color: #53514f;
        font-weight: 600;
        margin-left: 20px;
      }
      .price {
        color: $color-main;
        font-size: 14px;
        font-weight: 600;
        em {
          font-size: 12px;
          font-weight: normal;
        }
      }
    }
    .order-tbody-ordersn {
      background-color: #fff;
      border: 1px solid #f9dbcc;
      padding: 5px 0;
      color: #999;
      height: 20px;
      font-size: 12px;
      font-weight: 400;
      span { margin-left: 20px }
    }
    .order-tbody-item {
      display: flex;
      align-items: center;
      position: relative;
      border: 1px solid #f9dbcc;
      border-top: none;
      .order-item-sku {
        width: 470px + 80px + 80px;
      }
      .sku-item {
        display: flex;
        align-items: center;
        margin-top: 6px;
      }
      .goods-image {
        display: flex;
        justify-content: center;
        align-items: center;
        width: 86px;
        height: 86px;
        a {
          display: flex;
          justify-content: center;
          align-items: center;
          width: 66px;
          height: 66px;
          text-align: center;
          border: 1px solid #ccc;
          background-color: #fff;
        }
        img {
          width: 62px;
          height: 62px;
        }
      }
      .goods-name-box { width: 450px - 86px }
      .goods-name {
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 2;
        overflow: hidden;
        .assemble-color {
          color: $color-main;
          border: 1px solid $color-main;
          border-radius: 3px;
        }
      }
      .sku-price { color: $color-main }
      .sku-price, .sku-num {
        width: 80px;
        text-align: center;
      }
      .after-sale-btn {
        width: 70px;
        a {
          color: #666;
          display: block;
        }
        a:hover { color: $color-main }
      }
      .order-item-price, .order-item-status {
        width: 100px;
        text-align: center;
      }
      .order-item-price {
        width: 150px;
        &::before {
          content: ' ';
          position: absolute;
          top: 0;
          right: 110px + 100px + 150px;
          width: 1px;
          height: 100%;
          background-color: #f9dbcc;
        }
        &::after {
          content: ' ';
          position: absolute;
          top: 0;
          right: 110px + 100px;
          width: 1px;
          height: 100%;
          background-color: #f9dbcc;
        }
        strong { color: $color-main }
      }
      .order-item-status {
        width: 120px;
        &::after {
          content: ' ';
          position: absolute;
          top: 0;
          right: 100px;
          width: 1px;
          height: 100%;
          background-color: #f9dbcc;
        }
      }
      .order-item-operate {
        display: flex;
        justify-content: center;
        align-items: center;
        flex-direction: column;
        width: 110px;
        a {
          display: block;
          width: 80px;
          height: 30px;
          line-height: 30px;
          border: 1px solid #ccc;
          text-align: center;
          background: #f9f9f9;
          color: #666;
          cursor: pointer;
          margin-bottom: 5px;
          &:first-child {
            margin-top: 5px;
          };
          &:hover {
            background: #eaeaea;
          }
        }
      }
    }
  }
  .wait-pay {
    color: $color-main !important;
  }
  .time-down {
    .ea-icon-time {
      vertical-align: -1px;
    }
  }
  /deep/ .is-error .el-form-item__error {
    position: relative;
  }
  /deep/ .el-dialog__body {
    padding: 1px 20px;
  }
  /deep/ .el-form-item {
    margin-bottom: 5px;
  }
  .apply-service {
    display: block;
    width: 80px;
    height: 30px;
    line-height: 30px;
    border: 1px solid #ccc;
    text-align: center;
    background: #f9f9f9;
    color: #666;
    cursor: pointer;
    margin-left: 40%;
  }
</style>
