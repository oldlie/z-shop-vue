<template>
  <div class="user-panel">
    <template v-if="isAdmin">
      <router-link to="/admin/dashboard">Dashboard</router-link>
     <a-divider type="vertical"/> 
    </template>
    <template v-if="username !== ''">
      <router-link to="/profile">{{username}}</router-link>
    </template>
    <template v-else>
      <router-link to="/login">登录</router-link>
      <a-divider type="vertical"/>
      <a href="#">注册</a>
    </template>
    <a-divider type="vertical"/>
    <router-link to="/cart">
      <a-icon type="shopping-cart"/>购物车
    </router-link>
  </div>
</template>
<script>
export default {
  props: {
      username: String,
      isAdmin: Boolean
  },
  data() {
    return {}
  }
};
</script>
<style scoped>
</style>
