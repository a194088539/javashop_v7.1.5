/**
 * Created by Andste on 2018/10/11.
 */
import * as API_Members from '../../apis/members'
import * as types from '../constants/user'
import * as tokenActions from '../actions/token'
import * as cartActions from '../actions/cart'

/**
 * 初始化用户信息
 * @returns {{type : string}}
 */
export function initUserAction() {
  return dispatch => {
    // 初始化用户信息
    dispatch(initUser())
    // 初始化所有token
    dispatch(tokenActions.initAllTokenAction())
    // 清空购物车数据
    dispatch(cartActions.cleanCartAction())
  }
}

/**
 * 初始化用户信息
 * @returns {{type : string}}
 */
function initUser() {
  return {
    type: types.INIT_USER
  }
}

/**
 * 获取用户信息
 * @returns {Function}
 */
export function getUserAction(success, error) {
  return async dispatch => {
    try {
      const user = await API_Members.getUserInfo()
      await dispatch(getUserSuccess(user))
      typeof success === 'function' && success()
    } catch (e) {
      await dispatch(initUserAction())
      typeof error === 'function' && error(e)
    }
  }
}

/**
 * 获取用户信息成功
 * @param user
 * @returns {{type : string, user : *}}
 */
function getUserSuccess(user) {
  return {
    type: types.SET_USER,
    user
  }
}

/**
 * 登录成功
 * @param res
 * @returns {{type : string}}
 */
export function loginSuccessAction(res) {
  return dispatch => {
    dispatch(setUIDAction(res.uid))
    dispatch(tokenActions.setAccessTokenAction(res.access_token))
    dispatch(tokenActions.setRefreshTokenAction(res.refresh_token))
    dispatch(cartActions.getCartDataAction())
  }
}

/**
 * 设置用户UID
 * @param uid
 * @returns {{type : string, uid : *}}
 */
export function setUIDAction(uid) {
  return {
    type: types.SET_UID,
    uid
  }
}

/**
 * 设置UUID
 * @param uuid
 * @returns {{type : string, uuid : *}}
 */
export function setUUIDAction(uuid) {
  return {
    type: types.SET_UUID,
    uuid
  }
}
