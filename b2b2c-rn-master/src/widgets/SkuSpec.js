/**
 * Created by Andste on 2019-01-17.
 */
import React from 'react'
import {
  Text,
  StyleSheet
} from 'react-native'
import { colors } from '../../config'

export default function ({ data, style, ...props }) {
  if (!data['spec_list'] || !data['spec_list'].length) {
    return null
  }
  const sku_spec = data['spec_list'].map(spec => spec.spec_value).join(' - ')
  return (
    <Text style={ [styles.container, style] } { ...props }>
      { sku_spec }
    </Text>
  )
}

const styles = StyleSheet.create({
  container: {
    color: colors.sku_spec,
    fontSize: 12
  }
})
