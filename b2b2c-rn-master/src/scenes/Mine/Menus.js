/**
 * Created by Andste on 2018/10/18.
 */
import React, { Component } from 'react'
import {
  View,
  Text,
  Image,
  TouchableOpacity,
  StyleSheet
} from 'react-native'
import { navigate } from '../../navigator/NavigationService'
import { colors } from '../../../config'
import { Screen } from '../../utils'

export default class Menus extends Component {
  constructor(props) {
    super(props)
    this.menus = [
      [
        {
          title: '待付款',
          icon: require('../../images/icon-order-wait_pay.png'),
          name: 'MyOrder',
          params: { status: 'WAIT_PAY' }
        },
        {
          title: '待收货',
          icon: require('../../images/icon-order-wait_rog.png'),
          name: 'MyOrder',
          params: { status: 'WAIT_ROG' }
        },
        {
          title: '待评论',
          icon: require('../../images/icon-order-wait_comments.png'),
          name: 'MyOrder',
          params: { status: 'WAIT_COMMENT' }
        },
        { title: '退换/售后', icon: require('../../images/icon-order-refund.png'), name: 'MyAfterSale' },
        { title: '我的订单', icon: require('../../images/icon-order.png'), name: 'MyOrder' }
      ],
      [
        { title: '我的积分', icon: require('../../images/icon-my_point.png'), name: 'MyPoint' },
        { title: '我的优惠券', icon: require('../../images/icon-my_coupon.png'), name: 'MyCoupon' },
        {
          title: '收藏的商品',
          icon: require('../../images/icon-planet.png'),
          name: 'MyCollection',
          params: { type: 'goods' }
        },
        { title: '收藏的店铺', icon: require('../../images/icon-shop.png'), name: 'MyCollection', params: { type: 'shop' } },
        { title: '账户安全', icon: require('../../images/icon-safe.png'), name: 'Safe' }
      ]
    ]
    this.submenus = [
      { title: '站内消息', icon: require('../../images/icon-message_color.png'), name: 'SiteMessage' },
      { title: '客户服务', icon: require('../../images/icon-service_color.png'), name: 'ContactUs', dontNeedLogin: true },
      { title: '券市场', icon: require('../../images/icon-coupon_color.png'), name: 'Coupons', dontNeedLogin: true },
      { title: '收货地址', icon: require('../../images/icon-address_color.png'), name: 'MyAddress' },
      { title: '关联账号', icon: require('../../images/icon-connect_color.png'), name: 'MyConnect' }
    ]
  }
  
  _toScene = (item) => {
    const { name, dontNeedLogin, params } = item
    if (dontNeedLogin) return navigate(name, params)
    const { user } = this.props
    if (!user) {
      navigate('Login')
    } else {
      navigate(name, params)
    }
  }
  
  render() {
    return (
      <View style={ styles.container }>
        { this.menus.map((menu, menuIndex) => (
          <View style={ styles.item_view } key={ menuIndex }>
            { menu.map((item, itemIndex) => (
              <TouchableOpacity
                onPress={ () => {this._toScene(item)} }
                style={ styles.item }
                key={ itemIndex }
              >
                <Image
                  style={ [
                    styles.item_icon,
                    { tintColor: '#3c3c3f' },
                    itemIndex === menu.length - 1 && { tintColor: colors.main }
                  ] }
                  source={ item.icon }/>
                <Text style={ styles.item_title }>{ item.title }</Text>
              </TouchableOpacity>
            )) }
            <Image source={ require('../../images/icon-mine_splitline.png') } style={ styles.splitline }/>
          </View>
        )) }
        <View style={ [styles.item_view, styles.item_view_more] }>
          { this.submenus.map((menu, itemIndex) => (
            <TouchableOpacity
              onPress={ () => {this._toScene(menu)} }
              style={ styles.item }
              key={ itemIndex }
            >
              <Image style={ styles.item_icon } source={ menu.icon }/>
              <Text style={ styles.item_title }>{ menu.title }</Text>
            </TouchableOpacity>
          )) }
        </View>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    paddingHorizontal: 10,
    marginTop: 20
  },
  item_view: {
    flexDirection: 'row',
    width: Screen.width - 20,
    height: 90,
    backgroundColor: '#FFFFFF',
    borderRadius: 10,
    marginBottom: 10,
    shadowOffset: { width: 0, height: 0 },
    shadowColor: '#CCC',
    shadowOpacity: .2,
    shadowRadius: 10,
    elevation: 1
  },
  splitline: {
    width: 8,
    height: 50,
    position: 'absolute',
    top: 15,
    right: (Screen.width - 20) / 5 - 10
  },
  item: {
    justifyContent: 'center',
    alignItems: 'center',
    width: (Screen.width - 20) / 5,
    height: 90
  },
  item_icon: {
    width: (Screen.width - 20) / 5 * .45,
    height: (Screen.width - 20) / 5 * .45,
    marginBottom: 10
  },
  item_title: {
    fontSize: 12,
    color: colors.text
  },
  item_view_more: {
    justifyContent: 'space-between',
    height: 'auto',
    flexWrap: 'wrap'
  }
})
