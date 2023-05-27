/**
 * Created by Andste on 2018/11/9.
 */
import React, { Component } from 'react'
import {
  View,
  Image,
  TextInput,
  TouchableOpacity,
  StatusBar,
  StyleSheet
} from 'react-native'
import { isIphoneX } from 'react-native-iphone-x-helper'
import { connect } from 'react-redux'
import { messageActions } from '../../redux/actions'
import { colors } from '../../../config'
import { Screen, request, Foundation } from '../../utils'
import { F16Text, F14Text } from '../../widgets/Text'
import { Loading, BigButton, TextItem } from '../../widgets'
import { KeyboardAwareScrollView } from 'react-native-keyboard-aware-scroll-view'
import Icon from 'react-native-vector-icons/Ionicons'
import StarRating from 'react-native-star-rating'
import ImagePicker from 'react-native-image-picker'
import * as API_Common from '../../apis/common'
import * as API_Members from '../../apis/members'

class CommentOrderScene extends Component {
  static navigationOptions = {
    title: '评价晒单'
  }
  
  constructor(props) {
    super(props)
    const { data, append_comment } = props.navigation.state.params
    this.sku_list = data.sku_list
    this.order_sn = data.sn
    this.state = {
      comments: data.sku_list.map(sku => ({
        content: '',
        grade: 'good',
        sku_id: sku.sku_id,
        images: []
      })),
      description_score: 5,
      service_score: 5,
      delivery_score: 5,
      // 是否为初评价 true 默认为初评
      first_comment: !append_comment,
      loading: false
    }
  }

  componentDidMount() {
    if(!this.state.first_comment){
      this._getAppendComment()
    }
  }

  _getAppendComment = async () => {
    const res = await API_Members.getFirstCommentsDetail({ order_sn: this.order_sn, sku_id: this.state.comments.sku_id })
    this.setState({
      comments: res.map((item, index) => {
        return {
          $$goods_comment: item,
          sku_id: item.sku_id,
          content: '',
          grade: 'good',
          images: []
        }
      })
    })
  }
  
  _onChangeText = (text, index) => {
    let { comments } = this.state
    comments = JSON.parse(JSON.stringify(comments))
    comments[index].content = text
    this.setState({ comments })
  }
  
