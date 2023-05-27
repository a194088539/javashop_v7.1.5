/**
 * Created by Andste on 2018/9/30.
 */
import React, { PureComponent } from 'react'
import {
  View,
  Image,
  Text,
  TouchableOpacity,
  StyleSheet
} from 'react-native'
import { navigate } from '../../navigator/NavigationService'
import { colors } from '../../../config'
import { Screen } from '../../utils'

export default class Menus extends PureComponent {
  // 其它URL需要根据业务自行适配，例如：个人中心、订单等等。。。
  _onPress = (item) => {
    // console.log(item)
    const { url } = item
    if (/\/points-mall/.test(url)) {
      // 积分商城
      navigate('PointMall')
    } else if (/\/coupons/.test(url)) {
      // 领券
      navigate('Coupons')
    } else if (/\/group-buy/.test(url)) {
      // 团购
      navigate('GroupBuy')
    } else if (/\/goods\/\d/.test(url)) {
      // 具体某个商品
      const inputs = url.match(/\/goods\/(\d+)/)
      if (inputs && inputs[1]) {
        navigate('Goods', { id: inputs[1] })
      }
    } else {
      navigate('SingIn')
    }
  }
  
  menuItem = (item) => {
    return (
      <TouchableOpacity
        key={ item['navigation_id'] }
        style={ styles.item }
        onPress={ () => { this._onPress(item) } }
      >
        <Image source={ { uri: item['image'] } } style={ styles.item_img }/>
        <Text style={ styles.item_text }>{ item['navigation_name'] }</Text>
      </TouchableOpacity>
    )
  }
  
  render() {
    const { data } = this.props
    if (!data || !data.length) return <View style={ styles.container }/>
    return (
      <View style={ styles.container }>
        { data.map(this.menuItem) }
      </View>
    )
  }
}

const item_width = (Screen.width - 20) / 5
const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'row',
    justifyContent: 'space-around',
    flexWrap: 'wrap',
    backgroundColor: '#ffffff',
    paddingHorizontal: 10,
    paddingVertical: 10
  },
  item: {
    justifyContent: 'space-around',
    alignItems: 'center',
    width: item_width,
    height: item_width
  },
  item_img: {
    width: item_width * 0.6,
    height: item_width * 0.6
  },
  item_text: {
    fontSize: 12,
    color: colors.menu_item_text
  }
})
