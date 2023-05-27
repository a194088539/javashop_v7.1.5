/**
 * Created by Andste on 2018/11/21.
 */
import React, { PureComponent } from 'react'
import {
  Animated,
  View,
  Text,
  Image,
  TouchableOpacity,
  StyleSheet
} from 'react-native'
import { isIphoneX } from 'react-native-iphone-x-helper'
import { HeaderBack } from '../../components'
import { colors } from '../../../config'
import { Screen } from '../../utils'

export default class GoodsHeader extends PureComponent {
  constructor(props) {
    super(props)
    this.state = {
      translateX: new Animated.Value(SPACE_WIDTH)
    }
  }
  
  static getDerivedStateFromProps(nextProps, prevState) {
    Animated.timing(prevState.translateX, {
      toValue: (nextProps.curPage * (CENTER_WIDTH / 3)) + SPACE_WIDTH,
      duration: 175,
      useNativeDriver: true
    }).start()
    return null
  }
  
  render() {
    const { opacity, curPage, onPress, onShare } = this.props
    const { translateX } = this.state
    return (
      <Animated.View style={ [styles.container, curPage === 0 && { opacity }] }>
        <View style={ styles.left_right }>
          <HeaderBack/>
          <View/>
        </View>
        <View style={ styles.center }>
          <TouchableOpacity style={ styles.btn } onPress={ () => {onPress(0)} }>
            <Text style={ [styles.btn_text, curPage === 0 && styles.cur_style] }>商品</Text>
          </TouchableOpacity>
          <TouchableOpacity style={ styles.btn } onPress={ () => {onPress(1)} }>
            <Text style={ [styles.btn_text, curPage === 1 && styles.cur_style] }>详情</Text>
          </TouchableOpacity>
          <TouchableOpacity style={ styles.btn } onPress={ () => {onPress(2)} }>
            <Text style={ [styles.btn_text, curPage === 2 && styles.cur_style] }>评价</Text>
          </TouchableOpacity>
          <Animated.View style={ [styles.btn_line, { transform: [{ translateX }] }] }/>
        </View>
        <View style={ styles.left_right }>
          <TouchableOpacity style={ styles.icon_view } onPress={ onShare }>
            <Image style={ styles.icon } source={ require('../../images/icon-share.png') }/>
          </TouchableOpacity>
          {/* <TouchableOpacity style={ styles.icon_view } onPress={ () => {} }>
            <Image style={ styles.icon } source={ require('../../images/icon-more.png') }/>
          </TouchableOpacity>*/}
        </View>
      </Animated.View>
    )
  }
}

// 中心宽度
const CENTER_WIDTH = Screen.width - 88 * 2
// 间隔宽度
const SPACE_WIDTH = (CENTER_WIDTH / 3 - 36) / 2
const styles = StyleSheet.create({
  container: {
    position: 'absolute',
    top: 0,
    left: 0,
    right: 0,
    zIndex: 99,
    flexDirection: 'row',
    justifyContent: 'space-between',
    height: isIphoneX() ? 84 : 64,
    paddingTop: isIphoneX() ? 40 : 20,
    backgroundColor: '#FFFFFF'
  },
  left_right: {
    width: 88,
    height: 44,
    flexDirection: 'row'
  },
  center: {
    width: CENTER_WIDTH,
    height: 44,
    flexDirection: 'row'
  },
  btn: {
    justifyContent: 'center',
    alignItems: 'center',
    width: CENTER_WIDTH / 3,
    height: 44
  },
  btn_text: {
    fontSize: 15
  },
  cur_style: {
    fontSize: 18,
    color: colors.main
  },
  btn_line: {
    position: 'absolute',
    bottom: 0,
    width: 36,
    backgroundColor: colors.main,
    height: 3,
    borderRadius: 3
  },
  icon_view: {
    width: 44,
    height: 44,
    justifyContent: 'center',
    alignItems: 'center'
  },
  icon: {
    width: 20,
    height: 20,
    tintColor: colors.navigator_tint_color
  }
})
