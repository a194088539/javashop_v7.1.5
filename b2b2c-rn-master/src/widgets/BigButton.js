/**
 * Created by Andste on 2017/7/29.
 */
import React from 'react'
import {
  Text,
  TouchableOpacity,
  StyleSheet
} from 'react-native'
import { colors } from '../../config'

export default function ({ title, disabled, style, textStyles, onPress }) {
  const _onPress = () => {
    !disabled && typeof onPress === 'function' && onPress()
  }
  
  return (
    <TouchableOpacity
      style={ [
        styles.container,
        style,
        disabled && { backgroundColor: colors.disabled_background }
      ] }
      onPress={ _onPress }
      activeOpacity={ disabled ? 1 : 0.5 }
    >
      <Text
        style={ [
          styles.title,
          textStyles,
          disabled && { color: colors.disabled_text }
        ] }
      >{ title }</Text>
    </TouchableOpacity>
  )
}

const styles = StyleSheet.create({
  container: {
    justifyContent: 'center',
    alignItems: 'center',
    width: 150,
    height: 50,
    backgroundColor: colors.main
  },
  title: {
    color: '#FFFFFF',
    fontSize: 18
  }
})
