/**
 * Created by Andste on 2018/10/25.
 */
import React, { Component } from 'react'
import {
  View,
  Platform,
  Text,
  Image,
  Linking,
  StyleSheet
} from 'react-native'
import { isIphoneX } from 'react-native-iphone-x-helper'
import VersionNumber from 'react-native-version-number'
import { appId } from '../../../config'
import { Screen } from '../../utils'
import { Cell, CellGroup } from '../../widgets'

const isIos = Platform.OS === 'ios'

export default class AboutAppScene extends Component {
  static navigationOptions = {
    title: '关于'
  }
  
  _toStore = () => {
    Linking.canOpenURL(`https://itunes.apple.com/WebObjects/MZStore.woa/wa/viewContentsUserReviews?id=${ appId.appStoreId }&pageNumber=0&sortOrdering=2&type=Purple+Software&mt=8`)
  }
  
  render() {
    let _p_str = isIos ? 'iPhone' : 'Android'
    return (
      <View style={ styles.container }>
        <View style={ styles.app_logo_view }>
          <Image style={ styles.app_logo_image } source={ require('../../images/icon-app-logo.png') }/>
          <Text style={ styles.copyright_text }>{ `For ${ _p_str } v${ VersionNumber.appVersion }` }</Text>
        </View>
        <CellGroup>
          { isIos ? <Cell title="评分支持" onPress={ this._toStore } label="打个分吧"/> : undefined }
          <Cell title="服务条款" label="暂无条款"/>
          <Cell title="免责声明" label="暂无声明"/>
        </CellGroup>
        <View style={ styles.copyright_view }>
          <Text style={ styles.copyright_text }>Copyright© 2007-2018 javamall,Inc. All rights reserved</Text>
          <Text style={ styles.copyright_text }>易族智汇（北京）科技有限公司</Text>
        </View>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1
  },
  app_logo_view: {
    alignItems: 'center',
    width: Screen.width,
    height: 220,
    paddingVertical: 30
  },
  app_logo_image: {
    width: 80,
    height: 80,
    borderRadius: 15,
    marginBottom: 10
  },
  copyright_view: {
    position: 'absolute',
    bottom: isIphoneX() ? 40 : 20,
    left: 0,
    width: Screen.width,
    alignItems: 'center'
  },
  copyright_text: {
    fontSize: 12,
    color: '#777777',
    paddingBottom: 5
  }
})
