<template>
  <div id="apply-service">
    <div class="member-nav">
      <ul class="member-nav-list">
        <li class="active">
          <a href="javascript:;" style="color: #f42424;">申请售后</a>
        </li>
      </ul>
    </div>
    <template>
      <div class="mod-main mod-comm">
        <div class="mc">
          <table class="tb-void mb10">
            <thead>
              <tr>
                <th width="280">商品名称</th>
                <th width="150">商品价格</th>
                <th width="105">购买数量</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>
                  <ul class="list-proinfo">
                    <li class="fore1">
                      <a :href="'/goods/' + applyInfo.goods_id" target="_blank">
                        <img height="50" width="50" :src="applyInfo.goods_img" :title="applyInfo.goods_name" :alt="applyInfo.goods_name">
                      </a>
                      <div class="p-info">
                        <a :href="'/goods/' + applyInfo.goods_id" target="_blank">{{ applyInfo.goods_name }}</a>
                      </div>
                    </li>
                    <li class="fore1" v-if="applyInfo.gift_list && applyForm.service_type != 'SUPPLY_AGAIN_GOODS' && applyForm.service_type != 'CHANGE_GOODS'" v-for="gift in applyInfo.gift_list" :key="gift.gift_id">
                      <img height="50" width="50" :src="gift.gift_img" :title="gift.gift_name" :alt="gift.gift_name">
                      <div class="p-info">
                        [赠品]{{ gift.gift_name }}
                        <el-tooltip placement="right">
                          <div slot="content">赠品要随购买的商品一并退回</div>
                          <i class="el-icon-info"></i>
                        </el-tooltip>
                      </div>
                    </li>
                  </ul>
                </td>
                <td>
                  ￥{{ (applyInfo.goods_price || 0) | unitPrice }}
                </td>
                <td>{{ applyInfo.buy_num }}</td>
              </tr>
            </tbody>
          </table>
        </div>
        <div class="form">
          <div class="repair-steps">
            <el-form :model="applyForm" :rules="applyRules" ref="applyForm" label-width="100px" class="demo-ruleForm">
              <!-- 申请信息 -->
              <div class="repair-step qww-repair-step repair-step-curr repair-step-sxh pt10 mb10">
                <div class="item">
                  <span class="label"> <em> * </em> 服务类型： </span>
                  <div class="fl" style="width: 550px;">
                    <ul class="list-type list-type-new">
                      <li v-if="applyInfo.allow_return_goods">
                        <el-radio v-model="applyForm.service_type" label="RETURN_GOODS" border size="medium" @change="handleChangeType('RETURN_GOODS')">申请退货</el-radio>
                      </li>
                      <li>
                        <el-radio v-model="applyForm.service_type" label="CHANGE_GOODS" border size="medium" @change="handleChangeType('CHANGE_GOODS')">申请换货</el-radio>
                      </li>
                      <li>
                        <el-radio v-model="applyForm.service_type" label="SUPPLY_AGAIN_GOODS" border size="medium" @change="handleChangeType('SUPPLY_AGAIN_GOODS')">申请补发</el-radio>
                      </li>
                    </ul>
                  </div>
                  <div class="clr"></div>
                </div>
                <div v-if="applyForm.service_type">
                  <div v-if="applyForm.service_type === 'RETURN_GOODS'">
                    <el-form-item label="退款方式：">
                      <div v-if="applyInfo.is_retrace">
                        <span>原路退回  </span>
                        <el-tooltip placement="right">
                          <div slot="content">您的退货申请审核通过后，退款将按您订单的<br/>支付方式原路退回</div>
                          <i class="el-icon-info"></i>
                        </el-tooltip>
                      </div>
                      <div v-else>
                        <span>账号退款  </span>
                        <el-tooltip placement="right">
                          <div slot="content">您的退货申请审核通过后，平台会将退款在线<br/>转入您下面所输入的账户中</div>
                          <i class="el-icon-info"></i>
                        </el-tooltip>
                      </div>
                    </el-form-item>
                    <div v-if="!applyInfo.is_retrace">
                      <el-form-item label="账户类型：" prop="account_type">
                        <el-select v-model="applyForm.account_type" clearable placeholder="请选择账户类型" size="small">
                          <el-option label="支付宝" value="ALIPAY"></el-option>
                          <el-option label="微信" value="WEIXINPAY"></el-option>
                          <el-option label="银行卡" value="BANKTRANSFER"></el-option>
                        </el-select>
                      </el-form-item>
                      <div v-if="applyForm.account_type === 'BANKTRANSFER'">
                        <el-form-item label="银行名称：" prop="bank_name">
                          <el-input v-model="applyForm.bank_name" :maxlength="180" placeholder="请输入银行名称" style="width: 50%;" size="small"/>
                        </el-form-item>
                        <el-form-item label="银行开户行：" prop="bank_deposit_name">
                          <el-input v-model="applyForm.bank_deposit_name" :maxlength="180" placeholder="请输入银行开户行" style="width: 50%;" size="small"/>
                        </el-form-item>
                        <el-form-item label="银行开户名：" prop="bank_account_name">
                          <el-input v-model="applyForm.bank_account_name" :maxlength="180" placeholder="请输入银行开户名" style="width: 50%;" size="small"/>
                        </el-form-item>
                        <el-form-item label="银行账号：" prop="bank_account_number">
                          <el-input v-model="applyForm.bank_account_number" :maxlength="180" placeholder="请输入银行账号" style="width: 50%;" size="small"/>
                        </el-form-item>
                      </div>
                      <el-form-item v-else label="退款账号：" prop="return_account">
                        <el-input v-model="applyForm.return_account" :maxlength="180" placeholder="请输入退款账号" style="width: 50%;" size="small"/>
                      </el-form-item>
                    </div>
                  </div>
                  <el-form-item label="提交数量：" prop="return_num">
                    <el-input-number v-model="num" :min="1" :max="applyInfo.buy_num" size="mini" @change="handleNumberChange"></el-input-number>
                    <el-tooltip placement="right" style="margin-left: 5px;">
                      <div slot="content">您最多可提交数量为{{ applyInfo.buy_num }}</div>
                      <i class="el-icon-info"></i>
                    </el-tooltip>
                  </el-form-item>
                  <el-form-item label="提交原因：" prop="reason">
                    <el-select v-model="applyForm.reason" clearable placeholder="请选择" size="small" v-if="applyForm.service_type === 'RETURN_GOODS'">
                      <el-option label="商品降价" value="商品降价"></el-option>
                      <el-option label="商品与页面描述不符" value="商品与页面描述不符"></el-option>
                      <el-option label="缺少件" value="缺少件"></el-option>
                      <el-option label="质量问题" value="质量问题"></el-option>
                      <el-option label="发错货" value="发错货"></el-option>
                      <el-option label="商品损坏" value="商品损坏"></el-option>
                      <el-option label="不想要了" value="不想要了"></el-option>
                      <el-option label="其他" value="其他"></el-option>
                    </el-select>
                    <el-select v-model="applyForm.reason" clearable placeholder="请选择" size="small" v-if="applyForm.service_type === 'CHANGE_GOODS'">
                      <el-option label="商品与页面描述不符" value="商品与页面描述不符"></el-option>
                      <el-option label="缺少件" value="缺少件"></el-option>
                      <el-option label="质量问题" value="质量问题"></el-option>
                      <el-option label="发错货" value="发错货"></el-option>
                      <el-option label="商品损坏" value="商品损坏"></el-option>
                      <el-option label="大小/颜色/型号等不合适" value="大小/颜色/型号等不合适"></el-option>
                      <el-option label="其他" value="其他"></el-option>
                    </el-select>
                    <el-select v-model="applyForm.reason" clearable placeholder="请选择" size="small" v-if="applyForm.service_type === 'SUPPLY_AGAIN_GOODS'">
                      <el-option label="商品发货数量不对" value="商品发货数量不对"></el-option>
                      <el-option label="其他" value="其他"></el-option>
                    </el-select>
                  </el-form-item>
                  <el-form-item label="问题描述：" prop="problem_desc">
                    <el-input type="textarea" placeholder="请输入问题描述" v-model="applyForm.problem_desc" maxlength="200" show-word-limit rows="5" resize="none" style="width: 60%;"></el-input>
                  </el-form-item>
                  <el-form-item label="图片信息：" prop="images">
                    <div :class="['comments-images', applyForm.images.length >= 5 && 'limit']">
                      <el-upload
                        ref="my-upload"
                        :action="MixinUploadApi"
                        list-type="picture-card"
                        multiple
                        :on-exceed="() => { $message.error('超过最大可上传数！') }"
                        :limit="5"
                        :on-success="(res, file, fileList) => { applyForm.images = fileList }"
                        :on-remove="(file, fileList) => { applyForm.images = fileList }"
                      >
                        <i class="el-icon-plus"></i>
                      </el-upload>
                    </div>
                    <p class="p-title">为了帮助您更好的解决问题，请上传图片（最多可上传5张照片）</p>
                  </el-form-item>
                </div>
              </div >
              <!-- 订单信息 -->
              <div class="repair-step confirm-info" v-if="applyForm.service_type">
                <div class="mt-10">
                  <strong>确认信息</strong>
                  <a href="javascript:;" class="ftx-05 ml5 edit-repair-step" @click="handleOpenShipInfo" v-if="isClose">[修改]</a>
                </div>
                <div v-if="isClose">
                  <div class="item">
                    <span class="label">申请凭据：</span>
                    <div class="fl">
                      <label v-if="applyInfo.is_receipt">有发票</label>
                      <label v-else>暂无</label>
                    </div>
                    <div class="clr"></div>
                  </div>
                  <div class="item">
                    <span class="label"><em>*</em>返回方式：</span>
                    <div class="fl">
                      <ul class="list-return">
                        <li>
                          <div class="l-col">
                            <label>快递至第三方卖家</label>
                          </div>
                          <div class="msg-text" style="width: 450px"></div>
                        </li>
                      </ul>
                    </div>
                    <div class="clr"></div>
                  </div>
                  <div class="item">
                    <span class="label"><em>*</em>收货地址：</span>
                    <div class="fl">
                      {{ applyInfo.province }}{{ applyInfo.city }}{{ applyInfo.county }}{{ applyInfo.town }}{{ applyInfo.ship_addr }}
                    </div>
                    <div class="clr"></div>
                  </div>
                  <div class="item">
                    <span class="label"><em>*</em>顾客姓名：</span>
                    <div class="fl">
                      {{ applyInfo.ship_name }}
                    </div>
                    <div class="clr"></div>
                  </div>
                  <div class="item">
                    <span class="label"><em>*</em>手机号码：</span>
                    <div class="fl" style="width: 135px;">
                      {{ applyInfo.ship_mobile | secrecyMobile }}
                    </div>
                    <div class="msg-text">提交服务单后，售后专员可能与您电话沟通，请保持手机畅通</div>
                    <div class="clr"></div>
                  </div>
                </div>
                <div v-else>
                  <el-form-item label="申请凭据：">
                    <el-checkbox-group v-model="checkList">
                      <el-checkbox label="有发票"></el-checkbox>
                      <el-checkbox label="有检测报告"></el-checkbox>
                    </el-checkbox-group>
                  </el-form-item>
                  <el-form-item label="返回方式：">
                    快递至第三方卖家
                    <el-tooltip placement="right">
                      <div slot="content">商品寄回地址将在审核通过后以短信形式告知，或在申请记录中查询。</div>
                      <i class="el-icon-info"></i>
                    </el-tooltip>
                  </el-form-item>
                  <el-form-item label="收货地区：" prop="region">
                    <en-region-picker :api="MixinRegionApi" :default="areas" @changed="(object) => { applyForm.region = object.last_id }" style="float: left;margin-top: 7px;"></en-region-picker>
                  </el-form-item>
                  <el-form-item label="收货详细地址：" prop="ship_addr">
                    <el-input v-model="applyForm.ship_addr" :maxlength="180" placeholder="请输入收货详细地址" style="width: 50%;" size="small" @input="change($event)"/>
                  </el-form-item>
                  <el-form-item label="顾客姓名：" prop="ship_name">
                    <el-input v-model="applyForm.ship_name" :maxlength="180" placeholder="请输入顾客姓名" style="width: 50%;" size="small" @input="change($event)"/>
                  </el-form-item>
                  <el-form-item label="手机号码：" prop="ship_mobile">
                    <el-input v-model="applyForm.ship_mobile" :maxlength="180" placeholder="请输入手机号码" style="width: 50%;" size="small" @input="change($event)"/>
                  </el-form-item>
                </div>
              </div>
            </el-form>
            <!-- 提交按钮 -->
            <div class="item" v-if="applyForm.service_type">
              <span class="label">&nbsp;</span>
              <div class="submit-btn-qww">
                <a href="javascript:void(0)" class="submit-qww" @click="submitApplyForm"><s></s>确认提交</a>
              </div>
              <div class="clr"></div>
            </div>
          </div>
        </div>
      </div>
    </template>
  </div>
