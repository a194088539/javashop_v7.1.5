/**
 * Created by Andste on 2019-01-14.
 */
import React, { PureComponent } from 'react'
import {
  Animated,
  Image,
  findNodeHandle,
  View,
  Platform,
  TouchableOpacity,
  StyleSheet
} from 'react-native'
import { connect } from 'react-redux'
import { messageActions } from '../../redux/actions'
import { colors } from '../../../config'
import { Screen } from '../../utils'
import { store } from '../../redux/store'
import { F14Text, F12Text } from '../../widgets/Text'
import * as API_Members from '../../apis/members'

const isIos = Platform.OS === 'ios'

class ShopInfo extends PureComponent {
  constructor(props) {
    super(props)
    this.state = {
      viewRef: null,
      collect_count: this.props.shop['shop_collect'],
      collected: false
    }
  }
  
  async componentDidMount() {
    if (!store.getState().user.user) return
    const { shopId } = this.props
    const { message: collected } = await API_Members.getShopIsCollect(shopId)
    this.setState({ collected })
  }
  
  /**
   * 收藏、取消收藏店铺
   * @returns {Promise<void>}
   * @private
   */
  _onCollectShop = async () => {
    let { collected } = this.state
    const { shopId } = this.props
    if (!store.getState().user.user) {
      this.props.dispatch(messageActions.error('请先登录！'))
      return
    }
    try{
      if (collected) {
        await API_Members.deleteShopCollection(shopId)
      } else {
        await API_Members.collectionShop(shopId)
      }
      this.setState({
        collected: !collected,
        collect_count: this.state.collect_count + (collected ? -1 : 1)
      })
    }catch(err){
      this.props.dispatch(messageActions.error(err.response.data.message))
    }
    
  }
  
  render() {
    const { shop, marginTop } = this.props
    const { collect_count, collected } = this.state
    return (
      <Animated.View style={ [styles.container, isIos && { marginTop }] }>
        <Image style={ styles.shop_logo } source={ { uri: shop['shop_logo'] } } resizeMode="cover"/>
        <View style={ [styles.shop_blur_view, styles.shop_info_mask ] }/>
        <View style={ styles.info_view }>
          <View style={ styles.info_view_logo }>
            <Image style={ styles.shop_logo_mini } source={ { uri: shop['shop_logo'] } }/>
            <View style={ { marginLeft: 5 } }>
              <F14Text style={ styles.shop_name }>{ shop['shop_name'] }</F14Text>
              <F12Text style={ styles.shop_name }>{ shop['shop_credit'] }分</F12Text>
            </View>
          </View>
          <View style={ styles.info_view_follow }>
            <TouchableOpacity
              style={ styles.follow_btn }
              onPress={ this._onCollectShop }
            >
              <Image
                style={ styles.follow_btn_icon }
                source={ require('../../images/icon-follow.png') }
              />
              <F14Text style={ styles.follow_btn_text }>{ collected ? '已关注' : '关注' }</F14Text>
            </TouchableOpacity>
            <View style={ styles.shop_collect_count }>
              <F12Text style={ styles.shop_name }>{ collect_count }人</F12Text>
            </View>
          </View>
        </View>
      </Animated.View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    width: Screen.width,
    height: 100,
    backgroundColor: '#F5F5F5'
  },
  shop_logo: {
    position: 'absolute',
    width: Screen.width,
    height: 100,
    zIndex: 1
  },
  shop_blur_view: {
    position: 'absolute',
    top: 0,
    left: 0,
    zIndex: 2,
    width: Screen.width,
    height: 100
  },
  info_view: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'flex-end',
    position: 'absolute',
    top: 0,
    left: 0,
    zIndex: 3,
    width: Screen.width,
    height: 100
  },
  info_view_logo: {
    flexDirection: 'row',
    height: 30,
    marginBottom: 10,
    marginLeft: 5
  },
  shop_logo_mini: {
    width: 80,
    height: 30,
    borderColor: '#fff',
    borderWidth: Screen.onePixel
  },
  shop_name: {
    color: '#FFF',
    backgroundColor: colors.transparent
  },
  info_view_follow: {
    width: 80,
    marginBottom: 2
  },
  follow_btn: {
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    width: 80,
    height: 25,
    backgroundColor: colors.main,
    borderTopLeftRadius: 12,
    borderBottomLeftRadius: 12
  },
  follow_btn_icon: {
    width: 18,
    height: 18,
    marginRight: 5
  },
  follow_btn_text: {
    color: '#FFF'
  },
  shop_collect_count: {
    alignItems: 'center',
    marginTop: 2
  },
  shop_info_mask: {
    backgroundColor: 'rgba(0,0,0,.5)'
  }
})

export default connect()(ShopInfo)
