/**
 * Created by Andste on 2017/7/22.
 */
import React, { PureComponent } from 'react'
import {
  Text,
  TouchableOpacity,
  StyleSheet
} from 'react-native'
import { colors } from '../../../config'

export default class RootListItem extends PureComponent {
  render() {
    const { selected, title, ...props } = this.props
    return (
      <TouchableOpacity
        style={ [styles.container, { backgroundColor: selected ? colors.gray_background : '#FFFFFF' }] }
        { ...props }
      >
        <Text
          style={ [styles.item_title, { color: selected ? colors.main : colors.text }] }
        >{ title }</Text>
      </TouchableOpacity>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    height: 55,
    backgroundColor: '#FFFFFF',
    justifyContent: 'center'
  },
  item_title: {
    fontSize: 14,
    alignSelf: 'center'
  }
})
