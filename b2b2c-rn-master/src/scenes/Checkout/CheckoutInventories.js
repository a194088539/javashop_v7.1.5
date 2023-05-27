/**
 * Created by Andste on 2019-01-18.
 * 商品清单
 */
import React, { PureComponent } from 'react'
import {
  View,
  Image,
  Text,
  TouchableOpacity,
  StyleSheet
} from 'react-native'
import Icon from "react-native-vector-icons/Ionicons"
import Dialog, {
  DialogTitle,
  DialogContent,
  ScaleAnimation,
} from 'react-native-popup-dialog'
import { colors } from '../../../config'
import { Screen } from '../../utils'
import { Price, SkuSpec } from '../../widgets'

export default class CheckoutInventories extends PureComponent {

  constructor(props) {
    super(props)
    this.state = {
      showActDialog: ''
    }
  }

  render() {
    const { data } = this.props
    return (
      <View style={ styles.container }>
        { data.map(item => (
          <View key={ item['seller_id'] }>
            <ShopItem data={ item }/>
            { (item.gift_list && item.gift_coupon_list && !!(item.gift_list.length || item.gift_list.length)) && 
              <View style={styles.act_cell_view}>
                <TouchableOpacity style={styles.act_cell} onPress={ () => { this.setState({showActDialog: item['seller_id']}) } }>
                  <Text>赠品</Text>
                  <View style={ styles.act_text_view }>
                    { item.gift_coupon_list.map((coupon, couponIndex) => ( <Text key={couponIndex}> { coupon.amount }元的优惠券 </Text> )) }
                    { item.gift_list.map((gift, giftIndex) => ( <Text key={giftIndex}> 价值{ gift.gift_price }元的{ gift.gift_name }</Text> )) }
                  </View>
                  <Icon style={ styles.act_icon } name="ios-arrow-forward" color="#A8A9AB" size={20}/>
                </TouchableOpacity> 
              </View>
            }
            <Dialog
                onDismiss={() => {
                  this.setState({showActDialog: ''})
                }}
                width={0.9}
                visible={this.state.showActDialog === item['seller_id'] }
                rounded
                actionsBordered
                dialogTitle={
                    <DialogTitle
                        title="赠品详情"
                        style={{
                            backgroundColor: '#F7F7F8',
                        }}
                        hasTitleBar={false}
                        align="center"
                    />
                }
                onHardwareBackPress={() => {
                  this.setState({ showActDialog: '' });
                  return true;
                }}
                onTouchOutside={() => {
                  this.setState({ showActDialog: '' });
                }}
                dialogAnimation={new ScaleAnimation()}
            >
              <DialogContent style={{backgroundColor: '#F7F7F8',}}>
                { item.gift_coupon_list.map((coupon, couponIndex) => (
                  <View key={couponIndex} style={styles.popup_act_item}>
                    <Image
                      style={ styles.popup_act_img }
                      source={ require('../../images/icon-color-coupon.png') }
                    />
                    <View style={ styles.popup_act_name }><Text> { coupon.amount }元的优惠券 </Text></View>
                    
                  </View>
                )) }
                { item.gift_list.map((gift, giftIndex) => ( 
                  <View key={giftIndex} style={styles.popup_act_item}>
                    <Image
                      style={ styles.popup_act_img }
                      source={ { uri: gift.gift_img } }
                    />
                    <View style={ styles.popup_act_name }><Text> 价值{ gift.gift_price }元的{ gift.gift_name }</Text></View>
                  </View>
                )) }
              </DialogContent>
            </Dialog>
          </View>
        )) }
      </View>
    )
  }
}



/**
 * 店铺项
 * @param data
 * @returns {*}
 * @constructor
 */
const ShopItem = ({ data }) => {
  return (
    <View style={ styles.shop_item }>
      <View style={ [styles.shop_header, styles.bottom_line] }>
        <Image
          style={ styles.shop_header_icon }
          source={ require('../../images/icon-shop.png') }
        />
        <Text style={ styles.shop_header_name }>
          { data['seller_name'] }
        </Text>
      </View>
      <View style={ [styles.shop_ship, styles.bottom_line] }>
        <Text>运费合计：</Text><Price price={ data['price']['freight_price'] }/>
        <Text> 总重量：</Text><Text style={ styles.shop_weight }>{ data['weight'] }kg</Text>
      </View>
      { data['promotion_notice'] ? (
        <View style={ [styles.shop_ship, styles.bottom_line] }>
          <Text style={ styles.shop_weight }>{ data['promotion_notice'] }</Text>
        </View>
      ) : undefined }
      <View>
        { data['sku_list'].map(item => (
          <SkuItem data={ item } key={ item['sku_id'] }/>
        )) }
      </View>
    </View>
  )
}

