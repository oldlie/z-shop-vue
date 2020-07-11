<template>
  <div :style="{ background: '#fff', padding: '24px', minHeight: '280px', 'margin': '30px 0' }">
    <a-spin :spinning="loading">
      <a-row>
        <a-col :span="12" :style="{'text-align': 'left'}">
          <a-breadcrumb>
            <a-breadcrumb-item v-for="item in tags" :key="item.id">{{item.title}}</a-breadcrumb-item>
          </a-breadcrumb>
        </a-col>
        <a-col :span="24">
          <a-tabs defaultActiveKey="4" @change="onTabChange">
            <a-tab-pane tab="概述" key="1">
              <div v-html="profile.detail"></div>
            </a-tab-pane>
            <a-tab-pane tab="参数" key="2">
              <a-list :dataSource="specificationData">
                <a-list-item slot="renderItem" slot-scope="item">
                  <a-row style="width:100%;">
                    <a-col :span="10" style="text-align:right;">
                      <h3>{{item.name}}</h3>
                    </a-col>
                    <a-col :span="2"></a-col>
                    <a-col :span="12" style="text-align:left;">
                      <p>{{item.text}}</p>
                    </a-col>
                  </a-row>
                </a-list-item>
              </a-list>
            </a-tab-pane>
            <!--
            <a-tab-pane tab="用户评价" key="3">
              <product-comment></product-comment>
            </a-tab-pane>
            -->
            <a-tab-pane tab="立即购买" key="4" forceRender>
              <a-row>
                <a-col :span="12">
                  <div
                    id="J_sliderView"
                    class="sliderWrap"
                    style="width: auto; position: relative;"
                  >
                    <!--img
                      data-src="//i8.mifile.cn/a1/pms_1550642240.48638886.jpg"
                      class="slider done"
                      style="float: none; list-style: outside none none; width: 540px; height: 540px;object-fit:cover; z-index: 50; display: block;"
                      :src="commodity.thumbnail"
                    /-->
                    <a-carousel arrows dots-class="slick-dots slick-thumb" 
                    style="background: #ffffff; width: 540px; height: 540px;object-fit:cover;">
                      <a slot="customPaging" slot-scope="props">
                        <img :src="images[props.i]" />
                      </a>
                      <div v-for="image in images">
                        <img
                          style="float: none; list-style: outside none none; width: 460px; height: 460px;object-fit:cover; z-index: 50; display: block;"
                          :src="image"
                        />
                      </div>
                    </a-carousel>
                  </div>
                </a-col>
                <a-col :span="12" :style="{'text-align': 'left'}">
                  <h1>{{commodity.title}}</h1>
                  <p>
                    <!--
                    <span style="color:#ff6700">「4月16日上午10点开售」</span>全息幻彩全曲面玻璃机身 / 骁龙855旗舰处理器 / 索尼4800万超广角微距三摄 / 20W无线闪充 / 第5代极速屏下指纹解锁
                    -->
                    {{commodity.introduction}}
                  </p>
                  <!-- div class="J_addressWrap address-wrap">
                    <div class="user-default-address" id="J_userDefaultAddress">
                      <a-icon type="environment" />
                      <div>
                        <div class="address-info">
                          <span class="item">北京</span>
                          <span class="item">北京市</span>
                          <span class="item">东城区</span>
                          <span class="item">安定门街道</span>
                        </div>
                        <span class="switch-choose-regions" id="J_switchChooseRegions">修改</span>
                      </div>
                      <div class="product-status soldout" id="J_productStatus">
                        <span class="init">正在加载...</span>
                        <span class="sale">有现货</span>
                        <span class="over">该地区暂时缺货</span>
                        <span class="no">暂时无法送达</span>
                        <span class="pre">预售商品</span>
                        <span class="book">预售商品</span>
                        <span class="nohasAddress"></span>
                        <span class="time"></span>
                      </div>
                    </div>
                  </div-->

                  <h2>选择规格</h2>

                  <a-row>
                    <a-col
                      v-for="item in formulas"
                      :key="item.key"
                      :span="12"
                      :style="{'padding': '10px 5px'}"
                    >
                      <!-- 'border-color': '#ff6700', -->
                      <a-button
                        :style="buttonActiveId === item.id ? buttonType.active : buttonType.normal"
                        @click="checkedFormula(item)"
                      >{{item.title}}</a-button>
                    </a-col>
                  </a-row>

                  <h2>选择数量</h2>

                  <a-row>
                    <a-col :span="24" :style="{'padding': '10px 5px'} ">
                      <a-input-number v-model="count" :min="1" :max="10" style="margin: 0 5px;" />
                    </a-col>
                  </a-row>

                  <a-row>
                    <a-col :span="24" :style="{'padding': '10px 5px'} ">
                      <div class="buy-tip">
                        <p>
                          总计
                          <span style="color:#ff6700;font-size:24px;">{{price}}</span> 积分
                        </p>
                      </div>
                    </a-col>
                  </a-row>

                  <a-row>
                    <!--
                    <a-col :span="12" :style="{'padding': '10px 5px'} ">
                      <a-button
                        :style="{'width': '100%', 'height': '64px', 'border-color': '#ff6700', 'color': '#ff6700', 'border-radius': '0'}"
                      >加入购物车</a-button>
                    </a-col>
                    -->
                    <a-col :span="12" :style="{'padding': '10px 5px'}">
                      <a-button
                        :style="{'width': '100%', 'height': '64px', 'border-color': '#ff6700', 'background-color': '#ff6700', 'color': '#ffffff', 'border-radius': '0'}"
                        @click="submitOrder"
                        :loading="submitting"
                      >立即购买</a-button>
                    </a-col>
                  </a-row>
                </a-col>
              </a-row>
            </a-tab-pane>
          </a-tabs>
        </a-col>
      </a-row>
    </a-spin>
  </div>
