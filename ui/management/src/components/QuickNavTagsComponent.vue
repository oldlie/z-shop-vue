<template>
  <a-spin :spinning="loading">
    <a-row class="inner-row">
      <a-divider orientation="left">{{title}}</a-divider>
    </a-row>
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
      <a-button
        style="margin:5px;"
        v-for="(item, index) in checkedTags"
        :key="index"
        @click="uncheck(item)"
      >
        {{item.title}}
        <a-divider type="vertical" />
        <a-icon type="delete"></a-icon>
      </a-button>
    </a-row>
  </a-spin>
</template>
<script>
export default {
  props: ["title", "rootId", "delay"],
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
    if (this.delay) {
      let timeout = setTimeout(() => {
        this.loadTags(this.rootId);
        clearTimeout(timeout);
      }, this.delay);
    } else {
      this.loadTags(this.rootId);
    }
  },
  methods: {
    loadTags(id) {
      console.log("id", id);
      const url = `/backend/tags/${id}`;
      this.loading = true;
      this.$h
        .get(url)
        .cb(data => {
          if (data.status === 0) {
            this.tags = data.list;
          } else {
            this.$message.error(data.message);
          }
        })
        .fcb(() => {
          this.loading = false;
          this.loadchecked();
          })
        .req();
    },
    loadchecked() {
      const url = `/backend/quick-nav-tags`;
      this.loading = true;
      this.$h
        .get(url)
        .cb(data => {
          if (data.status === 0) {
            if (!!data.list && data.list.length > 0) {
              let temp = [];
              for (let index = 0; index < data.list.length; index++) {
                let item = data.list[index];
                temp.push({
                  id: item.tagId,
                  title: item.tagTitle
                });
              }
              this.checkedTags = temp;
            }
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
      const url = `/backend/quick-nav-tag`;
      this.loading = true;
      let params = {
        tagId: tag.id,
        tagTitle: tag.title,
        sequence: 0
      };
      this.$h
        .post(url, params)
        .cb(data => {
          if (data.status === 0) {
            this.checkedTags.push(tag);
            this.$message.success("已保存");
          } else {
            this.$message.error(decodeURIComponent(data.message));
          }
        })
        .fcb(() => {
          this.loading = false;
        })
        .req();
    },
    nextLevel(tag) {
      this.checkedPath.push(JSON.parse(JSON.stringify(this.defaultTag)));
      this.defaultTag = tag;
      this.loadTags(tag.id);
    },
    upLevel() {
      let tag = this.checkedPath.pop();
      this.loadTags(tag.id);
    },
    uncheck(item) {}
  }
};
</script>