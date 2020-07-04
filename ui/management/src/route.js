import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

import AccountPage from './pages/AccountPage'
import AccountsPage from './pages/AccountsPage'
import ArticlePage from './pages/ArticlePage'
import ArticlesPage from './pages/ArticlesPage'
import CarouselPage from './pages/CarouselPage'
import CommodityPage from './pages/CommodityPage'
import CommoditiesPage from './pages/CommoditiesPage'
import DashboardPage from './pages/DashboardPage'
import HomeNavPage from './pages/HomeNavPage'
import LoginPage from './pages/LoginPage'
import PayCardPage from './pages/PayCardPage'
import PayCardPreviewPage from './pages/PayCardPreviewPage'
import PayCardsPage from './pages/PayCardsPage'
import TagPage from './pages/TagPage'
import NotFoundPage from './pages/404'

export default new Router({
  routes: [
    { path: '/', name: 'index', component: LoginPage},
    { path: '/account/:id', name: 'account', component: AccountPage},
    { path: '/accounts', name: 'accounts', component: AccountsPage},
    { path: '/article/:id', name: 'article', component: ArticlePage},
    { path: '/articles', name: 'articles', component: ArticlesPage},
    { path: '/login', name: 'login', component: LoginPage},
    { path: '/dashboard', name: 'dashboard', component: DashboardPage},
    { path: '/carousel', name: 'carousel', component: CarouselPage},
    { path: '/commodity/:id', name: 'commodity', component: CommodityPage},
    { path: '/commodities', name: 'commodities', component: CommoditiesPage},
    { path: '/home-nav', name: 'homeNave', component: HomeNavPage},
    { path: '/card', name: 'card', component: PayCardPage},
    { path: '/card-preview/:id', name: 'cardPreview', component: PayCardPreviewPage},
    { path: '/cards', name: 'cards', component: PayCardsPage},
    { path: '/tags', name: 'tags', component: TagPage},
    { path: "/*", name: '404', component: NotFoundPage}
  ]
})