/**
 * Created by Andste on 2018/10/15.
 */
import React, { Component } from 'react'
import {
  Alert,
  View,
  Text,
  TouchableOpacity,
  StatusBar,
  ScrollView,
  StyleSheet
} from 'react-native'
import { connect } from 'react-redux'
import { userActions, tokenActions } from '../../redux/actions'
import { Cell, CellGroup, Face } from '../../widgets'
import { colors } from '../../../config'
import { Screen } from '../../utils'
import * as API_Members from '../../apis/members'

class SettingScene extends Component {
  static navigationOptions = {
    title: '账户设置'
  }
  
  _confirmLogout = () => {
    Alert.alert('提示', '确定要退出吗？', [
      { text: '取消' },
      { text: '确定', onPress: this._Logout }
    ])
  }
  
  _Logout = async () => {
    await API_Members.logout()
    this.props.dispatch(userActions.initUserAction())
    this.props.navigation.goBack()
  }
  
  _navigate = (name, params) => {
    let { user, navigation } = this.props
    if (!user) {
      navigation.navigate('Login')
      return
    }
    navigation.navigate(name, params)
  }
  
  render() {
    let { user, navigation } = this.props
    if (!user) user = {}
    return (
      <View style={ styles.container }>
        <ScrollView
          showsHorizontalScrollIndicator={ false }
          showsVerticalScrollIndicator={ false }
          style={ { flex: 1 } }
        >
          <StatusBar barStyle='dark-content'/>
          <CellGroup marginBottom={ true }>
            <Cell
              icon={ <Face style={ styles.face } uri={ user.face }/> }
              title={ <FaceCellTitle user={ user }/> }
              onPress={ () => {this._navigate('MyProfile')} }
            />
          </CellGroup>
    
          <CellGroup marginBottom={ true }>
            <Cell title="地址管理" onPress={ () => {this._navigate('MyAddress')} }/>
          </CellGroup>
    
          <CellGroup marginBottom={ true }>
            <Cell title="关联账号" onPress={ () => this._navigate('MyConnect') }/>
            <Cell title="账户安全" label="修改密码、更换手机号" onPress={ () => this._navigate('Safe') }/>
          </CellGroup>
    
          <CellGroup marginBottom={ true }>
            <Cell title="设置" label="清除缓存/关于等" onPress={ () => {navigation.navigate('SettingMore')} }/>
          </CellGroup>
    
          {
            user.member_id ? (
              <CellGroup marginBottom={ true }>
                <TouchableOpacity style={ styles.login_out_btn } onPress={ this._confirmLogout }>
                  <Text style={ styles.login_out_text }>退出登录</Text>
                </TouchableOpacity>
              </CellGroup>
            ) : undefined
          }
  
        </ScrollView>
      </View>
    )
  }
}

const FaceCellTitle = ({ user }) => {
  if (!user.uname) return <Text style={ styles.face_cell_nickname }>登录/注册</Text>
  return (
    <View>
      <Text style={ styles.face_cell_nickname }>{ user.nickname }</Text>
      <Text style={ [styles.face_cell_uname] }>用户名：{ user.uname }</Text>
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1
  },
  face: {
    width: 70,
    height: 70,
    borderRadius: 35,
    backgroundColor: colors.gray_background
  },
  face_cell_nickname: {
    fontSize: 18,
    color: colors.text
  },
  face_cell_uname: {
    fontSize: 14,
    color: '#919091',
    paddingTop: 3
  },
  login_out_btn: {
    justifyContent: 'center',
    alignItems: 'center',
    width: Screen.width,
    height: 48,
    backgroundColor: '#FFFFFF'
  },
  login_out_text: {
    fontSize: 16,
    color: colors.text
  }
})

export default connect(state => ({ user: state.user.user }))(SettingScene)
