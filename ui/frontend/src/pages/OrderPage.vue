<template>
  <a-spin :spinning="loading">
    <div
      :style="{ background: '#fff', padding: '24px', minHeight: '280px', 'margin': '30px 0', 'width:': '100%','text-align':'left'}"
    >
      <h1>订单管理</h1>

      <a-tabs defaultActiveKey="2" @change="onTabsChange">
        <a-tab-pane tab="待支付" key="1">
          <a-row class="inner-row" v-for="so in unpayData" :key="so.id">
            <a-col :span="24">
              <a-row class="inner-row">
                <a-col :span="16">订单号: {{so.serialNumber}} {{so.addressInfo}}</a-col>
              </a-row>

              <a-row class="inner-row" v-for="soi in so.items" :key="soi.id">
                <a-col :span="7">{{soi.commodityTitle}}</a-col>
                <a-col :span="4">
                  <a-avatar :size="64" icon="gift" shape="square" :src="soi.commodityImage" />
                </a-col>
                <a-col :span="7">{{soi.formulaTitle}}</a-col>
                <a-col :span="2">{{soi.formulaCount}}</a-col>
                <a-col :span="4" style="text-align:right">单价：{{soi.price['amount']}}</a-col>
              </a-row>

              <a-row class="inner-row">
                <a-col :span="4">
                  <button class="cancel-button active" @click="onCancelOrder(so)">
                    <span>取消订单</span>
                  </button>
                </a-col>
                <a-col :span="4" :offset="12">总价：{{so.totalMoney['amount']}}</a-col>
                <a-col :span="4">
                  <button class="fav-button active" @click="onSubmitOrder(so)">
                    <span>去结算</span>
                  </button>
                </a-col>
              </a-row>

              <a-divider />
            </a-col>
          </a-row>
        </a-tab-pane>
        <a-tab-pane tab="待收货" key="2">
          <a-row class="inner-row" v-for="so in onWayData" :key="so.id">
            <a-col :span="24">
              <a-row class="inner-row">
                <a-col :span="16">
                  <a-tag v-if="so.status === 0" color="#ff6700">待支付</a-tag>
                  <a-tag v-if="so.status === 1" color="#87d068">正在出库</a-tag>
                  <a-tag v-if="so.status === 2" color="#2db7f5">已发货</a-tag>
                  <a-tag v-if="so.status === 8" color="#135200">已完成</a-tag>
                  <a-tag v-if="so.status === 10">已取消</a-tag>
                  订单号: {{so.serialNumber}} {{so.addressInfo}}
                </a-col>
              </a-row>

              <a-row class="inner-row" v-for="soi in so.items" :key="soi.id">
                <a-col :span="7">{{soi.commodityTitle}}</a-col>
                <a-col :span="4">
                  <a-avatar :size="64" icon="gift" shape="square" :src="soi.commodityImage" />
                </a-col>
                <a-col :span="7">{{soi.formulaTitle}}</a-col>
                <a-col :span="2">{{soi.formulaCount}}</a-col>
                <a-col :span="4" style="text-align:right">单价：{{soi.price['amount']}}</a-col>
              </a-row>

              <a-row class="inner-row">
                <a-col :span="4">总价：{{so.totalMoney['amount']}}</a-col>
                <a-col :span="16">
                  <template>
                    <div v-if="so.status === 2">
                      <span>{{so.updateDate}}</span>
                      <span>&nbsp;&nbsp;已发&nbsp;&nbsp;</span>
                      <span
                        style="font-size:1.1em;font-weight:bold;"
                      >{{so.postCompany}},单号：{{so.postSerialNumber}}</span>
                    </div>
                  </template>
                </a-col>
                <a-col :span="4">
                  <button class="fav-button active" @click="onCompleteOrder(so)">
                    <span>确认收货</span>
                  </button>
                </a-col>
              </a-row>

              <a-divider />
            </a-col>
          </a-row>
        </a-tab-pane>
        <a-tab-pane tab="订单记录" key="3">
          <a-row class="inner-row" v-for="so in historyData" :key="so.id">
            <a-col :span="24">
              <a-row class="inner-row">
                <a-col :span="16">
                  <a-tag v-if="so.status === 10" color="#d9d9d9">已取消</a-tag>
                  <a-tag v-if="so.status === 8" color="#73d13d">已完成</a-tag>
                  订单号: {{so.serialNumber}} {{so.addressInfo}}
                </a-col>
              </a-row>

              <a-row class="inner-row" v-for="soi in so.items" :key="soi.id">
                <a-col :span="7">{{soi.commodityTitle}}</a-col>
                <a-col :span="4">
                  <a-avatar :size="64" icon="gift" shape="square" :src="soi.commodityImage" />
                </a-col>
                <a-col :span="7">{{soi.formulaTitle}}</a-col>
                <a-col :span="2">{{soi.formulaCount}}</a-col>
                <a-col :span="4" style="text-align:right">单价：{{soi.price['amount']}}</a-col>
              </a-row>

              <a-row class="inner-row">
                <a-col :span="4">总价：{{so.totalMoney['amount']}}</a-col>
                <a-col
                  :span="16"
                  v-if="so.status === 2"
                >{{so.postCompany}},快递单号：{{so.postSerialNumber}}</a-col>
              </a-row>

              <a-divider />
            </a-col>
          </a-row>
        </a-tab-pane>
      </a-tabs>
    </div>
  </a-spin>
