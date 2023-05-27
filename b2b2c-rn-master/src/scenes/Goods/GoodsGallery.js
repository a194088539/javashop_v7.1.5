/**
 * Created by Andste on 2018/11/21.
 */
import React, { PureComponent } from 'react'
import {
  View,
  Image,
  Modal,
  TouchableOpacity,
  StyleSheet
} from 'react-native'
import { colors } from '../../../config'
import { Screen } from '../../utils'
import { isIphoneX } from 'react-native-iphone-x-helper'
import Swiper from 'react-native-swiper'
import ImageViewer from 'react-native-image-zoom-viewer'

export default class GoodsGallery extends PureComponent {
  constructor(props) {
    super(props)
    this.state = {
      showModal: false,
      imageIndex: 0,
      images: props.data.map(item => ({
        url: item['big']
      }))
    }
  }
  
  _onShowModal = (index) => {
    this.setState({ showModal: true, imageIndex: index })
  }
  
  _onCloseModal = () => {
    this.setState({ showModal: false, imageIndex: 0 })
  }
  
  render() {
    const { showModal, imageIndex, images } = this.state
    return (
      <View style={ styles.container }>
        <Swiper
          height={ Screen.width }
          loop={ true }
          autoplay={ true }
          showsPagination={ true }
          paginationStyle={ { bottom: 3 } }
          activeDotColor={ colors.main }
        >
          { images.map((item, index) => (
            <TouchableOpacity
              onPress={ () => this._onShowModal(index) }
              key={ index }
              activeOpacity={ 1 }
            >
              <Image source={ { uri: item['url'] } } style={ styles.goods_img }/>
            </TouchableOpacity>
          )) }
        </Swiper>
        <Modal
          visible={ showModal }
          transparent={ true }
          animationType="fade"
          onRequestClose={ this._onCloseModal }
        >
          <ImageViewer
            imageUrls={ images }
            index={ imageIndex }
            enablePreload={ true }
            saveToLocalByLongPress={ false }
            enableSwipeDown={ true }
            onSwipeDown={ this._onCloseModal }
            onCancel={ this._onCloseModal }
            enableImageZoom={ true }
          />
        </Modal>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    height: Screen.width,
    paddingTop: isIphoneX() ? 40 : 25,
  },
  goods_img: {
    width: Screen.width,
    height: Screen.width
  },
  goods_img_viewer: {
    width: Screen.width,
    height: Screen.height
  }
})
