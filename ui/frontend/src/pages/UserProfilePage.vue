<template>
  <div
    :style="{ background: '#fff', padding: '24px', minHeight: '280px', 'margin': '30px 0', 'width:': '100%','text-align':'left'}"
  >
    <a-spin :spinning="loading">
      <a-tabs defaultActiveKey="1" @change="onTabChange">
        <a-tab-pane tab="基本信息" key="1">
          <a-form>
            <a-form-item
              label="账号"
              :label-col="{ span: 5 }"
              :wrapper-col="{ span: 12 }"
            >{{username}}</a-form-item>
            <a-form-item label="昵称" :label-col="{span:5}" :wrapper-col="{span:6}">
              <a-input
                :validate-status="nickname.status"
                :help="nickname.help"
                has-feedback
                v-model="nickname.value"
              ></a-input>
            </a-form-item>
            <a-form-item :wrapper-col="{offset: 5, span: 12}">
              <a-button type="primary" @click="saveUser">保存</a-button>
            </a-form-item>
          </a-form>
        </a-tab-pane>

        <a-tab-pane tab="安全信息" key="2">
          <a-form>
            <p>修改账号密码：</p>
            <a-form-item
              label="密码"
              :label-col="{span:5}"
              :wrapper-col="{span:6}"
              :validate-status="password.status"
              :help="password.help"
              has-feedback
            >
              <a-input type="password" v-model="password.value"></a-input>
            </a-form-item>
            <a-form-item
              label="新密码"
              :label-col="{span:5}"
              :wrapper-col="{span:6}"
              :validate-status="newPassword.status"
              :help="newPassword.help"
              has-feedback
            >
              <a-input type="password" v-model="newPassword.value"></a-input>
            </a-form-item>
            <a-form-item
              label="确认密码"
              :label-col="{span:5}"
              :wrapper-col="{span:6}"
              :validate-status="rePassword.status"
              :help="rePassword.help"
              has-feedback
            >
              <a-input type="password" v-model="rePassword.value"></a-input>
            </a-form-item>
            <a-form-item :wrapper-col="{offset: 5, span: 12}">
              <a-button type="primary" @click="resetPwd" :loading="resetPwdLoading">修改密码</a-button>
            </a-form-item>
          </a-form>
          <a-form>
            <p>修改支付密码：</p>
            <a-form-item
              label="密码"
              :label-col="{span:5}"
              :wrapper-col="{span:6}"
              :validate-status="password1.status"
              :help="password1.help"
              has-feedback
            >
              <a-input type="password" v-model="password1.value"></a-input>
            </a-form-item>
            <a-form-item
              label="新支付密码"
              :label-col="{span:5}"
              :wrapper-col="{span:6}"
              :validate-status="payPwd.status"
              :help="payPwd.help"
              has-feedback
            >
              <a-row :gutter="4">
                <a-col :span="4">
                  <a-input v-model="pwd1" maxlength="1" placeholder="0" />
                </a-col>
                <a-col :span="4">
                  <a-input v-model="pwd2" maxlength="1" placeholder="0" />
                </a-col>
                <a-col :span="4">
                  <a-input v-model="pwd3" maxlength="1" placeholder="0" />
                </a-col>
                <a-col :span="4">
                  <a-input v-model="pwd4" maxlength="1" placeholder="0" />
                </a-col>
                <a-col :span="4">
                  <a-input v-model="pwd5" maxlength="1" placeholder="0" />
                </a-col>
                <a-col :span="4">
                  <a-input v-model="pwd6" maxlength="1" placeholder="0" />
                </a-col>
              </a-row>
            </a-form-item>
            <a-form-item :wrapper-col="{offset: 5, span: 12}">
              <a-button type="primary" :loading="resetPayPwdLoading" @click="resetPayPwd">保存支付密码</a-button>
            </a-form-item>
          </a-form>
        </a-tab-pane>

        <a-tab-pane tab="钱包" key="3">
          <a-row class="inner-row">
            <a-col :span="16">
              <p>当前余额：{{balance}}</p>
            </a-col>
          </a-row>
          <a-row class="inner-row">
            <a-col :span="24">
              <p>兑换卡片：</p>
              <a-form>
                <a-form-item label="请输入序列号" :label-col="{span:5}" :wrapper-col="{span:6}">
                  <a-input v-model="sn"></a-input>
                </a-form-item>
                <a-form-item label="请输入兑换码" :label-col="{span:5}" :wrapper-col="{span:6}">
                  <a-input v-model="cardPassword"></a-input>
                </a-form-item>
                <a-form-item :wrapper-col="{offset: 5, span: 12}">
                  <a-button @click="exchange" type="primary">充值</a-button>
                </a-form-item>
              </a-form>
            </a-col>
          </a-row>
          <a-row class="inner-row">
            <pay-card @exchanged="handleExchange"></pay-card>
          </a-row>
        </a-tab-pane>

        <a-tab-pane tab="订单信息" key="4"></a-tab-pane>
      </a-tabs>
    </a-spin>
  </div>
