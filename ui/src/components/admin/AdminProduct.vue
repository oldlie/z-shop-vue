<template>
    <div>
        <a-row v-if="viewModel === 0">
            
        </a-row>
        <a-row v-if="viewModel === 1"></a-row>
    </div>
</template>
<script>
export default {
    data () {
        return {
            viewModel: 0
        }
    }
}
</script>
<style scoped>

</style>
