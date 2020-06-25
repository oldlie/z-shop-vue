<template>
  <a-spin :spinning="loading">
    <div class="main-panel">
      <a-row>
        <a-col :span="4">
          <admin-menu :activeIndex="'1'" :openKey="'sub1'" @menuChangeEvent="activeIndexChanged"></admin-menu>
        </a-col>
        <a-col :span="20">
          <a-row v-if="activeIndex === '1'">
            <admin-carousel></admin-carousel>
          </a-row>
          <a-row v-if="activeIndex === '5'">
            <a-col>
              <a-row class="inner-row">
                <p>快捷导航</p>
                <admin-common-tags
                  :defaultCheckedTags="defaultChecked"
                  @addTagEvent="handleAddTagEvent"
                  @removeTagEvent="handleRemoveTagEvent"
                ></admin-common-tags>
              </a-row>
              <a-row class="inner-class">
                <p>首页商品栏目</p>
                <admin-common-tags
                  :defaultCheckedTags="homeTags"
                  @addTagEvent="handleAddHomeTagEvent"
                  @removeTagEvent="handleRemoveHomeTagEvent"
                ></admin-common-tags>
              </a-row>
              <a-row class="inner-class">
                <p>首页资讯栏目</p>
                <admin-common-tags
                  :defaultCheckedTags="homeArticleTags"
                  @addTagEvent="handleAddArticleTagEvent"
                  @removeTagEvent="handleRemoveArticleTagEvent"
                ></admin-common-tags>
              </a-row>
            </a-col>
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
      activeIndex: "1",
      list: [],
      loading: false,
      defaultChecked: [],
      //#region
      homeTags: [],
      homeArticleTags: []
      //#endregion
    };
  },
  mounted() {
    this.activeIndexChanged("1");
  },
  methods: {
    activeIndexChanged(index) {
      this.activeIndex = index;
      if (this.activeIndex === "5") {
        let timeCounter = 0;
        let interval = setInterval(()=> {
          if (timeCounter === 0) {
            this.loadCheckedTags();
          } else if (timeCounter === 1) {
            this.loadHomeCommodityTags();
          } else if (timeCounter === 2) {
            this.loadHomeArticleTags();
          } else {
            clearInterval(interval);
          }
          timeCounter++;
        }, 1000);
      }
    },
    // region Quick Navigation Tags
    handleAddTagEvent(tag, tags) {
      const url = `${this.apiUrl}/backend/quick-nav-tag`;
      this.loading = true;
      let params = {
        tagId: tag.id,
        tagTitle: tag.title,
        sequence: 0
      };
      this.$g
        .post(url, params)
        .cb(data => {
          if (data.status === 0) {
            this.$message.success("已保存");
          } else {
            console.error(data);
          }
        })
        .fcb(() => {
          this.loading = false;
        })
        .req();
    },
    handleRemoveTagEvent(tag) {
      const url = `${this.apiUrl}/backend/quick-nav-tag/${tag.id}`;
      this.loading = true;
      this.$g
        .delete(url)
        .cb(data => {
          if (data.status === 0) {
            this.$message.success("已删除");
          } else {
            console.log(data);
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
      this.$g
        .get(url)
        .cb(data => {
          if (data.status === 0) {
            console.log(data.list.length);
            if (!!data.list && data.list.length > 0) {
              let temp = [];
              for (let index = 0; index < data.list.length; index++) {
                let item = data.list[index];
                temp.push({
                  id: item.tagId,
                  title: item.tagTitle
                });
              }
              this.defaultChecked = temp;
            }
          } else {
            console.error(data);
          }
        })
        .fcb(() => (this.loading = false))
        .req();
    },
    // endregion
    // region Home Commodity Tags
    loadHomeCommodityTags() {
      const url = `${this.apiUrl}/backend/home-commodity-tags`;
      this.$g
        .get(url)
        .cb(data => {
          if (data.status === 0) {
            this.homeTags = data.list;
          } else {
            this.$message.error(data.message);
          }
        })
        .fcb()
        .req();
    },
    handleAddHomeTagEvent(tag) {
      const url = `${this.apiUrl}/backend/home-tag`;
      const params = {
        tagId: tag.id,
        sequence: 0,
        category: 0
      };
      this.loading = true;
      this.$g
        .post(url, params)
        .cb(data => {
          if (data.status === 0) {
            this.$message.success("已添加");
          } else {
            this.$message.error(data.message);
          }
        })
        .fcb(() => (this.loading = false))
        .req();
    },
    handleRemoveHomeTagEvent(tag) {
      const url = `${this.apiUrl}/backend/home-tag/${tag.id}`;
      this.loading = true;
      this.$g
        .delete(url)
        .cb(data => {
          if (data.status === 0) {
            this.$message.success("已删除");
          } else {
            this.$message.error(data.message);
          }
        })
        .fcb(() => (this.loading = false))
        .req();
    },
    // endregion
    loadHomeArticleTags() {
      const url = `${this.apiUrl}/backend/home-article-tags`;
      this.$g
        .get(url)
        .cb(data => {
          if (data.status === 0) {
            this.homeArticleTags = data.list;
          } else {
            this.$message.error(data.message);
          }
        })
        .fcb()
        .req();
    },
    handleAddArticleTagEvent(tag) {
      const url = `${this.apiUrl}/backend/home-tag`;
      const params = {
        tagId: tag.id,
        sequence: 0,
        category: 1
      };
      this.loading = true;
      this.$g
        .post(url, params)
        .cb(data => {
          if (data.status === 0) {
            this.$message.success("已添加");
          } else {
            this.$message.error(data.message);
          }
        })
        .fcb(() => (this.loading = false))
        .req();
    },
    handleRemoveArticleTagEvent(tag) {
      const url = `${this.apiUrl}/backend/home-tag/${tag.id}`;
      this.loading = true;
      this.$g
        .delete(url)
        .cb(data => {
          if (data.status === 0) {
            this.$message.success("已删除");
          } else {
            this.$message.error(data.message);
          }
        })
        .fcb(() => (this.loading = false))
        .req();
    }
  } // end of methods
};
</script>
<style scoped>
</style>
