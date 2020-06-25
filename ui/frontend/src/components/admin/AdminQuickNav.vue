<template>
  <a-spin :spinning="loading">
    <a-row>
      <a-col span="24">
        <admin-common-tags @addTagEvent="handleAddTagEvent" @removeTagEvent="handleRemoveTagEvent"></admin-common-tags>
      </a-col>
    </a-row>
  </a-spin>
</template>
<script>
export default {
  data () {
    return {
      loading: false,
      tags: [],
      checkedPath: [],
      checkedTags: [],
    }
  },
  // end of data
  mounted() {},
  // end of mounted
  methods: {
    load () {

    },
    handleAddTagEvent(tag, tags) {
      console.log('add', tag, tags);
    },
    handleRemoveTagEvent(tag, tags) {
      console.log('add', tag, tags);
    }
  }, 
  // end of methods
}
</script>