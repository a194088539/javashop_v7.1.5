/**
 * Created by Andste on 2019-01-21.
 * 结算发票相关
 */
import React, { Component } from 'react'
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  TouchableWithoutFeedback,
  Keyboard,
  ScrollView,
  StatusBar,
  NativeModules,
  NativeEventEmitter,
  StyleSheet
} from 'react-native'
import { connect } from 'react-redux'
import { isIphoneX } from 'react-native-iphone-x-helper'
import { messageActions } from '../../redux/actions'
import { colors } from '../../../config'
import { Screen, RegExp } from '../../utils'
import { BigButton, TextLabel } from '../../widgets'
import { F16Text } from '../../widgets/Text'
import Icon from 'react-native-vector-icons/Ionicons'
import { KeyboardAwareScrollView } from 'react-native-keyboard-aware-scroll-view'
import Autocomplete from 'react-native-autocomplete-input';
import * as API_Members from '../../apis/members'
import * as API_Trade from '../../apis/trade'
import * as API_Common from '../../apis/common'

class CheckoutReceiptScene extends Component {
  static navigationOptions = {
    title: '发票信息'
  }
  
  constructor(props) {
    super(props)
    const { receipt = {}, inventories } = props.navigation.state.params
    this.shop_id = inventories.map(item => item.seller_id).join(',')
    this.state = {
      showAlert: true,
      showReceipts: false,
      isShowtip: true,
      receipt,
      receipts: [],
      invoiceInfo: '',
      invoiceAddressInfo: {},
      // 发票原始类型列表
      receipt_type_menu: [
        { show_detail: false, name: '增值税普通发票', status: 'ordin_receipt_status', checked: false, receipt_type: 'VATORDINARY'},
        { show_detail: false, name: '电子普通发票',  status: 'elec_receipt_status', checked: false, receipt_type: 'ELECTRO'},
        { show_detail: false, name: '增值税专用发票', status: 'tax_receipt_status', checked: false, receipt_type: 'VATOSPECIAL'}
      ],
      // 发票内容列表 code为代号 false 未选
      receipt_contents: [
        {
          receipt_content: '商品明细',
          code: false,
          // 显示所对应的发票类型 '' 代表对应所有
          receipt_type: ''
        },
        {
          receipt_content: '商品类别',
          code: false,
          // 显示所对应的发票类型 ELECTRO=>电子普通发票
          receipt_type: 'ELECTRO'
        }
      ],
      // 发票类型列表
      receipt_type_list: [
        { show_detail: false, name: '不开发票', status: '', checked: false, receipt_type: ''}
      ],
      // 发票抬头类型 个人为0 单位为1
      receipt_title_type: 0,
      receipts_types: [
        { name: '增值税普通发票', key: 'ordin_receipt_status', receipt_type: 'VATORDINARY', show:false },
        { name: '电子普通发票', key: 'elec_receipt_status', receipt_type: 'ELECTRO', show:false },
        { name: '增值税专用发票', key: 'tax_receipt_status', receipt_type: 'VATOSPECIAL', show:false },
        
      ],
      current_receipt_type: '',
      // 开票方式
      receipt_method: '订单完成后开票',
      // 上次选择的单位发票抬头
      last_receipt_title: '',
      // 发票表单
      receiptForm: {
        member_name: '',
        // 发票抬头
        receipt_title: '',
        //发票内容
        receipt_content: '不开发票',
        // 发票类型
        receipt_type: '',
        // 纳税人识别号
        tax_no: '',
        address: '',
        member_mobile: ''
      },
      need_receipt: false
    }
  }
  
  async componentDidMount() {
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
    this._initReceiptInfo()
  }

