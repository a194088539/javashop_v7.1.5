/**
 * Created by Andste on 2019-02-11.
 */
import React, { PureComponent } from 'react'
import {
  View,
  Modal,
  Animated,
  Image,
  Text,
  TouchableOpacity,
  StyleSheet
} from 'react-native'
import { store } from '../redux/store'
import { messageActions } from '../redux/actions'
import { isIphoneX } from 'react-native-iphone-x-helper'
import { colors } from '../../config'
import { Screen } from '../utils'
import * as Wechat from 'react-native-wechat'
import * as QQAPI from 'react-native-qq'
import * as Weibo from 'react-native-weibo'
// import Alipay from '@0x5e/react-native-alipay'

const bodyHeight = 300
const shareBtnHeight = isIphoneX() ? 60 : 50
const animateDuration = 200

const shares = [
  { title: '新浪微博', key: 'weibo', icon: require('../images/icon-share-sina.png') },
  { title: '微信', key: 'wechat', icon: require('../images/icon-share-wechat.png') },
  { title: '微信朋友圈', key: 'wechat_timeline', icon: require('../images/icon-share-wechat-timeline.png') },
  { title: '微信收藏', key: 'wechat_favorite', icon: require('../images/icon-share-wechat-favorite.png') },
  { title: 'QQ', key: 'qq', icon: require('../images/icon-share-qq.png') },
  { title: 'QQ空间', key: 'qzone', icon: require('../images/icon-share-qzone.png') },
  // { title: '支付宝', key: 'alipay', icon: require('../images/icon-share-alipay.png') }
]

export default class ShareActionSheet extends PureComponent {
  constructor(props) {
    super(props)
    this.bodyTranslateY = new Animated.Value(bodyHeight)
    this.state = { visible: false }
  }
  
  /**
   * 打开模态框
   */
  open = async () => {
    await this.setState({ visible: true })
    Animated.timing(this.bodyTranslateY, {
      toValue: 0,
      duration: animateDuration
    }).start()
  }
  
  /**
   * 关闭模态框
   * @returns {Promise<any> | Promise<*>}
   */
  close = () => {
    return new Promise((resolve, reject) => {
      Animated.timing(this.bodyTranslateY, {
        toValue: bodyHeight,
        duration: animateDuration
      }).start()
      setTimeout(async () => {
        await this.setState({ visible: false })
        resolve()
      }, animateDuration)
    })
  }
  
  /**
   * 分享成功
   * @private
   */
  _onShareSuccess = async (msg = '分享成功！') => {
    await this.close()
    store.dispatch(messageActions.success(msg))
  }
  
  /**
   * 分享失败
   * @returns {Promise<void>}
   * @private
   */
  _onShareError = async (msg = '分享失败！') => {
    await this.close()
    store.dispatch(messageActions.error(msg))
  }
  
  /**
   * 点击分享
   * @private
   */
  _onPress = async (item) => {
    switch (item.key) {
      case 'weibo':
        await this._shareByWeibo()
        break
      case 'wechat':
        await this._shareByWechat()
        break
      case 'wechat_timeline':
        await this._shareByWechatTimeline()
        break
      case 'wechat_favorite':
        await this._shareByWechatFavorite()
        break
      case 'qq':
        await this._shareByQQ()
        break
      case 'qzone':
        await this._shareByQzone()
        break
      case 'alipay':
        await this._shareByAlipay()
        break
    }
  }
  
  /**
   * 微博分享
   * @private
   */
  _shareByWeibo = async () => {
    const { data } = this.props
    try {
      await Weibo.share({
        type: 'image',
        text: `${ data['title'] } ${ data['webpageUrl'] } ${ data['description'] }`,
        imageUrl: data['imageUrl']
      })
      await this._onShareSuccess()
    } catch (e) {
      await this._onShareError()
    }
  }
  
  /**
   * 分享到微信
   * @returns {Promise<void>}
   * @private
   */
  _shareByWechat = async () => {
    const { data } = this.props
    if (!await Wechat.isWXAppInstalled()) {
      await this._onShareError('未安装微信！')
      return
    }
    try {
      await Wechat.shareToSession({
        ...data,
        thumbImage: data['imageUrl']
      })
      await this._onShareSuccess()
    } catch (e) {
      await this._onShareError()
    }
  }
  
  /**
   * 分享到微信朋友圈
   * @returns {Promise<void>}
   * @private
   */
  _shareByWechatTimeline = async () => {
    const { data } = this.props
    if (!await Wechat.isWXAppInstalled()) {
      await this._onShareError('未安装微信！')
      return
    }
    try {
      await Wechat.shareToTimeline({
        ...data,
        thumbImage: data['imageUrl']
      })
      await this._onShareSuccess()
    } catch (e) {
      await this._onShareError()
    }
  }
  
