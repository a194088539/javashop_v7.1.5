/**
 * 咨询管理API
 */

import request from '@/utils/request'

/**
 * 获取咨询列表
 * @param params
 * @returns {Promise<any>}
 */
export function getConsultationList(params) {
  return request({
    url: 'seller/members/asks',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 回复咨询
 * @param ask_id
 * @param reply_content
 * @returns {Promise<any>}
 */
export function replyConsultation(ask_id, reply_content) {
  return request({
    url: `seller/members/asks/${ask_id}/reply`,
    method: 'put',
    loading: false,
    data: {
      reply_content
    }
  })
}

/**
 * 获取会员咨询详情
 * @param params
 */
export function getMemberAsksDetail(ask_id) {
  return request({
    url: `seller/members/asks/${ask_id}`,
    method: 'get'
  })
}

/**
 * 获取会员咨询回复列表
 * @param params
 */
export function getMemberAsksReply(params) {
  return request({
    url: 'seller/members/reply',
    method: 'get',
    loading: false,
    params
  })
}
