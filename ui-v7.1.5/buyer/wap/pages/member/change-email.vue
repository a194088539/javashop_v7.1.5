<template>
  <div id="change-email">
    <nav-bar title="修改电子邮箱"/>
    <div v-if="step !== 3" class="change-mobile-container">
      <div v-show="step === 1" class="valid-mobile">
        <van-cell-group :border="false">
          <van-field v-model="bindMobile" readonly>
            <span slot="label">已验证手机</span>
          </van-field>
          <van-field
            v-model="validMobileForm.img_code"
            clearable
            label="图片验证码"
            placeholder="请输入图片验证码"
            maxlength="4"
          >
            <img v-show="valid_img_url" :src="valid_img_url" slot="button" @click="getValidImgUrl" class="captcha-img">
          </van-field>
          <van-field
            v-model="validMobileForm.sms_code"
            center
            clearable
            label="短信验证码"
            placeholder="请输入短信验证码"
            maxlength="6"
          >
            <en-count-down-btn slot="button" :start="sendValidMobileSms">发送验证码</en-count-down-btn>
          </van-field>
        </van-cell-group>
        <div class="big-btn">
          <van-button size="large" :disabled="val_disabled_mobile" @click="submitValMobileForm">提交校验</van-button>
        </div>
      </div>
      <div v-show="step === 2" class="change-email">
        <van-cell-group :border="false">
          <van-field
            v-model="changeEmailForm.email"
            clearable
            label="电子邮箱"
            placeholder="请输入新的电子邮箱"
            maxlength="100"
          />
          <van-field
            v-model="changeEmailForm.img_code"
            clearable
            label="图片验证码"
            placeholder="请输入图片验证码"
            maxlength="4"
          >
            <img v-show="valid_img_url" :src="valid_img_url" slot="button" @click="getValidImgUrl" class="captcha-img">
          </van-field>
          <van-field
            v-model="changeEmailForm.email_code"
            center
            clearable
            label="邮箱验证码"
            placeholder="请输入邮箱验证码"
            maxlength="6"
          >
            <en-count-down-btn slot="button" :start="sendChangeEmailCode">发送验证码</en-count-down-btn>
          </van-field>
        </van-cell-group>
        <div class="big-btn">
          <van-button size="large" :disabled="val_disabled_change" @click="submitChangeForm">确认修改</van-button>
        </div>
      </div>
    </div>
    <div v-else class="change-success">
      <div class="inner-success">
        <img src="../../assets/images/icon-success.png" class="icon-success">
        <div class="success-title">
          您的电子邮箱已成功更换为：<p class="success-mobile">{{ changeEmailForm.email }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import { mapActions, mapGetters } from 'vuex'
  import * as API_Common from '@/api/common'
  import * as API_Safe from '@/api/safe'
  import { Foundation, RegExp } from '@/ui-utils'
  import Storage from '@/utils/storage'
  export default {
    name: 'change-email',
    head() {
      return {
        title: `修改电子邮箱-${this.site.title}`
      }
    },
    data() {
      return {
        uuid: Storage.getItem('uuid'),
        /** 步骤 */
        step: 1,
        /** 校验手机号 表单 */
        validMobileForm: {
          img_code: '',
          sms_code: ''
        },
        /** 图片验证码URL */
        valid_img_url: '',
        /** 更换电子邮箱 表单 */
        changeEmailForm: {
          email: '',
          img_code: '',
          email_code: ''
        }
      }
    },
    mounted() {
      this.$nextTick(this.getValidImgUrl)
    },
    computed: {
      ...mapGetters(['user']),
      bindMobile() {
        return Foundation.secrecyMobile(this.$store.getters.user.mobile)
      },
      /** 校验手机号 按钮是否禁用 */
      val_disabled_mobile() {
        const { img_code, sms_code } = this.validMobileForm
        return !(img_code && sms_code)
      },
      /** 更换电子邮箱 按钮是否禁用 */
      val_disabled_change() {
        const { email, img_code, email_code } = this.changeEmailForm
        return !(email && img_code && email_code)
      }
    },
    methods: {
      /** 获取图片验证码URL */
      getValidImgUrl() {
        this.valid_img_url = API_Common.getValidateCodeUrl(this.uuid, this.step === 1 ? 'VALIDATE_MOBILE' : 'BIND_EMAIL')
      },
      /** 发送手机验证码 */
      sendValidMobileSms() {
        return new Promise((resolve, reject) => {
          const { uuid } = this
          const { img_code } = this.validMobileForm
          if (!img_code) {
          this.$message.error('请填写图片验证码！')
          return false
        }
        API_Safe.sendMobileSms(this.uuid, img_code).then(() => {
          this.$message.success('发送成功，请注意查收！')
          resolve()
        }).catch(reject)
      })
      },
      /** 校验更换手机号验证码 */
      submitValMobileForm() {
        const { sms_code } = this.validMobileForm
        API_Safe.validChangeMobileSms(sms_code).then(() => {
          this.step = 2
          this.getValidImgUrl()
        })
      },
      /** 修改电子邮箱 发送验证码 */
      sendChangeEmailCode() {
        return new Promise((resolve, reject) => {
          const { uuid } = this
          const { email, img_code } = this.changeEmailForm
          if (!RegExp.email.test(email)) {
          this.$message.error('电子邮箱格式有误！')
          return false
        }
        if (!img_code) {
          this.$message.error('请填写图片验证码！')
          return false
        }
        API_Safe.sendBindEmailCode(email, img_code, uuid).then(() => {
          this.$message.success('验证码发送成功，请注意查收！')
          resolve()
        }).catch(reject)
      })
      },
      /** 确认修改 */
      submitChangeForm() {
        const { email, email_code } = this.changeEmailForm
        if (!RegExp.email.test(email)) {
          this.$message.error('电子邮箱格式有误！')
          return false
        }
        API_Safe.bindEmail(email, email_code).then(() => {
          this.$message.success('更换成功！')
          this.$store.dispatch('user/getUserDataAction')
          this.step = 3
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../assets/styles/color";
  .captcha-img {
    width: 70px;
    height: 24px;
  }
  .change-mobile-container {
    padding-top: 46px;
  }
  .change-success {
    padding-top: 46px;
    width: 100%;
    min-height: 300px;
    .inner-success {
      display: flex;
      justify-content: center;
      align-items: center;
      margin-top: 20px;
    }
    .icon-success {
      width: 50px;
      height: 50px;
      margin-right: 15px;
    }
    .success-title {
      font-size: 16px;
      color: #333;
      .success-mobile {
        margin-top: 5px;
      }
    }
  }
</style>
