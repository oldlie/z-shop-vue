<template>
  <div id="homePage">

    <div style="width: 100%; display: inline-block;position:relative;margin: 20px 0">
      <a-carousel arrows :afterChange="onChange" dotsClass="slick-dots slick-thumb">
        <a slot="customPaging" slot-scope="props">
          <img :src="getImgUrl(props.i)" :style="{height:'20px',width:'60px'}">
        </a>
        <div v-for="item in 4" :key="item">
          <img
            :src="baseUrl+'abstract0'+item+'.jpg'"
            :style="{height:'400px',width:'1200px','min-width':'247px','min-height':'186px'}"
          >
        </div>
      </a-carousel>
    </div>

    <div :style="{ background: '#fff', padding: '24px', minHeight: '280px', 'margin': '30px 0' }">
      <product-card :title="topProduct['title']" :products="topProduct['list']"></product-card>
      <product-card
        v-for="product in products"
        :key="product['title']"
        :title="product['title']"
        :products="product['list']"
      ></product-card>
    </div>

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
      console.log(a, b, c);
    },
    onTabChange() {}
  }
};
</script>
<style scoped>
</style>