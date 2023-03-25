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
      </div>
    </div>
  </div>
</template>

<script>
import processStore from '../stores/processStore.js'
import Process from '../entities/Process.js'
import axios from 'axios'

export default {
  data() {
    return {
      id: this.$route.params.id,
      store: processStore(),
      process: Process.emptyProcess()
    }
  },

  mounted() {
    let processJson = this.store.get(this.id)

    if (processJson === undefined) {
      (async () => {
        processJson = await axios({
          method: 'GET',
          url: `${import.meta.env.VITE_BACKEND_URL}/processes/${this.id}`,
          headers: {
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin': '*'
          }
        }).then(response => response.data)

        this.process.copyJsonValues(processJson)
        console.log(this.process)
      })()
    }
  }
}
</script>
