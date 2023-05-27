/**
 * Created by Andste on 2019-01-18.
 * 支付配送相关
 */
import React, { Component } from 'react'
import {
  View,
  Text,
  StatusBar,
  StyleSheet
} from 'react-native'
import Icon from 'react-native-vector-icons/Ionicons'
import { isIphoneX } from 'react-native-iphone-x-helper'
import { Screen } from '../../utils'
import { TextLabel, BigButton } from '../../widgets'

export default class CheckoutPayTimeScene extends Component {
  static navigationOptions = {
    title: '支付配送'
  }
  
  constructor(props) {
    super(props)
    const { params } = this.props.navigation.state
    this.state = {
      payment: params['payment'] || 'ONLINE',
      ship_time: params['ship_time'] || '任意时间'
    }
  }
  
  _onSelect = (item) => {
    this.setState(item)
  }
  
  _onConfirm = () => {
    const { payment, ship_time } = this.state
    const { state, goBack } = this.props.navigation
    state.params.callback({ payment, ship_time })
    goBack()
  }
  
  render() {
    const { payment, ship_time } = this.state
    return (
      <View style={ styles.container }>
        <StatusBar barStyle="dark-content"/>
        {/*支付方式*/ }
        <View style={ styles.item }>
          <View style={ styles.item_header }>
            <Icon name="ios-card" size={ 27 }/>
            <Text style={ styles.item_header_text }>支付方式</Text>
          </View>
          <View style={ styles.item_body }>
            <TextLabel
              text="在线支付"
              selected={ payment === 'ONLINE' }
              onPress={ () => this._onSelect({ payment: 'ONLINE' }) }
            />
            <TextLabel
              text="货到付款"
              selected={ payment === 'COD' }
              onPress={ () => this._onSelect({ payment: 'COD' }) }
            />
          </View>
        </View>
        {/*配送时间*/ }
        <View style={ styles.item }>
          <View style={ styles.item_header }>
            <Icon name="ios-stopwatch-outline" size={ 27 }/>
            <Text style={ styles.item_header_text }>配送时间</Text>
          </View>
          <View style={ styles.item_body }>
            <TextLabel
              text="任意时间"
              selected={ ship_time === '任意时间' }
              onPress={ () => this._onSelect({ ship_time: '任意时间' }) }
            />
            <TextLabel
              text="仅工作日"
              selected={ ship_time === '仅工作日' }
              onPress={ () => this._onSelect({ ship_time: '仅工作日' }) }
            />
            <TextLabel
              text="仅休息日"
              selected={ ship_time === '仅休息日' }
              onPress={ () => this._onSelect({ ship_time: '仅休息日' }) }
            />
          </View>
        </View>
        <BigButton
          title="确认"
          style={ styles.save_btn }
          onPress={ this._onConfirm }
        />
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1
  },
  item: {
    backgroundColor: '#FFF',
    marginBottom: 10,
    paddingHorizontal: 10
  },
  item_header: {
    flexDirection: 'row',
    alignItems: 'center',
    height: 40
  },
  item_header_text: {
    marginLeft: 10,
    fontSize: 16
  },
  item_body: {
    flexDirection: 'row',
    paddingVertical: 10
  },
  save_btn: {
    width: Screen.width,
    position: 'absolute',
    bottom: isIphoneX ? 30 : 0
  }
})
