<template>
  <div class="main-panel">
    <a-row>
      <a-col :span="6">
        <a-menu
          @click="handleClick"
          style="width: 256px;min-height:280px;"
          :defaultSelectedKeys="['1']"
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
          <a-sub-menu key="sub2" @titleClick="titleClick">
            <span slot="title">
              <span>Products</span>
            </span>
            <a-menu-item key="5">List</a-menu-item>
            <a-menu-item key="6">Tags</a-menu-item>
          </a-sub-menu>
          <a-sub-menu key="sub4">
            <span slot="title">
              <span>Articles</span>
            </span>
            <a-menu-item key="7">List</a-menu-item>
            <a-menu-item key="8">Tags</a-menu-item>
          </a-sub-menu>
        </a-menu>
      </a-col>
      <a-col :span="18"></a-col>
    </a-row>
  </div>
</template>
<script>
export default {
  data() {
    return {
      current: ["mail"],
      openKeys: ["sub1"]
    };
  },
  methods: {
    handleClick(e) {
      console.log("click", e);
    },
    titleClick(e) {
      console.log("titleClick", e);
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
