<template>
  <div class="container is-fluid">
    <div class="notification is-primary">
      <h1 class="title process-name">
        {{ process.name }} ({{ process.filename }})
        <span class="tag is-success" v-if="parseFloat(processClassification.safePercentage) >= 70">
          Safe
        </span>
        <span class="tag is-danger" v-else-if="parseFloat(processClassification.unsafePercentage) >= 70">
          Unsafe
        </span>
        <span class="tag is-warning" v-else>
          Unknown
        </span>
      </h1>
      <div class="notification is-light">
        <p><strong>Name:</strong> {{ process.name }}</p>
        <p><strong>Filename:</strong> {{ process.filename }}</p>
        <p><strong>Operating System:</strong> {{ process.os }}</p>
        <p>
          <strong>Percentage of Safe Classifications:</strong>
          {{ processClassification.safePercentage }}
        </p>
        <p>
          <strong>Percentage of Unsafe Classifications:</strong>
          {{ processClassification.unsafePercentage }}
        </p>
        <br>
        <div class="notification is-white">
          {{ 
            (process.description === '' || process.description === null) ? 'No description available.' 
                                                                         : process.description 
          }}
        </div>
        <div v-if="isUserLoggedIn">
          <RouterLink class="button is-link" :to="`/processes/${ id }/edit`">
            Edit
          </RouterLink>
          <button class="button is-link back-button" @click="deleteProcess">
            Delete
          </button>
        </div>
      </div>
      <div class="notification is-light">
        <h2 class="title">Comments</h2>
        <RouterLink class="button is-link" :to="`/processes/${ id }/comments/new`">
          Add Comment
        </RouterLink>
        <br/><br/>
        <div class="notification is-white" v-for="comment in process.comments">
          <strong>{{ comment.author }}</strong> - {{ comment.dateWritten }} [{{ comment.safe ? "Safe" : "Not safe" }}]<br>
          {{ comment.info }}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import processStore from '../stores/processStore.js'
import Process from '../entities/Process.js'

export default {
  props: {
    isUserLoggedIn: Boolean
  },

  data() {
    return {
      id: this.$route.params.id,
      store: processStore(),
      process: Process.emptyProcess(),
      processClassification: ''
    }
  },

  async created() {
    this.process = await this.store.get(this.$route.params.id)
    this.processClassification = this.process.getClassification()
  },

  methods: {
    deleteProcess() {
      (async () => {
        let successfulAddition = await this.store.delete(this.process)

        if (successfulAddition)
          this.$router.push({ name: 'process_list' })
      })()
    }
  }
}
</script>
