<template>
  Processes
  <div class="container is-fluid" v-for="p in store.processes">
    <div class="notification is-primary">
      {{ p.name }} | {{ p.filename }} ({{ p.os }})
    </div>
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
