/**
 * Created by Andste on 2018/10/21.
 */
import React, { Component } from 'react'
import {
  View,
  StatusBar,
  StyleSheet
} from 'react-native'
import { colors } from '../../../config'
import { Screen } from '../../utils'
import ScrollableTabView, { DefaultTabBar } from 'react-native-scrollable-tab-view'
import CollectionFlatList from './MyCollectionFlatList'

export default class MyCollectionScene extends Component {
  static navigationOptions = {
    title: '我的收藏'
  }
  
  constructor(props) {
    super(props)
  }
  
  _renderTabBar = () => (<DefaultTabBar style={ styles.tabBar } tabStyle={ { paddingBottom: 0 } }/>)
  
  render() {
    const { params = {} } = this.props.navigation.state
    const initialPage = params.type === 'shop' ? 1 : 0
    return (
      <View style={ styles.container }>
        <StatusBar barStyle="dark-content"/>
        <ScrollableTabView
          initialPage={ initialPage }
          tabBarUnderlineStyle={ styles.tabBarUnderlineStyle }
          tabBarActiveTextColor={ colors.main }
          renderTabBar={ this._renderTabBar }
          contentProps={ { bounces: false } }
        >
          <CollectionFlatList
            tabLabel="收藏的商品"
            type="goods"
            key="goods"
          />
          <CollectionFlatList
            tabLabel="收藏的店铺"
            type="shop"
            key="shop"
          />
        </ScrollableTabView>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1
  },
  tabBar: {
    height: 40,
    borderColor: colors.cell_line_backgroud,
    backgroundColor: '#FFFFFF'
  },
  tabBarUnderlineStyle: {
    backgroundColor: colors.main,
    height: 1,
    width: 80,
    left: (Screen.width / 2 - 80) / 2
  }
})
