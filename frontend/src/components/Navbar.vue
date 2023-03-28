<template>
  <nav class="navbar" role="navigation" aria-label="main navigation">
  <div class="navbar-brand">
    <RouterLink to="/" class="navbar-item">
      ProcessDB
    </RouterLink>
  </div>

  <div class="navbar-menu">
    <div class="navbar-start">
      <RouterLink class="navbar-item" to="/processes">
        Processes
      </RouterLink>
    </div>

    <div class="navbar-end" v-if="isUserLoggedIn">
      <div class="navbar-item">{{ user }}</div>
      <div class="navbar-item navbar-button-group">
        <div class="buttons">
          <button class="button is-light" @click="logout">
            <strong>Log Out</strong>
          </button>
        </div>
      </div>
    </div>
    <div class="navbar-end" v-else>
      <div class="navbar-item navbar-button-group">
        <div class="buttons">
          <RouterLink class="button is-primary" to="/user/register">
            Sign Up
          </RouterLink>
          <RouterLink class="button is-light" to="/user/login">
            <strong>Log In</strong>
          </RouterLink>
        </div>
      </div>
    </div>
  </div>
</nav>
</template>

<script>
import UserUtils from '../util/UserUtils.js'

export default {
  props: {
    isUserLoggedIn: Boolean,
    user: String
  },

  methods: {
    logout() {
      UserUtils.clearUserDetails()

      this.$emit('logout')
      this.$router.push({ name: 'home' })
    }
  }
}
</script>

<style>
</style>
