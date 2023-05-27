/**
 * Created by Andste on 2018-12-11.
 */
import React, { PureComponent } from 'react'
import {
  Alert,
  View,
  Image,
  Text,
  DeviceEventEmitter,
  Platform,
  ScrollView,
  StyleSheet
} from 'react-native'
import { connect } from 'react-redux'
import { messageActions, cartActions } from '../../redux/actions'
import { navigate } from '../../navigator/NavigationService'
import { isIphoneX } from 'react-native-iphone-x-helper'
import { colors } from '../../../config'
import { Screen, Foundation } from '../../utils'
import { Modal } from '../../components'
import { TextLabel, Quantity, BigButton } from '../../widgets'
import ItemCell from './GoodsItemCell'
import * as API_Trade from '../../apis/trade'

class GoodsSkus extends PureComponent {
  constructor(props) {
    super(props)
    this.state = {
      showSkusModal: false
    }
  }
  
  componentDidMount() {
    const { goodsId } = this.props
    // 监听展示skus模态框事件
    this.listenerSkusModal = DeviceEventEmitter.addListener(`ShowGoodsSkusModal-${goodsId}`, this.showModal)
  }
  
  componentWillUnmount() {
    // 当组件卸载时，移除监听事件
    if (this.listenerSkusModal) {
      this.listenerSkusModal.remove()
    }
  }
  
  /**
   * 显示skus模态框
   */
  showModal = () => {
    this.setState({ showSkusModal: true })
  }
  
  /**
   * skus模态框关闭
   * @private
   */
  _onModalClosed = () => {
    this.setState({ showSkusModal: false })
  }
  
  /**
   * 添加到购物车
   * @returns {Promise<void>}
   * @private
   */
  _onAddToCart = async () => {
    try {
      const { selectedSku, buyNum } = this.props
      if (await this._checkCanBuy()) {
        const { sku_id } = selectedSku
        await this.setState({ showSkusModal: false })
        await API_Trade.addToCart(sku_id, buyNum)
        const { dispatch } = this.props
        await dispatch(cartActions.getCartDataAction())
        dispatch(messageActions.success('加入购物车成功！'))
      }
    } catch(err) {
    }
    

  }
  
  /**
   * 立即购买
   * @returns {Promise<void>}
   * @private
   */
  _onBuyNow = async () => {
    try {
      const { selectedSku, buyNum } = this.props
      if (await this._checkCanBuy()) {
        const { sku_id } = selectedSku
        await this.setState({ showSkusModal: false })
        await API_Trade.buyNow(sku_id, buyNum)
        navigate('Checkout', { way: 'BUY_NOW' })
      }
    } catch(err) {
    }
  }
  
  /**
   * 检查是否可以购买
   * @private
   */
  _checkCanBuy = async () => {
    const { user, selectedSku } = this.props
    if (!user) {
      await this.setState({ showSkusModal: false })
      navigate('Login')
      return false
    }
    if (!selectedSku) {
      Alert.alert('提示', '请选择规格！')
      return false
    }
    return true
  }
  
  render() {
    const { props } = this
    const { buyNum } = this.props
    const { showSkusModal } = this.state
    return (
      <ItemCell
        label="已选"
        icon
        style={ { marginTop: 10 } }
        onPress={ this.showModal }
        content={
          <View>
            <View style={ styles.skus_cell }>
              <Text>{ props['showInfo']['specVals'] } { buyNum }件</Text>
            </View>
            <Modal
              header={ <SkusHeader data={ props['showInfo'] }/> }
              style={ styles.modal_style }
              headerStyle={ { height: 100 } }
              isOpen={ showSkusModal }
              onClosed={ this._onModalClosed }
            >
              <ScrollView
                style={ styles.skus_scroll }
                showsHorizontalScrollIndicator={ false }
                showsVerticalScrollIndicator={ false }
              >
                <SkusList
                  data={ props.specList }
                  onPress={ props['onSelectSpec'] }
                />
                <View style={ styles.skus_spec_quantity }>
                  <View style={ [styles.skus_spec_title, styles.skus_quantity_title] }>
                    <Text style={ styles.skus_spec_text }>数量</Text>
                  </View>
                  <Quantity
                    defaultValue={ buyNum }
                    onPress={ props['onQuantityPress'] }
                    onEndEditing={ props['onQuantityEditing'] }
                  />
                </View>
              </ScrollView>
              <SkusBtns
                disBuyBtn={ props['disBuyBtn'] }
                onAddToCart={ this._onAddToCart }
                onBuyNow={ this._onBuyNow }
              />
            </Modal>
          </View>
        }
      />
    )
  }
}

