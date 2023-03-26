<script setup>
import { RouterLink, RouterView } from 'vue-router'
import Navbar from './components/Navbar.vue'
</script>

<template>
  <Navbar 
    :isUserLoggedIn="isUserLoggedIn" 
    :user="user"
    v-on:logout="onLogout()" 
  />
  <RouterView v-on:login="onLogin()" />
</template>

<script>
export default {
  data() {
    return {
      isUserLoggedIn: (localStorage.getItem('token') !== null),
      user: localStorage.getItem('user')
    }
  },

  methods: {
    onLogin() {
      this.user = localStorage.getItem('user')
      this.isUserLoggedIn = true
    },
    onLogout() {
      this.user = null
      this.isUserLoggedIn = false
    }
  }
}
</script>

<style>
</style>
