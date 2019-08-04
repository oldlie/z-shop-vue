<template>
  <div>
    <a-row class="inner-row">
      <a-col :span="6">
        <a-button shape="circle" icon="rollback" @click="back" />
      </a-col>
    </a-row>
    <a-row class="inner-row">
      <a-col :span="24">
        <a-form refs="form" :form="form">
          <a-form-item>
            <a-alert v-if="warnMessage !== ''" :message="warnMessage" type="warning" showIcon />
          </a-form-item>

          <a-form-item label="标签" :label-col="{ span: 5 }" has-feedback :wrapper-col="{ span: 12 }">
            <a-input
              placeholder="请输入标签"
              v-decorator="[
                'title',
                {
                  rules: [
                   {required: true, message: '请输入标签！'}
                  ]
                }
              ]"
            ></a-input>
          </a-form-item>

          <a-form-item
            label="排序序号"
            :label-col="{ span: 5 }"
            has-feedback
            :wrapper-col="{ span: 12 }"
          >
            <a-input-number
              v-decorator="[
                'tagOrder'
              ]"
              :min="1"
              :max="1000"
            />
          </a-form-item>

          <a-form-item :wrapper-col="{ span: 12, offset: 5 }">
            <a-button type="primary" @click="handleSubmit">提交</a-button>
          </a-form-item>
        </a-form>
      </a-col>
    </a-row>
  </div>
</template>
<script>
export default {
  props: {
    tag: {
      type: Object,
      default: () => {
        return {
          id: 0,
          title: "",
          category: 0,
          tagOrder: 1,
          parentId: 0,
          childCount: 0
        };
      }
    }
  },
  data() {
    return {
      form: this.$form.createForm(this),
      warnMessage: ""
    };
  },
  methods: {
    back() {
      this.$emit("goBackEvent");
    },
    handleSubmit(e) {
      e.preventDefault();
      const self = this;
      this.form.validateFields((err, values) => {
        if (!err) {
          console.log("Received values of form: ", values);
          const url = self.apiUrl + '/admin/dashboard/tag';
          
        }
      });
    }
  }
};
</script>