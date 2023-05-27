/**
 * Created by Andste on 2019-01-24.
 */
import React, { Component } from 'react'
import {
  Alert,
  View,
  Image,
  BackHandler,
  Platform,
  StatusBar,
  ScrollView,
  StyleSheet
} from 'react-native'
import { connect } from 'react-redux'
import { messageActions } from './../../redux/actions'
import { isIphoneX } from 'react-native-iphone-x-helper'
import { Screen, Foundation } from '../../utils'
import {F16Text} from '../../widgets/Text'
import { HeaderBack } from '../../components'
import { BigButton, Cell, Checkbox, Price, TextLabel } from '../../widgets'
import * as API_Trade from '../../apis/trade'
import * as Wechat from 'react-native-wechat'
import Alipay from '@0x5e/react-native-alipay'

// 支付宝图标
const alipay_icon = require('../../images/icon-alipay.png')
// 微信支付图标
const wechat_icon = require('../../images/icon-wechat-pay.png')

const paymentIcons = {
  'alipayDirectPlugin': alipay_icon,
  'weixinPayPlugin': wechat_icon
}

class CashierScene extends Component {
  static navigationOptions = ({ navigation }) => {
    return {
      title: '订单创建成功',
      headerLeft: <HeaderBack onPress={ () => navigation.goBack() }/>,
      gesturesEnabled: false
    }
  }
  
  constructor(props) {
    super(props)
    const { trade_sn, order_sn, fromScene } = props.navigation.state.params
    this.trade_sn = trade_sn
    this.order_sn = order_sn || null
    this.state = {
      fromScene,
      // 支付方式列表
      payment_list: [],
      // 订单详情
      order: {},
      // 支付方式id
      payment_plugin_id: null,
      // 显示确认订单支付完成模态框
      showConfirmModal: false
    }
  }
  
  componentDidMount() {
    // 监听安卓返回按键
    if (Platform.OS === 'android') {
      BackHandler.addEventListener('hardwareBackPress', this._onBackAndroid)
    }
    this._getCashierDate()
  }

  _getCashierDate = async () => {
    const { trade_sn, order_sn } = this
    const params = trade_sn ? { trade_sn } : { order_sn }
    const values = await Promise.all([
      API_Trade.getCashierData(params),
      API_Trade.getPaymentList('REACT')
    ])
    this.setState({
      order: values[0],
      payment_list: values[1]
    })
  }
  
  // 当组件即将卸载时，移除监听
  componentWillUnmount() {
    if (Platform.OS === 'android') {
      BackHandler.removeEventListener('hardwareBackPress', this._onBackAndroid)
    }
  }
  
  /**
   * 监听安卓返回的方法
   * @returns {boolean}
   * @private
   */
  _onBackAndroid = () => {
    return this.props.navigation.goBack()
  }
  
  /**
   * 选择支付方式
   * @param item
   * @private
   */
  _onSelectPayment = (item) => {
    this.setState({ payment_plugin_id: item['plugin_id'] })
  }
  
  /**
   * 发起APP支付
   * @private
   */
  _onInitiatePay = async () => {
    const { trade_sn, order_sn } = this
    const { dispatch } = this.props
    const { payment_plugin_id } = this.state
    if (payment_plugin_id === null) {
      dispatch(messageActions.error('请选择一个支付方式！'))
      return
    }
    // 支付模式
    const pay_mode = 'normal'
    // 客户端类型
    const client_type = 'REACT'
    // 支付编号
    const sn = trade_sn || order_sn
    // 交易类型【交易号|订单号】
    const trade_type = trade_sn ? 'trade' : 'order'
    
    // 如果是微信支付，先检查是否安装微信
    if (payment_plugin_id === 'weixinPayPlugin') {
      const isWXAppInstalled = await Wechat.isWXAppInstalled()
      if (!isWXAppInstalled) {
        dispatch(messageActions.error('您的设备没有安装微信！'))
        return
      }
    }
    
    // 初始化支付签名
    const signXml = await API_Trade.initiateAppPay(trade_type, sn, {
      payment_plugin_id,
      pay_mode,
      client_type
    })
    switch (payment_plugin_id) {
      case 'alipayDirectPlugin':
        this._onAliPay(signXml)
        break
      case 'weixinPayPlugin':
        this._onWechatPay(signXml)
        break
    }
  }
  
  /**
   * 支付宝支付
   * @param signXml
   * @returns {Promise<void>}
   * @private
   */
  _onAliPay = async (signXml) => {
    const { dispatch } = this.props
    try {
      Alipay.pay(signXml)
      Alert.alert('提示', '支付成功了吗？切记不能重复支付！', [
        { text: '重新支付' },
        { text: '支付成功', onPress: this._paySuccess }
      ])
    } catch (e) {
      let errMsg = '支付失败'
      switch (e.code) {
        case '6001':
          errMsg = '用户中途取消'
          break
        case '6002':
          errMsg = '网络连接出错'
          break
        case '5000':
          errMsg = '重复支付'
          break
        case '8000':
          errMsg = '正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态'
      }
      dispatch(messageActions.error(errMsg))
    }
  }
  
