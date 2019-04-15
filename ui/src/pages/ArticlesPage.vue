<template>
  <div :style="{ background: '#fff', padding: '24px', minHeight: '280px', 'margin': '30px 0' }">
    <a-tabs defaultActiveKey="1" @change="onTabChange">
      <a-tab-pane tab="最新资讯" key="1">
        <article-list></article-list>
      </a-tab-pane>
      <a-tab-pane tab="重要通知" key="2" forceRender>
        <article-list></article-list>
      </a-tab-pane>
      <a-tab-pane tab="试吃测评" key="3">
        <article-list></article-list>
      </a-tab-pane>
      <a-tab-pane tab="地理标志" key="4">
        <article-list></article-list>
      </a-tab-pane>
    </a-tabs>
  </div>
</template>
<script>
export default {
  name: "ArticlesPage",
  methods: {
      onTabChange () {}
  }
};
</script>

