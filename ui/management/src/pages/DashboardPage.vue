<template>
  <div>
    <a-row :gutter="4">
      <a-col :span="4">
        <sidebar :index="['1']" />
      </a-col>
      <a-col :span="20">
        <a-card>
          <span slot="title">
            <a-badge :count="commentTotal">最新的评论</a-badge>
          </span>
          <a-row class="inner-row">
            <a-col :span="24">
              <a-spin :spinning="commentLoading">
                <a-table :columns="columns" :dataSource="comments" :pagination="false">
                  <span slot="action" slot-scope="record">
                    <a href="javascript:;" @click="passComment(record)">
                      <a-icon type="check"></a-icon>
                    </a>
                    <a-divider type="vertical"></a-divider>
                    <a href="javascript:;" @click="failComment(record)">
                      <a-icon type="close"></a-icon>
                    </a>
                  </span>
                </a-table>
              </a-spin>
            </a-col>
          </a-row>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>
<script>
const columns = [
  { title: "评价", dataIndex: "evaText" },
  { title: "sn", dataIndex: "sn" },
  { title: "账号", dataIndex: "nickname" },
  { title: "更新时间", dataIndex: "updateDate" },
  { title: "操作", scopedSlots: { customRender: "action" } },
];
export default {
  data() {
    return {
      columns,
      collapsed: false,
      comments: [],
      commentLoading: false,
      commentTotal: 0,
    };
  },
  mounted() {
    let reqeustCount = 30;
    this.loadComments();
    let interval = setInterval(() => {
      this.loadComments();
      reqeustCount--;
      if (reqeustCount <= 0) {
        clearInterval(interval);
      }
    }, 15000);
  },
  methods: {
    loadComments() {
      const url = `/backend/comments/new/top10`;
      this.commentLoading = true;
      this.$h
        .get(url)
        .cb((data) => {
          if (data.status === 0) {
            this.comments = data.list;
            let length = data.list.length;
            this.commentTotal = length > 9 ? "9+" : length;
          } else {
            this.$message.me(data.message);
          }
        })
        .fcb(() => (this.commentLoading = false))
        .req();
    },
    passComment(comment) {
      const url = `/backend/comment/pass?id=${comment.id}`;
      this.commentLoading = true;
      this.$h
        .post(url)
        .cb((data) => {
          if (data.status === 0) {
            this.$message.success("通过");
            this.loadComments();
          } else {
            this.$message.me(data.message);
          }
        })
        .fcb(() => (this.commentLoading = false))
        .req();
    },
    failComment(comment) {
      const url = `/backend/comment/fail?id=${comment.id}`;
      this.commentLoading = true;
      this.$h
        .post(url)
        .cb((data) => {
          if (data.status === 0) {
            this.$message.success("通过");
            this.loadComments();
          } else {
            this.$message.me(data.message);
          }
        })
        .fcb(() => (this.commentLoading = false))
        .req();
    },
  },
};
</script>