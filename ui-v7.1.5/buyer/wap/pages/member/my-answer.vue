<template>
  <div id="my-answer" style="background-color: #f7f7f7">
    <van-nav-bar
      left-arrow
      title="咨询管理"
      @click-left="onClickLeft"
    >
    </van-nav-bar>
    <van-tabs v-model="active" :swipe-threshold="3" @click="handleTabChange">
      <van-tab title="我的提问"/>
      <van-tab title="我的回答"/>
      <van-tab title="邀请我答"/>
    </van-tabs>
    <div class="ask-container">
      <empty-member v-if="finished && !answerList.length">暂无回答</empty-member>
      <van-list v-else v-model="loading" :finished="finished" @load="onLoad">
        <ul class="faq_answers_list">
          <li v-for="(answer, index) in answerList" :key="index">
            <van-swipe-cell :right-width="65">
              <div class="faq_goods type_slim type_arrow">
                <div class="faq_goods_cover">
                  <img :src="answer.goods_img">
                </div>
                <div class="faq_goods_info">
                  <div class="faq_goods_info_name">{{ answer.goods_name }}</div>
                </div>
              </div>
              <div class="faq_question">
                <div class="faq_question_wrap">
                  <div class="faq_question_head">{{ answer.ask_content }}</div>
                  <div class="faq_question_desc">
                    <strong>我的回答：</strong>{{ answer.content }}
                  </div>
                </div>
              </div>
              <span slot="right" @click="handleDeleteAnswer(answer.id)">删除</span>
            </van-swipe-cell>
          </li>
        </ul>
      </van-list>
    </div>
  </div>
</template>

<script>
  import * as API_Members from '@/api/members'
  export default {
    name: 'my-answer',
    data() {
      return {
        // 加载中
        loading: false,
        // 是否全部已加载完成
        finished: false,
        active: 1,
        params: {
          page_no: 0,
          page_size: 10,
          reply_status: 'YES'
        },
        answerList: []
      }
    },
    methods: {
      onClickLeft() {
        this.$router.push({path: '/member', query: {}})
      },

      handleTabChange(index) {
        if (index === 0) {
          this.$router.push({path: '/member/my-ask', query: {}})
        } else if (index === 2) {
          this.$router.push({path: '/member/my-invite-ask', query: {}})
        }
      },

      /** 加载数据 */
      onLoad() {
        this.params.page_no += 1
        this.GET_Answer(false)
      },

      /** 删除回复 */
      handleDeleteAnswer(id) {
        this.$confirm('确定要删除这条回答吗？', () => {
          API_Members.deleteAnswers(id).then(() => {
            this.$message.success('删除成功！')
            this.GET_Answer(true)
          })
        })
      },

      /** 获取会员咨询列表 */
      GET_Answer(reset) {
        this.loading = true
        if (reset) {
          this.params.page_no = 1
          this.finished = false
          this.answerList = []
        }
        API_Members.getAnswers(this.params).then(response => {
          this.loading = false
          const { data } = response
          if (!data || !data.length) {
            this.finished = true
          } else {
            this.answerList.push(...data)
          }
        }).catch(() => { this.loading = false })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  @import "../../assets/styles/color";
  .faq_goods {
    position: relative;
    padding: 10px;
    height: 75px;
    background-color: #fff;
  }
  .faq_goods.type_arrow {
    padding: 10px 10px 10px 10px;
  }
  .faq_goods.type_slim {
    height: 50px;
  }
  .faq_goods_cover {
    position: absolute;
    top: 10px;
    left: 10px;
    width: 75px;
    height: 75px;
    overflow: hidden;
  }
  .faq_goods.type_slim .faq_goods_cover {
    width: 50px;
    height: 50px;
  }
  .faq_goods_cover img {
    position: absolute;
    top: 50%;
    left: 50%;
    width: 100%;
    height: auto;
    transform: translate(-50%,-50%);
  }
  .faq_goods.type_slim .faq_goods_info {
    margin-left: 60px;
    padding: 7px 0;
  }
  .faq_goods_info_name {
    color: #333;
    font-size: 12px;
    height: 36px;
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
  }
  .faq_question {
    padding: 10px;
    background-color: #fff;
  }
  .faq_question_wrap {
    position: relative;
    border-radius: 2px;
  }
  .faq_question_head {
    position: relative;
    color: #333;
    font-size: 14px;
    font-weight: 700;
    line-height: 1.5;
    word-wrap: break-word;
  }
  .faq_question_desc {
    position: relative;
    color: #666;
    font-size: 13px;
    padding-top: 10px;
    word-wrap: break-word;
  }
  .faq_answers_list li:not(:last-child) {
    border-bottom: 1px solid #ddd;
  }
</style>
