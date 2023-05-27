<template>
    <div id="complaint-apply">
      <div class="member-nav">
        <ul class="member-nav-list">
          <li class="active">
            交易投诉申请
          </li>
        </ul>
      </div>
      <div class="complaint-apply-container">
        <div class="complaint-apply-left">
          <el-form :model="complaintForm" :rules="complaintRules" ref="complaintForm" label-width="110px">
            <el-form-item label="选择投诉主题：" prop="theme">
              <template>
                <el-radio-group v-model="complaintForm.complain_topic">
                  <el-radio v-for="item in radioTypes" :label="item.topic_name" :key="item.topic_id">{{item.topic_name}} <p class="topic-remark">{{item.topic_remark}}</p> </el-radio>
                </el-radio-group>
              </template>
            </el-form-item>
            <el-form-item label="投诉内容：" prop="content">
              <el-input
                type="textarea"
                :autosize="{ minRows: 3, maxRows: 10 }"
                :maxlength="200"
                placeholder="填写投诉内容，字数限制200字"
                v-model="complaintForm.content">
              </el-input>
            </el-form-item>
            <el-form-item label="上传凭证：">
              <el-upload
                :action="MixinUploadApi"
                list-type="picture-card"
                multiple
                :on-exceed="() => { $message.error('超过最大可上传数！') }"
                :limit="3"
                :on-success="(res, file, fileList) => { complaintForm.images = fileList }"
                :on-remove="(file, fileList) => { complaintForm.images = fileList }"
              >
                <i class="el-icon-plus"></i>
                <div slot="tip" class="el-upload__tip">凭证限定3张图片</div>
              </el-upload>
            </el-form-item>
            <el-form-item>
              <el-button type="danger" @click="handleSubmit">确认提交</el-button>
              <el-button @click="$router.back()">取消并返回</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div class="complaint-apply-right">
          <div class="title">相关商品交易信息</div>
          <div class="item-goods">
            <dl v-if="skuList[0]">
              <dt>
                <a :href="'/goods/' + skuList[0].goods_id">
                  <img :src="skuList[0].goods_image" :alt="skuList[0].name">
                </a>
              </dt>
              <dd>
                <a :href="'/goods/' + skuList[0].goods_id" class="goods-name">{{skuList[0].name}}</a>
                <span>￥{{ (order.goods_price || 0) | unitPrice }}</span>
              </dd>
            </dl>
          </div>
          <div class="item-order">
            <ul>
              <li>订单编号：{{ order.sn }}</li>
              <li>下单时间：{{ order.create_time | unixToDate }}</li>
              <li>支付方式：{{ order.payment_type === "ONLINE" ? '在线支付' : '货到付款' }}</li>
            </ul>
          </div>
        </div>
      </div>
    </div>
</template>

<script>
    import Vue from 'vue'
    import * as API_Complaint from '@/api/complaint'
    import * as API_Order from '@/api/order'
    import { Upload, RadioGroup, Radio } from 'element-ui'
    Vue.use(Upload).use(RadioGroup).use(Radio)
    export default {
      name: 'complaint-apply',
      head() {
        return {
          title: `投诉交易申请-${this.site.title}`
        }
      },
      data() {
        return {
          // 申请表单
          complaintForm: {},
          // 申请表单 表单规则
          complaintRules: {
            content: [{ required: true, message: '请选择投诉主题！', trigger: 'blur' }],
            content: [{ required: true, message: '请输入投诉内容！', trigger: 'blur' }],
          },
          radioTypes: [],
          order_sn: this.$route.query.order_sn,
          order: '',
          skuList: ''
        }
      },
      mounted() {
        this.GET_ComplaintTheme()
        this.GET_OrderDetail()
      },
      methods: {
        /** 提交表单 */
        handleSubmit() {
          this.$refs['complaintForm'].validate((valid) => {
            if (valid) {
              let params = JSON.parse(JSON.stringify(this.complaintForm))
              params.images && (params.images = params.images.map(item => item.response && item.response.url))
              params = { ...params, ...this.$route.query }
              API_Complaint.appendComplaint(params).then(response => {
                this.$message.success('投诉申请提交成功！')
                this.$router.replace({ path: '/member/my-complaint' })
              }).catch(error => {
                this.$message.error('投诉申请提交失败！')
              })
            } else {
              return false
            }
          })
        },
        /** 获取投诉主题列表 */
        GET_ComplaintTheme() {
          API_Complaint.getComplaintTheme().then(response => {
            if (Array.isArray(response) && response.length) {
              this.radioTypes = response
              this.$set(this.complaintForm, 'complain_topic', response[0].topic_name)
            }
          })
        },
        GET_OrderDetail() {
          API_Order.getOrderDetail(this.order_sn).then(response => {
            this.order = response
            this.skuList = JSON.parse(response.items_json)
          })
        }
      }
    }
</script>

<style type="text/scss" lang="scss" scoped>
  .member-nav-list{
    li{
      padding:0 20px;
    }
  }
  .topic-remark{
    overflow: hidden;
    width: 543px;
    word-wrap: break-word;
    white-space: normal;
    word-break: break-all;
  }
 .complaint-apply-container{
   padding-top:30px;
   overflow: hidden;
   .complaint-apply-left{
     float: left;
     width: 650px;
     padding: 0 20px;
     border-right: solid 1px #F5F5F5;
   }
   .complaint-apply-right{
     float: right;
     width:280px;
     .title{
       font-size: 14px;
       font-weight: 600;
       padding: 10px 0;
       border-bottom: solid 1px #EEEEEE;
     }
     .item-goods{
       overflow: hidden;
       padding:10px 0;
       border-bottom: solid 1px #EEEEEE;
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
       li{
         line-height: 20px;
       }
     }
   }
 }
</style>
