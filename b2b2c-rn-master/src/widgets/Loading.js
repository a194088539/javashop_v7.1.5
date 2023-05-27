/**
 * Created by Andste on 2017/8/14.
 */

import React from 'react'
import {
  View,
  Image,
  Modal,
  StyleSheet
} from 'react-native'
import { Screen } from '../utils'


export default function ({ show, ...props }) {
  return (
    <Modal
      style={ styles.container }
      transparent={ true }
      animationType="fade"
      visible={ show }
      onRequestClose={ () => {} }
      { ...props }
    >
      <View style={ styles.loading_view }>
        <Image style={ styles.icon } source={ require('../images/icon-loading.gif') }/>
      </View>
    </Modal>
  )
}

const styles = StyleSheet.create({
  container: {
    width: Screen.width,
    height: Screen.height,
    position: 'absolute',
    zIndex: 99999
  },
  loading_view: {
    justifyContent: 'center',
    alignItems: 'center',
    width: Screen.width,
    height: Screen.height
    //backgroundColor: 'rgba(0,0,0,.3)'
  },
  icon: {
    width: 35,
    height: 35
  }
})
