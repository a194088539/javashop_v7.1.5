/**
 * Created by Andste on 2018/9/29.
 */
import React, { Component } from 'react'
import {
  View,
  Animated,
  RefreshControl,
  ScrollView,
  StyleSheet
} from 'react-native'
import { connect } from 'react-redux'
import { userActions } from '../../redux/actions'
import Icon from 'react-native-vector-icons/Ionicons'

import Header from './Header'
import FaceBg from './FaceBg'
import Menus from './Menus'

class Mine extends Component {
  static navigationOptions = {
    tabBarLabel: '我的',
    tabBarIcon: ({ focused, tintColor }) => (
      <Icon name={ focused ? 'ios-person' : 'ios-person-outline' } size={ 30 } color={ tintColor }/>
    )
  }
  
  constructor(props) {
    super(props)
    this.nav = this.props.navigation
    this.headerOpacity = new Animated.Value(0)
    this.headerOpacity.addListener(({ value }) => this._countScrollValue(value))
    this.state = {
      loading: false,
      headerStyle: 'light'
    }
  }
  
  componentDidMount() {
    if (this.props.user) this._onRefresh()
  }
  
  _countScrollValue = value => {
    let _style = value > 75 ? 'dark' : 'light'
    this.setState({ headerStyle: _style })
  }
  
  _onRefresh = async () => {
    await this.setState({ loading: true })
    await this.props.dispatch(userActions.getUserAction())
    this.setState({ loading: false })
  }
  
  render() {
    //  头部背景透明
    let interpolatedHeaderOpacity = this.headerOpacity.interpolate({
      inputRange: [0, 100],
      outputRange: [0, .98],
      extrapolate: 'clamp'
    })
    //  头部头像和标题透明
    let interpolatedShowInfoOpacity = this.headerOpacity.interpolate({
      inputRange: [90, 100],
      outputRange: [0, 1],
      extrapolate: 'clamp'
    })
    let event = Animated.event([{ nativeEvent: { contentOffset: { y: this.headerOpacity } } }])
    const { headerStyle, loading } = this.state
    let { user } = this.props
    return (
      <View style={ styles.container }>
        <Header
          face={ user ? user.face : null }
          headerStyle={ headerStyle }
          opacity={ interpolatedHeaderOpacity }
          showInfoOpacity={ interpolatedShowInfoOpacity }
        />
        <ScrollView
          scrollEventThrottle={ 16 }
          onScroll={ event }
          showsHorizontalScrollIndicator={ false }
          showsVerticalScrollIndicator={ false }
          refreshControl={
            <RefreshControl
              refreshing={ loading }
              onRefresh={ this._onRefresh }
              tintColor="#ff0000"
              title="下拉刷新..."
              colors={ ['#ff0000', '#00ff00', '#0000ff'] }
            />
          }
        >
          <FaceBg user={ user }/>
          <Menus user={ user }/>
        </ScrollView>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#F4F4F4'
  }
})

export default connect(state => ({ ...state.user }))(Mine)