/**
 * 商品项
 * @param data
 * @returns {*}
 * @constructor
 */
const SkuItem = ({ data }) => {
  return (
    <View style={ [styles.sku_item, styles.bottom_line] }>
      <View style={ styles.sku_img_view }>
        <Image style={ styles.sku_img } source={ { uri: data['goods_image'] } }/>
        { !data['is_ship'] ? (
          <View style={ styles.sku_no_ship }>
            <Text style={ styles.sku_no_ship_text }>该地区无货</Text>
          </View>
        ) : undefined }
      </View>
      <View style={ styles.sku_info }>
        <Text
          style={ styles.sku_name }
          numberOfLines={ 2 }
        >{ data['name'] }</Text>
        { data['promotion_tags'].length ? (
          <View style={ styles.sku_pro_tag }>
            { data['promotion_tags'].map((tag, index) => (
              <View style={ styles.sku_pro_tag_view } key={ index }>
                <Text style={ styles.sku_pro_tag_text }>{ tag }</Text>
              </View>
            )) }
          </View>
        ) : undefined }
        <SkuSpec data={ data }/>
        <View style={ styles.sku_price }>
          <View style={ styles.sku_price_view }>
            <Price advanced price={ data['purchase_price'] }/>
            <Text style={ styles.sku_weight }>{ data['goods_weight'] }kg</Text>
          </View>
          <Text style={ styles.sku_num }>x{ data['num'] }</Text>
        </View>
      </View>
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    marginTop: 10
  },
  bottom_line: {
    borderBottomWidth: Screen.onePixel,
    borderBottomColor: colors.cell_line_backgroud
  },
  act_cell_view: {
    width: Screen.width ,
    backgroundColor: '#FFF'
  },
  act_cell:{
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    width: Screen.width,
    paddingHorizontal: 10,
  },
  act_icon: {
    alignItems: 'flex-end',
  },
  popup_act_item:{
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 10
  },
  popup_act_img: {
    width: 60,
    height: 60
  },
  act_text_view:{
    width: Screen.width - 120,
  },
  popup_act_name: {
    width: Screen.width - 120,
    marginLeft: 10
  },
  shop_item: {
    paddingHorizontal: 10,
    marginTop: 10,
    backgroundColor: '#FFF'
  },
  shop_header: {
    flexDirection: 'row',
    alignItems: 'center',
    width: Screen.width - 20,
    height: 35
  },
  shop_header_icon: {
    width: 25,
    height: 25
  },
  shop_header_name: {
    marginLeft: 10,
    fontSize: 18
  },
  shop_ship: {
    flexDirection: 'row',
    alignItems: 'center',
    height: 30
  },
  shop_weight: {
    color: colors.main
  },
  sku_item: {
    flexDirection: 'row',
    paddingVertical: 10
  },
  sku_img_view: {
    width: 75,
    height: 75
  },
  sku_img: {
    width: 75,
    height: 75
  },
  sku_no_ship: {
    position: 'absolute',
    justifyContent: 'center',
    alignItems: 'center',
    width: 75,
    height: 75,
    backgroundColor: 'rgba(0,0,0,.7)'
  },
  sku_no_ship_text: {
    fontSize: 14,
    fontWeight: '600',
    color: colors.main
  },
  sku_info: {
    width: Screen.width - 20 - 75 - 10,
    marginLeft: 10
  },
  sku_name: {
    fontSize: 18,
    color: colors.text
  },
  sku_pro_tag: {
    flexDirection: 'row'
  },
  sku_pro_tag_view: {
    borderWidth: Screen.onePixel,
    borderColor: colors.main,
    borderRadius: 1,
    marginRight: 3
  },
  sku_pro_tag_text: {
    color: colors.main,
    fontSize: 12,
    padding: 1
  },
  sku_price_view: {
    flexDirection: 'row',
    alignItems: 'center'
  },
  sku_price: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center'
  },
  sku_num: {
    marginLeft: 10,
    color: '#a6a6a6'
  },
  sku_weight: {
    color: '#a6a6a6',
    marginLeft: 10
  }
})
