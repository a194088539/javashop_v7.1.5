/**
 * Created by Andste on 2017/8/9.
 */
import React from 'react'
import { StyleSheet, Text } from 'react-native'
import { colors } from "../../config"

export function BaseText({ style, color, ...props } : Object) {
  return <Text style={ [styles.base, color && { color }, style] } { ...props } />
}

export function TitleText({ style, color, ...props }) : Object {
  return <Text style={ [styles.title, color && { color }, style] } { ...props } />
}

export function F16Text({ style, color, ...props }) : Object {
  return <Text style={ [styles.f16, color && { color }, style] } { ...props } />
}

export function F14Text({ style, color, ...props }) : Object {
  return <Text style={ [styles.f14, color && { color }, style] } { ...props } />
}

export function F12Text({ style, color, ...props }) : Object {
  return <Text style={ [styles.f12, color && { color }, style] } { ...props } />
}

let { text: font_color } = colors
const styles = StyleSheet.create({
  base: {
    fontSize: 18,
    color: font_color
  },
  title: {
    fontSize: 17,
    color: font_color
  },
  f16: {
    fontSize: 16,
    color: font_color
  },
  f14: {
    fontSize: 14,
    color: font_color
  },
  f12: {
    fontSize: 12,
    color: font_color
  }
})
