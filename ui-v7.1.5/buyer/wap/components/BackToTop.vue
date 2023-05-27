<template>
  <div
    v-show="show"
    class="back-to-top"
    :style="{ right: `${right}px`, bottom: `${bottom}px` }"
    @click="handleToTop"
    @touchstart="handleTouchStart"
    @touchmove="handleTouchMove"
    @touchend="handleTouchEnd">
    <van-icon name="upgrade"/>
  </div>
</template>

<script>
  export default {
    name: 'EnBackToTop',
    data() {
      return {
        show: false,
        bottom: 100,
        right: 15
      }
    },
    mounted() {
      window.addEventListener('scroll', this.showToTop)
    },
    methods: {
      showToTop() {
        this.show = window.scrollY > 200
      },
      handleToTop() {
        $('body,html').animate({ scrollTop: 0 }, 300)
      },
      moveEvent(e) {
        const clientWidth = document.documentElement.clientWidth || document.body.clientWidth
        const clientHeight = document.documentElement.clientHeight || document.body.clientHeight
        const event = e.changedTouches[0]
        const domWidth = clientWidth / 10
        this.right = clientWidth - event.clientX - (domWidth / 2)
        if (this.right > clientWidth - domWidth) {
          this.right = clientWidth - domWidth
        } else if (this.right < 0) {
          this.right = 0
        }
        this.bottom = clientHeight - event.clientY - (domWidth / 2)
        if (this.bottom > clientHeight - domWidth) {
          this.bottom = clientHeight - domWidth
        } else if (this.bottom < 0) {
          this.bottom = 0
        }
      },
      handleTouchStart(e) {
        this.moveEvent(e)
      },
      handleTouchMove(e) {
        e.preventDefault()
        this.moveEvent(e)
      },
      handleTouchEnd(e) {
        this.moveEvent(e)
      }
    },
    destroyed() {
      window.removeEventListener('scroll', this.showToTop)
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  .back-to-top {
    position: fixed;
    z-index: 1000;
    bottom: 100px;
    right: 15px;
    width: 10vw;
    height: 10vw;
    background-color: #ffffff;
    border-radius: 100%;
    text-align: center;
    box-shadow: 0 0 10px #ccc;
    /deep/ .van-icon {
      line-height: 10vw;
      font-size: 5vw;
      font-weight: 600;
      color: #787878;
    }
  }
</style>
