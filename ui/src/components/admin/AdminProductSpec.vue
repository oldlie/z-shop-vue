<template>
  <a-spin :spinning="loading">
    <a-row>
      <a-col :span="24">
        <a-form :form="specForm">
          <a-form-item>输入规格，例如：【重量:200g】</a-form-item>
          <a-form-item
            v-for="(k, index) in specForm.getFieldValue('keys')"
            :key="k"
            v-bind="formItemLayoutWithOutLabel"
            :required="false"
          >
            <a-input
              v-decorator="[
                `names[${k}]`,
                {
                    validateTrigger: ['change', 'blur'],
                    rules: [{
                    required: true,
                    whitespace: true,
                    message: 'Please input passenger\'s name or delete this field.',
                    }],
                }
                ]"
              placeholder="重量：200g"
              style="width: 60%; margin-right: 8px"
            />
            <a-icon
              v-if="specForm.getFieldValue('keys').length > 1"
              class="dynamic-delete-button"
              type="minus-circle-o"
              :disabled="specForm.getFieldValue('keys').length === 1"
              @click="() => remove(k)"
            />
          </a-form-item>
          <a-form-item v-bind="formItemLayoutWithOutLabel">
            <a-button type="dashed" style="width: 60%" @click="add">
              <a-icon type="plus" />Add field
            </a-button>
          </a-form-item>
        </a-form>
      </a-col>
    </a-row>
    <a-row>
      <a-col :span="24"></a-col>
    </a-row>
  </a-spin>
</template>
<script>
let count = 1;
export default {
  props: {
    id: { type: Number, default: 0 }
  },
  data() {
    return {
      loading: false,
      formItemLayoutWithOutLabel: {
        wrapperCol: {
          xs: { span: 24, offset: 0 },
          sm: { span: 20, offset: 4 }
        }
      },
      spec: {
        commodityId: 0,
        specification: "",
        images: "",
        detail: ""
      }
    };
  },
  beforeCreate() {
    this.specForm = this.$form.createForm(this);
    this.specForm.getFieldDecorator("keys", {
      initialValue: [],
      preserve: true
    });
  },
  methods: {
    init(id) {
      const url = `${this.apiUrl}/backend/product/profile/${id}`;
      const self = this;
      self.loading = true;
      G.get(url)
        .cb(data => {
          if (data.status === 0) {
            self.spec = data.item;
          } else {
            console.error(data);
          }
        })
        .fcb(() => {
          self.loading = false;
        })
        .req();
    },
    remove(k) {
      const { specForm } = this;
      const keys = specForm.getFieldValue("keys");
      if (keys.length === 1) {
        return;
      }
      // can use data-binding to set
      specForm.setFieldsValue({
        keys: keys.filter(key => key !== k)
      });
    },
    add() {
      const { specForm } = this;
      const keys = specForm.getFieldValue("keys");
      const nextKeys = keys.concat(++count);
      // can use data-binding to set
      // important! notify form to detect changes
      specForm.setFieldsValue({
        keys: nextKeys
      });
    },
    handleSubmit(e) {
      e.preventDefault();
      this.form.validateFields((err, values) => {
        if (!err) {
          console.log("Received values of form: ", values);
        }
      });
    }
  }
};
</script>