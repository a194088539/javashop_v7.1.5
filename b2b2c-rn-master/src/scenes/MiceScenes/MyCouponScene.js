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
import CouponFlatList from './MyCouponFlatList'

export default class MyCouponScene extends Component {
  static navigationOptions = {
    title: '我的优惠券'
  }
  
  constructor(props) {
    super(props)
  }
  
  _renderTabBar = () => (<DefaultTabBar style={ styles.tabBar } tabStyle={ { paddingBottom: 0 } }/>)
  
  render() {
    return (
      <View style={ styles.container }>
        <StatusBar barStyle="dark-content"/>
        <ScrollableTabView
          tabBarUnderlineStyle={ styles.tabBarUnderlineStyle }
          tabBarActiveTextColor={ colors.main }
          renderTabBar={ this._renderTabBar }
          contentProps={ { bounces: false } }
        >
          <CouponFlatList
            tabLabel="可用优惠券"
            type="1"
            key="can_use"
          />
          <CouponFlatList
            tabLabel="已用优惠券"
            type="2"
            key="cant_use"
          />
          <CouponFlatList
            tabLabel="已过期优惠券"
            type="3"
            key="expired"
          />
        </ScrollableTabView>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#FFFFFF'
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
    left: (Screen.width / 2 - 145) / 2
  }
})
