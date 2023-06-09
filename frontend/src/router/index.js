import { createRouter, createWebHistory } from 'vue-router'
import UserFormType from '../entities/UserFormType.js'
import ProcessFormType from '../entities/ProcessFormType.js'

import HomeView from '../views/HomeView.vue'
import UserFormView from '../views/UserFormView.vue'
import ProcessView from '../views/ProcessView.vue'

import UserLogin from '../components/UserLogin.vue'
import UserRegistration from '../components/UserRegistration.vue'
import ProcessList from '../components/ProcessList.vue'
import ProcessInfo from '../components/ProcessInfo.vue'
import ProcessNew from '../components/ProcessNew.vue'
import ProcessEdit from '../components/ProcessEdit.vue'
import ProcessCommentForm from '../components/ProcessCommentForm.vue'

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
        },
        {
          path: 'new',
          name: 'process_new',
          props: {'type': ProcessFormType.NEW},
          component: ProcessNew
        },
        {
          path: ':id/edit',
          name: 'process_edit',
          props: {'type': ProcessFormType.EDIT},
          component: ProcessEdit
        },
        {
          path: ':id/comments/new',
          name: 'process_comment_new',
          component: ProcessCommentForm
        }
      ]
    }
  ]
})

export default router
