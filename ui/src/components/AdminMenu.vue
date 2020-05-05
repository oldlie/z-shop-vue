<template>
  <a-menu
    @click="handleClick"
    style="width: 160px;min-height:280px;"
    :defaultSelectedKeys="[index]"
    :openKeys.sync="openKeys"
    mode="inline"
  >
    <a-sub-menu key="sub1" @titleClick="titleClick">
      <span slot="title">
        <a-icon type="home" />
        <router-link to="/admin/dashboard">首页设置</router-link>
      </span>
      <a-menu-item key="1">轮播图设置</a-menu-item>
      <a-menu-item key="5">快捷导航设置</a-menu-item>
      <a-menu-item key="2">New Product</a-menu-item>
      <a-menu-item key="3">Products Setting</a-menu-item>
      <a-menu-item key="4">文章</a-menu-item>
    </a-sub-menu>
    <a-menu-item key="tags">
      <router-link to="/admin/tags">目录/标签</router-link>
    </a-menu-item>
    <a-menu-item key="sub2">
      <router-link to="/admin/products">商品</router-link>
    </a-menu-item>
    <a-menu-item key="sub3">
      <router-link to="/admin/article">文章</router-link>
    </a-menu-item>
    <a-menu-item key="sub4">
      <router-link to="/admin/pay-card">礼品卡</router-link>
    </a-menu-item>
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
      index: JSON.parse(JSON.stringify(this.activeIndex)),
      current: [this.index],
      openKeys: [this.openKey]
    };
  },
  methods: {
    handleClick(e) {
      if (this.index !== e.key) {
        this.index = e.key;
        this.$emit("menuChangeEvent", e.key);
      }
    },
    titleClick(e) {}
  },
  watch: {
    openKeys(val) {}
  }
};
</script>
<style scoped>
</style>
