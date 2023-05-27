/**
 * Created by Andste on 2018/10/12.
 */
import * as types from '../constants/token'

/**
 * 设置访问令牌
 * @param access_token
 * @returns {{type : string, accessToken : *}}
 */
export function setAccessTokenAction(access_token) {
  return {
    type: types.SET_ACCESS_TOKEN,
    access_token
  }
}

/**
 * 设置刷新令牌
 * @param refresh_token
 * @returns {{type : string, refresh_token : *}}
 */
export function setRefreshTokenAction(refresh_token) {
  return {
    type: types.SET_REFRESH_TOKEN,
    refresh_token
  }
}

/**
 * 移除访问令牌
 * @returns {{type : string}}
 */
export function removeAccessTokenAction() {
  return {
    type: types.REMOVE_ACCESS_TOKEN
  }
}

/**
 * 移除刷新令牌
 * @returns {{type : string}}
 */
export function removeRefreshTokenAction() {
  return {
    type: types.REMOVE_REFRESH_TOKEN
  }
}

/**
 * 初始化所有令牌
 * @returns {{type : string}}
 */
export function initAllTokenAction() {
  return {
    type: types.INIT_ALL_TOKEN
  }
}