</template>
<script>
export default {
  name: "OrderPage",
  data() {
    return {
      loading: false,
      unpayData: [],
      onWayData: [],
      historyData: [],
      total: 0,
      page: 1,
      size: 10
    };
  },
  mounted() {
    this.loadOnWayList();
  },
  methods: {
    onTabsChange(index) {
      if (index === "1") {
        this.loadUnpayList();
      } else if (index === "2") {
        this.loadOnWayList();
      } else if (index === "3") {
        this.loadHistory();
      }
    },
    onAfterSale() {
      this.$router.push("/after-sale");
    },
    onComment() {
      this.$router.push("/order/comment");
    },
    loadUnpayList() {
      const url = `${this.apiUrl}/frontend/shopping-order/waiting-for-pay`;
      this.loading = true;
      this.$g
        .get(url)
        .cb(data => {
          console.log("load unpay list--->", data);
          if (data.status === 0) {
            this.unpayData = data.list;
            this.total = 0;
          } else {
            console.error("load unpay list error--->", data);
          }
        })
        .fcb(() => (this.loading = false))
        .req();
    },
    loadOnWayList() {
      const url = `${this.apiUrl}/frontend/shopping-order/before-confirmed/${this.page}/${this.size}`;
      this.loading = true;
      this.$g
        .get(url)
        .cb(data => {
          if (data.status === 0) {
            this.onWayData = data.list;
            this.total = data.total;
          } else {
            console.error("load on way list --->", data);
          }
        })
        .fcb(() => (this.loading = false))
        .req();
    },
    loadHistory() {
      const url = `${this.apiUrl}/frontend/shopping-order/history/${this.page}/${this.size}`;
      this.loading = true;
      this.$g
        .get(url)
        .cb(data => {
          if (data.status === 0) {
            this.historyData = data.list;
            this.total = data.total;
          } else {
            console.error("load history error--->", data);
          }
        })
        .fcb(() => (this.loading = false))
        .req();
    },
    onSubmitOrder(item) {
      this.$router.push(`/buy/${item.serialNumber}`);
    },
    onCancelOrder(item) {
      const url = `${this.apiUrl}/frontend/shopping-order/cancel`;
      const fd = new FormData();
      fd.append("sn", item.serialNumber);
      fd.append("reason", "用户取消");
      this.loading = true;
      this.$g
        .post(url, fd)
        .cb(data => {
          if (data.status === 0) {
            this.$message.success("订单已经取消");
            this.loadUnpayList();
          } else {
            this.$message.me(data.message);
          }
        })
        .fcb(() => (this.loading = false))
        .req();
    },
    onCompleteOrder(item) {
      const url = `${this.apiUrl}/frontend/shopping-order/complete`;
      const fd = new FormData();
      fd.append("sn", item.serialNumber);
      this.loading = true;
      this.$g
        .post(url, fd)
        .cb(data => {
          if (data.status === 0) {
            this.$message.success("订单已完成");
            this.loadOnWayList();
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


