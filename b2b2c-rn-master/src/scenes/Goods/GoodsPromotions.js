/**
 * Created by Andste on 2019-01-02.
 */
import React, { PureComponent } from 'react'
import {
  View,
  Image,
  Text,
  ScrollView,
  StyleSheet
} from 'react-native'
import { connect } from 'react-redux'
import { colors } from '../../../config'
import { Screen } from '../../utils'
import { Modal } from '../../components'
import ItemCell from './GoodsItemCell'

class GoodsPromotions extends PureComponent {
  constructor(props) {
    super(props)
    this.state = {
      showModal: false
    }
  }
  
  _showModal = () => {
    this.setState({ showModal: true })
  }
  
  _onModalClosed = () => {
    this.setState({ showModal: false })
  }
  
  render() {
    const { data } = this.props
    const { showModal } = this.state
    const pros = filterPros(data)
    return (
      <ItemCell
        label="促销"
        icon
        style={ { marginTop: 10 } }
        onPress={ this._showModal }
        content={ <View>
          <View style={ styles.promotion_cell }>
            { pros.slice(0, 4).map(item => (
              <Text style={ styles.promotion_tag } key={ item.title }>
                { item.title }
              </Text>
            )) }
          </View>
          <Modal
            title="促销信息"
            style={ styles.modal_style }
            isOpen={ showModal }
            onClosed={ this._onModalClosed }
          >
            <ScrollView
              style={ styles.promotion_scroll }
              showsHorizontalScrollIndicator={ false }
              showsVerticalScrollIndicator={ false }
            >
              { pros.map(item => (
                <View style={ styles.pros_item } key={ item.title }>
                  <Text style={ styles.promotion_tag } key={ item.title }>
                    { item.title }
                  </Text>
                  { item.value }
                </View>
              )) }
            </ScrollView>
          </Modal>
        </View> }
      />
    )
  }
}

/**
 * 筛选出促销活动
 * @param promotions
 * @returns {Array}
 */
const filterPros = (promotions) => {
  const pros = []
  promotions.forEach(pro => {
    // 满减
    if (pro['full_discount_vo']) {
      const full = pro['full_discount_vo']
      // 满减
      full['is_full_minus'] && pros.push({
        title: '满减',
        value: <Text>
          满<Text style={ styles.price }>{ full['full_money'] }</Text>元，
          立减现金<Text style={ styles.price }>{ full['minus_value'] }元</Text>
        </Text>
      })
      // 打折
      full['is_discount'] && pros.push({
        title: '打折',
        value: <Text>
          满<Text style={ styles.price }>{ full['full_money'] }</Text>元，
          立享<Text style={ styles.price }>{ full['discount_value'] }折</Text>优惠
        </Text>
      })
      // 赠送礼品
      full['is_send_gift'] && pros.push({
        title: '赠礼',
        value: 
        <View style={ { flexDirection: 'row', alignItems: 'center', width: Screen.width - 120} }>
          <Text>
            满<Text style={ styles.price }>{ full['full_money'] }</Text>元，
            赠送价值<Text style={ styles.price }>{ full['full_discount_gift_do']['gift_price'] }元</Text>的
          </Text>
          <Image style={ styles.gift_img } source={ { uri: full['full_discount_gift_do']['gift_img'] } }/>
        </View>
      })
      // 免邮
      full['is_free_ship'] && pros.push({
        title: '免邮',
        value: <Text>
          满<Text style={ styles.price }>{ full['full_money'] }</Text>元，
          立享<Text style={ styles.price }>免邮</Text>优惠
        </Text>
      })
      // 赠送优惠券
      full['is_send_bonus'] && pros.push({
        title: '赠券',
        value: <Text>
          满<Text style={ styles.price }>{ full['full_money'] }</Text>元，
          赠送<Text style={ styles.price }>{ full['coupon_do']['coupon_price'] }元</Text>优惠券
        </Text>
      })
      // 赠送积分
      full['is_send_point'] && pros.push({
        title: '积分',
        value: <Text>
          满<Text style={ styles.price }>{ full['full_money'] }</Text>元，
          赠送<Text style={ styles.price }>{ full['point_value'] }点</Text>积分
        </Text>
      })
    }
    // 单品立减
    if (pro['minus_vo']) {
      pros.push({
        title: '单品立减',
        value: <Text>
          单件立减现金
          <Text style={ styles.price }>
            { pro['minus_vo']['single_reduction_value'] }元
          </Text>
        </Text>
      })
    }
    // 第二件半价
    if (pro['half_price_vo']) {
      pros.push({
        title: '第二件半价',
        value: <Text>第二件半价优惠</Text>
      })
    }
  })
  return pros
}

const styles = StyleSheet.create({
  promotion_cell: {
    flexDirection: 'row'
  },
  promotion_tag: {
    borderWidth: 1,
    borderColor: colors.main,
    borderRadius: 1,
    color: colors.main,
    marginRight: 10,
    paddingHorizontal: 2,
    paddingVertical: 2
  },
  modal_style: {
    height: Screen.height - 200,
    backgroundColor: colors.gray_background
  },
  margin_vert: {},
  promotion_scroll: {
    paddingHorizontal: 10
  },
  price: {
    color: colors.main
  },
  gift_img: {
    width: 50,
    height: 50
  },
  pros_item: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 10,
    paddingVertical: 5,
    borderBottomWidth: Screen.onePixel,
    borderBottomColor: colors.cell_line_backgroud
  }
})

export default connect(state => ({ user: state.user.user }))(GoodsPromotions)
