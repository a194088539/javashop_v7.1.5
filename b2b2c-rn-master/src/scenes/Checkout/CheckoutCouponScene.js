/**
 * Created by Andste on 2019-01-21.
 */
import React, { Component } from 'react'
import {
  View,
  Image,
  Text,
  TouchableOpacity,
  DeviceEventEmitter,
  StyleSheet,
  SectionList
} from 'react-native'
import { connect } from 'react-redux'
import { messageActions } from '../../redux/actions'
import { Screen, Foundation } from '../../utils'
import { navigate } from '../../navigator/NavigationService'
import { isIphoneX } from 'react-native-iphone-x-helper'
import { BonusEmpty } from '../../components/EmptyViews'
import { Checkbox, Price } from '../../widgets'
import * as API_Trade from '../../apis/trade'

class CheckoutCouponScene extends Component {
  static navigationOptions = {
    title: '店铺优惠券'
  }
  
  constructor(props) {
    super(props)
    const { shopList } = props.navigation.state.params
    this.state = {
      shopList: shopList.map(item => {
        item.key = item['seller_id']
        item.data = item['coupon_list']
        return item
      })
    }
  }

  componentDidMount() {
    this.listener=DeviceEventEmitter.addListener('updateInventories',this._refreshCoupon)
  }
  
  componentWillUnmount() {
    this.listener.remove()
  }

  _refreshCoupon = async () => {
    const res = await API_Trade.getCarts('checked', this.way)
    const inventories = res['cart_list']
    this.setState({ 
      shopList: inventories.map(item => {
        item.key = item['seller_id']
        item.data = item['coupon_list']
        return item
      })
    })
  }

  _onPressItem = (item) => {
    const { dispatch, navigation } = this.props
    if (item.enable === 0) {
      dispatch(messageActions.error('订单金额不满足此优惠券使用条件'))
      return
    }
    navigation.state.params.callback(item)
    navigation.goBack()
  }
  
  /**
   * 头部信息
   * @param section
   * @returns {*}
   * @private
   */
  _renderSectionHeader = ({ section }) => {
    return (
      <View style={ styles.shop_header }>
        <Text>{ section['seller_name'] }</Text>
      </View>
    )
  }
  
  /**
   * 底部信息
   * @param section
   * @private
   */
  _renderSectionFooter = ({ section }) => {
    if (section.data.length) return null
    return (
      <View style={ styles.coupon_no_data }>
        <Text style={ styles.coupon_no_data_text }>您在此店铺还没有领到优惠券，</Text>
        <TouchableOpacity
          onPress={ () => navigate('Shop', { id: section['seller_id'] }) }>
          <Text style={ [styles.coupon_no_data_text, { color: '#0366d6' }] }>[去店铺]</Text>
        </TouchableOpacity>
        <Text style={ styles.coupon_no_data_text }>看看？</Text>
      </View>
    )
  }
  
  /**
   * 底部占位
   * @private
   */
  _ListFooterComponent = () => (<View style={ styles.coupon_footer }/>)
  
  /**
   * 渲染优惠券项
   * @param item
   * @returns {*}
   * @private
   */
  _renderItem = ({ item }) => (
    <CouponItem data={ item } onPress={ () => this._onPressItem(item) }/>
  )
  
  render() {
    const { shopList } = this.state
    return (
      <View style={ styles.container }>
        <SectionList
          sections={ shopList }
          renderItem={ this._renderItem }
          renderSectionHeader={ this._renderSectionHeader }
          renderSectionFooter={ this._renderSectionFooter }
          ListFooterComponent={ this._ListFooterComponent }
          ListEmptyComponent={ <BonusEmpty text="暂无优惠券" textStyle={ { color: '#878B8D' } }/> }
          keyExtractor={ item => String(item['member_coupon_id']) }
        />
      </View>
    )
  }
}

const CouponItem = ({ data, ...props }) => {
  const disabled = false
  return (
    <TouchableOpacity style={ styles.coupon_item } { ...props }>
      <Image
        style={ [
          styles.coupon_item_line,
          disabled && { tintColor: '434343' }
        ] }
        source={ data.enable ? require('../../images/icon-coupon-top-line.png') : require('../../images/icon-coupon-top-line-unable.png') }
      />
      <View style={ data.enable ? styles.coupon_body : styles.coupon_body_unable }>
        <Checkbox checked={ data['selected'] === 1 }/>
        <View>
          <Price
            style={ styles.conpon_price }
            price={ data['amount'] }
            advanced
            scale={ 1.7 }
          />
          <Text style={ styles.coupon_use_text }>{ data['use_term'] }</Text>
          <Text
            style={ styles.coupon_use_text }>使用有效期截止：{ Foundation.unixToDate(data['end_time'], 'yyyy-MM-dd') }</Text>
        </View>
      </View>
    </TouchableOpacity>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1
  },
  shop_header: {
    width: Screen.width,
    justifyContent: 'center',
    height: 35,
    padding: 10,
    backgroundColor: '#FFF'
  },
  coupon_item: {
    paddingHorizontal: 10,
    marginTop: 10,
    marginBottom: 10
  },
  coupon_item_line: {
    width: Screen.width - 20,
    height: 15
  },
  coupon_body_unable: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingBottom: 10,
    backgroundColor: '#f1f1f1',
    paddingHorizontal: 10
  },
  coupon_body: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingBottom: 10,
    backgroundColor: '#FFF',
    paddingHorizontal: 10
  },
  conpon_price: {
    fontWeight: '700'
  },
  coupon_use_text: {
    marginTop: 3,
    color: '#686868'
  },
  coupon_footer: {
    height: isIphoneX() ? 30 : 0
  },
  coupon_no_data: {
    height: 50,
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center'
  },
  coupon_no_data_text: {
    fontSize: 12
  }
})

export default connect()(CheckoutCouponScene)
