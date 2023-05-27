/**
 * Created by Andste on 2018/11/21.
 */
import React, { PureComponent } from 'react'
import {
  View,
  DeviceEventEmitter,
  StyleSheet
} from 'react-native'
import { connect } from 'react-redux'
import { messageActions } from '../../redux/actions'
import { colors } from '../../../config'
import { TextCell, Price } from '../../widgets'
import * as API_Goods from '../../apis/goods'
import * as API_Promotions from '../../apis/promotions'
import * as API_Shop from '../../apis/shop'
import ItemCell from './GoodsItemCell'
import GoodsProBar from './GoodsProBar'
import GoodsCoupons from './GoodsCoupons'
import GoodsPromotions from './GoodsPromotions'
import MainShop from './GoodsMainShop'
import GoodsSkus from './GoodsSkus'
import GoodsShip from './GoodsShip'

class GoodsMainPage extends PureComponent {
  constructor(props) {
    super(props)
    this.skuMap = new Map()
    this.state = {
      // skus
      skus: [],
      // 规格列表
      specList: [],
      // 优惠券列表
      coupons: [],
      // 促销列表
      promotions: [],
      // 积分商品、团购活动、限时抢购【其一】
      proBars: '',
      // 店铺信息
      shop: '',
      // 已选中的规格值ID
      selectedSpecIds: [],
      // 已选中的规格值
      selectedSpecVals: [],
      // 已选规格
      selectedSku: '',
      // 购买数量
      buyNum: '1',
      // 显示信息
      showInfo: {}
    }
  }
  
  async componentDidMount() {
    await this._getOtherData()
  }
  
  /**
   * 获取其它数据
   * sku数据
   * 优惠券数据
   * 促销信息
   * 店铺数据
   * @returns {Promise<void>}
   * @private
   */
  _getOtherData = async () => {
    const { goods_id, seller_id } = this.props.goods
    const req_arrays = [
      API_Goods.getGoodsSkus(goods_id),
      API_Promotions.getShopCoupons(seller_id),
      API_Promotions.getGoodsPromotions(goods_id),
      API_Shop.getShopBaseInfo(seller_id)
    ]
    const values = await Promise.all(req_arrays)
    // ⬇筛选出促销活动
    const promotions = values[2].map(item => {
      if (item['exchange'] || item['groupbuy_goods_vo'] || item['seckill_goods_vo']) {
        return null
      }
      return item
    }).filter(item => !!item)
    // ⬇筛选出【积分商品、团购活动、限时抢购】
    const proBars = values[2].map(item => {
      if (item['exchange'] || item['groupbuy_goods_vo'] || item['seckill_goods_vo']) {
        return item
      }
      return null
    }).filter(item => !!item)
    await this._makeSkus(values[0], proBars)
    this.setState({
      coupons: values[1],
      promotions,
      proBars,
      shop: values[3]
    })
  }
  
  /**
   * 处理skus
   * @param skus
   * @param pors 积分、团购、限时抢购
   * @returns {Promise<void>}
   * @private
   */
  _makeSkus = async (skus, pors) => {
    const specList = []
    skus.forEach(sku => {
      const { spec_list } = sku
      if (!spec_list) {
        this.skuMap.set('no_spec', sku)
        return
      }
      const spec_value_ids = []
      spec_list.forEach(spec => {
        const _specIndex = specList.findIndex(_spec => _spec['spec_id'] === spec.spec_id)
        const _spec = {
          spec_id: spec.spec_id,
          spec_name: spec.spec_name,
          spec_type: spec.spec_type
        }
        const _value = {
          spec_value: spec.spec_value,
          spec_value_id: spec.spec_value_id,
          spec_value_img: {
            original: spec['spec_image'],
            thumbnail: spec['thumbnail']
          }
        }
        spec_value_ids.push(spec.spec_value_id)
        if (_specIndex === -1) {
          specList.push({ ..._spec, valueList: [{ ..._value }] })
        } else if (specList[_specIndex]['valueList'].findIndex(_value => _value['spec_value_id'] === spec['spec_value_id']) === -1) {
          specList[_specIndex]['valueList'].push({ ..._value })
        }
      })
      this.skuMap.set(spec_value_ids.join('-'), sku)
    })
    await this.setState({ skus, specList })
    // 如果没有规格，把商品第一个sku给已选择sku
    const { showInfo } = this.state
    let sku
    if (!specList.length) {
      sku = this.skuMap.get('no_spec')
      await this.setState({ selectedSku: sku })
      const { goods } = this.props
      DeviceEventEmitter.emit(`SelectedSkuChange-${goods['goods_id']}`, sku)
    } else {
      sku = this.props.goods
    }
    const _showInfo = {
      ...showInfo,
      image: sku['thumbnail'],
      price: sku['price'],
      sn: sku['sn'],
      specVals: '默认 '
    }
    // 如果有积分、团购、限时抢购
    if (pors.length) {
      let proPrice
      const exchange = pors.filter(item => item['exchange'])[0]
      const groupbuy = pors.filter(item => item['groupbuy_goods_vo'])[0]
      const seckill = pors.filter(item => item['seckill_goods_vo'])[0]
      if (exchange) {
        const ex = exchange['exchange']
        proPrice = ex['exchange_money']
        _showInfo['point'] = ex['exchange_point']
      } else if (groupbuy) {
        proPrice = groupbuy['groupbuy_goods_vo']['price']
      } else if (seckill) {
        proPrice = seckill['seckill_goods_vo']['seckill_price']
      }
      _showInfo['proPrce'] = proPrice
    }
    await this.setState({ showInfo: _showInfo })
  }
  
