/**
 * Created by Andste on 2018/11/15.
 */
import * as types from '../constants/cart'
import * as API_Trade from '../../apis/trade'
import { store } from '../store'

/**
 * 获取购物车数据，包括购物车总价
 * @returns {Function}
 */
export function getCartDataAction() {
  if (!store.getState().user.user) return {type: 'NOTHING'}
  return async dispatch => {
    const res = await API_Trade.getCarts()
    dispatch(getCartDataSuccess(res))
  }
}

/**
 * 获取购物车数据成功
 * @param data
 * @returns {{type : string, data : *}}
 */
function getCartDataSuccess(data) {
  return {
    type: types.GET_CART_DATA,
    data
  }
}

/**
 * 清空购物车
 * @returns {{type : string}}
 */
export function cleanCartAction() {
  return {
    type: types.CLEAN_CART
  }
}
