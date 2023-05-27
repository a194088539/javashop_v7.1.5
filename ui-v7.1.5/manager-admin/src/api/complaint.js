/**
 * 分类管理API
 */
import request from '@/utils/request'

/**
 * 获取投诉主题列表
 * @param params
 */
export function getComplainSubjectList(params) {
  return request({
    url: 'admin/systems/complain-topics',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 添加投诉主题
 * @param params
 */
export function addComplainSubject(params) {
  return request({
    url: 'admin/systems/complain-topics',
    method: 'post',
    params
  })
}

/**
 * 删除投诉主题
 * @param params
 */
export function deleteComplainSubject(id) {
  return request({
    url: `admin/systems/complain-topics/${id}`,
    method: 'DELETE'
  })
}

/**
 * 修改投诉主题
 * @param params
 */
export function modifyComplainSubject(id, params) {
  return request({
    url: `admin/systems/complain-topics/${id}`,
    method: 'PUT',
    params
  })
}

/**
 * 获取投诉订单列表
 * @param params
 */
export function getComplainList(params) {
  return request({
    url: 'admin/trade/order-complains',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 获取投诉订单的详细信息
 * @param id
 */
export function getComplainInfo(id) {
  return request({
    url: `admin/trade/order-complains/${id}`,
    method: 'get',
    loading: false
  })
}

/**
 *审核并交由商家申诉
 * @param id
 */
export function examineComplain(id) {
  return request({
    url: `admin/trade/order-complains/${id}/to-appeal`,
    method: 'put'
  })
}

/**
 *直接仲裁结束流程
 * @param id
 * @param params
 */
export function arbitrationProcess(id, params) {
  return request({
    url: `admin/trade/order-complains/${id}/complete`,
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
    url: `admin/trade/order-complains/${id}/communication`,
    method: 'put',
    params
  })
}
