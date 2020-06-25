<template>
  <div style="width:100%;height:460px;clear:both;margin:20px 0 30px 0;">
    <div style="width: 100%; display: inline-block;position:relative;margin: 0 0;">
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
        <div v-for="(item, index) in list" :key="index">
          <a :href="item.url" target="_blank">
            <img
              :src="item.imageUrl"
              :style="{height:'460px',width:'1200px','min-width':'247px','min-height':'186px'}"
            />
          </a>
        </div>
      </a-carousel>
    </div>

    <!--
    <a-menu
      style="width: 280px;height:460px;float:left;text-align:left;"
      mode="vertical"
      @click="handleClick"
    >
      <a-sub-menu key="1">
        <span slot="title">
          <span>水产品</span>
        </span>
        <a-menu-item key="2">
          <img
            width="32px"
            height="32px"
            alt="Example"
            src="https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png"
          />
          <span style="padding:5px">阳澄湖大闸蟹</span>
        </a-menu-item>
        <a-menu-item key="3">
          <img
            width="32px"
            height="32px"
            alt="Example"
            src="https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png"
          />
          <span style="padding:5px">阳澄湖大闸蟹</span>
        </a-menu-item>
        <a-menu-item key="4">
          <img
            width="32px"
            height="32px"
            alt="Example"
            src="https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png"
          />
          <span style="padding:5px">阳澄湖大闸蟹</span>
        </a-menu-item>
        <a-menu-item key="5">
          <img
            width="32px"
            height="32px"
            alt="Example"
            src="https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png"
          />
          <span style="padding:5px">阳澄湖大闸蟹</span>
        </a-menu-item>
      </a-sub-menu>
      <a-sub-menu key="6">
        <span slot="title">
          <span>水果</span>
        </span>
        <a-menu-item key="7">
          <img
            width="32px"
            height="32px"
            alt="Example"
            src="https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png"
          />
          <span style="padding:5px">阳澄湖大闸蟹</span>
        </a-menu-item>
        <a-menu-item key="8">
          <img
            width="32px"
            height="32px"
            alt="Example"
            src="https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png"
          />
          <span style="padding:5px">阳澄湖大闸蟹</span>
        </a-menu-item>
        <a-menu-item key="9">
          <img
            width="32px"
            height="32px"
            alt="Example"
            src="https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png"
          />
          <span style="padding:5px">阳澄湖大闸蟹</span>
        </a-menu-item>
        <a-menu-item key="10">
          <img
            width="32px"
            height="32px"
            alt="Example"
            src="https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png"
          />
          <span style="padding:5px">阳澄湖大闸蟹</span>
        </a-menu-item>
      </a-sub-menu>
      <a-sub-menu key="11">
        <span slot="title">
          <span>零食</span>
        </span>
        <a-menu-item key="12">
          <img
            width="32px"
            height="32px"
            alt="Example"
            src="https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png"
          />
          <span style="padding:5px">阳澄湖大闸蟹</span>
        </a-menu-item>
        <a-menu-item key="13">
          <img
            width="32px"
            height="32px"
            alt="Example"
            src="https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png"
          />
          <span style="padding:5px">阳澄湖大闸蟹</span>
        </a-menu-item>
        <a-menu-item key="14">
          <img
            width="32px"
            height="32px"
            alt="Example"
            src="https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png"
          />
          <span style="padding:5px">阳澄湖大闸蟹</span>
        </a-menu-item>
        <a-menu-item key="15">
          <img
            width="32px"
            height="32px"
            alt="Example"
            src="https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png"
          />
          <span style="padding:5px">阳澄湖大闸蟹</span>
        </a-menu-item>
      </a-sub-menu>
      <a-sub-menu key="16">
        <span slot="title">
          <span>生活用品</span>
        </span>
        <a-menu-item key="17">阳澄湖大闸蟹</a-menu-item>
        <a-menu-item key="18">阳澄湖大闸蟹</a-menu-item>
        <a-menu-item key="19">阳澄湖大闸蟹</a-menu-item>
        <a-menu-item key="20">阳澄湖大闸蟹</a-menu-item>
        <a-menu-item key="17">阳澄湖大闸蟹</a-menu-item>
        <a-menu-item key="18">阳澄湖大闸蟹</a-menu-item>
        <a-menu-item key="19">阳澄湖大闸蟹</a-menu-item>
        <a-menu-item key="20">阳澄湖大闸蟹</a-menu-item>
      </a-sub-menu>
      <a-sub-menu key="1">
        <span slot="title">
          <span>水产品</span>
        </span>
        <a-menu-item key="2">
          <img
            width="32px"
            height="32px"
            alt="Example"
            src="https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png"
          />
          <span style="padding:5px">阳澄湖大闸蟹</span>
        </a-menu-item>
        <a-menu-item key="3">
          <img
            width="32px"
            height="32px"
            alt="Example"
            src="https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png"
          />
          <span style="padding:5px">阳澄湖大闸蟹</span>
        </a-menu-item>
        <a-menu-item key="4">
          <img
            width="32px"
            height="32px"
            alt="Example"
            src="https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png"
          />
          <span style="padding:5px">阳澄湖大闸蟹</span>
        </a-menu-item>
        <a-menu-item key="5">
          <img
            width="32px"
            height="32px"
            alt="Example"
            src="https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png"
          />
          <span style="padding:5px">阳澄湖大闸蟹</span>
        </a-menu-item>
      </a-sub-menu>
      <a-sub-menu key="6">
        <span slot="title">
          <span>水果</span>
        </span>
        <a-menu-item key="7">
          <img
            width="32px"
            height="32px"
            alt="Example"
            src="https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png"
          />
          <span style="padding:5px">阳澄湖大闸蟹</span>
        </a-menu-item>
        <a-menu-item key="8">
          <img
            width="32px"
            height="32px"
            alt="Example"
            src="https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png"
          />
          <span style="padding:5px">阳澄湖大闸蟹</span>
        </a-menu-item>
        <a-menu-item key="9">
          <img
            width="32px"
            height="32px"
            alt="Example"
            src="https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png"
          />
          <span style="padding:5px">阳澄湖大闸蟹</span>
        </a-menu-item>
        <a-menu-item key="10">
          <img
            width="32px"
            height="32px"
            alt="Example"
            src="https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png"
          />
          <span style="padding:5px">阳澄湖大闸蟹</span>
        </a-menu-item>
      </a-sub-menu>
      <a-sub-menu key="11">
        <span slot="title">
          <span>零食</span>
        </span>
        <a-menu-item key="12">
          <img
            width="32px"
            height="32px"
            alt="Example"
            src="https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png"
          />
          <span style="padding:5px">阳澄湖大闸蟹</span>
        </a-menu-item>
        <a-menu-item key="13">
          <img
            width="32px"
            height="32px"
            alt="Example"
            src="https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png"
          />
          <span style="padding:5px">阳澄湖大闸蟹</span>
        </a-menu-item>
        <a-menu-item key="14">
          <img
            width="32px"
            height="32px"
            alt="Example"
            src="https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png"
          />
          <span style="padding:5px">阳澄湖大闸蟹</span>
        </a-menu-item>
        <a-menu-item key="15">
          <img
            width="32px"
            height="32px"
            alt="Example"
            src="https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png"
          />
          <span style="padding:5px">阳澄湖大闸蟹</span>
        </a-menu-item>
      </a-sub-menu>
      <a-sub-menu key="16">
        <span slot="title">
          <span>生活用品</span>
        </span>
        <a-menu-item key="17">阳澄湖大闸蟹</a-menu-item>
        <a-menu-item key="18">阳澄湖大闸蟹</a-menu-item>
        <a-menu-item key="19">阳澄湖大闸蟹</a-menu-item>
        <a-menu-item key="20">阳澄湖大闸蟹</a-menu-item>
        <a-menu-item key="17">阳澄湖大闸蟹</a-menu-item>
        <a-menu-item key="18">阳澄湖大闸蟹</a-menu-item>
        <a-menu-item key="19">阳澄湖大闸蟹</a-menu-item>
        <a-menu-item key="20">阳澄湖大闸蟹</a-menu-item>
      </a-sub-menu>
    </a-menu>
    -->
  </div>
