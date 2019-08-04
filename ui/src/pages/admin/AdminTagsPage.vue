<template>
  <div class="main-panel">
    <a-row>
      <a-col :span="6">
        <admin-menu :activeIndex="'tags'" :openKey="''"></admin-menu>
      </a-col>
      <a-col :span="18">
        <admin-tags v-if="viewModel === 'list'" @gotoFormEvent="changeViewModel('form')"></admin-tags>
        <admin-tag v-if="viewModel === 'form'" @goBackEvent="changeViewModel('list')"></admin-tag>
      </a-col>
    </a-row>
  </div>
</template>
<script>
export default {
    data () {
        return {
            viewModel: 'form'
        }
    },
    methods: {
        changeViewModel (m) {
            this.viewModel = m;
        }
    }
};
</script>