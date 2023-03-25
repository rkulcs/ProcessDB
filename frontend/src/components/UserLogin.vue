<template>
  <UserForm v-on:formFilled="login($event)" />
</template>

<script>
import UserForm from './UserForm.vue'
import axios from 'axios'

export default {
  components: {
    UserForm
  },

  methods: {
    login(formData) {
      axios({
        method: 'POST',
        url: `${import.meta.env.VITE_BACKEND_URL}/auth/login`,
        headers: {
          'Content-type': 'application/json',
          'Access-Control-Allow-Origin': '*'
        },
        data: formData
      }).then(response => localStorage.setItem('token', response.data))

      this.$router.push({ name: 'home' })
    }
  }
}
</script>
