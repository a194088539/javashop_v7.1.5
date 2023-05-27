/**
 * Created by Andste on 2017/8/11.
 */

import React, { Component } from 'react'
import {
  Keyboard,
  StatusBar,
  StyleSheet,
  View,
  TextInput,
  TouchableOpacity
} from 'react-native'
import DismissKeyboardHOC from '../../components/DismissKeyboardHOC'
import { colors } from '../../../config'
import { Screen } from '../../utils'
import { F16Text } from '../../widgets/Text'

const DismissKeyboardView = DismissKeyboardHOC(View)

export default class InputScene extends Component {
  static navigationOptions = ({ navigation }) => {
    let { title } = navigation.state.params
    return {
      title: title || '请输入值',
      headerRight: (
        <TouchableOpacity
          onPress={ () => navigation.setParams({ isConfirm: true }) }
          style={ styles.confirm_btn }
        >
          <F16Text style={ styles.confirm_text }>确定</F16Text>
        </TouchableOpacity>
      )
    }
  }
  
  constructor(props) {
    super(props)
    this.navParams = this.props.navigation.state.params
    this.state = {
      textValue: this.navParams.defaultValue
    }
  }
  
  shouldComponentUpdate(nextProps, nextState) {
    let text = this.state.textValue
    let { navigation } = nextProps
    let { validate = () => true, confirm, isConfirm } = navigation.state.params
    if (isConfirm) {
      if (validate(text)) {
        Keyboard.dismiss()
        confirm(text) !== false && navigation.goBack()
      } else {
        navigation.setParams({ isConfirm: false })
      }
      return false
    }
    return nextState !== this.state
  };
  
  _onChangeText = text => {
    this.setState({ textValue: text })
  }
  
  render() {
    let { inputProps, inputStyle } = this.navParams
    let { textValue } = this.state
    return (
      <DismissKeyboardView style={ styles.container }>
        <StatusBar barStyle="dark-content"/>
        <TextInput
          style={ [styles.input, inputStyle] }
          value={ textValue }
          autoCorrect={ false }
          autoFocus
          multiline
          clearButtonMode="while-editing"
          placeholderTextColor="#777777"
          onChangeText={ this._onChangeText }
          underlineColorAndroid="transparent"
          { ...inputProps }
        />
      </DismissKeyboardView>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center'
  },
  confirm_btn: {
    width: 44,
    height: 44,
    justifyContent: 'center',
    alignItems: 'center'
  },
  confirm_text: {
    color: colors.main
  },
  input: {
    width: Screen.width,
    height: 75,
    fontSize: 16,
    padding: 10,
    backgroundColor: '#FFF'
  },
  prompt: {
    fontSize: 12,
    marginTop: 15,
    marginLeft: 10,
    color: '#777777'
  }
})
