<template>
  <div id="prodcutCard">
    <a-row :style="{'margin': '0' }">
      <a-col :span="12">
        <h1 style="color:#2c3e50;text-align:left;padding:0;">{{title}}</h1>
      </a-col>
      <a-col :span="12" style="text-align:right;padding: 15px 0 0 0;">
        <router-link :style="{'text-align': 'right', 'color':'#2c3e50'}" :to="to">
          查看全部
          <a-icon type="right-circle" />
        </router-link>
      </a-col>
    </a-row>
    <a-row
      :style="{ background: '#fff', padding: '24px', minHeight: '280px', 'margin': '0 0 50px 0' }"
    >
      <a-col :span="6" v-for="item in products" :key="item['id']">
        <a :href="`#/product/${item.id}`" target="_blank">
          <a-card hoverable class="product-card">
            <img :alt="item['title']" :src="item['thumbnail']" slot="cover" style="height: 238px; width: 238px;object-fit:cover" />
            <a-card-meta :title="item['title']">
              <template slot="description">{{item['introduction']}}</template>
            </a-card-meta>
          </a-card>
        </a>
      </a-col>
    </a-row>
  </div>
</template>
<script>
export default {
  name: "ProductCard",
  props: {
    title: String,
    products: Array,
    to: String
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


