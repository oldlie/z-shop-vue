<template>
  <a-spin :spinning="loading">
    <div class="main-panel">
      <a-row>
        <a-col :span="4">
          <admin-menu :activeIndex="'5'" :openKey="'sub1'" @menuChangeEvent="activeIndexChanged"></admin-menu>
        </a-col>
        <a-col :span="20">
          <a-row v-if="activeIndex === '1'">
            <admin-carousel></admin-carousel>
          </a-row>
          <a-row v-if="activeIndex === '5'">
            <admin-common-tags @addTagsEvent="handleAddTagEvent" @removeTagEvent="handleRemoveTagEvent"></admin-common-tags>
          </a-row>
        </a-col>
      </a-row>
    </div>
  </a-spin>
</template>
<script>
export default {
  data() {
    return {
      activeIndex: "2",
      list: [],
      loading: false,
      defaultChecked: []
    };
  },
  mounted() {
    this.activeIndexChanged(5);
  },
  methods: {
    activeIndexChanged(index) {
      console.log("activeIndexChanged", index);
      this.activeIndex = index;
      if (this.activeIndex === 5) {
        this.loadCheckedTags();
      }
    },
    // region Quick Navigation Tags
    handleAddTagEvent(tag, tags) {
      console.log("update tags:", tag, tags);
      const url = `${this.apiUrl}/backend/quick-nav-tag`;
      this.loading = true;
      let params = {
        tagId: tag.id,
        tagTitle: tag.title,
        sequence: 0
      };
      this.$g.post(url, params)
      .cb(data => {
        if (data.status === 0) {
          this.$message.success('已保存');
        } else {
          console.error(data)
        }
      })
      .fcb(() => {
        this.loading = false;
      })
      .req();
    },
    handleRemoveTagEvent(tag, tags) {
      const url = `${this.apiUrl}/backend/quick-nav-tag/${tag.id}`;
      this.loading = true;
      this.$g.delete(url)
      .cb(data => {
        if (data.status === 0) {
          this.$message.success("已删除");
        } else {
          console.log(data)
        }
      })
      .fcb(() => {
        this.loading = false;
      })
      .req();
    },
    loadCheckedTags() {
      const url = `${this.apiUrl}/backend/quick-nav-tags`;
      this.loading = true;
      this.$g.get(url)
      .cb(data => {
        if (data.status === 0) {
          if (!!data.list && data.list.length > 0) {
            for (let index = 0; index < data.list.length; index++) {
              let item = data.list[index];
              this.defaultChecked.push(
                {
                  id: item.tagId,
                  title: item.tagTitle
                }
              );
            }
          }
        } else {
          console.error(data);
        }
      })
      .fcb(() => this.loading = false)
      .req();
    }
    // endregion
  } // end of methods
};
</script>
<style scoped>
</style>
