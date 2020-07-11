<template>
  <div>
    <a-row :gutter="4">
      <a-col :span="4">
        <sidebar :index="['9']" />
      </a-col>
      <a-col :span="20">
        <a-row class="inner-row">
          <a-col :span="24">
            <h2>订单管理</h2>
          </a-col>
        </a-row>
        <a-row class="inner-row">
          <a-col :span="24">
            <a-spin :spinning="loading">
              <a-tabs defaultActiveKey="1" @change="onTabsChange">
                <a-tab-pane tab="新订单/未处理订单" key="1">
                  <a-row class="inner-row" v-for="item in newList" :key="item.id">
                    <a-col :span="24">
                      <a-row class="inner-row">
                        <a-col :span="16">订单号：{{item.serialNumber}}</a-col>
                      </a-row>
                      <a-row class="inner-row">
                        <a-col :span="16">快递地址：{{item.addressInfo}}</a-col>
                      </a-row>
                      <a-row class="inner-row" v-for="item_ in item.items" :key="item_.id">
                        <a-col :span="7">{{item_.commodityTitle}}</a-col>
                        <a-col :span="4">
                          <a-avatar
                            :size="64"
                            icon="gift"
                            shape="square"
                            :src="item_.commodityImage"
                          />
                        </a-col>
                        <a-col :span="7">{{item_.formulaTitle}}</a-col>
                        <a-col :span="2">{{item_.formulaCount}}</a-col>
                        <a-col :span="4" style="text-align:right">单价：{{item_.price['amount']}}</a-col>
                      </a-row>
                      <a-row class="inner-row" v-if="item.status === 1">
                        <a-col :span="4">
                          <a-button @click="onCancelOrder(item)">
                            <span>取消订单</span>
                          </a-button>
                        </a-col>
                        <a-col :span="4" :offset="12">总价：{{item.totalMoney['amount']}}</a-col>
                        <a-col :span="4">
                          <a-button type="primary" @click="onSubmitOrder(item)">
                            <span>发货</span>
                          </a-button>
                        </a-col>
                      </a-row>

                      <a-divider />
                    </a-col>
                  </a-row>
                  <a-row class="inner-row">
                    <a-col :span="24">
                      <a-pagination
                        size="small"
                        :total="total"
                        :pageSize="size"
                        :current="index"
                        @change="paginationChange"
                      />
                    </a-col>
                  </a-row>
                </a-tab-pane>
                <a-tab-pane tab="在途订单" key="2">
                  <a-row class="inner-row" v-for="item in onWayList" :key="item.id">
                    <a-col :span="24">
                      <a-row class="inner-row">
                        <a-col :span="16">订单号：{{item.serialNumber}}</a-col>
                      </a-row>
                      <a-row class="inner-row">
                        <a-col :span="16">快递地址：{{item.addressInfo}}</a-col>
                      </a-row>
                      <a-row class="inner-row" v-for="item_ in item.items" :key="item_.id">
                        <a-col :span="7">{{item_.commodityTitle}}</a-col>
                        <a-col :span="4">
                          <a-avatar
                            :size="64"
                            icon="gift"
                            shape="square"
                            :src="item_.commodityImage"
                          />
                        </a-col>
                        <a-col :span="7">{{item_.formulaTitle}}</a-col>
                        <a-col :span="2">{{item_.formulaCount}}</a-col>
                        <a-col :span="4" style="text-align:right">单价：{{item_.price['amount']}}</a-col>
                      </a-row>
                      <a-row class="inner-row"></a-row>
                        <a-col :span="24">
                          <p>已发快递：{{item.postCompany}} </p>
                          <p>快递号单：{{item.postSerialNumber}}</p>
                        </a-col>
                      <a-divider />
                    </a-col>
                  </a-row>
                  <a-row class="inner-row">
                    <a-col :span="24">
                      <a-pagination
                        size="small"
                        :total="total"
                        :pageSize="size"
                        :current="index"
                        @change="paginationChange"
                      />
                    </a-col>
                  </a-row>
                </a-tab-pane>
                <a-tab-pane tab="全部订单" key="3">
                  <a-row class="inner-row">
                    <a-col :span="24">
                      <a-input-search
                        v-model="sn"
                        placeholder="输入订单号，需要模糊查询时请添加%号"
                        style="width: 400px"
                        @search="onSearch"
                      />
                    </a-col>
                  </a-row>
                  <a-row class="inner-row" v-for="item in list" :key="item.id">
                    <a-col :span="24">
                      <a-row class="inner-row">
                        <a-col :span="16">
                          <span>订单号：{{item.serialNumber}}</span>
                          <a-divider type="vertical" />
                          <a-tag v-if="item.status === 0" color="#ff6700">待支付</a-tag>
                          <a-tag v-if="item.status === 1" color="#87d068">正在出库</a-tag>
                          <a-tag v-if="item.status === 2" color="#2db7f5">已发货</a-tag>
                          <a-tag v-if="item.status === 8" color="#135200">已完成</a-tag>
                          <a-tag v-if="item.status === 10">已取消</a-tag>
                        </a-col>
                      </a-row>
                      <a-row class="inner-row">
                        <a-col :span="16">快递地址：{{item.addressInfo}}</a-col>
                      </a-row>
                      <a-row class="inner-row" v-for="item_ in item.items" :key="item_.id">
                        <a-col :span="7">{{item_.commodityTitle}}</a-col>
                        <a-col :span="4">
                          <a-avatar
                            :size="64"
                            icon="gift"
                            shape="square"
                            :src="item_.commodityImage"
                          />
                        </a-col>
                        <a-col :span="7">{{item_.formulaTitle}}</a-col>
                        <a-col :span="2">{{item_.formulaCount}}</a-col>
                        <a-col :span="4" style="text-align:right">单价：{{item_.price['amount']}}</a-col>
                      </a-row>
                      <a-row class="inner-row"></a-row>

                      <a-divider />
                    </a-col>
                  </a-row>
                  <a-row class="inner-row">
                    <a-col :span="24">
                      <a-pagination
                        size="small"
                        :total="total"
                        :pageSize="size"
                        :current="index"
                        @change="paginationChange"
                      />
                    </a-col>
                  </a-row>
                </a-tab-pane>
              </a-tabs>
            </a-spin>
          </a-col>
        </a-row>
      </a-col>
    </a-row>

    <a-modal v-model="visible" title="取消订单" @ok="onCancel" @cancel="visible = false">
      <template slot="footer">
        <a-button key="back" @click="visible = false">关闭对话框</a-button>
        <a-button key="submit" type="primary" :loading="loading" @click="onCancel">取消订单</a-button>
      </template>
      <p>
        <a-row class="inner-row" :gutter="6">
          <a-col :span="24">
            <a-textarea
              v-model="reason"
              placeholder="填写取消理由"
              :auto-size="{ minRows: 3, maxRows: 5 }"
            />
          </a-col>
        </a-row>
      </p>
    </a-modal>

    <a-modal v-model="visible1" title="发货信息" @cancel="visible1 = false">
      <template slot="footer">
        <a-button key="back" @click="visible1 = false">取消</a-button>
        <a-button key="submit" type="primary" :loading="loading" @click="onDelivering">发货</a-button>
      </template>
      <p>
        快递号单：
        <a-input v-model="expressSn"></a-input>
      </p>
      <p>
        快递公司：
        <a-input v-model="expressCom"></a-input>
      </p>
    </a-modal>
  </div>
