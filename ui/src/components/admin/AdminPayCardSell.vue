<template>
  <a-spin :spinning="loading">
    <p>{{tips}}</p>
    <a-form :form="form" @submit="handleSubmit">
      <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="购卡人">
        <a-input v-decorator="customerValidate"></a-input>
      </a-form-item>
      <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="联系电话">
        <a-input v-decorator="phoneValidate"></a-input>
      </a-form-item>
      <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="实际金额">
        <a-input v-decorator="priceValidate"></a-input>
      </a-form-item>
    </a-form>
  </a-spin>
</template>

<script>
export default {
  props: {
    ids: { type: Array, default: [] }
  },
  data() {
    return {
      loading: false,
      form: this.$form.createForm(this, { name: "form" }),
      tips: "",
      labelCol: {
        xs: { span: 24 },
        sm: { span: 8 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 12 }
      },
      customerValidate: [
        "customer",
        { rules: [{ required: true, message: "请填写购买人信息" }] }
      ],
      phoneValidate: [
        "phone",
        { rules: [{ required: true, message: "请填写购买人信息" }] }
      ],
      priceValidate: [
        "price",
        {
          rules: [
            { required: true, message: "请填写实际金额" },
            { validator: this.moneyValidate }
          ]
        }
      ]
    };
  },
  mounted() {
    if (this.ids.length === 1) {
      this.tips = "您将要出售一张礼品卡，请确认选择的卡信息正确";
    } else if (this.ids.length > 1) {
      this.tips = "您正在出售多张礼品卡，请确认选择的卡信息正确";
    } else {
      this.tips = "请选择一张礼品卡";
    }
  },
  methods: {
    handleSubmit(e) {
      e.preventDefault();
    },
    moneyValidate(rule, value, callback) {
      let priceRegex = /^[1-9]?[\d]*\.[\d]{2}$/;
      if (!priceRegex.test(value)) {
        callback("请检测金额格式：1.00");
      }
      callback();
    }
  }
};
</script>

<style>
</style>