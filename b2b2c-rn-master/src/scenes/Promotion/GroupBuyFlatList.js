/**
 * Created by Andste on 2018/10/21.
 */
import React, { PureComponent } from 'react'
import {
  View,
  Text,
  Image,
  TouchableOpacity,
  StyleSheet
} from 'react-native'
import { navigate } from '../../navigator/NavigationService'
import { Screen, Foundation } from '../../utils'
import { colors } from '../../../config'
import { Price } from '../../widgets'
import { FlatList } from '../../components'
import { GoodsEmpty } from '../../components/EmptyViews'
import * as API_Promotions from '../../apis/promotions'

export default class GroupBuyFlatList extends PureComponent {
  constructor(props) {
    super(props)
    this.params = {
      page_no: 1,
      page_size: 10,
      cat_id: this.props['catId']
    }
    this.state = {
      loading: false,
      noData: false,
      goodsList: []
    }
  }
  
  componentDidMount() {
    this._onRefresh()
  }
  
  _getGoodsList = async () => {
    const params = JSON.parse(JSON.stringify(this.params))
    if (params.cat_id === 0) delete params.cat_id
    const { goodsList } = this.state
    try{
      const { data } = await API_Promotions.getGroupBuyGoods(params)
      this.setState({
        loading: false,
        noData: !data || !data.length,
        goodsList: params.page_no === 1 ? data : goodsList.concat(data)
      })
    }catch(error){
      this.setState({
        loading: false
      })
    }
    
  }
  
  _onRefresh = () => {
    this.params.page_no = 1
    this.setState({ loading: true }, this._getGoodsList)
  }
  
  _onEndReached = () => {
    const { goodsList, loading, noData } = this.state
    if (!goodsList[9] || loading || noData) return
    this.params.page_no++
    this._getGoodsList()
  }
  
  _getItemLayout = (_, index) => {
    const height = 90 + Screen.onePixel
    return { length: height, offset: height * index, index }
  }
  
  _ItemSeparatorComponent = () => (<View style={ styles.item_separator }/>)
  
  _renderItem = ({ item }) => {
    return (
      <TouchableOpacity onPress={ () => navigate('Goods', { id: item['goods_id'] }) } style={ styles.item }>
        <Image source={ { uri: item['img_url'] } } style={ styles.item_image }/>
        <View style={ styles.item_detail }>
          <View style={ styles.item_detail_name }>
            <Text numberOfLines={ 2 } style={ styles.item_detail_name_text }>{ item['goods_name'] }</Text>
          </View>
          <View style={ styles.item_price }>
            <View style={ styles.item_price_left }>
              <Price advanced price={ item['price'] }/>
              <Text style={ styles.item_price_orig }>￥{ Foundation.formatPrice(item['original_price']) }</Text>
            </View>
            <View style={ styles.item_price_right }>
              <Text style={ styles.item_btn }>去抢购</Text>
              <Text style={ styles.item_buy_num }>已出售：{ item['show_buy_num'] }件</Text>
            </View>
          </View>
        </View>
      </TouchableOpacity>
    )
  }
  
  render() {
    const { loading, goodsList } = this.state
    return (
      <View style={ { flex: 1, backgroundColor: '#FFFFFF' } }>
        <FlatList
          data={ goodsList }
          renderItem={ this._renderItem }
          ListEmptyComponent={ <GoodsEmpty/> }
          ItemSeparatorComponent={ this._ItemSeparatorComponent }
          keyExtractor={ (_, index) => String(index) }
          getItemLayout={ this._getItemLayout }
          onRefresh={ this._onRefresh }
          onEndReached={ this._onEndReached }
          onEndReachedThreshold={ 0.1 }
          refreshing={ loading }
        />
      </View>
    )
  }
}

const styles = StyleSheet.create({
  item: {
    width: Screen.width,
    height: 100,
    backgroundColor: '#fff',
    flexDirection: 'row',
    alignItems: 'center',
    paddingVertical: 5,
    paddingHorizontal: 10
  },
  item_image: {
    width: 90,
    height: 80
  },
  item_detail: {
    width: Screen.width - 20 - 90,
    height: 80
  },
  item_detail_name: {
    height: 40,
    marginLeft: 10
  },
  item_detail_name_text: {
    color: colors.text,
    fontSize: 14
  },
  item_price: {
    flexDirection: 'row',
    marginLeft: 10
  },
  item_price_left: {
    width: Screen.width - 20 - 90 - 10 - 70
  },
  item_price_orig: {
    color: '#aeaeae',
    textDecorationLine: 'line-through'
  },
  item_price_right: {
    justifyContent: 'center'
  },
  item_btn: {
    width: 75,
    textAlign: 'center',
    padding: 4,
    backgroundColor: colors.main,
    color: '#FFF',
    borderRadius: 5
  },
  item_separator: {
    width: Screen.width,
    height: Screen.onePixel,
    backgroundColor: colors.cell_line_backgroud
  },
  item_buy_num: {
    marginTop: 5,
    fontSize: 12
  }
})
