/**
 * 交易投诉API
 */

import request from '@/utils/request'

/**
 * 获取投诉列表
 * @param params
 */
export function getComplainList(params) {
  return request({
    url: 'seller/trade/order-complains',
    method: 'get',
    params
  })
}

/**
 * 获取投诉详情
 * @param id
 */
export function getComplainDetail(id) {
  return request({
    url: `seller/trade/order-complains/${id}`,
    method: 'get'
  })
}

/**
 * 查询一个交易投诉的流程图
 * @param id
 */
export function getComplaintFlow(id) {
  return request({
    url: `seller/trade/order-complains/${id}/flow`,
    method: 'get'
  })
}

/**
 * 提交商家申诉
 * @param id
 * @param params
 */
export function submitAppeal(id, params) {
  return request({
    url: `seller/trade/order-complains/${id}/appeal`,
    method: 'put',
    params
  })
}

/**
 * 发起对话
 * @param id
 * @param params
 */
export function initiationSession(id, params) {
  return request({
    url: `seller/trade/order-complains/${id}/communication`,
    method: 'put',
    params
  })
}

/**
 * 提交仲裁
 * @param id
 */
export function arbitrationProcess(id) {
  return request({
    url: `seller/trade/order-complains/${id}/to-arbitration`,
    method: 'put'
  })
}
