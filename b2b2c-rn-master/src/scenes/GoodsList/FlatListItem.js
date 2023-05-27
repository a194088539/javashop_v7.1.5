/**
 * Created by Andste on 2018/10/20.
 */

import React from 'react'
import {
  Image,
  StyleSheet,
  TouchableOpacity,
  Text,
  View
} from 'react-native'
import { navigate } from '../../navigator/NavigationService'
import { Screen } from '../../utils'
import { colors } from '../../../config'
import Price from '../../widgets/Price'

const WIDTH = Screen.width

const FlatListItem = ({ data: item, view_type = 'single', ...props }) => {
  let single = view_type === 'single'
  return (
    <TouchableOpacity
      style={ styles['container_' + view_type] }
      onPress={ () => {navigate('Goods', { id: item.goods_id })} }
      { ...props }
    >
      <Image style={ styles['item_image_' + view_type] } source={ { uri: item.thumbnail } }/>
      <View style={ styles['item_detail_' + view_type] }>
        <Text
          style={ styles['item_title_' + view_type] }
          suppressHighlighting={ false }
          allowFontScaling={ false }
          numberOfLines={ single ? 2 : 1 }
        >{ item.name }</Text>
        <View style={ styles['item_info_' + view_type] }>
          <View style={ styles['item_price_' + view_type] }>
            <Price price={ item.price }/>
          </View>
          <View style={ styles['item_comment_' + view_type] }>
            <Text
              style={ styles['item_comment_text_' + view_type] }
              allowFontScaling={ false }
            >{ item.comment_num }条评论</Text>
            <Text
              style={ styles['item_comment_text_' + view_type] }
              allowFontScaling={ false }
            >{ item.grade }%好评</Text>
            { single ? (
              <Text
                style={ styles['item_comment_text_' + view_type] }
                allowFontScaling={ false }
              >{ item.buy_count }人已购买</Text>
            ) : undefined }
          </View>
        </View>
      </View>
    </TouchableOpacity>
  )
}

let detail_width = WIDTH - 115 - 15 - 10
let double_width = (WIDTH - 5) / 2
const styles = StyleSheet.create({
  container_single: {
    flexDirection: 'row',
    justifyContent: 'flex-start',
    alignItems: 'center',
    width: WIDTH,
    height: 120,
    backgroundColor: '#FFFFFF'
  },
  container_double: {
    flexDirection: 'column',
    justifyContent: 'space-around',
    alignItems: 'center',
    width: double_width,
    backgroundColor: '#FFFFFF'
  },
  item_image_single: {
    width: 115,
    height: 115
  },
  item_image_double: {
    width: double_width,
    height: (WIDTH - 5) / 2
  },
  item_detail_single: {
    flexDirection: 'column',
    justifyContent: 'space-around',
    width: Screen.width - 115 - 15,
    height: 120,
    marginLeft: 15,
    paddingTop: 10,
    paddingBottom: 10
  },
  item_detail_double: {
    flexDirection: 'column',
    justifyContent: 'space-around',
    width: double_width - 10,
    paddingHorizontal: 5,
    paddingVertical: 10
  },
  item_title_single: {
    width: detail_width,
    height: 50,
    fontSize: 18,
    color: colors.text
  },
  item_title_double: {
    width: double_width - 10,
    fontSize: 14,
    color: colors.text
  },
  item_info_single: {
    flexDirection: 'column',
    justifyContent: 'space-between',
    width: detail_width,
    height: 50
  },
  item_info_double: {
    flexDirection: 'column',
    justifyContent: 'space-between',
    width: double_width - 10
  },
  item_price: {
    width: detail_width,
    height: 30
  },
  item_comment_single: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    width: detail_width,
    height: 20
  },
  item_comment_double: {
    flexDirection: 'row',
    justifyContent: 'flex-start',
    width: double_width - 10
  },
  item_comment_text_single: {
    textAlign: 'center',
    alignSelf: 'center'
  },
  item_comment_text_double: {
    textAlign: 'center',
    alignSelf: 'center',
    paddingRight: 10
  }
})

export default FlatListItem
