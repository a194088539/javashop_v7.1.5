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
import { messageActions } from '../../redux/actions'
import { colors } from '../../../config'
import { Screen, Foundation } from '../../utils'
import { DismissKeyboardHOC, ImageCodeModal } from '../../components'
import { BigButton } from '../../widgets'
import * as API_Safe from '../../apis/safe'
import * as API_Passport from '../../apis/passport'

const DismissKeyboardView = DismissKeyboardHOC(View)

class CheckMobileScene extends Component {
  static navigationOptions = {
    title: '验证账户'
  }
  
  constructor(props) {
    super(props)
    this.time = 60
    const { params } = props.navigation.state
    this.params = params
    this.type = params.type
    const { user } = props
    this.state = {
      mobile: this.type === 'find-password' ? params.mobile : user.mobile,
      mobilecode: '',
      sendSmsBtnDisabled: false,
      sendBtnText: '发送验证码',
      imageCodeModal: false
    }
  }
  
  componentWillUnmount() {
    this.interval && clearInterval(this.interval)
  }
  
  _mobilecodeChange = text => this.setState({ mobilecode: text })
  
  _sendSms = () => this.setState({ imageCodeModal: true })
  
  _imageCodeConfirm = async (image_code) => {
    await this.setState({ imageCodeModal: false })
    if (this.type === 'find-password') {
      const { uuid } = this.params
      await API_Passport.sendFindPasswordSms(uuid, image_code)
    } else {
      await API_Safe.sendMobileSms(image_code)
    }
    this.props.dispatch(messageActions.success('发送成功'))
    this._countDown()
    this._textInputMobilecode.focus()
  }
  
  _nextStep = async () => {
    let { mobilecode } = this.state
    const { uuid } = this.params
    if (this.type === 'mobile') {
      await API_Safe.validChangeMobileSms(mobilecode)
    } else if (this.type === 'find-password') {
      await API_Passport.validFindPasswordSms(uuid, mobilecode)
    } else {
      await API_Safe.validChangePasswordSms(mobilecode)
    }
    let toScene = 'ChangePassword'
    if (this.type === 'mobile') {
      toScene = 'ChangeMobile'
    }
    this.props.navigation.navigate(toScene, { ...this.params })
  }
  
  _countDown = () => {
    this.time = 60
    this.interval = setInterval(() => {
      this.time--
      if (this.time === 0) {
        clearInterval(this.interval)
        this.setState({ sendBtnText: '重新发送', sendSmsBtnDisabled: false })
        return
      }
      this.setState({
        time: this.time,
        sendBtnText: this.time + 's',
        sendSmsBtnDisabled: true
      })
    }, 1000)
  }
  
  render() {
    let { mobile, mobilecode, sendSmsBtnDisabled, sendBtnText, imageCodeModal } = this.state
    const { uuid } = this.params
    return (
      <DismissKeyboardView style={ styles.container }>
        <StatusBar barStyle="dark-content"/>
        { imageCodeModal ? (
          <ImageCodeModal
            uuid={ uuid }
            type={ this.type === 'find-password' ? 'FIND_PASSWORD' : "VALIDATE_MOBILE" }
            isOpen={ imageCodeModal }
            onClosed={ () => this.setState({ imageCodeModal: false }) }
            confirm={ this._imageCodeConfirm }
          />
        ) : undefined }
        <View style={ styles.register_form }>
          <View style={ styles.register_mobile }>
            <View style={ styles.register_mobile_area }>
              <Text style={ { fontSize: 18 } }>手机号</Text>
            </View>
            <View style={ { width: Screen.width - 40 - 65 - 80 } }>
              <TextInput
                style={ styles.register_mobile_input }
                defaultValue={ Foundation.secrecyMobile(mobile) }
                multiline={ false }
                editable={ false }
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
            <View style={ { width: Screen.width - 40 - 65 } }>
              <TextInput
                style={ styles.register_mobile_input }
                ref={ textInput => this._textInputMobilecode = textInput }
                value={ mobilecode }
                maxLength={ 6 }
                multiline={ false }
                onChangeText={ this._mobilecodeChange }
                keyboardType="numeric"
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
            title="下一步"
            disabled={ !mobilecode }
            onPress={ this._nextStep }
          />
        </View>
      </DismissKeyboardView>
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
    width: 65,
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

export default connect(state => ({ user: state.user.user }))(CheckMobileScene)
