/**
 * Created by Andste on 2018/10/9.
 */
import React, { Component } from 'react'
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StatusBar,
  StyleSheet,
  Keyboard
} from 'react-native'
import { connect } from 'react-redux'
import { messageActions } from '../../redux/actions'
import { DismissKeyboardHOC, ImageCodeModal } from '../../components'
import { colors } from '../../../config'
import { RegExp, Screen } from '../../utils'
import { BigButton } from '../../widgets'
import Icon from 'react-native-vector-icons/Ionicons'
import * as API_Article from '../../apis/article'
import * as API_Passport from '../../apis/passport'
import { WebView } from 'react-native-webview'
import { Modal } from '../../components'

const DismissKeyboardView = DismissKeyboardHOC(View)

class RegisterScreen extends Component {
  static navigationOptions = {
    title: '手机快速注册'
  }
  
  constructor(props) {
    super(props)
    this.scene = 'REGISTER'
    this.time = 60
    this.state = {
      mobile: '',
      mobilecode: '',
      sendSmsBtnDisabled: false,
      protocol: true,
      sendBtnText: '发送验证码',
      // 显示注册协议
      showAgreement: false,
      // 注册协议
      agreement: '',
      imageCodeModal: false
    }
  }

  componentDidMount() {
    this._getRegAgreement()
  }
  
  componentWillUnmount() {
    clearInterval(this.interval)
  }
  
  _getRegAgreement = async () => {
    const res = await API_Article.getArticleByPosition('REGISTRATION_AGREEMENT')
    const agreement = res.content
    this.setState({
      agreement
    })
  }

  _onRegModalClose = () => {
    this.setState({showAgreement: false})
  }

  _onRegModalOpen = () => {
    this.setState({showAgreement: true})
  }

  /**
   * 手机号输入框内容发生改变
   * @param text
   * @private
   */
  _onMobileChange = (text) => {
    this.setState({ mobile: text })
  }
  
  /**
   * 短信验证码输入框内容发生改变
   * @param text
   * @private
   */
  _mobileCodeChange = (text) => {
    this.setState({ mobilecode: text })
  }
  
  /**
   * 注册协议勾选状态发生改变
   * @private
   */
  _protocolChange = () => {
    this.setState({ protocol: !this.state.protocol })
  }
  
  //  发送短信验证码
  _sendMobileSms = () => {
    let { mobile } = this.state
    if (!RegExp.mobile.test(mobile)) {
      this.props.dispatch(messageActions.error('手机号格式有误！'))
      this.setState({ mobile: '' }, this._textInputMobile.focus())
    } else {
      Keyboard.dismiss()
      this.setState({ imageCodeModal: true })
    }
  }
  
  _imageCodeConfirm = async code => {
    const { mobile } = this.state
    await this.setState({ imageCodeModal: false })
    await new Promise(resolve => setTimeout(resolve, 500))
    await API_Passport.sendRegisterSms(mobile, code)
    this.props.dispatch(messageActions.success('发送成功！'))
    this._countDown()
    this._textInputMobilecode.focus()
  }
  
  //  倒计时
  _countDown = () => {
    this.time = 60
    this.interval = setInterval(() => {
      this.time--
      if (this.time === 0) {
        clearInterval(this.interval)
        this.setState({
          sendBtnText: '重新发送',
          sendSmsBtnDisabled: false
        })
        return
      }
      this.setState({
        time: this.time,
        sendBtnText: this.time + 's',
        sendSmsBtnDisabled: true
      })
    }, 1000)
  }
  
  _nextStep = async () => {
    let { mobile, mobilecode } = this.state
    const { navigate, state } = this.props.navigation
    await API_Passport.validMobileSms(mobile, this.scene, mobilecode)
    const { params } = state
    navigate('RegisterComplete', {
      mobile: mobile,
      connectParams: params
    })
  }
  
  render() {
    let { mobile, mobilecode, protocol, sendSmsBtnDisabled, sendBtnText, imageCodeModal, showAgreement, agreement } = this.state
    return (
      <DismissKeyboardView style={ styles.container }>
        <StatusBar barStyle="dark-content"/>
        { imageCodeModal ? (
          <ImageCodeModal
            type={ this.scene }
            mobile={ mobile }
            isOpen={ imageCodeModal }
            onClosed={ () => this.setState({ imageCodeModal: false }) }
            confirm={ this._imageCodeConfirm }
          />
        ) : undefined }
        <View style={ styles.register_form }>
          <View style={ styles.register_mobile }>
            <View style={ styles.register_mobile_area }>
              <Text style={ { fontSize: 18 } }>+86</Text>
            </View>
            <View style={ { width: Screen.width - 40 - 65 - 80 } }>
              <TextInput
                style={ styles.register_mobile_input }
                ref={ textInput => this._textInputMobile = textInput }
                value={ mobile }
                maxLength={ 11 }
                multiline={ false }
                onChangeText={ this._onMobileChange }
                autoFocus={ true }
                keyboardType="numeric"
                returnKeyType="done"
                placeholder="请输入手机号"
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
              onPress={ this._sendMobileSms }
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
                onChangeText={ this._mobileCodeChange }
                keyboardType="numeric"
                returnKeyType="done"
                placeholder="请输入短信验证码"
                placeholderTextColor="#777777"
                clearButtonMode="while-editing"
                underlineColorAndroid="transparent"
              />
            </View>
          </View>
          <View style={ styles.register_form_protocol }>
            <TouchableOpacity onPress={ this._protocolChange }>
              <Icon
                style={ styles.protocol_icon }
                name={ protocol ? 'ios-checkmark-circle' : 'ios-radio-button-off' } size={ 18 }/>
            </TouchableOpacity>
            <Text style={ { color: '#777777' } }>同意</Text>
            <TouchableOpacity onPress={ this._onRegModalOpen }>
              <Text style={ { color: colors.text } }>用户注册协议</Text>
            </TouchableOpacity>
          </View>
          <BigButton
            style={ { width: Screen.width - 40, height: 44, borderRadius: 3, marginTop: 30 } }
            title="下一步"
            disabled={ !mobile || !mobilecode || !protocol }
            onPress={ this._nextStep }
          />
        </View>
        <Modal
          header={ <Text>注册协议</Text> }
          isOpen={ showAgreement }
          onRequestClose={ this._onRegModalClose }
          onClosed={ this._onRegModalClose }
        >
          <WebView originWhitelist={['*']} source={{html: agreement}} />
        </Modal>
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
  },
  register_form_protocol: {
    flexDirection: 'row',
    alignItems: 'center',
    marginTop: 20
  },
  protocol_icon: {
    color: colors.main,
    marginRight: 7
  }
})

export default connect()(RegisterScreen)
