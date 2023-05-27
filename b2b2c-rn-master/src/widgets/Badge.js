/**
 * Created by Andste on 2019-02-14.
 */
import React from 'react'
import {
  StyleSheet,
  Text,
  View
} from 'react-native'
import { colors } from '../../config'

export default function ({ children, style }) {
  return (
    <View style={[ styles.badge, style ]}>
      <Text style={ styles.text }>{ children }</Text>
    </View>
  )
}

const size = 18
const miniSize = 8

const styles = StyleSheet.create({
  badge: {
    alignSelf: 'center',
    minWidth: size,
    height: size,
    borderRadius: size / 2,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: colors.main,
    borderWidth: StyleSheet.hairlineWidth,
    borderColor: '#fff',
  },
  miniBadge: {
    paddingHorizontal: 0,
    paddingVertical: 0,
    minWidth: miniSize,
    height: miniSize,
    borderRadius: miniSize / 2,
  },
  text: {
    fontSize: 12,
    color: 'white',
    paddingHorizontal: 4,
  },
})
