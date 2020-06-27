<template>
  <div>
    <a-row :gutter="4">
      <a-col :span="4">
        <sidebar :index="[]" />
      </a-col>
      <a-col :span="20">
        <div class="inner-row">
          <h1>404 页面找不到了</h1>
          <p>您访问的页面找不到了！</p>
        </div>
      </a-col>
    </a-row>
  </div>
</template>
<script>
export default {};
</script>