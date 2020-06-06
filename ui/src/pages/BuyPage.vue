<template>
  <div
    :style="{ background: '#fff', padding: '24px', minHeight: '280px', 'margin': '30px 0', 'width:': '100%','text-align':'left'}"
  >
    <a-spin :spinning="loading">
      <h1>
        提交订单
        <span style="font-size:.7em">{{innerSn}}</span>
      </h1>

      <div v-if="addrViewModel === 0" style="margin: 40px 0 30px 0">
        <!--
        <span style="padding-right:20px;font-weight:bold;color:#333;display:inline-block">赵钱孙李先生</span>
        <span style="padding-right:20px;display:inline-block">13412345678</span>
        <span style="padding-right:20px;display:inline-block">北京市 北京市 海淀区 北外太平庄50号</span>
        -->
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
                <router-link :to="{ path: '/product/' + item['commodityId']}">{{item.commodityTitle}}</router-link>
              </a-col>
              <a-col :span="6">
                <p>{{item.formulaTitle}} {{item.formulaCount}} * {{item['price']['amount']}}</p>
              </a-col>
              <a-col :span="6">
                <p>{{item.formulaCount}}</p>
              </a-col>
              <a-col :span="3">
                <p>
                  <span style="font-size:.8rem;color:#666">需要积分</span>
                  <span style="color:#ff6700;font-size:1.1rem">
                    {{item.formulaCount * item['price']['amount']}}
                    </span>
                </p>
              </a-col>
            </a-row>
          </a-list-item>
        </a-list>
      </div>

      <div style="margin: 20px 0 30px 0;text-align:right;">
        <a-row :style="{'width': '100%'}">
          <a-col :span="4">
            <button class="cancel-button">
              <span>取消订单</span>
            </button>
          </a-col>
          <a-col :span="10" :offset="6" :style="{'text-align': 'right', 'padding': '0 20px 0 0'}">
            <span style="font-size:.8rem;color:rgb(136, 136, 136)">需要积分/拥有积分：</span>
            <span style="color:#ff6700;font-size:1.6rem">{{totalPrice}}</span>
            <span style="font-size:.8rem;color:rgb(136, 136, 136)">/{{balance}}</span>
          </a-col>
          <a-col :span="4">
            <button class="fav-button active" @click="onSubmitOrder">
              <span>提交订单</span>
            </button>
          </a-col>
        </a-row>
      </div>
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
      items: []
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
      if (this.addrViewModel === 1) {
      }
    },
    onSubmitOrder() {
      this.$router.push("/order");
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
              return;
            }
            this.address = info["address"];
            if (!this.address) {
              this.addrViewModel = 1;
            }
            this.totalPrice = info["totalPrice"];
            this.balance = info["balance"];
            this.items = info["items"];
          } else {
            this.$message.error(data.message);
            console.error("init buy info --->", data);
          }
        })
        .fcb(() => (this.loading = false))
        .req();
    },
    handleAddressChange(addr) {
      this.address = addr;
    }
  }
};
</script>
<style scoped>
.cancel-button {
  cursor: pointer;
  width: 100%;
  height: 48px;
  border: 0;
  color: rgb(136, 136, 136);
  padding: 12px 0 10px 0;
}
.fav-button {
  cursor: pointer;
  width: 100%;
  height: 48px;
  color: #ff6700;
  border: 1px solid #ff6700;
  padding: 12px 0 10px 0;
}
.fav-button:hover {
  color: #ffffff;
  background: #ff6700;
}
.fav-button.active {
  color: #ffffff;
  background: #ff6700;
}
</style>


