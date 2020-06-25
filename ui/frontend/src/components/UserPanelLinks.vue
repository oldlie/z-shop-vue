<template>
  <a-spin class="user-panel" :spinning="userInfoLoading">
    <template v-if="userInfo.isAdmin">
      <router-link to="/admin/dashboard">管理员</router-link>
      <a-divider type="vertical" />
    </template>
    <!--
    <router-link to="/cart">
      <a-icon type="shopping-cart" />购物车
    </router-link>
    -->
    <router-link to="/order">
      <a-icon type="gift" />订单管理
    </router-link>
    <a-divider type="vertical" />
    <template v-if="!!userInfo.username">
      <router-link to="/profile">{{userInfo.username}}</router-link>
      <a-divider type="vertical" />
      <a @click="logout">退出</a>
    </template>
    <template v-else>
      <router-link to="/login">登录</router-link>
      <a-divider type="vertical" />
      <a href="#">注册</a>
    </template>
  </a-spin>
</template>
<script>
export default {
  data() {
    return {
      count: 1,
      userInfo: {
        username: "",
        roles: [],
        isAdmin: false
      },
      userInfoLoading: false
    };
  },
  watch: {
    userInfo(nv, ov) {
      this.userInfo = nv;
    }
  },
  mounted() {
    const self = this;
    if (!this.bus._events["updateUserInfoEvent"]) {
      this.bus.$on("updateUserInfoEvent", () => {
        self.loadUserInfo();
      });
    }

    const token = this.$cookie.get("token");
    if (!!token) {
      this.loadUserInfo();
    } else {
      this.$router.push("/login");
    }
  },
  methods: {
    logout() {
      const url = this.apiUrl + "/logout";
      const self = this;
      G.get(url)
        .callback(data => {
          self.$cookie.delete("token");
          self.bus.$emit("updateUserInfoEvent");
        })
        .request();
    },
    updateUserInfo(userInfo) {
      this.userInfo = userInfo;
    },
    loadUserInfo() {
      this.userInfoLoading = true;
      const url = this.apiUrl + "/name";
      const self = this;

      G.get(url)
        .callback(function(data) {
          if (data["status"] === 0) {
            let user = data["item"];
            let userInfo = {};
            userInfo["username"] = user["username"];
            if (user["username"] === null) {
              self.userInfo = {
                username: "",
                roles: [],
                isAdmin: false
              };
              return;
            }
            userInfo["roles"] = user["roles"];
            if (self.userInfo.roles !== null) {
              let flag = false;
              for (let key in user["roles"]) {
                if (user["roles"][key]["name"] === "ADMIN") {
                  flag = true;
                  break;
                }
              }
              userInfo["isAdmin"] = flag;
              self.userInfo = JSON.parse(JSON.stringify(userInfo));
            }
          } else {
            self.userInfo = {
              username: "",
              roles: [],
              isAdmin: false
            };
          }
        })
        .finalCallback(() => {
          self.userInfoLoading = false;
        })
        .request();
    }
  }
};
</script>
<style scoped>
</style>
