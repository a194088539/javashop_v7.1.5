<template>
  <div id="goods-specs">
    <van-cell-group class="specs-cells">
      <van-cell is-link @click="showPopup = true">
        <div slot="title">
          <span>已选</span>
          <span class="spec-vals">{{ selectedSku ? (specList.length ? selectedSpecVals.join('-') : '默认') : '未选规格' }} {{ buyNum }}件</span>
        </div>
      </van-cell>
    </van-cell-group>
    <van-popup v-model="showPopup" position="bottom" style="height:70%">
      <div class="spec-headr">
        <div class="pic-header">
          <img :src="selectedSpecImg.thumbnail || selectedSku.thumbnail || goods.thumbnail">
        </div>
        <i class="iconfont ea-icon-close" @click="showPopup = false"/>
        <div v-if="promotions_price" class="price-header price">{{ promotions_price }}</div>
        <div v-else class="price-header price">￥{{ selectedSku.price | unitPrice }}</div>
        <div class="sn-header">
          <span>商品编号：</span>
          <span>{{ selectedSku.sn }}</span>
        </div>
      </div>
      <div class="spec-body">
        <div class="spec-sel">
          <span class="tit-spec-body">已选</span>
          <span class="prod-spec-info">{{ selectedSku ? selectedSpecVals.join('-') : '未选规格' }}</span>
          <span class="count-spec-body">{{ buyNum }}件</span>
        </div>
        <div class="spec-list">
          <div class="val-spec" v-for="(spec, specIndex) in specList" :key="specIndex">
            <span class="tit-spec-body">{{ spec.spec_name }}</span>
            <p class="p-spec-body">
              <template v-for="spec_val in spec.valueList" >
                <a
                  :key="spec_val.spec_value_id"
                  v-if="canSelected(specIndex, specList, spec_val)"
                  :class="[spec_val.selected && 'selected', spec.spec_type === 1 && 'spec-image']"
                  @click="handleClickSpec(spec, specIndex, spec_val)"
                  class="item-spec-value"
                >{{ spec_val.spec_value }}</a>
              </template>
            </p>
          </div>
        </div>
        <div class="spec-num">
          <span class="tit-spec-body">数量</span>
          <div class="quantity-wrapper">
            <a class="quantity-decrease" :class="[buyNum < 2 && 'limited']" @click="handleBuyNumChanged('-')"><em class="minus">-</em></a>
            <input type="number" v-model="buyNum" class="quantity number" @change="handleBuyNumChanged(parseInt(buyNum))">
            <a class="quantity-increase" @click="handleBuyNumChanged('+')"><em class="plus">+</em></a>
          </div>
        </div>
        <div></div>
      </div>
      <div class="spec-footer">
        <a
          v-if="goods.is_auth === 0 || goods.goods_off === 0"
          class="buy-btn buy-disabled"
        >{{ goods.is_auth === 0 ? '商品审核中' : '商品已下架' }}</a>
        <template v-else>
          <a class="buy-btn add-cart" @click="$emit('add-cart')">加入购物车</a>
          <a class="buy-btn direct-order" @click="$emit('buy-now')">立即购买</a>
        </template>
      </div>
    </van-popup>
  </div>
</template>

