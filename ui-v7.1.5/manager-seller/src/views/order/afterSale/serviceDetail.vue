<template>
  <div id="serviceDetail" style="background: #fff">
    <el-row :gutter="0">
      <el-col :span="24">
        <div class="d-header">商品列表</div>
        <el-table :data="serviceDetail.goods_list" :header-cell-style="{textAlign: 'center'}">
          <el-table-column label="商品图片" width="180">
            <template slot-scope="scope">
              <a :href="`${MixinBuyerDomain}/goods/${scope.row.goods_id}`" target="_blank">
                <img :src="scope.row.goods_image" :title="scope.row.goods_name" :alt="scope.row.goods_name" class="goods-image"/>
              </a>
            </template>
          </el-table-column>
          <el-table-column label="商品名称" align="center">
            <template slot-scope="scope">
              <a :href="`${MixinBuyerDomain}/goods/${scope.row.goods_id}`" class="goods-name" target="_blank">{{ scope.row.goods_name }}</a>
            </template>
          </el-table-column>
          <el-table-column label="商品价格" width="150">
            <template slot-scope="scope">￥{{ scope.row.price }}</template>
          </el-table-column>
          <el-table-column prop="price" label="商品价格"/>
          <el-table-column prop="ship_num" label="购买数量"/>
          <el-table-column prop="return_num" label="申请售后数量"/>
          <el-table-column prop="storage_num" label="入库数量" v-if="allowable.allow_show_storage_num"/>
        </el-table>
      </el-col>
    </el-row>
    <el-row :gutter="0">
      <el-col :span="24">
        <div class="d-header">服务单信息</div>
        <div class="d-content submit-from" v-if="allowable.allow_audit">
          <div style="margin: 20px;">
            <el-alert v-if="refundShow" title="最终实际退还给用户的金额将以此处填写的金额为准，请谨慎填写避免出现纠纷" type="warning" :closable="false" show-icon></el-alert>
            <el-alert v-if="serviceDetail.service_type === 'CHANGE_GOODS'" title="换货申请审核通过后会自动生成状态为待发货的新订单，商家可在用户退还商品后对新订单进行发货操作" type="warning" :closable="false" show-icon></el-alert>
            <el-alert v-if="serviceDetail.service_type === 'SUPPLY_AGAIN_GOODS'" title="补发商品申请审核通过后会自动生成状态为待发货的新订单，可进入订单列表中对订单商品进行发货操作" type="warning" :closable="false" show-icon></el-alert>
          </div>
          <el-form :model="auditForm" :rules="auditRules" ref="auditForm" label-width="135px">
            <el-form-item label="退款金额：" prop="refund_price" v-if="refundShow">
              <el-input v-model="auditForm.refund_price" :maxlength="20" placeholder="请输入退款金额" style="width: 15%;" size="small"/>
            </el-form-item>
            <el-form-item label="退货地址信息：" prop="return_addr" v-if="serviceDetail.service_type === 'RETURN_GOODS' || serviceDetail.service_type === 'CHANGE_GOODS'">
              <el-input type="textarea" placeholder="请输入退货地址信息" v-model="auditForm.return_addr" maxlength="300" show-word-limit rows="3" resize="none" style="width: 40%;"></el-input>
              <div style="color: #ff2828;font-size: 12px;">退货地址是用户退还商品时填写的邮寄地址，请务必填写清楚。默认为店铺的地址信息</div>
            </el-form-item>
            <el-form-item label="审核备注：" prop="audit_remark">
              <el-input type="textarea" placeholder="请输入审核备注" v-model="auditForm.audit_remark" maxlength="150" show-word-limit rows="5" resize="none" style="width: 40%;"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="small" @click="submitAuditForm('PASS')">通 过</el-button>
              <el-button type="primary" size="small" @click="submitAuditForm('REFUSE')">拒 绝</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div class="d-content submit-from" v-if="allowable.allow_put_in_store">
          <div style="margin: 20px 20px 0;">
            <el-alert v-if="refundShow && serviceDetail.order_ship_status === 'SHIP_NO'" title="系统会根据入库数量来恢复商品的库存，如商品无需入库，那么入库数量填写0即可，入库成功后才会退款给用户（当前申请售后的订单商品还未发货，因此入库数量无需改动，全部入库即可）" type="warning" :closable="false" show-icon></el-alert>
            <el-alert v-if="refundShow && serviceDetail.order_ship_status != 'SHIP_NO'" title="请先确认已经收到用户退还的商品后再执行此操作。系统会根据入库数量来恢复商品的库存，如商品无需入库，那么入库数量填写0即可，入库成功后才会退款给用户" type="warning" :closable="false" show-icon></el-alert>
            <el-alert v-if="serviceDetail.service_type === 'CHANGE_GOODS'" title="请先确认已经收到用户退还的商品后再执行此操作。系统会根据入库数量来恢复商品的库存，如商品无需入库，那么入库数量填写0即可" type="warning" :closable="false" show-icon></el-alert>
          </div>
          <en-table-layout :tableData="tableData">
            <template slot="table-columns">
              <el-table-column prop="goods_name" label="商品名称"/>
              <el-table-column prop="return_num" label="申请售后数量"/>
              <el-table-column label="入库数量">
                <template slot-scope="scope">
                  <el-input-number v-model="scope.row.storage_num" :min="0" :max="scope.row.return_num" size="mini" :disabled="true" v-if="serviceDetail.order_ship_status === 'SHIP_NO'"></el-input-number>
                  <el-input-number v-model="scope.row.storage_num" :min="0" :max="scope.row.return_num" size="mini" v-else></el-input-number>
                </template>
              </el-table-column>
            </template>
          </en-table-layout>
          <div class="storage-table-div">
            <el-input type="textarea" placeholder="请输入备注信息" v-model="stockInfo.remark" maxlength="150" show-word-limit rows="5" resize="none" style="width: 40%;"></el-input>
          </div>
          <div class="storage-table-div">
            <el-button type="primary" size="small" @click="submitPutInStock">入 库</el-button>
          </div>
        </div>
        <div class="d-content submit-from" v-if="allowable.allow_seller_refund">
          <div class="form" style="margin: 20px 10px;text-align: center;">
            <div style="margin-bottom: 20px;">
              <el-alert title="当前退款金额是在审核售后服务申请时商家同意退还给用户的金额。如果退款方式是原路退回，点击退款按钮后，会将款项直接退还给用户，否则需要平台进行人工退款。" type="warning" :closable="false" show-icon></el-alert>
            </div>
            <div style="color: #999">退款金额：<strong style="font-size: 18px;color: red;">{{ refund_info.agree_price | unitPrice('￥') }}</strong></div>
            <div style="margin-top: 20px;"><el-button type="primary" size="small" @click="submitRefund">退 款</el-button></div>
          </div>
        </div>
        <div class="d-content submit-from" v-if="allowable.allow_seller_create_order" >
          <div style="margin: 20px;">
            <el-alert v-if="operaType === 'create'" title="系统自动生成新订单失败，需要手动创建新订单。" type="warning" :closable="false" show-icon></el-alert>
            <el-alert v-else title="关闭售后服务需要取得用户同意，以避免发生纠纷，关闭后用户可以继续申请售后服务。" type="warning" :closable="false" show-icon></el-alert>
          </div>
          <div style="text-align: center;">
            <el-radio-group v-model="operaType">
              <el-radio label="create">创建新订单</el-radio>
              <el-radio label="close">关闭售后单</el-radio>
            </el-radio-group>
          </div>
          <div class="storage-table-div" v-if="operaType === 'close'">
            <el-input type="textarea" placeholder="请输入关闭原因" v-model="closeReason" maxlength="150" show-word-limit rows="5" resize="none" style="width: 40%;"></el-input>
          </div>
          <div class="storage-table-div">
            <el-button type="primary" size="small" @click="createNewOrder" v-if="operaType === 'create'">确 定</el-button>
            <el-button type="primary" size="small" @click="closeAfterSale" v-else>确 定</el-button>
          </div>
        </div>
        <div class="d-content">
          <div class="item">
            <span class="item-label">售后服务单号：</span>
            <span class="item-value">{{ serviceDetail.sn }}</span>
          </div>
          <div class="item">
            <span class="item-label">订单编号：</span>
            <span class="item-value">{{ serviceDetail.order_sn }}</span>
          </div>
          <div class="item" v-if="serviceDetail.new_order_sn">
            <span class="item-label">新订单编号：</span>
            <span class="item-value">{{ serviceDetail.new_order_sn }}</span>
          </div>
          <div class="item">
            <span class="item-label">售后服务类型：</span>
            <span class="item-value">{{ serviceDetail.service_type_text }}</span>
          </div>
          <div class="item">
            <span class="item-label">售后服务状态：</span>
            <span class="item-value">{{ serviceDetail.service_status_text }}</span>
          </div>
          <div class="item" v-if="serviceDetail.service_type === 'RETURN_GOODS' || serviceDetail.service_type === 'CHANGE_GOODS'">
            <span class="item-label">商品返回方式：</span>
            <span class="item-value">客户期望处理方式为“快递至第三方卖家“</span>
          </div>
          <div class="item" v-if="serviceDetail.apply_vouchers">
            <span class="item-label">申请凭证：</span>
            <span class="item-value">{{ serviceDetail.apply_vouchers }}</span>
          </div>
          <div class="item">
            <span class="item-label">申请原因：</span>
            <span class="item-value">{{ serviceDetail.reason }}</span>
          </div>
          <div class="item" v-if="serviceDetail.problem_desc">
            <span class="item-label">问题描述：</span>
            <span class="item-value">{{ serviceDetail.problem_desc }}</span>
            <div class="img-list" v-if="serviceDetail.images">
              <a :key="index" class="service-img-item" v-for="(image, index) in serviceDetail.images" :href="image.img" target="_blank">
                <img :src="image.img" alt="">
              </a>
            </div>
          </div>
          <div class="item">
            <span class="item-label">收货地址：</span>
            <span class="item-value">{{ `${change_info.province || '' } ${change_info.city || ''} ${change_info.county || ''} ${change_info.town || ''} ${change_info.ship_addr || ''} `}}</span>
          </div>
          <div class="item">
            <span class="item-label">联系信息：</span>
            <span class="item-value">联系人：{{ change_info.ship_name }}，联系人手机号：{{ change_info.ship_mobile }}</span>
          </div>
          <div class="item" v-if="allowable.allow_show_return_addr">
            <span class="item-label">退货地址信息：</span>
            <span class="item-value">{{ serviceDetail.return_addr }}</span>
          </div>
          <div class="item" v-if="allowable.allow_show_ship_info">
            <span class="item-label">发货单信息：</span>
            <span class="item-value">{{ express_info.ship_time | unixToDate('yyyy-MM-dd') }}，快递公司：{{ express_info.courier_company }}，运单号：{{ express_info.courier_number }}</span>
          </div>
          <div class="item" v-if="serviceDetail.audit_remark">
            <span class="item-label">商家审核备注：</span>
            <span class="item-value">{{ serviceDetail.audit_remark }}</span>
          </div>
          <div class="item" v-if="serviceDetail.stock_remark">
            <span class="item-label">商家入库备注：</span>
            <span class="item-value">{{ serviceDetail.stock_remark }}</span>
          </div>
          <div class="item" v-if="serviceDetail.refund_remark">
            <span class="item-label">平台退款备注：</span>
            <span class="item-value">{{ serviceDetail.refund_remark }}</span>
          </div>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="0" v-if="refundShow">
      <el-col :span="24">
        <div class="d-header">退款信息</div>
        <div class="d-content">
          <div class="item">
            <span class="item-label">申请退款金额：</span>
            <span class="item-value">{{ refund_info.refund_price | unitPrice('￥') }}</span>
          </div>
          <div class="item" v-if="refund_info.agree_price">
            <span class="item-label">同意退款金额：</span>
            <span class="item-value">{{ refund_info.agree_price | unitPrice('￥') }}</span>
          </div>
          <div class="item" v-if="refund_info.actual_price">
            <span class="item-label">实际退款金额：</span>
            <span class="item-value">{{ refund_info.actual_price | unitPrice('￥') }}</span>
          </div>
          <div class="item" v-if="refund_info.actual_price">
            <span class="item-label">退款时间：</span>
            <span class="item-value">{{ refund_info.refund_time | unixToDate }}</span>
          </div>
          <div class="item">
            <span class="item-label">退款方式：</span>
            <span class="item-value">
              {{ refund_info.refund_way | refundWayFilter }}
              <el-tooltip placement="right" v-if="refund_info.refund_way === 'OFFLINE' && serviceDetail.service_status != 'COMPLETED'">
                <div slot="content">请联系买家获取退款账号后<br/>进行线下人工退款</div>
                <i class="el-icon-info"></i>
              </el-tooltip>
            </span>
          </div>
          <div class="item" v-if="accountShow">
            <span class="item-label">账户类型：</span>
            <span class="item-value">{{ refund_info.account_type | accountTypeFilter }}</span>
          </div>
          <div class="item" v-if="bankShow">
            <span class="item-label">退款账号：</span>
            <span class="item-value">{{ refund_info.return_account }}</span>
          </div>
          <div class="item" v-if="bankShow">
            <span class="item-label">银行名称：</span>
            <span class="item-value">{{ refund_info.bank_name }}</span>
          </div>
          <div class="item" v-if="bankShow">
            <span class="item-label">银行账号：</span>
            <span class="item-value">{{ refund_info.bank_account_number }}</span>
          </div>
          <div class="item" v-if="bankShow">
            <span class="item-label">银行开户名：</span>
            <span class="item-value">{{ refund_info.bank_account_name }}</span>
          </div>
          <div class="item" v-if="bankShow">
            <span class="item-label">银行开户行：</span>
            <span class="item-value">{{ refund_info.bank_deposit_name }}</span>
          </div>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="0">
      <el-col :span="24">
        <div class="d-header">日志信息</div>
        <div class="mc">
          <div class="deal-cont">
            <div class="deal-items">
              <div class="deal-item ">
                <div class="deal-txt">
                  审核环节<b></b>
                </div>
                <ul>
                  <li class="fore1" v-for="log in logList" :key="log.id">
                    <div class="deal-msg">
                      <strong>处理信息：</strong>
                      <div>{{ log.log_detail }}
                      </div>
                    </div>
                    <div class="deal-opers">
                      <strong>操作人：</strong>{{ log.operator }}
                      ({{ log.log_time | unixToDate }})
                    </div>
                  </li>
                </ul>
              </div>
              <div class="deal-more" v-if="logMoreShow">
                <a href="javascript:void(0);" class="ftx-05 deal-more-btn" v-if="!logShow" @click="handleOpenLogDetail">查看更多</a>
                <a href="javascript:void(0);" class="ftx-05 deal-more-btn" v-else @click="handleCloseLogDetail">收起</a>
              </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import * as API_AfterSale from '@/api/afterSale'
  import { RegExp } from '~/ui-utils'

  export default {
    name: 'serviceDetail',
    data() {
      return {
        // 售后服务单号
        service_sn: '',
        // 初始化页面展示信息
        serviceDetail: '',
        // 售后服务单允许操作信息
        allowable: '',
        // 日志信息集合
        logList: [],
        // 是否展示查看更多日志按钮
        logMoreShow: true,
        // 是否展示全部日志
        logShow: false,
        // 收货地址信息
        change_info: '',
        // 物流信息
        express_info: '',
        // 退款相关信息
        refund_info: '',
        // 是否展示退款相关信息
        refundShow: false,
        // 是否展示退款账号相关信息
        accountShow: false,
        // 是否展示银行卡相关信息
        bankShow: false,
        // 审核表单
        auditForm: {
          refund_price: '',
          audit_remark: '',
          audit_status: '',
          return_addr: ''
        },
        auditRules: {
          // 退款金额
          refund_price: [{ required: true, message: '请填写退款金额！', trigger: 'blur' }],
          // 退货地址
          return_addr: [{ required: true, message: '请填写退货地址！', trigger: 'blur' }]
        },
        // 售后商品入库表单商品列表数据
        tableData: [],
        // 入库信息
        stockInfo: {
          // 商品信息集合
          storage_list: [],
          // 入库备注
          remark: ''
        },
        // 操作类型
        operaType: 'create',
        // 关闭原因
        closeReason: ''
      }
    },
    beforeRouteUpdate(to, from, next) {
      this.service_sn = to.params.service_sn
      this.GET_AfterSaleServiceDetail()
      next()
    },
    mounted() {
      this.service_sn = this.$route.params.service_sn
      this.GET_AfterSaleServiceDetail()
    },
    activated() {
      this.service_sn = this.$route.params.service_sn
      this.GET_AfterSaleServiceDetail()
    },
    filters: {
      refundWayFilter(val) {
        switch (val) {
          case 'ACCOUNT':
            return '账户退款'
          case 'OFFLINE':
            return '线下退款'
          case 'ORIGINAL':
            return '原路退回'
          default:
            return ''
        }
      },
      accountTypeFilter(val) {
        switch (val) {
          case 'WEIXINPAY':
            return '微信'
          case 'ALIPAY':
            return '支付宝'
          case 'BANKTRANSFER':
            return '银行卡'
          default:
            return ''
        }
      }
    },
    methods: {

      /** 展开全部日志信息 */
      handleOpenLogDetail() {
        this.logList = this.serviceDetail.logs
        this.logShow = true
      },

      /** 关闭全部日志信息 */
      handleCloseLogDetail() {
        this.logList = []
        this.logList.push(this.serviceDetail.logs[0])
        this.logShow = false
      },

      /** 提交审核售后服务表单 */
      submitAuditForm(status) {
        this.$refs['auditForm'].validate((valid) => {
          if (valid) {
            var msg = status === 'PASS' ? '此操作将通过用户的售后服务申请, 是否继续?' : '此操作将拒绝用户的售后服务申请, 是否继续?'
            this.$confirm(msg, '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              this.auditForm.audit_status = status
              API_AfterSale.auditAfterSaleService(this.service_sn, this.auditForm).then(() => {
                this.$message.success('操作成功')
                this.GET_AfterSaleServiceDetail()
              })
            })
          } else {
            this.$message.error('表单填写有误，请核对！')
            return false
          }
        })
      },

      /** 提交商品入库信息 */
      submitPutInStock() {
        this.$confirm('确定要将商品入库吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.stockInfo.service_sn = this.service_sn
          this.stockInfo.storage_list = []
          this.stockInfo.storage_list = this.tableData.map((elem) => {
            return {
              goods_id: elem.goods_id,
              sku_id: elem.sku_id,
              return_num: elem.return_num,
              storage_num: elem.storage_num
            }
          })

          const params = JSON.parse(JSON.stringify(this.stockInfo))

          API_AfterSale.goodsPutInStock(params).then(() => {
            this.$message.success('入库成功')
            this.GET_AfterSaleServiceDetail()
          })
        })
      },

      /** 商家退款操作 */
      submitRefund() {
        this.$confirm('确定要进行退款吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          API_AfterSale.sellerRefund(this.service_sn).then(() => {
            this.$message.success('操作成功')
            this.GET_AfterSaleServiceDetail()
          })
        })
      },

      /** 商家手动创建新订单操作 */
      createNewOrder() {
        this.$confirm('确定要创建新订单吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          API_AfterSale.sellerCreateOrder(this.service_sn).then(() => {
            this.$message.success('创建成功')
            this.GET_AfterSaleServiceDetail()
          })
        })
      },

      /** 商家关闭售后服务操作 */
      closeAfterSale() {
        if (!this.closeReason) {
          this.$message.error('请输入关闭原因')
          return
        }
        this.$confirm('确定要关闭当前售后服务单吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          API_AfterSale.closeAfterSale(this.service_sn, this.closeReason).then(() => {
            this.$message.success('操作成功')
            this.GET_AfterSaleServiceDetail()
          })
        })
      },

      /** 获取售后服务详情页面数据 */
      GET_AfterSaleServiceDetail() {
        API_AfterSale.getServiceDetail(this.service_sn).then(response => {
          this.serviceDetail = response
          this.allowable = this.serviceDetail.allowable
          this.change_info = this.serviceDetail.change_info
          this.express_info = this.serviceDetail.express_info
          this.refund_info = this.serviceDetail.refund_info
          this.auditForm.refund_price = this.refund_info === null ? 0.00 : this.refund_info.refund_price
          this.auditForm.return_addr = this.serviceDetail.return_addr
          this.refundShow = this.serviceDetail.service_type === 'RETURN_GOODS' || this.serviceDetail.service_type === 'ORDER_CANCEL'
          this.accountShow = (this.serviceDetail.service_type === 'RETURN_GOODS' || this.serviceDetail.service_type === 'ORDER_CANCEL') && this.refund_info.refund_way === 'ACCOUNT'
          this.bankShow = (this.serviceDetail.service_type === 'RETURN_GOODS' || this.serviceDetail.service_type === 'ORDER_CANCEL') && this.refund_info.refund_way === 'ACCOUNT' && this.refund_info.account_type === 'BANKTRANSFER'

          this.tableData = []
          let _key = {}
          this.serviceDetail.goods_list.forEach((key, index) => {
            _key = {
              goods_name: key.goods_name,
              goods_id: key.goods_id,
              sku_id: key.sku_id,
              return_num: key.return_num,
              storage_num: key.return_num
            }
            this.tableData.push(_key)
          })

          this.logList = []
          if (this.serviceDetail.logs.length === 1) {
            this.logList = this.serviceDetail.logs
            this.logMoreShow = false
          } else {
            this.logList.push(this.serviceDetail.logs[0])
            this.logMoreShow = true
          }
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  /deep/ .el-dialog__body {
    padding: 20px 25px;
    .logistics-info {
      max-height: 500px;
      overflow-y: scroll;
    }
  }
  /deep/ .el-form-item__label {
    color: #999;
  }
  .container {
    padding: 0;
  }
  .d-header {
    padding: 10px 0 10px 20px;
    font-size: 14px;
    color: #333;
    background-color: #f5f7fa;
    border-bottom: 1px solid #e1e8ed;
    &h2 {
      font-size: 14px;
      font-weight: 400;
    }
  }
  .d-content {
    background: #fff;
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
  /deep/ .el-table td:not(.is-left) {
    text-align: center;
  }
  .goods-image {
    width: 50px;
    height: 50px;
  }
  .goods-name {
    color: #4183c4;
    &:hover { color: #f42424 }
  }
  .submit-from{
    border: 1px solid #dcdfe6;
    margin: 20px;
  }


  table {
    border-collapse: collapse;
  }
  .mod-main {
    padding: 10px;
    background-color: #fff;
    margin-bottom: 20px;
  }
  .mod-main .mc {
    overflow: visible;
  }
  .mod-comm {
    padding: 10px 20px 20px;
  }
  .mod-comm .mc {
    line-height: 20px;
  }
  .mod-comm .mt {
    padding: 10px 0;
    display: block;
  }
  .mod-main .mt:after {
    content: ".";
    display: block;
    height: 0;
    clear: both;
    visibility: hidden;
  }
  .mod-comm .mt h3 {
    font-size: 12px;
    color: #666;
    font-family: '\5b8b\4f53';
    line-height: 18px;
    float: left;
  }
  .remind-box {
    background: #fffdee;
    border: 1px solid #edd28b;
    padding: 10px;
  }
  .after-sale-info {
    display: block;
  }
  .after-sale-info:after {
    content: ".";
    display: block;
    height: 0;
    clear: both;
    visibility: hidden;
  }
  .after-sale-info .r-col {
    float: left;
    border-right: 1px solid #edd28b;
  }
  .after-sale-info .l-col {
    float: left;
    font-size: 14px;
    font-weight: 700;
    padding: 10px 0 0 30px;
    line-height: 25px;
  }
  .ftx-01, .ftx01 {
    color: #e4393c;
  }
  .after-sale-info .after-sale-item {
    display: block;
  }
  .after-sale-info .after-sale-item:after {
    content: ".";
    display: block;
    height: 0;
    clear: both;
    visibility: hidden;
  }
  .after-sale-info .after-sale-item .after-sale-msg {
    float: left;
    margin-left: 20px;
    padding: 10px 0 0;
    line-height: 25px;
    width: 420px;
    white-space: nowrap;
    overflow: hidden;
    font-size: 14px;
    font-weight: 700;
  }
  .tb-void {
    line-height: 18px;
    text-align: center;
    border: 1px solid #f2f2f2;
    border-top: 0;
    color: #333;
    width: 100%;
  }
  .tb-void th {
    background: #f5f5f5;
    height: 32px;
    line-height: 32px;
    padding: 0 5px;
    text-align: center;
    font-weight: 400;
  }
  .tb-void td {
    border: 1px solid #f2f2f2;
    padding: 10px 5px;
  }
  .mod-main .tb-void td {
    padding: 10px;
  }
  .tb-left {
    text-align: left;
  }
  .list-proinfo {
    text-align: left;
    padding-left: 18px;
    padding-right: 18px;
  }
  .list-proinfo li {
    margin-bottom: 15px;
  }
  .list-proinfo .fore1 {
    overflow: hidden;
  }
  .mod-main .tb-void a {
    color: #333;
    &:hover {
      color: #e4393c;
    }
  }
  .link-a {
    color: #333;
    &:hover {
      color: #e4393c;
    }
  }
  .list-proinfo .p-info {
    display: inline-block;
    height: 60px;
    line-height: 20px;
    vertical-align: middle;
    overflow: hidden;
    margin-left: 5px;
  }
  .qww-promise-w {
    height: 35px;
    position: relative;
    margin-left: 20px;
    border-bottom: 1px solid #f5f5f5;
  }
  .qww-promise-w .qww-promise {
    border-left: 2px solid #7abd53;
    background: #fff;
    height: 34px;
    line-height: 34px;
    padding: 0 20px 0 10px;
    color: #7abd53;
    overflow: hidden;
    position: absolute;
    width: 828px;
    left: -20px;
    top: -10px;
  }
  .qww-promise-w em {
    float: left;
    font-weight: 700;
    padding-right: 5px;
    font-size: 14px;
  }
  .qww-promise-w b {
    font-weight: 400;
    padding-left: 5px;
  }
  .qww-promise-w span {
    float: left;
  }
  .deal-cont {
    border: 1px solid #e6e6e6;
    padding: 20px 0;
  }
  .deal-cont .deal-items {
    position: relative;
  }
  .deal-cont .deal-item {
    position: relative;
    line-height: 20px;
    width: 86%;
    margin: 0 auto;
    padding: 5px 0 0 5px;
    border-left: 1px solid #7abd54;
  }
  .deal-cont .deal-item .deal-txt {
    position: absolute;
    top: -1px;
    left: -145px;
    width: 150px;
    text-align: right;
    color: #7abd54;
    font-weight: 700;
  }
  .deal-cont .deal-item .deal-txt b {
    display: inline-block;
    vertical-align: middle;
    width: 10px;
    height: 20px;
    overflow: hidden;
    margin-left: 10px;
    background: url(../../../assets/icon-repair.png) #fff center no-repeat;
  }
  .deal-cont .deal-item li {
    border-bottom: 1px dashed #dfdfdf;
    padding: 15px 0;
  }
  .deal-cont .deal-item li.fore1 {
    padding: 0 0 15px;
    margin-bottom: 10px;
    font-size: 16px;
    color: #666;
  }
  .deal-cont .deal-item .deal-msg {
    margin: 0 0 10px;
    strong{
      padding-bottom: 5px;
      display: inline-block;
    }
  }
  .deal-cont .deal-more {
    width: 86%;
    margin: 20px auto 0;
    padding: 0 0 0 42px;
  }
  .ftx-05, .ftx05 {
    color: #005ea7;
    &:hover {
      color: red;
    }
  }
  .row6col2, .row7col2 {
    text-align: left;
    word-break: break-all;
    word-wrap: break-word;
  }
  .row6col2 {
    position: relative;
    padding-right: 55px;
    width: 620px;
  }
  .tb-void .img-list {
    padding: 10px 0;
  }
  .tb-void .service-img-item {
    float: left;
    display: block;
    width: 50px;
    height: 50px;
    margin-right: 10px;
    cursor: pointer;
    border: 1px solid #e2e2e2;
    padding: 1px;
    img {
      width: 100%;
      height: 100%;
    }
  }
  .repair-box {
    border: 1px solid #ff6c00;
  }
  .repair-box {
    border-color: #999;
  }
  .tip {
    margin-bottom: 20px;
  }
  .storage-table-div {
    padding: 20px;
    text-align: center;
  }
</style>
