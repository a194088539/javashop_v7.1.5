/**
 * Created by Andste on 2017/4/22.
 * 首页搜索栏
 */
import React, { PureComponent } from 'react'
import {
  Animated,
  Easing,
  DeviceEventEmitter,
  StatusBar,
  Platform,
  View,
  Text,
  TouchableOpacity,
  StyleSheet
} from 'react-native'
import { isIphoneX } from 'react-native-iphone-x-helper'
import { navigate } from '../navigator/NavigationService'
import { Screen } from '../utils'
import SearchBar from '../components/SearchBar'
import QRcodeScannerView from '../components/QRcodeScannerView'
import Icon from 'react-native-vector-icons/Ionicons'
import { colors } from '../../config'

export default class SearchHeader extends PureComponent {
  constructor(props) {
    super(props)
    this.state = {
      hidden: false,
      theme: 'light',
      pullPan: new Animated.Value(styles.root.height),
      showQrcode: false
    }
    this.listener = DeviceEventEmitter.addListener('moveDownReleaseHomeHeader',(y)=>{
      this.state.pullPan.setValue(styles.root.height + y)
    })
    this.listenerAnimated = DeviceEventEmitter.addListener('moveDownHomeHeader',(y)=>{
      Animated.timing(this.state.pullPan, {
        toValue: y,
        easing: Easing.linear,
        duration: 300
      }).start()
    })
  }

  componentWillUnmount(){
    this.listener.remove()
    this.listenerAnimated.remove()
  }
  
  componentWillReceiveProps(nextProps) {
    let {simple, offset} = nextProps
    if (simple) {
      this.setState({hidden: false})
      return
    }
    offset > 50
      ? this.setState({
        hidden: false,
        theme: 'drak'
      }, () => StatusBar.setBarStyle('dark-content'))
      : this.setState({
        hidden: offset < -1,
        theme: 'light'
      }, () => StatusBar.setBarStyle('light-content'))
  };
  
  _onPressSearch = () => navigate('Search')
  _toMine = () => navigate('Mine')
  _showQrcode = () => this.setState({showQrcode: true})
  
  render() {
    let simple = this.props['simple']
    let is_light = simple ? false : (this.state.theme === 'light'),
      theme_color = is_light ? '#FFFFFF' : '#333333'
    return (
      <Animated.View
        style={ [
          styles.root,{ height: this.state.pullPan},
          {opacity: this.state.hidden ? 0 : 1, position: 'absolute'},
          this.props.style
        ] }>
          <Animated.View
            style={ [styles.headerBg, {opacity: this.props.opacity}] }>
          </Animated.View>
          <TouchableOpacity onPress={ this._showQrcode } style={ styles.touchable }>
            <Icon name="md-qr-scanner" style={ [styles.icon, {color: theme_color}] }/>
            <Text style={ [styles.icon_text, {color: theme_color}] }>扫一扫</Text>
          </TouchableOpacity>
          <SearchBar is_light={ is_light } onPress={ this._onPressSearch }/>
          <TouchableOpacity style={ styles.touchable } onPress={ this._toMine }>
            <Icon name="ios-person-outline" style={ [styles.icon, {color: theme_color}] }/>
            <Text style={ [styles.icon_text, {color: theme_color}] }>我的</Text>
          </TouchableOpacity>
          <QRcodeScannerView
            show={ this.state.showQrcode }
            onClosed={ () => this.setState({showQrcode: false}) }
          />
      </Animated.View>
    )
  }
}

const styles = StyleSheet.create({
  root: {
    width: Screen.width,
    justifyContent: 'space-between',
    alignItems: 'center',
    flexDirection: 'row',
    ...Platform.select({
      ios: {
        height: isIphoneX() ? 90 : 70,
        paddingTop: isIphoneX() ? 34 : 14
      },
      android: {
        height: 56 + StatusBar.currentHeight,
        paddingTop: StatusBar.currentHeight
      }
    }),
    backgroundColor: colors.transparent
  },
  headerBg: {
    width: Screen.width,
    position: 'absolute',
    ...Platform.select({
      ios: {
        height: isIphoneX() ? 90 : 70
      },
      android: {
        height: 56 + StatusBar.currentHeight
      }
    }),
    shadowOffset: {width: 0, height: 0},
    shadowColor: '#CCCCCC',
    shadowOpacity: .85,
    shadowRadius: 3,
    backgroundColor: '#FFFFFF'
  },
  touchable: {
    width: 40,
    height: 40,
    alignItems: 'center'
  },
  icon: {
    fontSize: 25,
    color: '#FFFFFF'
  },
  icon_text: {
    fontSize: 10,
    color: '#FFFFFF',
    alignContent: 'center'
  },
  back: {
    paddingLeft: 5
  },
  title: {
    fontSize: 18,
    color: '#FFFFFF'
  },
  right: {
    paddingRight: 5
  }
})
