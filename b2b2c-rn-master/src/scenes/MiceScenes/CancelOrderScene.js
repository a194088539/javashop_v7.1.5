/**
 * Created by Andste on 2018/11/6.
 */
import React, { Component } from 'react'
import {
  FlatList,
  Image,
  StyleSheet,
  TextInput,
  TouchableOpacity,
  View
} from 'react-native'
import { connect } from 'react-redux'
import { navigate } from '../../navigator/NavigationService'
import { messageActions } from '../../redux/actions'
import { KeyboardAwareScrollView } from 'react-native-keyboard-aware-scroll-view'
import { isIphoneX } from 'react-native-iphone-x-helper'
import { colors } from '../../../config'
import { Screen } from '../../utils'
import { BigButton, Price, Loading, SkuSpec } from '../../widgets'
import { F16Text, F14Text } from '../../widgets/Text'
import * as API_Order from '../../apis/order'

class CancelOrderScene extends Component {
  static navigationOptions = {
    title: '取消订单'
  }
  
  constructor(props) {
    super(props)
    const { params } = this.props.navigation.state
    this.order = params.data
    this.state = {
      loading: false,
      reason: ''
    }
  }
  
  _renderItem = ({ item }) => (<GoodsItem data={ item }/>)
  _ItemSeparatorComponent = () => (<View style={ styles.goods_line }/>)
  _onChangeText = (text) => this.setState({ reason: text })
  _apply = async () => {
    const { dispatch, navigation } = this.props
    const { reason } = this.state
    if (!reason) {
      dispatch(messageActions.error('取消原因不能为空！'))
      return
    }
    try{
      await this.setState({ loading: true })
      await API_Order.cancelOrder(this.order['sn'], reason)
      await this.setState({ loading: false })
    }catch(error){
      await this.setState({ loading: false })
    }
    dispatch(messageActions.success('操作成功！'))
    navigation.state.params.callback()
    navigation.goBack()
  }
  
  render() {
    const { order } = this
    const { loading, reason } = this.state
    return (
      <View style={ styles.container }>
        <KeyboardAwareScrollView keyboardOpeningTime={ 0 }>
          <View style={ styles.order_sn }>
            <F16Text>订单号：{ order['sn'] }</F16Text>
          </View>
          <View style={ styles.items }>
            <FlatList
              data={ order['sku_list'] }
              renderItem={ this._renderItem }
              ItemSeparatorComponent={ this._ItemSeparatorComponent }
              keyExtractor={ (_, index) => String(index) }
            />
          </View>
          <View style={ styles.reason_view }>
            <F14Text>取消原因</F14Text>
            <TextInput
              style={ styles.reason_input }
              value={ reason }
              maxLength={ 60 }
              autoFocus={ true }
              multiline={ true }
              onChangeText={ this._onChangeText }
              placeholder="请输入您取消订单的原因。"
            />
          </View>
        </KeyboardAwareScrollView>
        <BigButton style={ styles.confirm_btn } title="提交申请" onPress={ this._apply }/>
        <Loading show={ loading }/>
      </View>
    )
  }
  
}

const GoodsItem = ({ data }) => {
  return (
    <TouchableOpacity
    style={ styles.goods_item }
    activeOpacity={ 1 }
    onPress={ () => navigate('Goods', { id: data.goods_id }) }
  >
    <Image style={ styles.goods_item_image } source={ { uri: data['goods_image'] } }/>
    <View style={ styles.goods_item_info }>
      <F16Text numberOfLines={ 1 }>{ data['name'] }</F16Text>
      <F14Text numberOfLines={ 2 }>
        <F14Text style={ styles.goods_item_num }>数量：{ data['num'] }</F14Text>
      </F14Text>
      <SkuSpec data={ data }/>
      <View style={ { flexDirection: 'row', alignItems: 'center' } }>
        <Price advanced price={ data['purchase_price'] }/>
      </View>
    </View>
  </TouchableOpacity>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1
  },
  order_sn: {
    width: Screen.width,
    height: 40,
    justifyContent: 'center',
    backgroundColor: '#FFFFFF',
    paddingHorizontal: 10
  },
  items: {
    marginTop: 10
  },
  goods_item: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingHorizontal: 10,
    paddingVertical: 10,
    width: Screen.width,
    height: 90,
    backgroundColor: '#FFFFFF'
  },
  goods_item_image: {
    width: 70,
    height: 70,
    borderColor: colors.cell_line_backgroud,
    borderWidth: Screen.onePixel
  },
  goods_item_info: {
    justifyContent: 'space-between',
    width: Screen.width - 20 - 70 - 10,
    height: 70
  },
  goods_item_num: {
    marginTop: 5,
    color: '#a5a5a5'
  },
  goods_line: {
    position: 'absolute',
    bottom: 0,
    left: 0,
    width: Screen.width,
    height: Screen.onePixel,
    backgroundColor: colors.cell_line_backgroud
  },
  reason_view: {
    marginTop: 10,
    paddingHorizontal: 10,
    paddingVertical: 10,
    backgroundColor: '#FFFFFF',
    marginBottom: 50
  },
  reason_input: {
    marginTop: 10,
    width: Screen.width - 20,
    height: 50,
    paddingLeft: 5,
    paddingRight: 5,
    borderColor: colors.cell_line_backgroud,
    borderWidth: Screen.onePixel
  },
  confirm_btn: {
    position: 'absolute',
    bottom: isIphoneX() ? 30 : 0,
    left: 0,
    width: Screen.width,
    height: 45
  }
})

export default connect()(CancelOrderScene)
