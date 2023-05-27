<template>
  <span v-if="isMiniPrograme" @click="toMiniPage"><slot/></span>
  <a v-else :href="toPage" target="_blank"><slot/></a>
</template>

<script>
  let wx
  if (process.browser) wx = require('weixin-js-sdk')
  export default {
    name: 'EnOperation',
    props: ['opt','is-mini-programe'],
    computed: {
      toPage() {
        const { type, value } = this.opt
        switch (type) {
            // 链接地址
            case 'URL': return value
            // 商品
            case 'GOODS': return `/goods/${value}`
            // 关键字
            case 'KEYWORD': return `/goods?keyword=${value}`
            // 店铺
            case 'SHOP': return `/shop/${value}`
            // 分类
            case 'CATEGORY': return `/goods?category=${value}`

            default: return '/'
          }
      }
    },
    methods:{
      /** 如果是小程序环境 */
      toMiniPage(){
        const { type, value } = this.opt
        switch (type) {
          // 链接地址
          case 'URL': wx.miniProgram.navigateTo({ url: `/pages${value}${value}` })
            break
          // 商品
          case 'GOODS': wx.miniProgram.navigateTo({ url: `/pages/goods/goods?goods_id=${value}` })
            break
          // 关键字 跳转至商品搜索页面 keyword
          case 'KEYWORD':  wx.miniProgram.navigateTo({ url: `/pages/category/category?keyword=${value}` })
            break
          // 店铺
          case 'SHOP': wx.miniProgram.navigateTo({ url: `/pages/shop/shop_id/shop_id?id=${value}` })
            break
          // 分类
          case 'CATEGORY': wx.miniProgram.navigateTo({ url: `/pages/category/category?category=${value}` })
            break
          default: wx.miniProgram.reLaunch({ url: '/pages/home/home' })
        }
      }
    }
  }
</script>