</template>
<script>
const baseUrl =
  "https://raw.githubusercontent.com/vueComponent/ant-design-vue/master/components/vc-slick/assets/img/react-slick/";

export default {
  name: "HomeCarousel",
  data() {
    return {
      baseUrl,
      list: []
    };
  },
  mounted() {
    this.load();
  },
  methods: {
    load() {
      const url = this.apiUrl + "/public/home/carousels";
      this.$g
        .get(url)
        .cb(data => {
          if (data.status === 0) {
            this.list = data.list;
          } else {
            console.error("load carousels:", data);
          }
        })
        .fcb()
        .req();
    },
    handleClick() {},
    getImgUrl(i) {
      return `${baseUrl}abstract0${i + 1}.jpg`;
    },
    onChange() {}
  }
};
</script>
<style scoped>
.ant-carousel .custom-slick-arrow {
  width: 48px;
  height: 48px;
  font-size: 48px;
  color: #fff;
  background-color: rgba(31, 45, 61, 0.11);
  opacity: 0.3;
}
.ant-carousel .custom-slick-arrow:before {
  display: none;
}
.ant-carousel .custom-slick-arrow:hover {
  opacity: 0.5;
}

.ant-carousel .slick-slide h3 {
  color: #ff6700;
}

.ant-carousel .slick-dots {
  height: auto;
}
.ant-carousel .slick-slide img {
  /*border: 5px solid #fff;*/
  display: block;
  margin: auto;
  max-width: 100%;
}
.ant-carousel .slick-thumb {
  bottom: -45px;
}
.ant-carousel .slick-thumb li {
  width: 60px;
  height: 45px;
}
.ant-carousel .slick-thumb li img {
  width: 100%;
  height: 100%;
  filter: grayscale(100%);
}
.ant-carousel .slick-thumb li.slick-active img {
  filter: grayscale(0%);
}
</style>
