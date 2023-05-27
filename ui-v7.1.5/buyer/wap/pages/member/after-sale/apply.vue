<template>
  <div id="choose-type" style="background-color: #f7f7f7">
    <van-nav-bar left-arrow title="申请售后" @click-left="MixinRouterBack"></van-nav-bar>
    <div class="after-sale-container">
      <div class="goods-info-box">
        <div class="seller-tip">
          <span class="tip01">
            本次售后服务将由<span class="tip02">{{ applyInfo.seller_name }}</span>为您提供
          </span>
        </div>
        <div class="goods-info-item">
          <div class="goods-img">
            <img :src="applyInfo.goods_img">
          </div>
          <div class="item-name">
            <span>{{ applyInfo.goods_name }}</span>
            <div class="price-num">
              <span class="price-show">单价：{{ applyInfo.goods_price | unitPrice('￥') }}</span>
              <span class="price-show">购买数量：{{ applyInfo.buy_num }}</span>
            </div>
          </div>
        </div>
        <div class="apply-num">
          <span>申请数量</span>
          <van-stepper class="change-num" v-model="applyForm.return_num" integer :min="1" :max="applyInfo.buy_num"/>
        </div>
      </div>
      <div class="apply-info-box">
        <div class="apply-reason">
          <div class="reason-show" @click="reasonSelectShow = true">
            <span>申请原因</span>
            <div style="flex-direction: row; align-items: center;">
              <span style="font-weight: bold;">{{ applyForm.reason }}</span>
              <van-icon name="arrow" style="margin: 0px 5px;"/>
            </div>
          </div>
          <div class="black-line"></div>
          <div class="desc-show">
            <textarea v-model="applyForm.problem_desc" class="desc-text" maxlength="200" placeholder="请描述申请售后服务的具体原因，不能超过200个字符，还可上传最多5张图片哦~"></textarea>
          </div>
          <div class="upload-img">
            <div class="img-list" v-for="(image, index) in applyForm.images" :key="index" v-if="applyForm.images && applyForm.images.length > 0">
              <div class="del-img">
                <van-icon name="clear" @click="applyForm.images.splice(index, 1)"/>
              </div>
              <div style="background-color: transparent; cursor: pointer;">
                <img :src="image">
              </div>
            </div>
            <span class="file" v-if="applyForm.images.length < 5">
              <div class="file-box">
                <van-uploader :after-read="(file) => { onFileRead(file) }" accept="image/png, image/jpeg, image/jpg">
                  <van-icon name="photograph" />
                </van-uploader>
              </div>
            </span>
          </div>
          <div><span style="color: red;">为了帮您更好的解决问题,请上传照片凭证</span></div>
        </div>
      </div>
      <div class="refund-info-box" v-if="service_type === 'RETURN_GOODS'">
        <div class="refund-item">
          <div class="item-key">
            <span>退款方式</span>
            <div class="item-val">
              <span v-if="applyInfo.is_retrace">原路退回</span>
              <span v-else>账号退款</span>
            </div>
            <img src="../../../assets/images/icon-warn.png" @click="handleTitleShow">
          </div>
          <div v-if="!applyInfo.is_retrace">
            <div class="item-key">
              <span>账户类型</span>
              <div class="item-val" @click="accountTypeSelectShow = true">
                <div style="flex-direction: row; align-items: center;">
                  <span style="font-weight: bold;">{{ accountTypeText }}</span>
                  <van-icon name="arrow" style="margin: 0px 5px;"/>
                </div>
              </div>
            </div>
            <div v-if="applyForm.account_type === 'BANKTRANSFER'">
              <div class="item-key">
                <span>银行名称</span>
                <div class="item-val">
                  <van-field v-model="applyForm.bank_name" clearable right-icon="question-o" placeholder="请输入银行名称"/>
                </div>
              </div>
              <div class="item-key">
                <span>银行开户行</span>
                <div class="item-val">
                  <van-field v-model="applyForm.bank_deposit_name" clearable right-icon="question-o" placeholder="请输入银行开户行"/>
                </div>
              </div>
              <div class="item-key">
                <span>银行开户名</span>
                <div class="item-val">
                  <van-field v-model="applyForm.bank_account_name" clearable right-icon="question-o" placeholder="请输入银行开户名"/>
                </div>
              </div>
              <div class="item-key">
                <span>银行账号</span>
                <div class="item-val">
                  <van-field v-model="applyForm.bank_account_number" clearable right-icon="question-o" placeholder="请输入银行账号"/>
                </div>
              </div>
            </div>
            <div class="item-key" v-else>
              <span>退款账号</span>
              <div class="item-val">
                <van-field v-model="applyForm.return_account" clearable right-icon="question-o" placeholder="请输入退款账号"/>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="refund-info-box">
        <div class="refund-item">
          <div class="item-key">
            <span>返回方式</span>
            <div class="item-val">
              <span>快递至第三方卖家</span>
            </div>
            <img src="../../../assets/images/icon-warn.png" @click="handleTipsShow">
          </div>
          <div class="item-key">
            <span>申请凭证</span>
            <div class="item-val" @click="voucherPopupShow = true">
              <div style="flex-direction: row; align-items: center;">
                <span style="font-weight: bold;">{{ voucherText }}</span>
                <van-icon name="arrow" style="margin: 0px 5px;"/>
              </div>
            </div>
          </div>
          <div class="item-key">
            <span>收货地址</span>
            <div class="item-val" @click="showAddressSelector = true">
              <div style="flex-direction: row; align-items: center; width: 90%;">
                <span style="font-weight: bold; width: 100%; text-align: right; text-overflow: ellipsis;">{{ addrText }}</span>
                <van-icon name="arrow" style="margin: 0px 5px;"/>
              </div>
            </div>
          </div>
          <div class="item-key">
            <span>详细地址</span>
            <div class="item-val">
              <van-field v-model="applyForm.ship_addr" clearable right-icon="question-o" placeholder="请输入详细地址"/>
            </div>
          </div>
          <div class="item-key">
            <span>联系人</span>
            <div class="item-val">
              <van-field v-model="applyForm.ship_name" clearable right-icon="question-o" placeholder="请输入联系人名称"/>
            </div>
          </div>
          <div class="item-key">
            <span>联系方式</span>
            <div class="item-val">
              <van-field v-model="applyForm.ship_mobile" clearable right-icon="question-o" placeholder="请输入联系方式"/>
            </div>
          </div>
        </div>
      </div>
      <div class="tips-box">
        <span>提交服务单后，售后专员可能与您电话沟通，请保持手机畅通</span>
      </div>
      <van-button type="danger" size="normal" @click="submitApplyForm">提交</van-button>
    </div>
    <!-- 申请原因下拉框 -->
    <van-actionsheet v-model="reasonSelectShow" :actions="reasonSelectActions" @select="onSelectReason" />
    <!-- 账户类型下拉框 -->
    <van-actionsheet v-model="accountTypeSelectShow" :actions="accountTypeSelectActions" @select="onSelectAccountType" />
    <!-- 申请凭证弹出层 -->
    <van-popup v-model="voucherPopupShow" position="bottom" :overlay="true" :close-on-click-overlay="false">
      <div>
        <span class="voucher-title">请选择申请凭证</span>
      </div>
      <van-checkbox-group v-model="voucherList">
        <van-cell-group>
          <van-cell title="有发票" clickable>
            <van-checkbox slot="right-icon" name="有发票" />
          </van-cell>
          <van-cell title="有检测报告" clickable>
            <van-checkbox slot="right-icon" name="有检测报告" />
          </van-cell>
        </van-cell-group>
      </van-checkbox-group>
      <van-button type="danger" @click="handleChooseVoucher">确定</van-button>
    </van-popup>
    <!-- 地区选择器 -->
    <en-region-picker :show="showAddressSelector" :api="MixinRegionApi" :default="regions" @closed="showAddressSelector = false" @changed="handleAddressSelectorChanged"/>
  </div>
