<template>
  <h1>{{ isRegistration() ? 'Sign Up' : 'Log In' }}</h1>
  <div class="field">
    <label class="label">Username</label>
    <div class="control">
      <input v-model="formData.username" class="input" type="text">
    </div>
  </div>
  <div class="field">
    <label class="label">Password</label>
    <div class="control">
      <input v-model="formData.password" class="input" type="password">
    </div>
  </div>
  <div class="field" v-if="isRegistration()">
    <label class="label">Password Confirmation</label>
    <div class="control">
      <input v-model="formData.passwordConfirmation" class="input" type="password">
    </div>
  </div>
  <div class="field">
    <div class="control">
      <button class="button is-link" @click="save">
        {{ isRegistration() ? 'Sign Up' : 'Log In' }}
      </button>
    </div>
  </div>
</template>

<script>
import { User } from '../entities/User'
import UserFormType from '../entities/UserFormType.js'

export default {
  props: {
    type: UserFormType
  },

  data() {
    return {
      formData: (this.type === UserFormType.REGISTRATION) ? User.emptyLogin()
                                                          : User.emptyRegistration()
    }
  },

  methods: {
    isRegistration() {
      return (this.type === UserFormType.REGISTRATION)
    },
    save() {
      this.$emit('formFilled', this.formData)
    }
  }
}
</script>
