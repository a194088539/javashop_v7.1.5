/**
 * Created by Andste on 2018/11/20.
 */
import React, { Component } from 'react'
import {
  View,
  Text,
  Image,
  StatusBar,
  ScrollView,
  DeviceEventEmitter,
  StyleSheet
} from 'react-native'
import { connect } from 'react-redux'
import { cartActions, messageActions } from '../../redux/actions'
import { colors } from '../../../config'
import { replace } from '../../navigator/NavigationService'
import { Screen, Foundation } from '../../utils'
import { isIphoneX } from 'react-native-iphone-x-helper'
import { Modal } from '../../components'
import { BigButton, CellGroup, Cell, Price } from '../../widgets'
import { BaseText } from '../../widgets/Text'
import * as API_Trade from '../../apis/trade'
import * as API_Address from '../../apis/address'
import AddressCell from './CheckoutAddressCell'
import Inventories from './CheckoutInventories'

class CheckoutScene extends Component {
  static navigationOptions = {
    title: '填写订单'
  }
  
  constructor(props) {
    super(props)
    this.way = this.props.navigation.state.params.way
    this.state = {
      // 已选收货地址
      address: '',
      // 购物清单
      inventories: '',
      // 支付类型
      payment: 'ONLINE',
      // 配送时间
      ship_time: '任意时间',
      // 发票信息
      receipt: '',
      // 可用优惠券数量
      coupon_num: 0,
      // 订单价格总计
      totalPrice: '',
      // 订单备注
      remark: '',
      // 错误数据
      errorData: [],
      // 错误消息标题
      errorTitle: '',
      // 展示错误数据
      showError: false
    }
  }
  
  async componentDidMount() {
    const params = await API_Trade.getCheckoutParams()
    let address = ''
    if (params['address_id']) {
      address = await API_Address.getAddressDetail(params['address_id'])
    }
    this.setState({
      address,
      payment: params['payment_type'],
      ship_time: params['receive_time'],
      receipt: params['receipt'],
      remark: params['remark']
    })
    this._getInventories()
    this.listener=DeviceEventEmitter.addListener('updateInventories',this._getInventories)
  }

  componentWillUnmount(){
    this.listener.remove()
  }
  
  _refreshCoupon = async () => {
    this._getInventories()
  }

  /**
   * 获取购物清单
   * @returns {Promise<void>}
   * @private
   */
  _getInventories = async () => {
    const res = await API_Trade.getCarts('checked', this.way)
    const inventories = res['cart_list']
    const totalPrice = res['total_price']
    let coupon_num = 0
    inventories.forEach(shop => { 
      shop['coupon_list'].forEach(coupon => {
        if(coupon.enable === 1) { 
          coupon_num ++
        }
      })
    })
    await this.setState({
      inventories,
      coupon_num,
      totalPrice
    })
  }

  _refreshInventories = async () => {
    const res = await API_Trade.getCarts('checked', this.way)
    const inventories = res['cart_list']
    inventories.forEach(shop => { 
      shop['coupon_list'].forEach(coupon => {
        if(coupon.enable === 1) { 
          coupon_num ++
        }
      })
    })
    return inventories
  }
  
  /**
   * 设置收货地址ID
   * @param address
   * @returns {Promise<void>}
   * @private
   */
  _setAddress = async (address) => {
    await API_Trade.setAddressId(address['addr_id'])
    this.setState({ address })
    // 设置收货地址后，重新获取购物清单
    await this._getInventories()
  }
  
  /**
   * 去选择收货地址
   * @private
   */
  _onSelectAddress = () => {
    this.props.navigation.navigate('MyAddress', {
      callback: this._setAddress
    })
  }
  
  /**
   * 设置支付类型，配送时间
   * @param item
   * @returns {Promise<void>}
   * @private
   */
  _setPayTime = async (item) => {
    const { payment, ship_time } = this.state
    if (item.payment !== payment) {
      await API_Trade.setPaymentType(item.payment)
    }
    if (item.ship_time !== ship_time) {
      await API_Trade.setReceiveTime(item.ship_time)
    }
    this.setState(item)
  }

  /**
   * 设置支付类型，配送时间
   * @param item
   * @returns {Promise<void>}
   * @private
   */
  _setReceipt = (receipt) => {
    this.setState({ receipt: receipt })
  }

  /**
   * 去选择支付方式和送货时间
   * @private
   */
  _onSelectPayTime = () => {
    const { payment, ship_time } = this.state
    this.props.navigation.navigate('CheckoutPayTime', {
      payment,
      ship_time,
      callback: this._setPayTime
    })
  }
  
