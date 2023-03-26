<template>
  <div class="container is-fluid">
    <div class="notification is-primary">
      <h1 class="title process-name">{{ process.name }} ({{ process.filename }})</h1>
      <div class="notification is-light">
        <p><strong>Name:</strong> {{ process.name }}</p>
        <p><strong>Filename:</strong> {{ process.filename }}</p>
        <p><strong>Operating System:</strong> {{ process.os }}</p>
        <br>
        <div class="notification is-white">
          {{ 
            (process.description === null) ? 'No description available.' 
                                          : process.description 
          }}
        </div>
        <RouterLink class="button is-link" :to="`/processes/${ id }/edit`">
          Edit
        </RouterLink>
        <button class="button is-link back-button" @click="deleteProcess">
          Delete
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import processStore from '../stores/processStore.js'
import Process from '../entities/Process.js'

export default {
  data() {
    return {
      id: this.$route.params.id,
      store: processStore(),
      process: Process.emptyProcess()
    }
  },

  async mounted() {
    this.process = await this.store.get(this.$route.params.id)
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
