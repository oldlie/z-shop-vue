<template>
  <div class="user-panel">
    <template v-if="isAdmin">
      <router-link to="/admin/dashboard">管理员</router-link>
      <a-divider type="vertical" />
    </template>
    <router-link to="/cart">
      <a-icon type="shopping-cart" />购物车
    </router-link>
    <a-divider type="vertical" />
    <template v-if="!!username">
      <router-link to="/profile">{{username}}</router-link>
      <a-divider type="vertical" />
      <a @click="logout">退出</a>
    </template>
    <template v-else>
      <router-link to="/login">登录</router-link>
      <a-divider type="vertical" />
      <a href="#">注册</a>
    </template>
  </div>
</template>
<script>
export default {
  props: {
    username: String,
    isAdmin: Boolean
  },
  data() {
    return {};
  },
  methods: {
    logout() {
      let url = this.apiUrl + "/logout";
      G.get(url)
        .callback(data => {
          Cookie.clearCookie('token');
          console.log('logout ===> ', data)
        })
        .request();
    }
  }
};
</script>
<style scoped>
</style>
