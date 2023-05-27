/**
 * Created by Andste on 2018/11/19.
 */
import React, { PureComponent } from 'react'
import {
  View,
  Text,
  StyleSheet
} from 'react-native'
import { isIphoneX } from 'react-native-iphone-x-helper'
import { colors } from '../../../config'
import { Screen } from '../../utils'

export default function ({ backEle }) {
  return (
    <View style={ styles.container }>
      <View style={ styles.left }>
        { backEle }
      </View>
      <View style={ styles.center }>
        <Text style={ styles.center_title }>购物车</Text>
      </View>
      <View style={ styles.right }>
      
      </View>
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    width: Screen.width,
    height: isIphoneX() ? 84 : 64,
    paddingTop: isIphoneX() ? 40 : 20,
    backgroundColor: colors.navigator_background
  },
  left: {
    width: 88
  },
  center: {
    justifyContent: 'center',
    alignItems: 'center',
    width: Screen.width - 44 * 4
  },
  center_title: {
    color: colors.text,
    fontSize: 17,
    fontWeight: '400'
  },
  right: {
    width: 88
  }
})
