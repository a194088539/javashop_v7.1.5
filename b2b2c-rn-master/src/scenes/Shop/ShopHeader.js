/**
 * Created by Andste on 2019-01-14.
 */
import React, { PureComponent } from 'react'
import {
  Animated,
  Image,
  View,
  Platform,
  StatusBar,
  StyleSheet,
  TouchableOpacity,
  Text,
  TextInput
} from 'react-native'
import { navigate } from '../../navigator/NavigationService'
import { colors } from '../../../config'
import { Screen } from '../../utils'
import Icon from 'react-native-vector-icons/Ionicons'
import { isIphoneX } from 'react-native-iphone-x-helper'
import { HeaderBack } from '../../components'

export default class ShopHeader extends PureComponent {
  constructor(props) {
    super(props)
    this.shop_id = props['shopId']
    this.hasClassify = this.props.scene === 'home'
    const width = Screen.width - 20 - 44 - 44 + (this.hasClassify ? 0 : 44)
    this.inputViewWidth = width
    this.inputWidth = width - 34
    this.state = {
      keyword: this.props.keyword,
      inputFpcus: false,
      backWidth: new Animated.Value(44),
      inputWidth: new Animated.Value(this.inputViewWidth)
    }
  }
  
  _onChangeText = (text) => this.setState({ keyword: text })
  
  /**
   * 提交搜索
   * @private
   */
  _onSubmitEditing = () => {
    this.refs['textInput'].blur()
    this.props['onKeywordChange'](this.state.keyword)
  }
  
  /**
   * 取消搜索
   * @private
   */
  _cancelSearch = () => {
    this.refs['textInput'].blur()
  }
  
  /**
   * 去分类页
   * @private
   */
  _toStoreClassify = () => {
    this.refs['textInput'].blur()
    navigate('ShopClassify', { shop_id: this.shop_id })
  }
  
  /**
   * 输入框获得、失去焦点
   * @param type
   * @returns {Promise<void>}
   * @private
   */
  _onFocusBlur = async (type) => {
    const focus = type === 'focus'
    await this.setState({ inputFocus: focus })
    Animated.timing(this.state.backWidth, {
      toValue: focus ? 0 : 44,
      duration: 200
    }).start()
    Animated.timing(this.state.inputWidth, {
      toValue: this.inputViewWidth + (focus ? (this.hasClassify ? 44 : 0) : 0),
      duration: 200
    }).start()
  }
  
  render() {
    const { keyword, inputFocus, backWidth, inputWidth } = this.state
    return (
      <View style={ styles.container }>
        <Animated.View style={ { width: backWidth, overflow: 'hidden' } }>
          <HeaderBack/>
        </Animated.View>
        <Animated.View style={ [styles.search_view, { width: inputWidth }] }>
          <View style={ styles.search_icon_view }>
            <Image
              style={ styles.search_icon }
              source={ require('../../images/icon-search.png') }
            />
          </View>
          <TextInput
            style={ [styles.search_input, { width: this.inputWidth + (inputFocus ? 44 : 0) }] }
            ref="textInput"
            value={ keyword }
            placeholder="搜索店铺内商品"
            placeholderTextColor="#C1C3CE"
            underlineColorAndroid="transparent"
            clearButtonMode="while-editing"
            onChangeText={ this._onChangeText }
            onSubmitEditing={ this._onSubmitEditing }
            returnKeyType="search"
            returnKeyLabel="搜索"
            maxLength={ 15 }
            onFocus={ () => this._onFocusBlur('focus') }
            onBlur={ () => this._onFocusBlur('blur') }
          />
        </Animated.View>
        <TouchableOpacity
          style={ styles.header_right }
          onPress={ inputFocus ? this._cancelSearch : this._toStoreClassify }
        >
          { inputFocus ? (
            <Text style={ styles.cancel_btn }>取消</Text>
          ) : (
            this.hasClassify
              ? <Icon name="ios-list" size={ 44 }/>
              : null
          ) }
        </TouchableOpacity>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    alignItems: 'center',
    ...Platform.select({
      ios: {
        height: isIphoneX() ? 90 : 70,
        paddingTop: isIphoneX() ? 34 : 14
      },
      android: {
        height: 56 + StatusBar.currentHeight,
        paddingTop: StatusBar.currentHeight
      }
    }),
    paddingHorizontal: 10,
    backgroundColor: '#f5f5f5',
    zIndex: 999,
    overflow: 'hidden'
  },
  search_view: {
    flexDirection: 'row',
    height: 34,
    marginTop: 5,
    backgroundColor: '#fff',
    borderWidth: Screen.onePixel,
    borderColor: colors.cell_line_backgroud,
    borderRadius: 5
  },
  search_icon_view: {
    justifyContent: 'center',
    alignItems: 'center',
    width: 34,
    height: 34
  },
  search_icon: {
    width: 18,
    height: 18
  },
  search_input: {
    width: Screen.width - 20,
    height: 34,
    paddingVertical: 5,
    borderRadius: 5
  },
  header_right: {
    justifyContent: 'center',
    alignItems: 'center',
    width: 44,
    height: 44
  },
  cancel_btn: {
    fontSize: 16,
    color: colors.text
  }
})
