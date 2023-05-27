<template>
  <div>
    <en-table-layout
      :table-data="tableData.data"
      :loading="loading"
    >
      <div slot="toolbar" class="inner-toolbar">
        <div class="toolbar-btns">
          <el-button size="mini" type="primary" icon="el-icon-circle-plus-outline" @click="handleAddSensitiveWords">添加</el-button>
        </div>
        <div class="toolbar-search">
          <en-table-search @search="searchEvent" placeholder="请输入敏感词"/>
        </div>
      </div>
      <template slot="table-columns">
        <el-table-column prop="word_name" label="敏感词"/>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="danger"
              @click="handleDeleSensitiveWords(scope.$index, scope.row)">删除</el-button>
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
    <el-dialog
      title="添加敏感词"
      :visible.sync="dialogsensitiveWords"
      width="500px">
      <el-form :model="sensitiveWordsForm" :rules="sensitiveWordsRules" ref="sensitiveWordsForm" size="small" label-width="120px">
        <el-form-item label="敏感词名称" prop="word_name">
          <el-input v-model="sensitiveWordsForm.word_name"/>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogsensitiveWords = false">关 闭</el-button>
        <el-button type="primary" @click="submitSensitiveWords">保 存</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
    import * as API_sensitiveWords from '@/api/sensitiveWords'
    export default {
      name: 'sensitiveWords',
      data() {
        return {
          /** 列表loading状态 */
          loading: false,
          /** 列表数据 */
          tableData: '',
          /** 列表参数 */
          params: {
            page_no: 1,
            page_size: 10
          },

          /** 添加敏感词  dialog*/
          dialogsensitiveWords: false,

          /** 添加敏感词表单*/
          sensitiveWordsForm: {},

          /** 添加敏感词表单*/
          sensitiveWordsRules: {
            word_name: [
              this.MixinRequired('请输入敏感词名称！'),
              { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur' }
            ]
          }
        }
      },
      mounted() {
        this.GET_SensitiveWordsList()
      },
      methods: {
        /** 分页大小发生改变 */
        handlePageSizeChange(size) {
          this.params.page_size = size
          this.GET_SensitiveWordsList()
        },

        /** 分页页数发生改变 */
        handlePageCurrentChange(page) {
          this.params.page_no = page
          this.GET_SensitiveWordsList()
        },

        /** 添加敏感词 */
        handleAddSensitiveWords() {
          this.dialogsensitiveWords = true
        },

        /** 搜索事件触发 */
        searchEvent(data) {
          this.params.page_no = 1
          this.params.keyword = data
          if (!data) delete this.params.keyword
          this.GET_SensitiveWordsList()
        },

        /** 添加敏感词 表单提交 */
        submitSensitiveWords() {
          this.$refs['sensitiveWordsForm'].validate((valid) => {
            if (valid) {
              API_sensitiveWords.addSensitiveWords(this.sensitiveWordsForm).then(() => {
                this.dialogsensitiveWords = false
                this.$message.success('保存成功！')
                this.GET_SensitiveWordsList()
              })
            } else {
              this.$message.error('表单填写有误，请检查！')
              return false
            }
          })
        },

        /** 删除敏感词 */
        handleDeleSensitiveWords(index, row) {
          API_sensitiveWords.deleSensitiveWords(row.id).then(() => {
            this.$message.success('删除成功！')
            this.GET_SensitiveWordsList()
          })
        },

        /** 获取敏感词列表 */
        GET_SensitiveWordsList() {
          this.loading = true
          API_sensitiveWords.getSensitiveWordsList(this.params).then(response => {
            this.loading = false
            this.tableData = response
          }).catch(() => {
            this.loading = false
          })
        }
      }
    }
</script>

<style scoped>

</style>
