/**
 * Created by Andste on 2017/7/22.
 */
import React, { PureComponent } from 'react'
import {
  View,
  FlatList,
  StyleSheet
} from 'react-native'
import { Screen } from '../../utils'
import { colors } from '../../../config'

import RootListItem from './RootListItem'

export default class RootList extends PureComponent {
  constructor(props) {
    super(props)
    this._curId = null
    this.state = {
      selected: (() => {
        let selected = (new Map() : Map<string, boolean>)
        let _id = this.props.data[0]['category_id']
        selected.set(_id, true)
        this._curId = _id
        return selected
      })()
    }
    this._listRef = null
    this._halfListHeight = (Screen.height - 64 - 50) / 2  //  屏幕高度 - searchHeader高度 - tabBar高度
  };
  
  _onPressItem = (id : number) => {
    this.setState((state) => {
      const selected = new Map(state.selected)
      selected.set(this._curId, false)
      selected.set(id, true)
      this._curId = id
      return { selected }
    })
    let data = this.props.data
    let curData = data.filter((item) => {
      return item['category_id'] === id
    })[0]
    let curIndex = data.findIndex((item) => {
      return item === curData
    })
    //  计算当前位置距离底部位置，如果大于半个list的高度，只需要滚动到底部。【防止出现底部出现空白】
    let curOffset = curIndex * 56
    let maxOffste = (data.length * 56 - 1) - this._halfListHeight
    curOffset >= maxOffste
      ? this._listRef.scrollToEnd({ animated: true })
      : this._listRef.scrollToItem({ animated: true, item: curData, viewPosition: 0.5 })
    this.props.onPress(curData)
  }
  
  _renderItem = ({ item }) => (
    <RootListItem
      onPress={ () => this._onPressItem(item['category_id']) }
      selected={ !!this.state.selected.get(item['category_id']) }
      title={ item.name }
    />
  )
  
  _captureRef = (ref) => { this._listRef = ref }
  
  render() {
    let { data } = this.props
    return (
      <FlatList
        style={ styles.container }
        data={ data }
        extraData={ this.state }
        keyExtractor={ item => String(item['category_id']) }
        renderItem={ this._renderItem }
        ItemSeparatorComponent={ () => <View style={ styles.item_separator }/> }
        ListEmptyComponent={ () => <View/> }
        removeClippedSubviews={ false }
        initialNumToRender={ 10 }
        getItemLayout={ (data, index) => ({ length: 56, offset: 56 * index, index }) }
        ref={ this._captureRef }
      />
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flexGrow: 0,
    flexShrink: 0,
    flexBasis: 85,
    marginTop: 15,
    backgroundColor: colors.transparent
  },
  item_separator: {
    width: 85,
    height: 1,
    backgroundColor: colors.gray_background
  }
})
