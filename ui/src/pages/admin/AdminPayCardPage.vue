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
          <admin-pay-card></admin-pay-card>
        </a-row>

        <a-row class="inner-row" v-if="view === 'table'">
          <a-col :span="24">
            <a-button @click="changeView('form')" icon="plus" type="link">添加卡</a-button>
          </a-col>
        </a-row>

        <a-row class="inner-row" v-if="view === 'preview'">
          <a-col :span="24">
            <a-button @click="changeView('table')" icon="table" type="link">礼品卡列表</a-button>
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
  {title: '卡名称', dataIndex: 'title'},
  {title: '备注', dataIndex: 'node'},
  {title: '面值', dataIndex: 'denomination'},
  {title: '售价', dataIndex: 'amount'},
  {title: '有效期', dataIndex: 'validDay'},
  {title: '制卡日期', dataIndex: 'createDate'},
  {title: '制卡日期', dataIndex: 'createDate'},
];

export default {
  data() {
    return {
      view: _view.form
    };
  },
  methods: {
    changeView(view) {
      this.view = view;
    }
  }
};
</script>

<style>
</style>