/**
 * Created by Andste on 2018/11/21.
 */
import React, { PureComponent } from 'react'
import {
  View,
  TouchableOpacity,
  Text,
  Modal,
  StyleSheet
} from 'react-native'
import { isIphoneX } from 'react-native-iphone-x-helper'
import { colors } from '../../../config'
import { Screen } from '../../utils'
import { FlatList } from '../../components'
import { DataEmpty } from '../../components/EmptyViews'
import * as API_Members from '../../apis/members'
import CommentsItem from './GoodsCommentsItem'
import ImageViewer from 'react-native-image-zoom-viewer'

const filterBars = [
  { text: '全部', key: 'all' },
  { text: '好评', key: 'good' },
  { text: '中评', key: 'neutral' },
  { text: '差评', key: 'bad' },
  { text: '有图', key: 'image' }
]

export default class GoodsCommentsPage extends PureComponent {
  constructor(props) {
    super(props)
    this.goods_id = this.props['goodsId']
    this.params = {
      page_no: 1,
      page_size: 10,
      grade: 'all'
    }
    this.state = {
      dataList: [],
      noData: false,
      cur_key: 'all',
      count: {},
      showModal: false,
      imageIndex: 0,
      images: [],
      loading: false
    }
  }
  
  componentDidMount() {
    this._getCommentsCount()
    this._getCommentsData()
  }
  
  _getCommentsCount = async () => {
    const res = await API_Members.getGoodsCommentsCount(this.goods_id)
    await this.setState({ count: res })
  }
  
  _getCommentsData = async () => {
    const { page_no, grade } = this.params
    const { dataList } = this.state
    grade === 'all' && delete this.params.grade
    try{
      const res = await API_Members.getGoodsComments(this.goods_id, this.params)
      await this.setState({
        loading: false,
        noData: ! res.data || ! res.data[0],
        dataList: page_no === 1 ? res.data : dataList.concat(res.data)
      })
    }catch(error){
      await this.setState({
        loading: false
      })
    }
  }
  
  _onFilterChange = async (item) => {
    await this.setState({ cur_key: item.key })
    if (item.key === 'image') {
      this.params.grade = 'all'
      this.params.have_image = 1
    } else {
      this.params.grade = item.key
      delete this.params.have_image
    }
    this._onRefresh()
  }
  
  _ItemSeparatorComponent = () => (<View/>)
  
  _onRefresh = async () => {
    await this.setState({ loading: true })
    this.params.page = 1
    this._getCommentsData()
  }
  
  _onEndReached = () => {
    const { dataList, loading, noData } = this.state
    if (loading || noData || ! dataList[9]) return
    this.params.page_no ++
    this._getCommentsData()
  }

  _onShowModal = (index, images) => {
    this.setState({ showModal: true, imageIndex: index, images })
  }

  _onCloseModal = () => {
    this.setState({ showModal: false, imageIndex: 0 })
  }
  
  _renderItem = ({ item }) => {
    return (
      <View>
        <CommentsItem data={ item } imageShow={this._onShowModal} imageClose={ this._onCloseModal }/>
      </View>
    )
  }
  
  render() {
    const { state } = this
    const { dataList, count, loading } = state
    return (
      <View style={ styles.container }>
        <View style={ styles.filter_bar }>
          { filterBars.map(item => (
            <TouchableOpacity
              style={ styles.bar_btn }
              key={ item.key }
              onPress={ () => this._onFilterChange(item) }
            >
              <Text
                style={ [
                  styles.bar_text,
                  state.cur_key === item.key && styles.bar_text_cur
                ] }>{ item.text }</Text>
              <Text
                style={ [
                  styles.bar_text,
                  state.cur_key === item.key && styles.bar_text_cur
                ] }>{ count[item.key + '_count'] || 0 }</Text>
            </TouchableOpacity>
          )) }
        </View>
        <FlatList
          data={ dataList }
          renderItem={ this._renderItem }
          ListEmptyComponent={ <DataEmpty/> }
          ItemSeparatorComponent={ this._ItemSeparatorComponent }
          onRefresh={ this._onRefresh }
          refreshing={ loading }
          onEndReached={ this._onEndReached }
        />
        <Modal
          visible={ this.state.showModal }
          transparent={ true }
          animationType="slide"
          onRequestClose={ this._onCloseModal }
        >
          <ImageViewer
            imageUrls={ this.state.images }
            index={ this.state.imageIndex }
            enablePreload={ true }
            saveToLocalByLongPress={ false }
            enableSwipeDown={ true }
            onSwipeDown={ this._onCloseModal }
            onCancel={ this._onCloseModal }
            enableImageZoom={ true }
          />
        </Modal>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    marginTop: isIphoneX() ? 84 : 64
  },
  filter_bar: {
    flexDirection: 'row',
    width: Screen.width,
    height: 50,
    backgroundColor: '#FFFFFF'
  },
  bar_btn: {
    width: Screen.width / 5,
    justifyContent: 'center',
    alignItems: 'center'
  },
  bar_text: {
    fontSize: 14,
    color: colors.text
  },
  bar_text_cur: {
    color: colors.main
  },
  comment_item: {}
})
