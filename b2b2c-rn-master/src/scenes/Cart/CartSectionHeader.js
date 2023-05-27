/**
 * Created by Andste on 2018/11/19.
 */
import React from 'react'
import {
  View,
  Text,
  Image,
  TouchableOpacity,
  StyleSheet
} from 'react-native'
import Icon from 'react-native-vector-icons/Ionicons'
import { navigate } from '../../navigator/NavigationService'
import { colors } from '../../../config'
import { Screen } from '../../utils'
import { Checkbox } from '../../widgets'

export default function ({ data, onPress }) {
  const invalid = data['invalid'] === 1
  return (
    <View style={ styles.container }>
      { invalid ? (
        <View style={ styles.invalid_view }/>
      ) : (
        <Checkbox checked={ data.checked } onPress={ onPress }/>
      ) }
      <View>
        <View style={ styles.oper_view }>
          <TouchableOpacity
            style={ styles.to_shop }
            onPress={ () => navigate('Shop', { id: data['seller_id'] }) }
          >
            <Image style={ styles.icon } source={ require('../../images/icon-shop.png') }/>
            <Text style={ styles.shop_name }>{ data['seller_name'] }</Text>
          </TouchableOpacity>
          <TouchableOpacity
            onPress={ () => {
              navigate('CartCoupon', { shop_id: data['seller_id'] })
            } }
          >
            <Text>| 领券</Text>
          </TouchableOpacity>
        </View>
        { data['promotion_notice'] ? (
          <View style={ styles.pro_notice }>
            <Text style={ styles.pro_notice_text }>{ data['promotion_notice'] }</Text>
          </View>
        ) : undefined }
      </View>
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    alignItems: 'center',
    width: Screen.width,
    backgroundColor: '#FFFFFF',
    paddingLeft: 10,
    paddingTop: 10
  },
  invalid_view: {
    width: 30
  },
  oper_view: {
    width: Screen.width - 20 - 30,
    flexDirection: 'row',
    justifyContent: 'space-between'
  },
  pro_notice: {
    marginLeft: 5
  },
  pro_notice_text: {
    fontSize: 14,
    color: colors.main
  },
  to_shop: {
    flexDirection: 'row',
    marginLeft: 5
  },
  icon: {
    width: 18,
    height: 18
  },
  shop_name: {
    color: colors.text,
    fontSize: 17,
    marginLeft: 3,
    marginRight: 5
  }
})
