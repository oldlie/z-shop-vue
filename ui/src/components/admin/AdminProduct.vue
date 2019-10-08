<template>
  <div>
    <admin-product-basic v-show="step === STEP.BASIC_INFO" @gotoBasicNext=gotoSpec></admin-product-basic>
    <div v-show="step === STEP.SPECIFICATION">
    </div>
  </div>
    
</template>
<script>
const _step_ = {
  BASIC_INFO: 0,
  SPECIFICATION: 1,
  IMAGES: 2,
  DETAILS: 3,
  FORMULAS: 4
};

export default {
  props: {
    id: { type: Number, default: 0 }
  },
  data() {
    return {
      STEP: _step_,
      step: _step_.BASIC_INFO
    };
  },
  mounted() {
  },
  methods: {
    gotoSpec(id) {
      this.id = id;
      this.step = _step_.SPECIFICATION;
    }
  }
};
</script>
<style scoped>
</style>
