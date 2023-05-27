/**
 * Created by Andste on 2018/11/12.
 */
import React, { Component } from 'react'
import {
  View,
  StatusBar,
  StyleSheet
} from 'react-native'
import { connect } from 'react-redux'
import { colors } from '../../../config'
import { Screen } from '../../utils'
import ScrollableTabView, { DefaultTabBar } from 'react-native-scrollable-tab-view'
import SiteMessageFlatlist from './SiteMessageFlatlist'

class SiteMessageScene extends Component {
  static navigationOptions = {
    title: '站内消息'
  }
  
  _renderTabBar = () => (<DefaultTabBar style={ styles.tabBar } tabStyle={ { paddingBottom: 0 } }/>)
  
  render() {
    return (
      <View style={ styles.container }>
        <StatusBar barStyle="dark-content"/>
        <ScrollableTabView
          locked
          tabBarUnderlineStyle={ styles.tabBarUnderlineStyle }
          tabBarActiveTextColor={ colors.main }
          renderTabBar={ this._renderTabBar }
          contentProps={ { bounces: false } }
        >
          <SiteMessageFlatlist
            tabLabel="未读消息"
            type="unread"
          />
          <SiteMessageFlatlist
            tabLabel="全部消息"
            type="all"
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

export default connect()(SiteMessageScene)
