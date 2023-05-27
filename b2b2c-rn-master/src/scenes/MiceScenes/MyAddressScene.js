/**
 * Created by Andste on 2018/10/21.
 */
import React, { Component } from 'react'
import {
  Alert,
  View,
  StatusBar,
  FlatList,
  StyleSheet
} from 'react-native'
import { connect } from 'react-redux'
import { messageActions } from '../../redux/actions'
import { isIphoneX } from 'react-native-iphone-x-helper'
import { navigate } from '../../navigator/NavigationService'
import { colors } from '../../../config'
import { Screen } from '../../utils'
import { AddressEmpty } from '../../components/EmptyViews'
import { BigButton } from '../../widgets'
import * as API_Address from '../../apis/address'
import AddressItem from './MyAddressItem'

class MyAddressScene extends Component {
  static navigationOptions = ({ navigation }) => {
    const { params = {} } = navigation.state
    return {
      title: params.title || '我的地址'
    }
  }
  
  constructor(props) {
    super(props)
    this.state = {
      dataList: []
    }
  }
  
  componentDidMount() {
    this._getAddressList()
  }
  
  _getAddressList = async () => {
    const res = await API_Address.getAddressList() || []
    this.setState({ dataList: res })
  }
  
  _setDef = async (item) => {
    await API_Address.setDefaultAddress(item['addr_id'])
    await this.props.dispatch(messageActions.success('设置成功！'))
    this._getAddressList()
  }
  
  _editAdd = (item) => {
    navigate('AddressAddEdit', {
      address: item,
      callback: this._getAddressList
    })
  }
  
  _delAdd = (item) => {
    const { dispatch } = this.props
    if (item['def_addr'] === 1) {
      dispatch(messageActions.error('默认地址不能删除！'))
      return
    }
    Alert.alert('提示', '确认要删除这个地址吗？', [
      { text: '取消', onPress: () => {} },
      {
        text: '确定', onPress: async () => {
          await API_Address.deleteAddress(item['addr_id'])
          dispatch(messageActions.success('删除成功！'))
          this._getAddressList()
        }
      }
    ])
  }
  
  _onPressItem = (item) => {
    let { params = {} } = this.props.navigation.state
    let { callback } = params
    if (typeof callback === 'function') {
      callback(item)
      this.props.navigation.goBack()
    }
  }
  
  _ItemSeparatorComponent = () => (<View style={ styles.separator }/>)
  _renderItem = ({ item }) => {
    return (
      <AddressItem
        data={ item }
        setDef={ () => this._setDef(item) }
        editAdd={ () => this._editAdd(item) }
        delAdd={ () => this._delAdd(item) }
        onPress={ () => this._onPressItem(item) }
      />
    )
  }
  
  render() {
    const { dataList } = this.state
    return (
      <View style={ styles.container }>
        <StatusBar barStyle="dark-content"/>
        <View style={ { flex: 1 } }>
          <FlatList
            data={ dataList }
            ItemSeparatorComponent={ this._ItemSeparatorComponent }
            ListEmptyComponent={ <AddressEmpty/> }
            renderItem={ this._renderItem }
            keyExtractor={ (_, index) => String(index) }
          />
        </View>
        <BigButton
          style={ styles.big_btn } title="新建地址"
          onPress={ () => navigate('AddressAddEdit', { callback: this._getAddressList }) }
        />
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: colors.gray_background,
    paddingBottom: isIphoneX() ? 30 : 0
  },
  separator: {
    width: Screen.width,
    height: 10,
    backgroundColor: colors.gray_background
  },
  big_btn: {
    width: Screen.width
  }
})

export default connect()(MyAddressScene)
