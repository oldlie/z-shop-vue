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
        <a-input
          id="name"
          placeholder="请输入商品名称，回车保存或创建"
          @pressEnter="saveTitle"
          v-model="name.value"
        />
      </a-form-item>

      <template v-if="commodity.id > 0">
        <a-form-item
          :label-col="labelCol"
          :wrapper-col="wrapperCol"
          label="简要描述"
          :validate-status="summary.status"
          :help="summary.help"
          has-feedback
        >
          <a-input placeholder="请输入简要描述" @pressEnter="saveSummary" v-model="summary.value" />
        </a-form-item>

        <a-form-item
          :label-col="labelCol"
          :wrapper-col="wrapperCol"
          label="首页图"
          :validate-status="thumbnail.status"
          has-feedback
        >
          <a-upload
            name="file"
            listType="picture-card"
            class="avatar-uploader"
            :showUploadList="false"
            :action="uploadUrl"
            :headers="headers"
            :beforeUpload="beforeThumbnailUpload"
            @change="handleThumbnailChange"
          >
            <img v-if="thumbnail" :src="thumbnail.value" alt="上传首图" width="52px" height="52px" />
            <div v-else>
              <a-icon :type="thumbnailLoading ? 'loading' : 'plus'" />
              <div class="ant-upload-text">上传缩略图</div>
            </div>
          </a-upload>
        </a-form-item>

        <a-form-item :label-col="labelCol" :wrapper-col="wideWrapperCol" label="规格">
          <a-row class="innter-row" :gutter="8">
            <a-col :span="16">
              <a-table :columns="columns"></a-table>
            </a-col>
            <a-col :span="8">
              <a-form>
                <a-form-item>
                  <a-input placeholder="输入规格名称"></a-input>
                </a-form-item>
                <a-form-item>
                  <a-input placeholder="输入规格"></a-input>
                </a-form-item>
                <a-form-item>
                  <a-button>添加规格</a-button>
                </a-form-item>
              </a-form>
            </a-col>
          </a-row>
        </a-form-item>
      </template>
    </a-form>
  </a-spin>
</template>
<script>
const _step_ = {
  BASIC_INFO: 0,
  SPECIFICATION: 1,
  FORMULAS: 2
};

const specColumns = [
  { title: "规格名", key: "title" },
  { title: "内容", key: "content" },
  { title: "Action", scopedSlots: { customRender: "action" } }
];

function getBase64(img, callback) {
  const reader = new FileReader();
  reader.addEventListener("load", () => callback(reader.result));
  reader.readAsDataURL(img);
}

export default {
  props: {
    commodity: { type: Object, default: { id: 0 } }
  },
  data() {
    const auth = `ZShop ${this.$cookie.get("token")}`;
    return {
      loading: false,
      labelCol: {
        xs: { span: 24 },
        sm: { span: 3 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 12 }
      },
      wideWrapperCol: {
        sm: { span: 21 }
      },
      headers: { Authorization: auth },
      // commodity: {
      //   id: 0,
      //   title: "",
      //   introduction: "",
      //   thumbnail: "",
      //   likeCount: 0,
      //   shareCount: 0,
      //   viewCount: 0,
      //   status: 0
      // },
      // region commodity fields
      name: { status: "", help: "", value: "" },
      summary: { status: "", help: "", value: "" },
      thumbnail: { status: "", help: "", value: "" },
      thumbnailLoading: false,
      uploadUrl: `${this.apiUrl}/backend/file/upload`,
      // endregion
      // region specification
      columns: specColumns
      // endregion
    };
  },
  created() {},
  mounted() {
    console.log("input commodity=>", this.commodity);
    this.innerId = this.commodity.id;
    this.name.value = this.commodity.title;
    this.summary.value = this.commodity.introduction;
  },
  methods: {
    gotoSpec(id) {
      this.innerId = id;
      this.step = _step_.SPECIFICATION;
    },
    saveTitle() {
      console.log("nameChange");
      if (!this.name.value) {
        this.name.status = G._status.error;
        this.name.help = "请填写商品名称";
        return;
      }
      let name = this.name.value.trim();
      if (name === "") {
        this.name.status = G._status.error;
        this.name.help = "请填写商品名称";
        return;
      }
      this.name.status = "";
      this.name.help = "";
      this.commodity.title = this.name.value;
      const self = this;
      this.name.status = G._status.validating;
      this.saveCommodity(data => {
        if (data.status === 0) {
          self.commodity.id = data.item;
          self.name.status = "success";
        } else {
          self.name.status = "error";
          self.name.help = data.message;
        }
      });
    },
    saveSummary() {
      if (!this.summary.value) {
        this.summary.status = "warning";
        this.summary.help = "请填写描述";
        return;
      }
      let val = this.summary.value.trim();
      if (val === "") {
        this.summary.status = "warning";
        this.summary.help = "请填写描述，不用使用空格替代";
        return;
      }
      this.summary.status = "";
      this.summary.help = "";
      this.commodity.introduction = this.summary.value;
      const self = this;
      this.summary.status = G._status.validating;
      this.saveCommodity(data => {
        self.summary.status =
          data.status === 0 ? G._status.success : G._status.error;
        if (self.summary.status === G._status.error) {
          self.summary.help = data.message;
        }
      });
    },
    beforeThumbnailUpload(file) {
      const isJPG = file.type === "image/jpeg";
      if (!isJPG) {
        this.$message.error("仅能上传JPG文件!");
      }
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        this.$message.error("最大上传2M的文件!");
      }
      return isJPG && isLt2M;
    },
    handleThumbnailChange(info) {
      if (info.file.status === "uploading") {
        this.thumbnailLoading = true;
        return;
      }
      if (info.file.status === "done") {
        console.log("handleChange ===>", info);
        let response = info.file.response;
        if (!!response["data"]) {
          this.thumbnail.value = response["data"][0];
        }

        console.log(this.thumbnail);
        // Get this url from response in real world.
        getBase64(info.file.originFileObj, imageUrl => {
          this.thumbnail.value = imageUrl;
        });

        this.commodity.thumbnail = this.thumbnail.value;
        this.thumbnail.status = G._status.validating;
        this.saveCommodity(data => {
          if (data.status === 0) {
            this.thumbnail.status = G._status.success;
          } else {
            this.thumbnail.status = G._status.error;
          }
        });
        this.thumbnailLoading = false;
      }
    },
    saveCommodity(callback) {
      const url = `${this.apiUrl}/backend/product`;
      const self = this;
      G.post(url, this.commodity)
        .cb(data => {
          callback(data);
        })
        .fcb()
        .req();
    }
  }
};
</script>
<style scoped>
</style>
