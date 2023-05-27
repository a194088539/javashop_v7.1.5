/**
 * Created by Andste on 2018/10/24.
 */
import React, { PureComponent } from 'react'
import {
  View,
  Text,
  TouchableOpacity,
  StyleSheet
} from 'react-native'
import { connect } from 'react-redux'
import { searchActions } from '../../redux/actions'
import Icon from 'react-native-vector-icons/MaterialIcons'
import { colors } from '../../../config'
import { Screen } from '../../utils'

class FilterBar extends PureComponent {
  constructor(props) {
    super(props)
    this.state = {
      params: {
        def: { selected: true, sort: 'desc' },
        buy_num: { selected: false, sort: 'desc' },
        price: { selected: false, sort: 'desc' }
      },
      filter: {
        selected: false
      },
      canPress: true
    }
  }
  
  _onPress = async (key) => {
    const { params, canPress } = this.state
    if (! canPress) return
    await this.setState({ canPress: false })
    if (key === 'buy_num' && params.buy_num.selected) {
      this.setState({ canPress: true })
      return
    }
    let _params = Object.assign({}, params)
    Object.keys(_params).forEach(item => {_params[item].selected = false})
    let _item = _params[key]
    _item.selected = true
    _item.sort = _item.sort === 'desc' ? 'asc' : 'desc'
    await this.setState({ params: _params })
    let _sort = key + '_' + this.state.params[key].sort
    this.props.dispatch(searchActions.searchSortChaned(_sort))
    setTimeout(() => {this.setState({ canPress: true })}, 500)
  }
  
  render() {
    let { def, buy_num, price } = this.state.params
    return (
      <View style={ styles.container }>
        <FilterText
          selected={ def.selected }
          title="默认"
          sort={ def.sort }
          onPress={ () => {this._onPress('def')} }
        />
        <FilterText
          selected={ buy_num.selected }
          title="销量"
          onPress={ () => {this._onPress('buy_num')} }
        />
        <FilterText
          selected={ price.selected }
          title="价格"
          sort={ price.sort }
          onPress={ () => {this._onPress('price')} }
        />
      </View>
    )
  }
}

class FilterText extends PureComponent {
  render() {
    const { selected, title, sort, onPress } = this.props
    const up_selected = selected ? sort === 'asc' : false
    const down_selected = selected ? sort === 'desc' : false
    return (
      <TouchableOpacity
        style={ styles.item }
        activeOpacity={ 1 }
        onPress={ onPress }
      >
        <Text style={ [styles.title, selected ? styles.selected : ''] }>{ title }</Text>
        { sort ? <View style={ styles.sort_drop }>
          <Icon
            style={ [styles.sort_drop_icon, { marginTop: 2 }, up_selected ? styles.selected : ''] }
            name="arrow-drop-up"
          />
          <Icon
            style={ [styles.sort_drop_icon, { marginTop: - 5 }, down_selected ? styles.selected : ''] }
            name="arrow-drop-down"
          />
        </View> : <View/> }
      </TouchableOpacity>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    width: Screen.width,
    height: 40,
    borderBottomWidth: 1,
    borderBottomColor: colors.border_line,
    flexDirection: 'row',
    justifyContent: 'space-between',
    backgroundColor: '#FFFFFF'
  },
  item: {
    width: Screen.width / 3,
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center'
  },
  title: {
    fontSize: 14,
    color: colors.text
  },
  selected: {
    color: colors.main
  },
  sort_drop: {
    flexDirection: 'column',
    justifyContent: 'center',
    height: 44
  },
  sort_drop_icon: {
    fontSize: 12
  }
})

export default connect()(FilterBar)
