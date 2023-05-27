/**
 * Created by Andste on 2018/10/21.
 */
import React, { Component } from 'react'
import {
  View,
  StatusBar
} from 'react-native'
import ScrollableTabView from 'react-native-scrollable-tab-view'
import TabBar from 'react-native-underline-tabbar'
import { colors } from '../../../config'
import { Screen } from '../../utils'

import OrderFlatList from './MyOrderFlatList'

export default class MyOrderScene extends Component {
  static navigationOptions = {
    title: '我的订单'
  }
  
  constructor(props) {
    super(props)
    this.state = {
      tabs: [
        { label: '全部', status: 'ALL' },
        { label: '待付款', status: 'WAIT_PAY' },
        { label: '待发货', status: 'WAIT_SHIP' },
        { label: '待收货', status: 'WAIT_ROG' },
        { label: '待评论', status: 'WAIT_COMMENT' }
      ]
    }
  }
  
  _renderTabBar = () => (
    <TabBar
      underlineColor={ colors.main }
      underlineHeight={ 3 }
      tabBarStyle={ {
        marginTop: 0,
        paddingTop: 10,
        height: 40,
        alignItems: 'center'
      } }
      tabBarTextStyle={ {
        fontWeight: 'bold',
        fontSize: 14
      } }
      tabStyles={ {
        tab: { width: (Screen.width - 120) / 5 }
      } }
    />
  )
  
  render() {
    let initialPage = 0
    const { params = {} } = this.props.navigation.state
    if (params.status) {
      initialPage = this.state.tabs.findIndex(item => item.status === params.status)
    }
    return (
      <View style={ { flex: 1, backgroundColor: '#FAFAFA' } }>
        <StatusBar barStyle="dark-content"/>
        <ScrollableTabView
          initialPage={ initialPage }
          tabBarInactiveTextColor={ colors.text }
          tabBarActiveTextColor={ colors.main }
          tabBarBackgroundColor="#FFFFFF"
          renderTabBar={ this._renderTabBar }
          contentProps={ { bounces: false } }
        >
          { this.state.tabs.map(item => (
            <OrderFlatList
              tabLabel={ { label: item.label } }
              status={ item.status }
              key={ item.status }
            />
          )) }
        </ScrollableTabView>
      </View>
    )
  }
}