  /**
   * 去设置发票信息
   * @private
   */
  _onSetReceipt = () => {
    const { receipt, inventories } = this.state
    this.props.navigation.navigate('CheckoutReceipt', {
      inventories,
      receipt,
      callback: this._setReceipt
    })
  }
  
  /**
   * 使用优惠券
   * @private
   */
  _useCoupon = async (item) => {
    const { seller_id, selected, member_coupon_id } = item
    if (selected === 1) {
      await API_Trade.useCoupon(seller_id, 0, this.way)
    } else {
      await API_Trade.useCoupon(seller_id, member_coupon_id, this.way)
    }
    this._getInventories()
  }
  
  /**
   * 去选择优惠券
   * @private
   */
  _onSelectCoupon = () => {
    this.props.navigation.navigate('CheckoutCoupon', {
      shopList: this.state.inventories,
      getShopList: this._refreshInventories,
      callback: this._useCoupon
    })
  }
  
  /**
   * 设置订单备注
   * @param remark
   * @returns {Promise<void>}
   * @private
   */
  _setRemark = async (remark) => {
    await API_Trade.setRemark(remark)
    this.setState({ remark })
    return true
  }
  
  /**
   * 去输入订单备注
   * @private
   */
  _onInputRemark = () => {
    this.props.navigation.navigate('Input', {
      title: '订单备注',
      defaultValue: this.state.remark,
      inputProps: {
        placeholder: '请填写订单备注，不超过30个字。',
        maxLength: 30
      },
      confirm: this._setRemark
    })
  }
  
  /**
   * 关闭错误消息模态框
   * @private
   */
  _onErrorModalClose = () => {
    this.setState({ showError: false })
  }
  
  /**
   * 提交订单
   * @private
   */
  _onSubmitOrder = async () => {
    const { dispatch } = this.props
    const { address } = this.state
    if (!address) {
      dispatch(messageActions.error('请选择一个收货地址！'))
      return
    }
    try {
      const res = await API_Trade.createTrade('APP',this.way)
      await dispatch(cartActions.getCartDataAction())
      const { trade_sn } = res
      replace('Cashier', { trade_sn, fromScene: 'Checkout' } )
    } catch (e) {
      const { dispatch } = this.props
      const { data } = e.response || {}
      if (data.data) {
        const { data: list } = data
        if (!list || !list[0]) {
          dispatch(messageActions.error(data.message))
          return
        }
        this.setState({
          errorData: typeof list === 'string'
            ? JSON.parse(list)
            : list,
          errorTitle: data.message,
          showError: true
        })
      } else {
        dispatch(messageActions.error(data.message))
      }
    }
  }

  _receiptType = (type) => {
    switch (type) {
      case 'VATORDINARY' : return '增值税普通发票'
      case 'ELECTRO' : return '电子普通发票'
      case 'VATOSPECIAL' : return '增值税专用发票'
      default: return '不开发票'
    }
  }
  
