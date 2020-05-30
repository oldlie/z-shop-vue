<template>
  <a-spin :spinning="loading">
    <a-row>
      <a-col
        :span="16"
        :style="{ background: '#fff', padding: '24px', minHeight: '580px', 'margin': '30px 0','text-align': 'left' }"
      >
        <h1>{{article.title}}</h1>

        <a-divider></a-divider>
        <div style="color:#888">
          <span>{{article.createDate}}</span>
          <a-divider type="vertical" />
          <span>{{article.author}}</span>
          <a-divider type="vertical" />
          <!--
          <span slot="actions" v-for="{type, text} in actions" :key="type">
            <span :key="type">
              <a-icon :type="type" style="margin-right: 8px" />
              {{text}}
            </span>
            <a-divider type="vertical" />
          </span>
          -->
        </div>
        <a-divider></a-divider>

        <blockquote style="color:#888">{{article.summary}}</blockquote>

        <div class="article" id="article">
          <div v-html="article.content"></div>
          <p class="show_author">责任编辑：{{article.publisher}}</p>
        </div>
      </a-col>
      <a-col :span="8" :style="{'padding': '0 0 0 10px','margin': '30px 0',}">
        <!--
        <div
          style="background:#fff;width:100%;min-height:300px;margin:0 0 10px 0;text-align:left;padding:10px;"
        >
          <h2>重要通知</h2>
          <a-list :dataSource="noticise">
            <a-list-item slot="renderItem" slot-scope="item" :style="{'border-bottom':'none'}">
              <a-row style="width:100%;">{{item.title}}</a-row>
            </a-list-item>
          </a-list>
        </div>
        -->
        <div
          style="background:#fff;width:100%;min-height:300px;margin:0 0 10px 0;text-align:left;padding:10px;"
        >
          <h2>最新资讯</h2>
          <a-list :dataSource="newArticles">
            <a-list-item slot="renderItem" slot-scope="item" :style="{'border-bottom':'none'}">
              <a-row style="width:100%;">
                <!-- <router :to="{ path: `/article/${item.id}`, params: {id: item.id}}">{{item.title}}</router-link> -->
                <a target="_blank" :href="`#/article/${item.id}`">{{item.title}}</a>
              </a-row>
            </a-list-item>
          </a-list>
        </div>
      </a-col>
    </a-row>
  </a-spin>
</template>
<script>
const noticise = [
  { title: " 【紧急通知】 这董事长要求秘书每天按“手册”为其.... " },
  { title: " 【网站通知】 如何看待舰载型直20直升机迟迟不露面  " },
  { title: " 【物流通知】 殡葬业暴利：有公司殡葬业务毛利率88.. " },
  { title: " 【物流通知】 西甲-武磊连续3轮首发 西班牙人2-1  " },
  { title: " 【物流通知】 《芳华》被诉抄袭冯小刚等遭索赔300万  " }
];
const newArticles = [
];

export default {
  name: "ArticlePage",
  props: {
    id: Number
  },
  data() {
    return {
      loading: false,
      innerId: 0,
      actions: [
        { type: "star-o", text: "156" },
        { type: "like-o", text: "156" },
        { type: "message", text: "2" }
      ],
      newArticles,
      noticise,
      article: {}
    };
  },
  watch: {
    id (nv, ov) {
      console.log('id changed --->', nv)
      this.innerId = nv;
      this.load();
    },
    innerId (nv, ov) {
      console.log('inner id changed --->', nv);
    }
  },
  mounted() {
    console.log(this.actions);
    if (!this.id) {
      console.log("url --->", window.location.href);
      const url = window.location.href;
      const lastSlah = url.lastIndexOf("/");
      this.innerId = url.substring(lastSlah + 1, url.length);
      console.log(this.innerId);
    } else {
      this.innerId = JSON.parse(JSON.stringify(this.id));
    }
    this.load();
    var timeOut = setTimeout(() => {
      const url = `${this.apiUrl}/public/home/articles/latest`;
      this.$g
        .get(url)
        .cb(data => {
          if (data.status === 0) {
            this.newArticles = data.list;
          } else {
            console.error("load latest articles --->", newArticles);
          }
        })
        .fcb()
        .req();
      clearTimeout(timeOut);
    }, 1000);
  },
  methods: {
    load() {
      const url = `${this.apiUrl}/public/home/article/${this.innerId}`;
      this.loading = true;
      this.$g
        .get(url)
        .cb(data => {
          if (data.status === 0) {
            this.article = data.item;
          } else {
            console.error("load article error -->", data);
          }
        })
        .fcb(() => {
          this.loading = false;
        })
        .req();
    }
  }
};
</script>
<style scoped>
</style>


