<template>
  <div id="homePage">
    
    <home-carousel></home-carousel>

    <product-card :title="topProduct['title']" :products="topProduct['list']"></product-card>

    <product-card
      v-for="product in products"
      :key="product['title']"
      :title="product['title']"
      :products="product['list']"
    ></product-card>

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
      topProduct: {
        title: "今日上新",
        list: [
          {
            id: 1,
            title: "Example",
            image: "https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png"
          },
          {
            id: 2,
            title: "Example",
            image: "https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png"
          },
          {
            id: 3,
            title: "Example",
            image: "https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png"
          },
          {
            id: 4,
            title: "Example",
            image: "https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png"
          }
        ]
      },
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
  methods: {
    getImgUrl(i) {
      return `${baseUrl}abstract0${i + 1}.jpg`;
    },
    onChange(a, b, c) {
    },
    onTabChange() {}
  }
};
</script>
<style scoped>
</style>