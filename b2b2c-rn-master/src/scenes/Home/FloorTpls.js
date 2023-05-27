/**
 * Created by Andste on 2018/10/13.
 */
import React from 'react'
import {
  View,
  Text,
  StyleSheet
} from 'react-native'
import { colors } from '../../../config'
import { Screen } from '../../utils'
import Swiper from 'react-native-swiper'
import { Image, Price } from '../../widgets'
import OptNavigate from './OptNavigate'

// 单列单张大图模块
export function tpl_23(index, blockList) {
  const block = blockList[0]
  return (
    <View key={ index }>
      <OptNavigate data={ block['block_opt'] }>
        <Image uri={ block['block_value'] } style={ styles.tpl_23_image }/>
      </OptNavigate>
    </View>
  )
}

// 左一右二图片模块
export function tpl_24(index, blockList) {
  const left_block = blockList[0]
  const right_blocks = blockList.slice(1)
  return (
    <View style={ styles.tpl_24 } key={ index }>
      <OptNavigate data={ left_block['block_opt'] }>
        <Image uri={ left_block['block_value'] } style={ styles.tpl_24_left_image }/>
      </OptNavigate>
      <View style={ styles.tpl_24_right }>
        { right_blocks.map((item, _index) => (
          <OptNavigate data={ item['block_opt'] } key={ _index }>
            <Image uri={ item['block_value'] } style={ styles.tpl_24_right_image }/>
          </OptNavigate>
        )) }
      </View>
    </View>
  )
}

// 左二右一图片模块
export function tpl_25(index, blockList) {
  const left_blocks = blockList.slice(0, 2)
  const right_block = blockList[2]
  return (
    <View style={ styles.tpl_25 } key={ index }>
      <View style={ styles.tpl_25_left }>
        { left_blocks.map((item, _index) => (
          <OptNavigate data={ item['block_opt'] } key={ _index }>
            <Image uri={ item['block_value'] } style={ styles.tpl_24_right_image }/>
          </OptNavigate>
        )) }
      </View>
      <OptNavigate data={ right_block['block_opt'] }>
        <Image uri={ right_block['block_value'] } style={ styles.tpl_25_right_image }/>
      </OptNavigate>
    </View>
  )
}

// 三列单行图片模块
export function tpl_26(index, blockList) {
  return (
    <View style={ styles.tpl_26 } key={ index }>
      { blockList.map((item, _index) => (
        <OptNavigate data={ item['block_opt'] } key={ _index }>
          <Image uri={ item['block_value'] } style={ styles.tpl_26_image }/>
        </OptNavigate>
      )) }
    </View>
  )
}

// 五列单行小图模块
export function tpl_27(index, blockList) {
  return (
    <View style={ styles.tpl_27 } key={ index }>
      { blockList.map((item, _index) => (
        <OptNavigate data={ item['block_opt'] } key={ _index }>
          <Image uri={ item['block_value'] } style={ styles.tpl_27_image }/>
        </OptNavigate>
      )) }
    </View>
  )
}

// 轮播图模块
export function tpl_28(index, blockList) {
  return (
    <View key={ index }>
      <Swiper
          height={Screen.height / 4}
          loop={true}
          autoplay={true}
          showsPagination={true}
          paginationStyle={{bottom: 3}}
      >
          {blockList.map((item, _index) => (
            <OptNavigate data={ item['block_opt'] } key={ _index }>
              <Image uri={ item['block_value'] } style={ styles.tpl_28_image }/>
            </OptNavigate>
          ))}
      </Swiper>
    </View>
  )
}

// 四列单行图片模块
export function tpl_29(index, blockList) {
  return (
    <View style={ styles.tpl_29 } key={ index }>
      { blockList.map((item, _index) => (
        <OptNavigate data={ item['block_opt'] } key={ _index }>
          <Image uri={ item['block_value'] } style={ styles.tpl_29_image }/>
        </OptNavigate>
      )) }
    </View>
  )
}

// 标题或间隔性模块
export function tpl_30(index, blockList) {
  const block = blockList[0]
  return (
    <View key={ index }>
      <OptNavigate data={ block['block_opt'] }>
        <Image uri={ block['block_value'] } style={ styles.tpl_30_image }/>
      </OptNavigate>
    </View>
  )
}

// 四列单行小图模块
export function tpl_31(index, blockList) {
  return (
    <View style={ styles.tpl_31 } key={ index }>
      { blockList.map((item, _index) => (
        <OptNavigate data={ item['block_opt'] } key={ _index }>
          <Image uri={ item['block_value'] } style={ styles.tpl_31_image }/>
        </OptNavigate>
      )) }
    </View>
  )
}

