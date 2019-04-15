<template>
  <div id="prodcutCard">
    <a-divider><h1 style="color:#ff6700">{{title}}</h1></a-divider>
    <a-row>
      <a-col :span="6" v-for="item in products" :key="item['id']">
        <a-card hoverable class="product-card">
          <img :alt="item['title']" :src="item['image']" slot="cover">
          <a-card-meta title="Europe Street beat">
            <template slot="description">www.instagram.com</template>
          </a-card-meta>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>
<script>
export default {
  name: "ProductCard",
  props: {
    title: String,
    products: Array
  }
};
</script>
<style scoped>
.product-card {
  width: 240px;
  height: 400px;
  margin: 10px;
}
</style>


