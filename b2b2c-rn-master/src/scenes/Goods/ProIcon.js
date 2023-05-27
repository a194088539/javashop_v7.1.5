/**
 * Created by Andste on 2019-01-08.
 */
import React from 'react'
import {
  View,
  Text,
  Image,
  StyleSheet
} from 'react-native'
import { Screen } from '../../utils'

export default function ({ icon, text }) {
  return (
    <View style={ styles.container }>
      <Image style={ styles.icon } source={ icon }/>
      <Text style={ styles.text }>{ text }</Text>
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    alignItems: 'center',
    width: 70,
    height: 15,
    backgroundColor: '#F2315D',
    borderWidth: Screen.onePixel,
    borderColor: '#FFF'
  },
  icon: {
    tintColor: '#F2315D',
    width: 15,
    height: 15,
    backgroundColor: '#FFF'
  },
  text: {
    width: 70 - 15,
    color: '#FFF',
    fontSize: 12,
    fontWeight: '500',
    textAlign: 'center'
  }
})
