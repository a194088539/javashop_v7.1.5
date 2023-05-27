/**
 * Created by Andste on 2018-12-21.
 */

import React, { Component } from 'react'
import {
  View,
  Text,
  TouchableOpacity,
  StyleSheet
} from 'react-native'
import { Screen } from '../utils'
import ModalBox from 'react-native-modalbox'
import Icon from 'react-native-vector-icons/Ionicons'

export default function ({ style, title, content, header, headerStyle, duration, onClosed, children, ...props }) {
  return (
    <ModalBox
      style={ [styles.container, style] }
      position="bottom"
      animationDuration={ duration || 350 }
      swipeToClose={ false }
      coverScreen={ true }
      backdropOpacity={ .65 }
      onClosed={ onClosed }
      { ...props }
    >
      <View style={ [styles.header, headerStyle] }>
        { header ? header : <Text style={ styles.title } numberOfLines={ 1 }>{ title || '没有标题' }</Text> }
        <TouchableOpacity style={ styles.close } onPress={ onClosed }>
          <Icon style={ styles.icon } name="ios-close" size={ 30 }/>
        </TouchableOpacity>
      </View>
      { children || content }
    </ModalBox>
  )
}

const styles = StyleSheet.create({
  container: {
    width: Screen.width,
    height: Screen.height - 130,
    backgroundColor: '#FFFFFF',
    shadowOffset: {
      width: 0,
      height: 0
    },
    shadowColor: '#000000',
    shadowOpacity: .75,
    shadowRadius: 5
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    width: Screen.width,
    height: 50,
    paddingLeft: 10,
    paddingRight: 10
  },
  title: {
    fontSize: 18,
    color: '#777777'
  },
  close: {
    justifyContent: 'center',
    alignItems: 'center',
    width: 40,
    height: 50,
    position: 'absolute',
    top: 0,
    right: 0
  }
})
