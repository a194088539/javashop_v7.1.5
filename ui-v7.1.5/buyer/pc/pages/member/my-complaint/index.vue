<template>
  <div id="my-complaint">
    <div class="member-nav">
      <ul class="member-nav-list">
        <li v-for="item in navList" :key="item.tag">
          <nuxt-link v-if="!item.tag" to="./my-complaint">{{ item.title }}</nuxt-link>
          <nuxt-link v-else :to="'./my-complaint?tag=' + item.tag">{{ item.title }}</nuxt-link>
        </li>
      </ul>
    </div>
    <empty-member v-if="complaintData && !complaintData.data.length">暂无交易投诉</empty-member>
    <template v-else>
      <div class="order-table">
        <div class="order-table-thead">
          <span style="width: 660px">投诉商品</span>
          <span style="width: 158px">投诉主题</span>
          <span style="width: 115px">投诉状态</span>
          <span style="width: 120px">操作</span>
        </div>
        <ul class="order-table-tbody">
          <li v-for="(item, index) in complaintData.data" :key="index">
            <div class="order-tbody-ordersn">
              <span>投诉编号：{{ item.complain_id }}</span>
              <span>投诉时间：{{ item.create_time | unixToDate }}</span>
              <span>投诉的店铺：{{ item.seller_name }}</span>
            </div>
            <div class="order-tbody-item">
              <div class="order-item-sku">
                <div class="sku-item">
                  <div class="goods-image">
                    <a :href="'/goods/' + item.goods_id" target="_blank">
                      <img :src="item.goods_image" :alt="item.name">
                    </a>
                  </div>
                  <div class="goods-name-box">
                    <a :href="'/goods/' + item.goods_id" class="goods-name" target="_blank">
                      {{ item.goods_name }}
                    </a>
                    <span class="order-sn">订单编号：{{ item.order_sn }}</span>
                  </div>
                </div>
              </div>
              <div class="order-item-price">
                {{ item.complain_topic }}
              </div>
              <div class="order-item-status">
                <span>{{ item.status | statusFilter }}</span>
                <nuxt-link :to="'./my-complaint/detail?id=' + item.complain_id" class="detail">查看投诉详情</nuxt-link>
              </div>
              <div class="order-item-operate">
                <span v-if="item.status === 'NEW'" @click="handleCancel(item.complain_id)">撤销投诉</span>
              </div>
            </div>
          </li>
        </ul>
      </div>
      <div class="member-pagination">
        <el-pagination
          @current-change="handleCurrentPageChange"
          :current-page.sync="params.page_no"
          :page-size="params.page_size"
          layout="total, prev, pager, next"
          :total="complaintData.data_total">
        </el-pagination>
      </div>
    </template>
  </div>
</template>

