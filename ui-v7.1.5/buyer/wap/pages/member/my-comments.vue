<template>
  <nuxt-child></nuxt-child>
</template>

<script>
  export default {
    name: 'my-comments',
    head() {
      return {
        title: `评论管理-${this.site.title}`
      }
    }
  }
</script>
