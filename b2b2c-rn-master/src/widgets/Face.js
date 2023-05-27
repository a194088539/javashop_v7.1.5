/**
 * Created by Andste on 2018/10/16.
 */
import React from 'react'
import {
  Image,
  StyleSheet
} from 'react-native'

export default function ({ uri, style, ...props }) {
  let source = { uri }
  if (!uri) source = require('../images/icon-no-face.png')
  return (
    <Image
      source={ source }
      style={ [
        styles.container,
        !uri && { resizeMode: 'center', tintColor: '#777777' },
        style
      ] }
      { ...props }/>
  )
}

const styles = StyleSheet.create({
  container: {
    width: '100%',
    height: '100%',
    backgroundColor: '#FFFFFF',
    resizeMode: 'cover'
  }
})
