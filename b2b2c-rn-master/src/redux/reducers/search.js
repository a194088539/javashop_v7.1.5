/**
 * Created by Andste on 2018/10/24.
 */
import * as types from '../constants/search'

// 猝死状态
const initialState = {
  type: 'init',
  cat_id: null,
  keyword: '',
  sort: 'def_desc',
  view_type: 'single'
}

export default function search(state = initialState, action) {
  switch (action.type) {
    // 搜索关键字发生改变
    case types.SEARCH_KEYWORD_CHANGED:
      return {
        ...state,
        type: action.type,
        keyword: action.keyword,
        cat_id: null
      }
    // 搜索排序发生改变
    case types.SEARCH_SORT_CHANGED:
      return {
        ...state,
        type: action.type,
        sort: action.sort
      }
    // 搜索分类发生改变
    case types.SEARCH_CAT_ID_CHANGED:
      return {
        ...state,
        type: action.type,
        cat_id: action.cat_id,
        keyword: ''
      }
    // 商品页商品排列方式发生改变
    case types.SEARCH_VIEW_TYPE_CHANGED:
      return {
        ...state,
        view_type: action.view_type
      }
    default:
      return state
  }
}
