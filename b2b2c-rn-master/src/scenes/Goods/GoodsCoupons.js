/**
 * Created by Andste on 2018-12-11.
 */
import React, { PureComponent } from 'react'
import {
  View,
  Image,
  Text,
  TouchableOpacity,
  ScrollView,
  StyleSheet
} from 'react-native'
import { connect } from 'react-redux'
import { messageActions } from '../../redux/actions'
import { colors } from '../../../config'
import { Screen, Foundation } from '../../utils'
import { Modal } from '../../components'
import ItemCell from './GoodsItemCell'
import * as API_Members from '../../apis/members'

class GoodsCoupons extends PureComponent {
  constructor(props) {
    super(props)
    this.state = {
      showModal: false
    }
  }
  
  _showModal = () => {
    this.setState({ showModal: true })
  }
  
  _onModalClosed = () => {
    this.setState({ showModal: false })
  }
  
  /**
   * 领取优惠券
   * @param item
   * @returns {Promise<void>}
   * @private
   */
  _receiveCoupon = async (item) => {
    const { user, dispatch } = this.props
    await this.setState({ showModal: false })
    if (! user) {
      dispatch(messageActions.error('未登录，不能领取优惠券！'))
      return
    }
    await API_Members.receiveCoupons(item['coupon_id'])
    this.props.dispatch(messageActions.success('领取成功！'))
  }
  
  render() {
    const { data } = this.props
    const { showModal } = this.state
    return (
      <ItemCell
        label="领券"
        icon
        style={ { marginTop: 10 } }
        onPress={ this._showModal }
        content={
          <View>
            <View style={ styles.coupon_cell }>
              { data.slice(0, 2).map((item, index) => (
                <Text
                  key={ index }
                  style={ styles.coupon_tag }
                >
                  满{ item['coupon_threshold_price'] }减{ item['coupon_price'] }
                </Text>
              )) }
            </View>
            <Modal
              title="优惠券"
              style={ styles.modal_style }
              isOpen={ showModal }
              onClosed={ this._onModalClosed }
            >
              <ScrollView
                style={ styles.coupon_scroll }
                showsHorizontalScrollIndicator={ false }
                showsVerticalScrollIndicator={ false }
              >
                { data.map((item, index) => (
                  <CouponItem
                    data={ item }
                    key={ index }
                    onPress={ () => this._receiveCoupon(item) }
                  />)) }
              </ScrollView>
            </Modal>
          </View>
        }
      />
    )
  }
}

const CouponItem = ({ onPress, data }) => {
  return (
    <TouchableOpacity
      style={ styles.coupon_item }
      activeOpacity={ 1 }
      onPress={ onPress }
    >
      <View style={ styles.coupon_left }>
        <Text
          style={ styles.coupon_money_text }
          adjustsFontSizeToFit={ data['coupon_price'] > 9999 }
          numberOfLines={ 1 }
        >
          <Text style={ styles.coupon_symbol }>￥</Text>
          <Text style={ styles.coupon_money }>{ data['coupon_price'] }</Text>
        </Text>
        <Text style={ styles.coupon_min }>满{ data['coupon_threshold_price'] }可用</Text>
      </View>
      <View style={ styles.coupon_right }>
        <View style={ styles.coupon_right_view }>
          <Text style={ styles.coupon_right_view_text } numberOfLines={ 2 }>{ data['title'] } -
            仅限[{ data['seller_name'] }]店铺可用</Text>
        </View>
        <View style={ [styles.coupon_right_view, styles.coupon_right_time] }>
          <Text
            style={ [styles.coupon_right_view_text, { fontSize: 12, width: 100 }] }
          >
            { Foundation.unixToDate(data['start_time'], 'yyyy-MM-dd') } 至
            { Foundation.unixToDate(data['end_time'], 'yyyy-MM-dd') }
          </Text>
          { ! data['received'] && <View style={ styles.coupon_receive }>
            <Text style={ styles.coupon_receive_text }>点击领取</Text>
          </View> }
        </View>
        { data['received'] === 1
          ? (<Image
            style={ styles.coupon_received_img }
            source={ require('../../images/icon-received.png')
            }/>)
          : undefined }
      </View>
    </TouchableOpacity>
  )
}

const COUPON_WIDTH = Screen.width - 20

const styles = StyleSheet.create({
  coupon_cell: {
    flexDirection: 'row'
  },
  coupon_tag: {
    backgroundColor: colors.main,
    color: '#FFFFFF',
    marginRight: 10,
    paddingHorizontal: 2,
    paddingVertical: 2
  },
  modal_style: {
    height: Screen.height - 200,
    backgroundColor: colors.gray_background
  },
  margin_vert: {},
  coupon_scroll: {
    paddingHorizontal: 10
  },
  coupon_item: {
    flex: 1,
    flexDirection: 'row',
    width: COUPON_WIDTH,
    height: 100,
    marginBottom: 10,
    shadowOffset: {
      width: 0,
      height: 2
    },
    shadowColor: '#CCCCCC',
    shadowOpacity: .75,
    shadowRadius: 3,
    backgroundColor:'#FFF'
  },
  coupon_left: {
    width: 120,
    height: 100,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: colors.main
  },
  coupon_money_text: {
    color: '#FFFFFF',
    fontWeight: '600'
  },
  coupon_symbol: {
    fontSize: 16
  },
  coupon_money: {
    fontSize: 35
  },
  coupon_min: {
    color: '#FFFFFF',
    fontSize: 12
  },
  coupon_right: {
    width: COUPON_WIDTH - 120,
    height: 100,
    paddingLeft: 10,
    paddingRight: 10,
    backgroundColor: '#FFFFFF'
  },
  coupon_right_view: {
    height: 50,
    justifyContent: 'center',
    alignItems: 'flex-start'
  },
  coupon_right_view_text: {
    color: '#777777'
  },
  coupon_right_time: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    flexWrap: 'wrap',
    alignItems: 'center'
  },
  coupon_receive: {
    width: 60,
    height: 22,
    justifyContent: 'center',
    alignItems: 'center',
    borderColor: colors.main,
    borderWidth: 1,
    borderRadius: 22
  },
  coupon_receive_text: {
    fontSize: 12,
    color: colors.main
  },
  coupon_received_img: {
    position: 'absolute',
    right: - 15,
    top: - 15,
    width: 60,
    height: 60
  }
})

export default connect(state => ({ user: state.user.user }))(GoodsCoupons)
