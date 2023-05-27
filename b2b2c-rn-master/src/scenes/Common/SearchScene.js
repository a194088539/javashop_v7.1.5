/**
 * Created by Andste on 2017/9/6.
 */

import React, { Component, PureComponent } from 'react'
import {
  Alert,
  AsyncStorage,
  FlatList,
  Image,
  Keyboard,
  Platform,
  ScrollView,
  StyleSheet,
  StatusBar,
  SafeAreaView,
  TextInput,
  TouchableOpacity,
  BackHandler,
  View
} from 'react-native'
import { connect } from 'react-redux'
import { searchActions } from '../../redux/actions'
import { colors } from '../../../config'
import { Screen } from '../../utils'
import { F16Text, F14Text } from '../../widgets/Text'
import * as API_Home from '../../apis/home'

class SearchScene extends Component {
  static navigationOptions = {
    header: null,
    gesturesEnabled: false
  }
  
  constructor(props) {
    super(props)
    this.nav_params = this.props.navigation.state.params || {}
    this.state = {
      keyword: this.props.keyword || '',
      hot_keyword: null,
      keywords: []
    }
  }
  
  componentDidMount() {
    if (Platform.OS === 'android') {
      BackHandler.addEventListener('hardwareBackPress', this._onBackAndroid)
    }
    this._getHotKeywords()
    this._getKeywordsHistory()
  }

  componentWillUnmount() {
    if (Platform.OS === 'android') {
      BackHandler.removeEventListener('hardwareBackPress', this.onBackAndroid);
    }
   
  }
  
  _onBackAndroid = () => {
    let keyword = ''
    this.props.dispatch(searchActions.searchKeywordChaned(keyword))
    return this.props.navigation.goBack()
  }

  _getHotKeywords = async () => {
    this.setState({ hot_keyword: await API_Home.getHotKeywords(8) })
  }
  
  _getKeywordsHistory = async () => {
    const historys = await AsyncStorage.getItem('searchKeywords')
    this.setState({ keywords: JSON.parse(historys) || [] })
  }
  _setKeywordsHistory = (keywords) => {
    AsyncStorage.setItem('searchKeywords', JSON.stringify(keywords))
  }
  
  _clearKeywordHistory = () => {
    Alert.alert('提示', '确认要清空历史搜索吗？', [
      { text: '取消' },
      {
        text: '确定', onPress: async () => {
          await AsyncStorage.removeItem('searchKeywords')
          this.setState({ keywords: [] })
        }
      }
    ])
  }
  
  _keyExtractor = (item, index) => String(index)
  _renderItem = ({ item }) => (
    <TouchableOpacity style={ styles.key_word_item } onPress={ () => this._onSubmitEditing(item) }>
      <F14Text color="#686868">{ item }</F14Text>
    </TouchableOpacity>
  )
  _ItemSeparatorComponent = () => (<View style={ styles.line }/>)
  _close = () => {
    Keyboard.dismiss()
    let keyword = ''
    this.props.dispatch(searchActions.searchKeywordChaned(keyword))
    this.props.navigation.goBack()
  }
  
  _onChangeText = (text) => this.setState({ keyword: text })
  
  _onSubmitEditing = (_keyword) => {
    let { keyword } = this.state
    keyword = typeof _keyword === 'string' ? _keyword : keyword
    if (keyword) {
      let _keywords = this.state.keywords.concat()
      const _index = _keywords.findIndex(item => (item === keyword))
      if (_index !== -1) _keywords.splice(_index, 1)
      if (_keywords.length > 7) _keywords.pop()
      _keywords.unshift(keyword)
      this.setState({
        keyword,
        keywords: _keywords
      })
      this._setKeywordsHistory(_keywords)
    }
    this.props.dispatch(searchActions.searchKeywordChaned(keyword))
    if (this.nav_params.replace) {
      Keyboard.dismiss()
      this.props.navigation.goBack()
      return
    }
    this.props.navigation.navigate('GoodsList')
  }
  
