<template>
  <div id="app">
    <a-layout-header style="background-color:#f0f2f5">
      <span>后台管理</span>
    </a-layout-header>
    <a-layout-content style="min-height:350px">
      <router-view></router-view>
    </a-layout-content>
    <a-layout-footer>
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
              src="http://localhost/resources/images/wd.jpg"
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
  </div>
</template>

<script>
export default {
  name: "App",
  data() {
    return {}
  }
};
</script>

<style>
#app {
  min-width: 1200px;
  box-sizing: border-box;
}
</style>
