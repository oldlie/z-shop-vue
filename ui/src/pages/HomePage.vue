<template>
  <div id="homePage">
    <home-carousel></home-carousel>

    <a-spin :spinning="topProductLoading">
      <product-card :title="topProduct['title']" :products="topProduct['list']" to="/products"></product-card>
    </a-spin>

    <a-spin :spinning="productsLoading">
      <product-card
        v-for="product in products"
        :key="product['title']"
        :title="product['title']"
        :products="product['list']"
        :to="`/products?tagId=${product['tagId']}`"
      ></product-card>
    </a-spin>

    <a-spin :spinning="articleLoading">
      <div :style="{ background: '#fff', padding: '24px', minHeight: '280px', 'margin': '20px 0' }">
        <a-tabs defaultActiveKey="1" @change="onTabChange">
          <a-tab-pane v-for="item in articleTags" :key="item.id" :tab="item.title">
            <article-list :articles="articles" :total="articleTotal"></article-list>
          </a-tab-pane>
        </a-tabs>
      </div>
    </a-spin>
  </div>
</template>
<script>
const __cache = {
  articles: []
};

export default {
  name: "HomePage",
  props: {
    msg: String
  },
  data() {
    return {
      topProductLoading: false,
      topProduct: {},
      productsLoading: false,
      products: [],
      articleTags: [],
      articles: [],
      articleTotal: 0,
      articleLoading: false
    };
  },
  mounted() {
    this.loadLatestCommodities();
    this.loadProducts();
    const timeOut = setTimeout(() => {
      this.loadArticles();
      clearTimeout(timeOut);
    }, 500);
  },
  methods: {
    onChange(a, b, c) {},
    onTabChange(index) {
      this.loadArticlesByTagId(index);
    },
    loadLatestCommodities() {
      const url = `${this.apiUrl}/public/home/top-commodities`;
      this.topProductLoading = true;
      this.$g
        .get(url)
        .cb(data => {
          if (data.status === 0) {
            this.topProduct = data.item;
          } else {
            console.error("load top products error ->", data);
          }
        })
        .fcb(() => (this.topProductLoading = false))
        .req();
    },
    loadProducts() {
      const url = `${this.apiUrl}/public/home/commodities`;
      this.productsLoading = true;
      this.$g
        .get(url)
        .cb(data => {
          console.log("load products --->", data);
          if (data.status === 0) {
            this.products = data.list;
          } else {
            console.error("load products error --->", data);
          }
        })
        .fcb(() => (this.productsLoading = false))
        .req();
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
      if (__cache.articles[tagId]) {
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
<style scoped>
</style>