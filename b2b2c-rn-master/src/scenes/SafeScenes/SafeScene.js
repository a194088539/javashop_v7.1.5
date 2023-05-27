/**
 * Created by Andste on 2018/11/13.
 */
import React, { Component } from 'react'
import {
  View,
  StatusBar,
  StyleSheet
} from 'react-native'
import { Cell, CellGroup } from '../../widgets'
import { navigate } from '../../navigator/NavigationService'

export default class SafeScene extends Component {
  static navigationOptions = {
    title: '账户安全'
  }
  
  render() {
    return (
      <View styles={ styles.container }>
        <StatusBar barStyle="dark-content"/>
        <CellGroup>
          <Cell title="修改密码" onPress={ () => {navigate('CheckMobile', { type: 'password' })} }/>
          <Cell title="更换手机" onPress={ () => {navigate('CheckMobile', { type: 'mobile' })} }/>
        </CellGroup>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1
  }
})
