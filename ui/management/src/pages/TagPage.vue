<template>
  <div>
    <a-row :gutter="4">
      <a-col :span="4">
        <sidebar :index="['3']" />
      </a-col>
      <a-col :span="20" v-if="viewModel === 'form'">
        <a-row class="inner-row">
          <a-col :span="24">
            <h2>添加标签</h2>
          </a-col>
        </a-row>
        <a-row class="inner-row">
          <a-col :span="16">
            <a-button shape="circle" icon="rollback" @click="back2List" />
          </a-col>
        </a-row>
        <a-row class="inner-row">
          <a-col :span="16">
            <a-form>
              <a-form-item
                label="上级标签"
                :label-col="{ span: 5 }"
                :wrapper-col="{ span: 12 }"
              >{{parentTag.title}}</a-form-item>
              <a-form-item
                label="标签名称"
                :validate-status="tagText.status"
                :help="tagText.help"
                has-feedback
                :label-col="{ span: 5 }"
                :wrapper-col="{ span: 12 }"
              >
                <a-input placeholder="请输入标签" v-model="tagText.value"></a-input>
              </a-form-item>
              <a-form-item :wrapper-col="{ offset: 5, span: 12 }">
                <a-button icon="save" @click="save" :loading="saveLoading">保存</a-button>
              </a-form-item>
            </a-form>
          </a-col>
        </a-row>
      </a-col>
      <a-col :span="20" v-if="viewModel === 'list'">
        <a-row class="inner-row">
          <a-col :span="24">
            <h2>标签列表</h2>
          </a-col>
        </a-row>
        <a-row class="inner-row">
          <a-col :span="16">
            <a-col :span="6">
              <template v-if="parentTag.id !== 0">
                <a-button shape="circle" icon="rollback" @click="gotoBack" />
                <a-divider type="vertical" />
              </template>
              <a-button shape="circle" icon="plus" @click="back2Form" />
              <a-divider type="vertical" />
              {{parentTag.title}}
            </a-col>
          </a-col>
        </a-row>
        <a-spin :spinning="listLoading">
          <a-row class="inner-row">
            <a-col :span="24">
              <a-button-group
                v-for="(item, index) in dataSet"
                :key="index"
                style="margin:5px 10px;"
              >
                <a-button @click="gotoNext(item)">
                  {{item.title}}
                  <a-divider type="vertical" />
                  <a-icon type="right" />
                </a-button>
                <a-popconfirm
                  title="确认删除？"
                  @confirm="confirmDelete(item.id)"
                  @cancel="cancelDelete"
                  okText="是"
                  cancelText="否"
                >
                  <a-button type="danger">
                    <a-icon type="delete" />
                  </a-button>
                </a-popconfirm>
              </a-button-group>
            </a-col>
          </a-row>
        </a-spin>
      </a-col>
    </a-row>
  </div>
</template>
<script>
export default {
  data() {
    return {
      viewModel: "list",
      parentTag: { title: "根节点", id: 0 },
      tagText: { value: "", help: "", status: "" },
      saveLoading: false,
      page: 1,
      size: 50,
      orderBy: "id",
      order: "desc",
      dataSet: [],
      total: 0,
      listLoading: false,
      stack: []
    };
  },
  mounted() {
    this.loadTags(0);
  },
  methods: {
    back2List() {
      this.viewModel = "list";
      this.loadTags(this.parentTag.id);
    },
    back2Form() {
      this.viewModel = "form";
      this.tagText.value = "";
      this.tagText.status = "";
      this.tagText.help = "";
    },
    gotoBack(tag) {
      let _tag = this.stack.pop();
      this.parentTag = _tag;
      this.stack.forEach(x => console.log("pop this.stack", x.title));
      this.tag = JSON.parse(JSON.stringify(_tag));
      this.loadTags(_tag.id);
    },
    save() {
      let text = this.tagText.value.trim();
      if (text === "") {
        this.tagText.status = "warning";
        this.tagText.help = "请输入标签";
        return;
      }
      this.tagText.status = "validating";
      this.tagText.help = "";
      const url = `/backend/tag`;
      this.saveLoading = true;
      let param = {
        id: 0,
        title: text,
        tagOrder: 0,
        parentId: this.parentTag.id
      };
      this.$h
        .post(url, {
          body: param
        })
        .cb(data => {
          if (data.status === 0) {
            this.tagText.status = "success";
          } else {
            this.tagText.status = "error";
            this.tagText.help = data.message;
          }
        })
        .fcb(() => {
          this.saveLoading = false;
        })
        .req();
    },
    loadTags(parentId) {
      let url = `/backend/tags/${parentId}/${this.page}/${this.size}/${this.orderBy}/${this.order}`;
      this.listLoading = true;
      this.$h
        .get(url)
        .cb(data => {
          if (data.status === 0) {
            this.total = data.total;
            this.dataSet = data.list;
            console.log("data set --->", this.dataSet);
          } else {
            this.$message.error(data.message);
          }
        })
        .fcb(() => (this.listLoading = false))
        .req();
    },
    gotoNext(tag) {
      this.stack.push(JSON.parse(JSON.stringify(this.parentTag)));
      this.stack.forEach(x => console.log("this.stack", x.title));
      this.parentTag = JSON.parse(JSON.stringify(tag));
      this.loadTags(tag.id);
    },
    confirmDelete(id) {
      this.deleteById(id);
    },
    cancelDelete() {},
    deleteById(id) {
      let url = `/backend/tag/${id}`;
      this.listLoading = true;
      this.$h
        .delete(url)
        .cb(data => {
          if (data.status === 0) {
            let _t = JSON.parse(JSON.stringify(this.dataSet));
            _t = _t.filter(_e => _e.id !== id);
            this.dataSet = _t;
            this.$message.success("已删除");
          } else if (data.status === 1) {
            this.$message.warn(data.message);
          }
        })
        .fcb(() => (this.listLoading = false))
        .req();
    }
  }
};
</script>