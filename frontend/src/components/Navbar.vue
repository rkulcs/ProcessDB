<template>
  <nav class="navbar" role="navigation" aria-label="main navigation">
  <div class="navbar-brand">
    <RouterLink to="/" class="navbar-item">
      ProcessDB
    </RouterLink>
  </div>

  <div class="navbar-menu">
    <div class="navbar-start">
      <a class="navbar-item">
        Processes
      </a>
    </div>

    <div class="navbar-end">
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
</script>

<style>
</style>
