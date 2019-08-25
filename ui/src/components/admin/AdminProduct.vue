<template>
  <a-spin :spinning="loading">
    <div v-show="step === STEP.BASIC_INFO">
      <a-form refs="form" :form="basicForm">
        <!-- title -->
        <a-form-item label="名称" :label-col="{ span: 5 }" has-feedback :wrapper-col="{ span: 12 }">
          <a-input
            placeholder="请输入名称"
            v-decorator="[
                'title',
                {
                  rules: [
                   {required: true, message: '请输入名称！'}
                  ]
                }
              ]"
          ></a-input>
        </a-form-item>
        <!-- ./ title -->
        <!-- introduction -->
        <a-form-item
          label="一句话简介"
          :label-col="{ span: 5 }"
          has-feedback
          :wrapper-col="{ span: 12 }"
        >
          <a-input
            placeholder="请输入一句话简介"
            v-decorator="[
                'introduction'
              ]"
          ></a-input>
        </a-form-item>
        <!-- ./introduction -->
        <!-- thumbnail -->
        <a-form-item
          label="用于列表的缩略图"
          :label-col="{ span: 5 }"
          has-feedback
          :wrapper-col="{ span: 12 }"
        >
          <a-upload
            name="file"
            listType="picture-card"
            class="avatar-uploader"
            :showUploadList="false"
            :action="uploadUrl"
            :headers="headers"
            :beforeUpload="beforeUpload"
            @change="handleChange"
          >
            <img v-if="imageUrl" :src="imageUrl" alt="thumbnail" width="52px" height="52" />
            <div v-else>
              <a-icon :type="imageLoading ? 'loading' : 'plus'" />
              <div class="ant-upload-text">上传缩略图</div>
            </div>
          </a-upload>
        </a-form-item>
        <!-- thumbnail -->
        <a-form-item :wrapper-col="{ span: 12, offset: 5 }">
          <a-button type="primary" @click="next2Spec">提交</a-button>
        </a-form-item>
      </a-form>
    </div>

    <div v-show="step === STEP.SPECIFICATION">
        
    </div>
  </a-spin>
</template>
<script>
const _step_ = {
  BASIC_INFO: 0,
  SPECIFICATION: 1,
  IMAGES: 2,
  DETAILS: 3,
  FORMULAS: 4
};
function getBase64(img, callback) {
  const reader = new FileReader();
  reader.addEventListener("load", () => callback(reader.result));
  reader.readAsDataURL(img);
}
export default {
  props: {
    id: { type: Number, default: 0 }
  },
  data() {
    let auth = `ZShop ${this.$cookie.get("token")}`;
    return {
      STEP: _step_,
      loading: false,
      step: _step_.BASIC_INFO,
      basicForm: this.$form.createForm(this),
      uploadUrl: "",
      imageLoading: false,
      imageUrl: "",
      thumbnail: "",
      headers: { Authorization: auth },
      commodity: {
        id: 0,
        title: '',
        introduction: '',
        thumbnail: '',
        likeCount: 0,
        shareCount: 0,
        viewCount: 0,
        status: 0
      }
    };
  },
  created() {
    this.uploadUrl = `${this.apiUrl}/backend/file/image`;
  },
  mounted() {
    if (this.id > 0) {
      this.loadCommodity();
    }
  },
  methods: {
    loadCommodity() {
      const url = `${this.apiUrl}//backend/product/${this.id}`;
      const self = this;
      this.loading = true;
      G.get(url)
        .cb(data => {
          if (data.status === 0) {
            self.commodity = data.item;
          } else {
            self.$message.warn(data.message);
          }
        })
        .fcb(() => {
          self.loading = false;
        })
        .req();
    },
    beforeUpload(file) {
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
    handleChange(info) {
      if (info.file.status === "uploading") {
        this.loading = true;
        return;
      }
      if (info.file.status === "done") {
        console.log("handleChange ===>", info);
        let response = info.file.response;
        this.thumbnail = response.item;
        console.log(this.thumbnail);
        // Get this url from response in real world.
        getBase64(info.file.originFileObj, imageUrl => {
          this.imageUrl = imageUrl;
          this.loading = false;
        });
      }
    },
    next2Spec(e) {
      e.preventDefault();
      const url = `${this.apiUrl}/backend/product`;
      const self = this;
      this.form.validateFields((err, values) => {
        if (!err) {
            self.commodity.title = values["title"];
            self.commodity.introduction = values["introduction"];
            if (self.thumbnail !== '') {
                self.commodity.thumbnail = self.thumbnail;
            }
            self.loading = true;
            G.post(url, {body: self.commodity})
            .cb(data => {
                if (data.status === 0) {
                    self.$message.success('已保存');
                    self.step = _step_.SPECIFICATION;
                } else {
                    self.$message.warn(data.message);
                }
            })
            .fcb(() => self.loading = false)
            .req();
        }
      });
    }
  }
};
</script>
<style scoped>
</style>
