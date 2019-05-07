<template>
  <div
    :style="{ background: '#fff', padding: '24px', minHeight: '280px', 'margin': '30px 0', 'width:': '100%','text-align':'left'}"
  >
    <a-tabs defaultActiveKey="1" @change="onTabChange">
      <a-tab-pane tab="Basic Information" key="1">
        <a-form>
          <a-form-item label="Username" :label-col="{ span: 5 }" :wrapper-col="{ span: 12 }">{{username}}</a-form-item>
          <a-form-item label="Email" :label-col="{span:5}" :wrapper-col="{span:6}">
            <a-input type="email"></a-input>
          </a-form-item>
        </a-form>
      </a-tab-pane>
      <a-tab-pane tab="Security Information" key="2">
        <a-form>
          <a-form-item label="Password" :label-col="{span:5}" :wrapper-col="{span:6}">
            <a-input type="password"></a-input>
          </a-form-item>
          <a-form-item label="New password" :label-col="{span:5}" :wrapper-col="{span:6}">
            <a-input type="password"></a-input>
          </a-form-item>
          <a-form-item label="Confirm password" :label-col="{span:5}" :wrapper-col="{span:6}">
            <a-input type="password"></a-input>
          </a-form-item>
          <a-form-item :wrapper-col="{offset: 5, span: 12}">
            <a-button type="primary">Submit</a-button>
          </a-form-item>
        </a-form>
      </a-tab-pane>
    </a-tabs>
  </div>
</template>
<script>
export default {
  data() {
    return {
      username: "admin",
      nikename: "cat"
    };
  },
  methods: {
    onTabChange () {}
  }
};
</script>
<style scoped>
</style>
