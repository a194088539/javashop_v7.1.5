/**
 * Created by Andste on 2018/11/8.
 */
import React, { PureComponent } from 'react'
import {
  View,
  StyleSheet
} from 'react-native'
import { Screen } from '../../utils'
import * as API_Members from '../../apis/members'
import { FlatList } from '../../components'
import { BonusEmpty } from '../../components/EmptyViews'
import CouponItem from './MyCouponItem'

export default class MyCouponFlatList extends PureComponent {
  constructor(props) {
    super(props)
    this.params = {
      page_no: 1,
      page_size: 10,
      // 是否可用，1：未使用，2：已使用，3：已过期
      status: Number(this.props.type)
    }
    this.state = {
      loading: false,
      noData: false,
      dataList: []
    }
  }
  
  componentDidMount() {
    this._onRefresh()
  }
  
  _getDataList = async () => {
    const { params, state } = this
    const { dataList } = state
    try{
      const res = await API_Members.getCoupons(params) || {}
      await this.setState({
        loading: false,
        noData: !res.data || !res.data[0],
        dataList: params.page_no === 1 ? res.data : dataList.concat(res.data)
      })
    }catch(error){
      await this.setState({
        loading: false
      })
    }
  }
  
  _ItemSeparatorComponent = () => (<View style={ styles.separator }/>)
  _getItemLayout = (_, index) => ({ length: 110, offset: 110 * index, index })
  
  /**
   * 刷新数据
   * @private
   */
  _onRefresh = async () => {
    await this.setState({ loading: true })
    this.params.page_no = 1
    this._getDataList()
  }
  
  /**
   * 滚动到底部触发
   * @private
   */
  _onEndReached = () => {
    let { dataList, loading, noData } = this.state
    if (loading || noData || !dataList[9]) return
    this.params.page_no++
    this._getDataList()
  }
  
  /**
   * 渲染数据
   * @param item
   * @returns {*}
   * @private
   */
  _renderItem = ({ item }) => {
    return (
      <CouponItem data={ item }/>
    )
  }
  
  render() {
    const { dataList, loading } = this.state
    return (
      <View style={ styles.container }>
        <FlatList
          data={ dataList }
          renderItem={ this._renderItem }
          ListHeaderComponent={ <View style={ { height: 10 } }/> }
          ListEmptyComponent={ <BonusEmpty text="暂无优惠券" /> }
          ItemSeparatorComponent={ this._ItemSeparatorComponent }
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
  container: {
    flex: 1,
    paddingHorizontal: 10
  },
  separator: {
    width: Screen.width,
    height: 10
  }
})
