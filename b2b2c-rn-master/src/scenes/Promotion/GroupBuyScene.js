/**
 * Created by Andste on 2018/10/21.
 */
import React, { Component } from 'react'
import {
  View,
  Image,
  StatusBar
} from 'react-native'
import ScrollableTabView from 'react-native-scrollable-tab-view'
import TabBar from 'react-native-underline-tabbar'
import { HeaderBack } from '../../components'
import { colors } from '../../../config'
import * as API_Promotions from '../../apis/promotions'

import GroupBuyFlatList from './GroupBuyFlatList'

export default class GroupBuyScene extends Component {
  static navigationOptions = ({ navigationOptions }) => ({
    headerLeft: <HeaderBack tintColor="#FFFFFF"/>,
    headerTintColor: '#FFFFFF',
    headerTitle: (<Image source={ require('../../images/icon-group-buy-header.png') } style={ { width: 100, height: 23 } }/>),
    headerStyle: {
      ...navigationOptions.headerStyle,
      backgroundColor: colors.main
    }
  })
  
  constructor(props) {
    super(props)
    this.state = {
      category: [{ cat_id: 0, cat_name: '全部' }]
    }
  }
  
  componentDidMount() {
    this._getCategroy()
  }
  
  _getCategroy = async () => {
    const { category } = this.state
    const res = await API_Promotions.getGroupBuyCategorys() || []
    this.setState({ category: category.concat(res) })
  }
  
  _renderTabBar = () => (
    <TabBar
      underlineColor="#fff"
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
    />
  )
  
  render() {
    const { category } = this.state
    return (
      <View style={ { flex: 1 } }>
        <StatusBar barStyle="light-content"/>
        <ScrollableTabView
          tabBarInactiveTextColor="#ACACAC"
          tabBarActiveTextColor="#FFFFFF"
          tabBarBackgroundColor="#333333"
          renderTabBar={ this._renderTabBar }
        >
          { category.map(item => (
            <GroupBuyFlatList
              tabLabel={ { label: item['cat_name'] } }
              catId={ item['cat_id'] }
              key={ item['cat_id'] }
            />
          )) }
        </ScrollableTabView>
      </View>
    )
  }
}
