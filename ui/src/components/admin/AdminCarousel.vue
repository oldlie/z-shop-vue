<template>
  <div>

    <a-row v-if="isShowForm" class="inner-row">
      <a-form :form="form" @submit="handleSubmit">
        <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="标题">
          <a-input v-decorator="titleValidate"></a-input>
        </a-form-item>
        <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="简介">
          <a-input v-decorator="summaryValidate"></a-input>
        </a-form-item>
        <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="跳转URL">
          <a-input v-decorator="urlValidate"></a-input>
        </a-form-item>
        <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="上传图片">
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
              <div class="ant-upload-text">上传图片</div>
            </div>
          </a-upload>
        </a-form-item>
        <a-form-item :wrapper-col="{ span: 12, offset: 3}">
          <a-button @click="handleSubmit" icon="save" :loading="loading">保存</a-button>
        </a-form-item>
      </a-form>
    </a-row>
    <a-row class="inner-row">
      <a-col :span="24">
        <a-table :columns="columns" :dataSource="list" :pagination="false" :rowKey="'id'">
          <span slot="title" slot-scope="item">
            <div
              style="width:240px; white-space: nowrap;  overflow: hidden;  text-overflow: ellipsis;"
            >{{item.title}}</div>
          </span>
          <span slot="summary" slot-scope="item">
            <div
              style="width:240px; white-space: nowrap;  overflow: hidden;  text-overflow: ellipsis;"
            >{{item.summary}}</div>
          </span>
          <span slot="url" slot-scope="item">
            <div
              style="width:240px; white-space: nowrap;  overflow: hidden;  text-overflow: ellipsis;"
            >{{item.url}}</div>
          </span>
          <span slot="action" slot-scope="item">
            <a-popconfirm
              title="删除"
              @confirm="confirm(item)"
              @cancel="cancel"
              okText="确定"
              cancelText="取消"
            >
              <a href="javascript:;" style="color:#f5222d">
                <a-icon type="delete"></a-icon>
              </a>
            </a-popconfirm>
          </span>
        </a-table>
      </a-col>
    </a-row>

    <a-row class="inner-row">
      <a-col :span="24">
        <a-carousel arrows :afterChange="onChange" dotsClass="slick-dots slick-thumb">
          <div
            slot="prevArrow"
            slot-scope="props"
            class="custom-slick-arrow"
            style="left: 10px;zIndex: 1"
          >
            <a-icon type="left-circle" />
          </div>
          <div slot="nextArrow" slot-scope="props" class="custom-slick-arrow" style="right: 10px">
            <a-icon type="right-circle" />
          </div>
          <div v-for="item in list" :key="item.id">
            <img
              :src="item.imageUrl"
              :style="{height:'460px',width:'920px','min-width':'247px','min-height':'186px'}"
            />
          </div>
        </a-carousel>
      </a-col>
    </a-row>
  </div>
</template>
<script>
function getBase64(img, callback) {
  const reader = new FileReader();
  reader.addEventListener("load", () => callback(reader.result));
  reader.readAsDataURL(img);
}

const columns = [
  { title: "标题", width: "240px", scopedSlots: { customRender: "title" } },
  {
    title: "简要说明",
    width: "240px",
    scopedSlots: { customRender: "summary" }
  },
  { title: "跳转URL", width: "240px", scopedSlots: { customRender: "url" } },
  { title: "操作", scopedSlots: { customRender: "action" } }
];

export default {
  data() {
    const auth = `ZShop ${this.$cookie.get("token")}`;

    return {
      columns,
      list: [],
      imageList: [],
      isShowForm: true,
      loading: false,
      headers: { Authorization: auth },
      uploadUrl: `${this.apiUrl}/backend/file/upload`,
      form: this.$form.createForm(this, { name: "carousel" }),
      labelCol: {
        xs: { span: 24 },
        sm: { span: 3 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 12 }
      },
      titleValidate: [
        "title",
        {
          rules: [
            { required: true, message: "请输入标题" },
            { max: 32, message: "标题最多输入32个字符" }
          ]
        }
      ],
      summaryValidate: [
        "summary",
        {
          rules: [
            { required: true, message: "请输入简介" },
            { max: 255, message: "标题最多输入255个字符" }
          ]
        }
      ],
      urlValidate: [
        "url",
        {
          rules: [
            { required: true, message: "请输入URL" },
            { max: 255, message: "标题最多输入255个字符" }
          ]
        }
      ],
      thumbnail: "",
      thumbnailLoading: false
    };
  },
  mounted() {
    const url = `${this.apiUrl}/backend/carousels`;
    G.get(url)
      .cb(data => {
        if (data.status === 0) {
          this.list = data.list ? data.list : [];
          if (this.list) {
            this.list.forEach(x => {
              this.imageList.push(x.imageUrl);
            });
          }
        } else {
          this.$message.error(data.message);
        }
      })
      .fcb()
      .req();
  },
  methods: {
    onChange() {},
    edit(item) {},
    confirm(item) {
      const url = `${this.apiUrl}/backend/carousel/${item.id}`;
      G.delete(url)
      .cb(data => {
        if (data.status === 0) {
          this.list = this.list.filter(x => x.id !== item.id);
        }
      })
      .fcb()
      .req();
    },
    cancel() {},
    showAddForm() {},
    handleSubmit(e) {
      e.preventDefault();
      if (!this.thumbnail) {
        this.$message.warning('还没有上传图片。');
        return;
      }
      this.form.validateFields((err, values) => {
        if (err) {
          return;
        }
        const url = `${this.apiUrl}/backend/carousel`;
        const body = {
          title: values["title"],
          summary: values["summary"],
          url: values["url"],
          imageUrl: this.thumbnail
        };
        this.loading = true;
        G.post(url, body)
        .cb(data => {
          if (data.status === 0) {
            this.list.push({
              id: data.item,
              title: body.title,
              summary: body.summary,
              url: body.url,
              imageUrl: body.imageUrl
            });
            this.$message.success('已添加');
            values['title'] = '';
            values['summary'] = '';
            values['url'] = '';
            this.thumbnail = '';
          } else {
            this.$message.error(data.message);
          }
        })
        .fcb(() => {
          this.loading = false;
        })
        .req();
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
        let response = info.file.response;
        if (response["data"]) {
          this.thumbnail = response["data"][0];
        }

        // Get this url from response in real world.
        getBase64(info.file.originFileObj, imageUrl => {
          //this.thumbnail = imageUrl;
        });

        this.thumbnailLoading = false;
      }
    }
  }
};
</script>