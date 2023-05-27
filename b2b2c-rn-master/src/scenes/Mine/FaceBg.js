/**
 * Created by Andste on 2018/10/16.
 */
import React, { PureComponent } from 'react'
import {
  View,
  Text,
  TouchableOpacity,
  ImageBackground,
  StyleSheet
} from 'react-native'
import { navigate } from '../../navigator/NavigationService'
import Icon from 'react-native-vector-icons/Ionicons'
import { ifIphoneX } from 'react-native-iphone-x-helper'
import { Screen } from '../../utils'
import { Face } from '../../widgets'

const default_bg = require('../../images/bg-mine-face.png')
export default class FaceBg extends PureComponent {
  _toScenes = (name, params) => {
    const { user } = this.props
    if (!user) {
      navigate('Login')
    } else {
      navigate(name, params)
    }
  }
  
  render() {
    let { user } = this.props
    if (!user) user = {}
    return (
      <ImageBackground
        source={ default_bg }
        style={ styles.container }
      >
        <View style={ styles.view }>
          <TouchableOpacity onPress={ () => {this._toScenes('Setting')} } style={ styles.face_view }>
            <Face uri={ user.face }/>
          </TouchableOpacity>
          { user.member_id ? (
            <View style={ styles.info_view }>
              <View style={ styles.info_view_top }>
                <Text style={ styles.info_view_uname }>{ user.nickname || user.uname || '请登录' }</Text>
                {/*<View style={ styles.info_view_level }>
									<Text style={ styles.info_view_level_text }>黄金会员</Text>
								</View>*/ }
              </View>
              <View style={ styles.info_view_bottom }>
                <TouchableOpacity
                  onPress={ () => {this._toScenes('MyPoint')} }
                  style={ styles.info_view_point }
                >
                  <Text
                    numberOfLines={ 1 }
                    style={ styles.info_view_point_text }
                  >等级积分：{ user.grade_point || '请登录' }</Text>
                  <Icon name="ios-arrow-forward-outline" color='#eeeeee' size={ 17 }/>
                </TouchableOpacity>
                <TouchableOpacity
                  onPress={ () => {this._toScenes('MyPoint')} }
                  style={ styles.info_view_point }
                >
                  <Text
                    numberOfLines={ 1 }
                    style={ styles.info_view_point_text }
                  >消费积分：{ user.consum_point || '请登录' }</Text>
                  <Icon name="ios-arrow-forward-outline" color='#eeeeee' size={ 17 }/>
                </TouchableOpacity>
              </View>
            </View>
          ) : (
            <TouchableOpacity onPress={ () => {navigate('Login')} } style={ styles.info_view }>
              <Text style={ styles.info_please_login }>登录/注册</Text>
            </TouchableOpacity>
          ) }
        </View>
      </ImageBackground>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    width: Screen.width,
    ...ifIphoneX({
      height: 215
    }, {
      height: 175
    }),
    justifyContent: 'center',
    alignItems: 'center'
  },
  view: {
    flexDirection: 'row',
    alignItems: 'center',
    width: Screen.width,
    height: 100,
    paddingHorizontal: 15,
    ...ifIphoneX({
      marginTop: 50
    }, {
      marginTop: 30
    })
  },
  face_view: {
    width: 90,
    height: 90,
    borderRadius: 90,
    overflow: 'hidden',
    borderWidth: 2,
    borderColor: '#d3d3d3'
  },
  info_view: {
    justifyContent: 'center',
    marginLeft: 10,
    height: 65
  },
  info_please_login: {
    fontSize: 18,
    color: '#FFFFFF'
  },
  info_view_top: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 10
  },
  info_view_uname: {
    color: '#eeeeee',
    fontSize: 14,
    fontWeight: '500'
  },
  info_view_level: {
    marginLeft: 10,
    padding: 5,
    backgroundColor: 'rgba(0,0,0,.1)',
    borderRadius: 14
  },
  info_view_level_text: {
    color: '#eeeeee',
    fontSize: 15,
    fontWeight: '500'
  },
  info_view_bottom: {
    flexDirection: 'row',
    alignItems: 'center'
  },
  info_view_point: {
    flexDirection: 'row',
    alignItems: 'center',
    marginRight: 5,
    paddingHorizontal: 5,
    paddingVertical: 2,
    borderColor: 'rgba(0,0,0,.3)',
    borderWidth: 1,
    backgroundColor: 'rgba(0,0,0,.2)',
    borderRadius: 15
  },
  info_view_point_text: {
    color: '#eeeeee',
    fontSize: 13,
    paddingHorizontal: 2,
    marginRight: 2,
    maxWidth: (Screen.width - 30 - 100 - 10 - 20) / 2
  }
})
