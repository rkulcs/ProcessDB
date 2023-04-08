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
  <RouterView 
    :isUserLoggedIn="isUserLoggedIn"
    v-on:login="onLogin()" 
  />
</template>

<script>
import UserUtils from './util/UserUtils.js'

export default {
  data() {
    return {
      isUserLoggedIn: (UserUtils.getLoggedInUser() !== null),
      user: UserUtils.getLoggedInUser()
    }
  },

  methods: {
    onLogin() {
      this.user = UserUtils.getLoggedInUser()
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
