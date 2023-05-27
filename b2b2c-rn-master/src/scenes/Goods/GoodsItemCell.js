/**
 * Created by Andste on 2018/11/23.
 */
import React from 'react'
import {
  View,
  Text,
  TouchableOpacity,
  StyleSheet
} from 'react-native'
import Icon from 'react-native-vector-icons/Ionicons'
import { colors } from '../../../config'
import { Screen } from '../../utils'

export default function ({ style, label, content, icon, border = '', scale, onPress }) {
  if (typeof content === 'string') {
    content = <Text style={ styles.content_text }>{ content }</Text>
  }
  if (typeof icon === 'boolean') {
    icon = <Icon style={ styles.icon } name="ios-arrow-forward" size={ 25 }/>
  } else if (icon === 'more') {
    icon = <Icon style={ styles.icon } name="ios-more" size={ 25 }/>
  }
  return (
    <TouchableOpacity
      style={ [styles.container, style] }
      onPress={ onPress }
      activeOpatiy={ onPress ? 0.5 : 1 }
    >
      { border.indexOf('top') !== - 1 ? <View style={ styles.line }/> : undefined }
      <View style={ styles.main }>
        { label && <Text style={ styles.label }>{ label }</Text> }
        <View style={ [styles.content, { width: label ? Screen.width - 85 : Screen.width - 45 }] }>{ content }</View>
        <View style={ styles.view_icon }>{ icon }</View>
      </View>
      { (border === '' || border.indexOf('bottom') !== - 1) ? <View style={ styles.line }/> : undefined }
    </TouchableOpacity>
  )
}

const styles = StyleSheet.create({
  container: {
    flexDirection: 'column',
    alignItems: 'flex-end',
    backgroundColor: '#FFFFFF'
  },
  view_icon: {
    width: 25,
    alignItems: 'flex-end'
  },
  icon: {
    color: '#777777'
  },
  main: {
    flexDirection: 'row',
    justifyContent: 'flex-start',
    alignItems: 'center',
    padding: 10
  },
  label: {
    width: 40,
    fontSize: 16,
    color: '#777777',
    textAlign: 'left'
  },
  content_text: {
    fontSize: 16,
    color: colors.text
  },
  line: {
    width: Screen.width - 10,
    height: Screen.onePixel,
    backgroundColor: colors.cell_line_backgroud
  }
})
