/**
 * Created by Andste on 2017/7/22.
 */
import React, { PureComponent } from 'react'
import {
  View,
  Text,
  FlatList,
  StyleSheet
} from 'react-native'
import { connect } from 'react-redux'
import { navigate } from '../../navigator/NavigationService'
import { searchActions } from '../../redux/actions'
import { colors } from '../../../config'
import { Screen } from '../../utils'
import { DataEmpty } from '../../components/EmptyViews'
import ContentListItem from './ContentListItem'

class ClassifyContentList extends PureComponent {
  
  componentWillReceiveProps(nextProps) {
    if (nextProps !== this.props) {
      const { children } = nextProps.data || {}
      if (!children || !children[0]) return
      setTimeout(() => {
        this._listRef.scrollToIndex({
          animated: true,
          index: 0,
          viewOffset: 0,
          viewPosition: 0
        })
      })
    }
  }
  
  _captureRef = (ref) => { this._listRef = ref }
  
  _renderItem = ({ item }) => {
    let children = item.children,
      sectionItems = []
    children && children[0] ? children.forEach((_item) => {
      sectionItems.push(
        <ContentListItem
          key={ _item['category_id'] }
          image={ _item.image }
          title={ _item.name }
          onPress={ () => this._onPressSectionItem(_item['category_id']) }
        />
      )
    }) : sectionItems.push(
      <View
        key={ Math.floor(Math.random() * 1000) }
        style={ styles.section_empty }
      ><Text>暂无数据...</Text></View>
    )
    return (
      <View style={ styles.section }>
        <View style={ styles.section_title }>
          <Text style={ styles.section_title_text }>{ item.name }</Text>
        </View>
        <View style={ styles.section_container }>
          { sectionItems }
        </View>
      </View>
    )
  }
  
  _onPressSectionItem = (cat_id) => {
    this.props.dispatch(searchActions.searchCatIdChanged(cat_id))
    navigate('GoodsList', { cat: cat_id })
  }
  
  _getItemLayout = (sections, index) => {
    let section = sections[index],
      offset
    let children = section.children,
      childrenLen = children.length
    offset = childrenLen > 0 ? (Math.ceil(childrenLen / 3) * 100 + 40) : 140
    return { length: offset, offset: offset * index, index }
  }
  
  render() {
    let sections = this.props.data.children
    return (
      <FlatList
        style={ styles.container }
        data={ sections }
        keyExtractor={ item => String(item['category_id']) }
        renderItem={ this._renderItem }
        ListEmptyComponent={ () => (<DataEmpty/>) }
        ListHeaderComponent={ () => <View style={{ height: 10 }}/> }
        removeClippedSubviews={ false }
        getItemLayout={ this._getItemLayout }
        ref={ this._captureRef }
      />
    )
  }
}

const SECTION_WIDTH = Screen.width - 85 - 20
const styles = StyleSheet.create({
  container: {
    paddingLeft: 10
  },
  section_empty: {
    width: SECTION_WIDTH,
    minHeight: 100,
    justifyContent: 'center',
    alignItems: 'center'
  },
  section_width: {
    width: Screen.width - 85
  },
  section: {
    width: SECTION_WIDTH,
    backgroundColor: '#FFFFFF'
  },
  section_title: {
    justifyContent: 'center',
    height: 40,
    backgroundColor: colors.gray_background
  },
  section_title_text: {
    fontSize: 14
  },
  section_container: {
    flexDirection: 'row',
    flexWrap: 'wrap'
  },
  section_header: {
    width: SECTION_WIDTH,
    height: 95,
    marginTop: 15
  }
})

export default connect()(ClassifyContentList)
