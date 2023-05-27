/**
 * Created by Andste on 2017/9/14.
 * 二维码页面
 */

import React, { PureComponent } from 'react'
import {
  View,
  Modal,
  TouchableOpacity,
  StatusBar,
  StyleSheet
} from 'react-native'
import { navigate } from '../navigator/NavigationService'
import { getStatusBarHeight } from 'react-native-iphone-x-helper'
import QRCodeScanner from 'react-native-qrcode-scanner'
import { colors } from '../../config'
import { Screen } from '../utils'
import { TitleText } from '../widgets/Text'
import Icon from 'react-native-vector-icons/Ionicons'

export default class QRcodeScannerView extends PureComponent {
  constructor(props) {
    super(props)
    this.state = {visible: false}
  }
  
  componentWillReceiveProps(nextProps) {
    let {show} = nextProps
    show !== this.state.visible && this.setState({visible: show})
  }
  
  _onSuccess = (event) => {
    if (!event.data) returnStatement
    let execStr = /goods\/(\d+)/g.exec(event.data)
    if (!execStr || execStr.length < 2) {
      alert('二维码没有包含商品ID标识，请重新扫描！')
      return
    }
    let _id = execStr[1]
    this.setState({visible: false}, () => {
      this.props.onClosed()
      navigate('Goods', {id: _id})
    })
  }
  
  _closeModal = () => {
    this.setState({visible: false}, this.props.onClosed)
  }
  
  render() {
    let {visible} = this.state
    return (
      <Modal
        animationType="slide"
        visible={ visible }
        transparent={ true }
        onRequestClose={ this._closeModal }
        { ...this.props }
      >
        <StatusBar barStyle='light-content'/>
        <View style={ styles.container }>
          <View style={ styles.header }>
            <TouchableOpacity style={ styles.back } onPress={ this._closeModal }>
              <Icon name="ios-arrow-back-outline" color="#FFFFFF" size={ 44 }/>
            </TouchableOpacity>
            <View style={ styles.title }>
              <TitleText style={ styles.title_text }>扫一扫</TitleText>
            </View>
            <TouchableOpacity style={ styles.back }>
            
            </TouchableOpacity>
          </View>
          <View style={ styles.qr_view }>
            <QRCodeScanner
              onRead={ this._onSuccess }
              cameraStyle={ styles.camera_style }
              showMarker={ true }
              fadeIn={ false }
            />
          </View>
          <View style={ styles.mask_view }>
            <View style={ styles.mask_view_top }/>
            <TitleText style={ styles.tip }>对准二维码 / 条形码到框内即可扫描</TitleText>
            <View style={ styles.mask_view_content }>
              <View style={ styles.mask_view_left }/>
              <View style={ styles.mask_view_center }>
                <Icon style={ [styles.qr_arrow, styles.qr_arrow_1] } name="ios-arrow-up-outline"/>
                <Icon style={ [styles.qr_arrow, styles.qr_arrow_2] } name="ios-arrow-up-outline"/>
                <Icon style={ [styles.qr_arrow, styles.qr_arrow_3] } name="ios-arrow-up-outline"/>
                <Icon style={ [styles.qr_arrow, styles.qr_arrow_4] } name="ios-arrow-up-outline"/>
              </View>
              <View style={ styles.mask_view_left }/>
            </View>
            <View style={ styles.mask_view_top }/>
          </View>
        </View>
      </Modal>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#000000'
  },
  header: {
    position: 'absolute',
    zIndex: 3,
    top: 0,
    left: 0,
    flexDirection: 'row',
    justifyContent: 'space-between',
    width: Screen.width,
    height: 44,
    paddingTop: getStatusBarHeight(true)
  },
  back: {
    width: 44,
    height: 44,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: colors.transparent
  },
  title: {
    justifyContent: 'center',
    alignItems: 'center',
    width: Screen.width - 88,
    height: 44
  },
  title_text: {
    color: '#FFFFFF',
    backgroundColor: colors.transparent
  },
  qr_view: {
    width: Screen.width,
    height: Screen.height,
    zIndex: 1
  },
  camera_style: {
    width: Screen.width,
    height: Screen.height
  },
  mask_view: {
    alignItems: 'center',
    position: 'absolute',
    zIndex: 2,
    top: 0,
    left: 0,
    width: Screen.width,
    height: Screen.height
  },
  mask_view_top: {
    width: Screen.width,
    height: (Screen.height - 250) / 2,
    backgroundColor: 'rgba(0,0,0,.4)'
  },
  tip: {
    position: 'absolute',
    top: (Screen.height - 250) / 2 - 30,
    zIndex: 3,
    color: '#FFFFFF',
    backgroundColor: colors.transparent
  },
  mask_view_content: {
    flexDirection: 'row',
    width: Screen.width,
    height: 250
  },
  mask_view_left: {
    width: (Screen.width - 250) / 2,
    height: 250,
    backgroundColor: 'rgba(0,0,0,.4)'
  },
  mask_view_center: {
    width: 250,
    height: 250
  },
  qr_arrow: {
    position: 'absolute',
    zIndex: 3,
    color: '#FFFFFF',
    fontSize: 45,
    backgroundColor: colors.transparent
  },
  qr_arrow_1: {
    top: -17.5,
    left: -7,
    transform: [{rotate: "-45deg"}]
  },
  qr_arrow_2: {
    top: -17.5,
    right: -7,
    transform: [{rotate: "45deg"}]
  },
  qr_arrow_3: {
    bottom: -17.5,
    left: -7,
    transform: [{rotate: "-135deg"}]
  },
  qr_arrow_4: {
    bottom: -17.5,
    right: -7,
    transform: [{rotate: "135deg"}]
  }
})
