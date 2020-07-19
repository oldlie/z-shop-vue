<template>
  <div>
    <a-row :gutter="4">
      <a-col :span="4">
        <sidebar :index="['4']" />
      </a-col>
      <a-col :span="20">
        <a-row class="inner-row">
          <a-col :span="24">
            <h2>添加/编辑文章</h2>
          </a-col>
        </a-row>
        <a-spin :spinning="loading">
          <a-row class="inner-row">
            <a-col :span="24">
              <a-button-group>
                <a-button
                  type="primary"
                  icon="upload"
                  @click="onlineCommodity"
                  :loading="onlineLoading"
                >上架商品</a-button>
                <a-button icon="table" @click="back2List">返回列表</a-button>
                <a-button icon="download" @click="offlineCommodity" :loading="offlineLoading">下架商品</a-button>
              </a-button-group>
            </a-col>
          </a-row>
          <div class="inner-row">
            <a-tabs default-active-key="1" @change="tabChangeCallback">
              <a-tab-pane key="1" tab="基本信息">
                <a-row :span="16">
                  <a-form>
                    <a-form-item
                      :label-col="labelCol"
                      :wrapper-col="wrapperCol"
                      label="商品名称"
                      :validate-status="title.status"
                      :help="title.help"
                      has-feedback
                    >
                      <a-input id="name" placeholder="请输入商品名称" v-model="title.value" />
                    </a-form-item>
                    <a-form-item
                      :label-col="labelCol"
                      :wrapper-col="wrapperCol"
                      label="简要描述"
                      :validate-status="introduction.status"
                      :help="introduction.help"
                      has-feedback
                    >
                      <a-input id="name" placeholder="请输入简要描述" v-model="introduction.value" />
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
                        <img
                          v-if="thumbnail"
                          :src="thumbnail.value"
                          alt="上传首图"
                          width="86px"
                          height="86px"
                        />
                        <div v-else>
                          <a-icon type="plus" />
                          <div class="ant-upload-text">上传缩略图</div>
                        </div>
                      </a-upload>
                    </a-form-item>
                    <a-form-item
                      :label-col="labelCol"
                      :wrapper-col="wrapperCol"
                      label="二维码"
                      :validate-status="qrCode.status"
                      has-feedback
                    >
                      <a-upload
                        name="file"
                        listType="picture-card"
                        class="avatar-uploader"
                        :showUploadList="false"
                        :action="uploadUrl"
                        :headers="headers"
                        :beforeUpload="beforeQRCodeUpload"
                        @change="handleQRCodeChange"
                      >
                        <img
                          v-if="qrCode.value !== ''"
                          :src="qrCode.value"
                          alt="二维码"
                          width="86px"
                          height="86px"
                        />
                        <div v-else>
                          <a-icon type="plus" />
                          <div class="ant-upload-text">二维码</div>
                        </div>
                      </a-upload>
                    </a-form-item>

                    <a-form-item :wrapper-col="{ offset: 3, sm: 12 }">
                      <a-button icon="save" :loading="saveBasicLoading" @click="saveBasicInfo">保存</a-button>
                    </a-form-item>
                  </a-form>
                </a-row>
              </a-tab-pane>
              <a-tab-pane key="2" tab="详细" force-render>
                <a-form>
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
                          <a-form-item
                            :validate-status="specName.status"
                            :help="specName.help"
                            has-feedback
                          >
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
                    <a-button @click="saveDetail" :loading="saveDetailLoading">
                      <a-icon type="save"></a-icon>保存详情
                    </a-button>
                  </a-form-item>
                </a-form>
              </a-tab-pane>
              <a-tab-pane key="3" tab="套餐">
                <a-row class="inner-row" :gutter="8">
                  <a-col :span="18">
                    <a-table
                      :columns="formulaColumns"
                      :dataSource="formulaDataSet"
                      :pagination="false"
                    >
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
              </a-tab-pane>
              <a-tab-pane key="4" tab="标签">
                <a-form>
                  <a-form-item :label-col="labelCol" :wrapper-col="wideWrapperCol" label="选择标签">
                    <a-spin :spinning="tagLoading">
                      <a-row>
                        <a-col span="24">
                          <a-button v-if="tagPath.length > 0" @click="upTags">&lt;</a-button>
                          <a-button-group
                            v-for="(tag, index) in tags"
                            :key="index"
                            style="margin:6px;"
                          >
                            <a-button @click="addToCommodity(tag)">{{tag.title}}</a-button>
                            <a-button v-if="tag.childCount > 0" @click="nextTags(tag)">&gt;</a-button>
                          </a-button-group>
                        </a-col>
                      </a-row>
                      <a-row>
                        <a-col span="24">
                          <a-tag
                            v-for="(tag, index) in commodityTags"
                            :key="index"
                            closable
                            @close="removeTag(tag)"
                            style="margin:6px;"
                          >{{tag.title}}</a-tag>
                        </a-col>
                      </a-row>
                    </a-spin>
                  </a-form-item>
                </a-form>
              </a-tab-pane>
              <a-tab-pane key="5" tab="评价维度">
                <a-row class="inner-row">
                  <a-col :span="16">
                    <p>维度用于用户给商品打分，最多添加5个评价维度</p>
                    <a-form>
                      <a-form-item
                        :validate-status="dimension.status"
                        :help="dimension.help"
                        has-feedback
                      >
                        <a-input placeholder="输入维度" v-model="dimension.value"></a-input>
                      </a-form-item>
                      <a-form-item>
                        <a-button icon="save" :loading="submitDimLoading" @click="submitDim">保存</a-button>
                      </a-form-item>
                    </a-form>
                  </a-col>
                </a-row>
                <a-row class="inner-row">
                  <a-tag style="margin:5px 5px 5px 0" v-for="item in dimensions" :key="item.id" closable @close="removeDimension(item)">{{item.title}}</a-tag>
                </a-row>
              </a-tab-pane>
            </a-tabs>
          </div>
          <a-row class="inner-row">
            <a-col :span="24">
              <a-button-group>
                <a-button
                  type="primary"
                  icon="upload"
                  @click="onlineCommodity"
                  :loading="onlineLoading"
                >上架商品</a-button>
                <a-button icon="table" @click="back2List">返回列表</a-button>
                <a-button icon="download" @click="offlineCommodity" :loading="offlineLoading">下架商品</a-button>
              </a-button-group>
            </a-col>
          </a-row>
        </a-spin>
      </a-col>
    </a-row>
  </div>
