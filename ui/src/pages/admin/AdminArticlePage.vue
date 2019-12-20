<template>
  <a-spin :spinning="loading">
    <div class="main-panel">
      <a-row>
        <a-col :sm="24" :md="8" :lg="6">
          <admin-menu :activeIndex="'sub3'" :openKey="'sub3'"></admin-menu>
        </a-col>

        <a-col :sm="24" :md="16" :lg="18">
          <a-row class="inner-row">
            <a-button @click="onAddTag(1)" shape="circle" icon="plus" style="margin:5px;"></a-button>
            <a-tag v-for="tag in tags" :key="tag.id" closable @close="onTagClose">{{tag.title}}</a-tag>
          </a-row>
        </a-col>
      </a-row>
    </div>
  </a-spin>
</template>
<script>
const tags = [
  { id: 0, title: "New Products" },
  { id: 1, title: "Fruit" },
  { id: 2, title: "Aquatic Products" },
  { id: 3, title: "Snacks" },
  { id: 4, title: "Commodity" }
];
export default {
  data() {
    return {
      loading: false,
      tags
    };
  },
  methods: {
    onTagClose() {}
  }
};
</script>