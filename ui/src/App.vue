<template>
  <div id="app">
    <a-layout id="components-layout-demo-top" class="layout">
      <a-layout-header>
        <div class="user-panel">
          <div>
            <a href="#">登录</a>
            <a-divider type="vertical"/>
            <a href="#">注册</a>
            <a-divider type="vertical"/>
            <a href="#">
              <a-icon type="shopping-cart"/>购物车
            </a>
          </div>
        </div>
        <a-menu
          mode="horizontal"
          :defaultSelectedKeys="['2']"
          :style="{ lineHeight: '64px'}"
        >
          <a-menu v-model="current" mode="horizontal" >
            <a-menu-item key="home">
              <router-link to="/">首页</router-link>
            </a-menu-item>
            <a-sub-menu>
              <span slot="title" class="submenu-title-wrapper">
                <router-link to="/products" style="color:rgba(0, 0, 0, 0.65)">产品</router-link>
              </span>
              <a-sub-menu>
                <span slot="title" class="submenu-title-wrapper">水产品</span>
                <a-menu-item key="setting:3">螃蟹</a-menu-item>
              </a-sub-menu>
              <a-menu-item key="setting:1">水产品</a-menu-item>
              <a-menu-item key="setting:2">地标产品</a-menu-item>
            </a-sub-menu>
            <a-sub-menu>
              <span slot="title" class="submenu-title-wrapper">
                <router-link to="/articles" style="color:rgba(0, 0, 0, 0.65)">资讯</router-link>
              </span>
              <a-menu-item key="article:1">重要通知</a-menu-item>
              <a-menu-item key="article:2">最新文章</a-menu-item>
            </a-sub-menu>
          </a-menu>
        </a-menu>
      </a-layout-header>
      <a-layout-content style="padding: 0 50px">
        <a-row class="all-categories">
          <a-col :span="4">
            <div class="logo"/>
          </a-col>
          <a-col :span="16" :style="{'text-align': 'left', 'padding': '20px 0 0 0'}">
            <span>全部产品分类</span>
            <a-divider type="vertical"/>
            <a href="#">商标局</a>
            <a-divider type="vertical"/>
            <a href="#">农业部</a>
            <a-divider type="vertical"/>
            <a href="#">知识产权出版社</a>
            <a-divider type="vertical"/>
            <a href="#">海中地标平台</a>
          </a-col>
          <a-col :span="4" :style="{'padding': '20px 10px 0 0'}">
            <a-input-search placeholder="input search text" style="width: 100%"/>
          </a-col>
        </a-row>

        <router-view></router-view>

        <a-row class="friend-links">
          <a-col :span="24">
            <a href="#">国家知识产权局</a>
            <a-divider type="vertical"/>
            <a href="#">商标局</a>
            <a-divider type="vertical"/>
            <a href="#">农业部</a>
            <a-divider type="vertical"/>
            <a href="#">知识产权出版社</a>
            <a-divider type="vertical"/>
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
                src="http://localhost/resoucres/images/wd.jpg"
                alt="知人甄选"
              >
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
  </div>
</template>

<script>
import HelloWorld from "./components/HelloWorld.vue";

export default {
  name: "app",
  components: {
    HelloWorld
  },
  data() {
    return {
      current: ["home"]
    };
  },
  methods: {
    onChange(a, b, c) {
      console.log(a, b, c);
    }
  }
};
</script>

<style>
#components-layout-demo-top .logo {
  width: 158px;
  height: 31px;
  background: rgba(255, 255, 255, 0.2);
  margin: 16px 24px 16px 0;
  float: left;
  background-repeat: no-repeat;
  background-image: url(http://localhost/resoucres/images/logo.png);
  background-size: 100% 100%;
  -moz-background-size: 100% 100%;
  margin-left: 5px;
}

#components-layout-demo-top .ant-layout-header {
  background: #f0f2f5;
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

.user-panel {
  width: 180px;
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

.ant-carousel .slick-slide h3 {
  color: #ff6700;
}

.ant-carousel .slick-dots {
  height: auto;
}
.ant-carousel .slick-slide img {
  border: 5px solid #fff;
  display: block;
  margin: auto;
  max-width: 100%;
}
.ant-carousel .slick-thumb {
  bottom: -45px;
}
.ant-carousel .slick-thumb li {
  width: 60px;
  height: 45px;
}
.ant-carousel .slick-thumb li img {
  width: 100%;
  height: 100%;
  filter: grayscale(100%);
}
.ant-carousel .slick-thumb li.slick-active img {
  filter: grayscale(0%);
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
