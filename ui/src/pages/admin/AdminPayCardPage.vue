<template>
  <div class="main-panel">
    <a-row>
      <a-col :span="4">
        <admin-menu :activeIndex="'sub4'" :openKey="'sub4'"></admin-menu>
      </a-col>

      <a-col :span="20">
        <a-row class="inner-row" v-if="view === 'form'">
          <a-col :span="24">
            <a-button @click="changeView('table')" icon="table" type="link">礼品卡列表</a-button>
          </a-col>
          <a-col :span="24" class="inner-row">
            <admin-pay-card></admin-pay-card>
          </a-col>
        </a-row>

        <a-row class="inner-row" v-if="view === 'table'">
          <a-spin :spinning="loading">
            <a-col :span="24">
              <a-button @click="changeView('form')" icon="plus" type="link">添加卡</a-button>
            </a-col>
            <a-col :span="24" class="inner-row">
              <a-table :columns="columns" :dataSource="list" :pagination="false">
                <span slot="isExchanged" slot-scope="record">
                  <a-tag v-if="record === 0" color="#2db7f5">未兑换</a-tag>
                  <a-tag v-if="record === 1" color="#87d068">已兑换</a-tag>
                </span>
                <span slot="action" slot-scope="record">
                  <a-button
                    type="link"
                    icon="eye"
                    @click="handlePreview(record)"
                  ></a-button>
                  <a-button
                    type="link"
                    icon="delete"
                    style="color:#f5222d"
                    @click="deleteFormula(record)"
                  ></a-button>
                </span>
              </a-table>
            </a-col>
            <a-col class="inner-row">
              <a-pagination
                :total="total"
                :pageSize="size"
                :current="index"
                @change="handlePagChange"
              ></a-pagination>
            </a-col>
          </a-spin>
        </a-row>

        <a-row class="inner-row" v-if="view === 'preview'">
          <a-col :span="24" class="inner-row">
            <a-button @click="changeView('table')" icon="table" type="link">礼品卡列表</a-button>
          </a-col>
          <a-col class="inner-row" :span="24">
            <admin-pay-card-preview :pay-card-id="previewId"></admin-pay-card-preview>
          </a-col>
        </a-row>

      </a-col>
    </a-row>
  </div>
</template>

<script>
const _view = {
  table: "table",
  form: "form",
  preview: "preview"
};

const columns = [
  { title: "SN", dataIndex: "serialNumber" },
  { title: "卡名称", dataIndex: "title" },
  { title: "备注", dataIndex: "node" },
  { title: "面值", dataIndex: "denomination" },
  { title: "售价", dataIndex: "amount" },
  { title: "有效期", dataIndex: "validDay" },
  { title: "制卡日期", dataIndex: "createDate" },
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
      view: _view.table,
      loading: false,
      columns,
      index: 1,
      size: 10,
      total: 0,
      orderBy: "id",
      order: "desc",
      key: "",
      value: "",
      list: [],
      previewId: 0,
    };
  },
  mounted() {
    this.load();
  },
  methods: {
    changeView(view) {
      this.view = view;
    },
    load() {
      const url = `${this.apiUrl}/backend/pay-cards/${this.index}-${this.size}-${this.orderBy}-${this.order}`;
      this.loading = true;
      G.get(url)
        .cb(data => {
          if (data.status === 0) {
            let arr = [];
            for (let i = 0; i < data.list.length; i++) {
              let tmp = data.list[i];
              arr.push({
                id: tmp['id'],
                serialNumber: tmp["serialNumber"],
                title: tmp["title"],
                node: tmp["node"],
                denomination: "¥" + tmp["denomination"]["amount"],
                amount: "¥" + tmp["amount"]["amount"],
                validDay: tmp["validDay"] + "日",
                createDate: tmp["createDate"],
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
    },
    handlePagChange(page, pageSize) {
      this.index = page;
      this.load();
    },
    handlePreview(record) {
      console.log('record', record);
       this.previewId = record.id;
       this.view = _view.preview;
    }
  }
};
</script>

<style>
</style>