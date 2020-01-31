<template>
  <a-spin :spinning="loading">
    <a-form :form="form" @submit="handleSubmit">
      <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="标题">
        <a-input v-decorator="titleValidate"></a-input>
      </a-form-item>
      <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="作者">
        <a-input v-decorator="authorValidate"></a-input>
      </a-form-item>
      <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="题图">
        <a-upload
          name="file"
          listType="picture-card"
          :showUploadList="false"
          :action="uploadUrl"
          :headers="headers"
          :beforeUpload="beforeThumbnailUpload"
          @change="handleThumbnailChange"
        >
          <img v-if="thumbnail" :src="thumbnail" alt="上传题图" width="86px" height="86px" />
          <div v-else>
            <a-icon :type="thumbnailLoading ? 'loading' : 'plus'" />
            <div class="ant-upload-text">上传题图</div>
          </div>
        </a-upload>
      </a-form-item>
      <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="摘要">
        <a-textarea v-decorator="summaryValidate" :autosize="{minRows: 2, maxRows: 4}"></a-textarea>
      </a-form-item>
      <a-form-item>
        <div ref="editor" style="text-align:left"></div>
      </a-form-item>
      <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="允许评论">
        <a-switch @change="onChangeAllowComment" :checked="allowComment" />
      </a-form-item>
      <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="选择标签">
        <a-spin :spinning="tagLoading">
          <a-row>
            <a-col span="24">
              <a-button v-if="tagPath.length > 0" @click="upTags">&lt;</a-button>
              <a-button-group v-for="(tag, index) in tags" :key="index" style="margin:6px;">
                <a-button @click="addToAritcle(tag)">{{tag.title}}</a-button>
                <a-button v-if="tag.childCount > 0" @click="nextTags(tag)">&gt;</a-button>
              </a-button-group>
            </a-col>
          </a-row>
          <a-row>
            <a-col span="24">
              <a-tag
                v-for="(tag, index) in checkedTags"
                :key="index"
                closable
                @close="removeTag(tag)"
                style="margin:6px;"
              >{{tag.title}}</a-tag>
            </a-col>
          </a-row>
        </a-spin>
      </a-form-item>

      <a-form-item :wrapper-col="wrapperCol">
        <a-button type="primary" html-type="submit">保存</a-button>
      </a-form-item>
    </a-form>
  </a-spin>
</template>
<script>
import E from "wangeditor";

function getBase64(img, callback) {
  const reader = new FileReader();
  reader.addEventListener("load", () => callback(reader.result));
  reader.readAsDataURL(img);
}

