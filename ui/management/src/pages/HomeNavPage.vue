<template>
  <div>
    <a-row>
      <a-col :span="4">
        <sidebar :index="['2', '2-2']" />
      </a-col>
      <a-col :span="20">
        <tags :title="'快捷导航'" :root-id="0" />
      </a-col>
    </a-row>
  </div>
</template>
<script>
export default {
  
}
</script>