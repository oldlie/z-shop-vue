<template>
  <a-spin :spinning="loading"></a-spin>
</template>
<script>
export default {
  props: {
    articleId: { type: Number, default: 0 }
  },
  data() {
    return {
      loading: false,
      title: '',
      author: '',
      summary: '',
      content: '',
      thumbnail: ''
    };
  },
  mounted: {}
};
</script>