<template>
  <div :style="{ background: '#fff', padding: '24px', minHeight: '280px', 'margin': '30px 0' }">
    <a-row>
      <a-col :span="12" :style="{'text-align': 'left'}">
        <a-breadcrumb>
          <a-breadcrumb-item>Home</a-breadcrumb-item>
          <a-breadcrumb-item>
            <a href>Application Center</a>
          </a-breadcrumb-item>
          <a-breadcrumb-item>
            <a href>Application List</a>
          </a-breadcrumb-item>
          <a-breadcrumb-item>An Application</a-breadcrumb-item>
        </a-breadcrumb>
      </a-col>
      <a-col :span="24">
        <a-tabs defaultActiveKey="3" @change="onTabChange">
          <a-tab-pane tab="概述" key="1">
            <img src="http://localhost/resoucres/images/blue.jpg" alt width="100%">

            <div class="txt">
              <div class="tit1 webfont transi-up">全新 小米9 透明版</div>
              <div class="tit2 webfont transi-up delay-05">8GB 大内存+256GB 大容量</div>
              <!--            <div class="tit3 webfont transi-up delay-10">12GB 内存手机</div>-->
              <div class="des webfont transi-up delay-15">
                体验极好的小米9 带来一个全新版本：小米9 透明版。
                <br>拥有与透明尊享版一样的透明机甲机身，
                <br>极具未来感的外观设计兼顾大内存与大容量，给你一次体验上的超快感表现。
              </div>
            </div>
            <div class="con transi-up delay-20">
              <div class="ui-wrapper" style="max-width: 100%;">
                <div
                  class="ui-viewport"
                  style="width: 100%; overflow: hidden; position: relative; height: 471px;"
                >
                  <ul class="list clearfix J_pic3" style="width: auto; position: relative;">
                    <li
                      class="slider"
                      style="float: none; list-style: outside none none; position: absolute; width: 590px; z-index: 50; display: block;"
                    >
                      <img
                        data-src="//i1.mifile.cn/f/i/2019/mi9/index/dear.jpg?v=2"
                        src="https://i1.mifile.cn/f/i/2019/mi9/index/dear.jpg?v=2"
                      >
                    </li>
                  </ul>
                </div>
                <div class="ui-controls ui-has-pager">
                  <div class="ui-pager"></div>
                </div>
              </div>
            </div>
          </a-tab-pane>
          <a-tab-pane tab="参数" key="2">
            <a-list :dataSource="specificationData">
              <a-list-item slot="renderItem" slot-scope="item">
                <a-row style="width:100%;">
                  <a-col :span="12" style="text-align:center;">
                    <h3>{{item.name}}</h3>
                  </a-col>
                  <a-col :span="12" style="text-align:left;">
                    <p>{{item.text}}</p>
                  </a-col>
                </a-row>
              </a-list-item>
            </a-list>
          </a-tab-pane>
          <a-tab-pane tab="用户评价" key="3">
            <product-comment></product-comment>
          </a-tab-pane>
          <a-tab-pane tab="立即购买" key="4" forceRender>
            <a-row>
              <a-col :span="12">
                <div id="J_sliderView" class="sliderWrap" style="width: auto; position: relative;">
                  <img
                    data-src="//i8.mifile.cn/a1/pms_1550642240.48638886.jpg"
                    class="slider done"
                    style="float: none; list-style: outside none none; width: 540px; z-index: 50; display: block;"
                    src="//i8.mifile.cn/a1/pms_1550642240.48638886.jpg"
                  >
                </div>
              </a-col>
              <a-col :span="12" :style="{'text-align': 'left'}">
                <h1>小米9</h1>
                <p>
                  <span style="color:#ff6700">「4月16日上午10点开售」</span>全息幻彩全曲面玻璃机身 / 骁龙855旗舰处理器 / 索尼4800万超广角微距三摄 / 20W无线闪充 / 第5代极速屏下指纹解锁
                </p>
                <div class="J_addressWrap address-wrap">
                  <div class="user-default-address" id="J_userDefaultAddress">
                    <a-icon type="environment"/>
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
                </div>

                <h2>选择规格</h2>

                <a-row>
                  <a-col :span="12" :style="{'padding': '10px 5px'}">
                    <a-button
                      :style="{'width': '100%','border-color': '#ff6700', 'border-radius': '0'}"
                    >规格一</a-button>
                  </a-col>
                  <a-col :span="12" :style="{'padding': '10px 5px'}">
                    <a-button
                      :style="{'width': '100%','border-color': '#e0e0e0', 'border-radius': '0'}"
                    >规格二</a-button>
                  </a-col>
                  <a-col :span="12" :style="{'padding': '10px 5px'}">
                    <a-button
                      :style="{'width': '100%','border-color': '#e0e0e0', 'border-radius': '0'}"
                    >规格三</a-button>
                  </a-col>
                </a-row>

                <h2>选择数量</h2>

                <a-row>
                  <a-col :span="24" :style="{'padding': '10px 5px'} ">
                    <a-input-number :min="1" :max="10" style="margin: 0 5px;"/>
                  </a-col>
                </a-row>

                <a-row>
                  <a-col :span="24" :style="{'padding': '10px 5px'} ">
                    <div class="buy-tip">
                      <p>
                        总计
                        <span style="color:#ff6700;font-size:24px;">1999</span> 积分
                      </p>
                    </div>
                  </a-col>
                </a-row>

                <a-row>
                  <a-col :span="12" :style="{'padding': '10px 5px'} ">
                    <a-button
                      :style="{'width': '100%', 'height': '64px', 'border-color': '#ff6700', 'color': '#ff6700', 'border-radius': '0'}"
                    >加入购物车</a-button>
                  </a-col>
                  <a-col :span="12" :style="{'padding': '10px 5px'}">
                    <a-button
                      :style="{'width': '100%', 'height': '64px', 'border-color': '#ff6700', 'background-color': '#ff6700', 'color': '#ffffff', 'border-radius': '0'}"
                    >立即购买</a-button>
                  </a-col>
                </a-row>
              </a-col>
            </a-row>
          </a-tab-pane>
        </a-tabs>
      </a-col>
    </a-row>
  </div>
</template>
<script>
const specificationData = [
  {
    key: "1",
    name: "品种",
    text: "3摄Ai手机"
  },
  {
    key: "2",
    name: "特征",
    text: "水果貝殼冰，海菜冰我都覺得還不錯吃，但是有點酸酸但又不是很甜又沒煉乳的仙人掌冰我個人就覺得還好，海菜冰比較特別卻又不會奇怪，我個人會推薦點海菜冰吃看看。至於水果煉乳冰當然就沒什麼好嫌棄，用各種新鮮水果品質還不錯，在這樣的離岸小島，我真心覺得很用心了啊！"
  },
  {
    key: "3",
    name: "原产地",
    text: "中国台湾澎湖地区"
  },
  {
    key: "4",
    name: "生产日期",
    text: "下单后3天内"
  },
  {
    key: "5",
    name: "包装规格",
    text: "每只不小于50g"
  },
  {
    key: "6",
    name: "存储方式",
    text: "3摄Ai手机"
  }
];

export default {
  name: "ProductPage",
  props: {
    id: String
  },
  data() {
    return {
      specificationData: specificationData
    };
  },
  mounted() {
    console.log(this.id);
  },
  methods: {
    onTabChange() {}
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
</style>


