<template>
  <h1>{{ isLogin() ? 'Log In' : 'Sign Up' }}</h1>
</template>

<script>
import UserFormType from '../entities/UserFormType.js'

export default {
  props: {
    type: UserFormType
  },

  methods: {
    isLogin() {
      return (this.type === UserFormType.LOGIN)
    }
  }
}
</script>
