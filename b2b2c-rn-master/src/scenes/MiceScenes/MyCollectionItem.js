/**
 * Created by Andste on 2018/11/7.
 */
import React from 'react'
import {
  View,
  Image,
  TouchableOpacity,
  ScrollView,
  StyleSheet
} from 'react-native'
import { colors } from '../../../config'
import { Screen, Foundation } from '../../utils'
import { F16Text, F12Text } from '../../widgets/Text'
import { TextLabel } from '../../widgets'

export function CollectionGoodsItem({ data, delItem, toDetail }) {
  return (
    <View style={ styles.g_container }>
      <Image style={ styles.g_item_image } source={ { uri: data['goods_img'] } }/>
      <View style={ styles.g_item_detail }>
        <F16Text numberOfLines={ 2 }>{ data['goods_name'] }</F16Text>
        <F16Text style={ { color: colors.main } }>￥ { Foundation.formatPrice(data['goods_price']) }</F16Text>
        <View style={ { flexDirection: 'row' } }>
          <TextLabel
            text="删除"
            style={ [styles.g_item_label_btn, styles.g_item_label_btn_del] }
            textStyle={ { color: '#FFFFFF' } }
            onPress={ delItem }
          />
          <TextLabel
            text="查看详情"
            style={ styles.g_item_label_btn }
            onPress={ toDetail }
          />
        </View>
      </View>
    </View>
  )
}

export function CollectionShopItem({ data, toShop, toDetail, delItem }) {
  const hasGoods = data['goods_list'] && data['goods_list'].length
  return (
    <View style={ [styles.s_container, !hasGoods && { height: 50 }] }>
      <View style={ styles.s_item_top }>
        <View style={ { flexDirection: 'row' } }>
          <Image style={ styles.s_shop_logo } source={ { uri: data['logo'] } }/>
          <View style={ styles.s_shop_name }>
            <F16Text>{ data['shop_name'] }</F16Text>
            { /*<F12Text>商品数：{ item['goods_num'] || 0 }</F12Text>*/ }
          </View>
        </View>
        <TextLabel style={ styles.s_item_label_btn } text="进店看看" onPress={ toShop }/>
        <TextLabel
            text="删除"
            style={ [styles.s_item_label_btn, { backgroundColor:colors.main }] }
            textStyle= { { color: '#FFFFFF' } }
            onPress={ delItem }
        />
      </View>
      { hasGoods ? (
        <View style={ styles.s_item_bottom }>
          <ScrollView horizontal={ true } showsHorizontalScrollIndicator={ false }>
            { data['goods_list'].map((goods, index) => {
              return (
                <GoodsItem
                  key={ index }
                  data={ goods }
                  onPress={ () => toDetail(goods) }
                />
              )
            }) }
          </ScrollView>
        </View>
      ) : undefined }
    </View>
  )
}

const GoodsItem = ({ data, ...props }) => {
  return (
    <TouchableOpacity style={ styles.s_goods_item } { ...props }>
      <Image style={ styles.s_goods_image } source={ { uri: data['thumbnail'] } }/>
      <View style={ styles.s_goods_price_view }>
        <F12Text style={ styles.s_goods_price }>￥{ Foundation.formatPrice(data['price']) }</F12Text>
      </View>
    </TouchableOpacity>
  )
}

const styles = StyleSheet.create({
  g_container: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    width: Screen.width,
    height: 135,
    paddingHorizontal: 10,
    paddingVertical: 15,
    backgroundColor: '#FFFFFF'
  },
  g_item_image: {
    width: 105,
    height: 105,
    borderColor: colors.cell_line_backgroud,
    borderWidth: 1,
    borderRadius: 3
  },
  g_item_detail: {
    justifyContent: 'space-between',
    width: Screen.width - 20 - 105 - 15,
    height: 105,
    marginLeft: 15
  },
  g_item_label_btn: {
    height: 25,
    marginRight: 10,
    marginBottom: 0
  },
  g_item_label_btn_del: {
    borderColor: colors.main,
    backgroundColor: colors.main
  },
  s_container: {
    justifyContent: 'space-between',
    alignItems: 'center',
    width: Screen.width,
    height: 180,
    paddingHorizontal: 10,
    backgroundColor: '#FFFFFF'
  },
  s_item_top: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    width: Screen.width,
    height: 50,
    paddingHorizontal: 10,
    borderColor: colors.cell_line_backgroud,
    borderWidth: Screen.onePixel
  },
  s_shop_logo: {
    width: 90,
    height: 30,
    borderColor: colors.cell_line_backgroud,
    borderWidth: Screen.onePixel
  },
  s_shop_name: {
    marginLeft: 10
  },
  s_item_label_btn: {
    marginBottom: 0,
    marginRight: 0
  },
  s_item_bottom: {
    justifyContent: 'center',
    width: Screen.width,
    height: 130,
    paddingVertical: 10
  },
  s_goods_item: {
    width: 110,
    height: 110,
    marginRight: 15,
    overflow: 'hidden'
  },
  s_goods_image: {
    width: 110,
    height: 110,
    borderRadius: 5
  },
  s_goods_price_view: {
    position: 'absolute',
    bottom: 0,
    right: 0,
    paddingHorizontal: 3,
    paddingVertical: 2,
    backgroundColor: 'rgba(0,0,0,0.5)'
  },
  s_goods_price: {
    color: '#FFFFFF'
  }
})
