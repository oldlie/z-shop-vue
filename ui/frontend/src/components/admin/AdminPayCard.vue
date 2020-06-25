<template>
  <a-spin :spinning="loading">
    <a-form :form="form" @submit="handleSubmit">
      <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="卡名称">
        <a-input v-decorator="titleValidate"></a-input>
      </a-form-item>
      <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="备注">
        <a-textarea v-decorator="noteValidate" :autosize="{minRows: 2, maxRows: 4}"></a-textarea>
      </a-form-item>
      <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="有效天数">
        <a-input-number v-decorator="validDayValidate" :min="1" :max="9999"></a-input-number>
      </a-form-item>
      <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="面值">
        <a-input v-decorator="denominationValidate"></a-input>
      </a-form-item>
      <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="售价">
        <a-input v-decorator="amountValidate"></a-input>
      </a-form-item>
      <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="数量">
        <a-input-number v-decorator="['count']" :min="1" :max="9999"></a-input-number>
      </a-form-item>
      <a-form-item :wrapper-col="wrapperCol">
        <a-button type="primary" html-type="submit">保存</a-button>
      </a-form-item>
    </a-form>
  </a-spin>
</template>

<script>
export default {
  data() {
    return {
      loading: false,
      form: this.$form.createForm(this, { name: "payCard" }),
      labelCol: {
        xs: { span: 24 },
        sm: { span: 3 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 12 }
      },
      // region validate
      titleValidate: [
        "title",
        {
          rules: [
            { required: true, message: "请输入标题" },
            { max: 32, message: "标题最多输入32个字符" }
          ]
        }
      ],
      noteValidate: [
        "note",
        {
          rules: [
            { required: true, message: "请输入备注" },
            { max: 250, message: "标题最多输入250个字符" }
          ]
        }
      ],
      validDayValidate: [
        "validDay",
        {
          initialValue: 90,
          rules: [{ required: true, message: "请输入有效天数" }]
        }
      ],
      denominationValidate: [
        "denomination",
        {
          rules: [
            { required: true, message: "请输入面值" },
            { validator: this.moneyValidate }
          ]
        }
      ],
      amountValidate: [
        "amount",
        {
          rules: [
            { required: true, message: "请输入售价" },
            { validator: this.moneyValidate }
          ]
        }
      ]
      // endregion
    };
  },
  methods: {
    handleSubmit(e) {
      e.preventDefault();
      this.form.validateFields((err, values) => {
        if (!!err) {
          return;
        }
        console.log("values =>", values);
        const url = `${this.apiUrl}/backend/pay-cards`;
        let fd = new FormData();
        fd.append("title", values["title"]);
        fd.append("note", values["note"]);
        fd.append("validDay", values["validDay"]);
        fd.append("denomination", "CNY " + values["denomination"]);
        fd.append("amount", "CNY " + values["amount"]);
        fd.append("led", "2099-12-30");
        fd.append('count', values['count'])
        this.loading = true;
        console.log(url);
        console.log(fd);
        console.log(G);
        G.post(url, fd)
          .cb(data => {
            if (data.status === 0) {
              this.$message.success("已保存");
            } else {
              this.$message.error(data.message);
            }
          })
          .fcb(() => (this.loading = false))
          .req();
      });
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