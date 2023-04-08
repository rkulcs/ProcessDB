<template>
  <h1 class="title">New Comment</h1>
  <div class="field">
    <label class="label">Safe process?</label>
    <div class="control">
      <input v-model="comment.safe" class="checkbox" type="checkbox" checked>
    </div>
  </div>
  <div class="field">
    <label class="label">Comment</label>
    <div class="control">
      <input v-model="comment.info" class="input" type="text">
    </div>
  </div>
  <div class="field">
    <div class="control">
      <button class="button is-link" @click="save">
        Add Comment
      </button>
      <button class="button is-link back-button" @click="returnToProcessInfo">
        Back
      </button>
    </div>
  </div>
</template>

<script>
import Comment from '../entities/Comment'
import processStore from '../stores/processStore'
import UserDetails from '../util/UserUtils'

export default {
  data() {
    return {
      store: processStore(),
      processId: this.$route.params.id,
      comment: ''
    }
  },

  mounted() {
    this.comment = Comment.emptyComment()
  },

  methods: {
    save() {
      (async () => {
        let successfulAddition = await processStore().addComment(this.processId, this.comment)

        if (successfulAddition)
          this.$router.push({ name: 'process_info', params: { id: this.processId } })
      })()
    },

    returnToProcessInfo() {
      this.$router.push({ name: 'process_info', params: { id: this.processId } })
    }
  }
}
</script>
