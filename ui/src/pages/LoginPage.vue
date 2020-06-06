<template>
  <div>
    <a-row
      :style="{ background: '#fff', padding: '24px', minHeight: '280px', 'margin': '30px 0', 'width:': '100%','text-align':'left'}"
    >
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
      <a-col :span="12" style="padding: 50px 0 0 0;">
        <a-form refs="form" :form="form" @submit="handleSubmit">
          <a-form-item>
            <a-alert v-if="loginMessage !== ''" :message="loginMessage" type="warning" showIcon />
          </a-form-item>
          <a-form-item label="账号" :label-col="{ span: 5 }" has-feedback :wrapper-col="{ span: 12 }">
            <a-input
              placeholder="请输入账号"
              v-decorator="[
                'username',
                {
                  rules: [
                   {required: true, message: '请输入账号！'}
                  ]
                }
              ]"
            >
              <a-icon slot="prefix" type="user" />
            </a-input>
          </a-form-item>

          <a-form-item label="密码" :label-col="{ span: 5 }" has-feedback :wrapper-col="{ span: 12 }">
            <!--{validator: validatePassword}-->
            <a-input
              placeholder="请输入密码"
              type="password"
              v-decorator="[
              'password',
              {
                rules: [
                  {required: true, message: '请输入密码！'},
                ]
              }
            ]"
            >
              <a-icon slot="prefix" type="lock" />
            </a-input>
          </a-form-item>
          <a-form-item :wrapper-col="{ span: 12, offset: 5 }">
            <a-button type="primary" @click="handleSubmit" :loading="submitting">提交</a-button>
          </a-form-item>
        </a-form>
      </a-col>
    </a-row>
  </div>
</template>
<script>
import { setTimeout, clearTimeout } from "timers";
export default {
  data: function() {
    return {
      form: this.$form.createForm(this),
      loginMessage: "",
      submitting: false
    };
  },
  beforeCreated: function() {},
  mounted() {},
  methods: {
    test() {},
    handleSubmit(e) {
      e.preventDefault();

      const self = this;
      this.form.validateFields((err, values) => {
        if (!err) {
          const url = this.apiUrl + "/login";
          self.submitting = true;
          G.post(url, values)
            .callback(function(data) {
              if (data["status"] === 0) {
                let token = data["item"];
                self.$cookie.set("token", token, 7);
                let t = setTimeout(() => {
                  self.bus.$emit("updateUserInfoEvent");
                  clearTimeout(t);
                  self.$router.push("/home");
                }, 100);
              } else if (data["status"] === 1) {
                self.loginMessage = "账号或者密码错误";
              } else {
              }
            })
            .finalCallback(() => {
              self.submitting = false;
            })
            .request();
        }
      });
    },
    validatePassword(r, v, cb) {
      const form = this.form;
      const reg = /^(?![a-zA-z]+$)(?!\d+$)(?![!@#$%^&*]+$)[a-zA-Z\d!@#$%^&*]+$/;
      if (!reg.test(v)) {
        cb("密码由字母数字以及“!@#$%^&*“组成");
      } else {
        cb();
      }
    }
  }
};
</script>
<style scoped>
</style>
