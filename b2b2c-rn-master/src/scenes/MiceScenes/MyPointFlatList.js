/**
 * Created by Andste on 2018/11/8.
 */
import React, { PureComponent } from 'react'
import {
  View,
  Text,
  StyleSheet
} from 'react-native'
import { colors } from '../../../config'
import { Screen, Foundation } from '../../utils'
import { FlatList } from '../../components'
import { F16Text, F14Text } from '../../widgets/Text'
import { DataEmpty } from '../../components/EmptyViews'
import * as API_Members from '../../apis/members'

export default class MyPointFlatList extends PureComponent {
  
  constructor(props) {
    super(props)
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
   * 获取积分数据
   * @returns {Promise<void>}
   * @private
   */
  _getDataList = async () => {
    const { params, state } = this
    const { dataList } = state
    try{
      const res = await API_Members.getPointsData(params) || {}
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
  _getItemLayout = (_, index) => ({ length: (60 + Screen.onePixel), offset: (60 + Screen.onePixel) * index, index })
  
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
  
  _renderItem = ({ item }) => {
    return (
      <View style={ styles.point_item }>
        <View style={ styles.point_left }>
          <F16Text numberOfLines={ 2 }>{ item['reason'] }</F16Text>
          <F14Text style={ styles.point_item_time }>{ Foundation.unixToDate(item['time']) }</F14Text>
        </View>
        <View style={ styles.point_right }>
          <Text style={ styles.point_item_num }>
            <Text style={ styles.point_item_num_tit }>消费：</Text>
            { item['consum_point_type'] === 1 ? '+' : '-' }{ item['consum_point'] }
          </Text>
          <Text style={ styles.point_item_num }>
            <Text style={ styles.point_item_num_tit }>等级：</Text>
            { item['consum_point_type'] === 1 ? '+' : '-' }{ item['grade_point'] }
          </Text>
        </View>
      </View>
    )
  }
  
  render() {
    const { dataList, loading } = this.state
    return (
      <View style={ styles.container }>
        <FlatList
          data={ dataList }
          renderItem={ this._renderItem }
          ListEmptyComponent={ <DataEmpty/> }
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
  point_item: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    width: Screen.width,
    height: 60,
    backgroundColor: '#FFFFFF',
    paddingHorizontal: 10,
    paddingVertical: 10
  },
  point_left: {
    width: (Screen.width - 20) / 2,
    height: 40,
    justifyContent: 'space-between'
  },
  point_item_time: {
    color: '#666666'
  },
  point_right: {
    width: (Screen.width - 20) / 2,
    justifyContent: 'center',
    alignItems: 'flex-end'
  },
  point_item_num: {
    fontSize: 20,
    fontWeight: '600',
    color: colors.main
  },
  point_item_num_tit: {
    fontSize: 12,
    color: '#777777'
  },
  separator: {
    width: Screen.width - 10,
    height: Screen.onePixel,
    marginLeft: 10,
    backgroundColor: colors.cell_line_backgroud
  }
})
