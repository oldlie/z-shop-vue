<template>
  <div
    :style="{ background: '#fff', padding: '24px', minHeight: '280px', 'margin': '30px 0', 'width:': '100%','text-align':'left'}"
  >
    <h1>申请售后</h1>
    <a-row>
      <a-col :span="24" class="inner-row">
        <p>订单号：</p>201904230001
      </a-col>
      <a-col :span="16" class="inner-row">
        <a-textarea placeholder="售后原因" :rows="4"/>
      </a-col>
      <a-col :span="16" class="inner-row">
        <a-upload
          action="//jsonplaceholder.typicode.com/posts/"
          listType="picture-card"
          :fileList="fileList"
          @preview="handlePreview"
          @change="handleChange"
        >
          <div v-if="fileList.length < 3">
            <a-icon type="plus"/>
            <div class="ant-upload-text">Upload</div>
          </div>
        </a-upload>
        <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
          <img alt="example" style="width: 100%" :src="previewImage">
        </a-modal>
      </a-col>
      <a-col :span="16">
          <a-button>提交申请</a-button>
      </a-col>
    </a-row>
  </div>
</template>
<script>
export default {
  data() {
    return {
      previewVisible: false,
      previewImage: "",
      fileList: [
        {
          uid: "-1",
          name: "xxx.png",
          status: "done",
          url:
            "https://zos.alipayobjects.com/rmsportal/jkjgkEfvpUPVyRjUImniVslZfWPnJuuZ.png"
        }
      ]
    };
  },
  methods: {
    handleCancel() {
      this.previewVisible = false;
    },
    handlePreview(file) {
      this.previewImage = file.url || file.thumbUrl;
      this.previewVisible = true;
    },
    handleChange({ fileList }) {
      this.fileList = fileList;
    }
  }
};
</script>
<style scoped>
/* you can make up upload button and sample style by using stylesheets */
.ant-upload-select-picture-card i {
  font-size: 32px;
  color: #999;
}

.ant-upload-select-picture-card .ant-upload-text {
  margin-top: 8px;
  color: #666;
}
.inner-row{
    margin: 10px 0;
    padding: 10px 0;
}
</style>

