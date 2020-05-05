<template>
  <a-spin :spinning="loading">
    <a-row class="inner-row">
        <a-button style="margin:5px" v-if="defaultTag.id !== 0" @click="upLevel">上一级</a-button>
        <a-button-group style="margin:5px" v-for="(item, index) in tags" :key="index">
          <a-button @click="check(item)">{{item.title}}</a-button>
          <a-button @click="nextLevel(item)">
            <a-icon type="right" />
          </a-button>
        </a-button-group>
    </a-row>
    <a-row class="inner-row" v-if="this.checkedTags.length > 0">
      <p>已选的快捷标签（点击已选的标签取消）：</p>
      <a-button
        style="margin:5px;"
        v-for="(item, index) in checkedTags"
        :key="index"
        @click="uncheck(item)"
      >{{item.title}}</a-button>
    </a-row>
  </a-spin>
</template>
<script>
export default {
  props: {
    defaultCheckedTags: Array
  },
  data() {
    return {
      loading: false,
      defaultTag: { id: 0 },
      tags: [],
      checkedPath: [],
      checkedTags: []
    };
  },
  mounted() {
    this.checkedTags = JSON.parse(JSON.stringify(this.defaultCheckedTags));
    this.nextLevel({ id: 0 });
  },
  methods: {
    load(id) {
      const url = `${this.apiUrl}/backend/tags/${id}`;
      this.loading = true;
      this.$g
        .get(url)
        .cb(data => {
          if (data.status === 0) {
            this.tags = data.list;
          } else {
            console.error(data);
          }
        })
        .fcb(() => (this.loading = false))
        .req();
    },
    check(tag) {
      let existed = this.checkedTags.filter(x => x.id === tag.id);
      if (!!existed && existed.length > 0) {
        this.$message.warning("该标签已经添加了!");
        return;
      }
      this.checkedTags.push(tag);
      this.$emit("addTagEvent", tag, this.checkedTags);
    },
    uncheck(tag) {
      this.checkedTags = this.checkedTags.filter(x => x.id !== tag.id);
      this.$emit("removeTagEvent", tag, this.checkedTags);
    },
    nextLevel(tag) {
      this.checkedPath.push(JSON.parse(JSON.stringify(this.defaultTag)));
      this.defaultTag = tag;
      this.load(tag.id);
    },
    upLevel() {
      let tag = this.checkedPath.pop();
      this.load(tag.id);
    }
  } // end of methods
};
</script>