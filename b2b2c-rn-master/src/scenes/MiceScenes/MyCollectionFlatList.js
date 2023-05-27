/**
 * Created by Andste on 2018/11/7.
 */
import React, { PureComponent } from 'react'
import {
  Alert,
  View,
  StyleSheet
} from 'react-native'
import { connect } from 'react-redux'
import { messageActions } from '../../redux/actions'
import { navigate } from '../../navigator/NavigationService'
import { colors } from '../../../config'
import { Screen } from '../../utils'
import { FlatList } from '../../components'
import { MyCollectionGoodsEmpty, MyCollectionShopEmpty } from '../../components/EmptyViews'
import * as API_Members from '../../apis/members'
import { CollectionGoodsItem, CollectionShopItem } from './MyCollectionItem'

class MyCollectionFlatList extends PureComponent {
  constructor(props, context) {
    super(props, context)
    this.is_shop = this.props.type === 'shop'
    this.params = {
      page_no: 1,
      page_size: 10
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
  
  /**
   * 获取数据
   * @returns {Promise<void>}
   * @private
   */
  _getDataList = async () => {
    const { params, state } = this
    const { dataList } = state
    try{
      let res = this.is_shop
      ? await API_Members.getShopCollection(params)
      : await API_Members.getGoodsCollection(params)
      res = res || {}
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
  _getItemLayout = (data, index) => ({
    length: this.is_shop ? 190 : (135 + Screen.onePixel),
    offset: this.is_shop ? 190 : (135 + Screen.onePixel) * index,
    index
  })
  
  /**
   * 刷新数据
   * @private
   */
  _onRefresh = async () => {
    await this.setState({ loading: true })
    this.params.page = 1
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
  
  _deleteGoods = async (item) => {
    Alert.alert('提示', '确认删除吗？', [
      { text: '取消', onPress: () => {} },
      {
        text: '确定', onPress: async () => {
          await API_Members.deleteGoodsCollection(item.goods_id)
          this.props.dispatch(messageActions.success('删除成功！'))
          this._onRefresh()
        }
      }
    ])
  }

  _deleteShop = async (item) => {
    Alert.alert('提示', '确认删除吗？', [
      { text: '取消', onPress: () => {} },
      {
        text: '确定', onPress: async () => {
          await API_Members.deleteShopCollection(item.shop_id)
          this.props.dispatch(messageActions.success('删除成功！'))
          this._onRefresh()
        }
      }
    ])
  }
  
  _toDetail = (goods) => navigate('Goods', { id: goods.goods_id })
  _toShop = (shop) => navigate('Shop', { id: shop.shop_id })
  
  /**
   * 渲染数据
   * @param item
   * @returns {*}
   * @private
   */
  _renderItem = ({ item }) => {
    return (
      this.is_shop
        ? <CollectionShopItem
          data={ item }
          toDetail={ this._toDetail }
          delItem={ () => this._deleteShop(item) }
          toShop={ () => this._toShop(item) }
        />
        : <CollectionGoodsItem
          data={ item }
          delItem={ () => this._deleteGoods(item) }
          toDetail={ () => this._toDetail(item) }
        />
    )
  }
  
  render() {
    const { loading, dataList } = this.state
    return (
      <View style={ styles.container }>
        <FlatList
          data={ dataList }
          renderItem={ this._renderItem }
          ListEmptyComponent={this.is_shop ? <MyCollectionShopEmpty/> : <MyCollectionGoodsEmpty/> }
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
    flex: 1
  },
  separator: {
    width: Screen.width - 120,
    height: Screen.onePixel,
    marginLeft: 120,
    backgroundColor: colors.cell_line_backgroud
  },
  list_footer: {
    height: 25,
    backgroundColor: '#FFFFFF'
  }
})

export default connect()(MyCollectionFlatList)