</template>
<script>
export default {
  data() {
    return {
      loading: false,
      newList: [],
      onWayList: [],
      list: [],
      index: 1,
      size: 10,
      total: 0,
      tab: 1,
      sn: "",
      orderBy: "id",
      direct: "desc",
      visible: false,
      visible1: false,
      item: {},
      reason: "",
      expressSn: "",
      expressCom: ""
    };
  },
  mounted() {
    this.loadNewList();
  },
  methods: {
    onTabsChange(index) {
      this.tab = index;
      if (this.tab === "1") {
        this.loadNewList();
      } else if (this.tab === "2") {
        this.loadOnWayList();
      } else if (this.tab === "3") {
        this.loadList();
      }
    },
    loadNewList() {
      const url = `/backend/shopping-order/new-list?page=${this.index}&size=${this.size}`;
      this.loading = true;
      this.$h
        .get(url)
        .cb(data => {
          if (data.status === 0) {
            this.newList = data.list;
            this.total = data.total;
          } else {
            this.$message.me(data.message);
          }
        })
        .fcb(() => (this.loading = false))
        .req();
    },
    onCancelOrder(item) {
      this.item = item;
      this.visible = true;
    },
    onCancel() {
      const url = `/backend/shopping-order/cancel`;
      const fd = new FormData();
      fd.append("sn", this.item.serialNumber);
      fd.append("reason", this.reason);
      this.loading = true;
      this.$h
        .post(url, fd)
        .cb(data => {
          if (data.status === 0) {
            this.$message.success("已取消");
            this.item.status = 8;
          } else {
            this.$message.me(data.message);
          }
        })
        .fcb(() => (this.loading = false))
        .req();
    },
    onSubmitOrder(item) {
      this.item = item;
      this.visible1 = true;
    },
    onDelivering() {
      const url = `/backend/shopping-order/delivering`;
      const fd = new FormData();
      fd.append("id", this.item.id);
      fd.append("psn", this.expressSn);
      fd.append("pc", this.expressCom);
      this.loading = true;
      this.$h
        .post(url, fd)
        .cb(data => {
          if (data.status === 0) {
            this.$message.success("已发货");
            this.item.status = 2;
          } else {
            this.$message.me(data.message);
          }
        })
        .fcb(() => (this.loading = false))
        .req();
    },
    paginationChange(page) {
      this.index = page;
      if (this.tab === "1") {
        this.loadNewList();
      } else if (this.tab === "2") {
        this.loadOnWayList();
      } else if (this.tab === "3") {
        this.loadList();
      }
    },
    loadOnWayList() {
      const url = `/backend/shopping-order/on-way-list?page=${this.index}&size=${this.size}`;
      this.loading = true;
      this.$h
        .get(url)
        .cb(data => {
          console.log("onWayList--->", data);
          if (data.status === 0) {
            this.onWayList = data.list;
            this.total = data.total;
          } else {
            this.$message.me(data.message);
          }
        })
        .fcb(() => (this.loading = false))
        .req();
    },
    loadList() {
      let url = `/backend/shopping-order-list?page=${this.index}&size=${this.size}&orderBy=${this.orderBy}&direct=${this.direct}`;
      if (this.sn && this.sn !== "") {
        url += `&sn=${encodeURIComponent(this.sn)}`;
      }
      this.loading = true;
      this.$h
        .get(url)
        .cb(data => {
          if (data.status === 0) {
            this.list = data.list;
            this.total = data.total;
          } else {
            this.$message.me(data.message);
          }
        })
        .fcb(() => (this.loading = false))
        .req();
    },
    onSearch() {
      this.loadList();
    }
  } // end of methods;
};
</script>