/**
 * Created by Andste on 2018/10/15.
 */
import React, { PureComponent } from 'react'
import {
  View,
  Animated,
  Image,
  Text,
  TouchableOpacity,
  StyleSheet
} from 'react-native'
import Icon from 'react-native-vector-icons/Ionicons'
import { getStatusBarHeight } from 'react-native-iphone-x-helper'
import { navigate } from '../../navigator/NavigationService'
import { colors } from '../../../config'
import { Screen } from '../../utils'

export default class Header extends PureComponent {
  _toSetting = () => {
    navigate('Setting')
  }
  
  render() {
    const { face, opacity, headerStyle, showInfoOpacity } = this.props
    return (
      <View style={ styles.container }>
        <Animated.View style={ [styles.place_bg, { opacity }] }/>
        <View style={ styles.content }>
          <Animated.View style={ [styles.icon, styles.left, { opacity: showInfoOpacity }] }>
            { face ? (
              <TouchableOpacity style={ styles.face_view } onPress={ this._toSetting }>
                <Image style={ styles.face } source={ { uri: face } }/>
              </TouchableOpacity>
            ) : undefined }
          </Animated.View>
          <Animated.View style={ [styles.center, { opacity: showInfoOpacity }] }>
            <Text style={ styles.title }>我的</Text>
          </Animated.View>
          <View style={ [styles.icon, styles.right] }>
            <TouchableOpacity onPress={ this._toSetting }>
              <Icon color={ headerStyle === 'dark' ? colors.text : '#FFFFFF' } name="ios-settings-outline" size={ 25 }/>
            </TouchableOpacity>
          </View>
        </View>
      </View>
    )
  }
}

const headerHeight = 44
const styles = StyleSheet.create({
  container: {
    alignItems: 'center',
    justifyContent: 'center',
    position: 'absolute',
    zIndex: 99,
    width: Screen.width,
    height: headerHeight + getStatusBarHeight(true),
    backgroundColor: colors.transparent
  },
  place_bg: {
    position: 'absolute',
    zIndex: -99,
    width: Screen.width,
    height: headerHeight + getStatusBarHeight(true),
    backgroundColor: '#FFFFFF',
    borderColor: colors.cell_line_backgroud,
    borderBottomWidth: Screen.onePixel
  },
  content: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    width: Screen.width - 20,
    height: headerHeight,
    paddingTop: getStatusBarHeight(true)
  },
  icon: {
    width: headerHeight * 2,
    height: headerHeight
  },
  face_view: {
    width: 22,
    height: 22,
    borderRadius: 22,
    overflow: 'hidden'
  },
  face: {
    justifyContent: 'center',
    alignItems: 'center',
    width: '100%',
    height: '100%'
  },
  title: {
    fontSize: 17,
    color: colors.text
  },
  left: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'flex-start'
  },
  center: {
    justifyContent: 'center',
    alignItems: 'center',
    width: Screen.width - 20 - 88 * 2,
    height: headerHeight
  },
  right: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'flex-end'
  }
})
