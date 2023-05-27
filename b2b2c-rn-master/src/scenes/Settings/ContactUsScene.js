/**
 * Created by Andste on 2017/8/30.
 */

import React, { Component } from 'react'
import {
  View,
  Text,
  Linking,
  StatusBar,
  StyleSheet
} from 'react-native'
import { Cell, CellGroup } from '../../widgets'

export default class ContactUsScene extends Component {
  static navigationOptions = {
    title: '联系我们'
  }
  
  _onPhonePress = () => {
    Linking.openURL('tel:4007788098').catch(() => {})
  }
  
  _onEmailPress = () => {
    Linking.openURL('mailto:service@javamall.com.cn').catch(() => {})
  }
  
  render() {
    return (
      <View style={ styles.container }>
        <StatusBar barStyle="dark-content"/>
        <CellGroup>
          <Cell
            title="咨询电话"
            label={ <Text style={ styles.label }>400-7788-098</Text> }
            onPress={ this._onPhonePress }
          />
          <Cell
            title="电子邮箱"
            label={ <Text style={ styles.label }>service@javamall.com.cn</Text> }
            onPress={ this._onEmailPress }
          />
        </CellGroup>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1
  },
  label: {
    fontSize: 18,
    color: '#0300CF'
  }
})
