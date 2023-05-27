/**
 * Created by Andste on 2018/10/11.
 */
import * as types from '../constants/message'

const initialState = {
  message: null,
  timestamp: new Date().getTime(),
  type: null
}

export default function message(state = initialState, action) {
  state.timestamp = new Date().getTime()
  switch (action.type) {
    case types.INFO:
      return {
        ...state,
        type: types.INFO,
        message: action.message
      }
    case types.SUCCESS:
      return {
        ...state,
        type: types.SUCCESS,
        message: action.message
      }
    case types.WARNING:
      return {
        ...state,
        type: types.WARNING,
        message: action.message
      }
    case types.ERROR:
      return {
        ...state,
        type: types.ERROR,
        message: action.message
      }
    default:
      return initialState
  }
}
