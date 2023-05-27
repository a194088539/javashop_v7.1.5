/**
 * Created by Andste on 2019-01-30.
 */
import React, { PureComponent } from 'react'
import {
  Alert,
  View,
  Image,
  Text,
  TouchableOpacity,
  StyleSheet
} from 'react-native'
import { connect } from 'react-redux'
import { messageActions, userActions } from '../../redux/actions'
import { navigate } from '../../navigator/NavigationService'
import { appId } from '../../../config'
import { Screen } from '../../utils'
import * as API_Connect from '../../apis/connect'
import * as Wechat from 'react-native-wechat'
import * as QQAPI from 'react-native-qq'
import * as Weibo from 'react-native-weibo'
import Alipay from '@0x5e/react-native-alipay'

class LoginByConnect extends PureComponent {
  
  /**
   * 获取微信授权
   * @returns {Promise<void>}
   * @private
   */
  _getWechatAuth = async () => {
    const { dispatch } = this.props
    const isWXAppInstalled = await Wechat.isWXAppInstalled()
    if (!isWXAppInstalled) {
      dispatch(messageActions.error('您的设备没有安装微信！'))
      return
    }
    try {
      const res = await Wechat.sendAuthRequest('snsapi_userinfo')
      await this._getOpenidBinded(res.code, 'WECHAT')
    } catch (e) {
      dispatch(messageActions.error('获取微信授权失败，请稍后再试！'))
    }
  }
  
  /**
   * 获取QQ授权
   * @private
   */
  _getQQAuth = async () => {
    const { dispatch } = this.props
    if (!QQAPI.isQQInstalled) {
      dispatch(messageActions.error('您的设备没有QQ！'))
      return
    }
    try {
      const res = await QQAPI.login('get_simple_userinfo')
      await this._getOpenidBinded(res.openid, 'QQ')
    } catch (e) {
      dispatch(messageActions.error('获取QQ授权失败，请稍后再试！'))
    }
  }
  
  /**
   * 获取微博授权
   * @returns {Promise<void>}
   * @private
   */
  _getWeiboAuth = async () => {
    const { dispatch } = this.props
    try {
      const res = await Weibo.login({
        scope: 'all',
        redirectURI: appId.weiboRedirectURI
      })
      await this._getOpenidBinded(res['userID'], 'WEIBO')
    } catch (e) {
      let errMsg = '获取微博授权失败'
      if (e['message'] === '用户取消发送') errMsg = '用户取消授权'
      dispatch(messageActions.error(errMsg))
    }
  }
  
  /**
   * 获取支付宝授权
   * @returns {Promise<void>}
   * @private
   */
  _getAlipayAuth = async () => {
    const { dispatch } = this.props
    const infoObj = {
      apiname: 'com.alipay.account.auth',
      method: 'alipay.open.auth.sdk.code.get',
      app_name: 'mc',
      biz_type: 'openservice',
      product_id: 'APP_FAST_LOGIN',
      scope: 'kuaijie',
      target_id: (new Date).getTime().toString(),
      auth_type: 'AUTHACCOUNT',
      sign_type: 'RSA2',
      ...appId.alipayConfig
    }
    let infoArray = []
    for (let key in infoObj) {
      infoArray.push(`${key}=${infoObj[key]}`)
    }
    infoObj.sign = infoArray.join('&')
    try {
      const response = await Alipay['authWithInfo'](infoObj.sign)
      if (String(response['resultStatus']) === '9000') {
        const userId = response.result.match(/user_id=(\d+)/)[1]
        await this._getOpenidBinded(userId, 'ALIPAY')
      } else if (String(response[''] === '6001')) {
        dispatch(messageActions.error('用户取消授权'))
      } else {
        throw Error(response['memo'])
      }
    } catch (e) {
      dispatch(messageActions.error('获取支付宝授权失败，请稍后再试！'))
    }
  }
  
  /**
   * 获取openid是否已绑定账号
   * @param code
   * @param type
   * @returns {Promise<void>}
   * @private
   */
  _getOpenidBinded = async (code, type) => {
    const { dispatch } = this.props
    try {
      const res = await API_Connect.getOpenidBinded(code, type)
      if (res['is_bind']) {
        await dispatch(userActions.loginSuccessAction(res))
        dispatch(userActions.getUserAction(this.props.nav.goBack))
        dispatch(messageActions.success('登录成功！'))
      } else {
        const params = { openid: code, type }
        Alert.alert('提示', '您暂未绑定任何账户，可以选择登录或注册', [
          { text: '登录', onPress: () => this.props['loginByConnect'](params) },
          { text: '注册', onPress: () => navigate('Register', params) }
        ])
      }
    } catch (e) {
      dispatch(messageActions.error('获取绑定状态失败！'))
    }
  }
  
  /**
   * 点击第三方登录图标
   * @param type
   * @private
   */
  _onPress = (type) => {
    switch (type) {
      case 'wechat':
        this._getWechatAuth()
        break
      case 'qq':
        this._getQQAuth()
        break
      case 'weibo':
        this._getWeiboAuth()
        break
      case 'alipay':
        this._getAlipayAuth()
        break
    }
  }
  
  render() {
    return (
      <View style={ styles.container }>
        <View style={ styles.title }>
          <View style={ styles.title_line }/>
          <Text style={ styles.title_text }>其它方式登录</Text>
          <View style={ styles.title_line }/>
        </View>
        <View style={ styles.icons }>
          <ConnectIcon
            icon={ require('../../images/icon-qq-logo.png') }
            onPress={ () => { this._onPress('qq') } }
          />
          <ConnectIcon
            icon={ require('../../images/icon-wechat-logo.png') }
            onPress={ () => { this._onPress('wechat') } }
          />
          <ConnectIcon
            icon={ require('../../images/icon-weibo-logo.png') }
            onPress={ () => { this._onPress('weibo') } }
          />
          <ConnectIcon
            icon={ require('../../images/icon-alipay-logo.png') }
            onPress={ () => { this._onPress('alipay') } }
          />
        </View>
      </View>
    )
  }
}

const ConnectIcon = ({ icon, ...props }) => {
  return (
    <TouchableOpacity { ...props } >
      <Image style={ styles.icon } source={ icon }/>
    </TouchableOpacity>
  )
}

const styles = StyleSheet.create({
  container: {
    marginTop: 100,
    paddingHorizontal: 20
  },
  title: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center'
  },
  title_line: {
    width: 85,
    height: Screen.onePixel,
    backgroundColor: '#CCC'
  },
  title_text: {
    color: '#8c8c8c',
    fontSize: 16
  },
  icons: {
    paddingHorizontal: 20,
    marginTop: 20,
    flexDirection: 'row',
    justifyContent: 'space-around',
    alignItems: 'center'
  },
  icon: {
    width: 45,
    height: 45
  }
})

export default connect()(LoginByConnect)
