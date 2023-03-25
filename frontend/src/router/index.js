import { createRouter, createWebHistory } from 'vue-router'
import UserFormType from '../entities/UserFormType.js'

import HomeView from '../views/HomeView.vue'
import UserFormView from '../views/UserFormView.vue'
import ProcessView from '../views/ProcessView.vue'

import UserLogin from '../components/UserLogin.vue'
import UserRegistration from '../components/UserRegistration.vue'
import ProcessList from '../components/ProcessList.vue'
import ProcessInfo from '../components/ProcessInfo.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/user',
      name: 'user',
      component: UserFormView,
      children: [
        {
          path: 'login',
          name: 'user_login',
          props: {'type': UserFormType.LOGIN},
          component: UserLogin
        },
        {
          path: 'register',
          name: 'user_registration',
          props: {'type': UserFormType.REGISTRATION},
          component: UserRegistration
        }
      ]
    },
    {
      path: '/processes',
      name: 'processes',
      component: ProcessView,
      children: [
        {
          path: '',
          name: 'process_list',
          component: ProcessList
        },
        {
          path: ':id',
          name: 'process_info',
          component: ProcessInfo
        }
      ]
    }
  ]
})

export default router
