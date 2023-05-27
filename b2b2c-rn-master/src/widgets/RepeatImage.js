/**
 * Created by Andste on 2019-01-17.
 */
import React from 'react'
import {
  View,
  Image,
  StyleSheet
} from 'react-native'
import { Screen } from '../utils'

export default function ({ source, imgWidth, imgStyle }) {
  let images = []
  for (let i = 0; i < Math.ceil(Screen.width / imgWidth); i++) {
    images.push((
      <Image style={ imgStyle } source={ source } key={ i }/>
    ))
  }
  return (
    <View style={ styles.container }>
      { images }
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row'
  }
})
