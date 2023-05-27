/**
 * Created by Andste on 2018/10/12.
 */
import * as types from '../constants/token'

const initialState = {
  access_token: null,
  refresh_token: null
}

export default function token(state = initialState, action) {
  switch (action.type) {
    // 设置访问令牌
    case types.SET_ACCESS_TOKEN:
      return {
        ...state,
        access_token: action.access_token
      }
    // 设置刷新令牌
    case types.SET_REFRESH_TOKEN:
      return {
        ...state,
        refresh_token: action.refresh_token
      }
    // 移除访问令牌
    case types.REMOVE_ACCESS_TOKEN:
      return {
        ...state,
        access_token: null
      }
    // 移除刷新令牌
    case types.REMOVE_REFRESH_TOKEN:
      return {
        ...state,
        refresh_token: null
      }
    // 初始化所有令牌
    case types.INIT_ALL_TOKEN:
      return {
        ...state,
        access_token: null,
        refresh_token: null
      }
    default:
      return state
  }
}
