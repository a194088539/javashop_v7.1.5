/**
 * Created by Andste on 2018/11/7.
 */
import React, { Component } from 'react'
import {
  Animated,
  View,
  StatusBar,
  SectionList,
  StyleSheet
} from 'react-native'
import * as API_Shop from '../../apis/shop'
import { DismissKeyboardHOC } from '../../components'
import ScrollableTabView from 'react-native-scrollable-tab-view'

import ShopHeader from './ShopHeader'
import ShopInfo from './ShopInfo'
import CustomTabBar from './ShopCustomTabBar'
import TabHome from './ShopTabHome'
import TabGoods from './ShopTabGoods'

const DismissKeyboardHOCView = DismissKeyboardHOC(View)

export default class ShopScene extends Component {
  static navigationOptions = { header: null }
  
  constructor(props) {
    super(props)
    this.shop_id = this.props.navigation.state.params.id
    this.infoMarginTop = new Animated.Value(0)
    this.state = {
      shop: '',
      tagGoods: ''
    }
  }
  
  async componentDidMount() {
    const { shop_id } = this
    const shop = await API_Shop.getShopBaseInfo(shop_id)
    this.setState({ shop })
  }
  
  /**
   * 搜索关键字发生改变
   * @param keyword
   * @private
   */
  _onKeywordChange = (keyword) => {
    const { shop_id } = this
    this.props.navigation.navigate('ShopGoods', { shop_id, keyword })
  }
  
  render() {
    const { shop_id } = this
    const { shop } = this.state
    const interpolatedInfoMarginTop = this.infoMarginTop.interpolate({
      inputRange: [0, 100],
      outputRange: [0, -100],
      extrapolate: 'clamp'
    })
    const event = Animated.event([{
      nativeEvent: {
        contentOffset: {
          y: this.infoMarginTop
        }
      }
    }])
    return (
      <View style={ styles.container }>
        <StatusBar barStyle="dark-content"/>
        <ShopHeader shopId={ shop_id } onKeywordChange={ this._onKeywordChange }/>
        <DismissKeyboardHOCView style={ styles.goods_list }>
          { shop ? (
            <ShopInfo
              shopId={ shop_id }
              shop={ shop }
              marginTop={ interpolatedInfoMarginTop }
            />
          ) : undefined }
          <ScrollableTabView
            renderTabBar={ () => (<CustomTabBar
              shopId={ shop_id }
              allNum={ shop['goods_num']}
            />) }
            contentProps={ { bounces: false } }
          >
            <TabHome  tabLabel="店铺首页" shopId={ shop_id } event={ event }/>
            <TabGoods tabLabel="全部商品" shopId={ shop_id } tag="all" event={ event }/>
            {/*<TabGoods tabLabel="上新" shopId={ shop_id } tag="new" event={ event }/>
            <TabGoods tabLabel="热卖" shopId={ shop_id } tag="hot" event={ event }/>
            <TabGoods tabLabel="推荐" shopId={ shop_id } tag="recommend" event={ event }/>*/}
          </ScrollableTabView>
        </DismissKeyboardHOCView>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#FFF'
  },
  goods_list: {
    flex: 1
  }
})
