/**
 * Created by Andste on 2018/9/30.
 */
import React, { Component } from 'react'
import {
  View,
  Image,
  TouchableOpacity,
  StatusBar,
  Platform,
  BackHandler,
  StyleSheet
} from 'react-native'
import { StackActions } from 'react-navigation'
import { connect } from 'react-redux'
import { searchActions } from '../../redux/actions'
import { colors } from '../../../config'

import SearchBar from '../../components/SearchBar'
import FilterBar from './FilterBar'
import FlatList from './FlatList'

class GoodsList extends Component {
  static navigationOptions = ({ navigation }) => {
    const { params = {} } = navigation.state
    const viewType = params.view_type === 'double'
      ? require('../../images/icon-viewtype_double.png')
      : require('../../images/icon-viewtype_single.png')
    const toSearch = () => navigation.dispatch(StackActions.push({ routeName: 'Search' }))
    const viewTypeChanged = () => navigation.setParams({ view_type: params.view_type === 'double' ? 'single' : 'double' })
    return {
      headerTitle: (<SearchBar onPress={ toSearch }/>),
      headerRight: (
        <TouchableOpacity onPress={ viewTypeChanged } style={ styles.header_right }>
          <Image source={ viewType } style={ styles.view_type_icon }/>
        </TouchableOpacity>
      )
    }
  }
  
  constructor(props) {
    super(props)
    this.params = this.props.navigation.state.params || {}
    const { cat_id, keyword } = this.params
    if (cat_id) {
      this.props.dispatch(searchActions.searchCatIdChanged(cat_id))
    }
    if (keyword) {
      this.props.dispatch(searchActions.searchKeywordChaned(keyword))
    }
  }

  componentDidMount() {
    if (Platform.OS === 'android') {
      BackHandler.addEventListener('hardwareBackPress', () => { return this.props.navigation.goBack() })
    }
  }

  componentWillUnmount() {
    if (Platform.OS === 'android') {
      BackHandler.removeEventListener('hardwareBackPress', this.onBackAndroid);
    }
  }
  
  shouldComponentUpdate(nextProps) {
    let { view_type } = nextProps.navigation.state.params
    this.props.dispatch(searchActions.searchViewTypeChanged(view_type))
    return false
  }
  
  render() {
    return (
      <View style={ styles.container }>
        <StatusBar barStyle="dark-content"/>
        <FilterBar/>
        <FlatList/>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: colors.border_line
  },
  header_right: {
    width: 34,
    height: 44,
    justifyContent: 'center',
    alignItems: 'center'
  },
  view_type_icon: {
    width: 20,
    height: 20
  }
})

export default connect()(GoodsList)
