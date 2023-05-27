/**
 * Created by Andste on 2017/8/10.
 */
import React from 'react'
import {
  View
} from 'react-native'

export default function ({ children, style, marginTop, marginBottom, ...props }) {
  let c_len = Array.isArray(children) ? children.length - 1 : 0
  let _children = React.Children.map(children, (child, index) => {
    if (!child) return null
    return React.cloneElement(child, { line: index !== c_len ? 'bottom' : '' })
  })
  return (
    <View
      style={ [
        { backgroundColor: '#FFFFFF' },
        style,
        marginTop && { marginTop: 10 },
        marginBottom && { marginBottom: 10 }
      ] }
      { ...props }
    >
      { _children }
    </View>
  )
}
