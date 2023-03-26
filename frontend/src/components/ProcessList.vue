<template>
  <h1 class="title">Processes</h1>
  <RouterLink class="button is-link new-process-button" to="/processes/new">
    New
  </RouterLink>
  <div class="container is-fluid" v-for="process in store.processes">
    <RouterLink :to="`/processes/${ process.id }`">
      <div class="notification is-primary">
        {{ process.name }} | {{ process.filename }} ({{ process.os }})
      </div>
    </RouterLink>
  </div>
</template>

<script>
import processStore from '../stores/processStore.js'

export default {
  data() {
    return {
      store: processStore()
    }
  },
  
  mounted() {
    this.store.getAll()
  }
}
</script>
