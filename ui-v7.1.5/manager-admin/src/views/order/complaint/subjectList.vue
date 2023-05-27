<template>
    <div>
      <en-table-layout
        :tableData="tableData.data"
        :loading="loading"
      >
        <div slot="toolbar" class="inner-toolbar">
          <div class="toolbar-btns">
            <el-button size="mini" type="primary" icon="el-icon-circle-plus-outline" @click="handleAddSubject">添加</el-button>
          </div>
          <div class="toolbar-search">
          </div>
        </div>
        <template slot="table-columns">
          <el-table-column prop="topic_name" label="投诉主题"/>
          <el-table-column prop="topic_remark" label="投诉主题描述"/>
          <el-table-column label="操作" width="180">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="primary"
                @click="handleEditSubject(scope.$index, scope.row)">修改</el-button>
              <el-button
                size="mini"
                type="danger"
                @click="handleDeleteSubject(scope.row)">删除</el-button>
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
        title="新增投诉主题"
        :visible.sync="dialogAddSubjectVisible"
        width="500px">
        <el-form :model="subjectForm" :rules="subjectRules" ref="subjectForm">
          <el-form-item label="投诉主题" prop="topic_name">
            <el-input v-model="subjectForm.topic_name" :minlength="2" :maxlength="30" placeholder="填写主题，2-30个字符"></el-input>
          </el-form-item>
          <el-form-item label="主题描述" prop="topic_remark">
            <el-input type="textarea" v-model="subjectForm.topic_remark" :maxlength="100" placeholder="填写主题描述，最多100个字符"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogAddSubjectVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitAddSubjectForm">确 定</el-button>
        </div>
      </el-dialog>
    </div>
</template>

<script>
    import * as API_Complaint from '@/api/complaint'
    export default {
      name: 'subjectList',
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
          tableData: '',

          /** 添加主题  dialog*/
          dialogAddSubjectVisible: false,

          /** 添加主题  表单数据*/
          subjectForm: {},
          subjectRules: {
            topic_name: [
              this.MixinRequired('请输入投诉主题！'),
              { min: 2, max: 20, message: '长度在 2 到 30 个字符', trigger: 'blur' }
            ],
            topic_remark: [this.MixinRequired('请输入主题描述！')]
          }
        }
      },
      mounted() {
        this.GET_SubjectList()
      },
      methods: {
        /** 分页大小发生改变 */
        handlePageSizeChange(size) {
          this.params.page_size = size
          this.GET_SubjectList()
        },

        /** 分页页数发生改变 */
        handlePageCurrentChange(page) {
          this.params.page_no = page
          this.GET_SubjectList()
        },

        /** 显示添加弹框 */
        handleAddSubject() {
          this.subjectForm = {}
          this.dialogAddSubjectVisible = true
        },

        /** 提交添加投诉主题表单 */
        submitAddSubjectForm() {
          const { topic_id } = this.subjectForm
          const params = this.MixinClone(this.subjectForm)
          this.$refs.subjectForm.validate((valid) => {
            if (!valid) {
              this.$message.error('表单输入有误！')
              return false
            }
            if (topic_id) {
              API_Complaint.modifyComplainSubject(topic_id, params).then(response => {
                this.dialogAddSubjectVisible = false
                this.$message.success('修改成功！')
                this.GET_SubjectList()
              })
            } else {
              API_Complaint.addComplainSubject(params).then(response => {
                this.dialogAddSubjectVisible = false
                this.$message.success('添加成功！')
                this.GET_SubjectList()
              })
            }
          })
        },

        /** 删除投诉主题 */
        handleDeleteSubject(row) {
          API_Complaint.deleteComplainSubject(row.topic_id).then(response => {
            this.$message.success('删除成功！')
            this.GET_SubjectList()
          })
        },
        handleEditSubject(index, row) {
          this.subjectForm = this.MixinClone(row)
          this.dialogAddSubjectVisible = true
        },
        /** 获取投诉主题列表 */
        GET_SubjectList() {
          this.loading = true
          API_Complaint.getComplainSubjectList(this.params).then(response => {
            this.loading = false
            this.tableData = response
          }).catch(() => { this.loading = false })
        }
      }
    }
</script>

<style scoped>

</style>
