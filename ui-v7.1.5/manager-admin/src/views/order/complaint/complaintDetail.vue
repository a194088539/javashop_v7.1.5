<template>
    <div class="detail-table">
      <el-row :gutter="0">
        <el-col :span="24">
          <div class="d-header">订单信息</div>
          <div class="d-content">
            <div class="item">
              <span class="item-label">订单号：</span>
              <span class="item-value">{{ complaninInfo.order_sn }}</span>
            </div>
            <div class="item">
              <span class="item-label">下单时间：</span>
              <span class="item-value">{{ complaninInfo.order_time | unixToDate }}</span>
            </div>
            <div class="item">
              <span class="item-label">订单金额：</span>
              <span class="item-value">{{ complaninInfo.order_price }}</span>
            </div>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="0">
        <el-col :span="24">
          <div class="d-header">投诉商品</div>
          <div class="d-content">
            <div class="item">
              <span class="item-label">商品名称：</span>
              <span class="item-value">{{ complaninInfo.goods_name }}</span>
            </div>
            <div class="item">
              <span class="item-label">单价：</span>
              <span class="item-value">{{ complaninInfo.goods_price }}</span>
            </div>
            <div class="item">
              <span class="item-label">购买数量：</span>
              <span class="item-value">{{ complaninInfo.num }}</span>
            </div>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="0">
        <el-col :span="24">
          <div class="d-header">投诉信息</div>
          <div class="d-content">
            <div class="item">
              <span class="item-label">投诉人：</span>
              <span class="item-value">{{ complaninInfo.member_name }}</span>
            </div>
            <div class="item">
              <span class="item-label">投诉时间：</span>
              <span class="item-value">{{ complaninInfo.create_time | unixToDate('yyyy-MM-dd hh:mm:ss') }}</span>
            </div>
            <div class="item">
              <span class="item-label">被投诉商家：</span>
              <span class="item-value">{{ complaninInfo.seller_name }}</span>
            </div>
            <div class="item">
              <span class="item-label">投诉主题：</span>
              <span class="item-value">{{ complaninInfo.complain_topic }}</span>
            </div>
            <div class="item">
              <span class="item-label">投诉内容：</span>
              <span class="item-value">{{ complaninInfo.content }}</span>
            </div>
            <div class="item">
              <span class="item-label">投诉状态：</span>
              <span class="item-value">{{ complaninInfo.status | invoiceStatus }}</span>
            </div>
            <div class="item">
              <span class="item-label">投诉凭证：</span>
              <div v-for="(image, index) in images" class="images" v-if="images" :key="index"><img :src="image" alt=""></div>
            </div>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="0" v-if="complaninInfo.appeal_content">
        <el-col :span="24">
          <div class="d-header">申诉信息</div>
          <div class="d-content">
            <div class="item">
              <span class="item-label">申诉时间：</span>
              <span class="item-value">{{ complaninInfo.appeal_time | unixToDate('yyyy-MM-dd hh:mm:ss') }}</span>
            </div>
            <div class="item">
              <span class="item-label">申诉内容：</span>
              <span class="item-value">{{ complaninInfo.appeal_content }}</span>
            </div>
            <div class="item">
              <span class="item-label">申诉凭证：</span>
              <div v-for="image in appeal_images" class="images" v-if="appeal_images"><img :src="image" alt=""></div>
            </div>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="0" v-if="complaninInfo.status === 'COMMUNICATION' || complaninInfo.status === 'WAIT_ARBITRATION' || complaninInfo.status === 'COMPLETE'">
        <el-col :span="24">
          <div class="d-header">对话详情</div>
          <div class="d-content" style="padding-bottom:20px;">
            <div class="item">
              <span class="item-label">对话记录：</span>
              <div v-if="communication_list && communication_list.length" class="communication">
                <p v-for="item in communication_list">{{item.owner}}[{{item.create_time | unixToDate('yyyy-MM-dd hh:mm:ss')}}]：{{item.content}}</p>
              </div>
              <div v-else class="communication">
                暂无对话
              </div>
            </div>
            <el-form :model="dialogueForm" :rules="dialogueRules" ref="dialogueForm" label-width="110px" v-if="complaninInfo.status === 'COMMUNICATION' || complaninInfo.status === 'WAIT_ARBITRATION'">
              <el-form-item label="发送对话：" prop="content">
                <el-input
                  type="textarea"
                  :autosize="{ minRows: 3, maxRows: 10 }"
                  :maxlength="200"
                  placeholder="填写对话，字数限制200字"
                  v-model="dialogueForm.content">
                </el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" v-if="complaninInfo.status === 'COMMUNICATION' || complaninInfo.status === 'WAIT_ARBITRATION'" @click="submitDialogueForm">发送对话</el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="0">
        <el-col :span="24">
          <div class="d-header">平台仲裁</div>
          <div class="d-content" v-if="complaninInfo.arbitration_result">
            <div class="item">
              <span class="item-label">仲裁意见：</span>
              <span class="item-value">{{ complaninInfo.arbitration_result }}</span>
            </div>
          </div>
          <div class="d-content" v-if="isShow">
            <el-form :model="arbitrationForm" :rules="arbitrationRules" ref="arbitrationForm" label-width="110px">
              <el-form-item label="仲裁意见：" prop="arbitration_result">
                <el-input
                  type="textarea"
                  :autosize="{ minRows: 3, maxRows: 10 }"
                  :maxlength="500"
                  placeholder="填写仲裁意见，不超过500字符"
                  width="500px"
                  v-model="arbitrationForm.arbitration_result"/>
              </el-form-item>
            </el-form>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="0">
        <el-col class="btn">
          <el-button type="primary" @click="handleExamine" v-if="complaninInfo.status === 'NEW'">审核并交由商家申诉</el-button>
          <el-button type="primary" @click="handleArbitration" v-if="complaninInfo.status !== 'COMPLETE' && !isShow">直接仲裁结束投诉流程</el-button>
          <el-button type="primary" @click="submitArbitrationForm" v-if="isShow">提交保存仲裁意见</el-button>
        </el-col>
      </el-row>
    </div>
