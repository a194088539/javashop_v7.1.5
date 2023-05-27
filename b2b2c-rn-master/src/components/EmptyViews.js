/**
 * Created by Andste on 2017/8/24.
 * 空占位
 */
import React from 'react'
import {
  StyleSheet,
  Image,
  Text,
  View
} from 'react-native'

//  暂无数据
const empty_data = require('../images/icon-empty-data.png')
//  订单列表为空
const empty_order = require('../images/icon-empty-order.png')
//  优惠券列表为空
const empty_bonus = require('../images/icon-empty-bonus.png')
//  地址列表为空
const empty_address = require('../images/icon-empty-address.png')
//  暂无图片
const empty_image = require('../images/icon-empty-image.png')
// 评论为空
const empty_comment = require('../images/icon-empty-comment.png')

/**
 * 暂无数据
 * @param params
 * @returns {XML}
 * @constructor
 */
export function DataEmpty(params : Object) {
  return EmptyView(empty_data, '数据都飞走啦...', params)
}

/**
 * 订单列表
 * @param params
 * @returns {XML}
 * @constructor
 */
export function OrderEmpty(params : Object) {
  return EmptyView(empty_order, '暂无订单...', params)
}

/**
 * 商品
 * @param params
 * @returns {XML}
 * @constructor
 */
export function GoodsEmpty(params : Object) {
  return EmptyView(empty_data, '暂无商品...', params)
}

/**
 * 收藏商品
 * @param params
 * @returns {XML}
 * @constructor
 */
export function MyCollectionGoodsEmpty(params : Object) {
  return EmptyView(empty_data, '暂无收藏商品...', params)
}

/**
 * 收藏店铺
 * @param params
 * @returns {XML}
 * @constructor
 */
export function MyCollectionShopEmpty(params : Object) {
  return EmptyView(empty_data, '暂无收藏店铺...', params)
}

/**
 * 售后列表
 * @param params
 * @returns {XML}
 * @constructor
 */
export function RefundEmpty(params : Object) {
  return EmptyView(empty_order, '暂无售后...', params)
}

/**
 * 优惠券
 * @param params
 * @returns {XML}
 * @constructor
 */
export function BonusEmpty(params : Object) {
  return EmptyView(empty_bonus, null, params)
}

/**
 * 地址
 * @param params
 * @returns {XML}
 * @constructor
 */
export function AddressEmpty(params : Object) {
  return EmptyView(empty_address, '您还没添加地址...', params)
}

/**
 * 评论
 * @param params
 * @returns {XML}
 * @constructor
 */
export function CommentEmpty(params : Object) {
  return EmptyView(empty_comment, '暂无评论...', params)
}

/**
 * 图片
 * @param params
 * @returns {XML}
 * @constructor
 */
export function ImageEmpty(params : Object) {
  return <Image source={ empty_image } { ...params }/>
}

/**
 * 公共View
 * @param module
 * @param _text
 * @param style
 * @param text
 * @param imageStyle
 * @param textStyle
 * @param props
 * @returns {XML}
 * @constructor
 */
function EmptyView(module, _text, {style, text, imageStyle, textStyle, ...props}) {
  return (
    <View style={ [styles.container, style] } { ...props }>
      <Image style={ [styles.empty_image, imageStyle] } source={ module } resizeMode="contain"/>
      <Text style={ [styles.empty_text, textStyle] }>{ text || _text || '暂无数据...' }</Text>
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center'
  },
  empty_image: {
    width: 100,
    height: 100,
    marginTop: 50
  },
  empty_text: {
    color: '#96A8BF',
    fontSize: 16,
    marginTop: 10
  }
})
