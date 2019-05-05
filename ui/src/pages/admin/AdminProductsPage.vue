<template>
  <div class="main-panel">
    <a-row>
      <a-col :span="6">
        <admin-menu :activeIndex="'sub2'" :openKey="'sub2'"></admin-menu>
      </a-col>
      <a-col :span="18">
        <a-row class="inner-row">
          <a-col :span="24" v-if="tagViewModel === 0">
            <a-button @click="onAddTag(1)" shape="circle" icon="plus" style="margin:5px;"></a-button>
            <a-tag v-for="tag in tags" :key="tag.id" closable @close="onTagClose">{{tag.title}}</a-tag>
          </a-col>
          <a-col :span="24" v-if="tagViewModel === 1">
            <a-button @click="onAddTag(0)" shape="circle" icon="rollback" style="margin:5px;"></a-button>
            <add-tag></add-tag>
          </a-col>
        </a-row>
      </a-col>
    </a-row>
  </div>
</template>
<script>
const tags = [
  { id: 0, title: "New Products" },
  { id: 1, title: "Fruit" },
  { id: 2, title: "Aquatic Products" },
  { id: 3, title: "Snacks" },
  { id: 4, title: "Commodity" }
];
const dataSet = [
  {id: 0, title: 'MI 9', status: 1},
  {id: 1, title: 'MI 9se', status: 1},
  {id: 2, title: 'Redmi 5', status: 0},
  {id: 3, title: 'Mi 8', status: 1},
  {id: 4, title: 'Mi 8se', status: 1}
];
export default {
  data () {
    return {
      tags,
      tagViewModel: 0
    }
  },
  methods: {
    onTagClose () {},
    onAddTag (viewModel) {
      this.tagViewModel = viewModel;
    }
  }
};
</script>
<style scoped>
.inner-row {
  margin: 20px 0;
}
</style>
