<template>
  <div
    :style="{ background: '#fff', padding: '24px', minHeight: '280px', 'margin': '30px 0', 'width:': '100%','text-align':'left'}"
  >
    <h1>评价商品</h1>
    <a-form :form="form" @submit="handleSubmit">
      <a-form-item>
        <a-row :gutter="4">
          <a-col :span="4">
            <div class="fav-button active">
              <span>全选</span>
            </div>
          </a-col>
          <a-col :span="4" v-for="fav in favoriteList" :key="fav['id']">
            <div class="fav-button">
              <span>{{fav['text']}}({{fav['count']}})</span>
            </div>
          </a-col>
        </a-row>
      </a-form-item>
      <a-form-item label="购物体验" :label-col="{ span: 5 }" :wrapper-col="{ span: 12 }">
        <a-icon v-for="item in [1,2,3,4,5]" :key="item" type="star"/>
      </a-form-item>
      <a-form-item label="购物评价" :label-col="{ span: 5 }" :wrapper-col="{ span: 12 }">
        <a-textarea placeholder="评价" :rows="4"/>
      </a-form-item>
      <a-form-item :wrapper-col="{offset: 5, span: 12}">
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
      </a-form-item>
      <a-form-item :wrapper-col="{ span: 12, offset: 5 }">
        <a-button type="primary" html-type="submit">提交</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>
<script>
const favoriteList = [
  { id: 0, text: "值得拥有", count: "37210" },
  { id: 1, text: "手感很棒", count: "29874" },
  { id: 2, text: "外观漂亮", count: "22858" },
  { id: 3, text: "拍照好", count: "20008" },
  { id: 4, text: "速度快", count: "19874" }
];
export default {
  name: "CommonOrderPage",
  data() {
    return {
      formLayout: "horizontal",
      form: this.$form.createForm(this),
      favoriteList,
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
    handleSubmit(e) {
      e.preventDefault();
      this.form.validateFields((err, values) => {
        if (!err) {
          console.log("Received values of form: ", values);
        }
      });
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
    }
  }
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

