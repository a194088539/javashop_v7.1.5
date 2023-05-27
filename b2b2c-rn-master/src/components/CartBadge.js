/**
 * Created by Andste on 2018/11/21.
 */
import React, { PureComponent } from 'react'
import {
  View,
  StyleSheet
} from 'react-native'
import { connect } from 'react-redux'
import { Badge } from '../widgets'

class CartBadge extends PureComponent {
  render() {
    let {selectedNum} = this.props
    if (selectedNum === 0) return <View/>
    selectedNum = Number(selectedNum) > 99 ? '99+' : selectedNum
    return (
      <Badge
        style={ styles.badge }
        textStyle={ {fontSize: 10} }
        extraPaddingHorizontal={ 0 }
      >{ selectedNum }</Badge>
    )
  }
}

const styles = StyleSheet.create({
  badge: {
    position: 'absolute',
    zIndex: 99,
    top: 0,
    right: -15
  }
})

export default connect(state => ({
  selectedNum: state.cart.selectedNum
}))(CartBadge)
