<template>
  <div>
    <en-table-layout
      toolbar
      pagination
      :tableData="tableData"
      :loading="loading">
      <div slot="toolbar" class="inner-toolbar">
        <div class="toolbar-btns">
          <el-button type="primary" @click="handleAddGroupBuyGoods">新增团购商品</el-button>
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
                <el-form-item label="活动名称">
                  <el-input size="medium" v-model="advancedForm.act_name" clearable></el-input>
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
                <el-form-item label="活动状态">
                  <el-select v-model="advancedForm.act_status" placeholder="请选择">
                    <el-option label="全部" value="ALL"/>
                    <el-option label="未开始" value="NOT_STARTED"/>
                    <el-option label="进行中" value="STARTED"/>
                    <el-option label="已结束" value="OVER"/>
                  </el-select>
                </el-form-item>
              </el-form>
            </template>
          </en-table-search>
        </div>
      </div>
      <template slot="table-columns">
        <el-table-column label="商品图片" width="120">
          <template slot-scope="scope">
            <img :src="scope.row.img_url" class="goods-image"/>
          </template>
        </el-table-column>
        <el-table-column label="商品名称">
          <template slot-scope="scope">
            <a :href="MixinBuyerDomain + '/goods/' + scope.row.goods_id" target="_blank" class="goods-name">{{ scope.row.goods_name }}</a>
          </template>
        </el-table-column>
        <el-table-column label="团购名称" >
          <template slot-scope="scope">
            <div>
              <a :href="`${MixinBuyerDomain}/goods/${scope.row.goods_id}`" target="_blank" style="color: #266fd4;">{{ scope.row.gb_name }}</a>
            </div>
            <div>{{ scope.row.gb_title }}</div>
          </template>
        </el-table-column>
        <el-table-column  label="活动信息" >
          <template slot-scope="scope">
            <!--活动名称-->
            <div>{{ scope.row.title }}</div>
            <!--活动开始时间----活动结束时间-->
            <div>{{ scope.row.start_time| unixToDate('yyyy-MM-dd') }} - {{ scope.row.end_time| unixToDate('yyyy-MM-dd') }}</div>
            <div v-if="scope.row.delete_status === 'DELETED'">此活动已被平台删除</div>
            <div class="delete-reason" v-if="scope.row.delete_status === 'DELETED'" @click="showDeleteReason(scope.row)">删除原因</div>
          </template>
        </el-table-column>
        <!--已团购数量-->
        <el-table-column prop="buy_num" label="已团购" width="120"/>
        <el-table-column prop="gb_status_text" label="审核状态" width="80"/>
        <el-table-column label="操作" width="180">
          <template slot-scope="scope">
            <el-button
              type="success"
              v-if="scope.row.is_enable !== 0"
              :disabled="scope.row.gb_status_text=='已结束'"
              @click="handleEditGroupGoods(scope.row)">编辑
            </el-button>
            <el-button
              type="danger"
              @click="handleDeleteGroupGoods(scope.row)">删除
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
    <!--删除原因-->
    <el-dialog title="删除原因" :visible.sync="isShowDeleteReason" width="30%" >
      <div class="reason-info">删除日期：{{ delete_time| unixToDate('yyyy-MM-dd hh:mm:ss') }}</div>
      <div class="reason-info">操作人：{{ delete_name }}</div>
      <div class="reason-info">删除原因：{{ delete_reason }}</div>
    </el-dialog>
  </div>
</template>

