<template>
  <div id="productComment">
    <a-row style="margin:30px 0" :gutter="8">
      <a-col :span="4" v-for="fav in favoriteList" :key="fav['id']">
        <div class="fav-button">
          <span>{{fav['title']}}({{fav['count']}})</span>
        </div>
      </a-col>
    </a-row>
    <a-row :gutter="8" style="margin:10px 0;padding:10px 0;height:480px;">
      <a-col :span="16">
        <a-row style="margin: 10px 0 50px 0;" v-for="comment in commentList" :key="comment['id']">
          <a-col :span="6" :style="{padding:'5px 0'}">
            <a-avatar shape="square" :size="64" icon="user"/>
          </a-col>
          <a-col :span="18" style="text-align:left;">
            <div style="width:100%;padding:5px 0;">
              {{comment['nickname']}}
            </div>
            <div style="width:100%;padding:5px 0;color:#b0b0b0">{{comment['createTime']}}</div>
            <div style="width:100%;padding:5px 0;">{{comment['evaText']}}</div>
          </a-col>
        </a-row>
        <a-row style="margin: 10px 0 50px 0;" v-if="total > count">
          <a-col :span="24">
            <div class="fav-button active" @click="next">
              <span>更多评论</span>
            </div>
          </a-col>
        </a-row>
      </a-col>
      <a-col :span="8">
        <div style="height:128px;width:100%;border:1px sold ##f0f2f5">
          <div style="width:100%;">
            <span style="font-size:48px;font-weight:bold;color:#ff6700">{{buyCount}}</span>
            <span style="color:#b0b0b0">人购买后满意</span>
          </div>
          <div style="width:100%;">
            <a-progress type="circle" :percent="buyScore" :format="() => `满意度${buyScore}%`" status="success"/>
          </div>
        </div>
      </a-col>
    </a-row>
  </div>
</template>
<script>
const favoriteList = [];
const commentList = [
  
];
export default {
  name: "productComment",
  props: ["id"],
  data() {
    return {
      favoriteList,
      commentList,
      buyCount: 0,
      buyScore: 100,
      total: 0,
      count: 0,
      page: 1,
      size: 10
    };
  },
  mounted() {
    this.loadInfo();
  },
  methods: {
    loadInfo () {
      const url = `${this.apiUrl}public/home/commodity/comment/info?cid=${this.id}`;
      this.$g.get(url)
      .cb(data => {
        if (data.status === 0) {
          this.favoriteList = data.item['eva'];
          this.commentList = data.item['comments'];
          this.buyCount = data.item['buyCount'];
          this.buyScore = data.item['buyScore'];
          this.total = data.item['total'];
          this.count = this.commentList.length;
        }
      })
      .fcb()
      .req();
    },
    next () {
      this.page++;
      const url = `${this.apiUrl}public/home/commodity/comments?cid=${this.id}&page=${this.page}&size=${this.size}`;
      this.$g.get(url)
      .cb(data => {
        if (data.status === 0) {
          let temp = JSON.parse(JSON.stringify(this.commentList));
          for (let i in data.list) {
            temp.push(data.list[i]);
          }
          this.commentList = temp;
          this.count = this.commentList.length;
        }
      })
      .fcb()
      .req(); 
     
    }
  },
};
</script>
<style scoped>
.fav-button {
  cursor: pointer;
  width: 100%;
  height: 48px;
  color: #ff6700;
  border: 1px solid #ff6700;
  padding: 12px 0 10px 0;
}
.fav-button:hover {
  color: #ffffff;
  background: #ff6700;
}
.fav-button.active {
  color: #ffffff;
  background: #ff6700;
}
</style>


