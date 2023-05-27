/**
 * Created by Andste on 2018/11/8.
 */
import React from 'react'
import {
  View,
  TouchableOpacity,
  Text,
  StyleSheet
} from 'react-native'
import Icon from 'react-native-vector-icons/Ionicons'
import { colors } from '../../../config'
import { Screen, Foundation } from '../../utils'
import { Checkbox, Tag } from '../../widgets'
import { BaseText, F16Text } from '../../widgets/Text'

export default function ({ data, setDef, editAdd, delAdd, ...props }) {
  return (
    <TouchableOpacity style={ styles.container } activeOpacity={ 1 } { ...props }>
      <View style={ styles.info_view }>
        <BaseText>{ data['name'] }</BaseText>
        <BaseText style={ { marginLeft: 5 } }>{ Foundation.secrecyMobile(data['mobile']) }</BaseText>
        <View style={ styles.tags }>
          { data['ship_address_name'] ? <Tag color="#1E9DFF" text={ data['ship_address_name'] }/> : null }
        </View>
      </View>
      <View style={ styles.addr_view }>
        <F16Text style={ styles.addr_text }>
          { `${ data['province'] }${ data['city'] }${ data['county'] }${ data['town'] } ${ data['addr'] }` }
        </F16Text>
      </View>
      <View style={ styles.line }/>
      <View style={ styles.oper_view }>
        <Checkbox
          checked={ data['def_addr'] === 1 }
          label={ data['def_addr'] === 1 ? '默认地址' : '设为默认' }
          labelStyle={ styles.btn_label }
          onPress={ setDef }
        />
        <View style={ styles.oper_btns }>
          <TouchableOpacity style={ styles.oper_btn } onPress={ editAdd }>
            <Icon name="ios-create-outline" size={ 20 }/>
            <Text style={ styles.btn_label }>编辑</Text>
          </TouchableOpacity>
          <TouchableOpacity style={ styles.oper_btn } onPress={ delAdd }>
            <Icon name="ios-trash-outline" size={ 20 }/>
            <Text style={ styles.btn_label }>删除</Text>
          </TouchableOpacity>
        </View>
      </View>
    </TouchableOpacity>
  )
}

const styles = StyleSheet.create({
  container: {
    width: Screen.width,
    paddingHorizontal: 10,
    paddingVertical: 10,
    backgroundColor: '#FFFFFF'
  },
  info_view: {
    flexDirection: 'row',
    alignItems: 'center',
    marginTop: 5
  },
  tags: {
    marginLeft: 5
  },
  addr_view: {
    marginTop: 5
  },
  addr_text: {
    color: '#686868'
  },
  line: {
    width: Screen.width - 10,
    height: Screen.onePixel,
    marginTop: 10,
    backgroundColor: colors.cell_line_backgroud
  },
  oper_view: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingTop: 5
  },
  oper_btns: {
    flexDirection: 'row'
  },
  oper_btn: {
    flexDirection: 'row',
    alignItems: 'center',
    marginLeft: 10
  },
  btn_label: {
    color: '#686868',
    fontSize: 16,
    marginLeft: 3
  }
})
