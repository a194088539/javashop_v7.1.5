<template>
  <en-table-layout
    :tableData="tableData.data"
    :loading="loading"
  >
    <div slot="toolbar" class="inner-toolbar">
      <div class="toolbar-btns"></div>
      <div class="toolbar-search">
        <en-table-search
          @search="searchEvent"
          @advancedSearch="advancedSearchEvent"
          advanced
          placeholder="请输入会员名或手机号"
          :advancedWidth="450"
        >
          <template slot="advanced-content">
            <el-form ref="advancedForm" :model="advancedForm" label-width="100px">
              <el-form-item label="用户名">
                <el-input size="medium" v-model="advancedForm.uname" clearable></el-input>
              </el-form-item>
              <el-form-item label="手机号">
                <el-input size="medium" v-model="advancedForm.mobile" clearable></el-input>
              </el-form-item>
              <el-form-item label="电子邮箱">
                <el-input size="medium" v-model="advancedForm.email" clearable></el-input>
              </el-form-item>
              <el-form-item label="会员性别">
                <el-radio-group v-model="advancedForm.sex">
                  <el-radio :label="1">男</el-radio>
                  <el-radio :label="0">女</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="注册时间区间">
                <el-date-picker
                  v-model="advancedForm.register_time_range"
                  type="daterange"
                  :default-time="['00:00:00', '23:59:59']"
                  range-separator="-"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  value-format="timestamp"
                  style="width: 324px"
                  :picker-options="{ disabledDate (time) { return time.getTime() - 1 >= new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate()).getTime() + 86400000 - 1 }, shortcuts: MixinPickerShortcuts }"
                >
                </el-date-picker>
              </el-form-item>
            </el-form>
          </template>
        </en-table-search>
      </div>
    </div>

    <template slot="table-columns">
      <el-table-column prop="uname" label="用户名"/>
      <el-table-column label="手机号">
        <template slot-scope="scope">{{ scope.row.mobile | secrecyMobile }}</template>
      </el-table-column>
      <el-table-column prop="email" label="电子邮箱"/>
      <el-table-column label="注册时间">
        <template slot-scope="scope">{{ scope.row.create_time | unixToDate }}</template>
      </el-table-column>
      <el-table-column label="上次登录时间">
        <template slot-scope="scope" v-if="scope.row.last_login">{{ scope.row.last_login | unixToDate }}</template>
      </el-table-column>
      <el-table-column prop="login_count" label="登录次数"/>
      <el-table-column prop="sex" label="性别" :formatter="formatterSex"/>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="primary"
            @click="handleRecoverMember(scope.$index, scope.row)">恢复</el-button>
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
</template>

<script>
  import * as API_Member from '@/api/member'

  export default {
    name: 'memberRecycle',
    data() {
      return {
        /** 列表loading状态 */
        loading: false,

        /** 列表参数 */
        params: {
          page_no: 1,
          page_size: 10,
          disabled: -1
        },

        /** 列表数据 */
        tableData: '',

        /** 高级搜索数据 */
        advancedForm: {}
      }
    },
    filters: {
      secrecyMobile(mobile) {
        mobile = String(mobile)
        if (!/\d{11}/.test(mobile)) {
          return mobile
        }
        return mobile.replace(/(\d{3})(\d{4})(\d{4})/, '$1****$3')
      }
    },
    mounted() {
      this.GET_RecycleMemberList()
    },
    watch: {
      memberId() {
        this.params.member_id = this.memberId
        this.GET_RecycleMemberList()
      }
    },
    methods: {
      /** 分页大小发生改变 */
      handlePageSizeChange(size) {
        this.params.page_size = size
        this.GET_RecycleMemberList()
      },

      /** 分页页数发生改变 */
      handlePageCurrentChange(page) {
        this.params.page_no = page
        this.GET_RecycleMemberList()
      },

      /** 恢复会员操作 */
      handleRecoverMember(index, row) {
        this.$confirm('确定要恢复这个会员吗？', '提示', { type: 'warning' }).then(() => {
          API_Member.recycleMember(row.member_id).then(() => {
            this.$message.success('会员恢复成功！')
            this.GET_RecycleMemberList()
          })
        }).catch(() => {})
      },

      /** 性别格式化 */
      formatterSex(row, column, cellValue) {
        return row.sex === 1 ? '男' : '女'
      },

      /** 搜索事件触发 */
      searchEvent(keyword) {
        this.params.keyword = keyword
        Object.keys(this.advancedForm).forEach(key => delete this.params[key])
        this.params.page_no = 1
        this.GET_RecycleMemberList()
      },

      /** 高级搜索事件触发 */
      advancedSearchEvent() {
        const { advancedForm } = this
        const { register_time_range, sex } = advancedForm
        Object.keys(this.advancedForm).forEach(key => {
          if (advancedForm[key] !== undefined) {
            this.params[key] = advancedForm[key]
          }
        })
        if (register_time_range && register_time_range.length === 2) {
          this.params.start_time = register_time_range[0] / 1000
          this.params.end_time = register_time_range[1] / 1000
        } else {
          delete this.params.start_time
          delete this.params.end_time
        }
        delete this.params.register_time_range
        delete this.params.keyword
        if (sex === -1) delete this.params.sex
        this.params.page_no = 1
        this.GET_RecycleMemberList()
      },

      /** 获取回收站会员列表 */
      GET_RecycleMemberList() {
        this.loading = true
        API_Member.getRecycleMemberList(this.params).then(response => {
          this.loading = false
          this.tableData = response
        }).catch(() => { this.loading = false })
      }
    }
  }
</script>
