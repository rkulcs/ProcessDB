<template>
  <ProcessForm v-on:formFilled="update($event)"/>
</template>

<script>
import ProcessForm from './ProcessForm.vue'
import processStore from '../stores/processStore'

export default {
  components: {
    ProcessForm
  },

  methods: {
    update(process) {
      (async () => {
        let successfulUpdate = await processStore().add(process)

        if (successfulUpdate)
          this.$router.push({ name: 'process_info' })
      })()
    }
  }
}
</script>
