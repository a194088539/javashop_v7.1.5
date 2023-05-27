/**
 * Created by Andste on 2019-01-09.
 */
import React, { PureComponent } from 'react'
import {
  View,
  Image,
  Text,
  TouchableOpacity,
  ScrollView,
  DeviceEventEmitter,
  StyleSheet
} from 'react-native'
import { colors } from '../../../config'
import { Foundation, Screen } from '../../utils'
import { navigate } from '../../navigator/NavigationService'
import * as API_Promotions from '../../apis/promotions'

export default class HomeSeckill extends PureComponent {
  constructor(props) {
    super(props)
    this.state = {
      time: '',
      onlyOne: '',
      timeLine: '',
      goodsList: ''
    }
  }
  
  componentDidMount() {
    this._getSeckillTimeLine()
  }
  
  /**
   * 获取限时抢购时间线
   * @returns {Promise<void>}
   * @private
   */
  _getSeckillTimeLine = async () => {
    let res = await API_Promotions.getSeckillTimeLine()
    if (!res || !res.length) return
    res = res.sort((x, y) => (Number(x['time_text']) - Number(y['time_text'])))
    const onlyOne = res.length === 1
    const time = res[0].distance_time !== 0 ? res[0].distance_time : onlyOne ? Foundation.theNextDayTime() : res[1].distance_time
    const range_time = res[0]['time_text']
    const timeLine = res[0]
    const goods = await API_Promotions.getSeckillTimeGoods({ range_time })
    if (!goods.data || !goods.data.length) return
    this.setState({ time, goodsList: goods.data, onlyOne, timeLine })
  }
  
  /**
   * 去限时抢购页
   * @private
   */
  _toSeckill = () => {
    navigate('Seckill')
  }
  
  /**
   * 去商品页
   * @param item
   * @private
   */
  _toGoods = (item) => {
    navigate('Goods', { id: item['goods_id'] })
  }
  
  componentWillUnmount() {
    this._setInterval && clearInterval(this._setInterval)
  }
  
  render() {
    const { goodsList, time, onlyOne, timeLine } = this.state
    if (!goodsList || !goodsList.length) {
      return <View/>
    }
    return (
      <View style={ styles.container }>
        <TouchableOpacity style={ styles.sk_tools } onPress={ this._toSeckill }>
          <View style={ styles.sk_tools_left }>
            <Image style={ styles.sk_tools_img } source={ require('../../images/icon-seckill.png') }/>
            <CountDown label={ timeLine.distance_time === 0 ? (onlyOne ? '距离本轮结束还剩' : '距离下一轮还有' ) : '距开始'} time={ time }/>
          </View>
          <View style={ styles.sk_tools_more }>
            <Text style={ styles.sk_tools_more_text }>更多限时抢购></Text>
          </View>
        </TouchableOpacity>
        <View style={ styles.sk_goods }>
          <ScrollView horizontal={ true } showsHorizontalScrollIndicator={ false }>
            { goodsList.map((item, index) => (
              <GoodsItem
                data={ item }
                key={ index }
                onPress={ () => this._toGoods(item) }
              />
            )) }
          </ScrollView>
        </View>
      </View>
    )
  }
}

class CountDown extends PureComponent {
  constructor(props) {
    super(props)
    this.time = props.time
    this.state = {
      times: '',
      label: props.label
    }
  }
  
  componentDidMount() {
    this.time && this._countDown()
  }
  
  /**
   * 倒计时
   * @private
   */
  _countDown = () => {
    this._setInterval && clearInterval(this._setInterval)
    this._setInterval = setInterval(async () => {
      if (this.time <= 0) {
        clearInterval(this._setInterval)
        DeviceEventEmitter.emit('ReloadHome', 0)
        return false
      }
      this.time--
      this.setState({ times: Foundation.countTimeDown(this.time) })
    }, 1000)
  }
  
  render() {
    const { times, label } = this.state
    if (!times) return <View/>
    return (
      <View style={ styles.time_container }>
        {label && <Text style={ styles.time_label }> {label} </Text>}
        <Text style={ styles.time_box }>{ times.hours }</Text>
        <Text> : </Text>
        <Text style={ styles.time_box }>{ times.minutes }</Text>
        <Text> : </Text>
        <Text style={ styles.time_box }>{ times.seconds }</Text>
      </View>
    )
  }
}

const GoodsItem = ({ data, onPress }) => {
  return (
    <TouchableOpacity style={ styles.goods_item } onPress={ onPress }>
      <Image style={ styles.goods_img } source={ { uri: data['goods_image'] } } />
      <Text style={ styles.sk_price }>￥{Foundation.formatPrice(data['seckill_price'])}</Text>
      <Text style={ styles.sk_original_price }>￥{Foundation.formatPrice(data['original_price'])}</Text>
    </TouchableOpacity>
  )
}

const styles = StyleSheet.create({
  container: {},
  sk_tools: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    backgroundColor: '#FFF',
    width: Screen.width,
    height: 30,
    paddingVertical: 5,
    paddingHorizontal: 10
  },
  sk_tools_left: {
    flexDirection: 'row',
    alignItems: 'center'
  },
  sk_tools_img: {
    width: 65,
    height: 15,
    marginRight: 5
  },
  sk_tools_more: {
    flexDirection: 'row',
    alignItems: 'center'
  },
  sk_tools_more_text: {
    fontSize: 12,
    color: colors.main
  },
  time_label: {
    color: colors.main,
    fontSize: 11,
    marginTop: 5
  },
  time_container: {
    flexDirection: 'row'
  },
  time_box: {
    borderWidth: Screen.onePixel,
    borderColor: '#ccc',
    borderRadius: 2,
    padding: 3
  },
  sk_goods: {
    width: Screen.width,
    paddingTop: 5,
    paddingBottom: 10
  },
  goods_item: {
    alignItems: 'center',
    width: 100,
    backgroundColor: '#fff'
  },
  goods_img: {
    width: 80,
    height: 80
  },
  sk_price: {
    color: colors.main,
    fontWeight: 'bold',
    fontSize: 12,
    marginTop: 3
  },
  sk_original_price: {
    color: '#686868',
    fontSize: 12,
    textDecorationLine: 'line-through'
  }
})
