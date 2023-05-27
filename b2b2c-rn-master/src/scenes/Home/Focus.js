/**
 * Created by Andste on 2018/9/30.
 */
import React, {PureComponent} from 'react'
import {
    View,
    Image,
    StyleSheet,
    WebView,
    Alert
} from 'react-native'
import Swiper from 'react-native-swiper'
import {colors} from '../../../config'
import OptNavigate from './OptNavigate'
import {navigate} from '../../navigator/NavigationService'

const height = 180

export default class Focus extends PureComponent {
    render() {
        const {data} = this.props
        if (!data || !data.length) return <View style={styles.container}/>
        return (
            <View style={styles.container}>
                <Swiper
                    height={height}
                    loop={true}
                    autoplay={true}
                    showsPagination={true}
                    paginationStyle={{bottom: 3}}
                    activeDotColor={colors.main}
                >
                    {data.map((item, index) => (
                        <OptNavigate style={{flex: 1}} opacity={1} data={item} key={index} onPress={() => {
                            skip(item['operation_type'], item['operation_param'])
                        }}>
                            <Image source={{uri: item['pic_url']}} style={styles.focus_img}/>
                        </OptNavigate>
                    ))}
                </Swiper>
            </View>
        )
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        height,
        backgroundColor: '#FFFFFF'
    },
    focus_img: {
        flex: 1,
        height
    }
})

function skip(operation_type = 'CATEGORY', operation_param = 1) {
    switch (operation_type) {
        // 链接地址
        case 'URL':
            navigate("HtmlView", {url: operation_param})
            break
        // 商品
        case 'GOODS':
            navigate('Goods', {id: operation_param})
            break
        // 关键字
        case 'KEYWORD':
            navigate('GoodsList', {keyword: operation_param})
            break
        // 店铺
        case 'SHOP':
            navigate('Shop', {id: operation_param})
            break
        // 分类
        case 'CATEGORY':
            navigate('GoodsList', {cat_id: operation_param})
            break
        default:
            break

    }


}
