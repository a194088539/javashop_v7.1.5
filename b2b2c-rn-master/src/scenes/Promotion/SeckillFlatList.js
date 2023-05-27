/**
 * Created by Andste on 2019-01-10.
 */
import React, { PureComponent } from 'react'
import {
  View,
  Text,
  Image,
  FlatList,
  TouchableOpacity,
  StyleSheet
} from 'react-native'
import { navigate } from '../../navigator/NavigationService'
import { Screen, Foundation } from '../../utils'
import { colors } from '../../../config'
import { Price } from '../../widgets'
import { DataEmpty } from '../../components/EmptyViews'
import * as API_Promotions from '../../apis/promotions'

export default class SeckillFlatList extends PureComponent {
  constructor(props) {
    super(props)
    this.params = {
      page_no: 1,
      page_size: 10,
      range_time: props.data['time_text']
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
  
  /**
   * 获取商品数据
   * @returns {Promise<void>}
   * @private
   */
  _getGoodsList = async () => {
    const params = JSON.parse(JSON.stringify(this.params))
    const { goodsList } = this.state
    try{
      const { data } = await API_Promotions.getSeckillTimeGoods(params)
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
  
  /**
   * 刷新数据
   * @returns {Promise<void>}
   * @private
   */
  _onRefresh = async () => {
    this.params.page_no = 1
    await this.setState({ loading: true })
    this._getGoodsList()
  }
  
  /**
   * 滑动到底部触发
   * @private
   */
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
  
  _ListHeaderComponent = () => (
    <SeckillCountDown
      onlyOne={ this.props.onlyOne }
      data={ this.props.data }
      resTime={ this.props.resTime }
    />
  )
  
  /**
   * 去商品页
   * @param item
   * @private
   */
  _toGoods = (item) => {
    navigate('Goods', { id: item['goods_id'] })
  }
  
  /**
   * 计算百分比以及进度条宽度
   * @param item
   * @returns {{perc : string, width : number}}
   * @private
   */
  _countProgress = (item) => {
    const xx = (item.sold_num / (item.sold_num + item.sold_quantity) * 100)
    return {
      perc: xx.toFixed(0),
      width: 75 * xx / 100
    }
  }
  
  _renderItem = ({ item }) => {
    const { data } = this.props
    const cp = this._countProgress(item)
    return (
      <TouchableOpacity
        onPress={ () => this._toGoods(item) }
        style={ styles.goods_item }
      >
        <Image style={ styles.goods_img } source={ { uri: item['goods_image'] } }/>
        <View style={ styles.goods_info }>
          <Text
            style={ styles.goods_name }
            numberOfLines={ 2 }
            ellipsizeMode="tail"
          >{ item['goods_name'] }</Text>
          <View style={ styles.goods_price_buy }>
            <View style={ { width: 100 } }>
              <Price
                advanced
                price={ item['seckill_price'] }
              />
              <Price
                style={ styles.goods_original_price }
                price={ item['original_price'] }
              />
            </View>
            <View style={ styles.goods_btn_view }>
              <View style={ [styles.goods_btn,cp.perc === '100' ? {backgroundColor: '#443630'} :{}] }>
                <Text style={ styles.goods_btn_text }>{data['distance_time'] === 0 ? (cp.perc === '100' ? '已售空' : '立即抢购') : '即将开始'}</Text>
              </View>
              <View style={ styles.goods_progress }>
                <View style={ styles.goods_progress_text_view }>
                  <Text style={styles.goods_progress_text}>已抢{ cp.perc }%</Text>
                </View>
                <View style={ styles.goods_progress_view }>
                  <View style={ [styles.goods_progress_inner, { width: cp.width }] }/>
                </View>
              </View>
            </View>
          </View>
        </View>
      </TouchableOpacity>
    )
  }
  
  render() {
    const { goodsList, loading } = this.state
    return (
      <View style={ styles.container }>
        <FlatList
          data={ goodsList }
          renderItem={ this._renderItem }
          ListEmptyComponent={ <DataEmpty/> }
          ListHeaderComponent={ this._ListHeaderComponent }
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

class SeckillCountDown extends PureComponent {
  constructor(props) {
    super(props)
    const data = props['data']
    const diffTime = parseInt(new Date().getTime() / 1000) - this.props['resTime']
    this.time = (data['distance_time'] || data['next_distance_time'] || Foundation.theNextDayTime()) - diffTime
    this.state = {
      times: Foundation.countTimeDown(this.time)
    }
  }
  
  componentDidMount() {
    this._setInterval = setInterval(() => {
      if (this.time <= 0) {
        clearInterval(this._setInterval)
      } else {
        this.setState({ times: Foundation.countTimeDown(this.time) })
        this.time--
      }
    }, 1000)
  }
  
  componentWillUnmount() {
    this._setInterval && clearInterval(this._setInterval)
  }
  
  render() {
    const { data, onlyOne } = this.props
    const { times } = this.state
    const staring = data['distance_time'] === 0
    const text = staring ? '抢购中，先下单先得哦' : '即将开始，请保持关注'
    return (
      <View style={ styles.c_d_container }>
        <Text style={ [styles.c_d_text, staring && { color: colors.main }] }>{ text }</Text>
        <View style={ styles.c_d_time_view }>
          <Text style={ styles.c_d_time_tit }>{ staring ? ( onlyOne ? '距结束' : '距下一轮') : '距开始' }</Text>
          <Text style={ styles.c_d_time }>{ times.hours }</Text>
          <Text>:</Text>
          <Text style={ styles.c_d_time }>{ times.minutes }</Text>
          <Text>:</Text>
          <Text style={ styles.c_d_time }>{ times.seconds }</Text>
        </View>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#FFF'
  },
  item_separator: {},
  goods_item: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingVertical: 10,
    paddingHorizontal: 10
  },
  goods_img: {
    width: 102,
    height: 102
  },
  goods_info: {
    justifyContent: 'space-between',
    width: Screen.width - 102 - 20 - 5,
    height: 102,
    marginLeft: 5
  },
  goods_name: {
    fontSize: 16,
    color: colors.text
  },
  goods_price_buy: {
    flexDirection: 'row',
    justifyContent: 'space-between'
  },
  goods_original_price: {
    color: '#ccc',
    fontSize: 10,
    textDecorationLine: 'line-through'
  },
  goods_btn_view: {
    alignItems: 'flex-end',
    justifyContent: 'space-between'
  },
  goods_btn: {
    justifyContent: 'center',
    alignItems: 'center',
    width: 75,
    height: 30,
    marginRight: 15,
    backgroundColor: colors.main,
    borderRadius: 3
  },
  goods_btn_text: {
    color: '#FFF'
  },
  goods_progress: {
    flexDirection: 'row',
    alignItems: 'center'
  },
  goods_progress_text_view:{
    width:50,
    alignItems:'flex-end'
  },
  goods_progress_text: {
    fontSize:10
  },
  goods_progress_view: {
    width: 75,
    height: 10,
    alignItems: 'flex-start',
    borderWidth: Screen.onePixel,
    borderColor: colors.main,
    borderRadius: 5,
    marginLeft: 5,
    marginRight: 15
    
  },
  goods_progress_inner: {
    height: 10,
    backgroundColor: colors.main,
    borderRadius: 5
  },
  c_d_container: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingHorizontal: 10,
    width: Screen.width,
    height: 30
  },
  c_d_text: {
    color: colors.text
  },
  c_d_time_view: {
    flexDirection: 'row',
    alignItems: 'center'
  },
  c_d_time_tit: {
    marginRight: 5
  },
  c_d_time: {
    backgroundColor: '#000',
    color: '#FFF',
    padding: 1,
    borderRadius: 2
  }
})
