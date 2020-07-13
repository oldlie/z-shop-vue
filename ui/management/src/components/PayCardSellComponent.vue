<template>
  <div>
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
      <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="分配到系统内账号">
        <a-switch checked-children="开" un-checked-children="关" @change="onChange" />
        <template v-if="assign === 1">
          <span style="font-size:.8em">仅显示10个账号，请使用检索缩小范围</span>
          <div class="inner-row">将此卡分配给：{{username}}</div>
          <a-input-search
            v-model="username"
            placeholder="按账号搜索"
            style="width: 200px"
            @search="loadUsers"
          />
          <a-spin :spinning="userLoading">
            <a-list size="small" bordered :data-source="users" style="border:0;">
              <a-list-item slot="renderItem" slot-scope="item, index">
                {{index + 1}},
                <a-button @click="onAssignUser(item)" style="width: 80%;">{{item.username}}</a-button>
              </a-list-item>
            </a-list>
          </a-spin>
        </template>
      </a-form-item>
      <a-form-item :wrapper-col="wrapperCol">
        <a-button type="primary" html-type="submit" :loading="submitLoading">保存</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script>
export default {
  props: {
    ids: { type: Array, default: () => [] }
  },
  data() {
    return {
      submitLoading: false,
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
      ],
      userLoading: false,
      assign: 0,
      customerId: 0,
      username: "",
      users: []
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
      this.form.validateFields((err, values) => {
        if (!!err) {
          return;
        }
        const url = `/backend/pay-card/sold`;
        const fd = new FormData();
        fd.append("id", this.ids.join(","));
        fd.append("customer", values["customer"]);
        fd.append("customerPhone", values["phone"]);
        fd.append("amount", "CNY " + values["price"]);
        fd.append("customerId", this.customerId);
        fd.append("assign", this.assign);
        this.submitLoading = true;
        this.$h
          .post(url, fd)
          .cb(data => {
            if (data.status === 0) {
              this.$emit("soldEvent", {
                customer: values["customer"],
                customerPhone: values["phone"],
                price: values["price"]
              });
            } else {
              this.$message.me(data.message);
            }
          })
          .fcb(() => (this.submitLoading = false))
          .req();
      });
    },
    moneyValidate(rule, value, callback) {
      let priceRegex = /^[1-9]?[\d]*\.[\d]{2}$/;
      if (!priceRegex.test(value)) {
        callback("请检测金额格式：1.00");
      }
      callback();
    },
    onChange(chekced) {
      this.assign = chekced ? 1 : -1;
      if (chekced) {
        this.loadUsers();
      }
    },
    loadUsers() {
      let url = `/backend/accounts?page=1&size=10`;
      if (this.username && this.username !== "") {
        url += `&key=username&value=${decodeURIComponent(this.username)}`;
      }
      this.userLoading = true;
      this.$h
        .get(url)
        .cb(data => {
          if (data.status === 0) {
            this.users = data.list;
          } else {
            this.$message.me(data.message);
          }
        })
        .fcb(() => (this.userLoading = false))
        .req();
    },
    onAssignUser(user) {
      this.username = user.username;
      this.customerId = user.id;
    }
  }
};
</script>

<style>
</style>