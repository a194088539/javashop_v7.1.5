/**
 * 个人中心通用菜单
 * @type {*[]}
 */
let menus = [
  {
    title: '交易管理',
    name: 'transaction',
    children: [
      { title: '我的订单', name: 'my-order', path: 'my-order' },
      { title: '评论管理', name: 'my-comments', path: 'my-comments' },
      { title: '发表评论', name: 'comments', path: 'comments', hidden: true }
    ]
  },
  {
    title: '收藏中心',
    name: 'collection',
    children: [
      { title: '收藏的商品', name: 'goods-collection', path: 'goods-collection' },
      { title: '收藏的店铺', name: 'shop-collection', path: 'shop-collection?type=shop' },
      { title: '浏览记录', name: 'my-footprint', path: 'my-footprint' }
    ]
  },
  {
    title: '资产中心',
    name: 'assets',
    children: [
      { title: '优惠券', name: 'my-coupons', path: 'my-coupons' },
      { title: '积分', name: 'my-points', path: 'my-points' },
    ]
  },
  {
    title: '客户服务',
    name: 'service',
    children: [
      { title: '交易投诉', name: 'my-complaint', path: 'my-complaint' },
      { title: '售后管理', name: 'after-sale', path: 'after-sale', 'include': ['service-record', 'refund-record', 'apply-service', 'service-detail']  },
      { title: '售后服务记录', name: 'service-record', path: 'service-record', 'hidden': true },
      { title: '退款明细', name: 'refund-record', path: 'refund-record', 'hidden': true },
      { title: '申请售后', name: 'apply-service', path: 'apply-service', 'hidden': true },
      { title: '售后服务详情', name: 'service-detail', path: 'service-detail', 'hidden': true },
      { title: '我的发票', name: 'my-receipt', path: 'my-receipt' },
      { title: '购买咨询', name: 'my-consultation', path: 'my-consultation', 'include': ['my-answer'] },
      { title: '我的回答', name: 'my-answer', path: 'my-answer', 'hidden': true },
      { title: '站内消息', name: 'message-index', path: 'message-index', 'include': ['website-message', 'ask-message']  },
      { title: '系统消息', name: 'website-message', path: 'website-message', 'hidden': true },
      { title: '问答消息', name: 'ask-message', path: 'ask-message', 'hidden': true }
    ]
  },
  {
    title: '账户管理',
    name: 'account',
    children: [
      { title: '个人信息', name: 'my-profile', path: 'my-profile' },
      { title: '收货地址', name: 'shipping-address', path: 'shipping-address' },
      { title: '账户安全', name: 'account-safe', path: 'account-safe', 'include': ['change-password', 'change-mobile', 'bind-mobile'] },
      { title: '修改密码', name: 'change-password', path: 'change-password', 'hidden': true },
      { title: '修改手机', name: 'change-mobile', path: 'change-mobile', 'hidden': true },
      { title: '账号绑定', name: 'account-binding', path: 'account-binding' },
      { title: '增票资质', name: 'increase-ticket', path: 'increase-ticket' }
    ]
  }
];

/**
 * 个人中心分销菜单
 * @type {*[]}
 */
const dis_menus = [
  {
    title: '分销管理',
    name: 'distribution',
    children: [
      { title: '分销管理', name: 'distribution', path: 'distribution/my-recommend', 'hidden': true },
      { title: '我推荐的人', name: 'my-recommend', path: 'distribution/my-recommend' },
      { title: '我的业绩', name: 'my-performance', path: 'distribution/my-performance', 'include': ['distribution-my-performance-refund', 'distribution-my-performance-history'] },
      { title: '与我相关的退货单', name: 'my-performance-refund', path: 'distribution/my-performance-refund', 'hidden': true },
      { title: '我的历史业绩', name: 'my-performance-history', path: 'distribution/my-performance-history', 'hidden': true },
      { title: '提现设置', name: 'withdrawals-setting', path: 'distribution/withdrawals-setting' },
      { title: '提现申请', name: 'withdrawals-apply', path: 'distribution/withdrawals-apply' },
      { title: '提现记录', name: 'withdrawals-history', path: 'distribution/withdrawals-history' }
    ]
  }
];

if (process.env.distribution) {
  menus = menus.concat(dis_menus)
}

export default menus
