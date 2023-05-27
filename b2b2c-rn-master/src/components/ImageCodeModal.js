/**
 * Created by Andste on 2017/8/7.
 */

import React, { PureComponent } from 'react'
import {
  View,
  Text,
  TextInput,
  Image,
  TouchableOpacity,
  StyleSheet,
  Keyboard
} from 'react-native'
import Modal from 'react-native-modalbox'
import { Screen } from '../utils'
import { colors } from '../../config'
import * as API_Common from '../apis/common'

const MODAL_WIDTH = Screen.width - 80

export default class ImageCodeModal extends PureComponent {
  constructor(props) {
    super(props)
    this.state = {
      image_code: '',
      image_code_url: ''
    }
  }
  
  componentDidMount() {
    this._imageCodeChange()
  }
  
  _onCodeText = text => this.setState({image_code: text})
  
  _imageCodeChange = () => {
    const {type, uuid} = this.props
    this.setState({image_code_url: API_Common.getValidateCodeUrl(type, uuid)})
  }
  
  //  验证验证码
  _confirm = async () => {
    const {confirm} = this.props
    Keyboard.dismiss()
    await new Promise(resolve => setTimeout(resolve, 500))
    typeof confirm === 'function' && confirm(this.state.image_code)
  }
  
  render() {
    let {...props} = this.props
    let {image_code_url} = this.state
    return (
      <Modal
        style={ styles.container }
        ref={ modal => this._modal = modal }
        backdropPressToClose={ false }
        swipeToClose={ false }
        coverScreen={ true }
        position="center"
        animationDuratio={ 300 }
        { ...props }
      >
        <View style={ styles.modal_title }>
          <Text>请输入图片验证码</Text>
        </View>
        <View style={ styles.modal_body }>
          <View style={ styles.code_view }>
            <View style={ styles.code_input_view }>
              <TextInput
                style={ styles.code_input }
                ref={ textInput => this._textInput = textInput }
                placeholderTextColor="#777777"
                multiline={ false }
                autoCorrect={ false }
                underlineColorAndroid="transparent"
                clearButtonMode="while-editing"
                autoFocus={ true }
                keyboardType="email-address"
                returnKeyType="done"
                maxLength={ 4 }
                onChangeText={ this._onCodeText }
              />
            </View>
            <TouchableOpacity
              style={ [styles.send_btn] }
              onPress={ this._imageCodeChange }
            >
              { image_code_url ? (
                <Image style={ styles.image_code } source={ {uri: image_code_url} }/>
              ) : undefined }
            </TouchableOpacity>
          </View>
        </View>
        <View style={ styles.modal_footer }>
          <TouchableOpacity style={ [styles.modal_btn] } onPress={ () => this._modal.close() }>
            <Text style={ {fontSize: 14} }>取消</Text>
          </TouchableOpacity>
          <TouchableOpacity style={ [styles.modal_btn, styles.modal_btn_confirm] } onPress={ this._confirm }>
            <Text style={ styles.confirm_text }>确定</Text>
          </TouchableOpacity>
        </View>
      </Modal>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    justifyContent: 'center',
    alignItems: 'center',
    width: MODAL_WIDTH,
    height: 135,
    marginTop: -60,
    backgroundColor: '#FFFFFF',
    borderRadius: 5
  },
  modal_title: {
    justifyContent: 'center',
    alignItems: 'center',
    width: MODAL_WIDTH - 50,
    height: 35,
    borderTopLeftRadius: 5,
    borderTopRightRadius: 5
  },
  modal_body: {
    width: MODAL_WIDTH - 50,
    height: 56
  },
  modal_footer: {
    flexDirection: 'row',
    width: MODAL_WIDTH,
    height: 44,
    borderColor: colors.cell_line_backgroud,
    borderTopWidth: Screen.onePixel
  },
  modal_btn: {
    justifyContent: 'center',
    alignItems: 'center',
    width: MODAL_WIDTH / 2
  },
  modal_btn_confirm: {
    borderColor: colors.cell_line_backgroud,
    borderLeftWidth: Screen.onePixel,
    backgroundColor: colors.main,
    borderBottomRightRadius: 5
  },
  confirm_text: {
    color: '#FFFFFF'
  },
  code_view: {
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    width: MODAL_WIDTH - 50,
    height: 44
  },
  code_input_view: {
    width: MODAL_WIDTH - 50 - 80
  },
  code_input: {
    height: 38,
    textAlign: 'center',
    backgroundColor: colors.gray_background
  },
  send_btn: {
    justifyContent: 'center',
    alignItems: 'center',
    width: 80,
    height: 38,
    borderTopRightRadius: 5,
    borderBottomRightRadius: 5
  },
  send_btn_text: {
    color: '#FFFFFF',
    fontSize: 14
  },
  image_code: {
    width: 80,
    height: 38
  }
})
