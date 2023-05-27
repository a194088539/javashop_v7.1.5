/**
 * Created by Andste on 2018/10/21.
 */
import React, { Component } from 'react'
import {
  View,
  Alert,
  StatusBar,
  StyleSheet
} from 'react-native'
import { connect } from 'react-redux'
import { messageActions } from '../../redux/actions'
import { Cell, CellGroup } from '../../widgets'
import * as API_Connect from '../../apis/connect'

class MyConnectScene extends Component {
  static navigationOptions = {
    title: '关联账号'
  }
  
  constructor(props) {
    super(props)
    this.state = {
      connect: {}
    }
  }
  
  async componentDidMount() {
    await this._getConnect()
  }
  
  /**
   * 获取绑定列表
   * @returns {Promise<void>}
   * @private
   */
  _getConnect = async () => {
    const connect = {}
    const res = await API_Connect.getConnectList()
    res.forEach(item => {
      connect[item['union_type']] = item['is_bind']
    })
    this.setState({ connect })
  }
  
  /**
   * 去绑定或解绑
   * @param type
   * @param bind
   * @private
   */
  _onPressCell = (type, bind) => {
    if (bind) {
      Alert.alert('提示', '确定要解绑吗？', [
        { text: '取消' },
        {
          text: '确定',
          onPress: async () => {
            await API_Connect.unbindConnect(type)
            await this._getConnect()
            this.props.dispatch(messageActions.success('解绑成功！'))
          }
        }
      ])
    } else {
      // APP暂无绑定接口，需要可二开
    }
  }
  
  render() {
    const { QQ, WECHAT, WEIBO, ALIPAY } = this.state.connect
    return (
      <View style={ styles.container }>
        <StatusBar barStyle="dark-content"/>
        <CellGroup>
          <Cell
            title="腾讯QQ"
            label={ QQ ? '解绑' : '未绑定' }
            arrow={ QQ }
            onPress={ () => { this._onPressCell('QQ', QQ) } }
          />
          <Cell
            title="腾讯微信"
            label={ WECHAT ? '解绑' : '未绑定' }
            arrow={ WECHAT }
            onPress={ () => { this._onPressCell('WECHAT', WECHAT) } }
          />
          <Cell
            title="新浪微博"
            label={ WEIBO ? '解绑' : '未绑定' }
            onPress={ () => { this._onPressCell('WEIBO', WEIBO) } }
            arrow={ WEIBO }
          />
          <Cell
            title="支付宝"
            label={ ALIPAY ? '解绑' : '未绑定' }
            arrow={ ALIPAY }
            onPress={ () => { this._onPressCell('ALIPAY', ALIPAY) } }
          />
        </CellGroup>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1
  }
})

export default connect()(MyConnectScene)
