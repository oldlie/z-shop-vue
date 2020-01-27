<template>
  <div class="main-panel">
    <a-row>
      <a-col :span="4">
        <admin-menu :activeIndex="'sub2'" :openKey="'sub2'"></admin-menu>
      </a-col>
      <a-col :span="20">
        <a-row class="inner-row" v-if="viewModel === 'table'">
          <a-col :span="24">
            <a-button @click="addCommodity('form')" icon="plus" type="link">添加商品</a-button>
            <a-divider type="vertical"></a-divider>
            <a-button @click="onAddTag(0)" icon="filter" type="link">标签筛选</a-button>
            <a-divider type="vertical"></a-divider>
            <a-button @click="onAddTag(0)" icon="table" type="link">字段筛选</a-button>
          </a-col>
        </a-row>
        <a-row class="inner-row" v-if="viewModel === 'form'">
          <a-col :span="24">
            <a-button @click="changeViewMoel('table')" icon="rollback" type="link">添加商品</a-button>
          </a-col>
        </a-row>

        <a-row class="inner-row" v-if="viewModel === 'table'">
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
                <a href="javascript:;"  @click="offlineCommodity(record)">
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
                <a href="javascript:;" style="color:#f5222d">
                  <a-icon type="delete"></a-icon>
                </a>
              </span>
            </a-table>
          </a-col>
        </a-row>

        <a-row class="inner-row" v-if="viewModel === 'form'">
          <admin-product :commodity="commodity"></admin-product>
        </a-row>
      </a-col>
    </a-row>
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
const tags = [
  { id: 0, title: "New Products" },
  { id: 1, title: "Fruit" },
  { id: 2, title: "Aquatic Products" },
  { id: 3, title: "Snacks" },
  { id: 4, title: "Commodity" }
];
const dataSet = [
  { id: 0, title: "MI 9", status: 1 },
  { id: 1, title: "MI 9se", status: 2 },
  { id: 2, title: "Redmi 5", status: 0 },
  { id: 3, title: "Mi 8", status: 3 },
  { id: 4, title: "Mi 8se", status: 1 }
];
const viewModel = {
  table: "table",
  form: "form"
};
export default {
  data() {
    return {
      tags,
      tagViewModel: 0,
      viewModel: "table",
      // region table
      columns,
      dataSet,
      tableLoading: false,
      pagination: {
        index: 1,
        size: 10,
        orderBy: "id",
        order: "desc"
      },
      // endregion
      commodity: {}
    };
  },
  mounted() {
    this.loadCommodities();
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
  methods: {
    onTagClose() {},
    onAddTag(viewModel) {
      this.tagViewModel = viewModel;
    },
    changeViewMoel(viewModel) {
      this.viewModel = viewModel;
    },
    addCommodity() {
      this.viewModel = "form";
      this.commodity = {
        id: 0
      };
    },
    editCommodity(commodity) {
      this.commodity = commodity;
      this.viewModel = "form";
    },
    loadCommodities() {
      const url = `${this.apiUrl}/backend/products/\
${this.pagination.index}/\
${this.pagination.size}/\
${this.pagination.orderBy}/\
${this.pagination.order}`;
      this.tableLoading = true;
      const self = this;
      G.get(url)
        .cb(data => {
          console.log("data", data);
          self.dataSet = data.list;
        })
        .fcb(() => (self.tableLoading = false))
        .req();
    },
    onlineCommodity (commodity) {
      const url = `${this.apiUrl}/backend/product/online`;
      const fd = new FormData();
      fd.append('id', commodity.id);
      G.post(url, fd)
      .cb(data => {
        if (data.status === 0) {
          commodity.status = 1;
          this.$message.success('已上架');
        } else {
          console.error(data);
        }
      })
      .fcb()
      .req();
    },
    offlineCommodity (commodity) {
      const url = `${this.apiUrl}/backend/product/offline`;
      const fd = new FormData();
      fd.append('id', commodity.id);
      G.post(url, fd)
      .cb(data => {
        if (data.status === 0) {
          commodity.status = 2;
          this.$message.success('已下架');
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
<style scoped>
</style>
