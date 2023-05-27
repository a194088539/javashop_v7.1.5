/**
 * Created by Andste on 2018/10/15.
 */
import React from 'react'
import {
  Text,
  StyleSheet
} from 'react-native'
import { colors } from '../../config'
import { Foundation } from '../utils'

export default function Price({ advanced, seckill, style, price, unit = '￥', scale = 1, ...props }) {
  if (price === undefined || price === null) return <Text/>
  if (!advanced) return (
    <Text
      style={ [styles.container, style] }
      { ...props }
    >
      { unit }{ Foundation.formatPrice(price) }
    </Text>
  )
  price = typeof price === 'string' ? Number(price) : price
  price = price.toFixed(2).toString()
  const subIndex = price.length - 3
  let Integer = price.substr(0, subIndex).replace(/(\d)(?=(?:\d{3})+$)/g, '$1,')
  let Decimal = price.substr(subIndex, price.length)
  let integerFontSize = 20
  if (price.length > 7){
    integerFontSize = 16
    Integer = Decimal === '.00' ? price.substr(0, subIndex) : price
    Decimal = ''
  }
  return (
    <Text style={ [styles.container, style] } { ...props }>
      <Text style={ { fontSize: 12 * scale } }>￥</Text>
      <Text style={ { fontSize: integerFontSize * scale } } allowFontScaling={ false }>{ Integer }</Text>
      <Text style={ { fontSize: 12 * scale } } allowFontScaling={ false }>{ Decimal }</Text>
    </Text>
  )
}

const styles = StyleSheet.create({
  container: {
    color: colors.main
  }
})
