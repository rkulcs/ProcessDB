<template>
  <ProcessForm v-on:formFilled="add($event)"/>
</template>

<script>
import ProcessForm from './ProcessForm.vue'
import processStore from '../stores/processStore'

export default {
  components: {
    ProcessForm
  },

  methods: {
    add(process) {
      (async () => {
        let successfulAddition = await processStore().add(process)

        if (successfulAddition)
          this.$router.push({ name: 'process_list' })
      })()
    }
  }
}
</script>
