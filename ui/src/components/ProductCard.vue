<template>
  <div id="prodcutCard">
    <a-row :style="{'margin': '0' }">
      <a-col :span="12">
        <h1 style="color:#2c3e50;text-align:left;padding:0;">{{title}}</h1>
      </a-col>
      <a-col :span="12" style="text-align:right;padding: 15px 0 0 0;">
        <router-link :style="{'text-align': 'right', 'color':'#2c3e50'}" to="/">
          查看全部
          <a-icon type="right-circle"/>
        </router-link>
      </a-col>
    </a-row>
    <a-row :style="{ background: '#fff', padding: '24px', minHeight: '280px', 'margin': '0 0 50px 0' }">
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


