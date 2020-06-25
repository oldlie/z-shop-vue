<template>
  <div id="productComment">
    <a-row style="margin:30px 0" :gutter="8">
      <a-col :span="4">
        <div class="fav-button active">
          <span>全部</span>
        </div>
      </a-col>
      <a-col :span="4" v-for="fav in favoriteList" :key="fav['id']">
        <div class="fav-button">
          <span>{{fav['text']}}({{fav['count']}})</span>
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
              <a-tag color="#f50">满意</a-tag>
            </div>
            <div style="width:100%;padding:5px 0;color:#b0b0b0">{{comment['createTime']}}</div>
            <div style="width:100%;padding:5px 0;">{{comment['comment']}}</div>
          </a-col>
        </a-row>
        <a-row style="margin: 10px 0 50px 0;">
          <a-col :span="24">
            <div class="fav-button active">
              <span>更多评论</span>
            </div>
          </a-col>
        </a-row>
      </a-col>
      <a-col :span="8">
        <div style="height:128px;width:100%;border:1px sold ##f0f2f5">
          <div style="width:100%;">
            <span style="font-size:48px;font-weight:bold;color:#ff6700">34579</span>
            <span style="color:#b0b0b0">人购买后满意</span>
          </div>
          <div style="width:100%;">
            <a-progress type="circle" :percent="97" :format="() => '满意度97%'" status="success"/>
          </div>
        </div>
      </a-col>
    </a-row>
  </div>
</template>
<script>
const favoriteList = [
  { id: 0, text: "值得拥有", count: '37210' },
  { id: 1, text: "手感很棒", count: '29874' },
  { id: 2, text: "外观漂亮", count: '22858' },
  { id: 3, text: "拍照好", count: '20008' },
  { id: 4, text: "速度快", count: '19874' }
];
const commentList = [
  {
    id: 0,
    nickname: "布朗熊",
    createTime: new Date(),
    comment:
      "很不错喜欢挺好的拍照也好看清晰漂亮很不错喜欢挺好的拍照也好看清晰漂亮很不错喜欢挺好的拍照也好看清晰漂亮很不错喜欢挺好的拍照也好看清晰漂亮"
  },
  {
    id: 1,
    nickname: "布朗熊",
    createTime: new Date(),
    comment:
      "很不错喜欢挺好的拍照也好看清晰漂亮很不错喜欢挺好的拍照也好看清晰漂亮很不错喜欢挺好的拍照也好看清晰漂亮很不错喜欢挺好的拍照也好看清晰漂亮"
  },
  {
    id: 2,
    nickname: "布朗熊",
    createTime: new Date(),
    comment:
      "很不错喜欢挺好的拍照也好看清晰漂亮很不错喜欢挺好的拍照也好看清晰漂亮很不错喜欢挺好的拍照也好看清晰漂亮很不错喜欢挺好的拍照也好看清晰漂亮"
  },
  {
    id: 3,
    nickname: "布朗熊",
    createTime: new Date(),
    comment:
      "很不错喜欢挺好的拍照也好看清晰漂亮很不错喜欢挺好的拍照也好看清晰漂亮很不错喜欢挺好的拍照也好看清晰漂亮很不错喜欢挺好的拍照也好看清晰漂亮"
  },
  {
    id: 4,
    nickname: "布朗熊",
    createTime: new Date(),
    comment:
      "很不错喜欢挺好的拍照也好看清晰漂亮很不错喜欢挺好的拍照也好看清晰漂亮很不错喜欢挺好的拍照也好看清晰漂亮很不错喜欢挺好的拍照也好看清晰漂亮"
  },
  {
    id: 5,
    nickname: "布朗熊",
    createTime: new Date(),
    comment:
      "很不错喜欢挺好的拍照也好看清晰漂亮很不错喜欢挺好的拍照也好看清晰漂亮很不错喜欢挺好的拍照也好看清晰漂亮很不错喜欢挺好的拍照也好看清晰漂亮"
  },
  {
    id: 6,
    nickname: "布朗熊",
    createTime: new Date(),
    comment:
      "很不错喜欢挺好的拍照也好看清晰漂亮很不错喜欢挺好的拍照也好看清晰漂亮很不错喜欢挺好的拍照也好看清晰漂亮很不错喜欢挺好的拍照也好看清晰漂亮"
  },
  {
    id: 7,
    nickname: "布朗熊",
    createTime: new Date(),
    comment:
      "很不错喜欢挺好的拍照也好看清晰漂亮很不错喜欢挺好的拍照也好看清晰漂亮很不错喜欢挺好的拍照也好看清晰漂亮很不错喜欢挺好的拍照也好看清晰漂亮"
  },
  {
    id: 8,
    nickname: "布朗熊",
    createTime: new Date(),
    comment:
      "很不错喜欢挺好的拍照也好看清晰漂亮很不错喜欢挺好的拍照也好看清晰漂亮很不错喜欢挺好的拍照也好看清晰漂亮很不错喜欢挺好的拍照也好看清晰漂亮"
  },
  {
    id: 9,
    nickname: "布朗熊",
    createTime: new Date(),
    comment:
      "很不错喜欢挺好的拍照也好看清晰漂亮很不错喜欢挺好的拍照也好看清晰漂亮很不错喜欢挺好的拍照也好看清晰漂亮很不错喜欢挺好的拍照也好看清晰漂亮"
  }
];
export default {
  name: "productComment",
  data() {
    return {
      favoriteList,
      commentList
    };
  }
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