export default {
  props: {
    articleId: { type: Number, default: 0 }
  },
  data() {
    const auth = `ZShop ${this.$cookie.get("token")}`;
    return {
      loading: false,
      form: this.$form.createForm(this, { name: "article" }),
      headers: { Authorization: auth },
      uploadUrl: `${this.apiUrl}/backend/file/upload`,
      thumbnailLoading: false,
      labelCol: {
        xs: { span: 24 },
        sm: { span: 3 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 12 }
      },
      // region validate
      titleValidate: [
        "title",
        {
          rules: [
            { required: true, message: "请输入标题" },
            { max: 32, message: "标题最多输入32个字符" }
          ]
        }
      ],
      authorValidate: [
        "author",
        {
          rules: [
            { required: true, message: "请注明作者" },
            { max: 128, message: "作者最多128字符" }
          ]
        }
      ],
      summaryValidate: [
        "summary",
        { rules: [{ max: 255, message: "最多输入255字符" }] }
      ],
      // endregion
      // region fields
      innerId: 0,
      content: "",
      editor: {},
      thumbnail: "",
      allowComment: false,
      // endregion
      // region tag
      tagLoading: false,
      tags: [],
      tagPath: [],
      checkedTags: []
      // endregion
    };
  },
  mounted() {
    const editor = new E(this.$refs.editor);
    editor.customConfig.uploadImgServer = this.uploadUrl;
    editor.customConfig.uploadImgHeaders = this.headers;
    editor.customConfig.uploadImgMaxLength = 5;
    editor.customConfig.onchange = html => {
      this.content = html;
    };
    editor.create();
    this.editor = editor;
    this.innerId = this.articleId;
    this.loadTags(0);
    if (this.articleId > 0) {
      this.loadArticle();
      this.loadAritcleTags();
    }
  },
  methods: {
    // region 验证规则

    // endregion
    handleSubmit(e) {
      e.preventDefault();
      this.form.validateFields((err, values) => {
        if (!err) {
          const url = `${this.apiUrl}/backend/article`;
          var fd = new FormData();
          fd.append("id", this.innerId);
          fd.append("title", values.title);
          fd.append("author", values.author);
          fd.append("summary", values.summary);
          fd.append("content", this.content);
          fd.append("thumbnail", this.thumbnail);
          fd.append("allowComment", this.allowComment ? 1 : 0);
          this.loading = true;
          G.post(url, fd)
            .cb(data => {
              if (data.status === 0) {
                this.innerId = data.item;
                this.$message.success("已保存");
              } else {
                this.$message.error(data.message);
              }
            })
            .fcb(() => (this.loading = false))
            .req();
        }
      });
    },
    onChangeAllowComment(checked, e) {
      e.preventDefault();
      this.allowComment = checked;
      console.log(this.allowComment);
    },
    // region thumbnail
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
          this.thumbnail = response["data"][0];
          console.log("thumbnail=>", this.thumbnail);
        }

        console.log(this.thumbnail);
        // Get this url from response in real world.
        getBase64(info.file.originFileObj, imageUrl => {
          //this.thumbnail = imageUrl;
        });

        this.thumbnailLoading = false;
      }
    },
    // endregion
    loadArticle() {
      const url = `${this.apiUrl}/backend/article/${this.innerId}`;
      this.loading = true;
      G.get(url)
        .cb(data => {
          if (data.status === 0) {
            console.log("load articles", data);
            const tmp = data.item;
            this.form.setFieldsValue({
              title: tmp.title,
              summary: tmp.summary,
              author: tmp.author
            });
            this.thumbnail = tmp.imageUrl;
            this.editor.txt.html(tmp["content"]);
            this.content = tmp.content;
            this.allowComment = tmp.allowComment == 1;
            console.log("allow comment", this.allowComment);
          } else {
            console.error(data);
          }
        })
        .fcb(() => (this.loading = false))
        .req();
    },
    // region tags
    loadTags(id) {
      this.tagLoading = true;
      const url = `${this.apiUrl}/backend/tags/${id}`;
      G.get(url)
        .cb(data => {
          if (data.status === 0) {
            this.tags = data.list;
            console.log("tags:", this.tags);
          } else {
            this.$message.error(data.message);
          }
        })
        .fcb(() => (this.tagLoading = false))
        .req();
    },
    loadAritcleTags() {
      this.tagLoading = true;
      const url = `${this.apiUrl}/backend/article/tags/${this.innerId}`;
      G.get(url)
        .cb(data => {
          if (data.status === 0) {
            this.checkedTags = data.list;
          } else {
            this.$message.error(data.message);
          }
        })
        .fcb(() => (this.tagLoading = false))
        .req();
    },
    nextTags(tag) {
      const _tag = JSON.parse(JSON.stringify(tag));
      this.tagPath.push(_tag);
      console.log(this.tagPath.length);
      this.loadTags(tag.id);
    },
    upTags() {
      const tag = this.tagPath.pop();
      this.loadTags(tag.parentId);
    },
    addToAritcle(tag) {
      if (this.innerId <= 0) {
        this.$message.warning('请先保存文章');
        return;
      }
      let exist = false;
      for (let i = 0; i < this.checkedTags; i++) {
        let tmp = this.checkedTags[i];
        if (tmp.id === tag.id) {
          exist = true;
          break;
        }
      }
      if (exist) {
        this.$message.warning('已添加');
        return;
      }
      const url = `${this.apiUrl}/backend/article/tag`;
      const fd = new FormData();
      fd.append("tagId", tag.id);
      fd.append("articleId", this.innerId);
      this.tagLoading = true;
      G.post(url, fd)
        .cb(data => {
          if (data.status === 0) {
            this.checkedTags.push(JSON.parse(JSON.stringify(tag)));
          } else {
            this.$message.error(data.message);
          }
        })
        .fcb(() => (this.tagLoading = false))
        .req();
    },
    removeTag(tag) {
      const url = `${this.apiUrl}/backend/article/tag/${this.innerId}/${tag.id}`;
      G.delete(url)
        .cb(data => {
          if (data.status === 0) {
            this.checkedTags = this.checkedTags.filter(t => t.tagId !== tag.id);
          } else {
            console.error(data);
          }
        })
        .fcb(() => (this.tagLoading = false))
        .req();
    }
    // endregion
  }
};
</script>