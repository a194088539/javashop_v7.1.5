/**
 * Created by Andste on 2019-01-15.
 */
import React, { PureComponent } from 'react'
import {
  Animated,
  View,
  Image,
  Text,
  TouchableOpacity,
  StyleSheet
} from 'react-native'
import { colors } from '../../../config'
import * as API_Goods from '../../apis/goods'

export default class ShopCustomTabBar extends PureComponent {
  static defaultProps = {
    activeTextColor: colors.main,
    inactiveTextColor: colors.text,
    backgroundColor: null
  }
  
  constructor(props) {
    super(props)
    this.state = {
      nums: ''
    }
  }
  
  async componentDidMount() {
    const res = await API_Goods.getTagGoodsNum(this.props['shopId'])
    this.setState({ nums: res })
  }
  
  _renderTab = (name, page, isTabActive, onPressHandler) => {
    const { nums } = this.state
    const { activeTextColor, inactiveTextColor, textStyle, tabStyle } = this.props
    const textColor = isTabActive ? activeTextColor : inactiveTextColor
    let tabIcon
    switch (page) {
      case 0:
        tabIcon = <Image
          style={ [styles.shop_icon, { tintColor: isTabActive ? colors.main : colors.text }] }
          source={ require('../../images/icon-shop.png') }/>
        break
      case 1:
        tabIcon = <Text style={ [{ color: textColor }, textStyle] }>{ this.props['allNum'] || '0' }</Text>
        break
      case 2:
        tabIcon = <Text style={ [{ color: textColor }, textStyle] }>{ nums['new_num'] || '0' }</Text>
        break
      case 3:
        tabIcon = <Text style={ [{ color: textColor }, textStyle] }>{ nums['hot_num'] || '0' }</Text>
        break
      case 4:
        tabIcon = <Text style={ [{ color: textColor }, textStyle] }>{ nums['recommend_num'] || '0' }</Text>
        break
    }
    return (
      <TouchableOpacity
        style={ styles.flexOne }
        key={ name }
        accessible={ true }
        accessibilityLabel={ name }
        accessibilityTraits='button'
        onPress={ () => onPressHandler(page) }
      >
        <View style={ [styles.tab, tabStyle] }>
          { tabIcon }
          <Text style={ [{ color: textColor }, textStyle] }>
            { name }
          </Text>
        </View>
      </TouchableOpacity>
    )
  }
  
  render() {
    const {
      containerWidth,
      tabs,
      scrollValue,
      backgroundColor,
      style,
      activeTab,
      goToPage,
      underlineStyle
    } = this.props
    const numberOfTabs = tabs.length
    const tabUnderlineStyle = {
      position: 'absolute',
      width: containerWidth / numberOfTabs,
      height: 2,
      backgroundColor: colors.main,
      bottom: 0
    }
    const left = {
      transform: [
        {
          translateX: scrollValue.interpolate({
            inputRange: [0, 1],
            outputRange: [0, containerWidth / numberOfTabs]
          })
        }
      ]
    }
    return (
      <View style={ [styles.tabs, { backgroundColor }, style] }>
        { this.props.tabs.map((name, page) => {
          const isTabActive = activeTab === page
          const renderTab = renderTab || this._renderTab
          return renderTab(name, page, isTabActive, goToPage)
        }) }
        <Animated.View style={ [tabUnderlineStyle, left, underlineStyle] }/>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  tab: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center'
  },
  flexOne: {
    flex: 1
  },
  tabs: {
    height: 50,
    flexDirection: 'row',
    justifyContent: 'space-around',
    borderBottomWidth: 1,
    borderBottomColor: '#ccc'
  },
  shop_icon: {
    width: 20,
    height: 20
  }
})
