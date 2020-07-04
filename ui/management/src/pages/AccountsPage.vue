<template>
  <div>
    <a-row :gutter="4">
      <a-col :span="4">
        <sidebar :index="['7']" />
      </a-col>
      <a-col :span="20">
        <a-row class="inner-row">
          <a-col :span="24">
            <h2>账户管理</h2>
          </a-col>
        </a-row>
        <a-row class="inner-row">
          <a-col :span="24">
            <router-link :to="'/account/0'">
              <a-icon type="plus" /> 添加账号
            </router-link>
            <a-divider type="vertical" />
          </a-col>
        </a-row>
        <a-row class="inner-row">
          <a-col :span="24">
            <a-spin :spinning="loading">
              <a-table :columns="columns" :dataSource="list" :pagination="false">
                <span slot="role" slot-scope="record">
                  <a-tag v-for="role in record.roles" :key="role.id">{{role.title}}</a-tag>
                </span>
                <span slot="action" slot-scope="record">
                  <!--template v-if="record.username !== 'admin'">
                    <router-link :to="'/account/' + record.id"><a-icon type="edit"></a-icon>编辑</router-link>
                    <a-divider type="vertical"></a-divider>
                  </template-->
                  <a href="javascript:;" @click="resetPayPwd(record)">
                    <a-icon type="property-safety" />支付密码
                  </a>
                  <!--
                  <a-divider type="vertical"></a-divider>
                  <a v-if="record.isAccountNonLocked" href="javascript:;" @click="lock(record)">
                    <a-icon type="lock"></a-icon>解锁
                  </a>
                  <a v-else href="javascript:;" @click="unlock(record)">
                    <a-icon type="unlock"></a-icon>锁定
                  </a>
                  -->
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
              :current="page"
              @change="paginationChange"
            />
          </a-col>
        </a-row>
      </a-col>
    </a-row>

    <a-modal v-model="visible" title="重置支付密码" @ok="doResetPayPwd">
      <a-form>
        <a-form-item
          :label-col="{'span': '6'}"
          :wrapper-col="{'span': '16'}"
          label="支付密码"
          :validate-status="payPassword.status"
          :help="payPassword.help"
          has-feedback
        >
          <a-input placeholder="支付密码" v-model="payPassword.value"></a-input>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>
<script>
const columns = [
  { title: "账号", width: "240px", dataIndex: "username" },
  { title: "昵称", width: "240px", dataIndex: "nickname" },
  { title: "角色", scopedSlots: { customRender: "role" } },
  { title: "操作", scopedSlots: { customRender: "action" } }
];
export default {
  data() {
    return {
      list: [],
      loading: false,
      columns,
      total: 0,
      page: 1,
      size: 10,
      orderBy: "id",
      direct: "desc",
      key: "",
      value: "",
      userId: "",
      // region reset pay password
      visible: false,
      resetPayPwdLoading: false,
      payPassword: { value: "", status: "", help: "" }
      // endregion
    };
  },
  mounted() {
    this.load();
  },
  methods: {
    load() {
      const url = `/backend/accounts?page=${this.page}&size=${this.size}&orderBy=${this.orderBy}&direct=${this.direct}`;
      this.loading = true;
      this.$h
        .get(url)
        .cb(data => {
          if (data["status"] === 0) {
            this.list = data["list"];
            this.total = data["total"];
          } else {
            this.$message.error(decodeURIComponent(data["message"]));
          }
        })
        .fcb(() => (this.loading = false))
        .req();
    },
    edit() {},
    lock() {},
    unlock() {},
    resetPayPwd(item) {
      this.userId = item.id;
      this.payPassword.value = item.payPassword;
      this.payPassword.status = "";
      this.payPassword.help = "";
      this.visible = true;
    },
    doResetPayPwd() {
      const url = `/backend/pay-password`;
      const reg = /^\d{6}$/;
      if (!reg.test(this.payPassword.value)) {
        this.payPassword.status = "error";
        this.payPassword.help = "支付密码为6位数字";
        return;
      }
      this.payPassword.status = "validating";
      this.payPassword.help = "";
      this.resetPayPwdLoading = true;
      let fd = new FormData();
      fd.append("uid", this.userId);
      fd.append("pwd", this.payPassword.value);
      this.$h
        .post(url, fd)
        .cb(data => {
          if (data["status"] === 0) {
            this.payPassword.status = "success";
          } else {
            this.payPassword.status = "error";
            this.payPassword.help = decodeURIComponent(data["message"]);
          }
        })
        .fcb(() => (this.resetPayPwdLoading = false))
        .req();
    },
    paginationChange(page) {
      this.page = page;
      this.load();
    }
  }
};
</script>