// 左一右二竖排模板
export function tpl_32(index, blockList) {
  const left_block = blockList[0]
  const right_blocks = blockList.slice(1)
  return (
    <View style={ styles.tpl_32 } key={ index }>
      <OptNavigate data={ left_block['block_opt'] }>
        <Image uri={ left_block['block_value'] } style={ styles.tpl_32_left_image }/>
      </OptNavigate>
      { right_blocks.map((item, _index) => (
        <OptNavigate data={ item['block_opt'] } key={ _index }>
          <Image uri={ item['block_value'] } style={ styles.tpl_32_right_image }/>
        </OptNavigate>
      )) }
    </View>
  )
}

// 商品模块
export function tpl_37(index, blockList) {
  return (
    <View style={ styles.tpl_37 } key={ index }>
      { blockList.map((item, _index) => {
        const goods = item['block_value']
        if(goods === null || !item['block_value']){
          return
        }

        return (
          <OptNavigate
            data={ { opt_type: 'GOODS', opt_value: goods['goods_id'] } }
            style={ styles.tpl_37_view }
            key={ _index }
          >
            <Image uri={ goods['goods_image'] } style={ styles.tpl_37_goods_image }/>
            <Text
              numberOfLines={ 2 }
              style={ styles.tpl_37_goods_text }
            >{ goods['goods_name'] }</Text>
            <Price price={ goods['goods_price'] } style={ styles.tpl_37_goods_price }/>
          </OptNavigate>
        )
      }) }
    </View>
  )
}

// 文本模块
export function tpl_42(index, blockList) {
  const block = blockList[0]
  return (
    <View style={ styles.tpl_42 } key={ index }>
      <OptNavigate data={ block['block_opt'] }>
        <Text style={ styles.tpl_42_text }>{ block['block_value'] }</Text>
      </OptNavigate>
    </View>
  )
}

const styles = StyleSheet.create({
  // tpl_23
  tpl_23_image: {
    flex: 1,
    height: 130
  },
  // tpl_24
  tpl_24: {
    flexDirection: 'row'
  },
  tpl_24_left_image: {
    width: Screen.width / 2,
    height: 130
  },
  tpl_24_right: {
    width: Screen.width / 2,
    height: 130
  },
  
  tpl_24_right_image: {
    width: Screen.width / 2,
    height: 130 / 2
  },
  // tpl_25
  tpl_25: {
    flexDirection: 'row'
  },
  tpl_25_left: {
    width: Screen.width / 2,
    height: 130
  },
  tpl_25_left_image: {
    width: Screen.width / 2,
    height: 130 / 2
  },
  tpl_25_right_image: {
    width: Screen.width / 2,
    height: 130
  },
  // tpl_26
  tpl_26: {
    flexDirection: 'row'
  },
  tpl_26_image: {
    width: Screen.width / 3,
    height: 130
  },
  // tpl_27
  tpl_27: {
    flexDirection: 'row'
  },
  tpl_27_image: {
    width: Screen.width / 5,
    height: Screen.width / 5
  },
  tpl_28_image: {
    width: Screen.width,
    height: Screen.height / 4
  },
  // tpl_29
  tpl_29: {
    flexDirection: 'row'
  },
  tpl_29_image: {
    width: Screen.width / 4,
    height: 105
  },
  // tpl_30
  tpl_30_image: {
    width: Screen.width,
    height: 40
  },
  // tpl_31
  tpl_31: {
    flexDirection: 'row'
  },
  tpl_31_image: {
    width: Screen.width / 4,
    height: Screen.width / 4
  },
  // 左一右二竖排模板
  tpl_32: {
    flexDirection: 'row'
  },
  tpl_32_left_image: {
    width: Screen.width / 2,
    height: 130
  },
  tpl_32_right_image: {
    width: Screen.width / 4,
    height: 130
  },
  // tpl_37
  tpl_37: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    paddingHorizontal: 5,
    marginTop: 5
  },
  tpl_37_view: {
    width: (Screen.width - 20) / 2,
    height: (Screen.width - 20) / 2 + 60
  },
  tpl_37_goods_image: {
    width: (Screen.width - 20) / 2,
    height: (Screen.width - 20) / 2
  },
  tpl_37_goods_text: {
    color: colors.text,
    fontSize: 13,
    marginTop: 5,
    marginBottom: 5
  },
  tpl_37_goods_price: {
    color: colors.main
  },
  // tpl_42
  tpl_42: {
    height: 30,
    paddingHorizontal: 10,
    justifyContent: 'center'
  },
  tpl_42_text: {
    color: colors.text,
    fontSize: 14
  }
})
