<template>
  <div>
    <en-table-layout
    :tableData="tableData.data"
    @selection-change="handleSelectionChange"
    :loading="loading"
    ref="tableContainer"
  >
    <div slot="toolbar" class="inner-toolbar">
      <div class="toolbar-btns">
        <el-button
          size="mini"
          type="primary"
        @click="handleBatchReviewGroupBuyGoods">批量审核</el-button>
      </div>
      <div class="toolbar-search">
        <en-table-search
          @search="searchEvent"
          @advancedSearch="advancedSearchEvent"
          advanced
          advancedWidth="465"
          placeholder="请输入商品名称"
        >
          <template slot="advanced-content">
            <el-form ref="advancedForm" :model="advancedForm" label-width="80px">
              <el-form-item label="商品名称">
                <el-input size="medium" v-model="advancedForm.goods_name" clearable></el-input>
              </el-form-item>
              <el-form-item label="商家名称">
                <en-shop-picker @changed="(shop) => { advancedForm.seller_id = shop.shop_id }"/>
              </el-form-item>
              <el-form-item label="团购名称">
                <el-input size="medium" v-model="advancedForm.gb_name" clearable></el-input>
              </el-form-item>
              <el-form-item label="审核状态">
                <el-select v-model="advancedForm.gb_status" placeholder="请选择">
                  <el-option label="全部" value=""/>
                  <el-option label="待审核" value="0"/>
                  <el-option label="通过审核" value="1"/>
                  <el-option label="未通过审核" value="2"/>
                </el-select>
              </el-form-item>
            </el-form>
          </template>
        </en-table-search>
      </div>
    </div>

    <template slot="table-columns">
      <el-table-column type="selection" width="55" :selectable="isSelectRow" :reserve-selection="false"></el-table-column>
      <el-table-column prop="gb_name" label="团购名称"/>
      <el-table-column label="商品名称">
        <template slot-scope="scope">
          <a :href="MixinBuyerDomain + '/goods/' + scope.row.goods_id" target="_blank" class="goods-name">{{ scope.row.goods_name }}</a>
        </template>
      </el-table-column>
      <el-table-column label="团购价格">
        <template slot-scope="scope">{{ scope.row.price | unitPrice('￥') }}</template>
      </el-table-column>
      <el-table-column prop="seller_name" label="商家名称"/>
      <el-table-column prop="gb_status_text" label="审核状态"/>
      <el-table-column label="操作" width="230">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.gb_status === 0"
            size="mini"
            type="primary"
            @click="handlePassGoods(scope.$index, scope.row)">通过</el-button>
          <el-button
            v-if="scope.row.gb_status === 0"
            size="mini"
            type="warning"
            @click="handleRejectGoods(scope.$index, scope.row)">拒绝</el-button>
          <el-button
            size="mini"
            type="primary"
            @click="handleViewMember(scope.$index, scope.row)">查看</el-button>
        </template>
      </el-table-column>
    </template>
    <el-pagination
      v-if="tableData"
      slot="pagination"
      @size-change="handlePageSizeChange"
      @current-change="handlePageCurrentChange"
      :current-page="tableData.page_no"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="tableData.page_size"
      layout="total, sizes, prev, pager, next, jumper"
      :total="tableData.data_total">
    </el-pagination>
  </en-table-layout>
    <!--批量审核商品 dialog-->
    <el-dialog
      title="批量审核团购商品"
      :visible.sync="dialogBatchReviewGroupBuyGoods"
      width="400px">
      <el-form :model="batchReviewGroupBuyGoodsForm" ref="batchReviewGroupBuyGoodsForm" label-width="100px">
        <!--是否通过=-->
        <el-form-item label="是否通过" prop="status">
          <el-radio-group v-model="batchReviewGroupBuyGoodsForm.status">
            <el-radio :label="1">通过</el-radio>
            <el-radio :label="0">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogBatchReviewGroupBuyGoods = false">取 消</el-button>
        <el-button type="primary" @click="submitBatchReviewGroupBuyGoodsForm">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import * as API_Promotion from '@/api/promotion'

  export default {
    name: 'groupBuyGoods',
    data() {
      return {
        /** 列表loading状态 */
        loading: false,

        /** 列表参数 */
        params: {
          page_no: 1,
          page_size: 10,
          act_id: this.$route.params.id
        },

        /** 列表数据 */
        tableData: '',
        /** 高级搜索数据 */
        advancedForm: {},
        // 当前已选择的行
        multipleSelection: [],
        // 批量审核商品 dialog
        dialogBatchReviewGroupBuyGoods: false,
        // 批量审核商品 表单
        batchReviewGroupBuyGoodsForm: {
          act_id: '',
          gb_ids: [],
          status: 1
        }
      }
    },
    activated() {
      this.params.act_id = this.$route.params.id
    },
    beforeRouteUpdate(to, from, next) {
      this.params.act_id = to.params.id
      next()
    },
    watch: {
      'params.act_id': 'GET_GroupBuyGoodsList'
    },
    mounted() {
      this.GET_GroupBuyGoodsList()
    },
    methods: {
      // 单选或者全选
      handleSelectionChange(val) {
        this.multipleSelection = val
      },
      // 判断 当前行是都可以选中
      isSelectRow(row, index) {
        return row.gb_status === 0 ? 1 : 0
      },
      setRowKey(row) {
        return row.create_time
      },
      /** 分页大小发生改变 */
      handlePageSizeChange(size) {
        this.params.page_size = size
        this.GET_GroupBuyGoodsList()
      },

      /** 分页页数发生改变 */
      handlePageCurrentChange(page) {
        this.params.page_no = page
        this.GET_GroupBuyGoodsList()
      },

      /** 通过 */
      handlePassGoods(index, row) {
        this.$confirm('确定要通过这个活动吗？', '提示', { type: 'warning' }).then(() => {
          const act_id = row.act_id
          const gb_ids = [row.gb_id]
          API_Promotion.batchReviewGroupBuyGoods({ status: 1, gb_ids, act_id }).then(() => {
            this.$message.success('已审核通过！')
            this.GET_GroupBuyGoodsList()
          })
        }).catch(() => {})
      },

      /** 拒绝 */
      handleRejectGoods(index, row) {
        this.$confirm('确定要拒绝这个活动吗？', '提示', { type: 'warning' }).then(() => {
          const act_id = row.act_id
          const gb_ids = [row.gb_id]
          API_Promotion.batchReviewGroupBuyGoods({ status: 2, gb_ids, act_id }).then(() => {
            this.$message.success('已拒绝！')
            this.GET_GroupBuyGoodsList()
          })
        }).catch(() => {})
      },
      /** 批量审核 */
      handleBatchReviewGroupBuyGoods() {
        if (!this.multipleSelection.length) {
          return this.$message.error('请选择要审核的商品！')
        }
        this.dialogBatchReviewGroupBuyGoods = true
      },
      /** 批量审核团购商品 表单提交 */
      async submitBatchReviewGroupBuyGoodsForm() {
        let params = JSON.parse(JSON.stringify(this.batchReviewGroupBuyGoodsForm))
        try {
          params.act_id = this.$route.params.id
          params.gb_ids = this.multipleSelection.map(item => item.gb_id)
          await API_Promotion.batchReviewGroupBuyGoods(params)
          await this.GET_GroupBuyGoodsList()
          this.params.page_no = 1
          this.$refs.tableContainer && this.$refs.tableContainer.$refs && this.$refs.tableContainer.$refs.table && this.$refs.tableContainer.$refs.table.clearSelection()
          this.dialogBatchReviewGroupBuyGoods = false
          this.$message.success('批量审核成功！')
        } catch (e) {
          this.$message.error('批量审核失败，请重试！')
        }
      },
      /** 查看详情 */
      handleViewMember(index, row) {
        this.$router.push({ path: `/promotions/group-buy-manage/group-buy-goods-info/${row.gb_id}` })
      },

      /** 搜索事件触发 */
      searchEvent(data) {
        this.params.page_no = 1
        this.params.goods_name = data
        if (!data) delete this.params.goods_name
        this.GET_GroupBuyGoodsList()
      },

      /** 高级搜索事件触发 */
      advancedSearchEvent() {
        this.params = {
          ...this.params,
          ...this.advancedForm
        }
        this.params.page_no = 1
        this.GET_GroupBuyGoodsList()
      },

      /** 获取团购活动详情商品列表 */
      GET_GroupBuyGoodsList() {
        this.loading = true
        API_Promotion.getGroupBuyGoods(this.params).then(response => {
          this.loading = false
          this.tableData = response
        }).catch(() => { this.loading = false })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .goods-name {
    color: #4183c4;
    &:hover { color: #f42424 }
  }
</style>
