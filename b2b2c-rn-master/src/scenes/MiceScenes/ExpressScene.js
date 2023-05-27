/**
 * Created by Andste on 2018/10/21.
 */
import React, { Component } from 'react'
import {
  View,
  ScrollView,
  Image,
  StyleSheet
} from 'react-native'
import {Screen} from '../../utils'
import {F16Text} from '../../widgets/Text'
import * as API_Trade from '../../apis/trade'

export default class ExpressScene extends Component {
  static navigationOptions = {
    title: '物流详情'
  }
  
  constructor(props) {
    super(props)
    const {params} = this.props.navigation.state
    this.state = {
      express: '',
      logi_id: params.order.logi_id || '',
      ship_no: params.order.ship_no || ''
    }
  }

  componentDidMount() {
    const { logi_id,ship_no } = this.state
    API_Trade.getExpress(logi_id, ship_no).then(response => {
      this.setState({
        express: response
      })
    })
  }
  
  render() {
    const { express } = this.state
    return (
      <View style={ styles.container }>
        <View style={ styles.body }>
          <ScrollView>
            <View style={ styles.logistics }>
              <View>
                <F16Text>物流公司：{express['name'] || '获取中...'}</F16Text>
                <F16Text>物流单号：{express['courier_num'] || '获取中...'}</F16Text>
              </View>
            </View>
            <View style={ styles.logistics_info }>
              {!express && <F16Text style={ styles.logistics_info_u }>物流信息获取中...</F16Text> || !!express &&  express.data.map((item,index) => (
                <View style={ styles.ship_item } key={index}>
                  <View style={ styles.ship_msg }>
                    <F16Text style={ styles.ship_msg_text } >{ item.context }</F16Text>
                    <F16Text style={ styles.ship_msg_time } >{ item.time }</F16Text>
                  </View>
                </View>
              )) }
            </View>
          </ScrollView>
        </View>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  body: {
    flex: 1
  },
  logistics: {
    width: Screen.width,
    height: 70,
    justifyContent: 'center',
    backgroundColor: '#FFFFFF',
    paddingHorizontal: 10
  },
  logistics_info_u: {
    width: Screen.width,
    textAlign: 'center',
    lineHeight: 100 
  },
  logistics_info: {
    width: Screen.width,
    minHeight: 65,
    marginTop: 10,
    paddingHorizontal: 10,
    paddingVertical: 10,
    backgroundColor: '#FFFFFF'
  },
  ship_item: {
    position: 'relative',
    display: 'flex',
    flexWrap: 'wrap'
  },
  ship_dot: {
    position: 'relative',
    lineHeight: 20,
    fontSize: 10,
    textAlign: 'center',
    color: '#666',
    marginTop: 24,
    marginRight: 15,
    marginBottom: 0,
    marginLeft: 5,
    width: 10,
    height: 10,
    borderRadius: 5,
    backgroundColor: '#ccc'
  },
  ship_msg: {
    flex: 1,
    paddingTop: 15
  },
  ship_msg_text: {
    color: '#333',
    fontSize: 14,
    lineHeight: 18
  },
  ship_msg_time: {
    fontSize: 12,
    color: '#999'
  }
})