<script>
  import * as API_groupBuy from '@/api/groupBuy'
  import { CategoryPicker } from '@/components'
  export default {
    name: 'groupBuyManager',
    components: {
      [CategoryPicker.name]: CategoryPicker
    },
    data() {
      return {
        /** 列表loading状态 */
        loading: false,

        /** 列表参数 */
        params: {
          page_no: 1,
          page_size: 10
        },

        /** 列表数据 */
        tableData: [],

        /** 列表分页数据 */
        pageData: [],

        /** 当前团购状态*/
        currentGroupBuyStatus: '',

        /** 团购状态*/
        groupBuyStatus: [
          { group_buy_status_id: 0, group_buy_status_name: '未审核' },

          { group_buy_status_id: 1, group_buy_status_name: '已通过' },

          { group_buy_status_id: 2, group_buy_status_name: '审核失败' }

          // { group_buy_status_id: 3, group_buy_status_name: '已结束' }
        ],
        /** 高级搜索数据 */
        advancedForm: {},

        /** 是否显示删除原因 */
        isShowDeleteReason: false,
        /** 删除日期 */
        delete_time: '',
        /** 删除人 */
        delete_name: '',
        /** 删除原因 */
        delete_reason: ''
      }
    },
    mounted() {
      this.GET_GroupGoodsList()
    },
    methods: {
      /** 分页大小发生改变 */
      handlePageSizeChange(size) {
        this.params.page_size = size
        this.GET_GroupGoodsList()
      },

      /** 分页页数发生改变 */
      handlePageCurrentChange(page) {
        this.params.page_no = page
        this.GET_GroupGoodsList()
      },

      /** 搜索事件触发 */
      searchEvent(data) {
        this.params.page_no = 1
        this.params.goods_name = data
        if (!data) delete this.params.goods_name
        this.GET_GroupGoodsList()
      },

      /** 高级搜索事件触发 */
      advancedSearchEvent() {
        this.params = {
          ...this.params,
          ...this.advancedForm
        }
        this.params.page_no = 1
        this.GET_GroupGoodsList()
      },

      /** 获取团购商品列表 */
      GET_GroupGoodsList() {
        this.loading = true
        API_groupBuy.getGroupBuyGoodsList(this.params).then(response => {
          this.loading = false
          this.pageData = {
            page_no: response.page_no,
            page_size: response.page_size,
            data_total: response.data_total
          }
          this.tableData = response.data
        })
      },

      /** 新增团购商品商品 */
      handleAddGroupBuyGoods() {
        this.$router.push({ name: 'groupBuyGoods', query: { group_name: '新增' }, params: { callback: this.GET_GroupGoodsList }})
      },

      /** 编辑团购商品 */
      handleEditGroupGoods(row) {
        this.$router.push({ name: 'groupBuyGoods', query: { group_name: '编辑' }, params: { goods_id: row.gb_id, callback: this.GET_GroupGoodsList }})
      },

      /** 显示团购活动删除原因 */
      showDeleteReason(row) {
        this.isShowDeleteReason = false
        this.isShowDeleteReason = true
        this.delete_time = row.delete_time
        this.delete_name = row.delete_name
        this.delete_reason = row.delete_reason
      },

      /** 删除团购商品 */
      handleDeleteGroupGoods(row) {
        this.$confirm('确认删除此团购商品, 是否继续?', '提示', { type: 'warning' }).then(() => {
          API_groupBuy.deleteGroupBuyGoods(row.gb_id, {}).then(() => {
            this.GET_GroupGoodsList()
            this.$message.success('删除团购商品成功！')
          })
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  /deep/ div.toolbar {
    height: 70px;
    padding: 20px 0;
  }

  div.toolbar-btns {
    width: 100%;
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
      margin-right: 30px;
    }
  }

  /deep/ .el-table td:not(.is-left) {
    text-align: center;
  }

  .inner-toolbar {
    display: flex;
    width: 100%;
    justify-content: space-between;
  }

  .toolbar-search {
    margin-right: 10px;
  }

  .goods-image {
    width: 50px;
    height: 50px;
  }

  .goods-name {
    color: #4183c4;
    &:hover { color: #f42424 }
  }

  .delete-reason {
    color: #4183c4;
    cursor: pointer;
  }

  .reason-info {
    margin-bottom: 8px;
  }
</style>
