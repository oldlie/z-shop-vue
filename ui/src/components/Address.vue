<template>
  <div>
    <div v-if="viewModel === 0">
      <a-button @click="changeViewMode(1)">
        <a-icon type="plus"/>增加
      </a-button>
      <a-list :dataSource="data">
        <a-list-item slot="renderItem" slot-scope="item">
          <span style="width:32px;">
            <template v-if="item.isDefault">
              <a-icon type="flag" style="color:#108ee9;cursor: pointer;"/>
            </template>
            <template v-else>
              <a-icon type="flag" style="cursor: pointer;"/>
            </template>
          </span>
          <span style="width:220px;">
            {{item.name}}
          </span>
          <span style="width:120px;">{{item.phone}}</span>
          <span style="width:600px">{{item.address}}</span>
          <span style="width:120px;">
            <a>
              <a-icon type="edit"/>
            </a>
            <a-divider type="vertical"/>
            <a style="color:#f50">
              <a-icon type="delete"/>
            </a>
          </span>
        </a-list-item>
      </a-list>
    </div>

    <div v-if="viewModel === 1">
      <a-button @click="changeViewMode(0)">
        <a-icon type="rollback"/>返回
      </a-button>
      <a-form :form="form" @submit="onSubmit">
        <a-form-item label="姓名" :label-col="{ span: 5 }" :wrapper-col="{ span: 5 }">
          <a-input/>
        </a-form-item>
        <a-form-item label="手机号" :label-col="{ span: 5 }" :wrapper-col="{ span: 5 }">
          <a-input/>
        </a-form-item>
        <a-form-item label="地址" :label-col="{ span: 5 }" :wrapper-col="{ span: 12 }">
          <a-input/>
        </a-form-item>
        <a-form-item :wrapper-col="{ span: 12, offset: 5  }">
          <a-button>
            <a-icon type="save"/>保存
          </a-button>
        </a-form-item>
      </a-form>
    </div>
  </div>
</template>
<script>
const data = [
  {
    id: 0,
    name: "赵钱孙李先生",
    phone: "13412345678",
    address: "北京市 北京市 海淀区 北外太平庄50号",
    isDefault: true
  },
  {
    id: 0,
    name: "陈生",
    phone: "13412345678",
    address: "北京市 北京市 海淀区 北外太平庄50号",
    isDefault: false
  },
  {
    id: 0,
    name: "陈生",
    phone: "13412345678",
    address: "北京市 北京市 海淀区 北外太平庄50号",
    isDefault: false
  }
];

export default {
  name: "Address",
  data() {
    return {
      viewModel: 0,
      data
    };
  },
  methods: {
    changeViewMode(model) {
      this.viewModel = model;
    },
    onSubmit() {}
  }
};
</script>
<style scoped>
</style>

