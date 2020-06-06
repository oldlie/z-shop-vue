<template>
  <div id="homePage">
    <home-carousel></home-carousel>

    <a-spin :spinning="topProductLoading">
      <product-card :title="topProduct['title']" :products="topProduct['list']"></product-card>
    </a-spin>

    <a-spin :spinning="productsLoading">
      <product-card
        v-for="product in products"
        :key="product['title']"
        :title="product['title']"
        :products="product['list']"
      ></product-card>
    </a-spin>
    <a-spin :spinning="articleLoading">
      <div :style="{ background: '#fff', padding: '24px', minHeight: '280px', 'margin': '20px 0' }">
        <a-tabs defaultActiveKey="1" @change="onArticleTabChange">
          <a-tab-pane v-for="(item, index) in articleTags" :key="item.id" :tab="item.title">
            <article-list :articles="articles"></article-list>
          </a-tab-pane>
        </a-tabs>
      </div>
    </a-spin>
  </div>
</template>
<script>
const baseUrl =
  "https://raw.githubusercontent.com/vueComponent/ant-design-vue/master/components/vc-slick/assets/img/react-slick/";

const cache = {
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
      baseUrl,
      articleTags: [],
      articleLoading: false,
      articles: []
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
    getImgUrl(i) {
      return `${baseUrl}abstract0${i + 1}.jpg`;
    },
    onChange(a, b, c) {},
    onArticleTabChange(index) {
      const url = `${this.apiUrl}/public/home/articles/${index}/1/10`;

      if (!!cache.articles[index]) {
        this.articles = cache.articles[index];
        return;
      }

      this.articleLoading = true;
      this.$g
        .get(url)
        .cb(data => {
          if (data.status === 0) {
            this.articles = data.list;
            cache.articles[index] = data.list;
          } else {
            console.error('load articles error--->', data);
          }
        })
        .fcb(() => this.articleLoading = false)
        .req();
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
      this.$g
        .get(url)
        .cb(data => {
          if (data.status === 0) {
            this.articleTags = data.item.tags;
            this.articles = data.item.articles;
          }
        })
        .fcb()
        .req();
    }
  }
};
</script>
<style scoped>
</style>