<template>
  <div id="app">
    <a-layout id="components-layout-demo-top" class="layout">
      <a-layout-header>
        <div class="user-panel-left"></div>
        <user-panel-links></user-panel-links>
        <a-menu
          mode="horizontal"
          :defaultSelectedKeys="['2']"
          :style="{ lineHeight: '40px', 'background': '#333'}"
        >
          <a-menu v-model="current" mode="horizontal">
            <a-menu-item key="home">
              <router-link to="/">首页</router-link>
            </a-menu-item>
            <a-menu-item key="products">
              <router-link to="/products">产品</router-link>
            </a-menu-item>
            <a-menu-item key="articles">
              <router-link to="/articles">资讯</router-link>
            </a-menu-item>
          </a-menu>
        </a-menu>
      </a-layout-header>
      <a-layout-content style="padding: 0;width: 1200px;margin:0 auto;">
        <a-row class="all-categories">
          <a-col :span="4">
            <div class="logo" />
          </a-col>
          <a-col :span="14" :style="{'text-align': 'left', 'padding': '20px 0 0 0'}">
            <span>
              <router-link to="/products">全部产品分类</router-link>
            </span>
            <span v-for="(item, index) in quickNavTags" :key="index">
              <a-divider type="vertical" />
              <router-link :to="`/products?tagId=${item.tagId}`">{{item.tagTitle}}</router-link>
            </span>
          </a-col>
          <a-col :span="6" :style="{'padding': '20px 0 0 0'}">
            <a-input-search
              @search="searchCommodities"
              v-model="searchText.value"
              :validate-status="searchText.status"
              :help="searchText.help"
              has-feedback
              placeholder="请输入产品名称"
              style="width: 100%"
            />
          </a-col>
        </a-row>

        <router-view></router-view>

        <a-row class="friend-links">
          <a-col :span="24">
            <a href="#">国家知识产权局</a>
            <a-divider type="vertical" />
            <a href="#">商标局</a>
            <a-divider type="vertical" />
            <a href="#">农业部</a>
            <a-divider type="vertical" />
            <a href="#">知识产权出版社</a>
            <a-divider type="vertical" />
            <a href="#">海中地标平台</a>
          </a-col>
        </a-row>
      </a-layout-content>

      <a-layout-footer
        class="footer"
        :style="{'text-align': 'center','background': '#2f4056','color': '#ffffff','bottom': '0', 'padding-bottom': '50px'}"
      >
        <a-row>
          <a-col :span="8">
            <h3>主办单位</h3>
            <p>知人甄选</p>
            <p>地址：北京市海淀区气象路50号</p>
          </a-col>
          <a-col :span="8">
            <h3>微信公众号</h3>
            <p>
              <img
                width="128px"
                height="128px"
                src="http://39.96.190.31/resources/images/wd.jpg"
                alt="知人甄选"
              />
            </p>
          </a-col>
          <a-col :span="8">
            <h3>联系方式</h3>
            <p>QQ：16859307</p>
            <p>email：16859307@qq.com</p>
            <p>微信号：cnipren</p>
            <p>微信公众号：zhirenzx</p>
          </a-col>
        </a-row>

        <a-row>
          <a-col :span="24">
            <p>Copyright © 2019 知人甄选 京ICP备14020289号</p>
          </a-col>
        </a-row>
      </a-layout-footer>
    </a-layout>

    <a-back-top />
  </div>
</template>

<script>
export default {
  name: "app",
  data() {
    return {
      current: ["home"],
      quickNavTags: [],
      searchText: { value: "", status: "", help: "" }
    };
  },
  created() {},
  mounted() {
    this.loadQuickNavTags();
  },
  methods: {
    onChange(a, b, c) {},
    loadQuickNavTags() {
      const url = `${this.apiUrl}/public/home/quick-nav-tags`;
      this.$g
        .get(url)
        .cb(data => {
          if (data.status === 0) {
            this.quickNavTags = data.list;
          }
        })
        .fcb()
        .req();
    },
    searchCommodities() {
      if (!this.searchText.value || this.searchText.value === "") {
        this.searchText.status = "error";
        this.searchText.help = "请填写商品名称";
        return;
      }
      this.searchText.status = "success";
      this.searchText.help = "";
      this.$router.push(`/products?t=${this.searchText.value}`);
    }
  }
};
</script>

<style>
.spin-content {
  border: 1px solid #91d5ff;
  background-color: #e6f7ff;
  padding: 30px;
}

#components-layout-demo-top .logo {
  width: 158px;
  height: 31px;
  background: rgba(255, 255, 255, 0.2);
  margin: 16px 24px 16px 0;
  float: left;
  background-repeat: no-repeat;
  background-image: url(http://39.96.190.31/resources/images/logo.png);
  background-size: 100% 100%;
  -moz-background-size: 100% 100%;
  margin-left: 5px;
}

#components-layout-demo-top .ant-layout-header {
  padding: 0;
  height: 40px;
  color: #b0b0b0;
  font-size: 12px;
  margin-bottom: 24px;
}
#app {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  min-width: 1200px;
  margin: 0 auto;
  background: #f0f2f5;
}
.user-panel-left {
  width: 180px;
  float: left;
  margin-top: 1px;
  line-height: 46px;
  margin: 0;
  box-sizing: border-box;
}
.user-panel {
  width: 260px;
  float: right;
  margin-top: 1px;
  line-height: 46px;
  margin: 0;
  box-sizing: border-box;
}
.user-panel > div {
  box-sizing: border-box;
  padding: 0 0;
}
.user-panel a {
  color: rgba(0, 0, 0, 0.65);
}
.user-panel a:hover {
  color: rgba(0, 0, 0, 0.65);
}

.ant-carousel .slick-slide {
  text-align: center;
  height: 400px;
  /*line-height: 410px;*/
  background: #f0f2f5;
  overflow: hidden;
}

.footer h3 {
  color: #ffffff;
}

.footer a {
  text-decoration: none;
  color: #ffffff;
}

.footer .foot-content {
  margin: auto;
  padding: 24px;
  min-height: 280px;
  width: 960px;
}

.all-categories {
  width: 100%;
  margin: 0 0 0 0;
  text-align: left;
}
.all-categories a {
  color: #2c3e50;
}

.friend-links {
  width: 100%;
  height: 64px;
  line-height: 64px;
  margin: 0 0 30px 0;
  background: #fff;
  padding: 24px;
}
.friend-links a {
  color: #2c3e50;
}
</style>
