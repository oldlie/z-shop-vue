import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

import ArticlePage from './pages/ArticlePage'
import ArticlesPage from './pages/ArticlesPage'
import CarouselPage from './pages/CarouselPage'
import CommodityPage from './pages/CommodityPage'
import CommoditiesPage from './pages/CommoditiesPage'
import DashboardPage from './pages/DashboardPage'
import HomeNavPage from './pages/HomeNavPage'
import LoginPage from './pages/LoginPage'
import TagPage from './pages/TagPage'
import NotFoundPage from './pages/404'

export default new Router({
  routes: [
    { path: '/', name: 'index', component: LoginPage},
    { path: '/article/:id', name: 'article', component: ArticlePage},
    { path: '/articles', name: 'articles', component: ArticlesPage},
    { path: '/login', name: 'login', component: LoginPage},
    { path: '/dashboard', name: 'dashboard', component: DashboardPage},
    { path: '/carousel', name: 'carousel', component: CarouselPage},
    { path: '/commodity/:id', name: 'commodity', component: CommodityPage},
    { path: '/commodities', name: 'commodities', component: CommoditiesPage},
    { path: '/home-nav', name: 'homeNave', component: HomeNavPage},
    { path: '/tags', name: 'tags', component: TagPage},
    { path: "/*", name: '404', component: NotFoundPage}
  ]
})