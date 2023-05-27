/**
 * Created by Andste on 2019-01-08.
 */
import React, { PureComponent } from 'react'
import {
  View,
  Text,
  ImageBackground,
  StyleSheet
} from 'react-native'
import { colors } from '../../../config'
import { Screen } from '../../utils'
import { Price } from '../../widgets'
import ProIcon from './ProIcon'
import CountDown from './CountDown'

export default class GoodsProBar extends PureComponent {
  render() {
    const { data } = this.props
    const exchange = data.filter(item => item['exchange'])[0]
    const groupbuy = data.filter(item => item['groupbuy_goods_vo'])[0]
    const seckill = data.filter(item => item['seckill_goods_vo'])[0]
    // 积分商品
    if (exchange) {
      const ex = exchange['exchange']
      return (
        <View style={ styles.pro_view }>
          <ImageBackground
            style={ styles.point_pro_bg }
            source={ require('../../images/bg-pro-bar.png') }
          >
            <View style={ styles.pro_price_view }>
              <Price
                advanced={ true }
                price={ ex['exchange_money'] || 0 }
                style={ styles.pro_price }
                scale={ 1.5 }
              />
              <Text style={ styles.ex_point }>+ { ex['exchange_point'] }积分</Text>
            </View>
            <View style={ styles.pro_icon_view }>
              <Price style={ styles.pro_origin_price } price={ ex['goods_price'] }/>
              <ProIcon icon={ require('../../images/icon-exchange.png') } text="积分兑换"/>
            </View>
          </ImageBackground>
        </View>
      )
    }
    // 团购活动
    if (groupbuy) {
      const gb = groupbuy['groupbuy_goods_vo']
      return (
        <View style={ styles.pro_view }>
          <ImageBackground
            style={ styles.pro_bg }
            source={ require('../../images/bg-pro-bar.png') }
          >
            <View style={ styles.pro_price_view }>
              <Price
                advanced={ true }
                price={ gb['price'] }
                style={ styles.pro_price }
                scale={ 1.2 }
              />
            </View>
            <View style={ styles.pro_icon_view }>
              <Price style={ styles.pro_origin_price } price={ gb['original_price'] }/>
              <ProIcon icon={ require('../../images/icon-group-buy.png') } text="团购活动"/>
            </View>
          </ImageBackground>
          <View style={ styles.pro_time }>
            <Text style={ styles.pro_time_tit }>距离结束还剩</Text>
            <CountDown endTime={ groupbuy['end_time'] - parseInt(new Date() / 1000) }/>
          </View>
        </View>
      )
    }
    // 限时抢购
    if (seckill) {
      const sk = seckill['seckill_goods_vo']
      return (
        <View style={ styles.pro_view }>
          <ImageBackground
            style={ styles.pro_bg }
            source={ require('../../images/bg-pro-bar.png') }
          >
            <View style={ styles.pro_price_view }>
              <Price
                advanced={ true }
                price={ sk['seckill_price'] }
                style={ styles.pro_price }
                scale={ 1.5 }
              />
            </View>
            <View style={ styles.pro_icon_view }>
              <Price style={ styles.pro_origin_price } price={ sk['original_price'] }/>
              <ProIcon icon={ require('../../images/icon-seckill-timer.png') } text="限时抢购"/>
            </View>
          </ImageBackground>
          <View style={ styles.pro_time }>
            <Text style={ styles.pro_time_tit }>距离结束还剩</Text>
            <CountDown endTime={ sk['distance_end_time'] }/>
          </View>
        </View>
      )
    }
    return undefined
  }
}

const styles = StyleSheet.create({
  pro_view: {
    flexDirection: 'row',
    height: 50,
    backgroundColor: '#FFF'
  },
  pro_bg: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    width: Screen.width - 120,
    height: 50,
    paddingLeft: 5
  },
  point_pro_bg: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    width: Screen.width,
    height: 50,
    paddingLeft: 5
  },
  pro_time: {
    alignItems: 'center',
    justifyContent: 'center',
    width: 120,
    height: 50,
    backgroundColor: '#F8F8F8'
  },
  pro_time_tit: {
    color: '#666'
  },
  pro_price_view: {
    flexDirection: 'row'
  },
  pro_price: {
    color: '#FFF',
    fontWeight: '500'
  },
  ex_point: {
    color: '#FFF',
    fontWeight: '500',
    fontSize: 18,
    marginTop: 5,
    marginLeft: 5
  },
  pro_icon_view: {
    justifyContent: 'center',
    alignItems: 'center',
    width: 80,
    height: 50
  },
  pro_origin_price: {
    color: '#FFF',
    fontSize: 10,
    textDecorationLine: 'line-through'
  },
  ex_text: {
    marginBottom: 2,
    textAlign: 'center',
    fontSize: 16
  }
})
