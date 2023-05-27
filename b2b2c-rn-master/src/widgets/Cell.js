/**
 * Created by Andste on 2017/8/9.
 */
import React from 'react'
import {
  StyleSheet,
  TouchableHighlight,
  View
} from 'react-native'
import { colors } from '../../config'
import { Screen } from '../utils'
import { BaseText, F16Text } from '../widgets/Text'
import Icon from 'react-native-vector-icons/Ionicons'

export default function ({ onPress, style, disabled, icon, title, titleStyle, label, arrow = true, line = '' }) {
  const _onPress = () => {
    !disabled && typeof onPress === 'function' && onPress()
  }
  let _label = typeof label === 'object'
    ? label
    : <F16Text style={ styles.label_text }>{ label }</F16Text>
  return (
    <TouchableHighlight
      style={ style }
      underlayColor="#ADADAD"
      onPress={ _onPress }
      activeOpacity={ disabled ? 1 : .5 }
    >
      <View style={ [styles.container, disabled && { backgroundColor: colors.disabled_background }] }>
        <View style={ styles.left }>
          { icon && <View style={ styles.icon }>{ icon }</View> }
          { typeof title === 'object' ? title : <BaseText style={ titleStyle }>{ title }</BaseText> }
        </View>
        <View style={ styles.right }>
          <View style={ styles.label_view }>
            { _label }
          </View>
          { arrow ? (
            typeof arrow === 'object'
              ? arrow
              : <Icon
                name="ios-arrow-forward"
                color="#A8A9AB"
                size={ 20 }
              />
          ) : undefined }
        </View>
        { line.indexOf('bottom') > -1 && <View style={ styles.line }/> }
      </View>
    </TouchableHighlight>
  )
}

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    width: Screen.width,
    minHeight: 48,
    padding: 10,
    backgroundColor: '#FFFFFF'
  },
  left: {
    flexDirection: 'row',
    justifyContent: 'flex-start',
    alignItems: 'center',
    maxWidth: Screen.width - 120
  },
  right: {
    flexDirection: 'row',
    justifyContent: 'flex-end',
    alignItems: 'center',
    maxWidth: Screen.width - 120
  },
  icon: {
    marginRight: 5
  },
  label_view: {
    marginRight: 5
  },
  label_text: {
    color: '#BCBDBF'
  },
  line: {
    position: 'absolute',
    bottom: 0,
    width: Screen.width - 10,
    marginLeft: 10,
    height: Screen.onePixel,
    backgroundColor: colors.cell_line_backgroud
  }
})
