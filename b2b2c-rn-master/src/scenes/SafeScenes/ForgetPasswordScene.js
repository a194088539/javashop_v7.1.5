/**
 * Created by Andste on 2018/11/14.
 */
import React, { Component } from 'react'
import {
  View,
  Text,
  Keyboard,
  TextInput,
  StatusBar,
  StyleSheet
} from 'react-native'
import { connect } from 'react-redux'
import { colors } from '../../../config'
import { Screen } from '../../utils'
import { DismissKeyboardHOC, ImageCodeModal } from '../../components'
import { BigButton } from '../../widgets'
import * as API_Passport from '../../apis/passport'

const DismissKeyboardView = DismissKeyboardHOC(View)

class ForgetPasswordScene extends Component {
  static navigationOptions = {
    title: '找回密码'
  }
  
  constructor(props) {
    super(props)
    this.state = {
      imageCodeModal: false,
      username: ''
    }
  }
  
  _onUsernameChange = (text) => this.setState({ username: text })
  _nextStep = () => this.setState({ imageCodeModal: true })
  _imageCodeConfirm = async (image_code) => {
    const { username } = this.state
    await this.setState({ imageCodeModal: false })
    const res = await API_Passport.validAccount(image_code, username)
    this.props.navigation.navigate('CheckMobile', {
      type: 'find-password',
      mobile: res.mobile,
      uuid: res.uuid
    })
  }
  
  render() {
    const { imageCodeModal, username } = this.state
    return (
      <View style={ styles.container }>
        <StatusBar barStyle="dark-content"/>
        <DismissKeyboardView>
          <StatusBar barStyle="dark-content"/>
          { imageCodeModal ? (
            <ImageCodeModal
              type="FIND_PASSWORD"
              isOpen={ imageCodeModal }
              onClosed={ () => this.setState({ imageCodeModal: false }) }
              confirm={ this._imageCodeConfirm }
            />
          ) : undefined }
          <View style={ styles.register_form }>
            <View style={ [styles.register_mobile, { marginTop: 10 }] }>
              <View style={ styles.register_mobile_area }>
                <Text style={ { fontSize: 16 } }>账户名</Text>
              </View>
              <View style={ { width: Screen.width - 40 - 75 } }>
                <TextInput
                  style={ styles.register_mobile_input }
                  value={ username }
                  maxLength={ 20 }
                  multiline={ false }
                  onChangeText={ this._onUsernameChange }
                  returnKeyType="done"
                  placeholder="请输入账户名"
                  placeholderTextColor="#777777"
                  clearButtonMode="while-editing"
                  underlineColorAndroid="transparent"
                />
              </View>
            </View>
            <BigButton
              style={ { width: Screen.width - 40, height: 44, borderRadius: 3, marginTop: 30 } }
              title="验证账户"
              disabled={ !username }
              onPress={ this._nextStep }
            />
          </View>
        </DismissKeyboardView>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    paddingLeft: 20,
    paddingRight: 20
  },
  register_form: {
    marginTop: 50
  },
  register_mobile: {
    flexDirection: 'row',
    width: Screen.width - 40,
    height: 44,
    backgroundColor: '#FFFFFF',
    borderRadius: 2
  },
  register_mobile_area: {
    justifyContent: 'center',
    alignItems: 'center',
    width: 75,
    height: 44
  },
  register_mobile_input: {
    padding: 0,
    height: 44,
    fontSize: 16,
    color: colors.text
  }
})
export default connect()(ForgetPasswordScene)
