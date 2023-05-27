/**
 * Created by Andste on 2018/10/25.
 */
import React, { Component } from 'react'
import {
  View,
  Text,
  Platform,
  ScrollView,
  StyleSheet
} from 'react-native'
import Permissions from 'react-native-permissions'
import { colors } from '../../../config'
import { Cell, CellGroup } from '../../widgets'

export default class PrivacySettingScene extends Component {
  static navigationOptions = {
    title: '隐私设置'
  }
  
  constructor(props) {
    super(props)
    this.state = {
      camera: null,
      photo: null,
      contacts: null
    }
  }
  
  componentDidMount() {
    this._checkAllPermissions()
  }
  
  _checkAllPermissions = async () => {
    const perms = await Promise.all([
      Permissions.check('camera'),
      Permissions.check('photo'),
      Permissions.check('contacts')
    ])
    await this.setState({
      camera: perms[0],
      photo: perms[1],
      contacts: perms[2]
    })
  }
  
  _permissionStatus = (type) => {
    switch (type) {
      case null:
        return '检查中'
      case 'authorized':
        return '已开启'
      default:
        return '去设置'
    }
  }
  
  _requestPermission = async (type) => {
    if (Platform.OS === 'ios') {
      if (this.state[type] === 'undetermined') {
        this.setState({ [type]: await Permissions.request(type) })
      } else {
        Permissions.openSettings()
      }
    } else {
      let typeStr = ''
      let message = ''
      if (type === 'camera') {
        typeStr = '相机'
        message = '扫码、图片评论和修改头像需要访问您的相机。'
      } else if (type === 'photo') {
        typeStr = '相册'
        message = '图片评论和修改头像需要访问您的相册。'
      } else {
        typeStr = '联系人'
        message = '为了方便您添加收货人信息，我们需呀访问您的联系人。'
      }
      const res = await Permissions.request(type, {
        rationale: {
          title: `我们需要访问您的${ typeStr }权限`,
          message
        }
      })
      this.setState({ [type]: res })
    }
  }
  
  render() {
    const { camera, photo, contacts } = this.state
    return (
      <ScrollView style={ { flex: 1 } }>
        <Cell
          title="允许访问相机"
          onPress={ () => {this._requestPermission('camera')} }
          label={ this._permissionStatus(camera) }
        />
        <PlaceText title="扫码，更换头像功能需要此权限"/>
        <Cell
          title="允许访问相册"
          onPress={ () => {this._requestPermission('photo')} }
          label={ this._permissionStatus(photo) }
        />
        <PlaceText title="更换头像功能需要此权限"/>
        <Cell
          title="允许访问通讯录"
          onPress={ () => {this._requestPermission('contacts')} }
          label={ this._permissionStatus(contacts) }
        />
        <PlaceText title="方便您添加其它收货人信息"/>
      </ScrollView>
    )
  }
}

const PlaceText = ({ title }) => (
  <View style={ styles.place_view }>
    <Text style={ styles.place_text }>{ title }</Text>
  </View>
)

const styles = StyleSheet.create({
  container: {
    flex: 1
  },
  place_view: {
    height: 35,
    paddingHorizontal: 10,
    justifyContent: 'center'
  },
  place_text: {
    fontSize: 14,
    color: '#777777'
  }
})
