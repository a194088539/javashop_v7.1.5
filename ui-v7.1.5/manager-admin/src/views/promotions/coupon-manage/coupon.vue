<template>
  <div>
    <div class="coupon">
      <el-form
        :model="couponForm"
        :rules="couponRules"
        ref="couponForm"
        label-width="120px"
        status-icon
        label-position="right">
        <el-form-item label="活动名称" prop="title">
          <el-input v-model="couponForm.title" :maxlength="20" clearable></el-input>
        </el-form-item>
        <el-form-item label="面额" prop="coupon_price">
          <el-input v-model="couponForm.coupon_price" :maxlength="20" clearable></el-input>
        </el-form-item>
        <el-form-item label="消费限额" prop="coupon_threshold_price">
          <el-input v-model="couponForm.coupon_threshold_price" :maxlength="20" clearable></el-input>
        </el-form-item>
        <el-form-item label="店铺承担比例" prop="shop_commission">
          <el-input v-model="couponForm.shop_commission" :maxlength="20" clearable></el-input>
          <template slot="append">%</template>
        </el-form-item>
        <el-form-item label="发放数量" prop="create_num">
          <el-input v-model="couponForm.create_num" :maxlength="20" clearable></el-input>
        </el-form-item>
        <el-form-item label="限领数量" prop="limit_num">
          <el-input v-model="couponForm.limit_num" :maxlength="20" clearable></el-input>
        </el-form-item>
        <el-form-item label="优惠券有效期" prop="time_range">
          <el-date-picker
            v-model="couponForm.time_range"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            clearable
            :default-time="[MixinDefaultTime]"
            :picker-options="{disabledDate(time) { return time.getTime() < Date.now() - 8.64E7 }}">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="适用范围" prop="use_scope">
          <el-radio-group v-model="couponForm.use_scope">
            <el-radio label="ALL">全品</el-radio>
            <el-radio label="CATEGORY">分类</el-radio>
            <el-radio label="SOME_GOODS">部分商品</el-radio>
          </el-radio-group>
        </el-form-item>
       <!--部分商品选择器-->
        <el-form-item v-if="this.couponForm.use_scope === 'SOME_GOODS'">
          <en-table-layout
            toolbar
            pagination
            :tableData="tableData">
            <div slot="toolbar" class="inner-toolbar">
              <div class="toolbar-btns">
                <el-button type="primary" @click="selectgoodslist" >选择商品</el-button>
              </div>
            </div>
            <template slot="table-columns">
              <el-table-column label="商品列表" width="180">
                <template slot-scope="scope">
                  <a :href="`${MixinBuyerDomain}/goods/${scope.row.goods_id}`" target="_blank">
                    <img :src="scope.row.goods_image" class="goods-image"/>
                  </a>
                </template>
              </el-table-column>
              <el-table-column label="商品名称">
                <template slot-scope="scope">
                  <a :href="`${MixinBuyerDomain}/goods/${scope.row.goods_id}`" target="_blank" style="color: #00a2d4;">{{ scope.row.goods_name }}</a>
                </template>
              </el-table-column>
            </template>
          </en-table-layout>
        </el-form-item>
        <!--分类选择器-->
        <el-form-item v-if="this.couponForm.use_scope === 'CATEGORY'">
            <el-cascader
            v-model="customValue"
            :options="cascaderOptions"
            :props="customProps"/>
        </el-form-item>
        <el-form-item>
        </el-form-item>
        <el-form-item label="适用范围描述" prop="scope_description">
          <el-input
            type="textarea"
            :autosize="{ minRows: 2, maxRows: 4}"
            placeholder="请输入适用范围描述"
            v-model="couponForm.scope_description"
            :maxlength="50"
            clearable
          ></el-input>
        </el-form-item>
        <el-form-item label="活动说明" prop="activity_description">
          <el-input
            type="textarea"
            :autosize="{ minRows: 2, maxRows: 4}"
            placeholder="请输入活动说明"
            v-model="couponForm.activity_description"
            :maxlength="50"
            clearable
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSaveCoupon('couponForm')">保 存</el-button>
        </el-form-item>
      </el-form>
    </div>
    <en-goods-picker
      :api="goodsListApi"
      :multipleApi="multipleGoodsApi"
      :show="dialogGoodsShow"
      :default-data="defaultGoodsData"
      @close="dialogGoodsShow = false"
      @confirm="handleGoodsPickerConfirm"
    />
  </div>
</template>

