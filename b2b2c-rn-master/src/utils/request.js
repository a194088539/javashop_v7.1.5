import React from 'react'
import { store } from '../redux/store'
import {
  messageActions,
  userActions,
  tokenActions,
  cartActions
} from '../redux/actions'
import axios from 'axios'
import { api } from '../../config'
import { navigate } from '../navigator/NavigationService'
import * as API_Common from '../apis/common'
import { checkToken, Foundation } from './index'
import md5 from 'js-md5'
const qs = require('qs')

const service = axios.create({
  timeout: api.timeout,
  baseURL: api.buyer
})

// request拦截器
service.interceptors.request.use(async config => {
  const { needToken } = config
  // 如果是put/post请求，用qs.stringify序列化参数
  const is_put_post = config.method === 'put' || config.method === 'post'
  const is_json = config.headers['Content-Type'] === 'application/json'
  if (is_put_post && is_json) {
    config.data = JSON.stringify(config.data)
  }
  if (is_put_post && !is_json && config.url !== API_Common.upload) {
    config.data = qs.stringify(config.data, { arrayFormat: 'repeat' })
  }
	const { token, user } = store.getState()
  // uuid
	config.headers['uuid'] = user.uuid
  // 获取访问Token
  let accessToken = token.access_token
  if (accessToken && needToken) {
    if (api.api_model === 'pro') {
      const uid = user.uid
      const nonce = Foundation.randomString(6)
      const timestamp = parseInt(new Date().getTime() / 1000)
      const sign = md5(uid + nonce + timestamp + accessToken)
      const _params = { uid, nonce, timestamp, sign }
      let params = config.params || {}
      params = { ...params, ..._params }
      config.params = params
    } else {
      config.headers['Authorization'] = accessToken
    }
  }
  return config
})

// respone拦截器
service.interceptors.response.use(
  response => response.data,
  error => {
    const error_res = error.response || {}
	  const error_data = error_res.data || {}
	  if (error.config.message !== false) {
	  	let _message = error.code === '' ? '连接超时，请稍后再试！' : null
		  _message = error_data.message || _message
		  store.dispatch(messageActions.error(_message))
	  }
    if (error_res.status === 403) {
      store.dispatch(cartActions.cleanCartAction())
      store.dispatch(userActions.initUserAction())
      store.dispatch(tokenActions.removeAccessTokenAction())
      store.dispatch(tokenActions.removeRefreshTokenAction())
      store.dispatch(messageActions.error('登录信息失效，请重新登录'))
      navigate('Login')
    }
    return Promise.reject(error)
  }
)

export const Method = {
  GET: 'GET',
  POST: 'POST',
  PUT: 'PUT',
  DELETE: 'DELETE'
}

export default function request(options) {
  // 如果不需要token或是请求刷新token，不需要检查。
  if (!options.needToken || options.url.indexOf('passport/token') !== -1) {
    return service(options)
  }
  return new Promise((resolve, reject) => {
    checkToken(options).then(
      service(options).then(resolve).catch(reject)
    ).catch(reject)
  })
}
