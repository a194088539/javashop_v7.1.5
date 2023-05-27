/**
 * 售后服务相关API
 */

import request from '@/utils/request'

/**
 * 获取售后服务记录相关数据
 * @param params 参数
 */
export function getAfterSaleList(params) {
  return request({
    url: `seller/after-sales`,
    method: 'get',
    needToken: true,
    params
  })
}

/**
 * 获取售后服务详情数据
 * @param service_sn 售后服务单编号
 */
export function getServiceDetail(service_sn) {
  return request({
    url: `seller/after-sales/detail/${service_sn}`,
    method: 'get',
    needToken: true
  })
}

/**
 * 审核售后服务申请
 * @param service_sn 售后服务单编号
 * @param params 提交参数集合
 */
export function auditAfterSaleService(service_sn, params) {
  return request({
    url: `seller/after-sales/audit/${service_sn}`,
    method: 'post',
    needToken: true,
    params
  })
}

/**
 * 售后商品入库
 * @param params 提交参数集合
 */
export function goodsPutInStock(params) {
  return request({
    url: `seller/after-sales/put-in/warehouse`,
    method: 'post',
    needToken: true,
    headers: { 'Content-Type': 'application/json' },
    data: params
  })
}

/**
 * 获取退款单记录相关数据
 * @param params 参数
 */
export function getRefundList(params) {
  return request({
    url: `seller/after-sales/refund`,
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
    url: `seller/after-sales/export`,
    method: 'get',
    needToken: true,
    params
  })
}

/**
 * 商家退款操作
 * @param service_sn 售后服务单编号
 */
export function sellerRefund(service_sn) {
  return request({
    url: `seller/after-sales/refund/${service_sn}`,
    method: 'post',
    needToken: true
  })
}

/**
 * 商家创建新订单操作
 * @param service_sn 售后服务单编号
 */
export function sellerCreateOrder(service_sn) {
  return request({
    url: `seller/after-sales/create-order/${service_sn}`,
    method: 'post',
    needToken: true
  })
}

/**
 * 商家关闭售后服务单操作
 * @param service_sn 售后服务单编号
 * @param closeReason 关闭原因
 */
export function closeAfterSale(service_sn, closeReason) {
  return request({
    url: `seller/after-sales/close/${service_sn}`,
    method: 'post',
    needToken: true,
    data: {
      reason: closeReason
    }
  })
}
