/**
 * Created by andste.cc@gmail.com on 2018/9/29.
 */
import React from 'react'
import { Platform } from 'react-native'
import { createBottomTabNavigator } from 'react-navigation'
import Icon from 'react-native-vector-icons/Ionicons'
import { colors } from '../../config'

import Home from '../scenes/Home'
import Classify from '../scenes/Classify'
import Cart from '../scenes/Cart'
import Mine from '../scenes/Mine'

const screens = {
  Home,
  Classify,
  Cart,
  Mine
}

const options = {
  initialRouteName: 'Home',
  swipeEnabled: false,
  tabBarPosition: 'bottom',
  tabBarOptions: {
    showIcon: true,
    inactiveTintColor: colors.tab_icon_inactive,
    activeTintColor: colors.tab_icon_active,
    labelStyle: {
      fontSize: 12,
      textAlign: 'center'
    },
    indicatorStyle: {
      height: 0
    },
    style: {
      backgroundColor: colors.tab_background,
      shadowOffset: { width: 0, height: 0 },
      shadowColor: colors.tab_shadow,
      shadowOpacity: .85,
      shadowRadius: 3,
      ...Platform.select({
        android: {
          height: 60
        }
      })
    }
  }
}

export default createBottomTabNavigator(screens, options)
