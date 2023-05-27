/**
 * Created by Andste on 2018/11/19.
 */
import React from 'react'
import {
  View,
  Image,
  Text,
  TouchableOpacity,
  StyleSheet
} from 'react-native'
import Swipeout from 'react-native-swipeout'
import { Grayscale } from 'react-native-color-matrix-image-filters'
import { navigate } from '../../navigator/NavigationService'
import { Checkbox } from '../../widgets'
import { colors } from '../../../config'
import { Screen } from '../../utils'
import { Price, Quantity, SkuSpec } from '../../widgets'
import { F16Text } from '../../widgets/Text'

export default function ({ data, onCheck, onUpdateNumBySymbol, onUpdateNumByInput, showActivitySelect, ...props }) {
  const single_list = data['single_list']
  const checkedActivity = single_list.filter(item => item['is_check'] === 1)
  const proStr = checkedActivity[0] ? checkedActivity[0]['title'] : '不参加活动'
  const invalid = data['invalid'] === 1
  return (
    <View style={ styles.container }>
      { data['error_message'] ? (
        <View style={ styles.error_message }>
          <Text style={ styles.error_message_text }>{ data['error_message'] }</Text>
        </View>
      ) : undefined }
      <Swipeout { ...props }>
        <View style={ styles.item }>
          { invalid ? (
            <Text style={ styles.invalid_text }>已失效</Text>
          ) : (
            <Checkbox checked={ data.checked } onPress={ onCheck } activeOpacity={ 1 }/>
          ) }
          <TouchableOpacity
            style={ styles.action }
            onPress={ () => navigate('Goods', { id: data['goods_id'] }) }
          >
            <View style={ styles.image_view }>
              { invalid ? (
                <Grayscale>
                  <Image style={ styles.goods_image } source={ { uri: data['goods_image'] } }/>
                </Grayscale>
              ) : (
                <Image style={ styles.goods_image } source={ { uri: data['goods_image'] } }/>
              ) }
              { !data['is_ship'] ? (
                <View style={ styles.no_ship }>
                  <Text style={ styles.no_ship_text }>该地区无货</Text>
                </View>
              ) : undefined }
            </View>
            <View style={ styles.right }>
              <View style={ styles.right_text }>
                <F16Text
                  style={ [invalid && { color: colors.invalid_text }] }
                  numberOfLines={ 2 }
                >{ data['name'] }</F16Text>
                <SkuSpec
                  style={ [
                    { marginTop: 2 },
                    invalid && { color: colors.invalid_text }
                  ] }
                  data={ data }
                />
                { data['promotion_tags'].length ? (
                  <View style={ styles.pro_tag_view }>
                    { data['promotion_tags'].map((item, index) => (
                      <View style={ styles.pro_tag } key={ index }>
                        <Text style={ styles.pro_tag_text }>{ item }</Text>
                      </View>
                    )) }
                  </View>
                ) : undefined }
              </View>
              <View style={ styles.right_bottom }>
                <Price style={ [invalid && { color: colors.invalid_text}] } price={ data['purchase_price'] }/>
                { !invalid ? (
                  <Quantity
                    defaultValue={ String(data['num']) }
                    onPress={ onUpdateNumBySymbol }
                    onEndEditing={ onUpdateNumByInput }
                  />
                ) : undefined }
              </View>
            </View>
          </TouchableOpacity>
        </View>
      </Swipeout>
      { single_list && single_list[0] ? (
        <View style={ styles.activity_view }>
          <View style={ styles.act_arrow }/>
          <TouchableOpacity
            style={ styles.act_inner }
            onPress={ showActivitySelect }
          >
            <F16Text>促销</F16Text>
            <Text style={ styles.act_text }>{ proStr }</Text>
          </TouchableOpacity>
        </View>
      ) : undefined }
    </View>
  )
}

const RIGHT_WIDTH = Screen.width - 40 - 100 - 10
const styles = StyleSheet.create({
  container: {
    width: Screen.width
  },
  item: {
    flexDirection: 'row',
    alignItems: 'center',
    width: Screen.width,
    backgroundColor: '#FFFFFF',
    paddingLeft: 10
  },
  action: {
    flexDirection: 'row',
    alignItems: 'center',
    marginLeft: 5
  },
  image_view: {
    width: 90,
    height: 90
  },
  goods_image: {
    width: 90,
    height: 90,
    borderColor: '#CCCCCC',
    borderWidth: Screen.onePixel
  },
  right: {
    justifyContent: 'space-between',
    padding: 10
  },
  right_text: {
    width: RIGHT_WIDTH
  },
  right_bottom: {
    flexDirection: 'row',
    flexWrap:'wrap',
    width: Screen.width - 150,
    justifyContent: 'space-between',
    alignItems: 'center'
  },
  activity_view: {
    width: Screen.width,
    backgroundColor: '#FFF'
  },
  act_arrow: {
    position: 'absolute',
    zIndex: 2,
    top: 10,
    left: 30 + 10 + 15,
    width: 15,
    height: 15,
    backgroundColor: '#fdf3f3',
    transform: [{ rotate: '45deg' }]
  },
  act_inner: {
    flexDirection: 'row',
    alignItems: 'center',
    width: Screen.width - 20 - 30,
    height: 40,
    padding: 10,
    marginTop: 15,
    marginLeft: 30 + 10,
    backgroundColor: '#fdf3f3',
    marginBottom: 10
  },
  act_text: {
    color: '#7f828b',
    marginLeft: 10
  },
  invalid_text: {
    width: 30,
    fontSize: 8,
    color: colors.invalid_text
  },
  pro_tag_view: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    marginTop: 2
  },
  pro_tag: {
    borderWidth: Screen.onePixel,
    borderColor: colors.main,
    marginRight: 3
  },
  pro_tag_text: {
    color: colors.main,
    fontSize: 12,
    padding: 1
  },
  error_message: {
    justifyContent: 'center',
    backgroundColor: '#FFF',
    paddingLeft: 15 + 30
  },
  error_message_text: {
    fontSize: 14,
    color: colors.main
  },
  no_ship: {
    position: 'absolute',
    justifyContent: 'center',
    alignItems: 'center',
    width: 90,
    height: 90,
    backgroundColor: 'rgba(0,0,0,.7)'
  },
  no_ship_text: {
    fontSize: 14,
    fontWeight: '600',
    color: colors.main
  }
})
