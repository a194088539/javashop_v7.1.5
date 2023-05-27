<template>
    <div>
      <en-table-layout
        :tableData="tableData.data"
        :loading="loading"
      >
        <template slot="table-columns">
          <el-table-column prop="member_name" label="会员名称"/>
          <el-table-column prop="coupon_price" label="面额"/>
          <el-table-column prop="time" :formatter="MixinUnixToDate" label="有效期">
            <template slot-scope="scope" >
              {{ formatterDate(scope.row.start_time )}} 至 {{ formatterDate(scope.row.end_time )}}
            </template>
          </el-table-column>
          <el-table-column label="适用范围">
            <template slot-scope="scope">
              <span v-if="scope.row.use_scope === 'ALL'">全品</span>
              <span v-if="scope.row.use_scope === 'CATEGORY'">分类</span>
              <span v-if="scope.row.use_scope === 'SOME_GOODS'">部分商品</span>
            </template>
          </el-table-column>
          <el-table-column label="状态">
            <template slot-scope="scope">
              <span v-if="scope.row.used_status === 0">未使用</span>
              <span v-if="scope.row.used_status === 1">已使用</span>
              <span v-if="scope.row.used_status === 2">已过期</span>
              <span v-if="scope.row.used_status === 3">已作废</span>
            </template>
          </el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="primary"
                :disabled="scope.row.used_status === 3 || scope.row.used_status === 1"
                @click="handleNullify(scope.$index, scope.row)"
              >作废</el-button>
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
    </div>
</template>

<script>
    import * as API_Promotion from '@/api/promotion'
    import { Foundation } from '~/ui-utils'
    export default {
      name: 'ReceiveList',
      data() {
        return {
          // 列表loading状态
          loading: false,
          /** 列表参数 */
          params: {
            page_no: 1,
            page_size: 10,
            coupon_id: this.$route.params.id
          },
          /** 列表数据 */
          tableData: ''
        }
      },
      mounted() {
        this.GET_CouponsList()
      },
      methods: {
        /** 格式化时间 */
        formatterDate(date) {
          return Foundation.unixToDate(date, 'yyyy-MM-dd')
        },
        /** 分页大小发生改变 */
        handlePageSizeChange(size) {
          this.params.page_size = size
          this.GET_CouponsList()
        },

        /** 分页页数发生改变 */
        handlePageCurrentChange(page) {
          this.params.page_no = page
          this.GET_CouponsList()
        },

        /** 获取优惠劵领取列表 */
        GET_CouponsList() {
          API_Promotion.queryReceiveCouponsList(this.params).then(response => {
            this.tableData = response
          })
        },
        handleNullify(index, row) {
          API_Promotion.nullifyCoupons(row.mc_id).then(response => {
            this.$message.success('作废成功！')
            this.GET_CouponsList()
          })
        }
      }
    }
</script>

<style scoped>

</style>
