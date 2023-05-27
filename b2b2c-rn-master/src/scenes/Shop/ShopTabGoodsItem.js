/**
 * Created by Andste on 2019-01-15.
 */
import React, { PureComponent } from 'react'
import {
  View,
  Image,
  Text,
  TouchableOpacity,
  StyleSheet
} from 'react-native'
import { withNavigation } from 'react-navigation'
import { colors } from '../../../config'
import { Screen } from '../../utils'
import { Price } from '../../widgets'

class ShopTabGoodsItem extends PureComponent {
  _toGoods = () => {
    const { data, navigation } = this.props
    navigation.push('Goods', { id: data['goods_id'] })
  }
  
  render() {
    const { data } = this.props
    return (
      <TouchableOpacity
        style={ styles.container }
        onPress={ this._toGoods }
      >
        <Image style={ styles.goods_img } source={ { uri: data['thumbnail'] } }/>
        <View style={ styles.goods_detail }>
          <Text
            suppressHighlighting={ false }
            allowFontScaling={ false }
            numberOfLines={ 2 }
            style={ styles.goods_title }
          >{ data['goods_name'] }</Text>
          <Price advanced={ true } price={ data['price'] }/>
        </View>
      </TouchableOpacity>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    width: Screen.width,
    height: 120,
    backgroundColor: '#FFF'
  },
  goods_img: {
    width: 115,
    height: 115
  },
  goods_detail: {
    flexDirection: 'column',
    justifyContent: 'space-around',
    width: Screen.width - 155 - 15,
    height: 120,
    marginLeft: 15,
    paddingTop: 10,
    paddingBottom: 10
  },
  goods_title: {
    width: Screen.width - 115 - 15 - 10,
    height: 50,
    fontSize: 18,
    color: colors.text
  }
})

export default withNavigation(ShopTabGoodsItem)
