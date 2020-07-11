<template>
  <div>
    <a-layout-sider v-model="collapsed" :trigger="null" collapsible>
      <a-menu theme="light" mode="inline" :default-selected-keys="index" :defaultOpenKeys="index">
        <a-menu-item key="1">
          <router-link :to="'/dashboard'">
            <a-icon type="dashboard" />
            <span>面板</span>
          </router-link>
        </a-menu-item>
        <a-sub-menu key="2">
          <span slot="title">
            <a-icon type="home" />
            <span>首页设置</span>
          </span>
          <a-menu-item key="2-1">
            <router-link :to="'/carousel'">轮播图设置</router-link>
          </a-menu-item>
          <a-menu-item key="2-2">
            <router-link :to="'/home-nav'">首页导航设置</router-link>
          </a-menu-item>
        </a-sub-menu>
        <a-menu-item key="3">
          <router-link :to="'/tags'">
            <a-icon type="tags" />
            <span>标签</span>
          </router-link>
        </a-menu-item>
        <a-menu-item key="4">
          <router-link :to="'/commodities'">
            <a-icon type="shop" />
            <span>商品</span>
          </router-link>
        </a-menu-item>
        <a-menu-item key="5">
          <router-link :to="'/articles'">
            <a-icon type="read" />
            <span>文章</span>
          </router-link>
        </a-menu-item>
        <a-menu-item key="6">
          <router-link :to="'/cards'">
            <a-icon type="gift" />
            <span>礼品卡</span>
          </router-link>
        </a-menu-item>
        <a-menu-item key="7">
          <router-link :to="'/accounts'">
            <a-icon type="user" />
            <span>用户管理</span>
          </router-link>
        </a-menu-item>
        <a-menu-item key="9">
          <router-link :to="'/orders'">
            <a-icon type="shopping-cart" />
            <span>订单管理</span>
          </router-link>
        </a-menu-item>
        <a-menu-item key="10">
          <router-link :to="'/order-line'">
            <a-icon type="money-collect" />
            <span>流水管理</span>
          </router-link>
        </a-menu-item>
        <a-menu-item key="8">
          <router-link :to="'/setting'">
            <a-icon type="setting" />
            <span>系统管理</span>
          </router-link>
        </a-menu-item>
      </a-menu>
    </a-layout-sider>
  </div>
</template>
<script>
export default {
  props: ["index"],
  data() {
    return {
      collapsed: false
    };
  },
  mounted() {}
};
</script>