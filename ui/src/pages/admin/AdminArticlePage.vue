<template>
    <a-spin :spinning="loading">
        <div class="main-panel">
            <a-row>
                <a-col :span="4">
                    <admin-menu :activeIndex="'sub3'" :openKey="'sub3'"></admin-menu>
                </a-col>

                <a-col :span="20">
                    <a-row class="inner-row">
                        
                    </a-row>
                </a-col>
            </a-row>
        </div>
    </a-spin>
</template>
<script>
const _view = {
    table: "table",
    form: "form"
};

export default {
    data() {
        return {
            loading: false,
            // region Tag
           // endregion
        };
    },
    mounted() {
        this.loadTags(this.parentTag.id);
    },
    methods: {
    }
};
</script>