/**
 * Created by Andste on 2018/11/19.
 */
import React, { Component } from 'react'
import {
  View
} from 'react-native'
import CartScene from './CartScene'
import { CartBadge } from '../../components'
import Icon from 'react-native-vector-icons/Ionicons'

export default class Cart extends Component {
  static navigationOptions = () => ({
    tabBarLabel: '购物车',
    tabBarIcon: ({ focused, tintColor }) => (
      <View>
        <CartBadge/>
        <Icon name={ focused ? 'ios-cart' : 'ios-cart-outline' } size={ 30 } color={ tintColor }/>
      </View>
    )
  })
  
  render() {
    return <CartScene/>
  }
}
