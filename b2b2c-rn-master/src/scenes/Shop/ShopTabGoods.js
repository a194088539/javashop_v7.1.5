/**
 * Created by Andste on 2019-01-15.
 */
import React, { PureComponent } from 'react'
import {
  View,
  StyleSheet
} from 'react-native'
import { FlatList } from '../../components'
import * as API_Goods from '../../apis/goods'
import GoodsItem from './ShopTabGoodsItem'

export default class ShopTabGoods extends PureComponent {
  constructor(props) {
    super(props)
    this.state = {
      goodsList: ''
    }
  }
  
  async componentDidMount() {
    const { shopId, tag = '', num = 20 } = this.props
    let goodsList
    if (tag === 'all') {
      const res = await API_Goods.getGoodsList({
        seller_id: shopId,
        page_no: 1,
        page_size: 100
      })
      goodsList = res['data'].map(item => {
        item['goods_name'] = item['name']
        return item
      })
    } else {
      goodsList = await API_Goods.getTagGoods(shopId, tag, num)
    }
    this.setState({ goodsList })
  }
  
  _renderItem = ({ item }) => (
    <GoodsItem data={ item }/>
  )
  
  render() {
    const { goodsList } = this.state
    const { event } = this.props
    if (!goodsList) return <View/>
    return (
      <FlatList
        data={ goodsList }
        renderItem={ this._renderItem }
        onScroll={ event }
      />
    )
  }
}


