<template>
  <a-spin :spinning="loading">
    <a-row class="inner-row">
      <a-col :span="24">
        <div class="pay-card">
          <h1 style="color:#F0E68C;text-shadow:1px 1px 1px #ffffff;">{{payCard.title}}</h1>
          <div class="pay-card-price">
            <span>¥ {{!!payCard['denomination'] ? payCard['denomination']['amount'] : ''}} 元</span>
          </div>
          <div class="pay-card-verify">
            <div style="float:right; width: 69px;">{{payCard['verifyCode']}}</div>
            <span>兑换码：</span>
          </div>
          <div class="pay-card-bottom">
            <div class="pay-card-valid">有效期：{{payCard.latestExchangeDate}}</div>
            <div class="pay-card-sn">SN:{{payCard.serialNumber}}</div>
          </div>
        </div>
      </a-col>
    </a-row>

    <a-row class="inner-row">
      <a-col :span="18">
        <a-form>
          <a-form-item :label-col="labelCol" :wrapper-col="wideWrapperCol" label="是否有效">
            <a-tag color="#f5222d" v-if="payCard.isValid === 0">无效</a-tag>
            <a-tag color="#52c41a" v-if="payCard.isValid === 1">有效</a-tag>
          </a-form-item>
          <a-form-item
            :label-col="labelCol"
            :wrapper-col="wideWrapperCol"
            label="发布者"
          >{{payCard.publisher}}</a-form-item>
          <a-form-item
            :label-col="labelCol"
            :wrapper-col="wideWrapperCol"
            label="备注"
          >{{payCard.node}}</a-form-item>
          <a-form-item
            :label-col="labelCol"
            :wrapper-col="wideWrapperCol"
            label="售价"
          >¥ {{!!payCard.amount ? payCard['amount']['amount'] : ''}} 元</a-form-item>
          <a-form-item :label-col="labelCol" :wrapper-col="wideWrapperCol" label="是否已售">
            <template v-if="payCard.isSoldOut === 0">
              <a-tag color="#52c41a">未售</a-tag>
              <a-divider type="vertical"></a-divider>
              <a-button @click="openSellModal">出售</a-button>
            </template>
            <template v-if="payCard.isSoldOut === 1">
              <a-tag color="#f5222d">已售</a-tag>
            </template>
          </a-form-item>
          <a-form-item
            :label-col="labelCol"
            :wrapper-col="wideWrapperCol"
            label="购买人"
          >{{payCard.customer}}</a-form-item>
          <a-form-item
            :label-col="labelCol"
            :wrapper-col="wideWrapperCol"
            label="购买人电话"
          >{{payCard.customerPhone}}</a-form-item>
          <a-form-item
            :label-col="labelCol"
            :wrapper-col="wideWrapperCol"
            label="实际售价"
          >¥ {{!!payCard.price ? payCard.price['amount'] : '0.00'}} 元</a-form-item>
          <a-form-item :label-col="labelCol" :wrapper-col="wideWrapperCol" label="是否兑换">
            <a-tag color="#52c41a" v-if="payCard.isExchanged === 0">未兑换</a-tag>
            <a-tag color="#f5222d" v-if="payCard.isExchanged === 1">已兑换</a-tag>
          </a-form-item>
          <a-form-item
            :label-col="labelCol"
            :wrapper-col="wideWrapperCol"
            label="兑换用户ID"
          >{{payCard.uid}}</a-form-item>
          <a-form-item
            :label-col="labelCol"
            :wrapper-col="wideWrapperCol"
            label="兑换用户"
          >{{payCard.username}}</a-form-item>
        </a-form>
      </a-col>
    </a-row>

    <a-modal title="出售这张卡片" v-model="visible" :footer="null">
      <admin-pay-card-sell :ids="ids" @soldEvent="handleSoldEvent"></admin-pay-card-sell> 
    </a-modal>

  </a-spin>
</template>

<script>
export default {
  props: { payCardId: { type: Number, default: 0 } },
  data() {
    return {
      loading: false,
      payCard: {},
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
      visible: false,
      ids: []
    };
  },
  mounted() {
    this.load();
  },
  methods: {
    load() {
      const url = `${this.apiUrl}/backend/pay-card/${this.payCardId}`;
      this.loading = true;
      G.get(url)
        .cb(data => {
          if (data.status === 0) {
            this.payCard = data.item;
            this.ids.push(this.payCard.id);
          } else {
            this.$message.error(data.message);
          }
        })
        .fcb(() => (this.loading = false))
        .req();
    },
    handleOk () {

    },
    openSellModal () {
        this.visible = true;
    },
    handleSoldEvent(info) {
      this.payCard['isSoldOut'] = 1;
      this.payCard['customer'] = info['customer'];
      this.payCard['customerPhone'] = info['customerPhone'];
      this.payCard['price'] = {
        'amount': info['price']
      };
      this.visible = false;
    }
  }
};
</script>

<style>
.pay-card {
  width: 395px;
  height: 210px;
  border: 1px solid #ff5500;
  background: #ff5500;
  border-radius: 5px;
  padding: 15px;
  color: rgba(0, 0, 0, 0.85);
}
.pay-card .pay-card-bottom {
  color: #ffffff;
  width: 100%;
}
.pay-card .pay-card-bottom .pay-card-sn {
  float: left;
  width: 120px;
}
.pay-card .pay-card-bottom .pay-card-valid {
  float: right;
  width: 140px;
}
.pay-card .pay-card-price {
  color: #ffffff;
  font-size: 3em;
  width: 100%;
  height: 70px;
}
.pay-card .pay-card-price span {
  float: right;
  bottom: 0;
  margin: 5px 15px 0 0;
}
.pay-card .pay-card-verify {
  width: 100%;
  height: 32px;
  text-align: right;
  padding: 5px 15px 0 0;
}
.pay-card .pay-card-verify div {
  background-color: #000;
}
.pay-card .pay-card-verify div:hover {
  background-color: #ff5500;
}
</style>