  _onSelectImage = (index) => {
    let { comments } = this.state
    const { dispatch } = this.props
    comments = JSON.parse(JSON.stringify(comments))
    const { images } = comments[index]
    if (images.length > 5) {
      dispatch(messageActions.error('图片张数超出限制'))
      return
    }
    let options = {
      title: null,
      cancelButtonTitle: '取消',
      takePhotoButtonTitle: '拍照',
      chooseFromLibraryButtonTitle: '相册选取',
      quality: 0.7,
      allowsEditing: false,
      noData: true,
      storageOptions: { skipBackup: true, path: 'images' }
    }
    ImagePicker.showImagePicker(options, async (response) => {
      if (response.didCancel || response.error) return
      let { uri, fileName, fileSize, error, type } = response
      if (response.didCancel) return
      if (error) {
        dispatch(messageActions.error(error))
        return
      }
      if (fileSize > 1024 * 1024 * 5) {
        dispatch(messageActions.error('您选的照片过大'))
        return
      }
      if (uri) await this.setState({ loading: true })
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
        this.setState({ loading: false })
        images.push(res.url)
        this.setState({ comments })
      } catch(err) {
        this.setState({ loading: false })
        dispatch(messageActions.error('照片上传失败'))
      }
      
    })
  }
  
  _delImage = (index, imgIndex) => {
    let { comments } = this.state
    comments = JSON.parse(JSON.stringify(comments))
    comments[index]['images'].splice(imgIndex, 1)
    this.setState({ comments })
  }
  
  _commentGrade = (grade, index) => {
    let { comments } = this.state
    comments = JSON.parse(JSON.stringify(comments))
    comments[index].grade = grade
    this.setState({ comments })
  }

  _auditStatus = ( auditStatus )=> {
    switch (auditStatus) {
      case 'WAIT_AUDIT': return '您的初评待审核，不能进行追评'
      case 'REFUSE_AUDIT': return '您的初评审核未通过，不能进行追评'
      default: return '您的初评审核通过'
    }

  }
  
  _onSubmit = async () => {
    const { order_sn, state } = this
    const { comments, description_score, service_score, delivery_score, first_comment } = state
    comments.forEach(key => {
      // 如果没有评价 则默认好评
      if(!key.grade) key.grade = 'good'
      // 如果是追评 && 没有评价id
      if(!first_comment && !key.comment_id) key.comment_id = key.$$goods_comment.comment_id
      delete key.$$goods_comment
    })
    // 初评校验 非好评 必须输入原因
    if (first_comment) {
      const result = comments.some(key => (key.grade !== 'good' && !key.content))
      if(result) {
        this.props.dispatch(messageActions.error('请告诉我们您不满意的原因，我们愿尽最大的努力！'))
        return
      }
    }
    // 追评校验
    if(!first_comment) {
      const result = comments.some(key => !key.content)
      if(result) {
        this.props.dispatch(messageActions.error('请填写追评内容！'))
        return
      }
    }
    await API_Members[!first_comment?'AppendCommentsOrder':'commentsOrder'](!first_comment?comments:{
      order_sn,
      description_score,
      service_score,
      delivery_score,
      comments
    })
    await this.props.dispatch(messageActions.success('评价成功！'))
    const { navigation } = this.props
    const { params = {} } = navigation.state
    params.callback && params.callback()
    navigation.goBack()
  }
  
  render() {
    const { sku_list } = this
    const { comments, loading, first_comment } = this.state
    return (
      <View style={ styles.container }>
        <KeyboardAwareScrollView 
          enableOnAndroid
          extraScrollHeight={ 60 }
          keyboardOpeningTime={ 0 }>
          <StatusBar barStyle="dark-content"/>
          { comments.map((comment, index) => (
            <View key={ comment.sku_id } style={ styles.item_comment }>
              <View style={ styles.grade_view }>
                <Image style={ styles.grade_view_image } source={ { uri: sku_list[index].goods_image } }/>
                { first_comment && <View style={ styles.grade_view_grade }>
                  <GradeItem
                    checked={ comment.grade === 'good' }
                    text="好评"
                    nomal_img={ require('../../images/icon-good_nomal.png') }
                    active_img={ require('../../images/icon-good_active.png') }
                    onPress={ () => this._commentGrade('good', index) }
                  />
                  <GradeItem
                    checked={ comment.grade === 'neutral' }
                    text="中评"
                    nomal_img={ require('../../images/icon-general_nomal.png') }
                    active_img={ require('../../images/icon-general_active.png') }
                    onPress={ () => this._commentGrade('neutral', index) }
                  />
                  <GradeItem
                    checked={ comment.grade === 'bad' }
                    text="差评"
                    nomal_img={ require('../../images/icon-low_nomal.png') }
                    active_img={ require('../../images/icon-low_active.png') }
                    onPress={ () => this._commentGrade('bad', index) }
                  />
                </View> }
              </View>
              { !!comment.$$goods_comment && <View>
                  <TextItem title='初评日期' content={ Foundation.unixToDate(comment.$$goods_comment.create_time) } />
                  <TextItem title='初评评分' content={ comment.$$goods_comment.grade === 'good' ? '好评' : 
                  comment.$$goods_comment.grade === 'neutral' ? '中评' : comment.$$goods_comment.grade === 'bad' ? '差评': '' } />
                  <TextItem title='初评内容' content={ comment.$$goods_comment.content } />
                  { comment.$$goods_comment.images && comment.$$goods_comment.images.length && <View style={ styles.item_label }>
                    <F16Text>初评图片:</F16Text>
                    { comment.$$goods_comment.images.map((uri, _index) => (
                      <Image style={ [styles.comment_image, { marginLeft: 5, width: IMAGE_WIDTH - 20 }] } key={ _index } source={ { uri } }/>
                    )) }
                  </View> }
                  <TextItem title='初评审核状态' content={ this._auditStatus(comment.$$goods_comment.audit_status) } />
                </View>  }
              <View style={ styles.input_view }>
                <TextInput
                  key={ comment.sku_id }
                  style={ styles.input_input }
                  multiline={ true }
                  maxLength={ 500 }
                  onChangeText={ (text) => this._onChangeText(text, index) }
                  placeholder={ comment.grade === 'good' ? '宝贝满足您的期待吗？说说它的优点和美中不足的地方吧' : '可以告诉我们您遇到了什么问题吗？' }
                  placeholderColor="#777777"
                />
                <F14Text style={ styles.input_lenght }>{ 500 - comment.content.length }</F14Text>
              </View>
              <View style={ styles.images_view }>
                { comment.images.map((uri, _index) => (
                  <CommentImage key={ _index } uri={ uri } onPress={ () => this._delImage(index, _index) }/>
                )) }
                <TouchableOpacity
                  style={ styles.image_view }
                  activeOpacity={ 1 }
                  onPress={ () => this._onSelectImage(index) }
                >
                  <Image style={ styles.camera_image } source={ require('../../images/icon-camera.png') }/>
                </TouchableOpacity>
              </View>
            </View>
          )) }
          { first_comment && <View style={ styles.shop_grade_view }>
            <View style={ styles.shop_grade_title }>
              <Image
                style={ { width: 25, height: 25, marginRight: 10 } }
                source={ require('../../images/icon-shop_comment.png') }/>
              <F14Text>店铺评分</F14Text>
            </View>
            <View style={ styles.shop_grade }>
              <F14Text>描述相符</F14Text>
              <View style={ { width: 150, marginLeft: 10 } }>
                <StarRating
                  disabled={ false }
                  maxStars={ 5 }
                  rating={ this.state.description_score }
                  starSize={ 20 }
                  fullStarColor={ colors.main }
                  selectedStar={ (rating) => this.setState({ description_score: rating }) }
                />
              </View>
            </View>
            <View style={ styles.shop_grade }>
              <F14Text>服务态度</F14Text>
              <View style={ { width: 150, marginLeft: 10 } }>
                <StarRating
                  disabled={ false }
                  maxStars={ 5 }
                  rating={ this.state.service_score }
                  starSize={ 20 }
                  fullStarColor={ colors.main }
                  selectedStar={ (rating) => this.setState({ service_score: rating }) }
                />
              </View>
            </View>
            <View style={ styles.shop_grade }>
              <F14Text>物流服务</F14Text>
              <View style={ { width: 150, marginLeft: 10 } }>
                <StarRating
                  disabled={ false }
                  maxStars={ 5 }
                  rating={ this.state.delivery_score }
                  starSize={ 20 }
                  fullStarColor={ colors.main }
                  selectedStar={ (rating) => this.setState({ delivery_score: rating }) }
                />
              </View>
            </View>
          </View> }
          <Loading show={ loading }/>
        </KeyboardAwareScrollView>
        <BigButton title="提交评价" onPress={ this._onSubmit } style={ styles.big_btn }/>
      </View>
    )
  }
}