const SkusHeader = ({ data }) => {
  return (
    <View style={ styles.skus_header }>
      <View style={ styles.skus_header_img_box }>
        <Image style={ styles.skus_header_img } source={ { uri: data['image'] } }/>
      </View>
      <View style={ styles.skus_header_info }>
        <View style={ styles.skus_header_info_text }>
          { data['proPrce'] !== undefined
            ? (
              <Text style={ styles.skus_header_info_price }>
                ￥{ Foundation.formatPrice(data['proPrce']) }
                { data['point'] ? <Text> + { data['point'] }积分</Text> : undefined }
              </Text>
            )
            : <Text style={ styles.skus_header_info_price }>￥{ Foundation.formatPrice(data['price']) }</Text>
          }
          <Text style={ styles.skus_header_info_sn }>商品编号：{ data['sn'] }</Text>
        </View>
      </View>
    </View>
  )
}

const SkusList = ({ data, onPress }) => {
  return (
    <View style={ styles.skus_spec_container }>
      { data.map((item, specIndex) => {
        return (
          <View key={ item['spec_id'] }>
            <View style={ styles.skus_spec_title }>
              <Text style={ styles.skus_spec_text }>
                { item['spec_name'] }
              </Text>
            </View>
            <View style={ styles.skus_spec_value }>
              { item['valueList'].map(_item => (
                <TextLabel
                  key={ _item['spec_value_id'] }
                  text={ _item['spec_value'] }
                  selected={ _item['selected'] }
                  onPress={ () => onPress(item, specIndex, _item) }
                />
              )) }
            </View>
          </View>
        )
      }) }
    </View>
  )
}

const SkusBtns = ({ disBuyBtn, onAddToCart, onBuyNow }) => {
  return (
    <View style={ styles.skus_btns }>
      <BigButton
        style={ [styles.skus_btn, { backgroundColor: '#ffb03f' }] }
        title="加入购物车"
        disabled={ disBuyBtn }
        onPress={ onAddToCart }
      />
      <BigButton
        style={ styles.skus_btn }
        title="立即购买"
        disabled={ disBuyBtn }
        onPress={ onBuyNow }
      />
    </View>
  )
}

const styles = StyleSheet.create({
  skus_cell: {
    flexDirection: 'row'
  },
  modal_style: {
    height: Screen.height - 200,
    backgroundColor: '#FFF'
  },
  skus_header: {
    flexDirection: 'row',
    width: Screen.width,
    paddingLeft: 10,
    paddingRight: 10,
    borderBottomWidth: Screen.onePixel,
    borderBottomColor: colors.cell_line_backgroud,
    ...Platform.select({
      ios: { height: 100 },
      android: { height: 120 }
    })
  },
  skus_scroll: {
    paddingHorizontal: 10
  },
  skus_header_img_box: {
    width: 100,
    height: 100
  },
  skus_header_img: {
    position: 'absolute',
    ...Platform.select({
      ios: { top: -20 },
      android: { top: 10 }
    }),
    width: 99,
    height: 99,
    borderRadius: 5,
    shadowOffset: {
      width: 0,
      height: 0
    },
    shadowColor: '#000',
    shadowOpacity: .75,
    shadowRadius: 5,
    backgroundColor: '#FFF',
    borderWidth: 1,
    borderColor: '#FFF'
  },
  skus_header_info: {
    justifyContent: 'flex-end',
    alignItems: 'flex-start',
    width: Screen.width - 20 - 100,
    height: 100,
    paddingLeft: 10
  },
  skus_header_info_text: {
    marginBottom: 20
  },
  skus_header_info_price: {
    color: colors.main,
    fontSize: 18,
    fontWeight: '800'
  },
  skus_header_info_sn: {
    color: '#777',
    fontSize: 14
  },
  skus_spec_container: {
    flex: 1,
    backgroundColor: '#FFF'
  },
  skus_spec_title: {
    height: 35,
    justifyContent: 'center'
  },
  skus_spec_text: {
    color: '#777',
    fontSize: 14
  },
  skus_spec_value: {
    flexDirection: 'row',
    flexWrap: 'wrap'
  },
  skus_spec_quantity: {
    flexDirection: 'row',
    alignItems: 'center',
    marginTop: 7
  },
  skus_quantity_title: {
    marginRight: 10
  },
  skus_btns: {
    flexDirection: 'row',
    width: Screen.width,
    height: 50,
    backgroundColor: '#FFF',
    marginBottom: isIphoneX() ? 30 : 0
  },
  skus_btn: {
    width: Screen.width / 2
  }
})

export default connect(state => ({
  user: state.user.user
}))(GoodsSkus)
