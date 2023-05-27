<template>
  <div>
    <en-table-layout
    :tableData="tableData.data"
    :loading="loading"
    :row-key="setRowKey"
    ref="tableContainer"
    @selection-change="handleSelectionChange"
  >

    <div slot="toolbar" class="inner-toolbar">
      <div class="toolbar-btns">
        <el-button
          size="mini"
          type="primary"
        @click="handleBatchReviewSckillGoods">批量审核</el-button>
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
              <el-form-item label="店铺名称">
                <en-shop-picker @changed="(shop) => { advancedForm.seller_id = shop.shop_id }"/>
              </el-form-item>
            </el-form>
          </template>
        </en-table-search>
      </div>
    </div>

    <template slot="table-columns">
      <el-table-column type="selection" width="55" :selectable="isSelectRow" :reserve-selection="false"></el-table-column>
      <el-table-column label="商品名称" min-width="450">
        <template slot-scope="scope">
          <a :href="MixinBuyerDomain + '/goods/' + scope.row.goods_id" target="_blank" class="goods-name">{{ scope.row.goods_name }}</a>
        </template>
      </el-table-column>
      <el-table-column prop="shop_name" label="店铺名称"/>
      <el-table-column prop="original_price" :formatter="MixinFormatPrice" label="商品原价"/>
      <el-table-column prop="price" :formatter="MixinFormatPrice" label="活动价格"/>
      <el-table-column prop="sold_quantity" label="售空数量" width="100"/>
      <el-table-column label="抢购时刻" width="100">
        <template slot-scope="scope">{{ scope.row.time_line < 10 ? '0' + scope.row.time_line : scope.row.time_line }} : 00</template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="primary"
            @click="handlePassGoods(scope.$index, scope.row)">通过</el-button>
          <el-button
            size="mini"
            type="warning"
            @click="handleRejectGoods(scope.$index, scope.row)">驳回</el-button>
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
      title="批量审核限时抢购商品"
      :visible.sync="dialogBatchReviewSckillGoods"
      width="400px"
    >
      <el-form :model="batchReviewSckillGoodsForm" :rules="batchReviewSckillGoodsRules" ref="batchReviewSckillGoodsForm" label-width="100px">
        <!--是否通过=-->
        <el-form-item label="是否通过" prop="status">
          <el-radio-group v-model="batchReviewSckillGoodsForm.status">
            <el-radio label="PASS">通过</el-radio>
            <el-radio label="FAIL">驳回</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="驳回原因" prop="fail_reason" v-if="batchReviewSckillGoodsForm.status === 'FAIL'">
          <el-input
            type="textarea"
            :autosize="{ minRows: 2, maxRows: 4}"
            placeholder="请输入驳回原因(120字以内)"
            :maxlength="120"
            v-model="batchReviewSckillGoodsForm.fail_reason">
          </el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogBatchReviewSckillGoods = false">取 消</el-button>
        <el-button type="primary" @click="submitBatchReviewSckillGoodsForm">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import * as API_Promotion from '@/api/promotion'

  export default {
    name: 'seckillAuditGoodsList',
    data() {
      return {
        // 列表loading状态
        loading: false,
        // 列表参数
        params: {
          page_no: 1,
          page_size: 10,
          status: 'APPLY',
          seckill_id: this.$route.params.id
        },
        // 列表数据
        tableData: '',
        /** 高级搜索数据 */
        advancedForm: {},
        // 当前已选择的行
        multipleSelection: [],
        // 批量审核 dialog
        dialogBatchReviewSckillGoods: false,
        // 批量审核 提交表单
        batchReviewSckillGoodsForm: {
          apply_ids: '',
          fail_reason: '',
          status: 'PASS'
        },
        // 批量审核 表单验证
        batchReviewSckillGoodsRules: {
          fail_reason: [{ required: true, message: '请输入驳回原因！', trigger: 'blur' }]
        }
      }
    },
    mounted() {
      this.GET_SeckillAuditGoodsList()
    },
    methods: {
      // 单选或者全选
      handleSelectionChange(val) {
        this.multipleSelection = val
      },
      // 判断 当前行是都可以选中
      isSelectRow(row, index) {
        return row.status === 'PASS' ? 0 : 1
      },
      setRowKey(row) {
        return row.create_time
      },
      /** 分页大小发生改变 */
      handlePageSizeChange(size) {
        this.params.page_size = size
        this.GET_SeckillAuditGoodsList()
      },

      /** 分页页数发生改变 */
      handlePageCurrentChange(page) {
        this.params.page_no = page
        this.GET_SeckillAuditGoodsList()
      },

      /** 通过限时抢购商品 */
      handlePassGoods(index, row) {
        this.$confirm('确定要通过这个商品？', '提示', { type: 'warning' }).then(() => {
          const apply_ids = [row.apply_id]
          API_Promotion.batchReviewSckillGoods({ apply_ids, status: 'PASS' }).then(response => {
            this.$message.success('该商品已通过！')
            this.GET_SeckillAuditGoodsList()
          })
        }).catch(() => {})
      },

      /** 拒绝限时抢购商品 */
      handleRejectGoods(index, row) {
        this.$prompt('请输入拒绝原因', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPattern: /.+/,
          inputValidator: this.rejectGoodsFailReason,
          inputErrorMessage: '请输入拒绝原因'
        }).then(({ value }) => {
          const apply_ids = [row.apply_id]
          const fail_reason = value
          API_Promotion.batchReviewSckillGoods({ apply_ids, status: 'FAIL', fail_reason }).then(response => {
            this.$message.success('该商品已拒绝！')
            this.GET_SeckillAuditGoodsList()
          })
        }).catch(() => {})
      },
      /** 批量审核*/
      handleBatchReviewSckillGoods() {
        if (!this.multipleSelection.length) {
          return this.$message.error('请选择要审核的商品！')
        }
        this.dialogBatchReviewSckillGoods = true
      },
      async submitBatchReviewSckillGoodsForm() {
        let flag = false
        let params = JSON.parse(JSON.stringify(this.batchReviewSckillGoodsForm))
        params.apply_ids = this.multipleSelection.map(item => item.apply_id)
        if (params.status === 'PASS') {
          delete params.fail_reason
          flag = true
        } else {
          await this.$refs.batchReviewSckillGoodsForm.validate(volid => {
            if (volid) {
              flag = true
            } else {
              return false
            }
          })
        }
        if (flag) {
          try {
            await API_Promotion.batchReviewSckillGoods(params)
            await this.GET_SeckillAuditGoodsList()
            this.params.page_no = 1
            this.$refs.tableContainer && this.$refs.tableContainer.$refs && this.$refs.tableContainer.$refs.table && this.$refs.tableContainer.$refs.table.clearSelection()
            this.dialogBatchReviewSckillGoods = false
            this.$message.success('批量审核成功！')
          } catch (e) {
            this.$message.error('批量审核失败，请重试！')
          }
        }
      },
      rejectGoodsFailReason(value) {
        if (value.length > 500) {
          return '拒绝原因长度不能超过500个字符'
        }
      },

      /** 搜索事件触发 */
      searchEvent(data) {
        this.params.page_no = 1
        this.params.goods_name = data
        if (!data) delete this.params.goods_name
        this.GET_SeckillAuditGoodsList()
      },

      /** 高级搜索事件触发 */
      advancedSearchEvent() {
        this.params = {
          ...this.params,
          ...this.advancedForm
        }
        this.params.page_no = 1
        this.GET_SeckillAuditGoodsList()
      },

      /** 获取待审核商品列表 */
      GET_SeckillAuditGoodsList() {
        this.loading = true
        API_Promotion.getSeckillGoods(this.params).then(response => {
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
