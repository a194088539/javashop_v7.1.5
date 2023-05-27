/**
 * Created by Andste on 2018/10/13.
 */
import React, { PureComponent } from 'react'
import {
  View
} from 'react-native'
import * as tpls from './FloorTpls'

export default class Floor extends PureComponent {
  render() {
    const { data } = this.props
    if (!data) return <View/>
    return (
      <View>
        { data.map((item, index) => {
          const tpl_func = tpls[`tpl_${ item['tpl_id'] }`]
          if (!tpl_func) return undefined
          return tpl_func(index, item['blockList'])
        }) }
      </View>
    )
  }
}