</template>
<script>
const specificationData = [];
const buttonType = {
  normal: { width: "100%", "border-radius": "0" },
  active: { width: "100%", "border-radius": "0", "border-color": "#ff6700" }
};

export default {
  name: "ProductPage",
  props: {
    id: String
  },
  data() {
    return {
      specificationData: specificationData,
      innerId: 0,
      loading: false,
      commodity: {},
      formulas: [],
      profile: {},
      tags: [],
      buttonActiveId: 0,
      buttonActiveTitle: "",
      price: "",
      buttonType,
      address: {},
      count: 1,
      submitting: false,
      images: []
    };
  },
  mounted() {
    if (!this.id) {
      const url = window.location.href;
      const lastSlah = url.lastIndexOf("/");
      this.innerId = url.substring(lastSlah + 1, url.length);
    } else {
      this.innerId = JSON.parse(JSON.stringify(this.id));
    }
    this.loadCommodity();
    let timeout = setTimeout(() => {
      this.getDefaultAddress();
    }, 500);
  },
  methods: {
    onTabChange() {},
    loadCommodity() {
      const url = `${this.apiUrl}/public/home/commodity/${this.innerId}`;
      this.loading = true;
      this.$g
        .get(url)
        .cb(data => {
          console.log("load --->", data);
          if (data.status === 0) {
            this.commodity = data.item["commodity"];
            this.formulas = data.item["formulas"];
            this.profile = data.item["profile"];
            if (this.profile["images"]) {
              this.images = this.profile["images"].split(",");
            } else {
              this.images = [this.commodity["thumbnail"]];
            }
            let f = this.formulas[0];
            this.price = f["price"]["amount"];
            const spec = JSON.parse(this.profile.specification);
            let specList = [];
            for (let index in spec) {
              let item = spec[index];
              specList.push({
                key: index,
                name: item["title"],
                text: item["content"]
              });
            }
            this.specificationData = specList;
            this.tags = data.item["tags"];
          } else {
            console.error("load commodity --->", data);
          }
        })
        .fcb(() => (this.loading = false))
        .req();
    },
    checkedFormula(formula) {
      this.buttonActiveId = formula.id;
      this.buttonActiveTitle = formula.title;
      this.price = formula["price"]["amount"];
    },
    submitOrder() {
      if (this.buttonActiveId === 0) {
        this.$message.warning("请选择套餐");
        return;
      }
      const url = `${this.apiUrl}/frontend/direct-order`;
      const fd = new FormData();
      fd.append("address", encodeURIComponent(this.address.info));
      fd.append("cid", this.commodity.id);
      fd.append("fid", this.buttonActiveId);
      fd.append("count", this.count);
      this.submitting = true;
      this.$g
        .post(url, fd)
        .cb(data => {
          if (data.status === 0) {
            this.$router.push(`/buy/${data.item}`);
          } else {
            this.$message.error(data.message);
            console.error(data);
          }
        })
        .fcb(() => (this.submitting = false))
        .req();
    },
    getDefaultAddress() {
      const url = `${this.apiUrl}/frontend/default-address`;
      this.$g
        .get(url)
        .cb(data => {
          if (data.status === 0) {
            this.address = data.item;
            console.log("default address --->", this.address, data);
          } else {
            this.address = false;
            console.error("load address --->", data);
          }
        })
        .fcb()
        .req();
    }
  }
};
</script>
<style scoped>
h2 {
  margin: 20px 0;
}
.buy-tip {
  height: 64px;
  border: 1px solid #e0e0e0;
  background-color: #fafafa;
  padding: 10px 20px;
}

.address-wrap {
  margin-top: 40px;
  margin-bottom: 40px;
}
.address-wrap .user-default-address {
  background: #fafafa;
  border: 1px solid #e0e0e0;
  padding-left: 50px;
}
.user-default-address {
  padding: 30px 40px 30px 32px;
  padding-left: 32px;
  border-top: 1px solid #e0e0e0;
  position: relative;
}
.user-default-address .address-info {
  display: inline-block;
  *zoom: 1;
  *display: inline;
}
.user-default-address .address-info .item {
  margin-right: 14px;
}
.user-default-address .switch-choose-regions {
  line-height: 1;
  color: #ff6700;
  cursor: pointer;
}
.user-default-address .product-status {
  color: #b0b0b0;
}
.user-default-address .soldout .over {
  display: inline-block;
  *zoom: 1;
  *display: inline;
}
.ant-carousel >>> .slick-slide {
  text-align: center;
  height: 460px;
  line-height: 460px;
  background: #ffffff;
  overflow: hidden;
}

.ant-carousel >>> .slick-slide h3 {
  color: #fff;
}

.ant-carousel >>> .slick-dots {
  height: auto;
}
.ant-carousel >>> .slick-slide img {
  border: 5px solid #fff;
  display: block;
  margin: auto;
  max-width: 80%;
}
.ant-carousel >>> .slick-thumb {
  bottom: -45px;
}
.ant-carousel >>> .slick-thumb li {
  width: 60px;
  height: 45px;
}
.ant-carousel >>> .slick-thumb li img {
  width: 100%;
  height: 100%;
  filter: grayscale(100%);
}
.ant-carousel >>> .slick-thumb li.slick-active img {
  filter: grayscale(0%);
}
</style>


