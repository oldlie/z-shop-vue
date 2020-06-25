<template>
  <div>
    <a-row class="inner-row">
      <a-col :span="24">
        <a-spin :spinning="tagLoading">
          <a-row>
            <a-col span="24">
              <a-button v-if="tagPath.length > 0" @click="uptags">&lt;</a-button>
              <a-button-group v-for="(tag, index) in tags" :key="index" style="margin:6px;">
                <a-button @click="addToAritcle(tag)">{{tag.title}}</a-button>
                <a-button v-if="tag.childCount > 0" @click="nextTags(tag)">&gt;</a-button>
              </a-button-group>
            </a-col>
          </a-row>
          <a-row>
            <a-col span="24">
              <a-tag
                v-for="(tag, index) in checkedTags"
                :key="index"
                closable
                @close="removeTag(tag)"
                style="margin:6px;"
              >{{tag.title}}</a-tag>
            </a-col>
          </a-row>
        </a-spin>
      </a-col>
    </a-row>
  </div>
</template>
<script>
export default {
  data() {
    return {
      tagLoading: false,
      checkedTags: [],
      tagPath: [],
      tags: []
    };
  },
  mounted () {
    this.load(0);
  },
  methods: {
    load (id) {
      this.tagLoading = true;
      const url = `${this.apiUrl}/backend/tags/${id}`;
      G.get(url)
        .cb(data => {
          if (data.status === 0) {
            this.tags = data.list;
          } else {
            this.$message.error(data.message);
          }
        })
        .fcb(() => (this.tagLoading = false))
        .req();
    },
    uptags() {
      const tag = this.tagPath.pop();
      this.load(tag.parentId);

    },
    addToAritcle() {},
    nextTags(tag) {
      const _tag = JSON.parse(JSON.stringify(tag));
      this.tagPath.push(_tag);
      this.load(tag.id);
    
    },
    removeTag() {}
  }
};
</script>