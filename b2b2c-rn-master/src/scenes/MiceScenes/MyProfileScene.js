/**
 * Created by Andste on 2018/10/25.
 */
import React, { Component } from 'react'
import {
  View,
  StatusBar,
  ScrollView,
  StyleSheet
} from 'react-native'
import { connect } from 'react-redux'
import { messageActions, userActions } from '../../redux/actions'
import { Cell, CellGroup, Face } from '../../widgets'
import { Foundation, request } from '../../utils'
import * as API_Common from '../../apis/common'
import * as API_Members from '../../apis/members'
import DatePicker from 'react-native-datepicker'
import ImagePicker from 'react-native-image-picker'

class MyProfileScene extends Component {
  static navigationOptions = {
    title: '个人信息'
  }
  
  constructor(props, context) {
    super(props, context)
    this.state = {
    }
  }
  
  // 修改头像
  _editFace = async () => {
    const options = {
      title: null,
      cancelButtonTitle: '取消',
      takePhotoButtonTitle: '拍照',
      chooseFromLibraryButtonTitle: '相册选取',
      maxWidth: 200,
      maxHeight: 200,
      allowsEditing: true,
      storageOptions: {
        skipBackup: true,
        path: 'images'
      }
    }
    const { dispatch } = this.props
    ImagePicker.showImagePicker(options, async (response) => {
      let { uri, fileName, fileSize, error, type } = response
      if (response.didCancel) return
      if (error) {
        dispatch(messageActions.error(error))
        return
      }
      if (fileSize > 1024 * 1024 * 4) {
        dispatch(messageActions.error('您选的照片过大！'))
        return
      }
      const formData = new FormData()
      formData.append('file', { uri, type, name: fileName })
      try {
        const res = await request({
          url: API_Common.upload,
          data: formData,
          method: 'POST',
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        })
        this._saveEdit({ ...this.props.user, face: res.url })
      } catch (e) {
        dispatch(messageActions.error(e.message))
      }
    })
  }
  
  // 修改用户名
  _editUsername = () => {
    this.props.dispatch(messageActions.error('用户名不支持修改！'))
  }
  
  // 修改昵称
  _editNickname = () => {
    const { user } = this.props
    this.props.navigation.navigate('Input', {
      title: '修改昵称',
      prompt: '3-20个字符，不可输入特殊字符',
      validate: function (text) {
        if (!text) return false
        return text.length > 1
      },
      defaultValue: user.nickname,
      inputProps: {
        maxLength: 20,
        placeholder: '请输入昵称'
      },
      confirm: text => {
        if (user.name === text) {
          return
        }
        this._saveEdit({ ...user, nickname: text })
      }
    })
  }
  
  // 选择性别
  _selectSex = () => {
    const { user } = this.props
    this.props.navigation.navigate('Select', {
      title: '修改性别',
      type: 'radio',
      items: [
        { text: '男', value: 1, selected: user.sex === 1 },
        { text: '女', value: 0, selected: user.sex === 0 }
        /*{text: '保密', value: -1, selected: user.sex === -1, disabled: true}*/
      ],
      selected: item => {
        if (item.value === user.sex) return
        this._saveEdit({ ...user, sex: item.value })
      }
    })
  }
  
  _onDateChange = (date) => {
    const { user } = this.props
    let _birthday = date.replace(/\D/g, '-').substr(0, date.length - 1)
    let _unix = Foundation.dateToUnix(_birthday)
    let _today = parseInt((new Date(new Date().toLocaleDateString()).getTime() + 24 * 60 * 60 * 1000 - 1) / 1000)
    if (_unix > _today) {
      this.props.dispatch(messageActions.warning('所选日期不能大于今日'))
      return
    }
    if (user['birthday'] === _unix) return
    this._saveEdit({ ...user, birthday: _unix })
  }
  
  _saveEdit = async (params) => {
    const { dispatch } = this.props
    try {
      await API_Members.saveUserInfo(params)
      await dispatch(userActions.getUserAction())
      dispatch(messageActions.success('修改成功！'))
    } catch (e) {
      dispatch(messageActions.error(e.message))
    }
  }
  
  render() {
    let { user } = this.props
    if (!user) user = {}
    const sex = user.sex === 1 ? '男' : '女'
    const birthday = Foundation.unixToDate(user.birthday, 'yyyy年MM月dd日')
    return (
      <View style={ styles.container }>
        <StatusBar barStyle="dark-content"/>
        <ScrollView
          showsHorizontalScrollIndicator={ false }
          showsVerticalScrollIndicator={ false }
        >
          <CellGroup marginBottom={ false }>
            <Cell
              title="头像"
              label={ <Face uri={ user.face } style={ styles.face }/> }
              onPress={ this._editFace }
            />
            <Cell title="用户名" label={ user.uname } onPress={ this._editUsername } arrow={ false }/>
            <Cell title="昵称" label={ user.nickname } onPress={ this._editNickname }/>
            <Cell title="性别" label={ sex } onPress={ this._selectSex }/>
            <Cell title="生日" label={ birthday } onPress={ () => this._datepicker.onPressDate() }/>
          </CellGroup>
          <DatePicker
            style={ styles.datepicker }
            ref={ datepicker => this._datepicker = datepicker }
            date={ birthday }
            showIcon={ false }
            hideText={ true }
            format="YYYY年MM月DD日"
            confirmBtnText="确定"
            cancelBtnText="取消"
            customStyles={ {
              dateTouchBody: {
                borderWidth: 0
              },
              dateText: {
                color: '#BCBDBF',
                fontSize: 16,
                borderWidth: 0
              }
            } }
            onDateChange={ this._onDateChange }
          />
        </ScrollView>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1
  },
  face: {
    width: 50,
    height: 50,
    borderRadius: 25
  },
  datepicker: {
    width: 0,
    height: 0
  }
})

export default connect(state => ({ user: state.user.user }))(MyProfileScene)
