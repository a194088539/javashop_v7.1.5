<template>
  <div class="container">
    <div class="complaint">
      <div v-if="flow" class="complaint-flow">
        <el-steps align-center :active="flow_active" finish-status="success">
          <el-step
            v-for="(step, index) in flow"
            :key="index"
            :title="step.text"
          ></el-step>
        </el-steps>
      </div>
      <el-row :gutter="0">
        <el-col :span="24">
          <div class="d-header">投诉信息</div>
          <div class="d-content">
            <div class="item">
              <span class="item-label">投诉商品：</span>
              <span class="item-value">{{ complaninInfo.goods_name }}</span>
            </div>
            <div class="item">
              <span class="item-label">投诉状态：</span>
              <span class="item-value">{{ complaninInfo.status | invoiceStatus }}</span>
            </div>
            <div class="item">
              <span class="item-label">投诉主题：</span>
              <span class="item-value">{{ complaninInfo.complain_topic }}</span>
            </div>
            <div class="item">
              <span class="item-label">投诉时间：</span>
              <span class="item-value">{{ complaninInfo.create_time | unixToDate('yyyy-MM-dd hh:mm:ss') }}</span>
            </div>
            <div class="item">
              <span class="item-label">投诉内容：</span>
              <span class="item-value">{{ complaninInfo.content }}</span>
            </div>
            <div class="item">
              <span class="item-label">投诉凭证：</span>
              <div v-if="images" class="images">
                <viewer :images="images">
                  <a
                    :key="index"
                    class="complaint-img-item"
                    v-for="(image, index) in images"
                  >
                    <img :src="image" alt="">
                  </a>
                </viewer>
                <span class="clear"></span>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="0" v-if="complaninInfo.appeal_content">
        <el-col :span="24">
          <div class="d-header">商家申诉信息</div>
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
              <div v-if="appeal_images" class="images">
                <viewer :images="appeal_images">
                  <a
                    :key="index"
                    class="complaint-img-item"
                    v-for="(image, index) in appeal_images"
                  >
                    <img :src="image" alt="">
                  </a>
                </viewer>
                <span class="clear"></span>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="0" v-if="complaninInfo.status === 'WAIT_APPEAL'">
        <el-col :span="24">
          <div class="d-header" style="margin-bottom: 18px;">申诉</div>
          <div class="d-content">
            <el-form :model="appealForm" :rules="appealRules" ref="appealForm" label-width="110px">
              <el-form-item label="申诉内容：" prop="appeal_content">
                <el-input
                  type="textarea"
                  :autosize="{ minRows: 3, maxRows: 10 }"
                  :maxlength="200"
                  placeholder="填写申诉内容，长度不超过200字符"
                  v-model="appealForm.appeal_content">
                </el-input>
              </el-form-item>
              <el-form-item label="上传凭证：">
                <el-upload
                  :action="MixinUploadApi"
                  list-type="picture-card"
                  multiple
                  :on-exceed="() => { $message.error('超过最大可上传数！') }"
                  :limit="3"
                  :on-success="(res, file, fileList) => { appealForm.images = fileList }"
                  :on-remove="(file, fileList) => { appealForm.images = fileList }"
                >
                  <i class="el-icon-plus"></i>
                  <div slot="tip" class="el-upload__tip">凭证限定3张图片</div>
                </el-upload>
              </el-form-item>
            </el-form>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="0" v-if="complaninInfo.status === 'COMMUNICATION'|| complaninInfo.status === 'WAIT_ARBITRATION' || complaninInfo.status === 'COMPLETE'">
        <el-col :span="24">
          <div class="d-header">对话详情</div>
          <div class="d-content">
            <div class="item">
              <span class="item-label">对话记录：</span>
              <div v-if="communication_list && communication_list.length" class="communication">
                <p v-for="item in communication_list">{{item.owner}}[{{item.create_time | unixToDate('yyyy-MM-dd hh:mm:ss')}}]：{{item.content}}</p>
              </div>
              <div v-else class="communication">
                暂无对话
              </div>
            </div>
            <el-form :model="dialogueForm" :rules="dialogueRules" ref="dialogueForm" label-width="110px" v-if="complaninInfo.status === 'COMMUNICATION'|| complaninInfo.status === 'WAIT_ARBITRATION'">
              <el-form-item label="发送对话：" prop="content">
                <el-input
                  type="textarea"
                  :autosize="{ minRows: 3, maxRows: 10 }"
                  :maxlength="200"
                  placeholder="填写对话，字数限制200字"
                  v-model="dialogueForm.content">
                </el-input>
              </el-form-item>
            </el-form>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="0" v-if="complaninInfo.arbitration_result">
        <el-col :span="24">
          <div class="d-header">平台仲裁</div>
          <div class="d-content">
            <div class="item">
              <span class="item-label">仲裁意见：</span>
              <span class="item-value">{{ complaninInfo.arbitration_result }}</span>
            </div>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="0">
        <el-col :span="24" class="btn">
          <el-button type="primary" v-if="complaninInfo.status === 'WAIT_APPEAL'" @click="submitAppealForm">确认提交</el-button>
          <el-button type="primary" v-if="complaninInfo.status === 'COMMUNICATION' || complaninInfo.status === 'WAIT_ARBITRATION'" @click="submitDialogueForm">发送对话</el-button>
          <el-button type="danger" v-if="complaninInfo.status === 'COMMUNICATION'" @click="submitArbitration">提交仲裁</el-button>
        </el-col>
      </el-row>
    </div>
    <div class="goods-info">
      <div class="title">交易商品信息</div>
      <div class="item-goods">
        <dl>
          <dt>
            <a :href="`${MixinBuyerDomain}/goods/${complaninInfo.goods_id}`" target="_blank">
              <img :src="complaninInfo.goods_image" :alt="complaninInfo.goods_name">
            </a>
          </dt>
          <dd>
            <a :href="`${MixinBuyerDomain}/goods/${complaninInfo.goods_id}`" target="_blank" class="goods-name">{{ complaninInfo.goods_name }}</a>
            <span>￥{{ (complaninInfo.goods_price || 0) | unitPrice }}</span>
          </dd>
        </dl>
      </div>
      <div class="item-order">
        <ul>
          <li>运费：{{ (complaninInfo.shipping_price || 0) | unitPrice }}</li>
          <li>订单金额：{{ complaninInfo.order_price }}</li>
        </ul>
        <ul>
          <li>订单编号：{{ complaninInfo.order_sn }}</li>
        </ul>
        <ul>
          <li>商家：{{ complaninInfo.seller_name }}</li>
        </ul>
        <ul>
          <li>收货人姓名：{{ complaninInfo.ship_name }}</li>
          <li>收货详细地址：{{ complaninInfo.ship_addr }}</li>
          <li>收货人电话：{{ complaninInfo.ship_mobile }}</li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
  import Vue from 'vue'
  import 'viewerjs/dist/viewer.css'
  import Viewer from 'v-viewer'
  import { Steps, Step } from 'element-ui'
  Vue.use(Steps).use(Step).use(Viewer)
  import * as API_Complaint from '@/api/complain'
  export default {
    name: 'complaintDetail',
    data() {
      return {
        // 详情信息
        complaninInfo: {},
        id: this.$route.params.id,
        // 流程图数据
        flow: '',
        flow_active: 0,
        // 投诉凭证
        images: [],
        // 商家申诉凭证
        appeal_images: [],
        // 申诉表单
        appealForm: {},
        // 对话表单
        dialogueForm: {},
        // 对话记录
        communication_list: [],

        // 申诉表单验证
        appealRules: {
          appeal_content: [{ required: true, message: '请输入申诉内容！', trigger: 'blur' }]
        },

        // 对话表单验证
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
      this.GET_ComplaintDetail()
      next()
    },
    activated() {
      this.id = this.$route.params.id
      this.GET_ComplaintDetail()
    },
    mounted() {
      this.GET_ComplaintDetail()
    },
    methods: {
      /* 提交仲裁 */
      submitArbitration() {
        API_Complaint.arbitrationProcess(this.id).then(response => {
          this.$message.success('提交仲裁成功！')
          this.$store.dispatch('delCurrentViews', {
            view: this.$route,
            $router: this.$router
          })
          this.$router.push({
            name: 'complaintList'
          })
        })
      },
      /* 发送对话 */
      submitDialogueForm() {
        this.$refs['dialogueForm'].validate((valid) => {
          if (valid) {
            let params = JSON.parse(JSON.stringify(this.dialogueForm))
            API_Complaint.initiationSession(this.id, params).then(response => {
              this.GET_ComplaintDetail()
              this.dialogueForm.content = ''
            })
          } else {
            return false
          }
        })
      },
      /* 提交商家申诉 */
      submitAppealForm() {
        this.$refs['appealForm'].validate((valid) => {
          if (valid) {
            let params = JSON.parse(JSON.stringify(this.appealForm))
            params.images && (params.images = params.images.map(item => item.response && item.response.url))
            API_Complaint.submitAppeal(this.id, params).then(response => {
              this.$message.success('申诉提交成功！')
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
      /* 获取详情页信息 */
      GET_ComplaintDetail() {
        API_Complaint.getComplainDetail(this.id).then(response => {
          this.complaninInfo = response
          this.images = response.images ? response.images.split(',') : []
          this.appeal_images = response.appeal_images ? response.appeal_images.split(',') : []
          this.communication_list = response.communication_list ? response.communication_list.reverse() : []
        })
        // 交易投诉流程
        API_Complaint.getComplaintFlow(this.id).then(response => {
          this.flow = response
          const index = response.findIndex(item => item.show_status === 0)
          this.flow_active = index === -1 ? response.length : index
        })
      }
    }
  }
</script>

<style lang="scss" scoped>
  .container{
    padding:40px 0 40px 20px;
    background: #fff;
    display: flex;
    .btn{
      padding-left: 108px;
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
    }
    .complaint{
      width: 1200px;
      border-right: solid 1px #F5F5F5;
      padding-right: 20px;
      .complaint-flow {
        width: 100%;
        padding: 10px 0 40px;
        /deep/ {
          .el-step__head, .el-step__title {
            &.is-success, &.is-finish {
              color:#f42424;
              border-color:#f42424;
            }
          }
        }
      }
      .el-row{
        padding-left:30px;
      }
      .d-header {
        padding: 10px 0 10px 20px;
        font-size: 12px;
        color: #555;
        background-color: #F5F5F5;
        border-bottom: 1px solid #E7E7E7;
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
        .complaint-img-item{
          float: left;
          display: block;
          width: 50px;
          height: 50px;
          margin-right: 10px;
          cursor: pointer;
          border: 1px solid #e2e2e2;
          padding: 1px;
          img{
            width: 100%;
            height: 100%;
          }
        }
      }
    }
    .goods-info{
      float: right;
      width:280px;
      padding-left: 20px;
      .title{
        font-size: 14px;
        font-weight: 600;
        padding: 10px 0;
        border-bottom: solid 1px #EEEEEE;
      }
      .item-goods{
        overflow: hidden;
        padding:10px 0;
        dt{
          float: left;
          width: 40px;
          height: 40px;
          border: solid 1px #F5F5F5;
          margin: 0 10px;
          img{
            width: 40px;
            height: 40px;
          }
        }
        dd{
          .goods-name{
            display: block;
            text-overflow: ellipsis;
            white-space: nowrap;
            overflow: hidden;
            font-size: 14px;
            color: #666;
            padding-bottom: 5px;
            span{
              font-size: 14px;
              color: #666;
            }
          }
        }
      }
      .item-order{
        padding-top: 10px;
        ul{
          list-style: none;
          border-top: solid 1px #EEEEEE;
          padding:5px 0;
          li{
            line-height: 20px;
            font-size: 14px;
            color: #666;
          }
        }
      }
    }
  }

</style>