  render() {
    const { state } = this
    const { address, inventories, payment, ship_time, receipt, coupon_num, totalPrice, remark  } = this.state
    return (
      <View style={ styles.container }>
        <StatusBar barStyle="dark-content"/>
        <ScrollView>
          <AddressCell data={ address } onPress={ this._onSelectAddress }/>
          { inventories.length ? (
            <Inventories data={ inventories }/>
          ) : undefined }
          <CellGroup marginTop>
            <Cell
              title="支付配送"
              onPress={ this._onSelectPayTime }
              label={ (
                <View>
                  <Text>{ payment === 'ONLINE' ? '在线支付' : '货到付款' }</Text>
                  <Text style={ styles.label_text }>{ ship_time }</Text>
                </View>
              ) }
            />
            <Cell
              title="发票信息"
              onPress={ this._onSetReceipt }
              label={ (
                <Text>{ (receipt && receipt['receipt_title'] && ( this._receiptType(receipt['receipt_type'] ) +'-'+ receipt['receipt_title'] +'-'+ receipt['receipt_content'] ) ) || '不开发票' }</Text>
              ) }
            />
          </CellGroup>
          <CellGroup marginTop>
            <Cell
              title={ (
                <View style={ styles.coupon_cell_title }>
                  <BaseText>优惠券</BaseText>
                  <Text style={ styles.coupon_cell_can_use }>{ coupon_num }张可用</Text>
                </View>
              ) }
              onPress={ this._onSelectCoupon }
              label={ totalPrice['coupon_price'] ? (
                <Text style={ styles.coupon_cell_price }>
                  -￥{ Foundation.formatPrice(totalPrice['coupon_price']) }
                </Text>
              ) : <Text>未使用</Text> }
            />
            <Cell
              title="备注信息"
              onPress={ this._onInputRemark }
              label={ (
                <Text numberOfLines={ 2 }>{ remark || '未填写' }</Text>
              ) }
            />
          </CellGroup>
          <CellGroup marginTop>
            <PriceCell title="商品金额" value={ totalPrice['original_price'] }/>
            { totalPrice['coupon_price'] ? (
              <PriceCell title="优惠券抵扣" value={ totalPrice['coupon_price'] } negative/>
            ) : undefined }
            { totalPrice['cash_back'] ? (
              <PriceCell title="返现金额" value={ totalPrice['cash_back'] } negative/>
            ) : undefined }
            { totalPrice['exchange_point'] ? (
              <PriceCell title="积分抵扣" value={ `-${totalPrice['exchange_point']}积分` } tips='温馨提示：订单取消、申请售后积分不退还' />
            ) : undefined }
            <PriceCell title="运费" value={ totalPrice['freight_price'] }/>
          </CellGroup>
        </ScrollView>
        <View style={ styles.bottom_bar }>
          <View style={ styles.bottom_bar_left }>
            <Text>合计：</Text>
            <Price advanced price={ totalPrice['total_price'] }/>
          </View>
          <BigButton
            style={ styles.bottom_bar_btn }
            title="提交订单"
            disabled={ !inventories.length }
            onPress={ this._onSubmitOrder }
          />
        </View>
        <Modal
          style={ { height: Screen.height - 500 }}
          title={ state.errorTitle }
          isOpen={ state.showError }
          onClosed={ this._onErrorModalClose }
        >
          <ScrollView>
            { state.errorData.map((item, index) => (
              <View style={ styles.error_item } key={ index }>
                <Image
                  style={ styles.error_item_image }
                  source={ { uri: item['image']} }
                />
                <Text style={ styles.error_item_text } numberOfLines={ 2 }>{ item['name'] }</Text>
              </View>
            ))}
          </ScrollView>
        </Modal>
      </View>
    )
  }
}

const PriceCell = ({ title, value, negative, tips = '' }) => {
  const is_price = typeof value !== 'string'
  return (
    <View>
      <View style={ styles.price_cell }>
        <Text style={ styles.price_cell_title }>{ title }</Text>
        { is_price ? (
          <Text style={ styles.price_cell_vaule }>
            { negative && '-' }
            ￥{ Foundation.formatPrice(value) }
          </Text>
        ) : (
          <Text style={ styles.price_cell_vaule }>{value}</Text>
        ) }
      </View>
      { !!tips && <View style={ styles.price_cell_tips }><Text style={ { color: colors.main } }>{tips}</Text></View> }
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1
  },
  label_text: {
    marginTop: 7
  },
  coupon_cell_title: {
    flexDirection: 'row',
    alignItems: 'center'
  },
  coupon_cell_can_use: {
    backgroundColor: colors.main,
    color: '#FFF',
    marginLeft: 3,
    padding: 2
  },
  coupon_cell_price: {
    color: colors.main
  },
  price_cell: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    height: 35,
    backgroundColor: '#FFF',
    paddingHorizontal: 10
  },
  price_cell_title: {
    fontSize: 18
  },
  price_cell_vaule: {
    color: colors.main
  },
  price_cell_tips: {
    flex: 1,
    flexDirection: 'row',
    justifyContent: 'flex-end',
    marginRight: 10
  },
  bottom_bar: {
    flexDirection: 'row',
    height: 50,
    marginBottom: isIphoneX() ? 30 : 0,
    backgroundColor: '#FFF'
  },
  bottom_bar_left: {
    flexDirection: 'row',
    justifyContent: 'flex-end',
    alignItems: 'center',
    width: Screen.width - 120,
    paddingRight: 10
  },
  bottom_bar_btn: {
    width: 120
  },
  error_item: {
    flexDirection: 'row',
    alignItems: 'center',
    width: Screen.width,
    paddingHorizontal: 10,
    height: 50,
    marginBottom: 10
  },
  error_item_image: {
    width: 50,
    height: 50
  },
  error_item_text: {
    marginLeft: 10,
    fontSize: 16,
    color: colors.main
  }
})

export default connect()(CheckoutScene)
