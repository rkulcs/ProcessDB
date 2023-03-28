<template>
  <UserForm 
    :errorMessage="errorMessage" 
    v-on:formFilled="register($event)" 
  />
</template>

<script>
import UserForm from './UserForm.vue'
import axios from 'axios'
import UserUtils from '../util/UserUtils.js'

export default {
  components: {
    UserForm
  },

  data() {
    return {
      errorMessage: null
    }
  },

  methods: {
    register(formData) {
      (async () => {
        await axios({
          method: 'POST',
          url: `${import.meta.env.VITE_BACKEND_URL}/auth/register`,
          headers: {
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin': '*'
          },
          data: formData
        }).then(response => {
          UserUtils.setLoggedInUserAndToken(formData.username, response.data)

          this.errorMessage = null
          this.$emit('login')
          this.$router.push({ name: 'home' })
        }).catch(error => this.errorMessage = error.response.data)
      })()
    }
  }
}
</script>
  