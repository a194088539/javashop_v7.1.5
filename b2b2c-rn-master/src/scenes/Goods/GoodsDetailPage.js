/**
 * Created by Andste on 2018/11/21.
 */
import React, { PureComponent } from 'react'
import {
  View,
  Text,
  TouchableOpacity,
  StyleSheet
} from 'react-native'
import { WebView } from 'react-native-webview'
import { colors } from '../../../config'
import { Screen } from '../../utils'
import { isIphoneX } from 'react-native-iphone-x-helper'

export default class GoodsDetailPage extends PureComponent {
  
  constructor(props) {
    super(props)
    this.state = {
      cur_tab: 0
    }
  }
  
  /**
   * 拼接introHtml
   * @returns {string}
   * @private
   */
  _getIntroHtml = () => {
    const { intro } = this.props
    const styles = `<style type="text/css">* {margin: 0; padding: 0} * img {width: 98%; margin: 0 1%}</style>`
    return `<html><head>${ styles }</head><body>${ intro }</body></html>`
  }
  
  /**
   * 拼接paramsHtml
   * @returns {string}
   * @private
   */
  _getParamsHtml = () => {
    const { params } = this.props
    const styles = `<style>
                    .table-border {border-bottom: solid 1px #e7e7e7;border-left: solid 1px #e7e7e7;min-width: 100%;border-collapse: collapse;border-spacing: 0;word-wrap: break-word;word-break: break-all;font-size: 15px;}
                    .table-border td strong {font-weight: 700;color: #848689}
                    .table-border td, .table-border th {border-top: solid 1px #e7e7e7;border-right: solid 1px #e7e7e7;padding: 10px}
                    .table-border td:first-child {width: 65px}
                    .table-border td {color: #848689;font-size: 16px}
                  </style>`
    let trs = ''
    params.forEach(param => {
      let tds = ''
      param.params && param.params.forEach(value => {
        tds += `<tr><td>${ value['param_name'] }</td><td>${ value['param_value'] }</td></tr>`
      })
      trs += `<tr><td colspan="2"><strong>${ param['group_name'] }</strong></td></tr>${ tds }`
    })
    return `
     <html>
       <head>
         <meta name="content-type" content="text/html; charset=utf-8">
         <meta http-equlv="Content-Type" content="text/html;charset=utf-8">
         <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
         ${ styles }
       </head>
       <body>
         <div class="param-detail">
         <table class="table-border" width="100%">
           <tbody>${ trs }</tbody>
         </table>
         </div>
       </body>
     </html>`
  }
  
  render() {
    const { cur_tab } = this.state
    const html = cur_tab === 0
      ? this._getIntroHtml()
      : this._getParamsHtml()
    return (
      <View style={ styles.container }>
        <View style={ styles.header_space }/>
        <View style={ styles.nav_bar }>
          <TouchableOpacity style={ styles.nav_btn } onPress={ () => this.setState({ cur_tab: 0 }) }>
            <Text style={ [styles.nav_text, cur_tab === 0 && styles.nav_text_cur] }>商品介绍</Text>
          </TouchableOpacity>
          <TouchableOpacity style={ styles.nav_btn } onPress={ () => this.setState({ cur_tab: 1 }) }>
            <Text style={ [styles.nav_text, cur_tab === 1 && styles.nav_text_cur] }>规格参数</Text>
          </TouchableOpacity>
          <View style={ styles.space_line }/>
        </View>
        <WebView
          style={ styles.webview }
          originWhitelist={ ['*'] }
          source={ { html, baseUrl: '' } }
          scalesPageToFit={ true }
        />
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#FFFFFF'
  },
  header_space: {
    height: isIphoneX() ? 84 : 64,
    backgroundColor: '#FFFFFF'
  },
  webview: {
    flex: 1,
    width: Screen.width
  },
  nav_bar: {
    flexDirection: 'row',
    width: Screen.width,
    height: 40
  },
  nav_btn: {
    width: Screen.width / 2,
    justifyContent: 'center',
    alignItems: 'center'
  },
  nav_text: {
    fontSize: 16,
    color: colors.text
  },
  nav_text_cur: {
    color: colors.main
  },
  space_line: {
    position: 'absolute',
    top: (40 - 16) / 2,
    left: Screen.width / 2,
    width: Screen.onePixel,
    backgroundColor: colors.text,
    height: 16
  }
})