<script>
  import * as API_Goods from '@/api/goods'
  import * as API_Promotions from '@/api/promotions'
  import { Foundation } from '@/ui-utils'
  export default {
    name: 'goods-specs',
    props: ['goods', 'show', 'promotions'],
    data() {
      return {
        showPopup: false,
        // 购买数量
        buyNum: 1,
        // skuMap
        skuMap: new Map(),
        // 规格列表
        specList: [],
        // 被选中规格
        selectedSpec: [],
        selectedSpecVals: [],
        // 规格组合列表
        skusCombination: [],
        // 被选中sku
        selectedSku: '',
        // 被选中的规格图片【如果有】
        selectedSpecImg: '',
        // 没有选中sku，初始化为false
        unselectedSku: false
      }
    },
    mounted() {
      // 如果是拼团的 获取此商品参与拼团的sku规格列表
      Promise.all([
        this.$route.query.sku_id && this.$route.query.pintuan_id && this.$route.query.from_nav === 'assemble' ?
          API_Promotions.getAssembleSkuList(this.goods.goods_id, this.$route.query.pintuan_id):
          API_Goods.getGoodsSkus(this.goods.goods_id)
      ]).then(responses => {
        const response = responses[0]
        const specList = []
        response.forEach((sku, skuIndex) => {
          const { spec_list } = sku
          const spec_value_ids = []
          let _combination = []
          if (spec_list) {
            spec_list.forEach((spec, specIndex) => {
              const _specIndex = specList.findIndex(_spec => _spec['spec_id'] === spec.spec_id)
              const _spec = {
                spec_id: spec.spec_id,
                spec_name: spec.spec_name,
                spec_type: spec.spec_type
              }
              const _value = {
                spec_value: spec.spec_value,
                spec_value_id: spec.spec_value_id,
                spec_value_index: skuIndex,
                spec_value_img: {
                  original: spec.spec_image,
                  thumbnail: spec.thumbnail
                }
              }
              _combination.push({..._value})
              spec_value_ids.push(spec.spec_value_id)
              if(_specIndex === -1){
                specList.push({..._spec, valueList: [{..._value}]})
              }else if(specList[_specIndex]['valueList'].findIndex(_value => _value['spec_value_id'] === spec['spec_value_id']) === -1) {
                specList[_specIndex]['valueList'].push({ ..._value })
              }
            })
            this.skusCombination.push(_combination)
            this.skuMap.set(spec_value_ids.join('-'), sku)
          } else {
            this.skuMap.set('no_spec', sku)
          }
        })
        this.specList = specList
        // 如果有sku信息，初始化已选规格
        if (this.$route.query.sku_id) {
          this.initSpec()
        }
        // 如果没有规格，把商品第一个可选sku给已选择sku
        if (!specList.length) {
          this.selectedSku = this.skuMap.get('no_spec')
        }
      })
    },
    computed: {
      // 如果有活动【限时抢购、团购、积分兑换】
      // 无论什么规格，都只展示活动价格
      promotions_price() {
        const proList = ['exchange', 'groupbuy_goods_vo', 'seckill_goods_vo']
        const { promotions } = this
        if (!promotions) return null
        let promotion = null
        for (let i = 0; i < proList.length; i++) {
          const pro = promotions.filter(item => item[proList[i]])
          if (pro[0]) {
            promotion = pro[0]
            break
          }
        }
        if (promotion) {
          // 如果是积分兑换
          if (promotion['exchange']) {
            const pro = promotion['exchange']
            if (pro['exchange_money'] === 0) return `${pro['exchange_point']}积分`
            return `￥${Foundation.formatPrice(pro['exchange_money'])}+${pro['exchange_point']}积分`
          }
          // 如果是团购
          if (promotion['groupbuy_goods_vo']) {
            const pro = promotion['groupbuy_goods_vo']
            return `￥${Foundation.formatPrice(pro['price'])}`
          }
          // 如果是限时抢购
          if (promotion['seckill_goods_vo']) {
            const pro = promotion['seckill_goods_vo']
            return `￥${Foundation.formatPrice(pro['seckill_price'])}`
          }
        }
        return null
      }
    },
    methods: {
      /** 初始化规格 */
      initSpec() {
        let { sku_id } = this.$route.query
        let selectedSpecs = ''
        if (sku_id) {
          sku_id = Number(sku_id)
          this.skuMap.forEach((value, key) => {
            if (value.sku_id === sku_id) {
              selectedSpecs = key.split('-')
            }
          })
        }
        const _selectedSpecVals = []
        this.specList.forEach((spec, specIndex)=> {
          if (Array.isArray(spec.valueList)) {
            spec.valueList.forEach((val, specValIndex) => {
              if (selectedSpecs) {
                const spec_value_id = val.spec_value_id
                if (selectedSpecs.indexOf(String(spec_value_id)) !== -1) {
                  val.selected = true
                  this.selectedSpec[specIndex] = val.spec_value_id
                  _selectedSpecVals.push(val.spec_value)
                }
              } else if (specValIndex === 0) {
                val.selected = true
                this.selectedSpec[specIndex] = val.spec_value_id
                _selectedSpecVals.push(val.spec_value)
              }
            })
          }
        })
        this.$set(this, 'selectedSpecVals', _selectedSpecVals)
        this.handleSelectedSku()
      },
      /** 选择规格 */
      handleClickSpec(spec, specIndex, spec_val) {
        if (spec.spec_type === 1 ) {
          this.selectedSpecImg = JSON.parse(JSON.stringify(spec_val.spec_value_img))
        }
        if (spec_val.selected) return
        spec.valueList.map(item => {
          item.selected = item.spec_value_id === spec_val.spec_value_id
          return item
        })
        this.$set(this.specList, specIndex, spec)
        this.selectedSpec[specIndex] = spec_val.spec_value_id
        this.selectedSpecVals[specIndex] = spec_val.spec_value
        this.handleSelectedSku()
      },
      /** 是否可选 */
      canSelected(specIndex, specList, spec_val) {
        if (specIndex) {
          const selectedList = specList[--specIndex].valueList.filter(key => key.selected)
          if (selectedList.length) {
            const spec_id = selectedList[0].spec_value_id
            const _ids = []
            this.skusCombination.forEach(key => {
              key.forEach(item => {
                if (item.spec_value_id === spec_id) {
                  _ids.push([...key])
                }
              })
            })
            return _ids.findIndex(key => (key.findIndex(_spec => _spec.spec_value_id === spec_val.spec_value_id) !== -1) === true) !== -1
          }
          return true
        }
        return true
      },
      /** 购买数量增加减少 */
      handleBuyNumChanged(symbol) {
        if (symbol === '+') {
          const { enable_quantity } = this.selectedSku
          if (enable_quantity === 0) {
            this.$message.error('该规格暂时缺货！')
          } else if (this.buyNum >= enable_quantity) {
            this.$message.error('超过最大库存！')
          } else {
            this.buyNum += 1
          }
        } else if(symbol === '-'){
          if (this.buyNum < 2) {
            this.$message.error('不能再少啦！')
          } else {
            this.buyNum -= 1
          }
        }else{
          const { enable_quantity } = this.selectedSku
          if (isNaN(symbol) && typeof(symbol) === 'number' || symbol < 1) {
            this.$message.error('输入格式有误，请输入正整数！')
            return
          }else if (symbol >= enable_quantity ){
            this.$message.error('超过最大库存！')
            return
          }else{
            this.buyNum = symbol
          }
        }
      },
      /** 根据已选规格选出对应的sku */
      handleSelectedSku() {
        let sku
        if (this.selectedSpec.length) {
          const spec_vals = []
          this.selectedSpec.forEach(item => spec_vals.push(item))
          sku = this.skuMap.get(spec_vals.join('-'))
        } else {
          sku = this.skuMap.get('no_spec')
        }
        if (sku) {
          this.selectedSku = sku
          this.unselectedSku = false
          this.goodsInfo = { ...this.goodsInfo, ...sku }
          this.buyNum = sku.enable_quantity === 0 ? 0 : 1
        }
      }
    },
    watch: {
      show: function(newVal) {
        if (newVal) this.showPopup = newVal
      },
      showPopup: function(newVal) {
        if (!newVal) this.$emit('close')
      },
      selectedSku: {
        handler: function (newVal) {
          this.$emit('sku-changed', newVal)
        },
        deep: true
      },
      buyNum: function (newVal) {
        this.$emit('num-changed', newVal)
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../assets/styles/color";
  /deep/ {
    .van-popup { overflow: visible }
    .spec-vals {
      margin-left: 5px;
      font-size: 12px;
      color: #252525;
    }
  }
  .spec-headr {
    position: relative;
    width: 100%;
    height: 84px;
    &::after {
      position: absolute;
      content: '';
      transform: scaleY(.5);
      bottom: -1px;
      left: 0;
      width: 100%;
      height: 1px;
      border-bottom: 1px solid #e3e5e9;
      z-index: 10;
    }
    .pic-header {
      position: absolute;
      left: 8px;
      top: -26px;
      width: 100px;
      height: 100px;
      border-radius: 3px;
      border: 1px solid #f0f1f3;
      background-color: #fff;
      overflow: hidden;
      display: -webkit-box;
      -webkit-box-pack: center;
      -webkit-box-align: center;
      img {
        width: 100%;
        height: 100%;
        overflow: hidden;
      }
    }
    .ea-icon-close {
      position: absolute;
      top: 0;
      right: 0;
      width: 44px;
      height: 44px;
      text-align: center;
      line-height: 44px;
    }
    .price-header {
      font-size: 15px;
      line-height: 15px;
      padding-top: 42px;
      padding-left: 121px;
      font-weight: 700;
    }
    .sn-header {
      font-size: 11px;
      line-height: 11px;
      color: #81838e;
      padding-top: 8px;
      padding-left: 121px;
    }
  }
  .spec-body {
    padding: 0 10px;
    height: calc(100% - 84px - 43px);
    overflow-x: hidden;
    overflow-y: scroll;
    .spec-sel {
      padding-top: 15px;
      padding-bottom: 15px;
      .prod-spec-info {
        margin-left: 5px;
        margin-right: 10px;
        font-size: 13px;
        color: #252525;
        line-height: 18px;
      }
      .count-spec-body {
        margin-left: 5px;
      }
    }
    .spec-list {
      .val-spec {
        margin: 13px 0;
        font-size: 0;
        overflow: hidden;
        position: relative;
      }
      .p-spec-body {
        margin-left: 34px;
        overflow: hidden;
      }
      .item-spec-value {
        height: 27px;
        border: 1px solid #bfbfbf;
        border-radius: 4px;
        line-height: 25px;
        font-size: 13px;
        color: #232326;
        float: left;
        margin-right: 10px;
        padding-left: 20px;
        padding-right: 20px;
        margin-bottom: 10px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        min-width: 69px;
        max-width: 98%;
        box-sizing: border-box;
        text-align: center;
        &.selected {
          border: 1px solid $color-main;
          color: $color-main;
        }
      }
    }
    .spec-num {
      .quantity-wrapper {
        display: inline-block;
        height: 26px;
        border-radius: 5px;
        margin-left: 8px;
        float: left;
        line-height: 26px;
        em {
          background: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAMgAAADICAYAAACtWK6eAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyZpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMDY3IDc5LjE1Nzc0NywgMjAxNS8wMy8zMC0yMzo0MDo0MiAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTUgKFdpbmRvd3MpIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkZEQzRDOUU4MDdGODExRTc4OEUyRDhCMEM1RTg0Mjg4IiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkZEQzRDOUU5MDdGODExRTc4OEUyRDhCMEM1RTg0Mjg4Ij4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6RkRDNEM5RTYwN0Y4MTFFNzg4RTJEOEIwQzVFODQyODgiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6RkRDNEM5RTcwN0Y4MTFFNzg4RTJEOEIwQzVFODQyODgiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz52M0gxAABAH0lEQVR42uxd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") no-repeat;
          background-size: 100px 100px;
          height: 12px;
          width: 12px;
          display: block;
          margin: 6px;
        }
      }
      .quantity-decrease, .quantity-increase {
        background: #fff;
        border: 1px solid #cbcbcb;
        color: #232326;
        display: block;
        height: 24px;
        line-height: 24px;
        width: 26px;
        overflow: hidden;
        text-indent: -200px;
        box-sizing: border-box;
      }
      .quantity {
        color: #232326;
        border: solid #cbcbcb;
        border-width: 1px 0 1px 0;
        height: 24px;
        line-height: 24px;
        width: 34px;
        border-radius: 0;
        -webkit-appearance: none;
        box-sizing: border-box;
      }
      .quantity-decrease, .quantity-increase, .quantity {
        float: left;
        font-size: 15px;
        text-align: center;
      }
      .quantity-decrease {
        em {
          background-position: -32px -85px;
        }
        &.limited em {
          background-position: -45px -85px;
        }
      }
      .quantity-increase {
        em {
          background-position: -32px -67px;
        }
        &.limited em {
          background-position: -45px -67px;
        }
      }
    }
    .tit-spec-body {
      float: left;
      line-height: 18px;
      font-size: 13px;
      color: #81838e;
      width: 26px;
      white-space: normal;
      word-break: break-all;
      height: auto;
    }
  }
  .spec-footer {
    display: flex;
    height: 43px;
    .buy-btn {
      display: block;
      height: 43px;
      width: 1%;
      flex: 1;
      font-size: 15px;
      line-height: 43px;
      color: #fff;
      text-align: center;
    }
    .add-cart {
      background-color: #ffb03f;
    }
    .direct-order {
      background-color: #f23030;
    }
  }
</style>
