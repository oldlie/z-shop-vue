<template>
  <div class="main-panel">
    <a-row>
      <a-col :span="6">
        <admin-menu :activeIndex="'tags'" :openKey="''"></admin-menu>
      </a-col>
      <a-col :span="18">
        <admin-tags 
        v-show="viewModel === 'list'" 
        @gotoFormEvent="changeViewModel('form')"
        @updateTagEvent="updateTag"
        ></admin-tags>
        <admin-tag
          v-if="viewModel === 'form'"
          :parentTag="tag"
          @goBackEvent="changeViewModel('list')"
        ></admin-tag>
      </a-col>
    </a-row>
  </div>
</template>
<script>
export default {
  data() {
    return {
      viewModel: "list",
      tag: {
        id: 0,
        title: "根",
        category: 0,
        tagOrder: 1,
        parentId: 0,
        childCount: 0
      }
    };
  },
  methods: {
    changeViewModel(m) {
      this.viewModel = m;
    },
   updateTag(tag) {
     this.tag = tag;
   }
  }
};
</script>