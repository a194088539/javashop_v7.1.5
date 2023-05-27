/**
 * Created by Andste on 2018/11/8.
 */
import React from 'react'
import {
  View,
  Text,
  StyleSheet
} from 'react-native'
import { colors } from '../../config'

export default function ({ style, color, text, ...props }) {
  return (
    <View style={ [styles.container, color && { borderColor: color }, style] } { ...props }>
      <Text style={ [styles.text, color && { color }] }>{ text }</Text>
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    borderColor: colors.main,
    borderWidth: 1,
    borderRadius: 2
  },
  text: {
    fontSize: 12,
    paddingHorizontal: 5,
    paddingVertical: 1,
    textAlign: 'center',
    color: colors.main
  }
})
