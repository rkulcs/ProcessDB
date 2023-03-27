<template>
  <div class="container is-fluid">
    <div class="notification is-primary">
      <h1 class="title process-name">Welcome to ProcessDB</h1>
      <div class="notification is-light">
        <RouterLink to="/processes">
          <div class="notification is-white">
            View Processes
          </div>
        </RouterLink>
        <br/>
        <RouterLink to="/user/login" v-if="!isUserLoggedIn">
          <div class="notification is-white">
            Log In
          </div>
        </RouterLink>
        <br/>
        <RouterLink to="/user/register" v-if="!isUserLoggedIn">
          <div class="notification is-white">
            Create an Account
          </div>
        </RouterLink>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
      isUserLoggedIn: Boolean
  }
}
</script>
