/**
 * Created by Andste 2018/9/29.
 */
import React, { Component } from 'react'
import {
  View,
  StatusBar,
  Platform
} from 'react-native'
import { connect } from 'react-redux'
import { userActions } from '../redux/actions'
import { createAppContainer, createStackNavigator } from 'react-navigation'
import { StackViewStyleInterpolator } from 'react-navigation-stack'
import { setTopLevelNavigator } from './NavigationService'
import Toast from 'react-native-root-toast'
import uuidv1 from 'uuid/v1'
import { colors, appId } from '../../config'
import * as Wechat from 'react-native-wechat'

import TabNavigator from './TabNavigator'
import OtherNavigator from './OtherNavigator'
import { HeaderBack } from '../components'

const AppStackNavigator = createAppContainer(createStackNavigator({
  Root: { screen: TabNavigator, navigationOptions: { header: null } },
  ...OtherNavigator
}, {
  defaultNavigationOptions: {
    headerMode: 'none',
    headerLeft: <HeaderBack/>,
    headerRight: <View/>,
    headerBackTitle: null,
    headerTitleStyle: {
      fontSize: 17,
      fontWeight: '400',
      color: colors.text,
      textAlign: 'center',
      flex: 1
    },
    headerTintColor: colors.navigator_tint_color,
    headerStyle: {
      backgroundColor: colors.navigator_background,
      ...Platform.select({
        android: {
          paddingTop: StatusBar.currentHeight,
          height: 56 + StatusBar.currentHeight
        }
      })
    }
  },
  headerMode: 'screen',
  transitionConfig: () => ({
    screenInterpolator: (sceneProps) => {
      const { params = {}, routeName } = sceneProps.scene.route
      let transition = params.transition || 'forHorizontal'
      if (routeName === 'Login' || routeName === 'Search') transition = 'forVertical'
      return StackViewStyleInterpolator[transition](sceneProps)
    }
  }),
  cardStyle: {
    backgroundColor: colors.gray_background
  }
}))

class AppNavigator extends Component {
  constructor(props) {
    super(props)
    this.state = {}
    // 初始化uuid
    if (!this.props.uuid) this.props.dispatch(userActions.setUUIDAction(uuidv1()))
    // 注册微信APPid
    Wechat.registerApp(appId.wechatAppId)
  }
  
  static getDerivedStateFromProps(nextProps) {
    const { type, message } = nextProps
    if (message === null || message === undefined) return null
    if (this.toast) Toast.hide(this.toast)
    this.toast = Toast.show(message, {
      type,
      shadow: false,
      position: Toast.positions.CENTER
    })
    return null
  }
  
  shouldComponentUpdate(nextProps) {
    return nextProps.message !== this.props.message
  }
  
  render() {
    return <AppStackNavigator ref={ setTopLevelNavigator }/>
  }
}

const select = state => {
  return {
    ...state.message,
    uuid: state.user.uuid
  }
}
export default connect(select)(AppNavigator)
