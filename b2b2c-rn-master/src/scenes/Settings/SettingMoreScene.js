/**
 * Created by Andste on 2018/10/25.
 */
import React, { Component } from 'react'
import {
  Alert,
  ScrollView
} from 'react-native'
import VersionNumber from 'react-native-version-number'
import ClearCache from 'react-native-clear-cache'
import { Cell, CellGroup } from '../../widgets'

export default class SettingMoreScene extends Component {
  static navigationOptions = {
    title: '设置'
  }
  
  constructor(props) {
    super(props)
    this.state = {
      cacheSize: null,
      unit: null
    }
  }
  
  componentDidMount() {
    this._getCacheSize()
  }
  
  _getCacheSize = () => {
    ClearCache.getCacheSize((value, unit) => {
      this.setState({
        cacheSize: value,
        unit
      })
    })
  }
  
  _clearCache = () => {
    ClearCache.runClearCache(() => {
      Alert.alert('提示', '缓存清除成功！', [
        { text: '确定', onPress: this._getCacheSize }
      ])
    })
  }
  
  _confirmClearCache = () => {
    Alert.alert('提示', '确定要清除本地缓存吗？', [
      { text: '取消', onPress: () => {} },
      { text: '确定', onPress: this._clearCache }
    ])
  }
  
  _navigate = (routName, params) => this.props.navigation.navigate(routName, params)
  
  render() {
    const { cacheSize, unit } = this.state
    const { appVersion } = VersionNumber
    return (
      <ScrollView
        showsHorizontalScrollIndicator={ false }
        showsVerticalScrollIndicator={ false }
        style={ { flex: 1 } }
      >
        <CellGroup marginBottom={ true }>
          <Cell title="清除本地缓存" onPress={ this._confirmClearCache } label={ cacheSize + unit }/>
        </CellGroup>
        
        <CellGroup>
          <Cell title="联系我们" onPress={ () => this._navigate('ContactUs') }/>
          <Cell title="隐私设置" onPress={ () => this._navigate('PrivacySetting') }/>
          <Cell title="关于" label={ 'v' + appVersion } onPress={ () => this._navigate('AboutApp') }/>
        </CellGroup>
      </ScrollView>
    )
  }
}
