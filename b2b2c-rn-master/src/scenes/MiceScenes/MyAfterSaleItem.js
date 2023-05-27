import React from 'react'
import {
  View,
  TouchableOpacity,
  StyleSheet
} from 'react-native'
import { navigate } from '../../navigator/NavigationService'
import { colors } from '../../../config'
import { Screen, Foundation } from '../../utils'
import { F16Text } from '../../widgets/Text'
import { TextLabel } from '../../widgets'



export default function ({ data, onRefresh }) {
  return (
    <TouchableOpacity
      activeOpacity={ 1 }
      onPress={ () => navigate('AfterSaleDetail', { data, callback: onRefresh }) }
      style={ styles.container }
    >
      <View style={ styles.header }>
        <F16Text>售后单号：{ data['sn'] }</F16Text>
      </View>
      <View style={ styles.body }>
        <F16Text>售后状态：<F16Text style={ { color: '#3985FF' } }>{ data['refund_status_text'] }</F16Text></F16Text>
        <F16Text>退款金额：<F16Text style={ { color: colors.main } }>￥{ Foundation.formatPrice(data['refund_price']) }</F16Text></F16Text>
      </View>
      <View style={ styles.footer }>
          <TextLabel
              style={ styles.footer_btn }
              text="查看详情"
              onPress={ () => navigate('AfterSaleDetail', { data, callback: onRefresh }) }/>
        </View>
      <ItemLine/>
    </TouchableOpacity>
    
  )
}

const ItemLine = () => (<View style={ styles.item_line }/>)

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'space-between',
    alignItems: 'center',
    height: 150,
    backgroundColor: '#FFFFFF'
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    width: Screen.width,
    height: 45,
    paddingHorizontal: 15
  },
  body: {
    width: Screen.width,
    height: 105 - 50,
    paddingHorizontal: 15
  },

  b_price_view: {
    justifyContent: 'center',
    alignItems: 'flex-end',
    paddingHorizontal: 15,
    borderColor: colors.cell_line_backgroud,
    borderBottomWidth: Screen.onePixel
  },
  footer: {
    flexDirection: 'row',
    justifyContent: 'flex-end',
    alignItems: 'center',
    width: Screen.width,
    height: 40,
    paddingHorizontal: 15
  },
  footer_btn: {
    paddingVertical: 2,
    marginRight: 0,
    marginLeft: 10,
    marginBottom: 0,
  },
  item_line: {
    width: Screen.width,
    height: 10,
    backgroundColor: colors.gray_background
  },
  content_line: {
    width: Screen.width,
    height: 3,
    backgroundColor: colors.gray_background
  },
})
