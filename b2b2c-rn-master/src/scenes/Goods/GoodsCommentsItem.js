/**
 * Created by Andste on 2018/11/22.
 */
import React from 'react'
import {
  View,
  Text,
  Image,
  TouchableOpacity,
  StyleSheet
} from 'react-native'
import { colors } from '../../../config'
import { Screen, Foundation } from '../../utils'
import { Face, DashLine } from '../../widgets'

export default function ({ data, imageShow, imageClose }) {
  let reviewImages = !!data['images'] ? data['images'].map(item => ({
    url: item
  })) : {}
  let reviewAdditionalImages = data.additional_comment && !!data.additional_comment['images'] ? data.additional_comment['images'].map(item => ({
    url: item
  })) : {}
  let images = []
  data['images'] && data['images'][0] && data['images'].forEach((item, index) => {
    images.push(<TouchableOpacity
                  onPress={ () => imageShow(index, reviewImages) }
                  key={ index }
                  activeOpacity={ 1 }
                ><Image key={ item } style={ styles.comment_image } source={ { uri: item } }/></TouchableOpacity>)
  })
  let additional_images = []
  data.additional_comment && data.additional_comment['images'] && data.additional_comment['images'][0] && data.additional_comment['images'].forEach((item, index) => {
    additional_images.push(<TouchableOpacity
                  onPress={ () => imageShow(index, reviewAdditionalImages) }
                  key={ index }
                  activeOpacity={ 1 }
                ><Image key={ item } style={ styles.comment_image } source={ { uri: item } }/></TouchableOpacity>)
  })
  return (
    <View style={ styles.container }>
      <View style={ styles.main_comment_view }>
        <View style={ styles.face_view }>
          <Face style={ styles.face } uri={ data['member_face'] }/>
        </View>
        <View style={ styles.comment_view }>
          <View style={ styles.title }>
            <Text style={ styles.title_font }>{ data['member_name'] }</Text>
            <Text
              style={ [
                styles.title_font,
                styles.title_dateline
              ] }
            >{ Foundation.unixToDate(data['create_time']) }</Text>
          </View>
          <View>
            <Text style={ styles.content_text }>{ data['content'] }</Text>
          </View>
          { data['images'] && data['images'][0] ? (
            <View style={ styles.image_view }>{ images }</View>
          ) : undefined }
          { data['reply'] ? (
            <View style={ styles.reply_view }>
              <Text style={ styles.reply }>
                <Text style={ { color: colors.main } }>卖家回复：</Text>
                <Text>{ data['reply']['content'] }</Text>
              </Text>
            </View>
          ) : undefined }
        </View>
      </View>
      { !!data.additional_comment && 
        <View>
          <DashLine/>
          <View style={ styles.additional_comment_view }>
            <View style={ styles.comment_view }>
              <View style={ styles.additional_title }>
                <Text style={ [styles.title_font,{color: colors.text, fontSize: 16}] }>追加评论</Text>
                <View style={ {justifyContent: 'flex-end'} }>
                  <Text
                    style={ [
                      styles.title_font,
                      styles.title_dateline
                    ] }
                  >{ Foundation.unixToDate(data.additional_comment['create_time']) }</Text>
                </View>
              </View>
            </View>
            <View style={ {marginLeft: 45} }>
              <Text style={ styles.content_text }>{ data.additional_comment['content'] }</Text>
            </View>
            { data.additional_comment['images'] && data.additional_comment['images'][0] ? (
              <View style={ [styles.image_view, {marginLeft: 45}]}>{ additional_images }</View>
            ) : undefined }
            { data.additional_comment['reply'] ? (
              <View style={ [styles.reply_view, {marginLeft: 45}]}>
                <Text style={ styles.reply }>
                  <Text style={ { color: colors.main } }>卖家回复：</Text>
                  <Text>{ data.additional_comment['reply']['content'] }</Text>
                </Text>
              </View>
            ) : undefined }
          </View>
        </View>
        }
    </View>
  )
}

const COMMENTS_WIDTH = Screen.width - 20 - 38 - 5
const styles = StyleSheet.create({
  container: {
    padding: 10,
    backgroundColor: '#FFFFFF'
  },
  face_view: {
    width: 38,
    height: 38,
    marginRight: 5,
    borderRadius: 38,
    overflow: 'hidden'
  },
  face: {
    width: 38,
    height: 38
  },
  main_comment_view: {
    flexDirection: 'row'
  },
  additional_comment_view: {
    flexDirection: 'row',
    width: COMMENTS_WIDTH,
    flexWrap: 'wrap',
  },
  comment_view: {
    width: COMMENTS_WIDTH
  },
  title: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    width: COMMENTS_WIDTH,
    height: 38
  },
  additional_title: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    width: Screen.width - 20,
    height: 38
  },
  title_font: {
    fontSize: 14
  },
  title_dateline: {
    color: '#777777'
  },
  content_text: {
    fontSize: 16,
    color: colors.text,
    lineHeight: 22
  },
  image_view: {
    width: Screen.width,
    flexDirection: 'row',
    justifyContent: 'flex-start',
    alignContent: 'space-between',
    flexWrap: 'wrap',
    marginTop: 10
  },
  comment_image: {
    width: (COMMENTS_WIDTH - 20) / 3,
    height: (COMMENTS_WIDTH - 20) / 3,
    marginRight: 10,
    marginBottom: 10
  },
  
  reply_view: {
    borderTopWidth: 1,
    paddingTop: 10,
    borderColor: colors.gray_background
  },
  reply: {
    fontSize: 16,
    color: '#666666',
    lineHeight: 22
  }
})
