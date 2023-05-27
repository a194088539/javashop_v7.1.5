/**
 * 售后服务API
 */

import request from '@/utils/request'

/**
 * 获取售后服务记录相关数据
 * @param params 参数
 */
export function getAfterSaleList(params) {
  return request({
    url: `admin/after-sales`,
    method: 'get',
    params
  })
}

/**
 * 获取售后服务详情数据
 * @param service_sn 售后服务单编号
 */
export function getServiceDetail(service_sn) {
  return request({
    url: `admin/after-sales/detail/${service_sn}`,
    method: 'get'
  })
}

/**
 * 平台退款操作
 * @param service_sn 售后服务单编号
 * @param params 提交参数集合
 */
export function adminRefund(service_sn, params) {
  return request({
    url: `admin/after-sales/refund/${service_sn}`,
    method: 'post',
    params
  })
}

/**
 * 获取退款单记录相关数据
 * @param params 参数
 */
export function getRefundList(params) {
  return request({
    url: `admin/after-sales/refund`,
    method: 'get',
    needToken: true,
    params
  })
}

/**
 * 导出售后服务数据
 * @param params 参数
 */
export function exportAfterSale(params) {
  return request({
    url: `admin/after-sales/export`,
    method: 'get',
    needToken: true,
    params
  })
}
