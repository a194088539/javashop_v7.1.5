import React, {Component} from 'react'
import {
    FlatList,
    Image,
    StyleSheet,
    TouchableOpacity,
    View, Text, Picker, TextInput
} from 'react-native'
import { connect } from 'react-redux'
import {navigate} from '../../navigator/NavigationService'
import {colors} from '../../../config'
import {Foundation, Screen} from '../../utils'
import {F16Text, F14Text} from '../../widgets/Text'
import {BigButton, Loading, TextLabel} from '../../widgets'
import * as API_AfterSale from '../../apis/after-sale'
import Icon from "react-native-vector-icons/Ionicons"
import Dialog, {
    DialogTitle,
    DialogContent,
    DialogFooter,
    DialogButton,
} from 'react-native-popup-dialog';
import {isIphoneX} from "react-native-iphone-x-helper";
import { KeyboardAwareScrollView } from 'react-native-keyboard-aware-scroll-view'
import {messageActions} from "../../redux/actions";


class ApplyAfterSaleScene extends Component {

    static navigationOptions = {
        title: '申请售后 '
    }

    constructor(props, context) {
        super(props, context)
        const {params} = this.props.navigation.state
        if (params.sku_id) {
            this.sn = params.order_sn
            this.sku_id = params.sku_id
        } else {
            this.sn = params.data.sn
        }

        this.state = {
            defaultDialog: false,
            defaultRefundDialog: false,
            order: {},
            sku_list: [],
            original_way: '',
            return_money: 0,
            return_point: 0,
            original_money: 0,
            loading: false,
            sku_num: 0,
            // 是否为取消订单模式
            isCancel: !(!!this.sku_id),
            order_sn: this.sn,
            sku_id: this.sku_id,
            return_num: 0,
            refund_reason: '',
            account_type: '',
            return_account: '',
            customer_remark: '',
            bank_name: '',
            bank_account_number: '',
            bank_account_name: '',
            bank_deposit_name: '',
            // 是否为原路返回方式
            original_way: false,
            refuse_type: 'RETURN_MONEY'
        }
    }


    componentDidMount() {
        this._getAfterSaleData()
    }

    _switchFunction = async (_refund_type) => {
        this.setState({
            refuse_type: _refund_type
        })
    }

    _getAfterSaleData = async () => {
        await this.setState({loading: true})
        try{
            const afterSaleData = await API_AfterSale.getAfterSaleData(this.sn, this.sku_id)
            this.setState({
                loading: false,
                order: afterSaleData.order,
                sku_list: afterSaleData.sku_list,
                return_money: afterSaleData.return_money,
                return_point: afterSaleData.return_point,
                original_money: afterSaleData.return_money,
                service_type: {1: '退款', 2: '退货'},
                sku_num: afterSaleData.sku_list[0].num,
                return_num: afterSaleData.sku_list[0].num
            })
            if (afterSaleData.original_way === 'yes') {
                this.setState({
                    account_type: '',
                    original_way: true
                })
            }
        }catch(error){
            this.setState({
                loading: false
            })
        }
        
    }


    _updateNum = async (symbol) => {
        const {sku_num, return_num} = this.state
        const { refund_price } = this.state.sku_list[0]
        if (symbol === '-' && return_num < 2) {
            return
        }
        if (symbol === '+' && return_num >= sku_num) {
            return
        }
        let _num = symbol === '+' ? return_num + 1 : return_num - 1
        this.setState({
            return_num: _num,
            return_money: Foundation.formatPrice(refund_price * _num)
        })

    }

    _setDetail = async (text) => {
        this.setState({customer_remark: text})
    }
    _renderItem = ({item}) => (<GoodsItem
            data={item}
            nav={this.nav}/>
    )

    _onSubmit = async () => {

        const {order_sn, return_num, refund_reason, account_type, return_account, customer_remark, refuse_type, 
            bank_name, bank_account_number, bank_account_name, bank_deposit_name, order, return_money, isCancel, original_way} = this.state
        if (!refund_reason) {
            await this.props.dispatch(messageActions.error('请选择申请原因!'))
            return
        }
        if (!original_way && !account_type) {
            await this.props.dispatch(messageActions.error('请选择申退款方式!'))
            return
        }
        if (!original_way && !!account_type && account_type !== 'BANKTRANSFER' && !return_account ){
            await this.props.dispatch(messageActions.error('请填写退款账号!'))
            return
        }
        if (!!account_type && account_type === 'BANKTRANSFER' && (!bank_name || !bank_deposit_name || !bank_account_name || !bank_account_number)  ){
            await this.props.dispatch(messageActions.error('请检查银行账户信息是否完整!'))
            return
        }
        const param = {
            order_sn,
            return_num,
            refund_reason,
            account_type,
            return_account,
            customer_remark,
            bank_name,
            return_money,
            bank_account_number,
            bank_account_name,
            order_price:order.order_price,
            bank_deposit_name
        }
        if (refuse_type === 'RETURN_MONEY') {
            if (isCancel) {
                await API_AfterSale.applyAfterSaleCancel(param)
            }else{
                param.sku_id = this.sku_id
                await API_AfterSale.applyAfterSaleMoney(param)
            }
        } else {
            param.sku_id = this.sku_id
            await API_AfterSale.applyAfterSaleGoods(param)
        }
        await this.props.dispatch(messageActions.success('申请成功！'))
        const {navigation} = this.props
        const {params = {}} = navigation.state
        params.callback && params.callback()
        navigation.goBack()
    }

