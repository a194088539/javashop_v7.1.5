/**
 * Created by Andste on 2018/11/8.
 */
import React, { Component } from 'react'
import {
  View,
  Image,
  Text,
  TextInput,
  TouchableOpacity,
  NativeModules,
  NativeEventEmitter,
  Platform,
  StatusBar,
  StyleSheet
} from 'react-native'
import { connect } from 'react-redux'
import { messageActions } from '../../redux/actions'
import { colors } from '../../../config'
import { Screen, RegExp, Foundation } from '../../utils'
import { BigButton, TextLabel } from '../../widgets'
import { isIphoneX } from 'react-native-iphone-x-helper'
import PhonePicker from 'react-native-phone-picker'
import Icon from 'react-native-vector-icons/Ionicons'
import { KeyboardAwareScrollView } from 'react-native-keyboard-aware-scroll-view'
import * as API_Address from '../../apis/address'
import * as API_Common from '../../apis/common'

class AddressAddEditScene extends Component {
  static navigationOptions = ({ navigation }) => {
    const { params = {} } = navigation.state
    return {
      title: params.address ? '编辑收货地址' : '新增收货地址'
    }
  }
  
  constructor(props) {
    super(props)
    const { params = {} } = props.navigation.state
    const { address } = params
    let region_str = ''
    if (address) {
      region_str += Foundation.getAddConnect(address)
      this.is_edit = true
    }
    this.state = {
      name: '',
      addr: '',
      tel: '',
      mobile: '',
      def_addr: 0,
      ship_address_name: '',
      region: this.is_edit ? (address['town_id'] || address['county_id']) : '',
      region_str,
      ...address
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
  
  _onMobileChangeText = (mobile) => this.setState({ mobile })
  _onNameChangeText = (name) => this.setState({ name })
  _onAddrChangeText = (addr) => this.setState({ addr })
  
  _selectContacts = () => {
    PhonePicker.select(mobile => {
      if (mobile) {
        this.setState({ mobile: mobile.replace(/[^\d]/g, '') })
      } else {
        this.props.dispatch(messageActions.error('出错了！'))
      }
    })
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
        this._confirmRegion(lastReg.id, regions)
      }else{
        try {
          // 如果能获取到数据，说明地区不是最后一级
          const res = await API_Common.getRegionsById(lastReg.id)
          if (res && !res[0]) {
            // 如果有返回值，但是为空数组，也是最后一级
            this._confirmRegion(lastReg.id, regions)
          } else {
            dispatch(messageActions.error('地区选择不完整，请重新选择！'))
          }
        } catch (e) {
          // 如果获取数据出错了，说明是最后一级
          this._confirmRegion(lastReg.id, regions)
        }
      }
      
    })
  }
  
  _confirmRegion = (id, regions) => {
    this.setState({
      region: id,
      region_str: regions.map(region => region['name']).join('')
    })
  }
  
  _save = async () => {
    let { name, mobile, addr, region } = this.state
    let { dispatch, navigation } = this.props
    if (!name) {
      dispatch(messageActions.error('收货人不能为空！'))
      return
    }
    if (!RegExp.mobile.test(mobile)) {
      dispatch(messageActions.error('联系方式格式有误！'))
      return
    }
    if (!region) {
      dispatch(messageActions.error('请选择所在地区！'))
      return
    }
    if (!addr) {
      dispatch(messageActions.error('详细地址不能为空！'))
      return
    }
    const params = JSON.parse(JSON.stringify(this.state))
    delete params.region_str
    if (this.is_edit) {
      await API_Address.editAddress(params['addr_id'], params)
      dispatch(messageActions.success('编辑成功！'))
    } else {
      await API_Address.addAddress(params)
      dispatch(messageActions.success('添加成功！'))
    }
    navigation.state.params.callback()
    navigation.goBack()
  }
  
  render() {
    const { name, mobile, addr, ship_address_name: tag_name, region_str } = this.state
    const isIos = Platform.OS === 'ios'
    return (
      <View style={ styles.container }>
        <StatusBar barStyle="dark-content"/>
        <KeyboardAwareScrollView
          keyboardOpeningTime={ 0 }
          keyboardDismissMode="on-drag"
        >
          <View style={ { flexDirection: 'row' } }>
            <View style={ { width: Screen.width - (isIos ? 85 : 0) } }>
              <AddressInput
                style={ styles.address_input }
                label="收货人:"
                value={ name }
                maxLength={ 10 }
                onChangeText={ this._onNameChangeText }
              />
              <AddressInput
                style={ styles.address_input }
                label="联系方式:"
                value={ mobile }
                maxLength={ 11 }
                keyboardType="numeric"
                returnKeyType="done"
                onChangeText={ this._onMobileChangeText }
              />
            </View>
            {
              isIos && <TouchableOpacity
                style={ styles.select_contacts }
                activeOpacity={ 1 }
                onPress={ this._selectContacts }
              >
                <Image
                  style={ styles.select_contacts_image }
                  source={ require('../../images/icon-select-contacts.png') }
                />
                <Text style={ styles.select_contacts_text }>选联系人</Text>
              </TouchableOpacity>
            }
          </View>
          <TouchableOpacity style={ styles.select_address_cell } onPress={ this._selectAddress }>
            <Text style={ styles.add_input_label }>所在地区:</Text>
            <View style={ { width: Screen.width - 20 - 22 - 80 } }><Text
              style={ { fontSize: 16 } }>{ region_str }</Text></View>
            <Icon name="ios-arrow-forward" color="#A8A9AB" size={ 20 }/>
          </TouchableOpacity>
          <AddressInput
            label="详细地址:"
            value={ addr }
            maxLength={ 30 }
            onChangeText={ this._onAddrChangeText }
          />
          <View style={ styles.tag_view }>
            <View style={ styles.tag_view_label }>
              <Text style={ styles.add_input_label }>标签:</Text>
            </View>
            <View style={ styles.tags_view }>
              <TextLabel
                style={ styles.tag_item }
                text="家"
                selected={ tag_name === '家' }
                onPress={ () => this.setState({ ship_address_name: '家' }) }
              />
              <TextLabel
                style={ styles.tag_item }
                text="公司"
                selected={ tag_name === '公司' }
                onPress={ () => this.setState({ ship_address_name: '公司' }) }
              />
              <TextLabel
                style={ styles.tag_item }
                text="学校"
                selected={ tag_name === '学校' }
                onPress={ () => this.setState({ ship_address_name: '学校' }) }
              />
            </View>
          </View>
          <View style={ styles.custom_tags }>
          
          </View>
        </KeyboardAwareScrollView>
        <BigButton style={ { width: Screen.width } } title="保存" onPress={ this._save }/>
      </View>
    )
  }
}

