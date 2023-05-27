/**
 * Created by Andste on 2018/10/25.
 */

// AppStoreID
export const appStoreId = 1308188084

// 微信APPID
export const wechatAppId = 'wxea6dad1bd46739b4'

// 微博回调地址 类似：https://api.weibo.com/oauth2/default.html
export const weiboRedirectURI = 'http://m.javamall.com.cn/b2c/connect/weibo.do'

// 支付宝相关配置 授权时用
export const alipayConfig = {
  // 支付宝分配给开发者的应用ID 类似：2014123100022800
  app_id: '2017101909390405',
  // 签约的支付宝账号对应的支付宝唯一用户号，以2088开头的16位纯数字组成
  pid: '2088102971854915'
}

// 分享商品的配置
// 下面会被覆盖的配置，如果不是特别需要，都建议留空
export const shareConfig = {
  // 分享标题
  title: '这个商品降价啦，快来看啊! 👉 ',
  // 分享描述【这里为空的话，默认为商品名称】
  description: '',
  // 图片URL【这里为空的话，默认为商品图片】
  imageUrl: '',
  // 分享的URL【这里为空的话，默认为商品URL】
  webpageUrl: ''
}

