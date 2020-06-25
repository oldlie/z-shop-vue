<template>
  <a-spin :spinning="articleLoading">
    <div :style="{ background: '#fff', padding: '24px', minHeight: '280px', 'margin': '30px 0' }">
      <a-tabs defaultActiveKey="1" @change="onTabChange">
        <a-tab-pane v-for="item in articleTags" :key="item.id" :tab="item.title">
          <article-list :articles="articles" :total="articleTotal"></article-list>
        </a-tab-pane>
      </a-tabs>
    </div>
  </a-spin>
</template>
<script>

const __cache = {
  articles: []
};


export default {
  name: "ArticlesPage",
  data() {
    return {
      articleLoading: false,
      articleTags: [],
      articles: {},
      articleTotal: 0
    };
  },
  mounted() {
    this.loadArticles();
  },
  methods: {
    onTabChange(index) {
      this.loadArticlesByTagId(index);
    },
    loadArticles() {
      const url = `${this.apiUrl}/public/home/articles`;
      this.articleLoading = true;
      this.$g
        .get(url)
        .cb(data => {
          console.log("load articles --->", data);
          if (data.status === 0) {
            this.articleTags = data.item.tags;
            this.articles = data.item.articles;
            this.articleTotal = data.item.total;
          }
        })
        .fcb(() => (this.articleLoading = false))
        .req();
    },
    loadArticlesByTagId(tagId) {
      console.log("load articles by tag id...");
      if (!!__cache.articles[tagId]) {
        console.log("read cache");
        this.articles = __cache.articles[tagId].articles;
        this.articleTotal = __cache.articles[tagId].total;
        return;
      }
      const url = `${this.apiUrl}/public/home/articles/${tagId}/1/10`;
      this.articleLoading = true;
      this.$g
        .get(url)
        .cb(data => {
          if (data.status === 0) {
            this.articles = data.list;
            this.articleTotal = data.total;
            __cache.articles[tagId] = {
              articles: data.list,
              total: data.total
            };
          } else {
            console.error("load articles by tag error --->", data);
          }
        })
        .fcb(() => (this.articleLoading = false))
        .req();
    }
  }
};
</script>

