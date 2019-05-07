<template>
  <div>
    <a-row
      :style="{ background: '#fff', padding: '24px', minHeight: '280px', 'margin': '30px 0', 'width:': '100%','text-align':'left'}"
    >
      <a-col :span="12">
        <div id="J_sliderView" class style="width: auto; position: relative;margin:0 0 30px 0;">
          <img
            data-src="//i8.mifile.cn/a1/pms_1550642240.48638886.jpg"
            class="slider done"
            style="float: none; list-style: outside none none; width: 540px; z-index: 50; display: block;"
            src="//i8.mifile.cn/a1/pms_1550642240.48638886.jpg"
          >
        </div>
      </a-col>
      <a-col :span="12" style="padding: 50px 0 0 0;">
        <a-form :form="form" @submit="handleSubmit">
          <a-form-item label="Login" :label-col="{ span: 5 }" :wrapper-col="{ span: 12 }">
            <a-input></a-input>
          </a-form-item>

          <a-form-item label="Password" :label-col="{ span: 5 }" :wrapper-col="{ span: 12 }">
            <a-input></a-input>
          </a-form-item>
          <a-form-item :wrapper-col="{ span: 12, offset: 5 }">
            <a-button type="primary">Submit</a-button>
          </a-form-item>
        </a-form>
      </a-col>
    </a-row>
  </div>
</template>
<script>
export default {
  methods: {
    handleSubmit() {}
  }
};
</script>
<style scoped>
</style>
