<template>
  <a-menu
    @click="handleClick"
    style="width: 256px;min-height:280px;"
    :defaultSelectedKeys="[activeIndex]"
    :openKeys.sync="openKeys"
    mode="inline"
  >
    <a-sub-menu key="sub1" @titleClick="titleClick">
      <span slot="title">
        <a-icon type="home"/>
        <span>Home Page Setting</span>
      </span>
      <a-menu-item key="1">Carousel Setting</a-menu-item>
      <a-menu-item key="2">New Product</a-menu-item>
      <a-menu-item key="3">Products Setting</a-menu-item>
      <a-menu-item key="4">Aritcles Setting</a-menu-item>
    </a-sub-menu>
    <a-menu-item key="tags"><router-link to="/admin/tags">目录/标签</router-link></a-menu-item>
    <a-menu-item key="sub2"><router-link to="/admin/products">Products</router-link></a-menu-item>
    <a-sub-menu key="sub4">
      <span slot="title">
        <span>Articles</span>
      </span>
      <a-menu-item key="7">List</a-menu-item>
      <a-menu-item key="8">Tags</a-menu-item>
    </a-sub-menu>
  </a-menu>
</template>
<script>
export default {
  props: {
    activeIndex: String, 
    openKey: String
  },
  data() {
    return {
      current: [this.activeIndex],
      openKeys: [this.openKey]
    };
  },
  methods: {
    handleClick(e) {
      console.log("click", e);
      console.log("click", e.key);
      if (this.activeIndex !== e.key) {
        this.activeIndex = e.key;
        this.$emit('menuChangeEvent', e.key);
      }
    },
    titleClick(e) {

    }
  },
  watch: {
    openKeys(val) {
      console.log("openKeys", val);
    }
  }
};
</script>
<style scoped>
</style>
