import Vue from 'vue'
import Router from 'vue-router'
import Pases from '@/pages/pas/index'
import Pas from '@/pages/pas/show'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'pases',
      component: Pases
    },
    {
      path: '/pas/:id',
      name: 'pas',
      component: Pas
    }
  ]
})