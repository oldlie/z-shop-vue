<template>
  <div
    :style="{ background: '#fff', padding: '24px', minHeight: '280px', 'margin': '30px 0', 'width:': '100%','text-align':'left'}"
  >
    <a-spin :spinning="loading">
      <h1>
        订单
        <span style="font-size:.7em">{{innerSn}}</span>
      </h1>

      <div v-if="addrViewModel === 0" style="margin: 40px 0 30px 0">
        {{address.info}}
        <a @click="onAddrViewModelChange(1)">[选择地址]</a>
      </div>
      <div v-if="addrViewModel === 1" style="margin: 40px 0 30px 0">
        <a @click="onAddrViewModelChange(0)">[选择地址]</a>
        <address-com @defaultAddressEvent="handleAddressChange"></address-com>
      </div>

      <div style="margin: 20px 0 30px 0">
        <a-list :dataSource="items">
          <a-list-item slot="renderItem" slot-scope="item">
            <a-row style="width:100%;margin: 20px 0;">
              <!--a-col :span="3">
                <a-avatar :src="item.thumb" shape="square" :size="64" icon="user" />
              </a-col-->
              <a-col :span="6">
                <router-link
                  :to="{ path: '/product/' + item['commodityId']}"
                >{{item.commodityTitle}}</router-link>
              </a-col>
              <a-col :span="3">
                <a-avatar :size="64" icon="gift" shape="square" :src="item.commodityImage" />
              </a-col>
              <a-col :span="6">
                <p>{{item.formulaTitle}} {{item.formulaCount}} * {{item['price']['amount']}}</p>
              </a-col>
              <a-col :span="3">
                <p>{{item.formulaCount}}</p>
              </a-col>
              <a-col :span="3">
                <p>
                  <span style="font-size:.8rem;color:#666">需要积分</span>
                  <span
                    style="color:#ff6700;font-size:1.1rem"
                  >{{item.formulaCount * item['price']['amount']}}</span>
                </p>
              </a-col>
            </a-row>
          </a-list-item>
        </a-list>
      </div>

      <div style="margin: 20px 0 30px 0;text-align:right;" v-if="allowSubmit">
        <a-row :style="{'width': '100%'}">
          <a-col :span="4">
            <button class="cancel-button" @click="cancel">
              <span>取消订单</span>
            </button>
          </a-col>
          <a-col :span="10" :offset="6" :style="{'text-align': 'right', 'padding': '0 20px 0 0'}">
            <span style="font-size:.8rem;color:rgb(136, 136, 136)">需要积分/拥有积分：</span>
            <span style="color:#ff6700;font-size:1.6rem">{{totalPrice}}</span>
            <span style="font-size:.8rem;color:rgb(136, 136, 136)">/{{balance}}</span>
          </a-col>
          <a-col :span="4">
            <button class="fav-button active" @click="visible = true" :loading="submitLoading">
              <span>结算订单</span>
            </button>
          </a-col>
        </a-row>
      </div>
      <a-modal
        v-model="visible"
        title="结算订单"
        @ok="onSubmitOrder"
        okText="提交"
        cancelText="取消"
        @cancel="visible = false"
      >
        <p>请输入支付密码：</p>
        <p>
          <a-row class="inner-row" :gutter="6">
            <a-col :span="2">
              <a-input v-model="pwd1" maxLength="1" placeholder="0" />
            </a-col>
            <a-col :span="2">
              <a-input v-model="pwd2" maxLength="1" placeholder="0" />
            </a-col>
            <a-col :span="2">
              <a-input v-model="pwd3" maxLength="1" placeholder="0" />
            </a-col>
            <a-col :span="2">
              <a-input v-model="pwd4" maxLength="1" placeholder="0" />
            </a-col>
            <a-col :span="2">
              <a-input v-model="pwd5" maxLength="1" placeholder="0" />
            </a-col>
            <a-col :span="2">
              <a-input v-model="pwd6" maxLength="1" placeholder="0" />
            </a-col>
          </a-row>
        </p>
      </a-modal>
    </a-spin>
  </div>
</template>
<script>
const data = [];
const addrData = [];
export default {
  name: "BuyPage",
  props: ["sn"],
  data() {
    return {
      loading: false,
      innerSn: "",
      addrViewModel: 0,
      addrData,
      data,
      address: {},
      totalPrice: "",
      balance: "",
      items: [],
      submitLoading: false,
      allowSubmit: true,
      visible: false,
      pwd1: "",
      pwd2: "",
      pwd3: "",
      pwd4: "",
      pwd5: "",
      pwd6: ""
    };
  },
  mounted() {
    if (!this.sn) {
      const url = window.location.href;
      const lastSlah = url.lastIndexOf("/");
      this.innerSn = url.substring(lastSlah + 1, url.length);
    } else {
      this.innerSn = JSON.parse(JSON.stringify(this.sn));
    }
    this.initBuyInfo();
  },
  methods: {
    onAddrViewModelChange(model) {
      this.addrViewModel = model;
    },
    onSubmitOrder() {
      if (!this.address) {
        this.$message.warning("请选择地址");
        return;
      }
      console.log("submit order --->", this.totalPrice, this.balance);
      if (Number(this.totalPrice) > Number(this.balance)) {
        this.$message.warning("积分不够支付当前商品，请先兑换积分。");
        return;
      }
      let pwd = this.pwd1 + this.pwd2 + this.pwd3 + this.pwd4 + this.pwd5 + this.pwd6;
      const url = `${this.apiUrl}/frontend/shopping-order/pay`;
      const fd = new FormData();
      fd.append("sn", this.innerSn);
      fd.append("pwd", pwd);
      this.submitLoading = true;
      this.$g
        .post(url, fd)
        .cb(data => {
          if (data.status === 0) {
            this.$router.push("/order");
          } else {
            this.$message.me(decodeURIComponent(data.message));
          }
        })
        .fcb(() => (this.submitLoading = false))
        .req();
    },
    initBuyInfo() {
      const url = `${this.apiUrl}/frontend/buy-info/${this.innerSn}`;
      this.loading = true;
      this.$g
        .get(url)
        .cb(data => {
          console.log("buy info --->", data);
          if (data.status === 0) {
            const info = data.item;
            if (this.innerSn !== info["sn"]) {
              this.$message.error("订单号不正确");
              this.allowSubmit = false;
              return;
            }
            console.log(info["status"]);
            if (Number(info["status"]) !== 0) {
              this.$message.success("订单已经处理过了，请不要重复处理");
              this.allowSubmit = false;
            }
            this.address = info["address"];
            if (!this.address) {
              this.addrViewModel = 1;
            }
            this.totalPrice = info["totalPrice"];
            this.balance = info["balance"];
            this.items = info["items"];
          } else {
            this.$message.me(data.message);
            console.error("init buy info --->", data);
          }
        })
        .fcb(() => (this.loading = false))
        .req();
    },
    handleAddressChange(addr) {
      this.address = addr;
      this.addrViewModel = 0;
    },
    cancel() {
      const url = `${this.apiUrl}/frontend/shopping-order/cancel`;
      const fd = new FormData();
      fd.append("sn", this.innerSn);
      fd.append("reason", "用户取消");
      this.loading = true;
      this.$g
        .post(url, fd)
        .cb(data => {
          if (data.status === 0) {
            this.$message.success("订单已经取消");
            this.allowSubmit = false;
          } else {
            this.$message.me(data.message);
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


