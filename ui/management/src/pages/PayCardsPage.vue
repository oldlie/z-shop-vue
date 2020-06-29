<template>
  <div>
    <a-row :gutter="4">
      <a-col :span="4">
        <sidebar :index="['6']" />
      </a-col>
      <a-col :span="20">
        <a-row class="inner-row">
          <a-col :span="24">
            <router-link :to="'/card'">
              <a-icon type="plus" />添加卡片
            </router-link>
          </a-col>
        </a-row>

        <a-row class="inner-row">
          <a-col :span="24">
            <a-spin :spinning="loading">
              <a-table :columns="columns" :dataSource="list" :pagination="false">
                <span slot="isSoldOut" slot-scope="record">
                  <a-tag v-if="record === 0" color="#2db7f5">未出售</a-tag>
                  <a-tag v-if="record === 1" color="#87d068">已出售</a-tag>
                </span>
                <span slot="isExchanged" slot-scope="record">
                  <a-tag v-if="record === 0" color="#2db7f5">未兑换</a-tag>
                  <a-tag v-if="record === 1" color="#87d068">已兑换</a-tag>
                </span>
                <span slot="action" slot-scope="record">
                  <a-button type="link" icon="eye" @click="handlePreview(record)"></a-button>
                  <a-popconfirm
                    title="删除"
                    @confirm="deleteCard(record)"
                    okText="确定"
                    cancelText="取消"
                  >
                    <a-button type="link" style="color:#f5222d">
                      <a-icon type="delete"></a-icon>
                    </a-button>
                  </a-popconfirm>
                </span>
              </a-table>
            </a-spin>
          </a-col>
        </a-row>

        <a-row class="inner-row">
          <a-col :span="24">
            <a-pagination
              size="small"
              :total="total"
              :pageSize="size"
              :current="index"
              @change="handlePagChange"
            ></a-pagination>
          </a-col>
        </a-row>
      </a-col>
    </a-row>
  </div>
</template>
<script>
const columns = [
  { title: "SN", dataIndex: "serialNumber" },
  { title: "卡名称", dataIndex: "title" },
  { title: "备注", dataIndex: "node" },
  { title: "面值", dataIndex: "denomination" },
  { title: "售价", dataIndex: "amount" },
  { title: "有效期", dataIndex: "validDay" },
  { title: "制卡日期", dataIndex: "createDate" },
  {
    title: "已出售",
    dataIndex: "isSoldOut",
    scopedSlots: { customRender: "isSoldOut" }
  },
  {
    title: "已兑换",
    dataIndex: "isExchanged",
    scopedSlots: { customRender: "isExchanged" }
  },
  { title: "操作", scopedSlots: { customRender: "action" } }
];
export default {
  data() {
    return {
      list: [],
      columns: columns,
      total: 0,
      size: 10,
      index: 1,
      orderBy: "id",
      direct: "desc",
      loading: false
    };
  },
  mounted() {
    this.load();
  },
  methods: {
    handlePreview(record) {
      this.$router.push(`/card-preview/${record.id}`);
    },
    deleteCard(record) {
      const url = `/backend/pay-card/${record.id}`;
      this.$h
        .delete(url)
        .cb(data => {
          if (data.status === 0) {
            this.list = this.list.filter(x => x.id !== record.id);
          }
        })
        .fcb()
        .req();
    },
    handlePagChange(page, size) {
      this.index = page;
      this.load();
    },
    load() {
      const url = `/backend/pay-cards/${this.index}-${this.size}-${this.orderBy}-${this.direct}`;
      this.loading = true;
      this.$h
        .get(url)
        .cb(data => {
          if (data.status === 0) {
            let arr = [];
            for (let i = 0; i < data.list.length; i++) {
              let tmp = data.list[i];
              arr.push({
                id: tmp["id"],
                serialNumber: tmp["serialNumber"],
                title: tmp["title"],
                node: tmp["node"],
                denomination: "¥" + tmp["denomination"]["amount"],
                amount: "¥" + tmp["amount"]["amount"],
                validDay: tmp["validDay"] + "日",
                createDate: tmp["createDate"],
                isSoldOut: tmp["isSoldOut"],
                isExchanged: tmp["isExchanged"]
              });
            }
            this.list = arr;
            this.total = data.total;
          } else {
            this.$message.error(data.message);
          }
        })
        .fcb(() => (this.loading = false))
        .req();
    }
  }
};
</script>