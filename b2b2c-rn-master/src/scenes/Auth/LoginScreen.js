/**
 * Created by Andste on 2017/8/5.
 */
import React, { Component } from 'react'
import {
  Keyboard,
  StyleSheet,
  StatusBar,
  View,
  TouchableOpacity,
  Text
} from 'react-native'
import { store } from '../../redux/store'
import { userActions } from '../../redux/actions'
import Icon from 'react-native-vector-icons/Ionicons'
import { Screen } from '../../utils'
import { colors } from '../../../config'
import DismissKeyboardHOC from '../../components/DismissKeyboardHOC'
import * as API_Passport from '../../apis/passport'
import * as API_Connect from '../../apis/connect'

import LoginByNormal from './LoginByNormal'
import LoginByMobile from './LoginByMobile'
import LoginByConnect from './LoginByConnect'

const DismissKeyboardView = DismissKeyboardHOC(View)

export default class LoginScene extends Component {
  static navigationOptions = ({ navigation }) => {
    const _goBack = () => {
      Keyboard.dismiss()
      navigation.goBack()
    }
    return {
      header_left: (
        <TouchableOpacity style={ styles.header_left } onPress={ _goBack }>
          <Icon name="ios-close" style={ styles.header_left_close } size={ 44 }/>
        </TouchableOpacity>
      ),
      headerTitle: '账号登录',
      gesturesEnabled: false
    }
  }
  
  constructor(props) {
    super(props)
    this.nav = this.props.navigation
    this.state = {
      // 登录类型 账号密码登录或者手机快捷登录
      login_type: 'normal'
    }
  }
  
  /**
   * 普通登录
   * @param params
   * @returns {Promise<void>}
   * @private
   */
  _loginByNormal = (params) => {
    Keyboard.dismiss()
    const { username, password, valid_code } = params
    const { connectParams } = this
    return new Promise(async (resolve, reject) => {
      try {
        let res
        if (connectParams) {
          res = await API_Connect.connectLoginBinder({
            ...connectParams,
            username,
            password,
            captcha: valid_code,
            uuid: store.getState().user.uuid
          })
        } else {
          res = await API_Passport.login(username, password, valid_code)
        }
        this._loginSuccess(res)
        resolve(res)
      } catch (e) {
        reject(e)
      }
    })
  }
  
  /**
   * 手机号登录
   * @returns {Promise<void>}
   * @private
   */
  _loginByMobile = (params) => {
    Keyboard.dismiss()
    const { mobile, sms_code } = params
    const { connectParams } = this
    return new Promise(async (resolve, reject) => {
      try {
        let res
        if (connectParams) {
          res = await API_Connect.connectLoginBinderByMobile({
            ...connectParams,
            sms_code,
            mobile,
            uuid: store.getState().user.uuid
          })
        } else {
          res = await API_Passport.loginByMobile(mobile, sms_code)
        }
        this._loginSuccess(res)
        resolve()
      } catch (e) {
        reject(e)
      }
    })
  }
  
  /**
   * 登录成功
   * @param res
   * @returns {Promise<boolean>}
   * @private
   */
  _loginSuccess = async (res) => {
    await store.dispatch(userActions.loginSuccessAction(res))
    store.dispatch(userActions.getUserAction(this.nav.goBack))
    return false
  }
  
  /**
   * 登录类型发生改变
   * @param login_type
   * @private
   */
  _onLoginTypeChange = (login_type) => {
    this.setState({ login_type })
  }
  
  /**
   * 设置登录参数
   * @param params
   * @private
   */
  _setLoginByConnect = (params) => {
    this.connectParams = params
  }
  
  render() {
    const { state } = this
    let { login_type } = state
    return (
      <DismissKeyboardView style={ styles.container }>
        <StatusBar barStyle="dark-content"/>
        {/*登录类型切换*/ }
        <View style={ styles.type_nav }>
          <TouchableOpacity
            style={ [
              styles.nav_item,
              login_type === 'normal' && styles.nav_item_active
            ] }
            onPress={ () => this._onLoginTypeChange('normal') }
          >
            <Text
              style={ [
                styles.nav_item_text,
                login_type === 'normal' && styles.nav_item_active
              ] }
            >账号密码登录</Text>
          </TouchableOpacity>
          <TouchableOpacity
            style={ [
              styles.nav_item,
              login_type === 'mobile' && styles.nav_item_active
            ] }
            onPress={ () => this._onLoginTypeChange('mobile') }
          >
            <Text
              style={ [
                styles.nav_item_text,
                login_type === 'mobile' && styles.nav_item_active
              ] }
            >手机快捷登录</Text>
          </TouchableOpacity>
        </View>
        {/*登录表单*/ }
        { login_type === 'normal' ? (
          <LoginByNormal onLogin={ this._loginByNormal }/>
        ) : (
          <LoginByMobile onLogin={ this._loginByMobile }/>
        ) }
        <View style={ styles.login_other }>
          <TouchableOpacity onPress={ () => this.nav.navigate('Register') }>
            <Text style={ styles.login_other_text }>手机快速注册</Text>
          </TouchableOpacity>
          <TouchableOpacity onPress={ () => this.nav.navigate('ForgetPassword') }>
            <Text style={ styles.login_other_text }>忘记密码</Text>
          </TouchableOpacity>
        </View>
        {/*第三方登录*/ }
        <LoginByConnect loginByConnect={ this._setLoginByConnect } nav={ this.nav }/>
      </DismissKeyboardView>
    )
  }
}

const styles = StyleSheet.create({
  header_left: {
    width: 44,
    height: 44,
    justifyContent: 'center',
    alignItems: 'center'
  },
  header_left_close: {
    width: 20,
    height: 44
  },
  container: {
    flex: 1,
    backgroundColor: '#FFFFFF'
  },
  type_nav: {
    flexDirection: 'row',
    justifyContent: 'space-around',
    width: Screen.width,
    height: 40
  },
  nav_item: {
    justifyContent: 'center',
    alignItems: 'center',
    width: Screen.width / 3,
    height: 39,
    borderBottomWidth: 1,
    borderBottomColor: '#FFF'
  },
  nav_item_text: {
    fontSize: 16
  },
  nav_item_active: {
    borderBottomColor: colors.main,
    color: colors.main
  },
  login_other: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    paddingHorizontal: 20,
    marginTop: 30
  },
  login_other_text: {
    fontSize: 16,
    color: colors.navigator_tint_color
  },
  label_style: {
    width: 63
  }
})
