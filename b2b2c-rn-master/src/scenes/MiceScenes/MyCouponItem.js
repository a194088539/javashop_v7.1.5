/**
 * Created by Andste on 2018/11/8.
 */
import React, { PureComponent } from 'react'
import {
  View,
  Image,
  Text,
  TouchableOpacity,
  StyleSheet
} from 'react-native'
import { colors } from '../../../config'
import { Screen, Foundation } from '../../utils'

export default function ({ data }) {
  let icon = data['used_status'] === 0
    ? require('../../images/icon-bonus-unused.png')
    : require('../../images/icon-bonus-used.png')
  icon = data['used_status'] === 2 ? require('../../images/icon-bonus-expired.png') : icon
  return (
    <TouchableOpacity
      style={ styles.container }
      activeOpacity={ 1 }
      onPress={ () => {} }
    >
      <View style={ [styles.left, data['used_status'] === 1 && { backgroundColor: '#96A8BF'} || data['used_status'] === 2 && { backgroundColor: '#4f5c6e'} ] }>
        <Text style={ styles.money_text } adjustsFontSizeToFit={ data['coupon_price'] > 9999 } numberOfLines={ 1 }>
          <Text style={ styles.symbol }>￥</Text>
          <Text style={ styles.money }>{ data['coupon_price'] }</Text>
        </Text>
        <Text style={ styles.min }>满{ data['coupon_threshold_price'] }可用</Text>
      </View>
      <View style={ styles.right }>
        <View style={ styles.right_view }>
          <Text style={ styles.right_view_text } numberOfLines={ 2 }>{ data['title'] } -
            仅限[{ data['seller_name'] }]店铺可用</Text>
        </View>
        <View style={ [styles.right_view, styles.right_time] }>
          <Text
            style={ [styles.right_view_text, { fontSize: 12 }] }
          >
            { Foundation.unixToDate(data['start_time'], 'yyyy-MM-dd') }-{ Foundation.unixToDate(data['end_time'], 'yyyy-MM-dd') }
          </Text>
        </View>
        <Image style={ styles.received_img } source={ icon }/>
      </View>
    </TouchableOpacity>
  )
}

const WIDTH = Screen.width - 20

const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'row',
    width: WIDTH,
    height: 100,
    shadowOffset: {
      width: 0,
      height: 2
    },
    shadowColor: '#CCCCCC',
    shadowOpacity: .75,
    shadowRadius: 3,
    overflow: 'hidden'
  },
  left: {
    width: 120,
    height: 100,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: colors.main
  },
  money_text: {
    color: '#FFFFFF',
    fontWeight: '600'
  },
  symbol: {
    fontSize: 16
  },
  money: {
    fontSize: 35
  },
  min: {
    color: '#FFFFFF',
    fontSize: 12
  },
  right: {
    width: WIDTH - 120,
    height: 100,
    paddingLeft: 10,
    paddingRight: 10,
    backgroundColor: '#FFFFFF'
  },
  right_view: {
    height: 50,
    justifyContent: 'center',
    alignItems: 'flex-start'
  },
  right_view_text: {
    color: '#777777'
  },
  right_time: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center'
  },
  receive: {
    width: 60,
    height: 22,
    justifyContent: 'center',
    alignItems: 'center',
    borderColor: colors.main,
    borderWidth: 1,
    borderRadius: 22
  },
  receive_text: {
    fontSize: 12,
    color: colors.main
  },
  received_img: {
    position: 'absolute',
    right: -15,
    top: -15,
    width: 60,
    height: 60
  }
})
