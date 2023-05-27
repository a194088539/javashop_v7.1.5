/**
 * Created by Andste on 2018/9/29.
 */
import * as types from '../constants/user'
import uuidv1 from 'uuid/v1'

const initialState = {
  user: null,
  uuid: null,
  uid: null
}

export default function user(state = initialState, action) {
  switch (action.type) {
    // 初始化用户信息
    case types.INIT_USER:
      return {
        ...state,
        user: null,
        uuid: uuidv1(),
        uid: null
      }
    // 设置用户UID
    case types.SET_UID:
      return {
        ...state,
        uid: action.uid
      }
    // 设置用户信息
    case types.SET_USER:
      return {
        ...state,
        user: action.user
      }
    // 设置UUID
    case types.SET_UUID:
      return {
        ...state,
        uuid: action.uuid
      }
    default:
      return state
  }
}
