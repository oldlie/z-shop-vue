<template>
    <a-spin :spinning="loading">
        <div class="main-panel">
            <a-row>
                <a-col :span="4">
                    <admin-menu :activeIndex="'sub3'" :openKey="'sub3'"></admin-menu>
                </a-col>

                <a-col :span="20">
                    <a-row class="inner-row">
                        <a-button
                            @click="onAddTag()"
                            shape="circle"
                            icon="plus"
                            style="margin:5px;"
                        ></a-button>
                        <a-button
                            v-if="parentTag.id  > 0"
                            shape="circle"
                            icon="rollback"
                            @click="showParentTag"
                        />
                        <a-tag
                            v-for="tag in tags"
                            :key="tag.id"
                            @click="showTagChildren(tag)"
                        >{{tag.title}}</a-tag>
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
const tags = [
    { id: 0, title: "New Products" },
    { id: 1, title: "Fruit" },
    { id: 2, title: "Aquatic Products" },
    { id: 3, title: "Snacks" },
    { id: 4, title: "Commodity" }
];
export default {
    data() {
        return {
            loading: false,
            // region Tag
            tags,
            parentTag: { id: 0 },
            tagPath: []
            // endregion
        };
    },
    mounted() {
        this.loadTags(this.parentTag.id);
    },
    methods: {
        onTagClose() {},
        onAddTag(id) {},
        loadTags(id) {
            const url = `${this.apiUrl}/backend/tags/${id}`;
            const self = this;
            this.loading = true;
            G.get(url)
                .cb(data => {
                    if (data.status === 0) {
                        self.tags = data.list;
                    } else {
                        self.$message.error(data.message);
                    }
                })
                .fcb(() => (self.loading = false))
                .req();
        },
        showTagChildren(tag) {
            if (tag.childCount === 0) {
                return;
            }
            const temp = JSON.parse(JSON.stringify(this.parentTag));
            this.tagPath.push(temp);
            this.parentTag = tag;
            this.loadTags(tag.id);
        },
        showParentTag() {
            const tag = this.tagPath.pop();
            this.parentTag = tag;
            this.loadTags(tag.id);
        }
    }
};
</script>