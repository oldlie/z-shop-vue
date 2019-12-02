<template>
  <a-spin :spinning="loading">
    <a-tabs defaultActiveKey="2">
      <a-tab-pane tab="设置产品规格" key="1">
        <a-row>
          <a-col :span="24">
            <a-form :form="specForm">
              <a-form-item>输入规格，例如：【重量:200g】</a-form-item>
              <a-form-item
                v-for="k in specForm.getFieldValue('keys')"
                :key="k"
                v-bind="formItemLayoutWithOutLabel"
                :required="false"
              >
                <a-input
                  v-decorator="[
              `names[${k}]`,
              {
                  validateTrigger: ['change', 'blur'],
                  rules: [{
                  required: true,
                  whitespace: true,
                  message: 'Please input passenger\'s name or delete this field.',
                  }],
              }
              ]"
                  placeholder="重量：200g"
                  style="width: 60%; margin-right: 8px"
                />
                <a-icon
                  v-if="specForm.getFieldValue('keys').length > 1"
                  class="dynamic-delete-button"
                  type="minus-circle-o"
                  :disabled="specForm.getFieldValue('keys').length === 1"
                  @click="() => remove(k)"
                />
              </a-form-item>
              <a-form-item v-bind="formItemLayoutWithOutLabel">
                <a-button type="dashed" style="width: 60%" @click="add">
                  <a-icon type="plus" />Add field
                </a-button>
              </a-form-item>
            </a-form>
          </a-col>
        </a-row>
      </a-tab-pane>
      <a-tab-pane tab="设置产品详情" key="2">
        <a-row>
          <a-col :span="24">商品详情:</a-col>
          <a-col :md="24" :lg="20">
            <div ref="editor" style="text-align:left"></div>
          </a-col>
        </a-row>
      </a-tab-pane>
      <a-tab-pane tab="设置产品轮播图" key="3">
        <a-row>
          <a-col :span="24">商品展示图:</a-col>
          <a-col :md="24" :lg="20">
            <a-upload
              :action="uploadImageUrl"
              :headers="uploadImageHeader"
              listType="picture-card"
              :fileList="fileList"
              @preview="handleImagesPreview"
              @change="handleImagesChange"
            >
              <div v-if="fileList.length < 3">
                <a-icon type="plus" />
                <div class="ant-upload-text">Upload</div>
              </div>
            </a-upload>
            <a-modal :visible="previewVisible" :footer="null" @cancel="handlePreviewCancel">
              <img alt="example" style="width: 100%" :src="previewImage" />
            </a-modal>
          </a-col>
        </a-row>
      </a-tab-pane>
    </a-tabs>

    <a-row class="inner-row">
      <a-col :span="24">
        <a-button type="primary" @click="handleSubmit">
          <a-icon type="save" />保存
        </a-button>
      </a-col>
    </a-row>
  </a-spin>
</template>
<script>
import E from "wangeditor";

let count = 1;
export default {
  props: {
    id: { type: Number, default: 0 }
  },
  data() {
    const uploadImageUrl = this.apiUrl + "/backend/file/upload";
    const uploadImageHeader = {
      Authorization: "ZShop " + Cookie.getCookie("token")
    };
    return {
      loading: false,
      formItemLayoutWithOutLabel: {
        wrapperCol: {
          xs: { span: 24, offset: 0 },
          sm: { span: 20, offset: 4 }
        }
      },
      spec: {
        commodityId: 0,
        specification: "",
        images: "",
        detail: ""
      },
      editorContent: "",
      fileList: [
      ],
      previewVisible: false,
      previewImage: "",
      editor: {},
      uploadImageUrl: uploadImageUrl,
      uploadImageHeader: uploadImageHeader
    };
  },
  beforeCreate() {
    this.specForm = this.$form.createForm(this);
    this.specForm.getFieldDecorator("keys", {
      initialValue: [],
      preserve: true
    });
  },
  mounted() {
    const editor = new E(this.$refs.editor);
    editor.customConfig.uploadImgServer = this.uploadImageUrl;
    editor.customConfig.uploadImgHeaders = this.uploadImageHeader;
    editor.customConfig.uploadImgMaxLength = 5;
    editor.customConfig.onchange = html => {
      this.editorContent = html;
    };
    editor.create();
    this.editor = editor;
  },
  methods: {
    init(id) {
      const url = `${this.apiUrl}/backend/product/profile/${id}`;
      const self = this;
      self.loading = true;
      G.get(url)
        .cb(data => {
          if (data.status === 0) {
            self.spec = data.item;
          } else {
            console.error(data);
          }
        })
        .fcb(() => {
          self.loading = false;
        })
        .req();
    },
    remove(k) {
      const { specForm } = this;
      const keys = specForm.getFieldValue("keys");
      if (keys.length === 1) {
        return;
      }
      // can use data-binding to set
      specForm.setFieldsValue({
        keys: keys.filter(key => key !== k)
      });
    },
    add() {
      const { specForm } = this;
      const keys = specForm.getFieldValue("keys");
      const nextKeys = keys.concat(++count);
      // can use data-binding to set
      // important! notify form to detect changes
      specForm.setFieldsValue({
        keys: nextKeys
      });
    },
    handleSubmit(e) {
      e.preventDefault();
      var self = this;
      this.specForm.validateFields((err, values) => {
        if (!err) {
          console.log("Received values of form: ", values);
          if (!values.names || values.names.length <= 0) {
            this.$message.warning("至少填写一条规格");
            return;
          }
          let spec = "";
          values.names.forEach(item => {
            if (!!item) {
              spec += item;
            }
          });
          values.names.join(",");
          console.log("spec", spec, self.fileList);

          let imagePaths = [];
          if (self.fileList.length > 0) {
            for (let i = 0; i < self.fileList.length; i++) {
              let _file = self.fileList[i];
              if (!!_file['response']) {
                let _res = _file['response'];
                if (!!_res['data'] && _res['data'] instanceof Array) {
                  imagePaths.push(_res['data'][0]);
                }
              }
            }
          }

          const url = self.apiUrl + '/backend/product/profile';
          let params = {
            commodityId: self.id,
            specification: spec,
            images: imagePaths.length > 0 ? imagePaths.join(',') : '',
            detail: self.editor.txt.html()
          };

          console.log('params', params);
        }
      });
    },
    getContent() {
      alert(this.editorContent);
    },
    // region 上传浏览图片
    handleImagesPreview(file) {
      this.previewImage = file.url || file.thumbUrl;
      this.previewVisible = true;
    },
    handleImagesChange({ fileList }) {
      this.fileList = fileList;
    },
    handlePreviewCancel() {
      this.previewVisible = false;
    }
    // endregion
  }
};
</script>