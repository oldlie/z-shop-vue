<template>
  <a-spin :spinning="loading">
    <template v-if="viewModel === VIEWMODEL.form">
      <a-row>
        <a-col :md="12" :sm="24">
          <a-form refs="form" :form="form">
            <!-- title -->
            <a-form-item
              label="套餐"
              :label-col="{ span: 5 }"
              has-feedback
              :wrapper-col="{ span: 12 }"
            >
              <a-input
                placeholder="请输入套餐"
                v-decorator="[
                'title',
                {
                  rules: [
                   {required: true, message: '请输入套餐！'}
                  ]
                }
              ]"
              ></a-input>
            </a-form-item>
            <!-- ./ title -->
            <!-- price -->
            <a-form-item
              label="价格"
              :label-col="{ span: 5 }"
              has-feedback
              :wrapper-col="{ span: 12 }"
            >
              <a-input
                placeholder="请输入价格"
                v-decorator="[
                'price',
                {
                  rules: [
                   {required: true, message: '请输入价格！'}
                  ]
                }
              ]"
              >
                <a-icon slot="prefix" type="pay-circle" />
              </a-input>
            </a-form-item>
            <!-- ./ price -->
            <!-- inventory -->
            <a-form-item
              label="库存"
              :label-col="{ span: 5 }"
              has-feedback
              :wrapper-col="{ span: 12 }"
            >
              <a-input-number
                placeholder="请输入库存"
                :min="1"
                :max="9999"
                v-decorator="[
                'inventory',
                {
                  rules: [
                   {required: true, message: '请输入库存！'}
                  ]
                }
              ]"
              ></a-input-number>
            </a-form-item>
            <!-- ./ inventory -->
            <a-form-item :wrapper-col="{ span: 12, offset: 5 }">
              <a-button type="primary" @click="handleSubmit">提交</a-button>
              <a-button type="link" icon="table" @click="changeModel(VIEWMODEL.list)">显示列表</a-button>
            </a-form-item>
          </a-form>
        </a-col>
      </a-row>
    </template>
    <template v-if="viewModel === VIEWMODEL.list">
      <a-row>
        <a-col :span="24">
          <a-table :columns="columns" :dataSource="dataSet" :pagination="false">
            <span slot="action" slot-scope="text, record">
              <a-button style="color:#f5222d" type="link" icon="delete"></a-button>
            </span>
          </a-table>

          <a-button type="link" icon="plus" @click="changeModel(VIEWMODEL.form)">添加套餐</a-button>
        </a-col>
      </a-row>
    </template>
  </a-spin>
</template>
<script>
const __viewModel = {
  list: 0,
  form: 1
};

export default {
  props: {
    id: { type: Number, default: 0 }
  },
  data() {
    return {
      VIEWMODEL: __viewModel,
      loading: false,
      viewModel: __viewModel.form,
      form: this.$form.createForm(this),
      // region table
      dataSet: [
        {
          id: 0,
          createDate: "20190101",
          commodityId: 1,
          title: "套餐1",
          price: "188",
          inventory: "99"
        },
        {
          id: 1,
          createDate: "20190102",
          commodityId: 1,
          title: "套餐2",
          price: "288",
          inventory: "99"
        },
        {
          id: 2,
          createDate: "20190103",
          commodityId: 1,
          title: "套餐3",
          price: "388",
          inventory: "99"
        },
        {
          id: 3,
          createDate: "20190104",
          commodityId: 1,
          title: "套餐4",
          price: "488",
          inventory: "99"
        }
      ],
      columns: [
        {
          dataIndex: "title",
          key: "title",
          title: "套餐"
        },
        {
          dataIndex: "price",
          key: "price",
          title: "价格（元）"
        },
        {
          dataIndex: "inventory",
          key: "inventory",
          title: "价格（件）"
        },
        {
          title: "操作",
          key: "action",
          scopedSlots: { customRender: "action" }
        }
      ]
      // endregion
    };
  },
  methods: {
    handleSubmit(e) {
      e.preventDefault();
      const self = this;
      this.form.validateFields((err, values) => {
        if (!!err) {
          return;
        }

        if (self.id <= 0) {
          self.$message.error("请先选择商品");
          return;
        }

        const url = this.apiUrl + "/backend/product/formula";
        let params = JSON.parse(JSON.stringify(values));
        params["id"] = self.id;

        self.loading = true;

        G.post(url, params)
          .cb(data => {
            if (data.status === 0) {
              self.$message.success("已保存");
              let temp = JSON.parse(JSON.stringify(self.dataSet));
              temp.push(params);
              self.dataSet = temp;
            }
          })
          .fcb(() => {
            self.loading = false;
          })
          .req();
      });
    },
    changeModel(viewModel) {
      this.viewModel = viewModel;
    }
  }
};
</script>