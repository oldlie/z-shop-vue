<template>
  <div>
    <a-row :gutter="4">
      <a-col :span="4">
        <sidebar :index="['7']" />
      </a-col>
      <a-col :span="20">
        <a-row class="inner-row">
          <a-col :span="24">
            <h2>账户管理</h2>
          </a-col>
        </a-row>
        <a-row class="inner-row">
          <a-col :span="24">
            <router-link :to="'/accounts'">
              <a-icon type="table" /> 账号列表
            </router-link>
            <a-divider type="vertical" />
          </a-col>
        </a-row>
        <a-row class="inner-row">
          <a-col :span="16">
            <a-spin :spinning="loading">
              <a-form-model
                ref="accountForm"
                :model="form"
                :rules="rules"
                :label-col="labelCol"
                :wrapper-col="wrapperCol"
              >
                <a-form-model-item ref="username" label="账号" prop="username">
                  <a-input v-model="form.username" />
                </a-form-model-item>
                <a-form-model-item ref="nickname" label="昵称" prop="nickname">
                  <a-input v-model="form.nickname" />
                </a-form-model-item>
                <a-form-model-item ref="password" label="密码" prop="password">
                  <a-input v-model="form.password" />
                </a-form-model-item>
                <a-form-model-item ref="payPassword" label="支付密码" prop="payPassword">
                  <a-input v-model="form.payPassword" />
                </a-form-model-item>
                <a-form-model-item label="角色" prop="role">
                  <a-select v-model="form.role" placeholder="请选择角色">
                    <a-select-option value="ADMIN">管理员</a-select-option>
                    <a-select-option value="USER">注册用户</a-select-option>
                  </a-select>
                </a-form-model-item>
                <a-form-model-item :wrapper-col="{ span: 14, offset: 4 }">
                  <a-button type="primary" @click="onSubmit" icon="save" :loading="submitLoading">保存</a-button>
                </a-form-model-item>
              </a-form-model>
            </a-spin>
          </a-col>
        </a-row>
      </a-col>
    </a-row>
  </div>
</template>
<script>
export default {
  data() {
    function accountValidator(rule, value, callback) {
      let regex = /^[a-zA-Z\d]{3,128}$/;
      if (!regex.test(value)) {
        callback("账号仅允许字母数字和@符号且在3到128字符之间");
      } else {
        callback();
      }
    }

    function passwordValidator(rule, value, callback) {
      const reg = /^(?![a-zA-z]+$)(?!\d+$)(?![!@#$%^&*]+$)[a-zA-Z\d!@#$%^&*]+$/;
      if (!reg.test(value)) {
        callback("密码需要包含且仅包含字母，数字以及!@#$%^&*");
      } else {
        callback();
      }
    }

    function payPasswordValidator(rule, value, callback) {
      const reg = /^\d{6}$/;
      if (!reg.test(value)) {
        callback("支付密码为6位数字");
      } else {
        callback();
      }
    }

    return {
      loading: false,
      labelCol: { span: 4 },
      wrapperCol: { span: 14 },
      form: {
        id: "",
        username: "",
        nickname: "",
        password: "",
        payPassword: "",
        role: ""
      },
      rules: {
        username: [
          { required: true, message: "请填写账号名称", trigger: "blur" },
          {
            min: 3,
            max: 128,
            message: "账号名称字符长度在3到128位之间",
            trigger: "blur"
          },
          { validator: accountValidator }
        ],
        password: [
          { required: true, message: "请填写密码", trigger: "blur" },
          {
            min: 8,
            max: 128,
            message: "账号名称字符长度在8到128位之间",
            trigger: "blur"
          },
          { validator: passwordValidator }
        ],
        payPassword: [{ validator: payPasswordValidator }],
        role: [{ required: true, message: "请选择权限", trigger: "blur" }]
      },
      submitLoading: false
    };
  },
  mounted() {
    this.form.id = this.$route.params.id;
    if (this.form.id > 0) {
      this.load();
    }
  },
  methods: {
    load() {
      const url = `/backend/account?id=${this.form.id}`;
      console.log('load url --->', url);
      this.loading = true;
      this.$h
      .get(url)
      .cb(data => {
        if (data['status'] === 0) {
          let item = data['item'];
          this.form.username = item.username;
          this.form.nickname = item.nickname;
          this.form.payPassword = item.payPassword;
          let role = item.roles[0];
          this.form.role = role['name'];
        } else {
          this.$message.error(decodeURIComponent(data['message']))
        }
      })
      .fcb(() => this.loading = false)
      .req();
    },
    onSubmit() {
      this.$refs.accountForm.validate(valid => {
        if (valid) {
          let fd = new FormData();
          fd.append("account", this.form.username);
          fd.append("password", this.form.password);
          fd.append("nickname", this.form.nickname);
          fd.append("payPassword", this.form.payPassword);
          fd.append("role", this.form.role);
          this.submitLoading = true;
          this.$h
            .post("/backend/account", fd)
            .cb(data => {
              if (data["status"] === 0) {
                this.$message.success("已保存");
              } else {
                this.$message.error(decodeURIComponent(data["message"]));
              }
            })
            .fcb(() => (this.submitLoading = false))
            .req();
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    resetForm() {
      this.$refs.accountForm.resetFields();
    }
  }
};
</script>