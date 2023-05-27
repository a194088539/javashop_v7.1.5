/**
 * Created by Andste on 2019-01-16.
 */
import React, { Component } from 'react'
import {
  View,
  Image,
  Text,
  TouchableOpacity,
  StatusBar,
  StyleSheet
} from 'react-native'
import { connect } from 'react-redux'
import { messageActions } from '../../redux/actions'
import { colors } from '../../../config'
import { Screen } from '../../utils'
import { FlatList } from '../../components'
import { store } from '../../redux/store'
import * as API_promotions from '../../apis/promotions'
import * as API_Members from '../../apis/members'

class CouponScene extends Component {
  static navigationOptions = {
    title: '优惠券市场'
  }
  
  constructor(props) {
    super(props)
    this.params = {
      page_no: 1,
      page_size: 10
    }
    this.state = {
      loading: false,
      noData: false,
      coupons: []
    }
  }
  
  componentDidMount() {
    this._onRefresh()
  }
  
  /**
   * 领取优惠券
   * @param coupon
   * @private
   */
  _receiveCoupon = async (coupon) => {
    const { dispatch } = this.props
    if (!store.getState().user.user) {
      dispatch(messageActions.error('请先登录！'))
    } else {
      await API_Members.receiveCoupons(coupon['coupon_id'])
      dispatch(messageActions.success('领取成功！'))
    }
  }
  
  /**
   * 获取优惠券
   * @returns {Promise<void>}
   * @private
   */
  _getCoupons = async () => {
    const { page_no } = this.params
    try{
      const { data } = await API_promotions.getAllCoupons(this.params)
      this.setState({
        loading: false,
        noData: !data.length,
        coupons: page_no === 1 ? data : this.state.coupons.concat(data)
      })
    }catch(error){
      this.setState({
        loading: false
      })
    }
  }
  
  /**
   * 刷新优惠券
   * @returns {Promise<void>}
   * @private
   */
  _onRefresh = async () => {
    this.params.page_no = 1
    await this.setState({ loading: true })
    this._getCoupons()
  }
  
  /**
   * 到底部加载更多
   * @returns {Promise<void>}
   * @private
   */
  _onEndReadched = async () => {
    const { coupons, noData, loading } = this.state
    if (!coupons[9] || !noData || !loading) return
    this.params.page_no++
    this._getCoupons()
  }
  
  /**
   * 渲染间隔
   * @returns {*}
   * @private
   */
  _renderSeparator = () => (
    <View style={ { width: Screen.width, height: 10, backgroundColor: colors.gray_background } }/>
  )
  
  /**
   *
   * @param sections
   * @param index
   * @returns {{length : number, offset : number, index : *}}
   * @private
   */
  _getItemLayout = (sections, index) => {
    let height = 110
    return { length: height, offset: height * index, index }
  }
  
  /**
   * 渲染优惠券
   * @param item
   * @returns {*}
   * @private
   */
  _renderItem = ({ item }) => (
    <CouponItem
      data={ item }
      onPress={ () => this._receiveCoupon(item) }
    />
  )
  
  render() {
    const { coupons, loading } = this.state
    return (
      <View style={ styles.container }>
        <StatusBar barStyle="dark-content"/>
        <FlatList
          data={ coupons }
          refreshing={ loading }
          renderItem={ this._renderItem }
          onRefresh={ this._onRefresh }
          onEndReached={ this._onEndReadched }
          ListFooterBgColor={ colors.gray_background }
          ItemSeparatorComponent={ this._renderSeparator }
          ListHeaderComponent={ () => (<View style={ styles.list_header }/>) }
          getItemLayout={ this._getItemLayout }
        />
      </View>
    )
  }
}

const CouponItem = ({ data, onPress }) => (
  <View style={ styles.c_item }>
    <View style={ styles.c_left }>
      <Image
        style={ styles.c_img }
        source={ require('../../images/icon-coupon.png') }
      />
      <View style={ styles.c_info }>
        <Text style={ styles.c_info_title }>{ data['title'] }</Text>
        <Text>可用店铺：【{ data['seller_name'] }】</Text>
        <View style={ styles.c_price_view }>
          <Text style={ styles.c_price }>￥{ data['coupon_price'] } </Text>
        </View>
        <View style={ styles.c_price_view }>
          <Text style={ styles.c_threshold }>满￥{ data['coupon_threshold_price'] } 可用</Text>
        </View>
      </View>
    </View>
    { data.create_num === data.received_num && 
      <View style={ styles.c_right_limit }>
        <View style={ styles.c_receive_circle }>
          <Text style={ styles.c_receive_text }>已领完</Text>
        </View>
      </View> || 
      <TouchableOpacity style={ styles.c_right } onPress={ onPress } >
        <View style={ styles.c_receive_circle }>
          <Text style={ styles.c_receive_text }>立即</Text>
          <Text style={ styles.c_receive_text }>领取</Text>
        </View>
      </TouchableOpacity> }

    <View style={ [styles.place, { top: -5 }] }/>
    <View style={ [styles.place, { bottom: -5 }] }/>
  </View>
)

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: colors.gray_background,
    borderRadius: 10
  },
  list_header: {
    width: Screen.width,
    height: 10,
    backgroundColor: colors.gray_background
  },
  c_item: {
    position: 'relative',
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    width: Screen.width - 20,
    height: 100,
    backgroundColor: '#FFF',
    paddingLeft: 10,
    marginLeft: 10
  },
  c_img: {
    width: 75,
    height: 75
  },
  c_left: {
    flexDirection: 'row',
    width: Screen.width - 20 - 75 - 10
  },
  c_info: {
    width: Screen.width - 20 - 75 -75 - 10,
    justifyContent: 'space-between'
  },
  c_price_view: {
    flexDirection: 'row',
    alignItems: 'flex-end'
  },
  c_price: {
    fontSize: 22,
    color: colors.main
  },
  c_threshold: {
    fontSize: 12,
    color: colors.text,
    marginBottom: 2
  },
  c_right: {
    justifyContent: 'center',
    alignItems: 'center',
    width: 75,
    height: 100,
    backgroundColor: colors.main,
    borderTopRightRadius: 5,
    borderBottomRightRadius: 5
  },
  c_right_limit: {
    justifyContent: 'center',
    alignItems: 'center',
    width: 75,
    height: 100,
    backgroundColor: '#ccc',
    borderTopRightRadius: 5,
    borderBottomRightRadius: 5
  },
  c_receive_circle: {
    width: 50,
    height: 50,
    borderWidth: 2,
    borderColor: '#FFF',
    borderRadius: 50,
    justifyContent: 'center',
    alignItems: 'center'
  },
  c_receive_text: {
    color: '#FFF'
  },
  place: {
    width: 12,
    height: 12,
    right: 69,
    backgroundColor: colors.gray_background,
    borderRadius: 12,
    position: 'absolute'
  }
})

export default connect()(CouponScene)
