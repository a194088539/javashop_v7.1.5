<template>
  <div class="floor-layout tpl-37">
    <div class="layout-main">
      <layout-item
        v-for="(block, index) in data.blockList"
        :key="index"
        :block="block"
        :is-mini-programe="isMiniPrograme"
        @handle-edit="handleEditBlock(index)"
      >
        <template slot-scope="{ block }">
          <div class="goods-item" v-if="block.block_value">
            <div class="image-goods">
              <img :src="block.block_value.goods_image">
            </div>
            <div class="name-goods" v-html="block.block_value.goods_name">{{ block.block_value.goods_name }}</div>
            <div class="price-goods">¥{{ block.block_value.goods_price | formatPrice }}</div>
          </div>
        </template>
      </layout-item>
    </div>
  </div>
</template>

<script>
  import mixin from '../mixin'

  export default {
    name: 'tpl_37',
    mixins: [mixin],
    title: '商品模块',
    dataTpl: {
      tpl_id: 37,
      blockList: [
        { block_type: 'GOODS', block_value: '', block_opt: '' },
        { block_type: 'GOODS', block_value: '', block_opt: '' }
      ]
    },
    filters: {
      formatPrice(price) {
        if (typeof price !== 'number') return price
        return String(Number(price).toFixed(2)).replace(/\B(?=(\d{3})+(?!\d))/g, ',')
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>

</style>
