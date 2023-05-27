/**
 * Created by Andste on 2018/10/21.
 */
import React, { PureComponent } from 'react'
import {
  Alert,
  View,
  StyleSheet
} from 'react-native'
import { connect } from 'react-redux'
import { messageActions } from '../../redux/actions'
import { FlatList } from '../../components'
import { OrderEmpty } from '../../components/EmptyViews'
import * as API_Order from '../../apis/order'
import OrderItem from './MyOrderItem'

class MyOrderFlatList extends PureComponent {
  constructor(props) {
    super(props)
    this.params = {
      page_no: 1,
      page_size: 10,
      order_status: this.props.status || 'ALL'
    }
    this.state = {
      loading: false,
      orderList: [],
      noData: false
    }
  }
  
  componentDidMount() {
    this._onRefresh()
  }
  
  /**
   * 获取订单列表数据
   * @returns {Promise<void>}
   * @private
   */
  _getOrderData = async () => {
    const { page_no } = this.params
    const { orderList } = this.state
    try{
      const res = await API_Order.getOrderList(this.params) || {}
      const { data = [] } = res
      await this.setState({
        loading: false,
        noData: !data[0],
        orderList: page_no === 1 ? data : orderList.concat(data)
      })
    }catch(error){
      await this.setState({
        loading: false
      })
    }
  }
  
  /**
   * 刷新列表
   * @returns {Promise<void>}
   * @private
   */
  _onRefresh = async () => {
    this.params.page_no = 1
    await this.setState({ loading: true })
    this._getOrderData()
  }
  
  /**
   * 列表滚动到底部触发
   * @private
   */
  _onEndReached = () => {
    let { loading, noData, orderList } = this.state
    if (loading || noData || !orderList[9]) return
    this.params.page_no++
    this._getOrderData()
  }
  
  _getItemLayout = (data, index) => ({ length: 205, offset: 205 * index, index })
  
  _itemSeparatorComponent = () => (<View style={ styles.separator }/>)
  
  /**
   * 确认收货
   * @param item
   * @returns {Promise<void>}
   * @private
   */
  _confirmShip = async (item) => {
    const { dispatch } = this.props
    Alert.alert('提示', '请确定您已收到货物，否则可能钱财两空！', [
      { text: '取消', onPress: () => {} },
      {
        text: '确定', onPress: async () => {
          await API_Order.confirmReceipt(item['sn'])
          await dispatch(messageActions.success('确认收货成功！'))
          this._onRefresh()
        }
      }
    ])
  }
  
  /**
   * 渲染Item
   * @param item
   * @returns {*}
   * @private
   */
  _renderItem = ({ item }) => {
    return (
      <OrderItem
        data={ item }
        onRefresh={ this._onRefresh }
        confirmShip={ this._confirmShip }
      />
    )
  }
  
  render() {
    const { orderList, loading } = this.state
    return (
      <View style={ { flex: 1 } }>
        <FlatList
          data={ orderList }
          renderItem={ this._renderItem }
          ListHeaderComponent={ <View style={ { height: 10 } }/> }
          ListEmptyComponent={ <OrderEmpty/> }
          ListFooterBgColor="#FAFAFA"
          ItemSeparatorComponent={ this._itemSeparatorComponent }
          getItemLayout={ this._getItemLayout }
          onRefresh={ this._onRefresh }
          refreshing={ loading }
          onEndReached={ this._onEndReached }
        />
      </View>
    )
  }
  
}

const styles = StyleSheet.create({
  separator: {
    height: 10,
    backgroundColor: '#FAFAFA'
  }
})

export default connect()(MyOrderFlatList)
