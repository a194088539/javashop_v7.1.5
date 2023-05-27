/**
 * Created by Andste on 2018/10/8.
 */
import React, { Component } from 'react'
import {
  Animated,
  View,
  StatusBar,
  ScrollView,
  Platform,
  BackHandler,
  StyleSheet
} from 'react-native'
import { appId } from '../../../config'
import { isIphoneX } from 'react-native-iphone-x-helper'
import ScrollableTabView from 'react-native-scrollable-tab-view'
import * as API_Goods from '../../apis/goods'
import { ShareActionSheet } from '../../components'
import GoodsHeader from './GoodsHeader'
import GoodsFooter from './GoodsFooter'
import GoodsGallery from './GoodsGallery'
import GoodsMainPage from './GoodsMainPage'
import GoodsDetailPage from './GoodsDetailPage'
import GoodsCommentsPage from './GoodsCommentsPage'
import GoodsUnablePage from './GoodsUnablePage'
import {web_domain} from "../../../config/api";

export default class Goods extends Component {
  static navigationOptions = { header: null }
  
  constructor(props) {
    super(props)
    const { params } = props.navigation.state
    this.goods_id = params.id
    this.headerOpacity = new Animated.Value(0)
    this.state = {
      goods: '',
      cur_page: 0
    }
  }
  
  async componentDidMount() {
    if (Platform.OS === 'android') {
      BackHandler.addEventListener('hardwareBackPress', () => { return this.props.navigation.goBack() })
    }
    await this._getGoodsData()
  }

  componentWillUnmount() {
    if (Platform.OS === 'android') {
      BackHandler.removeEventListener('hardwareBackPress', this.onBackAndroid);
    }
  }
  
  /**
   * 获取商品数据
   * @returns {Promise<void>}
   * @private
   */
  _getGoodsData = async () => {
    const goods = await API_Goods.getGoods(this.goods_id)
    this.setState({ goods })
  }
  
  /**
   * tab索引发生改变
   * @param object
   * @returns {Promise<void>}
   * @private
   */
  _onChangeTab = (object) => this.setState({ cur_page: object.i })
  
  /**
   * 显示分享
   * @private
   */
  _onShowShare = () => {
    this.shareActionSheet.open()
  }
  
  render() {
    const { goods, cur_page } = this.state
    if (!goods) return <View/>
    // 头部透明
    let interpolatedHeaderOpacity = this.headerOpacity.interpolate({
      inputRange: [0, 50],
      outputRange: [0, 1],
      extrapolate: 'clamp'
    })
    
    const scrollEvent = Animated.event([{ nativeEvent: { contentOffset: { y: this.headerOpacity } } }])
    
    const { shareConfig } = appId
    const shareData = {
      title: shareConfig.title,
      description: shareConfig.description || goods['goods_name'],
      imageUrl: shareConfig.imageUrl || goods['thumbnail'],
      webpageUrl: `${web_domain}/goods/${ goods['goods_id'] }`
    }
    if (goods.market_enable === 0) return (<GoodsUnablePage goHome={() => { this.props.navigation.navigate('Home') }} goBack={ () => { this.props.navigation.goBack() } } />)
    return (
      <View style={ styles.container }>
        <StatusBar barStyle='dark-content'/>
        <GoodsHeader
          opacity={ interpolatedHeaderOpacity }
          curPage={ cur_page }
          onPress={ (index) => this.setState({ cur_page: index }) }
          onShare={ this._onShowShare }
        />
        <ScrollableTabView
          style={ styles.scroll_tab_view }
          renderTabBar={ false }
          prerenderingSiblingsNumber={ 0 }
          onChangeTab={ this._onChangeTab }
          page={ cur_page }
          contentProps={ { bounces: false } }
        >
          <ScrollView
            onScroll={ scrollEvent }
            scrollEventThrottle={ 16 }
            showsVerticalScrollIndicator={ false }
          >
            <GoodsGallery data={ goods['gallery_list'] }/>
            <GoodsMainPage goods={ goods }/>
          </ScrollView>
          <GoodsDetailPage
            intro={ goods['intro'] }
            params={ goods['param_list'] }
          />
          <GoodsCommentsPage goodsId={ this.goods_id }/>
        </ScrollableTabView>
        <GoodsFooter goods={ goods }/>
        <ShareActionSheet
          ref={ _ref => this.shareActionSheet = _ref }
          data={ shareData }
        />
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1
  },
  scroll_tab_view: {
    marginBottom: 50 + (isIphoneX() ? 30 : 0)
  }
})
