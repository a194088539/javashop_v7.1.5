<template>
  <div>
    <en-table-layout
      toolbar
      pagination
      :tableData="tableData"
      :height="700"
      :loading="loading">
      <div slot="toolbar" class="inner-toolbar">
        <div class="toolbar-btns">
          <!--商品类型-->
          <div v-if="parseInt(shopInfo.self_operated) === 1" class="conditions">
            <span>商品类型：</span>
            <el-select
              class="choose-machine"
              v-model="params.goods_type"
              placeholder="请选择商品类型"
              @change="changeGoodsType"
              clearable>
              <el-option key="NORMAL" label="正常商品" value="NORMAL"/>
              <el-option key="POINT" label="积分商品" value="POINT"/>
            </el-select>
          </div>
        </div>
        <div class="toolbar-search">
          <en-table-search @search="searchEvent" placeholder="请输入商品名称或编号"/>
        </div>
      </div>
      <template slot="table-columns">
        <el-table-column label="图片" width="120">
          <template slot-scope="scope">
            <a :href="`${MixinBuyerDomain}/goods/${scope.row.goods_id}`" target="_blank">
              <img :src="scope.row.thumbnail" class="goods-image"/>
            </a>
          </template>
        </el-table-column>
        <el-table-column label="商品编号" prop="sn" width="80"/>
        <el-table-column label="商品名称">
          <template slot-scope="scope">
            <a :href="`${MixinBuyerDomain}/goods/${scope.row.goods_id}`" target="_blank" style="color: #00a2d4;" v-html="scope.row.goods_name">{{ scope.row.goods_name }}</a>
          </template>
        </el-table-column>
        <el-table-column label="价格" width="120">
          <template slot-scope="scope">{{ scope.row.price | unitPrice('￥') }}</template>
        </el-table-column>
        <el-table-column label="库存" width="80">
          <template slot-scope="scope">{{ scope.row.quantity }}件</template>
        </el-table-column>
        <el-table-column label="可用库存" width="80">
          <template slot-scope="scope">{{ scope.row.enable_quantity }}件</template>
        </el-table-column>
        <el-table-column label="创建时间" width="120">
          <template slot-scope="scope">{{ scope.row.create_time | unixToDate('yyyy-MM-dd hh:mm') }}</template>
        </el-table-column>
        <el-table-column  label="状态" width="80">
          <template slot-scope="scope">
            <span>{{ scope.row | marketStatus }}</span>
            <div class="under-reason" v-if="scope.row.market_enable === 0" @click="showUnderReason(scope.row)">(下架原因)</div>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="300" style="text-align: left;">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="success"
              @click="handleEditGoods(scope.row)">编辑
            </el-button>
          </template>
        </el-table-column>
      </template>
      <el-pagination
        slot="pagination"
        v-if="pageData"
        @size-change="handlePageSizeChange"
        @current-change="handlePageCurrentChange"
        :current-page="pageData.page_no"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pageData.page_size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pageData.data_total">
      </el-pagination>
    </en-table-layout>
  </div>
</template>

