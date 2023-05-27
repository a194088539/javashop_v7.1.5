/**
 * Created by Andste on 2018/11/21.
 */
import React, { PureComponent } from 'react'
import {
  View,
  Text,
  StyleSheet
} from 'react-native'
import { TextLabel } from '../../widgets'
import { colors } from '../../../config'

export default class GoodsUnablePage extends PureComponent {
  
  constructor(props) {
    super(props)
    this.state = {
    }
  }
  
  render() {
    const { goHome, goBack } = this.props
    return (
      <View style={styles.goods_unable_view}>
        <Text style={ styles.goods_unable_text }>该商品已下架</Text>
        <View style={ styles.goods_btns }>
          <TextLabel style={ styles.btnsItem } text="返回首页" onPress={ goHome } />
          <TextLabel style={ styles.btnsItem } text="返回上一步" onPress={ goBack } />
        </View>
        
      </View>
    )
  }
}

const styles = StyleSheet.create({
    goods_unable_view: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center'
    },
    goods_unable_text: {
        textAlign: 'center',
        fontSize: 20,
        color: colors.main,
        marginBottom: 10
    },
    goods_btns: {
        flexDirection: 'row',
    },
    btnsItem: {
        backgroundColor: '#fff',
        color: '#333',
        fontSize: 12
    },
})
