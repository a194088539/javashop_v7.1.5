/**
 * Created by Andste 2018/9/29.
 */
import React, { PureComponent } from 'react'
import {
  Keyboard,
  StyleSheet,
  Image,
  TouchableOpacity
} from 'react-native'
import { colors } from '../../config'
import { withNavigation } from 'react-navigation'

class HeaderBack extends PureComponent {
  _onPress = () => {
    Keyboard.dismiss()
    this.props.navigation.goBack()
  }
  
  render() {
    const { style, tintColor, ...props } = this.props
    return (
      <TouchableOpacity
        style={ styles.headerLeft }
        onPress={ this._onPress }
        { ...props }
      >
        <Image
          source={ require('../images/icon-arrow-left.png') }
          style={ [
            { width: 20, height: 20 },
            { tintColor: tintColor || colors.navigator_tint_color },
            style
          ] }
        />
      </TouchableOpacity>
    )
  }
}

export default withNavigation(HeaderBack)

const styles = StyleSheet.create({
  headerLeft: {
    width: 44,
    height: 44,
    justifyContent: 'center',
    alignItems: 'center',
    overflow: 'hidden'
  }
})
