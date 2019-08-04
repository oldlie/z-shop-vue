<template>
    <div>
        <a-row class="inner-row">
          <a-col :span="6">
            <a-button shape="circle" icon="plus" @click="gotoForm" />
          </a-col>
        </a-row>
        <a-row class="inner-row">
          <a-col :span="6">
            <a-button-group>
              <a-button>
                商品
                <a-divider type="vertical" />
                <a-icon type="right" />
              </a-button>
              <a-button type="danger">
                <a-icon type="delete" />
              </a-button>
            </a-button-group>
          </a-col>
        </a-row>
    </div>
</template>
<script>
export default {
    data () {
        return {

        }
    },
    methods: {
        gotoForm () {
            this.$emit('gotoFormEvent');
        }
    }
}
</script>
