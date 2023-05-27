/**
 * Created by Andste on 2017/7/22.
 */
import React, { PureComponent } from 'react'
import {
  Text,
  View,
  Image,
  StyleSheet,
  TouchableOpacity
} from 'react-native'
import { Screen } from '../../utils'

export default class ContentListItem extends PureComponent {
  render() {
    const { image, title, ...props } = this.props
    return (
      <TouchableOpacity
        style={ styles.container }
        { ...props }
      >
        <Image
          style={ styles.item_image }
          source={ { uri: image || 'https://m.360buyimg.com/mobile/s100x100_jfs/t2107/288/1281038719/5743/e888a4ea/568e5f32N2c20803c.jpg' } }
        />
        <View style={ styles.item_title }>
          <Text style={ styles.item_title_text }>{ title }</Text>
        </View>
      </TouchableOpacity>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    width: (Screen.width - 85 - 21) / 3,
    height: 100,
    backgroundColor: '#FFFFFF',
    justifyContent: 'center'
  },
  item_image: {
    width: 50,
    height: 55,
    marginTop: 10,
    alignSelf: 'center'
  },
  item_title: {
    justifyContent: 'center',
    alignItems: 'center',
    height: 40
  },
  item_title_text: {
    fontSize: 14,
    color: '#333333'
  }
})