  _initReceiptInfo = async () => {
    if (this.shop_id) {
      await API_Members.queryMembersReceipt(this.shop_id).then(async response => {
        const _receipt_type_list = this.state.receipt_type_menu.filter(item => response[item.status] === 1)
        this.setState({receipt_type_list : [ ...this.state.receipt_type_list, ..._receipt_type_list ]})
        // 如果商家开启专票
        if (response.tax_receipt_status === 1) {
          // 获取增值税专用发票资质基础信息
          const invoiceInfo = await API_Members.queryInvoiceInfo()
          const invoiceAddressInfo = await API_Members.queryTicketAdress()
          await this.setState({ invoiceInfo: invoiceInfo, invoiceAddressInfo: invoiceAddressInfo })
        }
        // 如果没有发票信息
        if (!this.state.receipt) {
          this.state.receipt_type_list.forEach(key => {
            if(!key.receipt_type) key.checked = true
          })
        } else {
          // 如果有自带发票信息
          this.setState({ receiptForm: { ...this.state.receipt}, current_receipt_type: this.state.receipt.receipt_type })
          this.state.receipt_type_list.forEach(key => {
            if (key.receipt_type === this.state.current_receipt_type) key.checked = true
          })
          if (this.state.current_receipt_type === 'VATORDINARY' || this.state.current_receipt_type === 'VATOSPECIAL') {
            const _receipt = this.state.receipt_contents.find(key => key.receipt_content === this.state.receiptForm.receipt_content)
            if(_receipt && _receipt.code !== undefined && _receipt.code !== null) {
              _receipt.code = true
            }
          }
          this.setState({ need_receipt: true })
          await this._getReceiptList()
        }
      })
      // 如果存在发票信息
      if (this.state.receipt && this.state.receipt.receipt_title) {
        const _receipt = this.state.receipts.find(key => key.receipt_title === this.state.receiptForm.receipt_title)
        if (_receipt && _receipt.receipt_id) {
          this.setState({
            receiptForm: {
              ...this.state.receiptForm,
              receipt_id: _receipt.receipt_id
            }
          })
        }
        if (this.state.receipt.receipt_type === 'VATOSPECIAL') {
          this.setState({ 
            receiptForm:{
              ...this.state.receiptForm,
              receipt_title: this.state.invoiceInfo.company_name || '单位',
              detail_addr: this.state.receipt.detail_addr,
              member_name: this.state.receipt.member_name,
              region: !!this.state.receipt.region ? this.state.receipt.region : (!!this.state.invoiceAddressInfo.county_id ? this.state.invoiceAddressInfo.county_id : this.state.invoiceAddressInfo.city_id),
              address: !!this.state.receipt.address ? this.state.receipt.address : `${this.state.receipt.province} ${this.state.receipt.city} ${this.state.receipt.county} ${this.state.receipt.town}`
            },
            regions: [this.state.receipt.province_id, this.state.receipt.city_id, this.state.receipt.county_id, this.state.receipt.town_id]
          })
          this._receiptTypeOnPress(this.state.receipt_type_list.find(item => item.receipt_type == this.state.receipt.receipt_type))
        }
        this.state.receipt_contents.forEach(item => {
          if (item.receipt_content === this.state.receipt.receipt_content) {
            item.code = true
          } else {
            item.code = false
          }
        })

      } else { // 如果不存在发票信息
        this._receiptTypeOnPress(this.state.receipt_type_list[0])
      }
      this.setState({ receipt_title_type: this.state.receiptForm.receipt_title === '个人' ? 0 : 1 })
    }
  }

