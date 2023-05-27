/**
 * Created by Andste on 2019-01-15.
 */
import React, { Component } from 'react'
import {
  View,
  StatusBar,
  ScrollView,
  StyleSheet
} from 'react-native'
import { colors } from '../../../config'
import { Cell, CellGroup } from '../../widgets'
import * as API_Shop from '../../apis/shop'

export default class ShopClassify extends Component {
  static navigationOptions = {
    title: '店铺分组'
  }
  
  constructor(props) {
    super(props)
    this.shop_id = props.navigation.state.params.shop_id
    this.state = {
      categorys: ''
    }
  }
  
  async componentDidMount() {
    const categorys = await API_Shop.getShopCategorys(this.shop_id)
    this.setState({ categorys })
  }
  
  _toShopClass = (cat_id) => {
    const { shop_id } = this
    this.props.navigation.navigate('ShopGoods', {
      shop_id,
      cat_id: typeof cat_id === 'number' ? cat_id : 0
    })
  }
  
  render() {
    const { categorys } = this.state
    return (
      <View style={ styles.container }>
        <StatusBar barStyle="dark-content"/>
        <ScrollView>
          <CellGroup marginBottom>
            <Cell title="全部分类" onPress={ this._toShopClass }/>
          </CellGroup>
          { categorys ? (
            categorys.map(item => (
              <Cell
                line="bottom"
                key={ item['shop_cat_id'] }
                title={ item['shop_cat_name'] }
                onPress={ () => this._toShopClass(item['shop_cat_id']) }
              />
            ))
          ) : undefined }
        </ScrollView>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: colors.gray_background
  }
})
