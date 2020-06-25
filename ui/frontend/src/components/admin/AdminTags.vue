<template>
  <a-spin :spinning="loading">
    <a-row class="inner-row">
      <a-col :span="6">
        <template v-if="parentTag.id !== 0">
          <a-button shape="circle" icon="rollback" @click="gotoBack" />
          <a-divider type="vertical" />
        </template>
        <a-button shape="circle" icon="plus" @click="gotoForm" />
        <a-divider type="vertical" />
        {{parentTag.title}}
      </a-col>
    </a-row>

    <a-row class="inner-row">
      <a-col :span="6" v-for="(item, index) in dataSet" :key="index" style="margin:5px 0;">
        <a-button-group>
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
</template>
<script>
export default {
  props: {},
  data() {
    return {
      loading: false,
      dataSet: [],
      total: 0,
      page: 1,
      size: 20,
      order: "desc",
      orderBy: "id",
      stack: [],
      tag: {
        id: 0,
        title: "根节点",
        category: 0,
        tagOrder: 1,
        parentId: 0,
        childCount: 0
      },
      parentTag: {}
    };
  },
  mounted() {
    this.parentTag = G.copy(this.tag);
    this.gotoNext(this.tag);
  },
  methods: {
    refreshTags() {
      console.log(`refreshTags(${this.tag.id})`);
      this.loadByParentId(this.tag.id);
    },
    gotoForm() {
      this.$emit("gotoFormEvent");
    },
    loadByParentId(parentId) {
      let url = `${this.apiUrl}/backend/tags/${parentId}/${this.page}/${this.size}/${this.orderBy}/${this.order}`;
      const self = this;
      G.get(url)
        .cb(data => {
          if (data.status === 0) {
            self.total = data.total;
            self.dataSet = data.list;
          }
        })
        .fcb()
        .req();
    },
    deleteById(id) {
      let url = `${this.apiUrl}/backend/tag/${id}`;
      this.loading = true;
      const self = this;
      G.delete(url)
        .cb(data => {
          if (data.status === 0) {
            let _t = JSON.parse(JSON.stringify(self.dataSet));
            _t = _t.filter(_e => _e.id !== id);
            self.dataSet = _t;
            self.$message.success("已删除");
          } else if (data.status === 1) {
            self.$message.warn(data.message);
          }
        })
        .fcb(() => (self.loading = false))
        .req();
    },
    cancelDelete() {},
    confirmDelete(id) {
      this.deleteById(id);
    },
    gotoBack() {
      let _tag = this.stack.pop();
      this.parentTag = _tag;
      this.stack.forEach(x => console.log("pop this.stack", x.title));
      this.tag = G.copy(_tag);
      this.$emit("updateTagEvent", _tag);
      this.loadByParentId(_tag.id);
    },
    gotoNext(tag) {
      this.stack.push(G.copy(this.parentTag));
      this.stack.forEach(x => console.log("this.stack", x.title));
      this.parentTag = G.copy(tag);
      this.$emit("updateTagEvent", tag);
      this.loadByParentId(tag.id);
    }
  }
};
</script>
