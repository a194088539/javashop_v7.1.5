<template>
    <div>
      <en-table-layout
        :tableData="tableData.data"
        :loading="loading"
      >
        <div slot="toolbar" class="inner-toolbar">
          <div class="toolbar-btns">
            <el-button size="mini" type="primary" icon="el-icon-circle-plus-outline" @click="handleAddCoupon">添加</el-button>
          </div>
        </div>

        <template slot="table-columns">
          <el-table-column prop="title" label="名称"/>
          <el-table-column prop="time" :formatter="MixinUnixToDate" label="时间">
            <template slot-scope="scope" >
              {{ formatterDate(scope.row.start_time )}} 至 {{ formatterDate(scope.row.end_time )}}
            </template>
          </el-table-column>
          <el-table-column prop="coupon_price" label="面额"/>
          <el-table-column prop="coupon_threshold_price" label="满足金额"/>
          <el-table-column label="适用范围">
            <template slot-scope="scope">
              <span v-if="scope.row.use_scope === 'ALL'">全品</span>
              <span v-if="scope.row.use_scope === 'CATEGORY'">分类</span>
              <span v-if="scope.row.use_scope === 'SOME_GOODS'">部分商品</span>
            </template>
          </el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="primary"
                @click="handleEditCoupon(scope.$index, scope.row)"
              >编辑</el-button>
              <el-button
                size="mini"
                type="primary"
                @click="handleDeleteCoupon(scope.$index, scope.row)"
              >删除</el-button>
              <el-button
                size="mini"
                type="primary"
                @click="handleOperateReceiveList(scope.$index, scope.row)"
              >领取列表</el-button>
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
    import { RegExp, Foundation } from '~/ui-utils'
    import * as API_Promotion from '@/api/promotion'
    export default {
      name: 'couponList',
      data() {
        return {
          // 列表loading状态
          loading: false,
          /** 列表参数 */
          params: {
            page_no: 1,
            page_size: 10
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

        /** 添加优惠券 */
        handleAddCoupon() {
          this.$router.push({ name: 'coupon', params: { callback: this.GET_CouponsList }})
        },

        /** 编辑优惠券 */
        handleEditCoupon(index, row) {
          this.$router.push({ name: 'coupon', params: { coupon_id: row.coupon_id, callback: this.GET_CouponsList }})
        },

        /** 删除优惠券 */
        handleDeleteCoupon(index, row) {
          API_Promotion.deleteCoupons(row.coupon_id).then(response => {
            this.$message.success('删除成功！')
            this.GET_CouponsList()
          })
        },
        handleOperateReceiveList(index, row) {
          this.$router.push({ path: `/promotions/coupon-manage/receive-list/${row.coupon_id}` })
        },

        /** 获取优惠劵列表 */
        GET_CouponsList() {
          this.loading = true
          API_Promotion.getCouponsList(this.params).then(response => {
            this.loading = false
            this.tableData = response
          }).catch(() => { this.loading = false })
        }
      }
    }
</script>

<style type="text/scss" lang="scss" scoped>
  .el-form-item__content{
    position: relative;
  }
  .icon-percentage{
    position: absolute;
    top: 4px;
    right:12px;
  }
</style>
