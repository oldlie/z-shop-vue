<template>
  <div class="main-panel">
    <a-row>
      <a-col :span="4">
        <admin-menu :activeIndex="'1'" :openKey="'sub1'" @menuChangeEvent="activeIndexChanged"></admin-menu>
      </a-col>
      <a-col :span="20">
        <a-row v-if="activeIndex === '1'">
          <admin-carousel></admin-carousel>
        </a-row>
      </a-col>
    </a-row>
  </div>
</template>
<script>
export default {
  data() {
    return {
      activeIndex: "1",
      list: []
    };
  },
  mounted () {
  },
  methods: {
    activeIndexChanged(index) {
      console.log('activeIndexChanged', index);
      this.activeIndex = index;
    }
  }
};
</script>
<style scoped>
</style>
