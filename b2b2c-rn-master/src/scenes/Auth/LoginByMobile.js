/**
 * Created by Andste on 2019-01-29.
 */
import React, { PureComponent } from 'react'
import {
  View,
  Text,
  TouchableOpacity,
  StyleSheet
} from 'react-native'
import { connect } from 'react-redux'
import { messageActions } from '../../redux/actions'
import { colors } from '../../../config'
import { Screen, RegExp } from '../../utils'
import { InputLabel, BigButton } from '../../widgets'
import * as API_Common from '../../apis/common'
import * as API_Passport from '../../apis/passport'

class LoginByMobile extends PureComponent {
  constructor(props) {
    super(props)
    this.state = {
      // 手机号码
      mobile: '',
      // 短信验证码
      sms_code: '',
      // 图片验证码
      valid_code: '',
      // 图片验证码URL
      valid_code_url: API_Common.getValidateCodeUrl('LOGIN'),
      // 是否禁用登录按钮
      disabled_btn: true,
      // 登录按钮文字
      login_text: '发送验证码',
      // 发送验证码成功
      sms_send_success: false,
      // 重发验证码文本
      sms_send_text: ''
    }
  }
  
  componentWillUnmount() {
    clearInterval(this.interval)
  }
  
  /**
   * 获取图片验证码url
   * @private
   */
  _getValidCodeUrl = () => {
    this.setState({
      valid_code_url: API_Common.getValidateCodeUrl('LOGIN')
    })
  }
  
  /**
   * 手机号发生改变
   * @param text
   * @private
   */
  _onMobileChange = (text) => {
    this.setState({ mobile: text }, this._disabledLoginBtn)
  }
  
  /**
   * 图片验证码发生改变
   * @param text
   * @private
   */
  _onValidCodeChange = (text) => {
    this.setState({ valid_code: text }, this._disabledLoginBtn)
  }
  
  /**
   * 短信验证码发生改变
   * @param text
   * @private
   */
  _onSmsCodeChange = (text) => {
    this.setState({ sms_code: text }, this._disabledLoginBtn)
  }
  
  /**
   * 发送短信验证码
   * @returns {Promise<void>}
   * @private
   */
  _onSendSmsCode = async () => {
    const { mobile, valid_code } = this.state
    if (this.time > 0) return
    try {
      await this.setState({ disabled_btn: true })
      await API_Passport.sendLoginSms(mobile, valid_code)
      this.props.dispatch(messageActions.success('发送成功，请注意查收！'))
      this._onCountDown()
      this.setState({
        sms_send_success: true,
        login_text: '登录'
      })
    } catch (e) {
      this._getValidCodeUrl()
    }
  }
  
  /**
   * 检测是否禁用登录按钮
   * @private
   */
  _disabledLoginBtn = () => {
    const { mobile, valid_code, sms_code, sms_send_success } = this.state
    if (sms_send_success) {
      this.setState({ disabled_btn: !mobile || !valid_code || !sms_code })
    } else {
      this.setState({ disabled_btn: !mobile || !valid_code })
    }
  }
  
  /**
   * 倒计时
   * @private
   */
  _onCountDown = () => {
    this.time = 60
    this.interval = setInterval(() => {
      this.time--
      if (this.time <= 0) {
        clearInterval(this.interval)
        this.setState({ sms_send_text: '重新获取'} )
        return
      }
      this.setState({ sms_send_text: this.time + 's' })
    }, 1000)
  }
  
  /**
   * 点击按钮
   * @private
   */
  _onPressLogin = () => {
    const { mobile, sms_code, valid_code, sms_send_success } = this.state
    if (sms_send_success) {
      this.props.onLogin({ mobile, sms_code })
    } else {
      const { dispatch } = this.props
      if (!RegExp.mobile.test(mobile)) {
        dispatch(messageActions.error('手机号码格式有误！'))
        return
      }
      if (!valid_code) {
        dispatch(messageActions.error('请输入图片验证码！'))
        return
      }
      this._onSendSmsCode()
    }
  }
  
  render() {
    const { mobile, sms_code, sms_send_success, valid_code, valid_code_url, disabled_btn, login_text, sms_send_text } = this.state
    return (
      <View style={ styles.container }>
        <InputLabel
          label="手机号"
          labelStyle={ styles.label_style }
          placeholder="请输入手机号"
          keyboardType="phone-pad"
          value={ mobile }
          maxLength={ 11 }
          onChangeText={ this._onMobileChange }
        />
        <InputLabel
          label="验证码"
          labelStyle={ styles.label_style }
          placeholder="请输入图片验证码"
          keyboardType="email-address"
          value={ valid_code }
          validcodeUrl={ valid_code_url }
          maxLength={ 4 }
          onPressValidcodeImage={ this._getValidCodeUrl }
          onChangeText={ this._onValidCodeChange }
        />
        { sms_send_success ? (
          <View style={ styles.sms_code_view }>
            <InputLabel
              label="校验码"
              labelStyle={ styles.label_style }
              placeholder="请输入短信验证码"
              keyboardType="number-pad"
              value={ sms_code }
              maxLength={ 6 }
              onChangeText={ this._onSmsCodeChange }
            />
            <TouchableOpacity style={ styles.sms_code_btn } onPress={ this._onSendSmsCode }>
              <Text style={ { color: sms_send_text === '重新获取' ? '#1a73e8' : '#D5D4D5' } }>{ sms_send_text }</Text>
            </TouchableOpacity>
          </View>
        ) : undefined }
        <BigButton
          style={ styles.login_btn }
          disabled={ disabled_btn }
          title={ login_text }
          onPress={ this._onPressLogin }
        />
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    marginTop: 20,
    paddingHorizontal: 20
  },
  label_style: {
    width: 63,
    fontSize: 14
  },
  login_btn: {
    width: Screen.width - 40,
    marginTop: 20,
    height: 46
  },
  sms_code_view: {
    justifyContent: 'center'
  },
  sms_code_btn: {
    position: 'absolute',
    right: 10
  }
})

export default connect()(LoginByMobile)
