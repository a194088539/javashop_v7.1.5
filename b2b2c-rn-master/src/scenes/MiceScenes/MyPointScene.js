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
import { CellGroup, Cell } from '../../widgets'
import ScrollableTabView, { DefaultTabBar } from 'react-native-scrollable-tab-view'
import PointFlatList from './MyPointFlatList'
import * as API_Members from '../../apis/members'

export default class MyCollectionScene extends Component {
  static navigationOptions = {
    title: '我的积分'
  }
  
  constructor(props) {
    super(props)
    this.state = {
      point: ''
    }
  }
  
  componentDidMount() {
    this._getPointData()
  }
  
  _getPointData = async () => {
    const res = await API_Members.getPoints() || {}
    this.setState({ point: res })
  }
  
  _renderTabBar = () => (<DefaultTabBar style={ styles.tabBar } tabStyle={ { paddingBottom: 0 } }/>)
  
  render() {
    const { point } = this.state
    return (
      <View style={ styles.container }>
        <StatusBar barStyle="dark-content"/>
        <ScrollableTabView
          tabBarUnderlineStyle={ styles.tabBarUnderlineStyle }
          tabBarActiveTextColor={ colors.main }
          renderTabBar={ this._renderTabBar }
          contentProps={ { bounces: false } }
        >
          <CellGroup
            tabLabel="我的积分"
            key="point"
          >
            <Cell title="消费积分" label={ point['consum_point'] || '加载中...' } arrow={ false }/>
            <Cell title="等级积分" label={ point['grade_point'] || '加载中...' } arrow={ false }/>
          </CellGroup>
          <PointFlatList
            tabLabel="积分明细"
            key="detail"
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
    left: (Screen.width / 2 - 80) / 2
  }
})
