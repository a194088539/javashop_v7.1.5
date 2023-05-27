/**
 * Created by Paul on 2019-09-19.
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
import { messageActions } from '../../redux/actions'
import { isIphoneX } from 'react-native-iphone-x-helper'
import { Screen, Foundation } from '../../utils'
import {F16Text} from '../../widgets/Text'
import { HeaderBack } from '../../components'
import { BigButton, Cell, Checkbox, Price, TextLabel } from '../../widgets'
import * as API_Trade from '../../apis/trade'
import * as Wechat from 'react-native-wechat'
import Alipay from '@0x5e/react-native-alipay'

class PaySuccessScene extends Component {
  static navigationOptions = ({ navigation }) => {
    return {
      title: '支付成功',
      headerLeft: <HeaderBack onPress={ () => navigation.goBack() }/>,
      gesturesEnabled: false
    }
  }
  
  constructor(props) {
    super(props)

  }
  
  async componentDidMount() {
    // 监听安卓返回按键
    if (Platform.OS === 'android') {
      BackHandler.addEventListener('hardwareBackPress', this._onBackAndroid)
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
  
  render() {
    return (
      <View style={ styles.container }>
        <StatusBar barStyle="dark-content"/>
        <View style={ styles.inner_delivery }>
          <Image style={ styles.inner_delivery_img } source={ require('../../images/icon-inner-delivery.png')}></Image>
          <View style={ styles.content_delivery }>
            <F16Text>支付完成</F16Text>
          </View>
        </View>
        <View style={ styles.btns_delivery } >
          <TextLabel style={ styles.btnsItem } text="查看订单" onPress={ () => this.props.navigation.replace('MyOrder') } />
          <TextLabel style={ styles.btnsItem } text="返回首页" onPress={ () => this.props.navigation.navigate('Home') } />
        </View>
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
    height: 20,
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

export default connect()(PaySuccessScene)
