<template>
  <div id="complaint-detail">
    <div class="complaint-detail-container">
      <div class="complaint-detail-left">
        <div class="title">
          <h3>交易投诉申请</h3>
        </div>
        <div v-if="flow" class="complaint-flow">
          <el-steps align-center :active="flow_active" finish-status="success">
            <el-step
              v-for="(step, index) in flow"
              :key="index"
              :title="step.text"
            ></el-step>
          </el-steps>
        </div>
        <div class="complaint-info">
          <h3>投诉信息</h3>
          <div class="info-content">
            <ul>
              <li>投诉商品：{{ complaintDetail.goods_name }}</li>
              <li>投诉主题：{{ complaintDetail.complain_topic }}</li>
              <li>投诉时间：{{ complaintDetail.create_time | unixToDate }}</li>
              <li>投诉内容：{{ complaintDetail.content }}</li>
              <li>投诉凭证：<div v-if="images" class="images">
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
              </li>
            </ul>
          </div>
        </div>
        <div class="complaint-info" v-if="complaintDetail.appeal_content">
          <h3>商家申诉信息</h3>
          <div class="info-content">
            <ul>
              <li>申诉时间：{{ complaintDetail.appeal_time | unixToDate }}</li>
              <li>申诉内容：{{ complaintDetail.appeal_content }}</li>
              <li>申诉凭证：<div v-if="appeal_images" class="images">
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
              </li>
            </ul>
          </div>
        </div>
        <div class="complaint-info" v-if="complaintDetail.status === 'COMMUNICATION'|| complaintDetail.status === 'WAIT_ARBITRATION' || complaintDetail.status === 'COMPLETE'">
          <h3>对话详情</h3>
          <div class="info-content">
            <div style="display: flex;margin-bottom:20px;">
              <span style="width: 85px">对话记录：</span>
              <div v-if="communication_list && communication_list.length" class="communication">
                <p v-for="item in communication_list" :key="item.communication_id">{{item.owner}}[{{item.create_time | unixToDate('yyyy-MM-dd hh:mm:ss')}}]：{{item.content}}</p>
              </div>
              <div class="communication" v-else>
                <p>暂无对话</p>
              </div>
            </div>
            <el-form :model="dialogueForm" :rules="dialogueRules" ref="dialogueForm" label-width="85px" v-if="complaintDetail.status === 'COMMUNICATION' || complaintDetail.status === 'WAIT_ARBITRATION'">
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
        </div>
        <div class="complaint-info" v-if="complaintDetail.arbitration_result">
          <h3>平台仲裁</h3>
          <div class="info-content">
            <ul>
              <li>仲裁意见：{{ complaintDetail.arbitration_result }}</li>
            </ul>
          </div>
        </div>
        <div class="bottom">
          <el-button type="primary" size="small" @click="submitDialogueForm" v-if="complaintDetail.status === 'COMMUNICATION' || complaintDetail.status === 'WAIT_ARBITRATION'">发送对话</el-button>
          <el-button type="primary" size="small" @click="submitArbitration" v-if="complaintDetail.status === 'COMMUNICATION'">提交仲裁</el-button>
          <nuxt-link to="/member/my-complaint" class="return-btn">返回列表</nuxt-link>
        </div>
      </div>
      <div class="complaint-detail-right">
        <div class="title">交易商品信息</div>
        <div class="item-goods">
          <dl>
            <dt>
              <a :href="'/goods/' + complaintDetail.goods_id">
                <img :src="complaintDetail.goods_image" :alt="complaintDetail.goods_name">
              </a>
            </dt>
            <dd>
              <a :href="'/goods/' + complaintDetail.goods_id" class="goods-name">{{ complaintDetail.goods_name }}</a>
              <span>￥{{ (complaintDetail.goods_price || 0) | unitPrice }}</span>
            </dd>
          </dl>
        </div>
        <div class="item-order">
          <ul>
            <li>运费：{{ (complaintDetail.shipping_price || 0) | unitPrice }}</li>
            <li>订单金额：{{ complaintDetail.order_price }}</li>
          </ul>
          <ul>
            <li>订单编号：{{ complaintDetail.order_sn }}</li>
          </ul>
          <ul>
            <li>商家：{{ complaintDetail.seller_name }}</li>
          </ul>
        </div>
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
  import * as API_Complaint from '@/api/complaint'
  export default {
    name: 'complaint-detail',
    data() {
      return {
        // 交易投诉id
        id: this.$route.query.id,
        // 交易投诉详情数据
        complaintDetail: '',
        // 投诉凭证
        images: [],
        // 商家申诉凭证
        appeal_images: [],
        // 对话表单
        dialogueForm: {},
        // 对话记录
        communication_list: [],
        dialogueRules: {
          content: [{ required: true, message: '请输入对话内容！', trigger: 'blur' }],
        },
        // 流程图数据
        flow: '',
        flow_active: 0
      }
    },
    mounted() {
      this.GET_complaintDetail()
    },
    methods: {
      /* 提交仲裁 */
      submitArbitration() {
        API_Complaint.arbitrationProcess(this.id).then(response => {
          this.$message.success('提交仲裁成功！')
          this.$router.replace({ path: '/member/my-complaint' })
        })
      },
      /* 发送对话 */
      submitDialogueForm() {
        this.$refs['dialogueForm'].validate((valid) => {
          if(valid){
            let params = JSON.parse(JSON.stringify(this.dialogueForm))
            API_Complaint.initiationSession(this.id, params).then(response => {
              this.GET_complaintDetail()
              this.dialogueForm.content = ''
            })
          } else {
            return false
          }
        })
      },
      /* 获取详情信息 */
      GET_complaintDetail() {
        API_Complaint.getComplaintDetail(this.id).then(response => {
          this.complaintDetail = response
          this.images = response.images ? response.images.split(',') : []
          this.appeal_images = response.appeal_images ? response.appeal_images.split(',') : []
          this.communication_list = response.communication_list ? response.communication_list.reverse() : []
        })
        // 交易投诉流程
        API_Complaint.getComplaintFlow(this.id).then(response => {
          this.flow = response
          const index = response.findIndex(item => item.show_status === 0);
          this.flow_active = index === -1 ? response.length : index
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../../assets/styles/color";
  /deep/ .el-form-item__label {
    font-size: 12px;
    color:#666;
  }
  .complaint-detail-container{
    display: flex;
    .communication{
      background-color: #FFFFFF;
      border: 1px dashed #EEEEEE;
      max-height: 200px;
      word-break: normal;
      word-wrap: break-word;
      padding: 8px;
      overflow-y: scroll;
      width: 81%;
    }
    .complaint-detail-left{
      width: 615px;
      padding: 0 20px 0 0;
      border-right: solid 1px #F5F5F5;
      .title{
        height: 20px;
        padding: 10px 0;
        border-bottom: solid 1px #EEEEEE;
        h3{
          font-size: 14px;
          font-weight: 600;
          display: inline-block;
        }
      }
      .complaint-flow {
        width: 100%;
        padding: 30px 0;
        /deep/ {
          .el-step__head, .el-step__title {
            &.is-success, &.is-finish {
              color: lighten($color-main, 10%);
              border-color: lighten($color-main, 10%);
            }
          }
        }
      }
      .complaint-info{
        h3{
          font-weight: 600;
          line-height: 22px;
          color: #555;
          clear: both;
          background-color: #F5F5F5;
          padding: 5px 0 5px 12px;
          border-bottom: solid 1px #E1E1E1;
        }
        .info-content{
          padding: 20px 30px;
          li{
            line-height: 32px;
            font-size: 12px;
            display: flex;
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
      }
      .bottom{
        border-top: dotted 1px #E6E6E6;
        text-align: center;
        display: flex;
        height: 32px;
        padding:10px 0 0;
        justify-content: center;
        .return-btn{
          display: block;
          width: 80px;
          height: 30px;
          line-height: 30px;
          border: 1px solid #ccc;
          text-align: center;
          background: #f9f9f9;
          color: #666;
          cursor: pointer;
          margin-left: 10px;
          &:first-child {
            margin-top: 5px;
          };
          &:hover {
            background: #eaeaea;
          }
        }
      }
    }
    .complaint-detail-right{
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
          }
        }
      }
      .item-order{
        padding-top: 10px;
        ul{
          border-top: solid 1px #EEEEEE;
          padding:5px 0;
          li{
            line-height: 20px;
          }
        }
      }
    }
  }
</style>