  _getReceiptList = async () => {
    const { current_receipt_type } = this.state
    if (current_receipt_type === 'VATORDINARY' || current_receipt_type === 'ELECTRO') {
      const receipts = await API_Members.getReceipts(current_receipt_type)
      this.setState({ receipts: receipts })
    }
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
      this._confirmRegion(lastReg.id, regions)
    })
  }

  _confirmRegion = (id, regions) => {
    this.setState({
      receiptForm:{
        ...this.state.receiptForm,
        region: id,
        address: regions.map(region => region['name']).join(' ')
      }
    })
  }
  
  _onPressContainer = () => {
    Keyboard.dismiss()
  }
  
  /**
   * 关闭顶部提示
   * @private
   */
  _onCloseAlert = () => {
    this.setState({ showAlert: false })
  }
  
  /**
   * 显示发票列表
   * @private
   */
  _onShowRecipts = () => {
    this.setState({ showReceipts: true })
  }
  
  /**
   * 关闭发票列表
   * @private
   */
  _onReceiptsClosed = () => {
    this.setState({ showReceipts: false })
  }

  /**
   * 选择已有receipt
   * @param receipt
   * @private
   */
  _onSelectReceipt = (receipt) => {
    this.setState({
      showReceipts: false,
      receiptForm: {  ...receipt },
      last_receipt_title: receipt.receipt_title
    })
  }
  
  /**
   * 确认发票信息
   * @private
   */
  _onConfirm = async () => {
    let { need_receipt } = this.state
    const { state, goBack } = this.props.navigation
    // 如果不需要发票 则调用取消发票API
    if (!need_receipt) {
      API_Trade.cancelReceipt().then(() => {
        receipt = ''
        state.params.callback(receipt)
        goBack()
      })
      return false
    }
    const _speparams = {
      receipt_method: this.state.receipt_method,
      bank_name: this.state.invoiceInfo.bank_name,
      bank_account: this.state.invoiceInfo.bank_account,
      reg_addr: this.state.invoiceInfo.register_address,
      reg_tel: this.state.invoiceInfo.register_tel,
      member_name: this.state.receiptForm.member_name,
      region: this.state.receiptForm.region,
      detail_addr: this.state.receiptForm.detail_addr,
      address: this.state.receiptForm.address,
      tax_no: this.state.invoiceInfo.taxpayer_code
    }
    const _elecparams = {
      member_mobile: this.state.receiptForm.member_mobile,
      member_email: this.state.receiptForm.member_email,
      province: this.state.receiptForm.province
    }
    let params = {
      // 发票类型
      receipt_type: this.state.current_receipt_type,
      // 发票抬头
      receipt_title: this.state.receiptForm.receipt_title,
      // 发票内容
      receipt_content: this.state.receiptForm.receipt_content,
      // 税号
      tax_no: this.state.receiptForm.tax_no
    }
    if(this.state.current_receipt_type === 'ELECTRO') {
      params = {
        ...params,
        ..._elecparams
      }
    }
    if (this.state.current_receipt_type === 'VATOSPECIAL') {
      params = {
        ...params,
        ..._elecparams,
        ..._speparams
      }
    }
    // 校验
    // 抬头
    if(!params.receipt_title) {
      this.props.dispatch(messageActions.error('请填写发票抬头'))
      return false
    }
    // 税号
    if (params.receipt_title !== '个人' && !params.tax_no && !RegExp.TINumber.test(params.tax_no)) {
      this.props.dispatch(messageActions.error('纳税人识别号不正确'))
      return false
    }
     // 验证是否相同
     API_Trade.setRecepit(params).then(async () => {
      // 如果对发票列表进行操作 编辑/新增单位发票  如果操作增值税普通发票并且抬头不是个人 则调用以下API
      if(this.state.current_receipt_type !== 'VATOSPECIAL' && params.receipt_title !== '个人') {
        const isEdit = !!(this.state.receiptForm.receipt_id && this.state.receipts.find(key => key.receipt_title === this.state.receiptForm.receipt_title))
        Promise.all([
          isEdit
            ? API_Members.editReceipt(this.state.receiptForm.receipt_id, params)
            : API_Members.addReceipt(params)
        ]).then(async () => {
          this.props.dispatch(messageActions.success('设置成功！'))
          await this._getReceiptList()
          state.params.callback(params)
          goBack()
        })
      } else {
        this.props.dispatch(messageActions.success('设置成功！'))
        await this._getReceiptList()
        state.params.callback(params)
        goBack()
      }
    })
  }
  
  /**
   * 发票抬头信息发生改变
   * @private
   */
  _onChangeTextTitle = (text) => {
    const { receiptForm } = this.state
    this.setState({
      receiptForm: {
        ...receiptForm,
        receipt_title: text
      }
    })
  }

  /** 切换发票类型 */
  _receiptTypeOnPress = async (receipt) => {
    this.setState({ isShowtip: true })
    // 切换发票校验
    if(receipt.checked) return
    if(this.state.invoiceInfo && this.state.invoiceInfo.status !== 'AUDIT_PASS') {
      this.props.dispatch(messageActions.error('会员还未申请增票资质或者申请审核未通过'))
      return
    }
    // 获取当前发票类型 并且选择当前发票相关信息
    await this.setState({ current_receipt_type: receipt.receipt_type })
    // 获取会员发票列表
    await this._getReceiptList()
    // 选定发票内容 如果是增值税普通发票/专用发票 内容为商品明细； 电子发票可选类别【商品明细 | 商品类别】
    if (this.state.current_receipt_type === 'VATORDINARY' || this.state.current_receipt_type === 'VATOSPECIAL' || this.state.current_receipt_type === 'ELECTRO') {
      // 设置发票内容
      this.setState({ 
        receiptForm: {
          ...this.state.receiptForm,
          receipt_content: '商品明细'
        }
       })
      this.state.receipt_contents.forEach(key => key.code = false)
      this.state.receipt_contents.find(key => key.receipt_content === '商品明细').code = true
    }
    if(this.state.current_receipt_type === 'VATORDINARY' || this.state.current_receipt_type === 'ELECTRO') {
      // 设置发票抬头 如果不存在发票则首先选中默认发票 否则选中 个人
      const _receipt = this.state.receipts.find(key => key.is_default === 1)
      if(_receipt) {
        this.setState({ 
          receiptForm: JSON.parse(JSON.stringify(_receipt)),
          receipt_title_type: 1
         })
      } else {
        this.setState({ 
          receiptForm: {
            ...this.state.receiptForm,
            receipt_title: '个人'
          },
          receipt_title_type: 0
         })
      }
    }
    if (this.state.current_receipt_type === 'VATOSPECIAL') {
      this.setState({ 
        receipt_title_type: 1,
        receiptForm: {
          ...this.state.receiptForm,
          receipt_title: this.state.invoiceInfo.company_name || '单位',
          detail_addr: this.state.invoiceAddressInfo.detail_addr,
          member_mobile: this.state.invoiceAddressInfo.member_mobile,
          member_name: this.state.invoiceAddressInfo.member_name,
          region: !!this.state.invoiceAddressInfo.county_id ? this.state.invoiceAddressInfo.county_id : this.state.invoiceAddressInfo.city_id,
          address: `${this.state.invoiceAddressInfo.province || ''} ${this.state.invoiceAddressInfo.city || ''} ${this.state.invoiceAddressInfo.county || ''} ${this.state.invoiceAddressInfo.town || ''}`
        },
        regions: [this.state.invoiceAddressInfo.province_id, this.state.invoiceAddressInfo.city_id, this.state.invoiceAddressInfo.county_id, this.state.invoiceAddressInfo.town_id]
      })
      if (this.state.receipt) {
        this.setState({
          receiptForm: {
            ...this.state.receiptForm,
            detail_addr: this.state.receipt.detail_addr || this.state.invoiceAddressInfo.detail_addr || '',
            member_mobile: this.state.receipt.member_mobile || this.state.invoiceAddressInfo.member_mobile || '',
            member_name: this.state.receipt.member_name || this.state.invoiceAddressInfo.member_name || '',
            address: `
              ${this.state.receipt.province || this.state.invoiceAddressInfo.province || ''} 
              ${this.state.receipt.city || this.state.invoiceAddressInfo.city || ''} 
              ${this.state.receipt.county || this.state.invoiceAddressInfo.county || ''} 
              ${this.state.receipt.town || this.state.invoiceAddressInfo.town || ''}`
          },
          regions: [
            this.state.receipt.province_id || this.state.invoiceAddressInfo.province_id, 
            this.state.receipt.city_id || this.state.invoiceAddressInfo.city_id, 
            this.state.receipt.county_id || this.state.invoiceAddressInfo.county_id, 
            this.state.receipt.town_id || this.state.invoiceAddressInfo.town_id
          ]
        })
      }
      this.setState({ 
        receiptForm: {
          ...this.state.receiptForm,
          address: this.state.receiptForm.address.replace(/\s+/g, ' ')
        }
      })
      this._handleChooseReceipt()
    }
    this.state.receipt_type_list.forEach(key => key.checked = false)
    receipt.checked = true
    this.setState({ need_receipt: !!receipt.receipt_type })
  }

  // 当选择非个人发票类型的时候 匹配默认发票
  _handleChooseReceipt = () => {
    if (this.state.current_receipt_type === 'VATOSPECIAL') {
      this.setState({ receiptForm: { ...this.state.receiptForm} })
      return
    }
    this.setState({ 
      receiptForm: {
        ...this.state.receiptForm,
        receipt_title : '',
        tax_no: ''
      } 
    })
    let _receipt
    if (this.state.receiptForm.receipt_id && this.state.last_receipt_title) {
      _receipt = this.state.receipts.find(key => key.receipt_title === this.last_receipt_title)
    } else {
      _receipt = this.state.receipts.find(key => key.is_default === 1)
    }
    if (_receipt) {
      this.setState({
        receiptForm: { ..._receipt }
       })
      if(!_receipt.receipt_content) {
        this.receipt_contents.forEach(key => key.code = false)
      }
    }
    this.setState({ 
      last_receipt_title: this.state.receiptForm.receipt_title,
      receipt_title_type: 1
     })
  }

  _handlePersonReceipt = () => {
    this.setState({ 
      receiptForm: {
        ...this.state.receiptForm,
        tax_no: '',
        receipt_title: '个人'
      },
      receipt_title_type: 0
    })
  }

  _handleOutReceipt = () => {
    this.setState({ showReceipts: true })
  }

  _handleSelectReceipt = (receipt) => {
    this.setState({
      show_receipts: false,
      receiptForm: { ...receipt },
      last_receipt_title: this.state.receiptForm.receipt_title
    })
  }

  // 选择发票内容
  _handleChooseContent = (item) => {
    this.setState({ 
      receiptForm: {
        ...this.state.receiptForm,
        receipt_content: item.receipt_content
      }
     })
    this.state.receipt_contents.forEach(key => key.code = false)
    item.code = true
  }

  render() {
    const { showAlert, receipts, receipt_contents, show_receipts, current_receipt_type, receipt_title_type, receiptForm, receipt_type_list, invoiceInfo, showReceipts } = this.state
    return (
      <TouchableWithoutFeedback onPress={ this._onPressContainer }>
        <View style={ styles.container }>
          <StatusBar barStyle="dark-content"/>
          <KeyboardAwareScrollView 
            enableOnAndroid
            extraScrollHeight={ 130 }
            keyboardOpeningTime={ 0 }>
            {/*发票类型*/ }
            <View style={ styles.item }>
              <View style={ styles.item_header }>
                <Text style={ styles.item_header_text }>发票类型</Text>
              </View>
              <View style={ styles.item_body }>
                { receipt_type_list.length && receipt_type_list.map((item, index) => (
                  <TextLabel
                      text={ item.name }
                      key={ index }
                      selected={ current_receipt_type === item.receipt_type }
                      onPress={ () => this._receiptTypeOnPress(item) }
                  />
                )) }
              </View>
            </View>
            {/*发票抬头*/ }
            { current_receipt_type !== '' && 
              <View>
                <View style={ styles.item }>
                  <View style={ styles.item_header }>
                    <Text style={ styles.item_header_text }>发票抬头</Text>
                  </View>
                  <View style={ styles.receipt_title }> 
                    { current_receipt_type !== 'VATOSPECIAL' && 
                      <TextLabel
                        text="个人"
                        selected={ receipt_title_type === 0 }
                        onPress={ () => this._handlePersonReceipt() }
                      />}
                    
                    <TextLabel
                      text="单位"
                      selected={ receipt_title_type === 1 }
                      onPress={ () => this._handleChooseReceipt() }
                    />
                    { current_receipt_type !== 'VATOSPECIAL' && receipt_title_type === 1 && 
                      <View style={ styles.receipt_title_view }>
                          <Autocomplete
                            style={ styles.item_input }
                            placeholder="请填写单位名称"
                            data={ receipts }
                            defaultValue={ receiptForm.receipt_title }
                            hideResults={ !showReceipts }
                            onPress={ (text) => { this.setState({ receiptForm: { ...this.state.receiptForm,receipt_title: text } }); this._onReceiptsClosed  } }
                            onChangeText={ (text) => { this.setState({ receiptForm: { ...this.state.receiptForm,receipt_title: text } }) } }
                            onFocus={ this._onShowRecipts }
                            renderItem={({ item, index }) => (
                              <TouchableOpacity key={ index } onPress={ () => { this._onSelectReceipt(item) } }>
                                <Text key={ index }>{item.receipt_title}</Text>
                              </TouchableOpacity>
                            )}
                          />
                          <ReceiptInput
                            label="税号"
                            maxLength={ 18 }
                            value={ receiptForm.tax_no }
                            placeholder="抬头为个人无需填写"
                            onChangeText={ (text) => { this.setState({ receiptForm: { ...this.state.receiptForm, tax_no: text } }) } }
                          />
                      </View>}
                    { show_receipts && receipts.map((rep,index) => (
                        <TextLabel text={ rep.receipt_title } key={index} onPress={ () => this._handleSelectReceipt(rep) } />
                    )) }
                  </View>
                </View>
                { current_receipt_type === 'ELECTRO' && 
                <View style={ styles.item }>
                  <View style={ styles.item_header }>
                    <Text style={ styles.item_header_text }>收票人信息</Text>
                  </View>
                  <View style={ styles.item_body }>
                    <ReceiptInput
                      label="*收票人手机"
                      maxLength={ 30 }
                      value={ receiptForm.member_mobile }
                      placeholder="请输入收票人手机"
                      onChangeText={ (text) => { this.setState({ receiptForm: { ...this.state.receiptForm, member_mobile: text } }) } }
                    />
                    <ReceiptInput
                      label="  收票人邮箱"
                      maxLength={ 30 }
                      value={ receiptForm.member_email }
                      placeholder="请输入收票人邮箱"
                      onChangeText={ (text) => { this.setState({ receiptForm: { ...this.state.receiptForm, member_email: text } }) } }
                    />
                  </View>
                </View> 
                }
                { current_receipt_type === 'VATOSPECIAL' && 
                <View style={ styles.item }>
                  <View style={ styles.item_header }>
                    <Text style={ styles.item_header_text }>收票信息</Text>
                  </View>
                  <View style={ styles.item_body }>
                    <ReceiptInput
                      label="收票人"
                      maxLength={ 30 }
                      value={ receiptForm.member_name }
                      placeholder="请输入收票人姓名"
                      onChangeText={ (text) => { this.setState({ receiptForm: { ...this.state.receiptForm, member_name: text } }) } }
                    />
                    <ReceiptInput
                      label="手机号"
                      maxLength={ 30 }
                      value={ receiptForm.member_mobile }
                      placeholder="请输入收票人手机号"
                      onChangeText={ (text) => { this.setState({ receiptForm: { ...this.state.receiptForm, member_mobile: text } }) } }
                    />
                    <TouchableOpacity style={ styles.select_address_cell } onPress={ this._selectAddress }>
                      <Text style={ styles.add_input_label }>省市区县</Text>
                      <View style={ { width: Screen.width - 20 - 22 - 80 } }>
                        <Text style={ { fontSize: 16 } }>{ receiptForm.address }</Text>
                      </View>
                      <Icon name="ios-arrow-forward" color="#A8A9AB" size={ 20 }/>
                    </TouchableOpacity>
                    <ReceiptInput
                      label="详细地址"
                      maxLength={ 300 }
                      multiline={ true }
                      style={ {height: 60} }
                      value={ receiptForm.detail_addr }
                      placeholder="请输入详细地址"
                      onChangeText={ (text) => { this.setState({ receiptForm: { ...this.state.receiptForm, detail_addr: text } }) } }
                    />
                  </View>
                </View> 
                }
                <View style={ styles.item }>
                  <View style={ styles.item_header }>
                    <Text style={ styles.item_header_text }>发票内容</Text>
                  </View>
                  { showAlert ? (
                    <View style={ styles.alert }>
                      <View style={ styles.alert_icon }>
                        <Icon name="ios-notifications-outline" color="#FFF" size={ 20 }/>
                      </View>
                      <View style={ styles.alert_content }>
                        <Text style={ styles.alert_content_text }>
                         根据国家相关规定，发票的开票内容需与购买的商品一致，暂不支持手动选择开票内容。
                        </Text>
                      </View>
                      <TouchableOpacity style={ styles.alert_icon } onPress={ this._onCloseAlert }>
                        <Icon name="ios-close-outline" color="#FFF" size={ 20 }/>
                      </TouchableOpacity>
                    </View>
                  ) : undefined }
                  <View style={ styles.item_body }>
                    { receipt_type_list.length && receipt_contents.map((item, index) => (
                      (item.receipt_type === current_receipt_type || !item.receipt_type) && 
                      <View key={ index }>
                        <TextLabel
                          text={ item.receipt_content }
                          selected={ item.code }
                          onPress={ () => this._handleChooseContent(item) }
                        />
                        { !item.receipt_type && current_receipt_type !== 'VATOSPECIAL' && <Text>商品明细显示详细商品名称及价格信息</Text> }
                        { item.receipt_type === 'ELECTRO' && <Text>本单可开发票内容:商品类别集合。</Text> }
                      </View>
                    )) }
                  </View>
                </View>
                { current_receipt_type === 'VATOSPECIAL' && 
                <View style={ styles.item }>
                  <View style={ styles.item_header }>
                    <Text style={ styles.item_header_text }>明细</Text>
                  </View>
                  <View style={ styles.item_body }>
                    <F16Text>单位名称：{ invoiceInfo.company_name }</F16Text>
                    <F16Text style={ styles.vatospecial_text }>纳税人识别号：{ invoiceInfo.taxpayer_code }</F16Text>
                    <F16Text style={ styles.vatospecial_text }>注册地址：{ invoiceInfo.register_address }</F16Text>
                    <F16Text style={ styles.vatospecial_text }>注册电话：{ invoiceInfo.register_tel }</F16Text>
                    <F16Text style={ styles.vatospecial_text }>开户银行：{ invoiceInfo.bank_name }</F16Text>
                    <F16Text style={ styles.vatospecial_text }>银行账户：{ invoiceInfo.bank_account }</F16Text>
                 </View>
                </View> }
              </View>
            }
          </KeyboardAwareScrollView>
          <BigButton
            title="保存"
            style={ styles.save_btn }
            onPress={ this._onConfirm }
          />
        </View>
      </TouchableWithoutFeedback>
    )
  }
}

