/**
 * Created by Andste on 2019-01-23.
 */
import React, { Component } from 'react'
import {
  View,
  Image,
  Text,
  TouchableOpacity,
  StyleSheet
} from 'react-native'
import { connect } from 'react-redux'
import { messageActions } from '../../redux/actions'
import { FlatList } from '../../components'
import { colors } from '../../../config'
import { Screen, Foundation } from '../../utils'
import { Price } from '../../widgets'
import { BonusEmpty } from '../../components/EmptyViews'
import * as API_Promotions from '../../apis/promotions'
import * as API_Members from '../../apis/members'

class CartCouponScene extends Component {
  static navigationOptions = {
    title: '店铺优惠券'
  }
  
  constructor(props) {
    super(props)
    this.shop_id = props.navigation.state.params.shop_id
    this.state = {
      coupons: []
    }
  }
  
  async componentDidMount() {
    const { shop_id } = this
    const res = await API_Promotions.getShopCoupons(shop_id)
    this.setState({ coupons: res })
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
  
  /**
   * 渲染优惠券项
   * @param item
   * @returns {*}
   * @private
   */
  _renderItem = ({ item }) => {
    return (<CouponItem data={ item } onPress={ () => { this._receiveCoupon(item)} }/>)
  }
  
  render() {
    const { coupons } = this.state
    return (
      <View style={ styles.container }>
        <FlatList
          data={ coupons }
          renderItem={ this._renderItem }
          ListEmptyComponent={ <BonusEmpty text="暂无优惠券"/> }
          ListFooterBgColor={ colors.gray_background }
        />
      </View>
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
      <Price
        style={ styles.conpon_price }
        price={ data['coupon_price'] }
        advanced
        scale={ 1.7 }
      />
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
  container: {
    flex: 1
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
  coupon_body: {
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

export default connect()(CartCouponScene)
