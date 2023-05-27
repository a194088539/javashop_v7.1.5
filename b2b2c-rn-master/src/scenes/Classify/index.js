/**
 * Created by Andste on 2017/7/25.
 */
import React, { Component } from 'react'
import {
  Image,
  View,
  StatusBar,
  StyleSheet
} from 'react-native'
import { colors } from '../../../config'
import { Loading } from '../../widgets'
import { SearchHeader } from '../../components'
import { isIphoneX } from 'react-native-iphone-x-helper'
import * as API_Home from '../../apis/home'

import RootList from './RootList'
import ContentList from './ContentList'

export default class Classify extends Component {
  static navigationOptions = {
    tabBarLabel: '分类',
    tabBarIcon: ({ tintColor }) => (
      <Image
        style={ [{ width: 26, height: 26 }, { tintColor }] }
        source={ require('../../images/icon-classify.png') }/>
    )
  }
  
  constructor(props) {
    super(props)
    this.state = {
      categories: '',
      current: ''
    }
  }
  
  async componentDidMount() {
    // 获取分类数据
    const categories = await API_Home.getCategory()
    this.setState({
      categories,
      current: categories[0]
    })
  }
  
  _onPressRootListItem = (current) => {
    this.setState({ current })
  }
  
  render() {
    const { categories, current } = this.state
    if (!categories || !categories[0]) return (
      <View style={ styles.container }>
        <StatusBar barStyle="dark-content"/>
        <Loading show={ true }/>
      </View>
    )
    return (
      <View style={ styles.container }>
        <StatusBar barStyle="dark-content"/>
        <SearchHeader style={ styles.header } simple={ true }/>
        <RootList data={ categories } onPress={ this._onPressRootListItem }/>
        <ContentList data={ current } nav={ this.props.navigation }/>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    position: 'relative',
    flexDirection: 'row',
    justifyContent: 'space-between',
    backgroundColor: colors.gray_background,
    paddingTop: isIphoneX() ? 84 : 64
  },
  header: {
    zIndex: 2
  }
})
