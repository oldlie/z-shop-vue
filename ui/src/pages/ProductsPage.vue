<template>
  <div
    id="prodctsPage"
    :style="{ background: '#fff', padding: '24px', minHeight: '280px', 'margin': '30px 0' }"
  >
    <a-tabs defaultActiveKey="水产品" @change="onTabChange">
      <a-tab-pane :tab="product['title']" v-for="product in products" :key="product['title']">
        <a-row>
          <a-col :span="6" v-for="item in product['list']" :key="item['id']">
            <router-link :to="{ path: '/product/' + item['id']}">
              <a-card hoverable class="product-card">
                <img :alt="item['title']" :src="item['image']" slot="cover">
                <a-card-meta title="Europe Street beat">
                  <template slot="description">www.instagram.com</template>
                </a-card-meta>
              </a-card>
            </router-link>
          </a-col>
        </a-row>
      </a-tab-pane>
    </a-tabs>
  </div>
</template>
<script>
export default {
  name: "ProductsPage",
  data() {
    return {
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
      ]
    };
  },
  methods: {
    onTabChange() {}
  }
};
</script>
<style scoped>
.product-card {
  width: 240px;
  height: 400px;
  margin: 10px;
  text-align: center;
  margin: 0 auto;
}
</style>

