<template>
  <div>
    <a-row :gutter="4">
      <a-col :span="4">
        <sidebar :index="['10']" />
      </a-col>
      <a-col :span="20">
        <div class="inner-row">
          <h2>系统账户流水</h2>
          <p>获取系统的资金流水，确切流水应该已每项记录结果累加为准，每项中的余额仅作参考</p>
        </div>

        <a-row class="inner-row">
          <a-col :span="24">
            <a-spin :spinning="loading">
              <a-table :columns="columns" :dataSource="list" :pagination="false">
                <span slot="isAdd" slot-scope="item">
                  <a-tag color="#87d068" v-if="item.isAdd === 1">收入</a-tag>
                  <a-tag color="#f50" v-if="item.isAdd === -1">支出</a-tag>
                </span>
                <span slot="money" slot-scope="item">{{item['money']['amount']}}</span>
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
  { title: "状态", width: "240px", scopedSlots: { customRender: "isAdd" } },
  { title: "事件金额", scopedSlots: { customRender: "money" } },
  { title: "参考余额", scopedSlots: { customRender: "balance" } }
];
export default {
  data() {
    return {
      columns,
      loading: false,
      list: [],
      total: 0,
      page: 1,
      size: 20
    };
  },
  mounted() {
    this.loadData();
  },
  methods: {
    loadData() {
      const url = `/backend/financial/list?page=${this.page}&size=${this.size}`;
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