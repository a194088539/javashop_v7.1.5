/**
 * 订单相关API
 */

import request from '@/utils/request'

export function queryPrint(params) {
  return request({
    url: '/seller/waybill',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 获取订单列表
 * @param params
 * @returns {Promise<any>}
 */
export function getOrderList(params) {
  return request({
    url: 'seller/trade/orders',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 根据订单sn获取订单详情
 * @param sn
 * @returns {Promise<any>}
 */
export function getOrderDetail(sn) {
  return request({
    url: `seller/trade/orders/${sn}`,
    method: 'get',
    loading: false
  })
}

/**
 * 调整价格
 * @param sn
 * @returns {Promise<any>}
 */
export function updateOrderPrice(sn, params) {
  return request({
    url: `seller/trade/orders/${sn}/price`,
    method: 'put',
    loading: false,
    data: params
  })
}

/**
 * 修改收货人信息
 * @param sn
 * @param params
 * @returns {Promise<any>}
 */
export function updateConsigneeInfo(sn, params) {
  return request({
    url: `seller/trade/orders/${sn}/address`,
    method: 'put',
    loading: false,
    data: params
  })
}

/**
 * 确认收款
 * @param sn
 * @param params
 * @returns {Promise<any>}
 */
export function confirmGetAmount(sn, params) {
  return request({
    url: `seller/trade/orders/${sn}/pay`,
    method: 'post',
    loading: false,
    data: params
  })
}

/**
 * 发货
 * @param ids
 * @param params
 * @returns {Promise<any>}
 */
export function deliveryGoods(sn, params) {
  return request({
    url: `seller/trade/orders/${sn}/delivery`,
    method: 'post',
    loading: false,
    data: params
  })
}

/**
 * 查询快递物流信息
 * @param params
 * @returns {Promise<any>}
 */
export function getLogisticsInfo(params) {
  return request({
    url: `seller/express`,
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 生成电子面单
 * @param params
 * @returns {Promise<any>}
 */
export function generateElectronicSurface(params) {
  return request({
    url: `seller/waybill`,
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 批量生成电子面单
 * @param params
 * @returns {Promise<any>}
 */
export function generateWaybill(params) {
  return request({
    url: `seller/waybill/creates`,
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 获取订单流程图数据
 * @param ids
 * @param params
 * @returns {Promise<any>}
 */
export function getStepList(ids) {
  return request({
    url: `seller/trade/orders/${ids}/flow`,
    method: 'get',
    loading: false
  })
}

/**
 * 导出订单
 * @param params
 */
export function exportOrders(params) {
  return request({
    url: 'seller/trade/orders/export',
    method: 'get',
    params
  })
}

/**
 * 获取发货单
 * @param params
 * @returns {Promise<any>}
 */
export function getInvoiceList(params) {
  return request({
    url: 'seller/trade/invoice',
    method: 'get',
    params
  })
}

/**
 * 取消订单
 * @param order_sn 订单编号
 * @param reason   取消原因
 */
export function cancelOrder(order_sn, reason) {
  return request({
    url: `seller/trade/orders/${order_sn}/cancel`,
    method: 'post',
    data: { reason }
  })
}
