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
                <a-col :span="16">订单号: {{so.serialNumber}}</a-col>
              </a-row>

              <a-row class="inner-row" v-for="soi in so.items" :key="soi.id">
                <a-col :span="7">{{soi.commodityTitle}}</a-col>
                <a-col :span="4">
                  <a-avatar :size="64" icon="gift" shape="square" :src="soi.commodityImage" />
                </a-col>
                <a-col :span="7">{{soi.formulaTitle}}</a-col>
                <a-col :span="2">{{soi.formulaCount}}</a-col>
                <a-col :span="4" style="text-align:right">{{soi.price['amount']}}</a-col>
              </a-row>

              <a-row class="inner-row">
                <a-col :span="4">
                  <button class="cancel-button active" @click="onSubmitOrder">
                    <span>取消订单</span>
                  </button>
                </a-col>
                <a-col :span="4" :offset="12">积分：{{so.totalMoney['amount']}}</a-col>
                <a-col :span="4">
                  <button class="fav-button active" @click="onSubmitOrder">
                    <span>结算订单</span>
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
                  <a-tag v-if="so.status === 1" color="#87d068">正在出库</a-tag>
                  <a-tag v-if="so.status === 2" color="#87d068">已发货</a-tag>
                  订单号: {{so.serialNumber}}
                </a-col>
              </a-row>

              <a-row class="inner-row" v-for="soi in so.items" :key="soi.id">
                <a-col :span="7">{{soi.commodityTitle}}</a-col>
                <a-col :span="4">
                  <a-avatar :size="64" icon="gift" shape="square" :src="soi.commodityImage" />
                </a-col>
                <a-col :span="7">{{soi.formulaTitle}}</a-col>
                <a-col :span="2">{{soi.formulaCount}}</a-col>
                <a-col :span="4" style="text-align:right">{{soi.price['amount']}}</a-col>
              </a-row>

              <a-row class="inner-row">
                <a-col :span="4">积分：{{so.totalMoney['amount']}}</a-col>
                <a-col
                  :span="16"
                  v-if="so.status === 2"
                >{{so.postCompany}},快递单号：{{so.postSerialNumber}}</a-col>
                <a-col :span="4">
                  <button class="fav-button active" @click="onSubmitOrder">
                    <span>确认收货</span>
                  </button>
                </a-col>
              </a-row>

              <a-divider />
            </a-col>
          </a-row>
        </a-tab-pane>
        <a-tab-pane tab="订单记录" key="3">
          <a-list :dataSource="historyData"></a-list>
        </a-tab-pane>

        <!--
        <a-tab-pane tab="待收货" key="1">
          <a-list :dataSource="data">
            <a-list-item slot="renderItem" slot-scope="item">
              <a-row style="width:100%;margin: 20px 0;">
                <a-col :span="2">
                  <a-tag v-if="item.status === 0" color="#2db7f5">正在出库</a-tag>
                  <a-tag v-if="item.status === 1" color="#87d068">已出库</a-tag>
                </a-col>
                <a-col :span="2">
                  <a-avatar :src="item.thumb" shape="square" :size="64" icon="user" />
                </a-col>
                <a-col :span="4">
                  <router-link :to="{ path: '/product/' + item['id']}">{{item.title}}</router-link>
                </a-col>
                <a-col :span="3">
                  <p>{{item.specification}}</p>
                </a-col>
                <a-col :span="9">
                  <p>{{item.address}}</p>
                </a-col>
                <a-col :span="4">
                  <p v-if="item.status === 0">
                    <a-button>取消订单</a-button>
                  </p>
                  <p v-if="item.status === 1">{{item.expressName}}: {{item.expressNumber}}</p>
                </a-col>
              </a-row>
            </a-list-item>
          </a-list>
        </a-tab-pane>
        <a-tab-pane tab="已完成" key="2" forceRender>
          <a-list :dataSource="data">
            <a-list-item slot="renderItem" slot-scope="item">
              <a-row style="width:100%;margin: 20px 0;">
                <a-col :span="2">
                  <a-avatar :src="item.thumb" shape="square" :size="64" icon="user" />
                </a-col>
                <a-col :span="4">
                  <router-link :to="{ path: '/product/' + item['id']}">{{item.title}}</router-link>
                </a-col>
                <a-col :span="3">
                  <p>{{item.specification}}</p>
                </a-col>
                <a-col :span="5">
                  <p>完成时间: 2019年4月22日</p>
                </a-col>
                <a-col :span="10">
                  <a-button v-if="item.id === 1" @click="onComment">评价订单</a-button>&nbsp;
                  <a-tag v-if="item.id === 0" color="red">
                    <a-icon v-for="item in [1,2,3,4,5]" :key="item" type="star" />
                  </a-tag>&nbsp;
                  <a-button @click="onAfterSale">申请售后</a-button>
                </a-col>
              </a-row>
            </a-list-item>
          </a-list>
        </a-tab-pane>
        <a-tab-pane tab="售后" key="3">
          <a-list :dataSource="data">
            <a-list-item slot="renderItem" slot-scope="item">
              <a-row style="width:100%;margin: 20px 0;">
                <a-col :span="2">
                  <a-tag v-if="item.status === 0" color="#2db7f5">已赔付</a-tag>
                  <a-tag v-if="item.status === 1" color="#87d068">换货中</a-tag>
                </a-col>
                <a-col :span="2">
                  <a-avatar :src="item.thumb" shape="square" :size="64" icon="user" />
                </a-col>
                <a-col :span="4">
                  <router-link :to="{ path: '/product/' + item['id']}">{{item.title}}</router-link>
                  <p>{{item.specification}}</p>
                </a-col>
                <a-col :span="8">
                  <p>售后理由：快递损坏，申请换货</p>
                  <p>
                    <img
                      style="margin: 1px;"
                      width="64px"
                      height="64px"
                      src="http://localhost:8080/zshop/img/pms.jpg"
                      v-for="item in [1,2,3,4,5]"
                      :key="item"
                    />
                  </p>
                </a-col>
                <a-col :span="8">
                  <p v-if="item.id === 0">
                    <a-timeline>
                      <a-timeline-item color="green">2019年4月22日 已确认损坏，直接赔付</a-timeline-item>
                      <a-timeline-item>2019年4月18日 拍照上传，申请赔偿</a-timeline-item>
                    </a-timeline>
                  </p>
                  <p v-else>
                    <a-timeline>
                      <a-timeline-item color="red">2019年4月22日 已发货，请注意查收</a-timeline-item>
                      <a-timeline-item color="green">2019年4月20日 确认损坏，准备换货</a-timeline-item>
                      <a-timeline-item>2019年4月18日 拍照上传，申请赔偿</a-timeline-item>
                    </a-timeline>
                  </p>
                </a-col>
              </a-row>
            </a-list-item>
          </a-list>
        </a-tab-pane>
        -->
      </a-tabs>
    </div>
  </a-spin>
</template>
<script>
const data = [
  {
    id: 0,
    thumb: "http://localhost/zshop/img/pms.jpg",
    title: "小米手机9",
    specification: "暗夜黑",
    address: "赵钱孙李先生 13412345678 北京市 北京市 海淀区 阳光锦城2606",
    status: 1,
    expressName: "圆通大师",
    expressNumber: "201900123456"
  },
  {
    id: 1,
    thumb: "http://localhost/zshop/img/pms.jpg",
    title: "小米手机9 SE",
    specification: "暗夜黑",
    address: "赵钱孙李先生 13412345678 北京市 北京市 海淀区 阳光锦城2606",
    status: 0,
    expressName: "",
    expressNumber: ""
  }
];

export default {
  name: "OrderPage",
  data() {
    return {
      loading: false,
      data,
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
    onSubmitOrder(item) {}
  }
};
</script>
<style scoped>
</style>