</template>

<script>
  import Vue from 'vue'
  import * as API_AfterSale from '@/api/after-sale'
  import { RegExp } from '@/ui-utils'
  import { Tooltip, Radio, Select, Option, InputNumber, Upload, Checkbox, CheckboxGroup } from 'element-ui'
  Vue.use(Tooltip).use(Radio).use(Select).use(Option).use(InputNumber).use(Upload).use(Checkbox).use(CheckboxGroup)
  export default {
    name: 'apply-service',
    head() {
      return {
        title: `申请售后-${this.site.title}`
      }
    },
    data() {
      return {
        // 订单编号
        order_sn: this.$route.query.order_sn,
        // 商品skuID
        sku_id: this.$route.query.sku_id,
        // 是否显示收货信息修改页面
        isClose: true,
        // 提交凭据集合
        checkList: [],
        // 初始化页面展示信息
        applyInfo: '',
        // 地区信息集合
        areas: [],
        // 申请表单数据
        applyForm: {
          service_type: '',
          images: [],
          return_num: null,
          ship_addr: '',
          ship_name: '',
          ship_mobile: ''
        },
        // 提交数量
        num: null,
        // 申请售后服务 表单校验
        applyRules: {
          // 提交数量
          return_num: [{ required: true, message: '请填写提交数量！', trigger: 'blur' }],
          // 提交原因
          reason: [{ required: true, message: '请选择提交原因！', trigger: 'change' }],
          // 问题描述
          problem_desc: [{ required: true, message: '请填写问题描述！', trigger: 'blur' }],
          // 账户类型
          account_type: [{ required: false, message: '请选择账户类型！', trigger: 'change' }],
          // 退款账号
          return_account: [{ required: false, message: '请输入退款账号！', trigger: 'blur' }],
          // 银行名称
          bank_name: [{ required: false, message: '请输入银行名称!', trigger: 'blur' }],
          // 银行开户行
          bank_deposit_name: [{ required: false, message: '请输入银行开户行!', trigger: 'blur' }],
          // 银行开户名
          bank_account_name: [{ required: false, message: '请输入银行开户名!', trigger: 'blur' }],
          // 银行账号
          bank_account_number: [{ required: false, message: '请输入银行账号!', trigger: 'blur' }],
          // 收货详细地址
          ship_addr: [{ required: true, message: '请输入收货详细地址!', trigger: 'blur' }],
          // 顾客姓名
          ship_name: [{ required: true, message: '请输入顾客姓名!', trigger: 'blur' }],
          // 手机号
          ship_mobile: [{ required: true, trigger: 'blur', validator: (rule, value, callback) => {
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
        }
      }
    },
    mounted() {
      this.GET_AfterSaleApplyInfo()
    },
    watch: {
      'applyForm.service_type': function (newVal) {
        this.applyRules.account_type[0].required = newVal === 'RETURN_GOODS'
        this.applyRules.return_account[0].required = newVal === 'RETURN_GOODS'
      },
      'applyForm.account_type': function (newVal) {
        this.applyRules.return_account[0].required = newVal !== 'BANKTRANSFER'
        this.applyRules.bank_name[0].required = newVal === 'BANKTRANSFER'
        this.applyRules.bank_deposit_name[0].required = newVal === 'BANKTRANSFER'
        this.applyRules.bank_account_name[0].required = newVal === 'BANKTRANSFER'
        this.applyRules.bank_account_number[0].required = newVal === 'BANKTRANSFER'
      }
    },
    methods: {

      change (e) {
        this.$forceUpdate()
      },

      /** 提交数量修改事件 */
      handleNumberChange(value) {
        if (value === undefined) {
          this.num = this.applyInfo.buy_num
          this.applyForm.return_num = this.num
        } else {
          this.applyForm.return_num = value
        }
      },

      /** 修改收货地址 */
      handleOpenShipInfo() {
        this.isClose = false
        $('.repair-steps').addClass('repair-step-curr')
      },

      /** 售后服务类型改变事件 */
      handleChangeType(serviceType) {
        this.isClose = true
        $('.repair-steps').removeClass('repair-step-curr')
        this.applyForm = {}
        this.applyForm.service_type = serviceType
        this.$refs['my-upload'].clearFiles()
        this.applyForm.images = []
        this.num = this.applyInfo.buy_num
        this.applyForm.return_num = this.num
        this.applyForm.ship_addr = this.applyInfo.ship_addr
        this.applyForm.ship_name = this.applyInfo.ship_name
        this.applyForm.ship_mobile = this.applyInfo.ship_mobile
        this.areas = [this.applyInfo.province_id, this.applyInfo.city_id,
          this.applyInfo.county_id || -1, this.applyInfo.town_id || -1]
        if (this.applyInfo.is_receipt) {
          this.checkList = ['有发票']
        }
      },

      /** 提交申请售后服务表单数据 */
      submitApplyForm() {
        this.$refs['applyForm'].validate((valid) => {
          if (valid) {

            /** 申请凭证 */
            var vouchers = ''
            if (this.checkList.length != 0) {
              this.checkList.forEach(item => {
                vouchers += item + '，'
              })
              vouchers = vouchers.substr(0, vouchers.length-1)
            }

            this.applyForm.order_sn = this.order_sn
            this.applyForm.sku_id = this.sku_id
            this.applyForm.apply_vouchers = vouchers
            this.applyForm.images = this.applyForm.images.map(item => item.response.url)

            if (this.isClose) {
              this.applyForm.region = this.applyInfo.town_id && this.applyInfo.town_id != 0 ? this.applyInfo.town_id : this.applyInfo.county_id
            }

            if (this.applyForm.service_type === 'RETURN_GOODS') {
              API_AfterSale.applyReturnGoods(this.applyForm).then(() => {
                this.$message.success('提交成功')
                this.$router.replace({path: '/member/service-record', query: {}})
              })
            } else if (this.applyForm.service_type === 'CHANGE_GOODS') {
              API_AfterSale.applyChangeGoods(this.applyForm).then(() => {
                this.$message.success('提交成功')
                this.$router.replace({path: '/member/service-record', query: {}})
              })
            } else if (this.applyForm.service_type === 'SUPPLY_AGAIN_GOODS') {
              API_AfterSale.applyReplaceGoods(this.applyForm).then(() => {
                this.$message.success('提交成功')
                this.$router.replace({path: '/member/service-record', query: {}})
              })
            } else {
              this.$message.error('申请的售后服务类型不正确！')
              return false
            }

          } else {
            this.$message.error('表单填写有误，请核对！')
            return false
          }
        })
      },

      /** 获取申请售后服务页面展示数据 */
      GET_AfterSaleApplyInfo() {
        API_AfterSale.getApplyInfo(this.order_sn, this.sku_id).then(response => {
          this.applyInfo = response;
          this.num = this.applyInfo.buy_num
          this.applyForm.return_num = this.num
          this.applyForm.ship_addr = this.applyInfo.ship_addr
          this.applyForm.ship_name = this.applyInfo.ship_name
          this.applyForm.ship_mobile = this.applyInfo.ship_mobile
          this.areas = [this.applyInfo.province_id, this.applyInfo.city_id,
            this.applyInfo.county_id || -1, this.applyInfo.town_id || -1]
          if (this.applyInfo.is_receipt) {
            this.checkList = ['有发票']
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
  .mb10 {
    margin-bottom: 10px;
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
  .repair-steps {
    padding: 5px 10px;
  }
  .qww-repair-step {
    position: relative;
  }
  .repair-step-sxh {
    position: relative;
  }
  .mb10 {
    margin-bottom: 10px;
  }
  .pt10 {
    padding-top: 10px;
  }
  .repair-steps .repair-step {
    padding: 20px 9px 0;
    margin: -5px -10px 15px;
    border: 1px solid #e6e6e6;
  }
  .repair-step-curr, .repair-steps .repair-step-curr {
    border: 1px solid #ff6c00;
  }
  .repair-steps .repair-step-curr {
    border-color: #999;
  }
  .form .item {
    display: block;
    margin-bottom: 20px;
    line-height: 30px;
  }
  .form .item:after {
    content: ".";
    display: block;
    height: 0;
    clear: both;
    visibility: hidden;
  }
  .form .item span.label {
    float: left;
    height: 25px;
    line-height: 25px;
    padding: 6px 0;
    width: 122px;
    margin-right: 3px;
    text-align: right;
  }
  .form em {
    color: #e4393c;
  }
  .fl {
    float: left;
  }
  .clear, .clr {
    display: block;
    overflow: hidden;
    clear: both;
    height: 0;
    line-height: 0;
    font-size: 0;
  }
  .list-type li {
    position: relative;
    margin-bottom: 5px;
    float: left;
    margin-right: 14px;
    white-space: nowrap;
    overflow: hidden;
  }
  .list-type-new li a {
    display: block;
    border: 1px solid #ebebeb;
    text-align: center;
    height: 18px;
    line-height: 18px;
    padding: 6px 28px;
  }
  /deep/ .el-form-item__label {
    width: 141px !important;
  }
  .comments-images {
    width: 630px;
    height: 113px;
    overflow: hidden;
    &.limit /deep/ .el-upload--picture-card { display: none !important; }
    /deep/ {
      .el-upload-list__item, .el-upload--picture-card, .el-progress {
        width: 100px;
        height: 100px;
      }
      .el-upload--picture-card, .el-progress {
        line-height: 100px;
      }
      .el-upload-list__item-status-label i {
        margin-top: 6px !important;
      }
      .el-progress-circle {
        width: 75px !important;
        height: 75px !important;
        margin: 12.5px !important;
      }
      .el-progress__text {
        font-size: 14px !important;
      }
      .el-icon-plus {
        line-height: inherit !important;
      }
    }
  }
  .p-title {
    font-size: 12px;
    color: #a0a0a0;
  }
  .mt-10 a:hover {
    color: red;
  }
  .ml5 {
    margin-left: 5px;
  }
  .ftx-05, .ftx05 {
    color: #005ea7;
  }
  .confirm-info .fl {
    height: 36px;
    line-height: 36px;
  }
  .msg-text {
    height: 36px;
    line-height: 36px;
    color: #a0a0a0;
  }
  .submit-btn-qww {
    text-align: right;
  }
  .submit-btn-qww .submit-qww {
    width: 126px;
    height: 46px;
    text-align: center;
    line-height: 46px;
    color: #fff;
    font-size: 18px;
    background: #e2231a;
    display: inline-block;
  }
</style>
