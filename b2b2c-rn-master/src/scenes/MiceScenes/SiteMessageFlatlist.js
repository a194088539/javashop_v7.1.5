/**
 * Created by Andste on 2018/11/12.
 */
import React, { PureComponent } from 'react'
import {
  View,
  Text,
  StyleSheet
} from 'react-native'
import { colors } from '../../../config'
import { Screen, Foundation } from '../../utils'
import { isIphoneX } from 'react-native-iphone-x-helper'
import { FlatList } from '../../components'
import { DataEmpty } from '../../components/EmptyViews'
import Swipeout from 'react-native-swipeout'
import * as API_Message from '../../apis/message'

export default class SiteMessageFlatlist extends PureComponent {
  constructor(props) {
    super(props)
    this.unread = props.type === 'unread'
    this.params = {
      page_no: 1,
      page_size: 10
    }
    this.state = {
      loading: false,
      noData: false,
      dataList: [],
      open_id: null
    }
  }
  
  componentDidMount() {
    this._onRefresh()
  }
  
  _getDataList = async () => {
    let { params, state } = this
    const { dataList } = state
    if (this.unread) params.read = 0
    try{
      const res = await API_Message.getMessages(params) || {}
      this.setState({
        loading: false,
        noData: !res.data || !res.data[0],
        dataList: params.page_no === 1 ? res.data : dataList.concat(res.data)
      })
    }catch(error){
      this.setState({
        loading: false
      })
    }
  }
  
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
    const leftBtns = [{
      text: '已读', type: 'primary', onPress: async () => {
        await API_Message.messageMarkAsRead(item.id)
        this._onRefresh()
      }
    }]
    const rightBtns = [{
      text: '删除', type: 'delete', onPress: async () => {
        await API_Message.deleteMessage(item.id)
        this._onRefresh()
      }
    }]
    return (
      <View>
        <View style={ styles.separator }>
          <View style={ styles.separator_time }>
            <Text style={ styles.separator_time_text }>{ Foundation.unixToDate(item.receive_time) }</Text>
          </View>
        </View>
        <Swipeout
          close={ item.id !== this.state.open_id }
          left={ item['is_read'] === 0 ? leftBtns : null }
          right={ rightBtns }
          rowID={ item.id }
          onOpen={ (_, rowID) => this.setState({ open_id: rowID }) }
          style={ styles.item }
        >
          <View style={ styles.item_content }>
            <Text>{ item.content }</Text>
          </View>
        </Swipeout>
      </View>
    )
  }
  
  render() {
    const { loading, dataList } = this.state
    return (
      <View style={ styles.container }>
        <FlatList
          data={ dataList }
          renderItem={ this._renderItem }
          ListHeaderComponent={ () => <View style={ { height: 10 } }/> }
          ListFooterComponent={ () => <View style={ { height: isIphoneX() ? 25 : 0 } }/> }
          ListEmptyComponent={ <DataEmpty/> }
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
    height: 35,
    justifyContent: 'center',
    alignItems: 'center'
  },
  separator_time: {
    backgroundColor: '#DFDEDE',
    paddingHorizontal: 5,
    paddingVertical: 3,
    borderRadius: 10
  },
  separator_time_text: {
    color: '#FFFFFF',
    fontSize: 12
  },
  item: {
    backgroundColor: '#FFFFFF',
    borderRadius: 5,
    overflow: 'hidden'
  },
  item_content: {
    paddingHorizontal: 10,
    paddingVertical: 5,
    minHeight: 60
  }
})
