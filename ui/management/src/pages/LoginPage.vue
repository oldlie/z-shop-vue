<template>
  <div>
    <a-row class="inner-row">
      <a-col :span="12">
        <div id="J_sliderView" class style="width: auto; position: relative;margin:0 0 30px 0;">
          <img
            data-src="//i8.mifile.cn/a1/pms_1550642240.48638886.jpg"
            class="slider done"
            style="float: none; list-style: outside none none; width: 540px; z-index: 50; display: block;"
            src="//i8.mifile.cn/a1/pms_1550642240.48638886.jpg"
          />
        </div>
      </a-col>
      <a-col :span="12">
        <a-spin :spinning="loading">
          <a-form>
            <a-form-item
              :label-col="labelCol"
              :wrapper-col="wrapperCol"
              label="账号"
              :validate-status="account.status"
              :help="account.help"
              has-feedback
            >
              <a-input placeholder="请输入账号" v-model="account.value"></a-input>
            </a-form-item>
            <a-form-item
              :label-col="labelCol"
              :wrapper-col="wrapperCol"
              label="密码"
              :validate-status="password.status"
              :help="password.help"
              has-feedback
            >
              <a-input type="password" placeholder="请输入账号" v-model="password.value"></a-input>
            </a-form-item>
            <a-form-item>
              <a-button type="primary" @click="login">Login</a-button>
            </a-form-item>
          </a-form>
        </a-spin>
      </a-col>
    </a-row>
  </div>
</template>
<script>
export default {
  data() {
    return {
      loading: false,
      account: { value: "", status: "", help: "" },
      password: { value: "", status: "", help: "" },
      labelCol: {
        xs: { span: 24 },
        sm: { span: 3 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 12 }
      }
    };
  },
  methods: {
    login () {
      if (!this.account.value || this.account.value === '') {
        this.account.status = this.$input_status.error;
        this.account.help = '请填写账号'
        return;
      }
      this.account.status = this.$input_status.validating;
      this.account.help = '';

      if (!this.password.value || this.password.value === '') {
        this.password.status = this.$input_status.error;
        this.password.help = '请填写密码'
        return;
      }
      const reg = /^(?![a-zA-z]+$)(?!\d+$)(?![!@#$%^&*]+$)[a-zA-Z\d!@#$%^&*]+$/;
      if (!reg.test(this.password.value)) {
        this.password.status = this.$input_status.error;
        this.password.help = '密码格式不正确'
        return;
      }
      this.password.status = this.$input_status.validating;
      this.password.help = '';
      this.loading = true;

      this.$cookie.delete('token');

      this.$h.post("/login")
      .params({
        username: this.account.value,
        password: this.password.value
      })
      .cb(data => {
        if (data.status === 0) {
          const token = data.item;
          this.$cookie.set('token', token);
          this.$router.push('/dashboard');
        } else {
          this.$message.error(data.message);
        }
      })
      .fcb(() => {
        this.loading = false;
        this.account.status = '';
        this.password.status = '';
      })
      .req();
    }
  }
};
</script>