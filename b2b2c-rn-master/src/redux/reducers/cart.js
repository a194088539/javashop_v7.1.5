/**
 * Created by Andste on 2018/11/15.
 */
import * as types from '../constants/cart'

const initialState = {
  // 店铺列表
  shopList: [],
  // 购物车总价
  cartPrice: {},
  // 货品总数量
  totalNum: 0,
  // 已选货品中数量
  selectedNum: 0
}

export default function cart(state = initialState, action) {
  switch (action.type) {
    // 获取购物车数据
    case types.GET_CART_DATA:
      return {
        ...state,
        shopList: organizeCartData(action.data['cart_list']),
        totalNum: countCartTotalNum(action.data['cart_list']),
        selectedNum: countCartTotalNum(action.data['cart_list'], true),
        cartPrice: action.data['total_price']
      }
    // 清空购物车
    case types.CLEAN_CART:
      return {
        shopList: [],
        cartPrice: {},
        totalNum: 0,
        selectedNum: 0
      }
    default:
      return state
  }
}

/**
 * 重组店铺数据，以符合SectionList组件
 * @param data
 * @returns {*}
 */
const organizeCartData = (data) => {
  return data.map(item => {
    item.key = item.seller_id
    item.data = item.sku_list.map(sku => {
      sku.key = sku.sku_id
      return sku
    })
    return item
  })
}

/**
 * 计算所有货品数量
 * @param data
 * @param selected
 * @returns {number}
 */
const countCartTotalNum = (data, selected) => {
  let num = 0
  data.forEach(shop => {
    shop.sku_list.forEach(sku => {
      if (sku['invalid'] !== 1) {
        if (selected) {
          sku.checked && (num += sku.num)
        } else {
          num += sku.num
        }
      }
    })
  })
  return num
}