  /**
   * 微信支付
   * @param signXml
   * @private
   */
  _onWechatPay = async (signXml) => {
    const { dispatch } = this.props
    const signXML = String(signXml)
    // 商家向财付通申请的商家ID
    const partnerId = String(signXML.match(/<partnerid>(.+)<\/partnerid>/)[1])
    // 预支付订单ID
    const prepayId = String(signXML.match(/<prepayid>(.+)<\/prepayid>/)[1])
    // 随机串
    const nonceStr = String(signXML.match(/<noncestr>(.+)<\/noncestr>/)[1])
    // 时间戳
    const timeStamp = String(signXML.match(/<timestamp>(.+)<\/timestamp>/)[1])
    // 商家根据微信开放平台文档对数据做的签名
    const sign = String(signXML.match(/<sign>(.+)<\/sign>/)[1])
    try {
      Wechat.pay({ partnerId, prepayId, nonceStr, timeStamp, package: 'Sign=WXPay', sign })
      Alert.alert('提示', '支付成功了吗？切记不能重复支付！', [
        { text: '重新支付' },
        { text: '支付成功', onPress: this._paySuccess }
      ])
    } catch (e) {
      let errMsg = '支付失败'
      if (e.code === -2) errMsg = '用户中途取消'
      dispatch(messageActions.error(errMsg))
    }
  }
  
  /**
   * 支付成功回调
   * @private
   */
  _paySuccess = () => {
    const { navigation } = this.props
    const { callback } = navigation.state.params || {}
    const { fromScene } = this.state
    typeof callback === 'function' && callback()
    if (fromScene === 'Checkout') {
      this.props.dispatch(messageActions.success('支付成功'))
      navigation.replace('PaySuccess')
    } else {
      navigation.goBack()
    }
  }
  
  render() {
    const { order, payment_list, payment_plugin_id } = this.state
    const { need_pay_price = 0 } = order
    if(!order.pay_type_text) return <View/>
    return (
      <View style={ styles.container }>
        <StatusBar barStyle="dark-content"/>
        <View style={ styles.inner_delivery }>
          <Image style={ styles.inner_delivery_img } source={ require('../../images/icon-inner-delivery.png')}></Image>
          <View style={ styles.content_delivery }>
            <F16Text>支付方式：{ order.pay_type_text === 'ONLINE' ? '在线支付' : '货到付款' }</F16Text>
            <F16Text>订单金额：￥{ order.need_pay_price }</F16Text>
          </View>
        </View>
        <View style={ styles.btns_delivery } >
          <TextLabel style={ styles.btnsItem } text="查看订单" onPress={ () => this.props.navigation.replace('MyOrder') } />
          <TextLabel style={ styles.btnsItem } text="返回首页" onPress={ () => this.props.navigation.navigate('Home') } />
        </View>
        { order.pay_type_text === 'ONLINE' && need_pay_price !== 0 &&
          <View>
            <View style={ styles.need_pay }>
            <Cell
              title="需支付"
              disabled
              arrow={ false }
              label={ <Price price={ order['need_pay_price'] }/> }
            />
            </View>
            <ScrollView>
            { payment_list.map(item => {
              return (
                <Cell
                  line="bottom"
                  key={ item['plugin_id'] }
                  title={ item['method_name'] }
                  icon={ <Image style={ styles.payment_icon } source={ paymentIcons[item['plugin_id']] }/> }
                  onPress={ () => this._onSelectPayment(item) }
                  arrow={ (
                    <Checkbox
                      style={ styles.checkbox }
                      checked={ item['plugin_id'] === payment_plugin_id }
                      onPress={ () => this._onSelectPayment(item) }
                    />
                  ) }
                />
              )
            }) }
            </ScrollView>
          </View>
        }

        { order.pay_type_text === 'ONLINE' && need_pay_price !== 0 && 
          <BigButton
            style={ styles.pay_btn }
            title={ `去支付${ Foundation.formatPrice(need_pay_price) }元` }
            onPress={ this._onInitiatePay }
          />
        }
        
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1
  },
  need_pay: {},
  checkbox: {
    paddingRight: 0
  },
  inner_delivery: {
    alignItems: 'center',
    justifyContent:'center',
    alignSelf: 'center',
    flexWrap: 'wrap',
    width: 170,
    marginTop: 35,
    marginBottom: 30,
    height: 50
  },
  inner_delivery_img: {
    width: 50,
    height: 50
  },
  content_delivery: {
    height: 50,
  },
  btns_delivery: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent:'center',
  },
  btnsItem: {
    height: 25,
    lineHeight: 20,
    backgroundColor: '#fff',
    color: '#333',
    fontSize: 12,
    textAlign: 'center',
    width: 150,
    borderRadius: 5
  },
  payment_icon: {
    width: 27,
    height: 27,
    marginRight: 5
  },
  pay_btn: {
    position: 'absolute',
    bottom: 0,
    width: Screen.width,
    marginBottom: isIphoneX() ? 30 : 0
  }
})

export default connect()(CashierScene)
