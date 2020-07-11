<template>
  <div
    id="prodctsPage"
    :style="{ background: '#fff', padding: '24px', minHeight: '280px', 'margin': '30px 0' }"
  >
    <a-spin :spinning="loading">
      <a-row class="inner-row" style="text-align:left;">
        <h2>{{!!queryTitle? queryTitle : tagName}}</h2>
      </a-row>
      <a-row class="inner-row">
        <a-col :span="6" v-for="item in products" :key="item['id']">
          <a :href="`#/product/${item.id}`" target="_blank">
            <a-card hoverable class="product-card">
              <img :alt="item['title']" :src="item['thumbnail']" slot="cover"  style="height: 238px; width: 238px;object-fit:cover"/>
              <a-card-meta :title="item['title']">
                <template slot="description">{{item['introduction']}}</template>
              </a-card-meta>
            </a-card>
          </a>
        </a-col>
      </a-row>
    </a-spin>
  </div>
</template>
<script>
export default {
  name: "ProductsPage",
  data() {
    return {
      tagId: 0,
      queryTitle: '',
      loading: false,
      tagName: "全部商品",
      products: [],
      page: 1,
      size: 10
    };
  },
  watch : {
   $route(to, from) {
     this.tagId = !!to.query.tagId ? to.query.tagId : 0;
     this.queryTitle = to.query.t;
     this.load();
   } 
  },
  mounted() {
    this.tagId = !!this.$route.query.tagId ? this.$route.query.tagId : 0;
    this.queryTitle = !!this.$route.query.t ? this.$route.query.t : false;
    this.load();
  },
  methods: {
    load() {
      let url;
      if (!!this.queryTitle) {
        url = `${this.apiUrl}/public/home/commodities/${this.page}/${this.size}/title/${this.queryTitle}`;
      } else {
      url = `${this.apiUrl}/public/home/commodities/${this.tagId}/${this.page}/${this.size}`;
      }
      this.loading = true;
      this.$g
        .get(url)
        .cb(data => {
          if (data.status === 0) {
            const item = data.item;
            this.tagName = item.tag.title;
            this.products = item.commodities;
          } else {
            console.error(data);
          }
        })
        .fcb(() => (this.loading = false))
        .req();
    },
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
  margin: 0 auto 25px auto;
}
</style>

