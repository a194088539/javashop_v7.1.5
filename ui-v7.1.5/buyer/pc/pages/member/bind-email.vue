<template>
  <div id="bind-email">
    <el-alert v-if="step === 1" type="info" title="" :closable="false">
      <h2>为什么要进行身份验证？</h2>
      <p>1. 为保障您的账户信息安全，在变更账户中的重要信息时需要身份验证，感谢您的理解与支持。 </p>
      <p>2. 验证身份遇到问题？请提供用户名，手机号，历史发票，点击联系我司 在线客服 或者拨打400*****400咨询。</p>
    </el-alert>
    <el-alert v-if="step === 2 || step === 3" type="info" title="" :closable="false">
      <h2>为什么要验证电子邮箱？</h2>
      <p>1. 验证电子邮箱可加强账户安全，您可以使用已验证电子邮箱快速找回密码或支付密码。 </p>
      <p>2. 已验证电子邮箱可用于接收账户订单变动提醒。</p>
    </el-alert>
    <template v-if="step !== 3">
      <div v-show="step === 1" class="valid-mobile-container">
        <el-form v-if="user" :model="validMobileForm" :rules="validMobileRules" ref="validMobileForm" label-width="110px">
          <el-form-item label="已验证手机：">
            <h2>{{ user.mobile | secrecyMobile }}</h2>
          </el-form-item>
          <el-form-item label="图片验证码：" prop="img_code" class="img-code">
            <el-input v-model="validMobileForm.img_code" placeholder="请输入图片验证码" clearable :maxlength="4">
              <img slot="append" :src="valid_img_url" @click="getValidImgUrl">
            </el-input>
          </el-form-item>
          <el-form-item label="短信验证码：" prop="sms_code" class="sms-code">
            <el-input v-model="validMobileForm.sms_code" placeholder="请输入短信验证码" clearable :maxlength="6">
              <en-count-down-btn :time="60" :start="sendValidMobileSms" @end="getValidImgUrl" slot="append"/>
            </el-input>
          </el-form-item>
          <el-form-item label="">
            <el-button @click.stop="submitValMobileForm">提交验证</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div v-show="step === 2" class="change-email-container">
        <el-form :model="changeEmailForm" :rules="changeEmailRules" ref="changeEmailForm" label-width="160px">
          <el-form-item label="请输入电子邮箱：" prop="email">
            <el-input v-model="changeEmailForm.email" placeholder="请输入电子邮箱" :maxlength="100"></el-input>
          </el-form-item>
          <el-form-item label="图片验证码：" prop="img_code" class="img-code">
            <el-input v-model="changeEmailForm.img_code" placeholder="请输入图片验证码" clearable :maxlength="4">
              <img slot="append" :src="valid_img_url" @click="getValidImgUrl">
            </el-input>
          </el-form-item>
          <el-form-item label="请输入邮箱验证码：" prop="email_code">
            <el-input v-model="changeEmailForm.email_code" placeholder="请输入邮箱验证码" auto-complete="off">
              <en-count-down-btn :time="60" :start="sendChangeEmailCode" @end="getValidImgUrl" slot="append"/>
            </el-input>
          </el-form-item>
          <el-form-item label="">
            <el-button @click.stop="submitChangeForm">确认修改</el-button>
          </el-form-item>
        </el-form>
      </div>
    </template>
    <div v-else class="change-success-container">
      <div class="inner-success">
        <img src="../../assets/images/icon-success.png" class="icon-success">
        <div class="success-title">
          <p class="p1">您已成功更换电子邮箱：<span class="success-mobile">{{changeEmailForm.email }}</span></p>
          <p class="p2">您可能需要：<nuxt-link to="/member/account-safe">返回安全中心</nuxt-link></p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import { mapActions, mapGetters } from 'vuex'
  import * as API_Common from '@/api/common'
  import * as API_Safe from '@/api/safe'
  import { RegExp } from '@/ui-utils'
  import Storage from '@/utils/storage'
  export default {
    name: 'bind-email',
    head() {
      return {
        title: `更改电子邮箱-${this.site.title}`
      }
    },
    data() {
      return {
        uuid: Storage.getItem('uuid'),
        /** 步骤 */
        step: 1,
        /** 校验手机号 表单 */
        validMobileForm: {}, // 图片验证码
        /** 校验手机号 表单规则 */
        validMobileRules: {
          img_code: [{ required: true, message: '请输入图片验证码', trigger: 'blur' }],
          sms_code: [{ required: true, message: '请输入短信验证码', trigger: 'blur' }]
        },
        /** 图片验证码URL */
        valid_img_url: '',
        /** 更换电子邮箱 表单 */
        changeEmailForm: {},
        /** 更换电子邮箱 表单规则 */
        changeEmailRules: {
          email: [
            this.MixinRequired('请填写电子邮箱！'),
            {
              required: true,
              validator: (rule, value, callback) => {
              if (!RegExp.email.test(value)) {
        callback(new Error('电子邮箱格式不正确！'))
      } else {
        callback()
      }
    }
    }
    ],
      img_code: [this.MixinRequired('请输入图片验证码！')],
        email_code: [this.MixinRequired('请输入电子邮箱验证码！')],
    }
    }
    },
    mounted() {
      this.$nextTick(this.getValidImgUrl)
    },
    computed: {
      ...mapGetters(['user'])
    },
    methods: {
      /** 获取图片验证码URL */
      getValidImgUrl() {
        this.valid_img_url = API_Common.getValidateCodeUrl(this.uuid, this.step === 1 ? 'VALIDATE_MOBILE' : 'BIND_EMAIL')
      },
      /** 发送手机验证码 */
      sendValidMobileSms() {
        return new Promise((resolve, reject) => {
          this.$refs['validMobileForm'].validateField('img_code', (error) => {
            if (error) {
              reject()
              this.$message.error('请输入图片验证码！')
            } else {
              const { uuid } = this
              const { img_code } = this.validMobileForm
              API_Safe.sendMobileSms(this.uuid, img_code).then(() => {
                this.$message.success('发送成功，请注意查收！')
                resolve()
              }).catch(() => {
          this.getValidImgUrl()
          reject()
        })
      }
      })
      })
      },
      /** 校验更换手机号验证码 */
      submitValMobileForm() {
        this.$refs['validMobileForm'].validate((valid) => {
          if (valid) {
            const { sms_code } = this.validMobileForm
            API_Safe.validChangeMobileSms(sms_code).then(() => {
              this.step = 2
              this.getValidImgUrl()
            }).catch(this.getValidImgUrl)
          } else {
            this.$message.error('表单填写有误，请检查！')
            return false
          }
        })
      },

      /** 修改电子邮箱 发送验证码 */
      sendChangeEmailCode() {
        return new Promise((resolve, reject) => {
          const form = this.$refs['changeEmailForm']
          form.validateField('email', (error1) => {
            form.validateField('img_code', (error2) => {
              if (error1 || error2) {
          reject()
          this.$message.success('表单填写有误，请检查！')
        } else {
          const { uuid } = this
          const { email, img_code } = this.changeEmailForm
          API_Safe.sendBindEmailCode(email, img_code, uuid).then(() => {
            this.$message.success('验证码发送成功，请注意查收！')
            resolve()
          }).catch(() => {
            this.getValidImgUrl()
            reject()
          })
        }
      })
      })
      })
      },
      /** 确认修改 */
      submitChangeForm() {
        this.$refs['changeEmailForm'].validate((valid) => {
          if (valid) {
            const { email, email_code } = this.changeEmailForm
            API_Safe.bindEmail(email, email_code).then(() => {
              this.$message.success('更换成功！')
              this.$store.dispatch('user/getUserDataAction')
              this.step = 3
            }).catch(this.getValidImgUrl)
          } else {
            this.$message.error('请输入电子邮箱验证码！')
            return false
          }
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../assets/styles/color";
  /deep/ .el-alert {
    h2 { margin: 20px 0 }
    p { margin-bottom: 10px }
  }
  .valid-mobile-container, .change-email-container {
    width: 100%;
    /deep/ .el-form {
      margin-top: 10px;
      padding-left: 24px
    }
    /deep/ .el-input { width: auto }
    /deep/ .el-input__inner { width: 190px }
    /deep/ .img-code {
      .el-input { width: auto }
      .el-input-group__append {
        padding: 0;
        cursor: pointer;
        img { height: 38px }
      }
    }
    /deep/ .sms-code {
      .el-input__inner { width: 190px }
    }
  }
  .change-success-container {
    display: flex;
    justify-content: center;
    width: 100%;
    min-height: 300px;
    .inner-success {
      display: flex;
      margin-top: 30px;
    }
    .icon-success {
      width: 50px;
      height: 50px;
      margin-right: 15px;
    }
    .success-title {
      .p1 {
        font-size: 16px;
        color: #333;
        span {
          display: inline-block;
          padding: 3px 5px;
          background-color: #1E9DFF;
          color: #fff;
          border-radius: 10px;
        }
      }
      .p2 {
        font-size: 12px;
        color: #666;
        a {
          color: $color-href;
          &:hover { color: $color-main }
        }
      }
    }
  }
</style>
