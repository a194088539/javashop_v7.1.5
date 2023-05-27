/**
 * 交易投诉相关API
 */

import request, { Method } from '@/utils/request'

/**
 * 获取投诉主题列表
 */
export function getComplaintTheme() {
  return request({
    url:'trade/order-complains/topics',
    method: Method.GET,
    needToken: true
  })
}

/**
 * 提交交易投诉
 */
export function appendComplaint(params) {
  return request({
    url:'trade/order-complains',
    method: Method.POST,
    needToken: true,
    data: params
  })
}

/**
 * 获取交易投诉列表
 * @param params
 */
export function getComplaintList(params) {
  return request({
    url: 'trade/order-complains',
    method: Method.GET,
    needToken: true,
    params
  })
}

/**
 * 撤销交易投诉
 * @param id
 */
export function cancelComplaint(id) {
  return request({
    url: `trade/order-complains/${id}`,
    method: Method.PUT,
    needToken: true,
  })
}

/**
 * 获取交易投诉详情
 * @param id
 */
export function getComplaintDetail(id) {
  return request({
    url: `trade/order-complains/${id}`,
    method: Method.GET,
    needToken: true,
  })
}

/**
 * 获取交易投诉详情
 * @param id
 */
export function getComplaintFlow(id) {
  return request({
    url: `trade/order-complains/${id}/flow`,
    method: Method.GET,
    needToken: true,
  })
}

/**
 * 发起对话
 * @param id
 * @param params
 */
export function initiationSession(id, params) {
  return request({
    url: `trade/order-complains/${id}/communication`,
    method: Method.PUT,
    needToken: true,
    params
  })
}

/**
 * 提交仲裁
 * @param id
 */
export function arbitrationProcess(id) {
  return request({
    url: `trade/order-complains/${id}/to-arbitration`,
    method: Method.PUT,
    needToken: true,
  })
}
