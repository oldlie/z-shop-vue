<template>
  <div>
    <a-row >
      <a-col :span="4">
        <sidebar :index="['2', '2-2']" />
      </a-col>
      <a-col :span="20">
        <a-row class="inner-row">
            <a-col :span="24">
              <h2>导航设置</h2>
            </a-col>
          </a-row>
        <quick-nav-tags :title="'快捷导航'" :root-id="0" />
        <home-commodity-tags :title="'首页商品'" :root-id="0" :delay="200" />
        <home-article-tags :title="'首页文章'" :root-id="0" :delay="500"></home-article-tags>
      </a-col>
    </a-row>
  </div>
</template>
<script>
export default {
  
}
</script>