</template>

<script>
  import Vue from 'vue'
  import { Radio, RadioGroup, Actionsheet, Dialog, Checkbox, CheckboxGroup, Popup } from 'vant'
  Vue.use(Radio).use(RadioGroup).use(Actionsheet).use(Dialog).use(Checkbox).use(CheckboxGroup).use(Popup)
  import * as API_AfterSale from '@/api/after-sale'
  import request, { Method } from '@/utils/request'
  import { Foundation, RegExp } from '@/ui-utils'
  import '../../../static/lrz/lrz.all.bundle'
  export default {
    name: 'apply',
    head() {
      return {
        title: `申请售后-${this.site.title}`
      }
    },
    data() {
      const { serviceType } = this.$route.query
      return {
        /** 售后服务单号 */
        order_sn: this.$route.query.orderSn,
        /** 商品skuID */
        sku_id: this.$route.query.skuId,
        /** 售后类型 */
        service_type: this.$route.query.serviceType,
        /** 页面展示的信息 */
        applyInfo: '',
        /** 申请表单数据 */
        applyForm: {
          reason: '请选择申请原因',
          images: [],
          return_num: null,
          ship_addr: '',
          ship_name: '',
          ship_mobile: '',
          account_type: ''
        },
        /** 是否展示取消原因下拉框 */
        reasonSelectShow: false,
        /** 取消原因下拉框数据 */
        reasonSelectActions: this.getReasonActions(serviceType),
        /** 账户类型下拉框选中的值 */
        accountTypeText: '请选择账户类型',
        /** 是否展示账户类型下拉框 */
        accountTypeSelectShow: false,
        /** 账户类型下拉框数据 */
        accountTypeSelectActions: [
          { name: '支付宝', value: 'ALIPAY' },
          { name: '微信', value: 'WEIXINPAY' },
          { name: '银行卡', value: 'BANKTRANSFER' }
        ],
        /** 是否展示申请凭证弹出层 */
        voucherPopupShow: false,
        /** 选择的申请凭证 */
        voucherText: '暂无凭证',
        /** 申请凭证CheckBox数据集合 */
        voucherList: [],
        /** 收货地址地区文字展示 */
        addrText: '',
        /** 收货地址地区信息 */
        regions: null,
        /** 是否展示地区选择器 */
        showAddressSelector: false

      }
    },
    mounted() {
      this.GET_AfterSaleApplyInfo()
    },
    methods: {
      /** 获取申请原因下拉框数据 */
      getReasonActions(serviceType) {
        let actions = []
        if (serviceType === 'RETURN_GOODS') {
          actions = [
            { name: '商品降价' },
            { name: '商品与页面描述不符' },
            { name: '缺少件' },
            { name: '质量问题' },
            { name: '发错货' },
            { name: '商品损坏' },
            { name: '不想要了' },
            { name: '其他' }
          ]
          return actions
        } else if (serviceType === 'CHANGE_GOODS') {
          actions = [
            { name: '商品与页面描述不符' },
            { name: '缺少件' },
            { name: '质量问题' },
            { name: '发错货' },
            { name: '商品损坏' },
            { name: '不想要了' },
            { name: '大小/颜色/型号等不合适' },
            { name: '其他' }
          ]
          return actions
        } else {
          actions = [
            { name: '商品发货数量不对' },
            { name: '其他' }
          ]
          return actions
        }
      },

      /** 申请原因选中事件绑定 */
      onSelectReason(item) {
        this.reasonSelectShow = false
        this.applyForm.reason = item.name
      },

      /** 账户类型选中事件绑定 */
      onSelectAccountType(item) {
        this.accountTypeSelectShow = false
        this.accountTypeText = item.name
        this.applyForm.account_type = item.value
      },

      /** 选择申请凭证 */
      handleChooseVoucher() {
        this.voucherText = ''
        if (this.voucherList.length != 0) {
          this.voucherList.forEach(item => {
            this.voucherText += item + '，'
          })
          this.voucherText = this.voucherText.substr(0, this.voucherText.length-1)
        } else {
          this.voucherText = '暂无凭证'
        }
        this.voucherPopupShow = false
      },

      /** 地址选择器发生改变事件绑定 */
      handleAddressSelectorChanged(object) {
        this.applyForm.region = object.last_id
        this.addrText = object.string
      },

      /** 图片上传 */
      onFileRead(file) {
        lrz(file.file).then(async rst => {
          const res = await request({
            url: this.MixinUploadApi,
            method: Method.POST,
            headers: { 'Content-Type': 'multipart/form-data' },
            data: rst.formData
          })
          res && this.applyForm.images.push(res.url)
        })
      },

      /** 弹出提示 */
      handleTitleShow() {
        var msg = ""
        if (this.applyInfo.is_retrace) {
          msg = "退货申请审核通过后，退款将按您订单的支付方式原路退回"
        } else {
          msg = "退货申请审核通过后，平台会将退款在线转入您下面所填写的账户中"
        }
        Dialog.alert({
          closeOnClickOverlay: true,
          message: msg
        }).then(() => {})
      },

      handleTipsShow() {
        Dialog.alert({
          closeOnClickOverlay: true,
          message: '商品寄回地址将在审核通过后以短信形式告知，或在申请记录中查询。'
        }).then(() => {})
      },

      /** 校验参数 */
      handleCheckParams() {
        // 申请数量校验
        if (!this.applyForm.return_num) {
          this.$message.error('请填写申请数量！')
          return false
        }
        if (this.applyForm.return_num <= 0 || this.applyForm.return_num > this.applyInfo.buy_num) {
          this.$message.error('申请数量不能小于等于0或大于购买数量！')
          return false
        }
        // 申请原因校验
        if (!this.applyForm.reason || this.applyForm.reason === '请选择申请原因') {
          this.$message.error('请选择申请原因！')
          return false
        }
        // 问题描述校验
        if (!this.applyForm.problem_desc) {
          this.$message.error('请输入问题描述！')
          return false
        }

        // 如果当前申请的售后服务类型为退货，那么需要对退款相关信息进行校验
        if (this.service_type === 'RETURN_GOODS') {
          // 如果不支持原路退款
          if (!this.applyInfo.is_retrace) {
            // 账户类型校验
            if (!this.applyForm.account_type) {
              this.$message.error('请选择账户类型！')
              return false
            }

            // 如果账户类型不为银行卡
            if (this.applyForm.account_type != 'BANKTRANSFER') {
              // 退款账号校验
              if (!this.applyForm.return_account) {
                this.$message.error('请输入退款账号！')
                return false
              }
            } else {
              // 银行名称校验
              if (!this.applyForm.bank_name) {
                this.$message.error('请输入银行名称！')
                return false
              }
              // 银行开户行校验
              if (!this.applyForm.bank_deposit_name) {
                this.$message.error('请输入银行开户行！')
                return false
              }
              // 银行开户名校验
              if (!this.applyForm.bank_account_name) {
                this.$message.error('请输入银行开户名！')
                return false
              }
              // 银行账号校验
              if (!this.applyForm.bank_account_number) {
                this.$message.error('请输入银行账号！')
                return false
              }
            }
          }
        }

        // 详细地址校验
        if (!this.applyForm.ship_addr) {
          this.$message.error('请输入详细地址！')
          return false
        }

        // 联系人校验
        if (!this.applyForm.ship_name) {
          this.$message.error('请输入联系人！')
          return false
        }

        // 联系方式校验
        if (!this.applyForm.ship_mobile || !RegExp.mobile.test(this.applyForm.ship_mobile)) {
          this.$message.error('请输入正确格式的手机号码！')
          return false
        }

        return true
      },

      /** 提交售后服务申请 */
      submitApplyForm() {
        // 校验参数
        if (!this.handleCheckParams()) {
          return false
        }
        this.applyForm.order_sn = this.order_sn
        this.applyForm.sku_id = this.sku_id
        this.applyForm.service_type = this.service_type
        this.applyForm.apply_vouchers = this.voucherText
        this.applyForm.region = this.regions

        if (this.service_type === 'RETURN_GOODS') {
          API_AfterSale.applyReturnGoods(this.applyForm).then(() => {
            this.$message.success('提交成功')
            this.$router.replace({path: '/member/after-sale'})
          })
        } else if (this.service_type === 'CHANGE_GOODS') {
          API_AfterSale.applyChangeGoods(this.applyForm).then(() => {
            this.$message.success('提交成功')
            this.$router.replace({path: '/member/after-sale'})
          })
        } else if (this.service_type === 'SUPPLY_AGAIN_GOODS') {
          API_AfterSale.applyReplaceGoods(this.applyForm).then(() => {
            this.$message.success('提交成功')
            this.$router.replace({path: '/member/after-sale'})
          })
        } else {
          this.$message.error('申请的售后服务类型不正确！')
          return false
        }
      },

      /** 获取页面相关信息 */
      GET_AfterSaleApplyInfo() {
        API_AfterSale.getApplyInfo(this.order_sn, this.sku_id).then(response => {
          this.applyInfo = response;
          this.applyForm.return_num = this.applyInfo.buy_num
          this.addrText = this.applyInfo.province + this.applyInfo.city + this.applyInfo.county + this.applyInfo.town
          this.applyForm.ship_addr = this.applyInfo.ship_addr
          this.applyForm.ship_name = this.applyInfo.ship_name
          this.applyForm.ship_mobile = this.applyInfo.ship_mobile
          this.regions = this.applyInfo.town_id && this.applyInfo.town_id > 0 ? this.applyInfo.town_id : this.applyInfo.county_id
          if (this.applyInfo.is_receipt) {
            this.voucherText = '有发票'
            this.voucherList = ['有发票']
          }
        })
      }
    }
  }
