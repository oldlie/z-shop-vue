<template>
    <a-spin :spinning="loading">
        
    </a-spin>
</template>
<script>
export default {
    props: {
        id: { type: Number, default: 0 }
    },
    data(){
        return {
            loading: false,
            spec: {
                commodityId: 0,
                specification: '',
                images: '',
                detail: ''
            }
        }
    },
    methods: {
        init(id) {
            const url = `${this.apiUrl}/backend/product/profile/${id}`;
            const self = this;
            self.loading = true;
            G.get(url)
            .cb(data => {
                if (data.status === 0) {

                }
            })
            .fcb(() => {
                self.loading = false;
            })
            .req();
        }
    }
}
</script>