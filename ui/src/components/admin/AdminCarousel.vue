<template>
  <div>
    <a-row class="inner-row">
      <a-col :span="24">
        <a-button @click="showAddForm" icon="plus" type="link">添加</a-button>
      </a-col>
    </a-row>
    <a-row v-if="isShowForm" class="inner-row">
      <a-form :form="from" @submit="handleSubmit">
        <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="标题">
          <a-input v-decorator="titleValidate"></a-input>
        </a-form-item>
        <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="简介">
          <a-input v-decorator="summaryValidate"></a-input>
        </a-form-item>
        <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="跳转URL">
          <a-input v-decorator="urlValidate"></a-input>
        </a-form-item>
      </a-form>
    </a-row>
    <a-row class="inner-row">
      <a-col :span="24">
        <a-table :columns="columns" :dataSource="list" :pagination="false">
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
            <a href="javascript:;" @click="edit(record)">
              <a-icon type="edit"></a-icon>
            </a>
            <a-divider type="vertical"></a-divider>
            <a-popconfirm
              title="删除"
              @confirm="confirm(record)"
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
          <div v-for="item in 4" :key="item">
            <img
              :src="baseUrl+'abstract0'+item+'.jpg'"
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

const baseUrl =
  "https://raw.githubusercontent.com/vueComponent/ant-design-vue/master/components/vc-slick/assets/img/react-slick/";

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
    return {
      baseUrl,
      columns,
      list: [],
      isShowForm: true,
      loading: false,
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
            { required: true, message: "请输入标题" },
            { max: 32, message: "标题最多输入32个字符" }
          ]
        }
      ],
      urlValidate: [
        "url",
        {
          rules: [
            { required: true, message: "请输入标题" },
            { max: 32, message: "标题最多输入32个字符" }
          ]
        }
      ]
    };
  },
  mounted() {
    const url = `${this.apiUrl}/backend/carousels`;
    G.get(url)
      .cb(data => {
        if (data.status === 0) {
          this.list = !!data.list ? data.list : [];
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
    confirm() {},
    cancel() {},
    showAddForm () {},
    handleSubmit() {}
  }
};
</script>