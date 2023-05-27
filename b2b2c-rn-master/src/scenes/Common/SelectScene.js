/**
 * Created by Andste on 2017/8/11.
 */

import React, { Component } from 'react'
import {
  StatusBar,
  StyleSheet,
  ScrollView
} from 'react-native'
import { colors } from '../../../config'
import { Cell, CellGroup } from '../../widgets'
import Icon from 'react-native-vector-icons/Ionicons'

export default class SelectScene extends Component {
  static navigationOptions = ({ navigation }) => {
    let { title } = navigation.state.params
    return {
      title: title || '请选择'
    }
  }
  
  constructor(props) {
    super(props)
    this.navParams = this.props.navigation.state.params
  }
  
  shouldComponentUpdate(nextProps, nextState) {
    return nextState !== this.state
  };
  
  _onPress = item => {
    let { type, selected } = this.navParams
    let isRadio = type === 'radio'
    if (isRadio) {
      selected(item) !== false && this.props.navigation.goBack()
    }
  }
  
  render() {
    let { items } = this.navParams
    return (
      <ScrollView style={ styles.container }>
        <StatusBar barStyle="dark-content"/>
        <CellGroup>
          { items.map((item, index) => {
            return (
              <Cell
                key={ index } title={ item.text }
                arrow={ item.selected ? <Icon style={ styles.arrow } name="ios-checkmark" size={ 40 }/> : undefined }
                disabled={ item.disabled } onPress={ () => {this._onPress(item)} }/>
            )
          }) }
        </CellGroup>
      </ScrollView>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: colors.gray_background
  },
  arrow: {
    width: 40,
    height: 40,
    color: colors.main
  }
})
