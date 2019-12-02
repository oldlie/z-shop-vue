<template>
  <a-spin :spinning="loading">
    <a-row>
      <a-col :span="24">
        <a-table :columns="columns" :dataSource="formarlData">
          <span slot="action" slot-scope="text, record">
            <a href="javascript:;">Delete</a>
          </span>
        </a-table>
      </a-col>
    </a-row>
     <a-divider />
     <a-row>
         <a-col :md="12" :sm=24>
             Add Form
         </a-col>
     </a-row>
  </a-spin>
</template>
<script>
export default {
  props: {
    id: { type: Number, default: 0 }
  },
  data() {
    return {
      loading: false,
      formarlData: [],
      columns: [
        {
          dataIndex: "title",
          title: "套餐",
          key: "title",
          scopedSlots: { customRender: "title" }
        },
        {
          dataIndex: "price",
          title: "价格（元）",
          key: "price"
        },
        {
          dataIndex: "inventory",
          title: "库存（件）",
          key: "inventory"
        }
      ]
    };
  }
};
</script>