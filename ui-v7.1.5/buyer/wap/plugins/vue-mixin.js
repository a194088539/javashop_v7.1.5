import Vue from 'vue'
import * as API_Common from '@/api/common'
import { domain, api } from '@/ui-domain'

Vue.mixin({
  data() {
    return {
      // 图片上传API
      MixinUploadApi: `${process.env.API_BASE || api.base}/uploaders`,
      // 地区上传API
      MixinRegionApi: `${process.env.API_BASE || api.base}/regions/@id/children`,
      // 域名
      MixinDomain: domain
    }
  },
  computed: {
    /** 计算是否有forward */
    MixinForward() {
      const { forward } = this.$route.query
      return forward ? `?forward=${forward}` : ''
    },
    /** 站点信息 */
    site() {
      return this.$store.getters.site
    }
  },
  methods: {
    /** 滚动到顶部【动画】 */
    MixinScrollToTop(top) {
      if (process.isServer) return
      $("html,body").animate({ scrollTop: top || 0 }, 300)
    },
    /** 用得比较多，放到mixin里 */
    MixinRequired(message, trigger) {
      return { required: true, pattern: /^\S.*$/gi, message: message, trigger: trigger || 'blur' }
    },
    /** 返回上一页 */
    MixinRouterBack() {
      if (window.history.length <= 1) {
        location.href = '/'
      } else {
        window.history.back()
      }
    },
    /** 是否为微信浏览器 */
    MixinIsWeChatBrowser() {
      if (!process.client) return false
      return /micromessenger/i.test(navigator.userAgent)
    },
    /** 是否为支付宝浏览器 */
    MixinIsAliPayBrowser() {
      if (!process.client) return false
      return /alipay/i.test(navigator.userAgent)
    },
    /** 是否为安卓手机浏览器 */
    MixinIsAndroidBrowser() {
      if (!process.client) return false
      return /Android|Adr/i.test(navigator.userAgent)
    },
    /** base64转Blob */
    MixinBase64toBlob(base64) {
      const byteString = atob(base64.split(',')[1])
      const mimeString = base64.split(',')[0].split(':')[1].split(';')[0]
      const ab = new ArrayBuffer(byteString.length)
      const ia = new Uint8Array(ab)
      for (var i = 0; i < byteString.length; i++) {
        ia[i] = byteString.charCodeAt(i)
      }
      return new Blob([ab], {type: mimeString})
    },
    MixinEmojiCharacter (substring) {
      for ( var i = 0; i < substring.length; i++) {
        var hs = substring.charCodeAt(i);
        if (0xd800 <= hs && hs <= 0xdbff) {
          if (substring.length > 1) {
            var ls = substring.charCodeAt(i + 1);
            var uc = ((hs - 0xd800) * 0x400) + (ls - 0xdc00) + 0x10000;
            if (0x1d000 <= uc && uc <= 0x1f77f) {
              return true;
            }
          }
        } else if (substring.length > 1) {
          var ls = substring.charCodeAt(i + 1);
          if (ls == 0x20e3) {
            return true;
          }
        } else {
          if (0x2100 <= hs && hs <= 0x27ff) {
            return true;
          } else if (0x2B05 <= hs && hs <= 0x2b07) {
            return true;
          } else if (0x2934 <= hs && hs <= 0x2935) {
            return true;
          } else if (0x3297 <= hs && hs <= 0x3299) {
            return true;
          } else if (hs == 0xa9 || hs == 0xae || hs == 0x303d || hs == 0x3030
            || hs == 0x2b55 || hs == 0x2b1c || hs == 0x2b1b
            || hs == 0x2b50) {
            return true;
          }
        }
      }
    }
  }
})