</script>
<style type="text/scss" lang="scss" scoped>
  @import "../../../assets/styles/color";
  /deep/ .van-field__control {
    text-align: right;
    font-size: 14px;
    font-weight: bold;
  }
  /deep/ .van-cell {
    flex-direction: initial;
  }
  div {
    /*position: relative;*/
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
  .goods-info-box {
    padding-top: 6px;
    padding-bottom: 19px;
    align-items: center;
    background-color: rgb(255, 255, 255);
    border-bottom-left-radius: 13px;
    border-bottom-right-radius: 13px;
    .seller-tip {
      justify-content: center;
      align-items: center;
      padding: 4px 13px;
      margin-bottom: 22px;
      background-color: rgb(246, 246, 246);
      border-style: solid;
      border-width: 1px;
      border-color: rgb(246, 246, 246);
      border-radius: 55px;
      span {
        display: inline-block;
        margin: 0px;
        padding: 0px;
      }
      .tip01 {
        color: rgb(132, 132, 132);
        font-size: 12px;
        line-height: 14.4px;
      }
      .tip02 {
        color: rgb(255, 0, 0);
        line-height: 19.2px;
      }
    }
    .goods-info-item {
      flex-direction: row;
      align-items: center;
      width: 100%;
      .goods-img {
        width: 30%;
      }
      img {
        justify-content: flex-end;
        align-items: flex-end;
        height: 66px;
        width: 66px;
        margin: 0px 22px;
        border-radius: 4px;
      }
      .item-name {
        height: 72px;
        width: 70%;
        justify-content: space-between;
        span {
          display: -webkit-box;
          padding: 0px;
          color: rgb(132, 132, 132);
          font-size: 11px;
          line-height: 18px;
          overflow: hidden;
          text-overflow: ellipsis;
          overflow-wrap: break-word;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
          max-height: 36px;
          white-space: normal;
          margin-right: 5px;
        }
        .price-num {
          flex: 1 1 0%;
          flex-direction: row;
          margin-top: 14px;
          .price-show {
            display: inline-block;
            margin: 0px 11px 0px 0px;
            padding: 0px;
            font-size: 12px;
            font-family: JDZhengHT-Regular;
            line-height: 14.4px;
          }
        }
      }
    }
    .apply-num {
      height: 58px;
      width: 90%;
      flex-direction: row;
      justify-content: space-between;
      align-items: center;
      border-style: solid;
      border-width: 1px 0px 0px;
      border-color: rgb(238, 238, 238) black black;
      padding: 1px;
      margin-top: 20px;
      span {
        display: inline-block;
        margin: 0px;
        padding: 0px;
        font-size: 16px;
        font-weight: bold;
        line-height: 19.2px;
      }
      .change-num {
        flex-direction: row;
        height: 27px;
        align-items: center;
      }
    }
  }
  .apply-info-box {
    flex: 1 1 0%;
    padding: 16px 11px;
    .apply-reason {
      padding: 24px 16px;
      margin-bottom: 16px;
      background-color: rgb(255, 255, 255);
      border-radius: 13px;
      .reason-show {
        flex-direction: row;
        justify-content: space-between;
        align-items: flex-start;
        span {
          display: inline-block;
          margin: 0px;
          padding: 0px;
          font-size: 14px;
          color: #333;
          line-height: 16.8px;
        }
      }
      .black-line {
        width: 414px;
        margin-left: -28px;
        margin-top: 26px;
        margin-bottom: 19px;
        border-style: solid;
        border-width: 0px 0px 2px;
        border-color: black black rgb(246, 246, 246);
      }
      .desc-show {
        padding-top: 11px;
        padding-right: 13px;
        padding-left: 13px;
        background-color: rgb(246, 246, 246);
        border-style: solid;
        border-width: 1px;
        border-radius: 4px;
        border-color: rgb(246, 246, 246);
        .desc-text {
          background-color: rgb(246, 246, 246);
          border-color: black;
          border-width: 0px;
          box-sizing: border-box;
          color: inherit;
          font-style: inherit;
          font-variant: inherit;
          font-weight: inherit;
          font-stretch: inherit;
          font-size: 13px;
          line-height: inherit;
          font-family: inherit;
          padding: 0px;
          height: 88px;
        }
      }
      .upload-img {
        flex-flow: row wrap;
        justify-content: flex-start;
        align-items: center;
        margin-bottom: 13px;
        margin-top: 13px;
        width: 375px;
        .file {
          position: relative;
          display: inline-block;
          overflow: hidden;
          text-decoration: none;
          text-indent: 0;
          line-height: 20px;
          .file-box {
            justify-content: center;
            align-items: center;
            width: 71px;
            height: 71px;
            border-style: solid;
            border-width: 1px;
            border-color: rgb(191, 191, 191);
            border-radius: 4px;
            /deep/ .van-icon {
              font-size: 38px;
            }
          }
        }
        .img-list {
          height: 79px;
          width: 79px;
          justify-content: flex-end;
          align-items: flex-start;
          margin-right: 13px;
          margin-bottom: 6px;
          img {
            width: 71px;
            height: 71px;
          }
          .del-img {
            background-color: transparent;
            right: 0px;
            top: 0px;
            margin-left: 71px;
          }
        }
      }
    }
  }
  .refund-info-box {
    flex: 1 1 0%;
    padding-left: 11px;
    padding-right: 11px;
    padding-bottom: 16px;
    .refund-item {
      padding-left: 16px;
      padding-right: 16px;
      background-color: rgb(255, 255, 255);
      border-radius: 13px;
      margin-bottom: 16px;
      .item-key {
        height: 45px;
        flex-direction: row;
        align-items: center;
        span {
          display: inline-block;
          margin: 0px;
          padding: 0px;
          font-size: 14px;
          color: #333;
          line-height: 16.8px;
        }
        img {
          width: 12px;
          height: 12px;
        }
      }
      .item-val {
        flex-direction: row;
        align-items: center;
        justify-content: flex-end;
        flex: 1 1 0%;
        padding-left: 11px;
        span {
          display: inline-block;
          margin: 0px 11px 0px 0px;
          padding: 0px;
          font-size: 14px;
          font-weight: bold;
          justify-content: flex-end;
          line-height: 16.8px;
        }
      }
    }
  }
  .voucher-title {
    height: 44px;
    line-height: 44px;
    text-align: center;
    font-size: 16px;
    font-weight: bold;
  }
  .tips-box {
    justify-content: center;
    align-items: center;
    padding-bottom: 16px;
    span {
      display: inline-block;
      margin: 0px;
      padding: 0px 10px;
      color: #908e8e;
      font-size: 11px;
      line-height: 13.2px;
    }
  }
</style>
