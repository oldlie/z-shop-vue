<template>
  <div>
    <a-row :gutter="4">
      <a-col :span="4">
        <sidebar :index="['4']" />
      </a-col>
      <a-col :span="20">
        <a-row class="inner-row">
          <a-col :span="24">
            <h2>商品列表</h2>
          </a-col>
        </a-row>
        <a-row class="inner-row">
          <a-col :span="24">
            <a-button @click="addCommodity('form')" icon="plus" type="link">添加商品</a-button>
            <a-divider type="vertical"></a-divider>
          </a-col>
        </a-row>
        <a-row class="inner-row">
          <a-col :span="24">
            <a-table :columns="columns" :dataSource="dataSet" :loading="tableLoading">
              <span slot="thumbnail" slot-scope="url">
                <img v-if="!!url" :src="url" alt="首页图" width="32px" height="32px" />
                <img
                  v-else
                  src="http://localhost/resources/images/default.png"
                  alt="首页图"
                  width="32px"
                  height="32px"
                />
              </span>
              <span slot="status" slot-scope="status">
                <a-tag :color="status | formatStatusColor">{{status | formatStatus }}</a-tag>
              </span>
              <span slot="action" slot-scope="text, record">
                <a href="javascript:;" @click="editCommodity(record)">
                  <a-icon type="edit"></a-icon>
                </a>
                <a-divider type="vertical"></a-divider>
                <template v-if="record.status === 1">
                  <a href="javascript:;" @click="offlineCommodity(record)">
                    <a-icon type="download"></a-icon>
                  </a>
                  <a-divider type="vertical"></a-divider>
                </template>
                <template v-if="record.status === 2">
                  <a href="javascript:;" @click="onlineCommodity(record)">
                    <a-icon type="upload"></a-icon>
                  </a>
                  <a-divider type="vertical"></a-divider>
                </template>
              </span>
            </a-table>
          </a-col>
        </a-row>
      </a-col>
    </a-row>

    <a-modal
      title="添加新的商品信息"
      :visible="visible"
      :confirm-loading="confirmLoading"
      @ok="handleOk"
      :ok-button-props="{ props: { disabled: okButtonStatus } }"
      @cancel="handleCancel"
    >
      <p>请先输入商品名称：</p>
      <p>
        <a-input v-model="newCommodityTitle"></a-input>
      </p>
    </a-modal>
  </div>
</template>
<script>
const columns = [
  {
    title: "状态",
    key: "status",
    dataIndex: "status",
    scopedSlots: { customRender: "status" }
  },
  {
    title: "图",
    key: "thumbnail",
    dataIndex: "thumbnail",
    width: "48px",
    scopedSlots: { customRender: "thumbnail" }
  },
  {
    title: "商品名称",
    key: "title",
    dataIndex: "title",
    width: "40%"
  },
  {
    title: "简述",
    key: "introduction",
    dataIndex: "introduction"
  },
  {
    key: "action",
    title: "Action",
    scopedSlots: { customRender: "action" }
  }
];
const tags = [];
const dataSet = [];
const viewModel = {
  table: "table",
  form: "form"
};
export default {
  data() {
    return {
      // region add new commodity modal
      visible: false,
      confirmLoading: false,
      okButtonStatus: true,
      newCommodityTitle: "",
      // endregion
      columns,
      dataSet: [],
      tableLoading: false,
      pagination: {
        index: 1,
        size: 10,
        orderBy: "id",
        order: "desc"
      }
    };
  },
  watch: {
    newCommodityTitle(nv) {
      if (nv && nv.trim() !== "") {
        this.okButtonStatus = false;
      } else {
        this.okButtonStatus = true;
      }
    }
  },
  filters: {
    formatStatus(value) {
      if (value === undefined || value === null) return;
      const status = Number(value);
      switch (status) {
        case 0:
          return "还未完成初始化";
        case 1:
          return "已上架";
        case 2:
          return "已下架";
        case 3:
          return "售罄|暂停销售";
        default:
          return "";
      }
    },
    formatStatusColor(value) {
      if (!value) return;
      const status = Number(value);
      switch (status) {
        case 1:
          return "#87d068";
        case 2:
          return "#bfbfbf";
        case 3:
          return "#ff7a45";
        default:
          return "";
      }
    }
  },
  mounted() {
    this.loadCommodities();
  },
  methods: {
    addCommodity() {
      this.visible = true;
    },
    handleOk() {
      const url = "/backend/product";
      this.confirmLoading = true;
      this.$h
        .post(url, { title: this.newCommodityTitle })
        .cb(data => {
          if (data.status === 0) {
            this.$router.push(`/commodity/${data.item}`);
          }
        })
        .fcb(() => (this.confirmLoading = false))
        .req();
    },
    handleCancel() {
      this.visible = false;
    },
    editCommodity(commodity) {
      this.$router.push(`/commodity/${commodity.id}`);
    },
    loadCommodities() {
      const url = `/backend/products/\
${this.pagination.index}/\
${this.pagination.size}/\
${this.pagination.orderBy}/\
${this.pagination.order}`;
      this.tableLoading = true;
      const self = this;
      this.$h
        .get(url)
        .cb(data => {
          console.log("data", data);
          self.dataSet = data.list;
        })
        .fcb(() => (self.tableLoading = false))
        .req();
    },
    onlineCommodity(commodity) {
      const url = `/backend/product/online`;
      const fd = new FormData();
      fd.append("id", commodity.id);
      this.$h
        .post(url, fd)
        .cb(data => {
          if (data.status === 0) {
            commodity.status = 1;
            this.$message.success("已上架");
          } else {
            console.error(data);
          }
        })
        .fcb()
        .req();
    },
    offlineCommodity(commodity) {
      const url = `/backend/product/offline`;
      const fd = new FormData();
      fd.append("id", commodity.id);
      this.$h
        .post(url, fd)
        .cb(data => {
          if (data.status === 0) {
            commodity.status = 2;
            this.$message.success("已下架");
          } else {
            console.error(data);
          }
        })
        .fcb()
        .req();
    }
  }
};
</script>