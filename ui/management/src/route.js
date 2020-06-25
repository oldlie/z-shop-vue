import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

import CarouselPage from './pages/CarouselPage'
import DashboardPage from './pages/DashboardPage'
import HomeNavPage from './pages/HomeNavPage'
import LoginPage from './pages/LoginPage'

export default new Router({
  routes: [
    { path: '/', name: 'index', component: LoginPage},
    { path: '/login', name: 'login', component: LoginPage},
    { path: '/dashboard', name: 'dashboard', component: DashboardPage},
    { path: '/carousel', name: 'carousel', component: CarouselPage},
    { path: '/home-nav', name: 'homeNave', component: HomeNavPage},
  ]
})