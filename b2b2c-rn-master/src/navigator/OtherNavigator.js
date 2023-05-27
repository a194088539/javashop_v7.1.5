/**
 * Created by Andste on 2018/9/30.
 */
// 商品详情
import Goods from '../scenes/Goods'
// 商品列表
import GoodsList from '../scenes/GoodsList'
// 购物车【非root】
import CartScene from '../scenes/Cart/CartScene'
// 购物车领券
import CartCoupon from '../scenes/Cart/CartCouponScene'
// HTML 显示
import HtmlView from '../scenes/HtmlView/HtmlView'
// 认证相关【登录、注册】
import * as Auth from '../scenes/Auth'
// 我的相关【我的积分、优惠券、收藏...】
import * as Mines from '../scenes/MiceScenes'
// 设置相关【账户安全、关于...】
import * as Settings from '../scenes/Settings'
// 促销活动相关页面
import * as Promotion from '../scenes/Promotion'
// 其它公共页面
import * as Common from '../scenes/Common'
// 店铺相关页面
import * as Shop from '../scenes/Shop'
// 安全相关页面
import * as Safe from '../scenes/SafeScenes'
// 结算相关页面
import * as Checkout from '../scenes/Checkout'

export default {
  Goods,
  GoodsList,
  CartScene,
  CartCoupon,
  HtmlView,
  ...Auth,
  ...Mines,
  ...Settings,
  ...Promotion,
  ...Common,
  ...Shop,
  ...Safe,
  ...Checkout
}
