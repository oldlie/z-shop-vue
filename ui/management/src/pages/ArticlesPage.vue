<template>
  <div>
    <a-row :gutter="4">
      <a-col :span="4">
        <sidebar :index="['5']" />
      </a-col>
      <a-col :span="20">
        <a-row class="inner-row">
          <a-col :span="24">
            <h2>文章列表</h2>
          </a-col>
        </a-row>
        <a-row class="inner-row" v-if="view === 'form'">
          <a-row class="inner-row">
            <a-col :span="24">
              <a-button @click="showArticles" icon="table" type="link">显示列表</a-button>
            </a-col>
          </a-row>

          <admin-article :articleId="articleId"></admin-article>
        </a-row>

        <a-row class="inner-row">
          <a-col :span="24">
            <a-button @click="addArticle('form')" icon="plus" type="link">写文章</a-button>
            <a-divider type="vertical"></a-divider>
          </a-col>
        </a-row>

        <a-spin :spinning="loading">
          <a-table :columns="columns" :dataSource="list" :pagination="false">
            <span slot="title" slot-scope="record">
              <div
                style="width:240px; white-space: nowrap;  overflow: hidden;  text-overflow: ellipsis;"
              >{{record.title}}</div>
            </span>
            <span slot="summary" slot-scope="record">
              <div
                style="width:240px; white-space: nowrap;  overflow: hidden;  text-overflow: ellipsis;"
              >{{record.summary}}</div>
            </span>
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
        </a-spin>

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
</template>
<script>
const _view = {
  table: "table",
  form: "form"
};

const columns = [
  { title: "标题", width: "240px", scopedSlots: { customRender: "title" } },
  { title: "摘要", width: "240px", scopedSlots: { customRender: "summary" } },
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
    };
  },
  mounted() {
    this.loadArticles();
  },
  methods: {
    loadArticles() {
      const url = `/backend/articles/${this.index}-${this.size}-${this.orderBy}-${this.order}`;
      this.loading = true;
      this.$h
        .get(url)
        .cb(data => {
          if (data.status === 0) {
            console.log(data);
            this.list = data.list;
            this.total = data.total;
          } else {
            console.error(decodeURIComponent(data));
          }
        })
        .fcb(() => {
          this.loading = false;
        })
        .req();
    },
    paginationChange(page, pageSize) {
      this.index = page;
      this.loadArticles();
    },
    confirmDeleteArticle(record) {
      const url = `/backend/article/${record.id}`;
      console.log("confirm delete article=>", url);
      this.loading = true;
      this.$h
        .delete(url)
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
      this.$router.push("/article/0");
    },
    showArticles() {
      this.articleId = 0;
      this.view = _view.table;
      this.loadArticles();
    },
    editArticle(record) {
      this.$router.push(`/article/${record.id}`);
    }
  }
};
</script>