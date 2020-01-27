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
            <img v-if="thumbnail" :src="thumbnail.value" alt="上传首图" width="86px" height="86px" />
            <div v-else>
              <a-icon :type="thumbnailLoading ? 'loading' : 'plus'" />
              <div class="ant-upload-text">上传缩略图</div>
            </div>
          </a-upload>
        </a-form-item>

        <a-form-item :label-col="labelCol" :wrapper-col="wideWrapperCol" label="轮播图">
          <a-upload
            listType="picture-card"
            class="avatar-uploader"
            :fileList="imageFileList"
            :action="uploadUrl"
            :headers="headers"
            @preview="handleImagesPreview"
            @change="handleImagesChange"
          >
            <div v-if="imageFileList.length < 5">
              <a-icon type="plus" />
              <div class="ant-upload-text">点击上传</div>
            </div>
          </a-upload>
        </a-form-item>

        <a-form-item :label-col="labelCol" :wrapper-col="wideWrapperCol" label="规格">
          <a-row class="innter-row" :gutter="8">
            <a-col :span="16">
              <a-table
                :columns="columns"
                :dataSource="specification"
                size="small"
                :pagination="false"
              >
                <span slot="action" slot-scope="record">
                  <a-button type="danger" @click="removeSpec(record)">
                    <a-icon type="delete"></a-icon>
                  </a-button>
                </span>
              </a-table>
            </a-col>
            <a-col :span="8">
              <a-form>
                <a-form-item :validate-status="specName.status" :help="specName.help" has-feedback>
                  <a-input placeholder="输入规格名称" v-model="specName.value"></a-input>
                </a-form-item>
                <a-form-item
                  :validate-status="specContent.status"
                  :help="specContent.help"
                  has-feedback
                >
                  <a-input placeholder="输入规格" v-model="specContent.value"></a-input>
                </a-form-item>
                <a-form-item>
                  <a-button @click="addSpec" :loading="addSpecLoading" icon="plus">添加规格</a-button>
                </a-form-item>
              </a-form>
            </a-col>
          </a-row>
        </a-form-item>

        <a-form-item :label-col="labelCol" :wrapper-col="wideWrapperCol" label="详情">
          <div ref="editor" style="text-align:left"></div>
          <a-button @click="saveDetail" :loading="isDetalSaved">
            <a-icon type="save"></a-icon>保存详情
          </a-button>
        </a-form-item>

        <a-form-item :label-col="labelCol" :wrapper-col="wideWrapperCol" label="套餐">
          <a-row class="inner-row" :gutter="8">
            <a-col :span="18">
              <a-table :columns="formulaColumns" :dataSource="formulaDataSet" :pagination="false">
                <span slot="action" slot-scope="record">
                  <a-button
                    type="link"
                    icon="delete"
                    style="color:#f5222d"
                    @click="deleteFormula(record)"
                  ></a-button>
                </span>
              </a-table>
            </a-col>
            <a-col :span="6">
              <a-form>
                <a-form-item
                  :validate-status="formulaTitle.status"
                  :help="formulaTitle.help"
                  has-feedback
                >
                  <a-input placeholder="输入规格名称" v-model="formulaTitle.value"></a-input>
                </a-form-item>
                <a-form-item
                  :validate-status="formulaPrice.status"
                  :help="formulaPrice.help"
                  has-feedback
                >
                  <a-input
                    placeholder="输入价格,例如：1.00"
                    v-model="formulaPrice.value"
                    :min="0"
                    :max="9999999"
                  ></a-input>
                </a-form-item>
                <a-form-item
                  :validate-status="formulaInventory.status"
                  :help="formulaInventory.help"
                  has-feedback
                >
                  <a-input-number
                    :min="0"
                    :max="9999"
                    placeholder="输入库存"
                    v-model="formulaInventory.value"
                  ></a-input-number>
                </a-form-item>
                <a-form-item>
                  <a-button icon="plus" :loading="forumlaLoading" @click="saveFormula">添加套餐</a-button>
                </a-form-item>
              </a-form>
            </a-col>
          </a-row>
        </a-form-item>

        <a-form-item :label-col="labelCol" :wrapper-col="wideWrapperCol" label="选择标签">
          <a-spin :spinning="tagLoading">
            <a-row>
              <a-col span="24">
                <a-button v-if="tagPath.length > 0" @click="upTags">&lt;</a-button>
                <a-button-group v-for="(tag, index) in tags" :key="index" style="margin:6px;">
                  <a-button @click="addToCommodity(tag)">{{tag.title}}</a-button>
                  <a-button v-if="tag.childCount > 0" @click="nextTags(tag)">&gt;</a-button>
                </a-button-group>
              </a-col>
            </a-row>
            <a-row>
              <a-col span="24">
                <a-tag v-for="(tag, index) in commodityTags" 
                :key="index" 
                closable 
                @close="removeTag(tag)" 
                style="margin:6px;">{{tag.title}}</a-tag>
              </a-col>
            </a-row>
          </a-spin>
        </a-form-item>

        <a-form-item :label-col="labelCol" :wrapper-col="wideWrapperCol">
          <a-button icon="upload" @click="onlineCommodity">上架</a-button>
        </a-form-item>
      </template>
    </a-form>
  </a-spin>
