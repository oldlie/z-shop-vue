<template>
  <div
    :style="{ background: '#fff', padding: '24px', minHeight: '280px', 'margin': '30px 0', 'width:': '100%','text-align':'left'}"
  >
    <h1>评价商品</h1>

    <a-row class="inner-row" v-for="commodity in info.commodities" :key="commodity.cid">
      <a-col :span="24">
        <a-row class="inner-row">
          <a-col :span="16">{{commodity.commodity}}</a-col>
        </a-row>

        <a-row class="inner-row" :gutter="6">
          <a-col :span="4" style="text-align:right;">快速评价：</a-col>
          <a-col :span="4" v-for="dim in commodity.dimensions" :key="dim.id">
            <div
              @click="quickCommentCheck(dim, false)"
              v-if="dim['checked']"
              class="fav-button active"
            >
              <span>{{dim['title']}}</span>
            </div>
            <div @click="quickCommentCheck(dim, true)" v-else class="fav-button">
              <span>{{dim['title']}}</span>
            </div>
          </a-col>
        </a-row>

        <a-row class="inner-row">
          <a-col :span="24">
            <a-form>
              <a-form-item label="购物体验" :label-col="{ span: 5 }" :wrapper-col="{ span: 12 }">
                <a-rate v-model="commodity['rate']" />
              </a-form-item>
              <a-form-item label="购物评价" :label-col="{ span: 5 }" :wrapper-col="{ span: 12 }">
                <a-textarea v-model="commodity['comment']" placeholder="评价" :rows="4" />
              </a-form-item>
              <a-form-item :wrapper-col="{ span: 12, offset: 5 }">
                <a-button type="primary" @click="submit(commodity)" :loading="submitLoading">提交</a-button>
              </a-form-item>
            </a-form>
          </a-col>
        </a-row>
      </a-col>
    </a-row>
  </div>
</template>
<script>
export default {
  name: "CommonOrderPage",
  data() {
    return {
      formLayout: "horizontal",
      fileList: [
        {
          uid: "-1",
          name: "xxx.png",
          status: "done",
          url:
            "https://zos.alipayobjects.com/rmsportal/jkjgkEfvpUPVyRjUImniVslZfWPnJuuZ.png",
        },
      ],
      sn: "",
      info: {},
      submitLoading: false,
    };
  },
  mounted() {
    console.log("router --->", this.$route.query.sn);
    this.sn = this.$route.query.sn;
    this.loadOrder();
  },
  methods: {
    loadOrder() {
      const url = `${this.apiUrl}frontend/shopping-order/comment-info?sn=${this.sn}`;
      this.$g
        .get(url)
        .cb((data) => {
          console.log("data --->", data);
          if (data.status === 0) {
            this.info = data.item;
          } else {
            this.$message.me(data.message);
          }
        })
        .fcb()
        .req();
    },
    submit(commodity) {
      const url = `${this.apiUrl}frontend/shopping-order/comment`;
      let ids = "";
      for (let i in commodity.dimensions) {
        console.log(i);
        let dim = commodity.dimensions[i];
        if (dim["checked"]) {
          ids += dim["id"] + ",";
        }
      }
      const fd = new FormData();
      fd.append("cid", commodity.cid);
      fd.append("sn", this.sn);
      fd.append("rate", !commodity["rate"] ? commodity["rate"] : 5);
      fd.append("ids", ids);
      fd.append("comment", commodity["comment"]);
      this.submitLoading = true;
      this.$g
        .post(url, fd)
        .cb((data) => {
          if (data.status === 0) {
            this.$message.success("已保存");
          } else {
            this.$message.me(data.message);
          }
        })
        .fcb(() => (this.submitLoading = false))
        .req();
    },
    handleSelectChange(value) {
      console.log(value);
    },
    handleCancel() {
      this.previewVisible = false;
    },
    handlePreview(file) {
      this.previewImage = file.url || file.thumbUrl;
      this.previewVisible = true;
    },
    handleChange({ fileList }) {
      this.fileList = fileList;
    },
    quickCommentCheck(item, checked) {
      item["checked"] = checked;
      this.info = JSON.parse(JSON.stringify(this.info));
    },
  },
};
</script>
<style scoped>
.fav-button {
  cursor: pointer;
  width: 100%;
  color: #ff6700;
  border: 1px solid #ff6700;
  padding: 12px 0 10px 0;
  text-align: center;
}
.fav-button:hover {
  color: #ffffff;
  background: #ff6700;
}
.fav-button.active {
  color: #ffffff;
  background: #ff6700;
}
</style>

