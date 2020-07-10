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
            <a-form-item label="Password" :label-col="{span:5}" :wrapper-col="{span:6}">
              <a-input type="password"></a-input>
            </a-form-item>
            <a-form-item label="New password" :label-col="{span:5}" :wrapper-col="{span:6}">
              <a-input type="password"></a-input>
            </a-form-item>
            <a-form-item label="Confirm password" :label-col="{span:5}" :wrapper-col="{span:6}">
              <a-input type="password"></a-input>
            </a-form-item>
            <a-form-item :wrapper-col="{offset: 5, span: 12}">
              <a-button type="primary">Submit</a-button>
            </a-form-item>
          </a-form>
          <a-form>
            <p>修改支付密码：</p>
            <a-form-item label="Password" :label-col="{span:5}" :wrapper-col="{span:6}">
              <a-input type="password"></a-input>
            </a-form-item>
            <a-form-item label="New password" :label-col="{span:5}" :wrapper-col="{span:6}">
              <a-input type="password"></a-input>
            </a-form-item>
            <a-form-item label="Confirm password" :label-col="{span:5}" :wrapper-col="{span:6}">
              <a-input type="password"></a-input>
            </a-form-item>
            <a-form-item :wrapper-col="{offset: 5, span: 12}">
              <a-button type="primary">Submit</a-button>
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
                  <a-button @click="exchange" type="primary">兑换</a-button>
                </a-form-item>
              </a-form>
            </a-col>
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
            this.$message.error(decodeURIComponent(data["message"]));
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
        this.nickname.status = 'warning';
        this.nickname.help = '昵称不超过128字符';
        return;
      }
      const url = `${this.apiUrl}/frontend/nickname`;
      this.loading = true;
      const fd = new FormData();
      fd.append('nickname', this.nickname.value);
      this.$g.post(url, fd)
      .cb(data => {
        if (data.status === 0) {
          this.$message.success('已保存');
        } else {
          this.$message.me(data.message);
        }
      })
      .fcb(() => this.loading =false)
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
            this.$message.error(data.message);
          }
        })
        .fcb(() => (this.loading = false))
        .req();
    }
  }
};
</script>
<style scoped>
</style>