<script>
  import * as API_goods from '@/api/goods'
  import { RegExp } from '~/ui-utils'
  export default {
    name: 'authGoodsList',
    data() {
      return {
        /** 列表loading状态 */
        loading: false,

        /** 列表参数 */
        params: {
          page_no: 1,
          page_size: 10,
          // 商品类型 默认为'' NORMAL 正常商品 POINT 积分商品
          goods_type: '',
          market_enable: 1,
          is_auth: 0,
          ...this.$route.query
        },

        /** 列表数据 */
        tableData: [],

        /** 列表分页数据 */
        pageData: [],

        /** 商品状态列表 */
        goodsStatusList: [
          { value: 0, label: '未出售（已下架）' },
          { value: 1, label: '出售中（已上架）' }
        ],

        /** 当前商品id*/
        goodsId: '',

        /** 要合并的列的位置数组 */
        concactArray: [],

        /** 店铺信息 */
        shopInfo: this.$store.getters.shopInfo
      }
    },
    filters: {
      /** 销售状态格式化 */
      marketStatus(row) {
        switch (row.is_auth) {
          case 0 : return row.market_enable === 1 ? '待审核' : '已下架'
          case 1 : return row.market_enable === 1 ? '售卖中' : '已下架'
          case 2 : return row.market_enable === 1 ? '审核拒绝' : '已下架'
        }
      }
    },
    mounted() {
      this.params = { ...this.params, ...this.$route.query }
      this.params.market_enable && (this.params.market_enable = parseInt(this.params.market_enable))
      this.GET_GoodsList()
    },
    activated() {
      this.params = { ...this.params, ...this.$route.query }
      this.params.market_enable && (this.params.market_enable = parseInt(this.params.market_enable))
      this.GET_GoodsList()
    },
    methods: {

      /** 分页大小发生改变 */
      handlePageSizeChange(size) {
        this.params.page_size = size
        this.GET_GoodsList()
      },

      /** 分页页数发生改变 */
      handlePageCurrentChange(page) {
        this.params.page_no = page
        this.GET_GoodsList()
      },

      /** 搜索事件触发 */
      searchEvent(data) {
        this.params = {
          ...this.params,
          page_no: 1,
          keyword: data
        }
        this.GET_GoodsList()
      },

      /** 切换商品类型 */
      changeGoodsType(val) {
        delete this.params.goods_type
        if (val !== '') {
          this.params = {
            ...this.params,
            goods_type: val
          }
        }
        this.GET_GoodsList()
      },

      GET_GoodsList() {
        this.loading = true
        API_goods.getGoodsList(this.params).then(response => {
          this.loading = false
          this.pageData = {
            page_no: response.page_no,
            page_size: response.page_size,
            data_total: response.data_total
          }
          this.tableData = response.data
        })
      },

      /** 发布商品*/
      publishGoods() {
        this.$router.push({ name: 'goodPublish', params: { callback: this.GET_GoodsList }})
      },

      /** 跳转回收站*/
      gotoRecycle() {
        this.$router.push({ path: '/goods/recycle-station' })
      },

      /** 编辑商品 isdraft 商品列表1*/
      handleEditGoods(row) {
        this.$router.push({ name: 'goodPublish', params: { goodsid: row.goods_id, isdraft: 1, callback: this.GET_GoodsList }})
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  /deep/ div.toolbar {
    height: 70px;
    padding: 20px 0;
  }

  /deep/ .el-table {
    width: 100%;
    overflow-x: scroll;
    & td:not(.is-left) {
      text-align: center;
    }
  }

  .inner-toolbar {
    display: flex;
    flex-direction: row;
    flex-wrap: nowrap;
    width: 100%;
    justify-content: space-between;
    align-items: center;
  }

  /deep/ .pop-sku {
    .toolbar {
      display: none;
    }
    .el-dialog__body {
      .el-table {
        border: 1px solid #e5e5e5;
      }
    }
    .el-table__body-wrapper {
      max-height: 400px;
      overflow-y: scroll;
    }
  }


  .toolbar-search {
    margin-right: 10px;
    width: 20%;
  }

  div.toolbar-btns {
    display: flex;
    flex-direction: row;
    flex-wrap: nowrap;
    justify-content: flex-start;
    align-items: center;
    div {
      span {
        display: inline-block;
        font-size: 14px;
        color: #606266;
      }
    }
    .conditions {
      display: flex;
      flex-direction: row;
      flex-wrap: nowrap;
      justify-content: flex-start;
      align-items: center;
      min-width: 24.5%;
      .choose-machine {
        width: 60%;
      }
    }
  }

  .goods-image {
    width: 50px;
    height: 50px;
  }

  /deep/ div.cell {
    overflow:hidden;

    text-overflow:ellipsis;

    display:-webkit-box;

    -webkit-box-orient:vertical;

    -webkit-line-clamp:2;
  }
  /*下架原因*/
  .under-reason {
    color: red;
    cursor: pointer;
  }

</style>
