<template>
  <div id="goods-consulting" class="goods-consulting">
    <div class="inner-consulting">
      <span class="consu-tip">因商家更改商品包装、场地、附配件等不做提前通知，且每位咨询者购买、提问时间等不同，为此，已购用户的回复不能作为长期参考，给您带来的不便还请谅解，谢谢！</span>
      <a href="javascript:" class="want-consu-btn" @click="handleShowAskDialog">我要咨询</a>
    </div>
    <div v-if="consulting" class="askAnswer">
      <div class="askAnswer-item" v-for="consult in consulting.data" :key="consult.ask_id">
        <div class="ask">
          <i class="icon-ask">问</i>
          <div class="item-con">
            <p>{{ consult.content }}</p><span>{{ consult.member_name }} {{ consult.create_time | unixToDate }}</span>
          </div>
        </div>
        <div class="answer">
          <i class="icon-answer">答</i>
          <i class="icon-answer" v-if="consult.reply_status === 'YES'" style="background: #b50005;margin-left: 3px;margin-right: 3px;">商</i>
          <div class="item-con">
            <ul class="answer-list" :id="'answer-' + consult.ask_id">
              <li v-if="consult.reply_status === 'YES'">
                <p>{{ consult.reply }}</p>
                <span class="item-info">商家回复 {{ consult.reply_time | unixToDate }}</span>
              </li>
              <li v-if="consult.reply_status === 'NO'">
                <p>{{ consult.first_reply.content }}</p>
                <span class="item-info">{{ consult.first_reply.member_name }} {{ consult.first_reply.reply_time | unixToDate }}</span>
              </li>
            </ul>
            <div class="more" v-if="consult.reply_num > 1">
              <a href="javascript:" :id="'show-' + consult.ask_id" class="J-spread spread" :data-answercount="consult.reply_num-1" :data-currpage="1" @click="handleShowMoreAnswer(consult.ask_id, consult.reply_status, consult.first_reply.id)">
                继续查看<em :class="'J-rest-answer-count-'+consult.ask_id">{{ consult.reply_num - 1 }}</em>条回答<i class="sprite-arrowDown"></i>
              </a>
              <a href="javascript:" :id="'close-' + consult.ask_id" class="J-retract retract" :data-answercount="consult.reply_num-1" @click="handleCloseMoreAnswer(consult.ask_id)">收起回答<i class="sprite-arrowUp"></i></a>
            </div>
          </div>
        </div>
      </div>
    </div>
    <el-pagination
      @current-change="handleCurrentPageChange"
      :current-page.sync="params.page_no"
      :page-size="params.page_size"
      layout="total, prev, pager, next"
      :total="consulting.data_total">
    </el-pagination>

    <!--问题咨询 dialog-->
    <el-dialog
      title="提问"
      :visible.sync="dialogAskVisible"
      width="450px"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <el-form :model="goodsAskForm" :rules="goodsAskRules" ref="goodsAskForm">
        <el-form-item prop="ask_content">
          <el-input
            type="textarea"
            :autosize="{ minRows: 3, maxRows: 4}"
            placeholder="请输入您的问题吧~(3-120字之间)"
            :maxlength="120"
            v-model="goodsAskForm.ask_content">
          </el-input>
        </el-form-item>
        <!--是否匿名-->
        <el-form-item prop="checked">
          <el-checkbox v-model="goodsAskForm.checked">匿名提问</el-checkbox>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogAskVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitGoodsAskForm">发 布</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  /**
   * 商品咨询模块
   * 这里可以对商品进行咨询
   */
  import * as API_Members from '@/api/members'
  import Storage from '@/utils/storage'
  import Vue from 'vue'
  import { Checkbox } from 'element-ui'
  Vue.use(Checkbox);
  export default {
    name: "goods-consulting",
    props: ['goodsId'],
    data() {
      const askContent = (rule, value, callback) => {
        if (this.MixinEmojiCharacter(value)) {
          callback(new Error('不能输入表情符号！'))
        } else {
          callback()
        }
      }
      return {
        params: {
          page_no: 1,
          page_size: 10
        },
        // 问题咨询 表单
        goodsAskForm: {
          ask_content: '',
          checked: true,
          anonymous: 'YES'
        },
        // 问题咨询 表单校验
        goodsAskRules: {
          ask_content: [
            { required: true, message: '请输入您的问题！', trigger: 'blur' },
            { validator: askContent, trigger: 'blur' }
          ]
        },
        // 问题咨询 dialog
        dialogAskVisible: false,

        consulting: '',

        reply: {
          page_no: 1,
          page_size: 10,
          reply_id: ''
        }
      }
    },
    mounted() {
      this.GET_Consulting()
    },
    methods: {
      /** 当页数发生改变时 */
      handleCurrentPageChange(page_no) {
        this.params.page_no = page_no;
        this.GET_Consulting()
      },
      /** 显示咨询窗 */
      handleShowAskDialog() {
        if (!Storage.getItem('user')) {
          this.$confirm('您还未登录，要现在去登录吗？', () => {
            this.$router.push({ path: '/login', query: { forward: this.$route.path } })
          });
          return false
        }
        this.dialogAskVisible = true
      },
      /** 商品咨询 表单提交 */
      submitGoodsAskForm() {
        this.$refs['goodsAskForm'].validate((valid) => {
          if (valid) {
            this.goodsAskForm.anonymous = this.goodsAskForm.checked ? 'YES' : 'NO'
            API_Members.consultating(this.goodsId, this.goodsAskForm.ask_content, this.goodsAskForm.anonymous).then(() => {
              this.dialogAskVisible = false
              this.goodsAskForm.ask_content = ''
              this.goodsAskForm.checked = true
              this.$message.success('发布成功')
              this.GET_Consulting()
            })
          } else {
            this.$message.error('表单填写有误，请核对！')
            return false
          }
        })
      },

      /** 查看更多回复 */
      handleShowMoreAnswer(ask_id, reply_status, reply_id) {
        var _answer = $("#answer-"+ask_id),
            _show = $("#show-"+ask_id),
            _close = $("#close-"+ask_id),
            _count = $(".J-rest-answer-count-"+ask_id);

        if (reply_status === 'NO') {
          this.reply.reply_id = reply_id
        }

        var total = parseInt(_show.attr('data-answercount'))
        var pageNo = parseInt(_show.attr('data-currpage'))
        console.log(total)

        this.reply.page_no = pageNo

        API_Members.getGoodsAskReplys(ask_id, this.reply).then(response => {
          var answerLi = "";
          for(var i = 0; i < response.data.length; i++){
            answerLi += "<li style='overflow: hidden;zoom: 1;' class='li-"+ask_id+"'>" +
              "<p style='width: 75%;padding: 10px 0 8px;line-height: 220%;float: left;color: #333;'>"+response.data[i].content+"</p>" +
              "<span style='width: 25%;line-height: 220%;padding: 10px 0px 8px;text-align:right;float: left;color: gray;'>"+response.data[i].member_name +" "
              + this.GET_DateFormat(response.data[i].reply_time*1000, 'Y-M-D h:m:s')+"</span></li>"
          }

          _answer.append(answerLi)

          total = total - response.data.length
          _show.attr('data-answercount', total)
          _show.attr('data-currpage', pageNo + 1)

          console.log(total)

          if (total === 0) {
            _show.addClass('retract')
            _close.removeClass('retract')
          } else {
            _count.text(total)
          }
        })
      },

      /** 收起回复 */
      handleCloseMoreAnswer(ask_id) {
        var _li = $(".li-"+ask_id),
            _show = $("#show-"+ask_id),
            _close = $("#close-"+ask_id),
            _count = $(".J-rest-answer-count-"+ask_id);

        var total = parseInt(_close.attr('data-answercount'))

        _li.remove()
        _show.removeClass('retract')
        _close.addClass('retract')

        _show.attr('data-answercount', total)
        _show.attr('data-currpage', 1)
        _count.text(total)
      },

      /** 获取咨询列表 */
      GET_Consulting() {
        API_Members.getGoodsConsultations(this.goodsId, this.params).then(response => {
          this.consulting = response
        })
      },

      /** 日期转换 */
      GET_DateFormat(number, format) {
        let time = new Date(number)
        let newArr = []
        let formatArr = ['Y', 'M', 'D', 'h', 'm', 's']
        newArr.push(time.getFullYear())
        newArr.push(this.GET_FormatNumber(time.getMonth() + 1))
        newArr.push(this.GET_FormatNumber(time.getDate()))

        newArr.push(this.GET_FormatNumber(time.getHours()))
        newArr.push(this.GET_FormatNumber(time.getMinutes()))
        newArr.push(this.GET_FormatNumber(time.getSeconds()))

        for (let i in newArr) {
          format = format.replace(formatArr[i], newArr[i])
        }
        return format;
      },

      GET_FormatNumber (n) {
        n = n.toString()
        return n[1] ? n : '0' + n;
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../assets/styles/color";
  .goods-consulting {
    background-color: #fff;
    padding: 40px 30px 30px;
    .inner-consulting {
      position: relative;
      border: 1px solid #d7d7d7;
      height: 67px;
      padding: 10px 10px 20px 10px;
      background: url(../../assets/images/icon-goods-consulting-tip.png) no-repeat 10px 10px;
    }
    .consu-tip {
      color: #9b827d;
      height: 36px;
      left: 70px;
      line-height: 18px;
      position: absolute;
      top: 40px;
      width: 600px;
    }
    .want-consu-btn {
      padding: 5px 20px;
      background-color: $color-main;
      border-color: $color-main;
      color: #fff;
      position: absolute;
      top: 30px;
      right: 30px;
      &:hover { background-color: darken($color-main,10%) }
    }
    .content-consulting {
      border-top: 1px dashed #ccc;
      margin-top: 20px;
    }
    .item-consulting {
      margin-top: 15px;
    }
    .cons-left {
      float: left;
      width: 70px;
      .face-consulting, .name-consulting {
        width: 50px;
        height: 50px;
        text-align: center;
      }
      .name-consulting {
        height: auto;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
    }
    .cons-right {
      float: left;
      box-sizing: border-box;
      width: 900px - 70px;
      min-height: 100px;
      padding: 20px;
      background-color: #fafafa;
      .time-cons {
        padding: 10px;
        text-align: right;
        font-size: 14px;
        border-bottom: 1px dashed #ccc;
      }
      .box-cons {
        width: 900px - 70px - 40px;
        background-color: #fff;
        min-height: 50px;
        margin-top: 10px;
        box-sizing: border-box;
        padding: 10px;
        p { color: $color-main }
        p.reply-cons {
          color: #666;
        }
      }
    }
    /deep/ .el-pagination {
      text-align: right;
      padding: 10px 0;
    }

    .askAnswer .askAnswer-item {
      padding-top: 18px;
      border-bottom: 1px solid #ddd;
    }
    .askAnswer .askAnswer-item .answer-list li, .askAnswer .askAnswer-item .ask {
      overflow: hidden;
      zoom: 1;
    }
    .askAnswer .icon-ask {
      background: #f91;
    }
    .askAnswer .icon-answer {
      background: #9b1;
      margin-top: 15px;
    }
    .askAnswer .icon-answer, .askAnswer .icon-ask {
      width: 18px;
      height: 18px;
      border-radius: 9px;
      font-size: 12px;
      color: #fff;
      text-align: center;
      float: left;
    }
    .askAnswer .askAnswer-item .item-con {
      margin-left: 25px;
      overflow: hidden;
      zoom: 1;
    }
    .askAnswer .ask-wrap, .askAnswer .askAnswer-item .item-con {
      font-family: "microsoft yahei";
      font-size: 14px;
    }
    .askAnswer .askAnswer-item .ask .item-con p {
      font-weight: 700;
    }
    .askAnswer .askAnswer-item .item-con p {
      float: left;
      color: #333;
    }
    .askAnswer .askAnswer-item .ask p {
      width: 75%;
    }
    .askAnswer .askAnswer-item .item-con span {
      float: right;
      color: #999;
    }
    .askAnswer .askAnswer-item .item-con .item-info {
      width: 25%;
      line-height: 220%;
      padding: 10px 0px 8px;
      text-align: right;
    }
    .askAnswer .askAnswer-item .answer p {
      width: 75%;
      padding: 10px 0 8px;
      line-height: 220%;
    }
    .askAnswer .askAnswer-item .answer .item-con .more {
      line-height: 100%;
      padding-bottom: 18px;
    }
    .askAnswer .askAnswer-item .answer .item-con .more a {
      color: #005ea7;
    }
    .askAnswer .askAnswer-item .answer .item-con .more .sprite-arrowDown {
      display: inline-block;
      width: 9px;
      height: 5px;
      background-image: url(../../assets/images/sprite.png);
      background-position: -28px -48px;
      margin-left: 5px;
      vertical-align: 1px;
    }
    .askAnswer .askAnswer-item .answer .retract {
      display: none;
    }
    .askAnswer .askAnswer-item .answer .item-con .more .sprite-arrowUp {
      display: inline-block;
      width: 9px;
      height: 5px;
      background-image: url(../../assets/images/sprite.png);
      background-position: -37px -48px;
      margin-left: 5px;
      vertical-align: 1px;
    }
  }
</style>
