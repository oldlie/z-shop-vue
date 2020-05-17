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
    <div :style="{ background: '#fff', padding: '24px', minHeight: '280px', 'margin': '20px 0' }">
      <a-tabs defaultActiveKey="1" @change="onTabChange">
        <a-tab-pane tab="最新资讯" key="1">
          <article-list></article-list>
        </a-tab-pane>
        <a-tab-pane tab="重要通知" key="2" forceRender>
          <article-list></article-list>
        </a-tab-pane>
        <a-tab-pane tab="试吃测评" key="3">
          <article-list></article-list>
        </a-tab-pane>
        <a-tab-pane tab="地理标志" key="4">
          <article-list></article-list>
        </a-tab-pane>
      </a-tabs>
    </div>
  </div>
</template>
<script>
const baseUrl =
  "https://raw.githubusercontent.com/vueComponent/ant-design-vue/master/components/vc-slick/assets/img/react-slick/";

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
      products: [
        {
          title: "水产品",
          list: [
            {
              id: 1,
              title: "Example",
              image:
                "https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png"
            },
            {
              id: 2,
              title: "Example",
              image:
                "https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png"
            },
            {
              id: 3,
              title: "Example",
              image:
                "https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png"
            },
            {
              id: 4,
              title: "Example",
              image:
                "https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png"
            }
          ]
        },
        {
          title: "水果",
          list: [
            {
              id: 1,
              title: "Example",
              image:
                "https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png"
            },
            {
              id: 2,
              title: "Example",
              image:
                "https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png"
            },
            {
              id: 3,
              title: "Example",
              image:
                "https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png"
            },
            {
              id: 4,
              title: "Example",
              image:
                "https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png"
            }
          ]
        },
        {
          title: "零食",
          list: [
            {
              id: 1,
              title: "Example",
              image:
                "https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png"
            },
            {
              id: 2,
              title: "Example",
              image:
                "https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png"
            },
            {
              id: 3,
              title: "Example",
              image:
                "https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png"
            },
            {
              id: 4,
              title: "Example",
              image:
                "https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png"
            }
          ]
        },
        {
          title: "生活用品",
          list: [
            {
              id: 1,
              title: "Example",
              image:
                "https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png"
            },
            {
              id: 2,
              title: "Example",
              image:
                "https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png"
            },
            {
              id: 3,
              title: "Example",
              image:
                "https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png"
            },
            {
              id: 4,
              title: "Example",
              image:
                "https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png"
            }
          ]
        }
      ],
      baseUrl
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
    onTabChange() {},
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
          console.log('load products --->', data)
          if (data.status === 0) {
            this.products = data.list;
            
          } else {
            console.error("load products error --->", data);
          }
        })
        .fcb(() => (this.productsLoading = false))
        .req();
    },
    loadArticles () {
      const url = `${this.apiUrl}/public/home/articles`;
      this.$g.get(url)
      .cb(data => {
        console.log('load articles --->', data)
      })
      .fcb()
      .req();
    }
  }
};
</script>
<style scoped>
</style>