  render() {
    let { keyword, hot_keyword, keywords } = this.state
    return (
      <SafeAreaView style={ styles.container }>
        <StatusBar barStyle="dark-content"/>
        <View style={ styles.search_bar }>
          <View style={ styles.search_view }>
            <View style={ styles.search_input_container }>
              <View style={ styles.search_input_view }>
                <View style={ styles.search_input_icon_view }>
                  <Image
                    style={ styles.search_input_icon }
                    source={ require('../../images/icon-search.png') }
                  />
                </View>
                <TextInput
                  style={ styles.search_input }
                  value={ keyword }
                  placeholder="客官您要搜点什么？"
                  placeholderTextColor="#C1C3CE"
                  underlineColorAndroid="transparent"
                  autoFocus={ true }
                  clearButtonMode="while-editing"
                  onChangeText={ this._onChangeText }
                  onSubmitEditing={ this._onSubmitEditing }
                  returnKeyType="search"
                  returnKeyLabel="搜索"
                  maxLength={ 15 }
                />
              </View>
            </View>
            <TouchableOpacity style={ styles.cancel_btn } onPress={ this._close }>
              <F16Text>取消</F16Text>
            </TouchableOpacity>
          </View>
        </View>
        <ScrollView keyboardShouldPersistTaps="always" keyboardDismissMode="on-drag">
          <View style={ styles.search_hot }>
            <View style={ [styles.search_item_title] }>
              <F16Text>热搜</F16Text>
            </View>
            <ScrollView
              horizontal={ true }
              contentContainerStyle={ styles.search_hot_items }
              showsHorizontalScrollIndicator={ false }
              showsVerticalScrollIndicator={ false }
              keyboardShouldPersistTaps="always"
            >
              { hot_keyword && hot_keyword.map((item, index) => (
                <HotWord
                  key={ String(index) }
                  text={ item['hot_name'] }
                  onPress={ () => this._onSubmitEditing(item['hot_name']) }
                />
              )) }
            </ScrollView>
          </View>
          <View>
            <View style={ [styles.search_item_title, styles.search_keyword_title] }>
              <F16Text>历史搜索</F16Text>
            </View>
            <View style={ styles.key_word_items }>
              <FlatList
                data={ keywords }
                renderItem={ this._renderItem }
                ItemSeparatorComponent={ this._ItemSeparatorComponent }
                keyExtractor={ this._keyExtractor }
                keyboardShouldPersistTaps="always"
              />
            </View>
          </View>
          <TouchableOpacity style={ styles.clear_history_btn } onPress={ this._clearKeywordHistory }>
            <Image
              style={ styles.clear_history_btn_icon }
              source={ require('../../images/icon-delete.png') }
            />
            <F16Text color="#686868">清空历史搜索</F16Text>
          </TouchableOpacity>
        </ScrollView>
      </SafeAreaView>
    )
  }
}

class HotWord extends PureComponent {
  render() {
    return (
      <TouchableOpacity style={ styles.hot_item } { ...this.props }>
        <F14Text>{ this.props.text }</F14Text>
      </TouchableOpacity>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#FFFFFF'
  },
  search_bar: {
    width: Screen.width,
    justifyContent: 'center',
    ...Platform.select({
      ios: { height: 56 },
      android: {
        paddingTop: StatusBar.currentHeight,
        height: 56 + StatusBar.currentHeight
      }
    }),
    backgroundColor: '#FFFFFF',
    borderColor: colors.cell_line_backgroud,
    borderBottomWidth: Screen.onePixel
  },
  search_view: {
    flexDirection: 'row',
    paddingHorizontal: 10
  },
  search_input_container: {
    justifyContent: 'center',
    width: Screen.width - 20 - 44
  },
  search_input_view: {
    flexDirection: 'row',
    width: Screen.width - 20 - 44,
    height: 44 - 14,
    backgroundColor: '#F0F2F5',
    borderRadius: 15
  },
  search_input_icon_view: {
    justifyContent: 'center',
    alignItems: 'center',
    width: 30,
    height: 30
  },
  search_input_icon: {
    width: 16,
    height: 16
  },
  search_input: {
    width: Screen.width - 20 - 44 - 30,
    height: 44 - 14,
    paddingVertical: 5
  },
  cancel_btn: {
    justifyContent: 'center',
    alignItems: 'center',
    width: 44,
    height: 44
  },
  search_item_title: {
    justifyContent: 'center',
    width: Screen.width,
    height: 32,
    paddingHorizontal: 10
  },
  search_hot: {
    width: Screen.width,
    height: 85,
    paddingTop: 5,
    borderBottomColor: '#F3F5F7',
    borderBottomWidth: 15
  },
  hot_item: {
    justifyContent: 'center',
    alignItems: 'center',
    height: 23,
    backgroundColor: '#F3F5F7',
    borderRadius: 5,
    marginRight: 10,
    paddingHorizontal: 5
  },
  search_hot_items: {
    flexDirection: 'row',
    paddingHorizontal: 10
  },
  search_keyword_title: {
    borderBottomColor: colors.cell_line_backgroud,
    borderBottomWidth: Screen.onePixel
  },
  key_word_items: {
    borderBottomColor: colors.cell_line_backgroud,
    borderBottomWidth: Screen.onePixel
  },
  key_word_item: {
    justifyContent: 'center',
    width: Screen.width,
    height: 40,
    paddingHorizontal: 10
  },
  line: {
    width: Screen.width - 10,
    height: Screen.onePixel,
    marginLeft: 10,
    backgroundColor: colors.cell_line_backgroud
  },
  clear_history_btn: {
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    alignSelf: 'center',
    width: Screen.width - 120,
    height: 40,
    marginTop: 30,
    borderColor: colors.cell_line_backgroud,
    borderWidth: Screen.onePixel,
    borderRadius: 3
  },
  clear_history_btn_icon: {
    width: 18,
    height: 18,
    marginRight: 5
  }
})

export default connect(state => ({ keyword: state.search.keyword }))(SearchScene)
