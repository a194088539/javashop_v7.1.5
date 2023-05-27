/**
 * Created by Andste on 2018/11/23.
 */
import React from 'react'
import {
  View,
  Text,
  StyleSheet
} from 'react-native'
import { colors } from '../../config'

export default function ({ text }) {
  return (
    <View style={ styles.container }>
      <Text
        style={ styles.text }
        numberOfLines={ 3 }
      >{ text }</Text>
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    alignItems: 'flex-end',
    paddingLeft: 10,
    paddingRight: 10,
    paddingTop: 10,
    paddingBottom: 10,
    backgroundColor: '#FFFFFF'
  },
  text: {
    fontSize: 18,
    fontWeight: '400',
    color: colors.text
  }
})