</template>
<script>
import E from "wangeditor";
function getBase64(img, callback) {
  const reader = new FileReader();
  reader.addEventListener("load", () => callback(reader.result));
  reader.readAsDataURL(img);
}
const specColumns = [
  { title: "规格名", key: "title", dataIndex: "title" },
  { title: "内容", key: "content", dataIndex: "content" },
  { title: "Action", scopedSlots: { customRender: "action" }, width: "60px" }
];
const formulaColumns = [
  { title: "套餐名", key: "title", dataIndex: "title" },
  { title: "价格", key: "price", dataIndex: "price" },
  { title: "库存(件)", key: "inventory", dataIndex: "inventory" },
  { title: "Action", scopedSlots: { customRender: "action" }, width: "120px" }
];
export default {
  data() {
    const auth = `ZShop ${this.$cookie.get("token")}`;
    const uploadUrl = this.axios.defaults.baseURL + `/backend/file/upload`;
    return {
      loading: false,
      headers: { Authorization: auth },
      uploadUrl: uploadUrl,
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
      id: 0,
      // region basic info
      title: { value: "", status: "", help: "" },
      introduction: { value: "", status: "", help: "" },
      thumbnail: { value: "", status: "", help: "" },
      qrCode: { value: "", status: "", help: "" },
      saveBasicLoading: false,
      imageFileList: [],
      // endregion
      // region profile
      images: [],
      saveDetailLoading: false,
      editorContent: "",
      columns: specColumns,
      specification: [],
      specName: { value: "", status: "", help: "" },
      specContent: { value: "", status: "", help: "" },
      addSpecLoading: false,
      // endregion
      // region formula
      formulaColumns: formulaColumns,
      formulaDataSet: [],
      forumlaLoading: false,
      formulaTitle: { value: "", status: "", help: "" },
      formulaPrice: { value: "", status: "", help: "" },
      formulaInventory: { value: "", status: "", help: "" },
      // endregion
      // region tag
      tagLoading: false,
      tagPath: [],
      commodityTags: [],
      tags: [],
      // endregion
      onlineLoading: false,
      offlineLoading: false,
      // region evaluactive dimension
      dimension: { value: "", status: "", help: "" },
      dimensions: [],
      submitDimLoading: false
      // endregion
    };
  },
  mounted() {
    this.id = this.$route.params.id;
    const editor = new E(this.$refs.editor);
    editor.customConfig.uploadImgServer = this.uploadUrl;
    editor.customConfig.uploadImgHeaders = this.headers;
    editor.customConfig.uploadImgMaxLength = 5;
    editor.customConfig.onchange = html => {
      this.editorContent = html;
    };
    editor.create();
    this.editor = editor;

    this.loadCommodity();
  },
  methods: {
    // region commodity
    loadCommodity() {
      const url = `/backend/product/${this.id}`;
      this.loading = true;
      this.$h
        .get(url)
        .cb(data => {
          if (data.status === 0) {
            this.title.value = data.item.title;
            this.introduction.value = data.item.introduction;
            this.thumbnail.value = data.item.thumbnail;
          } else {
            this.$message.error(data.message);
          }
        })
        .fcb(() => (this.loading = false))
        .req();
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
        this.loading = true;
        return;
      }
      if (info.file.status === "done") {
        console.log("handleChange ===>", info);
        let response = info.file.response;
        if (!!response["data"]) {
          this.thumbnail.value = response["data"][0];
        }
        this.thumbnail.status = "success";
        this.loading = false;
      }
    },
    beforeQRCodeUpload(file) {
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
    handleQRCodeChange(info) {
      if (info.file.status === "uploading") {
        this.loading = true;
        return;
      }
      if (info.file.status === "done") {
        console.log("handleChange ===>", info);
        let response = info.file.response;
        if (!!response["data"]) {
          this.qrCode.value = response["data"][0];
        }
        this.qrCode.status = "success";
        this.loading = false;
      }
    },
    saveBasicInfo() {
      const url = "/backend/product";
      const params = {
        id: this.id,
        title: this.title.value,
        introduction: this.introduction.value,
        thumbnail: this.thumbnail.value,
        qrCode: this.qrCode.value,
        status: 0
      };
      this.saveBasicLoading = true;
      this.$h
        .post(url, params)
        .cb(data => {
          if (data.status === 0) {
            this.$message.success("基础信息已保存");
          } else {
            this.$message.error(data.message);
          }
        })
        .fcb(() => (this.saveBasicLoading = false))
        .req();
    },
    // endregion
    // region commodity profile
    handleImagesPreview(file) {},
    handleImagesChange({ file, fileList, event }) {
      this.imageFileList = fileList;
      if (file.status === "done") {
        this.images.push(file.response.data[0]);
        this.updateImages();
      }
      if (file.status === "removed") {
        this.images = this.images.filter(x => x !== file.response.data[0]);
        this.updateImages();
      }
    },
    updateImages() {
      const url = `/backend/product/profile/images`;
      const fd = new FormData();
      const images = this.images.length > 0 ? this.images.join(",") : "";
      fd.append("commodityId", this.id);
      fd.append("images", images);
      this.$h
        .post(url, fd)
        .cb(data => {
          if (data.status !== 0) {
            this.$message.error(data.message);
          }
        })
        .fcb()
        .req();
    },
    loadCommodityProfifle() {
      const url = `/backend/product/profile/${this.id}`;
      this.imageFileList = [];
      this.specification = [];
      this.$h
        .get(url)
        .cb(data => {
          console.log("load commodity profile--->", data);
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
    saveDetail() {
      const url = `/backend/product/profile/detail`;
      const fd = new FormData();
      fd.append("commodityId", this.id);
      fd.append("detail", this.editorContent);
      this.saveDetailLoading = true;
      this.$h
        .post(url, fd)
        .cb(data => {
          if (data.status === 0) {
            this.$message.success("已保存");
          } else {
            this.$message.error(data.message);
          }
        })
        .fcb(() => {
          this.saveDetailLoading = false;
        })
        .req();
    },
    removeSpec(item) {
      let origin = JSON.parse(JSON.stringify(this.specification));
      this.specification = this.specification.filter(
        _item => _item.title != item.title
      );
      const url = `/backend/product/profile/specification`;
      let fd = new FormData();
      fd.append("commodityId", this.id);
      fd.append("specification", JSON.stringify(this.specification));
      this.$h
        .post(url, fd)
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
    addSpec() {
      let title = this.specName.value;
      let content = this.specContent.value;
      if (!title || title.trim() === "") {
        this.specName.status = "error";
        this.specName.help = "请输入规格名称";
        return;
      }
      if (!content || content.trim() === "") {
        this.specContent.status = "error";
        this.specContent.help = "请输入规格";
        return;
      }
      this.specName.status = "validating";
      this.specName.help = "";
      this.specContent.status = "validating";
      this.specContent.help = "";
      this.specification.push({
        title: this.specName.value,
        content: this.specContent.value
      });

      const url = `/backend/product/profile/specification`;
      let fd = new FormData();
      fd.append("commodityId", this.id);
      fd.append("specification", JSON.stringify(this.specification));
      this.addSpecLoading = true;
      this.$h
        .post(url, fd)
        .cb(data => {
          if (data.status === 0) {
            this.specName.value = "";
            this.specName.status = "";
            this.specContent.value = "";
            this.specContent.status = "";
          } else {
            this.specName.status = "error";
            this.specContent.status = "error";
          }
        })
        .fcb(() => {
          this.addSpecLoading = false;
        })
        .req();
    },
    // endregion
    // region formula
    saveFormula() {
      const url = `/backend/product/formula`;

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
      fd.append("commodityId", this.id);
      fd.append("title", title);
      fd.append("price", "CNY " + price);
      fd.append("inventory", inventory);

      this.forumlaLoading = true;
      this.$h
        .post(url, fd)
        .cb(data => {
          if (data.status === 0) {
            this.formulaTitle.value = "";
            this.formulaTitle.status = "";
            this.formulaPrice.value = "";
            this.formulaPrice.status = "";
            this.formulaInventory.value = "";
            this.formulaInventory.status = "";
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
        .fcb(() => (this.forumlaLoading = false))
        .req();
    },
    deleteFormula(item) {
      const url = `/backend/product/formula/${item.id}`;
      this.$h
        .delete(url)
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
      const url = `/backend/product/formula-list/${this.id}`;
      this.$h
        .get(url)
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
    // endregion
    // region tag
    loadTags(id) {
      this.tagLoading = true;
      const url = `/backend/tags/${id}`;
      this.$h
        .get(url)
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
    loadCommodityTags(commodityId) {
      this.tagLoading = true;
      const url = `/backend/product/tags/${commodityId}`;
      this.$h
        .get(url)
        .cb(data => {
          if (data.status === 0) {
            this.commodityTags = !!data.list ? data.list : [];
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
      this.loadTags(tag.id);
    },
    upTags() {
      const tag = this.tagPath.pop();
      this.loadTags(tag.parentId);
    },
    addToCommodity(tag) {
      let exist = false;
      for (let i = 0; i < this.commodityTags.length; i++) {
        let tmp = this.commodityTags[i];
        if (tmp.id === tag.id) {
          exist = true;
          break;
        }
      }
      if (exist) {
        this.$message.warning("标签已经添加了");
        return;
      }
      const url = `/backend/product/tag`;
      const fd = new FormData();
      fd.append("tagId", tag.id);
      fd.append("commodityId", this.id);
      this.tagLoading = true;
      this.$h
        .post(url, fd)
        .cb(data => {
          if (data.status === 0) {
            this.commodityTags.push(JSON.parse(JSON.stringify(tag)));
          } else {
            this.$message.error(data.message);
          }
        })
        .fcb(() => (this.tagLoading = false))
        .req();
    },
    removeTag(tag) {
      const url = `/backend/product/tag/${this.id}/${tag.id}`;
      this.tagLoading = true;
      this.$h
        .delete(url)
        .cb(data => {
          if (data.status === 0) {
            this.commodityTags = this.commodityTags.filter(
              t => t.tagId !== tag.id
            );
          }
        })
        .fcb(() => (this.tagLoading = false))
        .req();
    },
    // endregion
    onlineCommodity() {
      const url = `/backend/product/online`;
      const fd = new FormData();
      fd.append("id", this.id);
      this.onlineLoading = true;
      this.$h
        .post(url, fd)
        .cb(data => {
          if (data.status === 0) {
            this.$message.success("已上架");
          } else {
            console.error(data);
          }
        })
        .fcb(() => (this.onlineLoading = false))
        .req();
    },
    offlineCommodity() {
      const url = `/backend/product/offline`;
      const fd = new FormData();
      fd.append("id", this.id);
      this.offlineLoading = true;
      this.$h
        .post(url, fd)
        .cb(data => {
          if (data.status === 0) {
            this.$message.success("已下架");
          } else {
            console.error(data);
          }
        })
        .fcb(() => (this.offlineLoading = false))
        .req();
    },
    back2List() {
      this.$router.push("/commodities");
    },
    tabChangeCallback(index) {
      console.log("index--->", index);
      if (index === "1") {
        this.loadCommodity();
      } else if (index === "2") {
        this.loadCommodityProfifle();
      } else if (index === "3") {
        this.loadFormulaList();
      } else if (index === "4") {
        this.loadTags(0);
        this.loadCommodityTags(this.id);
      } else if (index === "5") {
        this.loadDimensions();
      }
    },
    // region evaluative dimension
    submitDim() {
      if (this.dimension.value === "" || this.dimension.value.length > 6) {
        this.dimension.status = "warning";
        this.dimension.help = "请输入维度且维度不超过6个字符";
        return;
      }
      const url = `/backend/dimension`;
      this.dimension.status = "valdating";
      this.dimension.help = "";
      this.submitDimLoading = true;
      const fd = new FormData();
      fd.append("cid", this.id);
      fd.append("title", this.dimension.value);
      this.$h
        .post(url, fd)
        .cb(data => {
          if (data.status === 0) {
            this.$message.success("已保存");
            this.dimension.status = "sucess";
            this.loadDimensions();
          } else {
            this.$message.me(data.message);
            this.dimension.status = "error";
          }
        })
        .fcb(() => (this.submitDimLoading = false))
        .req();
    },
    loadDimensions() {
      const url = `/backend/dimensions?cid=${this.id}`;
      this.loading = true;
      this.$h
        .get(url)
        .cb(data => {
          if (data.status === 0) {
            this.dimensions = data.list;
          } else {
            this.$message.me(data.message);
          }
        })
        .fcb(() => (this.loading = false))
        .req();
    },
    removeDimension(item) {
      const url = `/backend/dimension?id=${item.id}`
      this.loading = true;
      this.$h.delete(url)
      .cb(data => {
        if (data.status === 0) {
          this.$message.success("已移除");
        } else {
          this.$message.me(data.message);
        }
      })
      .fcb(() => this.loading = false)
      .req();
    }
    // endregion
  }
};
</script>