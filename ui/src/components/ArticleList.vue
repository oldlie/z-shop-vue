<template>
  <a-list itemLayout="vertical" size="large" :pagination="pagination" :dataSource="listData">
    <a-list-item
      slot="renderItem"
      slot-scope="item"
      key="item.title"
      :style="{'text-align': 'left'}"
    >
      <template slot="actions" v-for="{type, text} in actions">
        <span :key="type">
          <a-icon :type="type" style="margin-right: 8px" />
          {{text}}
        </span>
      </template>
      <img slot="extra" width="272" alt="logo" :src="item.imageUrl" />
      <a-list-item-meta :description="item.description">
        <div slot="title" :href="item.href">
          <!--
          <a-tag color="#f50">#f50</a-tag>
          <a-tag color="#2db7f5">#2db7f5</a-tag>
          <a-tag color="#87d068">#87d068</a-tag>
          <a-tag color="#108ee9">#108ee9</a-tag>
          -->
          <router-link :to="{ path: '/article/1'}">{{item.title}}</router-link>
        </div>
      </a-list-item-meta>
      {{item.summary}}
    </a-list-item>
  </a-list>
</template>
<script>
const listData = [];
for (let i = 0; i < 23; i++) {
  listData.push({
    href: "https://vue.ant.design/",
    title: `ant design vue part ${i}`,
    avatar: "https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png",
    description:
      "Ant Design, a design language for background applications, is refined by Ant UED Team.",
    content:
      "We supply a series of design principles, practical patterns and high quality design resources (Sketch and Axure), to help people create their product prototypes beautifully and efficiently."
  });
}

export default {
  name: "ArticleList",
  props: {
    articles: Array
  },
  data() {
    return {
      listData,
      pagination: {
        onChange: page => {
          console.log(page);
        },
        pageSize: 10
      },
      actions: [
        { type: "star-o", text: "156" },
        { type: "like-o", text: "156" },
        { type: "message", text: "2" }
      ]
    };
  },
  watch: {
    articles(nv, ov) {
      this.listData = JSON.parse(JSON.stringify(this.articles));
    }
  },
  mounted() {
    this.listData = JSON.parse(JSON.stringify(this.articles));
  }
};
</script>
<style scoped>
</style>


