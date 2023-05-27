/**
 * Created by Andste on 2018/9/30.
 */

import React from 'react'
import {
  Dimensions,
  PixelRatio
} from 'react-native'

// 屏幕宽度
export const width = Dimensions.get('window').width
// 屏幕高度
export const height = Dimensions.get('window').height
// 屏幕最小像素
export const onePixel = 1 / PixelRatio.get()

