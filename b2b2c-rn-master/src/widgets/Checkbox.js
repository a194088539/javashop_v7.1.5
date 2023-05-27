/**
 * Created by Andste on 2018/11/8.
 */
import React from 'react'
import {
  TouchableOpacity,
  Text,
  StyleSheet
} from 'react-native'
import { colors } from '../../config'
import Icon from 'react-native-vector-icons/Ionicons'

export default function ({ checked, label, style, labelStyle, ...props }) {
  return (
    <TouchableOpacity style={ [styles.container, style] } activeOpacity={ 1 } { ...props }>
      <Icon
        style={ [styles.icon, checked && { color: colors.main }] }
        name={ checked ? 'ios-checkmark-circle' : 'ios-radio-button-off' }
        size={ 22 }
      />
      { label && <Text style={ [styles.label, labelStyle] }>{ label }</Text> }
    </TouchableOpacity>
  )
}

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    alignItems: 'center',
    height: 35,
    paddingRight: 10
  },
  icon: {
    color: '#CCCCCC'
  },
  label: {
    marginLeft: 7,
    fontSize: 16
  }
})
