/**
 * 敏感词相关API
 */

import request from '@/utils/request'

/**
 * 获取敏感词列表
 * @param params
 */
export function getSensitiveWordsList(params) {
  return request({
    url: 'admin/sensitive-words',
    method: 'get',
    loading: false,
    params
  })
}

/**
 * 添加敏感词
 * @param params
 */
export function addSensitiveWords(params) {
  return request({
    url: 'admin/sensitive-words',
    method: 'POST',
    data: params
  })
}

/**
 * 删除敏感词
 * @param params
 */
export function deleSensitiveWords(id) {
  return request({
    url: `admin/sensitive-words/${id}`,
    method: 'DELETE'
  })
}
