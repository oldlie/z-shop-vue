<template>
  <div>
    <a-row :gutter="4">
      <a-col :span="4">
        <sidebar :index="['6']" />
      </a-col>
      <a-col :span="20">
        <a-row class="inner-row">
          <a-col :span="24">
            <h2>兑换卡</h2>
          </a-col>
        </a-row>
        <a-row class="inner-row">
          <a-col :span="24">
            <router-link :to="'/cards'">
              <a-icon type="table" />卡片列表
            </router-link>
          </a-col>
        </a-row>
        <a-row class="inner-row">
          <a-form :form="form" @submit="handleSubmit">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="卡名称">
              <a-input v-decorator="titleValidate"></a-input>
            </a-form-item>
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="备注">
              <a-textarea v-decorator="noteValidate" :autoSize="{minRows: 2, maxRows: 4}"></a-textarea>
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
              <a-button type="primary" html-type="submit" :loading="loading">保存</a-button>
            </a-form-item>
          </a-form>
        </a-row>
      </a-col>
    </a-row>
  </div>
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
    };
  },
  methods: {
    handleSubmit(e) {
      e.preventDefault();
      this.form.validateFields((err, values) => {
        if (!!err) {
          return;
        }
        const url = `/backend/pay-cards`;
        let fd = new FormData();
        fd.append("title", values["title"]);
        fd.append("note", values["note"]);
        fd.append("validDay", values["validDay"]);
        fd.append("denomination", "CNY " + values["denomination"]);
        fd.append("amount", "CNY " + values["amount"]);
        fd.append("led", "2099-12-30");
        fd.append("count", values["count"]);
        this.loading = true;

        this.$h
          .post(url, fd)
          .cb(data => {
            if (data.status === 0) {
              this.$message.success("已保存");
            } else {
              this.$message.me(data.message);
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