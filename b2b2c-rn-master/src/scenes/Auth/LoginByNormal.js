/**
 * Created by Andste on 2019-01-29.
 */
import React, { PureComponent } from 'react'
import {
  View,
  StyleSheet
} from 'react-native'
import { Screen } from '../../utils'
import { InputLabel, BigButton } from '../../widgets'
import * as API_Common from '../../apis/common'

export default class LoginByNormal extends PureComponent {
  constructor(props) {
    super(props)
    this.state = {
      // 用户名
      username: '',
      // 密码
      password: '',
      // 图片验证码
      valid_code: '',
      // 图片验证码url
      valid_code_url: API_Common.getValidateCodeUrl('LOGIN'),
      // 是否禁用登录按钮
      disabled_btn: true,
      // 登录按钮文字
      login_text: '登录'
    }
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
   * 账户名发生改变
   * @param text
   * @private
   */
  _onNameChange = (text) => {
    this.setState({ username: text }, this._disabledLoginBtn)
  }
  
  /**
   * 密码发生改变
   * @param text
   * @private
   */
  _onPasswordChange = (text) => {
    this.setState({ password: text }, this._disabledLoginBtn)
  }
  
  /**
   * 图片验证码发生改变
   * @param text
   * @private
   */
  _onValidcodeChange = (text) => {
    this.setState({ valid_code: text }, this._disabledLoginBtn)
  }
  
  /**
   * 检测是否禁用登录按钮
   * @private
   */
  _disabledLoginBtn = () => {
    const { username, password, valid_code } = this.state
    this.setState({
      disabled_btn: !username || !password || !valid_code
    })
  }
  
  /**
   * 登录
   * @private
   */
  _loginAction = async () => {
    const { username, password, valid_code } = this.state
    try {
      await this.props.onLogin({ username, password, valid_code })
    } catch (e) {
      this._getValidCodeUrl()
    }
  }
  
  render() {
    const { username, password, valid_code, valid_code_url, disabled_btn, login_text } = this.state
    return (
      <View style={ styles.container }>
        <InputLabel
          label="账号"
          labelStyle={ styles.label_style }
          placeholder="用户名/邮箱/手机号"
          keyboardType="email-address"
          value={ username }
          onChangeText={ this._onNameChange }
        />
        <InputLabel
          label="密码"
          labelStyle={ styles.label_style }
          placeholder="请输入密码"
          secureTextEntry={ true }
          value={ password }
          onChangeText={ this._onPasswordChange }
        />
        <InputLabel
          label="验证码"
          labelStyle={ styles.label_style }
          placeholder="请输入验证码"
          keyboardType="email-address"
          value={ valid_code }
          onChangeText={ this._onValidcodeChange }
          validcodeUrl={ valid_code_url }
          onPressValidcodeImage={ this._getValidCodeUrl }
          maxLength={ 4 }
        />
        <BigButton
          style={ styles.login_btn }
          disabled={ disabled_btn }
          title={ login_text }
          onPress={ this._loginAction }
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
    width: 63
  },
  login_btn: {
    width: Screen.width - 40,
    marginTop: 20,
    height: 46
  }
})
