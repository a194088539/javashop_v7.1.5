/**
 * Created by Andste on 2018/11/19.
 */
import React from 'react'
import {
  View,
  TextInput,
  TouchableOpacity,
  StyleSheet
} from 'react-native'
import Icon from 'react-native-vector-icons/Ionicons'
import { Screen } from '../utils'

export default function ({ onPress, ...props }) {
  return (
    <View style={ styles.container }>
      <TouchableOpacity style={ [styles.btn, styles.btn_l] } onPress={ () => onPress('-') }>
        <Icon style={ styles.btn_icon } name="ios-remove" size={ 18 }/>
      </TouchableOpacity>
      <View style={ [styles.input_view, styles.border] }>
        <TextInput
          style={ styles.input }
          autoCorrect={ false }
          keyboardType="numeric"
          returnKeyType="done"
          maxLength={ 6 }
          multiline={ false }
          underlineColorAndroid="transparent"
          { ...props }
        />
      </View>
      <TouchableOpacity style={ [styles.btn, styles.btn_r] } onPress={ () => onPress('+') }>
        <Icon style={ styles.btn_icon } name="ios-add" size={ 18 }/>
      </TouchableOpacity>
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    width: 80,
    height: 25,
    backgroundColor: '#FFFFFF',
    borderColor: '#CACACB',
    borderWidth: Screen.onePixel,
    borderRadius: 3
  },
  btn: {
    alignItems: 'center',
    justifyContent: 'center',
    width: 25,
    height: 25,
    borderColor: '#CACACB'
  },
  btn_l: {
    borderRightWidth: Screen.onePixel
  },
  btn_r: {
    borderLeftWidth: Screen.onePixel
  },
  btn_icon: {
    color: '#242327'
  },
  input_view: {
    width: 80 - (50 + Screen.onePixel * 4),
    height: 25,
    padding: 0,
    borderLeftWidth: 0,
    borderRightWidth: 0
  },
  input: {
    width: 80 - (50 + Screen.onePixel * 4),
    height: 25,
    padding: 0,
    fontSize: 12,
    textAlign: 'center',
    color: '#242426'
  }
})