<script>
  import * as API_Complaint from '@/api/complaint'
  export default {
    name: "my-complaint-index",
    data() {
      return {
        params: {
          page_no: 1,
          page_size: 10,
          tag: this.$route.query.tag,
          key_words: '',
        },
        // 交易投诉列表数据
        complaintData: '',
        // 导航数据
        navList: [
          { title: '全部投诉', tag: '' },
          { title: '进行中', tag: 'COMPLAINING' },
          { title: '已完成', tag: 'COMPLETE' },
          { title: '已撤销', tag: 'CANCELED' }
        ],
      }
    },
    watch: {
      $route: function ({ query }) {
        this.params.page_no = 1
        this.params.tag = query.tag
        this.GET_ComplaintList()
      }
    },
    mounted() {
      if (!this.complaintData) this.GET_ComplaintList()
    },
    filters: {
      statusFilter(val) {
        switch (val) {
          case 'NEW': return '新投诉'
          case 'CANCEL': return '已撤销'
          case 'WAIT_APPEAL': return '待申诉'
          case 'COMMUNICATION': return '对话中'
          case 'WAIT_ARBITRATION': return '等待仲裁'
          case 'COMPLETE': return '已完成'
        }
      }
    },
    methods: {
      /** 当前页数发生改变 */
      handleCurrentPageChange(cur) {
        this.params.page_no = cur
        this.GET_ComplaintList()
      },
      /** 撤销交易投诉 */
      handleCancel(id) {
        API_Complaint.cancelComplaint(id).then(response => {
          this.$message.success('撤销投诉成功！')
          this.GET_ComplaintList()
        })
      },
      /** 获取交易投诉列表 */
      GET_ComplaintList() {
        // 去除参数中为空的选项
        let params = {}
        for (let key in this.params) {
          if (this.params[key]) {
            params[key] = this.params[key]
          }
        }
        API_Complaint.getComplaintList(params).then(response => {
          this.complaintData = response
          this.MixinScrollToTop()
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../../assets/styles/color";
  .order-table {
    .order-table-thead {
      display: flex;
      align-items: center;
      height: 20px;
      padding: 8px 0;
      text-align: center;
      background:#e7e7e7;
      color: #999;
      margin:20px 0 10px;
    }
    .order-table-tbody {
      li {
        margin-bottom: 20px;
      }
    }
    .order-tbody-title {
      background-color: #faece0;
      border: 1px solid #f9dbcc;
      padding: 9px 0;
      text-align: left;
      .pay-type {
        color: #53514f;
        font-weight: 600;
        margin-left: 20px;
      }
      .price {
        color: $color-main;
        font-size: 14px;
        font-weight: 600;
        em {
          font-size: 12px;
          font-weight: normal;
        }
      }
    }
    .order-tbody-ordersn {
      background-color: #e7e7e7;
      padding: 5px 0;
      color: #999;
      height: 20px;
      font-size: 12px;
      font-weight: 400;
      span { margin-left: 20px }
    }
    .order-tbody-item {
      display: flex;
      align-items: center;
      position: relative;
      border: 1px solid #e7e7e7;
      border-top: none;
      .order-item-sku {
        width: 660px;
      }
      .sku-item {
        display: flex;
        align-items: center;
        margin-top: 6px;
      }
      .goods-image {
        display: flex;
        justify-content: center;
        align-items: center;
        width: 86px;
        height: 86px;
        a {
          display: flex;
          justify-content: center;
          align-items: center;
          width: 66px;
          height: 66px;
          text-align: center;
          border: 1px solid #ccc;
          background-color: #fff;
        }
        img {
          width: 62px;
          height: 62px;
        }
      }
      .goods-name-box { width: 450px - 86px }
      .goods-name {
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 2;
        overflow: hidden;
        .assemble-color {
          color: $color-main;
          border: 1px solid $color-main;
          border-radius: 3px;
        }
      }
      .sku-price { color: $color-main }
      .order-sn{
        display: block;
        padding-top: 5px;
      }
      .after-sale-btn {
        width: 60px;
        a { color: #666 }
        a:hover { color: $color-main }
      }
      .order-item-price, .order-item-status {
        text-align: center;
      }
      .detail{
        display: block;
        color:#f42424;
      }
      .order-item-price {
        width: 158px;
        &::before {
          content: ' ';
          position: absolute;
          top: 0;
          right: 110px + 100px + 150px;
          width: 1px;
          height: 100%;
          background-color: #e7e7e7;
        }
        &::after {
          content: ' ';
          position: absolute;
          top: 0;
          right: 110px + 100px;
          width: 1px;
          height: 100%;
          background-color: #e7e7e7;
        }
        strong { color: $color-main }
      }
      .order-item-status {
        width: 115px;
        &::after {
          content: ' ';
          position: absolute;
          top: 0;
          right: 100px;
          width: 1px;
          height: 100%;
          background-color: #e7e7e7;
        }
      }
      .order-item-operate {
        display: flex;
        justify-content: center;
        align-items: center;
        flex-direction: column;
        width: 103px;
        span {
          cursor: pointer;
          display: block;
          width: 80px;
          height: 30px;
          line-height: 30px;
          border: 1px solid #ccc;
          text-align: center;
          background: #f9f9f9;
          color: #666;
          cursor: pointer;
          margin-bottom: 5px;
          &:first-child {
            margin-top: 5px;
          };
          &:hover {
            background: #eaeaea;
          }
        }
      }
    }
  }
</style>
