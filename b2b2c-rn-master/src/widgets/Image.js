/**
 * Created by Andste on 2018/10/13.
 */
import React from 'react'
import {
  View,
  Image
} from 'react-native'

export default function ({ uri, style, ...props }) {
  if (!uri) return <View></View>
  return (
    <Image
      source={ { uri } }
      style={ [style, { resizeMode: 'stretch' }] }
      { ...props }
    />
  )
}
