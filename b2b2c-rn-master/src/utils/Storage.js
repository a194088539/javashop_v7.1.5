/**
 * Created by Andste on 2018/9/30.
 */

import React from 'react'
import { AsyncStorage } from 'react-native'

export const setItem = AsyncStorage.setItem
export const getItem = AsyncStorage.getItem
export const removeItem = AsyncStorage.removeItem