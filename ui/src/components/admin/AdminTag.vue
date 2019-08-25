<template>
  <div>
    <a-row class="inner-row">
      <a-col :span="6">
        <a-button shape="circle" icon="rollback" @click="back" />
      </a-col>
    </a-row>
    <a-row class="inner-row">
      <a-col :span="24">
        <a-form refs="form" :form="form">
          <a-form-item>
            <a-alert v-if="warnMessage !== ''" :message="warnMessage" type="warning" showIcon />
          </a-form-item>

          <a-form-item label="上级标签" :label-col="{ span: 5 }" :wrapper-col="{ span: 12 }">
            <span>{{parentTag.title}}</span>
          </a-form-item>

          <a-form-item label="标签" :label-col="{ span: 5 }" has-feedback :wrapper-col="{ span: 12 }">
            <a-input
              placeholder="请输入标签"
              v-decorator="[
                'title',
                {
                  rules: [
                   {required: true, message: '请输入标签！'}
                  ]
                }
              ]"
            ></a-input>
          </a-form-item>

          <a-form-item
            label="排序序号"
            :label-col="{ span: 5 }"
            has-feedback
            :wrapper-col="{ span: 12 }"
          >
            <a-input-number
              v-decorator="[
                'tagOrder'
              ]"
              :min="1"
              :max="1000"
            />
          </a-form-item>

          <a-form-item :wrapper-col="{ span: 12, offset: 5 }">
            <a-button type="primary" @click="handleSubmit">提交</a-button>
          </a-form-item>
        </a-form>
      </a-col>
    </a-row>
  </div>
</template>
<script>
export default {
  props: {
    parentTag: {
      type: Object,
      default: () => {
        return {
          id: 0,
          title: "",
          category: 0,
          tagOrder: 1,
          parentId: 0,
          childCount: 0
        };
      }
    }
  },
  data() {
    return {
      form: this.$form.createForm(this),
      warnMessage: "",
      tag: {
        id: 0,
        title: "",
        category: 0,
        tagOrder: 1,
        parentId: 0,
        childCount: 0
      }
    };
  },
  methods: {
    back() {
      this.$emit("goBackEvent", this.parentTag, this.tag);
    },
    handleSubmit(e) {
      e.preventDefault();
      const self = this;
      this.form.validateFields((err, values) => {
        if (!err) {
          const url = self.apiUrl + "/backend/tag";
          self.tag.title = values.title;
          self.tag.tagOrder = values.tagOrder;
          self.tag.parentId = self.parentTag.id;
          let params = {
            body: self.tag
          };
          G.post(url, params)
            .callback(data => {
              if (data.status === 0) {
                self.$message.success("已保存");
                self.bus.$emit("savedEvent");
              }
            })
            .request();
        }
      });
    }
  }
};
</script>