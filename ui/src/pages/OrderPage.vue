<template>
  <div>
    <div
      :style="{ background: '#fff', padding: '24px', minHeight: '280px', 'margin': '30px 0', 'width:': '100%','text-align':'left'}"
    >
      <h1>订单管理</h1>

      <a-tabs defaultActiveKey="1" @change="onTabsChange">
        <a-tab-pane tab="待收货" key="1">
          <a-list :dataSource="data">
            <a-list-item slot="renderItem" slot-scope="item">
              <a-row style="width:100%;margin: 20px 0;">
                <a-col :span="2">
                  <a-tag v-if="item.status === 0" color="#2db7f5">正在出库</a-tag>
                  <a-tag v-if="item.status === 1" color="#87d068">已出库</a-tag>
                </a-col>
                <a-col :span="2">
                  <a-avatar :src="item.thumb" shape="square" :size="64" icon="user"/>
                </a-col>
                <a-col :span="4">
                  <router-link :to="{ path: '/product/' + item['id']}">{{item.title}}</router-link>
                </a-col>
                <a-col :span="3">
                  <p>{{item.specification}}</p>
                </a-col>
                <a-col :span="9">
                  <p>{{item.address}}</p>
                </a-col>
                <a-col :span="4">
                  <p v-if="item.status === 0">
                    <a-button>取消订单</a-button>
                  </p>
                  <p v-if="item.status === 1">{{item.expressName}}: {{item.expressNumber}}</p>
                </a-col>
              </a-row>
            </a-list-item>
          </a-list>
        </a-tab-pane>
        <a-tab-pane tab="已完成" key="2" forceRender>
          <a-list :dataSource="data">
            <a-list-item slot="renderItem" slot-scope="item">
              <a-row style="width:100%;margin: 20px 0;">
                <a-col :span="2">
                  <a-avatar :src="item.thumb" shape="square" :size="64" icon="user"/>
                </a-col>
                <a-col :span="4">
                  <router-link :to="{ path: '/product/' + item['id']}">{{item.title}}</router-link>
                </a-col>
                <a-col :span="3">
                  <p>{{item.specification}}</p>
                </a-col>
                <a-col :span="5">
                  <p>完成时间: 2019年4月22日</p>
                </a-col>
                <a-col :span="10">
                  <a-button v-if="item.id === 1">评价订单</a-button>
                  &nbsp;
                  <a-tag v-if="item.id === 0" color="red">
                    <a-icon v-for="item in [1,2,3,4,5]" :key="item" type="star"/>
                  </a-tag>
                  &nbsp;
                  <a-button >申请售后</a-button>
                </a-col>
              </a-row>
            </a-list-item>
          </a-list>
        </a-tab-pane>
        <a-tab-pane tab="售后" key="3">
          <a-list :dataSource="data">
            <a-list-item slot="renderItem" slot-scope="item">
              <a-row style="width:100%;margin: 20px 0;">
                <a-col :span="2">
                  <a-tag v-if="item.status === 0" color="#2db7f5">已赔付</a-tag>
                  <a-tag v-if="item.status === 1" color="#87d068">换货中</a-tag>
                </a-col>
                <a-col :span="2">
                  <a-avatar :src="item.thumb" shape="square" :size="64" icon="user"/>
                </a-col>
                <a-col :span="4">
                  <router-link :to="{ path: '/product/' + item['id']}">{{item.title}}</router-link> <p>{{item.specification}}</p>
                </a-col>
                <a-col :span="8">
                  <p>售后理由：快递损坏，申请换货</p>
                  <p>
                    <img style="margin: 1px;" width="64px" height="64px" src="http://localhost/zshop/img/pms.jpg" v-for="item in [1,2,3,4,5]" :key="item">
                  </p>
                </a-col>
                <a-col :span="8">
                  <p v-if="item.id === 0">
                    <ul>
                      <li>2019年4月23日 换货已经发出，请注意查收</li>
                      <li>2019年4月22日 已确认可以更换</li>
                    </ul>
                  </p>
                  <p v-if="item.id === 1">
                    <ul>
                      <li>2019年4月22日 已确认损坏，直接赔付</li>
                    </ul>
                  </p>
                </a-col>
              </a-row>
            </a-list-item>
          </a-list>
        </a-tab-pane>
      </a-tabs>
    </div>
  </div>
</template>
<script>
const data = [
  {
    id: 0,
    thumb: "http://localhost/zshop/img/pms.jpg",
    title: "小米手机9",
    specification: "暗夜黑",
    address: "赵钱孙李先生 13412345678 北京市 北京市 海淀区 阳光锦城2606",
    status: 1,
    expressName: "圆通大师",
    expressNumber: "201900123456"
  },
  {
    id: 1,
    thumb: "http://localhost/zshop/img/pms.jpg",
    title: "小米手机9 SE",
    specification: "暗夜黑",
    address: "赵钱孙李先生 13412345678 北京市 北京市 海淀区 阳光锦城2606",
    status: 0,
    expressName: "",
    expressNumber: ""
  }
];

export default {
  name: "OrderPage",
  data() {
    return {
      data
    };
  },
  methods: {
    onTabsChange() {}
  }
};
</script>
<style scoped>
</style>


