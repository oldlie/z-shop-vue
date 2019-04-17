import Vue from 'vue'
import Antd from 'ant-design-vue'
import App from './App.vue'
import VueRouter from 'vue-router'
import 'ant-design-vue/dist/antd.css'

import ArticleList from './components/ArticleList'
import ProductCard from './components/ProductCard'
import ProductComment from './components/ProductComment'

Vue.component('article-list', ArticleList);
Vue.component('product-card', ProductCard);
Vue.component('product-comment', ProductComment);

Vue.config.productionTip = false

import ArticlePage from './pages/ArticlePage'
import ArticlesPage from './pages/ArticlesPage';
import HomePage from './pages/HomePage'
import ProductPage from './pages/ProductPage'
import ProductsPage from './pages/ProductsPage'

const routes = [
  { path: '/', component: HomePage },
  { path: '/article/:id', component: ArticlePage},
  { path: '/articles', component: ArticlesPage },
  { path: '/home', component: HomePage },
  { path: '/product/:id', component: ProductPage},
  { path: '/products', component: ProductsPage },
]

const router = new VueRouter({
  routes // (缩写) 相当于 routes: routes
})

Vue.use(Antd)
Vue.use(VueRouter)

new Vue({
  router,
  render: h => h(App),
}).$mount('#app')
