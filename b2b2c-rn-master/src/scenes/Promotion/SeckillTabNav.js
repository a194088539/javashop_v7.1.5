/**
 * Created by Andste on 2019-01-11.
 */
import React, { PureComponent } from 'react'
import {
  View,
  Text,
  TouchableOpacity,
  StyleSheet
} from 'react-native'
import { colors } from '../../../config'
import { Screen } from '../../utils'

export default class SeckillTabNav extends PureComponent {
  static getDerivedStateFromProps(nextProps, prevState) {
    if (nextProps.curIndex !== prevState.curIndex) {
      const { curIndex, timeLine} = prevState
      timeLine[curIndex]['active'] = false
      timeLine[nextProps.curIndex]['active'] = true
      return {
        curIndex: nextProps.curIndex,
        timeLine
      }
    }
    return null
  }
  
  constructor(props) {
    super(props)
    this.state = {
      timeLine: props.data.map((item, index) => {
        item.active = index === 0
        return item
      }),
      curIndex: 0
    }
  }
  
  /**
   * 切换Nav
   * @param index
   * @private
   */
  _onPressNav = (index) => {
    const { curIndex, timeLine } = this.state
    timeLine[curIndex]['active'] = false
    timeLine[index]['active'] = true
    this.setState({ curIndex: index, timeLine })
    this.props['onNavChange'](index)
  }
  
  render() {
    const { timeLine } = this.state
    return (
      <View style={ styles.container }>
        {timeLine.map((item, index) => (
          <TouchableOpacity
            style={ styles.nav_item }
            onPress={ () => this._onPressNav(index) }
            key={ index }
          >
            <Text
              style={ [
                styles.nav_time,
                item.active && styles.nav_time_active
              ] }>{ item['time_text'] }:00</Text>
            <Text
              style={ [
                styles.nav_title,
                item.active && styles.nav_title_active
              ] }>{ index === 0 && (item['distance_time'] === 0) ? '正在抢购' : '即将开始' }</Text>
          </TouchableOpacity>
        ))}
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    justifyContent: 'center',
    width: Screen.width,
    height: 48,
    backgroundColor: '#FFF',
    borderBottomWidth: 2,
    borderBottomColor: colors.main
  },
  nav_item: {
    width: Screen.width / 5,
    height: 48,
    alignItems: 'center',
    justifyContent: 'center'
  },
  nav_time: {
    color: colors.text,
    fontSize: 16
  },
  nav_time_active: {
    color: colors.main,
    fontSize: 18,
    fontWeight: 'bold'
  },
  nav_title: {
    color: colors.text
  },
  nav_title_active: {
    color: colors.main
  }
})