  /**
   * 选择规格
   * @param spec
   * @param specIndex
   * @param specVal
   * @returns {Promise<void>}
   * @private
   */
  _onSelectSpec = async (spec, specIndex, specVal) => {
    // 如果当前规格值已选中，则不做任何操作
    if (specVal.selected) return
    const { showInfo } = this.state
    // ⬇设置选中效果
    const specList = JSON.parse(JSON.stringify(this.state.specList))
    let { valueList } = specList[specIndex]
    valueList = valueList.map(item => {
      item.selected = item['spec_value_id'] === specVal['spec_value_id']
      return item
    })
    specList[specIndex]['valueList'] = valueList
    // ⬇选出选中的规格
    let { selectedSpecIds, selectedSpecVals } = this.state
    selectedSpecIds[specIndex] = specVal['spec_value_id']
    selectedSpecVals[specIndex] = specVal['spec_value']
    // ⬇选出符合规格的sku
    const selectedSku = this._getSelectSku(selectedSpecIds)
    await this.setState({
      specList,
      selectedSpecIds,
      selectedSpecVals
    })
    let _showInfo = { ...showInfo }
    // ⬇如果规格是图片规格
    if (spec.spec_type === 1) {
      _showInfo['image'] = specVal['spec_value_img']['thumbnail']
    }
    if (selectedSku) {
      // ⬇选择sku
      await this.setState({ selectedSku })
      const { goods } = this.props
      DeviceEventEmitter.emit(`SelectedSkuChange-${goods['goods_id']}`, selectedSku)
      _showInfo['price'] = selectedSku['price']
      _showInfo['sn'] = selectedSku['sn']
      _showInfo['specVals'] = selectedSpecVals.join('-')
    }
    // ⬇展示信息
    this.setState({ showInfo: _showInfo })
  }
  
  /**
   * 筛选出符合规格的sku
   * @param specIds
   * @returns {Promise<*>}
   * @private
   */
  _getSelectSku = specIds => {
    let sku
    if (specIds.length) {
      sku = this.skuMap.get(specIds.join('-'))
    } else {
      sku = this.skuMap.get('no_spec')
    }
    return sku
  }
  
  /**
   * 更新购买数量
   * @param symbol
   * @returns {Promise<void>}
   * @private
   */
  _updateBuyNum = async (symbol) => {
    const { buyNum } = this.state
    let num = Number(buyNum)
    if (symbol === '-' && buyNum < 2) {
      return
    }
    if (symbol === '-') {
      num -= 1
    } else {
      num += 1
    }
    await this.setState({ buyNum: String(num) })
    const { goods } = this.props
    DeviceEventEmitter.emit(`BuyNumUpdate-${goods['goods_id']}`, String(num))
  }
  
  /**
   * 输入购买数量
   * @param event
   * @returns {Promise<void>}
   * @private
   */
  _editingBuyNum = async (event) => {
    event.persist()
    let num = Number(event.nativeEvent.text)
    if (num < 1 || num > 999999) return
    const { dispatch } = this.props
    if (isNaN(num)) {
      dispatch(messageActions.error('您的输入有误，请输入正整数！'))
      return
    }
    await this.setState({ buyNum: String(num) })
    const { goods } = this.props
    DeviceEventEmitter.emit(`BuyNumUpdate-${goods['goods_id']}`, String(num))
  }
  
  render() {
    const { goods } = this.props
    let { skus, specList, coupons, promotions, proBars, shop, selectedSku, buyNum, showInfo } = this.state
    return (
      <View style={ styles.container }>
        { proBars.length ? <GoodsProBar data={ proBars }/> : undefined }
        <TextCell text={ goods['goods_name'] }/>
        { !proBars.length ? (
          <ItemCell
            content={ <Price
              price={ showInfo['price'] }
              advanced
              scale={ 1.75 }
              style={ styles.price }/>
            }/>
        ) : undefined }
        { coupons.length ? <GoodsCoupons data={ coupons }/> : undefined }
        { promotions.length ? <GoodsPromotions data={ promotions }/> : undefined }
        { skus.length ? (
          <GoodsSkus
            goodsId={ goods['goods_id'] }
            specList={ specList }
            buyNum={ buyNum }
            selectedSku={ selectedSku }
            showInfo={ showInfo }
            onSelectSpec={ this._onSelectSpec }
            onQuantityPress={ this._updateBuyNum }
            onQuantityEditing={ this._editingBuyNum }
          />
        ) : undefined }
        <GoodsShip goodsId={ goods['goods_id'] }/>
        { shop ? <MainShop data={ shop }/> : undefined }
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1
  },
  price: {
    fontSize: 20,
    color: colors.main,
    fontWeight: '600'
  },
  margin_vert: {
    marginTop: 10
  }
})

export default connect()(GoodsMainPage)