</template>
<script>
export default {
  data() {
    return {
      username: "",
      nickname: { value: "", status: "", help: "" },
      loading: false,
      // region security
      pwd1: "",
      pwd2: "",
      pwd3: "",
      pwd4: "",
      pwd5: "",
      pwd6: "",
      resetPwdLoading: false,
      password: { value: "", status: "", help: "" },
      newPassword: { value: "", status: "", help: "" },
      rePassword: { value: "", status: "", help: "" },
      password1: { value: "", status: "", help: "" },
      payPwd: { value: "", status: "", help: "" },
      resetPayPwdLoading: false,
      // endregion
      // region
      balance: "0.00",
      sn: "",
      cardPassword: ""
      // endregion
    };
  },
  mounted() {
    this.account();
  },
  methods: {
    account() {
      const url = `${this.apiUrl}/frontend/account`;
      this.loading = true;
      this.$g
        .get(url)
        .cb(data => {
          if (data.status === 0) {
            let item = data.item;
            this.username = item["username"];
            this.nickname.value = item["nickname"];
          } else {
            this.$message.me(decodeURIComponent(data["message"]));
          }
        })
        .fcb(() => (this.loading = false))
        .req();
    },
    saveUser() {
      if (this.nickname.value === null) {
        return;
      }
      if (this.nickname.value) {
        this.nickname.value = this.nickname.value.trim();
      }
      if (this.nickname.value.length >= 128) {
        this.nickname.status = "warning";
        this.nickname.help = "昵称不超过128字符";
        return;
      }
      const url = `${this.apiUrl}/frontend/nickname`;
      this.loading = true;
      const fd = new FormData();
      fd.append("nickname", this.nickname.value);
      this.$g
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
    },
    resetPwd() {
      const reg = /^(?![a-zA-z]+$)(?!\d+$)(?![!@#$%^&*]+$)[a-zA-Z\d!@#$%^&*]+$/;
      if (!reg.test(this.password.value)) {
        this.password.status = "warning";
        this.password.help = `密码需包含且仅包含数字，字母以及字符"!@#$%^&*"`;
        return;
      }
      if (this.password.value.length <= 7 || this.password.value.length >= 32) {
        this.password.status = "warning";
        this.password.help = "密码长度在8到32位之间";
        return;
      }
      if (!reg.test(this.newPassword.value)) {
        this.newPassword.status = "warning";
        this.newPassword.help = `密码需包含且仅包含数字，字母以及字符"!@#$%^&*"`;
        return;
      }
      if (
        this.newPassword.value.length <= 7 ||
        this.newPassword.value.length >= 32
      ) {
        this.newPassword.status = "warning";
        this.newPassword.help = "密码长度在8到32位之间";
        return;
      }
      if (this.newPassword.value !== this.rePassword.value) {
        this.rePassword.status = "warning";
        this.rePassword.help = "两次密码不相同";
        return;
      }
      this.password.status = this.newPassword.status = this.rePassword.status =
        "validating";
      this.password.help = this.newPassword.help = this.rePassword.help = "";
      this.resetPwdLoading = true;
      const url = `${this.apiUrl}frontend/password`;
      const fd = new FormData();
      fd.append("oldPwd", this.password.value);
      fd.append("newPwd", this.newPassword.value);
      this.$g
        .post(url, fd)
        .cb(data => {
          if (data.status === 0) {
            this.$message.success("已修改");
            this.password.status = this.newPassword.status = this.rePassword.status =
              "success";
          } else {
            this.password.status = this.newPassword.status = this.rePassword.status =
              "";
            this.$message.me(data["message"]);
          }
        })
        .fcb(() => (this.resetPwdLoading = false))
        .req();
    },
    resetPayPwd() {
      let reg = /^(?![a-zA-z]+$)(?!\d+$)(?![!@#$%^&*]+$)[a-zA-Z\d!@#$%^&*]+$/;
      if (!reg.test(this.password1.value)) {
        this.password1.status = "warning";
        this.password1.help = `密码需包含且仅包含数字，字母以及字符"!@#$%^&*"`;
        return;
      }
      this.payPwd.value =
        this.pwd1 + this.pwd2 + this.pwd3 + this.pwd4 + this.pwd5 + this.pwd6;
      reg = /^\d{4,6}$/;
      if (!reg.test(this.payPwd.value)) {
        this.payPwd.status = "warning";
        this.payPwd.help = "支付密码为数字且至少四位";
        return;
      }
      this.payPwd.status = this.password1.status = "validating";
      this.payPwd.help = this.password1.help = "";
      this.resetPayPwdLoading = true;
      const url = `${this.apiUrl}frontend/pay-password`;
      const fd = new FormData();
      fd.append("password", this.password1.value);
      fd.append("payPwd", this.payPwd.value);
      this.$g
        .post(url, fd)
        .cb(data => {
          if (data.status === 0) {
            this.$message.success("已保存");
            this.payPwd.status = this.password1.status = "success";
          } else {
            this.$message.me(data.message);
          }
        })
        .fcb(() => (this.resetPayPwdLoading = false))
        .req();
    },
    onTabChange(index) {
      if (index === "4") {
        this.$router.push("/order");
      } else if (index === "3") {
        this.initWallet();
      }
    },
    initWallet() {
      const url = `${this.apiUrl}/frontend/wallet/balance`;
      this.loading = true;
      this.$g
        .get(url)
        .cb(data => {
          if (data.status === 0) {
            this.balance = data.item["balance"]["amount"];
          } else {
            console.error("init wallet error --->", data);
          }
        })
        .fcb(() => (this.loading = false))
        .req();
    },
    exchange() {
      if (this.sn === "" || this.cardPassword === "") {
        this.$message.warning("请输入序列号和密码");
        return;
      }
      const url = `${this.apiUrl}/frontend/card/exchange?sn=${this.sn}&vc=${this.cardPassword}`;
      this.loading = true;
      this.$g
        .post(url, {
          sn: this.sn,
          vc: this.cardPassword
        })
        .cb(data => {
          if (data.status === 0) {
            this.balance = data.item["amount"];
          } else {
            console.error("exchange card --->", data);
            this.$message.me(data.message);
          }
        })
        .fcb(() => (this.loading = false))
        .req();
    },
    handleExchange(amount) {
      console.log(amount);
      this.balance = amount;
    }
  }
};
</script>
<style scoped>
</style>
