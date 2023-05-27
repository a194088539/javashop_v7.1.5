/**
 * Created by Andste on 2019-01-15.
 */
import React, { Component } from 'react'
import {
  View,
  StatusBar,
  StyleSheet
} from 'react-native'
import { withNavigation } from 'react-navigation'
import { colors } from '../../../config'
import { Screen } from '../../utils'
import { FlatList } from '../../components'
import * as API_Goods from '../../apis/goods'
import GoodsItem from '../GoodsList/FlatListItem'
import ShopHeader from './ShopHeader'
import FilterBar from './ShopGoodsFilterBar'

class ShopGoods extends Component {
  static navigationOptions = { header: null }
  
  constructor(props) {
    super(props)
    const { params } = props.navigation.state
    this.params = {
      page_no: 1,
      page_size: 10,
      keyword: params.keyword,
      seller_id: params.shop_id,
      shop_cat_id: params.cat_id
    }
    this.state = {
      goodsList: '',
      loading: false,
      noData: false
    }
  }
  
  componentDidMount() {
    this._onRefresh()
  }
  
  /**
   * 搜索关键字发生改变
   * @param keyword
   * @private
   */
  _onKeywordChange = (keyword) => {
    this.params.keyword = keyword
    this._onRefresh()
  }
  
  /**
   * 排序发生改变
   * @param sort
   * @private
   */
  _sortChange = (sort) => {
    this.params.sort = sort
    this._onRefresh()
  }
  
  /**
   * 刷新商品数据
   * @returns {Promise<void>}
   * @private
   */
  _onRefresh = async () => {
    this.params.page_no = 1
    await this.setState({ loading: true })
    this._getGoodsList()
  }
  
  /**
   * 到底部加载更多
   * @returns {Promise<void>}
   * @private
   */
  _onEndReached = async () => {
    const { goodsList, noData, loading } = this.state
    if (!goodsList[9] || noData || loading) return
    this.params.page_no++
    this._getGoodsList()
  }
  
  /**
   * 获取商品数据
   * @returns {Promise<void>}
   * @private
   */
  _getGoodsList = async () => {
    const { goodsList } = this.state
    let params = JSON.parse(JSON.stringify(this.params))
    const { page_no } = params
    if (!params.shop_cat_id) {
      delete params.shop_cat_id
    }
    try{
      const res = await API_Goods.getGoodsList(params)
      this.setState({
        loading: false,
        noData: !res['data'] || !res['data'].length,
        goodsList: page_no === 1 ? res['data'] : goodsList.concat(res['data'])
      })
    }catch(error){
      this.setState({
        loading: false
      })
    }
  }
  
  _toGoods = (item) => {
    this.props.navigation.push('Goods', { id: item['goods_id'] })
  }
  
  _renderSeparator = () => (
    <View style={ { width: Screen.width, height: 5, backgroundColor: colors.gray_background } }/>
  )
  
  _getItemLayout = (sections, index) => {
    let height = 265
    return { length: height, offset: height * index, index }
  }
  
  /**
   * 渲染商品项
   * @param item
   * @private
   */
  _renderItem = ({ item }) => (
    <GoodsItem
      view_type="double"
      data={ item }
      onPress={ () => this._toGoods(item) }
    />
  )
  
  render() {
    const { goodsList, loading } = this.state
    return (
      <View style={ styles.container }>
        <StatusBar barStyle="dark-content"/>
        <ShopHeader keyword={ this.params.keyword } onKeywordChange={ this._onKeywordChange }/>
        <FilterBar sortChange={ this._sortChange }/>
        <FlatList
          data={ goodsList || [] }
          renderItem={ this._renderItem }
          refreshing={ loading }
          onRefresh={ this._onRefresh }
          onEndReached={ this._onEndReached }
          numColumns={ 2 }
          columnWrapperStyle={ { justifyContent: 'space-between' } }
          ListFooterBgColor={ colors.gray_background }
          ItemSeparatorComponent={ this._renderSeparator }
          getItemLayout={ this._getItemLayout }
        />
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: colors.gray_background
  }
})

export default withNavigation(ShopGoods)
