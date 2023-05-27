import React, { Component } from 'react'
import {
  FlatList,
  Image,
  StyleSheet,
  ScrollView,
  TouchableOpacity,
  View
} from 'react-native'
import { navigate } from '../../navigator/NavigationService'
import { colors } from '../../../config'
import { Foundation, Screen } from '../../utils'
import { F16Text, F14Text } from '../../widgets/Text'
import { Loading, TextItem } from '../../widgets'
import * as API_AfterSale from '../../apis/after-sale'

export default class AfterSaleDetailScene extends Component {
    static navigationOptions = {
        title: '售后详情'
    }

    constructor(props, context) {
        super(props, context)
        const { params } = this.props.navigation.state
        this.sn = params.data.sn
        this.state = {
            refund: {},
            refund_goods: {},
            loading: false
        }
    }

    componentDidMount() {
        this._getAfterSaleData()
    }

    _getAfterSaleData = async () => {
        await this.setState({ loading: true })
        try{
          const afterSaleData = await API_AfterSale.getAfterSaleDetail(this.sn)
          this.setState({ loading: false, refund: afterSaleData.refund, refund_goods: afterSaleData.refund_goods })
        }catch(error){
          this.setState({ loading: false })
          navigate('MyAfterSale')
        }
        
    }

    _getItemLayou = (data, index) => ({ length: (90 + Screen.onePixel), offset: (90 + Screen.onePixel) * index, index })

    _renderItem =  ({ item }) => (<GoodsItem
        data={ item }
        nav={ this.nav }/>
    )

    render() {
        const { refund, refund_goods, loading } = this.state
        return (
          <View style={ styles.container }>
            <View style={ styles.body }>
              <ScrollView>
                <View style={ styles.refund }>
                    <TextItem title="订单编号" content={ refund['order_sn'] }/>
                    <TextItem title="售后单号" content={ refund['sn']  }/>
                    <ItemLine/>
                    <TextItem title="申请时间" content={ Foundation.unixToDate(refund['create_time']) }/>
                    <TextItem title="售后状态" content={ refund['refund_status_text'] }/>
                    <ItemLine/>
                    <TextItem title="申请原因" content={ refund['refund_reason'] || '无' }/>
                    <TextItem title="详细描述" content={ refund['customer_remark'] || '无' }/>
                    <ItemLine/>
                    <TextItem title="商家回复" content={ refund['seller_remark'] || '无' }/>
                    <TextItem title="退款方式" content={ refund.refund_way === 'ORIGINAL' ? '原路退回' : refund['account_type_text'] || '无' }/>
                    <ItemLine/>
                    { refund.refund_way === 'ORIGINAL' ? <TextItem title="退款账号" content='原路退回' /> : refund.account_type === 'BANKTRANSFER' && <TextItem title="退款账号" content={ refund['bank_account_number'] }/> 
                      || <TextItem title="退款账号" content={ refund['return_account'] }/> }
                    <TextItem title="退款金额" content={ refund['refund_price'] }/>
                </View>
                <View style={ styles.refund_goods }>
                  <FlatList
                    data={ refund_goods }
                    renderItem={ this._renderItem }
                    keyExtractor={ (_, index) => String(index) }
                    getItemLayout={ this._getItemLayout }
                  />
                </View>
              </ScrollView>
            </View>
            <Loading show={ loading }/>
          </View>
        )
    }

}

const GoodsItem = ({ data }) => {

  return (
    <TouchableOpacity
      style={ styles.goods_item }
      activeOpacity={ 1 }
      onPress={ () => navigate('Goods', { id: data.goods_id }) }
    >
      <Image style={ styles.goods_item_image } source={ { uri: data['goods_image'] } }/>
      <View style={ styles.goods_item_info }>
        <F16Text numberOfLines={ 1 }>{ data['goods_name'] }</F16Text>
        <F14Text numberOfLines={ 2 }>
          <F14Text style={ styles.goods_item_num }>数量：{ data['return_num'] }</F14Text>
        </F14Text>
        <View style={ { flexDirection: 'row', alignItems: 'center' } }>
          <F16Text>￥{ Foundation.formatPrice(data['price']) }</F16Text>
        </View>
      </View>
    </TouchableOpacity>
  )
}

const ItemLine = () => (<View style={ styles.item_line }/>)

const styles = StyleSheet.create({
    container: {
      flex: 1
    },
    body: {
      flex: 1
    },
    refund: {
      marginTop: 10,
      backgroundColor: '#FFFFFF',
      paddingVertical: 5
    },
    refund_goods: {
      maxHeight: 250,
      marginTop: 10,
      backgroundColor: '#FFFFFF',
      padding: 10
    },
    goods_item: {
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'center',
      paddingHorizontal: 10,
      paddingVertical: 10,
      width: Screen.width,
      height: 90,
      backgroundColor: '#FFFFFF'
    },
    goods_item_image: {
      width: 70,
      height: 70,
      borderColor: colors.cell_line_backgroud,
      borderWidth: Screen.onePixel
    },
    goods_item_info: {
      justifyContent: 'space-between',
      width: Screen.width - 20 - 70 - 10,
      height: 70
    },
    goods_item_num: {
      marginTop: 5,
      color: '#a5a5a5'
    },
    item_line: {
      width: Screen.width,
      height: Screen.onePixel,
      backgroundColor: colors.gray_background
    },
  })
  