</template>
<script>
import E from "wangeditor";

const _step_ = {
  BASIC_INFO: 0,
  SPECIFICATION: 1,
  FORMULAS: 2
};

const specColumns = [
  { title: "规格名", key: "title", dataIndex: "title" },
  { title: "内容", key: "content", dataIndex: "content" },
  { title: "Action", scopedSlots: { customRender: "action" }, width: "60px" }
];

const formulaColumns = [
  { title: "套餐名", key: "title", dataIndex: "title" },
  { title: "价格", key: "price", dataIndex: "price" },
  { title: "库存(件)", key: "inventory", dataIndex: "inventory" },
  { title: "Action", scopedSlots: { customRender: "action" }, width: "60px" }
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
      commodityProfile: {
        commodityId: 0,
        specification: "",
        images: "",
        detail: ""
      },
      // region specification
      columns: specColumns,
      addSpecLoading: false,
      specification: [],
      specName: { status: "", help: "", value: "" },
      specContent: { status: "", help: "", value: "" },
      imageFileList: [],
      images: [],
      previewImage: "",
      previewVisible: "",
      // endregion
      // region Editor
      editor: {},
      editorContent: "",
      isDetalSaved: false,
      // endregion
      // region formula
      formulaColumns: formulaColumns,
      formulaDataSet: [],
      formulaTitle: { status: "", help: "", value: "" },
      formulaPrice: { status: "", help: "", value: "" },
      formulaInventory: { status: "", help: "", value: "" },
      forumlaLoading: false,
      // endregion
      // region tags
      tagLoading: false,
      tags: [],
      commodityTags: [],
      tagPath: []
      // endregion
    };
  },
  beforeCreate() {},
  created() {},
  mounted() {
    const editor = new E(this.$refs.editor);
    editor.customConfig.uploadImgServer = this.uploadUrl;
    editor.customConfig.uploadImgHeaders = this.headers;
    editor.customConfig.uploadImgMaxLength = 5;
    editor.customConfig.onchange = html => {
      this.editorContent = html;
    };
    editor.create();
    this.editor = editor;

    console.log("input commodity=>", this.commodity);
    this.innerId = this.commodity.id;
    this.name.value = this.commodity.title;
    this.summary.value = this.commodity.introduction;
    if (this.innerId > 0) {
      this.thumbnail.value = this.commodity.thumbnail;
      this.loadCommodityProfile(this.innerId);
      this.loadFormulaList();
      this.loadTags(0);
      this.loadCommodityTags(this.innerId);
    }
  },
  watch: {},
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
    },
    // region specification
    addSpec() {
      let title = this.specName.value;
      let content = this.specContent.value;
      if (!title || title.trim() === "") {
        this.specName.status = G._status.error;
        this.specName.help = "请输入规格名称";
        return;
      }
      if (!content || content.trim() === "") {
        this.specContent.status = G._status.error;
        this.specContent.help = "请输入规格";
        return;
      }
      this.specName.status = G._status.validating;
      this.specName.help = "";
      this.specContent.status = G._status.validating;
      this.specContent.help = "";
      this.specification.push({
        title: this.specName.value,
        content: this.specContent.value
      });

      const url = `${this.apiUrl}/backend/product/profile/specification`;
      let fd = new FormData();
      console.log("id ===>", this.commodity.id);
      fd.append("commodityId", this.commodity.id);
      fd.append("specification", JSON.stringify(this.specification));
      this.addSpecLoading = true;
      G.post(url, fd)
        .cb(data => {
          if (data.status === 0) {
            this.specName.status = G._status.success;
            this.specContent.status = G._status.success;
          } else {
            this.specName.status = G._status.error;
            this.specContent.status = G._status.error;
          }
        })
        .fcb(() => {
          this.addSpecLoading = false;
        })
        .req();
    },
    loadCommodityProfile(id) {
      const url = `${this.apiUrl}/backend/product/profile/${id}`;
      G.get(url)
        .cb(data => {
          if (data.status === 0) {
            let commodity = data.item;
            let specification = commodity["specification"];
            this.specification = !!specification
              ? JSON.parse(specification)
              : [];
            let images = commodity["images"];
            this.images = !!images ? images.split(",") : [];
            if (this.images.length > 0) {
              for (let index in this.images) {
                let image = this.images[index];
                this.imageFileList.push({
                  uid: index,
                  name: index + ".jpg",
                  response: {
                    data: [image]
                  },
                  url: image
                });
              }
            }
            this.editor.txt.html(commodity["detail"]);
          }
        })
        .fcb()
        .req();
    },
    removeSpec(item) {
      console.log("remove ===>", item);
      let origin = JSON.parse(JSON.stringify(this.specification));
      this.specification = this.specification.filter(
        _item => _item.title != item.title
      );
      const url = `${this.apiUrl}/backend/product/profile/specification`;
      let fd = new FormData();
      console.log("id ===>", this.commodity.id);
      fd.append("commodityId", this.commodity.id);
      fd.append("specification", JSON.stringify(this.specification));
      G.post(url, fd)
        .cb(data => {
          if (data.status === 0) {
            this.$message.success("已删除");
          } else {
            this.specification = origin;
          }
        })
        .fcb()
        .req();
    },
    handleImagesPreview(file) {
      this.previewImage = file.url || file.thumbUrl;
      this.previewVisible = true;
    },
    handleImagesChange({ file, fileList, event }) {
      this.imageFileList = fileList;
      if (file.status === "done") {
        console.log("file ===> ", file.uid);
        this.images.push(file.response.data[0]);
        console.log("images", this.images);
        this.updateImages();
      }
      if (file.status === "removed") {
        console.log("file ===>", file.uid);
        this.images = this.images.filter(x => x !== file.response.data[0]);
        console.log("images", this.images);
        this.updateImages();
      }
    },
    updateImages() {
      const url = `${this.apiUrl}/backend/product/profile/images`;
      const fd = new FormData();
      const images = this.images.length > 0 ? this.images.join(",") : "";
      fd.append("commodityId", this.innerId);
      fd.append("images", images);
      G.post(url, fd)
        .cb(data => {
          if (data.status !== 0) {
            this.$message.error(data.message);
          }
        })
        .fcb()
        .req();
    },
    handlePreviewCancel() {
      this.previewVisible = false;
    },
    // endregion
    saveDetail() {
      const url = `${this.apiUrl}/backend/product/profile/detail`;
      const fd = new FormData();
      fd.append("commodityId", this.innerId);
      fd.append("detail", this.editorContent);
      this.isDetalSaved = true;
      G.post(url, fd)
        .cb(data => {
          if (data.status === 0) {
            this.$message.success("已保存");
          } else {
            this.$message.error(data.message);
          }
        })
        .fcb(() => {
          this.isDetalSaved = false;
        })
        .req();
    },
    // region formula
    saveFormula() {
      const url = `${this.apiUrl}/backend/product/formula`;

      let title = !!this.formulaTitle.value
        ? this.formulaTitle.value.trim()
        : null;
      if (title === null || title === "") {
        this.formulaTitle.help = "请填入套餐名称";
        this.formulaTitle.status = "error";
        return;
      }

      this.formulaTitle.help = "";
      this.formulaTitle.status = "validating";

      let price = !!this.formulaPrice.value ? this.formulaPrice.value : null;
      let priceRegex = /^[1-9]?[\d]*\.[\d]{2}$/;
      if (!priceRegex.test(price)) {
        this.formulaPrice.status = "error";
        this.formulaPrice.help = "请输入正确的价格";
        return;
      }

      this.formulaPrice.help = "";
      this.formulaPrice.status = "validating";

      let inventory = !!this.formulaInventory.value
        ? this.formulaInventory.value
        : null;
      if (!/\d+/.test(inventory)) {
        this.formulaInventory.help = "请输入正确的库存";
        this.formulaInventory.status = "error";
        return;
      }
      try {
        inventory = Number(inventory);
      } catch (e) {
        this.formulaInventory.help = "请输入正确的库存";
        this.formulaInventory.status = "error";
        return;
      }

      this.formulaInventory.help = "";
      this.formulaInventory.status = "validating";

      const fd = new FormData();
      fd.append("commodityId", this.innerId);
      fd.append("title", title);
      fd.append("price", "CNY " + price);
      fd.append("inventory", inventory);

      G.post(url, fd)
        .cb(data => {
          if (data.status === 0) {
            this.formulaTitle.status = "success";
            this.formulaPrice.status = "success";
            this.formulaInventory.status = "success";
            let formula = {
              id: data.item,
              commodityId: this.innerId,
              title: title,
              price: price,
              inventory: inventory
            };
            this.formulaDataSet.push(formula);
          } else {
            this.$message.error(data.message);
            this.formulaTitle.status = "";
            this.formulaPrice.status = "";
            this.formulaInventory.status = "";
          }
        })
        .fcb()
        .req();
    },
    deleteFormula(item) {
      console.log("delete formula", item);
      const url = `${this.apiUrl}/backend/product/formula/${item.id}`;
      G.delete(url)
        .cb(data => {
          if (data.status === 0) {
            let temp = this.formulaDataSet;
            temp = temp.filter(x => x.id !== item.id);
            this.formulaDataSet = temp;
            this.$message.success("已删除");
          } else {
            this.$message.error(data.message);
          }
        })
        .fcb()
        .req();
    },
    loadFormulaList() {
      const url = `${this.apiUrl}/backend/product/formula-list/${this.innerId}`;
      G.get(url)
        .cb(data => {
          if (data.status === 0) {
            let arr = [];
            for (let i = 0; i < data.list.length; i++) {
              let item = data.list[i];
              arr.push({
                id: item["id"],
                commodityId: item["commodityId"],
                title: item["title"],
                price: item["price"]["amount"],
                inventory: item["inventory"]
              });
            }
            this.formulaDataSet = arr;
          }
        })
        .fcb()
        .req();
    },
    // endregin
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
        .fcb(() => this.tagLoading = false)
        .req();
    },
    loadCommodityTags(commodityId) {
      this.tagLoading = true;
      const url = `${this.apiUrl}/backend/product/tags/${commodityId}`;
      G.get(url)
        .cb(data => {
          if (data.status === 0) {
            this.commodityTags = data.list;
          } else {
            this.$message.error(data.message);
          }
        })
        .fcb(() => this.tagLoading = false)
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
    addToCommodity(tag) {
      const url = `${this.apiUrl}/backend/product/tag`;
      const fd = new FormData();
      fd.append("tagId", tag.id);
      fd.append("commodityId", this.innerId);
      this.tagLoading = true;
      G.post(url, fd)
        .cb(data => {
          if (data.status === 0)
          this.commodityTags.push(JSON.parse(JSON.stringify(tag)));
        })
        .fcb(() => this.tagLoading = false)
        .req();
    },
    removeTag (tag) {
      const url = `${this.apiUrl}/backend/product/tag/${tag.id}`;
      G.delete(url)
      .cb(data => {
        if (data.status === 0) {
          this.commodityTags = this.commodityTags.filter( t => t.tagId !== tag.id);
        }
      })
      .fcb(() => this.tagLoading = false)
      .req();
    },
    // endregion
    onlineCommodity () {
      const url = `${this.apiUrl}/backend/product/online`;
      const fd = new FormData();
      fd.append('id', this.innerId);
      G.post(url, fd)
      .cb(data => {
        if (data.status === 0) {
          this.$message.success('已上架');
        } else {
          console.error(data);
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