</template>

<script>
  import * as API_Complaint from '@/api/complaint'

  export default {
    name: 'complaintDetail',
    data() {
      return {
        // 详情信息
        complaninInfo: {},
        id: this.$route.params.id,
        // 投诉凭证
        images: [],
        // 申诉凭证
        appeal_images: [],
        // 仲裁表单
        arbitrationForm: {},
        // 对话表单
        dialogueForm: {},
        // 是否显示仲裁表单
        isShow: false,
        // 对话记录
        communication_list: [],
        // 仲裁表单验证
        arbitrationRules: {
          arbitration_result: [{ required: true, message: '请输入仲裁意见！', trigger: 'blur' }]
        },
        dialogueRules: {
          content: [{ required: true, message: '请输入对话内容！', trigger: 'blur' }]
        }
      }
    },
    filters: {
      /* 申诉状态 过滤 */
      invoiceStatus(status) {
        switch (status) {
          case 'NEW': return '新投诉'
          case 'WAIT_APPEAL': return '待申诉'
          case 'CANCEL': return '已撤销'
          case 'COMMUNICATION': return '对话中'
          case 'WAIT_ARBITRATION': return '等待仲裁'
          case 'COMPLETE': return '已完成'
        }
      }
    },
    beforeRouteUpdate(to, from, next) {
      this.id = to.params.id
      this.GET_ComplainInfo()
      next()
    },
    activated() {
      this.id = this.$route.params.id
      this.GET_ComplainInfo()
    },
    mounted() {
      this.GET_ComplainInfo()
    },
    methods: {
      /* 发送对话 */
      submitDialogueForm() {
        this.$refs['dialogueForm'].validate((valid) => {
          if (valid) {
            let params = JSON.parse(JSON.stringify(this.dialogueForm))
            API_Complaint.initiationSession(this.id, params).then(response => {
              this.GET_ComplainInfo()
              this.dialogueForm.content = ''
            })
          } else {
            return false
          }
        })
      },
      /* 显示仲裁表单 */
      handleArbitration() {
        this.isShow = true
      },
      /* 提交仲裁意见 */
      submitArbitrationForm() {
        this.$refs['arbitrationForm'].validate((valid) => {
          if (valid) {
            const params = this.MixinClone(this.arbitrationForm)
            API_Complaint.arbitrationProcess(this.id, params).then(response => {
              this.$message.success('仲裁成功！')
              this.$store.dispatch('delCurrentViews', {
                view: this.$route,
                $router: this.$router
              })
              this.$router.push({
                name: 'complaintList'
              })
            })
          }
        })
      },
      /* 审核并交由商家申诉 */
      handleExamine() {
        API_Complaint.examineComplain(this.id).then(response => {
          this.$message.success('审核完成！')
          this.$store.dispatch('delCurrentViews', {
            view: this.$route,
            $router: this.$router
          })
          this.$router.push({
            name: 'complaintList'
          })
        })
      },
      /* 获取投诉订单的详细信息 */
      GET_ComplainInfo() {
        API_Complaint.getComplainInfo(this.id).then(response => {
          this.complaninInfo = response
          this.images = response.images ? response.images.split(',') : []
          this.appeal_images = response.appeal_images ? response.appeal_images.split(',') : []
          this.communication_list = response.communication_list ? response.communication_list.reverse() : []
        })
      }
    }
  }
</script>

<style lang="scss" scoped>
  .el-form-item{
    margin:20px 0 0;
    width: 50%;
  }
  /deep/ .el-form-item__label {
    color:#999;
  }
.detail-table {
  display: block;
  width: 100%;
  box-sizing: border-box;
  background: #fff;
  font-size: 14px;
  color:#333;
  padding-bottom: 20px;
  .btn {
    padding:20px 0 0 20px;
  }
  .communication{
    background-color: #fff;
    border: 1px dashed #eee;
    max-height: 200px;
    overflow-y: scroll;
    padding: 8px;
    word-break: normal;
    word-wrap: break-word;
    font-size: 12px;
    line-height: 30px;
    color:#666;
    width: 25%;
  }
  .d-header {
    padding: 10px 0 10px 20px;
    font-size: 14px;
    color: #555;
    background-color: #F5F5F5;
    border-bottom: 1px solid #E7E7E7;
    font-weight: 600;
    &h2 {
      font-size: 14px;
      font-weight: 400;
    }
  }
  .item {
    width: 100%;
    position: relative;
    display: inline-block;
    vertical-align: top;
    box-sizing: border-box;
    padding: 16px 0 16px 20px;
    line-height: 24px;
    white-space: nowrap;
    & .item-label {
      float: left;
      margin-right: 20px;
      font-size: 14px;
      color: #999;
    }
    & .item-value {
      overflow: hidden;
      white-space: normal;
      word-break: break-all;
      font-size: 14px;
      color: #666;
    }
  }
  .images{
    float: left;
    width: 50px;
    height: 50px;
    margin-right: 10px;
    img{
      width: 100%;
      height: 100%;
    }
  }
}
</style>