  /**
   * 分享到微信收藏
   * @returns {Promise<void>}
   * @private
   */
  _shareByWechatFavorite = async () => {
    const { data } = this.props
    if (!await Wechat.isWXAppInstalled()) {
      await this._onShareError('未安装微信！')
      return
    }
    try {
      await Wechat.shareToFavorite({
        ...data,
        thumbImage: data['imageUrl']
      })
      await this._onShareSuccess('收藏成功！')
    } catch (e) {
      await this._onShareError('收藏失败！')
    }
  }
  
  /**
   * 分享到QQ
   * @returns {Promise<void>}
   * @private
   */
  _shareByQQ = async () => {
    const { data } = this.props
    try {
      await QQAPI.isQQInstalled()
    } catch (e) {
      await this._onShareError('未安装QQ！')
      return
    }
    try {
      await QQAPI.shareToQQ({ type: 'news', ...data })
      await this._onShareSuccess()
    } catch (e) {
      await this._onShareError()
    }
  }
  
  /**
   * 分享到QQ空间
   * @returns {Promise<void>}
   * @private
   */
  _shareByQzone = async () => {
    const { data } = this.props
    try {
      await QQAPI.isQQInstalled()
    } catch (e) {
      await this._onShareError('未安装QQ！')
      return
    }
    try {
      await QQAPI.shareToQzone({ type: 'news', ...data })
      await this._onShareSuccess()
    } catch (e) {
      await this._onShareError()
    }
  }
  
  /**
   * 分享到支付宝
   * @returns {Promise<void>}
   * @private
   */
  _shareByAlipay = async () => {
  
  }
  
  render() {
    const { visible } = this.state
    return (
      <Modal
        visible={ visible }
        transparent
        hardwareAccelerated
        animationType="fade"
      >
        <TouchableOpacity style={ styles.container } onPress={ this.close } activeOpacity={ 1 }/>
        <Animated.View style={ [
          styles.body,
          { transform: [{ translateY: this.bodyTranslateY }] }
        ] }>
          <View style={ styles.tip_view }>
            <Text style={ styles.tip_text }>请选择分享平台</Text>
          </View>
          <View style={ styles.icons_view }>
            { shares.map(item => (
              <IconItem
                key={ item.key }
                title={ item.title }
                icon={ item.icon }
                onPress={ () => this._onPress(item) }
              />
            )) }
          </View>
          <TouchableOpacity style={ styles.share_btn } onPress={ this.close }>
            <Text style={ styles.share_text }>取消分享</Text>
          </TouchableOpacity>
        </Animated.View>
      </Modal>
    )
  }
}

const IconItem = ({ icon, title, ...props }) => (
  <TouchableOpacity style={ styles.icon } { ...props }>
    <View style={ styles.icon_view }>
      <Image source={ icon } style={ styles.icon_img }/>
    </View>
    <Text style={ styles.icon_title }>{ title }</Text>
  </TouchableOpacity>
)

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: 'rgba(0,0,0,.5)'
  },
  body: {
    position: 'absolute',
    zIndex: 2,
    bottom: 0,
    width: Screen.width,
    height: bodyHeight,
    backgroundColor: '#E9EFF1'
  },
  tip_view: {
    height: 50,
    justifyContent: 'center',
    alignItems: 'center'
  },
  tip_text: {
    color: colors.text,
    fontSize: 16
  },
  icons_view: {
    flexWrap: 'wrap',
    flexDirection: 'row',
    justifyContent: 'space-around',
    height: bodyHeight - 50 - shareBtnHeight,
    paddingHorizontal: 30
  },
  icon: {
    alignItems: 'center',
    marginBottom: 20,
    width: (Screen.width - 60) / (shares.length > 6 ? 4 : 3)
  },
  icon_view: {
    justifyContent: 'center',
    alignItems: 'center',
    width: 50,
    height: 50,
    backgroundColor: '#FFF',
    borderRadius: 50,
    overflow: 'hidden'
  },
  icon_img: {
    width: 50 * .85,
    height: 50 * .85
  },
  icon_title: {
    marginTop: 10
  },
  share_btn: {
    justifyContent: 'center',
    alignItems: 'center',
    height: shareBtnHeight,
    backgroundColor: '#FFF',
    paddingBottom: isIphoneX() ? 15 : 0
  },
  share_text: {
    color: colors.text
  }
})
