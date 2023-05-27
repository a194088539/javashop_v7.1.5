/**
 * Created by Andste on 2018/7/2.
 * base    : 基础业务API
 * buyer   : 买家API
 * seller  : 商家中心API
 * admin   ：后台管理API
 */

const env = process.server
  ? process.env
  : (global.window ? window.__NUXT__.state.env : {})

module.exports = {
  // 开发环境
  dev: {
    base  : 'http://javashop7.s1.natapp.cc/base-api',
    buyer : 'http://javashop7.s1.natapp.cc/buyer-api',
    seller: 'http://javashop7.s1.natapp.cc/seller-api',
    admin : 'http://javashop7.s1.natapp.cc/manager-api'
  },
  // 生产环境
  pro: {
    base  : env.API_BASE || '${PROTOCOL}://${BASE_API_DOMAIN}:${BASE_API_PORT}',
    buyer : env.API_BUYER || '${PROTOCOL}://${BUYER_API_DOMAIN}:${BUYER_API_PORT}',
    seller: env.API_SELLER || '${PROTOCOL}://${SELLER_API_DOMAIN}:${SELLER_API_PORT}',
    admin : env.API_ADMIN || '${PROTOCOL}://${ADMIN_API_DOMAIN}:${ADMIN_API_PORT}',
  }
}
