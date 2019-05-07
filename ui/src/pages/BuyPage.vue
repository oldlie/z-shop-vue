<template>
  <div
    :style="{ background: '#fff', padding: '24px', minHeight: '280px', 'margin': '30px 0', 'width:': '100%','text-align':'left'}"
  >
    <h1>提交订单</h1>

    <div v-if="addrViewModel === 0" style="margin: 40px 0 30px 0">
      <span style="padding-right:20px;font-weight:bold;color:#333;display:inline-block">赵钱孙李先生 </span>
      <span style="padding-right:20px;display:inline-block">13412345678</span>
      <span style="padding-right:20px;display:inline-block">北京市 北京市 海淀区 北外太平庄50号</span>
      <a @click="onAddrViewModelChange(1)">[选择地址]</a>
    </div>
    <div v-if="addrViewModel === 1" style="margin: 40px 0 30px 0">
      <a @click="onAddrViewModelChange(0)">[选择地址]</a>
      <address-com></address-com>
    </div>

    <div style="margin: 20px 0 30px 0">
      <a-list :dataSource="data">
        <a-list-item slot="renderItem" slot-scope="item">
          <a-row style="width:100%;margin: 20px 0;">
            <a-col :span="3">
              <a-avatar :src="item.thumb" shape="square" :size="64" icon="user"/>
            </a-col>
            <a-col :span="6">
              <router-link :to="{ path: '/product/' + item['id']}">{{item.title}}</router-link>
            </a-col>
            <a-col :span="6">
              <p>{{item.specification}} * {{item.points}}</p>
            </a-col>
            <a-col :span="6">
              <p>{{item.count}}</p>
            </a-col>
            <a-col :span="3">
              <p>
                <span style="font-size:.8rem;color:#666">需要积分</span>
                <span style="color:#ff6700;font-size:1.1rem">{{item.total}}</span>
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
        <a-col :span="6" :offset="8" :style="{'text-align': 'right', 'padding': '0 20px 0 0'}">
          <span style="font-size:.8rem;color:rgb(136, 136, 136)">需要积分/拥有积分：</span>
          <span style="color:#ff6700;font-size:1.6rem">11994</span>
          <span style="font-size:.8rem;color:rgb(136, 136, 136)">/12020</span>
        </a-col>
        <a-col :span="6">
          <button class="fav-button active" @click="onSubmitOrder">
            <span>提交订单</span>
          </button>
        </a-col>
      </a-row>
    </div>
  </div>
</template>
<script>
const data = [
  {
    id: 0,
    thumb: "http://localhost/zshop/img/pms.jpg",
    title: "小米手机9",
    specification: "暗夜黑",
    count: 2,
    points: 1999,
    total: 3998
  },
  {
    id: 0,
    thumb: "http://localhost/zshop/img/pms.jpg",
    title: "小米手机9",
    specification: "暗夜黑",
    count: 2,
    points: 1999,
    total: 3998
  },
  {
    id: 0,
    thumb: "http://localhost/zshop/img/pms.jpg",
    title: "小米手机9",
    specification: "暗夜黑",
    count: 2,
    points: 1999,
    total: 3998
  }
];
const addrData = [
  {
    id: 0,
    name: "赵钱孙李先生",
    phone: "13412345678",
    address: "北京市 北京市 海淀区 北外太平庄50号",
    isDefault: true
  },
  {
    id: 0,
    name: "陈生",
    phone: "13412345678",
    address: "北京市 北京市 海淀区 北外太平庄50号",
    isDefault: false
  },
  {
    id: 0,
    name: "陈生",
    phone: "13412345678",
    address: "北京市 北京市 海淀区 北外太平庄50号",
    isDefault: false
  }
];
export default {
  name: "BuyPage",
  data() {
    return {
      addrViewModel: 0,
      addrData,
      data
    };
  },
  methods: {
    onAddrViewModelChange(model) {
      this.addrViewModel = model;
    },
    onSubmitOrder () {
      this.$router.push("/order");
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


