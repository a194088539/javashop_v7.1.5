/**
 * Created by Andste on 2019-01-08.
 */
import React, { PureComponent } from 'react'
import {
  View,
  Text,
  StyleSheet,
  DeviceEventEmitter,
  NativeModules,
  NativeEventEmitter
} from 'react-native'
import { connect } from 'react-redux'
import { messageActions } from '../../redux/actions'
import * as API_Common from '../../apis/common'
import * as API_Goods from '../../apis/goods'
import { Loading } from '../../widgets'
import ItemCell from './GoodsItemCell'

class GoodsShip extends PureComponent {
  constructor(props) {
    super(props)
    this.state = {
      loading: false,
      region: '',
      region_str: '',
      inStore: true
    }
  }
  
  componentDidMount() {
    const addressSelector = NativeModules['AddressSelectorModule']
    this.addressSelector = addressSelector
    const eventManager = new NativeEventEmitter(addressSelector)
    this.subscription = eventManager.addListener(addressSelector['DataEvent'], async (id) => {
      const res = await API_Common.getRegionsById(id)
      addressSelector.setData(res.map(item => {
        item['id'] = item['id']
        item['parentId'] = item['parent_id']
        item['name'] = item['local_name']
        item['type'] = item['region_grade']
        return item
      }))
    })
  }
  
  componentWillUnmount() {
    this.subscription.remove()
  }
  
  _selectAddress = () => {
    const { dispatch } = this.props
    this.addressSelector.create(async (result1, result2, result3, result4) => {
      if (!result1 || !result2 || !result3) {
        dispatch(messageActions.error('地区选择不完整，请重新选择！'))
        return
      }
      const l1 = result1 && JSON.parse(result1)
      const l2 = result2 && JSON.parse(result2)
      const l3 = result3 && JSON.parse(result3)
      const l4 = result4 && JSON.parse(result4)
      const regions = [l1, l2, l3, l4].filter(item => !!item)
      const lastReg = regions[regions.length - 1]
      if(regions.length > 3){
        await this._confirmRegion(lastReg.id, regions)
      }else{
        try {
          // 如果能获取到数据，说明地区不是最后一级
          const res = await API_Common.getRegionsById(lastReg.id)
          if (res && !res[0]) {
            // 如果有返回值，但是为空数组，也是最后一级
            await this._confirmRegion(lastReg.id, regions)
          } else {
            dispatch(messageActions.error('地区选择不完整，请重新选择！'))
          }
        } catch (e) {
          // 如果获取数据出错了，说明是最后一级
          await this._confirmRegion(lastReg.id, regions)
        }
      }
     
    })
  }
  
  _confirmRegion = async (id, regions) => {
    const { goodsId } = this.props
    const region_str = regions.map(region => region['name']).join(' ')
    this.setState({ region: id, region_str, loading: true })
    try {
      const res = await API_Goods.getGoodsShip(goodsId, id)
      DeviceEventEmitter.emit(`DisBuyBtn-${goodsId}`, !res)
      this.setState({ inStore: !!res, loading: false })
    } catch (e) {
      this.setState({ loading: false })
    }
  }
  
  render() {
    const { region_str, loading, inStore } = this.state
    let content = region_str || '选择配送地区'
    if (loading) content = <Loading show={ true }/>
    if (!inStore) content = (
      <View>
        <Text>{ region_str }</Text>
        <Text style={ styles.ship_tip }>该地区无货</Text>
      </View>
    )
    return (
      <ItemCell
        label="送至"
        icon
        style={ { marginTop: 1 } }
        onPress={ this._selectAddress }
        content={ content }
      />
    )
  }
}

const styles = StyleSheet.create({
  ship_tip: {
    color: 'red',
    marginTop: 2,
    fontSize: 12
  }
})

export default connect()(GoodsShip)
