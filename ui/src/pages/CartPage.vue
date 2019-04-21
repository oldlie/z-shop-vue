<template>
  <div>
    <div
      :style="{ background: '#fff', padding: '24px', minHeight: '280px', 'margin': '30px 0', 'width:': '100%','text-align':'left'}"
    >
      <h1>购物车</h1>
      <a-list :dataSource="data">
        <a-list-item slot="renderItem" slot-scope="item">
          <a-row style="width:100%;margin: 20px 0;">
            <a-col :span="3">
              <a-avatar shape="square" :size="64" icon="user"/>
            </a-col>
            <a-col :span="6">
              <router-link :to="{ path: '/product/' + item['id']}">{{item.title}}</router-link>
            </a-col>
            <a-col :span="6">
              <p>{{item.specification}}</p>
            </a-col>
            <a-col :span="3">
              <p>
                <span style="font-size:.8rem;color:#666">需要积分</span>
                <span style="color:#ff6700;font-size:1.1rem">{{item.total}}</span>
              </p>
            </a-col>
            <a-col :span="6">
              <p>
                <a-input-number :min="1" :max="10" v-model="item.count"/>
                <a-divider type="vertical"/>
                <a-button type="danger">
                  <a-icon type="delete"/>
                </a-button>
              </p>
            </a-col>
          </a-row>
        </a-list-item>
      </a-list>

      <a-row :style="{'width': '100%'}">
        <a-col :span="4">
          <!--
          <button class="cancel-button">
            <span>取消订单</span>
          </button>
          -->
        </a-col>
        <a-col :span="6" :offset="8" :style="{'text-align': 'right'}">
          <span style="font-size:.8rem;color:rgb(136, 136, 136)">总计需要积分：</span>
          <span style="color:#ff6700;font-size:1.4rem">11994</span>
          &nbsp;&nbsp;
        </a-col>
        <a-col :span="6">
          <button class="fav-button active" @click="onSettlement">
            <span>结算</span>
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
    thumb: "",
    title: "小米手机9",
    specification: "暗夜黑",
    count: 2,
    points: 1999,
    total: 3998
  },
  {
    id: 0,
    thumb: "",
    title: "小米手机9",
    specification: "暗夜黑",
    count: 2,
    points: 1999,
    total: 3998
  },
  {
    id: 0,
    thumb: "",
    title: "小米手机9",
    specification: "暗夜黑",
    count: 2,
    points: 1999,
    total: 3998
  }
];

export default {
  name: "CartPage",
  data() {
    return {
      data
    };
  },
  methods: {
    onSettlement() {
      this.$router.push("buy");
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