const CommentImage = ({ uri, onPress }) => {
  return (
    <View style={ styles.image_view }>
      <Image style={ styles.comment_image } source={ { uri } }/>
      <TouchableOpacity style={ styles.del_btn } activeOpacity={ 1 } onPress={ onPress }>
        <Icon name="md-trash" size={ 18 } color='red'/>
      </TouchableOpacity>
    </View>
  )
}

const GradeItem = ({ checked, text, active_img, nomal_img, onPress }) => {
  return (
    <TouchableOpacity onPress={ onPress } style={ styles.grade_item }>
      <Image style={ { width: 25, height: 25 } } source={ checked ? active_img : nomal_img }/>
      <F16Text>{ text }</F16Text>
    </TouchableOpacity>
  )
}

const IMAGE_WIDTH = (Screen.width - 20 - 40) / 5
const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: colors.gray_background
  },
  item_comment: {
    marginBottom: 10
  },
  header_right: {
    width: 44,
    height: 44,
    justifyContent: 'center',
    alignItems: 'center'
  },
  grade_item: {
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    marginLeft: 10
  },
  grade_view: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    width: Screen.width,
    height: 75,
    backgroundColor: '#FFFFFF',
    paddingHorizontal: 10,
    paddingVertical: 10
  },
  grade_view_image: {
    width: 60,
    height: 60,
    borderRadius: 3
  },
  grade_view_grade: {
    flexDirection: 'row',
    justifyContent: 'flex-start',
    alignItems: 'center',
    width: Screen.width - 20 - 60 - 10,
    height: 60,
    marginLeft: 10
  },
  input_view: {
    justifyContent: 'center',
    width: Screen.width,
    height: 100,
    backgroundColor: '#F8F8F8'
  },
  input_input: {
    width: Screen.width,
    height: 80,
    backgroundColor: '#F8F8F8',
    paddingHorizontal: 10,
    fontSize: 16
  },
  input_lenght: {
    position: 'absolute',
    right: 10,
    bottom: 10,
    backgroundColor: colors.transparent
  },
  images_view: {
    flexDirection: 'row',
    justifyContent: 'flex-start',
    alignItems: 'center',
    flexWrap: 'wrap',
    width: Screen.width,
    backgroundColor: '#FFFFFF',
    paddingLeft: 10,
    paddingTop: 10
  },
  image_view: {
    justifyContent: 'center',
    alignItems: 'center',
    width: IMAGE_WIDTH,
    height: IMAGE_WIDTH,
    marginRight: 10,
    marginBottom: 10,
    borderColor: colors.cell_line_backgroud,
    borderWidth: Screen.onePixel,
    borderRadius: 3
  },
  comment_image: {
    width: IMAGE_WIDTH,
    height: IMAGE_WIDTH
  },
  camera_image: {
    width: 35,
    height: 35
  },
  del_btn: {
    justifyContent: 'center',
    alignItems: 'center',
    position: 'absolute',
    left: 0,
    bottom: 0,
    width: IMAGE_WIDTH,
    height: 20,
    backgroundColor: 'rgba(0,0,0,.5)'
  },
  shop_grade_view: {
    backgroundColor: '#fff',
    paddingHorizontal: 10
  },
  shop_grade_title: {
    flexDirection: 'row',
    alignItems: 'center',
    height: 35
  },
  shop_grade: {
    flexDirection: 'row',
    alignItems: 'center',
    height: 50
  },
  item_label: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    paddingHorizontal: 10,
    paddingVertical: 10,
    backgroundColor: '#FFFFFF'
  },
  big_btn: {
    marginBottom: isIphoneX() ? 30 : 0,
    width: Screen.width
  }
})

export default connect()(CommentOrderScene)
