import React, { Component } from 'react'
import {
  View,
  StyleSheet
} from 'react-native'
import { FlatList } from '../../components'
import { RefundEmpty } from '../../components/EmptyViews'
import * as API_AfterSale from '../../apis/after-sale'
import AfterSaleItem from './MyAfterSaleItem'

export default class MyAfterSale extends Component {

  static navigationOptions = {
    title: '售后列表'
  }

  constructor(props) {
    super(props)
    this.params = {
      page_no: 1,
      page_size: 10
    }
    this.state = {
      loading: false,
      refundList: [],
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
  _getRefoundData = async () => {
    const { page_no } = this.params
    const { refundList } = this.state
    try{
      const res = await API_AfterSale.getAfterSale(this.params) || {}
      const { data = [] } = res
      await this.setState({
        loading: false,
        noData: !data[0],
        refundList: page_no === 1 ? data : refundList.concat(data)
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
    this._getRefoundData()
  }
  
  /**
   * 列表滚动到底部触发
   * @private
   */
  _onEndReached = () => {
    let { loading, noData, refundList } = this.state
    if (loading || noData || !refundList[9]) return
    this.params.page_no++
    this._getRefoundData()
  }
  
  _getItemLayout = (data, index) => ({ length: 205, offset: 205 * index, index })
  
  _itemSeparatorComponent = () => (<View style={ styles.separator }/>)
  
  
  /**
   * 渲染Item
   * @param item
   * @returns {*}
   * @private
   */
  _renderItem = ({ item }) => {
    return (
      <AfterSaleItem
        data={ item }
        onRefresh={ this._onRefresh }
      />
    )
  }
  
  render() {
    const { refundList, loading } = this.state
    return (
      <View style={ { flex: 1, backgroundColor: '#FAFAFA' } }>
        <FlatList
          data={ refundList }
          renderItem={ this._renderItem }
          ListHeaderComponent={ <View style={ { height: 10 } }/> }
          ListEmptyComponent={ <RefundEmpty/> }
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

