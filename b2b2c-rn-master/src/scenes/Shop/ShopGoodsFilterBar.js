/**
 * Created by Andste on 2019-01-16.
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
import Icon from 'react-native-vector-icons/Ionicons'

export default class ShopGoodsFilterBar extends PureComponent {
  
  constructor(props) {
    super(props)
    this.state = {
      items: [
        { title: '默认', active: true, sort: ['def', 'asc'] },
        { title: '销量', active: false, sort: ['buynum', 'asc'] },
        { title: '价格', active: false, sort: ['price', 'asc'] },
        { title: '好评率', active: false, sort: ['grade', 'asc'] }
      ]
    }
  }
  
  _onPress = (item, index) => {
    let { items } = this.state
    items = items.map((_item, _index) => {
      if (_item.active && _index === _index) {
        _item.sort[1] = _item.sort[1] === 'asc' ? 'desc' : 'asc'
      } else {
        _item.sort[1] = 'asc'
      }
      _item.active = _index === index
      return _item
    })
    this.setState({ items })
    const sort = items[index]['sort'].join('_')
    this.props['sortChange'](sort)
  }
  
  render() {
    const { items } = this.state
    return (
      <View style={ styles.container }>
        { items.map((item, index) => (
          <TouchableOpacity
            key={ item.title }
            style={ styles.item }
            onPress={ () => this._onPress(item, index) }
          >
            <Text
              style={ [
                styles.item_text,
                item.active && styles.item_text_active
              ] }
            >{ item.title }</Text>
            <View style={ styles.arrow_view }>
              <Icon
                style={ [styles.arrow, { marginTop: 2 } ] }
                name="md-arrow-dropup"
                color={ (item.active && item.sort[1] === 'asc') ? colors.main : '#848689' }
              />
              <Icon
                style={ [styles.arrow, { marginTop: -5 } ] }
                name="md-arrow-dropdown"
                color={ (item.active && item.sort[1] === 'desc') ? colors.main : '#848689' }
              />
            </View>
          </TouchableOpacity>
        )) }
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    height: 44,
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#FFF',
    borderBottomWidth: Screen.onePixel,
    borderBottomColor: colors.cell_line_backgroud
  },
  item: {
    flexDirection: 'row',
    justifyContent: 'center',
    width: Screen.width / 4,
    alignItems: 'center'
  },
  item_text: {
    color: '#848689'
  },
  item_text_active: {
    color: colors.main
  },
  arrow_view: {
    justifyContent: 'center',
    marginLeft: 5
  }
})