const ReceiptInput = ({ label, style, ...props }) => {
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
    flex: 1
  },
  alert: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    backgroundColor: '#F07F7F'
  },
  alert_icon: {
    alignItems: 'center',
    justifyContent: 'center',
    width: 30,
    height: 44,
    color: '#fff'
  },
  alert_content: {
    width: Screen.width - 30 * 2
  },
  alert_content_text: {
    color: '#FFF'
  },
  item: {
    backgroundColor: '#FFF',
    marginBottom: 10,
    paddingHorizontal: 10
  },
  item_header: {
    flexDirection: 'row',
    alignItems: 'center',
    height: 40
  },
  item_header_text: {
    fontSize: 16
  },
  receipt_type_right_text: {
    fontSize: 16,
    flexDirection: 'column',
    justifyContent: 'flex-end',
  },
  item_body: {
    flexDirection: 'column',
    alignItems: 'flex-start',
    paddingVertical: 10
  },
  receipt_title: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingVertical: 10
  },
  receipt_title_view: {
    width: Screen.width - 150
  },
  item_input: {
    height: 40,
    padding: 10,
    backgroundColor: '#F1F2F5'
  },
  item_sel_icon: {
    position: 'absolute',
    right: 10
  },
  modal_style: {
    flexDirection: 'row',
    height: 50,
    marginBottom: isIphoneX() ? 30 : 0,
    backgroundColor: '#FFF'
  },
  receipts_scroll_view: {
    flex: 1
  },
  receipt_item: {
    justifyContent: 'center',
    width: Screen.width,
    height: 40,
    paddingHorizontal: 10,
    borderBottomWidth: Screen.onePixel,
    borderBottomColor: colors.cell_line_backgroud
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
  add_input: {
    width: Screen.width - 20 - 80,
    height: 50,
    paddingLeft: 10
  },
  add_input_label: {
    textAlign: 'left',
    color: colors.text,
    fontSize: 16
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
  receipt_item_title: {
    fontSize: 14,
    color: colors.text
  },
  vatospecial_text: {
    marginTop: 8
  },
  save_btn: {
    width: Screen.width
  }
})

export default connect()(CheckoutReceiptScene)