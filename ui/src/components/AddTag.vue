<template>
  <a-form refs="form" :form="form">
    <a-form-item label="标签名" :label-col="{ span: 5 }" :wrapper-col="{ span: 12 }" has-feedback>
      <a-input
        placeholder="请输入标签名"
        v-decorator="[
                'username',
                {
                  rules: [
                   {required: true, message: '请输入标签名！'}
                  ]
                }
              ]"
      ></a-input>
    </a-form-item>
    <a-form-item label="标签排序" :label-col="{ span: 5 }" :wrapper-col="{ span: 12 }" has-feedback>
      <a-input-number
        :min="1"
        :max="10"
        v-decorator="[
                'tagOrder'
              ]"
      />
    </a-form-item>
    <a-form-item :wrapper-col="{ span: 12, offset:5 }">
      <a-button icon="save" @click="handleSubmit" :loading="submitLoading">提交</a-button>
    </a-form-item>
  </a-form>
</template>
<script>
export default {
  props: {
    parentId: Number
  },
  data() {
    return {
      form: this.$form.createForm(this),
      submitLoading: false,
      tag: {
        id: 0,
        title: "",
        category: "",
        tagOrder: 0,
        parentId: 0,
        childCount: 0
      }
    };
  },
  watch: {
    parentId(nv, ov) {
      console.log("store tag under parentId:", nv, ov);
    }
  },
  methods: {
    handleSubmit(e) {
      e.preventDefault();
      const self = this;
      const url = this.apiUrl + "/tag";
      this.form.validateFields((err, values) => {
        if (!err) {
          self.tag.title = values.title;
          self.tag.tagOrder = values.tagOrder;
          self.tag.parentId = self.parentId;
          G.post(url, self.tag)
            .cb(data => {
              console.log('store tag data ===>', data)
              if (data.status === 0) {
                this.$message.success("已保存");
              } else {
                this.$message.warning("未保存，请检查输入");
              }
            })
            .fcb(() => {
              self.submitLoading = false;
            })
            .ex(reason => {
              this.$message.error("出错了，稍后再试");
              console.error(reason);
            })
            .req();
        }
      });
    }
  }
};
</script>
<style scoped>
</style>
