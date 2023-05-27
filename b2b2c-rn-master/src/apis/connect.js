/**
 * Created by Andste on 2018/7/2.
 * 信任登录相关API
 */

import request, { Method } from '../utils/request'
import md5 from 'js-md5'

/**
 * 获取第三方登录url
 * @param client_type
 * @param login_type
 * @returns {string}
 */
export function getConnectUrl(client_type, login_type) {
  return `passport/connect/${client_type}/${login_type}`
}

/**
 * 获取个人中心绑定url
 * @param login_type
 * @returns {string}
 */
export function getLogindConnectUrl(login_type) {
  return request({
    url: `account-binder/pc/${login_type}`,
    method: Method.GET,
    needToken: true
  })
}

/**
 * 第三方登录绑定
 * @param uuid
 * @param params
 */
export function loginByConnect(uuid, params) {
  params.password = md5(params.password)
  return request({
    url: `passport/login-binder/pc/${uuid}`,
    method: Method.PUT,
    data: params
  })
}

/**
 * 登录绑定openid
 * @param uuid
 */
export function loginBindConnect(uuid) {
  return request({
    url: `account-binder/login/${uuid}`,
    method: Method.POST,
    needToken: true
  })
}

/**
 * 注册绑定openid
 * @param uuid
 */
export function registerBindConnect(uuid) {
  return request({
    url: `account-binder/register/${uuid}`,
    method: Method.POST,
    needToken: true
  })
}

/**
 * 获取绑定列表
 */
export function getConnectList() {
  return request({
    url: 'account-binder/list',
    method: Method.GET,
    needToken: true
  })
}

/**
 * 解绑
 * @param type
 */
export function unbindConnect(type) {
  return request({
    url: `account-binder/unbind/${type}`,
    method: Method.POST,
    needToken: true
  })
}

/**
 * 获取App联合登录所需参数
 * @param type
 * @returns {*|*}
 */
export function getConnectParams(type) {
  return request({
    url: `passport/connect/app/${type}/param`,
    method: Method.GET
  })
}

/**
 * 获取openId是否绑定
 * @param open_id
 * @param type
 * @returns {*|*}
 */
export function getOpenidBinded(open_id, type) {
  return request({
    url: `passport/connect/app/${type}/openid`,
    method: Method.GET,
    params: { openid: open_id }
  })
}

/**
 * 第三方登录绑定
 * @param params
 * @returns {*|*}
 */
export function connectLoginBinder(params: {
  openid: string,
  type: string,
  username: string,
  password: string,
  captcha: string,
  uuid: string
}) {
  params.password = md5(params.password)
  return request({
    url: `passport/login-binder/app`,
    method: Method.POST,
    params
  })
}

/**
 * 第三方注册绑定
 * @param params
 * @returns {*|*}
 */
export function connectRegisterBinder(params: {
  openid: string,
  type: string,
  mobile: string,
  captcha: string,
  password: string,
  uuid: string
}) {
  params.password = md5(params.password)
  return request({
    url: `passport/register-binder/app`,
    method: Method.POST,
    data: params
  })
}

/**
 * 第三方登录绑定-通过手机号
 * @param params
 * @returns {*|*}
 */
export function connectLoginBinderByMobile(params: {
  sms_code: string,
  openid: string,
  type: string,
  mobile: string,
  uuid: string
}) {
  return request({
    url: `passport/sms-binder/app`,
    method: Method.POST,
    data: params
  })
}
