<template>
    <a-spin :spinning="loading">

    </a-spin>
</template>
<script>
export default {
   data () {
       return {
           loading: false
       };
   }
}
</script>