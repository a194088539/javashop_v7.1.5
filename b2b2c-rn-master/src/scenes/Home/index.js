/**
 * Created by Andste on 2018/10/1.
 */
import React, { Component } from 'react'
import {
  View,
  Animated,
  Image,
  NetInfo,
  TouchableOpacity,
  StatusBar,
  DeviceEventEmitter,
  YellowBox,
  Platform,
  StyleSheet
} from 'react-native'
import { StackActions, NavigationActions } from 'react-navigation'
import * as API_Home from '../../apis/home'
import { Loading } from '../../widgets'
import { BaseText, F16Text } from '../../widgets/Text'
import Icon from 'react-native-vector-icons/Ionicons'
import SplashScreen from 'react-native-splash-screen'

import SearchHeader from '../../components/SearchHeader'
import { PullView } from '../../components/PullView'
import Focus from './Focus'
import Menus from './Menus'
import Seckill from './Seckill'
import Floor from './Floor'

YellowBox.ignoreWarnings(['Require cycle:'])

export default class Home extends Component {
  static navigationOptions = {
    tabBarLabel: '首页',
    tabBarIcon: ({ focused, tintColor }) => (
      <Icon name={ focused ? 'ios-home' : 'ios-home-outline' } size={ 30 } color={ tintColor }/>
    )
  }
  
  constructor(props) {
    super(props)
    this.searchHeaderOpacity = new Animated.Value(0)
    this.state = {
      loading: true,
      refreshing: false,
      focus: null,
      menus: null,
      floor: null,
      isConnected: true
    }
    this.searchHeaderOpacity.addListener((event) => {
      this.setState({ offset: event.value })
    })
  }
  
  async componentDidMount() {
    if (Platform.OS === 'ios') {
      SplashScreen.hide();
    }
    const isConnected = await NetInfo.isConnected.fetch()
    await this.setState({ isConnected })
    if (isConnected) {
      await this._getHomeData()
    }
    this.listener = DeviceEventEmitter.addListener('ReloadHome',(y)=>{
      this._getHomeData()
    })
  }
  
  componentWillUnmount(){
    this.listener.remove()
  }

  /**
   * 获取首页数据
   * @returns {Promise<void>}
   * @private
   */
  _getHomeData = async () => {
    this.setState({loading: true})
    try {
      const homeDatas = await Promise.all([
        API_Home.getFocusPictures('WAP'),
        API_Home.getSiteMenu('MOBILE'),
        API_Home.getFloorData('WAP')
      ])
      const { page_data } = homeDatas[2]
      this.setState({
        loading: false,
        focus: homeDatas[0],
        menus: homeDatas[1],
        floor: page_data ? JSON.parse(page_data) : []
      })
    } catch (e) {
      this.setState({ loading: false })
    }
  }

  _onRefresh = async (resolve) => {
    try { 
      const homeDatas = await Promise.all([
        API_Home.getFocusPictures('WAP'),
        API_Home.getSiteMenu('MOBILE'),
        API_Home.getFloorData('WAP')
      ])
      const { page_data } = homeDatas[2]
      this.setState({
        focus: homeDatas[0],
        menus: homeDatas[1],
        floor: page_data ? JSON.parse(page_data) : []
      })
    }
    catch (e){
      this._getHomeData()
    }
    resolve()
  }
  
  /**
   * 重置Navigation
   * @private
   */
  _resetNavigation = () => {
    const resetAction = StackActions.reset({
      index: 0,
      actions: [NavigationActions.navigate({ routeName: 'Root' })]
    })
    this.props.navigation.dispatch(resetAction)
  }
  
  render() {
    let interpolatedColor = this.searchHeaderOpacity.interpolate({
      inputRange: [0, 100],
      outputRange: [0, 1],
      extrapolate: 'clamp'
    })
    const { focus, menus, floor, loading, isConnected } = this.state
    const { navigation } = this.props
    if (!isConnected) return (
      <TouchableOpacity
        style={ styles.network_is_offline }
        onPress={ this._resetNavigation }>
        <Image
          source={ require('../../images/icon-network-disconnected.png') }
          style={ styles.network_is_offline_img }
        />
        <BaseText>网络未连接</BaseText>
        <F16Text style={ { color: '#777777', marginTop: 5 } }>请检查网络后点击页面刷新</F16Text>
      </TouchableOpacity>
    )
    if (loading) return (
      <View style={ styles.container }>
        <StatusBar barStyle="light-content"/>
        <Loading show={ true }/>
      </View>
    )
    return (
      <View style={ styles.container }>
        <StatusBar barStyle="light-content"/>
        <SearchHeader
          opacity={ interpolatedColor }
          offset={ this.state.offset }
        />
        <PullView onPullRelease={this._onRefresh} refreshing={this.state.loading} scrollY={this.searchHeaderOpacity} >
          <Focus data={ focus }/>
          <Menus data={ menus }/>
          <Seckill/>
          <Floor nav={ navigation } data={ floor }/>
        </PullView>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#FFFFFF'
  },
  network_is_offline: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center'
  },
  network_is_offline_img: {
    width: 100,
    height: 100
  }
})
