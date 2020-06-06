<template>
  <div :style="{ background: '#fff', padding: '24px', minHeight: '280px', 'margin': '30px 0' }">
    <a-spin :spinning="loading">
      <a-tabs defaultActiveKey="1" @change="onTabChange">
        <a-tab-pane v-for="(item, index) in articleTags" :key="item.id" :tab="item.title">
          <article-list :articles="articles" :total="20" @articlePageChangeEvent="handlePageChange"></article-list>
        </a-tab-pane>
      </a-tabs>
    </a-spin>
  </div>
</template>
<script>
export default {
  name: "ArticlesPage",
  data() {
    return {
      loading: false,
      articleTags: [],
      articles: []
    };
  },
  mounted () {
    this.loadArticles();
  },
  methods: {
    loadArticles() {
      const url = `${this.apiUrl}/public/home/articles`;
      this.$g
        .get(url)
        .cb(data => {
          console.log("load articles --->", data);
          if (data.status === 0) {
            this.articleTags = data.item.tags;
            this.articles = data.item.articles;
          }
        })
        .fcb()
        .req();
    },
   handlePageChange (page) {

   }, onTabChange() {}
  }
};
</script>

