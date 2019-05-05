<template>
  <a-form>
    <a-form-item label="Title" :label-col="{ span: 5 }" :wrapper-col="{ span: 12 }">
      <a-input></a-input>
    </a-form-item>
    <a-form-item label="Order" :label-col="{ span: 5 }" :wrapper-col="{ span: 12 }">
      <a-input-number :min="1" :max="10"/>
    </a-form-item>
    <a-form-item :wrapper-col="{ span: 12, offset:5 }">
      <a-button icon="save">Submit</a-button>
    </a-form-item>
  </a-form>
</template>
<script>
export default {};
</script>
<style scoped>
</style>
