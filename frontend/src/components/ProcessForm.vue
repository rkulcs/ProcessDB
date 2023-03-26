<template>
  <h1 class="title">{{ isNew() ? 'New Process' : 'Edit Process' }}</h1>
  <div class="field">
    <label class="label">Name</label>
    <div class="control">
      <input v-model="process.name" class="input" type="text">
    </div>
  </div>
  <div class="field">
    <label class="label">Filename</label>
    <div class="control">
      <input v-model="process.filename" class="input" type="text">
    </div>
  </div>
  <div class="field">
    <label class="label">Operating System</label>
    <div class="control">
      <input v-model="process.os" class="input" type="text">
    </div>
  </div>
  <div class="field">
    <label class="label">Description</label>
    <div class="control">
      <textarea v-model="process.description" class="textarea" type="text"/>
    </div>
  </div>
  <div class="field">
    <div class="control">
      <button class="button is-link" @click="save">
        {{ isNew() ? 'Add' : 'Update' }}
      </button>
      <button class="button is-link back-button" @click="save">
        Back
      </button>
    </div>
  </div>
</template>

<script>
import ProcessFormType from '../entities/ProcessFormType.js'
import Process from '../entities/Process'
import processStore from '../stores/processStore'

export default {
  props: {
    type: ProcessFormType
  },

  data() {
    return {
      id: this.$route.params.id,
      store: processStore(),
      process: '',
      loaded: false
    }
  },

  async mounted() {
    this.process = (this.type === ProcessFormType.NEW) ? Process.emptyProcess()
                                                       : await this.store.get(this.id)
  },

  methods: {
    isNew() {
      return (this.type === ProcessFormType.NEW)
    },
    save() {
      this.$emit('formFilled', this.process)
    }
  }
}
</script>

<style>
</style>
