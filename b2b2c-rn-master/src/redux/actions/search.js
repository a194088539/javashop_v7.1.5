/**
 * Created by Andste on 2018/10/24.
 */
import * as types from '../constants/search'

/**
 * 搜索关键字发生改变
 * @param keyword
 * @returns {{type : string, keyword : *}}
 */
export function searchKeywordChaned(keyword) {
  return {
    type: types.SEARCH_KEYWORD_CHANGED,
    keyword
  }
}

/**
 * 搜索排序发生改变
 * @param sort
 * @returns {{type : string, sort : *}}
 */
export function searchSortChaned(sort) {
  return {
    type: types.SEARCH_SORT_CHANGED,
    sort
  }
}

/**
 * 搜索分类ID发生改变
 * @param cat_id
 * @returns {{type : string, cat_id : *}}
 */
export function searchCatIdChanged(cat_id) {
  return {
    type: types.SEARCH_CAT_ID_CHANGED,
    cat_id
  }
}

/**
 * 商品页商品排列方式发生改变
 * @param view_type
 * @returns {{type : string, view_type : *}}
 */
export function searchViewTypeChanged(view_type) {
  return {
    type: types.SEARCH_VIEW_TYPE_CHANGED,
    view_type
  }
}
