<template>
  <div>
    <a-row :gutter="4">
      <a-col :span="4">
        <sidebar :index="['6']" />
      </a-col>
      <a-col :span="20">
        <div class="inner-row">
          <h2>兑换卡操作记录</h2>
          <p>参考余额是指用户兑换之后当时的余额，不是用户实时的余额，仅作参考只用</p>
        </div>

        <a-row class="inner-row">
          <a-col :span="24">
            <router-link :to="'/cards'">
              <a-icon type="table" />卡片列表
            </router-link>
            <a-divider type="vertical" />
            <router-link :to="'/exchanges'">
              <a-icon type="table" />兑换列表
            </router-link>
            <a-divider type="vertical" />
            <router-link :to="'/logs'">
              <a-icon type="table" />操作列表
            </router-link>
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

        <a-row class="inner-row">
          <a-col :span="24">
            <a-spin :spinning="loading">
              <a-table :columns="columns" :dataSource="list" :pagination="false">
                <span slot="money" slot-scope="item">{{item['exchangeMoney']['amount']}}</span>
                <span slot="balance" slot-scope="item">{{item['balance']['amount']}}</span>
              </a-table>
            </a-spin>
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
      </a-col>
    </a-row>
  </div>
</template>
<script>
const columns = [
  { title: "发生时间", dataIndex: "createDate" },
  { title: "事件信息", dataIndex: "comment" },
  { title: "兑换卡ID", dataIndex: "cardId" },
  { title: "操作描述", dataIndex: "optDescription" },
  { title: "操作用户", dataIndex: "optUsername" }
];
export default {
  data() {
    return {
      columns,
      loading: false,
      list: [],
      total: 0,
      page: 1,
      size: 10
    };
  },
  mounted() {
    this.loadData();
  },
  methods: {
    loadData() {
      const url = `/backend/pay-card/logs?page=${this.page}&size=${this.size}`;
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
    paginationChange(page) {
      this.page = page;
      this.loadData();
    }
  }
};
</script>