    _accountType = (type) => {
        switch (type) {
          case 'ALIPAY' : return '支付宝'
          case 'WEIXINPAY' : return '微信'
          case 'BANKTRANSFER' : return '银行转账'
          default: return '请选择退款方式'
        }
    }

    render() {
        const {sku_list, loading, refund_reason, return_num, return_money, refuse_type, order, isCancel, original_way, account_type} = this.state
        return (
            <View style={styles.container}>
                <View style={styles.body}>
                    <KeyboardAwareScrollView
                        enableOnAndroid
                        extraScrollHeight={ 130 }
                        keyboardOpeningTime={ 0 }>
                        <View style={styles.refund_goods}>
                            <FlatList
                                data={sku_list}
                                renderItem={this._renderItem}
                                keyExtractor={(_, index) => String(index)}
                                getItemLayout={this._getItemLayout}
                            />
                        </View>
                        {/*服务类型*/}
                        <View style={styles.refund}>
                            <View style={styles.header}>
                                <F16Text>服务类型</F16Text>
                            </View>
                            <View style={styles.footer}>
                                <TextLabel
                                    selected={ refuse_type === 'RETURN_MONEY' }
                                    style={styles.footer_btn}
                                    text="退款"
                                    onPress={() => {
                                        this._switchFunction('RETURN_MONEY')
                                    }}
                                />

                                { !isCancel && <TextLabel
                                    selected={ refuse_type === 'RETURN_GOODS' }
                                    style={styles.footer_btn}
                                    text="退货"
                                    onPress={() => {
                                        this._switchFunction('RETURN_GOODS')
                                    }}
                                /> }
                            </View>
                        </View>
                        {/*申请原因*/}
                        <View style={styles.refund}>
                            <TouchableOpacity
                                style={styles.select_cell}
                                onPress={() => {
                                    this.setState({
                                        defaultDialog: true,
                                    });
                                }}>
                                <Text style={styles.add_input_label}>申请原因</Text>
                                <View style={{width: Screen.width - 20 - 22 - 80}}><Text
                                    style={{fontSize: 16}}>{ !refund_reason ? '请选择申请原因' : refund_reason}</Text></View>
                                <Icon name="ios-arrow-forward" color="#A8A9AB" size={20}/>
                            </TouchableOpacity>
                        </View>
                        {/*退款方式*/}
                        <View style={styles.refund}>
                            <TouchableOpacity
                                style={styles.select_cell}
                                onPress={() => {
                                    if(!original_way){
                                        this.setState({
                                            defaultRefundDialog: true,
                                        })
                                    }
                                    
                                }}>
                                <Text style={styles.add_input_label}>退款方式</Text>
                                <View style={{width: Screen.width - 20 - 22 - 80}}>
                                    { original_way && <Text>原路退回</Text> || <Text
                                        style={{fontSize: 15}}>{ !account_type ? '请选择退款方式' : this._accountType(account_type)}</Text> }
                                </View>
                                { !original_way && <Icon name="ios-arrow-forward" color="#A8A9AB" size={20}/>}
                            </TouchableOpacity>
                            { !!account_type && account_type !== 'BANKTRANSFER' && !original_way &&
                                <View style={styles.header}>
                                    <View style={styles.select_cell}>
                                        <Text style={styles.add_input_label}>退款账户</Text>
                                        <TextInput
                                            style={{width: Screen.width - 20 - 22 - 80, fontSize: 15}}
                                            maxLength={50}
                                            placeholder={'请输入退款账户'}
                                            onChangeText={(text) => { this.setState({ return_account: text }) }}
                                            placeholderColor="#777777"
                                        />
                                    </View>
                                </View> }
                            { account_type === 'BANKTRANSFER' &&
                                <View>
                                     <View style={styles.header}>
                                        <Text style={styles.add_input_label}>银行名称</Text>
                                        <TextInput
                                            style={{width: Screen.width - 20 - 22 - 80, fontSize: 15}}
                                            maxLength={50}
                                            placeholder={'请输入银行名称'}
                                            onChangeText={(text) => { this.setState({ bank_name: text }) }}
                                            placeholderColor="#777777"
                                        />
                                    </View>
                                    <View style={styles.header}>
                                        <Text style={styles.add_input_label}>开 户 行</Text>
                                        <TextInput
                                            style={{width: Screen.width - 20 - 22 - 80, fontSize: 15}}
                                            maxLength={50}
                                            placeholder={'请输入开户行'}
                                            onChangeText={(text) => { this.setState({ bank_deposit_name: text }) }}
                                            placeholderColor="#777777"
                                        />
                                    </View>
                                    <View style={styles.header}>
                                        <Text style={styles.add_input_label}>银行户名</Text>
                                        <TextInput
                                            style={{width: Screen.width - 20 - 22 - 80, fontSize: 15}}
                                            maxLength={50}
                                            placeholder={'请输入银行户名'}
                                            onChangeText={(text) => { this.setState({ bank_account_name: text }) }}
                                            placeholderColor="#777777"
                                        />
                                    </View>
                                    <View style={styles.header}>
                                        <Text style={styles.add_input_label}>银行账号</Text>
                                        <TextInput
                                            style={{width: Screen.width - 20 - 22 - 80, fontSize: 15}}
                                            maxLength={50}
                                            placeholder={'请输入银行账号'}
                                            onChangeText={(text) => { this.setState({ bank_account_number: text }) }}
                                            placeholderColor="#777777"
                                        />
                                    </View>
                                </View> }
                        </View>
                        {/*申请数量*/}
                        { refuse_type === 'RETURN_GOODS' && <View style={styles.refund}>
                            <View style={styles.header}>
                                <F16Text>申请数量</F16Text>
                            </View>
                            <View style={styles.footer}>

                                <TextLabel style={styles.footer_text} text="-"
                                        onPress={() => {
                                            this._updateNum('-')
                                        }}
                                />
                                <TextLabel style={styles.footer_text} text={return_num}/>
                                <TextLabel style={styles.footer_text} text="+"
                                        onPress={() => {
                                            this._updateNum('+')
                                        }}
                                />
                            </View>
                        </View> }
                        {/*退款金额*/}
                        <View style={styles.refund}>
                            <View style={styles.header}>
                                <F16Text>退款金额</F16Text>
                                { isCancel && <Text style={{width: Screen.width - 20 - 22 - 80, fontSize: 16}}>{order.order_price}</Text>
                                 || <Text style={{width: Screen.width - 20 - 22 - 80, fontSize: 16}}>{return_money}</Text> }
                            </View>
                        </View>

                        {/*问题描述*/}
                        <View style={styles.refund}>
                            <View style={styles.header}>
                                <F16Text>问题描述</F16Text>
                            </View>
                            <View style={styles.input_view}>
                                <TextInput
                                    multiline={true}
                                    maxLength={500}
                                    onChangeText={(text) => this._setDetail(text)}
                                    placeholder={'请您在此处描述问题！'}
                                    placeholderColor="#777777"
                                />
                            </View>
                        </View>

                        {/*售后原因*/}
                        <Dialog
                            onDismiss={() => {
                                this.setState({defaultDialog: false});
                            }}
                            width={0.9}
                            visible={this.state.defaultDialog}
                            rounded
                            actionsBordered
                            dialogTitle={
                                <DialogTitle
                                    title="选择售后原因"
                                    style={{
                                        backgroundColor: '#F7F7F8',
                                    }}
                                    hasTitleBar={false}
                                    align="left"
                                />
                            }
                            footer={
                                <DialogFooter>
                                    <DialogButton
                                        text="取消"
                                        bordered
                                        onPress={() => {
                                            this.setState({defaultDialog: false});
                                        }}
                                        key="button-1"
                                    />
                                    <DialogButton
                                        text="确定"
                                        bordered
                                        onPress={() => {
                                            this.setState({defaultDialog: false});
                                        }}
                                        key="button-2"
                                    />
                                </DialogFooter>
                            }
                        >
                            <DialogContent style={{backgroundColor: '#F7F7F8',}}>
                                <Picker
                                    selectedValue={this.state.refund_reason}
                                    style={{fontSize: 18}}
                                    itemStyle={{fontSize: 15}}
                                    onValueChange={(itemValue) => this.setState({refund_reason: itemValue})}>
                                    <Picker.Item label="请选择申请原因" value=""/>
                                    <Picker.Item label="收到商品与描述不符" value="收到商品与描述不符"/>
                                    <Picker.Item label="不喜欢/不想要" value="不喜欢/不想要"/>
                                    <Picker.Item label="发票问题" value="发票问题"/>
                                    <Picker.Item label="空包裹" value="空包裹"/>
                                    <Picker.Item label="快递无记录" value="快递无记录"/>
                                    <Picker.Item label="快递一直没有收到" value="快递一直没有收到"/>
                                    <Picker.Item label="买错/不想要" value="买错/不想要"/>
                                    <Picker.Item label="未按照时间发货" value="未按照时间发货"/>
                                    <Picker.Item label="其他" value="其他"/>
                                </Picker>
                            </DialogContent>
                        </Dialog>

                        {/*退款方式*/}
                        <Dialog
                            onDismiss={() => {
                                this.setState({defaultRefundDialog: false});
                            }}
                            width={0.9}
                            visible={this.state.defaultRefundDialog}
                            rounded
                            actionsBordered
                            dialogTitle={
                                <DialogTitle
                                    title="选择退款方式"
                                    style={{
                                        backgroundColor: '#F7F7F8',
                                    }}
                                    hasTitleBar={false}
                                    align="left"
                                />
                            }
                            footer={
                                <DialogFooter>
                                    <DialogButton
                                        text="取消"
                                        bordered
                                        onPress={() => {
                                            this.setState({defaultRefundDialog: false});
                                        }}
                                        key="button-1"
                                    />
                                    <DialogButton
                                        text="确定"
                                        bordered
                                        onPress={() => {
                                            this.setState({defaultRefundDialog: false});
                                        }}
                                        key="button-2"
                                    />
                                </DialogFooter>
                            }
                        >
                            <DialogContent style={{backgroundColor: '#F7F7F8',}}>
                                <Picker
                                    selectedValue={this.state.account_type}
                                    style={{fontSize: 18}}
                                    itemStyle={{fontSize: 15}}
                                    onValueChange={(itemValue) => this.setState({account_type: itemValue})}>
                                        <Picker.Item label="请选择退款方式" value=""/>
                                        <Picker.Item label="支付宝" value="ALIPAY"/>
                                        <Picker.Item label="微信" value="WEIXINPAY"/>
                                        <Picker.Item label="银行转账" value="BANKTRANSFER"/>
                                </Picker>
                            </DialogContent>
                        </Dialog>

                    </KeyboardAwareScrollView>
                </View>

                <BigButton title="提交申请" onPress={this._onSubmit} style={styles.big_btn}/>

                <Loading show={loading}/>
            </View>
        )
    }

}


