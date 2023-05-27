/**
 * Created by Andste on 2019-01-10.
 */
import React, { Component } from 'react'
import {
  View,
  Image,
  StatusBar,
  StyleSheet
} from 'react-native'
import { HeaderBack } from '../../components'
import { colors } from '../../../config'
import ScrollableTabView from 'react-native-scrollable-tab-view'
import * as API_Promotions from '../../apis/promotions'
import SeckillTabNav from './SeckillTabNav'
import SeckillFlatList from './SeckillFlatList'

export default class SeckillScene extends Component {
  static navigationOptions = ({ navigationOptions }) => ({
    headerLeft: <HeaderBack tintColor="#FFFFFF"/>,
    headerTintColor: '#FFFFFF',
    headerTitle: <Image style={ styles.header_img } source={ require('../../images/icon-seckill.png') }/>,
    headerStyle: {
      ...navigationOptions.headerStyle,
      backgroundColor: colors.main
    }
  })
  
  constructor(props) {
    super(props)
    this.state = {
      timeLine: '',
      curPage: 0
    }
  }
  
  componentDidMount() {
    this._getTimeLine()
  }
  
  /**
   * 获取时间线
   * @returns {Promise<void>}
   * @private
   */
  _getTimeLine = async () => {
    let res = await API_Promotions.getSeckillTimeLine()
    let timeLine = res.sort((x, y) => (Number(x['time_text']) - Number(y['time_text']))).map((item, index) => {
      item.active = index === 0
      return item
    })
    timeLine.map((item, index) => {
      item.active = index === 0
      if (item.distance_time === 0 && index === 0) {
        if (res.length !== 1) {
          item.next_distance_time = res[1].distance_time
        }
      }
      return item
    })
    timeLine = timeLine.slice(0, 5)
    await this.setState({
      timeLine,
      resTime: parseInt(new Date().getTime() / 1000),
      onlyOne: res.length === 1
    })
  }
  
  _onChangeTab = ({ i }) => {
    this.setState({ curPage: i })
  }
  
  _onNavChange = (index) => {
    this.setState({ curPage: index })
  }
  
  render() {
    const { timeLine, resTime, curPage, onlyOne } = this.state
    if (!timeLine || !timeLine.length) return <View/>
    return (
      <View style={ styles.container }>
        <StatusBar barStyle="light-content"/>
        <SeckillTabNav
          data={ timeLine }
          onNavChange={ this._onNavChange }
          curIndex={ curPage }
        />
        <ScrollableTabView
          contentProps={ { bounces: false } }
          renderTabBar={ false }
          onChangeTab={ this._onChangeTab }
          page={ curPage }
        >
          { timeLine.map((item, index) => (
            <SeckillFlatList
              onlyOne={ onlyOne }
              data={ item }
              resTime={ resTime }
              key={ index }
            />
          )) }
        </ScrollableTabView>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  header_img: {
    width: 100,
    height: 23,
    tintColor: '#FFF'
  },
  container: {
    flex: 1
  }
})
