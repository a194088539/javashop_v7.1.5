import React from 'react'
import {
    View,
    StyleSheet
} from 'react-native'
import { Screen } from '../utils'
import { F16Text } from './Text'

export default function ({ title, content, style, textStyle }) {
    return (
      <View style={ [styles.item_label, style] }>
        <F16Text>{ title }：</F16Text>
        <F16Text style={ [{ maxWidth: Screen.width - 100 }, textStyle] }>{ content }</F16Text>
      </View>
    )
}

const styles = StyleSheet.create({ 
    item_label: {
        flexDirection: 'row',
        paddingHorizontal: 10,
        paddingVertical: 10,
        backgroundColor: '#FFFFFF'
    },
})