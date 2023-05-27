/**
 * Created by Andste on 2017/8/5.
 */
import React from 'react'
import {
  Image,
  StyleSheet,
  Text,
  TextInput,
  TouchableWithoutFeedback,
  View
} from 'react-native'
import { colors } from '../../config'
import { Screen } from '../utils'

const inputWidth = Screen.width - 40 - 63

export default function ({ label, labelStyle, inputSytle, onPressValidcodeImage, validcodeUrl, ...props }) {
  return (
    <View style={ styles.container }>
      <View style={ [styles.label_view, labelStyle] }>
        <Text style={ [styles.label, labelStyle] }>{ label }</Text>
      </View>
      <View style={ [styles.input_view, validcodeUrl && { width: inputWidth - 95 }] }>
        <TextInput
          style={ [styles.input, inputSytle] }
          ref={ textInput => this._textInput = textInput }
          placeholderTextColor={ colors.navigator_tint_color }
          multiline={ false }
          autoCorrect={ false }
          underlineColorAndroid="transparent"
          clearButtonMode="while-editing"
          { ...props }
        />
      </View>
      {
        validcodeUrl ? (
          <TouchableWithoutFeedback onPress={ () => {
            onPressValidcodeImage()
            this._textInput.clear()
          } }>
            <Image style={ styles.validcode_image } source={ { uri: validcodeUrl } }/>
          </TouchableWithoutFeedback>
        ) : undefined
      }
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    alignItems: 'center',
    height: 48,
    borderColor: colors.cell_line_backgroud,
    borderBottomWidth: Screen.onePixel * 2
  },
  label_view: {
    justifyContent: 'center',
    alignItems: 'flex-start',
    height: 48,
    minWidth: 48
  },
  label: {
    textAlign: 'justify',
    fontSize: 16,
    color: colors.text
  },
  input_view: {
    width: inputWidth,
    height: 48
  },
  input: {
    padding: 0,
    height: 48,
    fontSize: 16,
    color: colors.text
  },
  validcode_image: {
    width: 95,
    height: 38
  }
})
