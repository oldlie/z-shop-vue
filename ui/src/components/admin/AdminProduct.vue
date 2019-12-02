<template>
  <div>
    <admin-product-basic :id="innerId" v-show="step === STEP.BASIC_INFO" @gotoBasicNext="gotoSpec"></admin-product-basic>
    <admin-product-spec :id="innerId" v-show="step === STEP.SPECIFICATION"></admin-product-spec>
  </div>
</template>
<script>
const _step_ = {
  BASIC_INFO: 0,
  SPECIFICATION: 1,
  FORMULAS: 2
};

export default {
  props: {
    id: { type: Number, default: 0 }
  },
  data() {
    return {
      STEP: _step_,
      step: _step_.BASIC_INFO,
      innerId: 0
    };
  },
  created() {

  },
  mounted() {
    this.innerId = JSON.parse(JSON.stringify(this.id));
  },
  methods: {
    gotoSpec(id) {
      this.innerId = id;
      this.step = _step_.SPECIFICATION;
    }
  }
};
</script>
<style scoped>
</style>
