/**
 * Created by Andste on 2018/10/5.
 */
import React from 'react'
import {
  StyleSheet,
  TouchableOpacity,
  Text
} from 'react-native'
import { colors } from '../../config'
import { Screen } from '../utils'

export default function ({ selected, text, tintColor, style, textStyle, onPress }) {
  return (
    <TouchableOpacity
      style={ [
        styles.container,
        style,
        tintColor && { borderColor: tintColor },
        selected && { borderColor: colors.main }
      ] }
      onPress={ onPress }
    >
      <Text
        style={ [
          styles.text,
          textStyle,
          tintColor && { color: tintColor },
          selected && { color: colors.main }
        ] }
      >
        { text }
      </Text>
    </TouchableOpacity>
  )
}

const styles = StyleSheet.create({
  container: {
    justifyContent: 'center',
    alignItems: 'center',
    height: 30,
    paddingHorizontal: 10,
    paddingVertical: 3,
    marginRight: 10,
    marginBottom: 7,
    borderColor: colors.cell_line_backgroud,
    borderWidth: Screen.onePixel,
    borderRadius: 2
  },
  text: {
    color: colors.text,
    fontSize: 14
  }
})
