/**
 * Created by Andste on 2018/11/6.
 */
import React from 'react'
import {
  View,
  Image,
  TouchableOpacity,
  StyleSheet
} from 'react-native'
import { navigate } from '../../navigator/NavigationService'
import { colors } from '../../../config'
import { Screen, Foundation } from '../../utils'
import { F16Text } from '../../widgets/Text'
import { TextLabel } from '../../widgets'

export default function ({ data, onRefresh, confirmShip }) {
  const { sku_list } = data
  let countNum = 0
  const {
    allow_apply_service,
    allow_cancel,
    allow_comment,
    allow_pay,
    allow_rog,
    allow_service_cancel
  } = data['order_operate_allowable_vo']
  return (
    <TouchableOpacity
      activeOpacity={ 1 }
      onPress={ () => navigate('OrderDetail', { data, callback: onRefresh }) }
      style={ styles.container }
    >
      <View style={ styles.header }>
        <F16Text>订单号：{ data['sn'] }</F16Text>
        <F16Text style={ styles.order_status }>{ data['order_status_text'] }</F16Text>
      </View>
      <View style={ styles.body }>
        <View style={ styles.b_content_view }>
          { sku_list.map((goods, index) => {
            countNum += goods['num']
            if (index > 4) return
            if (sku_list.length === 1) {
              return (
                <View key={ index } style={ styles.is_once }>
                  <Image
                    style={ styles.goods_image }
                    source={ { uri: goods['goods_image'] } }
                  />
                  <F16Text
                    style={ styles.goods_name }
                    numberOfLines={ 3 }
                  >{ goods['name'] }</F16Text>
                </View>
              )
            }
            return (
              <Image
                key={ index }
                style={ styles.goods_image }
                source={ { uri: goods['goods_image'] } }
              />
            )
          }) }
        </View>
        <View style={ styles.b_price_view }>
          <F16Text>
            共{ countNum }件商品 { data['pay_status'] === 'PAY_NO' ? '需' : '实' }付款：
            ￥{ Foundation.formatPrice(data['order_amount']) }
          </F16Text>
        </View>
      </View>
      <View style={ styles.footer }>
        { !allow_apply_service && !allow_cancel && !allow_comment && !allow_pay && !allow_rog && !allow_service_cancel ? (
          <TextLabel
            style={ styles.footer_btn }
            text="查看详情"
            onPress={ () => navigate('OrderDetail', { data, callback: onRefresh }) }/>
        ) : undefined }
        { allow_apply_service ? (
          <TextLabel
            style={ styles.footer_btn }
            text="申请售后"
            onPress={ () => navigate('ApplyAfterSale', { data, callback: onRefresh }) }/>
        ) : undefined }
        { allow_service_cancel ? (
          <TextLabel
            style={ styles.footer_btn }
            text="取消订单"
            onPress={ () => navigate('ApplyAfterSale', { data, callback: onRefresh }) }/>
        ) : undefined }
        { allow_cancel ? (
          <TextLabel
            style={ styles.footer_btn }
            text="取消订单"
            onPress={ () => navigate('CancelOrder', { data, callback: onRefresh }) }/>
        ) : undefined }
        { allow_pay ? (
          <TextLabel
            style={ [styles.footer_btn, styles.footer_btn_topay] }
            textStyle={ { color: '#FFFFFF' } }
            text="去支付"
            onPress={ () => navigate('Cashier', { order_sn: data['sn'], callback: onRefresh }) }/>
        ) : undefined }
        { allow_rog ? (
          <TextLabel
            style={ [styles.footer_btn, styles.footer_btn_topay] }
            textStyle={ { color: '#FFFFFF' } }
            text="确认收货"
            onPress={ () => confirmShip(data) }/>
        ) : undefined }
        { allow_comment ? (
          <TextLabel
            style={ styles.footer_btn }
            text="去评价"
            onPress={ () => navigate('CommentOrder', { data, callback: onRefresh }) }/>
        ) : undefined }
        { data.comment_status === 'WAIT_CHASE' ? (
          <TextLabel
            style={ styles.footer_btn }
            text="追加评价"
            onPress={ () => navigate('CommentOrder', { data,append_comment: true, callback: onRefresh }) }/>
        ) : undefined }
      </View>
    </TouchableOpacity>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'space-between',
    alignItems: 'center',
    height: 205,
    backgroundColor: '#FFFFFF'
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    width: Screen.width,
    height: 40,
    paddingHorizontal: 15
  },
  order_status: {
    color: colors.main
  },
  body: {
    width: Screen.width,
    height: 205 - 80
  },
  b_content_view: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 15,
    paddingVertical: (205 - 120 - 60) / 2,
    backgroundColor: '#FAFAFA',
    height: 205 - 120
  },
  goods_image: {
    width: 60,
    height: 60,
    borderColor: '#FFFFFF',
    borderWidth: 2,
    borderRadius: 2,
    marginRight: 15
  },
  is_once: {
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    width: Screen.width - 30,
    height: 60
  },
  goods_name: {
    width: Screen.width - 30 - 60 - 15,
    lineHeight: 18
  },
  b_price_view: {
    justifyContent: 'center',
    alignItems: 'flex-end',
    height: 40,
    paddingHorizontal: 15,
    borderColor: colors.cell_line_backgroud,
    borderBottomWidth: Screen.onePixel
  },
  footer: {
    flexDirection: 'row',
    justifyContent: 'flex-end',
    alignItems: 'center',
    width: Screen.width,
    height: 40,
    paddingHorizontal: 15
  },
  footer_btn: {
    paddingVertical: 2,
    marginRight: 0,
    marginLeft: 10,
    marginBottom: 0
  },
  footer_btn_topay: {
    backgroundColor: colors.main,
    borderColor: colors.main
  }
})
