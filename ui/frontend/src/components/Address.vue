<template>
  <a-spin :spinning="loading">
    <div v-if="viewModel === 0">
      <a-button @click="changeViewMode(1)">
        <a-icon type="plus" />增加
      </a-button>
      <p>点击旗帜选择地址：</p>
      <a-list :dataSource="data">
        <a-list-item slot="renderItem" slot-scope="item">
          <span style="width:32px;">
            <template v-if="item.isDefault">
              <a-icon type="flag" style="color:#108ee9;cursor: pointer;" />
            </template>
            <template v-else>
              <a-icon @click="setDefaultEvent(item)" type="flag" style="cursor: pointer;" />
            </template>
          </span>
          <span style="width:220px;">{{item.contactName}}</span>
          <span style="width:120px;">{{item.contactPhone}}</span>
          <span style="width:600px">{{item.address}}</span>
          <span style="width:120px;">
            <a @click="edit(item)">
              <a-icon type="edit" />
            </a>
            <a-divider type="vertical" />
            <a-popconfirm title="删除" @confirm="confirmDelete(item)" okText="确定" cancelText="取消">
              <a href="javascript:;" style="color:#f5222d">
                <a-icon type="delete"></a-icon>
              </a>
            </a-popconfirm>
          </span>
        </a-list-item>
      </a-list>
    </div>

    <div v-if="viewModel === 1">
      <a-button @click="changeViewMode(0)">
        <a-icon type="rollback" />返回
      </a-button>
      <a-form>
        <a-form-item
          label="姓名"
          :validate-status="postName.status"
          :help="postName.help"
          has-feedback
          :label-col="{ span: 5 }"
          :wrapper-col="{ span: 5 }"
        >
          <a-input v-model="postName.value" />
        </a-form-item>
        <a-form-item
          label="手机号"
          :validate-status="postPhone.status"
          :help="postPhone.help"
          has-feedback
          :label-col="{ span: 5 }"
          :wrapper-col="{ span: 5 }"
        >
          <a-input v-model="postPhone.value" />
        </a-form-item>
        <a-form-item
          label="地址"
          :validate-status="postAddress.status"
          :help="postAddress.help"
          has-feedback
          :label-col="{ span: 5 }"
          :wrapper-col="{ span: 12 }"
        >
          <a-input v-model="postAddress.value" />
        </a-form-item>
        <a-form-item :wrapper-col="{ span: 12, offset: 5  }">
          <a-button @click="onSubmit" :loading="submitLoading">
            <a-icon type="save" />保存
          </a-button>
        </a-form-item>
      </a-form>
    </div>
  </a-spin>
</template>
<script>
const data = [];

export default {
  name: "Address",
  data() {
    return {
      viewModel: 0,
      data,
      loading: false,
      id: "0",
      postName: { status: "", help: "", value: "" },
      postPhone: { status: "", help: "", value: "" },
      postAddress: { status: "", help: "", value: "" },
      submitLoading: false
    };
  },
  mounted() {
    this.initAddress();
  },
  methods: {
    changeViewMode(model) {
      this.viewModel = model;
    },
    onSubmit() {
      console.log("on submit");
      let name = !!this.postName.value ? this.postName.value.trim() : null;
      if (name === null || name === "") {
        this.postName.status = "error";
        this.postName.help = "请填写收件人";
        return;
      }
      this.postName.status = "validating";
      this.postName.help = "";

      let phone = !!this.postPhone.value ? this.postPhone.value.trim() : null;
      if (phone === null || phone === "") {
        this.postPhone.status = "error";
        this.postPhone.help = "请填写手机号码";
        return;
      }
      let phoneReg = /1\d{10}/;
      if (!phoneReg.test(phone)) {
        this.postPhone.status = "error";
        this.postPhone.help = "手机号码格式不正确";
        return;
      }
      this.postPhone.status = "validating";
      this.postPhone.help = "";

      let address = !!this.postAddress.value
        ? this.postAddress.value.trim()
        : null;
      if (address === null || address === "") {
        this.postAddress.status = "error";
        this.postAddress.help = "请填写地址";
        return;
      }
      this.postAddress.status = "validating";
      this.postAddress.help = "";

      name = encodeURIComponent(name);
      phone = encodeURIComponent(phone);
      address = encodeURIComponent(address);
      const url = `${this.apiUrl}/frontend/address?id=${this.id}&pa=${name}&phone=${phone}&address=${address}`;
      this.submitLoading = true;
      this.$g
        .post(url, {})
        .cb(data => {
          if (data.status === 0) {
            this.initAddress();
            this.postName.value = "";
            this.postName.value = "";
            this.postAddress.value = "";
            this.viewModel = 0;
          }
        })
        .fcb(() => (this.submitLoading = false))
        .req();
    },
    initAddress() {
      const url = `${this.apiUrl}/frontend/addresses`;
      this.loading = true;
      this.$g
        .get(url)
        .cb(data => {
          if (data.status === 0) {
            this.data = data.list;
          } else {
            this.$message.me(data.message);
            console.error("init data error --->", data);
          }
        })
        .fcb(() => (this.loading = false))
        .req();
    },
    confirmDelete(item) {
      const url = `${this.apiUrl}/frontend/address/${item.id}`;
      this.$g
        .delete(url)
        .cb(data => {
          if (data.status === 0) {
            this.initAddress();
          } else {
            this.$message.me(data.message);
          }
        })
        .fcb()
        .req();
    },
    edit(item) {
      this.postName.value = item.contactName;
      this.postPhone.value = item.contactPhone;
      this.postAddress.value = item.address;
      this.id = item.id;
      this.viewModel = 1;
    },
    setDefaultEvent(item) {
      const url = `${this.apiUrl}/frontend/default-address`;
      const fd = new FormData();
      fd.append("id", item.id);
      this.$g
        .post(url, fd)
        .cb(data => {
          if (data.status === 0) {
            this.$emit('defaultAddressEvent', item);
            this.initAddress();
          } else {
            this.$message.me(data.message);
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