const GoodsItem = ({data}) => {
    return (
        <TouchableOpacity
            style={styles.goods_item}
            activeOpacity={1}
            onPress={() => navigate('Goods', {id: data.goods_id})}
        >
            <Image style={styles.goods_item_image} source={{uri: data['goods_image']}}/>
            <View style={styles.goods_item_info}>
                <F16Text numberOfLines={1}>{data['name']}</F16Text>
                <F14Text numberOfLines={2}>
                    <F14Text style={styles.goods_item_num}>数量：{data['num']}</F14Text>
                </F14Text>
                <View style={{flexDirection: 'row', alignItems: 'center'}}>
                    <F16Text>￥{Foundation.formatPrice(data['purchase_price'])}</F16Text>
                </View>
            </View>
        </TouchableOpacity>
    )
}

const styles = StyleSheet.create({
    big_btn: {
        marginBottom: isIphoneX() ? 30 : 0,
        width: Screen.width
    },
    input_view: {
        justifyContent: 'center',
        width: Screen.width,
        backgroundColor: '#F8F8F8'
    },
    //选择行 字符样式
    add_input_label: {
        textAlign: 'left',
        color: colors.text,
        fontSize: 16
    },
    //选择售后类型样式
    select_cell: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        width: Screen.width,
        height: 50,
        paddingHorizontal: 10,
        borderColor: colors.cell_line_backgroud,
        borderBottomWidth: Screen.onePixel
    },

    header: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        width: Screen.width,
        height: 40,
        paddingHorizontal: 15
    },
    footer: {
        flexDirection: 'row',
        alignItems: 'center',
        width: Screen.width,
        height: 40,
        paddingHorizontal: 15
    },
    footer_btn: {
        width: 80,
        paddingVertical: 2,
        marginRight: 0,
        marginLeft: 10,
        marginBottom: 0
    },
    footer_btn_check: {
        width: 80,
        paddingVertical: 2,
        marginRight: 0,
        marginLeft: 10,
        marginBottom: 0,
        borderColor: colors.main
    },
    footer_text: {
        width: 30,
        paddingVertical: 2,
        marginRight: 0,
        marginLeft: 1,
        marginBottom: 0
    },
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
})

export default connect()(ApplyAfterSaleScene)