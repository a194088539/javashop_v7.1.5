/**
 * Created by Andste on 2019-01-17.
 */
import React from 'react'
import {
  View,
  Text,
  TouchableOpacity,
  StyleSheet
} from 'react-native'
import { colors } from '../../../config'
import { Screen } from '../../utils'
import { RepeatImage } from '../../widgets'
import Icon from 'react-native-vector-icons/Ionicons'

export default function ({ data, ...props }) {
  const add_tit = `${data['name']} ${data['mobile']}`
  const add_str = `${data['province']} ${data['city']} ${data['county']} ${data['town']} ${data['addr']}`
  return (
    <TouchableOpacity style={ styles.container } { ...props }>
      { data ? (
        <View style={ styles.address_view }>
          <Icon name="ios-pin-outline" size={ 22 }/>
          <View style={ styles.address_info }>
            <View style={ styles.address_title_view }>
              <Text style={ styles.address_title }>{ add_tit }</Text>
              { data['ship_address_name'] ? (
                <View style={ styles.address_alias }>
                  <Text style={ styles.address_alias_text }>{ data['ship_address_name'] }</Text>
                </View>
              ) : undefined }
            </View>
            <Text>{ add_str }</Text>
          </View>
          <Icon
            name="ios-arrow-forward"
            color="#A8A9AB"
            size={ 20 }
          />
        </View>
      ) : (
        <View style={ styles.container }>
          <Text style={ styles.no_add_text }>选择或新建一个地址</Text>
        </View>
      ) }
      <RepeatImage
        source={ require('../../images/icon-address-line.png') }
        imgWidth={ 50 }
        imgStyle={ { height: 8 } }
      />
    </TouchableOpacity>
  )
}

const styles = StyleSheet.create({
  container: {
    alignItems: 'center',
    justifyContent: 'center',
    height: 78,
    backgroundColor: '#FFF'
  },
  address_view: {
    flexDirection: 'row',
    alignItems: 'center',
    width: Screen.width,
    height: 90,
    paddingHorizontal: 10
  },
  address_info: {
    width: Screen.width - 25 - 25,
    marginLeft: 10
  },
  address_title_view: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 5
  },
  address_title: {
    fontSize: 18,
    color: colors.text
  },
  address_alias: {
    borderColor: colors.tag,
    borderWidth: Screen.onePixel,
    padding: 2,
    marginLeft: 5
  },
  address_alias_text: {
    fontSize: 12,
    color: colors.tag
  },
  no_add_text: {
    color: colors.text,
    fontSize: 18
  }
})
