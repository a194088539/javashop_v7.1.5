/**
 * Created by Andste on 2018/11/5.
 */
import React, { PureComponent } from 'react'
import {
  StyleSheet,
  View,
  Text,
  TextInput
} from 'react-native'
import { connect } from 'react-redux'
import { messageActions, userActions, tokenActions } from '../../redux/actions'
import { store } from '../../redux/store'
import { RegExp, Screen } from '../../utils'
import { colors } from '../../../config'
import { DismissKeyboardHOC } from '../../components'
import BigButton from '../../widgets/BigButton'
import * as API_Passport from '../../apis/passport'
import * as API_Connect from '../../apis/connect'

const DismissKeyboardView = DismissKeyboardHOC(View)

class RegisterMobileLastStepScene extends PureComponent {
  static navigationOptions = {
    title: '请设置密码'
  }
  
  constructor(props) {
    super(props)
    this.state = {
      password: ''
    }
  }
  
  _passwordChange = text => this.setState({ password: text })
  
  _confirm = async () => {
    let { dispatch } = this.props
    let { mobile, connectParams } = this.props.navigation.state.params
    let { password } = this.state
    if (!RegExp.password.test(password)) {
      dispatch(messageActions.error('密码应为6-20位字母或数字！'))
      return
    }
    let res
    if (connectParams) {
      res = await API_Connect.connectRegisterBinder({
        ...connectParams,
        mobile,
        // Andste_TODO 2019-02-01: 还需传图片验证码，似乎有些不合理
        password
      })
    } else {
      res = await API_Passport.registerByMobile({ mobile, password })
    }
    dispatch(userActions.setUIDAction(res.uid))
    dispatch(tokenActions.setAccessTokenAction(res.access_token))
    dispatch(tokenActions.setRefreshTokenAction(res.refresh_token))
    await dispatch(userActions.getUserAction())
    dispatch(messageActions.success('注册成功！'))
    setTimeout(() => {
      this.props.navigation.navigate('Mine')
    }, 500)
  }
  
  render() {
    let { password } = this.state
    return (
      <DismissKeyboardView style={ styles.container }>
        <View style={ styles.register_form }>
          <Text style={ { fontSize: 16, color: colors.text } }>
            请牢记您设置的登录密码！
          </Text>
          <View style={ styles.register_mobile }>
            <View style={ styles.register_mobile_area }>
              <Text style={ { fontSize: 18 } }>密码</Text>
            </View>
            <View style={ { width: Screen.width - 40 - 75 } }>
              <TextInput
                style={ styles.register_mobile_input }
                value={ password }
                maxLength={ 20 }
                multiline={ false }
                secureTextEntry={ true }
                onChangeText={ this._passwordChange }
                placeholder="6-20位字母或数字"
                placeholderTextColor="#777777"
                clearButtonMode="while-editing"
                underlineColorAndroid="transparent"
              />
            </View>
          </View>
          <BigButton
            style={ { width: Screen.width - 40, height: 44, borderRadius: 3, marginTop: 30 } }
            title="确定"
            disabled={ !password }
            onPress={ this._confirm }
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
    marginTop: 10,
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

export default connect()(RegisterMobileLastStepScene)
