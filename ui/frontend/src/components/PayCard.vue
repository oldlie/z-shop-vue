<template>
  <a-spin :spinning="loading">
    <a-row class="inner-row">
      <a-button icon="sync" @click="loadData">刷新卡片列表</a-button>
    </a-row>
    <a-row>
      <a-col :span="24">
        <a-list :grid="{ gutter: 16, column: 2 }" :data-source="list">
          <a-list-item slot="renderItem" slot-scope="payCard, index">
            <a-card>
              <div style="margin: 0, auto; width: 400px">
                <div class="pay-card">
                  <h1 style="color:#F0E68C;text-shadow:1px 1px 1px #ffffff;">{{payCard.title}}</h1>
                  <div class="pay-card-price">
                    <span>¥ {{!!payCard['denomination'] ? payCard['denomination']['amount'] : ''}} 元</span>
                  </div>
                  <div class="pay-card-verify">
                    <div style="float:right; width: 69px;">{{payCard['verifyCode']}}</div>
                    <span>兑换码：</span>
                  </div>
                  <div class="pay-card-bottom">
                    <div class="pay-card-valid">有效期：{{payCard.latestExchangeDate}}</div>
                    <div class="pay-card-sn">SN:{{payCard.serialNumber}}</div>
                  </div>
                </div>
                <div class="inner-row">
                  <template v-if="payCard.isExchanged === 0">
                    <a-button @click="onExchange(payCard)" :loading="payCard.id === currentId">充值</a-button>
                    <a-divider type="vertical" />
                    <a-popconfirm
                      title="移除兑换卡?移除之后将不可充值"
                      content="卡片移除之后将不能兑换，确定移除吗？"
                      @confirm="invalid(payCard)"
                      okText="确定"
                      cancelText="取消"
                    >
                      <a href="javascript:;" style="color:#f5222d">
                        <a-icon type="delete" />移除
                      </a>
                    </a-popconfirm>
                  </template>
                  <span v-if="payCard.isExchanged === 1">已充值</span>
                  <span v-if="payCard.isExchanged === 2">已移除</span>
                </div>
              </div>
            </a-card>
          </a-list-item>
        </a-list>
      </a-col>
    </a-row>
    <a-row class="inner-row">
      <a-pagination
        size="small"
        :total="total"
        :pageSize="size"
        :current="page"
        @change="paginationChange"
      />
    </a-row>
  </a-spin>
</template>
<script>
export default {
  data() {
    return {
      loading: false,
      total: 0,
      size: 10,
      page: 1,
      list: [],
      currentId: 0
    };
  },
  mounted() {
    this.loadData();
  },
  methods: {
    paginationChange(page) {
      this.page = page;
      this.loadData();
    },
    loadData() {
      const url = `${this.apiUrl}frontend/card/my-valid-cards?page=${this.page}&size=${this.size}`;
      this.loading = true;
      this.$g
        .get(url)
        .cb(data => {
          if (data.status === 0) {
            this.total = data.total;
            this.list = data.list;
          } else {
            this.$message.me(data.message);
          }
        })
        .fcb(() => (this.loading = false))
        .req();
    },
    onExchange(item) {
      const url = `${this.apiUrl}/frontend/card/exchange?sn=${item.serialNumber}&vc=${item.verifyCode}`;
      this.loading = true;
      this.currentId = item.id;
      this.$g
        .post(url, {
          sn: item.serialNumber,
          vc: item.verifyCode
        })
        .cb(data => {
          if (data.status === 0) {
            item.isExchanged = 1;
            this.$emit("exchanged", data.item["amount"]);
          } else {
            console.error("exchange card --->", data);
            this.$message.me(data.message);
          }
        })
        .fcb(() => (this.currentId = 0))
        .req();
    },
    invalid (item) {
      const url = `${this.apiUrl}/frontend/card/invalid?sn=${item.serialNumber}&vc=${item.verifyCode}`;
      this.currentId = item.id;
      this.$g
        .post(url, {
          sn: item.serialNumber,
          vc: item.verifyCode
        })
        .cb(data => {
          if (data.status === 0) {
            item.isExchanged = 2;
          } else {
            this.$message.me(data.message);
          }
        })
        .fcb(() => (this.currentId = 0))
        .req();
    }
  }
};
</script>
<style scoped>
.product-card {
  width: 240px;
  height: 400px;
  margin: 10px;
}
</style>