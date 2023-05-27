/**
 * Created by Andste on 2019-01-15.
 */
import React, { PureComponent } from 'react'
import {
  View,
  SectionList,
  FlatList,
  Text,
  TouchableOpacity,
  DeviceEventEmitter,
  Image,
  StyleSheet
} from 'react-native'
import { connect } from 'react-redux'
import { isIphoneX } from 'react-native-iphone-x-helper'
import { colors } from '../../../config'
import { Screen, Foundation } from '../../utils'
import { F16Text } from '../../widgets/Text'
import { DataEmpty } from '../../components/EmptyViews'
import { messageActions } from '../../redux/actions'
import * as API_Promotions from '../../apis/promotions'
import * as API_Goods from '../../apis/goods'
import * as API_Members from '../../apis/members'
import GoodsItem from './ShopTabGoodsItem'
import { ScrollView } from 'react-native-gesture-handler'

class ShopTabHome extends PureComponent {
  
  constructor(props) {
    super(props)
    this.state = {
      tagGoods: '',
      coupons: []
    }
  }
  
  async componentDidMount() {
    const shop_id = this.props['shopId']
    const values = await Promise.all([
      API_Goods.getTagGoods(shop_id, 'new', 10),
      API_Goods.getTagGoods(shop_id, 'hot', 10),
      API_Goods.getTagGoods(shop_id, 'recommend', 10),
      API_Promotions.getShopCoupons(shop_id)
    ])
    this.setState({
      tagGoods: [
        { title: '上新', key: 'new', data: values[0] },
        { title: '热卖', key: 'hot', data: values[1] },
        { title: '推荐', key: 'rec', data: values[2] }
      ],
      coupons: values[3]
    })
  }

  componentWillUnmount() {
    DeviceEventEmitter.emit('updateInventories', '领取成功！')
  }

    /**
   * 领取优惠券
   * @param coupon
   * @returns {Promise<void>}
   * @private
   */
  _receiveCoupon = async (coupon) => {
    await API_Members.receiveCoupons(coupon['coupon_id'])
    this.props.dispatch(messageActions.success('领取成功！'))
  }
  
  _renderItem = ({ item }) => {
    item['name'] = item['goods_name']
    return (<GoodsItem data={ item }/>)
  }

  _renderCouponItem = ({ item }) => {
    return (<CouponItem data={ item } onPress={ () => { this._receiveCoupon(item)} }/>)
  }
  
  _renderSectionHeader = ({ section }) => (
    <View style={ styles.section_header }>
      <F16Text color={ colors.main }>{ section['title'] }</F16Text>
    </View>
  )
  
  _ListFooterComponent = () => (
    <View style={ { height: isIphoneX() ? 30 : 0 } }/>
  )
  
  render() {
    const { tagGoods } = this.state
    if (!tagGoods) return <View/>
    return (
      <ScrollView>
        <View>
          <FlatList
            data={ this.state.coupons }
            renderItem={ this._renderCouponItem }
            ListFooterBgColor={ colors.gray_background }
          />
        </View>
        <View>
          <SectionList
            sections={ tagGoods }
            renderItem={ this._renderItem }
            renderSectionHeader={ this._renderSectionHeader }
            ListFooterComponent={ this._ListFooterComponent }
            ListEmptyComponent={ <DataEmpty/> }
            onScroll={ this.props.event }
            scrollEventThrottle={ 16 }
          />
        </View>
      </ScrollView>
    )
  }
}

const CouponItem = ({ data, ...props }) => (
  <TouchableOpacity style={ styles.coupon_item } { ...props }>
    <Image
      style={ styles.coupon_item_line }
      source={ data.create_num === data.received_num ? require('../../images/icon-coupon-top-line-unable.png') : require('../../images/icon-coupon-top-line.png') }
    />
    <View style={ styles.coupon_body }>
      <Text style={ styles.coupon_price_text }>
        ￥{ data['coupon_price'] }元优惠券
      </Text>
      <Text style={ styles.coupon_use_text }>
        满{ data['coupon_threshold_price'] }可用 -【{ data['seller_name'] }】
      </Text>
      <Text style={ styles.coupon_use_text }>
        使用有效期截止：{ Foundation.unixToDate(data['end_time'], 'yyyy-MM-dd') }
      </Text>
    </View>
  </TouchableOpacity>
)

const styles = StyleSheet.create({
  coupon_item: {
    marginTop: 10,
    width: Screen.width - 30,
    borderWidth: 1,
    borderColor: '#ccc',
    borderRadius: 2,
    marginBottom: 10,
    marginLeft: 15
  },
  coupon_item_line: {
    width: Screen.width - 30,
    height: 15
  },
  coupon_body: {
    paddingBottom: 10,
    backgroundColor: '#FFF',
    paddingHorizontal: 10
  },
  coupon_price_text: {
    marginTop: 5,
    fontSize: 20,
    color: colors.main,
  },
  coupon_use_text: {
    marginTop: 3,
    color: '#686868'
  },
  section_header: {
    justifyContent: 'center',
    alignItems: 'center',
    height: 35,
    backgroundColor: '#FFF'
  },
  item_separator: {
    width: Screen.width,
    height: 1,
    backgroundColor: colors.gray_background
  }
})

export default connect()(ShopTabHome)