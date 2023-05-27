/**
 * Created by Andste on 2018/5/7.
 */

import React from 'react'
import jwt_decode from 'jwt-decode'
import { request } from './index'
import { store } from '../redux/store'
import {
  userActions,
  tokenActions,
  cartActions,
  messageActions
} from '../redux/actions'

/**
 * 检查token：
 * 1. user/accessToken/refreshToken都不存在。
 *    表示用户没有登录，放行所有API
 * 2. 不存在accessToken，但是user/refreshToken存在。
 *    表示accessToken过期，需要重新获取accessToken。
 *    如果重新获取accessToken返回token失效错误，说明已被登出。
 * @param options
 * @returns {Promise<any>}
 */
export default async function checkToken(options) {
  const { token, user: _user } = store.getState()
  // user
  const user = _user.user
  // 访问Token
  const accessToken = token.access_token
  // 刷新Token
  const refreshToken = token.refresh_token
  
  // 现在时间戳
  const nowTime = parseInt((new Date).getTime() / 1000)
  // 访问令牌是否过期【不存在也视为过期】
  const accessTokenOverdue = !accessToken || nowTime >= jwt_decode(accessToken)['exp']
  // 刷新令牌是否过期【不存在也视为过期】
  const refreshTokenOverdue = !refreshToken || nowTime >= jwt_decode(refreshToken)['exp']
  
  // 返回异步方法
  return new Promise((resolve, reject) => {
    /**
     * 如果user数据存在
     * 且accessToken和refreshToken都未过期
     * 说明必要条件都存在，可以直接通过，并且不需要后续操作。
     */
    if (user && !accessTokenOverdue && !refreshTokenOverdue) {
      resolve()
      return
    }
    /**
     * 如果需要Token，但是refreshToken过期
     * 说明登录已失效，需要重新登录。
     */
    if (options.needToken && refreshTokenOverdue) {
      cleanAllData()
      reject()
      return
    }
    /**
     * accessToken过期，但是refreshTokenOverdue未过期。
     * 说明用户已登录，只是accessToken过期，需要重新获取accessToken。
     * 如果没有needToken，说明不需要等待获取到新的accessToken后再请求。
     * 否则，需要等待
     */
    if (accessTokenOverdue && !refreshTokenOverdue) {
      // console.log(进入重新获取token环节。。。')
      /**
       * 即使这个API不需要token，也重新获取
       * 如果没有刷新token锁，需要刷新token。
       * 如果有刷新token锁，则进入循环检测。
       */
      if (!global['__refreshTokenLock__']) {
        // console.log(options.url + ' | 检测到accessToken失效，这个请求需要等待刷新token。')
        // 如果不需要Token，则不需要等拿到新的Token再请求。
        if (!options.needToken) resolve()
        // 开始请求新的Token，并加锁。
        global['__refreshTokenLock__'] = request({
          url: `passport/token`,
          method: 'post',
          data: { refresh_token: refreshToken }
        }).then(response => {
          store.dispatch(tokenActions.setAccessTokenAction(response.accessToken))
          store.dispatch(tokenActions.setRefreshTokenAction(response.refreshToken))
          global['__refreshTokenLock__'] = null
          // console.log(options.url + ' | 已拿到新的token。')
          options.needToken && resolve()
        }).catch(() => {
          global['__refreshTokenLock__'] = undefined
          cleanAllData()
          reject()
        })
      } else {
        if (!options.needToken) {
          resolve()
          return
        }
        // console.log('进入循环检测...')
        // 循环检测刷新token锁，当刷新锁变为null时，说明新的token已经取回。
        checkLock()
        
        // noinspection JSAnnotator
        function checkLock() {
          setTimeout(() => {
            const __RTK__ = global['__refreshTokenLock__']
            // console.log(options.url + ' | 是否已拿到新的token：', __RTK__ === null)
            if (__RTK__ === undefined) {
              // console.log('登录已失效了，不用再等待了...')
              cleanAllData()
              reject()
              return
            }
            __RTK__ === null
              ? resolve()
              : checkLock()
          }, 500)
        }
      }
      return
    }
    resolve()
  })
}

/**
 * 清除数据
 * 清除购物车
 * 初始化用户信息
 * 移除AccessToken
 * 移除refreshToken
 */
function cleanAllData() {
  store.dispatch(cartActions.cleanCartAction())
  store.dispatch(userActions.initUserAction())
  store.dispatch(tokenActions.removeAccessTokenAction())
  store.dispatch(tokenActions.removeRefreshTokenAction())
  store.dispatch(messageActions.error('登录信息失效，请重新登录'))
}
