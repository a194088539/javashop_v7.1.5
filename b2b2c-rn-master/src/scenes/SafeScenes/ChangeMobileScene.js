/**
 * Created by Andste on 2018/11/14.
 */
import React, { Component } from 'react'
import {
  View,
  Text,
  TextInput,
  StatusBar,
  StyleSheet
} from 'react-native'
import { connect } from 'react-redux'
import { messageActions, userActions } from '../../redux/actions'
import { colors } from '../../../config'
import { Screen, RegExp } from '../../utils'
import { DismissKeyboardHOC, ImageCodeModal } from '../../components'
import { BigButton } from '../../widgets'
import * as API_Safe from '../../apis/safe'

const DismissKeyboardView = DismissKeyboardHOC(View)

class ChangeMobileScene extends Component {
  static navigationOptions = {
    title: '修改手机号'
  }
  
  constructor(props) {
    super(props)
    this.state = {
      imageCodeModal: false,
      mobile: '',
      sms_code: '',
      sendSmsBtnDisabled: false,
      sendBtnText: '发送验证码'
    }
  }
  
  _onMobileChange = text => this.setState({ mobile: text })
  _onSmsCodeChange = text => this.setState({ sms_code: text })
  _sendSms = () => {
    const { mobile } = this.state
    const { dispatch } = this.props
    if (!mobile) {
      dispatch(messageActions.error('请输入手机号码！'))
      return
    }
    if (!RegExp.mobile.test(mobile)) {
      dispatch(messageActions.error('手机号格式不正确！'))
      return
    }
    this.setState({ imageCodeModal: true })
  }
  
  _imageCodeConfirm = async (image_code) => {
    const { mobile } = this.state
    await this.setState({ imageCodeModal: false })
    await API_Safe.sendBindMobileSms(mobile, image_code)
    this.props.dispatch(messageActions.success('发送成功'))
    this._countDown()
    this._textInputSmsCode.focus()
  }
  
  _nextStep = async () => {
    const { mobile, sms_code } = this.state
    const { dispatch, navigation } = this.props
    await API_Safe.changeMobile(mobile, sms_code)
    await dispatch(messageActions.success('修改成功！'))
    await dispatch(userActions.getUserAction())
    navigation.navigate('Mine')
  }
  
  render() {
    const { imageCodeModal, mobile, sms_code, sendBtnText, sendSmsBtnDisabled } = this.state
    return (
      <View style={ styles.container }>
        <StatusBar barStyle="dark-content"/>
        <DismissKeyboardView>
          <StatusBar barStyle="dark-content"/>
          { imageCodeModal ? (
            <ImageCodeModal
              type="BIND_MOBILE"
              isOpen={ imageCodeModal }
              onClosed={ () => this.setState({ imageCodeModal: false }) }
              confirm={ this._imageCodeConfirm }
            />
          ) : undefined }
          <View style={ styles.register_form }>
            <View style={ [styles.register_mobile, { marginTop: 10 }] }>
              <View style={ styles.register_mobile_area }>
                <Text style={ { fontSize: 16 } }>手机号</Text>
              </View>
              <View style={ { width: Screen.width - 40 - 75 - 80 } }>
                <TextInput
                  style={ styles.register_mobile_input }
                  value={ mobile }
                  maxLength={ 11 }
                  multiline={ false }
                  onChangeText={ this._onMobileChange }
                  keyboardType="phone-pad"
                  returnKeyType="done"
                  placeholder="请输入新的手机号！"
                  placeholderTextColor="#777777"
                  clearButtonMode="while-editing"
                  underlineColorAndroid="transparent"
                />
              </View>
              <BigButton
                title={ sendBtnText }
                style={ styles.send_sms_btn }
                textStyles={ styles.send_sms_btn_text }
                disabled={ sendSmsBtnDisabled }
                onPress={ this._sendSms }
              />
            </View>
            <View style={ [styles.register_mobile, { marginTop: 10 }] }>
              <View style={ styles.register_mobile_area }>
                <Text style={ { fontSize: 16 } }>验证码</Text>
              </View>
              <View style={ { width: Screen.width - 40 - 75 } }>
                <TextInput
                  ref={ textInput => this._textInputSmsCode = textInput }
                  style={ styles.register_mobile_input }
                  value={ sms_code }
                  maxLength={ 6 }
                  multiline={ false }
                  onChangeText={ this._onSmsCodeChange }
                  secureTextEntry={ true }
                  returnKeyType="done"
                  placeholder="请输入短信验证码"
                  placeholderTextColor="#777777"
                  clearButtonMode="while-editing"
                  underlineColorAndroid="transparent"
                />
              </View>
            </View>
            <BigButton
              style={ { width: Screen.width - 40, height: 44, borderRadius: 3, marginTop: 30 } }
              title="确认修改"
              disabled={ !mobile || !sms_code }
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
  },
  send_sms_btn: {
    justifyContent: 'center',
    alignItems: 'center',
    width: 80,
    height: 44,
    backgroundColor: '#20A0FF',
    borderTopRightRadius: 2,
    borderBottomRightRadius: 2
  },
  send_sms_btn_text: {
    color: '#FFFFFF',
    fontSize: 14
  }
})

export default connect()(ChangeMobileScene)