const AddressInput = ({ label, style, ...props }) => {
  return (
    <View style={ styles.add_input_view }>
      <Text style={ styles.add_input_label }>{ label }</Text>
      <TextInput
        style={ [styles.add_input, style] }
        clearButtonMode="while-editing"
        underlineColorAndroid="transparent"
        returnKeyType="done"
        { ...props }
      />
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#FFFFFF',
    paddingBottom: isIphoneX() ? 30 : 0
  },
  select_contacts: {
    justifyContent: 'center',
    alignItems: 'center',
    width: 85,
    height: 100 - Screen.onePixel,
    borderColor: colors.cell_line_backgroud,
    borderLeftWidth: Screen.onePixel,
    backgroundColor: '#FFFFFF'
  },
  select_contacts_image: {
    width: 30,
    height: 30
  },
  select_contacts_text: {
    marginTop: 10,
    fontSize: 14
  },
  address_input: {
    width: Screen.width - 20 - 75 - 85,
    paddingLeft: 10
  },
  select_address_cell: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    width: Screen.width,
    height: 50,
    paddingHorizontal: 10,
    borderColor: colors.cell_line_backgroud,
    borderBottomWidth: Screen.onePixel
  },
  add_input_view: {
    flexDirection: 'row',
    alignItems: 'center',
    width: Screen.width,
    paddingHorizontal: 10,
    paddingVertical: 5,
    height: 50,
    borderColor: colors.cell_line_backgroud,
    borderBottomWidth: Screen.onePixel
  },
  add_input_label: {
    textAlign: 'left',
    color: colors.text,
    fontSize: 16
  },
  add_input: {
    width: Screen.width - 20 - 80,
    height: 50,
    paddingLeft: 10
  },
  tag_view: {
    flexDirection: 'row',
    width: Screen.width,
    minHeight: 50,
    paddingHorizontal: 10
  },
  tag_view_label: {
    height: 50,
    justifyContent: 'center'
  },
  tags_view: {
    flexDirection: 'row',
    alignItems: 'center',
    marginLeft: 15
  },
  tag_item: {
    minWidth: 70,
    marginRight: 15,
    marginBottom: 0
  },
  custom_tags: {
    width: Screen.width
  }
})

export default connect()(AddressAddEditScene)
