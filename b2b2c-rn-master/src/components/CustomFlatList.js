/**
 * Created by Andste on 2018/11/7.
 */
import React from 'react'
import {
  View,
  FlatList
} from 'react-native'
import { DataEmpty } from '../components/EmptyViews'
import { isIphoneX } from 'react-native-iphone-x-helper'

export default function ({ data, ListFooterBgColor, ...props }) {
  return (
    <FlatList
      data={ data }
      keyExtractor={ (_, index) => String(index) }
      onEndReachedThreshold={ 0.1 }
      ListEmptyComponent={ () => <DataEmpty/> }
      ListFooterComponent={ data[0]
        ? <View style={ [{
          height: isIphoneX() ? 30 : 0,
          backgroundColor: ListFooterBgColor || '#FFFFFF'
        }] }/>
        : undefined }
      scrollEventThrottle={16}
      { ...props }
    />
  )
}