<script>
    import { RegExp, Foundation } from '~/ui-utils'
    import * as API_Promotion from '@/api/promotion'
    import { cloneObj } from '@/utils/index'
    import request from '@/utils/request'

    export default {
      name: 'coupon',
      data() {
        const checkCouponPrice = (rule, value, callback) => {
          if (!value) {
            callback(new Error('请输入面额'))
          } else if (!RegExp.money.test(value)) {
            callback(new Error('请输入正确的面额'))
          } else {
            callback()
          }
        }
        const checkCouponThresholdPrice = (rule, value, callback) => {
          if (!value) {
            callback(new Error('请输入消费限额'))
          } else if (!RegExp.money.test(value)) {
            callback(new Error('请输入正确的消费限额'))
          } else {
            callback()
          }
        }
        const checkCreateNum = (rule, value, callback) => {
          if (!value) {
            callback(new Error('请输入发放量'))
          } else if (!RegExp.integer.test(value)) {
            callback(new Error('请输入正整数'))
          } else if (parseInt(value) < parseInt(this.couponForm.limit_num)) {
            callback(new Error('每人限领数量不得大于发行量'))
          } else {
            callback()
          }
        }
        const checkLimitNum = (rule, value, callback) => {
          if (!value && parseInt(value) !== 0) {
            callback(new Error('请输入每人限领数量'))
          } else if (!RegExp.integer.test(value) && parseInt(value) !== 0) {
            callback(new Error('请输入正整数'))
          } else if (parseInt(value) > parseInt(this.couponForm.create_num)) {
            callback(new Error('每人限领数量不得大于发行量'))
          } else {
            callback()
          }
        }
        const checkShopCommission = (rule, value, callback) => {
          if (!value) {
            callback(new Error('请输入店铺承担比例'))
          } else if (!RegExp.integer.test(value)) {
            callback(new Error('请输入正整数'))
          } else {
            callback()
          }
        }
        return {
          customProps: {
            multiple: true,
            value: 'category_id',
            label: 'name',
            children: 'children',
            disabled: 'disabled'
          },
          cascaderOptions: [],
          customValue: [],
          // 获取商品列表API
          goodsListApi: 'admin/goods',
          // 根据商品id，获取商品列表API
          multipleGoodsApi: 'admin/goods/@ids/details',
          /** 商品默认数据 */
          defaultGoodsData: [],
          dialogGoodsShow: false,
          /** 商品skuIds */
          goods_ids: [],
          /** 商品表格信息*/
          tableData: [],
          /** 列表参数 */
          categoryApi: '/admin/goods/categories/@ids/all-children',
          isCategory: false,
          params: {
            coupon_id: ''
          },
          /** 添加，编辑优惠劵  表单*/
          couponForm: {},
          /** 添加，编辑优惠劵  表单规则*/
          couponRules: {
            /** 优惠券名称 */
            title: [
              { required: true, message: '请输入活动名称', trigger: 'blur' }
            ],

            /** 优惠券面额 */
            coupon_price: [
              { required: true, message: '请输入面额', trigger: 'blur' },
              { validator: checkCouponPrice, trigger: 'blur' }
            ],

            /** 消费限额 */
            coupon_threshold_price: [
              { required: true, message: '请输入消费限额', trigger: 'blur' },
              { validator: checkCouponThresholdPrice, trigger: 'blur' }
            ],

            /** 发放量 */
            create_num: [
              { required: true, message: '请输入发放量', trigger: 'blur' },
              { validator: checkCreateNum, trigger: 'blur' }
            ],

            /** 每人限领 */
            limit_num: [
              { required: true, message: '请输入限领数量', trigger: 'blur' },
              { validator: checkLimitNum, trigger: 'blur' }
            ],

            /** 优惠券有效期 */
            time_range: [{ required: true, message: '请选择优惠券有效期', trigger: 'blur' }],

            /** 店铺承担比例 */
            shop_commission: [
              { required: true, message: '请输入店铺承担比例', trigger: 'blur' },
              { validator: checkShopCommission, trigger: 'blur' }
            ],

            /** 适用范围 */
            use_scope: [{ required: true, message: '请输入适用范围', trigger: 'blur' }],

            /** 适用范围描述 */
            scope_description: [{ required: true, message: '请输入适用范围描述', trigger: 'blur' }]
          }
        }
      },
      async mounted() {
        await this.GET_CategoryData()
        this.couponForm = {}
        if (this.$route.params.coupon_id) {
          this.params.coupon_id = this.$route.params.coupon_id
          await this.GET_CouponActivity()
        }
      },
      methods: {
        /** 查询单个优惠劵信息 */
        GET_CouponActivity() {
          API_Promotion.queryCoupons(this.params.coupon_id).then(response => {
            let prevCascader = []
            if (response.scope_id && typeof response.scope_id === 'string') {
              prevCascader = response.scope_id.split(',').filter(item => !!item).map(item => +item)
            }
            function next(params, prev) {
              for (let i = 0; i < params.length; i++) {
                const item = params[i]
                if (item.children) {
                  next(item.children, [...prev, item])
                } else {
                  if (prevCascader.includes(+item.category_id)) {
                    prevCascader = prevCascader.map(key => {
                      if (+key === +item.category_id) {
                        let result = prev.map(item => item.category_id)
                        return [...result, item.category_id]
                      } else {
                        return key
                      }
                    })
                  } else {
                    i === params.length - 1 && (prev = [])
                  }
                }
              }
            }

            if (response.use_scope === 'SOME_GOODS') {
              this.defaultGoodsData = prevCascader
              response.scope_id && this.GET_GoodsByGoodsIdGroup(response.scope_id)
            } else if (response.use_scope === 'CATEGORY') {
              next(this.cascaderOptions, [])
              this.customValue = prevCascader
            }

            this.couponForm = {
              ...response,
              time_range: [parseInt(response.start_time) * 1000, parseInt(response.end_time) * 1000]
            }
          })
        },
        /**  显示商品选择器*/
        selectgoodslist() {
          this.dialogGoodsShow = true
        },
        /** 保存商品选择器选择的商品 */
        handleGoodsPickerConfirm(val) {
          const _val = val
          this.tableData = cloneObj(_val)
          this.defaultGoodsData = this.goods_ids = this.tableData.map(key => key.goods_id)
        },
        /** 提交优惠劵表单 */
        handleSaveCoupon(couponForm) {
          const _time_range = this.couponForm.time_range
          const _params = this.$route.params
          this.$refs[couponForm].validate((valid) => {
            const params = this.MixinClone(this.couponForm)
            if (valid) {
              const { coupon_id } = params
              delete params.time_range
              // params.use_scope === 'ALL' ? params.scope_id = 0 : params.scope_id = this.goods_ids
              switch (params.use_scope) {
                case 'ALL':
                  params.scope_id = 0
                  break
                case 'CATEGORY':
                  params.scope_id = this.customValue.map(item => item[item.length - 1])
                  break
                case 'SOME_GOODS':
                  params.scope_id = this.goods_ids
                  break
              }
              params.start_time = _time_range[0] / 1000
              params.end_time = _time_range[1] / 1000
              if (coupon_id) {
                API_Promotion.editCoupons(coupon_id, params).then(response => {
                  this.dialogCouponVisible = false
                  this.$message.success('修改成功！')
                  this.$store.dispatch('delCurrentViews', {
                    view: this.$route,
                    $router: this.$router
                  })
                  this.$router.push({ path: '/promotions/coupon-manage/coupon-list' })
                  _params.callback()
                })
              } else {
                API_Promotion.addCoupons(params).then(response => {
                  this.$message.success('添加成功！')
                  this.$store.dispatch('delCurrentViews', {
                    view: this.$route,
                    $router: this.$router
                  })
                  this.$router.push({ path: '/promotions/coupon-manage/coupon-list' })
                  _params.callback()
                })
              }
            } else {
              this.$message.error('表单填写有误，请检查！')
              return false
            }
          })
        },
        /* 获取 目录树结构 */
        async GET_CategoryData(id = 0) {
          return await request({
            url: this.categoryApi.replace('@ids', id),
            method: 'get',
            loading: false
          }).then(response => {
            function next(params) {
              for (let i = 0; i < params.length; i++) {
                const item = params[i]
                if (item.children) {
                  if (Array.isArray(item.children) && item.children.length) {
                    next(item.children)
                  } else {
                    item.children = null
                  }
                } else {
                  item.children = null
                }
              }
            }
            next(response)
            this.cascaderOptions = response
          })
        },
        /** 根据商品编号获取商品信息 */
        GET_GoodsByGoodsIdGroup(ids) {
          ids = typeof ids === 'string'
            ? ids.replace(/，/g, ',')
            : ids.join(',')
          return new Promise((resolve, reject) => {
            request({
              url: this.multipleGoodsApi.replace('@ids', ids),
              method: 'get',
              loading: false
            }).then(response => {
              const data = response.map(item => {
                item.goods_image = item.thumbnail
                item.goods_price = item.price
                return item
              })
              this.tableData = data
            }).catch(reject)
          })
        }
      }
    }
</script>

<style type="text/scss" lang="scss" scoped>
  /deep/ {
    .el-input {
      max-width: 400px;
    }
    .el-textarea__inner {
      max-width: 500px;
    }
  }
  .coupon{
    background-color: #fff;
    border: 1px solid #9a9a9a;
    margin: 10px;
    padding: 25px;
  }
  .goods-image{
    width:50px;
    height:50px;
  }
</style>
