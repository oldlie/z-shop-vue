<template>
  <a-spin :spinning="loading">
    <a-form>
      <a-form-item
        :label-col="labelCol"
        :wrapper-col="wrapperCol"
        label="商品名称"
        :validate-status="name.status"
        :help="name.help"
        has-feedback
      >
        <a-input id="name" placeholder="请输入商品名称" @pressEnter="saveTitle" v-model="name.value" />
      </a-form-item>
    </a-form>
  </a-spin>
</template>
<script>
const _step_ = {
  BASIC_INFO: 0,
  SPECIFICATION: 1,
  FORMULAS: 2
};

export default {
  props: {
    id: { type: Number, default: 0 }
  },
  data() {
    return {
      loading: false,
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 12 }
      },
      commodity: {
        id: 0,
        title: "",
        introduction: "",
        thumbnail: "",
        likeCount: 0,
        shareCount: 0,
        viewCount: 0,
        status: 0
      },
      name: {
        status: "",
        help: "",
        value: ""
      }
    };
  },
  created() {},
  mounted() {
    this.innerId = JSON.parse(JSON.stringify(this.id));
  },
  methods: {
    gotoSpec(id) {
      this.innerId = id;
      this.step = _step_.SPECIFICATION;
    },
    saveTitle() {
      console.log("nameChange");
      if (!this.name.value) {
        this.name.status = "error";
        this.name.help = "请填写商品名称";
        return;
      }
      let name = this.name.value.trim();
      if (name === "") {
        this.name.status = "error";
        this.name.help = "请填写商品名称";
        return;
      }
      this.name.status = "";
      this.name.help = "";
      this.commodity.title = this.name.value;
      this.saveCommodity();
    },
    saveCommodity() {
      const url = `${this.apiUrl}/backend/product`;
      const self = this;
      this.name.status = 'validating';
      G.post(url, this.commodity)
        .cb(data => {
          if (data.status === 0) {
            self.commodity.id = data.item;
            self.name.status = "success";
          } else {
            self.name.status = "error";
            self.name.help = data.message;
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
