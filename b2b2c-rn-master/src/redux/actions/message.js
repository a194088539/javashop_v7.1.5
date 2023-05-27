/**
 * Created by Andste on 2018/10/11.
 */
import * as types from '../constants/message'

/**
 * 提示消息
 * @param message
 * @returns {{type : string, message : *}}
 */
export function info(message) {
  return {
    type: types.INFO,
    message
  }
}

/**
 * 成功消息
 * @param message
 * @returns {{type : string, message : *}}
 */
export function success(message) {
  return {
    type: types.SUCCESS,
    message
  }
}

/**
 * 警告消息
 * @param message
 * @returns {{type : string, message : *}}
 */
export function warning(message) {
  return {
    type: types.WARNING,
    message
  }
}

/**
 * 错误消息
 * @param message
 * @returns {{type : string, message : *}}
 */
export function error(message) {
  return {
    type: types.ERROR,
    message
  }
}
