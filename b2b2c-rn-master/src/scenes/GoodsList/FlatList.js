/**
 * Created by Andste on 2017/7/24.
 */

import React, { PureComponent } from 'react'
import {
  View,
  Image,
  FlatList,
  StyleSheet
} from 'react-native'
import { connect } from 'react-redux'
import { searchActions } from '../../redux/actions'
import { GoodsEmpty } from '../../components/EmptyViews'
import { Screen } from '../../utils'
import { colors } from '../../../config'
import * as API_Goods from '../../apis/goods'
import FlatListItem from './FlatListItem'

const WIDTH = Screen.width

class GoodsListFlatList extends PureComponent {
  constructor(props) {
    super(props)
    this.params = {
      page_no: 1,
      page_size: 10,
      sort: 'def_desc',
      category: this.props.cat_id,
      keyword: this.props.keyword
    }
    this.state = {
      goodsList: [],
      filtering: false,
      loading: false,
      noData: false
    }
  }
  
  componentDidMount() {
    this._getGoodsList()
  }
  
  componentWillReceiveProps(nextProps) {
    let { status, cat, sort, keyword } = nextProps
    if (status === 'view_type_changed') return
    this.params = {
      ...this.params,
      cat,
      sort,
      keyword,
      page_no: 1
    }
    this.setState({ filtering: true }, this._getGoodsList)
  }
  
  componentWillUnmount() {
    this.props.dispatch(searchActions.searchViewTypeChanged('single'))
  }
  
  _getGoodsList = async () => {
    const { page_no } = this.params
    try{
      const { data } = await API_Goods.getGoodsList(this.params)
      this.setState({
        loading: false,
        goodsList: page_no === 1 ? data : this.state.goodsList.concat(data),
        noData: !data || !data[0],
        filtering: false
      })
    }catch(error){
      this.setState({
        loading: false
      })
    }
    
  }
  
  _renderItem = ({ item }) => (
    <FlatListItem
      data={ item }
      view_type={ this.props.view_type || 'single' }
      nav={ this.props.nav }
    />)
  _ListEmptyComponent = () => (<GoodsEmpty/>)
  _renderSeparator = () => {
    let double = this.props.view_type === 'double'
    return (
      double ? <View style={ { width: WIDTH, height: 5, backgroundColor: colors.gray_background } }/> :
        <View style={ styles.item_separator }>
          <View style={ { width: 120, height: 1, backgroundColor: '#FFFFFF' } }/>
          <View style={ { width: WIDTH - 120, height: 1, backgroundColor: colors.gray_background } }/>
        </View>
    )
  }
  
  _getItemLayout = (sections, index) => {
    let double = this.props.view_type === 'double'
    let height = double ? 265 : 121
    return { length: height, offset: height * index, index }
  }
  
  _onEndReached = () => {
    let { loading, noData, goodsList } = this.state
    if (!goodsList[9] || loading || noData) return
    this.params.page_no++
    this.setState({ loading: true }, this._getGoodsList)
  }
  
  render() {
    let { goodsList, filtering } = this.state
    let { view_type } = this.props
    const single = (view_type === undefined || view_type === 'single')
    return (
      filtering ? (
        <View style={ styles.filtering }>
          <Image
            source={ require('../../images/icon-filtering.gif') }
            style={ { width: Screen.width / 5 } }
            resizeMode="contain"/>
        </View>
      ) : <FlatList
        style={ styles.container }
        data={ goodsList }
        renderItem={ this._renderItem }
        keyExtractor={ (item, index) => String(index) }
        ItemSeparatorComponent={ this._renderSeparator }
        ListEmptyComponent={ this._ListEmptyComponent }
        initialNumToRender={ 10 }
        key={ single ? 'single' : 'double' }
        numColumns={ single ? 1 : 2 }
        columnWrapperStyle={ !single && { justifyContent: 'space-between' } }
        onEndReached={ this._onEndReached }
        onEndReachedThreshold={ 0.1 }
        getItemLayout={ this._getItemLayout }
      />
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    width: Screen.width,
    backgroundColor: colors.gray_background
  },
  filtering: {
    flex: 1,
    paddingTop: 175,
    justifyContent: 'flex-start',
    alignItems: 'center'
  },
  data_empty: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    paddingTop: 175
  },
  item_separator: {
    width: Screen.width,
    height: 1,
    backgroundColor: colors.gray_background
  },
  footer: {
    justifyContent: 'center',
    alignItems: 'center',
    width: Screen.width,
    height: 50
  }
})

const select = (state) => {
  return {
    status: state.search.status,
    keyword: state.search.keyword,
    sort: state.search.sort,
    cat_id: state.search.cat_id,
    view_type: state.search.view_type
  }
}
export default connect(select)(GoodsListFlatList)
