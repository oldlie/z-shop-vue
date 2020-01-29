<template>
  <a-spin :spinning="loading">
    <div class="main-panel">
      <a-row>
        <a-col :span="4">
          <admin-menu :activeIndex="'sub3'" :openKey="'sub3'"></admin-menu>
        </a-col>

        <a-col :span="20">
          <a-row class="inner-row" v-if="view === 'form'">
            <a-row class="inner-row">
              <a-col :span="24">
                <a-button @click="showArticles" icon="table" type="link">显示列表</a-button>
                <a-divider type="vertical"></a-divider>
              </a-col>
            </a-row>

            <admin-article :articleId="articleId"></admin-article>
          </a-row>

          <a-row class="inner-row" v-if="view === 'table'">
            <a-row class="inner-row">
              <a-col :span="24">
                <a-button @click="addArticle('form')" icon="plus" type="link">写文章</a-button>
                <a-divider type="vertical"></a-divider>
              </a-col>
            </a-row>

            <a-table :columns="columns" :dataSource="list" :pagination="false">
              <span slot="action" slot-scope="record">
                <a href="javascript:;" @click="editArticle(record)">
                  <a-icon type="edit"></a-icon>
                </a>
                <a-divider type="vertical"></a-divider>
                <a-popconfirm
                  title="删除"
                  @confirm="confirmDeleteArticle(record)"
                  @cancel="cancelDeleteArticle"
                  okText="确定"
                  cancelText="取消"
                >
                  <a href="javascript:;" style="color:#f5222d">
                    <a-icon type="delete"></a-icon>
                  </a>
                </a-popconfirm>
              </span>
            </a-table>
          </a-row>

          <a-row class="inner-row">
            <a-pagination
              size="small"
              :total="total"
              :pageSize="size"
              :current="index"
              @change="paginationChange"
            />
          </a-row>
        </a-col>
      </a-row>
    </div>
  </a-spin>
</template>
<script>
const _view = {
  table: "table",
  form: "form"
};

const columns = [
  { title: "标题", dataIndex: "title" },
  { title: "摘要", dataIndex: "summary" },
  { title: "作者", dataIndex: "author" },
  { title: "发布", dataIndex: "publisher" },
  { title: "时间", dataIndex: "createDate" },
  { title: "操作", scopedSlots: { customRender: "action" } }
];

export default {
  data() {
    return {
      loading: false,
      view: _view.table,
      articleId: 0,
      columns: columns,
      index: 1,
      size: 10,
      total: 0,
      orderBy: "id",
      order: "desc",
      list: []
      // region Tag
      // endregion
    };
  },
  mounted() {
    this.loadArticles();
  },
  methods: {
    loadArticles() {
      const url = `${this.apiUrl}/backend/articles/${this.index}-${this.size}-${this.orderBy}-${this.order}`;
      G.get(url)
        .cb(data => {
          if (data.status === 0) {
            console.log(data);
            this.list = data.list;
            this.total = data.total;
          } else {
            console.error(data);
          }
        })
        .fcb()
        .req();
    },
    paginationChange(page, pageSize) {
      this.index = page;
      this.loadArticles();
    },
    confirmDeleteArticle(record) {
      const url = `${this.apiUrl}/backend/article/${record.id}`;
      console.log("confirm delete article=>", url);
      this.loading = true;
      G.delete(url)
        .cb(data => {
          if (data.status === 0) {
            this.list = this.list.filter(x => x.id !== record.id);
            this.$message.success("已保存");
          } else {
            console.error(data);
          }
        })
        .fcb(() => (this.loading = false))
        .req();
    },
    cancelDeleteArticle() {},
    addArticle() {
      this.articleId = 0;
      this.view = _view.form;
    },
    showArticles() {
      this.articleId = 0;
      this.view = _view.table;
    },
    editArticle(record) {
        this.articleId = record.id;
        this.view = _view.form;
    }
  }
};
</script>