/**
 * Created by Andste on 2019-01-09.
 */
import React, { PureComponent } from 'react'
import {
  View,
  Text,
  StyleSheet
} from 'react-native'
import { colors } from '../../../config'
import { Foundation } from '../../utils'

export default class CountDown extends PureComponent {
  constructor(props) {
    super(props)
    this.endTime = this.props.endTime
    this.state = {
      times: Foundation.countTimeDown(this.endTime)
    }
  }
  
  componentDidMount() {
    this._count()
  }
  
  _count = () => {
    this._setInterval = setInterval(() => {
      if (this.endTime <= 0) {
        clearInterval(this._setInterval)
      } else {
        this.endTime--
        this.setState({ times: Foundation.countTimeDown(this.endTime) })
      }
    }, 1000)
  }
  
  componentWillUnmount() {
    this._setInterval && clearInterval(this._setInterval)
  }
  
  render() {
    const { times } = this.state
    return (
      <View style={ styles.container }>
        { times.day !== '00' ?
          (<Text style={ styles.day }>{ times.day }天</Text>)
          : undefined }
        <Text style={ styles.time }>{ times.hours }</Text>
        <Text> : </Text>
        <Text style={ styles.time }>{ times.minutes }</Text>
        <Text> : </Text>
        <Text style={ styles.time }>{ times.seconds }</Text>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    alignItems: 'center',
    marginTop: 3
  },
  day: {
    color: colors.main
  },
  time: {
    padding: 2,
    backgroundColor: colors.main,
    color: '#FFF',
    fontSize: 12, 
    fontWeight: '500'
  }
})
