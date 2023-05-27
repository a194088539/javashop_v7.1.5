<template>
  <el-button
    v-if="isShow"
    size="mini"
    type="danger"
    @click="handleClick"
  >
    <slot></slot>
  </el-button>
</template>

<script>
  export default {
    name: 'assembleOpenOrClose',
    data() {
      return {
        isShow: false,
        timer: null
      }
    },
    props: {
      row: {
        type: Object,
        default: () => {}
      },
      type: String,
      size: String
    },
    methods: {
      handleClick() {
        this.$emit('clickEmit', this.row)
      },
      sumTime(endTime) {
        this.timer = setInterval(() => {
          if (endTime < Date.now()) {
            this.isShow = false
            clearInterval(this.timer)
          }
        }, 1000)
      }
    },
    created() {
      let endTime = this.row.end_time * 1000
      if (endTime < Date.now()) {
        this.isShow = false
      } else {
        this.isShow = true
        this.sumTime(endTime)
      }
    }
  }
</script>

<style scoped>

</style>
